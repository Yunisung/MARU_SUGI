<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="modal-content">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
		<h4 class="modal-title">
			<small class="mapAddress"></small>
		</h4>
	</div>
	<div class="modal-body">
		<div id="printInfo">
			<div class="row">
				<div class="col-md-12">
					<div class="portlet-body">
						<div class="row map_info_row">
							<div class="col-sm-6">
								<label class="info-label-title col-sm-4 map_desc_1">사업자 번호 </label>
								<label class="info-label-data col-sm-8 map_mcht_id"></label>
							</div>
							<div class="col-sm-6">
								<label class="info-label-title col-sm-4 map_desc_2">가맹점명 </label>
								<label class="info-label-data col-sm-8 map_mcht_nm"></label>
							</div>
						</div>
						<div class="row map_info_row">
							<div class="col-sm-6">
								<label class="info-label-title col-sm-4 map_desc_3">대표 연락처</label>
								<label class="info-label-data col-sm-8 map_mcht_phone"></label>
							</div>
							<div class="col-sm-6">
								<label class="info-label-title col-sm-4 map_desc_4">대표자 이름</label>
								<label class="info-label-data col-sm-8 map_mcht_ceonm"></label>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<div id="map" style="width: 100%; height: 300px; margin-top: 10px; border: 1px gray solid; display: none"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="print btn gray" onclick="printDiv('printInfo')">
				<i class="fa fa-print"></i>&nbsp;print&nbsp;
			</button>
			<button type="button" class="btn default" data-dismiss="modal">Close</button>
		</div>
	</div>
</div>

<!-- /.modal-content -->
<!-- /.modal-dialog -->