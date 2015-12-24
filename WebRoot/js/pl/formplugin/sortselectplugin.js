/*
 *	SortSelectPlugin 1.1
 *	-----------------------------------
 *	排序选择插件
 *	-----------------------------------
 *
 *  @author		Lin ShaoZhong
 *  for iTASK 7.0
 *  Copyright (c) 2011 iTASK Team
 *
 *  modified	2011-09-28 
 */
 
$.css_import_style('.selected_li{height:30px;line-height:30px;}' +
		'.unSelected_li{height:30px;line-height:30px;}' +
		'.sort_bd{border:1px solid #B5B8C9;}' +
        '.sort_bg_mouseover{background-color:#EBF3FE;}' +
        '.sort_bg_title{background-color:#F2F2F2;}' +
		'.sort_list LI P{margin:0px 0px;}' +
		'.sort_link{PADDING-BOTTOM: 3px; PADDING-LEFT: 10px; PADDING-RIGHT: 0px; DISPLAY: block; HEIGHT: 18px; TEXT-DECORATION: none !important; PADDING-TOP: 6px}' +
		'.sort_label{margin-left:8px;}' +
		'.overdiv{filter:alpha(opacity=70,Style=0);opacity: 0.7;-khtml-opacity: 0.7;}' +
		'.sort_span{display:inline-block;cursor:default;text-align:left;}' +
		'.sort_span DIV {float:left;}' +
		'.sort_span .ic {font-size:30px;padding-left:4px;}' +
		'.sort_span .tt {padding-left:4px;line-height:30px;}');

PL('SortSelectPlugin', {
	/* 构造函数 */
	Const : function(o, fullScreen, cols){
		this.object = o;
		this.editable = true;
		this.fullScreen = fullScreen;
		this.cols = cols;
		o.vm().ext('sortPlugin',this);
		this.id = PL.SortSelectPlugin.g_id();
		PL.SortSelectPlugin.all[this.id] = this;
	},
	
	
	/*   辅助(静态方法)  */
	Helper : { 
		
		all : {},
		idp : 'sortPlugin-',
		idc : 0,
		g_id : function() { return this.idp + this.idc ++; }
	},
	
	
	/*  原型  */
	Prototype : {
		isEditable : false,
		
		_disposed : false,
		
		vis : false,
		
		render : function(){ 
			var value = '';
			var checkedOptionsObject = this.object.x('ext/s');
			var checkedOptions = null;
			if(checkedOptionsObject){
				value = checkedOptionsObject.getAttribute('v');
				checkedOptions = value.split(',');
			}
			var options = js.map(this.object.xs('ext/o'));
			this.selectedOptions = [];
			this.unselectedOptions = [];
			if(options && options.length > 0){
				if(checkedOptions && checkedOptions.length > 0){
					for(var i = 0; i < checkedOptions.length; i++){
						var checkedOption = checkedOptions[i];
						for(var j = 0; j < options.length; j++){
							var option = options[j];
							if(checkedOption == option.getAttribute('v')){
								this.selectedOptions[this.selectedOptions.length] = [option.getAttribute('v'), option.text, option.getAttribute('ic')];
								options.splice(j, 1);
								break;
							}
						}
					}
				}
				var unSelected = '';
				var unCheckedOptionsObject = this.object.x('ext/u');
				if(unCheckedOptionsObject){
					unSelected = unCheckedOptionsObject.getAttribute('v');
				}
				for(var i = 0; i < options.length; i++){
					var option = options[i];
					if(unSelected && unSelected.indexOf(option.getAttribute("v")) >= 0){
						this.unselectedOptions[this.unselectedOptions.length] = [option.getAttribute("v"), option.text, option.getAttribute('ic')];
					}
				}
			}
			var mxHeight = this.object.mx().ht();
			this.selectedCount = this.selectedOptions.length;
			var unselectedCount = this.unselectedOptions.length;
			var count = this.selectedCount + unselectedCount;
			var pluginHeight = count * 30 + 20;
			var height = mxHeight < pluginHeight ? pluginHeight : mxHeight;
			if(this.fullScreen){
				height = mxHeight - 4;
			}
			var cols = this.getCols();
			var rootDiv = new Fn.DivSet(cols, this.object.wd(), height, 'onselectstart=return false;', 'font-size:12px;'),
			    leftSplitDiv = rootDiv.add(),
			    leftDiv = rootDiv.addset('rows=40,*', 'class="sort_bd bg-white"', '', 2, 2),
			    leftTitleDivSet = leftDiv.addset('cols=32,*,32', 'class="ttdx sort_bd bd-onlybottom sort_bg_title"', null, 0, 1),
			    leftTitleDivLeft = leftTitleDivSet.add(),
			    leftTitleDiv = leftTitleDivSet.add(),
			    leftTitleDivRight = leftTitleDivSet.add(),
			    leftContentDiv = leftDiv.addset('rows=*', '', 'padding:0px;', 0, 0),
			    selectedDiv = leftContentDiv.add('class="sort_list"', 'white-space:nowrap;overflow-y:scroll;'),
			    middleBarDiv = rootDiv.add(),
			    rightDiv = rootDiv.addset('rows=40,*', 'class="sort_bd bg-white"', '', 2, 2),
			    rightTitleDivSet = rightDiv.addset('cols=32,*,32', 'class="ttdx sort_bd bd-onlybottom sort_bg_title"', null, 0, 1),
			    rightTitleDivLeft = rightTitleDivSet.add(),
			    rightTitleDiv = rightTitleDivSet.add(),
			    rightTitleDivRight = rightTitleDivSet.add(),	
			    rightContentDiv = rightDiv.addset('rows=*', '', 'padding:0px;', 0, 0),		    
			    unselectedDiv = rightContentDiv.add('', 'white-space:nowrap;overflow-y:scroll;')
			    rightSplitDiv = rootDiv.add();
			this.rootDiv = rootDiv;
			this.object.$().innerHTML = '';
			rootDiv.render( this.object.$() );
			leftTitleDiv.$().innerHTML = '<div style="padding:8px 0;text-align:center;">已选项</div>';
			rightTitleDiv.$().innerHTML = '<div style="padding:8px 0;text-align:center;">可选项</div>';
			leftTitleDivRight.$().innerHTML = '<div style="padding:8px 0;text-align:center;"><a href=\"javascript:;\" style=\"letter-spacing:0;text-decoration:none;\" onclick=\"PL.SortSelectPlugin.all[\'' + this.id + '\'].removeSelected()\" >清空</a></div>';
			rightTitleDivRight.$().innerHTML = '<div style="padding:8px 0;text-align:center;"><a href=\"javascript:;\" style=\"letter-spacing:0;text-decoration:none;\" onclick=\"PL.SortSelectPlugin.all[\'' + this.id + '\'].selectAll()\" >全选</a></div>';
			this.selectedOptionDiv = selectedDiv.$();
			this.unselectedOptionDiv = unselectedDiv.$();
			this.render_selected(selectedDiv);
			this.render_unselected(unselectedDiv);
			$.append(this.object.$(), '<input type="hidden" name="' + this.object.name + '" id="' + this.id + '-value" value="' + value + '">');
			this.isEditable = true;
			this.vis = true;
		},
		
		getCols : function(){
			return this.cols && this.cols.split(',').length == 5 ? 'cols=' + this.cols : 'cols=10%,35%,10%,35%,10%';
		},
		
		resize : function(){
			var w = this.object.wd();
			var mxHeight = this.object.mx().ht();
			this.selectedCount = this.selectedOptions.length;
			var unselectedCount = this.unselectedOptions.length;
			var count = this.selectedCount + unselectedCount;
			var pluginHeight = count * 30 + 20;
			var h = mxHeight < pluginHeight ? pluginHeight : mxHeight;
			if (this.rootDiv){
				this.rootDiv.resize(w, h);
			}
			var liWidth = this.getLiWidth();
			var titleWidth = this.getLiTitleWidth();
			if(this.selectedUL && this.selectedUL.children && this.selectedUL.children.length > 0){
				for(var i = 0; i < this.selectedUL.children.length; i++){
					var item = this.selectedUL.children[i];
					item.style.posWidth = liWidth;
					item.children[0].children[1].style.posWidth = titleWidth;
				}
			}
			if(this.unselectedUL && this.unselectedUL.children && this.unselectedUL.children.length > 0){
				for(var i = 0; i < this.unselectedUL.children.length; i++){
					var item = this.unselectedUL.children[i];
					item.style.posWidth = liWidth;
					item.children[0].children[1].style.posWidth = titleWidth;
				}
			}			
		},
		
		remove : function(){
			if ( this._disposed )
				return;
			this.object.dispose();
			delete PL.SortSelectPlugin.all[ this.id ];
			this._disposed = true;
		},
		
		check : function(){
			
		},
		
		save : function(){
			if(this.selectedUL.children){
				var value = '';
				for(var i = 0; i < this.selectedUL.children.length; i++){
					var option = this.selectedUL.children[i].children[0];
					value += (value ? ',' : '') + option.id.replace(this.id + '-', '');
				}
				document.getElementById(this.id + '-value').value = value;
			}
		},
		
		getLiWidth : function(){
			var cols = this.getCols();
			var ssca = js.s_fr(cols, '=');
			var scalesArr = js.m_scale(this.object.wd(), ssca);
			return scalesArr[1] - 3 - Br.scroll - 2;
		},
		
		getLiTitleWidth : function(){
			var liWidth = this.getLiWidth();
			var liTitleWidth = liWidth - 26 - 12 - 6;
			if(this.editable){
				liTitleWidth -= 30;
			}
			return liTitleWidth;
		},
		
		render_selected : function(selectedDiv){
			var liWidth = this.getLiWidth();
//			var maxWidth = 300;
//			liWidth = liWidth < maxWidth ? liWidth : maxWidth;
			var titleWidth = this.getLiTitleWidth();
			var s = [];
			s.push('<div class="clearfix" onselectstart="return false;" >');
			for(var i = 0; i < this.selectedOptions.length; i++){
				var option = this.selectedOptions[i];
				var icon=option[2];
				if(!icon)icon='img/m/business.gif';
				s.push('<div class="nobr" style="width:' + liWidth + 'px;height:30px;overflow:hidden;float:left;border-bottom:1px dotted #ddd;" onmouseover="PL.SortSelectPlugin.all[\'' + this.id + '\'].mouseover(this); return false;" onmouseout="PL.SortSelectPlugin.all[\'' + this.id + '\'].mouseout(this); return false;">' +
					   '<span id="' + this.id + '-' + option[0] + '" class="selected_li sort_span" style="' + (this.editable ? 'cursor:move;' : '') + '" order="' + i + '"' + (this.editable ? ' onmousedown="PL.SortSelectPlugin.all[\'' + this.id + '\'].mousedown(this, event)"' : '') + '>' +
					   '<div class="ic" style=""><div style="width:30px;height:30px;background: url('+icon+') no-repeat center;"></div></div>' +
					   '<div class="tt" style="width:' + titleWidth + 'px;line-height:30px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">' + option[1] + '</div>' +
					   '</span>' +
					   (this.editable ? '<span class="sort_span" ><div class="tt hand clr-deep" style="display: none;text-align:center;width:30px;" onclick="PL.SortSelectPlugin.all[\'' + this.id + '\'].hiddenClick(this)" onmousedown="PL.SortSelectPlugin.all[\'' + this.id + '\'].hidden_mousedown(this)">移除</div></span>' : '') +
					   '</div>');
			}
			s.push('</div>');
			this.selectedUL = $.append(selectedDiv.$(), s.join(''));
		},
		
		render_unselected : function(unselectedDiv){
			var liWidth = this.getLiWidth();
//			var maxWidth = 300;
//			liWidth = liWidth < maxWidth ? liWidth : maxWidth;			
			var titleWidth = this.getLiTitleWidth();
			var s = [];
			s.push('<div class="clearfix" onselectstart="return false;" >');
			for(var i = 0; i < this.unselectedOptions.length; i++){
				var option = this.unselectedOptions[i];
				var icon=option[2];
				if(!icon)icon='img/m/business.gif';
				s.push('<div class="nobr" style="width:' + liWidth + 'px;height:30px;line-height:30px;overflow:hidden;float:left;border-bottom:1px dotted #ddd;" onmouseover="PL.SortSelectPlugin.all[\'' + this.id + '\'].mouseover(this); return false;" onmouseout="PL.SortSelectPlugin.all[\'' + this.id + '\'].mouseout(this); return false;">' +
					   '<span id="' + this.id + '-' + option[0] + '" class="unSelected_li sort_span" style="' + (this.editable ? 'cursor:move;' : '') + '"' + (this.editable ? ' onmousedown="PL.SortSelectPlugin.all[\'' + this.id + '\'].mousedown(this, event)"' : '') + '>' +
					   '<div class="ic" style=""><div style="width:30px;height:30px;background: url('+icon+') no-repeat center;"></div></div>' +
					   '<div class="tt" style="width:' + titleWidth + 'px;line-height:30px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">' + option[1] + '</div>' +
					   '</span>' +
					   (this.editable ? '<span class="sort_span" ><div class="tt hand clr-deep" style="display: none;text-align:center;width:30px;" onclick="PL.SortSelectPlugin.all[\'' + this.id + '\'].hiddenClick(this)" onmousedown="PL.SortSelectPlugin.all[\'' + this.id + '\'].hidden_mousedown(this)">选择</div></span>' : '') +
					   '</div>');
			}
			s.push('</div>');
			this.unselectedUL = $.append(unselectedDiv.$(), s.join(''));
		},
				
		mouseover : function(o){
			o.className = o.className + ' sort_bg_mouseover';
			if(this.editable){
				o.children[1].children[0].style.display = '';
			}
		},
		
		mouseout : function(o){
			o.className = o.className.replace(' sort_bg_mouseover', '');
			if(this.editable){
				o.children[1].children[0].style.display = 'none';
			}
		},
		
		mousedown : function(o, e){
			this.operationType = null;
			this.oldSortOrder = 'no';
			if(o.className.indexOf('selected_li') >= 0 && o.getAttribute('order')){
				this.oldSortOrder = o.getAttribute('order');
			}
			this.sortOrder = 'no';
			o.parentNode.className = o.parentNode.className.replace(' bg-lowest', '').replace(' bg-gray', '') + ' bg-low';
//			o.onmouseover = '';
//			o.onmouseout = '';
			var id = this.id;
			this.distanceX = e.clientX - $.bcr(o).left;
			$.e_moveup(o, function(e){PL.SortSelectPlugin.all[id].mousemove(o, e)}, function(e){PL.SortSelectPlugin.all[id].mouseup(o, e)});
		},
		
		mousemove : function(o, e){
			if(o.parentNode.children[1].children[0].style.display != 'none'){
				o.parentNode.children[1].children[0].style.display = 'none';
			}
			if(o.parentNode.className.indexOf('bg-gray') < 0){
				o.parentNode.className = o.parentNode.className.replace(' bg-low', '') + ' bg-gray';
			}
			if(!this.overDiv){
				var width = o.parentNode.offsetWidth;
				this.overDiv = $.append(document.body, '<div class="overdiv bg-deep" style="width:' + width + 'px;height:30px;position:absolute;z-index:' + $.g_z() + ';" ></div>');
				this.lineDiv = $.append(document.body, '<div class="" style="background-color:red;width:' + width + 'px;height:2px;left:' + ($.bcr(this.selectedOptionDiv).left) + 'px;position:absolute;z-index:' + $.g_z() + ';" ></div>');
			}
			var positionTop = e.clientY - 15;
			var positionLeft = e.clientX - this.distanceX;
			with(this.overDiv.style){
				posTop = positionTop;
				posLeft = positionLeft;
			}
			
			var mouseX = e.clientX;
			var mouseY = e.clientY;		
			var posPlugin = $.bcr(this.rootDiv.$());	
			var selectedPosArr = $.bcr(this.selectedUL);
			var selectedPosLeft = selectedPosArr.left;
			var selectedPosRight = selectedPosArr.right;
			
			var unselectedPosArr = $.bcr(this.unselectedUL);
			var unselectedPosLeft = unselectedPosArr.left;
			var unselectedPosRight = unselectedPosArr.right;
			
			var selectedOptionTop = $.bcr(this.selectedOptionDiv).top;
			if(mouseX >= selectedPosLeft && mouseX <= selectedPosRight){
				var ch = this.selectedOptionDiv.offsetHeight + selectedOptionTop;
				var oy = e.clientY - ch;
				if (oy > 0) {
					this.selectedOptionDiv.scrollTop = Math.min(this.selectedOptionDiv.scrollTop + oy, this.selectedUL.offsetHeight + 1 - this.selectedOptionDiv.offsetHeight);
				} else {
					if (e.clientY < posPlugin.top) {
						this.selectedOptionDiv.scrollTop = Math.max(this.selectedOptionDiv.scrollTop + e.clientY - selectedOptionTop, 0);
					}
				}	
			}
			this.unselectedOptionDiv.className = "";
			if(mouseY >= posPlugin.top && mouseY <= posPlugin.bottom){
				var linePosY = '';
				if(mouseX >= selectedPosLeft && mouseX <= selectedPosRight + Br.scroll){
					var liWidth = this.getLiWidth();
					if(mouseX >= selectedPosRight && mouseX <= selectedPosRight + Br.scroll){
						if(!this.selectedUL.children || this.selectedUL.children.length == 0){
							linePosY = posPlugin.top + 22;
						} else {
							linePosY = $.bcr(this.selectedUL.children[this.selectedUL.children.length - 1]).bottom - 2;
						}
					} else if(mouseY >= posPlugin.top && mouseY <= posPlugin.top + 22 || !this.selectedUL.children || this.selectedUL.children.length == 0){
						linePosY = posPlugin.top + 22;
					} else {
						for(var i = 0; i < this.selectedUL.children.length; i++){
							var item = this.selectedUL.children[i];
							var pos = $.bcr(item);
							if(mouseY >= pos.top && mouseY <= pos.bottom){
								linePosY = pos.bottom - 2;
								break;
							}
						}
					}
					if(linePosY != '' && linePosY <= posPlugin.bottom){
						this.operationType = "selected";
						this.sortOrder = Math.round((this.selectedOptionDiv.scrollTop + linePosY + 2 - selectedOptionTop) / 30);
						with(this.lineDiv.style){
							posHeight = 2;
							posTop = linePosY;
						}
					} else {
						this.operationType = "selected";
						this.sortOrder = this.selectedUL.children.length;
						with(this.lineDiv.style){
							posHeight = 0;
						}
					}
				} else if(mouseX >= unselectedPosLeft && mouseX <= unselectedPosRight){
					if(o.className.indexOf('selected_li') >= 0){
						this.unselectedOptionDiv.className = "bg-lowest";
					}
					this.operationType = "unselected";
				} else {
					this.operationType = null;
					with(this.lineDiv.style){
						posHeight = 0;
					}
				}
			} else {
				this.operationType = null;
				with(this.lineDiv.style){
					posHeight = 0;
				}
			}
		},
		
		mouseup : function(o, e){
			var id = this.id;
//			o.onmouseover = function(){PL.SortSelectPlugin.all[id].mouseover(o)};
//			o.onmouseout = function(){PL.SortSelectPlugin.all[id].mouseout(o)};
			if(this.overDiv){
				this.overDiv.parentNode.removeChild(this.overDiv);
				this.overDiv = null;
			}
			if(this.lineDiv){
				this.lineDiv.parentNode.removeChild(this.lineDiv);
				this.lineDiv = null;
			}			
			o.parentNode.className = o.parentNode.className.replace(' bg-gray', '').replace(' bg-low', '');
			if(this.operationType){
				if(this.operationType == 'selected'){
					if(o.className.indexOf('selected_li') >= 0){
						if(this.sortOrder != 'no' && this.oldSortOrder != this.sortOrder && this.sortOrder - this.oldSortOrder != 1){
							var insertPos = null;
							if(this.selectedUL.children[this.sortOrder]){
								insertPos = this.selectedUL.children[this.sortOrder];
							}
							o.parentNode.children[1].children[0].innerText = '移除';
							this.selectedUL.insertBefore(o.parentNode, insertPos);
							var begin = this.sortOrder > this.oldSortOrder ? this.oldSortOrder : this.sortOrder;
							var end = this.sortOrder > this.oldSortOrder ? this.sortOrder : this.oldSortOrder;
							end = end >= this.selectedUL.children.length ? this.selectedUL.children.length - 1 : end;
							for(var i = begin; i <= end; i++){
								var item = this.selectedUL.children[i].children[0];
								item.setAttribute('order', i);
							}
						}
					} else {
						if(this.sortOrder != 'no'){
							var insertPos = null;
							if(this.selectedUL.children[this.sortOrder]){
								insertPos = this.selectedUL.children[this.sortOrder];
							}
							o.className = 'selected_li';
							o.parentNode.children[1].children[0].innerText = '移除';
							this.selectedUL.insertBefore(o.parentNode, insertPos);
							for(var i = this.sortOrder; i < this.selectedUL.children.length; i++){
								var item = this.selectedUL.children[i].children[0];
								item.setAttribute('order', i);
							}
						}
					}
				} else {
					if(o.className.indexOf('selected_li') >= 0){
						o.className = 'unSelected_li';
						o.parentNode.children[1].children[0].innerText = '选择';
						this.unselectedUL.appendChild(o.parentNode);
					}
				}
			}
			this.unselectedOptionDiv.className = "";
			this.updateRowsClass();
		}, 
		
		updateRowsClass : function(){
			if(this.selectedUL.children){
				for(var i = 0; i < this.selectedUL.children.length; i++){
					var option = this.selectedUL.children[i];
					option.className = 'nobr';
					option.children[0].className = 'selected_li sort_span';
				}
			}
			if(this.unselectedUL.children){
				for(var i = 0; i < this.unselectedUL.children.length; i++){
					var option = this.unselectedUL.children[i];
					option.className = 'nobr';
					option.children[0].className = 'unSelected_li sort_span';
				}
			}
		},
		
		hiddenClick : function(o){
			var type = o.parentNode.parentNode.children[0].className.indexOf('selected_li') >= 0 ? 'selected' : 'unselected';
			if(type == 'selected'){
				o.innerText = '选择';
				o.style.display = 'none';
				o.parentNode.parentNode.children[0].className = o.parentNode.parentNode.children[0].className.replace('selected_li', 'unSelected_li');
				this.unselectedUL.appendChild(o.parentNode.parentNode);
			} else {
				o.innerText = '移除';
				o.style.display = 'none';
				o.parentNode.parentNode.children[0].className = o.parentNode.parentNode.children[0].className.replace('unSelected_li', 'selected_li');
				o.parentNode.parentNode.children[0].setAttribute('order', this.selectedUL.children.length);
				this.selectedUL.appendChild(o.parentNode.parentNode);
			}
			this.updateRowsClass();
		},
		
		hidden_mousedown : function(o){
			$.e_stop( event );
			var id = this.id;
			$.e_moveup(o, function(){}, function(){PL.SortSelectPlugin.all[id].mouseout(o.parentNode.parentNode);});
		},
		
		removeSelected : function(){
			if(this.selectedUL && this.selectedUL.children && this.selectedUL.children.length > 0){
				while(this.selectedUL.children && this.selectedUL.children.length > 0){
					var option = this.selectedUL.children[0].children[1].children[0];
					option.onclick();
				}
			    this.updateRowsClass();			
			}
		},
		
		selectAll : function(){
			if(this.unselectedUL && this.unselectedUL.children && this.unselectedUL.children.length > 0){
				while(this.unselectedUL.children && this.unselectedUL.children.length > 0){
					var option = this.unselectedUL.children[0].children[1].children[0];
					option.onclick();
				}
			}
			this.updateRowsClass();	
		}				
	}
}
)