<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>CREDITOP</title>
		<link rel="stylesheet" href='<c:url value="/css/bootstrap.css"/>'>
		<link rel="stylesheet" href='<c:url value="/css/layout.css"/>'>
		<script src='<c:url value="/js/jquery.min.js"/>'></script>
		<script src='<c:url value="/js/bootstrap.js"/>'></script>
		<script src='<c:url value="/js/bootbox.min.js"/>'></script>
		<script type="text/javascript" src="https://api.bkwinners.kr/js/clientsideV2.js"></script>
		<style>
			input[type="number"]::-webkit-outer-spin-button, 
			input[type="number"]::-webkit-inner-spin-button {
				-webkit-appearance: none;
				margin: 0;
			}
		</style>
		<script>
			$( document ).ready(function() {
			   $('#name').focus();
			  
			
			   var filter = "win16|win32|win64|mac|macintel";
			   
			   if (filter.indexOf(navigator.platform.toLowerCase()) < 0) {
					$('#smsBtn').show();			    
			    
			   }else {
				   $('#smsBtn').hide();
			   }
			});		
			
			$.fn.serializeObject = function(){
	
			   var o = {};
			   var a = this.serializeArray();
	
			   $.each(a, function() {
			       if (o[this.name]) {
			           if (!o[this.name].push) {
			               o[this.name] = [o[this.name]];
			           }
			           o[this.name].push(this.value || '');
			       } else {
			           o[this.name] = this.value || '';
			       }
			   });
			   return o;
			};
			
			function fn_valid(){
				if(!$.trim($('#name').val())){
					bootbox.alert({
						message: "구매상품명을 입력하세요.",
						callback:function(){
							setTimeout(function (){
								$('#name').focus();	
							},10);
						}
					});
					return false;
				}
				if(!$.trim($('#amount').val())){
					bootbox.alert({
						message: "결제금액을 입력하세요.",
						callback:function(){
							setTimeout(function (){
								$('#amount').focus();	
							},10);
						}
					});
					return false;
				}
				
				if(!$.trim($('#payerName').val())){
					bootbox.alert({
						message: "고객 이름 입력은 필수입니다.",
						callback:function(){
							setTimeout(function (){
								$('#payerName').focus();	
							},10);
						}
					});
					return false;
				}
				
				if(!$.trim($('#payerTel').val())){
					bootbox.alert({
						message: "고객 전화번호 입력은 필수입니다.",
						callback:function(){
							setTimeout(function (){
								$('#name').focus();	
							},10);
						}
					});
					return false;
				}
				return true;
			}
			function fn_sms(){
				
				if(!fn_valid()){
					return false;
				}
				
				var products = new Array();
				var product = {"name":$('#name').val(),"price":$('#amount').val(),"qty":"1","desc":"수기결제"};
				products.push(product);
				
				$('#products').val(JSON.stringify(products));
				
				$.ajax({
			        url: "<c:url value='/mcht/smsPay'/>",
			        type: "POST",
			        data: $('#form1').serialize(),
			        dataType: "json",
			        success: function (data) {
			            console.log(data);
			            fn_sendSms(data.smsKey);
			        },
			        error: function () {
			        	
			        }
			    });
			}
			
			function fn_sendSms(smsKey){
				var payerTel = $('#payerTel').val();
				if(confirm(payerTel+"번호로 결제 URL을 전송하겠습니까?")){
					var baseUrl = 'https://sugi.bkwinners.kr/sms/';
					var url = baseUrl+smsKey+'/pay';
					var content = "상점명: ${TMNNAME }%0D%0A상품명: "+$('#name').val()+"%0D%0A결제금액: "+numberWithCommas($.trim($('#amount').val()))+"원%0D%0A아래 URL을 누르시면, 결제창으로 연결됩니다.%0D%0A"+url;
					if(navigator.userAgent.match(/Android/i) != null){
						location.href = 'sms:'+payerTel+'?body='+content;
					}else if(navigator.userAgent.match(/iPhone|iPad|iPod/i) != null){
						location.href = 'sms:'+payerTel+'&body='+content;
					}
				}
			}
			function fn_pay(){
				
				if(!fn_valid()){
					return false;
				}
				
				var products = new Array();
				var product = {"name":$('#name').val(),"price":$('#amount').val(),"qty":"1","desc":"수기결제"};
				products.push(product);
				
				MARU.pay({
			        amount: $('#amount').val(),
			        publicKey: $('#payKey').val(),
			        products: products, // Array
			        responseFunction: eventFnc, // Function
			        //KJM : redirect url 가져오기 추가
			        //redirectUrl: $('#redirectUrl').val(),
			        payerName: $('#payerName').val(),
			        payerEmail: $('#payerEmail').val(),
			        payerTel: $('#payerTel').val(),
			        mode: 'layer'
				});
			}
			
			function eventFnc(data) {
				   if(data.result.resultCd == "0000"){
					   $('#name').val('');
					   $('#amount').val('');
					   $('#payerName').val('');
					   $('#payerEmail').val('');
					   $('#payerTel').val('');
				   } else {
					   bootbox.alert("수기결제가 정상적으로 승인되지 않았습니다.\n결제정보를 다시 확인해 주시기 바랍니다.");
				   }
				}
			function fn_logout(){
// PYS : 나중에 추가                
//                 $.ajax({
//                     url: '/login/out',
//                     type: 'GET',
//                     success: function (data) {
//                         if(data == "0000"){
//                             alert('접속 세션이 종료되었습니다. 다시 로그인하여 주시기 바랍니다.');
//                             location.replace('/login/form');
//                         }
                        
//                     },
//                     error: function (request,status,error) {
//                         bootbox.alert("잠시후 다시 시도해주세요.");
//                     }
//                 });
                
				location.href = "<c:url value='/login/out'/>";
			}
			
			function numberWithCommas(x) {
	            return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	        }
		</script>
	</head>
	<body>

	
		<div class="container" style="margin-bottom:10px;">
			<div class="row">
			<span class="col-md-offset-1">
				<img src="/img/logo_top.png" style="margin-top: 10px; margin-left:10px; height: 20px;">
			</span>
			<span class="pull-right" style="padding-right:15px;">
				<button class="btn btn-sm btn-primary" style="margin-top:10px;" onclick='javascript:fn_logout();'>로그아웃</button>
			</span>
			</div>
			<h3 class="text-center">${CP_SESSION.mchtName }&nbsp;수기결제</h3>
			
			<form id="form1" class="form-horizontal" autocomplete="off">
				<input type="hidden" id="payKey" name="payKey" value="${CP_SESSION.payKey }"/>
				<input type="hidden" id="mchtId" name="mchtId" value="${CP_SESSION.mchtId }"/>
				<input type="hidden" id="mchtName" name="mchtName" value="${CP_SESSION.mchtName }"/>
				<input type="hidden" id="products" name="products"/>
				<input type="hidden" id="baseUrl" name="baseUrl" value="${baseUrl }"/>
				<!-- KJM : redirect url 추가 
				<input type="hidden" name="redirectUrl" id="redirectUrl" value="http://127.0.0.1:10035/redirect/Redirect.html" class="form-control">
				-->
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">구매 상품명</label>
				<div class="col-sm-10">
					<input type="text" name="name" id="name" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="price" class="col-sm-2 control-label">결제금액</label>
				<div class="col-sm-10">
					<input type="number" name="amount" id="amount" class="form-control" pattern="[0-9]*" inputmode="numeric" min="0">
				</div>
			</div>
			<div class="form-group">
				<label for="payerName" class="col-sm-2 control-label">고객 이름</label>
				<div class="col-sm-10">
					<input type="text" name="payerName" id="payerName" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="payerTel" class="col-sm-2 control-label">고객 전화번호</label>
				<div class="col-sm-10">
					<input type="tel" name="payerTel" id="payerTel" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="payerEmail" class="col-sm-2 control-label">고객 이메일</label>
				<div class="col-sm-10">
					<input type="email" name="payerEmail" id="payerEmail" class="form-control">
				</div>
			</div>
			

		</form>
			<div class="row">
				<div class="col-sm-2"></div>
				<div class="col-sm-10">
					<button class="btn btn-sm btn-primary" onclick="javascript:fn_pay();">결제</button>&nbsp;
					<button class="btn btn-sm btn-info pull-right" id="smsBtn" style="display:none;" onclick="javascript:fn_sms();">SMS</button>
				</div>
			</div>
			<div class="row" style="margin-top:10px;">
				<div class="col-sm-2"></div>
				<div class="col-sm-10">
					정산계좌 : ${ACCNTMAP.bankName } ${ACCNTMAP.account } ${ACCNTMAP.accntHolder }
				</div>
			</div>
			<div class="row" style="margin-top:10px;">
				<div class="col-sm-2"></div>
				<div class="col-sm-10">
					취소입금계좌 : 신한은행 100-035-741308 부국위너스(주)
				</div>
			</div>
		</div>
	</body>
</html>