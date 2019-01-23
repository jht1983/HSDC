var  obj_sys_WorkData=new Object();
var strDataScript=do_sys_ajax("sys_type=grrc&sys_grrc_ottype=1&uid="+strUid,"comp");
if(strDataScript!="");
	eval(strDataScript);

var ylt= ylt ||{};
ylt.WorkDay = ylt.WorkDay || {};
	
var yltWorkDay=ylt.WorkDay={
	iType:3,
	strWeeks:"一二三四五六日",
	iCurYear:0,iCurMonth:0,iCurDay:0,
	arrStrWeekDays:new Array(),
	iTimeHeight:60,
	strDaysHtml:"<table width='100%'>",
	strEvent:"onmousemove='sys_work_Data_moseMove(this);' onmousedown='sys_work_Data_moseDown(this);'",
	strWorkDataStart:"><b class='b1'></b><b class='b2'></b><b class='b3'></b><b class='b4'></b><div class='content'><h3>",
	strWorkDataEnd:"</div></div><b class='b5'  "+"onmousemove='sys_work_Data_moseMove(this);' onmousedown='sys_work_Data_moseDown(this);'"+"></b><b class='b6' "+"onmousemove='sys_work_Data_moseMove(this);' onmousedown='sys_work_Data_moseDown(this);'"+"></b><b class='b7' "+"onmousemove='sys_work_Data_moseMove(this);' onmousedown='sys_work_Data_moseDown(this);'"+"></b><b class='b8' "+"onmousemove='sys_work_Data_moseMove(this);' onmousedown='sys_work_Data_moseDown(this);'"+"></b></div>",
	objParStyle:null,
	clickStyle:function(_obj,_strStyle){
			if(this.objParStyle!=null)
				this.objParStyle.style.border="";
			_obj.style.border="1px solid gray";
			this.objParStyle=_obj;
			workstyle.value=_strStyle+"style";
	},
	getDayOther:function (_strCurDate,_dayCur,_addDays){     
		var dayOther = new Date(_strCurDate);
		
		dayOther.setDate(_dayCur.getDate()+_addDays);
		var i_Year=dayOther.getFullYear();
		var i_Month=dayOther.getMonth()+1;
		var i_Day=dayOther.getDate();
		var strMonth,strDay;
		i_Day<10?strDay="0"+i_Day:strDay=""+i_Day;
		i_Month<10?strMonth="0"+i_Month:strMonth=""+i_Month;
		return i_Year+"-"+strMonth+"-"+strDay;
	},
	getDayMonth:function(_strCurDate,_dayCur,_addDays){
		var dayOther = new Date(_strCurDate);
		dayOther.setDate(_dayCur.getDate()+_addDays);
		return dayOther.getMonth()+1;
	},
	getDayWeeks:function(_iYear,_iMonth,_iDay){
		var strMonth,strDay;
		_iDay<10?strDay="0"+_iDay:strDay=""+_iDay;
		_iMonth<10?strMonth="0"+_iMonth:strMonth=""+_iMonth;
		
		if(this.iType==1){
			this.arrStrWeekDays.push(_iYear+"-"+strMonth+"-"+strDay);
		}else{
			var sCurDate = _iYear+"/"+strMonth+"/"+strDay;
			
			var dayCurDate=new Date(sCurDate);
			
			var iCurWeek=dayCurDate.getDay();
			
			if(iCurWeek<=0)iCurWeek=7;
		
				var iStartDay=1-iCurWeek;
				var iEndDay=iStartDay+7;
				
				for(var i=iStartDay;i<iEndDay;i++)
					this.arrStrWeekDays.push(this.getDayOther(sCurDate,dayCurDate,i));
				}
	},
	
	getWorDayData:function(_strData,_iTime){
		var vResult="";
		var arrData=obj_sys_WorkData[_strData];
		if(arrData==null)
			return "";
		var strTime;
		_iTime<10?strTime="0"+_iTime+":00":strTime=_iTime+":00";
		var iDataLength=arrData.length;
		for(var i=0;i<iDataLength;i++){
			if(arrData[i][0]==strTime){
				var strBgColor=arrData[i][2];
				var strDayData=arrData[i][1];
				var strHeight=arrData[i][3];
				var strId=arrData[i][4];
				
				
				vResult+="<div sdate='"+_strData+"' idataindex='"+i+"' sid='"+strId+"' ondblclick=\"doUpdateWork(this,'"+_strData+"',"+i+");\" iHeight='"+strHeight+"' class='sharp "+strBgColor+"'  style='width:100%;Z-index:100;position:absolute  ;height:100%;border:0px solid #ececec;' "+this.strWorkDataStart+
				strTime+"--<font id='lableend"+strId+"'>"+strHeight+"</font></h3><div style='overflow:hidden;' id='contentdiv"+strId+"'><table  width='100%'><tr><td valign='top' id='content"+strId+"'>"+
				strDayData+
				"</td></tr></table>"+
				this.strWorkDataEnd;
			}
		}
		
		
		return vResult;
	},
	generDayWork:function(_iType){
		for(var i=0;i<=24;i++){
			var strTime;
			i<10?strTime="0"+i:strTime=""+i;
			this.strDaysHtml+="<tr><td class='td1' style='height:"+this.iTimeHeight+"px;' id='sys_time_"+i+"'>"+strTime+":00</td>";
			for(var j=0;j<_iType;j++){			
				var strDayData=this.getWorDayData(this.arrStrWeekDays[j],i);
				this.strDaysHtml+="<td  ondblclick='doAddWork(this,"+i+","+j+")' autor='yulongtao'  class='td1' onmousemove='sys_work_Data_td_moseMove(this);' onmouseup='sys_work_Data_moseUp(this);' valign='top'>"+
				//"<div  onclick='doAddWork(this,"+i+","+j+")' autor='yulongtao' style='position:absolute;width:100%;height:200;z-index:10;background-color:yellow;'>"+
				
				strDayData+
				//"</div></td>";
				"</td>";
			}
			this.strDaysHtml+="</tr>";
		}
	},
	getWorkData:function(_strDate){
		var vResult="";
		var arrData=obj_sys_WorkData[_strDate];
		if(arrData==null)
			return "";
		
		var iLength=arrData.length;
		var strOtherWork="";
		if(iLength>4){
			strOtherWork="后"+(iLength-3)+"条";
			iLength=3;
		}
		for(var i=0;i<iLength;i++){
			var strBgColor=arrData[i][2];
			vResult+="<div ondblclick=\"doUpdateWork(this,'"+_strDate+"',"+i+")\" style='margin-top:2px;background-image:url(images/miniwin/"+strBgColor+".gif);width:100%;height:15;overflow:hidden;'>"+arrData[i][1]+"</div>";
		}
		if(strOtherWork!="")
			vResult+="<div style='margin-top:2px;text-align:center;'>"+strOtherWork+"</div>";
		return vResult;
	},
	generMonthWork:function(_iYear,_iMonth,_iDay){
		var strMonth,strDay="01";
		_iMonth<10?strMonth="0"+_iMonth:strMonth=""+_iMonth;
		var sCurDate = _iYear+"/"+strMonth+"/"+strDay;
		var dayCurDate=new Date(sCurDate);
		var iCurWeek=dayCurDate.getDay();
		if(iCurWeek<=0)iCurWeek=7;
		var iStartDay=1-iCurWeek;
		for(var i=0;i<5;i++){
			var strTime;
			this.strDaysHtml+="<tr>";
			for(var j=0;j<7;j++){
				var strDoDate=this.getDayOther(sCurDate,dayCurDate,iStartDay);
				this.strDaysHtml+="<td ondblclick=\"doAddWork(this,0,'"+strDoDate+"');\" autor='yulongtao' class='td1' style='height:130px;' valign='top'><div class='yltworkdatemonth'><a>"+
				strDoDate
				+"</a></div>"+this.getWorkData(strDoDate)+"</td>";
				iStartDay++;
			}
			this.strDaysHtml+="</tr>";
		}
		if(this.getDayMonth(sCurDate,dayCurDate,iStartDay)==_iMonth){
			this.strDaysHtml+="<tr>";
			for(var j=0;j<7;j++){
				var strDoDate=this.getDayOther(sCurDate,dayCurDate,iStartDay);
				this.strDaysHtml+="<td class='td1'><div style='width:100;height:130px;overflow:hidden;'><div class='yltworkdatemonth'>"+
				strDoDate
				+"</div>"+this.getWorkData(strDoDate)+"</div></td>";
				iStartDay++;
			}
			this.strDaysHtml+="</tr>";
		}
	},
	addDays:function(_iStep){
		var strMonth,strDay;
		this.iCurDay<10?strDay="0"+this.iCurDay:strDay=""+this.iCurDay;
		this.iCurMonth<10?strMonth="0"+this.iCurMonth:strMonth=""+this.iCurMonth;
		var sCurDate = this.iCurYear+"/"+this.iCurMonth+"/"+this.iCurDay;
		var dayCurDate=new Date(sCurDate);
						
		dayCurDate.setDate(dayCurDate.getDate()+_iStep);
		this.iCurYear=dayCurDate.getFullYear();
		this.iCurMonth=dayCurDate.getMonth()+1;
		this.iCurDay=dayCurDate.getDate();
	},
	preNext:function(_iStep){
		switch(this.iType){
			case 1:
						this.addDays(_iStep);
				break;
			case 2:
				this.addDays(_iStep*7);
				break;
			case 3:
				this.iCurMonth+=_iStep;
				if(this.iCurMonth>12){
					this.iCurMonth=1;
					this.iCurYear++;
				}
				if(this.iCurMonth<=0){
					this.iCurMonth=12;
					this.iCurYear--;
				}
				break;
		}
		this.draw(this.iType,this.iCurYear,this.iCurMonth,this.iCurDay);
	},
	tableCellOffsetY:new Object(),
	resetWorkDataCellSize:function(_objCell,_iRowIndex,_iColIndex,_objRows){
		
		var objCell=_objCell;//.childNodes[0];
				var arrChildNodes=objCell.childNodes;
				var iChildNodes=arrChildNodes.length;
				
				var iStartLeftPosX=0;//初始偏移位置
				var arrIParentOffsetY=this.tableCellOffsetY[_iColIndex+""];
				
		/**		
				if(_iRowIndex==9){
				
				if(arrIParentOffsetY!=null){
					var iParentOffSetLength=arrIParentOffsetY.length;
					for(var i=0;i<iParentOffSetLength;i++)
						alert(_iRowIndex+"<="+arrIParentOffsetY[i][0]);
				}
				}
	**/			
				if(arrIParentOffsetY!=null){
					var iParentOffSetLength=arrIParentOffsetY.length;
					for(var i=0;i<iParentOffSetLength;i++)
						if(arrIParentOffsetY[i][2]<_iRowIndex)
						if(_iRowIndex<=(arrIParentOffsetY[i][0]+1)){//如果在父元素覆盖区间,时间和行之间差1
						//alert(_iRowIndex+"<="+arrIParentOffsetY[i][0]+":"+arrIParentOffsetY[i][1]+">"+iStartLeftPosX);
							if(arrIParentOffsetY[i][1]>iStartLeftPosX){
								
								iStartLeftPosX=arrIParentOffsetY[i][1];//寻找最大偏移
								
								}
						}	
				}else
					arrIParentOffsetY=new Array();
				
				
				
				
				if(iChildNodes>1||iStartLeftPosX!=0){
					var iCurCellWidth=objCell.offsetWidth-iStartLeftPosX;
					
				
					
					var iDataWidth=iCurCellWidth;
					
					if(iChildNodes!=0){
						iDataWidth=(iCurCellWidth-12)/(iChildNodes/2-0.5+1);//每一项的宽度
						}
					//alert(objCell.offsetWidth+"-"+iStartLeftPosX+"="+(objCell.offsetWidth-iStartLeftPosX));
					for(var k=0;k<iChildNodes;k++){
						
						var objChildNode=arrChildNodes[k];
					
				/**		if(k==(iChildNodes-1)&&k!=0)//如果是最后一个子元素并且有多个子元素扩充多于空间
							objChildNode.style.width=(iDataWidth+iDataWidth/2)+"px";
						else	**/
							objChildNode.style.width=iDataWidth+"px";
						if(k==0){
							objChildNode.style.marginLeft=iStartLeftPosX+"px";
							objChildNode.iMgLeft=iStartLeftPosX;
							//objChildNode.style.left=iStartLeftPosX+"px";
						}else{
							
							objChildNode.iMgLeft=arrChildNodes[k-1].iMgLeft+iDataWidth/2;
							objChildNode.style.marginLeft=objChildNode.iMgLeft+"px";
							
							
							//objChildNode.style.left=(arrChildNodes[k-1].offsetLeft+iDataWidth/2)+"px";
							}
					
						var strToCellTime=objChildNode.attributes.iHeight.value.split(":")[0];
					if(strToCellTime.charAt(0)=='0')
						strToCellTime=strToCellTime.charAt(1);
					var iToCellIndex=parseInt(strToCellTime);
					
					
						arrIParentOffsetY.push(	[iToCellIndex,(objChildNode.iMgLeft+objChildNode.offsetWidth/3),_iRowIndex]);
						//alert(objChildNode.offsetLeft+objChildNode.offsetWidth/2);
					
					
						var iWorDataDivHeight=(_objRows[iToCellIndex+1].cells[_iColIndex].offsetTop-_objCell.offsetTop+_objCell.offsetHeight-12);
						objChildNode.style.height=iWorDataDivHeight+"px";
						document.getElementById("contentdiv"+objChildNode.attributes.sid.value).style.height=(iWorDataDivHeight-30)+"px";
						
						//document.getElementById("lableend"+objChildNode.attributes.sid.value).innerText=_iRowIndex+"偏移:"+(objChildNode.offsetWidth/3);
			
					}
				}else if(iChildNodes==1){
					var objChildNode=arrChildNodes[0];
					
					
				
							objChildNode.style.marginLeft=iStartLeftPosX+"px";
							objChildNode.iMgLeft=iStartLeftPosX;
						
				//	alert(objChildNode.style.marginLeft+"::::::::::"+iStartLeftPosX);
					
				
					var strToCellTime=objChildNode.attributes.iHeight.value.split(":")[0];
					if(strToCellTime.charAt(0)=='0')
						strToCellTime=strToCellTime.charAt(1);
					var iToCellIndex=parseInt(strToCellTime);
					
					
					//document.getElementById("lableend"+objChildNode.attributes.sid.value).innerText=_iRowIndex+"偏移:"+(objChildNode.offsetWidth/3)+"到达行"+iToCellIndex;
					
					arrIParentOffsetY.push(	[iToCellIndex,(0+objChildNode.offsetWidth/3),_iRowIndex]);
				
					var iWorDataDivHeight=(_objRows[iToCellIndex+1].cells[_iColIndex].offsetTop-_objCell.offsetTop+_objCell.offsetHeight-12);
					objChildNode.style.height=iWorDataDivHeight+"px";
					document.getElementById("contentdiv"+objChildNode.attributes.sid.value).style.height=(iWorDataDivHeight-30)+"px";
					
					var iDivContentWidth=_objCell.offsetWidth-10;
					objChildNode.style.width=iDivContentWidth+"px";
					document.getElementById("contentdiv"+objChildNode.attributes.sid.value).style.width=iDivContentWidth+"px";
					
					}
					
				this.tableCellOffsetY[_iColIndex+""]=arrIParentOffsetY;
	
	},
	resetWorkDataSize:function(_iType){
		this.tableCellOffsetY=new Object();
		var objWorkTable=document.getElementById("sys_work_data_table");
		var arrObjRows=objWorkTable.rows;
		for(var i=1;i<26;i++){
			var objCells=arrObjRows[i].cells;
			for(var j=1;j<_iType;j++){
				this.resetWorkDataCellSize(objCells[j],i,j,arrObjRows);
			}
		}
	},
	sys_synVScroll:function (){
		var iScrollTop=vbar.scrollTop+25;
			if(iScrollTop<=25)
				iScrollTop=0;
			document.getElementById("tablediv").scrollTop=iScrollTop;
	},
	drawCur:function(_iType){
		this.draw(_iType,this.iCurYear,this.iCurMonth,this.iCurDay);
		var objDefaultTime=document.getElementById("sys_time_7");
		if(objDefaultTime!=null){
			//alert(document.getElementById("tablediv"));
			var iScollInitTop=objDefaultTime.getBoundingClientRect().top;
			document.getElementById("vbar").scrollTop=iScollInitTop;
			document.getElementById("tablediv").scrollTop=iScollInitTop;
		}
	},
	drawCurDate:function(_iType){
		var dayCurDate=new Date();
		this.iCurYear=dayCurDate.getFullYear();
		this.iCurMonth=dayCurDate.getMonth()+1;
		this.iCurDay=dayCurDate.getDate();
		this.draw(_iType,this.iCurYear,this.iCurMonth,this.iCurDay);
		var objDefaultTime=document.getElementById("sys_time_7");
		if(objDefaultTime!=null){
			//alert(document.getElementById("tablediv"));
			var iScollInitTop=objDefaultTime.getBoundingClientRect().top;
			document.getElementById("vbar").scrollTop=iScollInitTop;
			document.getElementById("tablediv").scrollTop=iScollInitTop;
		}
	},
	strUid:'',
	drawCurDateByDate:function(_iType,_iCurYear,_iCurMonth,_iCurDay,_strUid){
		var dayCurDate=new Date();
		this.iCurYear=_iCurYear;
		this.iCurMonth=_iCurMonth;
		this.iCurDay=_iCurDay;
		this.strUid=_strUid;
		
		this.draw(_iType,this.iCurYear,this.iCurMonth,this.iCurDay);
		var objDefaultTime=document.getElementById("sys_time_7");
		if(objDefaultTime!=null){
			//alert(document.getElementById("tablediv"));
			var iScollInitTop=objDefaultTime.getBoundingClientRect().top;
			document.getElementById("vbar").scrollTop=iScollInitTop;
			document.getElementById("tablediv").scrollTop=iScollInitTop;
		}
	},
	draw:function(_iType,_iYear,_iMonth,_iDay){
		
		tdworkpanel.innerHTML="";
		popwindialog.style.display="none";
		popwindialogarow.style.display="none";
		this.iType=_iType;
		this.iCurYear=_iYear;
		this.iCurMonth=_iMonth;
		this.iCurDay=_iDay;
		var iClientOpHeight=document.body.clientHeight-70;
		switch(this.iType){
			case 1:
				this.arrStrWeekDays=new Array();
				this.getDayWeeks(_iYear,_iMonth,_iDay);				
				this.strDaysHtml="<table border='0' width='100%' border='0' cellpadding='0' cellspacing='0'>"+
											"<tr><td id='sys_datatd'><div id='titlediv' style='position:relative;overflow:hidden;'>"+
											"<table width='100%' border='0' class='table1'  style='table-layout:fixed;'>";
				
				var strTableHead="<tr><th class='th1' style='width:71px;'>时间</th>";
				var strTableTrueHead="<tr><th class='th1' style='width:71px;height:0px;line-height:0px;overflow:hidden;text-overflow:ellipsis;word-break:keep-all;white-space:nowrap;'>时间</th>";
				
				
				var  strDay,strMonth;
				_iDay<10?strDay="0"+_iDay:strDay=""+_iDay;
				_iMonth<10?strMonth="0"+_iMonth:strMonth=""+_iMonth;
		
				var sCurDate = _iYear+"/"+strMonth+"/"+strDay;
				var dayCurDate=new Date(sCurDate);
				var iCurWeek=dayCurDate.getDay();
				if(iCurWeek<=0)iCurWeek=6;else iCurWeek--;
				
				
				for(var i=0;i<1;i++){
					strTableHead+="<th class='th1' style='text-align:center;'>"+this.arrStrWeekDays[i]+"(周"+this.strWeeks.charAt(iCurWeek)+")</th>";
					strTableTrueHead+="<th class='th1' style='text-align:center;height:0px;line-height:0px;overflow:hidden;text-overflow:ellipsis;word-break:keep-all;white-space:nowrap;'>"+this.arrStrWeekDays[i]+"(周"+this.strWeeks.charAt(i)+")</th>";
				}
				strTableHead+="</tr>";
				strTableTrueHead+="</tr>";
				
				
				this.strDaysHtml+=strTableHead+"</table></div>";
				
				this.strDaysHtml+="<div id='tablediv' style='position:relative;overflow:hidden;height:"+iClientOpHeight+"px;'>"+
				"<table width='100%' border='0' class='table1' id='sys_work_data_table'  style='table-layout:fixed;'>";
			
				this.strDaysHtml+=strTableTrueHead;
			
				this.generDayWork(1);
				this.strDaysHtml+="</table>";
				
				this.strDaysHtml+="</div></td><td height='"+iClientOpHeight+"px'><div id='vbar' style='width:18px;height:100%;DISPLAY: block; OVERFLOW-Y: auto;'   onscroll='yltWorkDay.sys_synVScroll();' ><div style='height:1525px;width:1px;'></div></div></td></tr></table>";
				
				tdworkpanel.innerHTML=this.strDaysHtml;
				yltWorkDay.tableCellOffsetY[1+""]=new Array();

			var objWorkTable=document.getElementById("sys_work_data_table");
			for(var i=1;i<26;i++){
				var arrObjRows=objWorkTable.rows;
				yltWorkDay.resetWorkDataCellSize(arrObjRows[i].cells[1],i,1,arrObjRows);
			}
				break;
			case 2:
				this.arrStrWeekDays=new Array();
				this.getDayWeeks(_iYear,_iMonth,_iDay);
				
				
				
				this.strDaysHtml="<table border='0' width='100%' border='0' cellpadding='0' cellspacing='0'>"+
											"<tr><td id='sys_datatd'><div id='titlediv' style='position:relative;overflow:hidden;'>"+
											"<table width='100%' border='0' class='table1'  style='table-layout:fixed;'>";
				
				var strTableHead="<tr><th class='th1' style='width:71px;'>时间</th>";
				var strTableTrueHead="<tr><th class='th1' style='width:71px;height:0px;line-height:0px;overflow:hidden;text-overflow:ellipsis;word-break:keep-all;white-space:nowrap;'>时间</th>";
				
				
				
				
				for(var i=0;i<7;i++){
					strTableHead+="<th class='th1' style='text-align:center;'>"+this.arrStrWeekDays[i]+"(周"+this.strWeeks.charAt(i)+")</th>";
					strTableTrueHead+="<th class='th1' style='text-align:center;height:0px;line-height:0px;overflow:hidden;text-overflow:ellipsis;word-break:keep-all;white-space:nowrap;'>"+this.arrStrWeekDays[i]+"(周"+this.strWeeks.charAt(i)+")</th>";
					}
				strTableHead+="</tr>";
				strTableTrueHead+="</tr>";
				
				
				this.strDaysHtml+=strTableHead+"</table></div>";
				this.strDaysHtml+="<div id='tablediv' style='position:relative;overflow:hidden;height:"+iClientOpHeight+"px;'>"+
				"<table width='100%' border='0' class='table1' id='sys_work_data_table'  style='table-layout:fixed;'>";
			
				this.strDaysHtml+=strTableTrueHead;
				
				
				
				
				this.generDayWork(7);
				this.strDaysHtml+="</table>";
				
				this.strDaysHtml+="</div></td><td height='"+iClientOpHeight+"px'><div id='vbar' style='width:18px;height:100%;DISPLAY: block; OVERFLOW-Y: auto;'   onscroll='yltWorkDay.sys_synVScroll();' ><div style='height:1525px;width:1px;'></div></div></td></tr></table>";
				
				tdworkpanel.innerHTML=this.strDaysHtml;
				this.resetWorkDataSize(8);
				break;
			case 3:
				this.strDaysHtml="<table border='0' width='100%' border='0' cellpadding='0' cellspacing='0'>"+
											"<tr><td id='sys_datatd'>";
				this.strDaysHtml+="<div id='tablediv' style='position:relative;overflow:hidden;height:"+iClientOpHeight+"px;'><table width='100%' border='0' class='table1' style='table-layout:fixed;'><tr>";
				for(var i=0;i<7;i++)
					this.strDaysHtml+="<th class='th1' style='text-align:center;'>周"+this.strWeeks.charAt(i)+"</th>";
				this.generMonthWork(_iYear,_iMonth,_iDay);
				this.strDaysHtml+="</table></div></td>";
				this.strDaysHtml+="<td height='"+iClientOpHeight+"px'><div id='vbar' style='width:18px;height:100%;DISPLAY: block; OVERFLOW-Y: auto;'   onscroll='yltWorkDay.sys_synVScroll();' ><div style='height:650px;width:1px;'></div></div></td></tr></table>";
				tdworkpanel.innerHTML=this.strDaysHtml;
				break;
		}
	},
	generId:function(){
		return new Date().getTime();
	},
	clear:function(){
		if(bSYS_ISUPDATE){
			var strParams="id="+strSYS_DataId;
			var vResult=do_sys_ajax(strParams,"comp?sys_type=grrc&sys_grrc_ottype=4&uid="+this.strUid);
				if(vResult=="ok"){
					obj_cur_workData_ContentDiv.parentElement.removeChild(obj_cur_workData_ContentDiv);
				}else
					alert("清除失败！");
		
		}
			popwindialog.style.display="none";
			popwindialogarow.style.display="none";
	},
	save:function(){
	
		if(bSYS_ISUPDATE){
			var strGZSX=ssxm.options[ssxm.selectedIndex].text+":"+sgzsx.options[sgzsx.selectedIndex].text;	
			var strParams="id="+strSYS_DataId+"&SSTYLE="+workstyle.value+"&SSXM="+ssxm.value+"&ZCBF="+zcbf.value+"&SGZSX="+sgzsx.value+"&SCONTENT="+strGZSX;
				var vResult=do_sys_ajax(strParams,"comp?sys_type=grrc&sys_grrc_ottype=2&uid="+this.strUid);
				if(vResult=="ok"){
					
					arr_curObjWorkData[1]=strGZSX;
					arr_curObjWorkData[2]=workstyle.value;
					
					arr_curObjWorkData[5]=ssxm.value;
					arr_curObjWorkData[6]=zcbf.value;
					arr_curObjWorkData[7]=sgzsx.value;
					
		
					
					
					
					if(this.iType==3){//按月
						obj_cur_workData_ContentDiv.style.backgroundImage="url(images/miniwin/"+workstyle.value+".gif)";
						obj_cur_workData_ContentDiv.innerHTML=strGZSX;
					}else{
						obj_cur_workData_ContentDiv.className=workstyle.value;
						document.getElementById("content"+strSYS_DataId).innerHTML=strGZSX;
					}
					
				}else
					alert("保存失败！");
		
		}else{
				var strId=this.generId();	
				var strGZSX=ssxm.options[ssxm.selectedIndex].text+":"+sgzsx.options[sgzsx.selectedIndex].text;	
				var strParams="id="+strId+"&SDATE="+workdate.value+"&SSTARTTIME="+starttime.value+"&SENDTIME="+endtime.value+"&SSTYLE="+workstyle.value+
				"&SSXM="+ssxm.value+"&ZCBF="+zcbf.value+"&SGZSX="+sgzsx.value+"&SCONTENT="+strGZSX;
				var vResult=do_sys_ajax(strParams,"comp?sys_type=grrc&sys_grrc_ottype=0&uid="+this.strUid);
				if(vResult=="ok"){
							
					var strBgColor=workstyle.value;
					
					if(obj_sys_WorkData[workdate.value]==null)
						obj_sys_WorkData[workdate.value]=[];
					obj_sys_WorkData[workdate.value].push([starttime.value,strGZSX,workstyle.value,endtime.value,strId,ssxm.value,zcbf.value,sgzsx.value]);
					
				
					
					var iWorkDataIndex=obj_sys_WorkData[workdate.value].length-1;
					
					if(this.iType==3){//按月
						obj_cur_workData_ContentDiv.innerHTML="<div class='yltworkdatemonth'><a>"+
						workdate.value+"</a></div>"+this.getWorkData(workdate.value);
					}else{
						obj_cur_workData_ContentDiv.innerHTML+="<div  sdate='"+workdate.value+"' idataindex='"+iWorkDataIndex+"' sid='"+strId+"' ondblclick=\"doUpdateWork(this,'"+workdate.value+"',"+iWorkDataIndex+");\" iHeight='"+starttime.value+"' class='sharp "+strBgColor+"'  style='width:100%;Z-index:100;position:absolute  ;height:100%;border:0px solid #ececec;' "+this.strWorkDataStart+
						starttime.value+"--<font id='lableend"+strId+"'>"+endtime.value+"</font></h3><div style='overflow:hidden;' id='contentdiv"+strId+"'><table  width='100%'><tr><td valign='top' id='content"+strId+"'>"+					
						strGZSX+"</td></tr></table>"+
						this.strWorkDataEnd;
					
						var objWorkTable=document.getElementById("sys_work_data_table");
						for(var i=1;i<26;i++){
							var arrObjRows=objWorkTable.rows;
							yltWorkDay.resetWorkDataCellSize(arrObjRows[i].cells[obj_cur_workData_ContentDiv.cellIndex],i,obj_cur_workData_ContentDiv.cellIndex,arrObjRows);
						}
					}
				
				}else
					alert("保存失败！");	
		}
				popwindialog.style.display="none";
				popwindialogarow.style.display="none";
	}
};
function get_sysAjaxActive(){
	var xmlHttp;
 if (window.ActiveXObject) { 
  xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
 } 
 else if (window.XMLHttpRequest) { 
  xmlHttp = new XMLHttpRequest();
 }
 return xmlHttp;
}
function do_sys_ajax(param,aStrUrl){
		var xml=get_sysAjaxActive();
		xml.open("POST",aStrUrl,false);
		//xml.setRequestHeader("content-length",param.length);  
		xml.setRequestHeader("content-type","application/x-www-form-urlencoded");  
		xml.setRequestHeader("charset", "UTF-8");
		xml.send(param);  
		var res = xml.responseText;
		return res;
	}
var isMSIE = (navigator.appName == "Microsoft Internet Explorer");
  function sys_work_Data_moseMove(obj){
		var e=event;
		
		var px = isMSIE?e.offsetY:e.layerY-obj.offsetTop;
		
		//obj.childNodes[4].innerHTML=px+":::"+obj.offsetHeight;
		
		if(px>obj.offsetHeight-20 && px<obj.offsetHeight){
			
			obj.style.cursor = "row-resize";
		}else{
			obj.style.cursor = "default";
		}
	}
	
	
var obj_sys_curDragWorkData=null;	
var obj_sys_Capture_curDragWorkData=null;		
function sys_work_Data_moseDown(obj){
		var e=event;
		var px = isMSIE?e.offsetY:e.layerY-obj.offsetTop;
		if(px>obj.offsetHeight-6 && px<obj.offsetHeight){
			e=e||window.event;
			obj=obj||this;
			obj_sys_curDragWorkData= obj.parentElement;
			obj_sys_Capture_curDragWorkData=obj;
			if(obj.setCapture){
				obj.setCapture();
			}else{
				e.preventDefault();
			}
		}
	}
	
	function sys_work_Data_moseUp(_obj){
		if(obj_sys_Capture_curDragWorkData!=null){
			if(obj_sys_Capture_curDragWorkData.releaseCapture){
				obj_sys_Capture_curDragWorkData.releaseCapture();
			}
			var iColIndex=_obj.cellIndex;
	
			yltWorkDay.tableCellOffsetY[iColIndex+""]=new Array();

			var objWorkTable=document.getElementById("sys_work_data_table");
			for(var i=1;i<26;i++){
				var arrObjRows=objWorkTable.rows;
				yltWorkDay.resetWorkDataCellSize(arrObjRows[i].cells[iColIndex],i,iColIndex,arrObjRows);
			}
			
			
			var vResult=do_sys_ajax("id="+obj_sys_curDragWorkData.attributes.sid.value+"&SENDTIME="+obj_sys_curDragWorkData.attributes.iHeight.value,"comp?sys_type=grrc&sys_grrc_ottype=2&uid="+strUid);
		
		
			var arrObjWorkData=obj_sys_WorkData[obj_sys_curDragWorkData.attributes.sdate.value][parseInt(obj_sys_curDragWorkData.attributes.idataindex.value)];
			arrObjWorkData[3]=obj_sys_curDragWorkData.attributes.iHeight.value;
		
			obj_sys_curDragWorkData = null;
			obj_sys_Capture_curDragWorkData=null;
		}
	}
  function sys_work_Data_td_moseMove(_obj){
  //_obj.style.backgroundColor='red';
		if(obj_sys_curDragWorkData!=null){
			var objStartTd=obj_sys_curDragWorkData.parentElement;
			var iStartY=objStartTd.offsetTop;
			var iHeight=event.clientY+document.getElementById("tablediv").scrollTop-iStartY-70;//
			
			if(iHeight>45){
				obj_sys_curDragWorkData.style.height=iHeight+"px";
				document.getElementById("contentdiv"+obj_sys_curDragWorkData.attributes.sid.value).style.height=(iHeight-30)+"px";
				
				var iToCellCount=iHeight/yltWorkDay.iTimeHeight-1;
				if(iToCellCount<=0)iToCellCount=-1;
				iToCell=parseInt(iToCellCount)+objStartTd.parentElement.rowIndex;
				
				//obj_sys_curDragWorkData.childNodes[4].childNodes[0].innerHTML=(iHeight/60-1)+":"+iToCell+":"+objStartTd.parentElement.rowIndex;
				
				var strEndTime;
				if(iToCell<10)
					strEndTime="0"+iToCell+":00";					
				else
					strEndTime=iToCell+":00";
				
				obj_sys_curDragWorkData.attributes.iHeight.value=strEndTime;
				
			document.getElementById("lableend"+obj_sys_curDragWorkData.attributes.sid.value).innerText=strEndTime;	
				
			}
		}
	}
	var iCurClient_body_Width=document.body.clientWidth;
	var iCurClient_body_Height=document.body.clientHeight;
	var obj_cur_workData_ContentDiv=null;var i_cur_workData_Row=0;var i_cur_workData_Col=0;
	var bSYS_ISUPDATE=false;
	var strSYS_DataId="";
	var arr_curObjWorkData=null;
	
	//工作事项div,日期,当天的第几条数据
	function doUpdateWork(_obj,_strDate,_iDataCol){
	
			bSYS_ISUPDATE=true;
		
			var arrObjWorkData=obj_sys_WorkData[_strDate][_iDataCol];
			arr_curObjWorkData=arrObjWorkData;
			
			
			sys_setPotWinPos(_obj,event);
			
		
			tdtime.innerHTML=arrObjWorkData[0];
			
			
			ssxm.value=arrObjWorkData[5];
			zcbf.value=arrObjWorkData[6];
			sgzsx.value=arrObjWorkData[7];
			
			
			
			strSYS_DataId=arrObjWorkData[4];
			
			workstyle.value=arrObjWorkData[2];
			document.getElementById(arrObjWorkData[2]).style.border="1px solid green";
			
			obj_cur_workData_ContentDiv=_obj;
			
	}
	function sys_setPotWinPos(_obj,_event){
		popwindialog.style.display="";
		popwindialogarow.style.display="";
		
		var objPos=_obj.getBoundingClientRect();
		
		var iPosArowX=objPos.left+_event.offsetX;
		var iPosArowY=objPos.top+_event.offsetY-64;
		
		if(iPosArowX>(iCurClient_body_Width-90))
			iPosArowX=iCurClient_body_Width-90;
		
		popwindialogarow.style.top=iPosArowY+"px";
		popwindialogarow.style.left=iPosArowX+"px";
		
		
	
		
		var iPosWinX=iPosArowX-200;
		if(iPosWinX<10)iPosWinX=10;
		
		var iRightX=iCurClient_body_Width-iPosWinX-600;
		if(iRightX<0)
			iPosWinX=iPosWinX+iRightX;
		
		
		popwindialog.style.left=iPosWinX+"px";
		
		var iDialogHeight=popwindialog.clientHeight+64;
		
		if(iPosArowY<iDialogHeight){
			popwindialogarow.style.backgroundImage="url(images/miniwin/angletop.png)";
			popwindialogarow.style.top=(iPosArowY+64)+"px";
			if((iPosArowX-86)<86)
				popwindialogarow.style.left="86px";
			else
				popwindialogarow.style.left=(iPosArowX-86)+"px";
			popwindialog.style.top=(objPos.top+63+_event.offsetY)+"px";
		}else{
			popwindialogarow.style.backgroundImage="url(images/miniwin/anglebottom.png)";
			popwindialog.style.top=(objPos.top+_event.offsetY-iDialogHeight)+"px";
		}
		popwindialog.style.zIndex=1000;
		popwindialogarow.style.zIndex=1001;
	}
		//工作事项的TD,_iRow+1行，列
	function doAddWork(_obj,_iRow,_iCol){
		//alert(event.srcElement.attributes.autor);
		if(event.srcElement.attributes.autor!=null){
			bSYS_ISUPDATE=false;
			var strTime;
			_iRow<10?strTime="0"+_iRow+":00":strTime=_iRow+":00";				
				
			if(yltWorkDay.iType==3)
				tdtime.innerHTML=_iCol+"  "+strTime;
			else
				tdtime.innerHTML=yltWorkDay.arrStrWeekDays[_iCol]+"  "+strTime;
			
	
			
			sys_setPotWinPos(_obj,event);

			workstyle.value="redstyle";
			redstyle.style.border="1px solid green";
			
			if(yltWorkDay.iType==3)
				workdate.value=_iCol;
			else
				workdate.value=yltWorkDay.arrStrWeekDays[_iCol];
			starttime.value=strTime;
			endtime.value=strTime;
			//scontent.value="";
			
			
			
			if(obj_cur_workData_ContentDiv!=null)
				obj_cur_workData_ContentDiv.style.backgroundColor="white";
			
			
			obj_cur_workData_ContentDiv=_obj;
			i_cur_workData_Row=_iRow;
			i_cur_workData_Col=_iCol;
			
			obj_cur_workData_ContentDiv.style.backgroundColor="yellow";
		}
			
	}