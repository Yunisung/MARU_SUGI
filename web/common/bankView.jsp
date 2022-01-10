<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
			<h4 class="modal-title">은행선택 <small class="error"></small></h4>
		</div>
		<div class="modal-body">
			
			<div class="table-scrollable">
			<table class="table table-striped table-bordered table-hover flip-content" id="sortTable">
				<!-- 작게 table-condensed -->
				<thead>
					<tr>
						<th>No</th>
						<th>은행코드</th>
						<th>은행</th>
						<th>선택</th>
						<th>No</th>
						<th>은행코드</th>
						<th>은행</th>
						<th>선택</th>
						
					</tr>
				</thead>
				<tbody id="list">
					
					<c:forEach var="entry" items="${bankCd}" varStatus="status">
						
						<tr>
							<td>${status.count}</td>
							<td>${entry.code}</td>
							<td>${entry.codenm}</td>
							<td><button type="button" class="btn blue btn-sm update">선택</button></td>
							<c:set var="status.count" scope="page" value="${status.count + 1}" />
							<td>${status.count}</td>
							<td>${entry.code}</td>
							<td>${entry.codenm}</td>
							<td><button type="button" class="btn blue btn-sm update">선택</button></td>
						</tr>
						
					</c:forEach>

				</tbody>

			</table>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn default" data-dismiss="modal">Close</button>
		</div>
	</div>
	
<!-- /.modal-dialog -->