var ylt= ylt ||{};
ylt.TreeGraph = ylt.TreeGraph || {};
var yltTreeGraph =ylt.TreeGraph ={
	strLineColor:'black',
	 root:null,
	 paper:null,
	 iZoom:0,
	zoomGraph:function(_bIsMax){
		if(_bIsMax)
			this.iZoom++;
		else
			this.iZoom--;
		if(this.iZoom>10)this.iZoom=10;if(this.iZoom<-9)this.iZoom=-9;
		document.body.style.zoom=1+this.iZoom/10;
	},
	zoomRestore:function(){
		this.iZoom=0;
		document.body.style.zoom=1;
	},
	createPolyLine: function(_strPath){
            var polyline=this.paper.path(_strPath);
			//this.strLineColor="red";
            polyline.attr("stroke",this.strLineColor);
            polyline.attr("fill","blue");
			
        },
	 getAbsPoint:function(obj,_iType) {   
		var   x,y;      
		var oRect   =   obj.getBoundingClientRect(); 
		x=(oRect.right-oRect.left-10)/2+oRect.left;
		if(_iType==3){
			y=oRect.top;
			return x+","+(y-20)+" "+x+","+y;
		}

		if(_iType==0)//parent  
			y=oRect.bottom-4;
		else if(_iType==2)
			y=oRect.top-20;
		else
			y=oRect.top;
		return x+","+y;   
		},
   getPolyPath:function(_objParent,_objChild) {
		var   x,y,cY;      
		var  oRect   =   _objParent.getBoundingClientRect(); 
		x=(oRect.right-oRect.left-10)/2+oRect.left;
		y=oRect.bottom-2;
		oRect   =   _objChild.getBoundingClientRect(); 
		cY=oRect.top-20;
		return "M"+x+","+y+" "+x+","+cY;   
	},
	getPath1:function(_objParent,_objChild){
		return "M"+this.getAbsPoint(_objParent,0)+" "+this.getAbsPoint(_objChild,1);
	},
	getPath:function(_objParent,_objChild){
		return "M"+this.getAbsPoint(_objChild,3);
	},
	reDrawLine:function(){
		this.root=document.getElementById("treepanel");
		this.root.innerHTML="";
		this.paper=Raphael(this.root,drawpanel.offsetWidth+drawpanel.offsetLeft,drawpanel.offsetHeight+drawpanel.offsetTop);
		doDraw();
	},
	init:function(_strParamId){
		this.root=document.getElementById("treepanel");
		this.root.innerHTML="";
		this.paper=Raphael(this.root,drawpanel.offsetWidth+drawpanel.offsetLeft,drawpanel.offsetHeight+drawpanel.offsetTop);
		doDraw();
		this.initNodeStyle(_strParamId);
		this.initBody(_strParamId);
	},
	initBody:function(_strParamId){
		var objFun=yltPub["initTree_"+_strParamId];
		if(objFun!=null)
			objFun();
	},
	drawPloyLine:function(_objParent,_objStart,_objEnd,_iChild){
		if(_iChild>1){
			this.createPolyLine("M"+this.getAbsPoint(_objStart,2)+" "+this.getAbsPoint(_objEnd,2));			
		}
		this.createPolyLine(this.getPolyPath(_objParent,_objStart));
	},
	clickNode:function(_objNode){
		var objFun=yltPub["clickNode_"+_objNode.sys_tree_paramid];
		if(objFun!=null)
			objFun(_objNode);
	},
	initNodeStyle:function(_strParamId){
		var objFun=yltPub["initNode_"+_strParamId];
		if(objFun!=null){
			for(var i in objNodeMsg){
				var objDiv=document.getElementById("node"+i);
				if(objDiv!=null)
					objFun(objDiv,objNodeMsg[i]);
			}
		}
	}
}