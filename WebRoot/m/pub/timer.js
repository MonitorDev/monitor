/***********************************************************
 * 日程
 * by chenmingyuan 2013-1-21 11:20:41
 ************************************************************/

var timer =  { 
	
   
	repeatConfig: function(vm){
		var timerConfig=vm.fv(vm.fv('name'));
		//FIXME 如果value为空的话，beginTime能把当前设定的日程开始时间带过去
		//带过去的话要求 {"unit":"","begin":"xxxx","endStyle","0"}
		var begin = vm.fv('dp');
		if(begin){
		if(!(/^(\d{4})\-(\d{2})\-(\d{2}) (\d{2}):(\d{2})$/ .test(begin))){
				DFish.alert('非法的时间格式');
				return;
			}
			
		}
		if(timerConfig=='{}'){
			var begin=vm.fv("beginTime");
			var failQuitCount = vm.fv("failQuitCount");
			var jobType = vm.fv("jobType");
			var misFiredType = vm.fv("misFiredType");
			var d = js.d_ps( begin, 'yyyy-mm-dd hh:ii');
			timerConfig='{"unit":"","begin":"'+begin+'","weekday":"'+(d.getDay()+1)+'","endStyle":"0","repeatStyle":"M","failQuitCount":"'+failQuitCount+'","jobType":"'+jobType+'","misFiredType":"'+misFiredType+'"}';
		}
		vm.cmd( { tagName : 'dialog', n : 'timerPicker', tpl : 'f_std', t : '定时设置', pophide : false,
		w :440, h :400 ,pos:0, src :'vm:|timer.sp?act=timer&v='+timerConfig } );
	},changeUnit :function(vm){
			var unit=vm.fv('unit');
			var showWeekday=false;
			var showRepeatStyle=false;
			var showBegin=true;
			var showEndStyle=true;
			var showInterval=true;
			if(unit==''){
				showBegin=false;
				showEndStyle=false;
				showInterval=false;
			}else if(unit=='N'){
				showBegin=true;
				showEndStyle=false;
				showInterval=false;
			}
			if(unit=='W'){
				showWeekday=true;
			}
			if(unit=='M'){
				showRepeatStyle=true;
			}
			vm.f('weekday',DFish.HIDDEN,!showWeekday);
			vm.f('repeatStyle',DFish.HIDDEN,!showRepeatStyle);
			vm.f('begin',DFish.HIDDEN,!showBegin);
			vm.f('endStyle',DFish.HIDDEN,!showEndStyle);
			vm.f('interval',DFish.HIDDEN,!showInterval);
			
			this.dataChange(vm);
		},
		setValueByPicker:function(vm){
			var begin = vm.fv('begin');
			var end = vm.fv('end');
			var count = vm.fv('count');
			var endStyle = vm.fv('endStyle');
			var weekday = vm.fv('weekday');
			var unit = vm.fv('unit');
			if(!begin){
				DFish.alert('开始时间不能为空');
				return;
			}
			if(!(/^(\d{4})\-(\d{2})\-(\d{2}) (\d{2}):(\d{2})$/ .test(begin))){
				DFish.alert('开始时间格式错误');
				return;
			}
			if(endStyle=='2'){
				if(end){
					if(!(/^(\d{4})\-(\d{2})\-(\d{2}) (\d{2}):(\d{2})$/ .test(end))){
						DFish.alert('结束时间格式错误');
						return;
					}
					var beginTime = js.d_ps(begin);
					var endTime = js.d_ps(end);
					if(endTime<beginTime){
						DFish.alert('结束时间不能早于开始时间');
						return;
					}
				}else{
					DFish.alert('结束时间不能为空');
						return;
				}
			}
			if(endStyle=='1'){
				if( !count  || count<1 || !(parseInt(count)==count)){
					DFish.alert('发生次数必须大于0,且是整数');
					return;
				}
			}
			if(unit=='W' && !weekday){
				
				DFish.alert('重复时间不能为空');
				return;
			}
			var panelView = DFish.g_dialog(vm).fromView;
			var unit = vm.fv('unit');
				var v={'unit':unit,
				'interval':vm.fv('interval'),
				'begin':vm.fv('begin'),
				'endStyle':vm.fv('endStyle'),
				'weekday':vm.fv('weekday'),
				'count':vm.fv('count'),
				'end':vm.fv('end'),
				'repeatStyle':vm.fv('repeatStyle'),
				'failQuitCount':DFish.g_dialog(vm).fromView.fv('failQuitCount'),
				'jobType':DFish.g_dialog(vm).fromView.fv('jobType'),
				'misFiredType':DFish.g_dialog(vm).fromView.fv('misFiredType')
				};
				var tx=vm.fv('summary');
				if(unit!=''){
				panelView.fs(panelView.fv('name'), js.toJSONString(v));
				}else{
				panelView.fs(panelView.fv('name'), '{}');
				}
				panelView.fs('timerTxt', tx );
				panelView.f('dp').parentNode.style.display='none';//onclick=\"timer.repeatConfig(VM(this),\''+vm.fv('begin')+'\')\"
				Q('#cfpz',panelView.$()).replaceWith(' <span  id=cfpz><a href=javascript:; class=a-blue onclick=\"timer.repeatConfig(VM(this),\''+vm.fv('begin')+'\')\">高级</a> | <a  href=javascript:; class=a-blue onclick=\"timer.cancelConFig(VM(this))\">取消</a></span>');
				DFish.close(vm);
			
			//DFish.alert( tx+"unit:"+vm.fv('unit'));
		},
		weekDayNameArr:['','日','一','二','三','四','五','六'],
		dataChange:function(vm){
			var summary='';
			var unit=vm.fv('unit');
			var interval=vm.fv('interval');
			var begin=vm.fv('begin');
			var endStyle=vm.fv('endStyle');
			if(unit==''){
				summary='无定时配置';
			}else if(unit=='N'){
				summary='在'+begin;//仅一次
			}else{
				summary+='从';
				summary+=begin;
				summary+='开始,每';
				if(interval>1){
					summary+=interval;
				}
				var intervalUnit='';
				if('I'==unit){
					summary+=intervalUnit='分钟';
				}else if('H'==unit){
					summary+=intervalUnit='小时';
				}else if('D'==unit){
					summary+=intervalUnit='天';
				}else if('W'==unit){
					summary+=intervalUnit='周';
					var weekday=vm.fv('weekday');
					//alert(weekday);
					if(weekday!=''&&weekday.length<13){
						summary+='('
						var wds=weekday.split(',');
						for(var w=0;w<wds.length;w++){
							summary+=this.weekDayNameArr[wds[w]];
							
							if(w==wds.length-1){
								summary+=')';
							}else{
								summary+=',';
							}
						}
					}
				}else if('M'==unit){
					summary+=intervalUnit='个月';
					var repeatStyle=vm.fv('repeatStyle');
					
					if(repeatStyle=='W'){
						var beginDate=js.d_ps(begin);
						if(beginDate.getDate() > 28){
							summary += '(最后一周' + this.weekDayNameArr[beginDate.getDay()+1]+")";
						}else{
								summary += '(第' + Math.ceil((beginDate.getDate() ) / 7) + '周的周' + this.weekDayNameArr[beginDate.getDay()+1]+")";
						}
					}
				}else if('Y'==unit){
					summary+=intervalUnit='年';
				}
				vm.fs('intervalUnit',intervalUnit);
				summary+='执行';
				if('1'==endStyle){
					var count=vm.fv('count');
					summary+=',共执行'+count+'次';
				}else if('2'==endStyle){
					var end=vm.fv('end');
					summary+='直到'+end;
					summary+='结束';
				}
			}
			vm.fs('summary',summary);
		},
		setTimerConfig: function(vm){
		var begin = vm.fv('dp');
		var failQuitCount = vm.fv("failQuitCount");
		var jobType = vm.fv("jobType");
		var misFiredType = vm.fv("misFiredType");
		var v={'unit':'D',
				'interval':'1',
				'begin':begin,
				'endStyle':'1',
				'weekday':'',
				'count':'1',
				'end':'',
				'repeatStyle':'M',
				'failQuitCount':failQuitCount,
				'jobType':jobType,
				'misFiredType':misFiredType
				};
		vm.fs(vm.fv('name'),js.toJSONString(v));

	},
	cancelConFig:function(vm){
	var begin = vm.fv('dp');
	var failQuitCount = vm.fv("failQuitCount");
	var jobType = vm.fv("jobType");
	var misFiredType = vm.fv("misFiredType");
	var v={'unit':'D',
				'interval':'1',
				'begin':begin,
				'endStyle':'1',
				'weekday':'',
				'count':'1',
				'end':'',
				'repeatStyle':'M',
				'failQuitCount':failQuitCount,
				'jobType':jobType,
				'misFiredType':misFiredType
				};
		vm.fs(vm.fv('name'), js.toJSONString(v));		
		//vm.fs('timerConfig',js.toJSONString(v));
		vm.f('dp').parentNode.style.display='';
		vm.fs('timerTxt','' );
		vm.f('dp',DFish.HIDDEN,false);
		Q('#cfpz',vm.$()).replaceWith('<span  id=cfpz><a href=javascript:; class=a-blue onclick=\"timer.repeatConfig(VM(this),\''+vm.fv('begin')+'\')\">高级</a></span>');
	}
}
