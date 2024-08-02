package com.bh.realtrack.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.bh.realtrack.dto.BillingCycleRemarksDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.ExceptionRemarksDTO;
import com.bh.realtrack.dto.UpdateDetailsDTO;

public interface IBillingPortfolioService {

	public Map<String, Object> getBillingDropDown(int customerId, int companyId, String business, String segment,
			String region);

	public Map<String, Object> getBillingCount(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) throws ParseException, Exception;

	public Map<String, Object> getSegmentSummary(int customerId, int companyId, String business, String region,
			String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	public Map<String, Object> getAllBillingTab(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	public Map<String, Object> getBillingCurveTab(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	public Map<String, Object> getBillingCurrentGap(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) throws ParseException, Exception;

	public Map<String, Object> getlastUpdateDate();

	public Map<String, Object> getBillingPercentagePopUp(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag) throws Exception;

	public Map<String, Object> getBillingGapRecovery(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	public Map<String, Object> getAllWeeks(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	public Map<String, Object> getBillingKpi(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	public Map<String, Object> getBillingExceptions(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	Map<String, Object> getOverallBillingCycleDetail(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	Map<String, Object> getBillingInvoiceDetails(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	Map<String, Object> getExceptionCategory(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	byte[] downloadBillingDetails(int customerId, int companyId, String business, String segment, String region,
			String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	Map<String, Object> getMonthlyBillingKpi(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	public Map<String, Object> getTPSBillingReportURI();

	public byte[] downloadBillingInvoiceDetailsPdf(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	public Map<String, Object> getBillingInvoiceRemarksList();

	public Map<String, Object> saveBillingMilestoneRemarkDetails(List<BillingCycleRemarksDTO> invoiceList);

	public Map<String, Object> getExceptionRemarksList();

	public Map<String, Object> saveBillingExceptionRemarkDetails(List<ExceptionRemarksDTO> exceptionList);

	public Map<String, Object> getBillingActivitiesDetails(String projectId, String cashMilestoneId);

	public Map<String, Object> getBillingLinearityChart(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment);

	public Map<String, Object> getBillingLinearityChartPopup(int customerId, int companyId, String business,
			String segment, String region, String pmLeader, String spm, String financialSegment, String quarterYear,
			String month);

	public byte[] downloadBillingMilestoneDetails(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	public byte[] downloadBillingDetailsExcel(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	public UpdateDetailsDTO getBillingProjectOutOfRTUploadStatus(String companyId);

	public List<ErrorDetailsDTO> getBillingNotProcessedProjectDetails(String companyId);

	public Map<String, String> uploadBillingProjectOutOfRT(String companyId, MultipartFile excelFile);

	public byte[] downloadBillingOutOfProjectTemplate(String companyId);

	public Map<String, Object> getQuarterlyBillingKpi(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment, String startDate, String endDate,
			String currentYearFlag);

	public byte[] downloadBillingLinearityExcel(int customerId, int companyId, String business, String segment,
			String region, String pmLeader, String spm, String financialSegment);

}
