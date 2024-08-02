package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.CustEntitlementChartPopupDTO;

public interface IBillingCustEntitlementDAO {

	public String getCustEntAnalysisUpdatedOn(String projectId);

	public Map<String, Object> getCustomerEntAnalysis(String projectId);

	public String getCustEntClosurePlanUpdatedOn(String projectId);

	public Map<String, Object> getCustEntClosurePlan(String projectId);

	public List<CustEntitlementChartPopupDTO> getCustomerEntAnalysisPopup(String projectId, String chartType,
			String xAxis, String yAxis);

	public List<CustEntitlementChartPopupDTO> getCustEntClosurePlanPopup(String projectId, String chartType,
			String xAxis, String yAxis);

	public List<CustEntitlementChartPopupDTO> getCustomerEntDetails(String projectId);

}
