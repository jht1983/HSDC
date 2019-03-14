/*1.1*/
Date.prototype.Format = function (fmt) { 
  var o = {
    "M+": this.getMonth() + 1, 
    "d+": this.getDate(), 
    "h+": this.getHours(),
    "m+": this.getMinutes(), 
    "s+": this.getSeconds(), 
    "q+": Math.floor((this.getMonth() + 3) / 3), 
    "S": this.getMilliseconds() 
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
  if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
}

function alter(_nameCod,_Str,_timeCod){
	var timestamp=new Date().Format("yyyy-MM-dd hh:mm:ss");
	document.getElementById(_nameCod).value=_Str;
	document.getElementById(_timeCod).value=timestamp;
}
function disableInput(){
    for(i=0;i<document.all.length;i++){
        if(document.all[i].tagName=="INPUT"){
			document.all[i].readOnly=true;
			document.all[i].setAttribute("readOnly", true);
			document.all[i].setAttribute("class", "inputreadonlly");
			document.all[i].onclick="function(){}"; 
		}
	    if(document.all[i].tagName=="TEXTAREA"){
			document.all[i].readOnly=true;
			document.all[i].setAttribute("class", "inputreadonlly");
	    }
	    if(document.all[i].tagName=="SELECT"){
		    document.all[i].readOnly=true;
		    document.all[i].setAttribute("class", "inputreadonlly");
		    document.all[i].onclick="function(){}"; 
		    document.all[i].onchange="function(){}"; 
		}
    }
}
function disableInputView(usercode,doview,loginusercode){
     if(document.getElementById(usercode) == null){
        var objbtn = document.getElementsByClassName("bttn_panel");
        if(objbtn.length==2){
            objbtn[0].parentNode.parentNode.href="";
            objbtn[0].parentNode.parentNode.style.display="none";
            objbtn[1].innerHTML="关闭";
            /*document.getElementsByClassName("tdtoupload")[0].style.display="none";*/
        }else if(objbtn.length==3){
            objbtn[0].parentNode.parentNode.href="";
            objbtn[0].parentNode.parentNode.style.display="none";
            objbtn[1].parentNode.parentNode.href="";
            objbtn[1].parentNode.parentNode.style.display="none";
            objbtn[2].innerHTML="关闭";
            var objupload = document.getElementsByClassName("tdtoupload");
            if(objupload.length!=0){
                objupload[0].style.display="none";
            }
            
        }
    }
    
    var strcjr=document.getElementById(usercode).value;
    if((strcjr!=""&&strcjr!=loginusercode)||(doview=="detail")){
        var objbtn = document.getElementsByClassName("bttn_panel");
        if(objbtn.length==2){
            objbtn[0].parentNode.parentNode.href="";
            objbtn[0].parentNode.parentNode.style.display="none";
            objbtn[1].innerHTML="关闭";
            /*document.getElementsByClassName("tdtoupload")[0].style.display="none";*/
        }else if(objbtn.length==3){
            objbtn[0].parentNode.parentNode.href="";
            objbtn[0].parentNode.parentNode.style.display="none";
            objbtn[1].parentNode.parentNode.href="";
            objbtn[1].parentNode.parentNode.style.display="none";
            objbtn[2].innerHTML="关闭";
            var objupload = document.getElementsByClassName("tdtoupload");
            if(objupload.length!=0){
                objupload[0].style.display="none";
            }
            
        }

        // disableInput();
    }
    
}
function pdview(col,loginusercode,type){
    var no_optype=document.getElementsByName("NO_OPTYPE");
    if(no_optype.length==1){
          disableInputView(col,type,loginusercode);
    }
}
/*1.3
 function sys_doCheckClick(obj,dataset,name,xx){
    if(obj.checked){
        obj.parentNode.parentNode.parentNode.parentNode.parentNode.getElementsByTagName('input')[0].value="true";
    }else{
        obj.parentNode.parentNode.parentNode.parentNode.parentNode.getElementsByTagName('input')[0].value="false";
    }
}
*/
function qingkonginput(){
    var documentall=document.all;
    for(var i =0;i<documentall.length;i++){
        if(documentall[i].tagName=="INPUT"&&documentall[i].type=="checkbox"){
            documentall[i].checked=false;
            documentall[i].parentNode.parentNode.parentNode.parentNode.parentNode.getElementsByTagName('input')[0].value="false";
        }
    }
}

function SYS_ChangeSelId(_strContent,_strTBId){
	var arrTreeId=eval("arrInitSelTreeId"+_strTBId);
	var iLength=arrTreeId.length;
	iBatchFormTreeIdIndex++;
	for(var i=0;i<iLength;i++)
		_strContent=_strContent.replace(
				"id=\""+arrTreeId[i]+"\"",
				"id=\""+arrTreeId[i]+"_"+iBatchFormTreeIdIndex+"\"");
	_strContent = _strContent.replace("type='checkbox' value='true'","type='checkbox' value='false'");
	_strContent = _strContent.replace("value=\"true\"","value=\"false\"");
	return _strContent;
}


/*V1.4 12/11 参照问题*/

function splitStr(_str,_split){/*切割字符串*/
    return _str.split(_split);
}

function addDataByName(_nameStr,_index,_value){ /*按照名字添加*/
    
    try{
        document.getElementsByName(_nameStr)[_index].value=_value;
    }catch(e){
        console.log("err:015004");
    }
}

function addDataById(_nameStr,_value){/*按照ID添加值*/
 try{
    document.getElementById(_nameStr).value=_value;
 }catch(e){
        console.log("err:015004:"+_nameStr);
    }
}

function moreRowCom(_tableId,_cellNumber,_split,_checked){/*返回数组*/
    var tableEle = document.getElementById(_tableId);
    var returnStrArr="";
    var funSplit="";
    for(var i = 1 , j = tableEle.rows.length ; i < j ; i++){
        if(_checked=="true"||_checked=="false"){
            var objCheckChilds=document.getElementsByName("syscheckbox");
            if(objCheckChilds[i].checked==_checked){
                returnStrArr+=funSplit+tableEle.rows[i].cells[_cellNumber].innerHTML;
                funSplit=_split;
            }
        }else{
            returnStrArr+=funSplit+tableEle.rows[i].cells[_cellNumber].innerHTML;
            funSplit=_split;
        }
    }
    return returnStrArr;
}

function insertActionButton(_buttonName,_strfun){
    var aEle = document.createElement("a");
    /*aEle.setAttribute("href","javascript:"+_strfun);*/
    /*if(typeof(_strfun)=="function"){
        aEle.setAttribute("onclick",_strfun);
    }else{
        aEle.setAttribute("href","javascript:"+_strfun);
    }*/
    if(typeof _strfun =="function"){
        aEle.onclick=_strfun;
    }else{
        aEle.setAttribute("href","javascript:"+_strfun);
        aEle.setAttribute("onclick","this.blur();");
    }
    aEle.setAttribute("class","button red");
    aEle.innerHTML="<span><div class=\"bttn_panel\" style=\"background-image:url(images/eve/gb.png);\">"+_buttonName+"</div></span>";
    $I("sys_form_opbttn").rows[0].cells[1].appendChild(aEle);
}

function inPutField(a,b,c,d,iRowIndex,_obj,_parentObj,_checked){
    if(a===""){/*主  GO*/
        outPutField(a,b,c,d,iRowIndex,_obj,_parentObj);
    }else{
        var divEle = objParent.document.getElementById("tabpage_0pg_"+a);
        var textareaID = divEle.getElementsByTagName('textarea')[0].id;
        var number = textareaID.replace("NO_SYS_BATCH_ROW","");
        outPutFieldZ(a,b,c,d,iRowIndex,_obj,_parentObj,number,_checked);
    }
}
function outPutField(a,b,c,d,iRowIndex,_obj,_parentObj,_checked){
    var rowInArr=splitStr(d,",");
    var rowOuArr=splitStr(b,",");
    if(c==""){/*主/主*/
        for(var i = 0 ; i <rowOuArr.length ; i++){
            _parentObj.document.getElementById(rowOuArr[i]).value=_obj.parent.lxleft.document.getElementById('tb').rows[iRowIndex].cells[rowInArr[i]].innerHTML;
        }
    }else{
        for(var i = 0 ; i <rowOuArr.length ; i++){
            _parentObj.document.getElementById(rowOuArr[i]).value=moreRowCom('tb',rowInArr[i],",",_checked);
        }
    }
}
function outPutFieldZ(a,b,c,d,iRowIndex,_obj,_parentObj,_pageNumber,_checked){
    var rowInArr=splitStr(d,",");
    var rowOuArr=splitStr(b,",");
    var index = _parentObj.document.getElementById("sys_form_content"+_pageNumber).rows.length;
    if(c==""){/*主*/
        for(var i = 0 ; i <rowOuArr.length ; i++){
            _parentObj.SYS_ADD_BATCH_ROW(_pageNumber);
            _parentObj.addDataByName(rowInArr[i],index+i,_obj.parent.lxleft.document.getElementById('tb').rows[iRowIndex].cells[rowInArr[i]].innerHTML);
        }
    }else{/*子*/
        var tableEle = document.getElementById('tb');
        for(var i = 1 , j = tableEle.rows.length ; i < j ; i++){
            if(_checked==true||_checked==false){
                var objCheckChilds=document.getElementsByName("syscheckbox");
                if(objCheckChilds[i-1].checked==_checked){
                    _parentObj.SYS_ADD_BATCH_ROW(_pageNumber);
                    for(var z = 0 ; z <rowOuArr.length ; z++){
                        _parentObj.addDataByName(rowOuArr[z],index+i-2,tableEle.rows[i].cells[rowInArr[z]].innerHTML);
                    }
                }
            }else{
                _parentObj.SYS_ADD_BATCH_ROW(_pageNumber);
                for(var z = 0 ; z <rowOuArr.length ; z++){
                    _parentObj.addDataByName(rowOuArr[z],index+i-2,tableEle.rows[i].cells[rowInArr[z]].innerHTML);
                }
            }
        }
    }
}
/*i==1?"":objParent.SYS_ADD_BATCH_ROW(number);*/
/* v1.5 12-13 计算方法*/
    // var mathComp={
    //     addition:function(){
    //         var a=0;
    //         try{for(var b in arguments){a=mathComp.accAdd(a,arguments[b])}}catch(e){a="NaN";}
    //         return a;
    //     },
    //     accAdd:function(arg1,arg2){   /* 小数数字相加 */
    //       var r1,r2,m;   
    //       try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}   
    //       try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}   
    //       m=Math.pow(10,Math.max(r1,r2))   
    //       return ((arg1*m+arg2*m)/m).toFixed(2);   
    //     } 
    // }
    
    var mathComp={
        addition:function(){
            var a=0;
            try{
                for(var b in arguments){
                    a=mathComp.accAdd(a,arguments[b])
                }
            }catch(e){
                a="NaN";
            }
            return a;
        },
        accAdd:function(arg1,arg2){   /* 小数数字相加 */
            var r1,r2,m;
            try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
            try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
            m=Math.pow(10,Math.max(r1,r2))
            return ((arg1*m+arg2*m)/m).toFixed(2);
        },
        mult:function (_arg1,_arg2){
            var r1,r2,m;
            try{r1=_arg1.toString().split(".")[1].length;}catch(e){r1=0;}
            try{r2=_arg2.toString().split(".")[1].length;}catch(e){r2=0;}
            m=Math.pow(10,Math.max(r1,r2));
            return (_arg1*m)*(_arg2*m)/Math.pow(m,2);
        },
        division:function (_arg1,_arg2){
            var r1,r2,m;
            try{r1=_arg1.toString().split(".")[1].length;}catch(e){r1=0;}
            try{r2=_arg2.toString().split(".")[1].length;}catch(e){r2=0;}
            m=Math.pow(10,Math.max(r1,r2));
            return (_arg1*m)/(_arg2*m);
        }
    }
    /* v1.6 12-15 只选择年的下拉框*/
    function returnYearOption(_str){
        var nowYear=parseInt(new Date().Format("yyyy"));
        var yearI=0;
        var returnValue="<option value=''></option> ";
        for(var i =10;i>0;i--){
            yearI=(nowYear-i);
            if(_str==yearI){
                returnValue+="<option selected value='"+yearI+"'>"+yearI+"</option> ";
            }else{
                returnValue+="<option value='"+yearI+"'>"+yearI+"</option> ";
            }
            
        }
        
        for(var i =0;i<30;i++){
            yearI=(nowYear+i);
            if(_str==yearI){
                returnValue+="<option selected value='"+yearI+"'>"+yearI+"</option> ";
            }else{
                returnValue+="<option value='"+yearI+"'>"+yearI+"</option> ";
            }
            
        }
        return returnValue;
    }
    /* v1.7 12-15 选择*/
    function $I(_str){
        if(document.getElementById(_str)==null){
            console.log("015009:"+_str);
        }
        return document.getElementById(_str);
    }
    function $N(_str){
        return document.getElementsByName(_str);
    }
    function $C(_str){
        return document.getElementsByClassName(_str);
    }
    /* v1.8 12-15*/
    	  function closeNO_DOSCRIPT(_str){
			var prinWindow=window.location.href;
        if(!$I('NO_DOSCRIPT')){
            var input = document.createElement("input");
            input.type="hidden";
            input.id="NO_DOSCRIPT";
            input.name="NO_DOSCRIPT";
            $I('add').appendChild(input);
        }
        //_str = SYS_FLOW_RUN_audit
     $I('NO_DOSCRIPT').value="var no_upl_kc = '"+_str+"';if(no_upl_kc.indexOf('gs_upl_kc')>-1){var src = parent.document.getElementById('sys_bd');src.src=src.src}else{if(no_upl_kc==='SYS_FLOW_RUN_audit'){window.location.href = '"+prinWindow+"';}else{if(parent.getOpenPage(no_upl_kc)==null){parent.closeWinById(no_upl_kc);parent.location.reload()}else{parent.getOpenPage(no_upl_kc).location.reload();parent.closeWinById(no_upl_kc)}}}";
		  
		  }  

function lc(ind){/**联查*/
    
    		        var objCheckChilds=document.getElementsByName("syscheckbox");
	                var strComponents="";
	                var objTable=document.getElementById("tb");
		            var iCheckCount=objCheckChilds.length;
		            var count=0;
            		for(var i=0;i<iCheckCount;i++){
            			if(objCheckChilds[i].checked){
            			    count++;
            				strComponents=rows[parseInt(objCheckChilds[i].value)+1].cells[ind].innerHTML;
            				break;
            			}
            		}
            		if(strComponents==""){
            		    alert("无关联单据");
            		    return ;
            		}else{
            		    if(count>1){
            		        alert("请选择一条数据 ");
            		    }else{
                    		miniWin('单据联查','',"View?SPAGECODE=1512461681484&srunid="+strComponents+"&bmid="+bmid,600,400,'','');
            		    }
            		}
    /**miniWin('正在处理请耐心等待......','','sql-data-tb.v?NO_UPL_KC=<<gs_upl_kc>>&S_ID='+sid,800,500,'','');**/
}
function setzdr(name,code,timess,nameval,codeval){
    if(document.getElementsByName("NO_OPTYPE").length==0){
        document.getElementById(name).value=nameval;
        document.getElementById(code).value=codeval;
        document.getElementById(timess).value=new Date().Format("yyyy-MM-dd hh:mm:ss");;
    }
}
 /* v1.9 12-18*/
 
 /* v2.0 12-18  2018-03-05 17:29:57修改*/
function personPut(_name,_code,_bmid){
    try{
       
    
        var general=_name+":0;"+_code+":1";
        general=general.replace(/\$/g,",");
        $I(_name).setAttribute("class","sysselinput");
        $I(_name).onclick=function (){
            miniWin('人员选择','','View?SPAGECODE=1512628953142&bmid='+_bmid+'&general='+general,1200,800,'','');
        }
    }catch(e){
        console.log("err:personPut"); 
    }
 }

function personPut_new(_name,_code,_code_new,_bmid){
    try{
       
        var general=_name+":0;"+_code+":6;"+_code_new+":1";
        general=general.replace(/\$/g,",");
        $I(_name).setAttribute("class","sysselinput");
        $I(_name).onclick=function (){
            miniWin('人员选择','','View?SPAGECODE=155244942139110583&bmid='+_bmid+'&general='+general,1200,800,'','');
        }
    }catch(e){
        console.log("err:personPut"); 
    }
 }

 function personAndBranchPut(_name,_code,_bmName,_bmCode,_bmid){
     try{
    var general=_name+":0;"+_code+":1;"+_bmName+":4;"+_bmCode+":5";
    general=general.replace(/\$/g,",");
    $I(_name).setAttribute("class","sysselinput");
    $I(_name).onclick=function (){
        miniWin('人员选择','','View?SPAGECODE=1512628953142&bmid='+_bmid+'&general='+general,1200,800,'','');
    }
 }catch(e){
        console.log("err:personAndBranchPut"); 
    }
 }
 
 function personAndBranchPut_new(_name,_code,_code_new,_bmName,_bmCode,_bmid){
     try{
    var general=_name+":0;"+_code+":6;"+_code_new+":1;"+_bmName+":4;"+_bmCode+":5";
    general=general.replace(/\$/g,",");
    $I(_name).setAttribute("class","sysselinput");
    $I(_name).onclick=function (){
        miniWin('人员选择','','View?SPAGECODE=155244942139110583&bmid='+_bmid+'&general='+general,1200,800,'','');
    }
 }catch(e){
        console.log("err:personAndBranchPut"); 
    }
 }
 
  function personAllPut(_name,_code,_roleName,_roleCode,_bmName,_bmCode,_bmid){
        try{
    var general=_name+":0;"+_code+":1;"+_bmName+":4;"+_bmCode+":5"+_roleName+":2;"+_roleCode+":3";
    general=general.replace(/\$/g,",");
    $I(_name).onclick=function (){
        miniWin('人员选择','','View?SPAGECODE=1512628953142&bmid='+_bmid+'&general='+general,1200,800,'','');
    }
  }catch(e){
        console.log("err:personAllPut"); 
    }
 }

  /* v2.1 12-26*/
function rowNumByCheckBox(){
	var checkboxArrByName = $N('syscheckbox');
    for(var i = 0 , j = checkboxArrByName.length; i < j ; i++){
        if(checkboxArrByName[i].checked){
            return checkboxArrByName[i].value;
        }
    }
    return '';
}
function proVal(){
    var style_flow = "<span style='color:red;'>\u6d41\u7a0b\u7ed3\u675f</span>";
    var tb=document.getElementById("tb");
    var rows=tb.rows;
    for(var i = 0 , j = rows.length-1 ; i < j ; i++){/* 流程结束 */
        if(sys_I_isover[i].innerHTML=="1"){
            SYS_SHPUT_SEL[i].innerHTML=style_flow;
        }
    }
}
function getselrow(strflowid,strrunid,strversion,strnode){
        var objCheckChilds=document.getElementsByName("syscheckbox");
        var strComponents="";
        var objTable=document.getElementById("tb");
        var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
			    var ind = parseInt(objCheckChilds[i].value)+1;
			    var obj = objTable.rows[ind];
				strComponents=obj.cells[strflowid].innerHTML+"&"+obj.cells[strversion].innerHTML+"&"+obj.cells[strrunid].innerHTML+"&"+obj.cells[strnode].innerHTML;
				break;
			}
		}
		return strComponents;
}
     


 function lct(ids){/**流程图 流程ID-版本号-运行ID-节点号*/
    var arrayids = ids.split("&");
    var strflowid = arrayids[0];
    var strrunid=arrayids[1];
    var strversion=arrayids[2];
    var strnode=arrayids[3];
     var id=getselrow(strflowid,strrunid,strversion,strnode);
     var arr=id.split("&");
    var strPic="comp?viewtype=flow&sys_type=viewflow&flowid="+arr[0]+"&flowversion="+arr[1]+"&runid="+arr[2];
     miniWin('流程图','',strPic,2000,2000,'','');
}
 function lcrz(ids){/**日志*/
         var arrayids = ids.split("&");
    var strflowid = arrayids[0];
    var strrunid=arrayids[1];
    var strversion=arrayids[2];
    var strnode=arrayids[3];
     var id=getselrow(strflowid,strrunid,strversion,strnode);
     var arr=id.split("&");
    var strPic="View?SPAGECODE=1502871394942&S_FLOW_ID="+arr[0]+"&S_AUDIT_VERSION="+arr[1]+"&S_RUN_ID="+arr[2];
     miniWin('审批日志','',strPic,2000,2000,'','');
}
function spjs(isover,optshow){
    var tb=document.getElementById("tb");
    var rows=tb.rows;
    var style_flow = "<span style='color:red;'>流程结束</span>";
      for(var i=1;i<rows.length;i++){
        var cells=rows[i].cells;
           if(cells[isover].innerHTML=="1"){
               cells[optshow].innerHTML=style_flow;
           }
     }
}

var transcoding = { 
    ToUnicode: function (str) {
      return escape(str).toLocaleLowerCase().replace(/%u/gi, '\\u');
      } 
    , ToGB2312: function (str) {
        return unescape(str.replace(/\\u/gi, '%u'));
      } 
  };
/** radio元素操作   */

/**
 * author:Mantra
 *  版本:1.0
 * 关于单选按钮元素控制
 *
 *  变量
 *  radioEleArr     初始化元素集合
 *  radioEleIndexLength     元素集合的长度
 *  方法
 * getCheckRaidoEle     得到选中的单选元素
 * getCheckRaidoId      得到选中的单选框元素的ID
 * getCheckRaidoAttr    得到选中的单选框元素的属性 arg1//属性名称
 * setRaidoCheckByValue 通过值设置单选框默认选中  _value//值
 * setRaidoCheckByAttr  通过属性设置单选框默认选中 _AttrName//属性名称  _value//值
 * setRaidoAttr         通过元素设置属性和值      _obj//元素对象  _AttrName//属性名称 _value//属性值
 * oncClick             设置单选框元素的点击事件    _fun//输入方法
 * setRaidoAttr         设置选中元素的属性和值     _AttrName//属性名称 _value//属性值
 * */
function radioControl(_radioName){
    this.radioEleArr=document.getElementsByName(_radioName);
    this.radioEleIndexLength=this.radioEleArr.length;
    this.getCheckRaidoEle = function(){
        for(var _index in this.radioEleArr)
            if(this.radioEleArr[i].checked)
                return this.radioEleArr[i];
        return null;
    }
    this.getCheckRaidoId=function(){
        return this.getCheckRaidoEle().id;
    }
    this.getCheckRaidoAttr=function(_argName){
        return this.getCheckRaidoEle().getAttribute(_argName);
    }
    this.setRaidoCheckByValue=function(_value){
        for(var i = 0 ; i < this.radioEleIndexLength ; i++){
            if(this.radioEleArr[i].value===_value){
                this.radioEleArr[i].checked=true;
                return true;
            }
        }
        return false
    }
    this.setRaidoCheckByAttr=function(_AttrName,_value){
        for(var i = 0 ; i < this.radioEleIndexLength ; i++){
            if(this.radioEleArr[i].getAttribute(_AttrName)===_value){
                this.radioEleArr[i].checked=true;
                return true;
            }
        }
        return false
    }
    this.setRaidoAttr=function(_obj,_AttrName,_value){
        _obj.setAttribute(_AttrName,_value);
    }
    this.oncClick=function(_fun){
        for(var _index in this.radioEleArr)
            if(this.radioEleArr[_index].tagName=="INPUT")
                this.radioEleArr[_index].onclick=_fun;
    }
    this.setRaidoAttr=function(_AttrName,_value){
        this.getCheckRaidoEle.setAttribute(_AttrName,_value);
    }
}


/*初始化表身*/

function initFormBody(){
    var opbttnWidth=$I('sys_form_opbttn').style.width;
    $('sysformwindiv').style.width=opbttnWidth;
    var sys_form_child_tableArr = $('sys_form_child').getElementsByTagName("div");
    for(var i = 0 , j = sys_form_child_tableArr.length ; i < j ; i++){
        if(sys_form_child_tableArr[i].className=="selInputpanel"){
            continue;
        }
        sys_form_child_tableArr[i].style.width=opbttnWidth;
    }
}

/*表单行处理*/

function tableRowDis(_bool,_fun){
    var table = $I('tb');
    var checkboxArrByName = $N('syscheckbox');
    var func = eval(_fun);
    for(var i = 0 , j = checkboxArrByName.length; i < j ; i++){
        if(checkboxArrByName[i].checked==_bool){
            new func(table.rows[i+1]);
        }
    }
}
function setSelectChecked(select, checkValue){  
    for(var i=0; i<select.options.length; i++){  
            console.log(select.options[i].innerHTML+"---"+checkValue);
        if(select.options[i].innerHTML == checkValue){
            select.options[i].selected = true;  
            break;  
        }  
    }  
};
    function addcol(strcol,strval){
            var input = document.createElement("input");
            input.type="hidden";
            input.id=strcol;
            input.name=strcol;
            $I('add').appendChild(input);
        $I(strcol).value=strval;
    } 
    /**
 * author:Mantra
 *  版本:1.1 
 * 2018-01-19 11:04:34
 * 关于复选按钮元素控制
 *
 *  变量
 *  checkboxEleArr     初始化元素集合
 *  checkboxEleIndexLength     元素集合的长度
 *  方法
 * getCheckedEleArr     通过条件得到复选元素 //_boole  获取选中或不选中的元素集合  空为所有
 * getCheckedValue      通过条件得到复选元素VALUE  //_boole  获取选中或不选中的元素集合  空为所有
 * Inverse              反选
 * checkAll             全部选中
 * onChick              点击时触发的方法 //_fun为方法
 * setCheckboxAttr      设置属性 _obj//元素对象  _AttrName//属性名称 _value//属性值
 * getCheckboxAttr      设置选中元素的属性和值     _obj//元素对象  _AttrName//属性名称
 *
 * */
function checkboxControl(_checkboxName){
    this.checkboxEleArr=document.getElementsByName(_checkboxName);
    this.checkboxEleIndexLength=this.checkboxEleArr.length;
    this.getCheckedEleArr = function (_boole){
        var returnTmo=[];
        for(var _index = 0 ; _index < this.checkboxEleIndexLength ; _index++)
            if (_boole == null || this.checkboxEleArr[_index].checked == _boole)
                returnTmo.push(this.checkboxEleArr[_index]);
        return returnTmo;
    }
    this.getCheckedValue = function (_boole){
        var returnTmo="";
        for(var _index = 0 ; _index < this.checkboxEleIndexLength ; _index++)
            if (_boole == null || this.checkboxEleArr[_index].checked == _boole)
                returnTmo+=this.checkboxEleArr[_index].value+",";
        returnTmo.substring(0,returnTmo.length-1);
        return returnTmo;
    }
    this.Inverse = function() {
        for(var _index = 0 ; _index < this.checkboxEleIndexLength ; _index++)
            this.checkboxEleArr[_index].click();
    }
    this.checkAll = function(){
        for(var _index = 0 ; _index < this.checkboxEleIndexLength ; _index++)
            this.checkboxEleArr[_index].checked = true;
    }
    this.onChick = function (_fun){
        for(var _index = 0 ; _index < this.checkboxEleIndexLength ; _index++)
            this.checkboxEleArr[_index].onclick = _fun;
    }
    this.setCheckboxAttr=function(_obj,_AttrName,_value){
        _obj.setAttribute(_AttrName,_value);
    }
    this.getCheckboxAttr=function(_obj,_AttrName){
        return _obj.getAttribute(_AttrName);
    }
}

function tempver(_obj,_str){

    var divEle = document.createElement("div");
    divEle.id="Mantra_SHOW";
    divEle.className="Mantra_SHOW";
    divEle.style="top:0px;left:0px;display:none";
    divEle.innerHTML=_str;
    $I('sysbttoparea').parentNode.appendChild(divEle);
    document.write("<style>.Mantra_SHOW>div{width:100%;height:40px;line-height:40px;color:#000;text-align:center}.Mantra_SHOW>div:hover{background:#539ddd;color:#fff;font-weight:bold}.Mantra_SHOW{position:fixed;width:200px;height:auto;background-color:#fff;border:1px solid#c3c3c3;overflow:auto;z-index:999}</style>");
    _obj.onclick=function(evt){
        if($I("Mantra_SHOW").style.display=="none"){
            $I("Mantra_SHOW").style.top=(evt.clientY+10)+"px";
            $I("Mantra_SHOW").style.left=evt.clientX+"px";
            $I("Mantra_SHOW").style.display="";
        }else{
            $I("Mantra_SHOW").style.display="none";
        }
    }
    return divEle;
}
function subGetTx(_subStr,_vari){
    var z = _subStr;
    _vari ="var "+_vari+"='";
    if(z.indexOf(_vari)==-1){
        return "";
    }
    
    z=z.substring(z.indexOf(_vari)+_vari.length);
    return z.substring(0,z.indexOf("';"));
}
function runCodeC(_eleId){
    try{
        $N('NO_DOSCRIPT$S_RUN_ID')[0].value=$N(_eleId)[0].value
        $N('NO_sys_S_RUN_ID')[0].value=$N(_eleId)[0].value
    }catch(e){
        console.log("err:015005"); 
    }
    
}
function removeSonEvent(){
    SYS_DEL_BATCH_ROW=function (_obj,_number){ return false;};
    SYS_INSERT_BATCH_ROW=function (_obj,_number){ return false;};
    SYS_ADD_BATCH_ROW=function (_number){ return false;};
}
function delSysbttoOnEdit(){
    var aEleArr = document.getElementById('sysbttoparea').rows[0].cells[0].getElementsByTagName("a");
    for(var i = 0 , j = aEleArr.length ; i < j ; i++ ){
        if(aEleArr[i].href.indexOf("sys_EditForm")>-1||aEleArr[i].href.indexOf("sys_CopyForm")>-1)
            aEleArr[i].style.display="none";
    }
}
    function numCheck(_rules,_number){
        try {
            var _rulesArr = _rules.split(",");
            var one = _rulesArr[0].substring(1);
            var two = _rulesArr[1].substring(0, _rulesArr[1].length - 1);
            one = one===""?Number.NEGATIVE_INFINITY:one;  //设置缺省 , 负无穷
            two = two===""?Number.POSITIVE_INFINITY:two;  //设置缺省 , 正无穷
            if (_rulesArr[0].indexOf("[") == 0)
                one--;
            if (_rulesArr[1].indexOf("]") > -1)
                two++;
            return one < _number && _number < two;
        }catch (e){
            console.log("err:015006"); 

        }
    }
/*2018-02-24 14:07:17*/
    var sFlow_evet = sFlow_evet || {
        curSelCheckBox: null, //复选框
        S_RUN_AGR: null,    //流程参数
        SYS_I_ISOVER: null, //是否结束
        SYS_SHPUT_SEL: null,
        SPAGECODE: null,    //修改页面
        bmid: null,
        runValidation: function (_lenNumber) {
            if (this.S_RUN_AGR === null || this.SYS_I_ISOVER === null || this.SYS_SHPUT_SEL === null){
                console.log("err:015008"); 
                return false;
            }
            this.curSelCheckBox = sys_getCurSelCheckBox();
            if (this.curSelCheckBox.length === 0) {
                alert("请选择一条数据进行查看");
                return false;
            }
            if (this.curSelCheckBox.length !== _lenNumber) {
                alert("只能选择一个");
                return false;
            }
            return true;
        },
        init: function (SPAGECODE, bmid) {
            this.SPAGECODE = SPAGECODE;
            this.bmid = bmid;
            this.SYS_SHPUT_SEL = $N('SYS_SHPUT_SEL');
            this.SYS_I_ISOVER = $N('sys_I_isover');
            this.S_RUN_AGR = $N('S_RUN_AGR');
        },
        audit: function (_obj) {
            var rowIndex = _obj.parentNode.parentNode.rowIndex - 1;
         
            var arr = this.S_RUN_AGR[rowIndex].innerHTML.split(",");
            miniWin('审核', '', 'flow-box.v?bmid=' + this.bmid + '&s_id=' + arr[0] + '&sys_flow_run_id=' + arr[1] + '&s_flow_id=' + arr[2] +
                '&flow_ver=' + arr[3] + '&node_code=' + arr[4] + '&spagecode=' + this.SPAGECODE, 2000, 2000, '', '');
        },
        auditLog: function () {
            if (!sFlow_evet.runValidation(1)) return;
            var arr = this.S_RUN_AGR[this.curSelCheckBox[0].value].innerHTML.split(",");
            var strPic = "View?SPAGECODE=1513749442902&S_FLOW_ID=" + arr[2] + "&S_AUDIT_VERSION=" + arr[3] + "&S_RUN_ID=" + arr[1];
            miniWin('审批日志', '', strPic, 2000, 1400, '', '');
        },
        auditProComp: function () {
            if (!sFlow_evet.runValidation(1)) return;
            var arr = this.S_RUN_AGR[this.curSelCheckBox[0].value].innerHTML.split(",");
            var strPic = "comp?viewtype=flow&sys_type=viewflow&flowid=" + arr[2] + "&flowversion=" + arr[3] + "&runid=" + arr[1];
            miniWin('流程图', '', strPic, 2000, 1400, '', '');
        },
        processOver: function () {
            var flowOver = "<span name='SYS_SHPUT_SEL' style='color:red;'>\u6d41\u7a0b\u7ed3\u675f</span>";
             var flowOverIs2 = "<span name='SYS_SHPUT_SEL' style='color:red;'>\u672a\u542f\u52a8\u6d41\u7a0b</span>";
            var tb = $I("tb");
            var rows = tb.rows;
            
            for (var i = 0, j = rows.length-1 ; i < j; i++) {/* 流程结束 */
                if(this.SYS_I_ISOVER.length>0){
                    if (this.SYS_I_ISOVER[i].innerHTML == "1") {
                        this.SYS_SHPUT_SEL[i].parentNode.innerHTML = flowOver;
                    }else if(this.SYS_I_ISOVER[i].innerHTML == ""){
                        this.SYS_SHPUT_SEL[i].parentNode.innerHTML = flowOverIs2;
                    }
                }
            }
        },
        reltionView: function (_obj) {
             if (!sFlow_evet.runValidation(1)) return;
            var arr = this.S_RUN_AGR[this.curSelCheckBox[0].value].innerHTML.split(",");
            var strPic = "associated.v?S_ID="+arr[0];
            miniWin('关系', '', strPic, 2000, 1400, '', '');
        },
        ancelProcess:function (){
            if (!sFlow_evet.runValidation(1)) return;
            if(this.SYS_I_ISOVER[this.curSelCheckBox[0].value].innerHTML=="1"){
                alert("\u6d41\u7a0b\u7ed3\u675f\u65e0\u6cd5\u9000\u56de");
                return;
            }
            var arr = this.S_RUN_AGR[this.curSelCheckBox[0].value].innerHTML.split(",");
            var AjxGetP = getTx("S_FLOW_ID=" + arr[2] + "&S_AUDIT_VERSION=" + arr[3] + "&S_RUN_ID=" + arr[1],"ajax.v");
            var SYS_FLOW_LASTUSER = subGetTx(AjxGetP,'SYS_FLOW_LASTUSER');
            var SYS_FLOW_NOWUSER = subGetTx(AjxGetP,'SYS_FLOW_NOWUSER');
            if(SYS_FLOW_LASTUSER===SYS_FLOW_NOWUSER){
               
                var getOVer = getTx("flowId="+arr[2]+"&flowRunCode="+arr[1]+"&flowRunUserCode="+SYS_FLOW_NOWUSER+"&flowVersion="+arr[3],"ajax_gzp_lc.v");
            
                var SYS_FLOW_RUNOVER = subGetTx(getOVer,'SYS_FLOW_RUNOVER');
                if(SYS_FLOW_RUNOVER=="false"){
                    alert("\u7f51\u7edc\u4e0d\u901a\u7545\u002c\u6216\u5df2\u7ecf\u5904\u7406");
                }else{
                    alert("\u64a4\u56de\u6210\u529f");
                    document.location.reload();
                }
            }else{
                alert("\u5f53\u524d\u64cd\u4f5c\u7528\u6237\u65e0\u6cd5\u64cd\u4f5c");
            }
            
        },
        screening:function(_str){
          
            sys_form_fiter_con.NO_FITER_CONDITION.value="S_AUD_USER like '%"+_str+"%'";
           
            sys_form_fiter_con.submit();
            //this.SYS_SHPUT_SEL
        }
        
    };
    
    function copeNewRunId(formId,bmid,tableName){
         try{
        var returnUserBz=getTx("formId="+formId+"&bmid="+bmid,"ajax.v");
        var UUID = subGetTx(returnUserBz,"UUID");
        var S_AUDIT_VERSION = subGetTx(returnUserBz,"S_AUDIT_VERSION");
        
        addDataByName('NO_DOSCRIPT$S_RUN_ID',0,UUID);
        addDataByName('NO_DOSCRIPT$SYS_FLOW_VER',0,S_AUDIT_VERSION);
        addDataByName(tableName+'$S_RUN_ID',0,UUID);
        addDataByName(tableName+'$SYS_FLOW_VER',0,S_AUDIT_VERSION);
        addDataByName('NO_sys_S_RUN_ID',0,UUID);
        addDataByName('NO_sys_flow_Ver',0,S_AUDIT_VERSION);

         }catch(e){
            console.log("err:015007"); 

         }
    }
    // 2018-04-04 13:20:16  son auto add content
   function sonAutoCont(cont,contValue,_pageCode){
        var _index = 0;
        for(var i = 0 , j = contValue.length ; i < j ; i++){
            if(i!=0){
                SYS_ADD_BATCH_ROW(_pageCode);
            }
            for(var n = 0 , m  = cont.length ; n < m ; n++){
                $N(cont[n])[_index].value=contValue[i][n];
            }
            _index++;
        }
    }
    //2018-04-12 10:11:24
    function getDataId(){
        var CheckBoxArr = sys_getCurSelCheckBox();
        var checkBoxLen = CheckBoxArr.length;
        var strComponents="";
        if( checkBoxLen == 0 ){
            alert("请选择数据");
            return false;
        }
        for(var i = 0 ; i < checkBoxLen ; i++){
            var checkIndex = CheckBoxArr[i].value;
            strComponents += "'"+sFlow_evet.S_RUN_AGR[checkIndex].innerHTML.split(",")[0]+"'";
            if(i+1 != checkBoxLen){
                strComponents+=",";
            }
        }
        return strComponents;
    }
    function getNowFormatDate() {
		var date = new Date();
		var seperator2 = ":";
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		if(month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if(strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		var currentdate = date.getFullYear() + "年" + month + "月" + strDate + 
			"日 " + date.getHours() + seperator2 + date.getMinutes() +
			seperator2 + date.getSeconds();
		return currentdate;
	}
    function initWork(){
     
        try{
            for(var p in Mantr_son_xh ){
                calculate(p,null);
             
            }
        }catch(e){
            console.log("err:015001");  
        }
   
    }
    function calculate(_number, _index) {
        try{
            var ele = $N(Mantr_son_xh[_number] );
            if (_index == null) {
                console.log(ele.length);
                ele[ele.length - 1].value = ele.length;
            } else {
                _index= _index-2;
                for (var i = _index, j = ele.length; i < j; i++) {
                     console.log(i);
                     
                    ele[i].value=i+1;
                }
            }
        }catch(e){
            console.log("err:015002");  
        }
    }
     function delCalculate(_number, _index) {
         
        try{
            var ele = $N(Mantr_son_xh[_number] );
           
            if (_index == null) {
                ele[ele.length - 1].value = ele.length;
            } else {
                
                for (var i = _index-1, j = ele.length; i < j; i++) {
                  
                    ele[i].value=i+1;
                }
            }
        }catch(e){
            console.log("err:015003");  
        }
    }
    function SYS_ADD_BATCH_ROW(_strTBId){
        var objTab=$("sys_form_content"+_strTBId);
        var objRow=document.createElement("tr");
        objRow.onmouseover=function(){this.style.background='#f1f1f1';};
        objRow.onmouseout=function(){this.style.background='#ffffff';};
        objRow.innerHTML=SYS_ChangeSelId($("NO_SYS_BATCH_ROW"+_strTBId).value,_strTBId);
        var objRowArea = document.createDocumentFragment();
        objTab.appendChild(objRowArea.appendChild(objRow));
        SYS_InitBatchSelId(_strTBId);
        calculate(_strTBId,null);
    }

    function SYS_INSERT_BATCH_ROW(_obj,_strTBId){
        var objTab=$("sys_form_content"+_strTBId);
        var objTr=_obj.parentNode.parentNode;
        var newTr = objTab.insertRow(objTr.rowIndex);
        newTr.onmouseover=function(){this.style.background='#f1f1f1';};
        newTr.onmouseout=function(){this.style.background='#ffffff';};
        newTr.innerHTML=SYS_ChangeSelId($("NO_SYS_BATCH_ROW"+_strTBId).value,_strTBId);
        SYS_InitBatchSelId(_strTBId);
        calculate(_strTBId,objTr.rowIndex);
    }
    
    function SYS_DEL_BATCH_ROW(_obj,_strTBId){
    	var objTr=_obj.parentNode.parentNode
    	var index=objTr.rowIndex;

    	objTr.parentNode.removeChild(objTr);
    
    	delCalculate(_strTBId,index);
    }

    function initDicAndConfig(){
        getTx("","DicManager?STROPTYPE=2"); //初始化字典
        getTx("","dohome?hometype=init");   //初始化配置
    }
    function modifyButton(_strId,_value,_fun){//修改为按钮
        var button = $I(_strId);
        button.setAttribute("type","button");
        button.setAttribute("value",_value);
        button.setAttribute("style","");
        button.setAttribute("class","button red");
        if(_fun!=null){
           button.onclick=_fun; 
        }
    }
// function delSysAll(){
// 	if(confirm("\u786e\u5b9a\u8981\u5220\u9664\u6240\u9009\u6570\u636e\u5417\uff1f")){
// 	var objCheckChilds=document.getElementsByName("syscheckbox");
// 		var iCheckCount=objCheckChilds.length;
// 		for(var i=0;i<iCheckCount;i++){
// 			if(objCheckChilds[i].checked){
// 				getTx($("sysdelvalue"+objCheckChilds[i].value).value,"YLDel");
// 				try{
//     			    var arr = sFlow_evet.S_RUN_AGR[objCheckChilds[i].value].innerHTML.split(",");
//     			    getTx("S_ID="+arr[0]+"&S_RUN_ID="+arr[1]+"&S_FLOW_ID="+arr[2]+"&S_AUTO_VER="+arr[3],"process_fun.v");
    			    
    			    
//     //              var returnUserBz=getTx('s_id=' + arr[0] + '&sys_flow_run_id=' + arr[1] + '&s_flow_id=' + arr[2] +
//     //             '&flow_ver=' + arr[3],"ajax.v");
         
//     //              var LCSFYX='$value';
//     // var jdlx='$value';
//     // var processIndex='$value';
//     // var S_LAUNCH_USER='$value';
    
// 				}catch(e){
// 				    console.log("err:015->delSysAll");
// 				}
// 			}
// 		}
// 		location.reload();
// 	}
// }
    function delSysAll(){
      var arrCurSelRow=sys_getCurSelCheckBox();
      if(arrCurSelRow.length>0){
          if(arrCurSelRow.length>1){
              alert("最多只能选中一条记录进行删除！");
          }else{
              var strComponents = arrCurSelRow[0].value;
              var arr = sFlow_evet.S_RUN_AGR[strComponents].innerHTML.split(",");

              var returnUserBz=getTx('s_id=' + arr[0] + '&sys_flow_run_id=' + arr[1] + '&s_flow_id=' + arr[2] +
                  '&flow_ver=' + arr[3],"ajax.v");
              var CJR = subGetTx(returnUserBz,"S_LAUNCH_USER");
              var LCSFYX = subGetTx(returnUserBz,"LCSFYX");

              var processIndex = subGetTx(returnUserBz,"processIndex");
               var loginUser = subGetTx(returnUserBz,"loginUser");
               alert(arr);
              if(loginUser!="888"){

                  if(processIndex!="0"&&LCSFYX=="0"){
                      alert("此单据已经启动流程");
                      return false;
                  }
                  if(LCSFYX=="1"){
                      alert("流程结束,不可删除单据");
                      return false;
                  }

              }
				
			var message = "确定要删除所选数据吗？";
                if(processIndex==""){
                message="此单据未启动流程,"+message;
            }
              if(confirm(message)){
                  strDelParam=$("sysdelvalue"+strComponents).value;
                  getTx(strDelParam,"YLDel");
                  try{
                      getTx("S_ID="+arr[0]+"&S_RUN_ID="+arr[1]+"&S_FLOW_ID="+arr[2]+"&S_AUTO_VER="+arr[3],"process_fun.v");
                  }catch(e){
                      console.log("err:015->delSysAll");
                  }
              }
              if(strDelParam.indexOf("NO_flush=true")!=-1)
                  parent.lxleft.yltTree.prototype.fushTree('testtree0');
              location.reload();
          }
      }else{
          alert("请选择一条删除项 ");
      }
  }
  

function userTranslation(_obj){
    var AjxGetP = getTx("selUserName="+_obj.value,"ajax.v");
    var selUserName = subGetTx(AjxGetP,'selUserName');
    _obj.value=selUserName;
}
function sys_EditForm(width,height,flag){

    if(flag!=null||flag!=""){
        var arrCurSelRow=sys_getCurSelCheckBox();
		if(arrCurSelRow.length>0){
			if(arrCurSelRow.length>1){
				alert("\u6700\u591a\u53ea\u80fd\u9009\u4e2d\u4e00\u6761\u8bb0\u5f55\u8fdb\u884c\u4fee\u6539\uff01");
			}else{
			    
				// 流程是否结束   ||   设置flag
				var strUrl=$("syseditvalue" + arrCurSelRow[0].value).value+"";
				
				console.log(flag);
				console.log(sFlow_evet.SYS_I_ISOVER[arrCurSelRow[0].value].innerHTML);
				
		        //1:flag 2:innerHTML 
		        
		        if(flag==="detail"&&sFlow_evet.SYS_I_ISOVER[arrCurSelRow[0].value].innerHTML===""){
		            
		        }else if(flag==="detail"||(sFlow_evet.SYS_I_ISOVER[arrCurSelRow[0].value].innerHTML==="1"||sFlow_evet.SYS_I_ISOVER[arrCurSelRow[0].value].innerHTML==="0")){
					strUrl+="&doview=detail";
					
				}
				miniWin('\u4fee\u6539\u4fe1\u606f','',strUrl,2000,1800,'','');
			}
		}else{
			alert("\u8bf7\u9009\u62e9\u4e00\u6761\u4fee\u6539\u9879\uff01");
		}
    }else{

        var arrCurSelRow=sys_getCurSelCheckBox();
	    if(arrCurSelRow.length>0){
		    if(arrCurSelRow.length>1)
			    alert("\u6700\u591a\u53ea\u80fd\u9009\u4e2d\u4e00\u6761\u8bb0\u5f55\u8fdb\u884c\u4fee\u6539\uff01");
		    else{
			    var strUrl=$("syseditvalue"+arrCurSelRow[0].value).value;
			    miniWin('\u4fee\u6539\u4fe1\u606f','',strUrl,_iWidth,_iHeight,'','');
			}
	    }else{
		    alert("\u8bf7\u9009\u62e9\u4e00\u6761\u4fee\u6539\u9879\uff01");
	    }
    }
}
	//2018-07-08 14:28:10  about checkBox

    function sys_admin_del_data_mod_power(){
    	if(confirm("确定要删除所选数据吗？")){
    	var objCheckChilds=document.getElementsByName("syscheckbox");
    	var strDelParam="";
    		var iCheckCount=objCheckChilds.length;
    		for(var i=0;i<iCheckCount;i++){
    			if(objCheckChilds[i].checked){
    				strDelParam=$("sysdelvalue"+objCheckChilds[i].value).value;
    				getTx(strDelParam,"YLDel");
    			}
    		}
    		if(strDelParam.indexOf("NO_flush=true")!=-1)
    			parent.lxleft.yltTree.prototype.fushTree('testtree0');
    		location.reload();
    	}
    }


    function checkBoxEcho(_obj,_checkName){
        S_QLYY = document.getElementById(_obj);
        S_QLYY_ARR = S_QLYY.value.split(",");
        S_QLYYCCC = document.getElementsByName(_checkName);
        for(var i = 0 ; i < S_QLYYCCC.length;i++){
            for(var j = 0 ; j<S_QLYY_ARR.length ; j++){
                if(S_QLYYCCC[i].value==S_QLYY_ARR[j]){
                    S_QLYYCCC[i].checked=true;
                }
            }
        }
    }
    
function dataFormat(_name,_formatStr){
    try{
        var upDat = document.getElementsByName("upDat");
        var datOn = null;
		for(let i = 0 ; i < upDat.length;i++){
		    let obj = upDat[i];
		    let objStr = obj.innerHTML;
    		objStr = objStr.replace(/ /g,"");
    		if(objStr=="") continue;
    		datOn = new Date(objStr);
    		obj.innerHTML = datOn.Format(_formatStr);
		}
    }catch(e){
        console.log("dataFormat-err:015003");  
    }
}

/* 2018-11-10 15:48:53 三权角色 */
function powPersonPut(_name,_code,_bmid){
    try{
       
    
        var general=_name+":0;"+_code+":6";
        general=general.replace(/\$/g,",");
        $I(_name).setAttribute("class","sysselinput");
        $I(_name).onclick=function (){
            miniWin('人员选择','','View?SPAGECODE=154183608275710113&bmid='+_bmid+'&general='+general,1200,800,'','');
        }
    }catch(e){
        console.log("err:personPut"); 
    }
 }
 function powPersonAndBranchPut(_name,_code,_bmName,_bmCode,_bmid){
     try{
    var general=_name+":0;"+_code+":6;"+_bmName+":4;"+_bmCode+":5";
    general=general.replace(/\$/g,",");
    $I(_name).setAttribute("class","sysselinput");
    $I(_name).onclick=function (){
        miniWin('人员选择','','View?SPAGECODE=154183608275710113&bmid='+_bmid+'&general='+general,1200,800,'','');
    }
 }catch(e){
        console.log("err:personAndBranchPut"); 
    }
 }
  function powPersonAllPut(_name,_code,_roleName,_roleCode,_bmName,_bmCode,_bmid){
        try{
    var general=_name+":0;"+_code+":6;"+_bmName+":4;"+_bmCode+":5"+_roleName+":2;"+_roleCode+":3";
    general=general.replace(/\$/g,",");
    $I(_name).onclick=function (){
        miniWin('人员选择','','View?SPAGECODE=154183608275710113&bmid='+_bmid+'&general='+general,1200,800,'','');
    }
  }catch(e){
        console.log("err:personAllPut"); 
    }
 }
 
 var specialChars = "!@#$^&*()_+";
 
 try {
    var uploadicoview = document.getElementsByClassName("uploadicoview");
    for(var i = 0; i<uploadicoview.length; i++) {
        var urlTag = uploadicoview[i].getElementsByTagName("a");
        for(var j = 0; j<urlTag.length; j++) {
            var hrefStr = urlTag[j].href;
			var fileNameArray = hrefStr.substring(hrefStr.lastIndexOf('/')+1,hrefStr.length);
			var fileChar;
			var fileName = '';
			for(var k = 0; k<fileNameArray.length; k++) {
				fileChar = fileNameArray.charAt(k);
				if (specialChars.indexOf(fileChar) != -1) {
					fileChar = encodeURIComponent(fileChar);
				}
				fileName += fileChar;
			}
			
			fileName = fileName.replace(/%%/g,"%25%");
			
            urlTag[j].href=hrefStr.substring(0, hrefStr.lastIndexOf('/')+1) + fileName;
        }
    }
} catch(err) {} 

