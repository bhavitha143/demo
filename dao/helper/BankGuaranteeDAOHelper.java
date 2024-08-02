package com.bh.realtrack.dao.helper;

import org.springframework.stereotype.Component;

import com.bh.realtrack.util.BillingBankGuaranteeConstants;

/**
 * @author Shweta D. Sawant
 */
@Component
public class BankGuaranteeDAOHelper {

	public String getBankGuaranteePopupQuery(String chartType, String status) {
		StringBuilder queryString = new StringBuilder();

		if (chartType.equalsIgnoreCase("SUMMARY")) {

			queryString.append(BillingBankGuaranteeConstants.GET_BG_SUMMARY_DETAILS_POPUP);
			if (status.equalsIgnoreCase("UNDER_RELEASE")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_SUMMARY_UNDER_RELEASE_DETAILS);
			} else if (status.equalsIgnoreCase("ISSUED")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_SUMMARY_ISSUED_DETAILS);
			} else if (status.equalsIgnoreCase("EXPIRED")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_SUMMARY_EXPIRED_DETAILS);
			}

		} else if (chartType.equalsIgnoreCase("BG_AMT_CONNECTED")) {

			queryString.append(BillingBankGuaranteeConstants.GET_BG_SUMMARY_DETAILS_POPUP);
			queryString.append(" and upper(is_cash_milestone_linked_to_bond) = 'YES'");

			if (status.equalsIgnoreCase("APPLIED")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_CONNECTED_APPLIED_DETAILS);
			} else if (status.equalsIgnoreCase("IN_PROCESS")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_CONNECTED_IN_PROCESS_DETAILS);
			} else if (status.equalsIgnoreCase("OUT_OF_BID")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_CONNECTED_OUT_OF_BID_DETAILS);
			} else if (status.equalsIgnoreCase("READY_FOR_REVIEW")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_CONNECTED_READY_FOR_REVIEW_DETAILS);
			} else if (status.equalsIgnoreCase("READY_TO_APPLY")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_CONNECTED_READY_TO_APPLY_DETAILS);
			} else if (status.equalsIgnoreCase("ISSUED")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_CONNECTED_ISSUED_DETAILS);
			} else if (status.equalsIgnoreCase("EXPIRED")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_CONNECTED_EXPIRED_DETAILS);
			}

		} else if (chartType.equalsIgnoreCase("BG_AMT_ISSUED_BY_TYPE")) {

			queryString.append(BillingBankGuaranteeConstants.GET_BG_SUMMARY_DETAILS_POPUP);
			if (status.equalsIgnoreCase("ADV_PAY_GUARANTEE")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_ISSUED_ADV_PAY_GUARANTEE_DETAILS);
			} else if (status.equalsIgnoreCase("CUST_GUARANTEE")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_ISSUED_CUST_GUARANTEE_DETAILS);
			} else if (status.equalsIgnoreCase("PAYMENT_GUARANTEE")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_ISSUED_PAYMENT_GUARANTEE_DETAILS);
			} else if (status.equalsIgnoreCase("PERFORM_GUARANTEE")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_ISSUED_PERFORM_GUARANTEE_DETAILS);
			} else if (status.equalsIgnoreCase("TENDER_GUARANTEE")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_ISSUED_TENDER_GUARANTEE_DETAILS);
			} else if (status.equalsIgnoreCase("OTHER_TYPE")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_ISSUED_OTHER_TYPE_DETAILS);
			}

		} else if (chartType.equalsIgnoreCase("BG_AMT_TO_RECOVER")) {

			queryString.append(BillingBankGuaranteeConstants.GET_BG_SUMMARY_DETAILS_POPUP);
			if (status.equalsIgnoreCase("OPEN_ENDED_EXPIRED")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_TO_RECOVER_EXPIRED_DETAILS);
			} else if (status.equalsIgnoreCase("AGED_BG")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_TO_RECOVER_OLDER_THAN_10_YRS_DETAILS);
			} else if (status.equalsIgnoreCase("OTHERS")) {
				queryString.append(BillingBankGuaranteeConstants.GET_BG_AMT_TO_RECOVER_OTHERS_DETAILS);
			}

		}
		return queryString.toString();
	}
}
