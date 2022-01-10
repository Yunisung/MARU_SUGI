package com.pgmate.lib.key;

/**
 * @author Administrator
 *
 */
public enum CPKEY {
	TOKEN {
		@Override
		public String toString(){
			return "tk_";
		}
	},
	CUSTOMER {
		@Override
		public String toString(){
			return "cust_";
		}
	},
	CARD {
		@Override
		public String toString(){
			return "card_";
		}
	},
	LOCALPAYMENT {
		@Override
		public String toString(){
			return "locp_";
		}
	},
	PUBLIC_KEY {
		@Override
		public String toString(){
			return "pk_";
		}
	},
	SECRET_KEY {
		@Override
		public String toString(){
			return "sk_";
		}
	},
	BILLING {
		@Override
		public String toString(){
			return "bill_";
		}
	},
	SHIPPING {
		@Override
		public String toString(){
			return "ship_";
		}
	},
	PRODUCT {
		@Override
		public String toString(){
			return "pdt_";
		}
	},
	AGENT_SERVICE {
		@Override
		public String toString(){
			return "agtsvc_";
		}
	},
	ADMIN_INFO {
		@Override
		public String toString(){
			return "admif_";
		}
	},
	INTER_MSG {
		@Override
		public String toString(){
			return "trmsg_";
		}
	},
	INTER_ERROR {
		@Override
		public String toString(){
			return "trerr_";
		}
	},
	MERCHANT_ID {
		@Override
		public String toString(){
			return "mcht_";
		}
	},
	GROUP_ID {
		@Override
		public String toString(){
			return "gp_";
		}
	},
	AGENT_ID {
		@Override
		public String toString(){
			return "agt_";
		}
	},
	CAPTURE_ID{
		@Override
		public String toString(){
			return "cp_";
		}
	},
	SERVICE_KEY{
		@Override
		public String toString(){
			return "bk_";
		}
	},
	MERCHANT_OPEN_STATUS {
		@Override
		public String toString(){
			return "mcht_open_status_";
		}
	},
	TRX_ID {
		@Override
		public String toString(){
			return "trx_";
		}
	},
	LOCAL {
		@Override
		public String toString(){
			return "lc_";
		}
	},
	CAPTURE {
		@Override
		public String toString(){
			return "cap_";
		}
	},
	REFUND {
		@Override
		public String toString(){
			return "rfd_";
		}
	},
	MCHT_TAX {
		@Override
		public String toString(){
			return "mtx";
		}
	},
	MCHT_TMN {
		@Override
		public String toString(){
			return "mtm";
		}
	},
	ACCNT{
		@Override
		public String toString(){
			return "accnt_";
		}
	},
	USER{
		@Override
		public String toString(){
			return "user_";
		}
	},
	MCHT{
		@Override
		public String toString(){
			return "mcht_";
		}
	},
	TRANSFER{
		@Override
		public String toString(){
			return "trx_";
		}
	}
	
	

}
