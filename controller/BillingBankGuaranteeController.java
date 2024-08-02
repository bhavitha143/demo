package com.bh.realtrack.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.service.IBillingBankGuaranteeService;
import com.bh.realtrack.util.AssertUtils;

@RestController
@CrossOrigin
public class BillingBankGuaranteeController {
	private static final Logger log = LoggerFactory.getLogger(BillingBankGuaranteeController.class);

	@Autowired
	private IBillingBankGuaranteeService iBillingBankGuaranteeService;

	@RequestMapping(value = "/getBankGuaranteeSummary", method = RequestMethod.GET)
	public Map<String, Object> getBankGuaranteeSummary(@RequestParam String projectId) {
		return iBillingBankGuaranteeService.getBankGuaranteeSummary(projectId);
	}

	@RequestMapping(value = "/getBankGuaranteePieChart", method = RequestMethod.GET)
	public Map<String, Object> getBankGuaranteePieChart(@RequestParam String projectId) {
		return iBillingBankGuaranteeService.getBankGuaranteePieChart(projectId);
	}

	@RequestMapping(value = "/getBankGuaranteePopupDetails", method = RequestMethod.GET)
	public Map<String, Object> getBankGuaranteePopupDetails(@RequestParam String projectId,
			@RequestParam String chartType, @RequestParam String status) {
		return iBillingBankGuaranteeService.getBankGuaranteePopupDetails(projectId, chartType, status);
	}

	@RequestMapping(value = "/downloadBankGuaranteeDetailsExcel", method = RequestMethod.GET)
	public void downloadBankGuaranteeDetailsExcel(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = "Bank_Guarantee_Details_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iBillingBankGuaranteeService.downloadBankGuaranteeDetailsExcel(projectId);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading bank guarantee excel file{}" , e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading bank guarantee excel file{}" , e.getMessage());
			}
		}
	}
}
