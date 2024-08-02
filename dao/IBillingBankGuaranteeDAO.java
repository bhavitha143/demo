package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.BankGuaranateeSummaryDetailsDTO;
import com.bh.realtrack.dto.BankGuaranteePopupDetailsDTO;

public interface IBillingBankGuaranteeDAO {

	public String getBankGuaranteeUpdatedOn(String projectId);

	public BankGuaranateeSummaryDetailsDTO getBankGuaranteeSummary(String projectId);

	public Map<String, Object> getBankGuaranteeBgAmtConnectedPieChart(String projectId);

	public Map<String, Object> getBankGuaranteeBgAmtIssuedByTypePieChart(String projectId);

	public Map<String, Object> getBankGuaranteeBgAmtToRecoverPieChart(String projectId);

	public List<BankGuaranteePopupDetailsDTO> getBankGuaranteePopupDetails(String projectId, String chartType,
			String status);

}
