var ylt= ylt ||{};
ylt.Phone_Img = ylt.Phone_Img || {};
var yltPhone_Img=ylt.Phone_Img={
	sys_cur_img_pos_x:0,
	sys_cur_img_pos_y:0,
	sys_cur_img_Width:0,
	sys_cur_img_Height:0,
	iScreenWidth:0,
	iScreenHeight:0,
	bIsPinchIng:false,
	iCurPos_x:0,
	iCurPos_y:0,
	iPinX:0,
	iPinY:0,
	viewPic:function(_obj){
		var iCurScrollTop=document.body.scrollTop;
		var objDiv=document.createElement("div");		
		objDiv.style.position="absolute";
		objDiv.style.top=iCurScrollTop+"px"
		objDiv.style.left="0px";
		objDiv.id="sys_popwin";
		objDiv.style.background="black";		
		objDiv.style.width="100%";
		objDiv.style.height="100%";
		objDiv.style.zIndex = "1001";
		objDiv.style.display="table";
		objDiv.style.color="white";
		objDiv.style.verticalAlign = "middle";
		objDiv.innerHTML="<img onclick='yltPhone.closeWin();' style='position:absolute;left:0px;top:50px;' id='sys_cur_view_img' src='"+_obj.src+"' width='100%'>";
		document.body.style.overflow="hidden";
		document.body.appendChild(objDiv);
		var objImg=yltPhone.$("sys_cur_view_img");
		this.iScreenWidth=document.body.offsetWidth;
		this.iScreenHeight=document.body.offsetHeight;
		
		var iTop=(this.iScreenHeight-objImg.offsetHeight)/2;
		objImg.style.top=iTop+"px";
		this.sys_cur_img_pos_x=0;
		this.sys_cur_img_pos_y=iTop;
		var hammertime = new Hammer(objImg);
        hammertime.on("pan", function (e) {
			 if(yltPhone_Img.bIsPinchIng)
				 return;
			 var objPinchImg=yltPhone.$("sys_cur_view_img");
			 yltPhone_Img.iCurPos_x=yltPhone_Img.sys_cur_img_pos_x+e.deltaX;
			 yltPhone_Img.iCurPos_y=yltPhone_Img.sys_cur_img_pos_y+e.deltaY;
			  if(e.isFinal){
				  if(yltPhone_Img.iCurPos_x>0){
						yltPhone_Img.iCurPos_x=0;
				  }else{
					  if((yltPhone_Img.iCurPos_x+objPinchImg.offsetWidth)<yltPhone_Img.iScreenWidth){
						  yltPhone_Img.iCurPos_x=yltPhone_Img.iScreenWidth-objPinchImg.offsetWidth;
						  
					  }
				  }
				 
				  if(objPinchImg.offsetHeight<yltPhone_Img.iScreenHeight)
					  yltPhone_Img.iCurPos_y=(yltPhone_Img.iScreenHeight-objImg.offsetHeight)/2;
				  else if(yltPhone_Img.iCurPos_y>0){
							yltPhone_Img.iCurPos_y=0;
					   }else{
								if((yltPhone_Img.iCurPos_y+objPinchImg.offsetHeight)<yltPhone_Img.iScreenHeight){
									yltPhone_Img.iCurPos_y=yltPhone_Img.iScreenHeight-objPinchImg.offsetHeight;
					  }
				  }
				  
				yltPhone_Img.sys_cur_img_pos_x=yltPhone_Img.iCurPos_x;
				yltPhone_Img.sys_cur_img_pos_y=yltPhone_Img.iCurPos_y;
			  }
			 objPinchImg.style.left=yltPhone_Img.iCurPos_x+"px";
			 objPinchImg.style.top=yltPhone_Img.iCurPos_y+"px";
			 
         });
		 hammertime.get('pinch').set({ enable: true });		 
		 hammertime.on("pinchstart", function (e) {
			 yltPhone_Img.bIsPinchIng=true;
			 yltPhone_Img.iPinX= yltPhone_Img.iCurPos_x;
			 yltPhone_Img.iPinY= yltPhone_Img.iCurPos_y;
			 yltPhone_Img.sys_cur_img_Width=yltPhone.$("sys_cur_view_img").offsetWidth;
			 yltPhone_Img.sys_cur_img_Height=yltPhone.$("sys_cur_view_img").offsetHeight;
			 yltPhone.$("sys_cur_view_img").style.width=(yltPhone_Img.sys_cur_img_Width*e.scale)+"px";
         });
		 hammertime.on("pinchmove", function (e) {
			 var objPinchImg=yltPhone.$("sys_cur_view_img");
			 
			 var iCurImgWidth=yltPhone_Img.sys_cur_img_Width*e.scale;
			 objPinchImg.style.width=iCurImgWidth+"px";
			 var iCurImgHeight=objPinchImg.offsetHeight;

			 yltPhone_Img.sys_cur_img_pos_x=yltPhone_Img.iPinX+(yltPhone_Img.sys_cur_img_Width-objImg.offsetWidth)/2;
			 if(yltPhone_Img.sys_cur_img_pos_x>0)
				 yltPhone_Img.sys_cur_img_pos_x=0;
			 var iRightPos=yltPhone_Img.sys_cur_img_pos_x+iCurImgWidth;
			 
			 if(iRightPos<yltPhone_Img.iScreenWidth)
					yltPhone_Img.sys_cur_img_pos_x=yltPhone_Img.iScreenWidth-objPinchImg.offsetWidth;
			 			
				 if(iCurImgWidth<yltPhone_Img.iScreenWidth)
					 yltPhone_Img.sys_cur_img_pos_x=(yltPhone_Img.iScreenWidth-objImg.offsetWidth)/2;

			 yltPhone_Img.sys_cur_img_pos_y=yltPhone_Img.iPinY+(yltPhone_Img.sys_cur_img_Height-iCurImgHeight)/2;
			 if(yltPhone_Img.sys_cur_img_pos_y>0)
				 yltPhone_Img.sys_cur_img_pos_y=0;
			 var iBottomPos=yltPhone_Img.sys_cur_img_pos_y+iCurImgHeight;
			 if(iBottomPos<yltPhone_Img.iScreenHeight)
					yltPhone_Img.sys_cur_img_pos_y=yltPhone_Img.iScreenHeight-iCurImgHeight;
			 			
				 if(iCurImgHeight<yltPhone_Img.iScreenHeight)
					 yltPhone_Img.sys_cur_img_pos_y=(yltPhone_Img.iScreenHeight-iCurImgHeight)/2;
			 objPinchImg.style.left= yltPhone_Img.sys_cur_img_pos_x+"px";
			 objPinchImg.style.top= yltPhone_Img.sys_cur_img_pos_y+"px";
         });
		 hammertime.on("pinchend", function (e) {
			 var objPinchImg=yltPhone.$("sys_cur_view_img");
			 if(objImg.offsetWidth<yltPhone_Img.iScreenWidth){
				objPinchImg.style.width="100%";
				yltPhone_Img.sys_cur_img_pos_y=(yltPhone_Img.iScreenHeight-objImg.offsetHeight)/2;
				yltPhone_Img.sys_cur_img_pos_x=0;
				objPinchImg.style.left= yltPhone_Img.sys_cur_img_pos_x+"px";
				objPinchImg.style.top= yltPhone_Img.sys_cur_img_pos_y+"px";
			 }
			 yltPhone_Img.bIsPinchIng=false;
         });
		 
	}

}