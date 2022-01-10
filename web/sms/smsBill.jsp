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
<script src='<c:url value="/js/jquery.min.js"/>'></script>
<script src='<c:url value="/js/bootstrap.js"/>'></script>
<style>
	div.contents{display: inline-block; width:320px;padding:20px; padding-bottom:30px; background-color: #fff; background-image: url(<c:url value='/img/bg_footer.png'/>); background-repeat: repeat-x; background-position: bottom left; box-shadow: 0px 5px 15px #e9e9e9;}
	div.contents h1{margin-bottom: 20px;}
	div.contents h3{margin:0 auto; font-size:20px; border-bottom:1px dashed #e9e9e9;padding-bottom: 40px; }
	div.row div.text-left{color:#999; line-height: 30px;}
	div.row div.text-right{color:#1b1b1b; line-height: 30px;}
</style>
</head>
<body>
	<div style="display: table; position: absolute; top: 0; left: 0; width: 100%; height: 100%;">
		<div style="display: table-cell; vertical-align: middle; text-align: center;">
			<div class="contents">
			<h1><img src=<c:url value="/img/check.gif"/> width="40px" /></h1>
				<h3>${bill.status }완료</h3>
				<div class="row">
					<div class="col-xs-5 text-left">승인금액</div>
					<div class="col-xs-7 text-right"><fmt:formatNumber value="${bill.amount }" groupingUsed="true"/></div>
				</div>
				<div class="row">
					<div class="col-xs-5 text-left">거래번호</div>
					<div class="col-xs-7 text-right">${bill.trxId }</div>
				</div>
				<div class="row">
					<div class="col-xs-5 text-left">거래일자</div>
					<div class="col-xs-7 text-right">
					<fmt:parseDate value="${bill.regDay }" var="dateFmt" pattern="yyyyMMdd"/>
					<fmt:formatDate value="${dateFmt }" pattern="yyyy-MM-dd"/>
					<fmt:parseDate value="${bill.regTime }" var="timeFmt" pattern="HHmmss"/>
					<fmt:formatDate value="${timeFmt }" pattern="HH:mm:ss"/>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-5 text-left">카드번호</div>
					<div class="col-xs-7 text-right">${bill.bin }******${bill.last4 }</div>
				</div>
				<div class="row">
					<div class="col-xs-5 text-left">승인번호</div>
					<div class="col-xs-7 text-right">${bill.authCd }</div>
				</div>
				<div class="row">
					<div class="col-xs-5 text-left">카드종류</div>
					<div class="col-xs-7 text-right">${bill.issuer }카드</div>
				</div>
			</div>
		</div>
	</div>	
</body>
</html>