package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IBillingCustEntitlementDAO;
import com.bh.realtrack.dto.ClosurePlanDTO;
import com.bh.realtrack.dto.CustEntitlementAnalysisDTO;
import com.bh.realtrack.dto.CustEntitlementChartPopupDTO;
import com.bh.realtrack.dto.CustEntitlementCustomerPlanDTO;
import com.bh.realtrack.dto.EntitlementAnalysisDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.BillingCustEntitlementConstants;

@Repository
public class BillingCustEntitlementDAOImpl implements IBillingCustEntitlementDAO {
	private static Logger log = LoggerFactory.getLogger(BillingCustEntitlementDAOImpl.class.getName());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String getCustEntAnalysisUpdatedOn(String projectId) {
		return jdbcTemplate.query(BillingCustEntitlementConstants.GET_ENTITLEMENT_ANALYSIS_CHART_DATE,
				new Object[] { projectId }, new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String date = BillingCustEntitlementConstants.EMPTY_STRING;
						while (rs.next()) {
							date = null != rs.getString("last_updated_dt") ? rs.getString("last_updated_dt")
									: BillingCustEntitlementConstants.EMPTY_STRING;
						}
						return date;
					}
				});
	}

	@Override
	public Map<String, Object> getCustomerEntAnalysis(String projectId) {
		return jdbcTemplate.query(BillingCustEntitlementConstants.GET_ENTITLEMENT_ANALYSIS_CHART,
				new Object[] { projectId }, new ResultSetExtractor<Map<String, Object>>() {

					Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
					CustEntitlementAnalysisDTO openEnt = new CustEntitlementAnalysisDTO();
					CustEntitlementAnalysisDTO reserved = new CustEntitlementAnalysisDTO();
					CustEntitlementAnalysisDTO closedEnt = new CustEntitlementAnalysisDTO();
					CustEntitlementAnalysisDTO remitted = new CustEntitlementAnalysisDTO();

					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							EntitlementAnalysisDTO dto = new EntitlementAnalysisDTO();
							dto.setClaimStatus(null != rs.getString("claim_status") ? rs.getString("claim_status")
									: BillingCustEntitlementConstants.EMPTY_STRING);
							dto.setClaimCategoryCode(
									null != rs.getString("claim_category_code") ? rs.getString("claim_category_code")
											: BillingCustEntitlementConstants.EMPTY_STRING);
							dto.setPotential(null != rs.getString("potential") ? rs.getString("potential")
									: BillingCustEntitlementConstants.EMPTY_STRING);
							dto.setAccuralSum(null != rs.getString("accural_sum") ? rs.getString("accural_sum")
									: BillingCustEntitlementConstants.EMPTY_STRING);
							dto.setEntitlementSum(
									null != rs.getString("entitlement_sum") ? rs.getString("entitlement_sum")
											: BillingCustEntitlementConstants.EMPTY_STRING);

							if (dto.getClaimStatus().equalsIgnoreCase("Open")) {
								if (dto.getClaimCategoryCode().equalsIgnoreCase("LD")) {
									if (dto.getPotential().equalsIgnoreCase("Claimed")) {
										openEnt.setLcClaimed(dto.getEntitlementSum());
										reserved.setLcClaimed(dto.getAccuralSum());
									} else if (dto.getPotential().equalsIgnoreCase("Potential")) {
										openEnt.setLcPotential(dto.getEntitlementSum());
										reserved.setLcPotential(dto.getAccuralSum());
									}
								} else if (dto.getClaimCategoryCode().equalsIgnoreCase("BC")) {
									if (dto.getPotential().equalsIgnoreCase("Claimed")) {
										openEnt.setBcClaimed(dto.getEntitlementSum());
										reserved.setBcClaimed(dto.getAccuralSum());
									} else if (dto.getPotential().equalsIgnoreCase("Potential")) {
										openEnt.setBcPotential(dto.getEntitlementSum());
										reserved.setBcPotential(dto.getAccuralSum());
									}
								} else if (dto.getClaimCategoryCode().equalsIgnoreCase("Litigation")) {
									openEnt.setLitigation(dto.getEntitlementSum());
									reserved.setLitigation(dto.getAccuralSum());
								} else if (dto.getClaimCategoryCode().equalsIgnoreCase("Others")) {
									openEnt.setOthers(dto.getEntitlementSum());
									reserved.setOthers(dto.getAccuralSum());
								}
							} else if (dto.getClaimStatus().equalsIgnoreCase("Settled/Closed")) {
								if (dto.getClaimCategoryCode().equalsIgnoreCase("LD")) {
									if (dto.getPotential().equalsIgnoreCase("Claimed")) {
										closedEnt.setLcClaimed(dto.getEntitlementSum());
										remitted.setLcClaimed(dto.getAccuralSum());
									} else if (dto.getPotential().equalsIgnoreCase("Potential")) {
										closedEnt.setLcPotential(dto.getEntitlementSum());
										remitted.setLcPotential(dto.getAccuralSum());
									}
								} else if (dto.getClaimCategoryCode().equalsIgnoreCase("BC")) {
									if (dto.getPotential().equalsIgnoreCase("Claimed")) {
										closedEnt.setBcClaimed(dto.getEntitlementSum());
										remitted.setBcClaimed(dto.getAccuralSum());
									} else if (dto.getPotential().equalsIgnoreCase("Potential")) {
										closedEnt.setBcPotential(dto.getEntitlementSum());
										remitted.setBcPotential(dto.getAccuralSum());
									}
								} else if (dto.getClaimCategoryCode().equalsIgnoreCase("Litigation")) {
									closedEnt.setLitigation(dto.getEntitlementSum());
									remitted.setLitigation(dto.getAccuralSum());
								} else if (dto.getClaimCategoryCode().equalsIgnoreCase("Others")) {
									closedEnt.setOthers(dto.getEntitlementSum());
									remitted.setOthers(dto.getAccuralSum());
								}
							}
							responseMap.put("OPEN_ENT", openEnt);
							responseMap.put("RESERVED", reserved);
							responseMap.put("CLOSED_ENT", closedEnt);
							responseMap.put("REMITTED", remitted);
						}
						return responseMap;
					}
				});
	}

	@Override
	public String getCustEntClosurePlanUpdatedOn(String projectId) {
		return jdbcTemplate.query(BillingCustEntitlementConstants.GET_CLOSURE_PLAN_CHART_DATE,
				new Object[] { projectId }, new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String date = BillingCustEntitlementConstants.EMPTY_STRING;
						while (rs.next()) {
							date = null != rs.getString("last_updated_dt") ? rs.getString("last_updated_dt")
									: BillingCustEntitlementConstants.EMPTY_STRING;
						}
						return date;
					}
				});
	}

	@Override
	public Map<String, Object> getCustEntClosurePlan(String projectId) {
		return jdbcTemplate.query(BillingCustEntitlementConstants.GET_CLOSURE_PLAN_CHART, new Object[] { projectId },
				new ResultSetExtractor<Map<String, Object>>() {
					Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
					CustEntitlementCustomerPlanDTO alreadyDue = new CustEntitlementCustomerPlanDTO();
					CustEntitlementCustomerPlanDTO currentQuarter = new CustEntitlementCustomerPlanDTO();
					CustEntitlementCustomerPlanDTO nextQuarter = new CustEntitlementCustomerPlanDTO();
					CustEntitlementCustomerPlanDTO abvNextQuarter = new CustEntitlementCustomerPlanDTO();
					int alreadyDueClaims = 0, currentQuarterClaims = 0, nextQuarterClaims = 0, abvNextQuarterClaims = 0;
					Double alreadyDueReserves = 0.0, currentQuarterReserves = 0.0, nextQuarterReserves = 0.0,
							abvNextQuarterReserves = 0.0;

					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							ClosurePlanDTO dto = new ClosurePlanDTO();
							dto.setQuarterDetail(null != rs.getString("quater_detail") ? rs.getString("quater_detail")
									: BillingCustEntitlementConstants.EMPTY_STRING);
							dto.setPotential(null != rs.getString("potential") ? rs.getString("potential")
									: BillingCustEntitlementConstants.EMPTY_STRING);
							dto.setClaims(null != rs.getString("count") ? rs.getString("count")
									: BillingCustEntitlementConstants.EMPTY_STRING);
							dto.setAccuralSum(null != rs.getString("accural_sum") ? rs.getString("accural_sum")
									: BillingCustEntitlementConstants.EMPTY_STRING);
							dto.setEntitlementSum(
									null != rs.getString("entitlement_sum") ? rs.getString("entitlement_sum")
											: BillingCustEntitlementConstants.EMPTY_STRING);

							if (dto.getQuarterDetail().equalsIgnoreCase("ALREADY_DUE")) {
								if (dto.getPotential().equalsIgnoreCase("Claimed")) {
									alreadyDue.setClaimedEnt(dto.getEntitlementSum());
								} else if (dto.getPotential().equalsIgnoreCase("Potential")) {
									alreadyDue.setPotentialEnt(dto.getEntitlementSum());
								}
								alreadyDueReserves = alreadyDueReserves + Double.parseDouble(dto.getAccuralSum());
								alreadyDueClaims = alreadyDueClaims + Integer.parseInt(dto.getClaims());
								alreadyDue.setReserves(Double.toString(Math.round(alreadyDueReserves * 100.0) / 100.0));
								alreadyDue.setClaims(String.valueOf(alreadyDueClaims));

							} else if (dto.getQuarterDetail().equalsIgnoreCase("CURRENT_QUARTER")) {
								if (dto.getPotential().equalsIgnoreCase("Claimed")) {
									currentQuarter.setClaimedEnt(dto.getEntitlementSum());
								} else if (dto.getPotential().equalsIgnoreCase("Potential")) {
									currentQuarter.setPotentialEnt(dto.getEntitlementSum());
								}
								currentQuarterReserves = currentQuarterReserves
										+ Double.parseDouble(dto.getAccuralSum());
								currentQuarterClaims = currentQuarterClaims + Integer.parseInt(dto.getClaims());
								currentQuarter.setReserves(
										Double.toString(Math.round(currentQuarterReserves * 100.0) / 100.0));
								currentQuarter.setClaims(String.valueOf(currentQuarterClaims));

							} else if (dto.getQuarterDetail().equalsIgnoreCase("NEXT_QUARTER")) {
								if (dto.getPotential().equalsIgnoreCase("Claimed")) {
									nextQuarter.setClaimedEnt(dto.getEntitlementSum());
								} else if (dto.getPotential().equalsIgnoreCase("Potential")) {
									nextQuarter.setPotentialEnt(dto.getEntitlementSum());
								}
								nextQuarterReserves = nextQuarterReserves + Double.parseDouble(dto.getAccuralSum());
								nextQuarterClaims = nextQuarterClaims + Integer.parseInt(dto.getClaims());
								nextQuarter
										.setReserves(Double.toString(Math.round(nextQuarterReserves * 100.0) / 100.0));
								nextQuarter.setClaims(String.valueOf(nextQuarterClaims));

							} else if (dto.getQuarterDetail().equalsIgnoreCase("ABOVE_NEXT_QUARTER")) {
								if (dto.getPotential().equalsIgnoreCase("Claimed")) {
									abvNextQuarter.setClaimedEnt(dto.getEntitlementSum());
								} else if (dto.getPotential().equalsIgnoreCase("Potential")) {
									abvNextQuarter.setPotentialEnt(dto.getEntitlementSum());
								}
								abvNextQuarterReserves = abvNextQuarterReserves
										+ Double.parseDouble(dto.getAccuralSum());
								abvNextQuarterClaims = abvNextQuarterClaims + Integer.parseInt(dto.getClaims());
								abvNextQuarter.setReserves(
										Double.toString(Math.round(abvNextQuarterReserves * 100.0) / 100.0));
								abvNextQuarter.setClaims(String.valueOf(abvNextQuarterClaims));

							}
							responseMap.put("ALREADY_DUE", alreadyDue);
							responseMap.put("CURRENT_QUARTER", currentQuarter);
							responseMap.put("NEXT_QUARTER", nextQuarter);
							responseMap.put("ABV_NEXT_QUARTER", abvNextQuarter);
						}
						return responseMap;
					}
				});
	}

	@Override
	public List<CustEntitlementChartPopupDTO> getCustomerEntAnalysisPopup(String projectId, String chartType,
			String xAxis, String yAxis) {
		Connection con = null;
		List<CustEntitlementChartPopupDTO> list = new ArrayList<CustEntitlementChartPopupDTO>();
		StringBuilder customerEntPopupQuery = new StringBuilder();
		String claimStatus = "", claimCategoryCode = "", potential = "";
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			customerEntPopupQuery.append(BillingCustEntitlementConstants.GET_CUST_ENTITLEMENT_POPUP);
			customerEntPopupQuery.append(BillingCustEntitlementConstants.GET_ENTITLEMENT_ANALYSIS_CLAIM_STATUS);
			customerEntPopupQuery.append(BillingCustEntitlementConstants.GET_ENTITLEMENT_ANALYSIS_CLAIM_CAT_CODE);

			if (xAxis.equalsIgnoreCase("OPEN_ENT") || xAxis.equalsIgnoreCase("RESERVED")) {
				claimStatus = "Open";
			} else if (xAxis.equalsIgnoreCase("CLOSED_ENT") || xAxis.equalsIgnoreCase("REMITTED")) {
				claimStatus = "Settled/Closed";
			}

			if (yAxis.equalsIgnoreCase("LC_CLAIMED") || yAxis.equalsIgnoreCase("LC_POTENTIAL")) {
				claimCategoryCode = "LD";
				potential = String.valueOf(yAxis).substring(3);
				potential = String.valueOf(potential).substring(0, 1).toUpperCase()
						.concat(String.valueOf(potential).substring(1).toLowerCase());
				customerEntPopupQuery.append(BillingCustEntitlementConstants.GET_ENTITLEMENT_ANALYSIS_CLAIM_POTENTIAL);
			} else if (yAxis.equalsIgnoreCase("BC_CLAIMED") || yAxis.equalsIgnoreCase("BC_POTENTIAL")) {
				claimCategoryCode = "BC";
				potential = String.valueOf(yAxis).substring(3);
				potential = String.valueOf(potential).substring(0, 1).toUpperCase()
						.concat(String.valueOf(potential).substring(1).toLowerCase());
				customerEntPopupQuery.append(BillingCustEntitlementConstants.GET_ENTITLEMENT_ANALYSIS_CLAIM_POTENTIAL);
			} else if (yAxis.equalsIgnoreCase("LITIGATION")) {
				claimCategoryCode = "Litigation";
			} else if (yAxis.equalsIgnoreCase("OTHERS")) {
				claimCategoryCode = "Others";
			}

			PreparedStatement pstm = con.prepareStatement(customerEntPopupQuery.toString());
			pstm.setString(1, projectId);
			pstm.setString(2, claimStatus);
			pstm.setString(3, claimCategoryCode);
			if (!potential.equalsIgnoreCase(BillingCustEntitlementConstants.EMPTY_STRING)) {
				pstm.setString(4, potential);
			}
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				CustEntitlementChartPopupDTO dto = new CustEntitlementChartPopupDTO();
				dto.setClaimNo(null != rs.getString("claim_no") ? rs.getString("claim_no")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setDescription(null != rs.getString("title") ? rs.getString("title")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimant(null != rs.getString("claimant") ? rs.getString("claimant")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimStatus(null != rs.getString("claim_status") ? rs.getString("claim_status")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setPotentiality(null != rs.getString("potential") ? rs.getString("potential")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimCategory(null != rs.getString("claim_category_code") ? rs.getString("claim_category_code")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimDesc(null != rs.getString("claim_category") ? rs.getString("claim_category")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimAmount(null != rs.getString("claimed_amount_pot_exposure")
						? rs.getString("claimed_amount_pot_exposure")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setCurr(null != rs.getString("currency") ? rs.getString("currency")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setComments(null != rs.getString("summary_comments") ? rs.getString("summary_comments")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setIssueDesc(null != rs.getString("issue_description") ? rs.getString("issue_description")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setBhPosition(null != rs.getString("bh_position") ? rs.getString("bh_position")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setBhRecommendation(null != rs.getString("bh_recommendation") ? rs.getString("bh_recommendation")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setNextAction(null != rs.getString("next_action") ? rs.getString("next_action")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setContractManager(null != rs.getString("contract_manager") ? rs.getString("contract_manager")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setAccuralAmtOrig(null != rs.getString("accrual_amount_type") ? rs.getString("accrual_amount_type")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setConcAccuralOrig(null != rs.getString("related_conc_accrual_amount")
						? rs.getString("related_conc_accrual_amount")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setUnlockPastDueOrig(null != rs.getString("un_locked_past_due") ? rs.getString("un_locked_past_due")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setCreationDate(null != rs.getString("creation_date") ? rs.getString("creation_date")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setExpectedClosureDate(
						null != rs.getString("expected_timing_closure") ? rs.getString("expected_timing_closure")
								: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setSettlementDate(null != rs.getString("settlement_date") ? rs.getString("settlement_date")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setEntitlementUSD(null != rs.getString("entitlement") ? rs.getString("entitlement")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setAccuralByTypeUSD(null != rs.getString("accrual_by_type") ? rs.getString("accrual_by_type")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setConcAccuralUSD(null != rs.getString("conc_accrual") ? rs.getString("conc_accrual")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setUnlockPastDueUSD(
						null != rs.getString("un_locked_past_due_usd") ? rs.getString("un_locked_past_due_usd")
								: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setProject(null != rs.getString("project_number") ? rs.getString("project_number")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Error in getting Customer Entitlement - Entitlement Analysis Popup :: {}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error(
							"Error in getting Customer Entitlement - Entitlement Analysis Popup :: {}" , e.getMessage());
				}
			}
		}
		return list;
	}

	@Override
	public List<CustEntitlementChartPopupDTO> getCustEntClosurePlanPopup(String projectId, String chartType,
			String xAxis, String yAxis) {
		Connection con = null;
		List<CustEntitlementChartPopupDTO> list = new ArrayList<CustEntitlementChartPopupDTO>();
		StringBuilder customerEntPopupQuery = new StringBuilder();
		String quarterDetail = "", potential = "";
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			customerEntPopupQuery.append(BillingCustEntitlementConstants.GET_CUST_ENTITLEMENT_POPUP);
			customerEntPopupQuery.append(BillingCustEntitlementConstants.GET_ENTITLEMENT_ANALYSIS_OUARTER_DETAIL);

			if (xAxis.equalsIgnoreCase("ALREADY_DUE")) {
				quarterDetail = "ALREADY_DUE";
			} else if (xAxis.equalsIgnoreCase("CURRENT_QUARTER")) {
				quarterDetail = "CURRENT_QUARTER";
			} else if (xAxis.equalsIgnoreCase("NEXT_QUARTER")) {
				quarterDetail = "NEXT_QUARTER";
			} else if (xAxis.equalsIgnoreCase("ABV_NEXT_QUARTER")) {
				quarterDetail = "ABOVE_NEXT_QUARTER";
			}

			if (yAxis.equalsIgnoreCase("POTENTIAL_ENT")) {
				potential = "Potential";
				customerEntPopupQuery.append(BillingCustEntitlementConstants.GET_ENTITLEMENT_ANALYSIS_CLAIM_POTENTIAL);
			} else if (yAxis.equalsIgnoreCase("CLAIMED_ENT")) {
				potential = "Claimed";
				customerEntPopupQuery.append(BillingCustEntitlementConstants.GET_ENTITLEMENT_ANALYSIS_CLAIM_POTENTIAL);
			}

			PreparedStatement pstm = con.prepareStatement(customerEntPopupQuery.toString());
			pstm.setString(1, projectId);
			pstm.setString(2, quarterDetail);
			if (!potential.equalsIgnoreCase(BillingCustEntitlementConstants.EMPTY_STRING)) {
				pstm.setString(3, potential);
			}
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				CustEntitlementChartPopupDTO dto = new CustEntitlementChartPopupDTO();
				dto.setClaimNo(null != rs.getString("claim_no") ? rs.getString("claim_no")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setDescription(null != rs.getString("title") ? rs.getString("title")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimant(null != rs.getString("claimant") ? rs.getString("claimant")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimStatus(null != rs.getString("claim_status") ? rs.getString("claim_status")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setPotentiality(null != rs.getString("potential") ? rs.getString("potential")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimCategory(null != rs.getString("claim_category_code") ? rs.getString("claim_category_code")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimDesc(null != rs.getString("claim_category") ? rs.getString("claim_category")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimAmount(null != rs.getString("claimed_amount_pot_exposure")
						? rs.getString("claimed_amount_pot_exposure")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setCurr(null != rs.getString("currency") ? rs.getString("currency")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setComments(null != rs.getString("summary_comments") ? rs.getString("summary_comments")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setIssueDesc(null != rs.getString("issue_description") ? rs.getString("issue_description")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setBhPosition(null != rs.getString("bh_position") ? rs.getString("bh_position")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setBhRecommendation(null != rs.getString("bh_recommendation") ? rs.getString("bh_recommendation")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setNextAction(null != rs.getString("next_action") ? rs.getString("next_action")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setContractManager(null != rs.getString("contract_manager") ? rs.getString("contract_manager")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setAccuralAmtOrig(null != rs.getString("accrual_amount_type") ? rs.getString("accrual_amount_type")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setConcAccuralOrig(null != rs.getString("related_conc_accrual_amount")
						? rs.getString("related_conc_accrual_amount")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setUnlockPastDueOrig(null != rs.getString("un_locked_past_due") ? rs.getString("un_locked_past_due")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setCreationDate(null != rs.getString("creation_date") ? rs.getString("creation_date")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setExpectedClosureDate(
						null != rs.getString("expected_timing_closure") ? rs.getString("expected_timing_closure")
								: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setSettlementDate(null != rs.getString("settlement_date") ? rs.getString("settlement_date")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setEntitlementUSD(null != rs.getString("entitlement") ? rs.getString("entitlement")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setAccuralByTypeUSD(null != rs.getString("accrual_by_type") ? rs.getString("accrual_by_type")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setConcAccuralUSD(null != rs.getString("conc_accrual") ? rs.getString("conc_accrual")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setUnlockPastDueUSD(
						null != rs.getString("un_locked_past_due_usd") ? rs.getString("un_locked_past_due_usd")
								: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setProject(null != rs.getString("project_number") ? rs.getString("project_number")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Error in getting Customer Entitlement - Closure Plan Popup ::{} " , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Customer Entitlement - Closure Plan Popup ::{} " , e.getMessage());
				}
			}
		}
		return list;
	}

	@Override
	public List<CustEntitlementChartPopupDTO> getCustomerEntDetails(String projectId) {
		Connection con = null;
		List<CustEntitlementChartPopupDTO> list = new ArrayList<CustEntitlementChartPopupDTO>();
		StringBuilder customerEntPopupQuery = new StringBuilder();
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			customerEntPopupQuery.append(BillingCustEntitlementConstants.GET_CUST_ENTITLEMENT_POPUP);

			PreparedStatement pstm = con.prepareStatement(customerEntPopupQuery.toString());
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				CustEntitlementChartPopupDTO dto = new CustEntitlementChartPopupDTO();
				dto.setClaimNo(null != rs.getString("claim_no") ? rs.getString("claim_no")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setDescription(null != rs.getString("title") ? rs.getString("title")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimant(null != rs.getString("claimant") ? rs.getString("claimant")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimStatus(null != rs.getString("claim_status") ? rs.getString("claim_status")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setPotentiality(null != rs.getString("potential") ? rs.getString("potential")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimCategory(null != rs.getString("claim_category_code") ? rs.getString("claim_category_code")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimDesc(null != rs.getString("claim_category") ? rs.getString("claim_category")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setClaimAmount(null != rs.getString("claimed_amount_pot_exposure")
						? rs.getString("claimed_amount_pot_exposure")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setCurr(null != rs.getString("currency") ? rs.getString("currency")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setComments(null != rs.getString("summary_comments") ? rs.getString("summary_comments")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setIssueDesc(null != rs.getString("issue_description") ? rs.getString("issue_description")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setBhPosition(null != rs.getString("bh_position") ? rs.getString("bh_position")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setBhRecommendation(null != rs.getString("bh_recommendation") ? rs.getString("bh_recommendation")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setNextAction(null != rs.getString("next_action") ? rs.getString("next_action")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setContractManager(null != rs.getString("contract_manager") ? rs.getString("contract_manager")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setAccuralAmtOrig(null != rs.getString("accrual_amount_type") ? rs.getString("accrual_amount_type")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setConcAccuralOrig(null != rs.getString("related_conc_accrual_amount")
						? rs.getString("related_conc_accrual_amount")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setUnlockPastDueOrig(null != rs.getString("un_locked_past_due") ? rs.getString("un_locked_past_due")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setCreationDate(null != rs.getString("creation_date") ? rs.getString("creation_date")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setExpectedClosureDate(
						null != rs.getString("expected_timing_closure") ? rs.getString("expected_timing_closure")
								: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setSettlementDate(null != rs.getString("settlement_date") ? rs.getString("settlement_date")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setEntitlementUSD(null != rs.getString("entitlement") ? rs.getString("entitlement")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setAccuralByTypeUSD(null != rs.getString("accrual_by_type") ? rs.getString("accrual_by_type")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setConcAccuralUSD(null != rs.getString("conc_accrual") ? rs.getString("conc_accrual")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setUnlockPastDueUSD(
						null != rs.getString("un_locked_past_due_usd") ? rs.getString("un_locked_past_due_usd")
								: BillingCustEntitlementConstants.EMPTY_STRING);
				dto.setProject(null != rs.getString("project_number") ? rs.getString("project_number")
						: BillingCustEntitlementConstants.EMPTY_STRING);
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Error in getting Customer Entitlement - Excel Details :: {}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("Error in getting Customer Entitlement - Excel Details :: {}" , e.getMessage());
				}
			}
		}
		return list;
	}

}
