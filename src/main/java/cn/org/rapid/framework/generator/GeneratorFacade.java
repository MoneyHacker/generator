package cn.org.rapid.framework.generator;
import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.org.rapid.framework.generator.provider.db.DbTableGeneratorModelProvider;
import cn.org.rapid.framework.generator.provider.db.model.Table;
import cn.org.rapid.framework.generator.provider.java.JavaClassGeneratorModelProvider;
import cn.org.rapid.framework.generator.provider.db.DbTableFactory;
import cn.org.rapid.framework.generator.provider.java.model.JavaClass;

public class GeneratorFacade {
	
	
	public GeneratorFacade() {
	}
	
	public void printAllTableNames() throws Exception {
		List tables = DbTableFactory.getInstance().getAllTables();
		System.out.println("\n----All TableNames BEGIN----");
		for(int i = 0; i < tables.size(); i++ ) {
			String sqlName = ((Table)tables.get(i)).getSqlName();
			System.out.println("g.generateTable(\""+sqlName+"\");");
		}
		System.out.println("----All TableNames END----");
	}
	
	public void generateByAllTable() throws Exception {
		List<Table> tables = DbTableFactory.getInstance().getAllTables();
		for(int i = 0; i < tables.size(); i++ ) {
			generateByTable(tables.get(i).getSqlName());
		}
	}
	
	public void generateByTable(String tableName) throws Exception {
		Generator g = createGeneratorForDbTable();
		Table table = DbTableFactory.getInstance().getTable(tableName);
		g.generateByModelProvider(new DbTableGeneratorModelProvider(table));
	}
	
	public void generateByTable(String tableName,String className) throws Exception {
		Generator g = createGeneratorForDbTable();
		Table table = DbTableFactory.getInstance().getTable(tableName);
		table.setClassName(className);
		g.generateByModelProvider(new DbTableGeneratorModelProvider(table));
	}
	
	public void generateByClass(Class clazz) throws Exception {
		Generator g = createGeneratorForJavaClass();
		g.generateByModelProvider(new JavaClassGeneratorModelProvider(new JavaClass(clazz)));
	}
	
	public void clean() throws IOException {
		Generator g = createGeneratorForDbTable();
		g.clean();//不清除目标目录
	}
	
	public Generator createGeneratorForDbTable() {
		Generator g = new Generator();
		File[] templateRootDirs = getTemplateRootDirs();
		g.setTemplateRootDirs(templateRootDirs);
		g.setOutRootDir(GeneratorProperties.getRequiredProperty("outRoot") + "/codegen-output");
		return g;
	}

	private File[] getTemplateRootDirs() {
		String[] dirs = GeneratorProperties.getRequiredProperty("templateRootDirs").split(",");
		File[] templateRootDirs = new File[dirs.length];
		for (int i = 0; i < dirs.length; i++) {
			templateRootDirs[i] = new File(dirs[i]).getAbsoluteFile();
		}
		return templateRootDirs;
	}
	
	private Generator createGeneratorForJavaClass() {
		throw new UnsupportedOperationException("暂不支持");
//		Generator g = new Generator();
//		g.setTemplateRootDir(new File(GeneratorProperties.getRequiredProperty("templateRootDir") + "/javaclass").getAbsoluteFile());
//		g.setOutRootDir(GeneratorProperties.getRequiredProperty("outRoot") + "/codegen-output");
//		return g;
	}
}
