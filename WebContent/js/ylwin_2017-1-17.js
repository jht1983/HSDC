var strWinEnd="</td><td class='rightline'>&nbsp;</td></td></tr><tr><td class='leftbot'>&nbsp;</td><td class='botline'></td><td class='rightbot'>&nbsp;</td></td></tr></table>";
var	strTitleStart="<table border='0' cellpadding='0' cellspacing='0' onmousedown='dragWin(this);' width='100%' height='100%'><tr><td class='lefttop'></td><td class='toptitle' align='right' valign='middle'><table width='100%'><tr>";

function yl_sys_addEvent(elm, evType, fn, useCapture) {
if (elm.addEventListener) {
elm.addEventListener(evType, fn, useCapture);//DOM2.0
return true;
}
else if (elm.attachEvent) {
var r = elm.attachEvent('on' + evType, fn);//IE5+
return r;
}
else {
elm['on' + evType] = fn;//DOM 0
}
}

function yl_sys_delEvent(element, type, handler){
        if (element.removeEventListener) {
            element.removeEventListener(type, handler, true);
        }
        else 
            if (element.detachEvent) {
                element.detachEvent("on" + type, handler);
            }
            else {
                element["on" + type] = null;
            }
    }
function yl_sys_leftMouseDown(){
	var iLeftButton=0;
	if(event.which==null)
		iLeftButton=event.button;
	else
		iLeftButton=event.which;
	if(iLeftButton==1)
		return true;
	else
		return false;
}
function yl_sys_setCapture(_obj){
if (!window.captureEvents) {    
       _obj.setCapture && _obj.setCapture();  
 }else {  
        window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);    
 }  
}
function yl_sys_releaseCapture(_obj){
if (!window.captureEvents) {
        _obj.releaseCapture&&_obj.releaseCapture();
}else {
       window.releaseEvents(Event.MOUSEMOVE|Event.MOUSEUP);
}
}
function dragWin(aObj){
	if(event.srcElement.id=="")
		return;
	var obj=aObj.parentElement;
    var s = obj.style;
    var b = document.body;
    var x = event.clientX + b.scrollLeft - s.left.slice(0,-2);
    var y = event.clientY + b.scrollTop - s.top.slice(0,-2);
	yl_sys_setCapture(aObj);
    var m = function()
    {
            if (yl_sys_leftMouseDown())
            {							
                    var iL = event.clientX + b.scrollLeft - x;
					var iT = event.clientY + b.scrollTop - y;
					var maxL = document.body.clientWidth - aObj.offsetWidth;
					//iL = iL < 0 ? 0 : iL; 
					//iL = iL > maxL ? maxL : iL; 
					iT = iT < 0 ? 0 : iT; 
					
					s.left = iL+"px";
                    s.top = iT+"px";
            }
            else {
					yl_sys_delEvent(document,"mousemove",m);
					//document.detachEvent("onmousemove", m);
			}
    }
	yl_sys_addEvent(document,"mousemove", m,true);
	document.onmouseup = window.onblur = aObj.onlosecapture = function () {yl_sys_releaseCapture(aObj);}; 
}
function win(aUrl){
	return miniWin('操作对话框','',aUrl,380,300,'','');
}
function win500(aUrl){
	return miniWin('操作对话框','',aUrl,650,300,'','');
}
function win700(aUrl){
	return miniWin('操作对话框','',aUrl,650,520,'','');
}
function winQuery(aUrl){
	return miniWin('查询条件','',aUrl,380,200,'','');
}

function winPic(aUrl){
	return miniWin('上传图片','',aUrl,350,100,'','');
}
var objPhotoBJ;
function generBg(_strWinId){
initScreen();
var sWidth,sHeight;
	objPhotoBJ=document.createElement("div");
	objPhotoBJ.setAttribute('id',_strWinId+'_bgDiv');
	objPhotoBJ.style.position="absolute";
	objPhotoBJ.style.top="0";
	objPhotoBJ.style.background="#000";
	objPhotoBJ.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=20,finishOpacity=75,StartX=0,StartY=0,FinishX=1024,FinishY=1000";
	objPhotoBJ.style.opacity="0.2";
	objPhotoBJ.style.left="0";
	objPhotoBJ.style.width=iScreen_Width+"px";
	objPhotoBJ.style.height=iScreen_Height+"px";
	objPhotoBJ.style.zIndex = "99";
	//bgObj.style.zIndex = "100002";
	//objPhotoBJ.attachEvent("onclick" ,canclePhoto);
	document.body.appendChild(objPhotoBJ);
}

var winId=0;
function zdh(aObj,aObjChild){	
	if(aObj.style.width!="100%"){
		aObj.winTop=aObj.style.top;
		aObj.winLeft=aObj.style.left;
		aObj.style.width="100%";
		aObj.style.height="100%";
		document.getElementById(aObjChild).height="100%";
		aObj.style.left=0;
		aObj.style.top=0;
	}else{
		aObj.style.width=aObj.winWidth;
		aObj.style.height=aObj.winHeight;
		aObj.style.left=aObj.winLeft;
		aObj.style.top=aObj.winTop;
		document.getElementById(aObjChild).height=aObj.winHeight;
	}
}
function titleclose(aObj){
	document.body.removeChild(document.getElementById(aObj.id+"_bgDiv"));
	var objFrame=document.getElementById(aObj.id+"_frame");
	if(objFrame!=null){
		var objFrameDocument=objFrame.contentWindow.document;
		var objFlash=objFrameDocument.getElementById("toexcelbttn_1");
		if(objFlash!=null)
			objFlash.parentElement.removeChild(objFlash);
	}
	document.body.removeChild(aObj);
	bMessageBoxIsOpen=false;
}
function closeAndFlush(aObj){
	document.body.removeChild(document.getElementById(aObj.id+"_bgDiv"));
	document.body.removeChild(aObj);
	aObj.objOpen.location.reload();
}
function closeWinById(_strId){
	var objWin=document.getElementById(_strId);
	titleclose(objWin);
}
function refreshParentPage(_strId){
	var objWin=document.getElementById(_strId);
	objWin.objOpen.location.reload();
}
function closeById(_strId){
	var objWin=document.getElementById(_strId);
	var openPage=objWin.objOpen.location.reload();
	titleclose(objWin);
}
function getOpenPage(_strId){
	return document.getElementById(_strId).objOpen;
}
function bttn_ok(aObj,aOk){		
	aObj.objOpen.eval(aOk);
	closeAndFlush(aObj);
	return true;
}
function bttn_cancle(aObj,aCancle){
	titleclose(aObj);
	return false;
}
var bMessageBoxIsOpen=false;
function messageBox(aTitle,aContent){
	if(!bMessageBoxIsOpen){
		bMessageBoxIsOpen=true;
		return miniWin(aTitle,aContent,"",350,150,"titleclose","");
	}else{
		titleclose(document.getElementById(sys_Cru_Open_Win_Id));
	}
}
function messageBoxCon(aTitle,aContent,aOk,aCancle){
	//generBg();
	return miniWin(aTitle,aContent,"",350,150,aOk,aCancle);
}
   
function winBox(aTitle,aUrl,aWidth,aHeight){
	return miniWin(aTitle,"",aUrl,aWidth,aHeight,"","");
}
var iScreen_Width=800;
var iScreen_Height=600;
function initScreen(){
	iScreen_Width = document.body.clientWidth;
	iScreen_Height = document.body.clientHeight;
}
function miniWin(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle){	
	var iTop=(iScreen_Height-aHeight)/2-23;
	var iLeft=(iScreen_Width-aWidth)/2;
	return miniWinAll(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle,iLeft,iTop,0);
}

function reWinSize(_objFrame,_objWin,_objWinContent){
	var iFrameWidth=900;
	var iFrameHeight=600;
	if (document.getElementById){ 
		if (_objFrame && !window.opera) { 
			if (_objFrame.contentDocument && _objFrame.contentDocument.body.scrollHeight) {
				iFrameHeight= _objFrame.contentDocument.body.scrollHeight;
				iFrameWidth=_objFrame.contentDocument.body.scrollWidth; 
			}else if(_objFrame.Document && _objFrame.Document.body.scrollHeight){
					iFrameHeight = _objFrame.Document.body.scrollHeight;
					iFrameWidth= _objFrame.Document.body.scrollWidth;
			}
		} 
	}
	if((iFrameHeight+23)>iScreen_Height){
		iFrameHeight=iScreen_Height-23;
		iFrameWidth+=23;
	}
	if(iFrameWidth>iScreen_Width){
		iFrameWidth=iScreen_Width;
	}
	_objFrame.width=iFrameWidth;
	_objFrame.height=iFrameHeight;
	
	var iTop=(iScreen_Height-iFrameHeight)/2-23;
	var iLeft=(iScreen_Width-iFrameWidth)/2;
	_objWin.style.top=iTop;
	_objWin.style.left=iLeft;
}

function miniWin1(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle){	
	var iTop=(iScreen_Height-aHeight)/2;
	var iLeft=(iScreen_Width-aWidth)/2;
	return miniWinAll(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle,iLeft,iTop,1)
}






var sys_obj_timer= null;
//物体运动自由落体
function moveStyleFreeFall(_obj,_iHp,_iVp,_iSpeed,_iTopLeft,_iToTop,_iInitTop) {//运动对象,横向偏移距离,纵向偏移距离,速度,目标顶点
		_obj.style.left=_iTopLeft+'px';
		_obj.style.top=_iInitTop+'px';
		sys_obj_timer&&clearInterval(sys_obj_timer);
	var iAddLeft=1,iAddTop=1,_iHp=(_iHp>0&&_iHp<1)?_iHp:0,_iVp=(_iVp>0&&_iVp<1)?_iVp:0.5;
		sys_obj_timer=setInterval(function(){
			if(_obj){
				var iObjeLeft=parseInt(_obj.style.left)+iAddLeft,iObjTop=parseInt(_obj.style.top)+iAddTop;
				_obj.style.left=iObjeLeft+'px';
				_obj.style.top=iObjTop+'px';
				if(iObjTop<_iToTop){
					iAddTop+=10;//最好加速度为2
				} else {
					iAddLeft=(iAddTop>0)?iAddTop*_iHp:0;
					iAddTop*=(iAddTop>0)?-1*_iVp:0;
					if(iAddLeft==0&&iAddTop==0){
						clearInterval(sys_obj_timer);
						_obj.style.left=_iTopLeft+'px';
						_obj.style.top=_iToTop+'px';
					}
				}
			}
		},_iSpeed);
	}









var bIsSysViewMoveStyle=false;
function sys_FilterUrl(_strUrl){
	_strUrl=_strUrl.replace(/#/g, "%23");
	return _strUrl;
}
var sys_Cru_Open_Win_Id="";
function miniWinAll(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle,_left,_top,_iType){
	var bgObj=document.createElement("div");
	var strWinId="win"+winId;
	sys_Cru_Open_Win_Id=strWinId;
if(aUrl!=""){
	if(aUrl.indexOf("?")!=-1){
		aUrl=aUrl+"&gs_upl_kc="+strWinId;
	}else
		aUrl=aUrl+"?gs_upl_kc="+strWinId;
	aUrl=sys_FilterUrl(aUrl);
}
	winId++;
	
	var iContentWidth=aWidth-26;
	var iContentHeight=aHeight-49;
	var strContent="<iframe id='"+strWinId+"_frame' name='"+strWinId+"_frame' src='"+aUrl+"' width='100%' height='100%' frameborder='no' border='0' marginwidth='0' marginheight='0'></iframe>";
	if(aUrl=="")
		strContent=aContent;
		
		
	var strWinOp="";	
	if(_iType==1){
		iContentHeight=iContentHeight-80;
		strWinOp="<tr><td class='win_left'></td><td  width='"+iContentWidth+"px' height='50px' align='center'>"+
		"<div style='font-family:微软雅黑;font-size:11px;color:#666666;background-color:white;text-align:left;width:100%;height:20px;'>&nbsp;&nbsp;选中项目</div><div style='padding:0px;margin:0px;color:#333;border:#ececec solid 1px;width:100%;height:40px;background-color:white;'><table width='100%' height='100%'><tr><td id='divselect"+strWinId+"'></td></tr></table></div>"+
		"<table align='center'  width='100%'  class='bttoparea'><tr><td align='center'>"+
		"<a href=\"javascript:titleclose("+strWinId+");\" class='button red' onclick='this.blur();' style='float:right;'><span><div class='bttn_panel' style='background-image:url(images/eve/qx.png);'>关闭</div></span></a>"+
		"<a href=\"javascript:"+strWinId+"_frame.setFiledValue();titleclose("+strWinId+");\" class='button green' onclick='this.blur();' style='float:right;'><span><div class='bttn_panel' style='background-image:url(images/eve/qd.png);'>确定</div></span></a>"+
		"</td></tr></table>"
		+"</td><td class='win_right'></td></tr>";
	}
		
		
	// onmousedown='dragWin(this);'
	var strContent="<table width='"+aWidth+"px' height='"+aHeight+"px' border='0' align='center' cellpadding='0' cellspacing='0'><tr><td class='win_top_left' align='center'></td><td class='win_top_center'><table width='100%' height='100%' border='0'><tr><td class='win_title' id='win_title'>"+
	aTitle+"</td><td align='right' width='30px'><img src='images/content/close.png' style='cursor:hand;' onmouseover=\"this.src='images/content/close1.png';\" onmouseout=\"this.src='images/content/close.png';\" onclick='titleclose("+strWinId+")'></td></tr></table></td><td class='win_top_right'></td></tr><tr><td class='win_left'></td><td class='win_content' width='"+iContentWidth+"px' height='"+iContentHeight+"px'>"+
	strContent+"</td><td class='win_right'></td></tr>"+strWinOp+"<tr><td class='win_bottom_left'></td><td class='win_bottom_center'></td><td class='win_bottom_right'></td></tr></table>";
	
	bgObj.setAttribute('winWidth',aWidth);
	bgObj.setAttribute('winHeight',aHeight);
	
	bgObj.setAttribute('winTop',_top);

	bgObj.setAttribute('winLeft',_left);

	bgObj.setAttribute('id',strWinId);
	
	bgObj.style.position="absolute";
	bgObj.style.top=_top+"px";
	bgObj.className="mimiwin";
	bgObj.style.left=_left+"px";
	bgObj.style.zIndex = "100";
	//bgObj.style.backgroundColor = "red";
	
	bgObj.innerHTML=strContent;
	
	

	generBg(strWinId);
	document.body.appendChild(bgObj);
	return bgObj;
}
var iContentLeft=110;
var iContentTop=90;
function closeCtt(_obj){
	document.body.removeChild(_obj);
}
var sys_bIsFullScreen=false;
function fullScreen(){
	var docElm = document.documentElement;
	if (docElm.requestFullscreen) {  
		docElm.requestFullscreen();  
	}
	else if (docElm.mozRequestFullScreen) {  
		docElm.mozRequestFullScreen();  
	}
	else if (docElm.webkitRequestFullScreen) {  
		docElm.webkitRequestFullScreen();  
	}
	else if (elem.msRequestFullscreen) {
		elem.msRequestFullscreen();
	}
	sys_bIsFullScreen=true;
}
function exitFullScreen(){
	if (document.exitFullscreen) {  
		document.exitFullscreen();  
	}  
	else if (document.mozCancelFullScreen) {  
		document.mozCancelFullScreen();  
	}  
	else if (document.webkitCancelFullScreen) {  
		document.webkitCancelFullScreen();  
	}
	else if (document.msExitFullscreen) {
      document.msExitFullscreen();
	}
	sys_bIsFullScreen=false;
}
function maxCtt(_obj){
	var objContent=document.getElementById(_obj.id+"_content");
	var objWinContent=document.getElementById(_obj.id+"_win_content");	
	if(_obj.style.width=="100%"){		
		//var iWidth= parseFloat(_obj.getAttribute("winWidth"));
		//var iHeight=parseFloat(_obj.getAttribute("winHeight"));
		var iWidth= document.body.clientWidth-100;
		var iHeight=document.body.clientHeight-100;
		var iContentWidth=iWidth-26;
		var iContentHeight=iHeight-59;
		objContent.style.width=iWidth+"px";
		objContent.style.height=iHeight+"px";
		objWinContent.style.width=iContentWidth+"px";
		objWinContent.style.height=iContentHeight+"px";
		
		_obj.style.width=_obj.getAttribute("winWidth")+"px";
		_obj.style.height=_obj.getAttribute("winHeight")+"px";
		_obj.style.left=_obj.getAttribute("winLeft")+"px";
		_obj.style.top=_obj.getAttribute("winTop")+"px";
	}else{
		var iWidth= document.body.clientWidth;
		var iHeight=document.body.clientHeight;
		var iContentWidth=iWidth-26;
		var iContentHeight=iHeight-59;
		objContent.style.width=iWidth+"px";
		objContent.style.height=iHeight+"px";
		objWinContent.style.width=iContentWidth+"px";
		objWinContent.style.height=iContentHeight+"px";
		_obj.style.width="100%";
		_obj.style.height="100%";
		_obj.style.left="0";
		_obj.style.top="0";
	}
	//var objContent=document.getElementById(_obj.id+"_contentframe").lxmain.setGrid();
	
}
function minCtt(_obj){
	
}
function winCtt(_strTitle,_strContent){
	doWinCtt(_strTitle,_strContent,2,60,0,160);
}
function doWinCtt(_strTitle,_strContent,_iLeft,_iTop,_iSubWidth,_iSubHeight){
	var bgObj=document.createElement("div");
	var strWinId="sys_cttwin";
	if(document.getElementById(strWinId)!=null)
		closeCtt(sys_cttwin);
	var iLeft=_iLeft;
	var iTop=_iTop;
	var iWidth= document.body.clientWidth-_iSubWidth;
	var iHeight=document.body.clientHeight-_iSubHeight;
	
	var iContentWidth=iWidth-26;
	var iContentHeight=iHeight-30;
	
	// onmousedown='dragWin(this);'	
	var strContent="<table id='"+strWinId+"_content'  ondblclick='maxCtt("+strWinId+");' width='"+iWidth+"px' height='"+iHeight+"px' border='0' align='center' cellpadding='0' cellspacing='0'><tr><td class='win_top_left' align='center'></td><td class='win_top_center'><table width='100%' height='100%' border='0'><tr><td class='win_title' id='win_title'>"+
	_strTitle+"</td><td align='right'><img src='images/content/close.png' style='cursor:hand;' onmouseover=\"this.src='images/content/close1.png';\" onmouseout=\"this.src='images/content/close.png';\" onclick='closeCtt("+strWinId+")'></td></tr></table></td><td class='win_top_right'></td></tr><tr><td class='win_left'></td><td id='"+strWinId+"_win_content' class='win_content' width='"+iContentWidth+"px' height='"+iContentHeight+"px'>"+
	"<iframe  id='"+strWinId+"_contentframe' src='"+_strContent+"' width='100%' height='100%' frameborder='no' border='0' marginwidth='0' marginheight='0'></iframe>"+"</td><td class='win_right'></td></tr><tr><td class='win_bottom_left'></td><td class='win_bottom_center'></td><td class='win_bottom_right'></td></tr></table>";
	bgObj.setAttribute('winWidth',iWidth);
	bgObj.setAttribute('winHeight',iHeight);
	bgObj.setAttribute('winTop',iTop);
	bgObj.setAttribute('winLeft',iLeft);
	bgObj.setAttribute('id',strWinId);
	bgObj.style.position="absolute";
	bgObj.style.top=iTop+"px";
	bgObj.className="mimiwin";
	bgObj.style.left=iLeft+"px";
	bgObj.style.zIndex = "1";
	//bgObj.style.backgroundColor = "red";
	
	bgObj.innerHTML=strContent;

	document.body.appendChild(bgObj);
}


function wcct_Reload(){
testiframesys_cttwin.window.location.reload();
}







function winCtt_Sec(_strName,_strContent){
	initScreen();
	var bgObj=document.createElement("div");
	
	var strWinId="sys_cttwin1";
	if(document.getElementById(strWinId)!=null)
		closeCtt(sys_cttwin1);
	var iLeft=10;
	var iTop=10;
	var iWidth=iScreen_Width-20;
	var iHeight=iScreen_Height-80;
	
	//_strContent="ModMain";
	var strTitle="<table cellpadding='0' cellspacing='0' width='100%' height='36'>"+
	"<tr><td width='12' height='36'><img src='images/content/second_title_left.png'></td>"+
	"<td background='images/content/second_title_bg.png' width='100%' valign='middle'><table width='100%' border='0'><tr><td  class='winCttTitle' width='200'>"+_strName+"</td><td>&nbsp;</td>"+
	"<td width='30' align='right'></td>"+
	"<td width='30' align='right'></td>"+
	"<td width='30' align='right'><img style='cursor:hand;' onmouseover='this.src=\"images/content/min1.png\";' onmouseout='this.src=\"images/content/min.png\";' src='images/content/min.png' onclick='minCtt(sys_cttwin1);'></td>"+
	"<td width='30' align='right'><img style='cursor:hand;' onmouseover='this.src=\"images/content/max1.png\";' onmouseout='this.src=\"images/content/max.png\";' src='images/content/max.png' onclick='maxCtt(sys_cttwin1);'></td>"+
	"<td width='30' align='right'><img style='cursor:hand;' onmouseover='this.src=\"images/content/close1.png\";' onmouseout='this.src=\"images/content/close.png\";' src='images/content/close.png' onclick='closeCtt(sys_cttwin1);'></td>"+
	"</tr></table></td>"+
	"<td width='12' height='36'><img src='images/content/second_title_right.png'></td></tr></table>";
	
	var strIttleShadow="<table cellpadding='0' cellspacing='0' width='100%' height='6'>"+
	"<tr><td width='204' height='6'><img src='images/content/shadow_left.png'></td>"+
	"<td background='images/content/top_shadow.jpg' width='100%'></td>"+
	"<td width='16' height='6'><img src='images/content/right_shadow_top.png'></td></tr></table>";
	
	bgObj.setAttribute('winWidth',iWidth);
	bgObj.setAttribute('winHeight',iHeight);
	bgObj.setAttribute('winTop',iTop);
	bgObj.setAttribute('winLeft',iLeft);
	bgObj.setAttribute('id',strWinId);
	bgObj.style.position="absolute";
	bgObj.style.top=iTop+"px";
	bgObj.className="mimiwin";
	bgObj.style.left=iLeft+"px";
	bgObj.style.width=iWidth+"px";
	bgObj.style.height=iHeight+"px";
	bgObj.style.zIndex = "1";
	//bgObj.style.background="red";
		aStrContent="<table cellpadding='0' cellspacing='0' width='100%' height='100%' id='sysframe1'><tr>"+
		"<td width='5' background='images/content/content_left_line.png'></td>"+
		"<td id='sys_frame_td1'><iframe id='testiframe1"+strWinId+"' src='"+
		_strContent+"' height='100%' width='100%' frameborder='no' border='0' marginwidth='0' marginheight='0'></iframe></td>"+
		"<td width='16' background='images/content/center_right_shadow.png'></td>"+
		"</tr></table>";
		
	var strEnd="<table cellpadding='0' cellspacing='0' width='100%' height='22'>"+
	"<tr><td width='204' height='22'><img src='images/content/left_shadow_bottom.png'></td>"+
	"<td background='images/content/center_bottom_bg.png' width='100%'></td>"+
	"<td width='16' height='22'><img src='images/content/second_bottom_right.png'></td></tr></table>";
	
	bgObj.innerHTML="<table border='0' cellpadding='0' cellspacing='0' onmousedown='dragWin(this);' width='100%' height='100%'><tr><td height='36px'>"+
					strTitle+
					"</td></tr><tr><td height='6px'>"+
					strIttleShadow+
					"</td></tr><tr><td id='sys_contend_td1'>"+
					aStrContent+
					"</td></tr><tr><td height='22px'>"+
					strEnd+
					"</td></tr></table>";

	document.body.appendChild(bgObj);
	//alert(document.getElementById("sys_contend_td").);
	document.getElementById("sysframe1").height=document.getElementById("sys_contend_td1").clientHeight+"px";
	document.getElementById("sys_frame_td1").height=document.getElementById("sys_contend_td1").clientHeight+"px";
	document.getElementById("testiframe1"+strWinId).height=document.getElementById("sys_contend_td1").clientHeight+"px";
	//return bgObj;
}













function  doSysStyle1(_strInnerHTML){
	sys_dskStyleDiv=document.getElementById("sys_bg_pageevent");
					if(sys_dskStyleDiv==null){
					sys_dskStyleDiv = document.createElement("DIV");	
					sys_dskStyleDiv.setAttribute("id","sys_bg_pageevent");
					sys_dskStyleDiv.style.position = "absolute";
					sys_dskStyleDiv.style.zIndex = 100008;
					sys_dskStyleDiv.style.left = '0px';
					sys_dskStyleDiv.style.top = '0px';
					sys_dskStyleDiv.style.width = "717px";
					sys_dskStyleDiv.style.height = "900px";
					sys_dskStyleDiv.style.overflow = "hidden";
					sys_dskStyleDiv.style.background = "url(images/eve/sys_left_event.png)";
					sys_dskStyleDiv.style.backgroundPosition = "0px 0px";
					sys_dskStyleDiv.innerHTML="<div id='sys_style_view_content' style='width:0px;height:0px;overflow:hidden;'>"+_strInnerHTML+"</div>";
					document.body.appendChild(sys_dskStyleDiv);
					}
					sys_dskTimer=setInterval(drawPage,200);
}

var arrStep=[0,-717];
var arrStepWidth=[717,1211];
var arrStepContent=[[195,297],[604,636]];
var bStepType=true;
	var iStep=0;
	function drawPage(_strEveHtml){
		sys_dskStyleDiv.style.backgroundPosition=arrStep[iStep]+"px 0px";
		sys_dskStyleDiv.style.width=arrStepWidth[iStep]+"px";
		document.getElementById("sys_style_view_content").style.height=arrStepContent[iStep][1]+"px";
		document.getElementById("sys_style_view_content").style.width=arrStepContent[iStep][0]+"px";
		//alert(arrStepWidth[iStep]);
		if(bStepType)
			iStep++;
		else
			iStep--;
		if(iStep==2){
			clearInterval(sys_dskTimer);
			iStep=1;
			bStepType=false;
			//document.body.removeChild(sys_bg_pageevent);
			}
		if(iStep==-1){
			clearInterval(sys_dskTimer);
			iStep=0;
			bStepType=true;
			document.body.removeChild(sys_bg_pageevent);
			}
	}

var  sys_dskTimer;
var sys_dskStyleDiv ;








function getAjaxActive_Win(){
	var xmlHttp;
 if (window.ActiveXObject) { 
  xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
 } 
 else if (window.XMLHttpRequest) { 
  xmlHttp = new XMLHttpRequest();
 }
 return xmlHttp;
}
function getTx_Win(param,aStrUrl){
		var xml=getAjaxActive_Win();
		xml.open("POST",aStrUrl,false);
		xml.setRequestHeader("content-length",param.length);  
		xml.setRequestHeader("content-type","application/x-www-form-urlencoded");  
		xml.setRequestHeader("charset", "UTF-8");
		xml.send(param);  
		var res = xml.responseText;
		return res;
	}

function sys_doProgress(){
	var objStep=document.getElementById("sys_progress_step");
	var objStepMsg=document.getElementById("sys_progress_step_msg");
	var strStepMsg=getTx_Win("","progressstep");
	var iIndex=strStepMsg.indexOf(":");
	var iStep=strStepMsg.substring(0,iIndex);
	var strStep=strStepMsg.substring(iIndex+1);
	objStep.style.width=iStep+"%";
	var iViewStep=parseInt(iStep);
	objStep.innerHTML=iViewStep+"%";
	objStepMsg.innerHTML=strStep;
	//alert(iViewStep);
	if(iViewStep>=100)
		document.body.removeChild(document.getElementById("sys_divprogress"));
	else
		setTimeout("sys_doProgress()", 100);
}
function sys_generProgress(){
	var objPhotoBJ=document.createElement("div");
	objPhotoBJ.setAttribute('id','sys_divprogress');
	objPhotoBJ.style.position="absolute";
	objPhotoBJ.style.top="0";
	objPhotoBJ.style.background="white";
	objPhotoBJ.style.filter="alpha(opacity=80);  ";
	//objPhotoBJ.style.opacity="1.0";
	objPhotoBJ.style.opacity="0.9";
	objPhotoBJ.style.left="0";
	initScreen();
	objPhotoBJ.style.width=iScreen_Width+"px";
	objPhotoBJ.style.height=iScreen_Height+"px";
	objPhotoBJ.style.zIndex = "100002";
	//bgObj.style.zIndex = "100002";
	//objPhotoBJ.attachEvent("onclick" ,canclePhoto);
	objPhotoBJ.innerHTML="<table align='center' height='100%'><tr><td valign='middle'><div class='sys_web_progress'>"+
	"<div id='sys_progress_step' class='sys_web_progress_step'>0%</div>"+
	"</div><div class='sys_web_progress_caption' id='sys_progress_step_msg'></div></td></tr></table>";
	document.body.appendChild(objPhotoBJ);
	setTimeout("sys_doProgress()", 100);
}