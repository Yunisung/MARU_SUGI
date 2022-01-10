<%@page contentType="text/plain; charset=utf-8" %>
<%@page import="java.io.*"%>
<%
	response.reset();
	response.setContentType("application/json; charset=utf-8");
	PrintWriter writer = response.getWriter();
	String textResponse = (String)request.getAttribute("message");
	writer.write(textResponse);
	writer.flush();
	writer.close();
%>