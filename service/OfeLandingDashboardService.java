package com.bh.realtrack.service;

import java.util.Map;

import com.bh.realtrack.dto.BillingDashboardFilterDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.LandingBubbleChartDTO;
import com.bh.realtrack.dto.LandingManageKPIDisplayData;

public interface OfeLandingDashboardService {

	
	Map<String, Object> getLandingManageProjects(HeaderDashboardDetailsDTO headerDetails);
	Map<String,Object> getLandingBillingCount(BillingDashboardFilterDTO filterValues) throws Exception;
	Map<String,Object> getLandingBubbleChart(LandingBubbleChartDTO chartValues);
	Map<String,Object> getLandingBubbleTrendChart(String projectId);
	Map<String,Object> getLandingManageKPIDropdowns(int companyId);
	Map<String,Object> saveLandingManageKPI(LandingManageKPIDisplayData saveManageKPI);
	Map<String,Object> getLandingManageKPI(int companyId);
	Map<String,Object> getLandingManageKPIDisplayData(int companyId, String KpiCode, String businessUnit);
	Map<String,Object> getKPITrendChart(String businessUnit, String kpiCategory);	
	Map<String,Object> getLandingProjectList(HeaderDashboardDetailsDTO projectDetails);
	byte[] downloadProjectDetails(String projectId,int companyId, String businessUnit, String segment, String region, int customerId);
	Map<String,Object> getLandingQualityKPI(BillingDashboardFilterDTO filterValues);

}
