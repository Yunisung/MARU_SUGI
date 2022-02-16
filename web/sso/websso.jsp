<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Creditop 수기결제</title>
		<link rel="stylesheet" href='<c:url value="/css/bootstrap.css"/>'>
		<link rel="stylesheet" href='<c:url value="/css/layout.css"/>'>
		<script src='<c:url value="/js/jquery.min.js"/>'></script>
		<script src='<c:url value="/js/bootstrap.js"/>'></script>
		<script src='<c:url value="/js/bootbox.min.js"/>'></script>


		<script>
	$( document ).ready(function() {
	   $('#tmnId').focus();
	});		

	function fn_login(){
		/*if(!$.trim($('#mchtId').val())){
			alert("가맹점 아이디를 입력해주세요.");
			$('#mchtId').focus();
			return false;
		}*/
		if(!$.trim($('#tmnId').val())){
			bootbox.alert({
				message: "터미널 아이디를 입력해주세요.",
				callback:function(){
					setTimeout(function (){
						$('#tmnId').focus();	
					},10);
				}
			});
			return false;
		}
		if(!$.trim($('#serial').val())){
			bootbox.alert({
				message: "일련번호를 입력해주세요.",
				callback:function(){
					setTimeout(function (){
						$('#serial').focus();	
					},10);
				}
			});
			return false;
		}
		
		if($("input:checkbox[id='rememberLogin']").is(":checked")){
			$('#autoLogin').val('Y');
		}else{ 
			$('#autoLogin').val('N');
		}
		
		//$('#form1').submit();
		
		$.ajax({
	        url: "<c:url value='/login/in'/>",
	        type: "POST",
	        data: $('#form1').serialize(),
	        dataType: "json",
	        success: function (data) {
	            if(data.resultCd == "Y"){
	            	location.replace("<c:url value='/mcht/order'/>");
	            }else{
	            	bootbox.alert("로그인 정보가 올바르지 않거나 수기결제가 허용되지 않은 터미널입니다.");
	            }
	        },
	        error: function () {
	        	bootbox.alert("잠시후 다시 시도해주세요.");
	        }
	    });

	}
</script>
	</head>
	<body>
		<div class="form_wrap">
			<div class="login">
				<h1> <img src="/img/logo_top.png" /></h1>
				<div class="form">
					<h2>LOGIN</h2>
					<form accept-charset="UTF-8" role="form" autocomplete="off" id="form1" onsubmit="return false;">
						<input type="hidden" name="autoLogin" id="autoLogin"/>
						<!-- <div class="form-group">
	                <input name="mchtId" value='' id="mchtId" type="text" class="input-login" />
	            </div>  -->
						<div class="form-group">
							<label>터미널아이디</label>
							<input name="tmnId" value='' id="tmnId" type="text" class="input-login" placeholder="터미널아이디" tabindex="1"/>
						</div>
						<div class="form-group">
							<label>일련번호</label>
							<input name="serial" id="serial" value='' type="password" class="input-login" placeholder="일련번호" tabindex="2"/>
						</div>
						<div class="form-group"> 
			            	<label for="rememberLogin">자동로그인</label> 
			            	<input type="checkbox" id="rememberLogin"/>
							<input type="submit" class="btn  btn-danger btn-login-submit btn-block btn-lg" value="Login" onclick="javascript:fn_login();"/>
						</div>
					</form>
				</div>
			</div>
		</div>
	</body>
</html>