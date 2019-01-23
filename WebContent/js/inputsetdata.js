var nameSpace="@data";
var strPageCode="";//当前页面预览参数
var strParams="";//修改参数
var strTable="";//已加入表
var strItemCodes="";//已加入字段
var strItemNames="";//已加入字段名
var strItemTypes="";//已加入字段类型
var strItemLength="";//已加入字段长度
var strItemSelect="";//对应选择页面
var strReturnFields="";//回填字段
var strHCFields="";//回传字段
var strGetchecks="";//js验证
var strSize="";//页面大小
var iNewCode=1;
var objTableColors = new Object();

function insertFields(){
    aParent.strItemCodes+=","+"NO_CODE_"+aParent.iNewCode;
	aParent.strItemNames+=","+"新名称_"+aParent.iNewCode;
    aParent.strItemTypes+=","+"0";
    aParent.strItemLength+=","+"200";
	aParent.strItemSelect+=","+"0";
    aParent.strReturnFields+=","+"0";
	aParent.strGetchecks+=","+"0";
	aParent.strSize+=":"+"0,0";    //大小
	aParent.strHCFields+=","+"0";  //回传

	aParent.iNewCode++;
	/////修改页面表
		var strTempParams="&T_SYS_PAGEMSG$SFIELDCODE="+aParent.strItemCodes.substring(1)+
						  "&T_SYS_PAGEMSG$SFIELDNAME="+aParent.strItemNames.substring(1)+
						  "&T_SYS_PAGEMSG$SFIELDSIZE="+aParent.strItemLength.substring(1)+
						  "&T_SYS_PAGEMSG$SQUERYFIELD="+aParent.strItemSelect.substring(1)+

						   "&T_SYS_PAGEMSG$SGLFIELD="+aParent.strReturnFields.substring(1)+

						  "&T_SYS_PAGEMSG$SDELCON="+aParent.strItemTypes.substring(1)+   //数据类型
						  
						  "&T_SYS_PAGEMSG$STRANS="+aParent.strHCFields.substring(1)+   //回传

						  "&T_SYS_PAGEMSG$SHREFIELD="+aParent.strGetchecks.substring(1)+   //js验证

                          "&T_SYS_PAGEMSG$SSIZE="+aParent.strSize.substring(1);
		aParent.updateFields(strTempParams);
	////预览页面
		aParent.viewData();
		closeWin();
   
}

//T_SYS_PAGEMSG$SGLFIELD  回填字段
//T_SYS_PAGEMSG$SQERYCON  字段生成相关表
//T_SYS_PAGEMSG$SQLFIELD  表溶解流程
//T_SYS_PAGEMSG$SHREFIELD js验证
function getValue(_strCode,_strName){
	if(strTable.indexOf(_strCode)!=-1){
		alert("已存在该表！");
		return;
	}
	strTable=strTable+","+_strCode;
	alert(_strCode);
	var strTableData=getTx("systables="+_strCode,"getinputfields.jsp");
	alert(strTableData);
	eval(strTableData);//得到表字段
	if(strItemCodes!=""){

	/////修改页面表
		var strTempParams="&T_SYS_PAGEMSG$SFIELDCODE="+strItemCodes.substring(1)+
						  "&T_SYS_PAGEMSG$SFIELDNAME="+strItemNames.substring(1)+
						  "&T_SYS_PAGEMSG$SFIELDSIZE="+strItemLength.substring(1)+
						  "&T_SYS_PAGEMSG$SQUERYFIELD="+strItemSelect.substring(1)+
						  "&T_SYS_PAGEMSG$SGLFIELD="+strReturnFields.substring(1)+
						  "&T_SYS_PAGEMSG$SDELCON="+strItemTypes.substring(1)+
						  "&T_SYS_PAGEMSG$STRANS="+strHCFields.substring(1)+ 
			              "&T_SYS_PAGEMSG$SHREFIELD="+strGetchecks.substring(1)+
			              "&T_SYS_PAGEMSG$SSIZE="+strSize.substring(1);

		updateFields(strTempParams);
	////预览页面
		viewData();
	}
}
function initParams(_strPageCode){
	strPageCode="SPAGECODE="+_strPageCode;
	strParams="NO_charset=UTF-8&NO_OPTYPE=1&NO_CON=T_SYS_PAGEMSG$SPAGECODE&T_SYS_PAGEMSG$SPAGECODE="+_strPageCode;
}
function updateFields(_strParam){
	var strTableData=getTx(strParams+_strParam,"../YLWebAction");
}

function updateFields1(_strParam){
	var strTableData=getTx1(strParams+_strParam,"savafield.jsp");
}


function getTx1(param,aStrUrl){ 
		var xml = new ActiveXObject("Microsoft.XMLHTTP");
		xml.open("POST",aStrUrl,false);//以同步方式通信  
		xml.setrequestheader("content-length",param.length);  
		xml.setrequestheader("content-type","application/x-www-form-urlencoded");  
		xml.setRequestHeader("charset", "UTF-8");
		xml.send(param);  
		var res = xml.responseText;
		return res;
	}




function viewData(){
	getTx(strPageCode,"../sys/pardata.jsp");
	var resultHtml = getTx(strPageCode,"../View");
	//resultHtml =resultHtml.replace(/#/g,'&nbsp;');// add by yyl  #  替换为空格
	divtabledata.innerHTML=resultHtml;
}
function makeFields(){
	if(strItemCodes=="")
		alert("帅锅！还没选字段！^_^");
	else
		miniWin('字段管理','','input/makefields.jsp',1200,500,'','');
}


//////////////////////////////
var inputSelectOption="";
function init(){
		var newTR;
		var newNameTD;
		var arrFields=aParent.strItemCodes.substring(1).split(",");
		var arrFieldsNames=aParent.strItemNames.substring(1).split(",");
		var arrFieldTypes=aParent.strItemTypes.substring(1).split(",");
		var arrFieldLens=aParent.strItemLength.substring(1).split(",");
		var arrFieldSlects=aParent.strItemSelect.substring(1).split(",");
		var arrFieldReturns=aParent.strReturnFields.substring(1).split(",");
		var arrHCFields=aParent.strHCFields.substring(1).split(",");  //回传
		var arrGetcheck=aParent.strGetchecks.substring(1).split(",");
		var	arrSize=aParent.strSize.substring(1).split(":");
		
		
		var reg=new RegExp("&nbsp;","g");
		var strSlect="<OPTGROUP  label='普通类' style='color:red'><option value='0'>普通文本框</option>"+
					 "<option value='3'>只读文本框</option>"+
					 "<option value='1'>只读流水号</option>"+
					 "<option value='2'>只读递增</option></OPTGROUP>"+

					 "<OPTGROUP  label='隐藏类' style='color:blue'><option value='4$'>隐藏普通</option>"+
					 "<option value='4$GUID_'>隐藏流水号</option>"+
					 "<option value='4$INCREMOD_'>隐藏三位递增</option>"+						 
					 "<option value='4$RE_'>隐藏页面接收</option>"+
					 "<option value='4$GET_'>隐藏同步</option>"+
					 "<option value='4$PY_'>隐藏拼音</option></OPTGROUP>"+
					 					 
					 
					 "<OPTGROUP  label='下拉框类' style='color:green'><option value='D$'>字典下拉框</option>"+
					 "<option value='R$'>关联下拉框</option></OPTGROUP>"+

					 "<OPTGROUP  label='只读类' style='color:purple'><option value='5$RE_'>只读页面接收</option>"+
					 "<option value='5$GET_'>只读同步</option>"+
					 "<option value='5$RE_'>只读文本框</option></OPTGROUP>"+

					 "<OPTGROUP  label='富媒体类' style='color:red'><option value='6'>普通日期</option><option value='66'>日期到小时</option><option value='666'>日期到分钟</option>"+					 
					 "<option value='PIC'>单文件上传</option>"+
					  "<option value='MUP'>多文件上传</option>"+
					 "<option value='8'>多行文本框</option>"+
		     		 "<option value='9'>富文本框</option></OPTGROUP>"+
		     		 
		     		 "<OPTGROUP  label='其它类' style='color:green'><option value='P$'>参数</option></OPTGROUP>"
		;

		var strCheck="<option value='0'>可以为空</option>"+"<option value='bxtx'>不能为空</option>"+"<option value='bxsz'>必须是数字</option>"+
	        "<option value='bxsj'>必须是手机</option>"+
	        "<option value='bxdh'>必须是电话</option>"+
	        "<option value='bxem'>必须是EMAL</option>"+
			"<option value='bxsf'>必须是身份证号</option>"+
			"<option value='bncf'>不能重复</option>"+
			"<option value='zdybncf'>自定义不能重复</option>"+
			"<option value='bxyb'>必须是邮编</option>";

			
		var iArrFieldsLength=arrFields.length;
		
			
		for(var i=0;i<iArrFieldsLength;i++){
		
			var iIsTable=arrFields[i].indexOf("$");
			var tableColor="blue";
			if(iIsTable!=-1){
				var curTable=arrFields[i].substring(0,iIsTable);
				if(!!!objTableColors[curTable]){
					tableColor=generColor();

					objTableColors[curTable]=tableColor;
				}else
					tableColor=objTableColors[curTable];
			}
			inputSelectOption=inputSelectOption+"<option value='"+arrFields[i]+"' style='color:"+tableColor+"'>"+arrFieldsNames[i]+"</option>";
		
		}
		for(var i=0;i<iArrFieldsLength;i++){
			newTR = tbfields.insertRow(tbfields.rows.length);			
			newTR.id = "trfield"+i;
			newTR.attachEvent("onmouseover",onmsov(newTR));
			newTR.attachEvent("onmouseout",onmsot(newTR));
			newTR.RID=i;
			//if(i%2==0)
				newTR.className="tr1";//tr1over
			//else
			//	newTR.className="tr1over";//tr1over

			
			var iIsTable=arrFields[i].indexOf("$");
			var tableColor="blue";
			if(iIsTable!=-1){
				var curTable=arrFields[i].substring(0,iIsTable);
					tableColor=objTableColors[curTable];
			}
			
			
			newNameTD=newTR.insertCell(0);
			newNameTD.className="td1";
			newNameTD.id="tdField"+i;
			newNameTD.width="100";
			newNameTD.innerHTML="<input name='inputfield"+i+"' type='text' value='"+arrFields[i]+"'  style='width:100;color:"+tableColor+";'>";//字段
		

            if(arrFieldsNames[i].indexOf("&nbsp;")!=-1){
            arrFieldsNames[i]=arrFieldsNames[i].replace(reg,"&amp;nbsp;");
            }
			newNameTD=newTR.insertCell(1);	
			newNameTD.className="td1";
			newNameTD.width="100";
			newNameTD.innerHTML="<input name='inputfieldname"+i+"' type='text' value='"+arrFieldsNames[i]+"' style='width:100;color:"+tableColor+";'>";//名称

			newNameTD=newTR.insertCell(2);	
			newNameTD.className="td1";
			newNameTD.width="100";
			newNameTD.innerHTML="<select name='seltype"+i+"' onchange='inputType("+i+")' style='width:150;'>"+strSlect+"</select>";//strItemTypes;//类型
			


			newNameTD=newTR.insertCell(3);	
			newNameTD.id="tdvalue"+i;
			newNameTD.className="td1";
			newNameTD.width="80";
			newNameTD.innerHTML="";//值

			newNameTD=newTR.insertCell(4);				
			newNameTD.className="td1";
			newNameTD.width="50";
			newNameTD.innerHTML="<input name='inputlen"+i+"' style='width:50;' type='text' value='"+arrFieldLens[i]+"'>";//长度
////////
			newNameTD=newTR.insertCell(5);				
			newNameTD.className="td1";
			newNameTD.width="100";
			//newNameTD.innerHTML="<input name='"+i+"' style='width:100;' type='text' value='"+arrFieldSlects[i]+"'>";//选择//

			newNameTD.innerHTML="<select name='inputsel"+i+"'  style='width:100;'>"+pageoption.value+"</select>";//arrFieldTypes[i];//类型

			
           	newNameTD=newTR.insertCell(6);				
			newNameTD.className="td1";
			newNameTD.width="100";
			newNameTD.innerHTML="<input name='inputsize"+i+"' style='width:100;' type='text'  value='"+arrSize[i]+"'>";//大小


			newNameTD=newTR.insertCell(7);				
			newNameTD.className="td1";
			newNameTD.width="100";
			newNameTD.innerHTML="<input name='inputset"+i+"' style='width:100;' type='text' value='"+arrFieldReturns[i]+"'>";//回填


			newNameTD=newTR.insertCell(8);				
			newNameTD.className="td1";
			newNameTD.width="100";
			newNameTD.innerHTML="<select name='inputcheck"+i+"'   style='width:100;' >"+strCheck+"</select>";//JS验证

			newNameTD=newTR.insertCell(9);				
			newNameTD.className="td1";
			newNameTD.width="50";
			
			
			if(arrHCFields[i]!="0")
				newNameTD.innerHTML="<input name='inputput"+i+"' type='checkbox' value='1'  checked  style='width:30'>";  //回传
			else
				newNameTD.innerHTML="<input name='inputput"+i+"' type='checkbox' value='0'   style='width:30'>";
///////////
			newNameTD=newTR.insertCell(10);				
			newNameTD.className="td1";
			newNameTD.innerHTML="<button onclick='javascript:frontToTop(trfield"+i+")'>顶</button>&nbsp;&nbsp;<button onclick='javascript:frontTr(trfield"+i+")'>上</button>&nbsp;&nbsp;<button onclick='javascript:pushTr(trfield"+i+")'>下</button>&nbsp;&nbsp;<button onclick='javascript:pushBottom(trfield"+i+","+iArrFieldsLength+")'>底</button>&nbsp;&nbsp;<button onclick='javascript:delField(trfield"+i+")'>删</button>&nbsp;&nbsp;";//操作
			
			//inputSelectOption=inputSelectOption+"<option value='"+arrFields[i]+"'>"+arrFieldsNames[i]+"</option>";

			setAllSel(i,arrFieldTypes[i],arrFieldSlects[i]);
			getCheck(i,arrGetcheck[i]);
		}
	}
	function setAllSel(_iIndex,_strTypeValue,_strSelValue){
		var strTempValue="";
		if(_strTypeValue.indexOf("4$INCREMOD_")!=-1){
			strTempValue=_strTypeValue.substring(12);
			if(_strTypeValue.split("$").length==5){
				strTempValue=strTempValue.substring(0,strTempValue.indexOf("$"));
			}else{
				strTempValue=strTempValue.substring(0,strTempValue.indexOf("$"))+strTempValue.substring(strTempValue.lastIndexOf("$"));
			}
			_strTypeValue="4$INCREMOD_";			
		}else if(_strTypeValue.indexOf("4$RE_")!=-1){
			strTempValue=_strTypeValue.substring(5);
			_strTypeValue="4$RE_";			

		}else if(_strTypeValue.indexOf("4$GET_")!=-1){
			strTempValue=_strTypeValue.substring(6);
			_strTypeValue="4$GET_";

		}else if(_strTypeValue.indexOf("5$RE_")!=-1){
			strTempValue=_strTypeValue.substring(5);
			_strTypeValue="5$RE_";

		}else if(_strTypeValue.indexOf("4$PY_")!=-1){
			strTempValue=_strTypeValue.substring(5);
			_strTypeValue="4$PY_";

		}else if(_strTypeValue.indexOf("5$GET_")!=-1){
			strTempValue=_strTypeValue.substring(6);
			_strTypeValue="5$GET_";
			
		}else if(_strTypeValue.indexOf("5$RT_")!=-1){
			strTempValue=_strTypeValue.substring(5);    //只读文本框
			_strTypeValue="5$RT_";
			
		}else if(_strTypeValue.indexOf("D$")!=-1){
			strTempValue=_strTypeValue.substring(2);
			_strTypeValue="D$";

		}else if(_strTypeValue.indexOf("R$")!=-1){//关联
			strTempValue=_strTypeValue.substring(2);
			_strTypeValue="R$";

		}else if(_strTypeValue.indexOf("4$")!=-1){
			strTempValue=_strTypeValue.substring(2);
			_strTypeValue="4$";
		}else if(_strTypeValue.indexOf("P$")!=-1){
			strTempValue=_strTypeValue.substring(2);
			_strTypeValue="P$";
		}
		setFieldsValue('seltype',_iIndex,_strTypeValue);
		inputType(_iIndex);
		if(strTempValue!=""){
			document.getElementById("inputdata"+_iIndex).value=strTempValue;
		}
		setFieldsValue('inputsel',_iIndex,_strSelValue);
		
	}
	function setFieldsValue(_sName,_iIndex,_sValue){
		var objSel=document.getElementById(_sName+_iIndex);
		for(i=0;i<objSel.length;i++){
			if(objSel[i].value==_sValue)
				objSel[i].selected=true;
			}			
	}
   function getCheck(_iIndex,_strGetcheck){
       var objSel=document.getElementById('inputcheck'+_iIndex);
	   for(i=0;i<objSel.length;i++){
            if(objSel[i].value==_strGetcheck)
		    objSel[i].selected=true;
			}
   }
	function frontToTop(_objTr){
		frontTr(_objTr);
		if(_objTr.rowIndex==1)
			return;
		else
			frontToTop(_objTr);
			
	}
	function frontTr(_objTr){
		if(_objTr.rowIndex==1) return;
		if(_objTr.previousSibling)
			exchangeTr(_objTr,_objTr.previousSibling);
		}
	function pushBottom(_objTr,_iRowCount){
		if(_objTr.rowIndex==(_iRowCount)){
			return;
		}else{
			pushTr(_objTr);			
			pushBottom(_objTr,_iRowCount);
			}
			
	}
	function pushTr(_objTr){
	    if(_objTr.nextSibling)
			exchangeTr(_objTr,_objTr.nextSibling);
	}

	function exchangeTr(_objTr,_objTrPre){
		 var _parent=_objTr.parentNode;
		 var objTr=_objTr.nextSibling;
		 var objTrPre=_objTrPre.nextSibling;
		 if(objTr)
			 _parent.insertBefore(_objTrPre,objTr);
		 else 
			 _parent.appendChild(_objTrPre);
		if(objTrPre)
			_parent.insertBefore(_objTr,objTrPre);
		else
			_parent.appendChild(_objTr);
	}





	function delField(_objTr){
		tbfields.deleteRow(_objTr.rowIndex);
	}
	function inputType(_iIndex){
		var objType=document.getElementById("seltype"+_iIndex);
		switch(objType.value){
			case "4$":
				document.getElementById("tdvalue"+_iIndex).innerHTML="<input name='inputdata"+_iIndex+"' type='text' style='width:80;'>";
				break;
			case "4$INCREMOD_":
				document.getElementById("tdvalue"+_iIndex).innerHTML="<input name='inputdata"+_iIndex+"' type='text' style='width:80;'>";
				break;
			case "4$RE_":
				document.getElementById("tdvalue"+_iIndex).innerHTML="<input name='inputdata"+_iIndex+"' type='text' style='width:80;'>";
				break;
			case "4$PY_":
				document.getElementById("tdvalue"+_iIndex).innerHTML="<select name='inputdata"+_iIndex+"'  style='width:100;'>"+inputSelectOption+"</select>";
				break;
			case "4$GET_":
				document.getElementById("tdvalue"+_iIndex).innerHTML="<select name='inputdata"+_iIndex+"'  style='width:100;'>"+inputSelectOption+"</select>";
				break;
			case "D$":
				document.getElementById("tdvalue"+_iIndex).innerHTML="<select name='inputdata"+_iIndex+"' style='width:100;'>"+dicoption.value+"</select>";
				break;
			case "5$RE_":
			case "5$RT_":
				document.getElementById("tdvalue"+_iIndex).innerHTML="<input name='inputdata"+_iIndex+"' type='text' style='width:80;'>";
				break;
				
			case "5$GET_":
			case "R$":

				document.getElementById("tdvalue"+_iIndex).innerHTML="<select name='inputdata"+_iIndex+"'  style='width:100;'>"+inputSelectOption+"</select>";
				break;
			case "P$":
				document.getElementById("tdvalue"+_iIndex).innerHTML="<select name='inputdata"+_iIndex+"' style='width:100;'>"+paramoption.value+"</select>";
				break;
			default:
				document.getElementById("tdvalue"+_iIndex).innerHTML="";
		}
	}
	function saveFields(){
		var arrRows=tbfields.rows;
		var iRowCount=arrRows.length;
		
		aParent.strItemCodes="";//已加入字段
		aParent.strItemNames="";//已加入字段名
		aParent.strItemTypes="";//已加入字段类型
		aParent.strItemLength="";//已加入字段长度

		aParent.strItemSelect="";//已加入字段选择
		aParent.strReturnFields="";//已加入字段回填
		aParent.strHCFields="";//回传字段
		aParent.strGetchecks="";//js验证
	    aParent.strSize="";//大小


		for( var i=1;i<iRowCount;i++){
			var strTempField=document.getElementById("inputfield"+arrRows[i].RID).value;//字段
			var strTempName=document.getElementById('inputfieldname'+arrRows[i].RID).value;//名称
			var strTempType=document.getElementById("seltype"+arrRows[i].RID).value;//类型
			var strTempLen=document.getElementById("inputlen"+arrRows[i].RID).value;//长度
			var objValue=document.getElementById("inputdata"+arrRows[i].RID);//值

			var strTempSelFields=document.getElementById("inputsel"+arrRows[i].RID).value;//选择
			var strTempReFields=document.getElementById("inputset"+arrRows[i].RID).value;//回填
			var strTempgetcheck=document.getElementById("inputcheck"+arrRows[i].RID).value;//js验证
			var strTempsize=document.getElementById("inputsize"+arrRows[i].RID).value;  //大小

			var strTempISHCFields="0";
			if(document.getElementById("inputput"+arrRows[i].RID).checked)
				strTempISHCFields=strTempField;
			

			if(objValue!=null){
				if(objValue.value==""){alert("请填写值！");objValue.focus();return}

				if(strTempType=="4$INCREMOD_")
					if(objValue.value.indexOf("$")!=-1)
						strTempType="4$INCREMOD_$"+objValue.value.substring(0,objValue.value.indexOf("$"))+"$"+document.getElementById("inputfield"+arrRows[i].RID).value+objValue.value.substring(objValue.value.indexOf("$"));
					else
						strTempType="4$INCREMOD_$"+objValue.value+"$"+document.getElementById("inputfield"+arrRows[i].RID).value;
					
				else
					strTempType=strTempType+objValue.value;
			}
			var reg=new RegExp("&nbsp;","g");
			var newstrTempName;
			aParent.strItemCodes+=","+strTempField;
            if(strTempName.indexOf("&nbsp;")==-1){
			aParent.strItemNames+=","+strTempName;
			}
			else{
            newstrTempName=strTempName.replace(reg,".");
            aParent.strItemNames+=","+newstrTempName;
			}
			aParent.strItemTypes+=","+strTempType;
			aParent.strItemLength+=","+strTempLen;

			aParent.strItemSelect+=","+strTempSelFields;
			aParent.strReturnFields+=","+strTempReFields;

			aParent.strHCFields+=","+strTempISHCFields;
			aParent.strGetchecks+=","+strTempgetcheck;
			aParent.strSize+=":"+strTempsize;
		}
		
		/////修改页面表
		var strTempParams="&T_SYS_PAGEMSG$SFIELDCODE="+aParent.strItemCodes.substring(1)+
						  "&T_SYS_PAGEMSG$SFIELDNAME="+aParent.strItemNames.substring(1)+
						  "&T_SYS_PAGEMSG$SFIELDSIZE="+aParent.strItemLength.substring(1)+
						  "&T_SYS_PAGEMSG$SQUERYFIELD="+aParent.strItemSelect.substring(1)+
						   "&T_SYS_PAGEMSG$SGLFIELD="+aParent.strReturnFields.substring(1)+
						   "&T_SYS_PAGEMSG$STRANS="+aParent.strHCFields.substring(1)+    //回传
						  "&T_SYS_PAGEMSG$SDELCON="+aParent.strItemTypes.substring(1)+
						  "&T_SYS_PAGEMSG$SHREFIELD="+aParent.strGetchecks.substring(1)+
			              "&T_SYS_PAGEMSG$SSIZE="+aParent.strSize.substring(1);
		aParent.updateFields1(strTempParams);
	////预览页面
		aParent.viewData();
		aParent.loadpageEvent();		
		closeWin();
	}


	var onmsov = function(_obj){  
		return function() 
			{    _obj.className="tr1over";
			}
		}
	var onmsot = function(_obj){  
	return function() 
		{    _obj.className="tr1";
		}
	}
function randomNumber(limit){    
  return Math.floor(Math.random()*limit);    
}function decToHex(dec)    
{    
  var hexStr = "0123456789ABCDEF";    
  var low = dec % 16;    
  var high = (dec - low)/16;    
  hex = "" + hexStr.charAt(high) + hexStr.charAt(low);    
  return hex;    
}function generColor()    
{    
  var r,g,b;    
  r = decToHex(randomNumber(256)-1);    
  g = decToHex(randomNumber(256)-1);    
  b = decToHex(randomNumber(256)-1);    
  return "#" + r + g + b;    
}


