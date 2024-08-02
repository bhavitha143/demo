package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ServerErrorException;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IBillingDAO;
import com.bh.realtrack.dto.ActivitiesDTO;
import com.bh.realtrack.dto.ActivitiesPopupDTO;
import com.bh.realtrack.dto.ActualCurveDTO;
import com.bh.realtrack.dto.AllMilestonesDTO;
import com.bh.realtrack.dto.BaselineCurveDTO;
import com.bh.realtrack.dto.BillingSummaryDTO;
import com.bh.realtrack.dto.BlankBaselineDateDTO;
import com.bh.realtrack.dto.BlankBaselineDatePopupDTO;
import com.bh.realtrack.dto.CashCollectionCurveTableDTO;
import com.bh.realtrack.dto.CashCollectionInvoicesDetails;
import com.bh.realtrack.dto.CollectedCashDTO;
import com.bh.realtrack.dto.CurveTable;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ExchangeRateDTO;
import com.bh.realtrack.dto.FinancialBLCurveDTO;
import com.bh.realtrack.dto.ForecastCashDTO;
import com.bh.realtrack.dto.ForecastCurveDTO;
import com.bh.realtrack.dto.InvoiceDetailDTO;
import com.bh.realtrack.dto.ItoBaselineCurveDTO;
import com.bh.realtrack.dto.ItoCashBaselineDTO;
import com.bh.realtrack.dto.MilestonesDTO;
import com.bh.realtrack.dto.MilestonesToBillDTO;
import com.bh.realtrack.dto.NextToBillDTO;
import com.bh.realtrack.dto.OpenInvoiceChartPopupDetails;
import com.bh.realtrack.dto.OpenInvoiceDataTable;
import com.bh.realtrack.dto.OtrCashBaselineDTO;
import com.bh.realtrack.dto.PODetailsDTO;
import com.bh.realtrack.dto.POInvoiceDetailDTO;
import com.bh.realtrack.dto.PastDueCommitmentDTO;
import com.bh.realtrack.dto.SaveOpenInvoiceDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.excel.ExportMilestoneToExcel;
import com.bh.realtrack.excel.ExportOpenInvoicesExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IBillingService;
import com.bh.realtrack.util.AssertUtils;

@Service
public class BillingServiceImpl implements IBillingService {

	private static final Logger log = LoggerFactory.getLogger(BillingServiceImpl.class);

	private IBillingDAO iBillingDAO;
	private CallerContext callerContext;

	@Autowired
	public BillingServiceImpl(IBillingDAO iBillingDAO, CallerContext callerContext) {
		this.iBillingDAO = iBillingDAO;
		this.callerContext = callerContext;
	}

	@Override
	public Map<String, Object> getBillingSummary(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<BillingSummaryDTO> summaryList = new ArrayList<BillingSummaryDTO>();
		try {
			if (projectId != null) {
				summaryList = iBillingDAO.getBillingSummary(projectId);
				responseMap.put("billingSummary", summaryList);
			} else {
				throw new Exception("Error getting billing summary for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getBillingSummary(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getNextToBillPopup(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<NextToBillDTO> nextToBillPopupList = new ArrayList<NextToBillDTO>();
		try {
			if (projectId != null) {
				nextToBillPopupList = iBillingDAO.getNextToBillPopup(projectId);
				responseMap.put("nextToBill", nextToBillPopupList);
			} else {
				throw new Exception("Error getting Next To Bill Popup for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getNextToBillPopup(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getMilestonesToBill(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		boolean flag = false;
		List<MilestonesToBillDTO> milestonesToBillList = new ArrayList<MilestonesToBillDTO>();
		try {
			if (projectId != null) {
				milestonesToBillList = iBillingDAO.getMilestonesToBill(projectId);
				flag = iBillingDAO.getShowPublishButtonFlag(projectId);
				responseMap.put("milestonesToBill", milestonesToBillList);
				responseMap.put("showPublishButtonFlag", flag);
			} else {
				throw new Exception("Error getting Milestones To Bill table for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getMilestonesToBill(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getAllMilestones(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<AllMilestonesDTO> milestoneList = new ArrayList<AllMilestonesDTO>();
		try {
			if (projectId != null) {
				milestoneList = iBillingDAO.getAllMilestones(projectId);
				responseMap.put("milestoneList", milestoneList);
			} else {
				throw new Exception("Error getting All Milestones for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getAllMilestones(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getBillingReport(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ForecastCurveDTO> forecastList = new ArrayList<ForecastCurveDTO>();
		List<ForecastCurveDTO> pubForecastList = new ArrayList<ForecastCurveDTO>();
		List<ActualCurveDTO> actualList = new ArrayList<ActualCurveDTO>();
		List<CurveTable> tableList = new ArrayList<CurveTable>();
		List<BaselineCurveDTO> baselineList = new ArrayList<BaselineCurveDTO>();
		List<BlankBaselineDateDTO> blankBaselineDate = new ArrayList<BlankBaselineDateDTO>();
		List<FinancialBLCurveDTO> financialBLList = new ArrayList<FinancialBLCurveDTO>();
		List<ItoBaselineCurveDTO> itoBaselineCurveList = new ArrayList<ItoBaselineCurveDTO>();

		String lastSavedDt = "";
		try {
			if (projectId != null) {
				forecastList = iBillingDAO.getForecastCurve(projectId);
				actualList = iBillingDAO.getActualCurve(projectId);
				tableList = iBillingDAO.getBillingTable(projectId);
				baselineList = iBillingDAO.getBaselineCurve(projectId);
				pubForecastList = iBillingDAO.getPubForecastCurve(projectId);
				blankBaselineDate = iBillingDAO.getBlankBaselineDate(projectId);
				lastSavedDt = iBillingDAO.getLastSavedDate(projectId);
				financialBLList = iBillingDAO.getFinancialBLCurve(projectId);
				itoBaselineCurveList = iBillingDAO.getItoBaselineCurve(projectId);

				Collections.sort(tableList, new Comparator<CurveTable>() {
					@Override
					public int compare(CurveTable p1, CurveTable p2) {
						try {

							return getDateParsed(p1.getGraphDt()).compareTo(getDateParsed(p2.getGraphDt()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return 0;

					}
				});

				for (int i = 1; i < tableList.size(); i++) {
					if (Double.valueOf(tableList.get(i).getItoBillingAmount()) == 0.00) {
						for (int j = i - 1; j >= 0; j--) {
							if (Double.valueOf(tableList.get(j).getItoBillingAmount()) != 0.00) {
								tableList.get(i).setItoBillingAmount(tableList.get(j).getItoBillingAmount());
								break;
							}
						}
					}
				}

				responseMap.put("forecastCurve", forecastList);
				responseMap.put("actualCurve", actualList);
				responseMap.put("baselineCurve", baselineList);
				responseMap.put("pubForecastCurve", pubForecastList);
				responseMap.put("financialBLCurve", financialBLList);
				responseMap.put("table", tableList);
				responseMap.put("blankForecast", blankBaselineDate.get(0).getConvertedAmount());
				responseMap.put("blankForecastDate", blankBaselineDate.get(0).getxAxisDt());
				responseMap.put("lastSavedDt", lastSavedDt);
				responseMap.put("itoBaselineCurve", itoBaselineCurveList);

			} else {
				throw new Exception("Error getting billing report for : " + projectId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("getBillingReport(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	public Date getDateParsed(String date) throws ParseException {
		return new SimpleDateFormat("MMM-yy", Locale.ENGLISH).parse(date);
	}

	@Override
	public Map<String, Object> updateMilestoneDetails(List<MilestonesDTO> milestonesList) {
		int result = 0;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String sso = callerContext.getName();
		try {
			result = iBillingDAO.updateMilestoneDetails(milestonesList, sso);
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
	public Map<String, Object> publishMilestoneDetails(String projectId, List<MilestonesDTO> milestonesList) {
		String result = "";
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Set<String> changedPubMilestones = new HashSet<String>();
		try {
			if (projectId != null) {
				for (MilestonesDTO dto : milestonesList) {
					if ("Y".equalsIgnoreCase(dto.getUpdated())) {
						if (dto.getForecastDt() != null && !dto.getForecastDt().equalsIgnoreCase("")) {
							// If forecast date for the milestone id is not null and is not empty
							changedPubMilestones.add(dto.getCashMilestoneActivityId());
						} else if (dto.getForecastDt() == null || dto.getForecastDt().equalsIgnoreCase("")) {
							// If forecast date for the milestone id is null or is empty.
							log.error("forecast date is empty for milestone id :: " + dto.getCashMilestoneActivityId());
							responseMap.put("status", "Error");
							responseMap.put("message", "Forecast Date is mandatory");
							return responseMap;
						}
					}
				}
				log.info("changedPubMilestones:: " + changedPubMilestones);
				if (!changedPubMilestones.isEmpty()) {
					List<String> changedPubMilestoneIds = new ArrayList<String>(changedPubMilestones);
					String sso = callerContext.getName();
					result = iBillingDAO.publishMilestoneDetails(projectId, sso, changedPubMilestoneIds);
					if (result.equalsIgnoreCase("S")) {
						responseMap.put("status", "success");
						responseMap.put("message", "Data published successfully");
					} else {
						responseMap.put("status", "Error");
						responseMap.put("message", "Error occured while publishing data");
					}
				} else if (changedPubMilestones.isEmpty()) {
					responseMap.put("status", "Error");
					responseMap.put("message", "No change detected in the milestone data");
				}
			} else {
				responseMap.put("status", "Error");
				responseMap.put("message", "Error occured while publishing data");
				log.error("publishMilestoneDetails(): Exception occurred : project id is empty");
			}
		} catch (Exception e) {
			responseMap.put("status", "Error");
			responseMap.put("message", "Error occured while publishing data");
			log.error("publishMilestoneDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public ResponseEntity<?> exportAllMilestoneDetailsExcel(String projectId) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		ExportMilestoneToExcel excelObj = new ExportMilestoneToExcel();
		List<AllMilestonesDTO> milestoneList = new ArrayList<AllMilestonesDTO>();
		ResponseEntity<InputStreamResource> responseEntity = null;
		String fileName = "Milestone_Details.xlsx";
		FileOutputStream fileOut = null;
		try {
			if (projectId != null) {
				milestoneList = iBillingDAO.getAllMilestones(projectId);
				fileOut = new FileOutputStream(fileName);
				workbook = excelObj.exportAllMilestoneDetailsExcel(workbook, milestoneList, "PM");
				workbook.write(fileOut);
				fileOut.flush();
				File file = new File(fileName);
				InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
				HttpHeaders headers = new HttpHeaders();
				headers.add("content-disposition", String.format("attachment; filename=\"%s\"", file.getName()));
				responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
						.contentType(MediaType.parseMediaType("application/text")).body(resource);
			}
		} catch (IOException | ParseException e) {
			log.error("something went wrong while downloading execution excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			try {
				fileOut.close();
				workbook.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return responseEntity;
	}

	@Override
	public Map<String, Object> getPublishBillingSummary(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<BillingSummaryDTO> summaryList = new ArrayList<BillingSummaryDTO>();
		try {
			if (projectId != null) {
				summaryList = iBillingDAO.getPublishBillingSummary(projectId);
				responseMap.put("billingSummary", summaryList);
			} else {
				throw new Exception("Error getting pub billing summary for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getPublishBillingSummary(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getPublishNextToBillPopup(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<NextToBillDTO> nextToBillPopupList = new ArrayList<NextToBillDTO>();
		try {
			if (projectId != null) {
				nextToBillPopupList = iBillingDAO.getPublishNextToBillPopup(projectId);
				responseMap.put("nextToBill", nextToBillPopupList);
			} else {
				throw new Exception("Error getting Publish Next To Bill Popup for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getPublishNextToBillPopup(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getPublishMilestonesToBill(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<MilestonesToBillDTO> milestonesToBillList = new ArrayList<MilestonesToBillDTO>();
		try {
			if (projectId != null) {
				milestonesToBillList = iBillingDAO.getPublishMilestonesToBill(projectId);
				responseMap.put("milestonesToBill", milestonesToBillList);
			} else {
				throw new Exception("Error getting Publish Milestones To Bill table for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getPublishMilestonesToBill(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getPublishAllMilestones(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<AllMilestonesDTO> milestoneList = new ArrayList<AllMilestonesDTO>();
		try {
			if (projectId != null) {
				milestoneList = iBillingDAO.getPublishAllMilestones(projectId);
				responseMap.put("milestoneList", milestoneList);
			} else {
				throw new Exception("Error getting Publish All Milestones for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getPublishAllMilestones(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getPublishBillingReport(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ForecastCurveDTO> forecastList = new ArrayList<ForecastCurveDTO>();
		List<ActualCurveDTO> actualList = new ArrayList<ActualCurveDTO>();
		List<CurveTable> tableList = new ArrayList<CurveTable>();
		List<BaselineCurveDTO> baselineList = new ArrayList<BaselineCurveDTO>();
		List<BlankBaselineDateDTO> blankBaselineDate = new ArrayList<BlankBaselineDateDTO>();
		List<FinancialBLCurveDTO> financialBLList = new ArrayList<FinancialBLCurveDTO>();
		List<ItoBaselineCurveDTO> itoBaselineCurveList = new ArrayList<ItoBaselineCurveDTO>();

		String lastPubDt = "";
		try {
			if (projectId != null) {
				forecastList = iBillingDAO.getPublishForecastCurve(projectId);
				actualList = iBillingDAO.getPublishActualCurve(projectId);
				tableList = iBillingDAO.getPublishBillingTable(projectId);
				baselineList = iBillingDAO.getPublishBaselineCurve(projectId);
				blankBaselineDate = iBillingDAO.getPublishBlankBaselineDate(projectId);
				lastPubDt = iBillingDAO.getLastPublishDate(projectId);
				financialBLList = iBillingDAO.getPubFinancialBLCurve(projectId);
				itoBaselineCurveList = iBillingDAO.getItoBaselineCurve(projectId);

				Collections.sort(tableList, new Comparator<CurveTable>() {
					@Override
					public int compare(CurveTable p1, CurveTable p2) {
						try {

							return getDateParsed(p1.getGraphDt()).compareTo(getDateParsed(p2.getGraphDt()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return 0;

					}
				});

				for (int i = 1; i < tableList.size(); i++) {
					if (!tableList.get(i).getItoBillingAmount().equalsIgnoreCase("")
							&& Double.valueOf(tableList.get(i).getItoBillingAmount()) == 0.00) {
						for (int j = i - 1; j >= 0; j--) {
							if (!tableList.get(j).getItoBillingAmount().equalsIgnoreCase("")
									&& Double.valueOf(tableList.get(j).getItoBillingAmount()) != 0.00) {
								tableList.get(i).setItoBillingAmount(tableList.get(j).getItoBillingAmount());
								break;
							}
						}
					}
				}

				responseMap.put("forecastCurve", forecastList);
				responseMap.put("actualCurve", actualList);
				responseMap.put("baselineCurve", baselineList);
				responseMap.put("financialBLCurve", financialBLList);
				responseMap.put("table", tableList);
				responseMap.put("blankForecast", blankBaselineDate.get(0).getConvertedAmount());
				responseMap.put("blankForecastDate", blankBaselineDate.get(0).getxAxisDt());
				responseMap.put("lastSavedDt", lastPubDt);
				responseMap.put("itoBaselineCurve", itoBaselineCurveList);
			} else {
				throw new Exception("Error getting Publish billing report for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getPublishBillingReport(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public ResponseEntity<?> exportPublishAllMilestoneDetailsExcel(String projectId) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		ExportMilestoneToExcel excelObj = new ExportMilestoneToExcel();
		List<AllMilestonesDTO> milestoneList = new ArrayList<AllMilestonesDTO>();
		ResponseEntity<InputStreamResource> responseEntity = null;
		String fileName = "Milestone_Details.xlsx";
		FileOutputStream fileOut = null;
		try {
			if (projectId != null) {
				milestoneList = iBillingDAO.getPublishAllMilestones(projectId);
				fileOut = new FileOutputStream(fileName);
				workbook = excelObj.exportAllMilestoneDetailsExcel(workbook, milestoneList, "Other");
				workbook.write(fileOut);
				fileOut.flush();
				File file = new File(fileName);
				InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
				HttpHeaders headers = new HttpHeaders();
				headers.add("content-disposition", String.format("attachment; filename=\"%s\"", file.getName()));
				responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
						.contentType(MediaType.parseMediaType("application/text")).body(resource);
			}
		} catch (IOException | ParseException e) {
			log.error("something went wrong while downloading all milestones excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			try {
				fileOut.close();
				workbook.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return responseEntity;
	}

	@Override
	public Map<String, Object> getBlankBaseLineDatePopup(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<BlankBaselineDatePopupDTO> popupList = new ArrayList<BlankBaselineDatePopupDTO>();
		try {
			if (projectId != null) {
				popupList = iBillingDAO.getBlankBaseLineDatePopup(projectId);
				responseMap.put("popup", popupList);
			} else {
				throw new Exception("Error getting Blank BaseLine Date Popupfor : " + projectId);
			}
		} catch (Exception e) {
			log.error("getBlankBaseLineDatePopup(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getPublishBlankBaseLineDatePopup(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<BlankBaselineDatePopupDTO> popupList = new ArrayList<BlankBaselineDatePopupDTO>();
		try {
			if (projectId != null) {
				popupList = iBillingDAO.getPublishBlankBaseLineDatePopup(projectId);
				responseMap.put("popup", popupList);
			} else {
				throw new Exception("Error getting Pub Blank BaseLine Date Popupfor : " + projectId);
			}
		} catch (Exception e) {
			log.error("getPublishBlankBaseLineDatePopup(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getActivitiesPopup(String projectId, String cashMilestoneActivityId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ActivitiesPopupDTO> actPopup = new ArrayList<ActivitiesPopupDTO>();
		try {
			if (projectId != null) {
				actPopup = iBillingDAO.getActivitiesPopup(projectId,
						AssertUtils.validateString(cashMilestoneActivityId));
				responseMap.put("popup", actPopup);
			} else {
				throw new Exception("Error getting Activities Popup for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getActivitiesPopup(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getPublishActivitiesPopup(String projectId, String cashMilestoneActivityId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ActivitiesPopupDTO> actPopup = new ArrayList<ActivitiesPopupDTO>();
		try {
			if (projectId != null) {
				actPopup = iBillingDAO.getPublishActivitiesPopup(projectId,
						AssertUtils.validateString(cashMilestoneActivityId));
				responseMap.put("popup", actPopup);
			} else {
				throw new Exception("Error getting Publish Activities Popup for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getPublishActivitiesPopup(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getAllActivities(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ActivitiesDTO> activitiesList = new ArrayList<ActivitiesDTO>();
		try {
			if (projectId != null) {
				activitiesList = iBillingDAO.getAllActivities(projectId);
				responseMap.put("activitiesList", activitiesList);
			} else {
				throw new Exception("Error getting All Activities for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getAllActivities(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getPublishAllActivities(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<ActivitiesDTO> activitiesList = new ArrayList<ActivitiesDTO>();
		try {
			if (projectId != null) {
				activitiesList = iBillingDAO.getPublishAllActivities(projectId);
				responseMap.put("activitiesList", activitiesList);
			} else {
				throw new Exception("Error getting Publish All Activities for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getPublishActivitiesPopup(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public ResponseEntity<?> exportAllActivitiesExcel(String projectId) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		ExportMilestoneToExcel excelObj = new ExportMilestoneToExcel();
		List<ActivitiesDTO> activityList = new ArrayList<ActivitiesDTO>();
		ResponseEntity<InputStreamResource> responseEntity = null;
		String fileName = "Activities_Details.xlsx";
		FileOutputStream fileOut = null;
		try {
			if (projectId != null) {
				activityList = iBillingDAO.getAllActivities(projectId);
				fileOut = new FileOutputStream(fileName);
				workbook = excelObj.exportAllActivitiesExcel(workbook, activityList);
				workbook.write(fileOut);
				fileOut.flush();
				File file = new File(fileName);
				InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
				HttpHeaders headers = new HttpHeaders();
				headers.add("content-disposition", String.format("attachment; filename=\"%s\"", file.getName()));
				responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
						.contentType(MediaType.parseMediaType("application/text")).body(resource);
			}
		} catch (IOException e) {
			log.error("something went wrong while downloading all milestones excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			try {
				fileOut.close();
				workbook.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return responseEntity;
	}

	@SuppressWarnings("resource")
	@Override
	public ResponseEntity<?> exportPublishAllActivitiesExcel(String projectId) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		ExportMilestoneToExcel excelObj = new ExportMilestoneToExcel();
		List<ActivitiesDTO> activityList = new ArrayList<ActivitiesDTO>();
		ResponseEntity<InputStreamResource> responseEntity = null;
		String fileName = "Activities_Details.xlsx";
		FileOutputStream fileOut = null;
		try {
			if (projectId != null) {
				activityList = iBillingDAO.getPublishAllActivities(projectId);
				fileOut = new FileOutputStream(fileName);
				workbook = excelObj.exportAllActivitiesExcel(workbook, activityList);
				workbook.write(fileOut);
				fileOut.flush();
				File file = new File(fileName);
				InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
				HttpHeaders headers = new HttpHeaders();
				headers.add("content-disposition", String.format("attachment; filename=\"%s\"", file.getName()));
				responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
						.contentType(MediaType.parseMediaType("application/text")).body(resource);
			}
		} catch (IOException e) {
			log.error("something went wrong while downloading all milestones excel:" + e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			try {
				fileOut.close();
				workbook.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return responseEntity;
	}

	@Override
	public Map<String, Object> getMilestoneDescription(String projectId, String cashMilestoneActivityId, String flag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String desc = "";
		try {
			if (projectId != null) {
				if (flag.equalsIgnoreCase("Y")) {
					desc = iBillingDAO.getPubMilestoneDescription(projectId,
							AssertUtils.validateString(cashMilestoneActivityId));
				} else if (flag.equalsIgnoreCase("N")) {
					desc = iBillingDAO.getMilestoneDescription(projectId,
							AssertUtils.validateString(cashMilestoneActivityId));
				} else {
					desc = "";
				}
				responseMap.put("milestoneDesc", desc);
			} else {
				throw new Exception("Error getting get Milestone Description for : " + cashMilestoneActivityId);
			}
		} catch (Exception e) {
			log.error("getMilestoneDescription(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public List<ExchangeRateDTO> getExchangeRate(String projectId) {

		List<ExchangeRateDTO> exchangeRateDTOList = new ArrayList<ExchangeRateDTO>();
		try {
			if (projectId != null) {
				exchangeRateDTOList = iBillingDAO.getExchangeRate(projectId);

			} else {
				throw new Exception("Error getting exchange rate for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getExchangeRate(): Exception occurred : " + e.getMessage());
		}
		return exchangeRateDTOList;
	}

	@Override
	public Map<String, Object> getOpenInvoiceChart(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> chartMap = new HashMap<String, Object>();
		String updateOnDate = "";
		try {
			if (projectId != null) {
				chartMap = iBillingDAO.getOpenInvoiceChart(projectId);
				updateOnDate = iBillingDAO.getLastUpdatedDate(projectId);
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
		List<OpenInvoiceChartPopupDetails> openInvoiceDetailsPopup = new ArrayList<OpenInvoiceChartPopupDetails>();
		try {
			if (projectId != null) {
				openInvoiceDetailsPopup = iBillingDAO.getOpenInvoiceChartPopupDetails(projectId, chartType, statusCode);
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
		List<OpenInvoiceDataTable> tableDetails = new ArrayList<OpenInvoiceDataTable>();
		try {
			if (projectId != null) {
				tableDetails = iBillingDAO.getOpenInvoiceDatatable(projectId);
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
	public Map<String, Object> saveOpenInvoiceDetails(List<SaveOpenInvoiceDTO> invoicesList) {
		boolean resultFlag = false;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String sso = callerContext.getName();
		try {
			resultFlag = iBillingDAO.saveOpenInvoiceDetails(invoicesList, sso);
			if (resultFlag) {
				responseMap.put("status", "success");
				responseMap.put("message", "Data saved successfully.");
				callCashCollectionProcedure();
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

	private void callCashCollectionProcedure() {
		Runnable caller1 = new Runnable() {
			@Override
			public void run() {
				try {
					iBillingDAO.callCashCollectionProcedure();
				} catch (Exception e) {
					log.error("Error while call cash collection procedure :: " + e.getMessage());
				}
			}
		};
		new Thread(caller1).start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCashCollectionReportCurve(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> cashCollectionCurveMap = new HashMap<String, Object>();
		List<ForecastCashDTO> forecastCashCurve = new ArrayList<ForecastCashDTO>();
		List<CollectedCashDTO> collectedCashCurve = new ArrayList<CollectedCashDTO>();
		List<OtrCashBaselineDTO> otrCashBaselineCurve = new ArrayList<OtrCashBaselineDTO>();
		List<ItoCashBaselineDTO> itoCashBaselineCurve = new ArrayList<ItoCashBaselineDTO>();
		List<PastDueCommitmentDTO> pastDueCurve = new ArrayList<PastDueCommitmentDTO>();
		List<CashCollectionCurveTableDTO> tableList = new ArrayList<CashCollectionCurveTableDTO>();
		List<String> xAxis = new ArrayList<String>();
		String forecastCash = "", collectedCash = "", otrCashBaseline = "", itoCashBaseline = "", pastDue = "";
		String lastSavedDate = "";
		try {
			if (projectId != null) {
				boolean checkFlag = false;
				checkFlag = iBillingDAO.checkProjectIsPascalProject(projectId);
				if (checkFlag) {
					log.info("Pascal Project Id :: " + projectId);
					cashCollectionCurveMap = iBillingDAO.getForecastCashCurveForPascalProject(projectId,
							cashCollectionCurveMap);
				} else {
					cashCollectionCurveMap = iBillingDAO.getForecastCashCurve(projectId, cashCollectionCurveMap);
				}
				cashCollectionCurveMap = iBillingDAO.getCollectedCashCurve(projectId, cashCollectionCurveMap);
				cashCollectionCurveMap = iBillingDAO.getOtrCashBaselineCurve(projectId, cashCollectionCurveMap);
				cashCollectionCurveMap = iBillingDAO.getItoCashBaselineCurve(projectId, cashCollectionCurveMap);
				cashCollectionCurveMap = iBillingDAO.getPastDueCurve(projectId, cashCollectionCurveMap);
				lastSavedDate = iBillingDAO.getOpenInvoiceLastSavedDate(projectId);

				if (cashCollectionCurveMap.get("forecastCash") != null) {
					forecastCashCurve = (List<ForecastCashDTO>) cashCollectionCurveMap.get("forecastCash");
				}
				if (cashCollectionCurveMap.get("collectedCash") != null) {
					collectedCashCurve = (List<CollectedCashDTO>) cashCollectionCurveMap.get("collectedCash");
				}
				if (cashCollectionCurveMap.get("otrCashBaseline") != null) {
					otrCashBaselineCurve = (List<OtrCashBaselineDTO>) cashCollectionCurveMap.get("otrCashBaseline");
				}
				if (cashCollectionCurveMap.get("itoCashBaseline") != null) {
					itoCashBaselineCurve = (List<ItoCashBaselineDTO>) cashCollectionCurveMap.get("itoCashBaseline");
				}
				if (cashCollectionCurveMap.get("pastDue") != null) {
					pastDueCurve = (List<PastDueCommitmentDTO>) cashCollectionCurveMap.get("pastDue");
				}

				if (cashCollectionCurveMap.get("tableList") != null) {
					tableList = (List<CashCollectionCurveTableDTO>) cashCollectionCurveMap.get("tableList");
					for (CashCollectionCurveTableDTO obj : tableList) {
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

						if (obj.getItoCashBaseline() != null && !obj.getItoCashBaseline().equalsIgnoreCase("")) {
							itoCashBaseline = obj.getItoCashBaseline();
						} else {
							obj.setItoCashBaseline(itoCashBaseline);
						}
					}
				}

				responseMap.put("forecastCash", forecastCashCurve);
				responseMap.put("collectedCash", collectedCashCurve);
				responseMap.put("otrCashBaseline", otrCashBaselineCurve);
				responseMap.put("itoCashBaseline", itoCashBaselineCurve);
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
			responseMap.put("itoCashBaseline", itoCashBaselineCurve);
			responseMap.put("pastDue", pastDueCurve);
			responseMap.put("xAxis", xAxis);
			responseMap.put("tableList", tableList);
			responseMap.put("lastSavedDate", lastSavedDate);
			log.error("getCashCollectionReportCurve(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getAllInvoicesDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<CashCollectionInvoicesDetails> allInvoicesList = new ArrayList<CashCollectionInvoicesDetails>();
		try {
			if (projectId != null) {
				boolean checkFlag = false;
				checkFlag = iBillingDAO.checkProjectIsPascalProject(projectId);
				if (checkFlag) {
					log.info("Pascal Project Id :: " + projectId);
					allInvoicesList = iBillingDAO.getAllInvoicesDetailsForPascalProject(projectId);
				} else {
					allInvoicesList = iBillingDAO.getAllInvoicesDetails(projectId);
				}
				responseMap.put("details", allInvoicesList);
			} else {
				throw new Exception("getAllInvoicesDetails(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("getAllInvoicesDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	public Map<String, Object> getVorFilter(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> filterList = new ArrayList<DropDownDTO>();
		try {
			if (projectId != null) {
				filterList = iBillingDAO.getVorFilter(projectId);
				responseMap.put("filterList", filterList);
			} else {
				throw new Exception("getVorFilter(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("getVorFilter(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getPoDetails(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		POInvoiceDetailDTO data = new POInvoiceDetailDTO();
		List<InvoiceDetailDTO> invoiceDetail = new ArrayList<InvoiceDetailDTO>();
		PODetailsDTO poDetailDto = new PODetailsDTO();
		List<PODetailsDTO> poDetail = new ArrayList<PODetailsDTO>();
		String updateOnDate = "";
		try {
			if (projectId != null) {
				updateOnDate = iBillingDAO.getPOLastUpdatedDate(projectId);
				poDetailDto = iBillingDAO.getPODetails(projectId);
				invoiceDetail = iBillingDAO.getPOInvoiceDetails(projectId);
				poDetail.add(poDetailDto);
				data.setPoDetail(poDetail);
				data.setLastUpdatedDate(updateOnDate);
				data.setInvoiceDetails(invoiceDetail);
				responseMap.put("data", data);
			} else {
				throw new Exception("getPoDetails(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("getPoDetails(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] downloadOpenInvoicesDetails(String projectId) {
		ExportOpenInvoicesExcel exportOpenInvoicesExcel = new ExportOpenInvoicesExcel();
		List<OpenInvoiceDataTable> list = new ArrayList<OpenInvoiceDataTable>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		try {
			if (projectId != null) {
				list = iBillingDAO.getOpenInvoiceDatatable(projectId);
				log.info("Creating Open Invoices Sheet with " + list.size() + " rows.");
				exportOpenInvoicesExcel.exportOpenInvoicesDetailsExcel(workbook, list);
				workbook.write(bos);
				excelData = bos.toByteArray();
			}
		} catch (Exception e) {
			log.error("Error occured when downloading Open Invoices details" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading Open Invoices details" + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public byte[] downloadAllInvoiceDetails(String projectId) {
		log.debug("INIT- downloadAllInvoiceDetails for projectId : {}", projectId);
		ExportOpenInvoicesExcel exportAllInvoicesExcel = new ExportOpenInvoicesExcel();
		List<CashCollectionInvoicesDetails> allInvoicesList = new ArrayList<CashCollectionInvoicesDetails>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		try {
			if (projectId != null) {
				boolean checkFlag = false;
				checkFlag = iBillingDAO.checkProjectIsPascalProject(projectId);
				if (checkFlag) {
					log.info("Pascal Project Id :: " + projectId);
					allInvoicesList = iBillingDAO.getAllInvoicesDetailsForPascalProject(projectId);
				} else {
					allInvoicesList = iBillingDAO.getAllInvoicesDetails(projectId);
				}
				log.info("Creating all Invoices Sheet with " + allInvoicesList.size() + " rows.");
				exportAllInvoicesExcel.exportAllInvoicesDetailsExcel(workbook, allInvoicesList);
				workbook.write(bos);
				excelData = bos.toByteArray();
			} else {
				throw new Exception("getAllInvoicesDetails(): Exception occurred : " + projectId);
			}
		} catch (Exception e) {
			log.error("Error occured when downloading all Invoices details" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading all Invoices details" + e.getMessage());
			}
		}
		log.debug("END- downloadAllInvoiceDetails for projectId : {}", projectId);
		return excelData;
	}

}
