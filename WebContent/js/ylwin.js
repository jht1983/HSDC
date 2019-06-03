function dragWin(oDrag, handle){
    var disX = dixY = 0;
    handle = handle || oDrag;
    //handle.style.cursor = "move";
    handle.onmousedown = function (event){
        var event = event || window.event;
        disX = event.clientX - oDrag.offsetLeft;
        disY = event.clientY - oDrag.offsetTop;
        document.onmousemove = function (event){
            var event = event || window.event;
            var iL = event.clientX - disX;
            var iT = event.clientY - disY;
            var maxL = document.documentElement.clientWidth - oDrag.offsetWidth;
            var maxT = document.documentElement.clientHeight - oDrag.offsetHeight;
            iL <= 0 && (iL = 0);
            iT <= 0 && (iT = 0);
            iL >= maxL && (iL = maxL);
            iT >= maxT && (iT = maxT);
            oDrag.style.left = iL + "px";
            oDrag.style.top = iT + "px";
            return false
        };
        document.onmouseup = function (){
            document.onmousemove = null;
            document.onmouseup = null;
            this.releaseCapture && this.releaseCapture()
        };
        this.setCapture && this.setCapture();
        return false
    };
}
//操作对话框
function win(aUrl){
	return miniWin('\u53ea\u8d4b\u503c\u4e00\u6b21','',aUrl,380,300,'','');
}
function win500(aUrl){
	return miniWin('\u53ea\u8d4b\u503c\u4e00\u6b21','',aUrl,650,300,'','');
}
function win700(aUrl){
	return miniWin('\u53ea\u8d4b\u503c\u4e00\u6b21','',aUrl,650,520,'','');
}
//查询条件
function winQuery(aUrl){
	return miniWin('\u67e5\u8be2\u6761\u4ef6','',aUrl,380,200,'','');
}
//上传图片
function winPic(aUrl){
	return miniWin('\u4e0a\u4f20\u56fe\u7247','',aUrl,350,100,'','');
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
	return miniWinAll(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle,iLeft,iTop,1);
}
function openSel(_strParam){
	var arrPrams=_strParam.split("|");
	var aWidth=parseInt(arrPrams[2]);
	var aHeight=parseInt(arrPrams[3]);
	var iTop=(iScreen_Height-aHeight)/2;
	var iLeft=(iScreen_Width-aWidth)/2;	
	
	objSelValues["win"+winId]={};
	return miniWinAll(arrPrams[0],"",
					"View?SPAGECODE="+arrPrams[1]+
					"&sys_select_type="+arrPrams[4]+"&RETURN=_eventsys_getOpenSelValues",aWidth,aHeight,"","",iLeft,iTop,1);
}
var objSelValues={};
function sys_delSelValues(_strId,_strWid){
	var objViewDiv=document.getElementById("divselect"+_strWid);
	var objViewLabel=document.getElementById(_strId);
	if(objViewLabel!=null)
			objViewDiv.removeChild(objViewLabel);
	var strSelValue=_strId.substring(9);
	var arrSelValus=objSelValues[_strWid][strSelValue];
	
	var iSelDataCell=parseInt(arrSelValus[arrSelValus.length-1]);
	var objContentWin=document.getElementById(_strWid+'_frame').contentWindow;
	var objTab=objContentWin.document.getElementById("tb");	
	if(objTab==null)
		objTab=objContentWin.lxmain.document.getElementById("tb");	
	var arrRows=objTab.rows;
	var iRowCount=arrRows.length;
	var strSelValueText=arrSelValus[0];
	for(var i=0;i<iRowCount;i++){
		var objRow=arrRows[i];
		var objCell=objRow.cells[iSelDataCell];
		if(objCell==null)
			continue;
		if(objCell.innerText==strSelValueText){
			objRow.cells[0].childNodes[0].checked=false;
			objRow.className=objRow.sys_cur_query_trclass;
			break;
		}
	}
	delete objSelValues[_strWid][strSelValue];
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
function miniWinAll_3D(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle,_left,_top,_iType){
	initScreen();
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
	
if(aWidth>iScreen_Width){
		aWidth=iScreen_Width;
		_left=0;
	}
	if(aHeight>iScreen_Height){
		aHeight=iScreen_Height;
		_top=0;
	}	
	
	var iContentWidth=aWidth-26;
	var iContentHeight=aHeight-49;
	var strContent="<iframe id='"+strWinId+"_frame' name='"+strWinId+"_frame' src='"+aUrl+"' width='100%' height='100%' frameborder='no' border='0' marginwidth='0' marginheight='0'></iframe>";
	if(aUrl=="")
		strContent=aContent;
		
		
	var strWinOp="";	
	if(_iType==1){
		aHeight=aHeight-20;
		iContentHeight=iContentHeight-120;
		strWinOp="<tr><td class='win_left'></td><td  width='"+iContentWidth+"px' height='50px' align='center'>"+
		"<div style='font-family:\u5fae\u8f6f\u96c5\u9ed1;font-size:11px;color:#666666;background-color:white;text-align:left;width:100%;height:20px;'>&nbsp;&nbsp;\u9009\u4e2d\u9879\u76ee</div><div class='mutseldiv'><table width='100%' height='100%'><tr><td id='divselect"+strWinId+"'></td></tr></table></div>"+
		"<table align='center'  width='100%'  class='bttformoparea'><tr><td align='center'>"+
		"<a href=\"javascript:titleclose("+strWinId+");\" class='button red' onclick='this.blur();' style='float:right;'><span><div class='bttn_panel' style='background-image:url(images/eve/qx.png);'>\u5173\u95ed</div></span></a>"+
		"<a href=\"javascript:sys_doSetValues('"+strWinId+"_frame');titleclose("+strWinId+");\" class='button green' onclick='this.blur();' style='float:right;'><span><div class='bttn_panel' style='background-image:url(images/eve/qd.png);'>\u786e\u5b9a</div></span></a>"+
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
	dragWin(bgObj,bgObj);
	return bgObj;
}




function miniWinAll(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle,_left,_top,_iType){
	initScreen();
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
	
if(aWidth>iScreen_Width){
		aWidth=iScreen_Width-100;
		_left=50;
	}
	if(aHeight>iScreen_Height){
		aHeight=iScreen_Height-50;
		_top=25;
	}	
	
	var iContentWidth=aWidth-26;
	var iContentHeight=aHeight-45;
	var strContent="<iframe id='"+strWinId+"_frame' name='"+strWinId+"_frame' src='"+aUrl+"' width='100%' height='100%' frameborder='no' border='0' marginwidth='0' marginheight='0' scrolling='auto' style='overflow:auto !important;'></iframe>";
	
	if(aUrl=="")
		strContent=aContent;
		
		
	var strWinOp="";	
	if(_iType==1){
		aHeight=aHeight-20;
		iContentHeight=iContentHeight-120;
		strWinOp="<tr><td></td><td  width='"+iContentWidth+"px' height='50px' align='center'>"+
		"<div style='font-family:\u5fae\u8f6f\u96c5\u9ed1;font-size:11px;color:#666666;background-color:white;text-align:left;width:100%;height:20px;'>&nbsp;&nbsp;\u9009\u4e2d\u9879\u76ee</div><div class='mutseldiv'><table width='100%' height='100%'><tr><td id='divselect"+strWinId+"'></td></tr></table></div>"+
		"<table align='center'  width='100%'  class='bttformoparea'><tr><td align='center'>"+
		"<a href=\"javascript:titleclose("+strWinId+");\" class='button red' onclick='this.blur();' style='float:right;'><span><div class='bttn_panel' style='background-image:url(images/eve/qx.png);'>\u5173\u95ed</div></span></a>"+
		"<a href=\"javascript:sys_doSetValues('"+strWinId+"_frame');titleclose("+strWinId+");\" class='button green' onclick='this.blur();' style='float:right;'><span><div class='bttn_panel' style='background-image:url(images/eve/qd.png);'>\u786e\u5b9a</div></span></a>"+
		"</td></tr></table>"
		+"</td><td></td></tr>";
	}
		
		
	// onmousedown='dragWin(this);'
	var strContent="<table width='"+aWidth+"px' height='"+aHeight+"px' border='0' align='center' cellpadding='0' cellspacing='0'><tr id='editpagetitle'><td style='height:40px;' colspan='3'><table width='100%' height='100%' border='0' cellpadding='0' cellspacing='0'><tr>"+
	//"<td style='width:8px;'><img src='images/eve/add.png?v=6.77' valign='middle'></td>"+
	"<td class='win_title' style='padding-left:28px;' id='win_title'>"+
	aTitle+"</td><td align='left' width='43px'><img src='images/content/close3.png' style='cursor:pointer;' onmouseover=\"this.src='images/content/close4.png?v=1';\" onmouseout=\"this.src='images/content/close3.png';\" onclick='titleclose("+strWinId+")'></td></tr></table></td></tr><tr><td width='13'></td><td class='win_content' width='"+iContentWidth+"px' height='"+iContentHeight+"px'>"+
	strContent+"</td><td width='13'></td></tr>"+strWinOp+"<tr><td></td><td class='win_bottom_center'></td><td></td></tr></table>";
	
	bgObj.setAttribute('winWidth',aWidth);
	bgObj.setAttribute('winHeight',aHeight);
	
	bgObj.setAttribute('winTop',_top);

	bgObj.setAttribute('winLeft',_left);

	bgObj.setAttribute('id',strWinId);
	bgObj.style.boxShadow="0px 0px 15px #333";
	//bgObj.style.border="1px solid red";
	bgObj.style.background="#fff";
	bgObj.style.borderRadius="10px";
	bgObj.style.position="absolute";
	bgObj.style.top=_top+"px";
	bgObj.className="mimiwin";
	bgObj.style.left=_left+"px";
	bgObj.style.zIndex = "100";
	bgObj.style.overflow = "hidden";
	//bgObj.style.backgroundColor = "red";
	
	bgObj.innerHTML=strContent;
	
	

	generBg(strWinId);
	document.body.appendChild(bgObj);
	dragWin(bgObj,bgObj);
	return bgObj;
}














function sys_doSetValues(_strFrame){
	var objContentWin=document.getElementById(_strFrame).contentWindow;
	var objTab=objContentWin.document.getElementById("tb");	
	if(objTab==null)
		objContentWin=objContentWin.lxmain;
	objContentWin.setFiledValue();
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
		document.getElementById("sys_style_view_content").style.height="1px";//arrStepContent[iStep][1]+
		document.getElementById("sys_style_view_content").style.width="1px";//arrStepContent[iStep][0]+
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