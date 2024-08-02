package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.ActivitiesDTO;
import com.bh.realtrack.dto.ActivitiesPopupDTO;
import com.bh.realtrack.dto.ActualCurveDTO;
import com.bh.realtrack.dto.AllMilestonesDTO;
import com.bh.realtrack.dto.BaselineCurveDTO;
import com.bh.realtrack.dto.BillingSummaryDTO;
import com.bh.realtrack.dto.BlankBaselineDateDTO;
import com.bh.realtrack.dto.BlankBaselineDatePopupDTO;
import com.bh.realtrack.dto.CashCollectionInvoicesDetails;
import com.bh.realtrack.dto.CurveTable;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ExchangeRateDTO;
import com.bh.realtrack.dto.FinancialBLCurveDTO;
import com.bh.realtrack.dto.ForecastCurveDTO;
import com.bh.realtrack.dto.InvoiceDetailDTO;
import com.bh.realtrack.dto.ItoBaselineCurveDTO;
import com.bh.realtrack.dto.MilestonesDTO;
import com.bh.realtrack.dto.MilestonesToBillDTO;
import com.bh.realtrack.dto.NextToBillDTO;
import com.bh.realtrack.dto.OpenInvoiceChartPopupDetails;
import com.bh.realtrack.dto.OpenInvoiceDataTable;
import com.bh.realtrack.dto.PODetailsDTO;
import com.bh.realtrack.dto.SaveOpenInvoiceDTO;

public interface IBillingDAO {

	public List<NextToBillDTO> getNextToBillPopup(String projectId);

	public List<BillingSummaryDTO> getBillingSummary(String projectId);

	public List<MilestonesToBillDTO> getMilestonesToBill(String projectId);

	public List<AllMilestonesDTO> getAllMilestones(String projectId);

	public List<ForecastCurveDTO> getForecastCurve(String projectId);

	public List<ActualCurveDTO> getActualCurve(String projectId);

	public int updateMilestoneDetails(List<MilestonesDTO> milestonesList, String sso);

	public String publishMilestoneDetails(String projectId, String sso, List<String> changedPubMilestoneIds);

	public List<CurveTable> getBillingTable(String projectId);

	public List<BaselineCurveDTO> getBaselineCurve(String projectId);

	public List<NextToBillDTO> getPublishNextToBillPopup(String projectId);

	public List<BillingSummaryDTO> getPublishBillingSummary(String projectId);

	public List<MilestonesToBillDTO> getPublishMilestonesToBill(String projectId);

	public List<AllMilestonesDTO> getPublishAllMilestones(String projectId);

	public List<ForecastCurveDTO> getPublishForecastCurve(String projectId);

	public List<ActualCurveDTO> getPublishActualCurve(String projectId);

	public List<CurveTable> getPublishBillingTable(String projectId);

	public List<BaselineCurveDTO> getPublishBaselineCurve(String projectId);

	public List<BlankBaselineDateDTO> getBlankBaselineDate(String projectId);

	public List<BlankBaselineDatePopupDTO> getBlankBaseLineDatePopup(String projectId);

	public String getLastSavedDate(String projectId);

	public List<BlankBaselineDateDTO> getPublishBlankBaselineDate(String projectId);

	public String getLastPublishDate(String projectId);

	public List<BlankBaselineDatePopupDTO> getPublishBlankBaseLineDatePopup(String projectId);

	public List<ForecastCurveDTO> getPubForecastCurve(String projectId);

	public List<ActivitiesPopupDTO> getActivitiesPopup(String projectId, String cashMilestoneActivityId);

	public List<ActivitiesPopupDTO> getPublishActivitiesPopup(String projectId, String cashMilestoneActivityId);

	public List<ActivitiesDTO> getAllActivities(String projectId);

	public List<ActivitiesDTO> getPublishAllActivities(String projectId);

	public String getPubMilestoneDescription(String projectId, String cashMilestoneActivityId);

	public String getMilestoneDescription(String projectId, String cashMilestoneActivityId);

	public List<ExchangeRateDTO> getExchangeRate(String projectId);

	public List<FinancialBLCurveDTO> getFinancialBLCurve(String projectId);

	public List<FinancialBLCurveDTO> getPubFinancialBLCurve(String projectId);

	public boolean getShowPublishButtonFlag(String projectId);

	public List<ItoBaselineCurveDTO> getItoBaselineCurve(String projectId);

	public Map<String, Object> getOpenInvoiceChart(String projectId);

	public String getLastUpdatedDate(String projectId);

	public List<OpenInvoiceChartPopupDetails> getOpenInvoiceChartPopupDetails(String projectId, String chartType,
			String statusCode);

	public List<OpenInvoiceDataTable> getOpenInvoiceDatatable(String projectId);

	public boolean saveOpenInvoiceDetails(List<SaveOpenInvoiceDTO> invoicesList, String sso);

	public void callCashCollectionProcedure();

	public String getOpenInvoiceLastSavedDate(String projectId);

	public Map<String, Object> getForecastCashCurve(String projectId, Map<String, Object> map);

	public Map<String, Object> getForecastCashCurveForPascalProject(String projectId, Map<String, Object> map);

	public Map<String, Object> getCollectedCashCurve(String projectId, Map<String, Object> map);

	public Map<String, Object> getOtrCashBaselineCurve(String projectId, Map<String, Object> map);

	public Map<String, Object> getItoCashBaselineCurve(String projectId, Map<String, Object> map);

	public Map<String, Object> getPastDueCurve(String projectId, Map<String, Object> map);

	public List<CashCollectionInvoicesDetails> getAllInvoicesDetails(String projectId);

	public List<CashCollectionInvoicesDetails> getAllInvoicesDetailsForPascalProject(String projectId);

	public Boolean checkProjectIsPascalProject(String projectId);

	public List<DropDownDTO> getVorFilter(String projectId);

	public String getPOLastUpdatedDate(String projectId);

	public PODetailsDTO getPODetails(String projectId);

	public List<InvoiceDetailDTO> getPOInvoiceDetails(String projectId);

}
