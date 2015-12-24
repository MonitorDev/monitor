var manifest = {
	checkTotalWayBill : function(o){
		var vm = VM(o);
		vm.button('saveDeclare').disable();
		if(!VM(o).fv('totalWayBill')){
			DFish.alert('尚未保存舱单信息！');
			vm.button('saveGoodsInf').disable();
		}else{
			
		}
	}

}