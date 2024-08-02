package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.IBillingCustEntitlementDAO;
import com.bh.realtrack.dto.CustEntitlementChartPopupDTO;
import com.bh.realtrack.excel.ExportCustomerEntitlementExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IBillingCustEntitlementService;

@Service
public class BillingCustEntitlementServiceImpl implements IBillingCustEntitlementService {

	private static final Logger log = LoggerFactory.getLogger(BillingCustEntitlementServiceImpl.class);

	private IBillingCustEntitlementDAO iBillingCustEntitlementDAO;
	private CallerContext callerContext;

	@Autowired
	public BillingCustEntitlementServiceImpl(IBillingCustEntitlementDAO iBillingCustEntitlementDAO,
			CallerContext callerContext) {
		this.iBillingCustEntitlementDAO = iBillingCustEntitlementDAO;
		this.callerContext = callerContext;
	}

	@Override
	public Map<String, Object> getCustomerEntAnalysis(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String updatedOn = "";
		try {
			if (projectId != null) {
				updatedOn = iBillingCustEntitlementDAO.getCustEntAnalysisUpdatedOn(projectId);
				dataMap = iBillingCustEntitlementDAO.getCustomerEntAnalysis(projectId);
				responseMap.put("updatedOn", updatedOn);
				responseMap.put("data", dataMap);
			} else {
				throw new Exception("Error getting billing customer entitlement analysis for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getCustomerEntAnalysis(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getCustomerEntClosurePlan(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String updatedOn = "";
		try {
			if (projectId != null) {
				updatedOn = iBillingCustEntitlementDAO.getCustEntClosurePlanUpdatedOn(projectId);
				dataMap = iBillingCustEntitlementDAO.getCustEntClosurePlan(projectId);
				responseMap.put("updatedOn", updatedOn);
				responseMap.put("data", dataMap);
			} else {
				throw new Exception("Error getting billing customer entitlement closure plan for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getCustomerEntClosurePlan(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getCustomerEntDetailsPopup(String projectId, String chartType, String xAxis,
			String yAxis) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<CustEntitlementChartPopupDTO> list = new ArrayList<CustEntitlementChartPopupDTO>();
		try {
			if (projectId != null) {
				if (chartType.equalsIgnoreCase("ENT_ANALYSIS")) {
					list = iBillingCustEntitlementDAO.getCustomerEntAnalysisPopup(projectId, chartType, xAxis, yAxis);
				} else if (chartType.equalsIgnoreCase("CLOSURE_PLAN")) {
					list = iBillingCustEntitlementDAO.getCustEntClosurePlanPopup(projectId, chartType, xAxis, yAxis);
				}
				responseMap.put("data", list);
			} else {
				throw new Exception(
						"Error getting billing customer entitlement : " + chartType + " popup for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getCustomerEntDetailsPopup(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@SuppressWarnings("resource")
	@Override
	public byte[] downloadCustomerEntDetails(String projectId) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = null;
		ExportCustomerEntitlementExcel exportCustomerEntDetailsExcel = new ExportCustomerEntitlementExcel();
		List<CustEntitlementChartPopupDTO> customerEntDetails = new ArrayList<CustEntitlementChartPopupDTO>();
		byte[] excelData = null;
		try {
			workbook = new XSSFWorkbook();
			customerEntDetails = iBillingCustEntitlementDAO.getCustomerEntDetails(projectId);
			workbook = exportCustomerEntDetailsExcel.downloadCustomerEntDetails(workbook, customerEntDetails);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occurred while downloading Customer Entitlement Excel file :: {}" , e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occurred while downloading Customer Entitlement Excel file :: {}" , e.getMessage());
			}
		}
		return excelData;
	}
}
