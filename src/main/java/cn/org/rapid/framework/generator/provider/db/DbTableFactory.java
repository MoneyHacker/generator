package cn.org.rapid.framework.generator.provider.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.org.rapid.framework.generator.GeneratorProperties;
import cn.org.rapid.framework.generator.provider.db.model.Column;
import cn.org.rapid.framework.generator.provider.db.model.Table;
import lombok.extern.log4j.Log4j2;
/**
 * 
 */
@Log4j2
public class DbTableFactory {
	/**
	 * Logger for this class
	 */
	private Connection connection;
	private static DbTableFactory instance = new DbTableFactory();
	
	private DbTableFactory() {
		init();
	}
	private void init() {
		final String driver = GeneratorProperties.getRequiredProperty("jdbc.driver");
		try {
			Class.forName(driver);
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException("not found jdbc driver class:["+driver+"]",e);
		}
	}
	
	public static DbTableFactory getInstance() {
		return instance;
	}
	
	public String getCatalog() {
		return GeneratorProperties.getNullIfBlank("jdbc.catalog");
	}

	public String getSchema() {
		return GeneratorProperties.getNullIfBlank("jdbc.schema");
	}

	public Connection getConnection() throws SQLException {
		if(connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection(GeneratorProperties.getRequiredProperty("jdbc.url"),GeneratorProperties.getRequiredProperty("jdbc.username"),GeneratorProperties.getProperty("jdbc.password"));
		}
		return connection;
	}

	public List getAllTables() throws Exception {
		final Connection conn = getConnection();
		return getAllTables(conn);
	}
	
	public Table getTable(final String sqlTableName) throws Exception {
		final Connection conn = getConnection();
		final DatabaseMetaData dbMetaData = conn.getMetaData();
		final ResultSet rs = dbMetaData.getTables(getCatalog(), getSchema(), sqlTableName, null);
		while(rs.next()) {
			final Table table = createTable(conn, rs);
			return table;
		}
		throw new RuntimeException("not found table with give name:"+sqlTableName);
	}

	private Table createTable(final Connection conn, final ResultSet rs) throws SQLException {
		String realTableName = null;
		try {
			realTableName = rs.getString("TABLE_NAME");
			final String tableType = rs.getString("TABLE_TYPE");
			String remarks = rs.getString("REMARKS");
			if(remarks == null && isOracleDataBase()) {
				remarks = getOracleTableComments(realTableName);
			}
			
			final Table table = new Table();
			table.setTableName(realTableName);
			table.setRemarks(remarks);
			if ("SYNONYM".equals(tableType) && isOracleDataBase()) {
			    table.setOwnerSynonymName(getSynonymOwner(realTableName));
			}
			retriveTableColumns(table);
			DatabaseMetaData dmd = conn.getMetaData();
			System.out.println("conn="+conn.isClosed());
			table.initExportedKeys(dmd);
			table.initImportedKeys(dmd);
			return table;
		}catch(final SQLException e) {
			throw new RuntimeException("create table object error,tableName:"+realTableName,e);
		}
	}
	
	private List getAllTables(final Connection conn) throws SQLException {
		final DatabaseMetaData dbMetaData = conn.getMetaData();
		final ResultSet rs = dbMetaData.getTables(getCatalog(), getSchema(), null, null);
		final List<Table> tables = new ArrayList<>();
		while(rs.next()) {
			final Table table = createTable(conn, rs);
			tables.add(table);
		}
		return tables;
	}

	private boolean isOracleDataBase() {
		boolean ret = false;
		try {
			ret = (getMetaData().getDatabaseProductName().toLowerCase()
					.indexOf("oracle") != -1);
		} catch (final Exception ignore) {
		}
		return ret;
	}
	   
   private String getSynonymOwner(final String synonymName)  {
	      PreparedStatement ps = null;
	      ResultSet rs = null;
	      String ret = null;
	      try {
	         ps = getConnection().prepareStatement("select table_owner from sys.all_synonyms where table_name=? and owner=?");
	         ps.setString(1, synonymName);
	         ps.setString(2, getSchema());
	         rs = ps.executeQuery();
	         if (rs.next()) {
	            ret = rs.getString(1);
	         }
	         else {
	            final String databaseStructure = getDatabaseStructureInfo();
	            throw new RuntimeException("Wow! Synonym " + synonymName + " not found. How can it happen? " + databaseStructure);
	         }
	      } catch (final SQLException e) {
	         final String databaseStructure = getDatabaseStructureInfo();
	         log.error(e.getMessage(), e);
	         throw new RuntimeException("Exception in getting synonym owner " + databaseStructure);
	      } finally {
	         if (rs != null) {
	            try {
	               rs.close();
	            } catch (final Exception e) {
	            }
	         }
	         if (ps != null) {
	            try {
	               ps.close();
	            } catch (final Exception e) {
	            }
	         }
	      }
	      return ret;
	   }
   
   private String getDatabaseStructureInfo() {
	      ResultSet schemaRs = null;
	      ResultSet catalogRs = null;
	      final String nl = System.getProperty("line.separator");
	      final StringBuffer sb = new StringBuffer(nl);
	      // Let's give the user some feedback. The exception
	      // is probably related to incorrect schema configuration.
	      sb.append("Configured schema:").append(getSchema()).append(nl);
	      sb.append("Configured catalog:").append(getCatalog()).append(nl);

	      try {
	         schemaRs = getMetaData().getSchemas();
	         sb.append("Available schemas:").append(nl);
	         while (schemaRs.next()) {
	            sb.append("  ").append(schemaRs.getString("TABLE_SCHEM")).append(nl);
	         }
	      } catch (final SQLException e2) {
	         log.warn("Couldn't get schemas", e2);
	         sb.append("  ?? Couldn't get schemas ??").append(nl);
	      } finally {
	         try {
	            schemaRs.close();
	         } catch (final Exception ignore) {
	         }
	      }

	      try {
	         catalogRs = getMetaData().getCatalogs();
	         sb.append("Available catalogs:").append(nl);
	         while (catalogRs.next()) {
	            sb.append("  ").append(catalogRs.getString("TABLE_CAT")).append(nl);
	         }
	      } catch (final SQLException e2) {
	         log.warn("Couldn't get catalogs", e2);
	         sb.append("  ?? Couldn't get catalogs ??").append(nl);
	      } finally {
	         try {
	            catalogRs.close();
	         } catch (final Exception ignore) {
	         }
	      }
	      return sb.toString();
    }
	   
	private DatabaseMetaData getMetaData() throws SQLException {
		return getConnection().getMetaData();
	}
	
	private void retriveTableColumns(final Table table) throws SQLException {
	      log.debug("-------setColumns(" + table.getSqlName() + ")");
	      final List primaryKeys = getTablePrimaryKeys(table);
	      table.setPrimaryKeyColumns(primaryKeys);
	      // get the indices and unique columns
	      final List indices = new LinkedList();
	      // maps index names to a list of columns in the index
	      final Map uniqueIndices = new HashMap();
	      // maps column names to the index name.
	      final Map uniqueColumns = new HashMap();
	      ResultSet indexRs = null;
	      try {
	         if (table.getOwnerSynonymName() != null) {
	            indexRs = getMetaData().getIndexInfo(getCatalog(), table.getOwnerSynonymName(), table.getSqlName(), false, true);
	         } else {
	            indexRs = getMetaData().getIndexInfo(getCatalog(), getSchema(), table.getSqlName(), false, true);
	         }
	         while (indexRs.next()) {
	            final String columnName = indexRs.getString("COLUMN_NAME");
	            if (columnName != null) {
	               log.debug("index:" + columnName);
	               indices.add(columnName);
	            }

	            // now look for unique columns
	            final String indexName = indexRs.getString("INDEX_NAME");
	            final boolean nonUnique = indexRs.getBoolean("NON_UNIQUE");

	            if (!nonUnique && columnName != null && indexName != null) {
	               List l = (List)uniqueColumns.get(indexName);
	               if (l == null) {
	                  l = new ArrayList();
	                  uniqueColumns.put(indexName, l);
	               }
	               l.add(columnName);
	               uniqueIndices.put(columnName, indexName);
	               log.debug("unique:" + columnName + " (" + indexName + ")");
	            }
	         }
	      } catch (final Throwable t) {
	         // Bug #604761 Oracle getIndexInfo() needs major grants
	         // http://sourceforge.net/tracker/index.php?func=detail&aid=604761&group_id=36044&atid=415990
	      } finally {
	         if (indexRs != null) {
	            indexRs.close();
	         }
	      }

	      final List columns = getTableColumns(table, primaryKeys, indices, uniqueIndices, uniqueColumns);

	      for (final Iterator i = columns.iterator(); i.hasNext(); ) {
	         final Column column = (Column)i.next();
	         table.addColumn(column);
	      }

	      // In case none of the columns were primary keys, issue a warning.
	      if (primaryKeys.size() == 0) {
	         log.warn("WARNING: The JDBC driver didn't report any primary key columns in " + table.getSqlName());
	      }
	}

	private List getTableColumns(final Table table, final List primaryKeys, final List indices, final Map uniqueIndices, final Map uniqueColumns) throws SQLException {
		// get the columns
	      final List columns = new LinkedList();
	      final ResultSet columnRs = getColumnsResultSet(table);
	      
	      while (columnRs.next()) {
	         final int sqlType = columnRs.getInt("DATA_TYPE");
	          String sqlTypeName = columnRs.getString("TYPE_NAME");
	         //
	         final String columnName = columnRs.getString("COLUMN_NAME");
	         final String columnDefaultValue = columnRs.getString("COLUMN_DEF");
	         
	         String remarks = columnRs.getString("REMARKS");
	         if(remarks == null && isOracleDataBase()) {
	        	 remarks = getOracleColumnComments(table.getSqlName(), columnName);
	         }
	         
	         // if columnNoNulls or columnNullableUnknown assume "not nullable"
	         final boolean isNullable = (DatabaseMetaData.columnNullable == columnRs.getInt("NULLABLE"));
	         final int size = columnRs.getInt("COLUMN_SIZE");
	         final int decimalDigits = columnRs.getInt("DECIMAL_DIGITS");

	         final boolean isPk = primaryKeys.contains(columnName);
	         final boolean isIndexed = indices.contains(columnName);
	         final String uniqueIndex = (String)uniqueIndices.get(columnName);
	         List columnsInUniqueIndex = null;
	         if (uniqueIndex != null) {
	            columnsInUniqueIndex = (List)uniqueColumns.get(uniqueIndex);
	         }
	         
	         

	         final boolean isUnique = columnsInUniqueIndex != null && columnsInUniqueIndex.size() == 1;
	         if (isUnique) {
	            log.debug("unique column:" + columnName);
	         }
	         final Column column = new Column(
	               table,
	               sqlType,
	               sqlTypeName,
	               columnName,
	               size,
	               decimalDigits,
	               isPk,
	               isNullable,
	               isIndexed,
	               isUnique,
	               columnDefaultValue,
	               remarks);
	         columns.add(column);
	      }
	      columnRs.close();
		return columns;
	}

	private ResultSet getColumnsResultSet(final Table table) throws SQLException {
		ResultSet columnRs = null;
	      if (table.getOwnerSynonymName() != null) {
	         columnRs = getMetaData().getColumns(getCatalog(), table.getOwnerSynonymName(), table.getSqlName(), null);
	      }
	      else {
	         columnRs = getMetaData().getColumns(getCatalog(), getSchema(), table.getSqlName(), null);
	      }
		return columnRs;
	}

	private List getTablePrimaryKeys(final Table table) throws SQLException {
		// get the primary keys
	      final List primaryKeys = new LinkedList();
	      ResultSet primaryKeyRs = null;
	      if (table.getOwnerSynonymName() != null) {
	         primaryKeyRs = getMetaData().getPrimaryKeys(getCatalog(), table.getOwnerSynonymName(), table.getSqlName());
	      }
	      else {
	         primaryKeyRs = getMetaData().getPrimaryKeys(getCatalog(), getSchema(), table.getSqlName());
	      }
	      while (primaryKeyRs.next()) {
	    	  
	    	  
	    	  
	         final String columnName = primaryKeyRs.getString("COLUMN_NAME");
	         log.debug("primary key:" + columnName);
	         primaryKeys.add(columnName);
	      }
	      primaryKeyRs.close();
		return primaryKeys;
	}

	private String getOracleTableComments(final String table)  {
		final String sql = "SELECT comments FROM user_tab_comments WHERE table_name='"+table+"'";
		return queryForString(sql);
	}

	private String queryForString(final String sql) {
		Statement s = null;
		ResultSet rs = null;
		try {
			s =  getConnection().createStatement();
			rs = s.executeQuery(sql);
			if(rs.next()) {
				return rs.getString(1);
			}
			return null;
		}catch(final SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			try {
				if(s != null) {
					s.close();
				}
				if(rs != null) {
					rs.close();
				}
			} catch (final SQLException e) {
			}
		}
	}
	
	private String getOracleColumnComments(final String table,final String column)  {
		final String sql = "SELECT comments FROM user_col_comments WHERE table_name='"+table+"' AND column_name = '"+column+"'";
		return queryForString(sql);
	}
	
}
