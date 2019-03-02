package cn.org.rapid.framework.generator.provider.java;

import java.util.Map;

import cn.org.rapid.framework.generator.IGeneratorModelProvider;
import org.apache.commons.beanutils.BeanUtils;

import cn.org.rapid.framework.generator.provider.java.model.JavaClass;

public class JavaClassGeneratorModelProvider implements IGeneratorModelProvider {
	JavaClass clazz;
	
	public JavaClassGeneratorModelProvider(JavaClass clazz) {
		super();
		this.clazz = clazz;
	}

	public String getDisaplyText() {
		return "JavaClass:"+clazz.getClassName();
	}

	public void mergeFilePathModel(Map model) throws Exception {
		model.putAll(BeanUtils.describe(clazz));
	}

	public void mergeTemplateModel(Map model) throws Exception {
		model.put("clazz",clazz);
	}

}
