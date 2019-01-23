var ylt= ylt ||{};
ylt.Phone = ylt.Phone || {};
var yltPhone=ylt.Phone={
	strCurPageParam:"",
	strScanParam:"",
	doSubmit:function(){
		
		add.action="YLWebAction";
		//alert(add.action);
		add.submit();
	},
	onLongClick:function(){
		this.doCheckBox();
	},
	doLocation:function(_strRevokeFun){
		my.doLocation(true,_strRevokeFun);
	},
	photoGraph:function(_strUrl){
		my.photoGraph(_strUrl);
	},
	reciveUploadMsg:function(_strMsg){
		this.$("photomsg").innerHTML+="<img src='dir/"+_strMsg+"' height='100px' style='border:2px solid #fba6f0;margin-right:10px;margin-top: 5px;'>";
		
	},
	bIsDoShowCheck:false,
	setBackDoClose:function(){
		my.setBackDoClose();
		this.bIsDoShowCheck=true;
	},
	setDoBack:function(){
		my.setDoBack();
	},
	openMap:function(_objLocation){
		if(!!_objLocation.citycode){
			dLong=_objLocation.longtitude;
			dLat=_objLocation.latitude;
			my.doLocation(false,"");
			my.openMap(dLong,dLat);
		}
	},
	openPanorama:function(_objLocation){
		if(!!_objLocation.citycode){
			dLong=_objLocation.longtitude;
			dLat=_objLocation.latitude;
			my.doLocation(false,"");
			my.openPanorama(dLong,dLat);
			//alert(_objLocation.citycode+":"+_objLocation.city);
		}
	},
	openLinkByResult:function(_strUrl){
		if(!!window.my)
			my.openLinkByResult(_strUrl);
		else
			window.location=_strUrl;
	},
	closeLinkByResult:function(_strUrl,_strMsg){
		if(!!window.my)
			my.closePageByResult(_strMsg);
		//else
		//	window.location=_strUrl;
	},
	openLink:function(_strUrl){
		if(!!window.my)
			my.openPage(_strUrl);
		else
			window.location=_strUrl;
	},
	closeLink:function(_strUrl){
		if(!!window.my)
			my.closePage();
		else
			window.location=_strUrl;
	},
	footClick:function(_strUrl){
		window.location=_strUrl;
	},
	getBarCode:function(_strBarCode){
		alert(_strBarCode);
	},
	scanBar:function(_strScanParam){
		this.strScanParam=_strScanParam;
		my.scanBar();
	},
	checkRowClick:function(_objEvent,_strCkId){
		if(_objEvent.srcElement.id!=_strCkId){
			var objCheck=this.$(_strCkId);
			objCheck.checked=!objCheck.checked;
		}
	},
	selAllCheckBox:function(_strCheckName,_bChecked){
		var arrCheckBoxs = document.getElementsByName(_strCheckName);
		var iCheckCount=arrCheckBoxs.length;
		for(var i=0;i<iCheckCount;i++){
			arrCheckBoxs[i].checked=_bChecked;
		}
	},
	sys_singer_col_fiter_Cur_Field:"",
	sys_singer_col_fiter_query:function(){
	var objCheckChilds=document.getElementsByName('checkfiter');
	var iCheckCount=objCheckChilds.length;
	var strSplit='';
	var strSelCodes="";
	var bIsCheck=false;
	var bIsAllCheck=true;
	for(var i=0;i<iCheckCount;i++){
			 if(objCheckChilds[i].checked){
				strSelCodes+=strSplit+objCheckChilds[i].value;
				strSplit="','";
				bIsCheck=true;
			 }else
				bIsAllCheck=false;
	}
	if(bIsCheck){
		strSelCodes=" "+this.sys_singer_col_fiter_Cur_Field+ " in('"+strSelCodes+"')";
		var strCurFiterCondition=sys_form_fiter_con.NO_FITER_CONDITION.value;
		if(strCurFiterCondition==""){
			if(bIsAllCheck){
				this.closeWin();
				return;
			}else
				sys_form_fiter_con.NO_FITER_CONDITION.value=strSelCodes;
			//alert(sys_form_fiter_con.NO_FITER_CONDITION.value);
		}else{
			var strCurDoFiterCondition=this.sys_singer_col_fiter_Cur_Field+" in(";
			if(strCurFiterCondition.indexOf(strCurDoFiterCondition)!=-1){//已经有该字段的过滤
				var arrStrFiterValue=strCurFiterCondition.split(" and ");
				var iConditionFieldCount=arrStrFiterValue.length;
				for(var j=0;j<iConditionFieldCount;j++){
					//alert(arrStrFiterValue[j].indexOf(strCurDoFiterCondition));
					if(arrStrFiterValue[j].indexOf(strCurDoFiterCondition)!=-1){//找到该字段的条件
						if(bIsAllCheck){//全选
							sys_form_fiter_con.NO_FITER_CONDITION.value=sys_form_fiter_con.NO_FITER_CONDITION.value.replace(" and "+arrStrFiterValue[j],"");
							sys_form_fiter_con.NO_FITER_CONDITION.value=sys_form_fiter_con.NO_FITER_CONDITION.value.replace(arrStrFiterValue[j],"");
							if(sys_form_fiter_con.NO_FITER_CONDITION.value.substring(0,5)==" and ")
								sys_form_fiter_con.NO_FITER_CONDITION.value=sys_form_fiter_con.NO_FITER_CONDITION.value.substring(5);
						}else
							sys_form_fiter_con.NO_FITER_CONDITION.value=sys_form_fiter_con.NO_FITER_CONDITION.value.replace(arrStrFiterValue[j],strSelCodes);
						}
				}
			}else
				if(bIsAllCheck){
					this.closeWin();
					return;
				}else
					sys_form_fiter_con.NO_FITER_CONDITION.value+=" and "+strSelCodes;
			}
	}else
		sys_form_fiter_con.NO_FITER_CONDITION.value="";
		//alert(sys_form_fiter_con.NO_FITER_CONDITION.value);
		sys_form_fiter_con.submit();
	},
	doWitchDataType:function(_strPageCode,_iType){
		this.flushPage(_strPageCode,0,"sys_data_switch="+_iType);
	},
	doSort:function(){
		this.popWin("",0,0,"gray",true,"100%","100%");
		this.popWin(sys_obj_sort_div.innerHTML,-1,0,"white",false,"50%","100%");
	},
	sortByField:function(_strField){
		var strDesc="";
		if(sys_form_fiter_con.NO_SORT_FIELD.value==_strField){
			if(sys_form_fiter_con.NO_SORT_FIELD_DESC.value=="desc")
				strDesc="";
			else
				strDesc="desc";
			
		}
		sys_form_fiter_con.NO_SORT_FIELD.value=_strField;
		sys_form_fiter_con.NO_SORT_FIELD_DESC.value=strDesc;
		sys_form_fiter_con.submit();
		//this.flushPage(_strPageId,0,"SYS_QUERY_FIELD="+_strField);
	},
	popFiter:function(_strFieldName,_strField,_strPageCode,_iQuereyElementIndex){
		this.popWin("",0,0,"gray",true,"100%","100%");
		//alert(this.strCurPageParam);
		this.sys_singer_col_fiter_Cur_Field=_strField;
		if(this.sys_singer_col_fiter_Cur_Field.substring(0,4)=="dic.")
			this.sys_singer_col_fiter_Cur_Field=this.sys_singer_col_fiter_Cur_Field.split(".")[2];
		
		var iActionIndex=sys_form_fiter_con.action.indexOf("?");
		var strParam="";
		if(iActionIndex!=-1)
			strParam="&"+sys_form_fiter_con.action.substring(iActionIndex+1);
		strParam+="&NO_FITER_CONDITION="+sys_form_fiter_con.NO_FITER_CONDITION.value;
		strParam+="&NO_q1="+sys_form_fiter_con.NO_q1.value;
		//alert(strParam+":"+_strPageCode);
		var strContent=this.doAjax("itp="+_iQuereyElementIndex+"&sfield="+_strField+"&sfnm="+_strFieldName+strParam,"viewapp?spg="+_strPageCode);
		
		this.popWin(strContent,-1,0,"white",false,"50%","100%");
	},
	popUser:function(_strBranchId,_strPageId){
		this.popWin("",0,0,"gray",true,"100%","100%");
		var strContent=this.doAjax("bid="+_strBranchId+"&pid="+_strPageId,"comp?sys_type=seluser");
		this.popWin(strContent,-1,0,"white",false,"50%","100%");
	},
	sys_changeOPUser:function(_strPageId){
	var objCheckChilds=document.getElementsByName('checkfiter');
	var iCheckCount=objCheckChilds.length;
	var strSelCodes="";
	var bIsCheck=false;
	var bIsAllCheck=true;
	var strSplit="";
	for(var i=0;i<iCheckCount;i++){
			 if(objCheckChilds[i].checked){
				strSelCodes+=strSplit+objCheckChilds[i].value;
				bIsCheck=true;
				strSplit=",";
			 }else
				bIsAllCheck=false;
	}
	if(bIsCheck){
		//window.location="sc_gylc_xld.app?SYS_STRCURUSER="+strSelCodes;
		this.flushPage(_strPageId,0,"SYS_STRCURUSER="+strSelCodes);
	}	
	},
	closeWin:function(){
		var objWin=yltPhone.$("sys_popwin");
		var objFiter=yltPhone.$("sys_popfiter");
		if(objWin!=null)
			document.body.removeChild(objWin);
		if(objFiter!=null)
			document.body.removeChild(objFiter);
		document.body.style.overflow="scroll";
	},
	popWin:function(_strContent,_iLeft,_iTop,_strColor,_bFiter,_strWidth,_strHeight){
		var objDiv=document.createElement("div");		
		objDiv.style.position="fixed";
		if(_iLeft==-1)
			objDiv.style.right="0px";
		else
			objDiv.style.left=_iLeft+"px";
		objDiv.style.top=_iTop+"px";
		objDiv.style.background=_strColor;		
		objDiv.style.width=_strWidth;
		objDiv.style.height=_strHeight;
		if(_bFiter){
			objDiv.setAttribute('id',"sys_popfiter");
			objDiv.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=20,finishOpacity=75,StartX=0,StartY=0,FinishX=1024,FinishY=1000";
			objDiv.style.opacity="0.6";
			objDiv.style.zIndex = "1000";			
			objDiv.onclick=function(){	
										yltPhone.closeWin();
									};
			document.body.style.overflow="hidden";
		}else{
			objDiv.setAttribute('id',"sys_popwin");
			objDiv.style.overflow="scroll";
			objDiv.style.zIndex = "1001";
		}
		objDiv.innerHTML=_strContent;
		document.body.appendChild(objDiv);
	},
	popQuery:function(_strScanParam){
		if(popheadmenu.style.display==""){
			popheadmenu.style.display="none";
			headnosplit.style.height="50px";
		}else{
			
			popheadmenu.style.display="";
			headnosplit.style.height=(popheadmenu.offsetHeight+50)+"px";
		}
	},
	showQuery:function(_strDisplay){
		if(_strDisplay==""){
			popheadmenu.style.display="";
			headnosplit.style.height=(popheadmenu.offsetHeight+50)+"px";
			
		}else{
			popheadmenu.style.display="none";
			headnosplit.style.height="50px";
		}
	},
	getNowFormatDate:function(){ 
		var day = new Date(); 
		var Year = 0; 
		var Month = 0; 
		var Day = 0; 
		var CurrentDate = ""; 
		//初始化时间 
		//Year= day.getYear();//有火狐下2008年显示108的bug 
		Year= day.getFullYear();//ie火狐下都可以 
		Month= day.getMonth()+1; 
		Day = day.getDate(); 
		CurrentDate += Year + "-"; 
		if (Month >= 10 ){ 
			CurrentDate += Month + "-"; 
		}else{
			CurrentDate += "0" + Month + "-";
			} 
		if (Day >= 10 ){
			CurrentDate += Day ; 
		}else{
			CurrentDate += "0" + Day ; 
		}
		return CurrentDate; 
	},
	clickTreeNode:function(_strCode,_strName){
		alert(_strCode+":"+_strName);
	},
	flushElement:function(_strPageId,_iElementIndex,_strPageParam){//0页面1侧边栏菜单
		this.$("sys_page_panel").innerHTML=this.doAjax(_strPageParam,"viewapp?spg="+_strPageId);
	},
	strUrlParams:"",
	initParams:function(__strPageParam){
		var objParam=new Object();
		var arrParams=__strPageParam.split("&");
		var iParamCount=arrParams.length;
		for(var i=0;i<iParamCount;i++){
			var arrP=arrParams[i].split("=");
			objParam[arrP[0]]=arrP[1];
		}
		return objParam;
	},
	flushPage:function(_strPageId,_iPageType,_strPageParam){//0页面1侧边栏菜单
		//window.location=_strPageId+".app?"+_strPageParam;
		var strParam=_strPageParam
		var objP=this.initParams(_strPageParam);
		
		if(this.strUrlParams!=""){
			var arrParams=this.strUrlParams.split("&");
			var iParamCount=arrParams.length;
			for(var i=0;i<iParamCount;i++){
				if(!(arrParams[i].split("=")[0] in objP))
					strParam+="&"+arrParams[i];
			}
		}
		var objPopQuery=this.$("popheadmenu");
		if(objPopQuery!=null)
			strParam+="&sys_pop_query="+objPopQuery.style.display;
		window.location.replace (_strPageId+".app?"+strParam);
	//alert(_strPageParam);
	/**
		if(_iPageType==0){
			this.$("sys_page_panel").innerHTML=this.doAjax(_strPageParam,"viewapp?spg="+_strPageId);
		}else{
			this.$("sys_sidebar_menu").innerHTML=this.doAjax(_strPageParam,"viewapp?spg="+_strPageId+"&itp=1");
		}
	**/
	},
	flushElement:function(_strElementId,_srtElementParam){
		
	},
	$:function(_strElementId){
		return document.getElementById(_strElementId);
	},
	getAjaxActive:function(){
		var xmlHttp;
		if (window.ActiveXObject) { 
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}else if (window.XMLHttpRequest) { 
				xmlHttp = new XMLHttpRequest();
		}
		return xmlHttp;
	},
	doAjax:function(param,aStrUrl){
		var xml=this.getAjaxActive();
		xml.open("POST",aStrUrl,false);
		xml.setRequestHeader("content-length",param.length);  
		xml.setRequestHeader("content-type","application/x-www-form-urlencoded");  
		xml.setRequestHeader("charset", "UTF-8");
		xml.send(param);  
		var res = xml.responseText;
		return res;
	},
	sys_updateCell:function(_obj,_strGJH,_strPartId,_strPid,_strDate,_strOpTb,_strOpField){
		this.sys_updateCell_MX(_obj,_strGJH,_strPartId,_strPid,_strDate,_strOpTb,_strOpField,"");
	},
	sys_updateCell_MX:function(_obj,_strGJH,_strPartId,_strPid,_strDate,_strOpTb,_strOpField,_strUser){
		var _upValue=_obj.value;
		//alert(_strDate);
		if(_upValue=='')
			return;
		if(isNaN(_upValue)){
			alert("必须是数字！");
			_obj.value="";
			return;
		}
		//var vResult=yltPhone.doAjax('comid=006&gjh='+_strGJH+'&sdate='+_strDate+'&partid='+_strPartId+'&pid='+_strPid+'&sl='+_upValue+'&optb='+_strOpTb+'&opcount='+_strOpField,'docommand');
		var vResult="";
		
		
		
		//var objRow=_obj.parentElement.parentElement;
		//alert(objSlFont.innerText.replace("(","").replace(")",""));
		//var strCellSl=objRow.cells[0].innerText;
		//var iSlSartIndex=strCellSl.indexOf("(");
		//var iSlEndIndex=strCellSl.indexOf(")");
		//var strCellTrueSl=strCellSl.substring(iSlSartIndex+1,iSlEndIndex);
		
		var strFontId=_strGJH;
		if(strFontId=="")strFontId=_strPartId;
		var objSlFont=this.$("fontsl"+strFontId);

	
		var arrZslzwc=objSlFont.innerText.replace("(","").replace(")","").split('/');
		
		var iYwc=parseInt(arrZslzwc[0]);
		var iZsl=parseInt(arrZslzwc[1]);
		var iDrwc=parseInt(_upValue);
		var parvalue=_obj.getAttribute('parchangevalue');
        if(parvalue=='')
            parvalue=0;
		
		var iDrZwc=iYwc-parseInt(parvalue)+iDrwc;
		if(iDrZwc>iZsl){
			alert('数量超过总量范围，请重新输入！');
			if(parvalue==0)
				_obj.value="";
			else
				_obj.value=parvalue;
			return;
		}
		var strOpUser=_strUser;
		
		if(strOpUser=="")
			strOpUser="SYS_STRCURUSER="+this.$("inopuid").value;
		else
			strOpUser="SYS_STRCURUSER="+strOpUser;
		//alert(strOpUser);
		
		if(_strGJH=="")
			vResult=yltPhone.doAjax('comid=011&'+strOpUser+'&sdate='+_strDate+'&partid='+_strPartId+'&pid='+_strPid+'&sl='+_upValue+'&optb='+_strOpTb+'&opcount='+_strOpField,'docommand');
		else{
			if(_strGJH.charAt(0)=="$"){
				_strGJH=_strGJH.substring(1);
				vResult=yltPhone.doAjax('comid=013&'+strOpUser+'&gjh='+_strGJH+'&sdate='+_strDate+'&partid='+_strPartId+'&pid='+_strPid+'&sl='+_upValue+'&optb='+_strOpTb+'&opcount='+_strOpField,'docommand');
			}else
				vResult=yltPhone.doAjax('comid=006&'+strOpUser+'&gjh='+_strGJH+'&sdate='+_strDate+'&partid='+_strPartId+'&pid='+_strPid+'&sl='+_upValue+'&optb='+_strOpTb+'&opcount='+_strOpField,'docommand');
		}
		if(vResult=='true'){
			_obj.setAttribute('parchangevalue',_upValue);
			objSlFont.innerText="("+iDrZwc+'/'+iZsl+")";
			this.$("fontdate"+strFontId).innerText=_strDate;
		}else{
			alert('操作失败！');
		}      
	},
	sys_doComMand:function(_strComMandId,_strParam){
		var vResult=yltPhone.doAjax('comid='+_strComMandId+'&'+_strParam,'docommand');
		if(vResult=="true")
			window.location.reload();
		else
			alert("操作失败！");
	},
	sys_updateDb:function(_obj,_iV,_strPid,_strXh,_strGjh){
		var strText=_obj.innerText;
		var strCaption="";
		if(strText=="未装箱"){
			_iV=1;
			strCaption="已装箱";
		}else{
			_iV=0;
			strCaption="未装箱";
		}
		var vResult=yltPhone.doAjax('comid=012&v='+_iV+'&pid='+_strPid+'&xh='+_strXh+'&gjh='+_strGjh,'docommand');
		if(vResult=='true'){
			_obj.className="bttn_"+_iV;
			_obj.innerText=strCaption;
		}
	},
	sys_updateGjCell:function(_obj,_strGJH,_strPid,_strDate,_strOpTb,_strOpField){
		this.sys_updateGjCell_MX(_obj,_strGJH,_strPid,_strDate,_strOpTb,_strOpField,"");
	},
	sys_updateGjCell_MX:function(_obj,_strGJH,_strPid,_strDate,_strOpTb,_strOpField,_strUser){
		var _upValue=_obj.value;
		//alert(_strDate);
		if(_upValue=='')
			return;
		if(isNaN(_upValue)){
			alert("必须是数字！");
			_obj.value="";
			return;
		}
		
		
		//var objRow=_obj.parentElement.parentElement;
		
		var objSlFont=this.$("fontsl"+_strGJH);

	
		var arrZslzwc=objSlFont.innerText.replace("(","").replace(")","").split('/');


	
		var iYwc=parseInt(arrZslzwc[0]);
		var iZsl=parseInt(arrZslzwc[1]);
		var iDrwc=parseInt(_upValue);
		var parvalue=_obj.getAttribute('parchangevalue');
        if(parvalue=='')
            parvalue=0;
		
		var iDrZwc=iYwc-parseInt(parvalue)+iDrwc;
		if(iDrZwc>iZsl){
			alert('数量超过总量范围，请重新输入！');
			if(parvalue==0)
				_obj.value="";
			else
				_obj.value=parvalue;
			return;
		}
		
		//var strOpUser="SYS_STRCURUSER="+this.$("inopuid").value;
		
		var strOpUser=_strUser;
		
		if(strOpUser=="")
			strOpUser="SYS_STRCURUSER="+this.$("inopuid").value;
		else
			strOpUser="SYS_STRCURUSER="+strOpUser;
		
		var strComId="007";
		if(_strOpTb=="t_mr_gjzz")
			strComId="014";
		var vResult=yltPhone.doAjax('comid='+strComId+'&'+strOpUser+'&gjh='+_strGJH+'&sdate='+_strDate+'&pid='+_strPid+'&sl='+_upValue+'&optb='+_strOpTb+'&opcount='+_strOpField,'docommand');
		if(vResult=='true'){
			_obj.setAttribute('parchangevalue',_upValue);
			objSlFont.innerText="("+iDrZwc+'/'+iZsl+")";
			this.$("fontdate"+_strGJH).innerText=_strDate;
		}else{
			alert('操作失败！');
		}      
	},
	doQuery:function(_obj,_strId){
		sys_form_fiter_con.elements[_strId].value=_obj.value;
		sys_form_fiter_con.submit();
	},
	bIsActiveCheck:false,
	activeCheck:function(){
		this.bIsActiveCheck=true;
	},
	bIsCheckStatus:false,
	doCheckBox:function(){
		if(!this.bIsActiveCheck){
			window.location.reload();
			return;
		}
		if(this.bIsCheckStatus)
			window.location.reload();
		else{
			if(this.bIsDoShowCheck&&this.$("sys_db_table")!=null){
			popheadmenu.style.display="";
		//popheadmenu.style.height="150px";
			popheadmenu.innerHTML+="<div style='width:100%;height:1px;background:#f5f5f5;'></div><table width='100%' style='heigh:50px;'><tr><td style='width:30%;'><input type='text' id='sys_in_op_num' class='ylnuminput'></td><td style='width:20%;' align='right'><button class='bttn_query' style='width:90%;' onclick='yltPhone.doCheckBoxValue()' >完成数量</button></td><td  style='width:10%;'></td><td valign='middle' style='width:20%;'><button class='bttn_query' onclick='yltPhone.doCheckBoxAll()'>全部完成</button></td><td style='width:20%;'><input type='checkbox' onclick='yltPhone.doSelCheckBoxAll(this);'></td></tr></table>";
			headnosplit.style.height=(popheadmenu.offsetHeight+50)+"px";
			var objInput=document.getElementsByTagName("INPUT");
			var iRows=objInput.length;
			for(var i=0;i<iRows;i++){
				if(objInput[i].className=="inputnumber")
					objInput[i].type="checkbox";
			}
			this.bIsCheckStatus=true;
		}else
			window.location.reload();
		}
	},
	doSelCheckBoxAll:function(_obj){
		var objInput=document.getElementsByTagName("INPUT");
		var iRows=objInput.length;
		for(var i=0;i<iRows;i++){
			if(objInput[i].className=="inputnumber"&&objInput[i].type=="checkbox")
				objInput[i].checked=_obj.checked;
		}
	},
	doCheckBoxAll:function(){
		var objInput=document.getElementsByTagName("INPUT");
		var iRows=objInput.length;
		
		for(var i=0;i<iRows;i++){
			var objCurInput=objInput[i];
			if(objCurInput.className=="inputnumber"&&objCurInput.checked){
				var objSlFont=objCurInput.parentElement.childNodes[0].childNodes[1].innerText;
				//alert(objSlFont);
				var arrZslzwc=objSlFont.replace("(","").replace(")","").split('/');
				objCurInput.type="input";
				var parvalue=objCurInput.getAttribute('parchangevalue');
				if(parvalue=='')
					parvalue=0;
				objCurInput.value=parseInt(arrZslzwc[1])-parseInt(arrZslzwc[0])+parseInt(parvalue);
				this.simulateClick(objCurInput);
			}
				
		}
	},
	doCheckBoxValue:function(){
		var objInput=document.getElementsByTagName("INPUT");
		var iRows=objInput.length;
		var iValue=this.$("sys_in_op_num").value;
		if(iValue==""){
			this.$("sys_in_op_num").focus();
			alert("请输入数量!");
			return;
		}
		for(var i=0;i<iRows;i++){
			var objCurInput=objInput[i];
			if(objCurInput.className=="inputnumber"&&objCurInput.checked){
				objCurInput.type="input";
				objCurInput.value=iValue;
				this.simulateClick(objCurInput);
			}
				
		}
	},
	simulateClick:function(el) {  
		var evt;  
		if (document.createEvent) { // DOM Level 2 standard  
			evt = document.createEvent("HTMLEvents");  
			evt.initEvent("change", false, true);
			el.dispatchEvent(evt);  
		} else if (el.fireEvent) { // IE  
			el.fireEvent('onchange');  
		}  
	}
}
function setDefaultPro(_strPid,_strPPid){
	var vResult=yltPhone.doAjax("O_SYS_TYPE=defmod&pid="+_strPid+"&ppid="+_strPPid,"Menu");
	window.location.reload();
}
function clickFQTree(_strPageCode,_strCode,_strName){
		yltPhone.strCurPageParam="v1="+_strCode+"&v2="+selprojectname.value+"&v3="+yltPhone.getNowFormatDate();
		yltPhone.flushPage(_strPageCode,0,yltPhone.strCurPageParam);
}
function clickProTree(_strPageCode,_strCode,_strName){
		yltPhone.strCurPageParam="v2="+_strCode+"&v3="+yltPhone.getNowFormatDate();
		yltPhone.flushPage(_strPageCode,0,yltPhone.strCurPageParam);
}
yltPhone.getBarCode=function(_strBarCode){
	yltPhone.flushPage(yltPhone.strScanParam,0,yltPhone.strCurPageParam+"&NO_q1="+_strBarCode);
}