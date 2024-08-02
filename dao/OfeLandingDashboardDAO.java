package com.bh.realtrack.dao;

import java.util.List;

import com.bh.realtrack.dto.BillingDashboardFilterDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.LandingBillingKpiDTO;
import com.bh.realtrack.dto.LandingBubbleChartDTO;
import com.bh.realtrack.dto.LandingBubbleTrendChartDTO;
import com.bh.realtrack.dto.LandingKPITrendChartDTO;
import com.bh.realtrack.dto.LandingManageKPI;
import com.bh.realtrack.dto.LandingManageKPIDisplayData;
import com.bh.realtrack.dto.LandingProjectDetailsDTO;
import com.bh.realtrack.dto.ManageProjectResponseDTO;

public interface OfeLandingDashboardDAO {

	List<ManageProjectResponseDTO> getmanageProjectList(HeaderDashboardDetailsDTO headerDetails, String projectId);

	String getOverdueKpi(BillingDashboardFilterDTO filterValues,String startDate);
	String getP90CycleTime(BillingDashboardFilterDTO filterValues, String endDate);
	List<LandingBillingKpiDTO> getCurrentActualOtrDifference(BillingDashboardFilterDTO filterValues,String startDate,String endDate);
	List<LandingBubbleChartDTO> getBubbleChartData(LandingBubbleChartDTO chartValues);
	List<LandingBubbleTrendChartDTO> getTrendingChartData(String projectId);
	List<String> getBusinessDropdown(int companyId);
	List<String> getKPICodeDropdown();
	List<LandingManageKPIDisplayData> getManageKPIData(int companyId,String kpiCode, String businessUnit);
	boolean saveLandingManageKPI(LandingManageKPIDisplayData manageKPI,String sso);
	int checkIfKPIAndBusinessExist(LandingManageKPIDisplayData manageKPI);
	List<LandingManageKPI> getManageKPI(int companyId);
	String getUpdatedOn();
	List<LandingKPITrendChartDTO> getKPITrendChartForOTD(String businessUnit);
	List<LandingKPITrendChartDTO> getKPITrendChartForBilling(String businessUnit);
	List<LandingKPITrendChartDTO> getKPITrendChartForRisk(String businessUnit);
	List<LandingKPITrendChartDTO> getKPITrendChartForOpportunities(String businessUnit);
	List<LandingKPITrendChartDTO> getKPITrendChartForDocumentation(String businessUnit);
	List<LandingKPITrendChartDTO> getKPITrendChartForChanges(String businessUnit);
	List<LandingKPITrendChartDTO> getKPITrendChartForQuality(String businessUnit);
	List<LandingProjectDetailsDTO> getLandingProjectDetailsExcel(String projectId,int companyId, String businessUnit, String segment, String region, int customerId);
	List<LandingProjectDetailsDTO> getLandingprojectDetails(HeaderDashboardDetailsDTO projectDetails);
	String qualityOverdueCIRKPI(BillingDashboardFilterDTO filterValues);
	String qualityOverdueNCRKPI(BillingDashboardFilterDTO filterValues);
	String qualityCoPQKPI(BillingDashboardFilterDTO filterValues);
}
