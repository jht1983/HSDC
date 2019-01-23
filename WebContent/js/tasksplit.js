var iNodeWidth=150,iNodeHeight=40,iOvlX=140,iOvlY=20,iOvlTwoX=50,iOvlTwoY=20,iNodeArea=350,objCurNode=null,strRoot="o000",objRoot=null,strTaskTable="",strTaskId="",strInitTxt="中心主题";
	function createNode(){
		if (window.event.keyCode==13)
			generChilNode(objCurNode);
	}
	function getChildNodeId(_objParent){
		var iNodeId=_objParent.iCount++;
		var strTempNodeId=""+iNodeId;
		if(iNodeId<10)
			strTempNodeId="00"+iNodeId;
		else if(iNodeId<100)
			strTempNodeId="0"+iNodeId;
			
		return _objParent.id+strTempNodeId;
	}
	function generChilNode(_objParent){
		var iNodeY;
		var iNodeX;
		if(_objParent.id==strRoot){
			if(_objParent.iCount%2==0){//右		
				iNodeX=_objParent.iX+iNodeArea;
				_objParent.iLeftCount++%2==0?iNodeY=_objParent.iInitTopY=_objParent.iInitTopY-50:iNodeY=_objParent.iInitBotY=_objParent.iInitBotY+50;			
			}else{//左
				iNodeX=_objParent.iX-iNodeArea;
				_objParent.iRightCount++%2!=0?iNodeY=_objParent.iInitLeftTopY=_objParent.iInitLeftTopY-50:iNodeY=_objParent.iInitLeftBotY=_objParent.iInitLeftBotY+50;
			}
		}else{
			if(_objParent.iX>objRoot.iX){//左边
				if(_objParent.iLeftCount==0){///////////////////////////加第一个节点，此处需要修改
					iNodeX=_objParent.iX+iNodeArea;
					iNodeY=_objParent.iInitTopY;
					_objParent.iLeftCount++;
				}else{
					iNodeX=_objParent.iX+iNodeArea;
					_objParent.iLeftCount++%2==0?iNodeY=_objParent.iInitTopY=_objParent.iInitTopY-50:iNodeY=_objParent.iInitBotY=_objParent.iInitBotY+50;
				}
			}else{
				if(_objParent.iRightCount==0){//右边
					iNodeX=_objParent.iX-iNodeArea;
					iNodeY=_objParent.iInitLeftTopY;
					_objParent.iRightCount++;
				}else{
					iNodeX=_objParent.iX-iNodeArea;
					_objParent.iRightCount++%2!=0?iNodeY=_objParent.iInitLeftTopY=_objParent.iInitLeftTopY-50:iNodeY=_objParent.iInitLeftBotY=_objParent.iInitLeftBotY+50;
				}
			}
		
		}
		resetNode(_objParent,iNodeY);
		generLine(_objParent,generNode(iNodeX,iNodeY,getChildNodeId(_objParent)));
	}
	function $(_strId){
		return document.getElementById(_strId);
	}
	function setFatherPosMsg(_objFather,_objParent,_iNodeY){
		if(_objParent.iX>_objFather.iX){//右边
			if(_iNodeY<_objParent.iY)//上边
				_objFather.iInitTopY=_objFather.iInitTopY-50;
			else//下边
				_objFather.iInitBotY=_objFather.iInitBotY+50;
		}else{//左边
			if(_iNodeY<_objParent.iY)//上边
				_objFather.iInitLeftTopY=_objFather.iInitLeftTopY-50;
			else//下边
				_objFather.iInitLeftBotY=_objFather.iInitLeftBotY+50;		
		}
			
		
	}
	function getParentId(_strNodeId){
		var iIdLength=_strNodeId.length;
		return _strNodeId.substring(0,(iIdLength-3));
	}
	function getChildNode(_strParentId,_iCount){
		var strCount=""+_iCount;
		if(_iCount<10)
			strCount="00"+_iCount;
		else if(_iCount<100)
			strCount="0"+_iCount;
		return $(_strParentId+strCount);
	}
	function resetNode(_objParent,_iNodeY){
		if(_objParent.id==strRoot)
			return;
		var strParentNodeId=getParentId(_objParent.id);
		var objParentFather=$(strParentNodeId);
		
		setFatherPosMsg(objParentFather,_objParent,_iNodeY)//设置爷节点坐标信息
		
		var iParentChildCount=objParentFather.iCount;
		for(var i=0;i<iParentChildCount;i++){
			var objCurNode=getChildNode(strParentNodeId,i);
			if(objCurNode.id==_objParent.id)
				continue;
				
		
			if(objCurNode.iX==_objParent.iX){
				if(_iNodeY<_objParent.iY&&objCurNode.iY<_objParent.iY){//加向上子节点并且父节点的兄弟节点在父节点之上
					objCurNode.iY=objCurNode.iY-50;
					objCurNode.style.top=objCurNode.iY;
					
					if(objCurNode.iX<objParentFather.iX){//叔节点
						setPosMsg(objCurNode,-50,-1);//左边
					}else{
						setPosMsg(objCurNode,-50,1);//右边						
					}
					
					setLinePos($(objCurNode.id+"_line"),$(strParentNodeId),objCurNode);
					setLineOvl($(objCurNode.id+"_line"),$(strParentNodeId),objCurNode);
					
					resetCurNode(objCurNode,-1);
					
				}else if(_iNodeY>_objParent.iY&&objCurNode.iY>_objParent.iY){//加向下子节点并且父节点的兄弟节点在父节点之下
					objCurNode.iY=objCurNode.iY+50;
					objCurNode.style.top=objCurNode.iY;
					
					if(objCurNode.iX<objParentFather.iX){
						setPosMsg(objCurNode,50,-1);//左边
					}else{
						setPosMsg(objCurNode,50,1);//右边
					}
					
					
					setLinePos($(objCurNode.id+"_line"),$(strParentNodeId),objCurNode);
					setLineOvl($(objCurNode.id+"_line"),$(strParentNodeId),objCurNode);
					
					resetCurNode(objCurNode,1);
				}
			}
		}
			
	}
	//设置位置信息
	function setPosMsg(_objNode,_iStep,_iType){
		if(_iType==-1){//左
			_objNode.iInitLeftTopY=_objNode.iInitLeftTopY+_iStep;
			_objNode.iInitLeftBotY=_objNode.iInitLeftBotY+_iStep;
		}else{//右
			_objNode.iInitTopY=_objNode.iInitTopY+_iStep;//右下
			_objNode.iInitBotY=_objNode.iInitBotY+_iStep;//右下
		}
	}
	function resetCurNode(_objNode,iType){
		if(iType==-1){
			if(_objNode.iCount>0){//有子集
				for(var i=0;i<_objNode.iCount;i++){
					var objCurNode=getChildNode(_objNode.id,i);
					objCurNode.iY=objCurNode.iY-50;
					objCurNode.style.top=objCurNode.iY;
					
					
					objCurNode.iInitTopY=objCurNode.iInitTopY-50;//右上
					objCurNode.iInitBotY=objCurNode.iInitBotY-50;//右下
					objCurNode.iInitLeftTopY=objCurNode.iInitLeftTopY-50;
					objCurNode.iInitLeftBotY=objCurNode.iInitLeftBotY-50;
					
					
					setLinePos($(objCurNode.id+"_line"),_objNode,objCurNode);
					setLineOvl($(objCurNode.id+"_line"),_objNode,objCurNode);
					
					resetCurNode(objCurNode,iType);//递归下级
				}
			}		
		}else{
			if(_objNode.iCount>0){//有子集
				for(var i=0;i<_objNode.iCount;i++){
					var objCurNode=getChildNode(_objNode.id,i);
					objCurNode.iY=objCurNode.iY+50;
					objCurNode.style.top=objCurNode.iY;
					
					objCurNode.iInitTopY=objCurNode.iInitTopY+50;//右上
					objCurNode.iInitBotY=objCurNode.iInitBotY+50;//右下
					objCurNode.iInitLeftTopY=objCurNode.iInitLeftTopY+50;
					objCurNode.iInitLeftBotY=objCurNode.iInitLeftBotY+50;
					
					
					setLinePos($(objCurNode.id+"_line"),_objNode,objCurNode);
					setLineOvl($(objCurNode.id+"_line"),_objNode,objCurNode);
					
					resetCurNode(objCurNode,iType);//递归下级
				}
			}	
		
		}
		
	}
	function generNode(_iX,_iY,_strId){
		var objNode=document.createElement("<div onclick='clickNode(this);'>");
		objNode.setAttribute("id",_strId);
		objNode.className="nodestyle";
		objNode.style.left=_iX;
		objNode.style.top=_iY;
		objNode.style.width=iNodeWidth;
		objNode.style.height=iNodeHeight;
		objNode.iX=_iX;
		objNode.iY=_iY;
		
		objNode.iCount=0;		
		
		objNode.iInitTopY=_iY-30;//右上
		objNode.iInitBotY=_iY+10;//右下
	
		objNode.iInitLeftTopY=_iY-30;
		objNode.iInitLeftBotY=_iY+10;
	
		objNode.iLeftCount=0;
		objNode.iRightCount=0;
		
		objNode.innerText=strInitTxt;
		document.body.appendChild(objNode);
		return objNode;
	}
	function initNode(_iX,_iY,_strId,_strName,_iCount,_iLeft,_iRight,_iITY,_iIBY,_iILTY,_iILBY){
		var objNode=generNode(_iX,_iY,_strId);
		objNode.innerText=_strName;
		objNode.iCount=_iCount;
		objNode.iLeftCount=_iLeft;
		objNode.iRightCount=_iRight;
		
		objNode.iInitTopY=_iITY;
		objNode.iInitBotY=_iIBY;
		objNode.iInitLeftTopY=_iILTY;
		objNode.iInitLeftBotY=_iILBY;
		
		objNode.style.color="green";
	}
	function generLine(_objStart,_objEnd){
		var objLine=document.createElement("v:curve");
		objLine.style.position="absolute";
		objLine.style.left="0";
		objLine.style.top="0";
		objLine.setAttribute('stroked','true');
		//objLine.setAttribute('strokecolor','red');
		objLine.setAttribute('filled','false');
		 
		
		objLine.setAttribute('id',_objEnd.id+"_line");
		setLinePos(objLine,_objStart,_objEnd);
		setLineOvl(objLine,_objStart,_objEnd);
		document.body.appendChild(objLine);
		return objLine;
	}
	function setLinePos(_objLine,_objStart,_objEnd){
		var iFromX,iToX;
		if(_objEnd.iX>_objStart.iX){
			iFromX=_objStart.iX+iNodeWidth;
			iToX=_objEnd.iX;
		}else{
			iFromX=_objStart.iX;
			iToX=_objEnd.iX+iNodeWidth;
			}
		_objLine.setAttribute('from',iFromX+"px,"+(_objStart.iY+iNodeHeight/2)+"px");
		_objLine.setAttribute('to',iToX+"px,"+(_objEnd.iY+iNodeHeight/2)+"px");
	}
	function setLineOvl(_objLine,_objStart,_objEnd){
		var strContr1="";
		var strContr2="";
		if(_objEnd.iY>_objStart.iY){
			if(_objEnd.iX>_objStart.iX){//右下
				strContr1=(_objStart.iX+iOvlX+iNodeWidth)+","+(_objStart.iY+iOvlY);
				strContr2=(_objEnd.iX-iOvlTwoX)+","+(_objEnd.iY+iOvlTwoY);
			}else{
			
				strContr1=(_objStart.iX-iOvlX)+","+(_objStart.iY+iOvlY);
				strContr2=(_objEnd.iX+iOvlTwoX+iNodeWidth)+","+(_objEnd.iY+iOvlTwoY);
			}
		}else{
			if(_objEnd.iX>_objStart.iX){//右上
			
				strContr1=(_objStart.iX+iOvlX+iNodeWidth)+","+(_objStart.iY+iOvlY);
				strContr2=(_objEnd.iX-iOvlTwoX)+","+(_objEnd.iY+iOvlTwoY);
			}else{
				strContr1=(_objStart.iX-iOvlX)+","+(_objStart.iY+iOvlY);
				strContr2=(_objEnd.iX+iOvlTwoX+iNodeWidth)+","+(_objEnd.iY+iOvlTwoY);
			}
		}
		_objLine.setAttribute('control1',strContr1);
		_objLine.setAttribute('control2',strContr2);
	}
	function clickNode(_objNode){
		if(objCurNode!=null)
			objCurNode.style.borderColor="#909090";		
		objCurNode=_objNode;
		objCurNode.style.borderColor="red";
	}
	function init(){	
		if(!initTasks())
			clickNode(generNode(1500,1500,strRoot));
		objRoot=$(strRoot);
		document.body.scrollTop=1200;
		document.body.scrollLeft=850;
		document.body.onmouseup=mouseUp;
		document.body.onmousemove=mousMove;

		document.body.onmousedown=mousDown;
		document.body.onmouseover=nodeOver;
		document.body.onmouseout=nodeOut;
		document.body.ondblclick=nodeDBClick;
	}
	var bIsSave=true;
	function targetTask(){
		miniWin('目标下发','','targettask.jsp?type='+strTaskId+"&tasktype="+strTaskTable,1440,850,'','');
	}
	function save(){		
		if(objRoot.style.color!='green'){
				alert("未设置属性，不能存储！");
				return ;
		}
		bIsSave=true;
		var sNodes=checkNodes(objRoot);
		//alert(sNodes);
		if(bIsSave){
			sNodes=strRoot+"X="+objRoot.iX+
			"&"+strRoot+"Y="+objRoot.iY+
			"&"+strRoot+"MAX="+objRoot.iCount+
			"&"+strRoot+"L="+objRoot.iLeftCount+
			"&"+strRoot+"R="+objRoot.iRightCount+
			
			"&"+strRoot+"ITY="+objRoot.iInitTopY+
			"&"+strRoot+"IBY="+objRoot.iInitBotY+
			"&"+strRoot+"ILTY="+objRoot.iInitLeftTopY+
			"&"+strRoot+"ILBY="+objRoot.iInitLeftBotY+
			sNodes+"&tasktype="+strTaskTable+"&type="+strTaskId;			
			var sResult=getTx(sNodes,"save.jsp");
			alert(sResult);
		}
	}
	function checkNodes(_objNode){
		var iChildCount=_objNode.iCount;
		var strParams="";
		for(var i=0;i<iChildCount;i++){
		    var objTempNode=getChildNode(_objNode.id,i);
			//alert(objTempNode);
			if(objTempNode.style.color!='green'){
				alert("未设置属性，不能存储！");
				bIsSave=false;
				return "";
			}
			var strTempId=objTempNode.id;
			strParams+="&"+strTempId+"X="+objTempNode.iX;
			strParams+="&"+strTempId+"Y="+objTempNode.iY;
			strParams+="&"+strTempId+"MAX="+objTempNode.iCount;
			strParams+="&"+strTempId+"L="+objTempNode.iLeftCount;
			strParams+="&"+strTempId+"R="+objTempNode.iRightCount;
			
			strParams+="&"+strTempId+"ITY="+objTempNode.iInitTopY;
			strParams+="&"+strTempId+"IBY="+objTempNode.iInitBotY;
			strParams+="&"+strTempId+"ILTY="+objTempNode.iInitLeftTopY;
			strParams+="&"+strTempId+"ILBY="+objTempNode.iInitLeftBotY;
			//alert(objTempNode.innerText);
			strParams+=checkNodes(objTempNode);
		}
		return strParams;
	}
	function nodeDBClick(){
		var objNode=isNodeEvent();		
		if(objNode!=null)
			miniWinAll('目标属性','','attr.jsp?SNODEID='+objNode.id+'&nodename='+objNode.innerText+"&tasktype="+strTaskTable+"&type="+strTaskId,800,350,'','',100,100);
	}
	function isNodeEvent(){
		var objNode=event.srcElement;
		if(objNode.id!="")
			return objNode;
		else
			return null;
	}
	function nodeOut(){
		var objNode=event.srcElement;
		if(bDragScroll){//属于拖动状态
			if(objNode.id!=""){//属于节点
				if(objMoveNode.id!=objNode.id){//不属于自身
					objNode.style.borderColor="black";
				}
			}
		}
	}
	function nodeOver(){
		var objNode=event.srcElement;
		if(bDragScroll){//属于拖动状态
			if(objNode.id!=""){//属于节点
				if(objMoveNode.id!=objNode.id){//不属于自身
					objNode.style.borderColor="blue";
				}
			}
		}
	}
	var bDragScroll=false;
	var objMoveNode;
	function mouseUp(){
		bDragScroll=false;
		nodetip.style.display="none";
	}
	function mousDown(){
		var objNode=event.srcElement;
		if(objNode.id!=""){
			bDragScroll=true;
			objMoveNode=objNode;
			nodetip.innerText=objMoveNode.innerText;
		}
	}
	function mousMove(){
		if(bDragScroll){
			nodetip.style.display="";
			nodetip.style.left=event.x+document.body.scrollLeft;
			nodetip.style.top=event.y+document.body.scrollTop;				
		}
	}