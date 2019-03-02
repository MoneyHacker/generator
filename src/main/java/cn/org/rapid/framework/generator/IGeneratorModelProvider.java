package cn.org.rapid.framework.generator;
import java.util.Map;
public interface IGeneratorModelProvider {

	 String getDisaplyText();
	
	/**
	 * 得到文件路径可以引用的变量
	 *@param model
	 *@throws Exception
	 */
	 void mergeTemplateModel(Map model) throws Exception;
	
	/**
	 * 得到模板文件可以引用的变量
	 * @param model
	 * @throws Exception
	 */
	 void mergeFilePathModel(Map model) throws Exception;
	
}
