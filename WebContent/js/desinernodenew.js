function setStart(){
		if(inputnextnode.value==""){
			alert("请填写下级节点名称!");
			inputnextnode.focus();
			return;
		}
		objOpenPage.ylt.workflow.setNode(inputnextnode.value,obj_sys_startNode);
		closeWin();
	}
function changeType(){
	if(rfztj.checked||rfzhq.checked){//条件
			addnodename.style.display="";
			overnode.style.display="none";
	}else{
			overnode.style.display="";
			addnodename.style.display="none";
	}
}
	function createNode(objStartNode,_iType){		
		var strName=addnodename.value;
		if(strName==""&&_iType==2)
			return;
		if(strName==""&&_iType==1&&overnode.value=="")
			return;
		var strId="";
		if(overnode.value!="")
				strId=overnode.value;
			else
				strId=objOpenPage.ylt.workflow.generId();
		//alert(strId);
		if(strId=="wkn2"){//结束节点
		//alert("结束");
			var objLine=objOpenPage.ylt.workflow.drawLine();
			objOpenPage.ylt.workflow.setLine(objStartNode,objOpenPage.wkn2,objLine);
		}else
			if(objOpenPage.document.getElementById(strId)==null){//如果没有生成
				var objCurNode=objOpenPage.ylt.workflow.addNode(strId,strName,objStartNode,_iType);
			}else{//已有节点
				var objLine=objOpenPage.ylt.workflow.drawLine();
				objLine.bIsHave=true;//已有节点
				objOpenPage.ylt.workflow.setLine(objStartNode,objOpenPage.document.getElementById(strId),objLine);
				}
		return strId;
	}
	function finishConNode(){
		var strName=addnodename.value;
		var objStartNode=obj_sys_startNode;
		var ObjStartNodeName=obj_sys_startNodeNm;
		
		ObjStartNodeName.innerText=hdmc.value;
		objStartNode.nodename=hdmc.value;
		
		
		
		if(strName==""&&overnode.value==""){
			closeWin();
		}else{
			if(textdocon.innerHTML==""&&!bismrcf.checked){
				alert("请选择条件!");
				return;
			}
			var strWinId=createNode(objStartNode,1);
			
			if(bismrcf.checked){
				objStartNode.objCons[strWinId]=nodedefault.value;
				objStartNode.objCN_Cons[strWinId]="默认触发";
			}else{			
				objStartNode.objCons[strWinId]=nodeconid.value;
				objStartNode.objCN_Cons[strWinId]=textdocon.innerHTML;
				}
		}
		
		closeWin();
	}
	function clickMrCf(){
		if(bHsDefault){
			bisxztj.checked=true;
			alert("一个条件网关只允许一个默认出口！");
		}
	}
	function finishNode(){
		var objStartNode=obj_sys_startNode;
		var ObjStartNodeName=obj_sys_startNodeNm;
		
		if(hdmc.value==""){
			alert("活动名称不能为空!");
			hdmc.focus();
			return;
		}
		if(rolecodes.value==""&&usercodes.value==""){
			alert("请选择参与者!");
			rolenames.focus();
			return;
		}
		
		
		if (isNaN(indays.value)||indays.value==""||indays.value.indexOf(".")!=-1){
			alert("限制天数必须为整数！");
			return;
		}
		if (isNaN(inhous.value)||inhous.value==""||inhous.value.indexOf(".")!=-1){
			alert("限制小时必须为整数！");
			return;
		}
		
		
		
		
		
		ObjStartNodeName.innerText=hdmc.value;
		objStartNode.nodename=hdmc.value;
		objStartNode.noderole=rolecodes.value;
		objStartNode.noderolename=rolenames.value;
		
		objStartNode.nodeusers=usercodes.value;
		objStartNode.nodenames=usernames.value;
		
		/***********************************新加*******************************************/
		if(checkishtqr.checked)
			objStartNode.checkishtqr="0";//回退确认
		else
			objStartNode.checkishtqr="1";//回退确认
		
		if(checkisqzht.checked)
			objStartNode.checkisqzht="0";//强制回退
		else
			objStartNode.checkisqzht="1";//强制回退
			
	
		objStartNode.selfhfs=selfhfs.value;//返回方式
		objStartNode.selhtfw=selhtfw.value;//回退范围
		
		if(checklz.checked)
			objStartNode.checklz="0";//流转时通知
		else
			objStartNode.checklz="1";//流转时通知
		
		if(checkht.checked)
			objStartNode.checkht="0";//回退时通知
		else
			objStartNode.checkht="1";//回退时通知
		
		if(checkjs.checked)
			objStartNode.checkjs="0";//结束时通知
		else
			objStartNode.checkjs="1";//结束时通知
		
		if(checkzz.checked)
			objStartNode.checkzz="0";//终止时通知
		else
			objStartNode.checkzz="1";//终止时通知
		
	
		objStartNode.indays=indays.value;//限制天数
		objStartNode.inhous=inhous.value;//限制小时
		
		objStartNode.selclfs=selclfs.value;//处理方式
		
		objStartNode.inmmgz=inmmgz.value;//命名规则
		
		var objBackNodes=document.getElementsByName("zdfwbacknode");
		var iLength=objBackNodes.length;
		objStartNode.strbacknodes="";
		for(var i=0;i<iLength;i++)
			if(objBackNodes[i].checked)
				objStartNode.strbacknodes+=objBackNodes[i].value+",";
		
		/***********************************新加*******************************************/
		
		if(rfztj.checked)//条件
			createNode(objStartNode,2);
		else 
			if(rfzhq.checked)//条件
				createNode(objStartNode,3);
			else
				createNode(objStartNode,1);
		
		closeWin();
	}
	function getConNode(){
		var objStartNode=obj_sys_startNode;
		var strParentId=objStartNode.parentnode;
		
		addHaveNode();//设置已有活动
		getChildNodes();
	}
	function getParentForm(){
		var strParentId=obj_sys_startNode.parentnode;
		var objParent=objOpenPage.document.getElementById(strParentId);
		return objParent.selforms;
	}
	function setUpDateCN(_strWinId,_strCnExp){
		obj_sys_startNode.objCN_Cons[_strWinId]=_strCnExp;
	}
	function updateCon(){
		var strFormId=getParentForm();
		var _strConId=event.srcElement.sConId;
		var strWinId=event.srcElement.strWinId;
		winBox('选择条件','flowform/addcon.jsp?winid='+strWinId+'&cn_exp=s'+_strConId+'&sconid='+_strConId+'&sformid='+strFormId,850,350);
	}
	function addCon(){
		var strFormId=getParentForm();
		var _strConId=nodeconid.value;
		winBox('选择条件','flowform/addcon.jsp?cn_exp=textdocon&sconid='+_strConId+'&sformid='+strFormId,850,350);
	}
	var bHsDefault=false;
	function getChildNodes(){
		var fromLine=obj_sys_startNode.fromline.substr(1);	
		if(fromLine!=""){
		var strArrNodeId=fromLine.split(",");
			for(var i in strArrNodeId){
					var objLine=objOpenPage.document.getElementById(strArrNodeId[i]);
					var objNode=objOpenPage.document.getElementById(objLine.strToId);
					
					var strConId=obj_sys_startNode.objCons[objNode.id];
					
					
			
					var newTr=childnodesmsg.insertRow(1);
					newTr.className="tr1";					
					
					
					var newTd = newTr.insertCell();
					newTd.className="td1";
					newTd.innerHTML=objNode.id;
					
					newTd = newTr.insertCell();
					newTd.className="td1";
					newTd.innerHTML=objNode.nodename;
					
					newTd = newTr.insertCell();
					newTd.className="td1";
					newTd.id="s"+strConId;
					newTd.sConId=strConId;
					newTd.strWinId=objNode.id;
					if(strConId!="SYS_DEFAULT_FLOW_NODE")
						newTd.attachEvent("onclick",updateCon);
					else{
						bHsDefault=true;
						newTd.style.color="green";
						}
					newTd.innerHTML=obj_sys_startNode.objCN_Cons[objNode.id];
				}
	}
	
	
	
	}
	function setcheckValue(_strValue,_objChecked){
		if(_strValue=="1")
			_objChecked.checked=false;
		else
			_objChecked.checked=true;
	}
	var strBackNodes="";
	function setzdfw(_obj){
		if(_obj.value=="2"){
			strBackNodes="";
			generBackNode(obj_sys_startNode);
			tdzdfw.innerHTML=strBackNodes;
		}
	}
	//wkn1
	function generBackNode(_objNode){
		//
		var strToLine=_objNode.toline.substr(1);
		var strArrNodeId=strToLine.split(",");
			for(var i in strArrNodeId){
				var objToLine=objOpenPage.document.getElementById(strArrNodeId[i]);
				var strParentId=objToLine.strFromId;
				var objParent=objOpenPage.document.getElementById(strParentId);
				if(_objNode.strbacknodes.indexOf(strParentId+",")!=-1)
					strBackNodes+="<input name='zdfwbacknode' checked type='checkbox' id='sback"+strParentId+"' value='"+strParentId+"'><label for='sback"+strParentId+"'>"+objParent.nodename+"</a>";
				else
					strBackNodes+="<input name='zdfwbacknode' type='checkbox' id='sback"+strParentId+"' value='"+strParentId+"'><label for='sback"+strParentId+"'>"+objParent.nodename+"</a>";
				
			}
	}
	function getBackNode(){//节点打开时初始化节点属性
		var objStartNode=obj_sys_startNode;
		var strParentId=objStartNode.parentnode;
		var newoption;
		
		
		
		
		/***********************************新加*******************************************/
		
		setcheckValue(objStartNode.checkishtqr,checkishtqr);//回退确认
		setcheckValue(objStartNode.checkisqzht,checkisqzht);//强制回退
		setcheckValue(objStartNode.checklz,checklz);//流转时通知
		setcheckValue(objStartNode.checkht,checkht);//回退时通知
		setcheckValue(objStartNode.checkjs,checkjs);//结束时通知
		setcheckValue(objStartNode.checkzz,checkzz);//终止时通知
	
			
		if(objStartNode.selfhfs!=null)
			selfhfs.value=objStartNode.selfhfs;//返回方式
		if(objStartNode.selhtfw!=null)
			selhtfw.value=objStartNode.selhtfw;//回退范围
		if(objStartNode.indays!=null)
			indays.value=objStartNode.indays;//限制天数
		if(objStartNode.inhous!=null)
			inhous.value=objStartNode.inhous;//限制小时
		if(objStartNode.selclfs!=null)
			selclfs.value=objStartNode.selclfs;//处理方式
		
		if(objStartNode.inmmgz!=null)		
			inmmgz.value=objStartNode.inmmgz;//命名规则
		
		if(selhtfw.value=="2"){
			strBackNodes="";
			generBackNode(obj_sys_startNode);
			tdzdfw.innerHTML=strBackNodes;
		}
		/***********************************新加*******************************************/
		
		
		if(!!objStartNode.noderole){//设置参与者
			rolecodes.value=objStartNode.noderole;
			rolenames.value=objStartNode.noderolename;
			
			usercodes.value=objStartNode.nodeusers;
			usernames.value=objStartNode.nodenames;			
		}else{
			rolecodes.value="";
			rolenames.value="";
			usercodes.value="";
			usernames.value="";	
		}

		addHaveNode();//设置已有活动
	}
	
	
	
function getHQNode(){
		var objStartNode=obj_sys_startNode;
		
		if(!!objStartNode.selforms)//设置表单
			selforms.value=objStartNode.selforms;
		if(objStartNode.selupdateforms!="")
			if(document.getElementById("selupdateforms")!=null)
				selupdateforms.value=objStartNode.selupdateforms;
		
		
		
		if(!!objStartNode.noderole){//设置参与者
			rolecodes.value=objStartNode.noderole;
			rolenames.value=objStartNode.noderolename;
	
		}else{
			rolecodes.value="SYS_ALL_ROLES";
			rolenames.value="所有岗位";
		}
		if(objStartNode.inmmgz!=null)		
			inmmgz.value=objStartNode.inmmgz;//命名规则
		addHaveNode();//设置已有活动
	}	
function finishHQNode(){
		var objStartNode=obj_sys_startNode;
		var ObjStartNodeName=obj_sys_startNodeNm;
		
		if(hdmc.value==""){
			alert("活动名称不能为空!");
			hdmc.focus();
			return;
		}
		if(rolecodes.value==""){
			alert("请选择参与者!");
			rolenames.focus();
			return;
		}
		ObjStartNodeName.innerText=hdmc.value;
		objStartNode.nodename=hdmc.value;
		objStartNode.noderole=rolecodes.value;
		objStartNode.noderolename=rolenames.value;
		alert(objStartNode.noderole+":"+objStartNode.noderolename);
		objStartNode.selforms=selforms.value;	

		objStartNode.inmmgz=inmmgz.value;//命名规则
		
		
		if(rfztj.checked)//条件
			createNode(objStartNode,2);
		else if(rfzhq.checked)//条件
				createNode(objStartNode,3);
			else
			createNode(objStartNode,1);
		
		closeWin();
	}	
	
function addHaveNode(){	
	var arrNodes=objOpenPage.document.getElementsByName("sysflownode");
	var iSize=arrNodes.length;
	var objOption=new Option("---选择已有活动---","");
	overnode.options.add(objOption);
	for(var i =0;i<iSize;i++){
		var strNodeId=arrNodes[i].value;
		if(strNodeId=="wkn1"||strNodeId==obj_sys_startNode.id)
			continue;
		
		var objNode=objOpenPage.document.getElementById(strNodeId);
		if(objNode.parentnode==obj_sys_startNode.id)//是当前节点的子节点
			continue;
		
		if(objNode.nodetype=="2")//属于条件网关
			continue;
		
		objOption=new Option(objNode.nodename,strNodeId);
		overnode.options.add(objOption);
	}
	}