package cn.org.rapid.framework.generator.util;

import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
public class PropertiesHelper {
	Properties p;

	public PropertiesHelper(final Properties p) {
		this.p = p;
	}
	
	public Properties getProperties() {
		return p;
	}
	
	public String getProperty(final String key, final String defaultValue) {
		return getProperties().getProperty(key, defaultValue);
	}
	
	public String getProperty(final String key) {
		return getProperties().getProperty(key);
	}
	
	public String getRequiredProperty(final String key) {
		String value = getProperty(key);
		if (value == null || "".equals(value.trim())) {
			throw new IllegalStateException("required property is blank by key=" + key);
		}
		return value;
	}
	
	public Integer getInt(final String key) {
		if (getProperty(key) == null) {
			return null;
		}
		return Integer.parseInt(getRequiredProperty(key));
	}
	
	public int getInt(final String key, final int defaultValue) {
		if (getProperty(key) == null) {
			return defaultValue;
		}
		return Integer.parseInt(getRequiredProperty(key));
	}
	
	public int getRequiredInt(final String key) {
		return Integer.parseInt(getRequiredProperty(key));
	}
	
	public Boolean getBoolean(final String key) {
		if (getProperty(key) == null) {
			return Boolean.FALSE;
		}
		return Boolean.parseBoolean(getRequiredProperty(key));
	}
	
	public boolean getBoolean(final String key, final boolean defaultValue) {
		if (getProperty(key) == null) {
			return defaultValue;
		}
		return Boolean.parseBoolean(getRequiredProperty(key));
	}
	
	public boolean getRequiredBoolean(final String key) {
		return Boolean.parseBoolean(getRequiredProperty(key));
	}
	
	public String getNullIfBlank(final String key) {
		String value = getProperties().getProperty(key);
		if (value == null || "".equals(value.trim())) {
			return null;
		}
		return value;
	}
	
	public PropertiesHelper setProperty(final String key, final String value) {
		p.setProperty(key, value);
		return this;
	}

	public void clear() {
		p.clear();
	}

	public Set<Entry<Object, Object>> entrySet() {
		return p.entrySet();
	}

	public Enumeration<?> propertyNames() {
		return p.propertyNames();
	}
	
	
	
}
