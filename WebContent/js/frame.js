var ylt= ylt ||{};
ylt.menuIco = ylt.menuIco || {};
var bIsViewOther=false;

yltMenuIco=ylt.menuIco={
	objMenuDiv:null,
	objContentDiv:null,
	objLeftTopDiv:null,
	objLeftBotDiv:null,
	objTabmsg:null,
	$:function(_strId){
		return document.getElementById(_strId);
	},
	bodyOnclick:function(event){
		if(event.srcElement.getAttribute("ismenu")==null)
			this.$("sys_div_msg").style.display="none";
	},
	init:function(){
		this.objMenuDiv=this.$("sys_div_msg");
		this.objContent=this.$("sys_td_menu_content");
		this.objLeftTop=this.$("sys_td_msgleftop");
		this.objLeftCenter=this.$("sys_td_msglefcenter");
		this.objLeftBot=this.$("sys_td_msglefbot");
		this.objTabmsg=this.$("sys_tab_msg");
		
		
	},
	menuClick:function(_event,_strModCode,_strModName){
		//winCtt('业务建模','md.jsp');
		var iMenuIcoHeight=69;
		if(this.objMenuDiv==null)
			this.init();
		//this.objContent.style.background="green";
		this.objContent.innerHTML=this.$("sys_chil"+_strModCode).innerHTML;
		this.objMenuDiv.style.display="";
		var iMenuDivHeight=0;
		if(_strModCode=="doother"){
			this.objTabmsg.style.width=600+"px";
			var chitabls=this.objContent.childNodes;
			var iTabCount=chitabls.length;
			for(var i=0;i<iTabCount;i++){
				var iChidCout=parseInt(chitabls[i].getAttribute("icount"));
				iMenuDivHeight+=parseInt(iChidCout/6)*iMenuIcoHeight;
				
				if(iChildCount%6!=0);
					iMenuDivHeight+=iMenuIcoHeight;
			}
			iMenuDivHeight=iMenuDivHeight-30+iTabCount*18;
			
		}else{
			this.objTabmsg.style.width=350+"px";
			var iChildCount=this.objContent.childNodes.length;
			iMenuDivHeight=parseInt(iChildCount/3)*iMenuIcoHeight-30;
			if(iChildCount%3!=0);
			iMenuDivHeight+=iMenuIcoHeight;
		}
		
	
		this.objLeftTop.style.height=iMenuDivHeight+"px";
		this.objMenuDiv.style.top=(_event.y-iMenuDivHeight-30)+"px";
		this.objLeftBot.style.height="30px";
		
	},
	doMenuChild:function(_strModCode,_strModName,_strUrl){
		doWinCtt(_strModName,_strUrl,100,80,100,100);
		this.objMenuDiv.style.display="none";
	},
	doTopMenu:function(_obj,_strUrl){
		window.frames['mainhomeframe'].location=_strUrl;
		_obj.className="selMenutab";
		if(this.objCurSelMenu!=null)
			this.objCurSelMenu.className="topmenutab";
		this.objCurSelMenu=_obj;
	},
	objCurSelMenu:null,
	doMenuChildCenter:function(_strModCode,_strModName,_strUrl){
		var strParentCode=_strModCode.substring(0,_strModCode.length-3);
		var iMenuCount=sys_arr_menu.length;		
		var strMenu="<span class='topmenutab'><img src='images/ico/home.png' class='topmenuico' onclick='window.location.reload();'></span>";
		var objCurMenu;
		var strSpanClass="topmenutab";
		for(var i=0;i<iMenuCount;i++){
			objCurMenu=sys_arr_menu[i];
			if(objCurMenu.pcode==strParentCode){
				if(objCurMenu.code==_strModCode)
					strSpanClass="selMenutab";
				else
					strSpanClass="topmenutab";
				strMenu+="<span id='menu"+objCurMenu.code+"' class='"+strSpanClass+"' "+
						 "onclick=\"yltMenuIco.doTopMenu(this,'"+
						objCurMenu.url
				+"');\">"+
				objCurMenu.name+"</span>";
			}
		}
		parent.document.getElementById("trmenucontainer").innerHTML=strMenu;
		parent.yltMenuIco.objCurSelMenu=parent.document.getElementById("menu"+_strModCode);
		window.location=_strUrl;
		//yltWinContent.openContent(_strUrl);
		
		//doWinCtt(_strModName,_strUrl,-5,130,-10,150);
		//this.objMenuDiv.style.display="none";
	},
	doMainMenuChild:function(_strModCode,_strModName,_strUrl){
		window.location=_strUrl;
	},
	doTopMainMenu:function(_obj,_strModCode){
		strCurSelManiMenuId="mainmenu"+_strModCode;
		var strHomeMenu="dohome?v=1";
		var strCurUrl=mainhomeframe.location+"";
		 if(strCurUrl.substring(strCurUrl.length-strHomeMenu.length)==strHomeMenu){
			mainhomeframe.document.getElementById("divcentmenu").innerHTML=document.getElementById(strCurSelManiMenuId).innerHTML;
		 }else{
			 document.getElementById("trsyssecmenu").style.display="";
			document.getElementById("divsyssecmenu").
			innerHTML=document.getElementById(strCurSelManiMenuId).innerHTML;
		}
	},
	menuItemClick:function(_iType){
		switch(_iType){
			case 1:
					window.location="login";			
					break;
			case 6:
					doSysStyle1("<table>"+
								"<tr>"+
								"<td><div onclick='yltMenuIco.setColor(\"#0a263c\");' style='background-color:#0a263c;width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setColor(\"#87012a\");' style='background-color:#87012a;width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setColor(\"#3a540c\");' style='background-color:#3a540c;width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setColor(\"#1fa01f\");' style='background-color:#1fa01f;width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"</tr>"+
								"<tr>"+
								"<td><div onclick='yltMenuIco.setColor(\"#323943\");' style='background-color:#323943;width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setColor(\"#88038a\");' style='background-color:#88038a;width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setColor(\"#ab4c07\");' style='background-color:#ab4c07;width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setColor(\"#0881b4\");' style='background-color:#0881b4;width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"</tr>"+
								"<tr>"+
								"<td><div onclick='yltMenuIco.setBackground(\"bg1\");' style='background:url(images/style/bgm1.jpg);width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setBackground(\"bg2\");' style='background:url(images/style/bgm2.jpg);width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setBackground(\"bg3\");' style='background:url(images/style/bgm3.jpg);width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setBackground(\"bg4\");' style='background:url(images/style/bgm4.jpg);width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"</tr>"+
								"<tr>"+
								"<td><div onclick='yltMenuIco.setBackground(\"bg5\");' style='background:url(images/style/bgm5.jpg);width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setBackground(\"bg6\");' style='background:url(images/style/bgm6.jpg);width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setBackground(\"bg7\");' style='background:url(images/style/bgm7.jpg);width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"<td><div onclick='yltMenuIco.setBackground(\"bg8\");' style='background:url(images/style/bgm8.jpg);width:100px;height:25px;cursor:pointer;float:left;border:5px solid white;' onmouseover='this.style.border=\"5px solid yellow\";' onmouseout='this.style.border=\"5px solid white\";'></div> </td>"+
								"</tr>"+
								"</table>");	
					if(this.objcurMainMenu!=null)
						this.objcurMainMenu.className="mainmenu";
					spanpersonworkpal.className="mainmenusel";
					this.objcurMainMenu=spanpersonworkpal;
					break;
			case 5:
					miniWin('修改密码','','Menu?O_SYS_TYPE=changelogin',400,300,'','');
					break;
			case 2:
					miniWin('设置默认项目','','View?SPAGECODE=1457611212331',500,600,'','');
					break;
			case 3:
					miniWin('刷新配置信息','','dohome?hometype=init',300,100,'','');				
					break;
			case 4:
					if(sys_bIsFullScreen)
						exitFullScreen();
					else
						fullScreen();
					break;
		}
	},
	toolItemClick:function(_iType,_obj){
		var iSrcLength=_obj.src.length;
		var strEnd=_obj.src.substring((iSrcLength-7),iSrcLength);
		switch(_iType){
			case 1:
					if(strEnd=="top.png"||strEnd=="aop.png"){
						sys_top_main_menu.style.display="none";
						_obj.src="images/frame/bot.png";
						var objContent=document.getElementById("framecontent");
						objContent.height=parseInt(objContent.height)+59;
					}else{
						sys_top_main_menu.style.display="";
						_obj.src="images/frame/top.png";
						var objContent=document.getElementById("framecontent");
						objContent.height=parseInt(objContent.height)-59;
					}
					break;
			case 2://top,bot,aop,aot
					if(strEnd=="top.png"){
						_obj.src="images/frame/aop.png";
					}else if(strEnd=="bot.png"){
						_obj.src="images/frame/aot.png";
					}
					break;
			case 3:	
					if(strEnd=="aop.png"){
						_obj.src="images/frame/top.png";
					}else if(strEnd=="aot.png"){
						_obj.src="images/frame/bot.png";
					}
					break;
		}
	},
	setColor:function(_strColor){
		this.setCookie("SYS_BACKGROUND_COLOR",_strColor);
		
		this.setCookie("SYS_BACKGROUND_PIC","");
		document.body.style.background="";
		document.body.style.background=_strColor;
	},
	setBackground:function(_strPic){
		var strPic=_strPic+".jpg";
		this.setCookie("SYS_BACKGROUND_PIC",strPic);
		document.body.style.background="url(images/style/"+strPic+")";
	},
	setCookie:function(_strName,_strValue){
		var Days = 30;
		var exp = new Date();
		exp.setTime(exp.getTime() + Days*24*60*60*1000);
		document.cookie = _strName+"="+_strValue+";expires=" + exp.toGMTString();
	},
	objCurChildMenu:null,
	objcurMainMenu:null,
	objCurChildDoMenu:null,
	frame10MainMenu:function(_strId,_obj){
		if(this.objCurChildMenu!=null)
			this.objCurChildMenu.style.display="none";
		
		var objChildMenu=document.getElementById("menu_"+_strId);
		if(objChildMenu!=null){
			objChildMenu.style.display="";
			this.objCurChildMenu=objChildMenu;
		}
		if(this.objcurMainMenu==null)
			spanpersonworkpal.className="mainmenu";
		else
			this.objcurMainMenu.className="mainmenu";
		_obj.className="mainmenusel";
		this.objcurMainMenu=_obj;
	},
	frame10ChildMenu:function(_strUrl,_obj){
		if(this.objCurChildDoMenu!=null)
			this.objCurChildDoMenu.style.color="#696969";
		framecontent.location=_strUrl;
		_obj.style.color="red";
		this.objCurChildDoMenu=_obj;
	},
	frame10ChildMenuOut:function(_obj){
		if(this.objCurChildDoMenu==_obj)
			_obj.style.color="red";
		else
			_obj.style.color="#696969";
	}
}


var i_Cur_MenuHeight=100;
	var i_sys_TimeWaite=50;
	var obj_cur_Menu=null;
	var obj_view_Menu=null;
	function do_ChildeMenuMove_View(){
	
		i_Cur_MenuHeight=i_Cur_MenuHeight+10;
		if(i_Cur_MenuHeight>100)
			i_Cur_MenuHeight=100;
		obj_view_Menu.style.height=i_Cur_MenuHeight+"px";
		if(i_Cur_MenuHeight<100)
			setTimeout("do_ChildeMenuMove_View()",i_sys_TimeWaite);
		else{
			i_Cur_MenuHeight=100;
			obj_cur_Menu=obj_view_Menu;
		}
	}
	
	
	function do_ChildeMenuMove(){
	
		i_Cur_MenuHeight=i_Cur_MenuHeight-10;
		if(i_Cur_MenuHeight<=0)
			i_Cur_MenuHeight=0;
		obj_cur_Menu.style.height=i_Cur_MenuHeight+"px";
		if(i_Cur_MenuHeight>0)
			setTimeout("do_ChildeMenuMove()",i_sys_TimeWaite);
		else{
			obj_cur_Menu.style.display="none";
			obj_view_Menu.style.display="";
			setTimeout("do_ChildeMenuMove_View()",i_sys_TimeWaite);
		}
	}
	function viewSecMenu(_strType){
		if(document.getElementById("sys_cttwin")!=null)
		closeCtt(sys_cttwin);
	
	
		if(i_Cur_MenuHeight>0&&i_Cur_MenuHeight<100)
			return;
		obj_view_Menu=document.getElementById("menu_"+_strType);
		if(obj_cur_Menu==null){
			i_Cur_MenuHeight=0;
			obj_view_Menu.style.display="";
			setTimeout("do_ChildeMenuMove_View()",i_sys_TimeWaite);
		}else
		if(obj_view_Menu==obj_cur_Menu)
			return;
		else
			setTimeout("do_ChildeMenuMove()",i_sys_TimeWaite);
	}
	function sys_Main_Menu_Over(_obj){
		sys_home_tip.style.display="";
		_obj.style.width='70px';
		sys_home_tip_content.innerText=_obj.attributes["mdname"].nodeValue;
		var  iX,iY;   
		var oRect   =   _obj.getBoundingClientRect(); 
		iX=oRect.left+35-sys_home_tip.clientWidth/2;
		iY=oRect.top-38;
		sys_home_tip.style.left=iX+"px";
		sys_home_tip.style.top=iY+"px";
		sys_home_tip.style.zIndex=1001;
	}
	function sys_Main_Menu_Out(_obj){
		_obj.style.width='60px';
		sys_home_tip.style.display="none";
	}
	function duMaxWin(_obj){
		var objTopLogo=document.getElementById("tabtrtoplogo");
		if(objTopLogo.style.display==""){
			objTopLogo.style.display="none";
			_obj.src="images/ico/down.png";
		}else{
			objTopLogo.style.display="";
			_obj.src="images/ico/up.png";
		}
	}
	