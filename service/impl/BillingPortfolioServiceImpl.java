package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.bh.realtrack.dao.IBillingPortfolioDAO;
import com.bh.realtrack.dto.ActivitiesDTO;
import com.bh.realtrack.dto.ActualCurveDTO;
import com.bh.realtrack.dto.BaselineCurveDTO;
import com.bh.realtrack.dto.BillingCycleDTO;
import com.bh.realtrack.dto.BillingCycleRemarksDTO;
import com.bh.realtrack.dto.BillingKpiDTO;
import com.bh.realtrack.dto.BillingLinearityChartDTO;
import com.bh.realtrack.dto.BillingLinearityChartPopupDTO;
import com.bh.realtrack.dto.BillingMilestoneListDTO;
import com.bh.realtrack.dto.BillingProjectListDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.ExceptionCategoryDTO;
import com.bh.realtrack.dto.ExceptionDTO;
import com.bh.realtrack.dto.ExceptionRemarksDTO;
import com.bh.realtrack.dto.FileUploadStatusDTO;
import com.bh.realtrack.dto.ForecastCurveDTO;
import com.bh.realtrack.dto.LastSuccessfulUpdateDetailsDTO;
import com.bh.realtrack.dto.LastUpdateDetailsDTO;
import com.bh.realtrack.dto.PercentageDTO;
import com.bh.realtrack.dto.SegmentSummaryDTO;
import com.bh.realtrack.dto.TPSBillingOutOfRTProjectDetailsDTO;
import com.bh.realtrack.dto.UpdateDetailsDTO;
import com.bh.realtrack.dto.ViewInvoiceDTO;
import com.bh.realtrack.excel.ExportBillingDetailsExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.pdf.ExportBillingDashboardDetailsPdf;
import com.bh.realtrack.service.IBillingPortfolioService;
import com.bh.realtrack.service.ICommonService;
import com.bh.realtrack.util.AssertUtils;
import com.bh.realtrack.util.BillingPortfolioConstants;

@Service
public class BillingPortfolioServiceImpl implements IBillingPortfolioService {

	private static final Logger log = LoggerFactory.getLogger(BillingPortfolioServiceImpl.class);

	private IBillingPortfolioDAO iBillingPortfolioDAO;
	private CallerContext callerContext;
	private ICommonService commonService;

	@Autowired
	public BillingPortfolioServiceImpl(CallerContext callerContext, IBillingPortfolioDAO iBillingPortfolioDAO,
			ICommonService commonService) {
		this.iBillingPortfolioDAO = iBillingPortfolioDAO;
		this.callerContext = callerContext;
		this.commonService = commonService;
	}

	@Override
	public Map<String, Object> getBillingDropDown(int customerId, int companyId, String business, String segment,
			String region) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<String> pmLeaderList = new ArrayList<String>();
		List<String> spmList = new ArrayList<String>();
		List<String> financialSegmentList = new ArrayList<String>();
		try {
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (AssertUtils.validateCheck(business) && AssertUtils.validateCheck(segment)
					&& AssertUtils.validateInt(companyId)) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, "0", "0", "0");
				pmLeaderList = iBillingPortfolioDAO.getPmLeaderDropDown(projectId);
				spmList = iBillingPortfolioDAO.getSpmDropDown(projectId);
				financialSegmentList = iBillingPortfolioDAO.getFinancialSegmentDropDown(projectId);
			}
		} catch (Exception e) {
			log.error("getBillingDropDown(): Exception occurred : {}" , e.getMessage());
		}
		responseMap.put("pmLeader", pmLeaderList);
		responseMap.put("spm", spmList);
		responseMap.put("financialSegmentList", financialSegmentList);
		return responseMap;
	}

	@Override
	public Map<String, Object> getBillingCount(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		try {
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (AssertUtils.validateCheck(business) && AssertUtils.validateCheck(segment)
					&& AssertUtils.validateCheck(pmLeader) && AssertUtils.validateCheck(spm)
					&& AssertUtils.validateInt(companyId)) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				responseMap = iBillingPortfolioDAO.getBillingDashOverAllSummary(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
			} else {
				responseMap.put("financialBlcount", "No data found");
				responseMap.put("forecastCount", "No data found");
				responseMap.put("actualCount", "No data found");
				responseMap.put("variationCount", "No data found");
				responseMap.put("financialITOBlcount", "No data found");
			}
		} catch (Exception e) {
			log.error("getBillingCount(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getSegmentSummary(int customerId, int companyId, String business, String region,
			String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<SegmentSummaryDTO> segmentList = new ArrayList<SegmentSummaryDTO>();
		try {
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && pmLeader.isEmpty() == false
					&& spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, "0", customerId, region, projectId, pmLeader, spm,
						financialSegment);
				segmentList = iBillingPortfolioDAO.getSegmentSummary(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				responseMap.put("segmentList", segmentList);
			} else {
				responseMap.put("segmentList", "No data found");
			}
		} catch (Exception e) {
			log.error("getSegmentSummary(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getAllBillingTab(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<BillingProjectListDTO> projectList = new ArrayList<BillingProjectListDTO>();
		List<BillingMilestoneListDTO> milestoneList = null;
		List<ExceptionCategoryDTO> categoryList = new ArrayList<ExceptionCategoryDTO>();
		try {
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				projectList = iBillingPortfolioDAO.getBillingProjectList(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				milestoneList = iBillingPortfolioDAO.getBillingMilestoneList(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				categoryList = getToBillCategory(milestoneList);
				responseMap.put("milestoneList", milestoneList);
				responseMap.put("projectList", projectList);
				responseMap.put("toBillCategory", categoryList);
			} else {
				responseMap.put("milestoneList", "No data found");
				responseMap.put("projectList", "No data found");
				responseMap.put("toBillCategory", categoryList);
			}
		} catch (Exception e) {
			log.error("getAllBillingTab(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	private List<ExceptionCategoryDTO> getToBillCategory(List<BillingMilestoneListDTO> milestoneList) {
		NumberFormat df = new DecimalFormat("#.##");
		String[] categoryApplicableArr = new String[] { "All Predecessors Completed", "No Predecessor Linked",
				"To Complete" };
		List<String> categoryApplicable = Arrays.asList(categoryApplicableArr);
		List<ExceptionCategoryDTO> categorylist = new ArrayList<ExceptionCategoryDTO>();
		Map<String, ExceptionCategoryDTO> compareMap = null;
		ExceptionCategoryDTO compareDTO = new ExceptionCategoryDTO();
		int noOfInvoice = 0, totalInvoice = 0;
		double amount = 0, totalAmount = 0;
		compareMap = new LinkedHashMap<String, ExceptionCategoryDTO>();
		for (BillingMilestoneListDTO obj : milestoneList) {
			String category = obj.getMilStatus();
			if (category != null && !category.equalsIgnoreCase("")) {
				if (!category.equalsIgnoreCase("INVOICED") && categoryApplicable.contains(category)) {
					compareDTO = (ExceptionCategoryDTO) compareMap.get(category);
					totalInvoice++;
					totalAmount = totalAmount + Double.parseDouble(obj.getAmountUsd());
					if (null == compareDTO) {
						compareDTO = new ExceptionCategoryDTO();
						compareDTO.setExceptionCategory(category);
						noOfInvoice = 1;
						amount = Double.parseDouble(obj.getAmountUsd());
						compareDTO.setNoOfInvoice(String.valueOf(noOfInvoice));
						compareDTO.setAmount(String.valueOf(df.format(amount)));
						compareMap.put(category, compareDTO);
					} else {
						noOfInvoice = Integer.parseInt(compareDTO.getNoOfInvoice()) + 1;
						amount = Double.parseDouble(compareDTO.getAmount()) + Double.parseDouble(obj.getAmountUsd());
						compareDTO.setNoOfInvoice(String.valueOf(noOfInvoice));
						compareDTO.setAmount(String.valueOf(df.format(amount)));
					}
				}
			}
		}
		categorylist.addAll(compareMap.values());
		for (ExceptionCategoryDTO obj : categorylist) {
			double noOfInvoicePercent = 0, amountPercent = 0;
			if (totalInvoice != 0 && noOfInvoice != 0) {
				noOfInvoicePercent = Double.valueOf(obj.getNoOfInvoice()) * 100 / Double.valueOf(totalInvoice);
			}
			if (totalInvoice != 0 && noOfInvoice != 0) {
				amountPercent = Double.valueOf(obj.getAmount()) * 100 / Double.valueOf(totalAmount);
			}
			obj.setNoOfInvoicePercent(String.valueOf(df.format(noOfInvoicePercent)));
			obj.setAmountPercent(String.valueOf(df.format(amountPercent)));
		}
		return categorylist;
	}

	public static Map<String, String> getDate(String startDate, String endDate, String currentYearFlag)
			throws Exception {
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM/yyyy");
		Calendar c = Calendar.getInstance();
		Date date, date1;
		Map<String, String> dateMap = new HashMap<String, String>();
		DateTime datetime = new DateTime();
		String year = datetime.toString("YYYY");
		if (currentYearFlag != null && currentYearFlag != "" && currentYearFlag.equalsIgnoreCase("Yes")) {
			startDate = "Jan/" + year;
			endDate = "Dec" + "/" + year;
			date = format2.parse(startDate);
			date1 = format2.parse(endDate);
			c.setTime(date1);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
			date = format2.parse(startDate);
			date1 = format2.parse(endDate);
			c.setTime(date1);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else {
			throw new Exception("No Date selected: ");
		}
		dateMap.put("startDate", format1.format(date));
		dateMap.put("endDate", format1.format(c.getTime()));
		return dateMap;
	}

	@Override
	public Map<String, Object> getBillingCurveTab(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<ForecastCurveDTO> forecastList = new ArrayList<ForecastCurveDTO>();
		List<ForecastCurveDTO> secondforecastList = new ArrayList<ForecastCurveDTO>();
		List<ActualCurveDTO> actual = new ArrayList<ActualCurveDTO>();
		List<ActualCurveDTO> secondactual = new ArrayList<ActualCurveDTO>();
		List<BaselineCurveDTO> baseline = new ArrayList<BaselineCurveDTO>();
		List<BaselineCurveDTO> secondbaseline = new ArrayList<BaselineCurveDTO>();
		List<BaselineCurveDTO> itobaseline = new ArrayList<BaselineCurveDTO>();
		List<String> weekly = new ArrayList<String>();
		try {
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && pmLeader.isEmpty() == false
					&& segment.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				forecastList = iBillingPortfolioDAO.getForecastCurve(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"));
				actual = iBillingPortfolioDAO.getActualCurve(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"));
				baseline = iBillingPortfolioDAO.getBaselineCurve(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"));
				weekly = iBillingPortfolioDAO.getWeeks(projectId, dateMap.get("startDate"), dateMap.get("endDate"));
				itobaseline = iBillingPortfolioDAO.getITOBaselineCurve(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"));

				Collections.sort(actual, new Comparator<ActualCurveDTO>() {
					@Override
					public int compare(ActualCurveDTO p1, ActualCurveDTO p2) {
						int campusResult = p1.getWeekly().split("-")[2].compareTo(p2.getWeekly().split("-")[2]);
						// first by year
						if (campusResult != 0) {
							return campusResult;
						}

						// Next by week
						return p1.getWeekly().split("-")[1].compareTo(p2.getWeekly().split("-")[1]);
					}
				});

				Collections.sort(forecastList, new Comparator<ForecastCurveDTO>() {
					@Override
					public int compare(ForecastCurveDTO p1, ForecastCurveDTO p2) {
						int campusResult = p1.getWeekly().split("-")[2].compareTo(p2.getWeekly().split("-")[2]);
						// first by year
						if (campusResult != 0) {
							return campusResult;
						}

						// Next by week
						return p1.getWeekly().split("-")[1].compareTo(p2.getWeekly().split("-")[1]);
					}
				});

				Collections.sort(baseline, new Comparator<BaselineCurveDTO>() {
					@Override
					public int compare(BaselineCurveDTO p1, BaselineCurveDTO p2) {
						int campusResult = p1.getWeekly().split("-")[2].compareTo(p2.getWeekly().split("-")[2]);
						// first by year
						if (campusResult != 0) {
							return campusResult;
						}

						// Next by week
						return p1.getWeekly().split("-")[1].compareTo(p2.getWeekly().split("-")[1]);
					}
				});

				Collections.sort(itobaseline, new Comparator<BaselineCurveDTO>() {
					@Override
					public int compare(BaselineCurveDTO p1, BaselineCurveDTO p2) {
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
				long weeklySize = weekly.size();
				boolean actualEnds = false;

				Double kthvalue = 0d;
				if (weeklySize > 0) {
					for (int k = 0; k < weeklySize; k++) {

						if (actual.size() != 0) {

							for (ActualCurveDTO a : actual) {
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

											ActualCurveDTO actualCurveDTO = new ActualCurveDTO();
											actualCurveDTO.setWeekly(weekly.get(k));
											actualCurveDTO.setCumDispAmount(kthvalue);

											secondactual.add(actualCurveDTO);
										}
									}
									if (!doneOnce) {

										ActualCurveDTO actualCurveDTO = new ActualCurveDTO();
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

				for (ActualCurveDTO a : secondactual) {
					actual.add(a);
				}

				Collections.sort(actual, new Comparator<ActualCurveDTO>() {
					@Override
					public int compare(ActualCurveDTO p1, ActualCurveDTO p2) {
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
				long weeklySize3 = weekly.size();

				Double kthvalue3 = 0d;
				if (weeklySize3 > 0) {
					for (int k = 0; k < weeklySize3; k++) {

						if (baseline.size() != 0) {

							for (BaselineCurveDTO a : baseline) {
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

										BaselineCurveDTO actualCurveDTO = new BaselineCurveDTO();
										actualCurveDTO.setWeekly(weekly.get(k));
										actualCurveDTO.setCumDispAmount(kthvalue3);

										secondbaseline.add(actualCurveDTO);
									}
								}
								if (!doneOnce3) {

									BaselineCurveDTO baselineCurveDTO = new BaselineCurveDTO();
									baselineCurveDTO.setWeekly(weekly.get(k));
									baselineCurveDTO.setCumDispAmount(kthvalue3);

									secondbaseline.add(baselineCurveDTO);

								}
								doneOnce3 = false;
							}

						} else {
							isPresent3 = false;

							BaselineCurveDTO baselineCurveDTO = new BaselineCurveDTO();
							baselineCurveDTO.setWeekly(weekly.get(k));
							baselineCurveDTO.setCumDispAmount(0d);

							secondbaseline.add(baselineCurveDTO);

						}
						isPresent3 = false;
					}
				}

				for (BaselineCurveDTO a : secondbaseline) {
					baseline.add(a);
				}

				Collections.sort(baseline, new Comparator<BaselineCurveDTO>() {
					@Override
					public int compare(BaselineCurveDTO p1, BaselineCurveDTO p2) {
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
				long weeklySize2 = weekly.size();

				Double kthvalue2 = 0d;
				if (weeklySize2 > 0) {
					for (int k = 0; k < weeklySize2; k++) {

						if (forecastList.size() != 0) {

							for (ForecastCurveDTO a : forecastList) {
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

										ForecastCurveDTO forecastCurveDTO = new ForecastCurveDTO();
										forecastCurveDTO.setWeekly(weekly.get(k));
										forecastCurveDTO.setCumDispAmount(kthvalue2);

										secondforecastList.add(forecastCurveDTO);
									}
								}
								if (!doneOnce2) {

									ForecastCurveDTO forecastCurveDTO = new ForecastCurveDTO();
									forecastCurveDTO.setWeekly(weekly.get(k));
									forecastCurveDTO.setCumDispAmount(kthvalue2);

									secondforecastList.add(forecastCurveDTO);

								}
								doneOnce2 = false;
							}
						} else {
							isPresent2 = false;

							ForecastCurveDTO forecastCurveDTO = new ForecastCurveDTO();
							forecastCurveDTO.setWeekly(weekly.get(k));
							forecastCurveDTO.setCumDispAmount(0d);

							secondforecastList.add(forecastCurveDTO);

						}
						isPresent2 = false;
					}
				}

				for (ForecastCurveDTO a : secondforecastList) {
					forecastList.add(a);
				}

				Collections.sort(forecastList, new Comparator<ForecastCurveDTO>() {
					@Override
					public int compare(ForecastCurveDTO p1, ForecastCurveDTO p2) {
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
				responseMap.put("itoBaseline", itobaseline);

			} else {
				responseMap.put("forecastCurve", "No data found");
				responseMap.put("actualCurve", "No data found");
				responseMap.put("baselineCurve", "No data found");
				responseMap.put("weekly", "No data found");
				responseMap.put("itoBaseline", itobaseline);

			}

		} catch (Exception e) {
			log.error("getBillingReport(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getBillingCurrentGap(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) throws Exception {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		double currentGap;
		try {
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (AssertUtils.validateCheck(business) && AssertUtils.validateCheck(segment)
					&& AssertUtils.validateCheck(pmLeader) && AssertUtils.validateCheck(spm)
					&& AssertUtils.validateInt(companyId)) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				currentGap = iBillingPortfolioDAO.getCurrentGap(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"));
				responseMap.put("currentGap", currentGap);
			} else {
				responseMap.put("currentGap", "No data found");
			}
		} catch (Exception e) {
			log.error("getBillingCurrentGap(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	public static Map<String, String> getDateForCurrentGap(String startDate, String currentYearFlag) throws Exception {
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM/yyyy");
		Date date;
		Map<String, String> dateMap = new HashMap<String, String>();
		DateTime datetime = new DateTime();
		String year = datetime.toString("YYYY");
		if (currentYearFlag != null && currentYearFlag != "" && currentYearFlag.equalsIgnoreCase("Yes")) {
			startDate = "Jan/" + year;
			date = format2.parse(startDate);
		} else if (startDate != null && !startDate.isEmpty()) {
			date = format2.parse(startDate);
		} else {
			throw new Exception("No Date selected: ");
		}
		dateMap.put("startDate", format1.format(date));
		return dateMap;
	}

	@Override
	public Map<String, Object> getlastUpdateDate() {
		String lastUpdateDate;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		lastUpdateDate = iBillingPortfolioDAO.getLastUpdatedDate();
		responseMap.put("lastUpdateDate", lastUpdateDate);
		return responseMap;
	}

	@Override
	public Map<String, Object> getBillingPercentagePopUp(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<PercentageDTO> percentage = new ArrayList<PercentageDTO>();
		try {
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && pmLeader.isEmpty() == false
					&& segment.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				percentage = iBillingPortfolioDAO.getPercentage(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"));
				responseMap.put("percentage", percentage);
			} else {
				responseMap.put("percentage", "No data found");
			}
		} catch (Exception e) {
			log.error("getBillingPercentagePopUp(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getBillingGapRecovery(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		int gapRecovery;
		try {
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && pmLeader.isEmpty() == false
					&& segment.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				gapRecovery = iBillingPortfolioDAO.getGapRecovery(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"));
				responseMap.put("gapRecovery", gapRecovery);
			} else {
				responseMap.put("gapRecovery", "No data found");
			}
		} catch (Exception e) {
			log.error("getBillingGapRecovery(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;

	}

	@Override
	public Map<String, Object> getAllWeeks(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		List<String> weekly = new ArrayList<String>();
		try {
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && pmLeader.isEmpty() == false
					&& segment.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				weekly = iBillingPortfolioDAO.getWeeks(projectId, dateMap.get("startDate"), dateMap.get("endDate"));
				responseMap.put("weekly", weekly);
			} else {
				responseMap.put("weekly", "No data found");
			}
		} catch (Exception e) {
			log.error("getAllWeeks(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getBillingKpi(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<BillingKpiDTO> kpiList = new ArrayList<BillingKpiDTO>();
		Map<String, String> dateMap = new HashMap<String, String>();
		try {
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				kpiList = iBillingPortfolioDAO.getBillingKpi(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				responseMap.put("kpi", kpiList);
			} else {
				responseMap.put("kpi", kpiList);
			}

		} catch (Exception e) {
			log.error("getBillingKpi(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getBillingExceptions(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ExceptionDTO> exceptionList = new ArrayList<ExceptionDTO>();
		Map<String, String> dateMap = new HashMap<String, String>();
		try {
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				exceptionList = iBillingPortfolioDAO.getBillingException(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				responseMap.put("exceptions", exceptionList);
			} else {
				responseMap.put("exceptions", exceptionList);
			}
		} catch (Exception e) {
			log.error("getBillingException(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	public Map<String, Object> getOverallBillingCycleDetail(int customerId, int companyId, String business,
			String segment, String region, String pmLeader, String spm, String financialSegment, String startDate,
			String endDate, String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		BillingCycleDTO overallBillCycleDTO = new BillingCycleDTO();
		BillingCycleDTO targetBillCycleDTO = new BillingCycleDTO();
		Map<String, String> dateMap = new HashMap<String, String>();
		String targetBillCycleVisible = "N";
		try {
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				overallBillCycleDTO = iBillingPortfolioDAO.getOverallBillingCycleDetail(projectId,
						dateMap.get("startDate"), dateMap.get("endDate"));
				if (!business.equalsIgnoreCase("0")) {
					targetBillCycleDTO = iBillingPortfolioDAO.getTargetBillingCycleDetail(business,
							dateMap.get("startDate"), dateMap.get("endDate"));
					if (targetBillCycleDTO.getP50() != null && !targetBillCycleDTO.getP50().isEmpty()
							&& targetBillCycleDTO.getP75() != null && !targetBillCycleDTO.getP75().isEmpty()
							&& targetBillCycleDTO.getP90() != null && !targetBillCycleDTO.getP90().isEmpty()) {
						targetBillCycleVisible = "Y";
					}
				}
			}
			responseMap.put("overallBillCycle", overallBillCycleDTO);
			responseMap.put("targetBillCycle", targetBillCycleDTO);
			responseMap.put("targetBillCycleVisible", targetBillCycleVisible);
		} catch (Exception e) {
			log.error("getOverallBillingCycleDetail(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getBillingInvoiceDetails(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ViewInvoiceDTO> invoiceList = new ArrayList<ViewInvoiceDTO>();
		Map<String, String> dateMap = new HashMap<String, String>();
		try {
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				invoiceList = iBillingPortfolioDAO.getBillingInvoiceDetails(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				responseMap.put("invoiceList", invoiceList);
			} else {
				responseMap.put("invoiceList", invoiceList);
			}
		} catch (Exception e) {
			log.error("getBillingInvoiceDetails(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getExceptionCategory(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ExceptionCategoryDTO> exceptionCategoryList = new ArrayList<ExceptionCategoryDTO>();
		Map<String, String> dateMap = new HashMap<String, String>();
		try {
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				exceptionCategoryList = iBillingPortfolioDAO.getBillingCategoryDetails(projectId,
						dateMap.get("startDate"), dateMap.get("endDate"), business);
				responseMap.put("exceptionCategoryList", exceptionCategoryList);
			} else {
				responseMap.put("exceptionCategoryList", exceptionCategoryList);
			}
		} catch (Exception e) {
			log.error("getExceptionCategory(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] downloadBillingDetails(int customerId, int companyId, String business, String segment, String region,
			String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		ExportBillingDetailsExcel exportBillingDetailsExcel = new ExportBillingDetailsExcel();
		List<ViewInvoiceDTO> invoiceList = new ArrayList<ViewInvoiceDTO>();
		List<ExceptionDTO> exceptionList = new ArrayList<ExceptionDTO>();
		Map<String, String> dateMap = new HashMap<String, String>();
		try {
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				exceptionList = iBillingPortfolioDAO.getBillingException(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				invoiceList = iBillingPortfolioDAO.getBillingInvoiceDetails(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
			}
			exportBillingDetailsExcel.exportDetailsTableExcel(workbook, invoiceList, exceptionList);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occurred when downloading Excel file{}" , e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occurred when downloading Excel file{}" , e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getMonthlyBillingKpi(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<BillingKpiDTO> kpiList = new ArrayList<BillingKpiDTO>();
		Map<String, String> dateMap = new HashMap<String, String>();
		try {
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				kpiList = iBillingPortfolioDAO.getMonthlyBillingKpi(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				responseMap.put("kpi", kpiList);
			} else {
				responseMap.put("kpi", kpiList);
			}
		} catch (Exception e) {
			log.error("getMonthlyBillingKpi(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getQuarterlyBillingKpi(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<BillingKpiDTO> kpiList = new ArrayList<BillingKpiDTO>();
		Map<String, String> dateMap = new HashMap<String, String>();
		try {
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			dateMap = getDate(startDate, endDate, currentYearFlag);
			log.info("startDate :" + dateMap.get("startDate") + " : endDate : " + dateMap.get("endDate"));
			String projectId = commonService.fetchFavProjects();
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				kpiList = iBillingPortfolioDAO.getQuarterlyBillingKpi(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				responseMap.put("kpi", kpiList);
			} else {
				responseMap.put("kpi", kpiList);
			}
		} catch (Exception e) {
			log.error("getQuarterlyBillingKpi(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getTPSBillingReportURI() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String uri = "";
		try {
			uri = iBillingPortfolioDAO.getTPSBillingReportURI();
			responseMap.put("uri", uri);
		} catch (Exception e) {
			responseMap.put("uri", uri);
			log.error("getTPSBillingReportURI(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] downloadBillingInvoiceDetailsPdf(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, String> dateMap = new HashMap<String, String>();
		List<ViewInvoiceDTO> invoiceList = new ArrayList<ViewInvoiceDTO>();
		ExportBillingDashboardDetailsPdf exportBillingDashboardDetailsPdf = new ExportBillingDashboardDetailsPdf();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] pdfData = null;
		try {
			String projectId = commonService.fetchFavProjects();
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			if (null == projectId) {
				projectId = "0";
			}
			dateMap = getDate(startDate, endDate, currentYearFlag);
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				invoiceList = iBillingPortfolioDAO.getBillingInvoiceDetails(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
			}
			log.info("Creating PDF with " + invoiceList.size() + " rows");
			bos = exportBillingDashboardDetailsPdf.exportInvoiceDetailsPdf(invoiceList);
			pdfData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occurred when downloading invoices with billing cycle pdf file{}" , e.getMessage());
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				log.error("Error occurred when downloading invoices with billing cycle pdf file{}" , e.getMessage());
			}
		}
		return pdfData;
	}

	@Override
	public Map<String, Object> getBillingInvoiceRemarksList() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> remarksList = new ArrayList<DropDownDTO>();
		try {
			remarksList = iBillingPortfolioDAO.getBillingInvoiceRemarksList();
			responseMap.put("remarksList", remarksList);
		} catch (Exception e) {
			responseMap.put("remarksList", remarksList);
			log.error("getBillingInvoiceRemarksList(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> saveBillingMilestoneRemarkDetails(List<BillingCycleRemarksDTO> invoiceList) {
		int result = 0;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String sso = callerContext.getName();
		try {
			if (invoiceList != null && !invoiceList.isEmpty()) {
				result = iBillingPortfolioDAO.saveBillingMilestoneRemarkDetails(invoiceList, sso);
				if (result == 1) {
					responseMap.put("status", "success");
					responseMap.put("message", "Data saved successfully");
				} else {
					responseMap.put("status", "Error");
					responseMap.put("message", "Error occured while saving data");
				}
			} else {
				responseMap.put("status", "Error");
				responseMap.put("message", "No change in data detected");
			}

		} catch (Exception e) {
			responseMap.put("status", "Error");
			responseMap.put("message", "Error occured while saving data");
			log.error("saveBillingMilestoneRemarkDetails(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getExceptionRemarksList() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> remarksList = new ArrayList<DropDownDTO>();
		try {
			remarksList = iBillingPortfolioDAO.getExceptionRemarksList();
			responseMap.put("remarksList", remarksList);
		} catch (Exception e) {
			responseMap.put("remarksList", remarksList);
			log.error("getExceptionRemarksList(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> saveBillingExceptionRemarkDetails(List<ExceptionRemarksDTO> exceptionList) {
		int result = 0;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String sso = callerContext.getName();
		try {
			if (exceptionList != null && !exceptionList.isEmpty()) {
				result = iBillingPortfolioDAO.saveBillingExceptionRemarkDetails(exceptionList, sso);
				if (result == 1) {
					responseMap.put("status", "success");
					responseMap.put("message", "Data saved successfully");
				} else {
					responseMap.put("status", "Error");
					responseMap.put("message", "Error occured while saving data");
				}
			} else {
				responseMap.put("status", "Error");
				responseMap.put("message", "No change in data detected");
			}

		} catch (Exception e) {
			responseMap.put("status", "Error");
			responseMap.put("message", "Error occured while saving data");
			log.error("saveBillingExceptionRemarkDetails(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getBillingActivitiesDetails(String projectId, String cashMilestoneId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ActivitiesDTO> activitiesList = new ArrayList<ActivitiesDTO>();
		try {
			if (projectId != null && cashMilestoneId != null) {
				activitiesList = iBillingPortfolioDAO.getBillingActivitiesDetails(projectId, cashMilestoneId);
				responseMap.put("activitiesList", activitiesList);
			} else {
				throw new Exception("Error while getting activities for cash milestone id: " + cashMilestoneId);
			}
		} catch (Exception e) {
			responseMap.put("activitiesList", activitiesList);
			log.error("getBillingActivitiesDetails(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getBillingLinearityChart(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<BillingLinearityChartDTO> chart = new ArrayList<BillingLinearityChartDTO>();
		try {
			String projectId = commonService.fetchFavProjects();
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				chart = iBillingPortfolioDAO.getBillingLinearityChart(projectId);
				responseMap.put("chart", chart);
			} else {
				responseMap.put("chart", chart);
			}
		} catch (Exception e) {
			responseMap.put("chart", chart);
			log.error("getBillingLinearityChart(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getBillingLinearityChartPopup(int customerId, int companyId, String business,
			String segment, String region, String pmLeader, String spm, String financialSegment, String quarterYear,
			String month) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, String> dateMap = new HashMap<String, String>();
		boolean isLE = false;
		if (null != quarterYear && quarterYear.contains("LE")) {
			isLE = true;
		}
		List<BillingLinearityChartPopupDTO> popup = new ArrayList<BillingLinearityChartPopupDTO>();
		try {
			String monthVal = "";
			String projectId = commonService.fetchFavProjects();
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			if (null == projectId) {
				projectId = "0";
			}
			if (month != null && month.isEmpty() == false && month.equalsIgnoreCase("firstMonth")) {
				monthVal = "1";
			} else if (month != null && month.isEmpty() == false && month.equalsIgnoreCase("secondMonth")) {
				monthVal = "2";
			} else if (month != null && month.isEmpty() == false && month.equalsIgnoreCase("thirdMonth")) {
				monthVal = "3";
			}
			if (quarterYear != null && month.isEmpty() == false) {
				quarterYear = quarterYear.substring(0, 5);
			}
			dateMap = getDateFromQuarterMonth(quarterYear, monthVal);
			String startDate = dateMap.get("startDate");
			String endDate = dateMap.get("endDate");
			log.info("startDate :" + startDate + " : endDate : " + endDate);
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				if (isLE) {
					popup = iBillingPortfolioDAO.getBillingLinearityChartPopupForLE(projectId, startDate, endDate);
				} else {
					popup = iBillingPortfolioDAO.getBillingLinearityChartPopup(projectId, startDate, endDate);
				}

				responseMap.put("popup", popup);
			} else {
				responseMap.put("popup", popup);
			}
		} catch (Exception e) {
			responseMap.put("popup", popup);
			log.error("getBillingLinearityChartPopup(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	private Map<String, String> getDateFromQuarterMonth(String quarter, String month) throws Exception {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM/yyyy");
		Map<String, String> response = new HashMap<String, String>();
		String startDate = "", endDate = "";
		Date date, date1;
		Calendar c = Calendar.getInstance();
		String selectedYear = "", selectedQuarter = "", selectedMonth = "";
		if (quarter != null && !quarter.isEmpty() && month != null && !month.isEmpty()) {
			String[] selectedQuarterArr = quarter.split("-");
			selectedYear = "20" + selectedQuarterArr[0];
			selectedQuarter = selectedQuarterArr[1];
			if (selectedQuarter.equalsIgnoreCase("1Q") && month.equalsIgnoreCase("1")) {
				selectedMonth = "Jan/";
			} else if (selectedQuarter.equalsIgnoreCase("1Q") && month.equalsIgnoreCase("2")) {
				selectedMonth = "Feb/";
			} else if (selectedQuarter.equalsIgnoreCase("1Q") && month.equalsIgnoreCase("3")) {
				selectedMonth = "Mar/";
			} else if (selectedQuarter.equalsIgnoreCase("2Q") && month.equalsIgnoreCase("1")) {
				selectedMonth = "Apr/";
			} else if (selectedQuarter.equalsIgnoreCase("2Q") && month.equalsIgnoreCase("2")) {
				selectedMonth = "May/";
			} else if (selectedQuarter.equalsIgnoreCase("2Q") && month.equalsIgnoreCase("3")) {
				selectedMonth = "Jun/";
			} else if (selectedQuarter.equalsIgnoreCase("3Q") && month.equalsIgnoreCase("1")) {
				selectedMonth = "Jul/";
			} else if (selectedQuarter.equalsIgnoreCase("3Q") && month.equalsIgnoreCase("2")) {
				selectedMonth = "Aug/";
			} else if (selectedQuarter.equalsIgnoreCase("3Q") && month.equalsIgnoreCase("3")) {
				selectedMonth = "Sep/";
			} else if (selectedQuarter.equalsIgnoreCase("4Q") && month.equalsIgnoreCase("1")) {
				selectedMonth = "Oct/";
			} else if (selectedQuarter.equalsIgnoreCase("4Q") && month.equalsIgnoreCase("2")) {
				selectedMonth = "Nov/";
			} else if (selectedQuarter.equalsIgnoreCase("4Q") && month.equalsIgnoreCase("3")) {
				selectedMonth = "Dec/";
			}
		}

		date = format2.parse(selectedMonth + selectedYear);
		date1 = format2.parse(selectedMonth + selectedYear);
		c.setTime(date1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		startDate = format1.format(date);
		endDate = format1.format(c.getTime());
		response.put("startDate", startDate);
		response.put("endDate", endDate);
		return response;
	}

	@Override
	public byte[] downloadBillingLinearityExcel(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment) {
		ExportBillingDetailsExcel exportBillingDetailsExcel = new ExportBillingDetailsExcel();
		List<BillingLinearityChartPopupDTO> linearityList = new ArrayList<BillingLinearityChartPopupDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		byte[] excelData = null;
		try {
			String projectId = commonService.fetchFavProjects();
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			if (null == projectId) {
				projectId = "0";
			}
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				linearityList = iBillingPortfolioDAO.downloadBillingLinearityExcel(projectId);
				log.info("Creating Billing Linearity Sheet with " + linearityList.size() + " rows.");
				exportBillingDetailsExcel.exportBillingLinearityDetailsExcel(workbook, linearityList);
				workbook.write(bos);
				excelData = bos.toByteArray();
			}
		} catch (Exception e) {
			log.error("Error occured when downloading Billing Linearity details{}" , e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occurred when downloading Billing Linearity details{}" , e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public byte[] downloadBillingMilestoneDetails(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, String> dateMap = new HashMap<String, String>();
		ExportBillingDetailsExcel exportBillingDetailsExcel = new ExportBillingDetailsExcel();
		List<BillingMilestoneListDTO> milestoneList = new ArrayList<BillingMilestoneListDTO>();
		List<ExceptionCategoryDTO> categoryList = new ArrayList<ExceptionCategoryDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		try {
			String projectId = commonService.fetchFavProjects();
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			if (null == projectId) {
				projectId = "0";
			}
			dateMap = getDate(startDate, endDate, currentYearFlag);
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				milestoneList = iBillingPortfolioDAO.getBillingMilestoneList(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				categoryList = getToBillCategory(milestoneList);
			}
			exportBillingDetailsExcel.exportBillingMilestoneDetailsExcel(workbook, milestoneList, categoryList);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured when downloading Billing Milestone Tab details" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading Billing Milestone Tab details" + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public byte[] downloadBillingDetailsExcel(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) {
		Map<String, String> dateMap = new HashMap<String, String>();
		ExportBillingDetailsExcel exportBillingDetailsExcel = new ExportBillingDetailsExcel();
		List<SegmentSummaryDTO> segmentList = new ArrayList<SegmentSummaryDTO>();
		List<BillingProjectListDTO> projectList = new ArrayList<BillingProjectListDTO>();
		List<BillingMilestoneListDTO> milestoneList = new ArrayList<BillingMilestoneListDTO>();
		List<ExceptionCategoryDTO> categoryList = new ArrayList<ExceptionCategoryDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		try {
			String projectId = commonService.fetchFavProjects();
			String[] list = new String[] { "NE", "PVS", "TD", "SRV" };
			List<String> businessList = Arrays.asList(list);
			if (null == projectId) {
				projectId = "0";
			}
			dateMap = getDate(startDate, endDate, currentYearFlag);
			if (companyId != 0 && business.isEmpty() == false && segment.isEmpty() == false
					&& (businessList.contains(business) || business.equalsIgnoreCase("0"))
					&& pmLeader.isEmpty() == false && spm.isEmpty() == false) {
				projectId = getProjectList(companyId, business, segment, customerId, region, projectId, pmLeader, spm,
						financialSegment);
				segmentList = iBillingPortfolioDAO.getSegmentSummary(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				projectList = iBillingPortfolioDAO.getBillingProjectList(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				milestoneList = iBillingPortfolioDAO.getBillingMilestoneList(projectId, dateMap.get("startDate"),
						dateMap.get("endDate"), business);
				categoryList = getToBillCategory(milestoneList);
			}
			log.info("Creating Segment Details Sheet with " + segmentList.size() + " rows.");
			exportBillingDetailsExcel.exportSegmentDetailsExcel(workbook, segmentList);

			log.info("Creating Project Details Sheet with " + projectList.size() + " rows.");
			exportBillingDetailsExcel.exportProjectDetailsExcel(workbook, projectList);

			log.info("Creating Milestone Details Sheet with " + milestoneList.size() + " rows.");
			exportBillingDetailsExcel.exportBillingMilestoneDetailsExcel(workbook, milestoneList, categoryList);

			workbook.write(bos);
			excelData = bos.toByteArray();

		} catch (Exception e) {
			log.error("Error occured when downloading Billing Dashboard Details" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading Billing Dashboard Details" + e.getMessage());
			}
		}
		return excelData;
	}

	public String getProjectList(int companyId, String business, String segment, int customerId, String region,
			String projectId, String pmLeader, String spm, String financialSegment) {
		return iBillingPortfolioDAO.getProjectList(companyId, business, segment, customerId, region, projectId,
				pmLeader, spm, financialSegment);
	}

	@Override
	public UpdateDetailsDTO getBillingProjectOutOfRTUploadStatus(String companyId) {
		UpdateDetailsDTO updateDetailsDTO = new UpdateDetailsDTO();
		List<LastSuccessfulUpdateDetailsDTO> lastSuccessfulUpdateDetailsDTO = new ArrayList<LastSuccessfulUpdateDetailsDTO>();
		List<LastUpdateDetailsDTO> lastUpdateDetailsDTO = new ArrayList<LastUpdateDetailsDTO>();
		List<ErrorDetailsDTO> errorDetails = new ArrayList<ErrorDetailsDTO>();
		try {
			lastSuccessfulUpdateDetailsDTO = iBillingPortfolioDAO.getLastSuccessfulUpdateData(companyId);
			lastUpdateDetailsDTO = iBillingPortfolioDAO.getBillingLastUpdatedData(companyId);
			errorDetails = iBillingPortfolioDAO.getErrorDetailsData(companyId);
			updateDetailsDTO.setLastSuccessfulUpdateDetails(lastSuccessfulUpdateDetailsDTO);
			updateDetailsDTO.setLastUpdateDetails(lastUpdateDetailsDTO);
			updateDetailsDTO.setErrorDetails(errorDetails);
		} catch (Exception e) {
			log.error("getBillingProjectOutOfRTUploadStatus(): Exception occurred : " + e.getMessage());
		}
		return updateDetailsDTO;
	}

	@Override
	public List<ErrorDetailsDTO> getBillingNotProcessedProjectDetails(String companyId) {
		List<ErrorDetailsDTO> errorDetails = new ArrayList<ErrorDetailsDTO>();
		try {
			errorDetails = iBillingPortfolioDAO.getNotProcessedProjectDetails(companyId);
		} catch (Exception e) {
			log.error("getBillingNotProcessedProjectDetails(): Exception occurred : " + e.getMessage());
		}
		return errorDetails;
	}

	@Override
	public byte[] downloadBillingOutOfProjectTemplate(String companyId) {
		ExportBillingDetailsExcel excelObj = new ExportBillingDetailsExcel();
		List<TPSBillingOutOfRTProjectDetailsDTO> list = new ArrayList<TPSBillingOutOfRTProjectDetailsDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		try {
			list = iBillingPortfolioDAO.getTPSBillingOutOfProjectTemplateDetails(companyId);
			log.info("Creating Billing Out of RT Project template file with " + list.size() + " rows.");
			excelObj.exportBillingOutOfProjectExcel(workbook, list);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured when downloading Billing Out of RT Project template file" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading Billing Out of RT Project template file" + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, String> uploadBillingProjectOutOfRT(String companyId, MultipartFile excelFile) {
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
						BillingPortfolioConstants.GET_PROJECT_EXCEL_MODULE_NAME)) {
					validationMessage.append("Already a file upload is in In-Progress status !!!");
					validationStatus = false;
					responseMap.put("status", "Error");
					responseMap.put("message", validationMessage.toString());
				} else {
					statusDTO.setStatus("In-Progress");
					statusDTO.setCompanyId(companyId);
					statusDTO.setSso(sso);
					statusDTO.setFileName(fileName);
					statusDTO.setModuleName(BillingPortfolioConstants.GET_PROJECT_EXCEL_MODULE_NAME);
					iBillingPortfolioDAO.insertFileTrackingDetails(statusDTO);
					responseMap.put("status", "Success");
					responseMap.put("message", "");
				}
				if (validationStatus) {
					try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());) {
						trackId = iBillingPortfolioDAO.getFileTrackingId(statusDTO);
						statusDTO.setTrackingId(String.valueOf(trackId));
						log.info("Billing Out of RT Project Excel - Track ID :: " + trackId);
						processProjectExcelFile(companyId, workbook, headerIndexMap, statusDTO);
					}
				}
			} else {
				log.error("Multipart File is empty or null");
				responseMap.put("status", "Error");
				responseMap.put("message", "Error while uploading Billing Out of RT Project excel file !!!");
				return responseMap;
			}
		} catch (Exception e) {
			log.error("Error while uploading Billing Out of RT Project excel file :: " + e.getMessage());
			if (excelFile != null && !excelFile.isEmpty()) {
				statusDTO.setErrorMsg("Error");
				statusDTO.setStatus("Error");
				iBillingPortfolioDAO.updateFileTrackingDetails(statusDTO);
			}
		}
		return responseMap;
	}

	private boolean checkInProgressFileUpload(String companyId, String moduleName) {
		boolean inProgressFileUpload = false;
		inProgressFileUpload = iBillingPortfolioDAO.getFileUploadStatus(companyId, moduleName);
		return inProgressFileUpload;
	}

	private Map<Integer, String> getBillingProjectOutOfRTExcelHeaderColumnMap() {
		Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
		headerIndexMap = iBillingPortfolioDAO.fetchProjectOutOfRTExcelHeaderColumnMap();
		return headerIndexMap;
	}

	private boolean validateExcelColumnDetails(String companyId, Workbook workbook, Map<Integer, String> headerIndexMap,
			StringBuilder validationMessage, FileUploadStatusDTO statusDTO) {
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
			iBillingPortfolioDAO.updateFileTrackingDetails(statusDTO);
		}
		return validationStatus;
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
						headerIndexMap = getBillingProjectOutOfRTExcelHeaderColumnMap();
						validationStatus = validateExcelColumnDetails(companyId, workbook, headerIndexMap,
								validationMessage, statusDTO);
					}
					if (!validationStatus) {
						// update tracking tbl as false (tracking id) create a new entry in error table
						statusDTO.setErrorMsg(validationMessage.toString());
						statusDTO.setStatus("Error");
						iBillingPortfolioDAO.updateFileTrackingDetails(statusDTO);
					}
					if (validationStatus) {
						List<TPSBillingOutOfRTProjectDetailsDTO> list = new ArrayList<TPSBillingOutOfRTProjectDetailsDTO>();
						list = readOutOfRTProjectDetailsExcelFile(workbook, headerIndexMap, statusDTO);
						iBillingPortfolioDAO.insertProjectExcelStageData(list, statusDTO);
						iBillingPortfolioDAO.callFileUploadStageToTarget(statusDTO);
					}
				} catch (Exception e) {
					// update tracking tbl as false (tracking id) create a new entry in error table
					log.error(
							"Error while processing Billing Out of RT Project Details excel file :: " + e.getMessage());
					statusDTO.setErrorMsg("Error");
					statusDTO.setStatus("Error");
					iBillingPortfolioDAO.updateFileTrackingDetails(statusDTO);
				}
			}
		};
		new Thread(caller1).start();
	}

	protected List<TPSBillingOutOfRTProjectDetailsDTO> readOutOfRTProjectDetailsExcelFile(Workbook workbook,
			Map<Integer, String> headerIndexMap, FileUploadStatusDTO statusDTO) {
		List<TPSBillingOutOfRTProjectDetailsDTO> list = new ArrayList<TPSBillingOutOfRTProjectDetailsDTO>();
		try {
			Sheet sheet = workbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();

			for (int rowNum = 1; rowNum <= totalRows; rowNum++) {

				TPSBillingOutOfRTProjectDetailsDTO dto = new TPSBillingOutOfRTProjectDetailsDTO();
				int colIx = 0;
				String cellValue = "";
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
				dto.setRegion(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setCustomer(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInstallationCountry(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setSegment(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setProject(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setProjectName(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setMilestone(cellValue);

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
				dto.setDescription(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setUsdMilestoneAmount(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setLastEstimate(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setRiskOppty(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setComment(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInvoiceNumber(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInvoiceDate(cellValue);

				colIx = colIx + 1;
				cell = dataRow.getCell(colIx);
				cellValue = cell != null ? getCellValueByType(cell) : "";
				dto.setInvoicedAmountUSD(cellValue);

				list.add(dto);
			}
			log.info("Project Details list size :: " + list.size());
		} catch (Exception e) {
			log.error("Error while processing Billing Out of RT Project Details excel file :: " + e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			iBillingPortfolioDAO.updateFileTrackingDetails(statusDTO);
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

}