package com.bh.realtrack.dao;

import java.util.List;

import com.bh.realtrack.dto.PrjConsoleAgingDetailsDTO;
import com.bh.realtrack.dto.PrjConsoleBillingChartDTO;
import com.bh.realtrack.dto.PrjConsoleDocumentationDto;
import com.bh.realtrack.dto.PrjConsoleDocumentationList;
import com.bh.realtrack.dto.PrjConsoleFinanceDTO;
import com.bh.realtrack.dto.PrjConsoleMrpDTO;
import com.bh.realtrack.dto.PrjConsoleNextToBillPopup;
import com.bh.realtrack.dto.PrjConsoleQualityKPIDTO;
import com.bh.realtrack.dto.PrjConsoleScruveDTO;
import com.bh.realtrack.dto.PrjConsoleScurveColorDTO;
import com.bh.realtrack.dto.PrjConsoleVorKpiDTO;
import com.bh.realtrack.dto.ProjectConsoleDTo;


public interface IOfeProjectConsoleDao {
	
	String getUserSsso(String userId);
	
	ProjectConsoleDTo getProjectList(String projectId, String user);
	
	String postMitigationEmvOpenrisk(String projectId);
	
	String postMitigationEmvOpenriskPerValue(String projectId);
	
	String openRiskOverDue(String projectId);

	String getRefreshDate(String projectId);
	
	String getForecastDate(String projectId);
	
	String getNextToBillValue(String projectId,String forecastDate);
	
	List<PrjConsoleNextToBillPopup> getNextToBillPopup(String projectId,String forecastDate);
	
	List<PrjConsoleBillingChartDTO> getPrjConsoleBillingChart(String projectId);
	
	PrjConsoleDocumentationDto getDocumentationCount(String projectId,String sso);
	
	String getMonOtdValue(String projectId);

	String getDocReworkValue(String projectId);

	String getDocReviewValue(String projectId);
	
	List<PrjConsoleDocumentationList> getDocPopupList(String projectId,String status);

	String getSCurveDataDate(String projectId);
	
	List<PrjConsoleScruveDTO> getScruveList(String projectId, String dataDate);
	
	String getSCurveProgressDate(String projectId);
	
	List<PrjConsoleScurveColorDTO> getSCurveColorList(String projectId, String dataDate,String progressDate);

	PrjConsoleFinanceDTO getFinanceData(String projectId);

	List<PrjConsoleMrpDTO> getPrjConsoleMrpDetails(String projectId);

	PrjConsoleQualityKPIDTO getPrjConsoleQualityDetails(String projectId);

	String getUserRole(String sso);

	List<PrjConsoleAgingDetailsDTO> getPrjConsoleAgingDetails(String projectId,String role);

	List<PrjConsoleVorKpiDTO> getApprovedVor(String projectId);

	String getProjectConsoleNcr(String projectId);

    String getPrjConsoleActiveMrp(String projectId);

	String getPrjConsoleOverDuePec(String projectId);
}
