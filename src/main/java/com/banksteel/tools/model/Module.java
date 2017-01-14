package com.banksteel.tools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;

/**
 * Module
 * @author bin.yin 2015.08.14
 */
public class Module implements Serializable {
	private static final long serialVersionUID = -5478073753066950122L;

	/**  模块名  */
	private String name = null;
	private String tabName = null;
	/**  表名前缀  */
	private String tablePrefix = null;
	/**  表名集合  */
	private List<String> tableNames = null;
	/**  忽略表集合  */
	private List<String> ignoreTables = null;
	/**  忽略列集合  */
	private List<String> ignoreColumns = null;
	private String pkType = null;
	private Boolean logData = null;

	@Value("${package.prefix.before.module}")
	private String packagePrefixBeforeModule = null;

	@Value("${project.java.src.path}")
	private String projectJavaSrcPath = null;

	@Value("${project.config.path}")
	private String projectConfigPath = null;
	
	@Value("${project.html.path}")
	private String projectHTMLPath = null;
	
	/** 数据库查询完后的表集合 */
	private List<Table> tableList = new ArrayList<Table>();
	private List<Map<String, Object>> modelList = new ArrayList<Map<String, Object>>();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public String getTablePrefix() {
		return tablePrefix;
	}
	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}
	public List<String> getTableNames() {
		return tableNames;
	}
	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}
	public List<String> getIgnoreTables() {
		return ignoreTables;
	}
	public void setIgnoreTables(List<String> ignoreTables) {
		this.ignoreTables = ignoreTables;
	}
	public List<String> getIgnoreColumns() {
		return ignoreColumns;
	}
	public void setIgnoreColumns(List<String> ignoreColumns) {
		this.ignoreColumns = ignoreColumns;
	}
	public String getPkType() {
		return pkType;
	}
	public void setPkType(String pkType) {
		this.pkType = pkType;
	}
	public Boolean getLogData() {
		return logData;
	}
	public void setLogData(Boolean logData) {
		this.logData = logData;
	}
	public String getPackagePrefixBeforeModule() {
		return packagePrefixBeforeModule;
	}
	public void setPackagePrefixBeforeModule(String packagePrefixBeforeModule) {
		this.packagePrefixBeforeModule = packagePrefixBeforeModule;
	}
	public String getProjectJavaSrcPath() {
		return projectJavaSrcPath;
	}
	public void setProjectJavaSrcPath(String projectJavaSrcPath) {
		this.projectJavaSrcPath = projectJavaSrcPath;
	}
	public String getProjectConfigPath() {
		return projectConfigPath;
	}
	public void setProjectConfigPath(String projectConfigPath) {
		this.projectConfigPath = projectConfigPath;
	}
	public String getProjectHTMLPath() {
		return projectHTMLPath;
	}
	public void setProjectHTMLPath(String projectHTMLPath) {
		this.projectHTMLPath = projectHTMLPath;
	}
	public List<Table> getTableList() {
		return tableList;
	}
	public void setTableList(List<Table> tableList) {
		this.tableList = tableList;
	}
	public void addTable(Table table) {
		this.tableList.add(table);
	}
	public List<Map<String, Object>> getModelList() {
		return modelList;
	}
	public void setModelList(List<Map<String, Object>> modelList) {
		this.modelList = modelList;
	}
	public void addModel(Map<String, Object> model) {
		this.modelList.add(model);
	}

	public String toString() {
		try {
			return ToStringBuilder.reflectionToString(this);
		} catch (Exception e) {
			try {
				return BeanUtils.describe(this).toString();
			} catch (Exception e2) {
				return null;
			}
		}
	}

	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
