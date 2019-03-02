/*
 * Created on Jan 1, 2005
 *
 */
package cn.org.rapid.framework.generator.provider.db.model;


import java.util.List;

import cn.org.rapid.framework.generator.provider.db.DbTableFactory;
import cn.org.rapid.framework.generator.util.ListHashTable;

/**
 * @author chris
 */
public class ForeignKey {
    protected String relationShip = null;
    protected String firstRelation = null;
    protected String secondRelation = null;
    protected Table parentTable;
    protected String tableName;
    protected ListHashTable columns;
    protected ListHashTable parentColumns;

    public ForeignKey(final Table aTable, final String tblName) {
        super();
        parentTable = aTable;
        tableName = tblName;
        columns = new ListHashTable();
        parentColumns = new ListHashTable();
    }

    /**
     * @return Returns the tableName.
     */
    public String getTableName() {
        return tableName;
    }

    public String getParentTableName() {
        return parentTable.getSqlName();
    }

    /**
     * @param col
     * @param seq
     */
    public void addColumn(final String col, final String parentCol, final Integer seq) {
        columns.put(seq, col);
        parentColumns.put(seq, parentCol);
    }

    public String getColumn(final String parentCol) {
        // return the associated column given the parent column
        final Object key = parentColumns.getKeyForValue(parentCol);
        final String col = (String) columns.get(key);
        return col;
    }

    public ListHashTable getColumns() {
        return columns;
    }

    /**
     *
     */
    private void initRelationship() {
        firstRelation = "";
        secondRelation = "";
        Table foreignTable = null;
        try {
            foreignTable = DbTableFactory.getInstance().getTable(tableName);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        final List parentPrimaryKeys = parentTable.getPrimaryKeyColumns();
        final List foreignPrimaryKeys = foreignTable.getPrimaryKeyColumns();

        if (hasAllPrimaryKeys(parentPrimaryKeys, parentColumns)) {
            firstRelation = "one";
        } else {
            firstRelation = "many";
        }

        if (hasAllPrimaryKeys(foreignPrimaryKeys, columns)) {
            secondRelation = "one";
        } else {
            secondRelation = "many";
        }

        relationShip = firstRelation + "-to-" + secondRelation;

    }

    private boolean hasAllPrimaryKeys(final List pkeys, final ListHashTable cols) {
        final boolean hasAll = true;
        // if size is not equal then false
        final int numKeys = pkeys.size();
        if (numKeys != cols.size()) {
            return false;
        }

        for (int i = 0; i < numKeys; i++) {
            final Column col = (Column) pkeys.get(i);
            final String colname = col.getColumnName();
            if (!cols.contains(colname)) {
                return false;
            }
        }

        return hasAll;
    }

    public boolean isParentColumnsFromPrimaryKey() {
        boolean isFrom = true;
        final int numKeys = getParentColumns().size();
        for (int i = 0; i < numKeys; i++) {
            final String pcol = (String) getParentColumns().getOrderedValue(i);
            if (!primaryKeyHasColumn(pcol)) {
                isFrom = false;
                break;
            }
        }
        return isFrom;
    }

    private boolean primaryKeyHasColumn(final String aColumn) {
        boolean isFound = false;
        final int numKeys = parentTable.getPrimaryKeyColumns().size();
        for (int i = 0; i < numKeys; i++) {
            final Column sqlCol = (Column) parentTable.getPrimaryKeyColumns().get(i);
            final String colname = sqlCol.getColumnName();
            if (colname.equals(aColumn)) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    public boolean getHasImportedKeyColumn(final String aColumn) {
        boolean isFound = false;
        final List cols = getColumns().getOrderedValues();
        final int numCols = cols.size();
        for (int i = 0; i < numCols; i++) {
            final String col = (String) cols.get(i);
            if (col.equals(aColumn)) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }

    /**
     * @return Returns the firstRelation.
     */
    public String getFirstRelation() {
        if (firstRelation == null) {
            initRelationship();
        }
        return firstRelation;
    }

    public Table getSqlTable() {
        Table table = null;
        try {
            table = DbTableFactory.getInstance().getTable(tableName);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    /**
     * @return Returns the parentTable.
     */
    public Table getParentTable() {
        return parentTable;
    }

    /**
     * @return Returns the relationShip.
     */
    public String getRelationShip() {
        if (relationShip == null) {
            initRelationship();
        }
        return relationShip;
    }

    /**
     * @return Returns the secondRelation.
     */
    public String getSecondRelation() {
        if (secondRelation == null) {
            initRelationship();
        }
        return secondRelation;
    }

    /**
     * @return Returns the parentColumns.
     */
    public ListHashTable getParentColumns() {
        return parentColumns;
    }

    public boolean getHasImportedKeyParentColumn(final String aColumn) {

        boolean isFound = false;
        final List cols = getParentColumns().getOrderedValues();
        final int numCols = cols.size();
        for (int i = 0; i < numCols; i++) {
            final String col = (String) cols.get(i);
            if (col.equals(aColumn)) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }
}
