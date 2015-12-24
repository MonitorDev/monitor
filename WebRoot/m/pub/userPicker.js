$.css_import_link('m/pub/userPicker.css');
var userPicker = {
	
	
	
	tree:{
		searchClick : function(o,onlyUseable,uid){
			var p = VM(o).parent;
			 VM(p).cmd({tagName:'ajax',src:'orgAndUserPicker.sp?act=userSearch&onlyUseable='+onlyUseable+'&userId='+uid+'&path='+VM(p).path});
		}
	},
	feedbkUserPicker : {
		init : function(o){
			var root = VM(o).find('root').$();
			var selectedResult = Q('.selected_result', root)[0];
			if(selectedResult && selectedResult.children.length > 0){
				for(var i = selectedResult.children.length - 1; i >= 0; i--){
					var item = selectedResult.children[i];
					var userId = item.id.replace('user_', '');
					var selectItemArr = Q('.item_' + userId, root);
					for(var j = 0; j < selectItemArr.length; j++){
//						selectItemArr[j].children[2].style.display = '';
						selectItemArr[j].setAttribute('selected', '1');
						$.class_add(selectItemArr[j], 'bg-mid');
						$.class_add(selectItemArr[j], 'user_selected');
//						$.class_add(selectItemArr[j], 'user_selected');
//						$.class_add(selectItemArr[j], 'bg-gray');
//						selectItemArr.find('.vi-bx').addClass('z-checked');
					}
				}
//				$.empty(selectedResult);
//				var selectResultRemark = Q('.selectResultRemark', root)[0];
//				selectResultRemark.innerText = '已选择' + selectedResult.children.length + '人';
			}
			var vm = VM(o);
			var boxGrid = vm.ext('boxGrid');
			if(boxGrid){
//				setTimeout(function(){ userPicker.feedbkUserPicker.batchdo(o, vm.ext('boxChecked'));}, 300);
				this.batchdo(o, vm.ext('boxChecked'));
				this.unlock(o);
			}
		},
		search:{
			 timer : null,
			 searchSession : function(type,cascade,roleIds,notNeedPersonRole){
			 	if ( this.timer ) {
        		clearTimeout( this.timer );
        	}
        	this.timer = setTimeout( function() { 
        		var vm = VM('/user_picker');
        		var orgId = vm.ext('orgId');
        		if(!orgId){
        			orgId=vm.find('orgTree').getSelected('pk');
        			};
        		var isPerson=vm.ext('isPerson');
				var value = vm.ext('searchTitle');
				if(value){
					vm.cmd('updateOrgUserGrid',orgId,type, cascade,value,roleIds,'1',isPerson,notNeedPersonRole);
				}
				else{
					vm.find('orgTree').click(orgId);
				};
        		
        	}, 1000);
			}	 
		},
		
		
		parser : function(xml, fld, colBegin){
			var userId = xml.getAttribute('userId' + colBegin);
			var userIcon = xml.getAttribute('userIcon' + (colBegin + 1));
			var userName = xml.getAttribute('userName' + (colBegin + 2));
			if(userId){
				return '<div id="item_' + userId + '" style="width:95px;padding:5px;line-height:10px;" title="' + userName + '" class=\"user_unselect hand userPickerItem item_' + userId + '\"' +
				'onclick="userPicker.feedbkUserPicker.multipleClick(this, \'' + userId + '\', \'' + js.url_esc(userName) + '\')" ' +
				'onmouseover="userPicker.feedbkUserPicker.over(this)" onmouseout="userPicker.feedbkUserPicker.out(this)">'+
				'<div class="left"><img src="' + userIcon + '" alt="' + userName + '" width="30px" height="30px" ></div>'+
				'<div class="nobr fix" style="text-align:left;padding-left:6px;line-height:150%;">' + js.s_byte(js.url_unesc(userName), 12, '…') + '</div><div style=\"clear:both;\"></div>';
//				return  '<div id="item_' + userId + '" style="text-align:center;width:82px;"  class=\"userPickerItem item_' + userId + '\"' +
//					'onclick="userPicker.feedbkUserPicker.multipleClick(this, \'' + userId + '\', \'' + js.url_esc(userName) + '\')" ' +
//					'onmouseover="userPicker.feedbkUserPicker.over(this)" onmouseout="userPicker.feedbkUserPicker.out(this)">'+
//					'<div style="padding-top:10px;height:50px;"><img src="'+userIcon+'" alt="'+userName+'" title="'+userName+'"></div>'+
//					'<p >' + js.s_byte(js.url_unesc(userName), 12, '…') + '</p><div style="left: 50px; top: -6px;" class="vi-bx"></div></div>';
			}
			return '';
		},
		
		over : function(o){
			if(!o.getAttribute('selected')){
				$.class_add(o, 'bg-mid');
				$.class_add(o, 'user_selected');
			}
		}, 
		
		out : function(o){
			if(!o.contains(event.toElement) && !o.getAttribute('selected')){
				$.class_del(o, 'bg-mid');
				$.class_del(o, 'user_selected');
			}
		},
		
		click : function(o, userId, userName){
			
			var root = VM(o).find('root').$();
			var feedbkUserCheckArr = Q('.item_' + userId, root);
			var check = null;
			try{
			check = feedbkUserCheckArr[0].getAttribute('selected') == '1' ? false : true;
			}catch(e){}
			for(var i = 0; i < feedbkUserCheckArr.length; i++){
				if(check){
					feedbkUserCheckArr[i].setAttribute('selected', '1');
					$.class_add(feedbkUserCheckArr[i], 'bg-mid');
					$.class_add(feedbkUserCheckArr[i], 'user_selected');
					feedbkUserCheckArr.find('.vi-bx').addClass('z-checked');
				} else {
					feedbkUserCheckArr[i].setAttribute('selected', '');
					$.class_del(feedbkUserCheckArr[i], 'bg-mid');
					$.class_del(feedbkUserCheckArr[i], 'user_selected');
					feedbkUserCheckArr.find('.vi-bx').removeClass('z-checked');
				}
			}
			var selectedResult = Q('.selected_result', root)[0];
			var selectResultRemark = Q('.selectResultRemark', root)[0];
			if(check){
				selectedResult.innerHTML += '<div class="selected_item itask_bg_gray nobr fix" id="user_' + userId + '" style="width:41px;overflow:hidden;" title="' + js.url_unesc(userName) + '" >' + js.url_unesc(userName) + '<a href=javascript:void(0) class="selected_item_close" onclick="userPicker.feedbkUserPicker.click(this, \'' + userId + '\', \'' + userName + '\')"></a></div>';
			} else {
				var selectedUser = Q('div#user_' + userId, root)[0];
				selectedResult.removeChild(selectedUser);
			}
			selectResultRemark.innerHTML = '<span name="transferRemark" style="color:#a8a8a8;">已选择<span style="color:red;"> ' + selectedResult.children.length + ' </span>人</span>';
		//	selectResultRemark.innerText = '你还可以选择' + selectedResult.children.length + '项';
		},
		
		multipleClick : function(o,userId,userName){
			var multipleNumber = VM('/user_picker').ext('multiple');
			if(multipleNumber == 0){
			userPicker.feedbkUserPicker.click(o,userId,userName);
			return;
			}
			
		
			
			var root = VM(o).find('root').$();
			var selectedResult = Q('.selected_result', root)[0];
			var feedbkUserCheckArr = Q('.item_' + userId, root);
			var check = null;
			try{
			check = feedbkUserCheckArr[0].getAttribute('selected') == '1' ? false : true;
			if(multipleNumber == 1){
				userPicker.feedbkUserPicker.clear(o);
			}
			}catch(e){}
			for(var i = 0; i < feedbkUserCheckArr.length; i++){
				if(check ){
					if(selectedResult.children.length+1 <= multipleNumber){
						feedbkUserCheckArr[i].setAttribute('selected', '1');
						$.class_add(feedbkUserCheckArr[i], 'bg-mid');
						$.class_add(feedbkUserCheckArr[i], 'user_selected');
						feedbkUserCheckArr.find('.vi-bx').addClass('z-checked');
					}
				} else {
					feedbkUserCheckArr[i].setAttribute('selected', '');
					$.class_del(feedbkUserCheckArr[i], 'bg-mid');
					$.class_del(feedbkUserCheckArr[i], 'user_selected');
					feedbkUserCheckArr.find('.vi-bx').removeClass('z-checked');
				}
			}
			
			var selectResultRemark = Q('.selectResultRemark', root)[0];
			if(check){
				if(selectedResult.children.length+1 <= multipleNumber){
					selectedResult.innerHTML += '<div class="selected_item itask_bg_gray nobr fix" id="user_' + userId + '" style="width:41px;overflow:hidden;" title="' + js.url_unesc(userName) + '" >' + js.url_unesc(userName) + '<a href=javascript:void(0) class="selected_item_close" onclick="userPicker.feedbkUserPicker.click(this, \'' + userId + '\', \'' + userName + '\')"></a></div>';
				}else{
					DFish.alert('每次最多选择人' + multipleNumber + '');
				}
			} else {
				var selectedUser = Q('div#user_' + userId, root)[0];
				if (selectedUser) {
					selectedResult.removeChild(selectedUser);
				}
			}
			selectResultRemark.innerHTML = '<span name="transferRemark" style="color:#a8a8a8;">您还可以选择<span style="color:red;"> ' + (multipleNumber-selectedResult.children.length) + ' </span>人</span>';
		},
		
		batchdo : function(o, check){
			var arr = Q(check ? '.user_unselect' : '.user_selected');
			var multipleNumber = VM('/user_picker').ext('multiple');
			var multipleNumber = VM('/user_picker').ext('multiple');
			if(arr && arr.length > 0){
				if(arr.length > 1 && multipleNumber == 1 && check){
					VM(o).fs('selectItem', '');
					DFish.alert('对不起，您只能选择一个人员！');
					return;
				}
				if (check) {
					userPicker.feedbkUserPicker.select(o);
				} else {
					userPicker.feedbkUserPicker.cancel(o);
				}
//				for(var i = 0; i < arr.length; i++){
//					var item = arr[i];
//					if(!check || item.className.indexOf('user_selected') < 0){
//						item.onclick();
//					}
//				}
			}
		},
		
		toggle : function(o){
			var resultVP = VM(o).find('resultVP');
			if(o.innerText == '展开'){
				o.innerText = '折叠';
				resultVP.resize(null, 68);
			} else {
				o.innerText = '展开';
				resultVP.resize(null, 0);
			}
			o.blur();
		},
		
		select : function(o){
			var root = VM(o).find('root').$();
			var relative = Q('.userPickerItem', root);
			if(relative && relative.length > 0){
				var result = '';
				for(var i = relative.length - 1; i >= 0; i--){
					var userId = relative[i].id.replace('item_', '');
					var html = userPicker.feedbkUserPicker.selectClick(o,userId,js.url_esc(Q(relative[i])[0].title));
					if (html) {
						result += html;
					}
				}
				var selectedResult = Q('.selected_result', root)[0];
				if (result) {
					selectedResult.innerHTML += result;
					var selectResultRemark = Q('.selectResultRemark', root)[0];
					selectResultRemark.innerHTML = '<span name="transferRemark" style="color:#a8a8a8;">已选择<span style="color:red;"> ' + selectedResult.children.length + ' </span>人</span>';
				}
			}
		},
		selectClick : function(o, userId, userName){
			var root = VM(o).find('root').$();
			var feedbkUserCheckArr = Q('.item_' + userId, root);
			var check = null;
			try{
			check = feedbkUserCheckArr[0].getAttribute('selected') == '1' ? false : true;
			}catch(e){}
			for(var i = 0; i < feedbkUserCheckArr.length; i++){
				if(check){
					feedbkUserCheckArr[i].setAttribute('selected', '1');
					$.class_add(feedbkUserCheckArr[i], 'bg-mid');
					$.class_add(feedbkUserCheckArr[i], 'user_selected');
//					feedbkUserCheckArr.find('.vi-bx').addClass('z-checked');
				} 
			}
//			var selectedResult = Q('.selected_result', root)[0];
			var selectResultRemark = Q('.selectResultRemark', root)[0];
			if(check){
				return '<div class="selected_item itask_bg_gray nobr fix" id="user_' + userId + '" style="width:41px;overflow:hidden;" title="' + js.url_unesc(userName) + '" >' + js.url_unesc(userName) + '<a href=javascript:void(0) class="selected_item_close" onclick="userPicker.feedbkUserPicker.click(this, \'' + userId + '\', \'' + userName + '\')"></a></div>';
//				selectedResult.innerHTML += '<div class="selected_item itask_bg_gray nobr fix" id="user_' + userId + '" style="width:41px;overflow:hidden;" title="' + js.url_unesc(userName) + '" >' + js.url_unesc(userName) + '<a href=javascript:void(0) class="selected_item_close" onclick="userPicker.feedbkUserPicker.click(this, \'' + userId + '\', \'' + userName + '\')"></a></div>';
			} 
			//selectResultRemark.innerText = '已选择' + selectedResult.children.length + '人';
//			selectResultRemark.innerHTML = '<span name="transferRemark" style="color:#a8a8a8;">已选择<span style="color:red;"> ' + selectedResult.children.length + ' </span>人</span>';
		},
		cancel : function(o){
			var root = VM(o).find('root').$();
			var relative = Q('.userPickerItem', root);
			if(relative && relative.length > 0){
				for(var i = relative.length - 1; i >= 0; i--){
					var userId = relative[i].id.replace('item_', '');
					userPicker.feedbkUserPicker.cancelClick(o,userId,js.url_esc(Q(relative[i])[0].title));
				}
			}
		},
		cancelClick : function(o, userId, userName){
			var root = VM(o).find('root').$();
			var feedbkUserCheckArr = Q('.item_' + userId, root);
			var check = null;
			try{
			check = feedbkUserCheckArr[0].getAttribute('selected') == '1' ? false : true;
			}catch(e){}
			for(var i = 0; i < feedbkUserCheckArr.length; i++){
				if(!check){
					feedbkUserCheckArr[i].setAttribute('selected', '');
					$.class_del(feedbkUserCheckArr[i], 'bg-mid');
					$.class_del(feedbkUserCheckArr[i], 'user_selected');
//					feedbkUserCheckArr.find('.vi-bx').removeClass('z-checked');
				}
			}
			var selectedResult = Q('.selected_result', root)[0];
			var selectResultRemark = Q('.selectResultRemark', root)[0];
			if(!check){
				var selectedUser = Q('div#user_' + userId, root)[0];
				selectedResult.removeChild(selectedUser);
			}
			//selectResultRemark.innerText = '已选择' + selectedResult.children.length + '人';
			selectResultRemark.innerHTML = '<span name="transferRemark" style="color:#a8a8a8;">已选择<span style="color:red;"> ' + selectedResult.children.length + ' </span>人</span>';

		},
		
		
		clear : function(o){
			var root = VM(o).find('root').$();
			var selectedResult = Q('.selected_result', root)[0];
			var relative = Q('.userPickerItem', root);
			var multipleNumber = VM('/user_picker').ext('multiple');
//			if(relative && relative.length > 0){
//				for(var i = relative.length - 1; i >= 0; i--){
//					var userId = relative[i].id.replace('item_', '');
				
					
//					userPicker.feedbkUserPicker.click(o,userId,js.url_esc(Q(relative[i]).find('p').html()));
				
//				}
//			}
			if(selectedResult && selectedResult.children.length > 0){
				for(var i = selectedResult.children.length - 1; i >= 0; i--){
					var item = selectedResult.children[i];
					var userId = item.id.replace('user_', '');
					var selectItemArr = Q('.item_' + userId, root);
					for(var j = 0; j < selectItemArr.length; j++){
//						selectItemArr[j].children[2].style.display = 'none';
						selectItemArr[j].setAttribute('selected', '');
						$.class_del(selectItemArr[j], 'bg-mid');
						$.class_del(selectItemArr[j], 'user_selected');
					//	$.class_del(selectItemArr[j], 'user_selected');
					//	$.class_del(selectItemArr[j], 'bg-gray');
					//	selectItemArr.find('.vi-bx').hide();
						selectItemArr.find('.vi-bx').removeClass('z-checked');
					}
				}
				$.empty(selectedResult);
				var selectResultRemark = Q('.selectResultRemark', root)[0];
				//selectResultRemark.innerText = '已选择' + selectedResult.children.length + '人';
				if(multipleNumber == 0){
					selectResultRemark.innerHTML = '<span name="transferRemark" style="color:#a8a8a8;">已选择<span style="color:red;"> ' + selectedResult.children.length + ' </span>人</span>';
				}else{
					selectResultRemark.innerHTML = '<span name="transferRemark" style="color:#a8a8a8;">您还可以选择<span style="color:red;"> ' + multipleNumber + ' </span>人</span>';
				}
			}
			o.blur();
		},
		
		value : function(o){
			var root = VM(o).find('root').$();
			var selectedResult = Q('.selected_result', root)[0];
			var value = '';
			if(selectedResult && selectedResult.children.length > 0){
				for(var i = 0; i < selectedResult.children.length; i++){
					var item = selectedResult.children[i];
					var userId = item.id.replace('user_', '');
					value += (value ? ',' : '') + userId;
				}
			}
			return value;
		},
		
		json : function(o){
			var root = VM(o).find('root').$();
			var selectedResult = $.query('.selected_item', root);
			var js = [];
			for(var i = 0; i < selectedResult.length; i++){
				var item = selectedResult[ i ];
				js.push( { id : item.id.replace('user_', ''), name : item.innerText || item.textContent } );		
			}
			return js;
		},
		
		lock : function(o, boxGrid, boxChecked){
			VM(o).ext('boxGrid', boxGrid);
			VM(o).ext('boxChecked', boxChecked);
		},
		
		unlock : function(o){
			VM(o).ext('boxGrid', '');
			VM(o).ext('boxChecked', '');
		}
	}
	
}
/*
$.css_import_style('.fq_focus {color:#dd4b39;font-weight:bold;}' +
		           '.fq_bold {font-weight:bold;}' + 
                   '.fq_focus_bd {border:1px solid #dd4b39;}' +
                   '.fq_bg_gray {background-color:#f7f7f7;}' +
                   '.task_tree_del {BACKGROUND: url(img/p/ps-icons.png) no-repeat ;BACKGROUND-POSITION: 0px -143px; width:12px;height:18px;overflow:hidden;margin-top:6px; }' +
                   '.task_exec_user {BACKGROUND: url(img/p/ps-icons.png) no-repeat ;BACKGROUND-POSITION: -176px -144px; width:16px;height:16px;overflow:hidden;}' +
                   '.task_exec_query {BACKGROUND: url(img/p/ps-icons.png) no-repeat ;BACKGROUND-POSITION: 0px -252px; width:16px;height:16px;overflow:hidden;}' +
                   '.task_navigate_icon {BACKGROUND: url(pl/ueditor/themes/default/images/icons.png) no-repeat;BACKGROUND-POSITION: -580px -20px;width:20px;height:20px;overflow:hidden;margin-top:10px;}' +
                   '.task_info_icon {BACKGROUND: url(pl/ueditor/themes/default/images/icons.png) no-repeat;BACKGROUND-POSITION: -560px -0px;width:20px;height:20px;overflow:hidden;margin-top:9px;}' +
                   '.feedbk_list_icon {BACKGROUND: url(pl/ueditor/themes/default/images/icons.png) no-repeat;BACKGROUND-POSITION: -20px -0px;width:20px;height:20px;overflow:hidden;margin-top:9px;}' +
                   '.feedbk_list_focus {background-color:#E66A34;}' +
                   '.task_info {BACKGROUND: url(img/p/icon.png) no-repeat ;BACKGROUND-POSITION: 0px -21px; width:22px;height:28px;overflow:hidden;}' +
		           '.task_person {BACKGROUND: url(img/p/icon.png) no-repeat ;BACKGROUND-POSITION: 0px -95px; width:22px;height:28px;overflow:hidden;}' +
		           '.task_app {BACKGROUND: url(img/p/icon.png) no-repeat ;BACKGROUND-POSITION: -120px -68px; width:22px;height:28px;overflow:hidden;}' +
		           '.task_search {BACKGROUND: url(img/p/icon.png) no-repeat ;BACKGROUND-POSITION: -72px -168px; width:22px;height:28px;overflow:hidden;}' +
		           '.msg_config_icon{BACKGROUND: url(img/p/ico_warn.png) no-repeat ;BACKGROUND-POSITION: 0px -150px; width:16px;height:16px;overflow:hidden;}' +
		           '.feedbk_user_check{BACKGROUND-image: url(img/p/group-create.png);BACKGROUND-POSITION: -1284px 0px;width:23px;height:23px;overflow:hidden;position:absolute;top:-10px;right:-10px;}' +
		           '.selected_result{position:relative;overflow:hidden;display:inline-block;}' +
		           '.selected_item{position:relative;text-indent : 5px;margin : 0px 7px 10px 0px;padding-right : 24px;white-space : nowrap;float : left;height : 24px;line-height:24px;word-break : normal;border-radius : 2px;background-clip : padding-box;}' +
		           '.selected_item_close{BACKGROUND: url(m/pub/img/x16/ulclose.gif) no-repeat center; width:24px;height:24px;overflow:hidden;position:absolute;top:0px;right:0px;}' +
		           '.selected_item_right{BACKGROUND: url(img/p/ulright.gif) no-repeat center; width:24px;height:24px;overflow:hidden;position:absolute;top:0px;right:0px;}' +
		           '.res_more{BACKGROUND: url(img/m/task/res/stream_flagicon.png) no-repeat ;BACKGROUND-POSITION: 0px 0px; width:15px;height:15px;line-height:14px;margin-left:5px;display:inline-block;margin-top:2px;}' +
		           '.res_more_over{BACKGROUND: url(img/m/task/res/stream_flagicon.png) no-repeat ;BACKGROUND-POSITION: -15px 0px; width:15px;height:15px;line-height:14px;margin-left:5px;display:inline-block;margin-top:2px;}' +
		           '.res_prev{BACKGROUND: url(img/m/task/res/prev_next.png) no-repeat ;BACKGROUND-POSITION: 0px -1px; width:17px;height:17px;overflow:hidden;}' +
		           '.res_next{BACKGROUND: url(img/m/task/res/prev_next.png) no-repeat ;BACKGROUND-POSITION: -17px -1px; width:17px;height:17px;overflow:hidden;}' +
		           '.res_search{BACKGROUND: url(img/p/main.png) no-repeat ;BACKGROUND-POSITION: -339px -205px; width:18px;height:18px;overflow:hidden;}' +
		           '.task_search_config {BACKGROUND: url(img/p/icon.png) no-repeat ;BACKGROUND-POSITION: -44px -118px; width:22px;height:28px;overflow:hidden;}' +
		           '.feedbk_oper {BACKGROUND: url(img/p/playd.gif) center no-repeat;width:18px;height:18px;overflow:hidden;border:1px solid #989898;}' +
		           '.feedbk_oper_hidden {display:none;}' +
		           '.user_unselect {BACKGROUND: url(img/p/select.gif) no-repeat left;BACKGROUND-POSITION: -5px -23px;height:24px;line-height:24px;padding-left:24px;overflow:hidden;}' +
		           '.user_selected {BACKGROUND-POSITION: -5px -0px;BACKGROUND-COLOR:#f3f3f4;}' +
		           '.userPickerItem{position:relative;cursor:pointer;}', 'css-userPicker');
		           */
