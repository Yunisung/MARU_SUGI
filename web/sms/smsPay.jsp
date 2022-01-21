<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>MTouch</title>
<link rel="stylesheet" href='<c:url value="/css/bootstrap.css"/>'>
<script src=<c:url value="/js/jquery.min.js"/>></script>
<script src=<c:url value="/js/bootstrap.js"/>></script>
<script type="text/javascript" src="https://devapi.bkwinners.kr/js/clientside.js?v=1"></script>

</head>
<body>
	<form id="form1">
		<input type="hidden" id="payKey" name="payKey" value="${mcht.payKey }"/>
		<input type="hidden" id="mchtId" name="mchtId" value="${mcht.mchtId }"/>
		<input type="hidden" id="mchtName" name="mchtName" value="${mcht.name }"/>
		<input type="hidden" id="products" name="products" value="${mcht.products }"/>
		<input type="hidden" id="amount" name="amount" value="${mcht.amount }"/>
		<input type="hidden" id="payerName" name="payerName" value="${mcht.payerName }"/>
		<input type="hidden" id="payerEmail" name="payerEmail" value="${mcht.payerEmail }"/>
		<input type="hidden" id="payerTel" name="payerTel" value="${mcht.payerTel }"/>
		<input type="hidden" id="smsKey" name="smsKey" value="${mcht.smsKey }"/>
		<input type="hidden" id="baseUrl" name="baseUrl" value="${baseUrl }"/>
		<input type="hidden" id="trxId" name="trxId"/>
	</form>
	<div style="display: table; position: absolute; top: 0; left: 0; width: 100%; height: 100%;">
		<div style="display: table-cell; vertical-align: middle; text-align: center;">
			<div style="display: inline-block; font-size: 5rem; padding: 5%;" class="bg-info">
				결제화면이 보이지 않으면<br> <strong>팝업해제 후 새로고침</strong> 하세요.
			</div>
		</div>
	</div>
	<script>
		$(window).load(function() {
			fn_pay();
		});
		
	function eventFnc(data) {
		console.log(data);
/*		if(data.result.resultCd == "0000"){
			setTimeout(function(){
				window.location.replace('/sms/'+$('#smsKey').val()+'/pay');
			},500);
		} */
	}
	
	function fn_pay(){	
		MARU.pay({
	        amount: $('#amount').val(),
	        publicKey: $('#payKey').val(),
	        products:JSON.parse($('#products').val()), // Array
	        responseFunction: eventFnc, // Function
	        payerName: $('#payerName').val(),
	        payerEmail: $('#payerEmail').val(),
	        payerTel: $('#payerTel').val(),
	        widgetLogoUrl: '',
	        udf1: $('#smsKey').val(),
	        webhookUrl: 'http://pgwas2:10009/sms/smsPayWebHook',
	        redirectUrl: $('#baseUrl').val()+'/sms/smsPayComplete',
	        mode: 'popup'
		});
	}
		
	</script>
		
</body>
</html>