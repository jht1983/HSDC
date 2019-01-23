var objSelTree={bIsDown:false,strMoveText:"",strMoveCode:"",msUp:function(){this.bIsDown=false;divtip.style.display="none";bDragScroll=false;},
					msDown:function(_strId){						
							this.bIsDown=true;							
							this.strMoveText=event.srcElement.innerText;
							this.strMoveCode=_strId;					
					},
					msMove:function(){
						if(this.bIsDown){									
							divtip.style.display="";
							parent.lxmain.divtip.style.display="none";
							divtip.innerHTML=this.strMoveText;
							divtip.style.top=event.y+document.body.scrollTop;
							divtip.style.left=event.x;
						}else{
							divtip.style.display="none";
							if(bDragScroll)
									downScroll(event.y);
						}
					},
					msUp1:function (){
						var objElement=event.srcElement;
						var objMsg=parent.lxleft.objSelTree;
						parent.lxleft.bDragScroll=false;
						if(objMsg.bIsDown){
								getValue(objMsg.strMoveCode,objMsg.strMoveText);
						}
						objMsg.bIsDown=false;
						divtip.style.display="none";
						
					},
					msMove1:function(){
							var objMsg=parent.lxleft.objSelTree;
							if(objMsg.bIsDown){
								divtip.style.display="";
								parent.lxleft.divtip.style.display="none";
								divtip.innerHTML=objMsg.strMoveText;
								divtip.style.top=event.y+10;
								divtip.style.left=event.x+30;
							}else{
								divtip.style.display="none";								
							}
					}
	}
	function init(_strId){
		document.body.onmouseup=function(){objSelTree["msUp"+_strId]();};
		document.body.onmousemove=function(){objSelTree["msMove"+_strId]();};

		document.body.onmousedown=function(){bDragScroll=true;iCurY=event.y;};	
	}
	if(typeof(nameSpace)=="undefined")
		init("");
	else
		init("1");



var iCurScroll=0;
	var iCurY=0;
	var bDragScroll=false;
	function downScroll(_iy){
		iCurScroll=document.body.scrollTop
		var aType=0;
		if(_iy>iCurY)
			aType=1;
		aType==1?iCurScroll+=20:iCurScroll-=20;
		if(iCurScroll>document.body.scrollHeight)
			iCurScroll=document.body.scrollHeight;
		if(iCurScroll<0)
			iCurScroll=0;
		iCurY=event.y;
		document.body.scrollTop=iCurScroll;
	}