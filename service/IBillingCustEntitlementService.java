package com.bh.realtrack.service;

import java.util.Map;

public interface IBillingCustEntitlementService {

	public Map<String, Object> getCustomerEntAnalysis(String projectId);

	public Map<String, Object> getCustomerEntClosurePlan(String projectId);

	public Map<String, Object> getCustomerEntDetailsPopup(String projectId, String chartType, String xAxis,
			String yAxis);

	public byte[] downloadCustomerEntDetails(String projectId);

}
