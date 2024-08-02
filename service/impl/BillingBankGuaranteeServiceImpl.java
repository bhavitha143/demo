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

import com.bh.realtrack.dao.IBillingBankGuaranteeDAO;
import com.bh.realtrack.dto.BankGuaranateeSummaryDetailsDTO;
import com.bh.realtrack.dto.BankGuaranteePopupDetailsDTO;
import com.bh.realtrack.dto.ConfigurationDTO;
import com.bh.realtrack.excel.ExportBankGuaranteeExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.IBillingBankGuaranteeService;

@Service
public class BillingBankGuaranteeServiceImpl implements IBillingBankGuaranteeService {

	private static final Logger log = LoggerFactory.getLogger(BillingBankGuaranteeServiceImpl.class);

	private IBillingBankGuaranteeDAO iBillingBankGuaranteeDAO;
	private CallerContext callerContext;

	@Autowired
	public BillingBankGuaranteeServiceImpl(IBillingBankGuaranteeDAO iBillingBankGuaranteeDAO,
			CallerContext callerContext) {
		this.iBillingBankGuaranteeDAO = iBillingBankGuaranteeDAO;
		this.callerContext = callerContext;
	}

	@Override
	public Map<String, Object> getBankGuaranteeSummary(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String updatedOn = "";
		BankGuaranateeSummaryDetailsDTO summaryDTO = new BankGuaranateeSummaryDetailsDTO();
		try {
			if (projectId != null) {
				updatedOn = iBillingBankGuaranteeDAO.getBankGuaranteeUpdatedOn(projectId);
				summaryDTO = iBillingBankGuaranteeDAO.getBankGuaranteeSummary(projectId);
				responseMap.put("summary", summaryDTO);
				responseMap.put("lastUpdatedOn", updatedOn);
			} else {
				throw new Exception("Error getting billing bank guarantee summary counts for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getBankGuaranteeSummary(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getBankGuaranteePieChart(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> bgAmtConnectedPieChart = new HashMap<String, Object>();
		Map<String, Object> bgAmtIssuedByTypePieChart = new HashMap<String, Object>();
		Map<String, Object> bgAmtToRecoverPieChart = new HashMap<String, Object>();
		List<ConfigurationDTO> bgAmtToRecoverPieChartConfig = new ArrayList<ConfigurationDTO>();
		String updatedOn = "";
		try {
			if (projectId != null) {
				updatedOn = iBillingBankGuaranteeDAO.getBankGuaranteeUpdatedOn(projectId);
				bgAmtConnectedPieChart = iBillingBankGuaranteeDAO.getBankGuaranteeBgAmtConnectedPieChart(projectId);
				bgAmtIssuedByTypePieChart = iBillingBankGuaranteeDAO
						.getBankGuaranteeBgAmtIssuedByTypePieChart(projectId);
				bgAmtToRecoverPieChart = iBillingBankGuaranteeDAO.getBankGuaranteeBgAmtToRecoverPieChart(projectId);
				bgAmtToRecoverPieChartConfig = getBgAmtToRecoverPieChartConfiguration();
				responseMap.put("bgAmtConnected", bgAmtConnectedPieChart);
				responseMap.put("bgAmtIssuedByType", bgAmtIssuedByTypePieChart);
				responseMap.put("bgAmtToRecover", bgAmtToRecoverPieChart);
				responseMap.put("bgAmtToRecoverPieChartConfig", bgAmtToRecoverPieChartConfig);
				responseMap.put("lastUpdatedOn", updatedOn);
			} else {
				throw new Exception("Error getting billing bank guarantee pie charts for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getBankGuaranteePieChart(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	public List<ConfigurationDTO> getBgAmtToRecoverPieChartConfiguration() {
		List<ConfigurationDTO> list = new ArrayList<ConfigurationDTO>();

		ConfigurationDTO openEndedExpired = new ConfigurationDTO();
		openEndedExpired.setLegendName("Open Ended Expired");
		openEndedExpired.setKey("openEndedExpired");
		openEndedExpired.setStatusKey("OPEN_ENDED_EXPIRED");
		openEndedExpired.setColor("#008a9c");
		list.add(openEndedExpired);

		ConfigurationDTO agedBG = new ConfigurationDTO();
		agedBG.setLegendName("Aged BG");
		agedBG.setKey("agedBG");
		agedBG.setStatusKey("AGED_BG");
		agedBG.setColor("#f5c242");
		list.add(agedBG);

		ConfigurationDTO others = new ConfigurationDTO();
		others.setLegendName("Others");
		others.setKey("others");
		others.setStatusKey("OTHERS");
		others.setColor("#c4d6e0");
		list.add(others);

		return list;
	}
	
	@Override
	public Map<String, Object> getBankGuaranteePopupDetails(String projectId, String chartType, String status) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<BankGuaranteePopupDetailsDTO> popup = new ArrayList<BankGuaranteePopupDetailsDTO>();
		try {
			if (projectId != null) {
				popup = iBillingBankGuaranteeDAO.getBankGuaranteePopupDetails(projectId, chartType, status);
				responseMap.put("popup", popup);
			} else {
				throw new Exception("Error getting billing bank guarantee popups for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getBankGuaranteePopupDetails(): Exception occurred : {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] downloadBankGuaranteeDetailsExcel(String projectId) {
		ExportBankGuaranteeExcel exportBankGuaranteeExcel = new ExportBankGuaranteeExcel();
		List<BankGuaranteePopupDetailsDTO> list = new ArrayList<BankGuaranteePopupDetailsDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		try {
			list = iBillingBankGuaranteeDAO.getBankGuaranteePopupDetails(projectId, "SUMMARY", "TOTAL");
			exportBankGuaranteeExcel.exportBankGuaranteeDetailsExcel(workbook, list);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occurred when downloading bank guarantee excel file{}" , e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occurred when downloading bank guarantee excel file{}" , e.getMessage());
			}
		}
		return excelData;
	}

}
