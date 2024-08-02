package com.bh.realtrack.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.ExchangeRateDTO;
import com.bh.realtrack.dto.MilestonesDTO;
import com.bh.realtrack.dto.SaveOpenInvoiceDTO;
import com.bh.realtrack.service.IBillingService;

@RestController
@CrossOrigin
public class BillingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingController.class);

	@Autowired
	private IBillingService iBillingService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String ping() {
		return "success from billing services";
	}

	@RequestMapping(value = "/getBillingSummary", method = RequestMethod.GET)
	public Map<String, Object> getBillingSummary(@RequestParam String projectId) {
		return iBillingService.getBillingSummary(projectId);
	}

	@RequestMapping(value = "/getNextToBillPopup", method = RequestMethod.GET)
	public Map<String, Object> getNextToBillPopup(@RequestParam String projectId) {
		return iBillingService.getNextToBillPopup(projectId);
	}

	@RequestMapping(value = "/getMilestonesToBill", method = RequestMethod.GET)
	public Map<String, Object> getMilestonesToBill(@RequestParam String projectId) {
		return iBillingService.getMilestonesToBill(projectId);
	}

	@RequestMapping(value = "/getAllMilestones", method = RequestMethod.GET)
	public Map<String, Object> getAllMilestones(@RequestParam String projectId) {
		return iBillingService.getAllMilestones(projectId);
	}

	@RequestMapping(value = "/getBillingReport", method = RequestMethod.GET)
	public Map<String, Object> getBillingReport(@RequestParam String projectId) {
		return iBillingService.getBillingReport(projectId);
	}

	@RequestMapping(value = "/updateMilestoneDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> updateMilestoneDetails(@RequestHeader HttpHeaders headers,
			@RequestBody List<MilestonesDTO> milestonesList) {
		return new ResponseEntity<Map<String, Object>>(iBillingService.updateMilestoneDetails(milestonesList),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/publishReportDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> publishMilestoneDetails(@RequestParam String projectId,
			@RequestBody List<MilestonesDTO> milestonesList) {
		return new ResponseEntity<Map<String, Object>>(
				iBillingService.publishMilestoneDetails(projectId, milestonesList), HttpStatus.OK);
	}

	@RequestMapping(value = "/downloadAllMilestoneDetails", method = RequestMethod.GET)
	public ResponseEntity<?> exportAllMilestoneDetailsExcel(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers) {
		return iBillingService.exportAllMilestoneDetailsExcel(projectId);
	}

	@RequestMapping(value = "/getPublishBillingSummary", method = RequestMethod.GET)
	public Map<String, Object> getPublishBillingSummary(@RequestParam String projectId) {
		return iBillingService.getPublishBillingSummary(projectId);
	}

	@RequestMapping(value = "/getPublishNextToBillPopup", method = RequestMethod.GET)
	public Map<String, Object> getPublishNextToBillPopup(@RequestParam String projectId) {
		return iBillingService.getPublishNextToBillPopup(projectId);
	}

	@RequestMapping(value = "/getPublishMilestonesToBill", method = RequestMethod.GET)
	public Map<String, Object> getPublishMilestonesToBill(@RequestParam String projectId) {
		return iBillingService.getPublishMilestonesToBill(projectId);
	}

	@RequestMapping(value = "/getPublishAllMilestones", method = RequestMethod.GET)
	public Map<String, Object> getPublishAllMilestones(@RequestParam String projectId) {
		return iBillingService.getPublishAllMilestones(projectId);
	}

	@RequestMapping(value = "/getPublishBillingReport", method = RequestMethod.GET)
	public Map<String, Object> getPublishBillingReport(@RequestParam String projectId) {
		return iBillingService.getPublishBillingReport(projectId);
	}

	@RequestMapping(value = "/downloadPublishAllMilestoneDetails", method = RequestMethod.GET)
	public ResponseEntity<?> exportPublishAllMilestoneDetailsExcel(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers) {
		return iBillingService.exportPublishAllMilestoneDetailsExcel(projectId);
	}

	@RequestMapping(value = "/getBlankBaseLineDatePopup", method = RequestMethod.GET)
	public Map<String, Object> getBlankBaseLineDatePopup(@RequestParam String projectId) {
		return iBillingService.getBlankBaseLineDatePopup(projectId);
	}

	@RequestMapping(value = "/getPublishBlankBaseLineDatePopup", method = RequestMethod.GET)
	public Map<String, Object> getPublishBlankBaseLineDatePopup(@RequestParam String projectId) {
		return iBillingService.getPublishBlankBaseLineDatePopup(projectId);
	}

	@RequestMapping(value = "/getActivitiesPopup", method = RequestMethod.GET)
	public Map<String, Object> getActivitiesPopup(@RequestParam String projectId, String cashMilestoneActivityId) {
		return iBillingService.getActivitiesPopup(projectId, cashMilestoneActivityId);
	}

	@RequestMapping(value = "/getPublishActivitiesPopup", method = RequestMethod.GET)
	public Map<String, Object> getPublishActivitiesPopup(@RequestParam String projectId,
			String cashMilestoneActivityId) {
		return iBillingService.getPublishActivitiesPopup(projectId, cashMilestoneActivityId);
	}

	@RequestMapping(value = "/getAllActivities", method = RequestMethod.GET)
	public Map<String, Object> getAllActivities(@RequestParam String projectId) {
		return iBillingService.getAllActivities(projectId);
	}

	@RequestMapping(value = "/getPublishAllActivities", method = RequestMethod.GET)
	public Map<String, Object> getPublishAllActivities(@RequestParam String projectId) {
		return iBillingService.getPublishAllActivities(projectId);
	}

	@RequestMapping(value = "/downloadAllActivities", method = RequestMethod.GET)
	public ResponseEntity<?> exportAllActivitiesExcel(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers) {
		return iBillingService.exportAllActivitiesExcel(projectId);
	}

	@RequestMapping(value = "/downloadPublishAllActivities", method = RequestMethod.GET)
	public ResponseEntity<?> exportPublishAllActivitiesExcel(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers) {
		return iBillingService.exportPublishAllActivitiesExcel(projectId);
	}

	@RequestMapping(value = "/getMilestoneDesc", method = RequestMethod.GET)
	public Map<String, Object> getMilestoneDescription(@RequestParam String projectId, String cashMilestoneActivityId,
			String flag) {
		return iBillingService.getMilestoneDescription(projectId, cashMilestoneActivityId, flag);
	}

	@RequestMapping(value = "/getExchangeRate", method = RequestMethod.GET)
	public List<ExchangeRateDTO> getExchangeRate(@RequestParam String projectId) {
		return iBillingService.getExchangeRate(projectId);
	}

	@RequestMapping(value = "/getOpenInvoiceChart", method = RequestMethod.GET)
	public Map<String, Object> getOpenInvoiceChart(@RequestParam String projectId) {
		return iBillingService.getOpenInvoiceChart(projectId);
	}

	@GetMapping("/getOpenInvoiceChartPopupDetails")
	public Map<String, Object> getOpenInvoiceChartPopupDetails(@RequestParam String projectId,
			@RequestParam String chartType, @RequestParam String statusCode) {
		return iBillingService.getOpenInvoiceChartPopupDetails(projectId, chartType, statusCode);
	}

	@GetMapping("/getOpenInvoiceDatatable")
	public Map<String, Object> getOpenInvoiceDatatable(@RequestParam String projectId) {
		return iBillingService.getOpenInvoiceDatatable(projectId);
	}

	@RequestMapping(value = "/saveOpenInvoiceDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> saveOpenInvoiceDetails(@RequestHeader HttpHeaders headers,
			@RequestBody List<SaveOpenInvoiceDTO> invoicesList) {
		return iBillingService.saveOpenInvoiceDetails(invoicesList);
	}

	@GetMapping("/getCashCollectionReportCurve")
	public Map<String, Object> getCashCollectionReportCurve(@RequestParam String projectId) {
		return iBillingService.getCashCollectionReportCurve(projectId);
	}

	@GetMapping("/getAllInvoicesDetails")
	public Map<String, Object> getAllInvoicesDetails(@RequestParam String projectId) {
		return iBillingService.getAllInvoicesDetails(projectId);
	}

	@RequestMapping(value = "/getVorFilter", method = RequestMethod.GET)
	public Map<String, Object> getVorFilter(@RequestParam String projectId) {
		return iBillingService.getVorFilter(projectId);
	}

	@RequestMapping(value = "/getPoDetails", method = RequestMethod.GET)
	public Map<String, Object> getPoDetails(@RequestParam String projectId) {
		return iBillingService.getPoDetails(projectId);
	}
	
	@RequestMapping(value = "/downloadOpenInvoiceDetails", method = RequestMethod.GET)
	public void downloadBillingDetails(@RequestParam String projectId, HttpServletResponse response) throws Exception {
		String fileName = "Open_Invoices_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iBillingService.downloadOpenInvoicesDetails(projectId);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading open invoices details :: {}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading open invoices details :: {}" , e.getStackTrace());
			}
		}
	}
	
	@RequestMapping(value = "/downloadAllInvoiceDetails", method = RequestMethod.GET)
	public void downloadBillingAllInvoiceDetails(@RequestParam String projectId, HttpServletResponse response)
			throws Exception {
		LOGGER.debug("INIT- downloadAllInvoiceDetails for projectId : {}", projectId);
		String fileName = "All_Invoices_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iBillingService.downloadAllInvoiceDetails(projectId);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading all invoices details :: {}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading all invoices details :: {}" , e.getStackTrace());
			}
		}
	}
}
