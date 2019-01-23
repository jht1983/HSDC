var ylt= ylt ||{};
ylt.Gtt = ylt.Gtt || {};
var yltGtt=ylt.Gtt={
	arrMothDays :[31,28,31,30,31,30,31,31,30,31,30,31],
	arrMothRNDays :[31,29,31,30,31,30,31,31,30,31,30,31],
	arrMoth:['01','02','03','04','05','06','07','08','09','10','11','12'],
	strWeeks:"日一二三四五六",
	arrTimeNames:['年','半年','季','月','周','日'],
	arrTimeCodes:['6','5','4','3','2','1'],
	strSecHead:"<tr height='20'>",
	strSecData:"<tr>",
	iTotalWidth:0,
	iTopHeadColSpan:0,
	arrDays:new Array(),
	objDayIndex:new Object(),
	iHeadWidth:25,
	iInitHeadWidth:25,
	iHeadHeight:20,
	iTaskHeight:42,
	graphDataHeight:30,//数据高度
	strGraph:"",
	strTasks:"",
	drawScript:"",
	iSecType:1,//1日2周3月4季5半年6年
	iTopType:3,
	iGraphWidth:0,
	iGraphHeight:0,
	iCurDataIndex:1,
	strDirClose:"folder1.gif",
	strDirOpen:"folderopen1.gif",
	strFileImg:"page1.gif",
	strHeadData:"<tr style='height:0px;'>",
	objCurSelectRow:null,
	objCurDate:null,
	strGreesGround:'images/content/grees.png',
	strGreesGround_Red:'images/content/grees_red.png',
	strGreesGround_Green:'images/content/grees_green.png',
	initConfig:function(){
		this.strSecHead="<tr height='20px'>";
		this.strSecData="<tr>";
		this.iTotalWidth=0;
		this.iTopHeadColSpan=0;
		this.arrDays=new Array();
		this.objDayIndex=new Object();
		this.strGraph="";
		this.strTasks="";
		this.iCurDataIndex=1;
		this.strHeadData="<tr style='height:0px;'>";
		this.objCurSelectRow=null;
	},
	yearIsRN:function(_year){//判断闰年
		if (0==_year%4&&((_year%100!=0)||(_year%400==0))) return true;else return false;
	},
	getMonthDays:function(_year,_month){//获取每月多少天
		var c=this.arrMothDays[_month-1];if((_month==2)&&this.yearIsRN(_year)) c++;return c;
	},
	getCurDate:function(){
		var iMonth=this.objCurDate.getMonth()+1;
		var strMonth=""+iMonth;
		if(iMonth<10)
			strMonth="0"+iMonth;
		var iDay=this.objCurDate.getDate();
		var strDay=""+iDay;
		if(iDay<10)
			 strDay="0"+iDay;
		return this.objCurDate.getFullYear()+"/"+strMonth+"/"+strDay;
	},
	generHead:function(_iMinYear,_iMinMonth,_iMaxYear,_iMaxMonth,_arrDaysTemp,_iYear){
		
		var iStartMonth=_iMinMonth-1;
		var iEndMonth=12;
		if(_iYear>_iMinYear)
			iStartMonth=0;
		if(_iYear==_iMaxYear)
			iEndMonth=_iMaxMonth;
		//iStartMonth=0;iEndMonth=12;
		var strTempCurDate=this.getCurDate();
		var strBorderStyle="";
		for(var j=iStartMonth;j<iEndMonth;j++){//月份
				var iCurMothDays=_arrDaysTemp[j];
				
		switch(this.iTopType){
			case 2://周
							
							
							switch(this.iSecType){
								case 1://日	   此处需要在年循环的外围再进行处理，显示多于的列头
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
													
												var sCurDate = _iYear+"/"+this.arrMoth[j]+"/"+strViewDay;
												var iCurWeek=new Date(sCurDate).getDay();												
												this.iTotalWidth+=this.iHeadWidth;
												strBorderStyle="";
												if(sCurDate==strTempCurDate){
													strBorderStyle=" align='right' valign='top'><div style='position:absolute;background:red;width:3px;height:100px;z-index:1;'></div";
													
												}												
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;'>"+this.strWeeks.charAt(iCurWeek)+"</th>";
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
												
												if(iCurWeek==0||iCurWeek==6)
													this.arrDays.push("<td bgcolor='#f6f6f6'"+strBorderStyle+">&nbsp;</td>");
												else
													this.arrDays.push("<td"+strBorderStyle+">&nbsp;</td>");
												
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
												
												
												this.iTopHeadColSpan++;
												if(iCurWeek==0){//星期日
													this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;' align='center'>"+_iYear+"-"+this.arrMoth[j]+"-"+strViewDay+"</th>";
													this.iTopHeadColSpan=0;
												}
											}
									//		if(iTopHeadSpan>0)
									//			this.strGraph+="<th colspan='"+iTopHeadSpan+"' style='height:"+this.iHeadHeight+";'>"+_iYear+"年"+this.arrMoth[j]+"月"+strViewDay+"日</th>";
									break;
							}
				break;
			case 3://月
							switch(this.iSecType){
								case 1://日
											this.strGraph+="<th colspan='"+iCurMothDays+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+"年"+this.arrMoth[j]+"月</th>";
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
												this.iTotalWidth+=this.iHeadWidth;
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;'>"+strViewDay+"</th>";
					
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
												
												var sCurDate = _iYear+"/"+this.arrMoth[j]+"/"+strViewDay;
												var iCurWeek=new Date(sCurDate).getDay();	
												
												strBorderStyle="";
												if(sCurDate==strTempCurDate){
													strBorderStyle=" style='border-right:3px solid red;'";
												}	
												
												if(iCurWeek==0||iCurWeek==6)
													this.arrDays.push("<td bgcolor='#f6f6f6'"+strBorderStyle+">&nbsp;</td>");
												else
													this.arrDays.push("<td"+strBorderStyle+">&nbsp;</td>");		
												
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
											}
									break;
								case 2://周		
											var iTopHeadColsSpan=0;
											
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;												
												var sCurDate = _iYear+"/"+this.arrMoth[j]+"/"+strViewDay;
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
												
												
												
												//alert(sCurDate+":"+strTempCurDate);
												if(sCurDate==strTempCurDate){
													strBorderStyle=" style='border-right:3px solid red;'";
												}
												//strBorderStyle=" style='border-right:3px solid red;'";
												if(new Date(sCurDate).getDay()!=0)
													continue;
												
												this.iTotalWidth+=this.iHeadWidth;
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;'>"+strViewDay+"</th>";
												this.arrDays.push("<td"+strBorderStyle+">&nbsp;</td>");
												strBorderStyle="";
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
												
												iTopHeadColsSpan++;
											}
											this.strGraph+="<th colspan='"+iTopHeadColsSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+"年"+this.arrMoth[j]+"月</th>";
									break;
							}
				break;
			case 4://季     此处需要在外围加入缺少的季度
							switch(this.iSecType){
								case 1:
											this.iTopHeadColSpan+=iCurMothDays;
											if(j==2||j==5||j==8||j==11){
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+"年"+(j+1)/3+"季度</th>";
												this.iTopHeadColSpan=0;
											}
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
												this.iTotalWidth+=this.iHeadWidth;
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;'>"+strViewDay+"</th>";
					
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
												
												
												var sCurDate = _iYear+"/"+this.arrMoth[j]+"/"+strViewDay;
												var iCurWeek=new Date(sCurDate).getDay();	
												
												strBorderStyle="";
												if(sCurDate==strTempCurDate){
													strBorderStyle=" style='border-right:3px solid red;'";
												}	
												
												if(iCurWeek==0||iCurWeek==6)
													this.arrDays.push("<td bgcolor='#f6f6f6'"+strBorderStyle+">&nbsp;</td>");
												else
													this.arrDays.push("<td"+strBorderStyle+">&nbsp;</td>");
												
												
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
												
												
												
											}
									break;
								case 2:									
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
													
												var sCurDate = _iYear+"/"+this.arrMoth[j]+"/"+strViewDay;
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
												
												if(sCurDate==strTempCurDate){
													strBorderStyle=" style='border-right:3px solid red;'";
												}
												
												if(new Date(sCurDate).getDay()!=0)
													continue;	
													
												this.iTotalWidth+=this.iHeadWidth;
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;'>"+strViewDay+"</th>";
					
												this.arrDays.push("<td"+strBorderStyle+">&nbsp;</td>");
												strBorderStyle="";
												
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
												
												
												
												this.iTopHeadColSpan++;
											}
											
											if(j==2||j==5||j==8||j==11){
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+"年"+(j+1)/3+"季度</th>";
												this.iTopHeadColSpan=0;
											}
									break;
								case 3:
									for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;													
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
												
												if((_iYear+"/"+this.arrMoth[j]+"/"+strViewDay)==strTempCurDate){
												
													strBorderStyle=" background:red;";
												}
											}
											this.iTotalWidth+=this.iHeadWidth;							
											this.strSecHead+="<th style='width:"+this.iHeadWidth*4+"px;height:"+this.iHeadHeight+"px;"+strBorderStyle+"'>"+this.arrMoth[j]+"</th>";
										
											this.arrDays.push("<td>&nbsp;</td>");
											strBorderStyle="";
											this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
											
											this.iTopHeadColSpan++;
											if(j==2||j==5||j==8||j==11){
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+"年"+(j+1)/3+"季度</th>";
												this.iTopHeadColSpan=0;
											}
										break;
							}
				break;
			case 5://半年          此处需要在外围加入缺少的半年度
							switch(this.iSecType){
								case 1:
											this.iTopHeadColSpan+=iCurMothDays;
											if(j==5||j==11){
												var  strBNDMsg="上";
												if(j==11)
													strBNDMsg="下";
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+strBNDMsg+"半年</th>";
												this.iTopHeadColSpan=0;
											}
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
												this.iTotalWidth+=this.iHeadWidth;
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;'>"+strViewDay+"</th>";
					
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
												
												
												var sCurDate = _iYear+"/"+this.arrMoth[j]+"/"+strViewDay;
												var iCurWeek=new Date(sCurDate).getDay();	
												
												strBorderStyle="";
												if(sCurDate==strTempCurDate){
													strBorderStyle=" style='border-right:3px solid red;'";
												}	
												
												if(iCurWeek==0||iCurWeek==6)
													this.arrDays.push("<td bgcolor='#f6f6f6'"+strBorderStyle+">&nbsp;</td>");
												else
													this.arrDays.push("<td"+strBorderStyle+">&nbsp;</td>");
												
												
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
											}
									break;
								case 2:											
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
													
												var sCurDate = _iYear+"/"+this.arrMoth[j]+"/"+strViewDay;
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
												
												
												if(sCurDate==strTempCurDate){
													strBorderStyle=" style='border-right:3px solid red;'";
												}	
												
												if(new Date(sCurDate).getDay()!=0)
													continue;	
													
												this.iTotalWidth+=this.iHeadWidth;
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;'>"+strViewDay+"</th>";
												this.arrDays.push("<td"+strBorderStyle+">&nbsp;</td>");
												strBorderStyle="";
												
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
												this.iTopHeadColSpan++;
											}
											
											if(j==5||j==11){
												var  strBNDMsg="上";
												if(j==11)
													strBNDMsg="下";
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+strBNDMsg+"半年</th>";
												this.iTopHeadColSpan=0;
											}
											
											
									break;
								case 3:
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
												if(_iYear+"/"+this.arrMoth[j]+"/"+strViewDay==strTempCurDate){
													strBorderStyle=" background:red;";
												}		
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
											}
											
											this.iTotalWidth+=this.iHeadWidth;
											this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;"+strBorderStyle+"'>"+this.arrMoth[j]+"</th>";
											this.arrDays.push("<td>&nbsp;</td>");
											
											strBorderStyle="";
											
											this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
											this.iTopHeadColSpan++;
											
											if(j==5||j==11){
												var  strBNDMsg="上";
												if(j==11)
													strBNDMsg="下";
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+strBNDMsg+"半年</th>";
												this.iTopHeadColSpan=0;
											}
									break;
								case 4:
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
												
												if(_iYear+"/"+this.arrMoth[j]+"/"+strViewDay==strTempCurDate){
													strBorderStyle=" background:red;";
												}	
												
												
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
											}
											
											if(j==2||j==5||j==8||j==11){
												this.iTotalWidth+=this.iHeadWidth;
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;"+strBorderStyle+"'>"+(j+1)/3+"</th>";
												this.arrDays.push("<td>&nbsp;</td>");
												
												strBorderStyle="";
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
												this.iTopHeadColSpan++;
											}
											
											if(j==5||j==11){
												var  strBNDMsg="上";
												if(j==11)
													strBNDMsg="下";
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+strBNDMsg+"半年</th>";
												this.iTopHeadColSpan=0;
											}
									break;
							}
				break;
			case 6://年
							switch(this.iSecType){
								case 1:
											this.iTopHeadColSpan+=iCurMothDays;
											if(j==11){
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+"年</th>";
												this.iTopHeadColSpan=0;
											}
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
												this.iTotalWidth+=this.iHeadWidth;
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;'>"+strViewDay+"</th>";
					
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
												
												var sCurDate = _iYear+"/"+this.arrMoth[j]+"/"+strViewDay;
												var iCurWeek=new Date(sCurDate).getDay();	
												
												
												strBorderStyle="";
												if(sCurDate==strTempCurDate){
													strBorderStyle=" style='border-right:3px solid red;'";
												}	
												
												if(iCurWeek==0||iCurWeek==6)
													this.arrDays.push("<td bgcolor='#f6f6f6'"+strBorderStyle+">&nbsp;</td>");
												else
												this.arrDays.push("<td"+strBorderStyle+">&nbsp;</td>");
												
												
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
											}
									break;
								case 2:											
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
													
												var sCurDate = _iYear+"/"+this.arrMoth[j]+"/"+strViewDay;
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
												
												if(sCurDate==strTempCurDate){
													strBorderStyle=" style='border-right:3px solid red;'";
												}	
												if(new Date(sCurDate).getDay()!=0)
													continue;	
													
												this.iTotalWidth+=this.iHeadWidth;
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;'>"+strViewDay+"</th>";
												this.arrDays.push("<td"+strBorderStyle+">&nbsp;</td>");
												
												strBorderStyle="";
												
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
												this.iTopHeadColSpan++;
											}
											
											if(j==11){
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+"年</th>";
												this.iTopHeadColSpan=0;
											}
											
											
									break;
								case 3:
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
												if(_iYear+"/"+this.arrMoth[j]+"/"+strViewDay==strTempCurDate){
													strBorderStyle=" background:red;";
												}		
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
											}
											
											this.iTotalWidth+=this.iHeadWidth;
											this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;"+strBorderStyle+"'>"+this.arrMoth[j]+"</th>";
											this.arrDays.push("<td>&nbsp;</td>");
											strBorderStyle="";
											
											this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
											this.iTopHeadColSpan++;
											
											if(j==11){
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+"年</th>";
												this.iTopHeadColSpan=0;
											}
									break;
								case 4:
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
												if(_iYear+"/"+this.arrMoth[j]+"/"+strViewDay==strTempCurDate){
													strBorderStyle=" background:red;";
												}	
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
											}
											
											if(j==2||j==5||j==8||j==11){
												this.iTotalWidth+=this.iHeadWidth;
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;"+strBorderStyle+"'>"+(j+1)/3+"</th>";
												this.arrDays.push("<td>&nbsp;</td>");
												strBorderStyle="";
												
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
												this.iTopHeadColSpan++;
											}
											
											if(j==11){
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+"年</th>";
												this.iTopHeadColSpan=0;
											}
									break;
									case 5:
											for(var k=1;k<=iCurMothDays;k++){
												var strViewDay=k;
												if(k<10)
													strViewDay="0"+k;
												if(_iYear+"/"+this.arrMoth[j]+"/"+strViewDay==strTempCurDate){
													strBorderStyle=" background:red;";
												}	
												this.objDayIndex[_iYear+"-"+this.arrMoth[j]+"-"+strViewDay]=this.arrDays.length;
											}
											
											if(j==5||j==11){
												var  strBNDMsg="上";
												if(j==11)
													strBNDMsg="下";
												this.iTotalWidth+=this.iHeadWidth;
												this.strSecHead+="<th style='width:"+this.iHeadWidth+"px;height:"+this.iHeadHeight+"px;"+strBorderStyle+"'>"+strBNDMsg+"</th>";
												this.arrDays.push("<td>&nbsp;</td>");
												strBorderStyle="";
												this.generDataHead();//////////////////////////////////////////////////////////////////////////////////////
												
												this.iTopHeadColSpan++;
											}
											
											if(j==11){
												this.strGraph+="<th colspan='"+this.iTopHeadColSpan+"' style='height:"+this.iHeadHeight+"px;'>"+_iYear+"年</th>";
												this.iTopHeadColSpan=0;
											}
									break;
							}
				break;
		}
		
			}
	},
	generDataHead:function(){
		this.strHeadData+="<td style='padding-top:0px;'></td>";///////////////////////////////////////////////////////////
	},
	viewGraph:function(_iMinYear,_iMinMonth,_iMaxYear,_iMaxMonth,_iTopType,_iSecType){
		this.iTopType=_iTopType;
		this.iSecType=_iSecType;
		for(var i=_iMinYear;i<=_iMaxYear;i++){
			var arrDaysTemp;
			if(this.yearIsRN(i))
				arrDaysTemp=this.arrMothRNDays;
			else
				arrDaysTemp=this.arrMothDays;			
			this.generHead(_iMinYear,_iMinMonth,_iMaxYear,_iMaxMonth,arrDaysTemp,i);
		}
		
		this.strGraph+="</tr>"+this.strSecHead+"</tr></table></div><div   id='ylgttData'   onscroll='synGraphDataHead();'  style='overflow-x:scroll;overflow-y:hidden;width:"+this.iGraphWidth+"px;height:"+this.iGraphHeight+"px;'><table id='ylgttDataTb' class='gtttb' style='width:"+this.iTotalWidth+"px;'>"+this.strHeadData+"</tr>";
		this.strGraph="<div  id='ylgtthead' style='width:"+this.iGraphWidth+"px;overflow:hidden;'><table id='ylgtt' class='gtttb' style='width:"+this.iTotalWidth+"px;'><tr>"+this.strGraph;
		this.strSecData+="</tr>";
	},
	generData:function(){
		this.strTasks="<div id='gtttaskhead' style='width:450px;'><table  class='gtttb' width='450px'><tr style='height:"+this.iTaskHeight+"px;'><th  style='width:50px;height:"+this.iTaskHeight+"px;'>序号</th><th width='200px'>任务名称</th><th width='100px'>开始时间</th><th width='100px'>结束时间</th></tr></table></div>";
		this.strTasks+="<div    id='ylgttTaskData'  style='overflow-x:scroll;overflow-y:hidden;height:"+this.iGraphHeight+"px;width:450px;'><table id='gtttasktable' class='gtttb' width='450px'><tr id='tasrktrhead' style='height:0px;'><th width='50px'></th><th width='200px'></th><th width='100px'></th><th width='100px'></th></tr>";
		this.generChilData(root,-1);
		this.strTasks+="</table></div>";
		yltGtt.strGraph+="</table></div>";
	},	
	msOver:function(_obj,_iType){
		if(_iType==0){
			var objGraphiRow=ylgttDataTb.rows[_obj.rowIndex];
			_obj.style.backgroundColor=objGraphiRow.style.backgroundColor="#e3ecf3";
		}else{
			var objTaskRow=gtttasktable.rows[_obj.rowIndex];
			_obj.style.backgroundColor=objTaskRow.style.backgroundColor="#e3ecf3";
		}
	},
	msOut:function(_obj,_iType){
		if(this.objCurSelectRow!=_obj){
			if(_iType==0){
				var objGraphiRow=ylgttDataTb.rows[_obj.rowIndex];
				_obj.style.backgroundColor=objGraphiRow.style.backgroundColor="white";
			}else{
				var objTaskRow=gtttasktable.rows[_obj.rowIndex];
				_obj.style.backgroundColor=objTaskRow.style.backgroundColor="white";
			}
		}
	},
	msClick:function(_obj){
		if(this.objCurSelectRow!=null){
			var objParGraphRow=ylgttDataTb.rows[this.objCurSelectRow.rowIndex];
			this.objCurSelectRow.style.backgroundColor=objParGraphRow.style.backgroundColor="white";
		}
		var objGraphiRow=ylgttDataTb.rows[_obj.rowIndex];
		_obj.style.backgroundColor=objGraphiRow.style.backgroundColor="#e3ecf3";
		this.objCurSelectRow=_obj;
		
		var objCurGraph=$("sys_graph_"+_obj.rowIndex);
		if(objCurGraph!=null){
			var iGraphPosX=objCurGraph.parentElement.getBoundingClientRect().left-472;
			ylgttData.scrollLeft=ylgttData.scrollLeft+iGraphPosX;
		}
		
	},
	iGraphNameIndex:1,
	generTextTip:function(_objNode){
		return "进度:<font color='green'><b>80</b></font>%&nbsp;&nbsp;&nbsp;&nbsp;督办：<font color='red' onclick=\"alert('ok');\"><b>10</b></font>次";
	},
	generChilData:function(_objNode,_iLevel){
		if(_iLevel>=0){
			var strViewImg=this.strFileImg;
			var bIsLeaf=true;
			if(_objNode.children!=null&&_objNode.children.length>0){
				 strViewImg=this.strDirOpen;
				 bIsLeaf=false;
			}
			
			this.strTasks+="<tr onmouseover='yltGtt.msOver(this,0);' onmouseout='yltGtt.msOut(this,0);' onClick='yltGtt.msClick(this);' style='height:"+this.graphDataHeight+"px;'><td width='50px'>"+this.iCurDataIndex+"</td><td style='padding:0em "+_iLevel+"em;word-break:keep-all;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;'>"+
			"<img src='images/tree/"+strViewImg+"' align='left'>"+_objNode.text+
			"</td><td width='100'>"+_objNode[_strStar]+"</td><td width='100'>"+_objNode[_strEnd]+"</td></tr>";
			//yltGtt.strGraph+=this.strSecData;
			var arrTempDays=this.arrDays.slice(0);
			var iGressWidth=(this.objDayIndex[_objNode[_strEnd]]-this.objDayIndex[_objNode[_strStar]]+1)*this.iHeadWidth-3;
			
			if(bIsLeaf){
			if(_strDoStar!="null"){
				var iDoGressLeft=(this.objDayIndex[_objNode[_strDoStar]]-this.objDayIndex[_objNode[_strStar]])*this.iHeadWidth-3;
				var iDoGressWidth=(this.objDayIndex[_objNode[_strDoEnd]]-this.objDayIndex[_objNode[_strDoStar]]+1)*this.iHeadWidth-3;
				var strDoTaskBackground=this.strGreesGround_Red;
				if(this.objDayIndex[_objNode[_strEnd]]>=this.objDayIndex[_objNode[_strDoEnd]]){		

					if(this.objDayIndex[_objNode[_strStar]]==this.objDayIndex[_objNode[_strEnd]])
						arrTempDays[this.objDayIndex[_objNode[_strEnd]]+1]="<td valign='top'><div style='position:absolute;padding-left:5px;'>"+this.generTextTip(_objNode)+"</div></td>";
					else
						arrTempDays[this.objDayIndex[_objNode[_strEnd]]]="<td valign='top'><div style='position:absolute;padding-left:"+(this.iHeadWidth+5)+"px;'>"+this.generTextTip(_objNode)+"</div></td>";
					strDoTaskBackground=this.strGreesGround_Green;
				}else{
					if(this.objDayIndex[_objNode[_strStar]]==this.objDayIndex[_objNode[_strDoEnd]])
						arrTempDays[this.objDayIndex[_objNode[_strDoEnd]]+1]="<td valign='top'><div style='position:absolute;padding-left:5px;'>"+this.generTextTip(_objNode)+"</div></td>";
					else
						arrTempDays[this.objDayIndex[_objNode[_strDoEnd]]]="<td valign='top'><div style='position:absolute;padding-left:"+(this.iHeadWidth+5)+"px;'>"+this.generTextTip(_objNode)+"</div></td>";
				}
				arrTempDays[this.objDayIndex[_objNode[_strStar]]]="<td valign='top'><div class='divgrees' style='width:"+iGressWidth+"px;height:"+this.graphDataHeight/2+"px;text-valign:middle;padding-left:"+iDoGressLeft+"px;' id='sys_graph_"+this.iGraphNameIndex+"'><div style='width:"+iDoGressWidth+"px;height:"+this.graphDataHeight/3+"px;background:url("+strDoTaskBackground+");border:1px solid blue;'></div></div></td>";	
			}else
				arrTempDays[this.objDayIndex[_objNode[_strStar]]]="<td valign='top'><div class='divgrees' style='width:"+iGressWidth+"px;height:"+this.graphDataHeight/2+"px;text-valign:middle;padding-left:"+iDoGressLeft+"px;' id='sys_graph_"+this.iGraphNameIndex+"'></div></td>";	
				this.iGraphNameIndex++;
			}else{
				arrTempDays[this.objDayIndex[_objNode[_strStar]]]="<td valign='top'><div class='divpargrees' style='width:"+iGressWidth+"px;height:"+this.graphDataHeight/2+"px;' id='sys_graph_"+this.iGraphNameIndex+"'></div></td>";
				this.iGraphNameIndex++;
				}
			yltGtt.strGraph+="<tr  onmouseover='yltGtt.msOver(this,1);'  onmouseout='yltGtt.msOut(this,1);' style='height:"+this.graphDataHeight+"px;'>"+arrTempDays.join("")+"</tr>";
			
			//this.drawScript+="yltGtt.drawDateGrees("+this.iIndex+",'"+_objNode.STAR+"','"+_objNode.END+"');";
			this.iCurDataIndex++;
		}
		_iLevel++;
		var arrTask=_objNode.children;
		if(arrTask==null)
			return ;
		var iTaskLength=arrTask.length;
		
		for(var i=0;i<iTaskLength;i++){
			
			this.generChilData(arrTask[i],_iLevel);
		}
		
	},
	doDraw:function(_objStartTd,_objEndTd){
		if(_objStartTd!=null&&_objEndTd!=null){
	
				var bgObj=document.createElement("DIV");
						bgObj.style.width=_objEndTd.offsetLeft-_objStartTd.offsetLeft+this.iHeadWidth;
						bgObj.style.top=_objStartTd.offsetTop+3;
						bgObj.style.left=_objStartTd.offsetLeft;//objStartPos.left;
						bgObj.style.position="absolute";
						bgObj.innerHTML='aaa';
						bgObj.className="divgrees";
						bgObj.style.height=(this.iHeadHeight-6);
					_objStartTd.appendChild(bgObj);
			
			}
	},
	drawDateGrees:function(_iRowIndex,_starDate,_endDate){
		var objCells=$("ylgtt").rows[_iRowIndex].cells;
		var objStartTd=null;
		var objEndTd=null;
		var iLength=objCells.length;
		for(var i=0;i<iLength;i++){
			if(objCells[i].attributes['cdate'].nodeValue==_starDate)
				objStartTd=objCells[i];
			
			if(objCells[i].attributes['cdate'].nodeValue==_endDate){
				objEndTd=objCells[i];
				break;
			}
		}
			this.doDraw(objStartTd,objEndTd);
	},
	changeType:function(_strRandId,_iMinYear,_iMinMonth,_iMaxYear,_iMaxMonth){
		this.init(_strRandId,this.iGraphWidth,this.iGraphHeight,_iMinYear,_iMaxYear,parseInt(seltop.value),parseInt(sectop.value));
	},
	init:function(_strRandId,_iWidth,_iHeight,_iMinYear,_iMaxYear,_iTopType,_iSecType){
		this.objCurDate=new Date();
		var _iMinMonth=1;
		var _iMaxMonth=12;
		this.iGraphWidth=_iWidth;
		this.iGraphHeight=_iHeight;
		this.initConfig();
		//var d1=new Date();
		var objRander=document.getElementById(_strRandId);
		
		var strChangeEvent="yltGtt.changeType(\""+_strRandId+"\","+_iMinYear+","+_iMinMonth+","+_iMaxYear+","+_iMaxMonth+");";
		
		var strTopTimeSel="<input id='seltop' value='"+_iTopType+"' type='text' texts='"+this.arrTimeNames.slice(0,(6-_iSecType)).join()+
										"' codes='"+this.arrTimeCodes.slice(0,(6-_iSecType)).join()+"' onchange='"+strChangeEvent+"'>";					
										
		var strSecTimeSel="<input id='sectop' value='"+_iSecType+"' type='text' texts='"+this.arrTimeNames.slice((7-_iTopType)).join()+
										"' codes='"+this.arrTimeCodes.slice((7-_iTopType)).join()+"' onchange='"+strChangeEvent+"'>";
		if(_iSecType>2)
			this.iHeadWidth=this.iInitHeadWidth*(_iSecType+1);
		else
			this.iHeadWidth=this.iInitHeadWidth;
		objRander.innerHTML="数据加载中...";
		this.viewGraph(_iMinYear,_iMinMonth,_iMaxYear,_iMaxMonth,_iTopType,_iSecType);
		this.generData();
		var strViewHtml="<table class='gtttbtools'><tr><td colspan='4' class='gtttools'><table><tr><td>顶部时间刻度：</td><td>"+strTopTimeSel+"</td><td>底部时间刻度：</td><td>"+strSecTimeSel+"</td><td>&nbsp;&nbsp;计划进度:&nbsp;&nbsp;&nbsp;&nbsp;</td><td><div style='background:url("+this.strGreesGround+");width:100px;height:12px;border:1px solid blue;'></div></td><td>&nbsp;&nbsp;实际进度:&nbsp;&nbsp;&nbsp;&nbsp;</td><td><div style='background:url("+this.strGreesGround_Green+");width:100px;height:12px;border:1px solid blue;'></div></td><td>&nbsp;&nbsp;延时进度:&nbsp;&nbsp;&nbsp;&nbsp;</td><td><div style='background:url("+this.strGreesGround_Red+");width:100px;height:12px;border:1px solid blue;'></div></td><td>&nbsp;&nbsp;当前时间:&nbsp;&nbsp;&nbsp;&nbsp;</td><td><div style='background:red;width:100px;height:2px;'></div></td></tr></table></td></tr>";
		strViewHtml+="<tr><td valign='top'><div id='divtasks' style='overflow:hidden;width:455px;'>";
		strViewHtml+=this.strTasks;
		strViewHtml+="</div></td><td onmouseover=\"this.style.background='url(images/content/splitover.png)';\" onmouseout=\"this.style.background='url(images/content/split.png)';\" style='background:url(images/content/split.png);width:8px;cursor:hand;' onclick='doHiddenTask(true)'></td><td valign='top'><div id='divgraphis' style='overflow:hidden;'>";
		strViewHtml+=this.strGraph;
		strViewHtml+="</div></td><td width='17' height='100%' valign='top'><DIV  onscroll='synTask();' style='Z-INDEX: 10;OVERFLOW-Y: auto; WIDTH: 17px; DISPLAY: block; HEIGHT: 100%;background:#f9f9f9; ' id='vbar'><DIV style='OVERFLOW-X: hidden; WIDTH: 1px; HEIGHT: 100%'>&nbsp;</DIV></div></td></tr></table>";
		objRander.innerHTML=strViewHtml;
		//var d2=new Date();
		
		var objTopOption=$$$("seltop");
		var objCTopOption=$$$("sectop");
		
		if(objTopOption!=null)
			document.body.removeChild(objTopOption);
		if(objCTopOption!=null)
			document.body.removeChild(objCTopOption);
		
		
		yltSelect.initSel("seltop");
		yltSelect.initSel("sectop");
		vbar.childNodes[0].style.height = (ylgtthead.offsetHeight+ylgttDataTb.offsetHeight)+"px";
		vbar.style.height=divgraphis.offsetHeight+"px"
		/*注册事件*/
	if(document.addEventListener){
		document.addEventListener('DOMMouseScroll',sytScrollGL,false);
	}//W3C
	window.onmousewheel=document.onmousewheel=sytScrollGL;//IE/Opera/Chrome
		//alert(" 字符串拼接方式耗时："+(d2.getTime()- d1.getTime())+"毫秒；");
	},
	initAuto:function(_strRandId,_iMinYear,_iMaxYear,_iTopType,_iSecType){
		var iScreen_Width = document.body.clientWidth-500;
		var iScreen_Height = document.body.clientHeight-80;
		this.init(_strRandId,iScreen_Width,iScreen_Height,_iMinYear,_iMaxYear,_iTopType,_iSecType);
		if($("sys_graph_1")!=null){
			var iGraphPosX=sys_graph_1.parentElement.getBoundingClientRect().left-472;
		
			ylgttData.scrollLeft=iGraphPosX;
		}

	}
};
function doHiddenTask(_bIsHidden){
	if(divtasks.style.display==""){
		divtasks.style.display="none";
		ylgtthead.style.width=yltGtt.iGraphWidth+450+"px";
		ylgttData.style.width=yltGtt.iGraphWidth+450+"px";
	}else{
		divtasks.style.display="";
		ylgtthead.style.width=yltGtt.iGraphWidth+"px";
		ylgttData.style.width=yltGtt.iGraphWidth+"px";
	}

}
function sytScrollGL(_e){
	var e=_e||window.event;
		if(e.wheelDelta<=0 || e.detail>0){
			vbar.scrollTop += 18;
		}else {
			vbar.scrollTop -= 18;
		}
}
function $(_strId){
		return document.getElementById(_strId);
	}
function $p(_strId){
		return document.getElementById(_strId+"_panel");
	}
function $$(_strId){
		return $(_strId+"_$");
	}
function $$$(_strId){
		return $(_strId+"_$$");
	}
	function generElement(_strTaget,_strId,_iWidth,_iHeight){
		var bgObj=document.createElement(_strTaget);		
		bgObj.setAttribute('id',_strId);
		bgObj.style.width=_iWidth+"px";
		if(_iHeight!=0)
			bgObj.style.height=_iHeight+"px";
		return bgObj;
	}
	function synTask(){
		var iScrollTop=vbar.scrollTop+10;
		if(iScrollTop<=10)
			iScrollTop=0;
		ylgttData.scrollTop=iScrollTop;
		ylgttTaskData.scrollTop=iScrollTop;
	}
	function synGraphDataHead(){
		ylgtthead.scrollLeft=ylgttData.scrollLeft;
	}