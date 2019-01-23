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
	miniWin('流程设计','','FlowFactory?flowid='+_strFlowId,1300,700,'','');
}
function desiner_Sys_Form(_strFormId){
	window.location="comp?sys_type=formdesiner&sys_formid="+_strFormId;
}
function view_Sys_Form(_strFormId){
	window.location="modexcel/"+_strFormId+".jsp";
}
	//启动会议
function startMeet(){
	miniWin('流程设计','','test.jsp',1300,700,'','');
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
	if(confirm("确定要重置所选用户的密码吗？")){
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked){
				var strUid=objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[1].innerText;
				getTx("NO_OPTYPE=1&T_RGXX$SYGZW="+strUid+"&T_RGXX$SYGMM=1&NO_CON=T_RGXX$SYGZW","YLWebAction");
				bIsReSucces=false;
			}
		}
		if(bIsReSucces)
			alert("请至少选择一个需要重置密码的用户！");
		else
			alert("重置成功！");
	}
}


function updateCount(_obj){
	var strCurValue=_obj.value;
	if (isNaN(strCurValue)||strCurValue==""){
		alert("数量必须是数字！");
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
		alert("装箱数量必须大于0");
		return;
	}
	
	if(iCurCount>iMaxCount){		
		alert("数量输入错误，该构件未装箱数量只剩"+iMaxCount+"件");
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
	miniWin('麦头','','genermt.jsp?xh='+_strXH+"&pid="+_strPid,800,600,'','');

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
		if(strZxdh=="")
			alert("请至少选择一条装箱单！");
		else
			miniWin('麦头','','generzxqdmx.jsp?xh='+strZxdh.substring(1),parent.parent.parent.iScreen_Width-50,parent.parent.parent.iScreen_Height-50,'','');

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
		if(strZxdh=="")
			alert("请至少选择一条装箱单！");
		else
			miniWin('麦头','','generzxqdmx_mt.jsp?xh='+strZxdh.substring(1),parent.parent.parent.iScreen_Width-50,parent.parent.parent.iScreen_Height-50,'','');
}
function submitComChange(_strBgDh){
	if(confirm("确定要提交变更吗？")){ 
		getTx("NO_OPTYPE=1&t_comchange$S_CHANGE_ID="+_strBgDh+"&t_comchange$S_BGDSTATUS=1404888406328&NO_CON=t_comchange$S_CHANGE_ID","YLWebAction");
		parent.closeById(gs_upl_kc);
	}
}
function auditgjsh_tg(_strBgDh){
	if(confirm("确定要  [通过]  构件变更吗？")){ 
		var vResult=getTx("bgdh="+_strBgDh,"jsp/auditpass.jsp");
		window.location.reload();
	}
}
function auditgjsh_th(_strBgDh){
	if(confirm("确定要  [退回]  构件变更吗")){ 
		var vResult=getTx("NO_OPTYPE=1&t_comchange$S_CHANGE_ID="+_strBgDh+"&t_comchange$S_BGDSTATUS=1405063714721&NO_CON=t_comchange$S_CHANGE_ID","YLWebAction");
		window.location.reload();
	}
}
function doUpdatePc(_strPc,_strPcName){
	var objDataTable=document.getElementById("tb");
	for(var i=i_sys_msd_start_row;i<=i_sys_msd_end_row;i++){
		getTx("NO_OPTYPE=1&t_component$S_GJH="+objDataTable.rows[i].cells[0].innerText+"&t_component$S_PCBH="+_strPc+"&NO_CON=t_component$S_GJH","YLWebAction");
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
	miniWin('装箱清单_虚拟构件(箱号:'+_strXH+')','','View?SPAGECODE=1409496832014&xh='+_strXH+'&pid='+_strPid,
	parent.parent.parent.iScreen_Width-50,
	parent.parent.parent.iScreen_Height-50,'','');


}
function show_xh_gj(_strXH){
	var iIndex=_strXH.indexOf("_");
	var _strPid=_strXH.substring(0,iIndex);
	var _strXH=_strXH.substring(iIndex+1);
	miniWin('装箱清单(箱号:'+_strXH+')','','View?SPAGECODE=1399615594440&xh='+_strXH+'&pid='+_strPid+'',parent.parent.parent.iScreen_Width-50,parent.parent.parent.iScreen_Height-50,'','');

}
function show_cph_gj(_strXH){
	var iIndex=_strXH.indexOf("_");
	var _strPid=_strXH.substring(0,iIndex);
	var _strXH=_strXH.substring(iIndex+1);
	miniWin('装车单:'+_strXH+'','','View?SPAGECODE=1401789371249&zcd='+_strXH+'&pid='+_strPid+'',parent.parent.parent.iScreen_Width-50,parent.parent.parent.iScreen_Height-50,'','');
}
function deltzml(_strPGT){
	var arrStr=_strPGT.split("_");
	var strPid=arrStr[0];
	var strGraphId=arrStr[1];
	var strTypeId=arrStr[2];
	if(confirm("确定要删除图纸目录吗？")){
		window.location="jsp/delgraphml.jsp?pid="+strPid+"&type="+strTypeId+"&gid="+strGraphId;
	
	}

}