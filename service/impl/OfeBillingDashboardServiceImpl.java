package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.*;
import com.bh.realtrack.excel.ExportOpenInvoicesExcel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.OfeBillingDashboardDAO;
import com.bh.realtrack.excel.ExportBillingDetailsExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.ICommonService;
import com.bh.realtrack.service.OfeBillingDashboardService;

@Service
public class OfeBillingDashboardServiceImpl implements OfeBillingDashboardService {

	private static final Logger log = LoggerFactory.getLogger(OfeBillingDashboardServiceImpl.class);

	@Autowired
	private OfeBillingDashboardDAO billingDashboardDAO;

	@Autowired
	private ICommonService commonService;

	@Autowired
	private CallerContext callerContext;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Map<String, Object> getBillingManageProjects(HeaderDashboardDetailsDTO headerDetails) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		String projectId = commonService.fetchFavProjects();

		if (null == projectId) {
			projectId = "0";
		}

		List<ManageProjectResponseDTO> manageProjectList = billingDashboardDAO.getmanageProjectList(headerDetails,
				projectId);
		data.put("manageProjectList", manageProjectList);

		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getBusinessUnitSummary(BillingDashboardFilterDTO filterValues) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> currentYear = null;
		currentYear = getDate(filterValues.getStartDate(), filterValues.getEndDate(),
				filterValues.getCurrentYearFlag());

		log.info("startDate :" + filterValues.getStartDate() + " : endDate : " + filterValues.getEndDate()
				+ ":startDate :" + currentYear.get("startDate") + " endDate :" + currentYear.get("endDate"));
		String startDate = currentYear.get("startDate");
		String endDate = currentYear.get("endDate");

		List<BusinessUnitSummaryDTO> businessUnitSummaryList = billingDashboardDAO.getBusinessUnitSummary(filterValues,
				startDate, endDate);
		log.info("businessUnitSummaryList=======================" + businessUnitSummaryList.size());

		data.put("businessUnitSummaryList", businessUnitSummaryList);

		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getBillingKpis(BillingDashboardFilterDTO filterValues) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> currentYear = null;
		currentYear = getDate(filterValues.getStartDate(), filterValues.getEndDate(),
				filterValues.getCurrentYearFlag());

		log.info("startDate :" + filterValues.getStartDate() + " : endDate : " + filterValues.getEndDate()
				+ ":startDate :" + currentYear.get("startDate") + " endDate :" + currentYear.get("endDate"));

		String startDate = currentYear.get("startDate");
		String endDate = currentYear.get("endDate");

		String financialBL = billingDashboardDAO.getFinancialBlKpi(filterValues, startDate, endDate);
		String lastEstimate = billingDashboardDAO.getLastEstimateKpi(filterValues, startDate, endDate);
		String actual = billingDashboardDAO.getActualKpi(filterValues, startDate, endDate);
		String togoVsFBL = billingDashboardDAO.getTogoVsFBLKpi(filterValues, startDate, endDate);
		String bmLinkage = billingDashboardDAO.getBmLinkageKpi(filterValues, startDate, endDate);
		String overdueKpi = billingDashboardDAO.getOverdueKpi(filterValues, startDate, endDate);
		String umatchedBL = billingDashboardDAO.getUnmatchedBLKpi(filterValues);

		data.put("financialBL", financialBL);
		data.put("lastEstimate", lastEstimate);
		data.put("actual", actual);
		data.put("togoVsFBL", togoVsFBL);
		data.put("bmLinkage", bmLinkage);
		data.put("overdueKpi", overdueKpi);
		data.put("umatchedBL", umatchedBL);

		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getBillingProjectList(BillingDashboardFilterDTO filterValues) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> currentYear = null;
		currentYear = getDate(filterValues.getStartDate(), filterValues.getEndDate(),
				filterValues.getCurrentYearFlag());

		log.info("startDate :" + filterValues.getStartDate() + " : endDate : " + filterValues.getEndDate()
				+ ":startDate :" + currentYear.get("startDate") + " endDate :" + currentYear.get("endDate"));
		String startDate = currentYear.get("startDate");
		String endDate = currentYear.get("endDate");

		List<OfeBillingProjectListDTO> projectList = billingDashboardDAO.getBillingProjectList(filterValues, startDate,
				endDate);
		List<OfeBillingMilestoneListDTO> milestoneList = billingDashboardDAO.getBillingMilestoneList(filterValues,
				startDate, endDate);

		log.info("projectList=============================" + projectList.size());
		log.info("milestoneList===========================" + milestoneList.size());

		data.put("projectList", projectList);
		data.put("milestoneList", milestoneList);

		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getBillingInvoiceList(BillingDashboardFilterDTO filterValues) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, String> currentYear = null;
		currentYear = getDate(filterValues.getStartDate(), filterValues.getEndDate(),
				filterValues.getCurrentYearFlag());

		log.info("startDate :" + filterValues.getStartDate() + " : endDate : " + filterValues.getEndDate()
				+ ":startDate :" + currentYear.get("startDate") + " endDate :" + currentYear.get("endDate"));
		String startDate = currentYear.get("startDate");
		String endDate = currentYear.get("endDate");

		List<OfeBillingInvoiceListDTO> invoiceList = billingDashboardDAO.getBillingInvoiceList(filterValues, startDate,
				endDate);

		log.info("invoiceList===========================" + invoiceList.size());

		data.put("invoiceList", invoiceList);

		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getBillingCurveList(BillingDashboardFilterDTO filterValues) throws Exception {

		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<OfeCurveResponseDTO> forecastList = new ArrayList<OfeCurveResponseDTO>();
		List<OfeCurveResponseDTO> secondforecastList = new ArrayList<OfeCurveResponseDTO>();
		List<OfeCurveResponseDTO> actual = new ArrayList<OfeCurveResponseDTO>();
		List<OfeCurveResponseDTO> secondactual = new ArrayList<OfeCurveResponseDTO>();
		List<OfeCurveResponseDTO> baseline = new ArrayList<OfeCurveResponseDTO>();
		List<OfeCurveResponseDTO> secondbaseline = new ArrayList<OfeCurveResponseDTO>();
		List<String> weekly = new ArrayList<String>();
		String projectId = commonService.fetchFavProjects();

		if (null == projectId) {
			projectId = "0";
		}

		try {
			Map<String, String> currentYear = null;

			currentYear = getDate(filterValues.getStartDate(), filterValues.getEndDate(),
					filterValues.getCurrentYearFlag());

			log.info("startDate :" + filterValues.getStartDate() + " : endDate : " + filterValues.getEndDate()
					+ ":startDate :" + currentYear.get("startDate") + " endDate :" + currentYear.get("endDate"));
			String startDate = currentYear.get("startDate");
			String endDate = currentYear.get("endDate");

			actual = billingDashboardDAO.getActualCurveList(filterValues, startDate, endDate);
			forecastList = billingDashboardDAO.getForecastCurveList(filterValues, startDate, endDate);
			baseline = billingDashboardDAO.getFinancialBlCurveList(filterValues, startDate, endDate);

			weekly = billingDashboardDAO.getWeeks(filterValues, startDate, endDate);

			Collections.sort(actual, new Comparator<OfeCurveResponseDTO>() {
				@Override
				public int compare(OfeCurveResponseDTO p1, OfeCurveResponseDTO p2) {
					int campusResult = p1.getWeekly().split("-")[2].compareTo(p2.getWeekly().split("-")[2]);
					// first by year
					if (campusResult != 0) {
						return campusResult;
					}

					// Next by week
					return p1.getWeekly().split("-")[1].compareTo(p2.getWeekly().split("-")[1]);
				}
			});

			Collections.sort(forecastList, new Comparator<OfeCurveResponseDTO>() {
				@Override
				public int compare(OfeCurveResponseDTO p1, OfeCurveResponseDTO p2) {
					int campusResult = p1.getWeekly().split("-")[2].compareTo(p2.getWeekly().split("-")[2]);
					// first by year
					if (campusResult != 0) {
						return campusResult;
					}

					// Next by week
					return p1.getWeekly().split("-")[1].compareTo(p2.getWeekly().split("-")[1]);
				}
			});

			Collections.sort(baseline, new Comparator<OfeCurveResponseDTO>() {
				@Override
				public int compare(OfeCurveResponseDTO p1, OfeCurveResponseDTO p2) {
					int campusResult = p1.getWeekly().split("-")[2].compareTo(p2.getWeekly().split("-")[2]);
					// first by year
					if (campusResult != 0) {
						return campusResult;
					}

					// Next by week
					return p1.getWeekly().split("-")[1].compareTo(p2.getWeekly().split("-")[1]);
				}
			});

			Collections.sort(weekly, new Comparator<String>() {
				@Override
				public int compare(String p1, String p2) {
					int campusResult = p1.split("-")[2].compareTo(p2.split("-")[2]);
					// first by year
					if (campusResult != 0) {
						return campusResult;
					}

					// Next by week
					return p1.split("-")[1].compareTo(p2.split("-")[1]);
				}
			});

			boolean isPresent = false;
			boolean doneOnce = false;
			long count = 0;
			long weeklySize = weekly.size();
			boolean actualEnds = false;

			Double kthvalue = 0d;
			if (weeklySize > 0) {
				for (int k = 0; k < weeklySize; k++) {

					if (actual.size() != 0) {

						for (OfeCurveResponseDTO a : actual) {
							if (weekly.get(k).equalsIgnoreCase(a.getWeekly())) {
								if (weekly.get(k).equalsIgnoreCase(actual.get(actual.size() - 1).getWeekly())) {
									actualEnds = true;
								}
								isPresent = true;
								kthvalue = a.getCumDispAmount();

							}

						}
						if (!actualEnds) {
							if (isPresent) {

							} else {

								isPresent = false;

								if (k < actual.size()) {

									int weeksDate = Integer.parseInt(weekly.get(k).split("-")[1]);
									int actualsDate = Integer.parseInt(actual.get(k).getWeekly().split("-")[1]);
									int weeksYear = Integer.parseInt(weekly.get(k).split("-")[2]);
									int actualsYear = Integer.parseInt(actual.get(k).getWeekly().split("-")[2]);
									if (((weeksYear == actualsYear) && (weeksDate < actualsDate))
											|| (weeksYear < actualsYear)) {
										doneOnce = true;

										OfeCurveResponseDTO actualCurveDTO = new OfeCurveResponseDTO();
										actualCurveDTO.setWeekly(weekly.get(k));
										actualCurveDTO.setCumDispAmount(kthvalue);

										secondactual.add(actualCurveDTO);
									}
								}
								if (!doneOnce) {

									OfeCurveResponseDTO actualCurveDTO = new OfeCurveResponseDTO();
									actualCurveDTO.setWeekly(weekly.get(k));
									actualCurveDTO.setCumDispAmount(kthvalue);

									secondactual.add(actualCurveDTO);

								}
								doneOnce = false;
							}
						}
						isPresent = false;
					}
				}
			}

			for (OfeCurveResponseDTO a : secondactual) {
				actual.add(a);
			}

			Collections.sort(actual, new Comparator<OfeCurveResponseDTO>() {
				@Override
				public int compare(OfeCurveResponseDTO p1, OfeCurveResponseDTO p2) {
					int campusResult = p1.getWeekly().split("-")[2].compareTo(p2.getWeekly().split("-")[2]);
					// first by year
					if (campusResult != 0) {
						return campusResult;
					}

					// Next by week
					return p1.getWeekly().split("-")[1].compareTo(p2.getWeekly().split("-")[1]);
				}
			});

			// ____________________________________________________________________________________________________________________

			boolean isPresent3 = false;
			boolean doneOnce3 = false;
			long count3 = 0;
			long weeklySize3 = weekly.size();

			Double kthvalue3 = 0d;
			if (weeklySize3 > 0) {
				for (int k = 0; k < weeklySize3; k++) {

					if (baseline.size() != 0) {

						for (OfeCurveResponseDTO a : baseline) {
							if (weekly.get(k).equalsIgnoreCase(a.getWeekly())) {

								isPresent3 = true;
								kthvalue3 = a.getCumDispAmount();

							}

						}

						if (isPresent3) {

						} else {

							isPresent3 = false;

							if (k < baseline.size()) {

								int weeksDate = Integer.parseInt(weekly.get(k).split("-")[1]);
								int actualsDate = Integer.parseInt(baseline.get(k).getWeekly().split("-")[1]);
								int weeksYear = Integer.parseInt(weekly.get(k).split("-")[2]);
								int actualsYear = Integer.parseInt(baseline.get(k).getWeekly().split("-")[2]);
								if (((weeksYear == actualsYear) && (weeksDate < actualsDate))
										|| (weeksYear < actualsYear)) {
									doneOnce3 = true;

									OfeCurveResponseDTO actualCurveDTO = new OfeCurveResponseDTO();
									actualCurveDTO.setWeekly(weekly.get(k));
									actualCurveDTO.setCumDispAmount(kthvalue3);

									secondbaseline.add(actualCurveDTO);
								}
							}
							if (!doneOnce3) {

								OfeCurveResponseDTO baselineCurveDTO = new OfeCurveResponseDTO();
								baselineCurveDTO.setWeekly(weekly.get(k));
								baselineCurveDTO.setCumDispAmount(kthvalue3);

								secondbaseline.add(baselineCurveDTO);

							}
							doneOnce3 = false;
						}

					} else {
						isPresent3 = false;

						OfeCurveResponseDTO baselineCurveDTO = new OfeCurveResponseDTO();
						baselineCurveDTO.setWeekly(weekly.get(k));
						baselineCurveDTO.setCumDispAmount(0d);

						secondbaseline.add(baselineCurveDTO);

					}
					isPresent3 = false;
				}
			}

			for (OfeCurveResponseDTO a : secondbaseline) {
				baseline.add(a);
			}

			Collections.sort(baseline, new Comparator<OfeCurveResponseDTO>() {
				@Override
				public int compare(OfeCurveResponseDTO p1, OfeCurveResponseDTO p2) {
					int campusResult = p1.getWeekly().split("-")[2].compareTo(p2.getWeekly().split("-")[2]);
					// first by year
					if (campusResult != 0) {
						return campusResult;
					}

					// Next by week
					return p1.getWeekly().split("-")[1].compareTo(p2.getWeekly().split("-")[1]);
				}
			});
			// _____________________________________________________________________________________________________________________

			boolean isPresent2 = false;
			boolean doneOnce2 = false;
			long count2 = 0;
			long weeklySize2 = weekly.size();

			Double kthvalue2 = 0d;
			if (weeklySize2 > 0) {
				for (int k = 0; k < weeklySize2; k++) {

					if (forecastList.size() != 0) {

						for (OfeCurveResponseDTO a : forecastList) {
							if (weekly.get(k).equalsIgnoreCase(a.getWeekly())) {
								isPresent2 = true;
								kthvalue2 = a.getCumDispAmount();

							}

						}

						if (isPresent2) {

						} else {

							isPresent2 = false;

							if (k < forecastList.size()) {

								int weeksDate = Integer.parseInt(weekly.get(k).split("-")[1]);
								int actualsDate = Integer.parseInt(forecastList.get(k).getWeekly().split("-")[1]);
								int weeksYear = Integer.parseInt(weekly.get(k).split("-")[2]);
								int actualsYear = Integer.parseInt(forecastList.get(k).getWeekly().split("-")[2]);
								if (((weeksYear == actualsYear) && (weeksDate < actualsDate))
										|| (weeksYear < actualsYear)) {
									doneOnce2 = true;

									OfeCurveResponseDTO forecastCurveDTO = new OfeCurveResponseDTO();
									forecastCurveDTO.setWeekly(weekly.get(k));
									forecastCurveDTO.setCumDispAmount(kthvalue2);

									secondforecastList.add(forecastCurveDTO);
								}
							}
							if (!doneOnce2) {

								OfeCurveResponseDTO forecastCurveDTO = new OfeCurveResponseDTO();
								forecastCurveDTO.setWeekly(weekly.get(k));
								forecastCurveDTO.setCumDispAmount(kthvalue2);

								secondforecastList.add(forecastCurveDTO);

							}
							doneOnce2 = false;
						}
					} else {
						isPresent2 = false;

						OfeCurveResponseDTO forecastCurveDTO = new OfeCurveResponseDTO();
						forecastCurveDTO.setWeekly(weekly.get(k));
						forecastCurveDTO.setCumDispAmount(0d);

						secondforecastList.add(forecastCurveDTO);

					}
					isPresent2 = false;
				}
			}

			for (OfeCurveResponseDTO a : secondforecastList) {
				forecastList.add(a);
			}

			Collections.sort(forecastList, new Comparator<OfeCurveResponseDTO>() {
				@Override
				public int compare(OfeCurveResponseDTO p1, OfeCurveResponseDTO p2) {
					int campusResult = p1.getWeekly().split("-")[2].compareTo(p2.getWeekly().split("-")[2]);
					// first by year
					if (campusResult != 0) {
						return campusResult;
					}

					// Next by week
					return p1.getWeekly().split("-")[1].compareTo(p2.getWeekly().split("-")[1]);
				}
			});

			responseMap.put("forecastCurve", forecastList);
			responseMap.put("actualCurve", actual);
			responseMap.put("baselineCurve", baseline);
			responseMap.put("weekly", weekly);

		} catch (Exception e) {
			log.error("getBillingReport(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	public static Map<String, String> getDate(String startDate, String endDate, String currentYearFlag)
			throws Exception {
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
	public String getEditAccess(String sso) {

		String editAccessFlag = "false";
		try {
			if (sso != null) {
				editAccessFlag = billingDashboardDAO.getEditAccess(sso);

			} else {
				throw new Exception("Error getting edit access for : " + sso);
			}
		} catch (Exception e) {
			log.error("getEditAccess(): Exception occurred : " + e.getMessage());
		}
		return editAccessFlag;
	}

	@Override
	public Map<String, Object> getProjectTargetList(HeaderDashboardDetailsDTO header) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<ProjectTargetDTO> projectTargetList = null;
		try {
			String projectId = commonService.fetchFavProjects();

			if (null == projectId) {
				projectId = "0";
			}

			projectTargetList = billingDashboardDAO.getProjectTargetList(header, projectId);

		} catch (Exception e) {
			log.error("getEditAccess(): Exception occurred : " + e.getMessage());
		}
		data.put("projectTargetList", projectTargetList);

		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> editProjectTargetList(ProjectTargetDTO targetDto) {
		int result = 0;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String sso = callerContext.getName();
		try {
			log.info("Project Id - " + targetDto.getProjectId());
			log.info("Project Target Day - " + targetDto.getProjectTargetDay());
			log.info("Project Payment Term - " + targetDto.getProjectPaymentTerm());
			log.info("Monthly Billing Slot - " + targetDto.getMonthlyBillingSlot());
			log.info("Cur Qtr Billing Target - " + targetDto.getCurQtrBillingTarget());
			log.info("Cur Qt Collection Target - " + targetDto.getCurQtrCollectionTarget());
			log.info("Tax Withholding Percentage - " + targetDto.getTaxWithholdingPercentage());

			result = billingDashboardDAO.editProjectTargetList(targetDto, sso);
			log.info("result : "+ result);
			if (result == 1) {
				responseMap.put("status", "success");
			} else {
				responseMap.put("status", "Error");
			}

		} catch (Exception e) {
			responseMap.put("status", "Error");
		}

		return responseMap;
	}

	@Override
	public Map<String, Object> getTrendChart(TrendChartRequestDTO header) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<TrendChartResponseDTO> trendChartList = null;
		try {

			Map<String, String> currentYear = null;
			currentYear = getDate(header.getStartDate(), header.getEndDate(), header.getCurrentYearFlag());

			log.info("startDate :" + header.getStartDate() + " : endDate : " + header.getEndDate() + ":startDate :"
					+ currentYear.get("startDate") + " endDate :" + currentYear.get("endDate"));
			String startDate = currentYear.get("startDate");
			String endDate = currentYear.get("endDate");
			trendChartList = billingDashboardDAO.getTrendChart(header, startDate, endDate);

		} catch (Exception e) {
			log.error("getTrendChart(): Exception occurred : " + e.getMessage());
		}
		data.put("trendChartList", trendChartList);

		response.put("data", data);
		return response;
	}

	@Override
	public byte[] getProjectTargetExcel(int companyId, String business, String segment, int customerId, String region) {
					
			//
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			XSSFWorkbook workbook = new XSSFWorkbook();
			byte[] excelData = null;
			ExportBillingDetailsExcel exportBillingDetailsExcel = new ExportBillingDetailsExcel();
			
			List<ProjectTargetDTO> projectTargetList = new ArrayList<ProjectTargetDTO>();
			String projectId = commonService.fetchFavProjects();

			if(null == projectId ) {
				projectId = "0";
			}

			try {
				HeaderDashboardDetailsDTO header = new HeaderDashboardDetailsDTO();
				header.setCompanyId(companyId);
				header.setBusiness(business);
				header.setSegment(segment);
				header.setRegion(region);
				header.setCustomerId(customerId);
					
				projectTargetList = billingDashboardDAO.getProjectTargetList(header, projectId);
				
				
				exportBillingDetailsExcel.exportTargetProjectExcel(workbook, projectTargetList);
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
	public Map<String, Object> getOpenInvoiceChart(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> chartMap = new HashMap<String, Object>();
		String updateOnDate = "";
		try {
			if (projectId != null) {
				chartMap = billingDashboardDAO.getOpenInvoiceChart(projectId);
				updateOnDate = billingDashboardDAO.getLastUpdatedDate(projectId);
				chartMap.put("updatedOn", updateOnDate);
				responseMap.put("responseData", chartMap);
			} else {
				throw new Exception("getOpenInvoiceChart(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("getOpenInvoiceChart(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getOpenInvoiceChartPopupDetails(String projectId, String chartType, String statusCode) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<OfeOpenInvoiceChartPopupDetails> openInvoiceDetailsPopup = new ArrayList<OfeOpenInvoiceChartPopupDetails>();
		try {
			if (projectId != null) {
				openInvoiceDetailsPopup = billingDashboardDAO.getOpenInvoiceChartPopupDetails(projectId, chartType,
						statusCode);
				responseMap.put("popup", openInvoiceDetailsPopup);
			} else {
				throw new Exception("getOpenInvoiceChartPopupDetails(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("getOpenInvoiceChartPopupDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getOpenInvoiceDatatable(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<OFEOpenInvoiceDataTableDTO> tableDetails = new ArrayList<OFEOpenInvoiceDataTableDTO>();
		try {
			if (projectId != null) {
				tableDetails = billingDashboardDAO.getOpenInvoiceDatatable(projectId);
				responseMap.put("dataTable", tableDetails);
			} else {
				throw new Exception("getOpenInvoiceDatatable(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("getOpenInvoiceDatatable(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> saveOpenInvoiceDetails(List<OFESaveOpenInvoiceDTO> invoicesList) {
		boolean resultFlag = false;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String sso = callerContext.getName();
		try {
			resultFlag = billingDashboardDAO.saveOpenInvoiceDetails(invoicesList, sso);
			if (resultFlag) {
				responseMap.put("status", "success");
				responseMap.put("message", "Data saved successfully.");
			} else {
				responseMap.put("status", "Error");
				responseMap.put("message", "Error in saving data.");
			}
		} catch (Exception e) {
			responseMap.put("status", "Error");
			responseMap.put("message", "Error in saving data.");
		}
		return responseMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCashCollectionReportCurve(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> cashCollectionCurveMap = new HashMap<String, Object>();
		List<OFEForecastCashDTO> forecastCashCurve = new ArrayList<OFEForecastCashDTO>();
		List<OFECollectedCashDTO> collectedCashCurve = new ArrayList<OFECollectedCashDTO>();
		List<OFEOtrCashBaselineDTO> otrCashBaselineCurve = new ArrayList<OFEOtrCashBaselineDTO>();
		List<OFEPastDueCommitmentDTO> pastDueCurve = new ArrayList<OFEPastDueCommitmentDTO>();
		List<OFECashCollectionCurveTableDTO> tableList = new ArrayList<OFECashCollectionCurveTableDTO>();
		List<String> xAxis = new ArrayList<String>();
		String forecastCash = "", collectedCash = "", otrCashBaseline = "", pastDue = "";
		String lastSavedDate = "";
		try {
			if (projectId != null) {
				cashCollectionCurveMap = billingDashboardDAO.getForecastCashCurve(projectId, cashCollectionCurveMap);
				cashCollectionCurveMap = billingDashboardDAO.getCollectedCashCurve(projectId, cashCollectionCurveMap);
				cashCollectionCurveMap = billingDashboardDAO.getOtrCashBaselineCurve(projectId, cashCollectionCurveMap);
				cashCollectionCurveMap = billingDashboardDAO.getPastDueCurve(projectId, cashCollectionCurveMap);
				lastSavedDate = billingDashboardDAO.getOpenInvoiceLastSavedDate(projectId);

				if (cashCollectionCurveMap.get("forecastCash") != null) {
					forecastCashCurve = (List<OFEForecastCashDTO>) cashCollectionCurveMap.get("forecastCash");
				}
				if (cashCollectionCurveMap.get("collectedCash") != null) {
					collectedCashCurve = (List<OFECollectedCashDTO>) cashCollectionCurveMap.get("collectedCash");
				}
				if (cashCollectionCurveMap.get("otrCashBaseline") != null) {
					otrCashBaselineCurve = (List<OFEOtrCashBaselineDTO>) cashCollectionCurveMap.get("otrCashBaseline");
				}
				if (cashCollectionCurveMap.get("pastDue") != null) {
					pastDueCurve = (List<OFEPastDueCommitmentDTO>) cashCollectionCurveMap.get("pastDue");
				}

				if (cashCollectionCurveMap.get("tableList") != null) {
					tableList = (List<OFECashCollectionCurveTableDTO>) cashCollectionCurveMap.get("tableList");
					for (OFECashCollectionCurveTableDTO obj : tableList) {
						xAxis.add(obj.getDisplayDate());

						if (obj.getForecastCash() != null && !obj.getForecastCash().equalsIgnoreCase("")) {
							forecastCash = obj.getForecastCash();
						} else {
							obj.setForecastCash(forecastCash);
						}

						if (obj.getCollectedCash() != null && !obj.getCollectedCash().equalsIgnoreCase("")) {
							collectedCash = obj.getCollectedCash();
						} else {
							obj.setCollectedCash(collectedCash);
						}

						if (obj.getOtrCashBaseline() != null && !obj.getOtrCashBaseline().equalsIgnoreCase("")) {
							otrCashBaseline = obj.getOtrCashBaseline();
						} else {
							obj.setOtrCashBaseline(otrCashBaseline);
						}

					}
				}

				responseMap.put("forecastCash", forecastCashCurve);
				responseMap.put("collectedCash", collectedCashCurve);
				responseMap.put("otrCashBaseline", otrCashBaselineCurve);
				responseMap.put("pastDue", pastDueCurve);
				responseMap.put("xAxis", xAxis);
				responseMap.put("tableList", tableList);
				responseMap.put("lastSavedDate", lastSavedDate);
			} else {
				throw new Exception("getCashCollectionReportCurve(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			responseMap.put("forecastCash", forecastCashCurve);
			responseMap.put("collectedCash", collectedCashCurve);
			responseMap.put("otrCashBaseline", otrCashBaselineCurve);
			responseMap.put("pastDue", pastDueCurve);
			responseMap.put("xAxis", xAxis);
			responseMap.put("tableList", tableList);
			responseMap.put("lastSavedDate", lastSavedDate);
			log.error("getCashCollectionReportCurve(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] getOpenInvoiceDatatableExcel(String projectId) {

		//
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		ExportBillingDetailsExcel exportBillingDetailsExcel = new ExportBillingDetailsExcel();

		List<OFEOpenInvoiceDataTableDTO> tableDetails = new ArrayList<OFEOpenInvoiceDataTableDTO>();
		
		try {
			if (projectId.isEmpty() == false) {
				tableDetails = billingDashboardDAO.getOpenInvoiceDatatable(projectId);

				exportBillingDetailsExcel.exportOpenInvoiceDatatableExcel(workbook, tableDetails);
				workbook.write(bos);
				excelData = bos.toByteArray();
			}
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
	public Map<String, Object> getForecastChart(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ForecastChartDTO> forecastChartDTO = new ArrayList<ForecastChartDTO>();
		try {
			if (projectId != null) {
				forecastChartDTO = billingDashboardDAO.getForecastChart(projectId);
				responseMap.put("responseData", forecastChartDTO);
			} else {
				throw new Exception("getOpenInvoiceChart(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("getOpenInvoiceChart(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] getForecastExcel(String projectId) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		ExportBillingDetailsExcel exportBillingDetailsExcel = new ExportBillingDetailsExcel();

		List<ForecastChartDTO> tableDetails = new ArrayList<ForecastChartDTO>();
		
		try {
			if (projectId.isEmpty() == false) {
				tableDetails = billingDashboardDAO.getForecastChart(projectId);

				exportBillingDetailsExcel.exportForecastChartExcel(workbook, tableDetails);
				workbook.write(bos);
				excelData = bos.toByteArray();
			}
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
	public String getEditAccessBillingWidget(String sso) {

		String editAccessFlag = "false";
		try {
			if (sso != null) {
				editAccessFlag = billingDashboardDAO.getEditAccessBillingWidget(sso);

			} else {
				throw new Exception("Error getting edit access for : " + sso);
			}
		} catch (Exception e) {
			log.error("getEditAccess(): Exception occurred : " + e.getMessage());
		}
		return editAccessFlag;
	}

	@Override
	public Map<String, Object> editInvoiceList(List<InvoiceListDTO> invoiceDto) {
		int result = 0;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String sso = callerContext.getName();
		try {
			result = billingDashboardDAO.editInvoiceList(invoiceDto, sso);
			if (result == 1) {
				responseMap.put("status", "success");
			} else {
				responseMap.put("status", "Error");
			}

		} catch (Exception e) {
			responseMap.put("status", "Error");
		}

		return responseMap;
	}

	@Override
	public Map<String, Object> editMilestoneList(List<MilestoneListDTO> milestoneDto) {
		int result = 0;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String sso = callerContext.getName();
		try {
			result = billingDashboardDAO.editMilestoneList(milestoneDto, sso);
			if (result == 1) {
				responseMap.put("status", "success");
			} else {
				responseMap.put("status", "Error");
			}

		} catch (Exception e) {
			responseMap.put("status", "Error");
		}

		return responseMap;
	}

	@Override
	public Map<String,Object> getBMVLastRefreshDate() {
		Map<String,Object> map = new HashMap<String,Object>();
		
		String lastDate = billingDashboardDAO.getBMVLastRefreshDate();
		
		map.put("lastUpdatedDate", lastDate);
		
		return map;
	}

	@Override
	public Map<String, Object> getScurvefilters(String projectId, String published) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> functionCode= new ArrayList<String>();
		List<DropDownDTO> productLines= new ArrayList<DropDownDTO>();
		List<String> projectCodes= new ArrayList<String>();
		
		try {
			functionCode = billingDashboardDAO.getScurveFunctionCode(projectId, published);
			productLines = billingDashboardDAO.getScurveProductLineCode(projectId, published);
			projectCodes = billingDashboardDAO.getScurveProjectCode(projectId, published);

		}catch(Exception e) {
			log.error("Exception in fetching Scurve Filters"+e.getMessage());
		}
		
		data.put("functions",functionCode);
		data.put("productLines",productLines);
		data.put("projectCodes",projectCodes);
		response.put("data",data);
		return response;
	}

	@Override
	public DashboardCountDTO getDashboardColorCount(int customerId,int companyId, String warrantyFlag) {
		String sso = callerContext.getName();
		log.info("SSO"+sso + "Warranty Flag"+warrantyFlag);
		
		DashboardCountDTO dto = new DashboardCountDTO();
		
		if(warrantyFlag.equals("N")) {
			dto= billingDashboardDAO.getDashboardColorCount(sso,customerId,companyId,warrantyFlag);
		}else {
			dto= billingDashboardDAO.getDashboardWarrantyFlagCount(sso,customerId,companyId,"N");
		}
    
	return dto;
		
	}

	@Override
	public byte[] downloadOpenInvoiceChartPopupDetails(String projectId, String chartType, String statusCode) {
		ExportOpenInvoicesExcel exportOpenInvoicesExcel = new ExportOpenInvoicesExcel();
		List<OfeOpenInvoiceChartPopupDetails> openInvoiceChartPopupDetailsList = new ArrayList<OfeOpenInvoiceChartPopupDetails>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		try {
			if (projectId != null) {
				openInvoiceChartPopupDetailsList = billingDashboardDAO.getOpenInvoiceChartPopupDetails(projectId, chartType,
						statusCode);
				log.info("Creating Open Invoices Sheet with " + openInvoiceChartPopupDetailsList.size() + " rows.");
				exportOpenInvoicesExcel.exportOpenInvoicesChartPopupExcel(workbook, openInvoiceChartPopupDetailsList);
				workbook.write(bos);
				excelData = bos.toByteArray();
			}
		} catch (Exception e) {
			log.error("Error occured when downloading Open Invoices Chart popup details" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading Open Invoices Chart popup details" + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getInvoiceDropDownList(String projectId) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<InvoiceDropdownListDTO> list = new ArrayList<>();
		list = billingDashboardDAO.getInvoiceDropDownList(projectId);
		data.put("invoiceList",list);
		return data;
	}
}