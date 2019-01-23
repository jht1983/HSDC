	var bIsMoveStart=false;
	var iStartTd={iCol:0,iRow:0,left:0,top:0}
	var iEndTd={iCol:0,iRow:0,left:0,top:0}
	var iStartRow=0;var iStartCol=0;
	var iEndRow=0;var iEndCol=0;
	
	/******************************/
	var textDrging=false;  
	var isDraging=false; //
	var isBorderDrag=false;//
	var isMouseDown=false;//
	var currentDragObj=null;//
	var currentWidth=0;//
	var currentHeight=0;//
	var currentMouseType="null";
	var posX,posY;
	var tools=parent;//
	var isQxMove=true;
	/*****************************/
	var oldHeadTd="undefined";
	/******************************/
	var strTdAttr;//=new Array("col","row","rowSpan","colSpan","className","bgColor","align","stype");
	var strTdStyle;//=new Array("fontFamily","fontSize");
	var	strDBName;//="ICOLSEQ,IROWSEQ,IROWSPAN,ICOLSPAN,SCLASS,SBGCOLOR,SALIGN,STYPE,SFONT,SFONTSIZE";
	/******************************/
	var objCurTd=null;//当前焦点单元格
	var iRow=50;
	var iCol=30;
	var sRepCode="";
	var iBatchRow=0;//批量行
	var arrBind=new Array();
	var bIsCopy=false;
	var objCopyTd=null;
	var objCopyBorder="";
function $(_strId){return document.getElementById(_strId);}
function getTx(param,aStrUrl){ 
		var xml = new ActiveXObject("Microsoft.XMLHTTP");
		xml.open("POST",aStrUrl,false);//以同步方式通信  
		xml.setrequestheader("content-length",param.length);  
		xml.setrequestheader("content-type","application/x-www-form-urlencoded");  
		xml.setRequestHeader("charset", "UTF-8");
		xml.send(param);  
		var res = xml.responseText;
		return res;
	}
var ylt= ylt ||{};
ylt.table = ylt.table || {};
var ylttable=ylt.table={
	bIsMoveStart:false,
	
	drawSelCells:function(){
		var obj=event.srcElement;
		if(obj.className!="headtd"&&(obj.tagName=="TD" || obj.tagName=="FONT"))	
			if(bIsMoveStart){
				if(obj.tagName=="FONT")
					obj=obj.parentElement;

				iEndTd.iCol=parseInt(obj.col);
				iEndTd.iRow=parseInt(obj.row);
				iEndTd.left=obj.offsetLeft;
				iEndTd.top=obj.offsetTop;			
				this.view();
			}		
	},
	contentDown:function(){
		var obj=event.srcElement;
		if(obj.className!="headtd"&&(obj.tagName=="TD" || obj.tagName=="FONT")){
			if(obj.tagName=="FONT")
				obj=obj.parentElement;
			bIsMoveStart=true;
			iStartTd.iCol=parseInt(obj.col);
			iStartTd.iRow=parseInt(obj.row);
			iStartTd.left=obj.offsetLeft;
			iStartTd.top=obj.offsetTop;
		}
	},
	contentUp:function(){
		bIsMoveStart=false;
		var obj=event.srcElement;
	},
	mouseUp:function (){
		if(isDraging){
			if(currentMouseType=="TOP")
				currentDragObj.style.width=tools.xyvalue.value;
			else
				currentDragObj.style.height=tools.xyvalue.value;
		}
		currentMouseType="null"
		isMouseDown=false;
		document.body.style.cursor='';
		drLine.style.display="none";
		pixshow.style.display="none";
		isOverBorder=false;
		textDrging=false;
		this.setDrag();
		tdSelMouseDown=false;
	},
	setDrag:function(){
		if(currentDragObj==null)
			return;
		if(isBorderDrag&&isMouseDown){	
			isDraging=true;
		}else
			isDraging=false;
	},
	view:function(){
		var iWidth=0;var iHeight=0;

		if(iStartTd.left==iEndTd.left&&iStartTd.top==iEndTd.top)
			return;
		
		if(iStartTd.left<iEndTd.left){
			selTDDiv.style.left=iStartTd.left;
			iStartCol=iStartTd.iCol;///////////
			iEndCol=iEndTd.iCol;//////////
			iWidth=iEndTd.left-iStartTd.left;
		}else{
			selTDDiv.style.left=iEndTd.left;
			iStartCol=iEndTd.iCol;///////////
			iEndCol=iStartTd.iCol;//////////
			iWidth=iStartTd.left-iEndTd.left;
		}

		if(iStartTd.top<iEndTd.top){
			selTDDiv.style.top=iStartTd.top;
			iStartRow=iStartTd.iRow;///////////
			iEndRow=iEndTd.iRow;//////////
			iHeight=iEndTd.top-iStartTd.top;
		}else{
			selTDDiv.style.top=iEndTd.top;
			iStartRow=iEndTd.iRow;///////////
			iEndRow=iStartTd.iRow;//////////
			iHeight=iStartTd.top-iEndTd.top;
		}
		var objEnd=document.getElementById(iEndRow+"$"+iEndCol);
		selTDDiv.style.width=iWidth+objEnd.offsetWidth;
		selTDDiv.style.height=iHeight+objEnd.offsetHeight;
		
		selTDDiv.style.display="";
	},
	moveBorder:function (){

			var obj=event.srcElement;
			var  moveType=obj.movetype;
		if(currentMouseType=="null")
				currentMouseType=moveType;
			this.setMouse();
			if(isDraging){
				switch (currentMouseType) {
					case "TOP":
						
						tools.xyvalue.value=currentWidth+event.x-posX;

						drLine.style.left=event.x;
						drLine.style.top=0;
						drLine.style.width="1";
						drLine.style.height="800";
						drLine.style.display="";


						
						pixshow.style.left=event.x+10;
						pixshow.style.top=event.y+10;
						pixshow.innerHTML="width:"+(currentWidth+event.x-posX);
						pixshow.style.display="";



						break;
					case "LEFT":
						
						
						drLine.style.font="0px/0px sans-serif";
						drLine.style.height="1";
						drLine.style.width="1000";
						drLine.style.left=0;
						drLine.style.top=event.y;
						tools.xyvalue.value=event.y;
						tools.xyvalue.value=currentHeight+event.y-posY;
						
						drLine.style.display="";
						
						pixshow.style.left=event.x+20;
						pixshow.style.top=event.y+20;
						pixshow.innerHTML="height:"+(currentHeight+event.y-posY);
						pixshow.style.display="";


						break;
			
				default:
					tools.xyvalue.value=currentWidth+event.x-posX;

					drLine.style.left=event.x;
					drLine.style.top=38;
					drLine.style.display="";
				}
			}else{
				currentDragObj=obj;
				currentMouseType=moveType
				this.setDrag();
			}
		},
	copyTd:function(_objCurTd){
		_objCurTd.childNodes[0].innerText=objCopyTd.childNodes[0].innerText;
		
		_objCurTd.sdataset=objCopyTd.sdataset;
		_objCurTd.skzfs=objCopyTd.skzfs;
	
		
		
		
		
		
		objCopyTd.className=objCopyBorder;
		bIsCopy=false;
	},
	contentClick:function(){	
		this.clear();
		var obj=event.srcElement;
		objCurTd=obj;
		if(obj.className!="headtd"){
			if(obj.tagName=="TD"){
				obj.childNodes[0].focus();				
			}else
				objCurTd=obj.parentNode;
			
			//objCurTd.className="cttdselect";
			
			if(objCurTd.colSpan>1||objCurTd.rowSpan>1){
				parent.imghebing.border="1";
				parent.imghebing.alt="拆分";
			}else
				parent.imghebing.border="0";
			//objCurTd.style.background="#f7cace";
			//objCurTd.style.border="1px dashed red";
		}
		
		if(bIsCopy)
		this.copyTd(objCurTd);
		
		
		this.initCell();
	},
	contentDbClick:function(){
		this.clear();
		var obj=event.srcElement;
		objCurTd=obj;
		if(obj.className!="headtd"){
			if(obj.tagName=="TD"){
				obj.childNodes[0].focus();				
			}else
				objCurTd=obj.parentNode;
			
			//objCurTd.className="cttdselect";
			
			if(objCurTd.colSpan>1||objCurTd.rowSpan>1){
				parent.imghebing.border="1";
				parent.imghebing.alt="拆分";
			}else
				parent.imghebing.border="0";
			if(objCopyTd!=null){
				objCopyTd.className=objCopyBorder;
			}			
			bIsCopy=true;
			objCopyBorder=objCurTd.className;
			objCopyTd=objCurTd;
			objCurTd.className="cttdselect";
		}
		this.initCell();
	},	
	clear:function(){
		bIsMoveStart=false;
		selTDDiv.style.display="none";
	},	
	addBorder:function(){
		if(parent.imghebing.border=="1")
			this.unMerger();
		else
			this.doMerger();
	},
	doMerger:function(){
		if(selTDDiv.style.display=="none")
			return;
		var iRowSpan=0;
		var iColSpan=0;		
		var strStartCellId=iStartRow+"$"+iStartCol;
		var strEndCellId=iEndRow+"$"+iEndCol;
		var iInsertCol=document.getElementById(strStartCellId).cellIndex;
		var objEndCell=document.getElementById(strEndCellId);
		if(objEndCell.rowSpan>1)iEndRow=iEndRow+objEndCell.rowSpan-1;
		if(objEndCell.colSpan>1)iEndCol=iEndCol+objEndCell.colSpan-1;

		 for(i=iEndRow;i>=iStartRow;i--){
			 for(j=iEndCol;j>=iStartCol;j--){
				objTemp=document.getElementById(i+"$"+j);
				if(objTemp!=null){
					if(j==iStartCol)
						iRowSpan=iRowSpan+objTemp.rowSpan;
					if(i==iStartRow)
						iColSpan=iColSpan+objTemp.colSpan;
					dragtable.rows[i].removeChild(objTemp);
				}
			 }
				
		 }
		var theCell=dragtable.rows[iStartRow].insertCell(iInsertCol);
		theCell.id=strStartCellId;
		theCell.row=iStartRow;
		theCell.col=iStartCol;
		theCell.colSpan=iColSpan;
		theCell.rowSpan=iRowSpan;
		theCell.className="cttd";
		theCell.innerHTML="<font contenteditable=\"true\"></font>";
		theCell.align="center";
		this.clear();
	 },
	 unMerger:function(){
		var oTFoot = document.getElementById("dragtable");
		var temp;
		if(objCurTd!=null){
			var iCurRow=parseInt(objCurTd.row);
			var iCurCol=parseInt(objCurTd.col);
			var iRowSpan=parseInt(objCurTd.rowSpan);
			var iColSpan=parseInt(objCurTd.colSpan);
			var strClassName=objCurTd.className;
			oTFoot.rows[iCurRow].removeChild(objCurTd);
			for(var r=0;r<iRowSpan;r++)
				for(var c=0;c<iColSpan;c++){
					var iNewRow=iCurRow+r;
					var iNewCol=iCurCol+c;
					var theCell=oTFoot.rows[iNewRow].insertCell(iNewCol);
					theCell.id=iNewRow+"$"+iNewCol;
					theCell.row=iNewRow;
					theCell.col=iNewCol;
					theCell.colSpan="1";
					theCell.rowSpan="1";
					theCell.className=strClassName;
					theCell.innerHTML="<font contenteditable=\"true\"></font>";	
					theCell.align="center";
				}
			parent.imghebing.border="0";
		}
	},
	drawLine:function(){
		 var strEndCellId=iEndRow+"$"+iEndCol;


		var objEndCell=document.getElementById(strEndCellId);
		if(objEndCell.rowSpan>1)iEndRow=iEndRow+objEndCell.rowSpan-1;
		if(objEndCell.colSpan>1)iEndCol=iEndCol+objEndCell.colSpan-1;
		for(i=iEndRow;i>=iStartRow;i--){
			 for(j=iEndCol;j>=iStartCol;j--){
				objTemp=document.getElementById(i+"$"+j);
				if(objTemp!=null){
						objTemp.className="cttdlefttop";
				}
			 }				
		 }
		 for(i=iEndRow;i>=iStartRow;i--){
			 objTemp=document.getElementById(i+"$"+(iEndCol+1));
			 objTemp.className="cttdleft";
		}
		for(i=iEndCol;i>=iStartCol;i--){
			 objTemp=document.getElementById((iEndRow+1)+"$"+i);
			 objTemp.className="cttdtop";
		}
	 },
	drawLine1:function(){
		 var strEndCellId=iEndRow+"$"+iEndCol;


		var objEndCell=document.getElementById(strEndCellId);
		if(objEndCell.rowSpan>1)iEndRow=iEndRow+objEndCell.rowSpan-1;
		if(objEndCell.colSpan>1)iEndCol=iEndCol+objEndCell.colSpan-1;
		for(i=iEndRow;i>=iStartRow;i--){
			 for(j=iEndCol;j>=iStartCol;j--){
				objTemp=document.getElementById(i+"$"+j);
				if(objTemp!=null){
					var iForCol=j+objTemp.colSpan-1;
					var iForRow=i+objTemp.rowSpan-1;
					if((i==iStartRow||iForRow==iStartRow)&&(iForCol==iStartCol||j==iStartCol)&&(i==iEndRow||iForRow==iEndRow)&&(iForCol==iEndCol||j==iEndCol))
						objTemp.className="cttdlefttoprightbottom";
					else
					if((i==iStartRow||iForRow==iStartRow)&&(iForCol==iStartCol||j==iStartCol))//左上
						objTemp.className="cttdlefttop";
					else
						if((i==iStartRow||iForRow==iStartRow)&&(iForCol==iEndCol||j==iEndCol))
							objTemp.className="cttdrighttop";
					else
						if((i==iEndRow||iForRow==iEndRow)&&(iForCol==iStartCol||j==iStartCol))
							objTemp.className="cttdleftbottom";
					else
						if((i==iEndRow||iForRow==iEndRow)&&(iForCol==iEndCol||j==iEndCol))
							objTemp.className="cttdrightbottom";
					else/////////////////

					if(iForRow==iStartRow||i==iStartRow)
							objTemp.className="cttdtop";
					else
						if(iForRow==iEndRow||i==iEndRow)
							objTemp.className="cttdbottom";
					else
						if(iForCol==iEndCol||j==iEndCol)
							objTemp.className="cttdright";
					else
						if(iForCol==iStartCol||j==iStartCol)
							objTemp.className="cttdleft";
					else						
						objTemp.className="cttd";

				}
			 }
				
		 }
	 },
	drawLine2:function(){
		 var strEndCellId=iEndRow+"$"+iEndCol;


		var objEndCell=document.getElementById(strEndCellId);
		if(objEndCell.rowSpan>1)iEndRow=iEndRow+objEndCell.rowSpan-1;
		if(objEndCell.colSpan>1)iEndCol=iEndCol+objEndCell.colSpan-1;
		for(i=iEndRow;i>=iStartRow;i--){
			 for(j=iEndCol;j>=iStartCol;j--){
				objTemp=document.getElementById(i+"$"+j);
				if(objTemp!=null){
										
						objTemp.className="cttd";

				}
			 }
				
		 }
	 },
	mouseDown:function (){
			var obj=event.srcElement;
			if(oldHeadTd!="undefined")
				oldHeadTd.style.backgroundColor="#CFDCEC";
			obj.style.backgroundColor="darkblue";

			oldHeadTd=obj;
			

			isMouseDown=true;
			posX=event.x;
			posY=event.y;
			currentWidth=obj.offsetWidth;
			currentHeight=obj.offsetHeight;
			currentDragObj=obj;
			this.setDrag();
		},

	setMouse:function(){
	if(currentDragObj==null)
		return;
	switch (currentMouseType) {
	case "TOP":
		var ox=event.offsetX+2;
		var oy=event.offsetY;
			var offset_x =currentDragObj.offsetWidth
			if(offset_x<=ox+5||isDraging){//
				document.body.style.cursor='col-resize';
				isBorderDrag=true;
			}else{
				document.body.style.cursor='';
				isBorderDrag=false;
			}
		break;
	case "LEFT":

		var oy=event.offsetY+2;
			var offset_y =currentDragObj.offsetHeight;
			if(offset_y<=oy+5||isDraging){//
				document.body.style.cursor='row-resize';
				isBorderDrag=true;
			}else{
				document.body.style.cursor='';
				isBorderDrag=false;
			}
	}
		
	},
	getCellMsg:function(){//保存
		var strContent="";
				for(var i=1;i<=iRow;i++){					
					for(var j=1;j<=iCol;j++){
						var objTd=$(i+"$"+j);
						if(objTd!=null){
							strContent+=this.generParams(objTd);
						}
					}
				}
				if(strContent!=""){
					strContent="srepcode="+sRepCode+"&imaxrow="+iRow+"&imaxcol="+iCol+"&attrname="+strTdAttr.join()+","+strTdStyle.join()+"&dbname="+strDBName+strContent;
					var strTemp="";
					for(var i=0;i<=iRow;i++)
						strTemp+=","+$(i+"$0").style.height;
					strContent+="&sysheight="+strTemp.substr(1);
					strTemp="";
					for(var i=0;i<=iCol;i++)
						strTemp+=","+$("0$"+i).style.width;
					strContent+="&syswidth="+strTemp.substr(1);
					//strContent+="&sysform="+parent.selformpage.value;
					strContent+="&batchrow="+iBatchRow;
					strContent+="&sysoptype=optsave";
					strContent+="&sysopbind="+arrBind.join();
					

					
					//alert(arrBind.join());
					var vResult=getTx(strContent,"RepDesiner");
					alert(vResult);
				}else
					alert("不需要保存！");
				//parent.paramvalues.value=strContent;
	},
	clearValue:function(_strValue){
		var vResult=_strValue.replace(/\+/g,"＋");	
		vResult=vResult.replace(/\%/g,"％");
		vResult=vResult.replace(/\ /g,"SYS_SPACE");
		vResult=vResult.replace(/\&nbsp;/g,"SYS_SPACE");
		return vResult;
	},
	generParams:function(_obj){
		if(_obj.childNodes[0].innerHTML==""&&_obj.className=="cttd"&&parseInt(_obj.colSpan)<=1&&parseInt(_obj.rowSpan)<=1&&_obj.bgColor=="")return "";			
		var strParams="&id"+_obj.id+"="+this.clearValue(_obj.childNodes[0].innerHTML);
		for(i in strTdAttr){
			var objAttrValue=_obj[strTdAttr[i]];
			if(typeof(objAttrValue)=="undefined")
				objAttrValue="";
			strParams+="&id"+_obj.id+"$"+strTdAttr[i]+"="+objAttrValue;
		}
		for(i in strTdStyle){
			strParams+="&id"+_obj.id+"$"+strTdStyle[i]+"="+_obj.style[strTdStyle[i]];
		}
		
		return strParams;
	},
	initCell:function(){//初始化单元格
		parent.seldataset.value=objCurTd.sdataset;//数据集		
		parent.selkzfs.value=objCurTd.skzfs;//扩展方式
		parent.seljydyg.value=objCurTd.sfjydyg;//是否基于单元格		
		this.initDataSet(objCurTd.sdataset);
	},
	setData:function(_value){
		if(objCurTd==null){
			alert("请选择单元格！");
			return;
		}
		objCurTd.sdataset=_value;
		this.initDataSet(_value);
	},
	initDataSet:function(_value){
		if(!objCurTd.sdataset||objCurTd.sdataset=="")
			parent.tdfields.innerHTML="";
		else{
			var strCols=getTx("sysoptype=optcols&sconid="+_value,"RepDesiner");
			parent.tdfields.innerHTML=strCols;
		}
	},	
	setFormData:function(_value){
		//设置全局表单

		this.initFormData(_value);
	},
	initFormData:function(_value){
		if(_value!=""){
			var strCols=getTx("sysoptype=optform&spagecode="+_value+"&srepcode="+sRepCode+"&sbinds="+arrBind.join(),"RepDesiner");
			parent.tdformfields.innerHTML=strCols;
			
		}else
			parent.tdformfields.innerHTML="";
	},
	checkBindFields:function(_objCurId){	
		var arrBindTemp;
		for(var i in arrBind){
			arrBindTemp=arrBind[i].split(":");
			if(arrBindTemp[0]==_objCurId)
				return true;
			//$(arrBindTemp[0]).childNodes[0].innerText="";
		}
		//arrBind=new Array();		
	},
	setFormFields:function(_value,_obj,_bType){   ////////////////////////////////////////////////绑定与解除
		if(objCurTd==null){
			alert("请选择单元格！");
			return;
		}
		if(this.checkBindFields(objCurTd.id)){
			alert("此单元格已经绑定其它元素，请先解除！");
			return;
		}
		if(!!_bType){//批量
			if(iBatchRow==0)
				iBatchRow=objCurTd.row;
			else{
				if(objCurTd.row!=iBatchRow){
					alert("批量数据必须放置在同一行！当前为第"+iBatchRow+"行！");
					return;
				}
			}

		}
		objCurTd.childNodes[0].innerText=_value;		
		_obj.innerHTML="<a href='javascript:desinermain.ylt.table.clearFormFields(\""+objCurTd.id+"\","+_obj.id+",\""+_value+"\");'><font color='red'>解除</font></a>";
		
		arrBind[arrBind.length]=objCurTd.id+":"+_value;
	},
	clearBindFields:function(_objCurId){	
		var arrBindTemp;
		for(var i in arrBind){
			arrBindTemp=arrBind[i].split(":");
			if(arrBindTemp[0]==_objCurId){
				arrBind.splice(i,1);
			}
				
		}			
	},
	clearFormFields:function(_strCell,_objOp,_strValue){
		
		$(_strCell).childNodes[0].innerText="";
		_objOp.innerHTML="<a href='javascript:desinermain.ylt.table.setFormFields(\""+_strValue+"\","+_objOp.id+")'><font color='green'>绑定</font></a>";
		this.clearBindFields(_strCell);
	},	
	setKzfs:function(_value){
		if(objCurTd==null){
			alert("请选择单元格！");
			return;
		}
		objCurTd.skzfs=_value;
	},
	setJydyg:function(_value){
		if(objCurTd==null){
			alert("请选择单元格！");
			return;
		}
		objCurTd.sfjydyg=_value;
	},
	setExp:function(_value){
		var strContent="<table><tr><td onclick=\"objOpen.objCurTd.childNodes[0].innerText='list("+_value+")'\">list("+
						_value+")</td></tr><tr><td>group("+
						_value+")</td></tr><tr><td onclick=\"objOpen.objCurTd.childNodes[0].innerText=':="+_value+"';\">:=("+
						_value+")</td></tr></table>";
		messageBox("sds",strContent);
	},
	setAreStyleValue:function(_iType,_objCell,_strValue){
		switch(_iType){
			case 1://///////////////////////////////////////////////////////////////////字体
				_objCell.style.fontFamily=_strValue;
				break;
			case 2://///////////////////////////////////////////////////////////////////字号
				_objCell.style.fontSize=_strValue;
				break;
			case 3://///////////////////////////////////////////////////////////////////对齐
				_objCell.align=_strValue;
				_objCell.childNodes[0].style.textAlign=_strValue;
				break;
			case 4://///////////////////////////////////////////////////////////////////字体颜色
				//_objCell.color=_strValue;
				_objCell.style.color=_strValue;
				//_objCell.childNodes[0].style.color=_strValue;
				break;
			case 6://///////////////////////////////////////////////////////////////////背景				
				_objCell.bgColor=_strValue;
				break;
		}
	},
	setAreStyle:function(_iType,_strValue){
		if(selTDDiv.style.display=="none"){
			if(objCurTd==null)
				alert("请选中单元格！");
			else
				this.setAreStyleValue(_iType,objCurTd,_strValue);
		}else
			for(i=iEndRow;i>=iStartRow;i--){
						for(j=iEndCol;j>=iStartCol;j--){
							objTemp=document.getElementById(i+"$"+j);
							if(objTemp!=null)
								this.setAreStyleValue(_iType,objTemp,_strValue);
						}
				
					 }
		

	},
	setColor:function(_iType,_objImg,_objRgb){
		parent.iSysColorType=_iType;
		parent.showColorPicker(_objImg,_objRgb);
	},
	setPic:function(){//1图片2统计图3背景图
		if(objCurTd==null){
			alert("请选择单元格！");
			return;
		}
		alert(objCurTd.id);
		winPic('uploadFile?itype=1&artn='+objCurTd.id);
	},
	addTotalGraph:function(){//统计图
		winBox("统计图","View?SPAGECODE=1352696090062&SREPCODE="+sRepCode,550,300);
	},
	addExp:function(){//表达式
		winBox("插入公式","InvokExp?pagercode=expressionpager&srepcode="+sRepCode+"&optype=1",750,400);
	},
	lockGroupRow:function(){//分组锁定
		winBox("插入公式","View?SPAGECODE=1382523677687&SREPCODE="+sRepCode+"&optype=1",750,400);
	},
	addParams:function(){//参数
		winBox("报表参数","View?SPAGECODE=1352099425879&SREPCODE="+sRepCode,550,300);
	},
	setStyle:function(_iType,_strValue){
		switch(_iType){
			case 7:
				tools.repelement.style.display="none";
				tools.frmcomponent.style.display="";
				break;
			case 8:
				tools.repelement.style.display="";
				tools.frmcomponent.style.display="none";
				break;
			case 9:
				objCurTd.childNodes[0].innerHTML="<input type='text'>:=文本框";
				objCurTd.stype="input";
				break;
			case 10:
				objCurTd.childNodes[0].innerText=":=多行文本框";
				objCurTd.stype="textarea";
				break;
			case 11:
				objCurTd.childNodes[0].innerText=":=日历";
				objCurTd.stype="date";
				break;
			case 12:
				winBox("字典","getdic.jsp",550,300);
				//objCurTd.childNodes[0].innerText=":=字典";
				//objCurTd.stype="dic";
				break;

		}
	}


}

document.body.onload= function(){
	dragtable.onmousemove=function(){ylt.table.drawSelCells();}
	dragtable.onmousedown=function(){ylt.table.contentDown();}
	dragtable.onclick=function(){ylt.table.contentClick();}
	dragtable.ondblclick=function(){ylt.table.contentDbClick();}
} 

//alert("a   aa".replace(/\ /g,"b"));


