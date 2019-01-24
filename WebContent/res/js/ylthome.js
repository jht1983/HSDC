var ylt= ylt ||{};
ylt.Home = ylt.Home || {};
(ylt.Home={
	objCurExpand:{},
	$:function(_strId){
		return document.getElementById(_strId);
	},
	getElementTop:function(_obj){ 
		var iOffset=_obj.offsetTop; 
		if(_obj.offsetParent!=null) iOffset+=this.getElementTop(_obj.offsetParent); 
		return iOffset; 
	},
	getElementLeft:function(_obj){ 
		var iOffset=_obj.offsetLeft; 
		if(_obj.offsetParent!=null) iOffset+=this.getElementLeft(_obj.offsetParent); 
		return iOffset; 
	}, 
	hiddenDlNode:function(_strNodeId,_objCurDlNode){
		_objCurDlNode.style.display="none";
		var objCurANode=this.$("a"+_strNodeId);
		objCurANode.className="menunode";
		objCurANode.childNodes[2].className="iconfont icon-xiala";
	},
	attachScrollEvent:function(_element){
		var isFirefox = (navigator.userAgent.indexOf("Firefox") !== -1);
		if(isFirefox){
			_element.addEventListener("DOMMouseScroll",wheelHandler,false);
		}
		_element.onmousewheel = wheelHandler;
		function wheelHandler(event){ event = event || window.event; var delta = event.wheelDelta || detail*-30;
			if(delta>0)
				_element.scrollTop-=25;
			else
				_element.scrollTop+=25;
		}
	},
	nodeMenuClick:function(_strParNodeId,_strNodeId){
		var strCurExpandId=this.objCurExpand[_strParNodeId];
		if(strCurExpandId!=null&&strCurExpandId!=_strNodeId){
			this.$("dl"+strCurExpandId).style.display="none";
			this.$("a"+strCurExpandId).className="menunode";
			this.$("a"+strCurExpandId).childNodes[2].className="iconfont icon-xiala";
		}
		var objCurDlNode=this.$("dl"+_strNodeId);
		
		if(objCurDlNode.style.display==""){
			this.hiddenDlNode(_strNodeId,objCurDlNode);
		}else{
			objCurDlNode.style.display="";
			if(_strParNodeId=="par"){
				var iMaxHeight=iScreen_Height-objCurDlNode.offsetTop-25;
				if(objCurDlNode.clientHeight>iMaxHeight){
					//objCurDlNode.style.height=iMaxHeight+"px";
				}else
					//objCurDlNode.style.height=(objCurDlNode.clientHeight-20)+"px";
				objCurDlNode.style.overflow="hidden";
				this.attachScrollEvent(objCurDlNode);
			}
			var objCurANode=this.$("a"+_strNodeId);
			objCurANode.className="menunode_sel";
			objCurANode.childNodes[2].className="iconfont icon-xiala2";
		}
		this.setExpandNode(_strParNodeId,_strNodeId);
		if(this.$("logomenucontainer").style.width=="38px"){
			this.openLeftMenu(this.$("tools_expand_bttn"));
		}
	},
	objCurLeafNode:null,
	leafNodeMenuClick:function(_obj,_strUrl){
		if(this.objCurLeafNode!=null){
			if(this.objCurLeafNode.className != 'span_ico_right'){
				this.objCurLeafNode.className="menunodeleaf";
			}
		}
		if(_strUrl == 'home.v'){
			_obj.className="span_ico_right";
		}else{
			_obj.className="menunodeleaf_sel";
		}
		this.objCurLeafNode=_obj;
		window.frames["framehome"].location=_strUrl;
	},
	setExpandNode:function(_strParNodeId,_strNodeId){
		this.objCurExpand[_strParNodeId]=_strNodeId;
	},
	objCurOpenMenu:null,
	openLeftMenu:function(_obj){
		this.$("logomenucontainer").style.width="220px";
		this.$("leftcontainer").style.width="220px";
		this.$("logocontainer").childNodes[0].style.maxWidth="220px";
		_obj.childNodes[0].className="iconfont icon-menufold";
		this.$("sys_div_msg_tip").style.display="none";
	},
	expandLeftMenu:function(_obj){
		if(this.$("logomenucontainer").style.width=="38px"){
			this.openLeftMenu(_obj);
			if(this.objCurOpenMenu!=null){
				this.nodeMenuClick("par",this.objCurOpenMenu.id.substring(2));
			}
		}else{
			this.$("logomenucontainer").style.width="38px";
			this.$("leftcontainer").style.width="38px";
			this.$("logocontainer").childNodes[0].style.maxWidth="38px";
			_obj.childNodes[0].className="iconfont icon-menuunfold";
			
			var strCurExpandId=this.objCurExpand["par"];
			if(strCurExpandId!=null){
				var objCurDlNode=this.$("dl"+strCurExpandId);
				if(objCurDlNode.style.display==""){
					this.hiddenDlNode(strCurExpandId,this.$("dl"+strCurExpandId));
					this.$("a"+strCurExpandId).className="menunode_lit_sel";
					this.objCurOpenMenu=objCurDlNode;
				}else
					this.objCurOpenMenu=null;
			}
		}
	},
	fullScreen:function(){
		var isFullscreen=document.fullScreen||document.mozFullScreen||document.webkitIsFullScreen;
		
		var docElm = document.documentElement;
		if(!isFullscreen){
				(docElm.requestFullscreen&&docElm.requestFullscreen())||
				(docElm.mozRequestFullScreen&&docElm.mozRequestFullScreen())||
				(docElm.webkitRequestFullscreen&&docElm.webkitRequestFullscreen())||
				(docElm.msRequestFullscreen&&docElm.msRequestFullscreen());
		}else{
			document.exitFullscreen?document.exitFullscreen():
			document.mozCancelFullScreen?document.mozCancelFullScreen():
			document.webkitExitFullscreen?document.webkitExitFullscreen():'';
		}
	},
	mainMenuOver:function(_obj){
		var objDivTip=this.$("sys_div_msg_tip");
		var iCurObjTop=this.getElementTop(_obj);
		var objMenuContainer=this.$("logomenucontainer");
		if(objMenuContainer.style.width=="38px"){
			objDivTip.innerHTML="<div class=\"yl-tipbox-direction yl-tipbox-left\"><em>&#9670;</em><span>&#9670;</span></div>"+
			"<p>"+_obj.childNodes[0].childNodes[1].innerHTML+"<p>";
			
			objDivTip.style.top=(iCurObjTop-10)+"px";
			objDivTip.style.left=(this.getElementLeft(_obj)+objMenuContainer.clientWidth)+"px";
			objDivTip.style.display="";
		}
		this.$("sys_div_rect_tip").style.left="0px";
		//this.$("sys_div_rect_tip").style.top=iCurObjTop+"px";
		this.$("sys_div_rect_tip").style.display="";
		ylt.Tools.animate(this.$("sys_div_rect_tip"), {top: iCurObjTop}, 100);
	},
	mainMenuOut:function(_obj){
		this.$("sys_div_msg_tip").style.display="none";
		this.$("sys_div_rect_tip").style.display="none";
	},
	getPosition:function(_objSel){
		var objPos=_objSel.getBoundingClientRect();
		var objDocument=_objSel.ownerDocument;
		var iScrollTop=Math.max(objDocument.documentElement.scrollTop,objDocument.body.scrollTop);
		var iScrollLeft=Math.max(objDocument.documentElement.scrollLeft,objDocument.body.scrollLeft);
		var x=objPos.left+iScrollLeft;
		var y=objPos.top+iScrollTop;
		return {left:x,top:y};
	},
	getPositionEx:function(_window){
		var objCurWin=_window;
		var objPos={left:0,top:0};
		
		while(objCurWin!=objCurWin.parent){
			
			if(objCurWin.frameElement){
				var objParentPos=this.getPosition(objCurWin.frameElement);
				//alert(objParentPos.left+":"+objParentPos.top);
				objPos.left+=objParentPos.left;
				objPos.top+=objParentPos.top;
			};
			//objCurWin.frameElement.style.display="none";
			var iScrollLeft=Math.max(objCurWin.document.body.scrollLeft,objCurWin.document.documentElement.scrollLeft);
			var iScrollTop=Math.max(objCurWin.document.body.scrollTop,objCurWin.document.documentElement.scrollTop);
			objPos.left-=iScrollLeft;
			objPos.top-=iScrollTop;
			objCurWin=objCurWin.parent;
		};
		//alert(objPos.x+","+objPos.y);
		return objPos;
	},
	showTipByMouseXY:function(_window,_obj){
		var objTip=_obj.getAttribute("tip-data");
		if(objTip==null)
			return;
		var objDivTip=this.$("sys_div_msg_tip");
		objDivTip.innerHTML="<div class=\"yl-tipbox-direction yl-tipbox-down\"><em>&#9670;</em><span>&#9670;</span></div>"+
		"<p>"+objTip+"<p>";
		objDivTip.style.display="";
		var posXy=this.getPositionEx(_window);
		_obj.style.filter="sepia(60%)";
		objDivTip.style.top=(posXy.top+this.getElementTop(_obj)-objDivTip.clientHeight-20)+"px";
		objDivTip.style.left=(posXy.left+this.getElementLeft(_obj)+(_obj.clientWidth-objDivTip.clientWidth)/2)+"px";
		
		
	},
	tipOut:function(_obj){
		this.$("sys_div_msg_tip").style.display="none";
		_obj.style.filter="";
	},
	toolsOver:function(_obj){
		var objDivTip=this.$("sys_div_msg_tip");
		objDivTip.innerHTML="<div class=\"yl-tipbox-direction yl-tipbox-up\"><em>&#9670;</em><span>&#9670;</span></div>"+
		"<p>"+_obj.getAttribute("tip-data")+"<p>";
		objDivTip.style.top=(this.getElementTop(_obj)+_obj.clientHeight)+"px";
		objDivTip.style.left=(this.getElementLeft(_obj)-20)+"px";
		objDivTip.style.display="";
	},
	toolsOut:function(_obj){
		this.$("sys_div_msg_tip").style.display="none";
	},
	
	bttnClick:function(_obj,_iType){
		switch(_iType){
			case 1:
				this.expandLeftMenu(_obj);				
				break;
			case 2:
				window.frames["framehome"].location="home.v";				
				break;
			case 3:
				window.frames["framehome"].location="dohome?hometype=init";			
				//window.frames["framehome"].location.reload();			
				break;
			case 4:
				ylt.Tools.openRightMenu("<div style='color:#6c6c6c;padding-left:10px;border-bottom:1px solid #ededed;line-height:50px;'>\u6d88\u606f</div>");
				break;
			case 5:
				ylt.Tools.openRightMenu("<div style='color:#6c6c6c;padding-left:10px;border-bottom:1px solid #ededed;line-height:50px;'>\u98ce\u683c</div>");
				break;
			case 6:
				this.fullScreen();			
				break;
			case 7:
				window.location="logindx.v";		
				break;
			case 8:
				winBox("\u4fee\u6539\u5934\u50cf","umsg.v",350,230);	
				break;
			case 9:
				winBox("\u4fee\u6539\u5bc6\u7801","Menu?O_SYS_TYPE=changelogin",380,230);	
				break;
		}
	},
	onload:function(){

		window.onresize=window.onload=function(){
			initScreen();
			//ylt.Tools.$("framehome").style.height=(iScreen_Height-50)+"px";
			var objDisableDiv=ylt.Tools.$("sys_disable_poplevel");
			if(objDisableDiv!=null){
				objDisableDiv.style.width=iScreen_Width+"px";
				//objDisableDiv.style.height=iScreen_Height+"px";
			}
			objDisableDiv=ylt.Tools.$("sys_div_msg_right");
			if(objDisableDiv!=null){
				//objDisableDiv.style.height=(iScreen_Height-51)+"px";
			}
			var winHeight = document.documentElement.clientHeight - 80 + "px";
			//alert(winHeight);
			document.getElementById("leftmenucontainer").style.height = winHeight;
			/*document.getElementByClassName("w220").height = winHeight;
			document.getElementByClassName("w220")[0];
			$(".w220").addCss("height",winHeight);*/
		}
	}
}).onload();

function getDate(){
	
}

//定时器每一秒触发一次
setInterval(function(){
	var date = new Date();
	//获取时间
	var time = 0;
	var month = Number(date.getMonth()) + 1;
	var day = Number(date.getDate());
	var hours = Number(date.getHours());
	var minutes = Number(date.getMinutes());
	var seconds = Number(date.getSeconds());
	if(month < 10){
		month = "0" + month;
	}
	if(day < 10){
		day = "0" + day;
	}
	if(hours < 10){
		hours = "0" + hours;
	}
	if(minutes < 10){
		minutes = "0" + minutes;
	}
	if(seconds < 10){
		seconds = "0" + seconds;
	}
	time = date.getFullYear() + '-' + month + "-" + day + " " + hours+":"+minutes+":"+seconds;
	//添加进入div
	var Timediv = document.getElementById("Show_Time");
	Timediv. innerHTML=(time);
}, 1000);


