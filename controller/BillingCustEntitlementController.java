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

import com.bh.realtrack.service.IBillingCustEntitlementService;
import com.bh.realtrack.util.AssertUtils;

@RestController
@CrossOrigin
public class BillingCustEntitlementController {
	private static final Logger log = LoggerFactory.getLogger(BillingCustEntitlementController.class);

	@Autowired
	private IBillingCustEntitlementService iBillingCustEntitlementService;

	@RequestMapping(value = "/getCustomerEntAnalysis", method = RequestMethod.GET)
	public Map<String, Object> getCustomerEntAnalysis(@RequestParam String projectId) {
		return iBillingCustEntitlementService.getCustomerEntAnalysis(projectId);
	}

	@RequestMapping(value = "/getCustomerEntClosurePlan", method = RequestMethod.GET)
	public Map<String, Object> getCustomerEntClosurePlan(@RequestParam String projectId) {
		return iBillingCustEntitlementService.getCustomerEntClosurePlan(projectId);
	}

	@RequestMapping(value = "/getCustomerEntDetailsPopup", method = RequestMethod.GET)
	public Map<String, Object> getCustomerEntDetailsPopup(@RequestParam String projectId,
			@RequestParam String chartType, @RequestParam String xAxis, @RequestParam String yAxis) {
		return iBillingCustEntitlementService.getCustomerEntDetailsPopup(projectId, chartType, xAxis, yAxis);
	}

	@RequestMapping(value = "/downloadCustomerEntDetails", method = RequestMethod.GET)
	public void downloadCustomerEntDetails(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = "Customer_Entitlement_Details_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] customerEntDetails = iBillingCustEntitlementService.downloadCustomerEntDetails(projectId);
			IOUtils.write(customerEntDetails, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured while downloading Customer Entitlement Excel file :: {}" , e.getMessage());
		}
	}
}
