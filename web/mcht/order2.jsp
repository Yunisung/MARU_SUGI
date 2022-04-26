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
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Creditop</title>
<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<script src=<c:url value="/js/jquery.min.js"/>></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script type="text/javascript" src="http://127.0.0.1:10002/js/clientside.js"></script>
<style>
	body
	input[type="number"]::-webkit-outer-spin-button,
	input[type="number"]::-webkit-inner-spin-button {
    	-webkit-appearance: none;
   		margin: 0;
	}
</style>
</head>
<body>
<c:choose>
	<c:when test="${mcht.payKey ne null }">
	
		<script>
			$( document ).ready(function() {
			   $('#payerName').focus();
			   
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
			
			function fn_products(){

				var products = new Array();
				var product = {"name":$('#name').val(),"price":$('#price').val(),"qty":$('#qty').val(),"desc":$('#desc').val()};
				products.push(product);
				
				return products;
			}
			
			function fn_sms(){
				
				var products = fn_products();
				
				console.log(products);
				
				if(!products.length){
					alert('상품정보를 입력하세요.');
					return false;
				}
				
				if(!$.trim($('#payerName').val())){
					alert('구매자명 입력은 필수입니다.');
					$('#payerName').focus();
					return false;
				}
				
				if(!$.trim($('#payerTel').val())){
					alert('구매자전화번호 입력은 필수입니다.');
					$('#payerTel').focus();
					return false;
				}
				
				$('#products').val(JSON.stringify(products));
				$('#amount').val($('#price').val());
				
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
				var baseUrl = $('#baseUrl').val()+'/mcht/';
				if(navigator.userAgent.match(/Android/i) != null){
					location.href = 'sms:'+$('#payerTel').val()+'?body='+baseUrl+smsKey+"/pay";
				}else if(navigator.userAgent.match(/iPhone|iPad|iPod/i) != null){
					location.href = 'sms:'+$('#payerTel').val()+'&body='+baseUrl+smsKey+"/pay";
				}
			}
			
			function fn_pay(){
				
				var products = fn_products();
				
				if(!products.length){
					alert('상품정보를 입력하세요.');
					return false;
				}
				
				if(!$.trim($('#payerName').val())){
					alert('구매자명 입력은 필수입니다.');
					$('#payerName').focus();
					return false;
				}
				
				if(!$.trim($('#payerTel').val())){
					alert('구매자전화번호 입력은 필수입니다.');
					$('#payerTel').focus();
					return false;
				}
				
				
				$('#amount').val($('#price').val());
				
				
				TPO.pay({
			        amount: $('#amount').val(),
			        publicKey: $('#payKey').val(),
			        products: products, // Array
			        responseFunction: eventFnc, // Function
			        payerName: $('#payerName').val(),
			        payerEmail: $('#payerEmail').val(),
			        payerTel: $('#payerTel').val()
				});
			}
			
			function eventFnc(data) {
				   console.log('eventFnc:', data);
				}
			function fn_logout(){
				location.href = "<c:url value='/logout'/>";
			}
		</script>
	
		<div class="container" style="margin-bottom:10px;">
			<div class="row text-right" style="padding-right:15px;">
				<button class="btn btn-sm btn-primary" style="margin-top:10px;" onclick='javascript:fn_logout();'>로그아웃</button>
			</div>
			<h2 class="text-center">${mcht.name }&nbsp;수기결제</h2>
			
			<form id="form1" class="form-horizontal" autocomplete="off">
				<input type="hidden" id="payKey" name="payKey" value="${mcht.payKey }"/>
				<input type="hidden" id="mchtId" name="mchtId" value="${mcht.mchtId }"/>
				<input type="hidden" id="mchtName" name="mchtName" value="${mcht.name }"/>
				<input type="hidden" id="products" name="products"/>
				<input type="hidden" id="amount" name="amount"/>
				<input type="hidden" id="baseUrl" name="baseUrl" value="${baseUrl }"/>
			<div class="form-group">
				<label for="payerName" class="col-sm-2 control-label">구매자명</label>
				<div class="col-sm-10">
					<input type="text" name="payerName" id="payerName" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="payerEmail" class="col-sm-2 control-label">이메일주소</label>
				<div class="col-sm-10">
					<input type="email" name="payerEmail" id="payerEmail" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="payerTel" class="col-sm-2 control-label">전화번호</label>
				<div class="col-sm-10">
					<input type="tel" name="payerTel" id="payerTel" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">상품명</label>
				<div class="col-sm-10">
					<input type="text" name="name" id="name" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label for="qty" class="col-sm-2 control-label">수량</label>
				<div class="col-sm-10">
					<input type="number" name="qty" id="qty" class="form-control" pattern="[0-9]*" inputmode="numeric" min="0">
				</div>
			</div>
			<div class="form-group">
				<label for="price" class="col-sm-2 control-label">결제금액</label>
				<div class="col-sm-10">
					<input type="number" name="price" id="price" class="form-control" pattern="[0-9]*" inputmode="numeric" min="0">
				</div>
			</div>
			<div class="form-group">
				<label for="desc" class="col-sm-2 control-label">상품설명</label>
				<div class="col-sm-10">
					<textarea rows="5" class="form-control col-sm-5" id="desc" name="desc"></textarea>
				</div>
			</div>
			</form>
			<div class="row">
				<div class="col-sm-2"></div>
				<div class="col-sm-10">
					<button class="btn btn-sm btn-primary" onclick="javascript:fn_pay();">결제</button>&nbsp;
					<button class="btn btn-sm btn-info" id="smsBtn" style="display:none;" onclick="javascript:fn_sms();">SMS</button>
				</div>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		등록되지 않은 터미널
	</c:otherwise>
</c:choose>
</body>
</html>