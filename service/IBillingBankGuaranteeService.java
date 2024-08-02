package com.bh.realtrack.service;

import java.util.Map;

public interface IBillingBankGuaranteeService {

	public Map<String, Object> getBankGuaranteeSummary(String projectId);

	public Map<String, Object> getBankGuaranteePieChart(String projectId);

	public Map<String, Object> getBankGuaranteePopupDetails(String projectId, String chartType, String status);

	public byte[] downloadBankGuaranteeDetailsExcel(String projectId);

}
