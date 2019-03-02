package cn.org.rapid.framework.generator;
public class GeneratorMain {
    /**
     * 请直接修改以下代码调用不同的方法以执行相关生成任务.
     */
    public static void main(final String[] args) throws Exception {
        final GeneratorFacade g = new GeneratorFacade();
        // g.printAllTableNames(); //打印数据库中的表名称
        g.clean(); // 删除生成器的输出目录
        //g.generateByTable(tableName, className);
        //根据表生成实体类及相关的模版
        g.generateByTable("sys_config", "SysConfig");
//		g.generateByTable("package_item");
        // g.generateByTable("CATEGORY"); //t_path_filters,注意: oracle
        // 需要指定schema及注意表名的大小写.
        // g.generateByTable("DOC_SOURCE");
        // g.generateByTable("table_name","TableName"); //通过数据库表生成文件,并可以自定义类名
        // g.generateByAllTable(); //自动搜索数据库中的所有表并生成文件
        // g.generateByClass(Blog.class);
        // 打开文件夹
        Runtime.getRuntime().exec(
                "cmd.exe /c start "
                        + GeneratorProperties.getRequiredProperty("outRoot"));
    }
}
