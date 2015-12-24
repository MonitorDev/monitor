

var formDeclare = {

	checkTotalWayBill : function(o){
		var vm = VM(o);
		vm.button('saveDeclare').disable();
		if(!vm.fv('totalWayBill')){
			DFish.alert('尚未保存清单信息！');
			vm.button('saveGoodsInf').disable();
			vm.button('send').disable();
		}else{
			if(vm.find('f_grid').trows().length){
				vm.button('send').disable(false);
			}else{
				vm.button('send').disable();
			}
		}
	},

	fillGoodsInfo : function(o,goodsId){
		var vm = VM(o);
		var declareId = vm.fv('declId');
		var totalWayBill = vm.fv('totalWayBill');
		var branchWayBill = vm.fv('branchWayBill');
		vm.cmd({tagName:'ajax',src:'formDeclare.sp?act=fillGoodsInfo&goodsId='+goodsId+'&totalWayBill='+totalWayBill+'&branchWayBill='+branchWayBill+'&declId='+declareId+''});
	},
	
	deleteConfirm : function(o,cp){
		var vm = VM(o);
		var itemNum = VM(o).find('f_grid').getSelectedData().length;
		if(!itemNum){
			DFish.alert("请选择要删除的项!");
			return false;
		}
		vm.cmd({tagName:'confirm',cn:'已选择了'+itemNum+'项,是否删除?',args:function(a){
			vm.cmd('deleteDeclare',cp);DFish.close(o);
		}});
	},
	
	receiptPS : function(o,xml){
		var code = xml.getAttribute('receiptCode');
		var result = xml.getAttribute('receiptResult');
		return  code=='CHECK' ?   "<a href=\"javascript:void(0)\" style=\"color:green\" onclick=\"VM(this).cmd('showCheckInfo')\" > "+result+"</a> ":
			 " <span>"+result+" </span>";
	},
	
	ciqReceiptPS : function(o,xml){
		var declId = xml.getAttribute('declareId');
		var num = xml.getAttribute('num');
		var ciqResultCode = xml.getAttribute('ciqResultCode');
		var content = ciqResultCode ?  xml.getAttribute('ciqResult') : xml.getAttribute('ciqCommStatus');
		var color =  ciqResultCode=='CHECK' ? 'green'  : 'black';
		
		var command = "VM(this).cmd('buildModifyView','"+declId+"','"+num+"',VM(this).find('f_page').attr('currentPage'), 'threeMenu')";
		return "<a href=\"javascript:void(0)\" style=\"color:"+color+"\" " +
			"onclick=\""+command+"\" >"+content+"</a> ";
	},
	
	
	ableTopBtn : function(o){
		vm.button('saveDeclare').disable(false);
		vm.button('saveGoodsInf').disable(false);
	}

};