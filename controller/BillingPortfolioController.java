package com.bh.realtrack.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.FormParam;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bh.realtrack.dto.BillingCycleRemarksDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.ExceptionRemarksDTO;
import com.bh.realtrack.dto.UpdateDetailsDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.service.IBillingPortfolioService;

@RestController
@CrossOrigin
public class BillingPortfolioController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingPortfolioController.class);

	@Autowired
	private IBillingPortfolioService iBillingService;

	@RequestMapping(value = "/getBillingDropDownValues", method = RequestMethod.GET)
	public Map<String, Object> getBillingDropDownValues(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region) {
		return iBillingService.getBillingDropDown(customerId, companyId, business, segment, region);
	}

	@RequestMapping(value = "/getBillingCount", method = RequestMethod.GET)
	public Map<String, Object> getBillingCount(@RequestParam int customerId, @RequestParam("company-id") int companyId,
			@RequestParam("business-unit") String business, @RequestParam String segment, @RequestParam String region,
			@RequestParam String pmLeader, @RequestParam String spm, @RequestParam String financialSegment,
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam String currentYearFlag)
			throws Exception {
		return iBillingService.getBillingCount(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getSegmentSummary", method = RequestMethod.GET)
	public Map<String, Object> getSegmentSummary(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String region, @RequestParam String pmLeader, @RequestParam String spm,
			@RequestParam String financialSegment, @RequestParam String startDate, @RequestParam String endDate,
			@RequestParam String currentYearFlag) throws ParseException {
		return iBillingService.getSegmentSummary(customerId, companyId, business, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getAllBillingTab", method = RequestMethod.GET)
	public Map<String, Object> getAllBillingTab(@RequestParam int customerId, @RequestParam("company-id") int companyId,
			@RequestParam("business-unit") String business, @RequestParam String segment, @RequestParam String region,
			@RequestParam String pmLeader, @RequestParam String spm, @RequestParam String financialSegment,
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam String currentYearFlag)
			throws ParseException {
		return iBillingService.getAllBillingTab(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getBillingCurveTab", method = RequestMethod.GET)
	public Map<String, Object> getBillingCurveTab(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag) throws ParseException {
		return iBillingService.getBillingCurveTab(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getBillingPercentagePopUp", method = RequestMethod.GET)
	public Map<String, Object> getBillingPercentagePopUp(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag) throws Exception {
		return iBillingService.getBillingPercentagePopUp(customerId, companyId, business, segment, region, pmLeader,
				spm, financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getBillingGapRecovery", method = RequestMethod.GET)
	public Map<String, Object> getBillingGapRecovery(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag) throws Exception {
		return iBillingService.getBillingGapRecovery(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getBillingCurrentGap", method = RequestMethod.GET)
	public Map<String, Object> getBillingCurrentGap(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag) throws Exception {
		return iBillingService.getBillingCurrentGap(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getlastUpdateDate", method = RequestMethod.GET)
	public Map<String, Object> getlastUpdateDate() throws Exception {
		return iBillingService.getlastUpdateDate();
	}

	@RequestMapping(value = "/getAllWeeks", method = RequestMethod.GET)
	public Map<String, Object> getAllWeeks(@RequestParam int customerId, @RequestParam("company-id") int companyId,
			@RequestParam("business-unit") String business, @RequestParam String segment, @RequestParam String region,
			@RequestParam String pmLeader, @RequestParam String spm, @RequestParam String financialSegment,
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam String currentYearFlag)
			throws Exception {
		return iBillingService.getAllWeeks(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getBillingKpi", method = RequestMethod.GET)
	public Map<String, Object> getBillingKpi(@RequestParam int customerId, @RequestParam("company-id") int companyId,
			@RequestParam("business-unit") String business, @RequestParam String segment, @RequestParam String region,
			@RequestParam String pmLeader, @RequestParam String spm, @RequestParam String financialSegment,
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam String currentYearFlag)
			throws ParseException {
		return iBillingService.getBillingKpi(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getBillingExceptions", method = RequestMethod.GET)
	public Map<String, Object> getBillingExceptions(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag) throws ParseException {
		return iBillingService.getBillingExceptions(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getOverallBillingCycleDetail", method = RequestMethod.GET)
	public Map<String, Object> getOverallBillingCycleDetail(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag) throws ParseException {
		return iBillingService.getOverallBillingCycleDetail(customerId, companyId, business, segment, region, pmLeader,
				spm, financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getBillingInvoiceDetails", method = RequestMethod.GET)
	public Map<String, Object> getBillingInvoiceDetails(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag) throws ParseException {
		return iBillingService.getBillingInvoiceDetails(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getExceptionCategory", method = RequestMethod.GET)
	public Map<String, Object> getExceptionCategory(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag) throws ParseException {
		return iBillingService.getExceptionCategory(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/downloadBillingDetails", method = RequestMethod.GET)
	public void downloadBillingDetails(@RequestParam int customerId, @RequestParam("company-id") int companyId,
			@RequestParam("business-unit") String business, @RequestParam String segment, @RequestParam String region,
			@RequestParam String pmLeader, @RequestParam String spm, @RequestParam String financialSegment,
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam String currentYearFlag,
			HttpServletResponse response) throws Exception {

		String fileName = "Billing_Invoice_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iBillingService.downloadBillingDetails(customerId, companyId, business, segment, region,
					pmLeader, spm, financialSegment, startDate, endDate, currentYearFlag);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading Billing details {}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading Billing details {}" , e.getStackTrace());
			}
		}

	}

	@RequestMapping(value = "/getMonthlyBillingKpi", method = RequestMethod.GET)
	public Map<String, Object> getMonthlyBillingKpi(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag) throws ParseException {
		return iBillingService.getMonthlyBillingKpi(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getQuarterlyBillingKpi", method = RequestMethod.GET)
	public Map<String, Object> getQuartelyBillingKpi(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag) throws ParseException {
		return iBillingService.getQuarterlyBillingKpi(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment, startDate, endDate, currentYearFlag);
	}

	@RequestMapping(value = "/getTPSBillingReportURI", method = RequestMethod.GET)
	public Map<String, Object> getTPSBillingReportURI() {
		return iBillingService.getTPSBillingReportURI();
	}

	@RequestMapping(value = "/downloadBillingInvoiceDetailsPdf", method = RequestMethod.GET)
	public void downloadBillingInvoiceDetailsPdf(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag, HttpServletResponse response)
			throws Exception {
		String fileName = "Billing_Invoice_Details.pdf";
		response.setContentType("application/pdf");
		response.setHeader("content-disposition", "attachment; filename=" + fileName);
		try {
			byte[] pdfData = iBillingService.downloadBillingInvoiceDetailsPdf(customerId, companyId, business, segment,
					region, pmLeader, spm, financialSegment, startDate, endDate, currentYearFlag);
			IOUtils.write(pdfData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading Billing details PDF {}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading Billing details PDF {}" , e.getStackTrace());
			}
		}
	}

	@RequestMapping(value = "/getBillingInvoiceRemarksList", method = RequestMethod.GET)
	public Map<String, Object> getBillingInvoiceRemarksList() {
		return iBillingService.getBillingInvoiceRemarksList();
	}

	@RequestMapping(value = "/saveBillingMilestoneRemarkDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> saveBillingMilestoneRemarkDetails(@RequestHeader HttpHeaders headers,
			@RequestBody List<BillingCycleRemarksDTO> invoiceList) {
		return iBillingService.saveBillingMilestoneRemarkDetails(invoiceList);
	}

	@RequestMapping(value = "/getExceptionRemarksList", method = RequestMethod.GET)
	public Map<String, Object> getExceptionRemarksList() {
		return iBillingService.getExceptionRemarksList();
	}

	@RequestMapping(value = "/saveBillingExceptionRemarkDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> saveBillingExceptionRemarkDetails(@RequestHeader HttpHeaders headers,
			@RequestBody List<ExceptionRemarksDTO> exceptionList) {
		return iBillingService.saveBillingExceptionRemarkDetails(exceptionList);
	}

	@RequestMapping(value = "/getBillingActivitiesDetails", method = RequestMethod.GET)
	public Map<String, Object> getBillingActivitiesDetails(@RequestParam String projectId,
			@RequestParam String cashMilestoneId) {
		return iBillingService.getBillingActivitiesDetails(projectId, cashMilestoneId);
	}

	@RequestMapping(value = "/getBillingLinearityChart", method = RequestMethod.GET)
	public Map<String, Object> getBillingLinearityChart(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment) throws ParseException {
		return iBillingService.getBillingLinearityChart(customerId, companyId, business, segment, region, pmLeader, spm,
				financialSegment);
	}

	@RequestMapping(value = "/getBillingLinearityChartPopup", method = RequestMethod.GET)
	public Map<String, Object> getBillingLinearityChartPopup(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String quarterYear,
			@RequestParam String month) throws ParseException {
		return iBillingService.getBillingLinearityChartPopup(customerId, companyId, business, segment, region, pmLeader,
				spm, financialSegment, quarterYear, month);
	}

	@RequestMapping(value = "/downloadBillingLinearityExcel", method = RequestMethod.GET)
	public void downloadBillingLinearityExcel(@RequestParam int customerId, @RequestParam("company-id") int companyId,
			@RequestParam("business-unit") String business, @RequestParam String segment, @RequestParam String region,
			@RequestParam String pmLeader, @RequestParam String spm, @RequestParam String financialSegment,
			HttpServletResponse response) throws Exception {
		String fileName = "Billing_Linearity_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iBillingService.downloadBillingLinearityExcel(customerId, companyId, business, segment,
					region, pmLeader, spm, financialSegment);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading Billing Linearity details {}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading Billing Linearity details{}" , e.getStackTrace());
			}
		}
	}

	@RequestMapping(value = "/downloadBillingMilestoneDetailsExcel", method = RequestMethod.GET)
	public void downloadBillingMilestoneDetailsExcel(@RequestParam int customerId,
			@RequestParam("company-id") int companyId, @RequestParam("business-unit") String business,
			@RequestParam String segment, @RequestParam String region, @RequestParam String pmLeader,
			@RequestParam String spm, @RequestParam String financialSegment, @RequestParam String startDate,
			@RequestParam String endDate, @RequestParam String currentYearFlag, HttpServletResponse response)
			throws Exception {
		String fileName = "Billing_Milestone_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iBillingService.downloadBillingMilestoneDetails(customerId, companyId, business, segment,
					region, pmLeader, spm, financialSegment, startDate, endDate, currentYearFlag);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading Billing Milestone Tab details{}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading Billing Milestone Tab details{}" , e.getStackTrace());
			}
		}
	}

	@RequestMapping(value = "/downloadBillingDetailsExcel", method = RequestMethod.GET)
	public void downloadBillingDetailsExcel(@RequestParam int customerId, @RequestParam("company-id") int companyId,
			@RequestParam("business-unit") String business, @RequestParam String segment, @RequestParam String region,
			@RequestParam String pmLeader, @RequestParam String spm, @RequestParam String financialSegment,
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam String currentYearFlag,
			HttpServletResponse response) throws Exception {
		String fileName = "Billing_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iBillingService.downloadBillingDetailsExcel(customerId, companyId, business, segment,
					region, pmLeader, spm, financialSegment, startDate, endDate, currentYearFlag);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading Billing Dashboard Details {}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading Billing Dashboard Details{}" , e.getStackTrace());
			}
		}
	}

	@RequestMapping(value = "/getBillingProjectOutOfRTUploadStatus", method = RequestMethod.GET)
	public UpdateDetailsDTO getBillingProjectOutOfRTUploadStatus(@RequestParam final String companyId,
			@RequestHeader final HttpHeaders headers) {
		return iBillingService.getBillingProjectOutOfRTUploadStatus(companyId);
	}

	@RequestMapping(value = "/getBillingNotProcessedProjectDetails", method = RequestMethod.GET)
	public Map<String, List<ErrorDetailsDTO>> getNotProcessedProjectDetails(@RequestParam final String companyId,
			@RequestHeader final HttpHeaders headers) {
		Map<String, List<ErrorDetailsDTO>> responseMap = new HashMap<String, List<ErrorDetailsDTO>>();
		List<ErrorDetailsDTO> errorDTOList = iBillingService.getBillingNotProcessedProjectDetails(companyId);
		responseMap.put("errorDetails", errorDTOList);
		return responseMap;
	}

	@RequestMapping(value = "/uploadBillingProjectOutOfRT", method = RequestMethod.POST)
	public Map<String, String> uploadProjectOutOfRT(@RequestParam final String companyId,
			@FormParam("excelFile") final MultipartFile excelFile, @RequestHeader final HttpHeaders headers) {
		try {
			return iBillingService.uploadBillingProjectOutOfRT(companyId, excelFile);
		} catch (Exception e) {
			LOGGER.error("Error while uploading Billing Out of RT Project Excel file :: {}" , e.getMessage());
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
	}

	@GetMapping("/downloadBillingOutOfProjectTemplate")
	public void downloadBillingOutOfProjectTemplate(@RequestParam final String companyId,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = "OUT_OF_RT_PROJECT_DETAILS.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iBillingService.downloadBillingOutOfProjectTemplate(companyId);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading Billing Out of RT Project excel file {}" , e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading Billing Out of RT Project excel file {}" , e.getMessage());
			}
		}
	}

}
