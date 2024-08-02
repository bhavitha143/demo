package com.bh.realtrack.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IOfeProjectConsoleDao;
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
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IOFEProjectConsoleService;

@Service
public class OFEProjectConsoleServiceImpl implements IOFEProjectConsoleService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OFEProjectConsoleServiceImpl.class);

	
	public OFEProjectConsoleServiceImpl(CallerContext callerContext, IOfeProjectConsoleDao ofeProjectConsoleDao) {
		super();
		this.callerContext = callerContext;
		this.ofeProjectConsoleDao = ofeProjectConsoleDao;
	}

	CallerContext callerContext;
	IOfeProjectConsoleDao ofeProjectConsoleDao;

	@Override
	public Map<String, Object> getProjectDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String sso = callerContext.getName();
		LOGGER.info("SSO {}",sso);
		String user = ofeProjectConsoleDao.getUserSsso(sso);
		ProjectConsoleDTo projectList = new ProjectConsoleDTo();
		projectList= ofeProjectConsoleDao.getProjectList(projectId,user);
		
		responseMap.put("projectList", projectList);
		
		return responseMap;
	}
	
	@Override
	public Map<String, Object> getProjectBillingDetails(String projectId) {
	Map<String, Object> responseMap = new HashMap<String, Object>();
	List<PrjConsoleNextToBillPopup> nextToBillPopupList = new ArrayList<PrjConsoleNextToBillPopup>();
	List<PrjConsoleBillingChartDTO> chartResponse = new ArrayList<PrjConsoleBillingChartDTO>();
	
	chartResponse = ofeProjectConsoleDao.getPrjConsoleBillingChart(projectId);
	String foreCastDate = ofeProjectConsoleDao.getForecastDate(projectId);
	String nextToBill = ofeProjectConsoleDao.getNextToBillValue(projectId,foreCastDate);
	nextToBillPopupList = ofeProjectConsoleDao.getNextToBillPopup(projectId,foreCastDate);
	responseMap.put("billingChart", chartResponse);
	responseMap.put("nextToBillValue", nextToBill);
	responseMap.put("nextToBillPopupList", nextToBillPopupList);

	return responseMap;
	}

	@Override
	public Map<String, Object> getProjectRiskDetails(String projectId) {
	Map<String, Object> responseMap = new HashMap<String, Object>();
	Map<String, Object> riskKpi = new HashMap<String, Object>();
	String postMitigationEmvOpenRisk = ofeProjectConsoleDao.postMitigationEmvOpenrisk(projectId);
	String postMitigationEmvOpenRiskPerValue = ofeProjectConsoleDao.postMitigationEmvOpenriskPerValue(projectId);
	String openRiskOverDue = ofeProjectConsoleDao.openRiskOverDue(projectId);

	riskKpi.put("postMitigationEmvOpenrisk", postMitigationEmvOpenRisk);
	riskKpi.put("postMitigationEmvOpenriskPerValue", postMitigationEmvOpenRiskPerValue);
	riskKpi.put("openRiskOverDue", openRiskOverDue);
		
	responseMap.put("riskKpiDetails", riskKpi);

	return responseMap;
	}

	@Override
	public Map<String, Object> getProjectDocumentCount(String projectId) {
	Map<String, Object> responseMap = new HashMap<String, Object>();
	Map<String, Object> documentKpi = new HashMap<String, Object>();
	Map<String,Object> documentCount = new HashMap<>();

	String sso = callerContext.getName();
	LOGGER.info(sso);

	PrjConsoleDocumentationDto docCount = ofeProjectConsoleDao.getDocumentationCount(projectId,sso);
	String monOtd = ofeProjectConsoleDao.getMonOtdValue(projectId);
	String rework = ofeProjectConsoleDao.getDocReworkValue(projectId);
	String review = ofeProjectConsoleDao.getDocReviewValue(projectId);

	if(docCount.getTotalCount().equals("0") && docCount.getFinishedCount().equals("0") && docCount.getPendingCount().equals("0")){
		responseMap.put("documentCount",documentCount);
	}
	else{
		responseMap.put("documentCount", docCount);
	}
	
	if((monOtd.equals("") || monOtd.equals("NA")) && (rework.equals("") || rework.equals("NA")) && (review.equals("") || review.equals("NA"))){
		responseMap.put("documentationKpi", documentKpi);
	}
	else {
			documentKpi.put("12monotd", monOtd);
			documentKpi.put("rework", rework);
			documentKpi.put("review", review);
			responseMap.put("documentationKpi", documentKpi);
	}
	return responseMap;
	}
	
	@Override
	public Map<String, Object> getProjectDocumentList(String projectId,String status) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
	
		List<PrjConsoleDocumentationList> docList = new ArrayList<PrjConsoleDocumentationList>();
		
		docList = ofeProjectConsoleDao.getDocPopupList(projectId,status);
		
		responseMap.put("docPopupList", docList);
		return  responseMap;
	}
	
	@Override
	public Map<String, Object> getPrjConsoleSCurveDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String dataDate = ofeProjectConsoleDao.getSCurveDataDate(projectId);
		List<PrjConsoleScruveDTO> sCurveList = new ArrayList<PrjConsoleScruveDTO>();
		List<PrjConsoleScurveColorDTO> colorList = new ArrayList<PrjConsoleScurveColorDTO>();
		
		sCurveList = ofeProjectConsoleDao.getScruveList(projectId,dataDate);
		String progressDate = ofeProjectConsoleDao.getSCurveProgressDate(projectId);
		colorList = ofeProjectConsoleDao.getSCurveColorList(projectId,dataDate,progressDate);
		responseMap.put("dataDate", dataDate);
		responseMap.put("sCurveData", sCurveList);
		responseMap.put("sCurveFuncColor",colorList);
		return  responseMap;
	}

	@Override
	public Map<String, Object> getPrjConsoleFinCashDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		PrjConsoleFinanceDTO financeData = new PrjConsoleFinanceDTO();
		financeData = ofeProjectConsoleDao.getFinanceData(projectId);
		
		responseMap.put("financeData", financeData);
		return responseMap;
	}

	@Override
	public Map<String, Object> getPrjConsoleMrpDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<PrjConsoleMrpDTO> projConsoleMrp= new ArrayList<PrjConsoleMrpDTO>();
		projConsoleMrp = ofeProjectConsoleDao.getPrjConsoleMrpDetails(projectId);
		String activeMrp = ofeProjectConsoleDao.getPrjConsoleActiveMrp(projectId);
		String overDuePercentage = ofeProjectConsoleDao.getPrjConsoleOverDuePec(projectId);
		responseMap.put("mrpResponse", projConsoleMrp);
		responseMap.put("activeMrp",activeMrp);
		responseMap.put("overDuePerc",overDuePercentage);
		return responseMap;
	}
	
	@Override
	public Map<String, Object> getPrjConsoleQualityDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		PrjConsoleQualityKPIDTO qualityKPI= new PrjConsoleQualityKPIDTO();
		List<PrjConsoleAgingDetailsDTO> agingDetails = new ArrayList<PrjConsoleAgingDetailsDTO>();
		qualityKPI = ofeProjectConsoleDao.getPrjConsoleQualityDetails(projectId);
		String sso = callerContext.getName();
		String ncrOverdue = ofeProjectConsoleDao.getProjectConsoleNcr(projectId);
		
		String role = ofeProjectConsoleDao.getUserRole(sso);
		agingDetails = ofeProjectConsoleDao.getPrjConsoleAgingDetails(projectId,role);
		
		responseMap.put("qualityKPI", qualityKPI);
		responseMap.put("AgingDetails", agingDetails);
		responseMap.put("ncrOverDue", ncrOverdue);
		return responseMap;
	}

	@Override
	public Map<String, Object> getPrjConsoleVorDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<PrjConsoleVorKpiDTO> consoleVor = ofeProjectConsoleDao.getApprovedVor(projectId);
		responseMap.put("VOR",consoleVor );
		return responseMap;
	}
}