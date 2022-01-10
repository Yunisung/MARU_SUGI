<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<div class="form-group col-sm-12 pg-form-group-12" id="gradeSelector">
	<label class="control-label col-md-2 selectGrade-label req-label">소속 선택</label>
	<div class="selecter-wrapper col-md-2 col-xs-12">
		<select name="selectedTargetGrade" class="selectpicker selecterGrade selectedTargetGrade col-xs-12" data-reg="false">
		</select>
	</div>
	<div class="selecter-wrapper col-md-2 col-xs-12">
		<select name="childGrade" class="selectpicker selecterGrade depth1 col-xs-12" data-reg="false">
			<c:forEach var="entryMap" items="${CP_SESSION.childList}">
				<option value="${entryMap['id']}">${entryMap['name']}</option>
			</c:forEach>
		</select>
	</div>
	<div class="selecter-wrapper col-md-2 col-xs-12 hide">
		<select class="selectpicker selecterGrade depth2 col-xs-12" name="depth2" data-reg="false"></select>
	</div>
	<div class="selecter-wrapper col-md-2 col-xs-12 hide">
		<select class="selectpicker selecterGrade depth3 col-xs-12" name="depth3" data-reg="false"></select>
	</div>
	<div class="selecter-wrapper col-md-2 col-xs-12 hide">
		<select class="selectpicker selecterGrade depth4 col-xs-12" name="depth4" data-reg="false"></select>
	</div>
	<div class="col-sm-2 col-xs-12">
		<button type="button" class="btn green btn-sm confirm-btn disabled" data-reg="false">선택</button>
	</div>
</div>
</body>

</html>