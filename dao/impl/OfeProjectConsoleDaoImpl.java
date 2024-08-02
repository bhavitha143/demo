package com.bh.realtrack.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IOfeProjectConsoleDao;
import com.bh.realtrack.dto.PrjConsoleAgingDetailsDTO;
import com.bh.realtrack.dto.PrjConsoleBillingChartDTO;
import com.bh.realtrack.dto.PrjConsoleDocumentationDto;
import com.bh.realtrack.dto.PrjConsoleDocumentationList;
import com.bh.realtrack.dto.PrjConsoleFinanceDTO;
import com.bh.realtrack.dto.PrjConsoleMrpDTO;
import com.bh.realtrack.dto.PrjConsoleNextToBillPopup;
import com.bh.realtrack.dto.PrjConsoleQualityKPIDTO;
import com.bh.realtrack.dto.PrjConsoleScruveDTO;
import com.bh.realtrack.dto.PrjConsoleScurveColorDTO;
import com.bh.realtrack.dto.PrjConsoleVorKpiDTO;
import com.bh.realtrack.dto.ProjectConsoleDTo;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.OfeProjectConsoleConstants;

@Repository
public class OfeProjectConsoleDaoImpl implements IOfeProjectConsoleDao{

	private static final Logger LOGGER = LoggerFactory.getLogger(OfeProjectConsoleDaoImpl.class);

	public OfeProjectConsoleDaoImpl(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	JdbcTemplate jdbcTemplate;

	@Override
	public String getUserSsso(String userId){
		return jdbcTemplate
				.query(OfeProjectConsoleConstants.GET_USER_SSO,
						new Object[] { userId },
						new ResultSetExtractor<String>() {
							public String extractData(ResultSet rs) throws SQLException {
								String count = "";
								while (rs.next()) {
									count = rs.getString(1);
								}
								return count;
							}
						});
	}
	
	@Override
	public ProjectConsoleDTo getProjectList(String projectId, String user) {
		
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_PROJECTS_INFO,
				new Object[] {projectId,Integer.parseInt(user)},
				new ResultSetExtractor<ProjectConsoleDTo>() {
					public ProjectConsoleDTo extractData(ResultSet rs) throws SQLException {
						ProjectConsoleDTo list = new ProjectConsoleDTo();
						while (rs.next()) {
							ProjectConsoleDTo dto = new ProjectConsoleDTo();

							dto.setProjectId(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setProjectName(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setProjectOwner(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setBusiness(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setEndCustomer(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setFinancialColor(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setQualityColor(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setRiskColor(null != rs.getString(8) ? rs.getString(8) : "");
							dto.setContractColor(null != rs.getString(9) ? rs.getString(9) : "");
							dto.setHseColor(null != rs.getString(10) ? rs.getString(10) : "");
							dto.setActionColor(null != rs.getString(11) ? rs.getString(11) : "");
							dto.setCustomerHealthColor(null != rs.getString(12) ? rs.getString(12) : "");
							dto.setDocManagementColor(null != rs.getString(13) ? rs.getString(13) : "");
							dto.setScheduleColor(null != rs.getString(14) ? rs.getString(14) : "");
							dto.setHighlightsOut(null != rs.getString(15) ? rs.getString(15) : "");
							dto.setPublishDateOut(null != rs.getString(16) ? rs.getString(16) : "");
							dto.setCmAsSoldOut(null != rs.getString(17) ? rs.getString(17) : "");
							dto.setCmActualOut(null != rs.getString(18) ? rs.getString(18) : "");
							dto.setDeltaCm(null != rs.getString(19) ? rs.getString(19) : "");
							dto.setDeltaCmTrend(null != rs.getString(20) ? rs.getString(20) : "");
							dto.setCustomerOtdOut(null != rs.getString(21) ? rs.getString(21) : "");
							dto.setCustomerOtdTrendOut(null != rs.getString(22) ? rs.getString(22) : "");
							dto.setInternalOtdOut(null != rs.getString(23) ? rs.getString(23) : "");
							dto.setInternalOutTrendOut(null != rs.getString(24) ? rs.getString(24) : "");
							dto.setOverallActualProgress(null != rs.getString(25) ? rs.getString(25) : "");
							dto.setOverallPlannedProgress(null != rs.getString(26) ? rs.getString(26) : "");
							dto.setPopPpOut(null != rs.getString(27) ? rs.getString(27) : "");
							
							return dto;
						}

						return list;
					}
				});
	}

	@Override
	public String postMitigationEmvOpenrisk(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_POST_MITIGATION_EMV_OPEN_RISK, new Object[] 
				{ projectId,projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = new String();
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public String postMitigationEmvOpenriskPerValue(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_POST_MITIGATION_EMV_OPEN_RISK_PER_VALUE, new Object[]
				{ projectId,projectId,projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override 
	public String openRiskOverDue(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.RISK_OVERDUE, new Object[] { projectId,projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}
	
	@Override
	public String getRefreshDate(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_REFRESH_DATE, new Object[] 
				{ projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public String getForecastDate(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_FORECAST_DATE, new Object[] 
				{ projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString("disp_calculated_forecast");
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public String getNextToBillValue(String projectId,String forecastDate) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_NEXT_TO_BILL, new Object[] 
				{ projectId,forecastDate }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString("next_to_bill");
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public List<PrjConsoleNextToBillPopup> getNextToBillPopup(String projectId,String forecastDate) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_NEXT_TO_BILL_POPUP_LIST, new Object[] {projectId,forecastDate,forecastDate },
				new ResultSetExtractor<List<PrjConsoleNextToBillPopup>>() {
					public List<PrjConsoleNextToBillPopup> extractData(ResultSet rs) throws SQLException {
						List<PrjConsoleNextToBillPopup> list = new ArrayList<PrjConsoleNextToBillPopup>();
						while (rs.next()) {
							PrjConsoleNextToBillPopup dto = new PrjConsoleNextToBillPopup();
							dto.setSubProjectId(rs.getString("sub_project_id"));
							dto.setActivityId(rs.getString("activity_id"));
							dto.setMileStoneDesc(rs.getString("milestone_description"));
							dto.setMileStoneAmt(rs.getString("milestone_amt_usd"));
							dto.setMileStoneStatus(rs.getString("milestone_status"));
							dto.setMileStoneForecast(rs.getString("milestone_forecast"));
							dto.setDays(rs.getString("days_until_milestone_completion"));
							dto.setMileStoneCompletion(rs.getString("milestone_completion"));
							dto.setStatus(rs.getString("status"));
							list.add(dto);
						}
						return list;
					}
				});
	}

	
	@Override
	public String getMonOtdValue(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_12_MON_OTD_VALUE, new Object[] 
				{ projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}


	@Override
	public String getDocReworkValue(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_DOC_REWORK_VALUE, new Object[] 
				{ projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}


	@Override
	public String getDocReviewValue(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_DOC_REVIEW, new Object[] 
				{ projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public PrjConsoleDocumentationDto getDocumentationCount(String projectId,String sso) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_DOCUMENT_COUNT, new Object[] 
				{ projectId,sso }, new ResultSetExtractor<PrjConsoleDocumentationDto>() {
			@Override
			public PrjConsoleDocumentationDto extractData(final ResultSet rs) {
				PrjConsoleDocumentationDto role = new PrjConsoleDocumentationDto();
				try {
					while (rs.next()) {
						role.setTotalCount(rs.getString("total"));
						role.setFinishedCount(rs.getString("finished"));
						role.setPendingCount(rs.getString("pending"));
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public List<PrjConsoleDocumentationList> getDocPopupList(String projectId, String status) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_DOC_POPUP_LIST, new Object[] { projectId, status },
				new ResultSetExtractor<List<PrjConsoleDocumentationList>>() {
					public List<PrjConsoleDocumentationList> extractData(ResultSet rs) throws SQLException {
						List<PrjConsoleDocumentationList> list = new ArrayList<PrjConsoleDocumentationList>();
						while (rs.next()) {
							PrjConsoleDocumentationList docList = new PrjConsoleDocumentationList();
							docList.setPid(rs.getString("pid"));
							docList.setProjectName(rs.getString("project_name"));
							docList.setDocId(rs.getString("doc_id"));
							docList.setDocNumber(rs.getString("doc_number"));
							docList.setBhDocId(rs.getString("BH_Document_ID"));
							docList.setClientDocId(rs.getString("Client_Doc_ID"));
							docList.setSupplierDocId(rs.getString("Supplier_Doc_ID"));
							docList.setDocTitle(rs.getString("Doc_Title"));
							docList.setDocStatus(rs.getString("Doc_Status"));
							docList.setDocGroup(rs.getString("Doc_Group"));
							docList.setPurposeOfIssue(rs.getString("Purpose_of_Issue"));
							docList.setFunctionalCode(rs.getString("Functional_Code"));
							docList.setProductLine(rs.getString("Product_Line"));
							docList.setOwner(rs.getString("Owner"));
							docList.setDocType(rs.getString("Doc_Type"));
							docList.setDocProgress(rs.getString("Doc_Progress"));
							docList.setLifeCycleCode(rs.getString("Lifecycle_Code"));
							docList.setNoOfMilestones(rs.getString("Number_Of_Milestones"));
							docList.setNonDeliverables(rs.getString("Has_ND_Milestone"));
							docList.setFirstMilestoneId(rs.getString("First_Milestone_ID"));
							docList.setFirstMilestoneOrder(rs.getString("First_Milestone_Order"));
							docList.setFirstMilestoneCode(rs.getString("First_Milestone_Code"));
							docList.setFirstMilestoneBlDate(rs.getString("First_Milestone_BL_Date"));
							docList.setFirstMilestoneFcDate(rs.getString("First_Milestone_FC_Date"));
							docList.setFirstMilestoneActualDate(rs.getString("First_Milestone_Actual_Date"));
							docList.setFirstMilestoneLastRevId(rs.getString("First_Milestone_Last_Revision_ID"));
							docList.setFirstMilestoneLastRevOrder(rs.getString("First_Milestone_Last_Revision_Order"));
							docList.setFirstMilestoneLastRev(rs.getString("First_Milestone_Last_Revision"));
							docList.setLastMilestoneId(rs.getString("Last_Milestone_ID"));
							docList.setLastMilestoneCode(rs.getString("Last_Milestone_Code"));
							docList.setLastMilestoneOrder(rs.getString("Last_Milestone_Order"));
							docList.setLastRev(rs.getString("Last_Revision"));
							docList.setLastRevId(rs.getString("Last_Revision_ID"));
							docList.setLastRevOrder(rs.getString("Last_Revision_Order"));
							docList.setLastCustRevOrder(rs.getString("Last_Customer_Revision_Order"));
							docList.setLastRevStatus(rs.getString("Last_Revision_Status"));
							docList.setLastRevStatusBundle(rs.getString("Last_Revision_Status_Bundle"));
							docList.setLastRevIssueDate(rs.getString("Last_Revision_Issue_Date"));
							docList.setDocLastUpdate(rs.getString("Docs_Last_Update"));
							list.add(docList);
						}
						return list;
					}
				});
	}
	
	@Override
	public String getSCurveDataDate(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_SCURVE_DATA_DATE, new Object[] 
				{ projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public List<PrjConsoleScruveDTO> getScruveList(String projectId,String dataDate) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_SCURVE_LIST, new Object[]
						{ dataDate,dataDate,dataDate,dataDate,projectId,projectId },
				new ResultSetExtractor<List<PrjConsoleScruveDTO>>() {
					public List<PrjConsoleScruveDTO> extractData(ResultSet rs) throws SQLException {
						List<PrjConsoleScruveDTO> list = new ArrayList<PrjConsoleScruveDTO>();
						while (rs.next()) {
							PrjConsoleScruveDTO dto = new PrjConsoleScruveDTO();
							dto.setPhaseDate(rs.getString("phasedate"));
							dto.setActualPerc(rs.getString("actualpercent"));
							dto.setForecastPerc(rs.getString("forecastpercent"));
							dto.setBaselinePerc(rs.getString("baselinepercent"));
							dto.setBlLatePerc(rs.getString("baselinelatepercent"));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public String getSCurveProgressDate(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_SCURVE_PROGRESS_DATE, new Object[] 
				{ projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public List<PrjConsoleScurveColorDTO> getSCurveColorList(String projectId,String dataDate,String progressDate) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_SCURVE_COLOR_LIST, new Object[] { projectId },
				new ResultSetExtractor<List<PrjConsoleScurveColorDTO>>() {
					public List<PrjConsoleScurveColorDTO> extractData(ResultSet rs) throws SQLException {
						List<PrjConsoleScurveColorDTO> list = new ArrayList<PrjConsoleScurveColorDTO>();
						while (rs.next()) {
							PrjConsoleScurveColorDTO dto = new PrjConsoleScurveColorDTO();
							dto.setProjectId(rs.getString("project_id_out"));
							dto.setFunction(rs.getString("department_name_out"));
							dto.setTrafficLight(rs.getString("traffic_light_out"));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<PrjConsoleBillingChartDTO> getPrjConsoleBillingChart(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_BILLING_CHART_LIST, new Object[] { projectId },
				new ResultSetExtractor<List<PrjConsoleBillingChartDTO>>() {
					public List<PrjConsoleBillingChartDTO> extractData(ResultSet rs) throws SQLException {
						List<PrjConsoleBillingChartDTO> list = new ArrayList<PrjConsoleBillingChartDTO>();
						while (rs.next()) {
							PrjConsoleBillingChartDTO dto = new PrjConsoleBillingChartDTO();
							dto.setCategory(rs.getString("category_out"));
							dto.setDisplayDate(rs.getString("date_out"));
							dto.setDisplayAmount(rs.getString("display_amt_out"));
							dto.setInvoiceDate(rs.getString("epoch_inv_date_out"));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public PrjConsoleFinanceDTO getFinanceData(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_FINANCE_DETAILS, new Object[] 
				{ projectId }, new ResultSetExtractor<PrjConsoleFinanceDTO>() {
			@Override
			public PrjConsoleFinanceDTO extractData(final ResultSet rs) {
				PrjConsoleFinanceDTO role = new PrjConsoleFinanceDTO();
				try {
					while (rs.next()) {
						role.setContractValue(rs.getString("contract_value_out"));
						role.setInvoicedAmt(rs.getString("invoice_amt_out"));
						role.setCashCollected(rs.getString("cash_collected_out"));
						role.setPastDue(rs.getString("past_due_out"));
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public List<PrjConsoleMrpDTO> getPrjConsoleMrpDetails(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_BILLING_MRP_LIST, new Object[] { projectId },
				new ResultSetExtractor<List<PrjConsoleMrpDTO>>() {
					public List<PrjConsoleMrpDTO> extractData(ResultSet rs) throws SQLException {
						List<PrjConsoleMrpDTO> list = new ArrayList<PrjConsoleMrpDTO>();
						while (rs.next()) {
							PrjConsoleMrpDTO dto = new PrjConsoleMrpDTO();
							dto.setValue(rs.getString("otd_value_out"));
							dto.setColor(rs.getString("otd_color_out"));
							dto.setCount(rs.getString("total_count_out"));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public PrjConsoleQualityKPIDTO getPrjConsoleQualityDetails(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_QUALITY_KPI, new Object[] 
				{ projectId }, new ResultSetExtractor<PrjConsoleQualityKPIDTO>() {
			@Override
			public PrjConsoleQualityKPIDTO extractData(final ResultSet rs) {
				PrjConsoleQualityKPIDTO role = new PrjConsoleQualityKPIDTO();
					try {
						while (rs.next()) {
							role.setOverdueCir(rs.getString("cir_overdue"));
							role.setOverdueNcrs(rs.getString("ncr_overdue"));
							role.setCoPo(rs.getString("cum_cost_poor_quality"));
							role.setQuality(rs.getString("quality_issues"));
						}
					} catch (SQLException e) {
						throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
					}
				return role;
			}
		});
	}

	@Override
	public String getUserRole(String sso) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_ROLE, new Object[]
				{ sso }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public List<PrjConsoleAgingDetailsDTO> getPrjConsoleAgingDetails(String projectId, String role) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_AGING_LIST, new Object[] { projectId,role },
				new ResultSetExtractor<List<PrjConsoleAgingDetailsDTO>>() {
					public List<PrjConsoleAgingDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<PrjConsoleAgingDetailsDTO> list = new ArrayList<PrjConsoleAgingDetailsDTO>();
						while (rs.next()) {
							PrjConsoleAgingDetailsDTO dto = new PrjConsoleAgingDetailsDTO();
							dto.setProjectId(rs.getString("project_id_out"));
							dto.setNcrNumber(rs.getString("ncrnumber_out"));
							dto.setStatus(rs.getString("status_out"));
							dto.setAging(rs.getString("aging_out"));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<PrjConsoleVorKpiDTO> getApprovedVor(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_APPROVED_VOR, new Object[] { projectId },
				new ResultSetExtractor<List<PrjConsoleVorKpiDTO>>() {
					public List<PrjConsoleVorKpiDTO> extractData(ResultSet rs) throws SQLException {
						List<PrjConsoleVorKpiDTO> list = new ArrayList<PrjConsoleVorKpiDTO>();
						while (rs.next()) {
							PrjConsoleVorKpiDTO dto = new PrjConsoleVorKpiDTO();
							dto.setKpi(rs.getString("kpi_name_out"));
							dto.setKpiValue(rs.getString("value_out"));
							dto.setAmountUsd(rs.getString("amount_usd_out"));
							dto.setFinalPriceOut(null!=rs.getString("final_price_out") ? rs.getString("final_price_out") : "");
							dto.setFinalCmPercOut(null!=rs.getString("final_cm_perc_out") ?rs.getString("final_cm_perc_out") : "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public String getProjectConsoleNcr(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_NCR, new Object[]
				{ projectId, projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = "";
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public String getPrjConsoleActiveMrp(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_ACTIVE_MRP, new Object[]
				{ projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String mrp = new String();
				try {
					while (rs.next()) {
						mrp = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return mrp;
			}
		});
	}

	@Override
	public String getPrjConsoleOverDuePec(String projectId) {
		return jdbcTemplate.query(OfeProjectConsoleConstants.GET_OVERDUE_PERC, new Object[]
				{ projectId }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String overduePerc = "";
				try {
					while (rs.next()) {
						overduePerc = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return overduePerc;
			}
		});
	}

}
