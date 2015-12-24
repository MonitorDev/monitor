$.css_import_link( 'm/notice/notice.css' );

var notice = {
	reloadIndex : function(target) {
		if (!target) {
			target = "/mainSource";
		}
		var vm = VM(target);
		if (vm) {
			var beginTime = vm.fv('beginTime');
			var endTime = vm.fv('endTime');
			var cp = vm.find('f_page').attr('currentPage');;
			vm.reload('vm:|notice.sp?act=index&beginTime=' + beginTime + '&endTime=' + endTime + '&cp=' + cp);
		}
	},
	reloadManageIndex : function(target) {
		if (!target) {
			target = "/mainSource";
		}
		var vm = VM(target);
		if (vm) {
			var beginTime = vm.fv('beginTime');
			var endTime = vm.fv('endTime');
			var noticeStatus = vm.fv('noticeStatus');
			var cp = vm.find('f_page').attr('currentPage');
			vm.reload('vm:|notice.sp?act=manageIndex&beginTime=' + beginTime + '&endTime=' + endTime + '&noticeStatus=' + noticeStatus + '&cp=' + cp);
		}
	}
};