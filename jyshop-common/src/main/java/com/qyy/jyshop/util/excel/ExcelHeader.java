package com.qyy.jyshop.util.excel;

public class ExcelHeader implements Comparable<ExcelHeader> {
	/**
	 * excel的标题名称
	 */
	private String title;
	/**
	 * 每一个标题的顺序
	 */
	private int order;

	private Boolean isCombine;

	private Boolean isCombineCondition;
	/**
	 * 说对应方法名称
	 */
	private String methodName;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Boolean getIsCombine() {
		return isCombine;
	}

	public void setIsCombine(Boolean isCombine) {
		this.isCombine = isCombine;
	}

	public Boolean getIsCombineCondition() {
		return isCombineCondition;
	}

	public void setIsCombineCondition(Boolean isCombineCondition) {
		this.isCombineCondition = isCombineCondition;
	}

	public int compareTo(ExcelHeader o) {
		return order > o.order ? 1 : (order < o.order ? -1 : 0);
	}

	public ExcelHeader(String title, int order, Boolean isCombine, Boolean isCombineCondition, String methodName) {
		super();
		this.title = title;
		this.order = order;
		this.isCombine = isCombine;
		this.isCombineCondition = isCombineCondition;
		this.methodName = methodName;
	}

	
}