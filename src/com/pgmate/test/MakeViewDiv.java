package com.pgmate.test;

public class MakeViewDiv {
	private String[] columns = null; 
	
	
	public MakeViewDiv(String[] columns) {
		this.columns = columns;
	}
	
	private String makeViewElement(){
		String result = "";
		String org = "<!--/span-->\n"
		+ "<div class='col-md-6'>\n"
		+ "	<div class='form-group pg-view-group'>\n"
		+ "		<label class='control-label col-md-3'>CHANGETHISTEXT</label>\n"
		+ "		<div class='col-md-9'>\n"
		+ "			<p class='form-control-static'>${DATAMAP.CHANGETHISTEXT}</p>\n"
		+ "		</div>\n"
		+ "	</div>\n"
		+ "</div>\n";
		
		for(String column : columns) {
			result += org.replaceAll("CHANGETHISTEXT", column);
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		MakeViewDiv mkDiv = new MakeViewDiv(new String[]{"capId", "trxId", "mchtId", "tmnId", "trackId", "capType", "rfdType", "rootTrxId", "amount", "vat", "cardId", "issuer", "last4", "authCd", "trxDay", "regDay", "regTime", "regDate", "stlAmount", "stlRate", "stlFee", "stlFeeVat", "stlType", "stlDay", "stlId", "stlDistFee", "stlDistRate", "stlDistDay", "stlDistId", "stlAgencyFee", "stlAgencyRate", "stlAgencyDay", "stlAgencyId", "stlSalesFee", "stlSalesRate", "stlSalesDay", "stlSalesId", "benefit", "name", "distId", "distName", "agencyId", "agencyName", "salesId", "salesName"});
		System.out.println(mkDiv.makeViewElement());
		
	}

}
