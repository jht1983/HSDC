/**url(grid.png);**/
var ylt=ylt||{};
ylt.mindDraw = ylt.mindDraw || {};
ylt.mindNode = ylt.mindNode || {};
ylt.winCommand = ylt.winCommand || {};
var iTop=100;
yltWinCommand=ylt.winCommand={
	getScrollLeft:function(){
		return document.body.scrollLeft+document.documentElement.scrollLeft;
	},
	getScrollTop:function(){
		return document.body.scrollTop+document.documentElement.scrollTop;
	}
}
yltMindNode=ylt.mindNode=function(_objParent,_attr,_bIsInput){
        this.id = "node"+yltMindDraw.iIdCount;
		this.objFather = _objParent;
		this.iAreaRightHeight=this.iNodeHeight;
		this.iAreaLeftHeight=this.iNodeHeight;
        if(_objParent==null){
            this.iX = 3000/2-100 ;
            this.iY = 2000/2-50;
        }else{			
			var iObjWidth=_objParent.objRect.right-_objParent.objRect.left;
			if(_objParent.iDirection==-1){
				var iSubRightCount=_objParent.arrRightChildren.length-_objParent.arrLeftChildren.length;
				var iTotalCount=_objParent.arrRightChildren.length+_objParent.arrLeftChildren.length;
				if(iSubRightCount<1){//右
					this.iDirection=0;
					_objParent.arrRightChildren[_objParent.arrRightChildren.length]=this;
					this.iX = _objParent.iX+iObjWidth+80;
					this.compAreaHeight(_objParent,0,false);
					if(_attr.background==null)
						_attr.background=yltMindDraw.arrNodeColor[iTotalCount%9];
				}else{//左
					this.iDirection=1;
					_objParent.arrLeftChildren[_objParent.arrLeftChildren.length]=this;
					this.iX = _objParent.iX-180;
					this.compAreaHeight(_objParent,1,false);
					if(_attr.background==null)
						_attr.background=yltMindDraw.arrNodeColor[iTotalCount%9];
				}
			}else{
				this.iDirection=_objParent.iDirection;		
				_objParent.arrRightChildren[_objParent.arrRightChildren.length]=this;
				if(_objParent.iDirection==0){					
					this.iX = _objParent.iX+iObjWidth+80;
					this.compAreaHeight(_objParent,0,true);
				}else{
					this.iX = _objParent.iX-180;
					this.compAreaHeight(_objParent,1,true);
				}
				
			}		
		}		
		this.arrRightChildren=[];
		this.arrLeftChildren=[];
		//this.label=this.iAreaLeftHeight;
        this.label = _attr.label || "任务" + this.id;
        this.data = _attr.data || null;
		this.background=_attr.background || _objParent.background;
		this.bIsInputIng=false;
		var objDiv=this.createNode();
		this.objRect=objDiv.getBoundingClientRect();		
		if(_objParent!=null){
			this.nodeLine=yltMindDraw.renderer(null,"",this.background);
		}
		this.compNodePosition();		
		yltMindDraw.iIdCount++;
		yltMindDraw.doNodeClick(objDiv,this);
		if(yltMindDraw.bIsAutoDBLClick)
			yltMindDraw.doNodeDBClick(objDiv,this,_bIsInput);
}
yltMindNode.prototype = {	
	arrRightChildren:[],
	arrLeftChildren:[],
	iNodeHeight:30,
	iNodePadding:20,
	iNodePaddingX:80,
	iDirection:-1,
	
	$:function(_strId){
		return document.getElementById(_strId);
	},
	doQX:function(x,y,x1,y1){
		var iScrollLeft=yltWinCommand.getScrollLeft();
		var iScrollTop=yltWinCommand.getScrollTop();
		x+=iScrollLeft;
		y+=iScrollTop;
		x1+=iScrollLeft;
		y1+=iScrollTop;
		var iX=(x1-x)/2+x;
		var iY=(y1-y)/2+y;
	
		var iCx=(iX-x)/2+x;
	//var iCy=(y1-iY)/4+iY;
	var iCy=y+(y1-iY)/6;
	var path="M"+x+","+y+" ";
	path+="Q"+iCx+","+iCy+" ";
	path+=iX+","+iY+" ";
	path+="T"+x1+","+y1;
	
	return path;
	},
	compAreaHeight:function(_obj,_iDrect,_bIsCChild){
		var objParent=_obj.objFather;
		if(_iDrect==0){
			var iChildCount=_obj.arrRightChildren.length;
			if(iChildCount>1){
				_obj.iAreaRightHeight+=this.iNodeHeight+this.iNodePadding;
				//this.$(_obj.id).innerHTML="right:"+_obj.iAreaRightHeight;
				if(objParent!=null)
					this.doParentAreaHeight(objParent,_iDrect);
			}
		}else{
			var iChildCount=0;
			if(_bIsCChild)
				iChildCount=_obj.arrRightChildren.length;
			else
				iChildCount=_obj.arrLeftChildren.length;
			if(iChildCount>1){
				_obj.iAreaLeftHeight+=this.iNodeHeight+this.iNodePadding;
				//this.$(_obj.id).innerHTML="left:"+_obj.iAreaLeftHeight;
				if(objParent!=null)
					this.doParentAreaHeight(objParent,_iDrect);
			}
		}
		
		
	},
	doParentAreaHeight:function(_obj,_iDrect){
		if(_iDrect==0){
			_obj.iAreaRightHeight+=this.iNodeHeight+this.iNodePadding;
			//this.$(_obj.id).innerHTML="right:"+_obj.iAreaRightHeight;
		}else{
			_obj.iAreaLeftHeight+=this.iNodeHeight+this.iNodePadding;
			//this.$(_obj.id).innerHTML="left:"+_obj.iAreaLeftHeight;
		}
		var objParent=_obj.objFather;
		if(objParent!=null)
			this.doParentAreaHeight(objParent,_iDrect);
	},
	compNodePosition:function(){
		//var objNodes=yltMindDraw.objNodes;
		if(mindRoot!=null){
			mindRoot.objRect= this.$(mindRoot.id).getBoundingClientRect();
			this.doCompNodePosition(mindRoot,mindRoot.arrRightChildren,mindRoot.iAreaRightHeight,0);
			this.doCompNodePosition(mindRoot,mindRoot.arrLeftChildren,mindRoot.iAreaLeftHeight,1);
		}
	},
	doCompNodePosition:function(_objFatherNode,_objFatherChild,_iParentAreaHeight,_iDrect){
		var iChildLenght=_objFatherChild.length;
		//var iCurAreaHeight=_objFatherNode.iY-(iChildLenght*this.iNodeHeight+(iChildLenght-1)*this.iNodePadding)/2+this.iNodeHeight/2;
			
		var iCurAreaHeight=_objFatherNode.iY-_iParentAreaHeight/2+this.iNodeHeight/2;
		for(var i=0;i<iChildLenght;i++){
			var curNode=_objFatherChild[i];
			var curNodeAreaHeight=0;
			if(_iDrect==0){
				curNodeAreaHeight=curNode.iAreaRightHeight;
				curNode.iX=_objFatherNode.iX+_objFatherNode.objRect.right-_objFatherNode.objRect.left+this.iNodePaddingX;
			}else{
				curNodeAreaHeight=curNode.iAreaLeftHeight;
				curNode.iX=_objFatherNode.iX-this.iNodePaddingX-curNode.objRect.right+curNode.objRect.left;
			}
			var iMiddleHeight=(curNodeAreaHeight-this.iNodeHeight)/2;
			iCurAreaHeight+=iMiddleHeight;
			curNode.iY=iCurAreaHeight;	
			
			var objDiv=this.$(curNode.id);
			objDiv.style.top=curNode.iY+"px";	
			objDiv.style.left=curNode.iX+"px";			
			curNode.objRect= objDiv.getBoundingClientRect();			
			if(curNode.nodeLine!=null){
				var path="";
				var iParentTop=_objFatherNode.objRect.top+this.iNodeHeight/2;
				var iSelfTop=curNode.objRect.top+this.iNodeHeight/2;
				if(_iDrect==0)
					path=this.doQX(_objFatherNode.objRect.right,iParentTop,curNode.objRect.left,iSelfTop);
				else
					path=this.doQX(_objFatherNode.objRect.left,iParentTop,curNode.objRect.right,iSelfTop);
				yltMindDraw.renderer(curNode.nodeLine,path,"");
			}
			
			iCurAreaHeight+=this.iNodeHeight+iMiddleHeight+this.iNodePadding;
			this.doCompNodePosition(curNode,curNode.arrRightChildren,curNodeAreaHeight,_iDrect);
		}
	},
	createNode:function(){
		var objDiv=document.createElement("div");		
		objDiv.style.position="absolute";
		objDiv.style.left=this.iX+"px";
		objDiv.style.top=this.iY+"px";
		objDiv.style.background=this.background;
		objDiv.style.height=this.iNodeHeight+"px";
		objDiv.setAttribute('id',this.id);
		objDiv.style.zIndex=10;
		
		objDiv.className="nodestyle";
		objDiv.innerHTML=this.label;
		var objNode=this;
		objDiv.onclick=function(){yltMindDraw.doNodeClick(this,objNode);};
		objDiv.ondblclick=function(){yltMindDraw.doNodeDBClick(this,objNode,true);};
		treepanel.appendChild(objDiv);
		return objDiv;
	},
}
var mindRoot=null;
var yltMindDraw=ylt.mindDraw={
	arrNodeColor:['#73a1bf','#73bf76','#bf7394','#7b73bf','#bf7373','#bf9373','#e9df98','#8f8fe3','#0fc2ab'],
	objNodes:{},//节点集合
	R:null,
	iIdCount:0,
	objCurSelNode:null,
	objCurDivNode:null,
	bCtrlIsDown:false,
	bIsDragPanel:false,
	bIsDragNode:false,
	iCurDragPanelX:0,
	iCurDragPanelY:0,
	objBody:null,
	bIsAutoDBLClick:true,
	$:function(_strId){
		return document.getElementById(_strId);
	},
	doNodeClick:function(_objDivNode,_objDataNode){
		if(this.objCurDivNode!=null){
			this.objCurDivNode.style.background=this.objCurSelNode.background;
		}
		_objDivNode.style.background="yellow";
		this.objCurSelNode=_objDataNode;
		this.objCurDivNode=_objDivNode;
		
	},
	doNodeDBClick:function(_objDivNode,_objDataNode,_bIsInput){
		_objDivNode.innerHTML="<input style='width:"+_objDivNode.offsetWidth+"px;' id='"+_objDataNode.id+"input' class='mindinput' type='text' value='"+_objDataNode.label+"'>";
		var objInput=this.$(_objDataNode.id+'input');
		objInput.focus();
		objInput.select();
		
		objInput.onblur=function(){yltMindDraw.doMindInputOnBlur(_objDataNode)};

		_objDataNode.bIsInputIng=_bIsInput;
	},
	doMindInputOnBlur:function(_objDataNode){		
		var strCurSelNodeId=_objDataNode.id;
		var objInput=this.$(strCurSelNodeId+'input')
		if(objInput==null)
			return;		
		_objDataNode.label=objInput.value;
		this.$(strCurSelNodeId).innerHTML="";
		this.$(strCurSelNodeId).innerHTML=_objDataNode.label;
		_objDataNode.bIsInputIng=false;
		
		_objDataNode.objRect=this.$(_objDataNode.id).getBoundingClientRect();
		_objDataNode.compNodePosition();
	},
	renderer:function(_objNodeLine,_strPath,_strColor){
		if(_objNodeLine==null)
			return this.R.path(_strPath).attr("stroke",_strColor);
		else
			return _objNodeLine.attr({"path":_strPath});
		
	},
	addNode: function(parent, attr,_bIsInput){
			if(parent!=null&&this.objCurSelNode!=null&&this.objCurSelNode.bIsInputIng){
				this.doMindInputOnBlur(this.objCurSelNode);
				return;
			}
            var node =null;
			node=new yltMindNode(parent,attr,_bIsInput);
            this.objNodes[node.id] = node;	
            return node;
        },
	delNodes:function(){
		if(this.objCurSelNode==null)
			return;
		var iobjAreaHeight=0;
		//var arrDelDirectChild=null;
		if(this.objCurSelNode.iDirection==-1)
			return;
		else if(this.objCurSelNode.iDirection==0){
			iobjAreaHeight=this.objCurSelNode.iAreaRightHeight;
		}else{
			iobjAreaHeight=this.objCurSelNode.iAreaLeftHeight;
		}
		this.findChildNodes(this.objCurSelNode,this.objCurSelNode.arrRightChildren,function(_objDiv,_objNode){yltMindDraw.delNode(_objDiv,_objNode);});
		this.delNode(this.$(this.objCurSelNode.id),this.objCurSelNode);
		var objParentNode=this.objCurSelNode.objFather;
		var arrParentChilds=null;
		if(objParentNode.iDirection==-1){
			if(this.objCurSelNode.iDirection==0)
				arrParentChilds=objParentNode.arrRightChildren;
			else
				arrParentChilds=objParentNode.arrLeftChildren;
		}else
			arrParentChilds=objParentNode.arrRightChildren;
			
		var iChildCount=arrParentChilds.length;
		for(var i=0;i<iChildCount;i++){
			if(arrParentChilds[i]==this.objCurSelNode){
				arrParentChilds.splice(i,1);
			}
		}
		
		iChildCount=arrParentChilds.length;
		if(iChildCount==0){
			if(this.objCurSelNode.iDirection==0){
				iobjAreaHeight=objParentNode.iAreaRightHeight-objParentNode.iNodeHeight-objParentNode.iNodePadding;
			}else{
				iobjAreaHeight=objParentNode.iAreaLeftHeight-objParentNode.iNodeHeight-objParentNode.iNodePadding;
			}
		}
			
		
		this.findParentNodes(objParentNode,this.objCurSelNode,iobjAreaHeight,function(_objParent,_obj,_iObjAreaHeight){
			if(_obj.iDirection==0){
				_objParent.iAreaRightHeight-=_iObjAreaHeight+_obj.iNodePadding;
				//yltMindDraw.$(_objParent.id).innerHTML=_objParent.iAreaRightHeight;
			}else{
				_objParent.iAreaLeftHeight-=_iObjAreaHeight+_obj.iNodePadding;
			}
		});
		objParentNode.compNodePosition();
		this.objCurSelNode=null;
		this.objCurDivNode=null;
	},
	delNode:function(_objDiv,_objNode){
		//_objDiv.style.background="red";
		treepanel.removeChild(_objDiv);
		_objNode.nodeLine.remove();
		//_objNode.nodeLine.hide();//隐藏
	},
	findParentNodes:function(_objParent,_obj,_iObjAreaHeight,_fun){
		_fun(_objParent,_obj,_iObjAreaHeight);		
		var objParent=_objParent.objFather;
		if(objParent!=null)
			this.findParentNodes(objParent,_objParent,_iObjAreaHeight,_fun);
	},
	findChildNodes:function(_objFatherNode,_arrObjChild,_fun){
		var iChildLenght=_arrObjChild.length;
		for(var i=0;i<iChildLenght;i++){
			var curNode=_arrObjChild[i];
			var arrObjCurNodeChild=curNode.arrRightChildren;
			if(arrObjCurNodeChild.length>0)
				this.findChildNodes(curNode,arrObjCurNodeChild,_fun);
			_fun(this.$(curNode.id),curNode);
		}
	},
	doKeyDown:function(){
		var iKeyCode=window.event.keyCode;
		if(this.objCurSelNode==null)return;
		if (iKeyCode==13){
			if(this.bCtrlIsDown||this.objCurSelNode.id=="node0")
				this.addNode(this.objCurSelNode,{},true);
			else{
			
				this.addNode(this.objCurSelNode.objFather,{},true);
			}
			event.returnValue=false;
		}else if(window.event.keyCode==17)
				this.bCtrlIsDown=true;
		},
	doKeyUp:function(){
		if(window.event.keyCode==17)
			this.bCtrlIsDown=false;
		if(window.event.keyCode==46)
			this.delNodes();
	},
	dragBodyPanel:function(){
		if(this.bIsDragNode){
			/**
			var objCurDragNode=$(this.curStrDragNodeId);
			nodetip.innerHTML=objCurDragNode.innerHTML;
			nodetip.style.background=objCurDragNode.style.background;
			nodetip.style.left=event.x+document.documentElement.scrollLeft-10;
			nodetip.style.top=event.y+document.documentElement.scrollTop+10;	
			nodetip.style.display="";
			**/
		}else if(this.bIsDragPanel){
				window.scrollTo(yltWinCommand.getScrollLeft()-(event.x-yltMindDraw.iCurDragPanelX),
								yltWinCommand.getScrollTop()-(event.y-yltMindDraw.iCurDragPanelY));
				//document.getElementById("node0").innerHTML=document.documentElement.scrollLeft-(event.x-yltMindDraw.iCurDragPanelX);
				yltMindDraw.iCurDragPanelY=event.y;
				yltMindDraw.iCurDragPanelX=event.x;
		}
	},
	_save:function(){
		var strCodes="";
		var strNames="";		
		var strBgColor="";
		var strSDate="";
		var strEDate="";
		var strPCodes="";
		var strSplit="";
		for(var i in this.objNodes){
			var objNode=this.objNodes[i];
			strCodes+=strSplit+i;
			strNames+=strSplit+objNode.label;
			strBgColor+=strSplit+objNode.background;
			strSDate+=strSplit+"";
			strEDate+=strSplit+"";
			var strPCode="";
			if(objNode.objFather!=null)
				strPCode=objNode.objFather.id;
			strPCodes+=strSplit+strPCode;
			strSplit=",";
		}
			//if(this.objNodes[i].objFather!=null)
			//alert(i+"===parent==="+this.objNodes[i].objFather.label);
		getTx("c="+strCodes+
			  "&p="+strPCodes+
			  "&n="+strNames+
			  "&b="+strBgColor+
			  "&sd="+strSDate+
			  "&ed="+strEDate,"Menu?O_SYS_TYPE=svpla");
	},
	_initPre:function(){
		return true;
	},
    _initRoot: function(){
		window.scrollTo((3000-document.body.offsetWidth)/2,(2000-document.body.offsetHeight)/2);
		this.R=Raphael(treepanel);
		document.body.onkeydown=function(){yltMindDraw.doKeyDown();};
		document.body.onkeyup=function(){yltMindDraw.doKeyUp();};
		document.body.onmousedown=function(){yltMindDraw.bIsDragPanel=true;yltMindDraw.iCurDragPanelX=event.x;yltMindDraw.iCurDragPanelY=event.y;};
		document.body.onmouseup=function(){yltMindDraw.bIsDragPanel=false;};
		
		document.body.onmousemove=function(){yltMindDraw.dragBodyPanel();};
	
		if(this._initPre())
			mindRoot= this.addNode(null,{
				label:"白云机场",background:this.arrNodeColor[8]},false);
		
	
		
	
		
		// onselect='document.selection.empty()' onselectstart ='return false'  

		
            return mindRoot;
        }
}
	var arr=[1,2,3,4,5];
	arr.splice(0,2);
	//alert(arr.length);