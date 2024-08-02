package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.filter.Filter;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.OfeLandingDashboardDAO;
import com.bh.realtrack.dto.BillingDashboardFilterDTO;
import com.bh.realtrack.dto.CMTrendDetailsDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.LandingBillingKpiDTO;
import com.bh.realtrack.dto.LandingBubbleChartDTO;
import com.bh.realtrack.dto.LandingBubbleTrendChartDTO;
import com.bh.realtrack.dto.LandingKPITrendChartDTO;
import com.bh.realtrack.dto.LandingManageKPI;
import com.bh.realtrack.dto.LandingManageKPIDisplayData;
import com.bh.realtrack.dto.LandingProjectDetailsDTO;
import com.bh.realtrack.dto.ManageProjectResponseDTO;
import com.bh.realtrack.excel.ExportCMTrackerExcel;
import com.bh.realtrack.excel.ExportLandingProjectDetailsExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.ICommonService;
import com.bh.realtrack.service.OfeLandingDashboardService;
import com.bh.realtrack.util.OfeLandingDashboardConstants;

@Service
public class OfeLandingDashboardServiceImpl implements OfeLandingDashboardService{

	private static final Logger log = LoggerFactory.getLogger(OfeLandingDashboardServiceImpl.class);
	
	@Autowired
	OfeLandingDashboardDAO landingDashboardDAO;
	
	@Autowired
	ICommonService commonService;
	
	@Autowired
	private CallerContext callerContext;
	
	
	@Override
	public Map<String, Object> getLandingManageProjects(HeaderDashboardDetailsDTO headerDetails) {
		HashMap<String,Object> data = new HashMap<String,Object>();
		HashMap<String,Object> response = new HashMap<String,Object>();
		
		String projectId = commonService.fetchFavProjects();
		
		if(null==projectId) {
			projectId="0";
		}
			
		List<ManageProjectResponseDTO> manageProjectList = landingDashboardDAO.getmanageProjectList(headerDetails, projectId);
		data.put("manageProjectList", manageProjectList);
		
		response.put("data", data);
		return response;
	}


	@Override
	public Map<String, Object> getLandingBillingCount(BillingDashboardFilterDTO filterValues) throws Exception {
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> response = new HashMap<String,Object>();
		Map<String, String> currentYear =null;
			
		currentYear = getDate(filterValues.getStartDate(), filterValues.getEndDate());
		
		String startDate= currentYear.get("startDate");
		String endDate = currentYear.get("endDate");
		
		log.info("startDate :"+filterValues.getStartDate()+ " endDate :"+filterValues.getEndDate()+" startDate :"+startDate + " endDate :"+endDate);
		
		log.info("ProjectList::"+filterValues.getProjectList());
		
		
		String overdueKPI = landingDashboardDAO.getOverdueKpi(filterValues,startDate);
		String p90CycleTime = landingDashboardDAO.getP90CycleTime(filterValues,endDate);
		List<LandingBillingKpiDTO> currentActualOTRDiff = landingDashboardDAO.getCurrentActualOtrDifference(filterValues, startDate, endDate);
		
		data.put("overdueKpi", overdueKPI);
		data.put("p90CycleTime", p90CycleTime);
		data.put("currentActualOTRDiff", currentActualOTRDiff);
		
		response.put("data", data);
		return response;
	}
	
	public Map<String,String> getDate(String startDate, String endDate) throws Exception
	{
		Map<String,String> currentYear = new HashMap<String, String>();
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM/yyyy");
		Calendar c = Calendar.getInstance();
		
		Date date = null,date1 = null;
		
		if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {

			date = format2.parse(startDate);

			date1 = format2.parse(endDate);

			c.setTime(date1);

			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		else {
			throw new Exception("No Date selected: ");
		}
		
		currentYear.put("startDate", format1.format(date));
		currentYear.put("endDate", format1.format(c.getTime()));
		return currentYear;
	}


	@Override
	public Map<String, Object> getLandingBubbleChart(LandingBubbleChartDTO chartValues) {
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> response = new HashMap<String,Object>();
		
		List<LandingBubbleChartDTO> bubbleChartData = landingDashboardDAO.getBubbleChartData(chartValues);
		data.put("bubbleChartData", bubbleChartData);
		response.put("data", data);	
		return response;
	}


	@Override
	public Map<String, Object> getLandingBubbleTrendChart(String projectId) {
		Map<String,Object> responseMap = new HashMap<String,Object>();
		List<LandingBubbleTrendChartDTO> trendChartData = landingDashboardDAO.getTrendingChartData(projectId);
		responseMap.put("trendChartData", trendChartData);
		return responseMap;
	}


	@Override
	public Map<String, Object> getLandingManageKPIDropdowns(int companyId) {
		Map<String,Object> response = new HashMap<String,Object>();
		
		List<String> getKPICode = landingDashboardDAO.getKPICodeDropdown();
		List<String> getBusinessUnit = landingDashboardDAO.getBusinessDropdown(companyId); 
	
		
		response.put("getKPICode", getKPICode);
		response.put("getBusinessUnit", getBusinessUnit);
		return response;
	}


	@Override
	public Map<String, Object> saveLandingManageKPI(LandingManageKPIDisplayData manageKPI) {
		Map<String,Object> response = new HashMap<String,Object>();
		boolean resultFlag = false;
		String sso = callerContext.getName();
		log.info(sso);
		
		try {
			resultFlag = landingDashboardDAO.saveLandingManageKPI(manageKPI,sso);
			if(resultFlag) {
				response.put("status", "Success");
				response.put("message", "Data saved Successfully");	
			}
			else {
				response.put("status", "Error");
				response.put("message", "Error in saving data");
			}
		}catch(Exception e)
		{
			response.put("status", "Error");
			response.put("message", "Error in saving data");
		}		
		return response;
	}


	@Override
	public Map<String, Object> getLandingManageKPI(int companyId) {
		Map<String,Object> response = new HashMap<String,Object>();
		List<LandingManageKPI> manageKPI = landingDashboardDAO.getManageKPI(companyId);
		String updatedOn = landingDashboardDAO.getUpdatedOn();
		
		response.put("manageKPI", manageKPI);
		response.put("updatedOn", updatedOn);
		return response;
	}


	@Override
	public Map<String, Object> getLandingManageKPIDisplayData(int companyId, String kpiCode, String businessUnit) {
		
		Map<String,Object> response = new HashMap<String,Object>();
		List<LandingManageKPIDisplayData> getManageKPI = landingDashboardDAO.getManageKPIData(companyId,kpiCode,businessUnit);
		response.put("getManageKPI", getManageKPI);	
		return response;
	}


	@Override
	public Map<String, Object> getKPITrendChart(String businessUnit, String kpiCategory) {
		Map<String,Object> response = new HashMap<String,Object>();	
		
		if(kpiCategory.equals("OTD")) {
			List<LandingKPITrendChartDTO> kpiTrendChartForOTD = landingDashboardDAO.getKPITrendChartForOTD(businessUnit);	
			response.put("kpiTrendChartForOTD", kpiTrendChartForOTD);
		}
		else if(kpiCategory.equals("Billing")) {
			List<LandingKPITrendChartDTO> kpiTrendChartForBilling = landingDashboardDAO.getKPITrendChartForBilling(businessUnit);
			response.put("kpiTrendChartForBilling", kpiTrendChartForBilling);
		}
		else if(kpiCategory.equals("Risk")){
			List<LandingKPITrendChartDTO> kpiTrendChartForRisk = landingDashboardDAO.getKPITrendChartForRisk(businessUnit);
			response.put("kpiTrendChartForRisk",kpiTrendChartForRisk);
		}
		else if(kpiCategory.equals("Opportunities")){
			List<LandingKPITrendChartDTO> kpiTrendChartForOpportunities = landingDashboardDAO.getKPITrendChartForOpportunities(businessUnit);
			response.put("kpiTrendChartForOpportunities",kpiTrendChartForOpportunities);
		}
		else if(kpiCategory.equals("Documentation")){
			List<LandingKPITrendChartDTO> kpiTrendChartForDocumentation = landingDashboardDAO.getKPITrendChartForDocumentation(businessUnit);
			response.put("kpiTrendChartForDocumentation",kpiTrendChartForDocumentation);
		}
		else if(kpiCategory.equals("Changes")) {
			List<LandingKPITrendChartDTO> kpiTrendChartForChanges = landingDashboardDAO.getKPITrendChartForChanges(businessUnit);
			response.put("kpiTrendChartForChanges", kpiTrendChartForChanges);
		}
		else if(kpiCategory.equals("Quality")){
			List<LandingKPITrendChartDTO> kpiTrendChartForQuality = landingDashboardDAO.getKPITrendChartForQuality(businessUnit);
			response.put("kpiTrendChartForQuality",kpiTrendChartForQuality);
		}
		else {
			response.put("message", "KPI Category does not exist");
		}
		
		return response;
	}
	
	@Override
	public Map<String, Object> getLandingProjectList(HeaderDashboardDetailsDTO projectDetails) {
		Map<String,Object> response = new HashMap<String,Object>();
		
		List<LandingProjectDetailsDTO> getLandingProjectList = landingDashboardDAO.getLandingprojectDetails(projectDetails);
		response.put("projectListData", getLandingProjectList);
		return response;
	}

	@Override
	public byte[] downloadProjectDetails(String projectId,int companyId, String businessUnit, String segment, String region, int customerId) {
		ExportLandingProjectDetailsExcel excelDto = new ExportLandingProjectDetailsExcel();
		List<LandingProjectDetailsDTO> list = new ArrayList<LandingProjectDetailsDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		byte[] excelData = null;
		try {
			list = landingDashboardDAO.getLandingProjectDetailsExcel(projectId,companyId,businessUnit,segment,region,customerId);
			excelDto.exportLandingProjectDetailsExcel(workbook, list);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured when downloading project details excel file :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading project details excel file :: " + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getLandingQualityKPI(BillingDashboardFilterDTO filterValues) {
		Map<String,Object> response = new HashMap<>();
		String overdueCIRKPI = landingDashboardDAO.qualityOverdueCIRKPI(filterValues);
		String overdueNCRKPI = landingDashboardDAO.qualityOverdueNCRKPI(filterValues);
		String coPQKPI = landingDashboardDAO.qualityCoPQKPI(filterValues);
		response.put("overdueCIRKPI",overdueCIRKPI);
		response.put("overdueNCRKPI",overdueNCRKPI);
		response.put("coPQKPI",coPQKPI);
		return response;
	}


}
