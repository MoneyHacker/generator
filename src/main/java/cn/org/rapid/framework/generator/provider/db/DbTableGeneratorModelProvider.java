package cn.org.rapid.framework.generator.provider.db;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import cn.org.rapid.framework.generator.IGeneratorModelProvider;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.BeanUtils;

import cn.org.rapid.framework.generator.provider.db.model.Table;


@Log4j2
public class DbTableGeneratorModelProvider implements IGeneratorModelProvider {
    Table table;

    public DbTableGeneratorModelProvider(Table table) {
        super();
        String name = table.getSqlName();
        this.table = table;
    }
    @Override
    public String getDisaplyText() {
        return table.toString();
    }

    @Override
    public void mergeFilePathModel(Map model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        model.putAll(BeanUtils.describe(table));
    }

    @Override
    public void mergeTemplateModel(Map model) {
        model.put("table", table);
    }

}
