var ylt= ylt ||{};
ylt.Select = ylt.Select || {};
var yltSelect=ylt.Select={
	bIsOverOption:false,
	paraString:"",
	objStartWindow:parent.parent.parent,
	objOtherChild:new Object(),
	setPos:function(_objSel,_objOption) {
		var objPos=_objSel.getBoundingClientRect();
		var iClientHeight=this.objStartWindow.document.body.clientHeight;
		
		var iBottomHeight=iClientHeight-objPos.bottom;
		var iOptionHeight=_objOption.offsetHeight;
		if(iBottomHeight>objPos.top){
			_objOption.style.top  = _objSel.offsetTop+_objSel.offsetHeight;
			if(iOptionHeight>iBottomHeight){
				_objOption.style.height=iBottomHeight;
				_objOption.style.overflowY="scroll";
			}	
		}else if(iOptionHeight<iBottomHeight)
				_objOption.style.top  = _objSel.offsetTop+_objSel.offsetHeight;
			else{ if(iOptionHeight>objPos.top){
					_objOption.style.height=objPos.top;
					_objOption.style.overflowY="scroll";
					_objOption.style.top  = _objSel.offsetTop-objPos.top;
					}else
					_objOption.style.top  = _objSel.offsetTop-iOptionHeight;
				}
	},
	setValue:function(_strId,_objEvent){
		this.setSelValue(_strId,_objEvent.innerHTML,_objEvent.attributes["code"].nodeValue);
	},
	setSelValue:function(_strId,_strTxt,_strValue){
		var _objViewInput=$(_strId+"_viewinput");
		var _objSel=$(_strId);
		_objViewInput.innerHTML=_strTxt;
		_objSel.text=_objViewInput.innerText;
		var _strOldValue=_objSel.value;
		_objSel.value=_strValue;
		yltSelect.hiddenSelWin(_strId);
		if(_objSel.onchange!=null&&_strOldValue!=_objSel.value)_objSel.onchange();
	},
	initOptions:function(_objTrigger,_arrStrText,_arrStrCode){
		var strTexts="<table border='0' width='100%'  cellpadding='2' cellspacing='0'>";
		
		var strCurValue=_objTrigger.value;
		var strFirstValue="";
		var iCodeSize=_arrStrCode.length;
		if(iCodeSize<1){
			$(_objTrigger.id+"_viewinput").innerHTML="";
				_objTrigger.text="";
				strFirstValue="";
		}
		
		var bIsSelect=false;
		for(var i=0;i<iCodeSize;i++){
			if(i==0){
				$(_objTrigger.id+"_viewinput").innerHTML=_arrStrText[i];
				_objTrigger.text=_arrStrText[i];
				strFirstValue=_arrStrCode[i];
			}else{
				if(strCurValue==_arrStrCode[i]){
					$(_objTrigger.id+"_viewinput").innerHTML=_arrStrText[i];
					_objTrigger.text=_arrStrText[i];
					bIsSelect=true;
				}
			}
			strTexts+="<tr><td  class='optionOut' code='"+_arrStrCode[i]+"' onmouseover='this.className=\"optionOver\";' onmouseout='this.className=\"optionOut\"' onclick='yltSelect.setValue(\""+_objTrigger.id+"\",this);'>"+_arrStrText[i]+"</td></tr>";
		}
		if(!bIsSelect)_objTrigger.value=strFirstValue;
		strTexts+="</table>";
		return strTexts;
	},
	clickTree:function(_strId,_objNode){
		this.setSelValue(_strId,_objNode.attributes.text,_objNode.attributes.nodeCode);
	},
	getPosition:function(_objSel){
		var objPos=_objSel.getBoundingClientRect();
		var objDocument=_objSel.ownerDocument;
		var iScrollTop=Math.max(objDocument.documentElement.scrollTop,objDocument.body.scrollTop);
		var iScrollLeft=Math.max(objDocument.documentElement.scrollLeft,objDocument.body.scrollLeft);
		var x=objPos.left+iScrollLeft;
		var y=objPos.top+iScrollTop;
		//alert(x+":"+objPos.top);
		return {left:x,top:y};
	},
	getPositionEx:function(_objSel,_objOption){
		var objCurWin=window;
		var objPos=this.getPosition(_objSel);
		
		while(objCurWin!=objCurWin.parent){
			if(objCurWin.frameElement){
				var objParentPos=this.getPosition(objCurWin.frameElement);
				//alert(objParentPos.left+":"+objParentPos.top);
				objPos.left+=objParentPos.left;
				objPos.top+=objParentPos.top;
			}
			//objCurWin.frameElement.style.display="none";
			var iScrollLeft=Math.max(objCurWin.document.body.scrollLeft,objCurWin.document.documentElement.scrollLeft);
			var iScrollTop=Math.max(objCurWin.document.body.scrollTop,objCurWin.document.documentElement.scrollTop);
			//alert(iScrollTop+":"+iScrollLeft);
			objPos.left-=iScrollLeft;
			objPos.top-=iScrollTop;
			objCurWin=objCurWin.parent;
		}
		//alert(objPos.left+":"+objPos.top);
		_objOption.style.left=objPos.left+"px";
		_objOption.style.top=(objPos.top+_objSel.clientHeight)+"px";
		//alert(_objOption.style.left+":"+_objOption.style.top);
		_objSel.objOptionWindow=objCurWin;
		objCurWin.document.body.appendChild(_objOption);
	},
	generOption:function(_strId,_fWidth){//option
		var objOption=generElement("DIV",_strId+"_$$",_fWidth,0);
		objOption.style.position="absolute";
		objOption.className="selOption";
		//objOption.style.display="none";
		objOption.style.border="1px solid blue";
		objOption.style.zIndex="200000";
		
		
		var objViewSel=$(_strId+"_viewinput");
		
		
		objOption.style.height="300px";
		//objOption.style.left="100px";
		
		return objOption;
	},
	popSelWin:function(_strSelId){
		var objOption=this.generOption(_strSelId,200);
		var objSelect=$(_strSelId);
		var objViewSel=$(_strSelId+"_viewinput");
		
		var arrStrText;
		var arrStrCode;
		
		
		
		
		if(!!objSelect.attributes["seltype"]){
			var selType=objSelect.attributes["seltype"].nodeValue;
			if(selType=="dic"){
				eval(getTx("sys_type=dic&sys_dictype="+objSelect.attributes["dataid"].nodeValue,gs_root+"/comp"));
				objOption.innerHTML=this.initOptions(objSelect,arrStrText,arrStrCode);
			}else if(selType=="datatree"){
			
				eval(getTx("sys_type=datatree&sys_dataid="+
							objSelect.attributes["dataid"].nodeValue+"&sys_nodeid="+
							objSelect.attributes["nodecode"].nodeValue+"&sys_nodepid="+
							objSelect.attributes["nodepcode"].nodeValue+"&sys_nodenm="+
							objSelect.attributes["nodename"].nodeValue+"&sys_randerid="+
							objOption.id+"&sys_selid="+_strSelId+"&sys_selvalue="+objSelect.value+this.paraString,gs_root+"/comp"));
			}else if(selType=="data"){
				eval(getTx("sys_type=data&sys_dataid="+
							objSelect.attributes["dataid"].nodeValue+"&sys_nodeid="+
							objSelect.attributes["nodecode"].nodeValue+"&sys_nodenm="+
							objSelect.attributes["nodename"].nodeValue+this.paraString,gs_root+"/comp"));
							
				objOption.innerHTML=this.initOptions(objSelect,arrStrText,arrStrCode);			
							
			}
		}else{
			arrStrText=objSelect.attributes["texts"].nodeValue.split(",");
			arrStrCode=objSelect.attributes["codes"].nodeValue.split(",");
			objOption.innerHTML=this.initOptions(objSelect,arrStrText,arrStrCode);
		}
		
		//alert(objViewSel);
	this.getPositionEx(objViewSel,objOption);
	
		//this.setPos($(_strSelId+"__$"),objOption);
		
	},
	hiddenSelWin:function(_strSelId){
	var objViewSel=$(_strSelId+"_viewinput");
	objViewSel.objOptionWindow.document.body.removeChild(objViewSel.objOptionWindow.document.getElementById(_strSelId+"_$$"));
	/**	$$$(_strSelId).style.display='none';
		$(_strSelId+"_panel1").className="selInput";
		var ObjPanel=$p(_strSelId);
		ObjPanel.style.zIndex="100000";
	**/	
	},
	_addEvent:function(_strSelId){
		var objViewSel=$(_strSelId+"_viewinput");
		objViewSel.onclick=function(){yltSelect.popSelWin(_strSelId);objViewSel.focus();};
		objViewSel.onblur=function(){if(!yltSelect.bIsOverOption)yltSelect.hiddenSelWin(_strSelId);};	
		
		
		$(_strSelId).setValue=function(_strCodes){
															var objOptionTb=$$$(_strSelId).childNodes[0];
															var objRows=objOptionTb.rows;
															var iRowCount=objRows.length;
															for(var i=0;i<iRowCount;i++)
																if(objRows[i].cells[0].attributes["code"].value==_strCodes){
																	yltSelect.setSelValue(_strSelId,objRows[i].cells[0].innerText,_strCodes);
																	break;
																}	
														  };
		$(_strSelId).setEnabled=function(_bIsEnable){
												$(_strSelId).disabled=_bIsEnable;
												if(!_bIsEnable){
														objViewSel.className="selInputdisable";
														objViewSel.onclick=function(){};
													}else{
															objViewSel.className="";
												
															objViewSel.onclick=function(){yltSelect.popSelWin(_strSelId);objViewSel.focus();};
			
													}
											};
//		$$$(_strSelId).onmouseover=function(){yltSelect.bIsOverOption=true;};	
//		$$$(_strSelId).onmouseout=function(){yltSelect.bIsOverOption=false;$(_strSelId+"_viewinput").focus();};
		
//		alert($(_strSelId+"_viewinput").onchange);	
		
	},
	generPanel:function(_strId,_objSelect,_fWidth,_fHeight){
		var objPanel=generElement("TABLE",_strId+"_panel1",_fWidth,_fHeight);
		objPanel.cellSpacing ="0";
		objPanel.cellPadding ="0";
		objPanel.className="selInput";
		return objPanel;
	},
	
	_createPanel:function(_strId){
		var objSelect=$(_strId);
		
		var  fSelectWidth=objSelect.offsetWidth;
		var  fSelectHeight=objSelect.offsetHeight;
		if(objSelect.style.width!="")
			fSelectWidth=parseFloat(objSelect.style.width.replace("px",""));
		if(objSelect.style.height!="")
			fSelectHeight=parseFloat(objSelect.style.height.replace("px",""));
		if(fSelectHeight==0)fSelectHeight=20;
	
		
		
		
		
		var objViewInput=generElement("DIV",_strId+"__$",fSelectWidth,fSelectHeight);
		objViewInput.style.position='relative';
		objViewInput.innerHTML="<div type='text' id='"+_strId+"_viewinput' style='width:100%;height:100%;border:1px solid red;' readonly='true'></div>";
	
		objSelect.parentNode.insertBefore(objViewInput,objSelect);
		objSelect.style.display="none";
	},
	initSel:function(_strId){
		this._createPanel(_strId);
		this._addEvent(_strId);
	},
	changeOption:function(objSelect,_parentValue){
		var _strId=objSelect.id;
		var arrStrText;
		var arrStrCode;
		var selType=objSelect.attributes["seltype"].nodeValue;
		var strSYS_PAR_PARAM_VALUE="&SYS_PAR_PARAM_VALUE="+_parentValue;
		var  objOption=$$$(_strId);
		objSelect.value="";
			if(selType=="datatree"){
				eval(getTx("sys_type=datatree&sys_dataid="+
							objSelect.attributes["dataid"].nodeValue+"&sys_nodeid="+
							objSelect.attributes["nodecode"].nodeValue+"&sys_nodepid="+
							objSelect.attributes["nodepcode"].nodeValue+"&sys_nodenm="+
							objSelect.attributes["nodename"].nodeValue+"&sys_randerid="+
							objOption.id+"&sys_selid="+_strId+"&sys_selvalue="+objSelect.value+strSYS_PAR_PARAM_VALUE+this.paraString,gs_root+"/comp"));
			}else if(selType=="data"){			
				/**eval(getTx("sys_type=data&sys_dataid="+
							objSelect.attributes["dataid"].nodeValue+"&sys_nodeid="+
							objSelect.attributes["nodecode"].nodeValue+"&sys_nodenm="+
							objSelect.attributes["nodename"].nodeValue+"&sys_randerid="+
							objOption.id+"&sys_selid="+_strId+"&sys_selvalue="+objSelect.value+strSYS_PAR_PARAM_VALUE,gs_root+"/comp"));**/
							
				eval(getTx("sys_type=data&sys_dataid="+
							objSelect.attributes["dataid"].nodeValue+"&sys_nodeid="+
							objSelect.attributes["nodecode"].nodeValue+"&sys_nodenm="+
							objSelect.attributes["nodename"].nodeValue+strSYS_PAR_PARAM_VALUE+this.paraString,gs_root+"/comp"));
				objOption.innerHTML=this.initOptions(objSelect,arrStrText,arrStrCode);			
							
			}
	},
	addChild:function(_strParentId,_objChild){
		this.objOtherChild[_strParentId]=_objChild;
	},
	init:function(_strId){
		var arrSels=getElementsByClassName(document,"ylselect");
		var n = arrSels.length;
		var objSels=new Object();
		var objParent=new Object();
		
		var url = location.href; 
		this.paraString = url.substring(url.indexOf("?")+1,url.length);
		if(this.paraString!="")this.paraString="&"+this.paraString;
		
		
		
		for (var i = 0; i < n; i++) {
			var e = arrSels[i];	
			var objNodePCode=e.attributes["selcode"];
			if(!!objNodePCode)
				objSels[objNodePCode.nodeValue]=e;
				
			var objParentPCode=e.attributes["selparent"];
			if(!!objParentPCode)
				objParent[objParentPCode.nodeValue]=e;	
				
				
			this.initSel(e.id);
		}
		for(var i in objParent){
			var objParentSel=objSels[i];
			if(objParentSel!=null){
				objParentSel.onchange=function(){ylselectChildSel(this,objParent[i]);};
				objParentSel.onchange();
			}
		}
		
		for(var i in this.objOtherChild){//下拉复选子类
			var objParentSel=objSels[i];
			if(objParentSel!=null){
				objParentSel.onchange=function(){ylselectCheckChildSel(this,yltSelect.objOtherChild[i]);};
				objParentSel.onchange();
			}
		
		}
		
		
	}
	}
	function ylselectCheckChildSel(_objParent,_objChild){
		var strSYS_PAR_PARAM_VALUE="&SYS_PAR_PARAM_VALUE="+_objParent.value;
		var strParam="viewname="+_objChild.viewname+"&sys_type=check&sys_dataid="+_objChild.id+strSYS_PAR_PARAM_VALUE+yltSelect.paraString;
		_objChild.innerHTML=getTx(strParam,gs_root+"/comp");
	}
	function ylselectChildSel(_objParent,_objChild){
		yltSelect.changeOption(_objChild,_objParent.value);
	}