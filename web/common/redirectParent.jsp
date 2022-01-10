<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  	<script>
		window.onload = function() {
		  <c:if test="${!empty message}">alert('${message}');</c:if>
		  <c:if test="${!empty redirectURL}">
		 	window.top.location.href = "${redirectURL}";
		  </c:if>
		  <c:if test="${empty redirectURL}">
		  	var domain = window.location.href.split('/');
	 		window.top.location.href = domain[0] + '//' + domain[1] + domain[2];
		  	//history.back();
		  	//history.reload();
		  </c:if>
		}
	</script>
</html>
