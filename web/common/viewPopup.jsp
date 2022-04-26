<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<title>CREDITOP</title>
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<meta content="width=device-width, initial-scale=1" name="viewport"/>
	<meta content="PG" name="description"/>
	<meta content=MTOUCH name="author"/>
	<!-- <meta name="MobileOptimized" content="640"/> -->
	<!-- BEGIN GLOBAL MANDATORY STYLES -->
	<link href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700&subset=all" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/global/plugins/bootstrap-toastr/toastr.css" rel="stylesheet"/>
	<!-- END GLOBAL MANDATORY STYLES -->
	<!-- BEGIN THEME GLOBAL STYLES -->
	<link href="/assets/global/css/components-rounded.min.css" rel="stylesheet" id="style_components" type="text/css" />
	<link href="/assets/global/css/plugins.min.css" rel="stylesheet" type="text/css" />
	<!-- END THEME GLOBAL STYLES -->
	
	<!-- BEGIN THEME LAYOUT STYLES -->
	<link href="/assets/layouts/layout3/css/layout.min.css" rel="stylesheet" type="text/css" />
	<link href="/assets/layouts/layout3/css/themes/blue-hoki.min.css" rel="stylesheet" type="text/css" id="style_color" />
	<link href="/assets/global/css/pgmate.css" rel="stylesheet" type="text/css" />
	<!-- END THEME LAYOUT STYLES -->
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
	<link rel="icon" href="favicon.ico" type="image/x-icon">
	
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-container-bg-solid page-header-menu-fixed">
	<div class="page-wrapper">
		<div class="page-wrapper-row full-height">
			<div class="page-wrapper-middle">
				<!-- BEGIN CONTAINER -->
				<div class="page-container">
					<div class="page-content-wrapper">
	
						<div class="page-content">
							<!-- END PAGE CONTENT-->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS-->
<!--[if lt IE 9]>
		<script src="/assets/global/plugins/respond.min.js"></script>
		<script src="/assets/global/plugins/excanvas.min.js"></script> 
		<script src="/assets/global/plugins/ie8.fix.min.js"></script> 
		<![endif]-->
<!-- BEGIN CORE PLUGINS -->
<script src="/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/js.cookie.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-toastr/toastr.js" type="text/javascript"></script>

<!-- 데이터 피커 -->
<script src="/assets/global/plugins/moment.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.ko.min.js" type="text/javascript"></script>

<!-- 폼 체크 -->
<script src="/assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/jquery-validation/js/localization/messages_ko.min.js" type="text/javascript"></script>
<script src="/assets/global/plugins/jquery-validation/js/jquery-validate.bootstrap-tooltip.min.js" type="text/javascript"></script>

<script src="/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>

<script src="/assets/global/scripts/pg-common.js?v=9" type="text/javascript"></script>
<script src="/assets/global/scripts/app.js?v=9" type="text/javascript"></script>
<script src="/assets/global/scripts/pgmate.js?v=9" type="text/javascript"></script>
<script src="/assets/global/scripts/pg-search.js?v=9" type="text/javascript"></script>
<script src="/assets/global/scripts/pg-form.js?v=9" type="text/javascript"></script>

<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="/assets/pages/scripts/components-bootstrap-select.min.js" type="text/javascript"></script>
<script src="/assets/layouts/layout3/scripts/layout.min.js" type="text/javascript"></script>
<script>

	jQuery(document).ready(function(){
		
		
		var content = window.opener.$('.page-content-inner').html();
		content = content.replace('container','container-fluid');
		$('.page-content').html(content);
	  
		
	});
</script>
</body>
<!-- END BODY -->	

</html>
