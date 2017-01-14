package com.banksteel.tools.model;

import java.io.Serializable;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Column
 * @author bin.yin 2015.08.14
 */
public final class Column implements Serializable {
	private static final long serialVersionUID = -8832328237569908358L;

	private String name = null;
	private String value = null;
	private String comment = null;

	private String jdbcType = null;
	private String javaType = null;

	private int precision = 0;
	private int scale = 0;

	private boolean isNullable = false;
	private boolean isPk = false;

	/** nullValue表达式 处理数据库类型为Number且可以为空情况 */
	private String nullValueExpression = "";

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	public String getJavaType() {
		return javaType;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public boolean isNullable() {
		return isNullable;
	}
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	public boolean isPk() {
		return isPk;
	}
	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}
	public String getNullValueExpression() {
		return nullValueExpression;
	}
	public void setNullValueExpression(String nullValueExpression) {
		this.nullValueExpression = nullValueExpression;
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
