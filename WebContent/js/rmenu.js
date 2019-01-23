function rClick_type_body(){
	if(event.srcElement.tagName=="IMG")
		sys_pop_menu_del.style.display="";
	else
		sys_pop_menu_del.style.display="none";
	file_list_pop_menu.style.left=event.x;
	file_list_pop_menu.style.top=event.y+document.body.scrollTop;
	file_list_pop_menu.style.display="";
	return false;
}
function rClick_body(){
	if(event.srcElement.tagName=="IMG"){
		sys_pop_menu_del.style.display="";
		sys_pop_menu_view.style.display="";		
	}else{
		sys_pop_menu_del.style.display="none";
		sys_pop_menu_view.style.display="none";
	}
	file_list_pop_menu.style.left=event.x;
	file_list_pop_menu.style.top=event.y+document.body.scrollTop;
	file_list_pop_menu.style.display="";
	return false;
}
function rclick_file(_strPageCode){
	strCurElementId=_strPageCode;
	click_file(document.getElementById("f"+_strPageCode));
}

function addElementType(_strPageType){
	file_list_pop_menu.style.display="none";
	var iLeft=file_list_pop_menu.style.left;
	var iTop=file_list_pop_menu.style.top;
	miniWinAll('新建元素类型','','View?SPAGECODE=1354511350781&stype='+_strPageType,380,150,'','',iLeft,iTop);
}

function addElement(_strPageType,_strElementType){
	file_list_pop_menu.style.display="none";
	var iLeft=file_list_pop_menu.style.left;
	var iTop=file_list_pop_menu.style.top;
	switch(_strPageType){
		case "1":miniWinAll('新建查询页面','','View?SPAGECODE=1318408883718&stype=1&elementtype='+_strElementType,500,200,'','',iLeft,iTop);break;//查询
		case "4":miniWinAll('新建选择页面','','View?SPAGECODE=1318408883718&stype=4&elementtype='+_strElementType,500,200,'','',iLeft,iTop);break;//选择
		case "2":miniWinAll('新建新增页面','','View?SPAGECODE=1318408883718&stype=2&elementtype='+_strElementType,500,200,'','',iLeft,iTop);break;//新增
		case "3":miniWinAll('新建修改页面','','View?SPAGECODE=1318408883718&stype=3&elementtype='+_strElementType,500,200,'','',iLeft,iTop);break;//修改
		case "18":miniWinAll('新建文件页面','','View?SPAGECODE=1318408883718&stype=18&elementtype=8888888888888',500,200,'','',iLeft,iTop);break;//文件
		case "9":miniWinAll('新建批量页面','','View?SPAGECODE=1318408883718&stype=9&elementtype=8888888888888',500,200,'','',iLeft,iTop);break;//批量
		
		case "5":parent.openMainContainer('component/makegraph.jsp');break;//图形
		case "100":miniWinAll('新建流程页面','','View?SPAGECODE=1346219743293',500,200,'','',iLeft,iTop);break;//流程
		case "200":miniWinAll('新建报表','','View?SPAGECODE=1332690992593&elementtype='+_strElementType,500,200,'','',iLeft,iTop);break;//报表
		
		case "10":parent.openMainContainer('sys/pagemgr/addpages.jsp');break;//页面
		
		case "89":miniWinAll('新建布局','','View?SPAGECODE=1318408883718&stype=89&elementtype=89',500,200,'','',iLeft,iTop);break;//查询
		
		case "300":miniWinAll('新建数据集','','View?SPAGECODE=1318150329578&elementtype='+_strElementType,600,350,'','',iLeft,iTop);break;//数据集
		case "150":miniWinAll('新建表关系','','View?SPAGECODE=1318241978593',600,350,'','',iLeft,iTop);break;//数据集
	
	}
}
var objOldFile=null;
var strCurElementId="";
function click_file(_objEvent){
	if(objOldFile!=null)
		objOldFile.style.color="black";
	_objEvent.style.color="blue";
	objOldFile=_objEvent;
}
function delElementType(_strPageType){
	var strOpMsg="t_sys_element_type$STYPEID="+strCurElementId+"&t_sys_element_type$IYSLX="+_strPageType;
	ophref.href='YLDel?'+strOpMsg;messageBoxCon('提示信息','本操作会删除该文件夹下所有元素，确定要删除吗？','ophref.click();','del');
}
function delElement(_strPageType){
	var strOpMsg="";
	if(_strPageType=="100"){
		strOpMsg="t_flowmain$SFLOWID="+strCurElementId+"&t_flowdetail$SID="+strCurElementId;
	}else if(_strPageType=="200"){
		strOpMsg="t_sys_repmain$SREPCODE="+strCurElementId+"&t_sys_repitem$SREPCODE="+strCurElementId;
	}else if(_strPageType=="300"){
		strOpMsg="T_SYS_DATASET$SCONID="+strCurElementId+"&T_SYS_CONDITION$SCONID="+strCurElementId;
	}else if(_strPageType=="150"){
		strOpMsg="t_table_relation$S_TYPEID="+strCurElementId+"&t_table_package$S_TYPEID="+strCurElementId;
	}else{
		strOpMsg="T_SYS_PAGEMSG$SPAGECODE="+strCurElementId;
	}
	ophref.href='YLDel?'+strOpMsg;messageBoxCon('提示信息','确定要删除吗？','ophref.click();','del');
}
function viewElement(_strPageType){
	file_list_pop_menu.style.display="none";
	if(_strPageType==10)
		parent.openMainContainer("sys/pardata.jsp?SPAGECODE="+strCurElementId);
	else
		parent.openMainContainer("View?SPAGECODE="+strCurElementId);
}
function dbclick_file(_strPageCode,_strPageType){
	var strTagetUrl="";
	switch(_strPageType){
		case "1":strTagetUrl="query/queryset1.jsp?SPAGECODE="+_strPageCode;break;//查询
		case "4":strTagetUrl="query/queryset4.jsp?SPAGECODE="+_strPageCode;break;//选择
		case "2":strTagetUrl="input/inputset2.jsp?SPAGECODE="+_strPageCode;break;//新增
		case "3":strTagetUrl="input/inputset3.jsp?SPAGECODE="+_strPageCode;break;//修改
		case "18":strTagetUrl="query/queryset18.jsp?SPAGECODE="+_strPageCode;break;//文件
		case "9":strTagetUrl="component/componentset9.jsp?SPAGECODE="+_strPageCode;break;//批量
		case "5":strTagetUrl="component/modifygraph.jsp?spagecode="+_strPageCode;break;//图形
		case "100":strTagetUrl="FlowFactory?flowid="+_strPageCode;break;//流程
		case "200":strTagetUrl="RepDesiner?formid=&batchcount=0&srepcode="+_strPageCode;break;//报表
		case "10":strTagetUrl="sys/pardata.jsp?SPAGECODE="+_strPageCode;break;//页面
		case "300":strTagetUrl="view.do?id=100"+_strPageCode;break;//数据集
		case "150":strTagetUrl="sys/trelation/bgx.jsp?id="+_strPageCode;break;//表关系
		case "89":strTagetUrl="component/bjgl.jsp?SPAGECODE="+_strPageCode;break;//布局管理
	
	}
	//alert(iScreen_Width);
	miniWin('选择对话框','',strTagetUrl,iScreen_Width,(iScreen_Height-100),'','');
	//openMainContainer(strTagetUrl);
}
function redirectRep(_strPageCode,_strFormId,_strBatchRow){
	openMainContainer("RepDesiner?formid="+_strFormId+"&batchcount="+_strBatchRow+"&srepcode="+_strPageCode);
}
function openMainContainer(_strTagetUrl){
	document.getElementById("sys_menu").style.display="none";
	document.getElementById("console_container").style.display="none";	
	console_top.style.display="none";
	document.getElementById("console_one_container").style.display="";
	document.getElementById("sys_screenone").src=_strTagetUrl;
}
function closeMainContainer(){
	document.getElementById("console_one_container").style.display="none";
	document.getElementById("sys_menu").style.display="";
	console_top.style.display="";
	document.getElementById("console_container").style.display="";	
	document.getElementById("sys_screenone").src="";
}
function loadEvent(){
	//document.getElementById("console_one_container").style.height=document.body.clientHeight-70;
}
var STRCURURL="";
function closeMainFlush(){

	document.getElementById("console_one_container").style.display="none";
	document.getElementById("sys_menu").style.display="";
	document.getElementById("console_container").style.display="";
	STRCURURL=document.getElementById("fullscreencontainerframe").src;	
	document.getElementById("fullscreencontainerframe").src="Redirect?SYS_TYPE=1";
}

function generContainerWin(_strTitle,_strContent, _iWidth,_iHeight ,_iType){
		var strWidth="100%";
		var strHeight="100%";
		if(_iWidth!=0)
			strWidth=_iWidth+10;
		else
			_iWidth="100%";
		if(_iHeight!=0)
			strHeight=_iHeight+10;
		else
			_iHeight="100%";
			
		if(_iWidth=="100%"){
			var bgobjOld=document.getElementById("fullscreencontainerframe");
			if(bgobjOld!=null){
				document.getElementById("fullscreencontainerframe").src=_strContent;
				document.getElementById("fullscreencontainertitle").innerText=_strTitle;
				
				return;
			}
		}		
		
		if(_iType==2)
			_strContent="<iframe id='fullscreencontainerframe' name='fullscreencontainerframe' frameborder=0 width=100% height='100%' src='"+_strContent+"'></iframe>";
			
		var bgObj=parent.document.createElement("<div id='fullscreencontainer' class='console_content_win' style='width:"+strWidth+";height:"+strHeight+";'>");			
		var strWinContent="<table cellpadding='0' cellspacing='0' width='"+_iWidth+"' height='"+_iHeight+"' border='0'>";
		strWinContent=strWinContent+"<tr><td class='console_content_win_lefttopang'></td><td class='console_content_win_topline'></td><td class='console_content_win_righttopang'></td></tr>";
		strWinContent=strWinContent+"<tr><td class='console_content_win_leftline'></td>";
		strWinContent=strWinContent+"<td valign='top' class='console_win_bttn'>";//窗口按钮栏
		strWinContent=strWinContent+"<table width='100%' height='100%'><tr><td id='fullscreencontainertitle' align='left' class='console_win_bttn_title'>"+_strTitle+"</td><td align='right'><img src='images/console/close.png'></td></tr></table>";
		strWinContent=strWinContent+"</td>";
		strWinContent=strWinContent+"<td class='console_content_win_rightline'></td></tr>";
		strWinContent=strWinContent+"<tr><td class='console_content_win_leftline'></td>";
		strWinContent=strWinContent+"<td valign='top' class='console_content_win_content'>";
		strWinContent=strWinContent+_strContent;
		strWinContent=strWinContent+"</td>";
		strWinContent=strWinContent+"<td class='console_content_win_rightline'></td></tr>";
		strWinContent=strWinContent+"<tr><td class='console_content_win_leftbotang'></td><td class='console_content_win_botline'></td><td class='console_content_win_rightbotang'></td></tr>";
		strWinContent=strWinContent+"</table>";
		bgObj.innerHTML=strWinContent;
		//parent.sys_file_list.style.display='none';
		//console_container.innerHTML="";
		console_container.appendChild(bgObj);
	}
//filter:alpha(opacity=50);-moz-opacity:0.5;-khtml-opacity: 0.5;opacity: 0.5;background:black;
function menuclick(_strMenuType){
	var iHeight=document.body.clientHeight-70;
	console_container.innerHTML="";
	switch(_strMenuType){
		case "excel":
			generContainerWin("报 表 管 理","View?SPAGECODE=1375090871625&sys_type=menuico", 0,0 ,2);
			document.getElementById("console_container").style.height=iHeight;
			break;
		case "ysgl":
				genIco("查 询 管 理","query.png","cxgl");
				genIco("选 择 管 理","select.png","selgl");
				genIco("新 增 管 理","newadd.png","xzgl");
				genIco("修 改 管 理","update.png","xggl");
				genIco("批 量 管 理","batch.png","plgl");
				genIco("图 形 管 理","pic.png","txgl");
				genIco("文 件 管 理","file.png","wjgl");				
			break;
		case "sys_ysgl":				
				generContainerWin("元 素 管 理","View?SPAGECODE=1385456370312", 0,0 ,2);
				document.getElementById("console_container").style.height=iHeight;
			break;
		case "bbgl":				
				generContainerWin("报 表 管 理","ConsoleFrameWork?SYS_VIEW_TYPE=10&SYS_PAGETYPE=200", 0,0 ,2);
				document.getElementById("console_container").style.height=iHeight;
			break;
		case "lcpz":				
				generContainerWin("流 程 配 置","ConsoleFrameWork?SYS_VIEW_TYPE=1&SYS_PAGETYPE=100", 0,0 ,2);
				document.getElementById("console_container").style.height=iHeight;
			break;
		case "ymgl":				
				generContainerWin("页 面 管 理","ConsoleFrameWork?SYS_VIEW_TYPE=1&SYS_PAGETYPE=10", 0,0 ,2);
				document.getElementById("console_container").style.height=iHeight;
			break;
		case "sjgl":				
				generContainerWin("数 据 管 理","ConsoleFrameWork?SYS_VIEW_TYPE=10&SYS_PAGETYPE=300", 0,0 ,2);
				document.getElementById("console_container").style.height=iHeight;
			break;
		case "xtpz":				
				genIco("元数据管理","ysjgl.png","ysjgl");
				genIco("参 数 配 置","cspz.png","cspz");
				genIco("关 系 管 理","bgxgl.png","gxgl");
				genIco("字 典 管 理","zdgl.png","zdgl");
				genIco("模 块 管 理","mkgl.png","mkgl");
				genIco("风 格 管 理","style.png","fggl");
			break;
		case "bjgl":			
			//openMainContainer("component/bjgl.jsp");
			generContainerWin("布 局 管 理","ConsoleFrameWork?SYS_VIEW_TYPE=1&SYS_PAGETYPE=89&SYS_TYPE=89", 0,0 ,2);
			document.getElementById("console_container").style.height=iHeight;
			break;
		case "gzpt":
				generContainerWin("S Q L 监 控","ConsoleFrameWork?SYS_VIEW_TYPE=2", 500,200 ,2);
				generContainerWin("C P U 占 用 率","ConsoleFrameWork?SYS_VIEW_TYPE=5", 630,220 ,2);
			break;
		case "xtts":			
			viewDebug()
			break;
			
			
			
	}	
}
function viewDebug(){
	runPage("O_SYS_TYPE=isdebugmod","Menu");
	href.click();
}
function runPage(param,aStrUrl){ 
		var xml = new ActiveXObject("Microsoft.XMLHTTP");
		xml.open("POST",aStrUrl,false);//以同步方式通信  
		xml.setrequestheader("content-length",param.length);  
		xml.setrequestheader("content-type","application/x-www-form-urlencoded");  
		xml.setRequestHeader("charset", "UTF-8");
		xml.send(param);  
		var res = xml.responseText;
		return res;
	}
function genIco(_strName,_strIco,_opType){
	var bgObj=document.createElement("<div style='width:100;height:55;cursor:hand;float:left;' onclick='MenuIcoClick(\""+_opType+"\")'>");
		bgObj.innerHTML="<img class='console_menu_ico' src='images/console/"+_strIco+"'><div class='console_menu_text'>"+_strName+"</div>"
		console_container.appendChild(bgObj);
}
function MenuIcoClick(_strType){
var iHeight=document.body.clientHeight-135;
	switch(_strType){
		case "fggl":
			if(document.getElementById("menuicocontent")==null){
			var bgObj=document.createElement("<div id='menuicocontent' class='console_content_win' style='width:100%;height:100%;text-align:left;'>");
			var strImgHtml="";
			for(var i=1;i<=18;i++){
				strImgHtml=strImgHtml+"<div style='cursor:hand;float:left;border:5px solid white' onmouseout='this.style.border=\"5px solid white\";' onmouseover='setBackground(this,"+i+");' onclick='console_container.removeChild(menuicocontent);'><img src='upload/bg"+i+".jpg' width='200' height='200'></div>";
			}
			bgObj.innerHTML=strImgHtml;
			console_container.appendChild(bgObj);
			}
			test();
			break;
		case "cxgl":			
			generContainerWin("查 询 管 理","ConsoleFrameWork?SYS_VIEW_TYPE=10&SYS_PAGETYPE=1", 0,0,2);			
			break;
		case "selgl":
			generContainerWin("选 择 管 理","ConsoleFrameWork?SYS_VIEW_TYPE=10&SYS_PAGETYPE=4", 0,0 ,2);
			break;
		case "xzgl":
			generContainerWin("新 增 管 理","ConsoleFrameWork?SYS_VIEW_TYPE=10&SYS_PAGETYPE=2", 0,0 ,2);
			break;
		case "xggl":
			generContainerWin("修 改 管 理","ConsoleFrameWork?SYS_VIEW_TYPE=10&SYS_PAGETYPE=3", 0,0 ,2);
			break;
		case "plgl":
			generContainerWin("批 量 管 理","ConsoleFrameWork?SYS_VIEW_TYPE=1&SYS_PAGETYPE=9", 0,0 ,2);
			break;
		case "txgl":
			generContainerWin("图 形 管 理","ConsoleFrameWork?SYS_VIEW_TYPE=1&SYS_PAGETYPE=5", 0,0 ,2);
			break;
		case "wjgl":
			generContainerWin("文 件 管 理","ConsoleFrameWork?SYS_VIEW_TYPE=1&SYS_PAGETYPE=18", 0,0 ,2);
			break;
		case "ysjgl":
			openMainContainer("sys/metadata/mytable.jsp");　//元数据管理
			break;
		case "cspz":
			openMainContainer("sys/paramcfg/cspz.jsp");//移动到sys/paramcfg
			break;
		case "gxgl":
			openMainContainer("ConsoleFrameWork?SYS_VIEW_TYPE=1&SYS_PAGETYPE=150");
			break;
		case "zdgl":
			openMainContainer("DicManager");
			break;
		case "mkgl":
			openMainContainer("ModMain");
			break;
	}
	document.getElementById("console_container").style.height=iHeight;
}
function setBackground(_objSelf,_iBjIndex){
	_objSelf.style.border="5px solid red";
	document.body.style.background="url(upload/bg"+_iBjIndex+".jpg)";
	
	var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = "CURBACKGROUND=bg"+_iBjIndex+".jpg;expires=" + exp.toGMTString();
}
var step=3;
var xstep=0;
function test(){

menuicocontent.style.filter='wave(freq=3, strength=10, phase='+xstep+', lightstrength=45, add=0, enabled=1)';
xstep+=step;
TIMER=setTimeout('test()',5);
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function addComponet(_iPageType,_iElementType){
	file_list_pop_menu.style.display="none";
	var iLeft=file_list_pop_menu.style.left;
	var iTop=file_list_pop_menu.style.top;
	switch(_iPageType){
		case 1:miniWinAll('新建查询元素','','View?SPAGECODE=1318408883718&stype='+_iPageType+'&elementtype='+_iElementType,380,150,'','',iLeft,iTop);break;//查询
		case 2:miniWinAll('新建表单元素','','View?SPAGECODE=1318408883718&stype='+_iPageType+'&elementtype='+_iElementType,380,150,'','',iLeft,iTop);break;//查询
		case 3:miniWinAll('新建元素类型','','View?SPAGECODE=1318408883718&stype='+_iPageType+'&elementtype='+_iElementType,380,150,'','',iLeft,iTop);break;//查询
		case 4:miniWinAll('新建元素类型','','View?SPAGECODE=1318408883718&stype='+_iPageType+'&elementtype='+_iElementType,380,150,'','',iLeft,iTop);break;//查询
		
		case 100:miniWinAll('新建流程','','View?SPAGECODE=1346219743293',500,200,'','',iLeft,iTop);break;//流程
		case 200:miniWinAll('新建报表','','View?SPAGECODE=1332690992593&elementtype='+_iElementType,500,200,'','',iLeft,iTop);break;//报表
		case 300:miniWinAll('新建数据集','','View?SPAGECODE=1318150329578&elementtype='+_iElementType,600,350,'','',iLeft,iTop);break;//数据集
		case 80:location.reload();break;
	
	}
}

function dbclick_Mutfile(_strPageCode,_strPageType){
	var strTagetUrl="";
	switch(_strPageType){
		case "1":strTagetUrl="view.do?id=801"+_strPageCode;break;//查询
		case "4":strTagetUrl="query/queryset4.jsp?SPAGECODE="+_strPageCode;break;//选择
		case "2":strTagetUrl="view.do?id=820"+_strPageCode;break;//表单
		case "3":strTagetUrl="input/inputset3.jsp?SPAGECODE="+_strPageCode;break;//修改
		case "18":strTagetUrl="query/queryset18.jsp?SPAGECODE="+_strPageCode;break;//文件
		case "9":strTagetUrl="component/componentset9.jsp?SPAGECODE="+_strPageCode;break;//批量
		case "5":strTagetUrl="component/modifygraph.jsp?spagecode="+_strPageCode;break;//图形
		case "100":strTagetUrl="FlowFactory?flowid="+_strPageCode;break;//流程
		case "200":strTagetUrl="RepDesiner?formid=111&batchcount=0&srepcode="+_strPageCode;break;//报表
		case "10":strTagetUrl="sys/pardata.jsp?SPAGECODE="+_strPageCode;break;//页面
		case "300":strTagetUrl="view.do?id=100"+_strPageCode;break;//数据集
		case "150":strTagetUrl="sys/trelation/bgx.jsp?id="+_strPageCode;break;//表关系
		case "89":strTagetUrl="component/bjgl.jsp?SPAGECODE="+_strPageCode;break;//布局管理
	
	}
	//alert(iScreen_Width);
	miniWin('选择对话框','',strTagetUrl,1400,700,'','');
	//openMainContainer(strTagetUrl);
}

function doConfigDataset(_strPageCode){
	parent.main.rows="30,45,7,*";
	parent.opqueryset.window.location="view.do?id=804"+_strPageCode;
}
function doConfigScript(_strPageCode,_iType){
	parent.main.rows="30,*,7,1";
	parent.opqueryset.window.location="view.do?id="+_iType+_strPageCode;
	parent.viewframe.window.location="about:blank";
}
function doConfigQuereySet(_strPageCode,_iType,_iSetHeight){
	parent.main.rows="30,"+_iSetHeight+",7,*";
	parent.opqueryset.window.location="view.do?id="+_iType+_strPageCode;
}
function doConfigFormSet(_strPageCode,_iType,_strType,_iSetHeight){
	parent.main.rows="30,"+_iSetHeight+",7,*";
	parent.opqueryset.window.location="view.do?id="+_iType+_strPageCode+"&sys_type="+_strType;
}
function doConfigOther(_strPageCode,_iType){
	 parent.opqueryset.window.location="view.do?id="+_iType+_strPageCode;
}

var bSys_IsDrag=false;
var bSys_IsChange=false;
var objSys_CurDragField=null;
var objSys_InsertField=null;
function sys_doMoveDown(_obj){
	if(objSys_CurDragField!=null)objSys_CurDragField.className="th1";
	if(objSys_InsertField!=null)objSys_InsertField.className="th1";
	
	bSys_IsDrag=true;
	_obj.className='th1over';
	objSys_CurDragField=_obj;
	divheadtitle.style.display="";
	sys_doMoveMove();
	divheadtitle.style.height="28px";
	divheadtitle.style.zIndex=1000;
	divheadtitle.innerHTML="<font color='red'>"+_obj.cellIndex+"</font>"+_obj.innerHTML;
}
function sys_doMoveMove(){
	divheadtitle.style.left=event.x+10;
	divheadtitle.style.top=event.y+10;
}


function sortColumn() { 
		
       var rowLength = systablehead.rows.length;  
	   var iSouceColIndex=objSys_CurDragField.cellIndex;
	   var iToColIndex=objSys_InsertField.cellIndex;
		for(var i=0;i<rowLength;i++){
			systablehead.rows[i].insertBefore(systablehead.rows[i].cells[iSouceColIndex],systablehead.rows[i].cells[iToColIndex]);
		}
} 

function sys_bodyMoveUp(){
	sys_clearEvent();
}

function sys_clearEvent(){
	bSys_IsDrag=false;	
	bSys_IsChange=false;
}

function sys_doMoveUp(_obj){
	divheadtitle.style.display="none";
	if(bSys_IsDrag&&bSys_IsChange)
		sortColumn();
	sys_clearEvent();
}
function sys_doMoveOver(_obj){
	if(bSys_IsDrag&&_obj!=objSys_CurDragField){
		_obj.className='th1over1';
		objSys_InsertField=_obj;
		bSys_IsChange=true;
	}
}
function sys_doMoveFieldOut(_obj){
	if(bSys_IsDrag&&_obj!=objSys_CurDragField)
		_obj.className='th1';

}
function saveFieldMsg(){
	 var iColLength = systablehead.rows[0].cells.length;
	 var strFieldCodes="";
	 var strFieldNames="";
	 var strFieldSize="";
	 var strFieldDic="";
	 var strFieldTypes="";
		for(var i=0;i<iColLength;i++){
			strFieldCodes+=","+systablehead.rows[1].cells[i].childNodes[0].value;
			strFieldNames+=","+systablehead.rows[2].cells[i].childNodes[0].value;
			strFieldSize+=","+systablehead.rows[3].cells[i].childNodes[0].value;
			strFieldDic+=","+systablehead.rows[4].cells[i].childNodes[0].value;
			strFieldTypes+=","+systablehead.rows[5].cells[i].childNodes[0].value;
		}
	add.t_sys_pagemsg$SFIELDCODE.value=strFieldCodes.substring(1);
	add.t_sys_pagemsg$SFIELDNAME.value=strFieldNames.substring(1);
	add.t_sys_pagemsg$SFIELDSIZE.value=strFieldSize.substring(1);
	add.t_sys_pagemsg$STRANS.value=strFieldDic.substring(1);
	add.t_sys_pagemsg$SSQLCON.value=strFieldTypes.substring(1);
	add.submit();
}
/**

						
						"<textarea style='display:;' id='t_sys_pagemsg$SDELCON' name='t_sys_pagemsg$SDELCON'></textarea>" +//字段类型
						"<textarea style='display:;' id='t_sys_pagemsg$STRANS' name='t_sys_pagemsg$STRANS'></textarea>" +//回传
						"<textarea style='display:;' id='t_sys_pagemsg$SHREFIELD' name='t_sys_pagemsg$SHREFIELD'></textarea>" +//验证
						"<textarea style='display:;' id='t_sys_pagemsg$SQUERYFIELD' name='t_sys_pagemsg$SQUERYFIELD'></textarea>" +//选择页面
						"<textarea style='display:;' id='t_sys_pagemsg$SGLFIELD' name='t_sys_pagemsg$SGLFIELD'></textarea>" +//回填信息
						"<textarea style='display:;' id='t_sys_pagemsg$SSIZE' name='t_sys_pagemsg$SSIZE'></textarea>" +//页面大小
**/						
function saveInputFieldMsg(){
	 var iColLength = systablehead.rows[0].cells.length;
	 var strFieldCodes="";
	 var strFieldNames="";
	 var strFieldSize="";
	 
	  var strFieldTypes="";
	 var strFieldHC="";
	 var strFieldCheck="";
	 var strFieldSelPage="";
	 var strPageFieldHT="";
	 var strSelPageSize="";
	
		for(var i=0;i<iColLength;i++){
			strFieldCodes+=","+systablehead.rows[1].cells[i].childNodes[0].value;
			strFieldNames+=","+systablehead.rows[2].cells[i].childNodes[0].value;
			strFieldSize+=","+systablehead.rows[3].cells[i].childNodes[0].value;
			
			
			
			var objValue=systablehead.rows[5].cells[i].childNodes[0];
			if(objValue!=null){
			
				if(objValue.value==""){alert("请填写值！");objValue.focus();return}
				var strTempType=systablehead.rows[4].cells[i].childNodes[0].value
				if(strTempType=="4$INCREMOD_")
					if(objValue.value.indexOf("$")!=-1)
						strTempType="4$INCREMOD_$"+objValue.value.substring(0,objValue.value.indexOf("$"))+"$"+objValue.value+objValue.value.substring(objValue.value.indexOf("$"));
					else
						strTempType="4$INCREMOD_$"+objValue.value+"$"+objValue.value;
				else
					strTempType=strTempType+objValue.value;
			strFieldTypes+=","+strTempType;
			}else
				strFieldTypes+=","+systablehead.rows[4].cells[i].childNodes[0].value;
			
			
			
			
			
			strFieldSelPage+=","+systablehead.rows[6].cells[i].childNodes[0].value;
			
			strSelPageSize+=":"+systablehead.rows[7].cells[i].childNodes[0].value;
			strPageFieldHT+=","+systablehead.rows[8].cells[i].childNodes[0].value;
			strFieldCheck+=","+systablehead.rows[9].cells[i].childNodes[0].value;
			
			if(systablehead.rows[10].cells[i].childNodes[0].checked)
				strFieldHC+=","+1;
			else
				strFieldHC+=","+0;
		}
	add.t_sys_pagemsg$SFIELDCODE.value=strFieldCodes.substring(1);
	add.t_sys_pagemsg$SFIELDNAME.value=strFieldNames.substring(1);
	add.t_sys_pagemsg$SFIELDSIZE.value=strFieldSize.substring(1);
	
	add.t_sys_pagemsg$SDELCON.value=strFieldTypes.substring(1);
	
	add.t_sys_pagemsg$STRANS.value=strFieldHC.substring(1);
	
	add.t_sys_pagemsg$SHREFIELD.value=strFieldCheck.substring(1);
	
	add.t_sys_pagemsg$SQUERYFIELD.value=strFieldSelPage.substring(1);
	
	add.t_sys_pagemsg$SGLFIELD.value=strPageFieldHT.substring(1);
	add.t_sys_pagemsg$SSIZE.value=strSelPageSize.substring(1);

	add.submit();
}
function sys_delField(_obj){
	var rowLength = systablehead.rows.length;
	   var iSouceColIndex=_obj.parentNode.cellIndex;
		for(var i=0;i<rowLength;i++){
			systablehead.rows[i].deleteCell(iSouceColIndex);
		}
}
var str_sys_formHomeId="";
function solveFormTableData(_strFormTables){
	if(_strFormTables!=""){
		parent.opqueryset.window.location=str_sys_formHomeId+"&sys_type=seltables&tables="+_strFormTables;
	}
}
function clearFormMsg(_strFormTables){
	parent.opqueryset.window.location=str_sys_formHomeId+"&sys_type=clearform";
}
function saveFormMsg(){
	if(add1.bdys[0].checked)
		add.t_sys_pagemsg$SFORM.value=add1.sform.value+"$"+add1.iformheight.value+"$"+add1.bdys[0].value;
	else
		add.t_sys_pagemsg$SFORM.value=add1.sform.value+"$"+add1.iformheight.value+"$"+add1.bdys[1].value;
	add.submit();
}

