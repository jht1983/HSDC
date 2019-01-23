var ylt= ylt ||{};
ylt.Form = ylt.Form || {};
var ylForm=ylt.Form={
		objCurSelect:null,
		excelModClick:function (_obj){
			if(this.objCurSelect!=null)
				this.objCurSelect.style.backgroundColor=this.objCurSelect.strOldColor;
			_obj.style.backgroundColor="green";
			this.objCurSelect=_obj;
		},
		excelModOver:function(_obj){
			if(_obj.strOldColor==null)
				_obj.strOldColor=_obj.style.backgroundColor;
			if(_obj.style.backgroundColor!="green")
				_obj.style.backgroundColor="pink";
			_obj.style.cursor="hand";
		},
		excelModOut:function(_obj){
			if(_obj.style.backgroundColor!="green")
				_obj.style.backgroundColor=_obj.strOldColor;
			_obj.style.cursor="";
		},
		excelModDbClick:function(_obj){
			parent.miniWin('插入控件','','comp?sys_type=generitem&id='+_obj.id,400,230,'','');
			//this.doInsertItem(_iType);
		},
		saveFrom:function(_strFromId){
			alert(parent.getTx("","comp?sys_type=saveform&sys_formid="+_strFromId));
		},
		changeKJLx:function(_strValue,_objSJCD,_objSJLX,_trxlz,_trxlmc){
			if(_strValue=="1"){
			
			}else if(_strValue=="4"){
			
				_trxlz.style.display="";
				_trxlmc.style.display="";
			}else if(_strValue=="2"){
				_objSJCD.value="800";
				_objSJLX.value="4";
				$(_objSJLX.id+"_viewinput").innerHTML="4";
			}else{
				_objSJCD.value="20";
				_objSJLX.value="1";
			}
		},
		doInsertItem:function(_iType,_strName){
			switch(_iType){
				case 1://单行
					this.objCurSelect.innerHTML="<div style='background-image:url(images/form/input.png);border:0;width:120;height:21;'>"+_strName+"</div>";
					break;
				case 2://多行
					this.objCurSelect.innerHTML="<div style='background-image:url(images/form/textarea.png);border:0;width:120;height:63;'>"+_strName+"</div>";
					break;
				case 3://日期
					this.objCurSelect.innerHTML="<div style='background-image:url(images/form/date.png);border:0;width:123;height:21;'>"+_strName+"</div>";
					break;
				case 4://下拉
					this.objCurSelect.innerHTML="<div style='background-image:url(images/form/select.png);border:0;width:122;height:23;'>"+_strName+"</div>";
					break;
				case 5://宏
					this.objCurSelect.innerHTML="<div style='background-image:url(images/form/input.png);border:0;width:120;height:21;'>[宏]"+_strName+"</div>";
					break;
				case 6://明细
					this.objCurSelect.innerHTML="<div style='background-image:url(images/form/input.png);border:0;width:120;height:21;'>[明细]"+_strName+"</div>";
					break;
			}
		}
	}
window.onload=function(){eval(parent.getTx("","getformmsg.jsp"))};


	