<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="modal-content">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
		<h4 class="modal-title">사업자(주민)번호</h4>
	</div>
	<div class="modal-body">
		<form role="form" id="modalForm">
			<div class="form-body row">
				<div class="form-group col-sm-12">
					<div class="col-md-12">
						<div class="input-group input-group-sm">
							<input type="text" class="form-control input-sm numberOnly" maxlength="14" name="identity" placeholder="" value="">
							<div class="input-group-btn">
								<button type="button" class="btn green dropdown-toggle" data-toggle="dropdown">
									<span>사업자번호</span>
									<i class="fa fa-angle-down"></i>
								</button>
								<ul class="dropdown-menu pull-right" data-target='idType'>
									<li><a href="javascript:;">사업자번호</a></li>
									<li><a href="javascript:;">주민번호</a></li>
								</ul>
							</div>
							
							<input type="hidden" class="idType" name="idType" value="사업자번호" />
							<input type="hidden" class="change-flag" name="change-flag" value="false" />
							<!-- /btn-group -->
						</div>
						<!-- /input-group -->
					</div>
				</div>
			</div>
		</form>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-sm gray" data-dismiss="modal"><i class="fa fa-close"></i> 취소</button>
		<button type="button" class="btn btn-sm blue-dark change-identity">
			<i class="fa fa-pencil"></i> 입력
		</button>
	</div>
</div>

<script>
$('#pgmate-modal').on('click', '.dropdown-menu>li>a', function() {
	var target = $(this).closest('ul').data('target');
	var buttonLabel = $(this).closest('.input-group-btn').find('.dropdown-toggle>span');
	var targetTxt = $(this).text();
	var inp = $('#modalForm').find('input[name="'+target+'"]');
	inp.val(targetTxt);
	buttonLabel.text($(this).text());
});

$('#pgmate-modal').on('click', '.change-identity', function() {
	var length = $('#modalForm input[name="identity"]').val().length;
	var idType = $('#modalForm input[name="idType"]').val();
	
	if((idType == '사업자번호' && length == 10) || (idType == '주민번호' && length == 13)) {
		bootbox.confirm("수정내역을 반영 하시겠습니까?", function(result) {
			if (result) {
				$('#modalForm input[name="change-flag"]').val("true");
				$('#pgmate-modal').modal('hide');
			}
		});
	} else {
		bootbox.alert("올바른 값을 입력해야 합니다.");
	}
});
</script>
<!-- /.modal-content -->
<!-- /.modal-dialog -->