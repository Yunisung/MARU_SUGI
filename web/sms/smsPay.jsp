<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- <!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CREDITOP</title>
<link rel="stylesheet" href='<c:url value="/css/bootstrap.css"/>'>
<script src=<c:url value="/js/jquery.min.js"/>></script>
<script src=<c:url value="/js/bootstrap.js"/>></script>
<script type="text/javascript" src="https://svcapi.mtouch.com/js/clientside.js"></script> -->

<!DOCTYPE html>
<html lang="en" style="height:100%;">
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
<link rel="stylesheet" href='<c:url value="/css/bootstrap.css"/>'>

  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <link rel="stylesheet" href='<c:url value="/css/index.css"/>'>
  
    <script src=<c:url value="/js/jquery.min.js"/>></script>
  <script src=<c:url value="/js/bootstrap.js"/>></script>



  <script type="text/javascript" src="https://devapi.bkwinners.kr/js/clientside.js"></script>
  <script>
   window.focus();
   </script>

  <title>CREDITOP</title>
</head>

</head>
<body class="c3-body">
	<form id="form1">
		<input type="hidden" id="sms_trackId" name="trackId" value="${trackId }"/>
		<input type="hidden" id="sms_payKey" name="payKey" value="${mcht.payKey }"/>
		<input type="hidden" id="sms_payInfo" name="payInfo" value="${payInfo }"/>
    	<input type="hidden" id="sms_tip" name="tip" value="${tip }"/>
    	<input type="hidden" id="sms_roundingAmount" name="roundingAmount" value="${roundingAmount }"/>
		<input type="hidden" id="sms_mchtName" name="mchtName" value="${mcht.name }"/>
		<input type="hidden" id="sms_products" name="products" value="${mcht.products }"/>
		<input type="hidden" id="sms_amount" name="amount" value="${mcht.amount }"/>
		<input type="hidden" id="sms_payerName" name="payerName" value="${mcht.payerName }"/>
		<input type="hidden" id="sms_payerEmail" name="payerEmail" value="${mcht.payerEmail }"/>
		<input type="hidden" id="sms_payerTel" name="payerTel" value="${mcht.payerTel }"/>
		<input type="hidden" id="sms_smsKey" name="smsKey" value="${mcht.smsKey }"/>
		<input type="hidden" id="sms_apiMaxInstall" name="apiMaxInstall" value="${apiMaxInstall }"/>
    	<input type="hidden" id="sms_semiAuth" name="semiAuth" value="${semiAuth }"/>
    	<input type="hidden" id="sms_installment" name="installment"/>
	    <input type="hidden" id="sms_trxId" name="trxId"/>
	    <input type="hidden" id="sms_cardNumber" name="cardNumber"/>
	    <input type="hidden" id="sms_expiry" name="expiry"/>
	    <input type="hidden" id="sms_cardAuth" name="cardAuth"/>
	    <input type="hidden" id="sms_authPw" name="authPw"/>
	    <input type="hidden" id="sms_authDob" name="authDob"/>
	</form>


<!--   <div class="c3-header"> -->
<!--     <span id="c3-btn-close" aria-label="Close"></span> -->
<!--     <div class="c3-logo"> -->
<!-- <!--       <img id="c3-logo-img" src="/img/logo.png"> --> -->
<!-- 		sms Test 중입니다 -->
<!--     </div> -->
<!--   </div> -->
${mcht.payKey }/${mcht.smsKey }/${mcht.payerName }/${mcht.name }/${mcht.amount }
  
  <div class="c3-content">
    <div class="contant-wrapper">
      <div class="c3-product-tag c3-tag" id="c3-product-tag"></div>
      <div class="row c3-money-wrapper">
          <div class="c3-money-tag-wrapper">
            <div class="c3-money-tag c3-tag">결제 금액</div>
          </div>
          <span class="c3-money">₩<span id="c3-amount"></span></span>
      </div>
      <div class="row">
          <span class="c3-card-tag c3-tag">카드번호</span>
          <span class="c3-card">
            <input type="text" name="card" class="c3-input c3-input-card" id="card" maxlength="19" value="" tabindex="1">
          </span>
      </div>
      <div class="row">
          <div class="c3-left-containner">
              <span class="c3-expiry-tag c3-tag">유효기간</span>
              <span class="c3-expiry">
                  <div class="c3-expiry-month">
                    <select class="c3-select-left" name="expiry-month" id="expiry-month" tabindex="2">
                      <option value="01">01</option>
                      <option value="02">02</option>
                      <option value="03">03</option>
                      <option value="04">04</option>
                      <option value="05">05</option>
                      <option value="06">06</option>
                      <option value="07">07</option>
                      <option value="08">08</option>
                      <option value="09">09</option>
                      <option value="10">10</option>
                      <option value="11">11</option>
                      <option value="12">12</option>
                    </select>
                  </div>
                  <div class="c3-expiry-year">
                      <select class="c3-select-right" name="expiry-year" id="expiry-year" tabindex="3">
                      </select>
                    </div>
              </span>
          </div>
          <div class="c3-right-containner">
              <span class="c3-installment-tag c3-tag">할부</span>
              <span class="c3-installment">
                <select class="c3-select" name="installment" id="installment" tabindex="4">
                </select>
              </span>
          </div>
      </div>
      <div class="row semi-auth" style="display:none;">
          <div class="c3-left-containner">
              <span class="c3-authPw-tag c3-tag">비밀번호 앞 2자리</span>
              <div class="c3-authPw">
                <input type="password" name="authPw" class="c3-input c3-input-authPw" id="authPw" maxlength="2" value="" tabindex="5">
                <input type="password" class="c3-input c3-span-authPw" value="oo" readonly tabindex="-1">
              </div>
          </div>
          <div class="c3-right-containner">
              <span class="c3-authDob-tag c3-tag">생년월일</span>
              <span class="c3-authDob">
                <input type="text" name="authDob" class="c3-input c3-input-authDob" id="authDob" maxlength="6" placeholder="YY MM DD" value="" tabindex="6">
              </span>
          </div>
      </div>
      <div class="row payer-info">
          <span class="c3-tag">구매자 성명</span>
          <span class="c3-payerName">
            <input type="text" name="payerName" class="c3-input c3-input-payerName" id="payerName" value="" tabindex="7">
          </span>
      </div>
      <div class="row payer-info">
          <span class="c3-tag">구매자 Email</span>
          <span class="c3-payerEmail">
            <input type="text" name="payerEmail" class="c3-input c3-input-payerEmail" id="payerEmail" value="" tabindex="8">
          </span>
      </div>
      <div class="row payer-info">
          <span class="c3-tag">구매자 연락처</span>
          <span class="c3-payerTel">
            <input type="text" name="payerTel" class="c3-input c3-input-payerTel" id="payerTel" value="" tabindex="9">
          </span>
      </div>
      
      <div class="seprator"></div>
      <div class="row">
          <a href="javascript:void(0);" class="btn green" id="c3-btn-pay" tabindex="-1">결제</a>
      </div>
    </div>
  </div>

  <div class="" id="c3-loading" style="display: block;">
    <div class="spinner">
      <div class="rect1"></div>
      <div class="rect2"></div>
      <div class="rect3"></div>
      <div class="rect4"></div>
      <div class="rect5"></div>
    </div>
    <div class="loading-tag">Loading...</div>  
  </div>

  <div class="sweet-alert showSweetAlert" id="c3-alert" style="display: none;">
    실패 아이콘
    <div class="sa-icon sa-error animateErrorIcon" id="c3-alert-error" style="display: none;">
      <span class="sa-x-mark">
        <span class="sa-line sa-left"></span>
        <span class="sa-line sa-right"></span>
      </span>
    </div>
    성공 아이콘
    <div class="sa-icon sa-success animate" id="c3-alert-success" style="display: none;">
      <span class="sa-line sa-tip animateSuccessTip"></span>
      <span class="sa-line sa-long animateSuccessLong"></span>
      <div class="sa-placeholder"></div>
      <div class="sa-fix"></div>
    </div>
    
    <div id="c3-alert-success-wrapper" style="display: none;">
        <h2>승인완료</h2>
        <div class="sa-success-content">
            <span class="sa-cell">승인 금액</span>
            <span class="sa-cell" id="c3-alert-success-amount"></span>
          </div>
        <div class="sa-success-content">
          <span class="sa-cell">거래번호</span>
          <span class="sa-cell" id="c3-alert-success-trxId"></span>
        </div>
        <div class="sa-success-content">
          <span class="sa-cell">거래일자</span>
          <span class="sa-cell" id="c3-alert-success-trxDate"></span>
        </div>
        <div class="sa-success-content">
          <span class="sa-cell">카드번호</span>
          <span class="sa-cell" id="c3-alert-success-card"></span>
        </div>
        <div class="sa-success-content">
          <span class="sa-cell">승인번호</span>
          <span class="sa-cell" id="c3-alert-success-authCd"></span>
        </div>
        <div class="sa-success-content">
          <span class="sa-cell">카드종류</span>
          <span class="sa-cell" id="c3-alert-success-issuer"></span>
        </div>
    </div>
    <div id="c3-alert-error-wrapper" style="display: none;">
        <h2>승인실패</h2>
        <p style="display: block;" id="c3-alert-error-msg"></p>
    </div>
    <div class="sa-button-container">
      <div class="seprator"></div>
      <div class="row" style="padding: 0 10%;">
        <a href="javascript:void(0);" class="btn green" id="c3-btn-ok" tabindex="-1">확인</a>
      </div>
    </div>
  </div>


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
// 	        webhookUrl: 'http://10.100.200.10:10009/sms/smsPayWebHook',
			//webhookUrl: 'http://192.168.0.53:10034/sms/smsPayWebHook',
	        redirectUrl: $('#baseUrl').val()+'/sms/smsPayComplete',
	        mode: 'popup'
		});
	}
	</script> 
		
</body>

<script src=<c:url value="/js/index.js?v=1"/>></script>
</html>