

function reserveChange(o){
	var selectOptions = VM(o).f('selectOptions',DFish.GET_VALUE);
	var options = selectOptions.split(",");
	var counts = 0;
	var sum = 0.0;
	for(i in options){
		var count = VM(o).f('resultCount-'+options[i],DFish.GET_VALUE);
		var price = VM(o).f('optionPrice-'+options[i],DFish.GET_VALUE);
		var resultCount = parseInt(count);
		if(!isNaN(resultCount)){
			counts+=parseInt(count);
		}
		var optionPrice = parseFloat(price);
		if(!isNaN(resultCount)&&!isNaN(optionPrice)){
			sum=sum+(resultCount*optionPrice);
		}
	}
	VM(o).f('priceLabel').innerHTML="";
	$.append(VM(o).f('priceLabel'), '<div name=totalInfo style=text-align:center;>累计数量:'+counts+'    累计总价:'+sum+'</div>');
}