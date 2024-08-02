package com.bh.realtrack.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.bh.realtrack.dto.ExchangeRateDTO;
import com.bh.realtrack.dto.MilestonesDTO;
import com.bh.realtrack.dto.SaveOpenInvoiceDTO;

public interface IBillingService {

	public Map<String, Object> getBillingSummary(String projectId);

	public Map<String, Object> getNextToBillPopup(String projectId);

	public Map<String, Object> getMilestonesToBill(String projectId);

	public Map<String, Object> getAllMilestones(String projectId);

	public Map<String, Object> getBillingReport(String projectId);

	public Map<String, Object> updateMilestoneDetails(List<MilestonesDTO> milestonesList);

	public Map<String, Object> publishMilestoneDetails(String projectId, List<MilestonesDTO> milestonesList);

	public ResponseEntity<?> exportAllMilestoneDetailsExcel(String projectId);

	public Map<String, Object> getPublishBillingSummary(String projectId);

	public Map<String, Object> getPublishNextToBillPopup(String projectId);

	public Map<String, Object> getPublishMilestonesToBill(String projectId);

	public Map<String, Object> getPublishAllMilestones(String projectId);

	public Map<String, Object> getPublishBillingReport(String projectId);

	public ResponseEntity<?> exportPublishAllMilestoneDetailsExcel(String projectId);

	public Map<String, Object> getBlankBaseLineDatePopup(String projectId);

	public Map<String, Object> getPublishBlankBaseLineDatePopup(String projectId);

	public Map<String, Object> getActivitiesPopup(String projectId, String cashMilestoneActivityId);

	public Map<String, Object> getPublishActivitiesPopup(String projectId, String cashMilestoneActivityId);

	public Map<String, Object> getAllActivities(String projectId);

	public Map<String, Object> getPublishAllActivities(String projectId);

	public ResponseEntity<?> exportAllActivitiesExcel(String projectId);

	public ResponseEntity<?> exportPublishAllActivitiesExcel(String projectId);

	public Map<String, Object> getMilestoneDescription(String projectId, String cashMilestoneActivityId, String flag);

	public List<ExchangeRateDTO> getExchangeRate(String projectId);

	public Map<String, Object> getOpenInvoiceChart(String projectId);

	public Map<String, Object> getOpenInvoiceChartPopupDetails(String projectId, String chartType, String statusCode);

	public Map<String, Object> getOpenInvoiceDatatable(String projectId);

	public Map<String, Object> saveOpenInvoiceDetails(List<SaveOpenInvoiceDTO> invoicesList);

	public Map<String, Object> getCashCollectionReportCurve(String projectId);

	public Map<String, Object> getAllInvoicesDetails(String projectId);

	public Map<String, Object> getVorFilter(String projectId);

	public Map<String, Object> getPoDetails(String projectId);

	public byte[] downloadOpenInvoicesDetails(String projectId);

	public byte[] downloadAllInvoiceDetails(String projectId);

}
