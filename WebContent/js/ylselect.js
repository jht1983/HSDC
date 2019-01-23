var ylt= ylt ||{};
ylt.Select = ylt.Select || {};
var yltSelect=ylt.Select={
	bIsOverOption:false,
	paraString:"",
	objOtherChild:new Object(),
	objPopWin:null,
	objPopWinPos:null,
	getPosition:function(_objSel){
		var objPos=_objSel.getBoundingClientRect();
		var objDocument=_objSel.ownerDocument;
		var iScrollTop=Math.max(objDocument.documentElement.scrollTop,objDocument.body.scrollTop);
		var iScrollLeft=Math.max(objDocument.documentElement.scrollLeft,objDocument.body.scrollLeft);
		var x=objPos.left+iScrollLeft;
		var y=objPos.top+iScrollTop;
		return {left:x,top:y};
	},
	getPositionEx:function(){
		var objCurWin=window;
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
		//alert(objPos.left+":"+objPos.top);
		this.objPopWinPos=objPos;
		this.objPopWin=objCurWin;
	},
	setPos:function(_objSel,_objOption) {
		this.getPositionEx();
		_objOption.style.height="";
		var iOptionWidth=_objOption.offsetWidth;
		this.objPopWin.document.body.appendChild(_objOption);
		
		var objSelPanel=$(_objSel.id+"panel");
		
		var objPos=this.getPosition(objSelPanel);
		var iOptionLeft=objPos.left+this.objPopWinPos.left;
		
		_objOption.style.left=iOptionLeft+"px";
		var iOptionTop=objPos.top+this.objPopWinPos.top+_objSel.offsetHeight+1;
		_objOption.style.top=iOptionTop+"px";
		_objOption.style.zIndex="200000";
		var iClientBottomHeight=this.objPopWin.document.body.clientHeight-iOptionTop;
		var iClientRightWidth=this.objPopWin.document.body.clientWidth-iOptionLeft;
		var iOptionHeight=_objOption.offsetHeight;
		//debug.innerHTML=iOptionHeight;
		if(iOptionHeight>iClientBottomHeight){
			var iClientTopHeight=iOptionTop-_objSel.offsetHeight;
			if(iClientTopHeight>iClientBottomHeight){
				_objOption.style.height=iClientTopHeight+"px";
				_objOption.style.top="0px";
			}else{
				_objOption.style.height=iClientBottomHeight+"px";
			}
		}else
			_objOption.style.height=iOptionHeight+"px";
		
		var iSelWidth=objSelPanel.offsetWidth;
		if(iOptionWidth<iSelWidth){
			_objOption.style.width=iSelWidth+"px";
			var objOptionChild=_objOption.childNodes[0];
			if(objOptionChild.tagName=="TABLE")
				objOptionChild.style.width="100%";
		}else{
				
			if(iOptionWidth>iClientRightWidth){
			
				_objOption.style.left=(iOptionLeft+_objSel.offsetWidth-iOptionWidth)+"px";
				_objOption.style.width=iOptionWidth+"px";
			}
		
		}
	
		
		_objOption.style.overflowY="auto";
		
	
	},
	setValue:function(_strId,_objEvent){
		this.setSelValue(_strId,_objEvent.innerText,_objEvent.attributes["code"].nodeValue);
	},
	setSelValue:function(_strId,_strTxt,_strValue){
		var _objViewInput=$(_strId+"_viewinput");
		
		var _objSel=$(_strId);
		_objViewInput.value=_strTxt;
		_objSel.text=_objViewInput.value;
		var _strOldValue=_objSel.value;
		_objSel.value=_strValue;
		yltSelect.hiddenSelWin(_strId);
		if(_objSel.onchange!=null&&_strOldValue!=_objSel.value)_objSel.onchange();
	},
	setSelValueById:function(_strId,_strValue){
		var _objViewInput=$(_strId+"_viewinput");
		var _objSel=$(_strId);
		var _strTxt=_objSel.keyValueText[_strValue];
		_objViewInput.value=_strTxt;
		_objSel.text=_objViewInput.value;
		var _strOldValue=_objSel.value;
		_objSel.value=_strValue;
		yltSelect.hiddenSelWin(_strId);
		if(_objSel.onchange!=null&&_strOldValue!=_objSel.value)_objSel.onchange();
	},
	initOptions:function(_objTrigger,_arrStrText,_arrStrCode){
		var strTexts="<table border='0' cellpadding='2' cellspacing='0' width='100%'>";
		
		var strCurValue=_objTrigger.value;
		var strFirstValue="";
		var iCodeSize=_arrStrCode.length;
		if(iCodeSize<1){
			$(_objTrigger.id+"_viewinput").value="";
				_objTrigger.text="";
				strFirstValue="";
		}
		
		var bIsSelect=false;
		var objTextValueKey=new Object();
		for(var i=0;i<iCodeSize;i++){
			objTextValueKey[_arrStrCode[i]]=_arrStrText[i];
			if(i==0){
				$(_objTrigger.id+"_viewinput").value=_arrStrText[i];
				_objTrigger.text=_arrStrText[i];
				strFirstValue=_arrStrCode[i];
			}else{
				if(strCurValue==_arrStrCode[i]){
					$(_objTrigger.id+"_viewinput").value=_arrStrText[i];
					_objTrigger.text=_arrStrText[i];
					bIsSelect=true;
				}
			}
			strTexts+="<tr><td  class='optionOut' style='white-space: nowrap;font-size:13px;' code='"+_arrStrCode[i]+"' onmouseover='this.style.background=\"#d7d7d7\";' onmouseout='this.style.background=\"white\"' onclick='document.getElementById(\""+_objTrigger.id+"_$$\").parentWin.yltSelect.setValue(\""+_objTrigger.id+"\",this);'>"+_arrStrText[i]+"</td></tr>";
		}
		if(!bIsSelect)_objTrigger.value=strFirstValue;
		strTexts+="</table>";
		_objTrigger.keyValueText=objTextValueKey;
		return strTexts;
	},
	setSelMutValue:function(_strId,_strTxt,_strValue,_bIsChecked){
		var _objViewInput=$(_strId+"_viewinput");
		
		var _objSel=$(_strId);
		
		if(_bIsChecked==1){
			if(_objSel.value==""){
				_objViewInput.value=_strTxt;//显示
				_objSel.text=_objViewInput.value;//值文本框
				_objSel.value=_strValue;//值文本框
			}else{
				_objViewInput.value=_objViewInput.value+","+_strTxt;//显示
				_objSel.text=_objViewInput.value;//值文本框
				_objSel.value=_objSel.value+","+_strValue;//值文本框
			}
		}else{
			var arrStrValue=_objSel.value.split(",");
			var arrStrText=_objViewInput.value.split(",");
			var iValueCount=arrStrValue.length;
			var strSplit="";
			var strNewValue="";
			var strNewText="";
			for(var i=0;i<iValueCount;i++){
				if(arrStrValue[i]!=_strValue){
					strNewValue+=strSplit+arrStrValue[i];
					strNewText+=strSplit+arrStrText[i];
					strSplit=",";
				}
			}
			_objViewInput.value=strNewText;//显示
			_objSel.text=_objViewInput.value;//值文本框
			//var _strOldValue=_objSel.value;
			_objSel.value=strNewValue;//值文本框
			
		}
		//yltSelect.hiddenSelWin(_strId);
		//if(_objSel.onchange!=null&&_strOldValue!=_objSel.value)_objSel.onchange();
	},
	clickTree:function(_strId,_objNode){
		//this.setSelMutValue(_strId,_objNode.attributes.text,_objNode.attributes.nodeCode,_objNode.checked);
		this.setSelValue(_strId,_objNode.attributes.text,_objNode.attributes.nodeCode);
	},
	clickCheckTree:function(_strId,_objNode){
		this.setSelMutValue(_strId,_objNode.attributes.text,_objNode.attributes.nodeCode,_objNode.checked);
		//this.setSelValue(_strId,_objNode.attributes.text,_objNode.attributes.nodeCode);
	},
	popSelWin:function(_strSelId){
		var objOption=$$$(_strSelId);
		if(objOption!=null){
			objOption.style.display="";
			this.setPos($(_strSelId+"_viewinput"),objOption);
		};
		objOption.parentWin=window;
	},
	hiddenSelWin:function(_strSelId){
	if(this.objPopWin!=null){
		var objOption=this.objPopWin.document.getElementById(_strSelId+"_$$");
		if(objOption!=null){
			objOption.style.display='none';
			document.body.appendChild(objOption);
		}
	}
	},
	_addEvent:function(_strSelId){
		var objViewSel=$(_strSelId+"_viewinput");
		var objViewSelPan=$(_strSelId+"_viewinputpanel");
		var bIsEdit=true;
		
		
		if(!!$(_strSelId).attributes["readOnly"])
			bIsEdit=false;
		if(bIsEdit)
			objViewSelPan.onclick=function(){yltSelect.popSelWin(_strSelId);objViewSel.focus();};
		else{
			objViewSel.style.background="#f1f1f1";
		}
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
						
														objViewSel.onclick=$$(_strSelId).onclick=function(){};
									
													}else{
															objViewSel.className="";
											
															objViewSel.onclick=$$(_strSelId).onclick=function(){yltSelect.popSelWin(_strSelId);objViewSel.focus();};
													}
											};
		$$$(_strSelId).onmouseover=function(){yltSelect.bIsOverOption=true;};
		$$$(_strSelId).onmouseout=function(){yltSelect.bIsOverOption=false;$(_strSelId+"_viewinput").focus();};
		//alert($(_strSelId+"_viewinput").onchange);		
	},
	generOption:function(_strId,_objSelect,_fWidth){//option
		
			objOption=generElement("DIV",_strId+"_$$",0,0);
			objOption.style.position="absolute";
			objOption.style.background="white";
			objOption.style.border="1px solid #d7d7d7";
		//objOption.className="selOption";
			objOption.style.display="none";
			objOption.style.cursor="hand";
		//objOption.left="1px";
		//objOption.innerHTML=this.initOptions($(_strId));
		return objOption;
	},
	_createPanel:function(_strId){
		var objSelect=$(_strId);
		var  fSelectWidth=objSelect.offsetWidth;
		var strSelWidth=objSelect.style.width
		if(strSelWidth.indexOf("%")!=-1)
			fSelectWidth=strSelWidth;

		

		var objViewInputPanel=generElement("Div",_strId+"_viewinputpanel",fSelectWidth,0);
		objViewInputPanel.className="selInputpanel";
		
		var objViewInput=generElement("INPUT",_strId+"_viewinput",fSelectWidth,0);
		objViewInput.readOnly=true;
		
		objViewInputPanel.appendChild(objViewInput);
		
		objSelect.parentNode.insertBefore(objViewInputPanel,objSelect);
		objViewInput.className="selInput";
	
		
		var objOption=$$$(_strId);
		if(objOption==null){
			objOption=this.generOption(_strId,objSelect,fSelectWidth);
			document.body.appendChild(objOption);
		
		
		var arrStrText;
		var arrStrCode;
		
		if(!!objSelect.attributes["seltype"]){
			var selType=objSelect.attributes["seltype"].nodeValue;
			if(selType=="dic"){
				eval(getTx("sys_type=dic&sys_dictype="+objSelect.attributes["dataid"].nodeValue,gs_root+"/comp"));
				objOption.innerHTML=this.initOptions(objSelect,arrStrText,arrStrCode);
			}else if(selType=="datatree"){
				var strIsMutSel="";
				if(objSelect.attributes["bismut"]!=null)
					strIsMutSel="&sys_is_mut=true";
				
				eval(getTx("sys_type=datatree&sys_dataid="+
							objSelect.attributes["dataid"].nodeValue+"&sys_nodeid="+
							objSelect.attributes["nodecode"].nodeValue+"&sys_nodepid="+
							objSelect.attributes["nodepcode"].nodeValue+"&sys_nodenm="+
							objSelect.attributes["nodename"].nodeValue+"&sys_randerid="+
							objOption.id+"&sys_selid="+_strId+"&sys_selvalue="+objSelect.value+this.paraString+strIsMutSel,gs_root+"/comp"));
			}else if(selType=="data"){
			
				var strScript=getTx("sys_type=data&sys_dataid="+
							objSelect.attributes["dataid"].nodeValue+"&sys_nodeid="+
							objSelect.attributes["nodecode"].nodeValue+"&sys_nodenm="+
							objSelect.attributes["nodename"].nodeValue+"&sys_randerid="+
							objOption.id+"&sys_selid="+_strId+"&sys_selvalue="+objSelect.value,gs_root+"/comp");
							
				eval(strScript);
							
							
				
							
				objOption.innerHTML=this.initOptions(objSelect,arrStrText,arrStrCode);			
							
			}
		}else{
			arrStrText=objSelect.attributes["texts"].nodeValue.split(",");
			arrStrCode=objSelect.attributes["codes"].nodeValue.split(",");
			objOption.innerHTML=this.initOptions(objSelect,arrStrText,arrStrCode);
		}
		}
	
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
				objSels[objNodePCode.nodeValue]=e;//所有参数下拉框
				
			var objParentPCode=e.attributes["selparent"];
			if(!!objParentPCode)
				objParent[objParentPCode.nodeValue]=e;	//所有子级下拉框
				
				
			this.initSel(e.id);
		}
		
		for(var i in objParent){
			var objParentSel=objSels[i];//当前下拉框的父下拉框
			
			if(objParentSel!=null){
				objParentSel.setAttribute("selchild",objParent[i].id);
				objParentSel.onchange=function(){ylselectChildSel(this);};//父级下拉框注册变更事件
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
	};
	function ylselectCheckChildSel(_objParent,_objChild){
		var strSYS_PAR_PARAM_VALUE="&SYS_PAR_PARAM_VALUE="+_objParent.value;
		var strParam="viewname="+_objChild.viewname+"&sys_type=check&sys_dataid="+_objChild.id+strSYS_PAR_PARAM_VALUE+yltSelect.paraString;
		_objChild.innerHTML=getTx(strParam,gs_root+"/comp");
	}
	function ylselectChildSel(_objParent){
		var objSelChild=$(_objParent.attributes["selchild"].nodeValue);
		yltSelect.changeOption(objSelChild,_objParent.value);
		var objAttrChild=objSelChild.attributes["selchild"];
		if(objAttrChild!=null)
			ylselectChildSel(objSelChild);
	}