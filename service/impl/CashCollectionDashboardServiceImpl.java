package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bh.realtrack.dao.CashCollectionDashboardDAO;
import com.bh.realtrack.dao.impl.OfeCashCollectionDashboardDAOImpl;
import com.bh.realtrack.dao.impl.TpsCashCollectionDashboardDAOImpl;
import com.bh.realtrack.dto.CashCollectionDashboardDropDownDTO;
import com.bh.realtrack.dto.CashCollectionDashboardFilterDTO;
import com.bh.realtrack.dto.CashCollectionDashboardOverallSummaryDetailDTO;
import com.bh.realtrack.dto.CashDashboardBusinessUnitSummaryDTO;
import com.bh.realtrack.dto.CashDashboardManageProjectResponseDTO;
import com.bh.realtrack.dto.CashDetailDTO;
import com.bh.realtrack.dto.CategoryDTO;
import com.bh.realtrack.dto.DownloadOFETargetTemplateDTO;
import com.bh.realtrack.dto.DownloadProjectTemplateDTO;
import com.bh.realtrack.dto.DownloadTPSTargetTemplateDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.FileUploadStatusDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.InvoiceDetailsDTO;
import com.bh.realtrack.dto.LastSuccessfulUpdateDetailsDTO;
import com.bh.realtrack.dto.LastUpdateDetailsDTO;
import com.bh.realtrack.dto.OFEBusinessSummaryDetailsDTO;
import com.bh.realtrack.dto.OFEProjectDetailsDTO;
import com.bh.realtrack.dto.OverallSummaryDetailDTO;
import com.bh.realtrack.dto.ProjectDetailDTO;
import com.bh.realtrack.dto.RegionSummaryDetailDTO;
import com.bh.realtrack.dto.SegmentSummaryDetailDTO;
import com.bh.realtrack.dto.TPSCashDetailDTO;
import com.bh.realtrack.dto.TPSProjectDetailDTO;
import com.bh.realtrack.dto.UpdateDetailsDTO;
import com.bh.realtrack.dto.YearDTO;
import com.bh.realtrack.excel.CashCollectionDashboardExcel;
import com.bh.realtrack.excel.ExportCashCollectionExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.CashCollectionDashboardService;
import com.bh.realtrack.service.ICommonService;
import com.bh.realtrack.util.CashCollectionDashboardConstants;

@Service
public class CashCollectionDashboardServiceImpl implements CashCollectionDashboardService {

	private static final Logger log = LoggerFactory.getLogger(CashCollectionDashboardServiceImpl.class);

	@Autowired
	private TpsCashCollectionDashboardDAOImpl tpsCashCollectionDashboardDAO;

	@Autowired
	private OfeCashCollectionDashboardDAOImpl ofeCashCollectionDashboardDAO;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private CallerContext callerContext;

	@Override
	public CashCollectionDashboardDropDownDTO getCashCollectionDashboardDropDownDetail(
			HeaderDashboardDetailsDTO headerDetails) {
		CashCollectionDashboardDropDownDTO dropDown = new CashCollectionDashboardDropDownDTO();
		CashCollectionDashboardDAO instance = getDAOInstance(headerDetails.getCompanyId());
		dropDown = instance.getCashCollectionDashboardDropDown(headerDetails);
		if (null != dropDown) {
			dropDown.setDefaultQuater(getDefaultQuater());
			dropDown.setDefaultTier3("Equipment");
		}
		return dropDown;
	}

	@Override
	public Map<String, Object> getOpenInvoiceDetails(String projectId) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<InvoiceDetailsDTO> details = new ArrayList<InvoiceDetailsDTO>();
		try {
			if (projectId != null) {
				if (projectId.contains("M_")) {
					details = tpsCashCollectionDashboardDAO.getOpenInvoiceDetails(projectId);
				} else {
					details = tpsCashCollectionDashboardDAO.getInstallOpenInvoiceDetails(projectId);
				}
				response.put("invoiceDetails", details);
			}
		} catch (Exception e) {
			log.error("getOpenInvoiceDetails(): Exception occurred : " + e.getMessage());
		}
		return response;
	}

	@Override
	public Map<String, Object> getConfiguratorDetails(String companyId) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<YearDTO> years = tpsCashCollectionDashboardDAO.getConfiguratorDetails(companyId);
		response.put("yearDropDownValues", years);
		String defaultYear = String.valueOf(new Date().getYear() + 1900);
		response.put("yearDropDownDefault", defaultYear);

		List<CategoryDTO> category = tpsCashCollectionDashboardDAO.getConfiguratorCategoryDetails(companyId);
		response.put("categoryDropDownValues", category);
		String defaultCategory = "Equipment Segment";
		response.put("categoryDropDownDefault", defaultCategory);

		return response;
	}

	private CashCollectionDashboardDAO getDAOInstance(int companyId) {
		CashCollectionDashboardDAO instance = null;

		if (companyId == 2) {
			instance = tpsCashCollectionDashboardDAO;
		} else if (companyId == 4) {
			instance = ofeCashCollectionDashboardDAO;
		}
		return instance;
	}

	@Override
	public Map<String, Object> getCashCollectionDashboardManageProjectDetails(HeaderDashboardDetailsDTO headerDetails) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		String projectId = commonService.fetchFavProjects();

		if (null == projectId) {
			projectId = "0";
		}

		List<CashDashboardManageProjectResponseDTO> manageProjectList = ofeCashCollectionDashboardDAO
				.getmanageProjectList(headerDetails, projectId);
		data.put("manageProjectList", manageProjectList);

		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getCashCollectionDashboardSegmentSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> dataMap = new HashMap<String, String>();
		List<SegmentSummaryDetailDTO> list = new ArrayList<SegmentSummaryDetailDTO>();
		String projectId = "", startDate = "", endDate = "";
		try {
			projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			kpiValues.setProjectId(projectId);
			dataMap = getDateFromQuarter(kpiValues.getSelectedQuater(), kpiValues.getCurrentYearFlag());
			startDate = dataMap.get("startDate");
			endDate = dataMap.get("endDate");
			log.info("startDate :" + startDate + " : endDate : " + endDate);
			kpiValues.setStartDate(startDate);
			kpiValues.setEndDate(endDate);
			if (null != kpiValues.getTier3() && !kpiValues.getTier3().isEmpty()
					&& kpiValues.getTier3().equalsIgnoreCase("Installation")) {
				list = tpsCashCollectionDashboardDAO.getCashCollectionDashboardInstallSegmentSummaryDetail(kpiValues);
			} else {
				list = tpsCashCollectionDashboardDAO.getCashCollectionDashboardSegmentSummaryDetail(kpiValues);
			}
			response.put("SegmentData", list);
		} catch (Exception e) {
			log.error("getCashCollectionDashboardSegmentSummaryDetail(): Exception occurred : " + e.getMessage());
		}
		return response;
	}

	@Override
	public Map<String, Object> getCashCollectionDashboardProjectDetail(CashCollectionDashboardFilterDTO filterValues)
			throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> currentYear = null;
		currentYear = getDate(filterValues.getStartDate(), filterValues.getEndDate(),
				filterValues.getCurrentYearFlag());

		log.info("startDate :" + filterValues.getStartDate() + " : endDate : " + filterValues.getEndDate()
				+ ":startDate :" + currentYear.get("startDate") + " endDate :" + currentYear.get("endDate"));
		String startDate = currentYear.get("startDate");
		String endDate = currentYear.get("endDate");

		List<ProjectDetailDTO> cashCollectionDashboardProjectList = ofeCashCollectionDashboardDAO
				.getCashCollectionDashboardProjectDetail(filterValues, startDate, endDate);
		log.info("cashCollectionDashboardProjectList======================="
				+ cashCollectionDashboardProjectList.size());

		data.put("ProjectData", cashCollectionDashboardProjectList);

		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getBusinessUnitSummary(CashCollectionDashboardFilterDTO filterValues) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> currentYear = null;
		currentYear = getDate(filterValues.getStartDate(), filterValues.getEndDate(),
				filterValues.getCurrentYearFlag());

		log.info("startDate :" + filterValues.getStartDate() + " : endDate : " + filterValues.getEndDate()
				+ ":startDate :" + currentYear.get("startDate") + " endDate :" + currentYear.get("endDate"));
		String startDate = currentYear.get("startDate");
		String endDate = currentYear.get("endDate");

		List<CashDashboardBusinessUnitSummaryDTO> cashcollectionbusinessUnitList = ofeCashCollectionDashboardDAO
				.getBusinessUnitSummary(filterValues, startDate, endDate);
		log.info("cashcollectionbusinessUnitList=======================" + cashcollectionbusinessUnitList.size());

		data.put("cashcollectionbusinessUnitList", cashcollectionbusinessUnitList);

		response.put("data", data);
		return response;
	}

	private Map<String, String> getDate(String startDate, String endDate, String currentYearFlag) throws Exception {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM/yyyy");

		Calendar c = Calendar.getInstance();
		Date date, date1;
		Map<String, String> currentYear = new HashMap<String, String>();
		DateTime datetime = new DateTime();
		String year = datetime.toString("YYYY");

		if (currentYearFlag != null && currentYearFlag != "" && currentYearFlag.equalsIgnoreCase("Yes")) {
			startDate = "Jan/" + year;
			endDate = "Dec" + "/" + year;
			date = format2.parse(startDate);
			date1 = format2.parse(endDate);

			c.setTime(date1);

			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

		}

		else if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {

			date = format2.parse(startDate);

			date1 = format2.parse(endDate);

			c.setTime(date1);

			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

		} else {
			throw new Exception("No Date selected: ");
		}

		currentYear.put("startDate", format1.format(date));

		currentYear.put("endDate", format1.format(c.getTime()));

		return currentYear;
	}

	@Override
	public Map<String, Object> getCashCollectionDashboardOverallSummaryDetail(
			CashCollectionDashboardFilterDTO filterValues) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> currentYear = null;
		currentYear = getDate(filterValues.getStartDate(), filterValues.getEndDate(),
				filterValues.getCurrentYearFlag());

		log.info("startDate :" + filterValues.getStartDate() + " : endDate : " + filterValues.getEndDate()
				+ ":startDate :" + currentYear.get("startDate") + " endDate :" + currentYear.get("endDate"));

		String startDate = currentYear.get("startDate");
		String endDate = currentYear.get("endDate");

		String pastDueKpi = ofeCashCollectionDashboardDAO.getPastDueKpi(filterValues, startDate, endDate);
		String pastDueLEKpi = ofeCashCollectionDashboardDAO.getPastDueLEKpi(filterValues, startDate, endDate);
		String cashLEKpi = ofeCashCollectionDashboardDAO.getCashLEKpi(filterValues, startDate, endDate);
		String collectedCashKpi = ofeCashCollectionDashboardDAO.getCollectedCashKpi(filterValues, startDate, endDate);
		String bmLinkage = ofeCashCollectionDashboardDAO.getBmLinkageKpi(filterValues, startDate, endDate);
		String umatchedBL = ofeCashCollectionDashboardDAO.getUnmatchedBLKpi(filterValues);
		String collectedToGoKpi = ofeCashCollectionDashboardDAO.getCollectedToGoKpi(filterValues, startDate, endDate);
		String pastDueDate = ofeCashCollectionDashboardDAO.getPastDueDateKpi(filterValues, startDate, endDate);

		data.put("pastDueKpi", pastDueKpi);
		data.put("pastDueLEKpi", pastDueLEKpi);
		data.put("cashLEKpi", cashLEKpi);
		data.put("collectedCashKpi", collectedCashKpi);
		data.put("bmLinkage", bmLinkage);
		data.put("umatchedBL", umatchedBL);
		data.put("collectedToGoKpi", collectedToGoKpi);
		data.put("pastDueDate", pastDueDate);

		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getCashCollectionDashboardTPSProjectDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<TPSProjectDetailDTO> list = new ArrayList<TPSProjectDetailDTO>();
		String projectId = "", startDate = "", endDate = "";
		try {
			projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			kpiValues.setProjectId(projectId);
			dateMap = getDateFromQuarter(kpiValues.getSelectedQuater(), kpiValues.getCurrentYearFlag());
			startDate = dateMap.get("startDate");
			endDate = dateMap.get("endDate");
			log.info("startDate :" + startDate + " : endDate : " + endDate);
			kpiValues.setStartDate(startDate);
			kpiValues.setEndDate(endDate);
			if (null != kpiValues.getTier3() && !kpiValues.getTier3().isEmpty()
					&& kpiValues.getTier3().equalsIgnoreCase("Installation")) {
				list = tpsCashCollectionDashboardDAO.getCashCollectionDashboardTPSInstallProjectDetail(kpiValues);
			} else {
				list = tpsCashCollectionDashboardDAO.getCashCollectionDashboardTPSProjectDetail(kpiValues);
			}
			response.put("ProjectData", list);
		} catch (Exception e) {
			log.error("getCashCollectionDashboardTPSProjectDetail(): Exception occurred : " + e.getMessage());
		}
		return response;
	}

	@Override
	public OverallSummaryDetailDTO getCashCollectionDashboardTPSOverallSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		Map<String, String> dateMap = new HashMap<String, String>();
		OverallSummaryDetailDTO details = new OverallSummaryDetailDTO();
		String projectId = "", startDate = "", endDate = "";
		try {
			projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			kpiValues.setProjectId(projectId);
			dateMap = getDateFromQuarter(kpiValues.getSelectedQuater(), kpiValues.getCurrentYearFlag());
			startDate = dateMap.get("startDate");
			endDate = dateMap.get("endDate");
			log.info("startDate :" + startDate + " : endDate : " + endDate);
			kpiValues.setStartDate(startDate);
			kpiValues.setEndDate(endDate);
			if (null != kpiValues.getTier3() && !kpiValues.getTier3().isEmpty()
					&& kpiValues.getTier3().equalsIgnoreCase("Installation")) {
				details = tpsCashCollectionDashboardDAO
						.getCashCollectionDashboardTPSInstallOverallSummaryDetail(kpiValues);
			} else {
				details = tpsCashCollectionDashboardDAO.getCashCollectionDashboardTPSOverallSummaryDetail(kpiValues);
			}
		} catch (Exception e) {
			log.error("getCashCollectionDashboardTPSOverallSummaryDetail(): Exception occurred : " + e.getMessage());
		}
		return details;
	}

	private String getDefaultQuater() {

		String defaultQuater = "OVERALL";
		StringBuilder currentQuater = new StringBuilder();
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		String yearString = String.valueOf(year).substring(2, 4);
		currentQuater.append(yearString);
		currentQuater.append("-");
		if (month <= 2) {
			currentQuater.append("1Q");
		} else if (month >= 3 && month <= 5) {
			currentQuater.append("2Q");
		} else if (month >= 6 && month <= 8) {
			currentQuater.append("3Q");
		} else {
			currentQuater.append("4Q");
		}
		defaultQuater = currentQuater.toString();
		return defaultQuater;
	}

	private Map<String, String> getDateFromQuarter(String selectedQuarter, String currentYearFlag) throws Exception {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM/yyyy");
		Map<String, String> response = new HashMap<String, String>();
		String startDate = "", endDate = "";
		Date date, date1;
		Calendar c = Calendar.getInstance();
		DateTime datetime = new DateTime();

		if (currentYearFlag != null && currentYearFlag != "" && currentYearFlag.equalsIgnoreCase("Yes")) {
			String year = "";
			year = datetime.toString("YYYY");

			startDate = "Jan/" + year;
			endDate = "Dec" + "/" + year;

		} else if (selectedQuarter != null && !selectedQuarter.isEmpty()) {
			String[] selectedQuarterStr = selectedQuarter.split(",");
			if (selectedQuarterStr != null && selectedQuarterStr.length == 1) {
				String year = "", quarter = "";
				String[] selectedQuarterArr = selectedQuarter.split("-");
				year = "20" + selectedQuarterArr[0];
				quarter = selectedQuarterArr[1];

				if (quarter.equalsIgnoreCase("1Q")) {
					startDate = "Jan/" + year;
					endDate = "Mar" + "/" + year;
				} else if (quarter.equalsIgnoreCase("2Q")) {
					startDate = "Apr/" + year;
					endDate = "Jun" + "/" + year;
				} else if (quarter.equalsIgnoreCase("3Q")) {
					startDate = "Jul/" + year;
					endDate = "Sep" + "/" + year;
				} else if (quarter.equalsIgnoreCase("4Q")) {
					startDate = "Oct/" + year;
					endDate = "Dec" + "/" + year;
				}

			} else if (selectedQuarterStr != null && selectedQuarterStr.length > 1) {
				String startYear = "", endYear = "", startQuarter = "", endQuarter = "";
				Arrays.sort(selectedQuarterStr);
				String[] minQuarterArr = selectedQuarterStr[0].split("-");
				String[] maxQuarterArr = selectedQuarterStr[selectedQuarterStr.length - 1].split("-");
				startYear = "20" + minQuarterArr[0];
				startQuarter = minQuarterArr[1];
				endYear = "20" + maxQuarterArr[0];
				endQuarter = maxQuarterArr[1];

				if (startQuarter.equalsIgnoreCase("1Q")) {
					startDate = "Jan/" + startYear;
				} else if (startQuarter.equalsIgnoreCase("2Q")) {
					startDate = "Apr/" + startYear;
				} else if (startQuarter.equalsIgnoreCase("3Q")) {
					startDate = "Jul/" + startYear;
				} else if (startQuarter.equalsIgnoreCase("4Q")) {
					startDate = "Oct/" + startYear;
				}

				if (endQuarter.equalsIgnoreCase("1Q")) {
					endDate = "Mar" + "/" + endYear;
				} else if (endQuarter.equalsIgnoreCase("2Q")) {
					endDate = "Jun" + "/" + endYear;
				} else if (endQuarter.equalsIgnoreCase("3Q")) {
					endDate = "Sep" + "/" + endYear;
				} else if (endQuarter.equalsIgnoreCase("4Q")) {
					endDate = "Dec" + "/" + endYear;
				}
			}
		}

		date = format2.parse(startDate);
		date1 = format2.parse(endDate);
		c.setTime(date1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		startDate = format1.format(date);
		endDate = format1.format(c.getTime());
		response.put("startDate", startDate);
		response.put("endDate", endDate);
		return response;
	}

	@Override
	public Map<String, Object> getCashCollectionDashboardCashDetail(CashCollectionDashboardFilterDTO filterValues)
			throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> currentYear = null;
		currentYear = getDate(filterValues.getStartDate(), filterValues.getEndDate(),
				filterValues.getCurrentYearFlag());

		log.info("startDate :" + filterValues.getStartDate() + " : endDate : " + filterValues.getEndDate()
				+ ":startDate :" + currentYear.get("startDate") + " endDate :" + currentYear.get("endDate"));
		String startDate = currentYear.get("startDate");
		String endDate = currentYear.get("endDate");

		List<CashDetailDTO> cashCollectionDashboardCashList = ofeCashCollectionDashboardDAO
				.getCashCollectionDashboardCashList(filterValues, startDate, endDate);
		log.info("cashCollectionDashboardCashList=======================" + cashCollectionDashboardCashList.size());

		data.put("CashData", cashCollectionDashboardCashList);

		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getCashCollectionDashboardTPSCashDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<TPSCashDetailDTO> list = new ArrayList<TPSCashDetailDTO>();
		String projectId = "", startDate = "", endDate = "";
		try {
			projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			kpiValues.setProjectId(projectId);
			dateMap = getDateFromQuarter(kpiValues.getSelectedQuater(), kpiValues.getCurrentYearFlag());
			startDate = dateMap.get("startDate");
			endDate = dateMap.get("endDate");
			log.info("startDate :" + startDate + " : endDate : " + endDate);
			kpiValues.setStartDate(startDate);
			kpiValues.setEndDate(endDate);
			if (null != kpiValues.getTier3() && !kpiValues.getTier3().isEmpty()
					&& kpiValues.getTier3().equalsIgnoreCase("Installation")) {
				list = tpsCashCollectionDashboardDAO.getCashCollectionDashboardTPSInstallCashDetail(kpiValues);
			} else {
				list = tpsCashCollectionDashboardDAO.getCashCollectionDashboardTPSCashDetail(kpiValues);
			}
			response.put("CashData", list);
		} catch (Exception e) {
			log.error("getCashCollectionDashboardTPSCashDetail(): Exception occurred : " + e.getMessage());
		}
		return response;
	}

	@Override
	public UpdateDetailsDTO getUpdateDetails(String companyId, String tier3) {
		UpdateDetailsDTO updateDetailsDTO = new UpdateDetailsDTO();
		List<LastSuccessfulUpdateDetailsDTO> lastSuccessfulUpdateDetailsDTO = new ArrayList<LastSuccessfulUpdateDetailsDTO>();
		List<LastUpdateDetailsDTO> lastUpdateDetailsDTO = new ArrayList<LastUpdateDetailsDTO>();
		List<ErrorDetailsDTO> errorDetails = new ArrayList<ErrorDetailsDTO>();
		if (null != tier3 && !tier3.isEmpty() && tier3.equalsIgnoreCase("Installation")) {
			lastSuccessfulUpdateDetailsDTO = tpsCashCollectionDashboardDAO
					.getInstallLastSuccessfulUpdateData(companyId);
			lastUpdateDetailsDTO = tpsCashCollectionDashboardDAO.getInstallLastUpdatedDate(companyId);
			errorDetails = tpsCashCollectionDashboardDAO.getInstallErrorDetailsData(companyId);
		} else {
			lastSuccessfulUpdateDetailsDTO = tpsCashCollectionDashboardDAO.getLastSuccessfulUpdateData(companyId);
			lastUpdateDetailsDTO = tpsCashCollectionDashboardDAO.getLastUpdatedDate(companyId);
			errorDetails = tpsCashCollectionDashboardDAO.getErrorDetailsData(companyId);
		}
		updateDetailsDTO.setLastSuccessfulUpdateDetails(lastSuccessfulUpdateDetailsDTO);
		updateDetailsDTO.setLastUpdateDetails(lastUpdateDetailsDTO);
		updateDetailsDTO.setErrorDetails(errorDetails);
		return updateDetailsDTO;
	}

	public List<ErrorDetailsDTO> getNotProcessedProjectDetails(String companyId) {
		List<ErrorDetailsDTO> errorDetails = tpsCashCollectionDashboardDAO.getNotProcessedProjectDetails(companyId);
		return errorDetails;
	}

	public List<ErrorDetailsDTO> getInstallNotProcessedProjectDetails(String companyId) {
		List<ErrorDetailsDTO> errorDetails = tpsCashCollectionDashboardDAO
				.getInstallNotProcessedProjectDetails(companyId);
		return errorDetails;
	}

	@Override
	public Map<String, Object> getUpdatedTargetDetails(String companyId, String year, String category) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			if (companyId != null && year != null && (category.equalsIgnoreCase("Equipment Segment")
					|| category.equalsIgnoreCase("Installation Segment"))) {
				response.put("UpdateTargetDetails",
						tpsCashCollectionDashboardDAO.getUpdatedTargetDetails(companyId, year, category));
			}
			if (companyId != null && year != null && (category.equalsIgnoreCase("Equipment Region")
					|| category.equalsIgnoreCase("Installation Region"))) {
				response.put("UpdateTargetDetails",
						tpsCashCollectionDashboardDAO.getUpdatedRegionTargetDetails(companyId, year, category));
			}
		} catch (Exception e) {
			log.error("getUpdatedTargetDetails(): Exception occurred : " + e.getMessage());
		}
		return response;
	}

	@Override
	public UpdateDetailsDTO getTargetUploadDetails(String companyId) {
		UpdateDetailsDTO updateDetailsDTO = new UpdateDetailsDTO();
		List<LastSuccessfulUpdateDetailsDTO> lastSuccessfulUpdateDetailsDTO = tpsCashCollectionDashboardDAO
				.getTargetLastSuccessfulUpdateData(companyId);
		List<LastUpdateDetailsDTO> lastUpdateDetailsDTO = tpsCashCollectionDashboardDAO
				.getTargetLastUpdatedDate(companyId);
		List<ErrorDetailsDTO> errorDetails = tpsCashCollectionDashboardDAO.getTargetErrorDetailsData(companyId);
		updateDetailsDTO.setLastSuccessfulUpdateDetails(lastSuccessfulUpdateDetailsDTO);
		updateDetailsDTO.setLastUpdateDetails(lastUpdateDetailsDTO);
		updateDetailsDTO.setErrorDetails(errorDetails);
		return updateDetailsDTO;
	}

	@SuppressWarnings("resource")
	public byte[] getProjectTemplate(String companyId) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportCashCollectionExcel exportTargetTemplateExcel = new ExportCashCollectionExcel();
		List<DownloadProjectTemplateDTO> list = new ArrayList<DownloadProjectTemplateDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			if (companyId == "2" || companyId.equals("2")) {
				list = tpsCashCollectionDashboardDAO.getTPSProjectTemplateDetails(companyId);
				workbook = exportTargetTemplateExcel.exportTPSProjectTemplateExcel(workbook, list);
			} else {
				workbook = exportTargetTemplateExcel.exportOFEProjectTemplateExcel(workbook);
			}
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading Project Template :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading Project Template :: " + e.getMessage());
			}
		}
		return excelData;

	}

	@SuppressWarnings("resource")
	public byte[] getInstallProjectTemplate(String companyId) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportCashCollectionExcel templateDTO = new ExportCashCollectionExcel();
		List<DownloadProjectTemplateDTO> list = new ArrayList<DownloadProjectTemplateDTO>();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			if (companyId == "2" || companyId.equals("2")) {
				list = tpsCashCollectionDashboardDAO.getTPSInstallProjectTemplateDetails(companyId);
				workbook = templateDTO.exportTPSInstallProjectTemplateExcel(workbook, list);
			}
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading Project Template :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading Project Template :: " + e.getMessage());
			}
		}
		return excelData;

	}

	@SuppressWarnings("resource")
	public byte[] getTargetTemplate(String companyId) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = null;
		ExportCashCollectionExcel exportTargetTemplateExcel = new ExportCashCollectionExcel();
		byte[] excelData = null;
		try {
			workbook = new SXSSFWorkbook();
			if (companyId == "2" || companyId.equals("2")) {
				List<DownloadTPSTargetTemplateDTO> list = new ArrayList<DownloadTPSTargetTemplateDTO>();
				list = tpsCashCollectionDashboardDAO.getTPSTargetTemplateDetails(companyId);
				workbook = exportTargetTemplateExcel.exportTPSTargetTemplateExcel(workbook, list);
			} else {
				workbook = exportTargetTemplateExcel.exportOFETargetTemplateExcel(workbook);
			}
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured while downloading Target Template :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured while downloading Target Template :: " + e.getMessage());
			}
		}
		return excelData;

	}

	public byte[] downloadCashDetails(CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		String projectId = "", startDate = "", endDate = "";
		Map<String, String> dateMap = new HashMap<String, String>();
		CashCollectionDashboardExcel cashCollectionDashboardExcel = new CashCollectionDashboardExcel();
		try {
			projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			kpiValues.setProjectId(projectId);
			dateMap = getDateFromQuarter(kpiValues.getSelectedQuater(), kpiValues.getCurrentYearFlag());
			startDate = dateMap.get("startDate");
			endDate = dateMap.get("endDate");
			log.info("startDate :" + startDate + " : endDate : " + endDate);
			kpiValues.setStartDate(startDate);
			kpiValues.setEndDate(endDate);

			List<TPSCashDetailDTO> cashDetails = tpsCashCollectionDashboardDAO
					.getCashCollectionDashboardTPSCashDetail(kpiValues);
			log.info("Creating Cash Details Sheet with " + cashDetails.size() + " rows.");

			List<SegmentSummaryDetailDTO> segmentDetails = tpsCashCollectionDashboardDAO
					.getCashCollectionDashboardSegmentSummaryDetail(kpiValues);
			log.info("Creating Segment Details Sheet with " + segmentDetails.size() + " rows.");

			List<TPSProjectDetailDTO> projectList = tpsCashCollectionDashboardDAO
					.getCashCollectionDashboardTPSProjectDetail(kpiValues);
			log.info("Creating Project Details Sheet with " + projectList.size() + " rows.");

			List<RegionSummaryDetailDTO> regionDetails = tpsCashCollectionDashboardDAO
					.getCashCollectionDashboardRegionSummaryDetail(kpiValues);
			log.info("Creating Region Details Sheet with " + regionDetails.size() + " rows.");

			List<TPSProjectDetailDTO> consolidatedProject = tpsCashCollectionDashboardDAO
					.getCashCollectionDashboardTPSConsolidatedProjectDetail(kpiValues);
			log.info("Creating Consolidated Project Details Sheet with " + consolidatedProject.size() + " rows.");

			cashCollectionDashboardExcel.exportDetailsTableExcel(workbook, segmentDetails, regionDetails,
					consolidatedProject, projectList, cashDetails);
			workbook.write(bos);
			excelData = bos.toByteArray();

		} catch (Exception e) {
			log.error("Error occured when downlaoding Excel file" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downlaoding Excel file" + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public byte[] downloadInstallCashDetails(CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		String projectId = "", startDate = "", endDate = "";
		Map<String, String> dateMap = new HashMap<String, String>();
		CashCollectionDashboardExcel cashCollectionDashboardExcel = new CashCollectionDashboardExcel();
		try {
			projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			kpiValues.setProjectId(projectId);
			dateMap = getDateFromQuarter(kpiValues.getSelectedQuater(), kpiValues.getCurrentYearFlag());
			startDate = dateMap.get("startDate");
			endDate = dateMap.get("endDate");
			log.info("startDate :" + startDate + " : endDate : " + endDate);
			kpiValues.setStartDate(startDate);
			kpiValues.setEndDate(endDate);

			List<TPSCashDetailDTO> cashDetails = tpsCashCollectionDashboardDAO
					.getCashCollectionDashboardTPSInstallCashDetail(kpiValues);
			log.info("Creating Cash Details Sheet with " + cashDetails.size() + " rows.");

			List<SegmentSummaryDetailDTO> segmentDetails = tpsCashCollectionDashboardDAO
					.getCashCollectionDashboardInstallSegmentSummaryDetail(kpiValues);
			log.info("Creating Segment Details Sheet with " + segmentDetails.size() + " rows.");

			List<TPSProjectDetailDTO> projectList = tpsCashCollectionDashboardDAO
					.getCashCollectionDashboardTPSInstallProjectDetail(kpiValues);
			log.info("Creating Project Details Sheet with " + projectList.size() + " rows.");

			List<RegionSummaryDetailDTO> regionDetails = tpsCashCollectionDashboardDAO
					.getCashCollectionDashboardInstallRegionSummaryDetail(kpiValues);
			log.info("Creating Region Details Sheet with " + regionDetails.size() + " rows.");

			cashCollectionDashboardExcel.exportInstallDetailsTableExcel(workbook, segmentDetails, regionDetails,
					projectList, cashDetails);
			workbook.write(bos);
			excelData = bos.toByteArray();

		} catch (Exception e) {
			log.error("Error occured when downlaoding Excel file" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downlaoding Excel file" + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, String> importTargetExcelData(String companyId, MultipartFile excelFile) {
		Map<String, String> responseMap = new HashMap<String, String>();
		Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
		FileUploadStatusDTO statusDTO = new FileUploadStatusDTO();
		boolean validationStatus = true;
		String sso = "", fileName = "";
		StringBuilder validationMessage = new StringBuilder();
		Integer trackId = null;
		try {
			if (excelFile != null && !excelFile.isEmpty()) {
				sso = callerContext.getName();
				fileName = excelFile.getOriginalFilename();
				if (validationStatus && checkInProgressFileUpload(companyId,
						CashCollectionDashboardConstants.GET_TARGET_EXCEL_MODULE_NAME)) {
					validationMessage.append("Already a file upload is in In-Progress status !!!");
					validationStatus = false;
					responseMap.put("status", "Error");
					responseMap.put("message", validationMessage.toString());
				} else {
					statusDTO.setStatus("In-Progress");
					statusDTO.setCompanyId(companyId);
					statusDTO.setSso(sso);
					statusDTO.setFileName(fileName);
					statusDTO.setModuleName(CashCollectionDashboardConstants.GET_TARGET_EXCEL_MODULE_NAME);
					tpsCashCollectionDashboardDAO.insertFileTrackingDetails(statusDTO);
					responseMap.put("status", "Success");
					responseMap.put("message", "");
				}

				if (validationStatus) {
					try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());) {
						trackId = tpsCashCollectionDashboardDAO.getFileTrackingId(statusDTO);
						statusDTO.setTrackingId(String.valueOf(trackId));
						log.info("Target Details - Track ID :: " + trackId);
						processTargetExcelFile(companyId, workbook, headerIndexMap, statusDTO);
					}
				}
			} else {
				log.error("Multipart File is empty or null");
				responseMap.put("status", "Error");
				responseMap.put("message", "Error while uploading target excel file");
				return responseMap;
			}
		} catch (Exception e) {
			log.error("Error while uploading target details excel file :: " + e.getMessage());
			if (excelFile != null && !excelFile.isEmpty()) {
				statusDTO.setErrorMsg("Error");
				statusDTO.setStatus("Error");
				tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
			}
		}
		return responseMap;
	}

	private boolean checkInProgressFileUpload(String companyId, String moduleName) {
		boolean inProgressFileUpload = false;
		inProgressFileUpload = tpsCashCollectionDashboardDAO.getFileUploadStatus(companyId, moduleName);
		return inProgressFileUpload;
	}

	private Map<Integer, String> getTargetExcelHeaderColumnMap(String companyId) {
		Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
		headerIndexMap = tpsCashCollectionDashboardDAO.fetchTargetExcelHeaderColumnMap(companyId);
		return headerIndexMap;
	}

	private boolean validateTargetExcelColumnDetails(String companyId, Workbook workbook,
			Map<Integer, String> headerIndexMap, StringBuilder validationMessage, FileUploadStatusDTO statusDTO) {
		Map<String, String> responseMap = new HashMap<String, String>();
		boolean validationStatus = true;
		boolean headerColumnsErrorFlag = false;
		StringBuilder errorHeaderColumns = new StringBuilder();
		short minColIx, maxColIx;
		Row headerRow = null;
		try {
			log.info("Validating Excel Column Details for :: " + statusDTO.getModuleName());
			Sheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			log.info("totalRows :: " + totalRows);

			// check if the excel contains at least one row
			if (validationStatus) {
				if (totalRows < 2) {
					validationMessage.append("The upload file has no content");
					validationStatus = false;
					responseMap.put("status", "Error");
					responseMap.put("message", validationMessage.toString());
				}
			}

			// check if the excel contains correct no of columns
			if (validationStatus) {
				headerRow = sheet.getRow(0);
				maxColIx = headerRow.getLastCellNum();
				log.info("headerIndexMap Size :: " + headerIndexMap.size());
				log.info("maxColIx :: " + maxColIx);
				if (maxColIx != headerIndexMap.size()) {
					validationMessage.append("Incorrect number of columns are present in excel file");
					validationStatus = false;
					responseMap.put("status", "Error");
					responseMap.put("message", validationMessage.toString());
				}
			}

			// check if the excel contains header with correct column names and order
			if (validationStatus) {
				headerRow = sheet.getRow(0);
				minColIx = headerRow.getFirstCellNum();
				maxColIx = headerRow.getLastCellNum();
				log.info("minColIx :: " + minColIx);
				for (int colIx = minColIx; colIx < maxColIx; colIx++) {
					Cell cell = headerRow.getCell(colIx);
					String headerCellValue = cell.getStringCellValue();
					String headerMapValue = headerIndexMap.get(colIx);
					if (!headerMapValue.equalsIgnoreCase(headerCellValue)) {
						validationStatus = false;
						if (headerColumnsErrorFlag) {
							errorHeaderColumns.append(", ");
						}
						errorHeaderColumns.append(headerMapValue);
						headerColumnsErrorFlag = true;
					}
				}

				if (headerColumnsErrorFlag) {
					validationMessage.append(
							"Column(s) " + errorHeaderColumns.toString() + " are either misplaced or misspelled");
					responseMap.put("status", "Error");
					responseMap.put("message", validationMessage.toString());
				}
			}
			log.info("validationStatus :: " + validationStatus);
			log.info("validationMessage :: " + validationMessage);
		} catch (Exception e) {
			log.error("Error while validating excel column details :: " + e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
		}
		return validationStatus;
	}

	private void processTargetExcelFile(String companyId, XSSFWorkbook workbook, Map<Integer, String> headerIndexMap,
			FileUploadStatusDTO statusDTO) {
		Runnable caller1 = new Runnable() {
			@Override
			public void run() {
				Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
				boolean validationStatus = true;
				StringBuilder validationMessage = new StringBuilder();
				try {
					if (validationStatus) {
						headerIndexMap = getTargetExcelHeaderColumnMap(companyId);
						validationStatus = validateTargetExcelColumnDetails(companyId, workbook, headerIndexMap,
								validationMessage, statusDTO);
					}

					if (!validationStatus) {
						statusDTO.setErrorMsg(validationMessage.toString());
						statusDTO.setStatus("Error");
						tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
					}

					if (validationStatus) {
						if (companyId.equalsIgnoreCase("2")) {
							List<DownloadTPSTargetTemplateDTO> list = new ArrayList<DownloadTPSTargetTemplateDTO>();
							list = readTPSTargetDetailsExcelFile(workbook, headerIndexMap, statusDTO);
							tpsCashCollectionDashboardDAO.insertTPSTargetExcelStageData(list, statusDTO);
							tpsCashCollectionDashboardDAO.callFileUploadStageToTarget(statusDTO);
						} else if (companyId.equalsIgnoreCase("4")) {
							List<DownloadOFETargetTemplateDTO> list = new ArrayList<DownloadOFETargetTemplateDTO>();
							list = readOFETargetDetailsExcelFile(workbook, headerIndexMap, statusDTO);
							tpsCashCollectionDashboardDAO.insertOFETargetExcelStageData(list, statusDTO);
							tpsCashCollectionDashboardDAO.callFileUploadStageToTarget(statusDTO);
						}
					}
				} catch (Exception e) {
					log.error("Error while processing Target Details excel file :: " + e.getMessage());
					statusDTO.setErrorMsg("Error");
					statusDTO.setStatus("Error");
					tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
				}
			}
		};
		new Thread(caller1).start();
	}

	private List<DownloadTPSTargetTemplateDTO> readTPSTargetDetailsExcelFile(XSSFWorkbook workbook,
			Map<Integer, String> headerIndexMap, FileUploadStatusDTO statusDTO) {
		List<DownloadTPSTargetTemplateDTO> list = new ArrayList<DownloadTPSTargetTemplateDTO>();
		try {
			Sheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
				Map<String, String> dateMap = new HashMap<String, String>();
				DownloadTPSTargetTemplateDTO dto = new DownloadTPSTargetTemplateDTO();
				int colIx = 0;
				String cellValue = "", startDate = "", endDate = "";
				Cell cell = null;
				Row dataRow = sheet.getRow(rowNum);
				if (isRowEmpty(dataRow)) {
					continue;
				}
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setBusiness(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setSegment(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setQuarter(cellValue.split("\\.")[0]);
				if (cellValue != null && !cellValue.equalsIgnoreCase("")) {
					dateMap = getFileUploadDateFromQuarter(cellValue);
					startDate = dateMap.get("startDate");
					endDate = dateMap.get("endDate");
				}
				dto.setStartDt(startDate);
				dto.setEndDt(endDate);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRemarks(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setValue(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setCategory(cellValue);
				list.add(dto);
			}
			log.info("TPS - Target Details list size :: " + list.size());
		} catch (Exception e) {
			log.error("Error while processing TPS - Target Details excel file :: " + e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
		}
		return list;
	}

	private List<DownloadOFETargetTemplateDTO> readOFETargetDetailsExcelFile(XSSFWorkbook workbook,
			Map<Integer, String> headerIndexMap, FileUploadStatusDTO statusDTO) {
		List<DownloadOFETargetTemplateDTO> list = new ArrayList<DownloadOFETargetTemplateDTO>();
		try {
			Sheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
				Map<String, String> dateMap = new HashMap<String, String>();
				DownloadOFETargetTemplateDTO dto = new DownloadOFETargetTemplateDTO();
				int colIx = 0;
				String cellValue = "", startDate = "", endDate = "";
				Cell cell = null;
				Row dataRow = sheet.getRow(rowNum);
				if (isRowEmpty(dataRow)) {
					continue;
				}
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setProjectId(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setSegment(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setQuarter(cellValue.split("\\.")[0]);
				if (cellValue != null && !cellValue.equalsIgnoreCase("")) {
					dateMap = getFileUploadDateFromQuarter(cellValue);
					startDate = dateMap.get("startDate");
					endDate = dateMap.get("endDate");
					log.info("startDate :" + startDate + " : endDate : " + endDate);
				}
				dto.setStartDt(startDate);
				dto.setEndDt(endDate);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRemarks(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setValue(cellValue);
				list.add(dto);
			}
			log.info("OFE - Target Details list size :: " + list.size());
		} catch (Exception e) {
			log.error("Error while processing OFE - Target Details excel file :: " + e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
		}
		return list;
	}

	private static boolean isRowEmpty(Row row) {
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();
		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
					isEmpty = false;
					break;
				}
			}
		}
		return isEmpty;
	}

	private String getCellValueByType(Cell cell) {
		String cellValue = "";
		if (null != cell.getCellType()) {
			switch (cell.getCellType()) {
			case STRING:
				cellValue = null != cell.getStringCellValue() ? cell.getStringCellValue() : "";
				break;
			case NUMERIC:
				cellValue = String.valueOf((Object) cell.getNumericCellValue());
				if (DateUtil.isCellDateFormatted(cell)) {
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = cell.getDateCellValue();
					cellValue = df.format(date);
				}
				break;
			case BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case BLANK:
				cellValue = "";
				break;
			case ERROR:
				cellValue = "";
				break;
			case FORMULA:
				cellValue = "";
				break;
			case _NONE:
				cellValue = "";
				break;
			default:
				cellValue = null != cell.getStringCellValue() ? cell.getStringCellValue() : "";
				break;
			}
		}
		return cellValue;
	}

	@Override
	public Map<String, String> importProjectExcelData(String companyId, MultipartFile excelFile) {
		Map<String, String> responseMap = new HashMap<String, String>();
		Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
		FileUploadStatusDTO statusDTO = new FileUploadStatusDTO();
		boolean validationStatus = true;
		String sso = "", fileName = "";
		StringBuilder validationMessage = new StringBuilder();
		Integer trackId = null;
		try {
			if (excelFile != null && !excelFile.isEmpty()) {
				sso = callerContext.getName();
				fileName = excelFile.getOriginalFilename();
				if (validationStatus && checkInProgressFileUpload(companyId,
						CashCollectionDashboardConstants.GET_PROJECT_EXCEL_MODULE_NAME)) {
					validationMessage.append("Already a file upload is in In-Progress status !!!");
					validationStatus = false;
					responseMap.put("status", "Error");
					responseMap.put("message", validationMessage.toString());
				} else {
					statusDTO.setStatus("In-Progress");
					statusDTO.setCompanyId(companyId);
					statusDTO.setSso(sso);
					statusDTO.setFileName(fileName);
					statusDTO.setModuleName(CashCollectionDashboardConstants.GET_PROJECT_EXCEL_MODULE_NAME);
					tpsCashCollectionDashboardDAO.insertFileTrackingDetails(statusDTO);
					responseMap.put("status", "Success");
					responseMap.put("message", "");
				}
				if (validationStatus) {
					try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());) {
						trackId = tpsCashCollectionDashboardDAO.getFileTrackingId(statusDTO);
						statusDTO.setTrackingId(String.valueOf(trackId));
						log.info("Project Excel - Track ID :: " + trackId);
						processProjectExcelFile(companyId, workbook, headerIndexMap, statusDTO);
					}
				}
			} else {
				log.error("Multipart File is empty or null");
				responseMap.put("status", "Error");
				responseMap.put("message", "Error while uploading project excel file !!!");
				return responseMap;
			}
		} catch (Exception e) {
			log.error("Error while uploading project excel file :: " + e.getMessage());
			if (excelFile != null && !excelFile.isEmpty()) {
				statusDTO.setErrorMsg("Error");
				statusDTO.setStatus("Error");
				tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
			}
		}
		return responseMap;
	}

	private void processProjectExcelFile(String companyId, XSSFWorkbook workbook, Map<Integer, String> headerIndexMap,
			FileUploadStatusDTO statusDTO) {

		Runnable caller1 = new Runnable() {
			@Override
			public void run() {
				Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
				boolean validationStatus = true;
				StringBuilder validationMessage = new StringBuilder();
				try {
					if (validationStatus) {
						headerIndexMap = getProjectExcelHeaderColumnMap(companyId);
						validationStatus = validateTargetExcelColumnDetails(companyId, workbook, headerIndexMap,
								validationMessage, statusDTO);
					}
					if (!validationStatus) {
						// update tracking tbl as false (tracking id) create a new entry in error table
						statusDTO.setErrorMsg(validationMessage.toString());
						statusDTO.setStatus("Error");
						tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
					}
					if (validationStatus) {
						List<DownloadProjectTemplateDTO> list = new ArrayList<DownloadProjectTemplateDTO>();
						list = readProjectDetailsExcelFile(workbook, headerIndexMap, statusDTO);
						tpsCashCollectionDashboardDAO.insertProjectExcelStageData(list, statusDTO);
						tpsCashCollectionDashboardDAO.callFileUploadStageToTarget(statusDTO);
						callCashCollectionProcedure();
					}
				} catch (Exception e) {
					// update tracking tbl as false (tracking id) create a new entry in error table
					log.error("Error while processing Project Details excel file :: " + e.getMessage());
					statusDTO.setErrorMsg("Error");
					statusDTO.setStatus("Error");
					tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
				}
			}
		};
		new Thread(caller1).start();
	}

	protected List<DownloadProjectTemplateDTO> readProjectDetailsExcelFile(Workbook workbook,
			Map<Integer, String> headerIndexMap, FileUploadStatusDTO statusDTO) {
		List<DownloadProjectTemplateDTO> list = new ArrayList<DownloadProjectTemplateDTO>();
		try {
			Sheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
				DownloadProjectTemplateDTO dto = new DownloadProjectTemplateDTO();
				int colIx = 0;
				String cellValue = "";
				Cell cell = null;
				Row dataRow = sheet.getRow(rowNum);
				if (isRowEmpty(dataRow)) {
					continue;
				}
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setProjectId(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setPm(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setSpm(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setPmLeader(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setProjectName(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setContractCustomer(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setBusiness(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setSegment(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRegion(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInvoiceNumber(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRiskyOppty(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRiskComments(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInvoiceDate(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInvoiceDueDate(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setCollectionDate(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setCurrency(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInvoiceAmountCurr(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setOutstandingAmountCurr(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setCashCollectedCurr(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setMilestoneID(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setMilestoneDesc(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setForecastCollectionDate(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setBillingCurrency(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setMilestoneAmountCurr(cellValue);
				list.add(dto);
			}
			log.info("Project Details list size :: " + list.size());
		} catch (Exception e) {
			log.error("Error while processing Project Details excel file :: " + e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
		}
		return list;
	}

	protected Map<Integer, String> getProjectExcelHeaderColumnMap(String companyId) {
		Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
		headerIndexMap = tpsCashCollectionDashboardDAO.getProjectExcelHeaderColumnMap(companyId);
		return headerIndexMap;
	}

	private Map<String, String> getFileUploadDateFromQuarter(String yearQuarter) throws Exception {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM/yyyy");
		Map<String, String> response = new HashMap<String, String>();
		String startDate = "", endDate = "";
		Date date, date1;
		Calendar c = Calendar.getInstance();
		if (yearQuarter != null && !yearQuarter.isEmpty() && yearQuarter.contains("Q")) {
			String year = "", quarter = "";
			String[] selectedQuarterArr = yearQuarter.split("-");
			year = "20" + selectedQuarterArr[1];
			quarter = selectedQuarterArr[0];

			if (quarter.equalsIgnoreCase("Q1")) {
				startDate = "Jan/" + year;
				endDate = "Mar" + "/" + year;
			} else if (quarter.equalsIgnoreCase("Q2")) {
				startDate = "Apr/" + year;
				endDate = "Jun" + "/" + year;
			} else if (quarter.equalsIgnoreCase("Q3")) {
				startDate = "Jul/" + year;
				endDate = "Sep" + "/" + year;
			} else if (quarter.equalsIgnoreCase("Q4")) {
				startDate = "Oct/" + year;
				endDate = "Dec" + "/" + year;
			}

		} else if (yearQuarter != null && !yearQuarter.isEmpty() && !yearQuarter.contains("Q")) {
			startDate = "Jan/" + yearQuarter;
			endDate = "Dec" + "/" + yearQuarter;
		}
		date = format2.parse(startDate);
		date1 = format2.parse(endDate);
		c.setTime(date1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		startDate = format1.format(date);
		endDate = format1.format(c.getTime());
		response.put("startDate", startDate);
		response.put("endDate", endDate);
		return response;
	}

	@Override
	public Map<String, Object> getCashCollectionDashboardRegionSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> dataMap = new HashMap<String, String>();
		List<RegionSummaryDetailDTO> list = new ArrayList<RegionSummaryDetailDTO>();
		String projectId = "", startDate = "", endDate = "";
		try {
			projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			kpiValues.setProjectId(projectId);
			dataMap = getDateFromQuarter(kpiValues.getSelectedQuater(), kpiValues.getCurrentYearFlag());
			startDate = dataMap.get("startDate");
			endDate = dataMap.get("endDate");
			log.info("startDate :" + startDate + " : endDate : " + endDate);
			kpiValues.setStartDate(startDate);
			kpiValues.setEndDate(endDate);
			if (null != kpiValues.getTier3() && !kpiValues.getTier3().isEmpty()
					&& kpiValues.getTier3().equalsIgnoreCase("Installation")) {
				list = tpsCashCollectionDashboardDAO.getCashCollectionDashboardInstallRegionSummaryDetail(kpiValues);
			} else {
				list = tpsCashCollectionDashboardDAO.getCashCollectionDashboardRegionSummaryDetail(kpiValues);
			}
			response.put("RegionData", list);
		} catch (Exception e) {
			log.error("getCashCollectionDashboardRegionSummaryDetail(): Exception occurred : " + e.getMessage());
		}
		return response;
	}

	@Override
	public Map<String, Object> getCashCollectionDashboardTPSConsolidatedProjectDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<TPSProjectDetailDTO> list = new ArrayList<TPSProjectDetailDTO>();
		String projectId = "", startDate = "", endDate = "";
		try {
			projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			kpiValues.setProjectId(projectId);
			dateMap = getDateFromQuarter(kpiValues.getSelectedQuater(), kpiValues.getCurrentYearFlag());
			startDate = dateMap.get("startDate");
			endDate = dateMap.get("endDate");
			log.info("startDate :" + startDate + " : endDate : " + endDate);
			kpiValues.setStartDate(startDate);
			kpiValues.setEndDate(endDate);
			list = tpsCashCollectionDashboardDAO.getCashCollectionDashboardTPSConsolidatedProjectDetail(kpiValues);
			response.put("ProjectData", list);
		} catch (Exception e) {
			log.error(
					"getCashCollectionDashboardTPSConsolidatedProjectDetail(): Exception occurred : " + e.getMessage());
		}
		return response;
	}

	@Override
	public Map<String, String> importInstallProjectExcelData(String companyId, MultipartFile excelFile) {
		Map<String, String> responseMap = new HashMap<String, String>();
		Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
		FileUploadStatusDTO statusDTO = new FileUploadStatusDTO();
		boolean validationStatus = true;
		String sso = "", fileName = "";
		StringBuilder validationMessage = new StringBuilder();
		Integer trackId = null;
		try {
			if (excelFile != null && !excelFile.isEmpty()) {
				sso = callerContext.getName();
				fileName = excelFile.getOriginalFilename();
				if (validationStatus && checkInProgressFileUpload(companyId,
						CashCollectionDashboardConstants.GET_INSTALL_PROJECT_EXCEL_MODULE_NAME)) {
					validationMessage.append("Already a file upload is in In-Progress status !!!");
					validationStatus = false;
					responseMap.put("status", "Error");
					responseMap.put("message", validationMessage.toString());
				} else {
					statusDTO.setStatus("In-Progress");
					statusDTO.setCompanyId(companyId);
					statusDTO.setSso(sso);
					statusDTO.setFileName(fileName);
					statusDTO.setModuleName(CashCollectionDashboardConstants.GET_INSTALL_PROJECT_EXCEL_MODULE_NAME);
					tpsCashCollectionDashboardDAO.insertFileTrackingDetails(statusDTO);
					responseMap.put("status", "Success");
					responseMap.put("message", "");
				}
				if (validationStatus) {
					try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());) {
						trackId = tpsCashCollectionDashboardDAO.getFileTrackingId(statusDTO);
						statusDTO.setTrackingId(String.valueOf(trackId));
						log.info("Install Project Excel - Track ID :: " + trackId);
						processInstallProjectExcelFile(companyId, workbook, headerIndexMap, statusDTO);
					}
				}
			} else {
				log.error("Multipart File is empty or null");
				responseMap.put("status", "Error");
				responseMap.put("message", "Error while uploading install project excel file !!!");
				return responseMap;
			}
		} catch (Exception e) {
			log.error("Error while uploading project excel file :: " + e.getMessage());
			if (excelFile != null && !excelFile.isEmpty()) {
				statusDTO.setErrorMsg("Error");
				statusDTO.setStatus("Error");
				tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
			}
		}
		return responseMap;
	}

	private void processInstallProjectExcelFile(String companyId, XSSFWorkbook workbook,
			Map<Integer, String> headerIndexMap, FileUploadStatusDTO statusDTO) {

		Runnable caller1 = new Runnable() {
			@Override
			public void run() {
				Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
				boolean validationStatus = true;
				StringBuilder validationMessage = new StringBuilder();
				try {
					if (validationStatus) {
						headerIndexMap = getInstallProjectExcelHeaderColumnMap(companyId);
						validationStatus = validateTargetExcelColumnDetails(companyId, workbook, headerIndexMap,
								validationMessage, statusDTO);
					}
					if (!validationStatus) {
						// update tracking tbl as false (tracking id) create a new entry in error table
						statusDTO.setErrorMsg(validationMessage.toString());
						statusDTO.setStatus("Error");
						tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
					}
					if (validationStatus) {
						List<DownloadProjectTemplateDTO> list = new ArrayList<DownloadProjectTemplateDTO>();
						list = readInstallProjectDetailsExcelFile(workbook, headerIndexMap, statusDTO);
						tpsCashCollectionDashboardDAO.insertInstallProjectExcelStageData(list, statusDTO);
						tpsCashCollectionDashboardDAO.callFileUploadStageToTarget(statusDTO);
						callInstallCashCollectionProcedure();
					}
				} catch (Exception e) {
					// update tracking tbl as false (tracking id) create a new entry in error table
					log.error("Error while processing Install Project Details excel file :: " + e.getMessage());
					statusDTO.setErrorMsg("Error");
					statusDTO.setStatus("Error");
					tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
				}
			}
		};
		new Thread(caller1).start();
	}

	protected List<DownloadProjectTemplateDTO> readInstallProjectDetailsExcelFile(Workbook workbook,
			Map<Integer, String> headerIndexMap, FileUploadStatusDTO statusDTO) {
		List<DownloadProjectTemplateDTO> list = new ArrayList<DownloadProjectTemplateDTO>();
		try {
			Sheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
				DownloadProjectTemplateDTO dto = new DownloadProjectTemplateDTO();
				int colIx = 0;
				String cellValue = "";
				Cell cell = null;
				Row dataRow = sheet.getRow(rowNum);
				if (isRowEmpty(dataRow)) {
					continue;
				}
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInstallationJobNumber(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setPm(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setPmLeader(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setProjectName(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setContractCustomer(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setBusiness(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setSegment(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRegion(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInvoiceNumber(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRiskyOppty(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRiskComments(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInvoiceDate(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInvoiceDueDate(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setCollectionDate(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setCurrency(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInvoiceAmountCurr(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setOutstandingAmountCurr(cellValue);
				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setCashCollectedCurr(cellValue);
				list.add(dto);
			}
			log.info("Install Project Details list size :: " + list.size());
		} catch (Exception e) {
			log.error("Error while processing Install Project Details excel file :: " + e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			tpsCashCollectionDashboardDAO.updateFileTrackingDetails(statusDTO);
		}
		return list;
	}

	protected Map<Integer, String> getInstallProjectExcelHeaderColumnMap(String companyId) {
		Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
		headerIndexMap = tpsCashCollectionDashboardDAO.getInstallProjectExcelHeaderColumnMap(companyId);
		return headerIndexMap;
	}

	private void callCashCollectionProcedure() {
		Runnable caller1 = new Runnable() {
			@Override
			public void run() {
				try {
					tpsCashCollectionDashboardDAO.callCashCollectionProcedure();
				} catch (Exception e) {
					log.error("Error while calling cash collection procedure :: " + e.getMessage());
				}
			}
		};
		new Thread(caller1).start();
	}

	private void callInstallCashCollectionProcedure() {
		Runnable caller1 = new Runnable() {
			@Override
			public void run() {
				try {
					tpsCashCollectionDashboardDAO.callInstallCashCollectionProcedure();
				} catch (Exception e) {
					log.error("Error while calling install cash collection procedure :: " + e.getMessage());
				}
			}
		};
		new Thread(caller1).start();
	}

	public byte[] downloadCashDetailsForOFE(CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		String projectId = "", startDate = "", endDate = "";
		Map<String, String> dateMap = new HashMap<String, String>();
		CashCollectionDashboardExcel cashCollectionDashboardExcel = new CashCollectionDashboardExcel();
		try {
			projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			kpiValues.setProjectId(projectId);
			dateMap = getDateFromQuarter(kpiValues.getSelectedQuater(), kpiValues.getCurrentYearFlag());
			startDate = dateMap.get("startDate");
			endDate = dateMap.get("endDate");
			log.info("startDate :" + startDate + " : endDate : " + endDate);
			kpiValues.setStartDate(startDate);
			kpiValues.setEndDate(endDate);

			List<CashDetailDTO> cashDetails = ofeCashCollectionDashboardDAO
					.getCashCollectionDashboardCashListForOFE(kpiValues);
			log.info("Creating Cash Details Sheet with " + cashDetails.size() + " rows.");

			List<OFEBusinessSummaryDetailsDTO> businessDetails = ofeCashCollectionDashboardDAO
					.getBusinessUnitSummaryForOFE(kpiValues);
			log.info("Creating Business Details Sheet with " + businessDetails.size() + " rows.");

			List<OFEProjectDetailsDTO> projectList = ofeCashCollectionDashboardDAO
					.getCashCollectionDashboardProjectDetailForOFE(kpiValues);
			log.info("Creating Project Details Sheet with " + projectList.size() + " rows.");

			cashCollectionDashboardExcel.exportDetailsTableExcelForOFE(workbook, businessDetails, projectList,
					cashDetails);
			workbook.write(bos);
			excelData = bos.toByteArray();

		} catch (Exception e) {
			log.error("Error occured when downlaoding Excel file" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downlaoding Excel file" + e.getMessage());
			}
		}
		return excelData;
	}
}