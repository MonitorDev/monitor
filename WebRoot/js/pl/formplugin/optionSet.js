
function delVoteOption(o){
	var count=VM(o).f('optionsCount').value;
	var i=0;var j=0;var k=0;
	for(i=1;i<=count;i++){
		if(VM(o).f('check'+i).innerHTML=='<img src=\"img/p/t/bx1.gif\">'){
			j++;k=i+1;VM(o).f('check'+i).innerHTML='<img src=\"img/p/t/bx0.gif\">';
			break;
		}
	}
	for(;i<=count-j;i++){
		while(k<=count){
			if(VM(o).f('check'+k).innerHTML=='<img src=\"img/p/t/bx1.gif\">'){
				VM(o).f('check'+k).innerHTML='<img src=\"img/p/t/bx0.gif\">';
				k++;j++;
			}else{
				break;
			}
		}
		if(i>count-j)break;
		VM(o).f('op'+i).value=VM(o).f('op'+k).value;k++;
	}
	if(j>0)VM(o).cmd('optionChange','del',j,VM(o).f('optionsCount').value);
}

function delReserveOption(o){
		var count=VM(o).f('optionsCount').value;
	var i=0;var j=0;var k=0;
	for(i=1;i<=count;i++){
		if(VM(o).f('check'+i).innerHTML=='<img src=\"img/p/t/bx1.gif\">'){
			j++;k=i+1;VM(o).f('check'+i).innerHTML='<img src=\"img/p/t/bx0.gif\">';
			break;
		}
	}
	for(;i<=count-j;i++){
		while(k<=count){
			if(VM(o).f('check'+k).innerHTML=='<img src=\"img/p/t/bx1.gif\">'){
				VM(o).f('check'+k).innerHTML='<img src=\"img/p/t/bx0.gif\">';
				k++;j++;
			}else{
				break;
			}
		}
		if(i>count-j)break;
		VM(o).f('op'+i).value=VM(o).f('op'+k).value;k++;
	}
	if(j>0)VM(o).cmd('optionChange','del',j,null,VM(o).f('optionsCount').value);
}
