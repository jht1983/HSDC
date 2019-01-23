
function myfun(title,perNum){
	//月缺陷统计
    var pieYqxtj = echarts.init(document.getElementById('piechartYqxtj'));
    pieYqxtj.setOption(optionYqxtj);
		/*window.addEventListener('resize', function (){
		chartOutChar.resize();
	});*/
    var value = [];
    var tit = [];
    for(var i = 0; i < title.length;i++){
    	var data = {};
    	data.value = Number(perNum[i]);
    	data.name = title[i];
    	tit.push(title[i]);
		value.push(data);
		
	}
    
    pieYqxtj.setOption({
    	legend: {
    		data:tit
        },
    	
		series:[
		   {
			   name:'\u6708\u7f3a\u9677\u7edf\u8ba1',
			   data:value
		   }

		]
	});
}


function ygzptjfun(title,kpsum,jpsum,indexNum){
	//月工作票统计
	var barYgzptj = echarts.init(document.getElementById('barchartYgzptj'));
	barYgzptj.setOption(optionYgzptj);
	
	//var tit = [];
	var kp = new Array(10);
	var jp = new Array(10);
	for(var i = 0;i < indexNum.length;i++){
		kp.splice(indexNum[i], 1, kpsum[i]);
		jp.splice(indexNum[i], 1, jpsum[i]);
		/*tit.push(title[i]);
		kp.push(kpsum[i]);
		jp.push(jpsum[i]);*/
	}
	
	barYgzptj.setOption({
		 xAxis: {
	        type: 'category',
	        data: ['\u7535\u6c14\u4e00\u79cd','\u7535\u6c14\u4e8c\u79cd','\u70ed\u529b\u673a\u68b0',
	               '\u53d7\u9650\u7a7a\u95f4','\u7ee7\u4fdd','\u4e00\u7ea7\u52a8\u706b','\u4e8c\u7ea7\u52a8\u706b',
	               '\u70ed\u63a7','\u5e94\u6025\u62a2\u4fee','\u52a8\u571f']
		 },
	    series: [
 	        {
 	            name: '\u5f00\u7968\u6570\u91cf',
 	            type: 'bar',
 	            barWidth : 12,
 	            barColor : "red",
 	            data: kp
 	        },
 	        {
 	            name: '\u7ed3\u7968\u6570\u91cf',
 	            type: 'bar',
 	            barWidth : 12,
 	            data: jp
 	        }
 	    ]
	});
	
	
}


//月缺陷统计
optionYqxtj = {
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient : 'vertical',
        x : 'left',
        y : 'center',
        data:[]
    },
    calculable : true,
    series : [
        {
            name:'\u6708\u7f3a\u9677\u7edf\u8ba1',
            type:'pie',
            radius : '70%',
            center: ['50%', '50%'],
            data:[]
        }
    ]
};

//月工作票统计
optionYgzptj = {
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'shadow'
	        }
	    },
	    legend: {
	    	show:true,
	    	left: 'right',
	        data: ['\u5f00\u7968\u6570\u91cf', '\u7ed3\u7968\u6570\u91cf']
	    },
	    grid: {
	    	top: '10%',
	        left: '3%',
	        right: '4%',
	        bottom: '20%',
	        containLabel: true
	    },
	    yAxis: {
	        type: 'value',
	        boundaryGap: [0, 0.01]
	    },
	    xAxis: {
	        type: 'category',
	        data: []
	    },
	    series: [
	        {
	            name: '\u5f00\u7968\u6570\u91cf',
	            type: 'bar',
	            data: []
	        },
	        {
	            name: '\u7ed3\u7968\u6570\u91cf',
	            type: 'bar',
	            data: []
	        }
	    ]
	
}
    