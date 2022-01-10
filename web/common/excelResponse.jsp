<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%
	String fileName = "PGMS"/* +CommonUtil.getCurrentDate("yyyyMMddHHmmss") */;
	
	String agent=request.getHeader("USER-AGENT");
		String requestUrl = request.getRequestURL().toString().toLowerCase();
		response.reset();
		if (agent.indexOf("MSIE 6.0") > -1 || agent.indexOf("MSIE 5.5") > -1) {
			response.setHeader("Content-type", "application/octet-stream;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls");
			response.setHeader("Content-Transfer-ENCODING", "binary");
			if(!requestUrl.startsWith("https")){
				response.setHeader("Pragma", "no-cache");
			}
			response.setHeader("Cache-Control", "private");
			response.setHeader("Expires", "0");
		} else {
			//response.setHeader("Content-type", "file/unknown");
			response.setHeader("Content-type", "application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls");
			response.setHeader("Content-Description", "Servlet Generated Data");
			if(!requestUrl.startsWith("https")){
				response.setHeader("Pragma", "no-cache");
			}
			response.setHeader("Cache-Control", "private");
			response.setHeader("Expires", "0");
		}
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<meta http-equiv=Content-Type content='text/html; charset=utf-8'>
<html>
	<title>PGMATE - TRANSACTION LIST</title>
	<style type="text/css">
	<!--
		table { font-family: "Seoul", "Arial", "Helvetica"; font-size: 10pt; color: #333333 ;border-collapse:collapse;border:1px gray black;text-align:center;}

	-->
	</style>
<body>
<font size="2">
${message}
</font>
</body>
</html>