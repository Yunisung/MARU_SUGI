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
<title>MTouch</title>
<link rel="stylesheet" href='<c:url value="/css/bootstrap.css"/>'>
<script src=<c:url value="/js/jquery.min.js"/>></script>
<script src='<c:url value="/js/bootstrap.js"/>'></script>
<script src='<c:url value="/js/bootbox.min.js"/>'></script>
<style>
	.input-login {
		width: 100%;
		border:0;
		outline:none;
		line-height: 30px;
		border-bottom: 1px solid #828282;
		padding-bottom: 5px;
		margin-bottom: 10px;
	}
	.input-login:focus{
		border-bottom: 1px solid #3787E8;
	}
</style>
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
    <div class="container">
      <div class="row">
        <div class="text-center" style="margin: 50px 0 50px 0;">
          <img src="/img/logo_top.png" style="margin-top: 5px; margin-left:10px; height: 25px;">
        </div>
        <div class="col-md-4 col-md-offset-4">
          <form accept-charset="UTF-8" role="form" autocomplete="off" id="form1" onsubmit="return false;">
          	   <input type="hidden" name="autoLogin" id="autoLogin"/>
	           <!-- <div class="form-group">
	                <input name="mchtId" value='' id="mchtId" type="text" class="input-login" />
	            </div>  --> 
	            <div class="form-group">
	                <input name="tmnId" value='' id="tmnId" type="text" class="input-login" placeholder="터미널아이디" tabindex="1"/>
	            </div>
	            <div class="form-group">
	                <input name="serial" id="serial" value='' type="password" class="input-login" placeholder="일련번호" tabindex="2"/>
	            </div>
	            <div class="form-group">
	            	<label for="rememberLogin">자동로그인</label>
	            	<input type="checkbox" id="rememberLogin"/>
	                <input type="submit" class="btn btn-default btn-login-submit btn-block m-t-md" value="Login" onclick="javascript:fn_login();"/>
	            </div>
	        </form>
        </div>
      </div>
    </div>
</body>
</html>