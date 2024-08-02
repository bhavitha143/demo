package com.bh.realtrack.dto;

public class BillingDashboardFilterDTO {

	private String startDate;
	private String endDate;
	private String projectList;
	private String billingDateFlag;
	private String currentYearFlag;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getProjectList() {
		return projectList;
	}
	public void setProjectList(String projectList) {
		this.projectList = projectList;
	}
	public String getBillingDateFlag() {
		return billingDateFlag;
	}
	public void setBillingDateFlag(String billingDateFlag) {
		this.billingDateFlag = billingDateFlag;
	}
	public String getCurrentYearFlag() {
		return currentYearFlag;
	}
	public void setCurrentYearFlag(String currentYearFlag) {
		this.currentYearFlag = currentYearFlag;
	}

	
}
