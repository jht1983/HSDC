var objOpen=parent.parent.parent.parent;
var objOldFile=null;
var strCurElementId="";
function win(aUrl){
	objOpen.miniWin('操作对话框','',aUrl,380,300,'','').objOpen=window;
}
function win500(aUrl){
	objOpen.miniWin('操作对话框','',aUrl,650,300,'','').objOpen=window;
}
function win700(aUrl){
	objOpen.miniWin('操作对话框','',aUrl,650,520,'','').objOpen=window;
}
function win1000(aUrl){
	objOpen.miniWin('操作对话框','',aUrl,1200,450,'','').objOpen=window;
}
function winQuery(aUrl){
	objOpen.miniWin('查询条件','',aUrl,380,200,'','').objOpen=window;
}
function winPic(aUrl){
	objOpen.miniWin('上传图片','',aUrl,350,100,'','').objOpen=window;
}
function messageBox(aTitle,aContent){
	objOpen.miniWin(aTitle,aContent,"",350,150,"titleclose","").objOpen=window;
}
function messageBoxCon(aTitle,aContent,aOk,aCancle){
	var objWindow=objOpen.miniWin(aTitle,aContent,"",350,150,aOk,aCancle);
	objWindow.objOpen=window;
	return objWindow;
}
function winBox(aTitle,aUrl,aWidth,aHeight){
	objOpen.miniWin(aTitle,"",aUrl,aWidth,aHeight,"","").objOpen=window;
}
function miniWin(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle){
	objOpen.miniWin(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle).objOpen=window;
}
function miniWinAll(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle,_left,_top){
	objOpen.miniWinAll(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle,_left,_top).objOpen=window;
}
function miniWin1(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle){
	objOpen.miniWin1(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle).objOpen=window;
}
function sys_openSelSinWin(_obj,aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle){
	var arrObjInputs=document.getElementsByName(_obj.getAttribute("name"));
	var iInputCount=arrObjInputs.length;
	var iNameIndex=0;
	for(var i=0;i<iInputCount;i++){
		if(arrObjInputs[i]==_obj){
			iNameIndex=i;
			break;
		}
	}
	aUrl+="&sys_name_index="+iNameIndex;
	objOpen.miniWin(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle).objOpen=window;
}
function sys_openSelMutWin(_obj,aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle){
	var arrObjInputs=document.getElementsByName(_obj.getAttribute("name"));
	var iInputCount=arrObjInputs.length;
	var iNameIndex=0;
	for(var i=0;i<iInputCount;i++){
		if(arrObjInputs[i]==_obj){
			iNameIndex=i;
			break;
		}
	}
	aUrl+="&sys_name_index="+iNameIndex;
	objOpen.miniWin1(aTitle,aContent,aUrl,aWidth,aHeight,aOnOK,aOnCancle).objOpen=window;
}
function autoWin(aTitle,aUrl){
	objOpen.autoWin(aTitle,aUrl).objOpen=window;
}
function openSel(_param){
	objOpen.openSel(_param).objOpen=window;
}
function sys_generProgress(){
	objOpen.sys_generProgress();
}
function generBg(_strWinId){
	function generBg(_strWinId){
var sWidth,sHeight;
	objPhotoBJ=document.createElement("div");
	objPhotoBJ.setAttribute('id',_strWinId+'_bgDiv');
	objPhotoBJ.style.position="absolute";
	objPhotoBJ.style.top="0";
	objPhotoBJ.style.background="#000";
	objPhotoBJ.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=20,finishOpacity=75,StartX=0,StartY=0,FinishX=1024,FinishY=1000";
	objPhotoBJ.style.opacity="0.6";
	objPhotoBJ.style.left="0";

	objPhotoBJ.style.width=iScreen_Width+"px";
	objPhotoBJ.style.height=iScreen_Height+"px";
	objPhotoBJ.style.zIndex = "100002";
	//bgObj.style.zIndex = "100002";
	//objPhotoBJ.attachEvent("onclick" ,canclePhoto);
	objPhotoBJ.innerHTML="<table width='100%' height='100%'><tr><td align='center' valign='middle'><div style=' width:500;height:20px;background: url(traspant.png) red;border:1px solid black;'><div style=' height:20px;width:50%;background: yellow;text-align:right;'>当前进度50%</div></td></tr></table>";
	document.body.appendChild(objPhotoBJ);
}
	
}