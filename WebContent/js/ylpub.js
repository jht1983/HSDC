var ylt= ylt ||{};
ylt.Pub = ylt.Pub || {};
function $$(_obj){
	alert("ok");
}
var idindex=100;
var yltPub=ylt.Pub={
		clickNode_001:function(_objNode){
			alert(_objNode.t_SYS_BRANCH__S_NAME);
		},
		initNode_001 : function(_objStyle, _objNode) {
		// _objStyle.className='nodetest';
},
		initTree_001:function(_objStyle,_objNode){
			yltTreeGraph.reDrawLine();
		}
	
	}
function doClickLM(_objNode){
	parent.parent.framemain .location="View?SPAGECODE=1392191451562&code="+_objNode.attributes.nodeCode;
}

function clickBranchNode(_objNode){//点击部门设置节点
	parent.lxmain .location="style/addbranch.jsp?code="+_objNode.attributes.nodeCode;
}
function initBranchPage(){//初始化部门设置页面
	parent.lxmain .location="style/addbranch.jsp?code=";
	//tree.expandAll();
}
function clickGWNode(_objNode){//点击岗位设置节点
	parent.lxmain .location="style/addgw.jsp?code="+_objNode.attributes.nodeCode;
}
function msg(){
	alert(testtree_tree.focusNode);
}
function initGWPage(){//初始化岗位设置页面
	parent.lxmain .location="style/addgw.jsp?code=";
	//sys_tree_op.className="sys_tree_op";
	//sys_tree_op.innerHTML="<iframe src='comp?sys_type=branch&paramid=002&sys_root='></iframe>";
}
function clickFlowType(_objNode){//流程管理
	if(_objNode.attributes.nodeCode=="")
		parent.lxmain .location="View?SPAGECODE=1374048026875";
	else
		parent.lxmain .location="View?SPAGECODE=1374048810406&code="+_objNode.attributes.nodeCode;
}
function do_Sys_ViewFormMsg(_objNode){//表单管理
	if(_objNode.attributes.nodeCode=="")
		parent.lxmain .location="View?SPAGECODE=1374048026875";
	else
		parent.lxmain .location="View?SPAGECODE=1375090568046&code="+_objNode.attributes.nodeCode;
}
function desinerFlow(_strFlowId){
	//流程设计
	miniWin('\u6d41\u7a0b\u8bbe\u8ba1','','FlowFactory?flowid='+_strFlowId,1300,700,'','');
}
function desiner_Sys_Form(_strFormId){
	window.location="comp?sys_type=formdesiner&sys_formid="+_strFormId;
}
function view_Sys_Form(_strFormId){
	window.location="modexcel/"+_strFormId+".jsp";
}
	//启动会议
function startMeet(){
	//流程设计
	miniWin('\u6d41\u7a0b\u8bbe\u8ba1','','test.jsp',1300,700,'','');
}


function importComponent_Plan(_strPid_PlanId){

	var iIndex=_strPid_PlanId.indexOf("_");
	var _strPid=_strPid_PlanId.substring(0,iIndex);
	var _strXH=_strPid_PlanId.substring(iIndex+1);
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var strComponents="";
	
	var objTable=document.getElementById("tb");
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				strComponents+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[1].innerText;
			}
		}
		if(strComponents!=""){
			strComponents=strComponents.substring(1);
			var vResult=getTx("components="+strComponents,"jsp/solvecomponent_plan.jsp?pid="+_strPid+"&xh="+_strXH);		
			//alert(vResult);			
			parent.parent.closeById(parent.lxleft.gs_upl_kc);
		}
}


function importComponent(_strPid_XH){
	var iIndex=_strPid_XH.indexOf("*!");
	var _strPid=_strPid_XH.substring(0,iIndex);
	var _strXH=_strPid_XH.substring(iIndex+2);
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var strComponents="";
	var strComponentsCount="";
	var objTable=document.getElementById("tb");
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				strComponents+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[1].innerText;
				strComponentsCount+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[3].innerText;
			}
		}
		if(strComponents!=""){
			strComponents=strComponents.substring(1);
			strComponentsCount=strComponentsCount.substring(1);
			//alert(strComponents);
			//alert(strComponentsCount);
			//
			var vResult=getTx("components="+strComponents+"&componentscount="+strComponentsCount,"jsp/solvecomponent.jsp?pid="+_strPid+"&xh="+_strXH);		
			//alert(vResult);			
			parent.parent.closeById(parent.lxleft.gs_upl_kc);
		}
}

function importComponent_Vir(_strPid_XH){
	var iIndex=_strPid_XH.indexOf("_");
	var _strPid=_strPid_XH.substring(0,iIndex);
	var _strXH=_strPid_XH.substring(iIndex+1);
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var strComponents="";
	var strComponentsCount="";
	var objTable=document.getElementById("tb");
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				strComponents+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[2].innerText;
				strComponentsCount+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[4].innerText;
			}
		}
		if(strComponents!=""){
			strComponents=strComponents.substring(1);
			strComponentsCount=strComponentsCount.substring(1);
			//alert(strComponents);
			//alert(strComponentsCount);
			var vResult=getTx("components="+strComponents+"&componentscount="+strComponentsCount,"jsp/solvecomponent_vir.jsp?pid="+_strPid+"&xh="+_strXH);		
			//alert(vResult);			
			parent.parent.closeById(parent.lxleft.gs_upl_kc);
		}
}


function importComponent_zcd(_strPid_XH){
	var iIndex=_strPid_XH.indexOf("_");
	var _strPid=_strPid_XH.substring(0,iIndex);
	var _strXH=_strPid_XH.substring(iIndex+1);
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var strComponents="";
	var strComponentsCount="";
	var objTable=document.getElementById("tb");
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				strComponents+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[2].innerText;
				strComponentsCount+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[4].innerText;
			}
		}
		if(strComponents!=""){
			strComponents=strComponents.substring(1);
			strComponentsCount=strComponentsCount.substring(1);
			//alert(strComponents);
			//alert(strComponentsCount);
			var vResult=getTx("components="+strComponents+"&componentscount="+strComponentsCount,"jsp/solvecomponent_zcd.jsp?pid="+_strPid+"&xh="+_strXH);		
			//alert(vResult);			
			parent.parent.closeById(parent.lxleft.gs_upl_kc);
		}
}


function importComponent_fcd(_strPid_XH){
	var iIndex=_strPid_XH.indexOf("_");
	var _strPid=_strPid_XH.substring(0,iIndex);
	var _strXH=_strPid_XH.substring(iIndex+1);
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var strComponents="";
	var strComponentsCount="";
	var objTable=document.getElementById("tb");
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				strComponents+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[2].innerText;
				strComponentsCount+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[4].innerText;
			}
		}
		if(strComponents!=""){
			strComponents=strComponents.substring(1);
			strComponentsCount=strComponentsCount.substring(1);
			//alert(strComponents);
			//alert(strComponentsCount);
			var vResult=getTx("components="+strComponents+"&componentscount="+strComponentsCount,"jsp/solvecomponent_fcd.jsp?pid="+_strPid+"&xh="+_strXH);		
			//alert(vResult);			
			parent.parent.closeById(parent.lxleft.gs_upl_kc);
		}
}


function importComponent_zcd_xb(_strPid_XH){
	var iIndex=_strPid_XH.indexOf("*!");
	var _strPid=_strPid_XH.substring(0,iIndex);
	var _strXH=_strPid_XH.substring(iIndex+2);
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var strComponents="";
	var strComponentsCount="";
	var objTable=document.getElementById("tb");
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				strComponents+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[2].innerText;
			}
		}
		if(strComponents!=""){
			strComponents=strComponents.substring(1);

			//alert(strComponents);
		
			//alert(strComponentsCount);
			var vResult=getTx("components="+strComponents,"jsp/solvecomponent_zcd_xb.jsp?pid="+_strPid+"&zcd="+_strXH);		
			//alert(vResult);			
			parent.closeById(gs_upl_kc);
		}
}


function importComponent_fcd_xb(_strPid_XH){

	var iIndex=_strPid_XH.indexOf("_");
	var _strPid=_strPid_XH.substring(0,iIndex);
	var _strXH=_strPid_XH.substring(iIndex+1);
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var strComponents="";
	var strComponentsCount="";
	var objTable=document.getElementById("tb");
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				strComponents+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[1].innerText;
			}
		}
		if(strComponents!=""){
			strComponents=strComponents.substring(1);

			//alert(strComponents);
		
			//alert(strComponentsCount);
			var vResult=getTx("components="+strComponents,"jsp/solvecomponent_fcd_xb.jsp?pid="+_strPid+"&zcd="+_strXH);		
			//alert(vResult);			
			parent.closeById(gs_upl_kc);
		}
}
function sys_RePass(){

	var objTable=document.getElementById("tb");
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var iCheckCount=objCheckChilds.length;
	var bIsReSucces=true;
	//确定要重置所选用户的密码吗？
	if(confirm("\u786e\u5b9a\u8981\u91cd\u7f6e\u6240\u9009\u7528\u6237\u7684\u5bc6\u7801\u5417\uff1f")){
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				var strUid=objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[1].innerText;
				getTx("NO_OPTYPE=1&T_RGXX$SYGZW_NEW="+strUid.trim()+"&T_RGXX$SYGMM=1&NO_CON=T_RGXX$SYGZW_NEW","YLWebAction");
				bIsReSucces=false;
			}
		}
		
		//if 请至少选择一个需要重置密码的用户！
		//else 重置成功！
		if(bIsReSucces)
			alert("\u8bf7\u81f3\u5c11\u9009\u62e9\u4e00\u4e2a\u9700\u8981\u91cd\u7f6e\u5bc6\u7801\u7684\u7528\u6237\uff01");
		else
			alert("\u91cd\u7f6e\u6210\u529f\uff01");
	}
}


function updateCount(_obj){
	var strCurValue=_obj.value;
	if (isNaN(strCurValue)||strCurValue==""){
		//数量必须是数字！
		alert("\u6570\u91cf\u5fc5\u987b\u662f\u6570\u5b57\uff01");
		_obj.focus();
		return;
	}
	var objCurRow=_obj.parentElement.parentElement;
	var iCurCount=parseInt(strCurValue);
	
		
	var iWZSL=parseInt(objCurRow.cells[6].innerText);
	var iZSL=parseInt(objCurRow.cells[7].innerText);
	
	
	
	var iOldValue=parseInt(_obj.attributes.oldvalue.value);
	
	var iMaxCount=iOldValue+iWZSL;
	if(iCurCount<=0){
		//装箱数量必须大于0
		alert("\u88c5\u7bb1\u6570\u91cf\u5fc5\u987b\u5927\u4e8e\u0030");
		return;
	}
	
	if(iCurCount>iMaxCount){
		//数量输入错误，该构件未装箱数量只剩
		//件
		alert("\u6570\u91cf\u8f93\u5165\u9519\u8bef\uff0c\u8be5\u6784\u4ef6\u672a\u88c5\u7bb1\u6570\u91cf\u53ea\u5269"+iMaxCount+"\u4ef6");
		_obj.value=iOldValue;
		return;
	}
	var strXH=objCurRow.cells[1].innerText;
	
	var strGJH=objCurRow.cells[2].innerText;
	var iNewWZXSL=iMaxCount-iCurCount;
	var vResult=getTx("xh="+strXH+"&gjh="+strGJH+"&sl="+iCurCount+"&wzsl="+iNewWZXSL,"jsp/updatesl.jsp");
	if(vResult=="ok"){
		_obj.attributes.oldvalue.value=iCurCount;
		objCurRow.cells[6].innerHTML="<div style='width:100%;height:100%;background-color:green;color:yellow;text-align:center;font-weight :bold;'>"+iNewWZXSL+"</div>";
	}
}

function doGenerMT(_strXH){
	var iIndex=_strXH.indexOf("_");
	var _strPid=_strXH.substring(0,iIndex);
	var _strXH=_strXH.substring(iIndex+1);
	//麦头
	miniWin('\u9ea6\u5934','','genermt.jsp?xh='+_strXH+"&pid="+_strPid,800,600,'','');

}
function doGenerZxdMx(){
	
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var objTable=$('tb');
	var strZxdh="";
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked)
				strZxdh+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[1].innerText;
		}
		//if 请至少选择一条装箱单！
		//else 麦头
		if(strZxdh=="")
			alert("\u8bf7\u81f3\u5c11\u9009\u62e9\u4e00\u6761\u88c5\u7bb1\u5355\uff01");
		else
			miniWin('\u9ea6\u5934','','generzxqdmx.jsp?xh='+strZxdh.substring(1),parent.parent.parent.iScreen_Width-50,parent.parent.parent.iScreen_Height-50,'','');

}
function doGenerZxdMx_Mt(){
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var objTable=$('tb');
	var strZxdh="";
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked)
				strZxdh+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[1].innerText;
		}
		//if 
		//else 
		if(strZxdh=="")
			alert("\u8bf7\u81f3\u5c11\u9009\u62e9\u4e00\u6761\u88c5\u7bb1\u5355\uff01");
		else
			miniWin('\u9ea6\u5934','','generzxqdmx_mt.jsp?xh='+strZxdh.substring(1),parent.parent.parent.iScreen_Width-50,parent.parent.parent.iScreen_Height-50,'','');
}
function submitComChange(_strBgDh){
	//确定要提交变更吗？
	if(confirm("\u786e\u5b9a\u8981\u63d0\u4ea4\u53d8\u66f4\u5417\uff1f")){ 
		getTx("NO_OPTYPE=1&t_comchange$S_CHANGE_ID="+_strBgDh+"&t_comchange$S_BGDSTATUS=1404888406328&NO_CON=t_comchange$S_CHANGE_ID","YLWebAction");
		parent.closeById(gs_upl_kc);
	}
}
function auditgjsh_tg(_strBgDh){
	//确定要  [通过]  构件变更吗？
	if(confirm("\u786e\u5b9a\u8981\u0020\u0020\u005b\u901a\u8fc7\u005d\u0020\u0020\u6784\u4ef6\u53d8\u66f4\u5417\uff1f")){ 
		var vResult=getTx("bgdh="+_strBgDh,"jsp/auditpass.jsp");
		window.location.reload();
	}
}
function auditgjsh_th(_strBgDh){
	//确定要  [退回]  构件变更吗
	if(confirm("\u786e\u5b9a\u8981\u0020\u0020\u005b\u9000\u56de\u005d\u0020\u0020\u6784\u4ef6\u53d8\u66f4\u5417")){ 
		var vResult=getTx("NO_OPTYPE=1&t_comchange$S_CHANGE_ID="+_strBgDh+"&t_comchange$S_BGDSTATUS=1405063714721&NO_CON=t_comchange$S_CHANGE_ID","YLWebAction");
		window.location.reload();
	}
}
function doUpdatePc(_strPc,_strPcName,_strPid){
	var objDataTable=document.getElementById("tb");	
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_parts$S_GJH="+objDataTable.rows[i].cells[0].innerText+"&t_parts$S_PID="+_strPid+"&t_parts$S_PARTID="+objDataTable.rows[i].cells[1].innerText+"&t_parts$S_SSPC="+_strPc+"&NO_CON=t_parts$S_GJH,t_parts$S_PARTID,t_parts$S_PID","YLWebAction");
		//strLjCon+=
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML=_strPcName;
	}
}

function doUpdatePc_gj(_strPc,_strPcName,_strPid){
	var objDataTable=document.getElementById("tb");	
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_com$S_GJH="+objDataTable.rows[i].cells[0].innerText+"&t_com$S_PID="+_strPid+"&t_com$S_SSPC="+_strPc+"&NO_CON=t_com$S_GJH,t_com$S_PID","YLWebAction");
		//strLjCon+=
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML=_strPcName;
	}
}

function doUpdate_zzgc(_strPc,_strPcName){
	var objDataTable=document.getElementById("tb");
	var strFgdw=_strPc;
	if(strFgdw!='001001001004002001'&&strFgdw!='001001001004002002'&&strFgdw!='PENDING')
		strFgdw='001003';
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_component$S_GJH="+objDataTable.rows[i].cells[0].innerText+"&t_component$S_ZZGC="+_strPc+"&t_component$S_BRANCHID="+strFgdw+"&NO_CON=t_component$S_GJH","YLWebAction");
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML=_strPcName;
	}
}

function doUpdate_zzgc_fgdw(_strPc,_strPcName){
	var objDataTable=document.getElementById("tb");
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_component$S_GJH="+objDataTable.rows[i].cells[0].innerText+"&t_component$S_BRANCHID="+_strPc+"&NO_CON=t_component$S_GJH","YLWebAction");
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML=_strPcName;
	}
}


function doUpdatePc_update(_strPc,_strPcName){
	var objDataTable=document.getElementById("tb");
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_component$S_GJH="+objDataTable.rows[i].cells[1].innerText+"&t_component$S_PCBH="+_strPc+"&NO_CON=t_component$S_GJH","YLWebAction");
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML=_strPcName;
	}
}
function doUpdatePlan(_strPc,_strPcName){
	var objDataTable=document.getElementById("tb");
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_component$S_GJH="+objDataTable.rows[i].cells[0].innerText+"&t_component$S_PLAN_ID="+_strPc+"&NO_CON=t_component$S_GJH","YLWebAction");
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML=_strPcName;
	}
}
function doUpdatePlan_update(_strPc,_strPcName){
	var objDataTable=document.getElementById("tb");
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_component$S_GJH="+objDataTable.rows[i].cells[1].innerText+"&t_component$S_PLAN_ID="+_strPc+"&NO_CON=t_component$S_GJH","YLWebAction");
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML=_strPcName;
	}
}
function sys_update_com_type(_strPid){
	var objDataTable=document.getElementById("tb");
	var strSelValue=sys_sel_com_type_tree.value;
	var strSelTerxt=sys_sel_com_type_tree.text;
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_component_vir$S_GJH="+objDataTable.rows[i].cells[1].innerText+"&t_component_vir$S_PROJIECTID="+_strPid+"&t_component_vir$S_TYPEID="+strSelValue+"&NO_CON=t_component_vir$S_GJHt_component_vir$S_PROJIECTID","YLWebAction");
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML=strSelTerxt;
	}
}
function do_jiaogong(){
	var objDataTable=document.getElementById("tb");
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_component$S_GJH="+objDataTable.rows[i].cells[0].innerText+"&t_component$S_COMPONENTSTATUS=5&NO_CON=t_component$S_GJH","YLWebAction");
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML="已交工";
	}
}
function sys_update_sel_cells_update(){

	var objDataTable=document.getElementById("tb");
	var strSelValue=sys_sel_branche_tree.value;
	var strSelTerxt=sys_sel_branche_tree.text;
	

	
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_component$S_GJH="+objDataTable.rows[i].cells[0].innerText+"&t_component$S_BRANCHID="+strSelValue+"&NO_CON=t_component$S_GJH","YLWebAction");
		objDataTable.rows[i].cells[i_sys_msd_cells_index].innerHTML=strSelTerxt;
	}
}
function do_view_wfq_vir_com(){
	parent.lxmain .location="View?SPAGECODE=1399530559133_1&type=&pid="+selprojectname.value;
}
function doGenerCHGJ(_aPidXH){
	var iIndex=_aPidXH.indexOf("_");
	var _strPid=_aPidXH.substring(0,iIndex);
	var _strXH=_aPidXH.substring(iIndex+1);
	//装箱清单_虚拟构件(箱号:
	miniWin('\u88c5\u7bb1\u6e05\u5355\u005f\u865a\u62df\u6784\u4ef6\u0028\u7bb1\u53f7\u003a'+_strXH+')','','View?SPAGECODE=1409496832014&xh='+_strXH+'&pid='+_strPid,
	parent.parent.parent.iScreen_Width-50,
	parent.parent.parent.iScreen_Height-50,'','');


}
function show_xh_gj(_strXH){
	var iIndex=_strXH.indexOf("*!");
	var _strPid=_strXH.substring(0,iIndex);
	var _strXH=_strXH.substring(iIndex+2);
	//装箱清单(箱号:
	miniWin('\u88c5\u7bb1\u6e05\u5355\u0028\u7bb1\u53f7\u003a'+_strXH+')','','View?SPAGECODE=1399615594440&xh='+_strXH+'&pid='+_strPid+'',parent.parent.parent.iScreen_Width-50,parent.parent.parent.iScreen_Height-50,'','');

}
function show_cph_gj(_strXH){
	var iIndex=_strXH.indexOf("*!");
	var _strPid=_strXH.substring(0,iIndex);
	var _strXH=_strXH.substring(iIndex+2);
	//装车单:
	miniWin('\u88c5\u8f66\u5355\u003a'+_strXH+'','','View?SPAGECODE=1401789659044&zcd='+_strXH+'&pid='+_strPid+'',parent.parent.parent.iScreen_Width-50,parent.parent.parent.iScreen_Height-50,'','');
}
function deltzml(_strPGT){
	var arrStr=_strPGT.split("_");
	var strPid=arrStr[0];
	var strGraphId=arrStr[1];
	var strTypeId=arrStr[2];
	//确定要删除图纸目录吗？
	if(confirm("\u786e\u5b9a\u8981\u5220\u9664\u56fe\u7eb8\u76ee\u5f55\u5417\uff1f")){
		window.location="jsp/delgraphml.jsp?pid="+strPid+"&type="+strTypeId+"&gid="+strGraphId;
	
	}

}
function getUrl(str) {
	var url = unescape(window.location.href);
	var allargs = url.split("?")[1];
	var args = allargs.split("&");
	for ( var i = 0; i < args.length; i++) {
		var arg = args[i].split("=");
		if (arg[0] == str) {
			return arg[1];
		}
	}
}
/**
 * 删除图形
 */
function delGraph(){
	var objCheckChilds = document.getElementsByName("syscheckbox");
	var iCheckCount = objCheckChilds.length;
	var tbRow = new Array();
	tbRow = sys_getCurSelRow();
	var k = 0;
	var spagecode ;
	for ( var i = 0; i < iCheckCount; i++) {
		if (objCheckChilds[i].checked){
			spagecode = getText(tbRow[k++].cells[2]);
			var str = "method=delData&from=t_sys_pagemsg&where=SPAGECODE LIKE '"+spagecode+"~'";
			var result = getTx(str,"graph");
		}
	}  
	
	if(result=="y"){
		//删除成功
		alert("\u5220\u9664\u6210\u529f");
		location.reload();
	}
}


function getH(){return document.body.clientHeight;}

function getW(){return document.body.clientWidth;}

function addGraphPage(){
	var spagecode = getTx("method=generalSpagecode", "graph");
	//图形配置
	miniWin("\u56fe\u5f62\u914d\u7f6e","","graph.jsp?spagecode="+spagecode,getW()+170,getH()+50,"","");
}

function editGraphPage(){
	var objCheckChilds = document.getElementsByName("syscheckbox");
	var iCheckCount = objCheckChilds.length;
	var tbRow = new Array();
	tbRow = sys_getCurSelRow();
	var k = 0;
	var spagecode ;
	for ( var i = 0; i < iCheckCount; i++) {
		if (objCheckChilds[i].checked){
			spagecode = getText(tbRow[k++].cells[2]);
			break;
		}
	}
	//图形配置
	miniWin("\u56fe\u5f62\u914d\u7f6e","","graph.jsp?spagecode="+spagecode,getW()+170,getH()+50,"","");
}


function getUrl(str) {
	var url = unescape(window.location.href);
	var allargs = url.split("?")[1];
	var args = allargs.split("&");
	for ( var i = 0; i < args.length; i++) {
		var arg = args[i].split("=");
		if (arg[0] == str) {
			return arg[1];
		}
	}
}


