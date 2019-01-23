var ylt=ylt||{};
	ylt.excelToDb = ylt.excelToDb || {};
	yltExcelToDb=ylt.excelToDb={
		iStartRow:0,
		objStarRow:null,
		objBindCol:new Object(),
		objDicCol:new Object(),
		objParamRecive:new Object(),
		objprimKey:new Object(),
		objFilter:new Object(),
		objExtends:new Object(),
		objCurSelHead:null,
		strImId:1,
		bIsDicConfig:false,
		setHiddenParamRecive:function(_strId,_obj){
			var i_type=_obj.value;
			if(i_type==-1){
				delete this.objParamRecive[_strId];
			}else
				this.objParamRecive[_strId]=i_type;
		},
		setPrimKeyValue:function(_obj,_strCode){
			if(_obj.checked)
				this.objprimKey[_strCode]="";
			else
				delete this.objprimKey[_strCode];
		},
		setFilterValue:function(_obj,_strCode){
			if(_obj.checked)
				this.objFilter[_strCode]="";
			else
				delete this.objFilter[_strCode];
		},
		setExtendsValue:function(_obj,_strCode){
			if(_obj.checked)
				this.objExtends[_strCode]="";
			else
				delete this.objExtends[_strCode];
		},
		setExtends:function(_obj){
		if(sys_prim_key.style.display=="none"){
			var strFilter="<table class='table1'>";
			for(var i in this.objBindCol){
				var objChild=this.objBindCol[i];
				for(var j in objChild){
					if(this.objExtends[j]!=null)
						strFilter+="<tr class='tr1'><td><input onclick=\"yltExcelToDb.setExtendsValue(this,'"+j+"')\" type='checkbox' value='"+j+"' checked></td><td class='td1'>"+j+"</td><td class='td1'>"+objChild[j].split(":")[0]+"</td></tr>";
					else
						strFilter+="<tr class='tr1'><td><input onclick=\"yltExcelToDb.setExtendsValue(this,'"+j+"')\" type='checkbox'></td><td class='td1'>"+j+"</td><td class='td1'>"+objChild[j].split(":")[0]+"</td></tr>";
				}
			}
			strFilter+="</table>";
			sys_prim_key.innerHTML=strFilter;
			sys_prim_key.style.display="";
			sys_prim_key.style.top="25px";
			sys_prim_key.style.left=bttnExtend.offsetLeft+"px";
			bttnExtend.className="button gray";
		}else{
			sys_prim_key.style.display="none";
			bttnExtend.className="button green";
		}
		},
		setFilter:function(_obj){
		if(sys_prim_key.style.display=="none"){
			var strFilter="<table class='table1'>";
			for(var i in this.objBindCol){
				var objChild=this.objBindCol[i];
				for(var j in objChild){
					if(this.objFilter[j]!=null)
						strFilter+="<tr class='tr1'><td><input onclick=\"yltExcelToDb.setFilterValue(this,'"+j+"')\" type='checkbox' value='"+j+"' checked></td><td class='td1'>"+j+"</td><td class='td1'>"+objChild[j].split(":")[0]+"</td></tr>";
					else
						strFilter+="<tr class='tr1'><td><input onclick=\"yltExcelToDb.setFilterValue(this,'"+j+"')\" type='checkbox'></td><td class='td1'>"+j+"</td><td class='td1'>"+objChild[j].split(":")[0]+"</td></tr>";
				}
			}
			strFilter+="</table>";
			sys_prim_key.innerHTML=strFilter;
			sys_prim_key.style.display="";
			sys_prim_key.style.top="25px";
			sys_prim_key.style.left=bttnfilter.offsetLeft+"px";
			bttnfilter.className="button gray";
		}else{
			sys_prim_key.style.display="none";
			bttnfilter.className="button green";
		}
		},
		
		setPrimKey:function(_obj){
		if(sys_prim_key.style.display=="none"){
			var strPrimKey="<table class='table1'>";
			for(var i in this.objBindCol){
				var objChild=this.objBindCol[i];
				for(var j in objChild){
					if(this.objprimKey[j]!=null)
						strPrimKey+="<tr class='tr1'><td><input onclick=\"yltExcelToDb.setPrimKeyValue(this,'"+j+"')\" type='checkbox' value='"+j+"' checked></td><td class='td1'>"+j+"</td><td class='td1'>"+objChild[j].split(":")[0]+"</td></tr>";
					else
						strPrimKey+="<tr class='tr1'><td><input onclick=\"yltExcelToDb.setPrimKeyValue(this,'"+j+"')\" type='checkbox'></td><td class='td1'>"+j+"</td><td class='td1'>"+objChild[j].split(":")[0]+"</td></tr>";
				}
			}
			for(var i in this.objParamRecive){
					if(this.objprimKey[i]!=null)
						strPrimKey+="<tr class='tr1'><td><input onclick=\"yltExcelToDb.setPrimKeyValue(this,'"+i+"')\" type='checkbox' value='"+i+"' checked></td><td class='td1'>"+i+"</td><td class='td1' style='color:red;'>数据接收</td></tr>";
					else
						strPrimKey+="<tr class='tr1'><td><input onclick=\"yltExcelToDb.setPrimKeyValue(this,'"+i+"')\" type='checkbox'></td><td class='td1'>"+i+"</td><td class='td1' style='color:red;'>数据接收</td></tr>";
			}
			
			
			strPrimKey+="</table>";
			sys_prim_key.innerHTML=strPrimKey;
			sys_prim_key.style.display="";
			sys_prim_key.style.top="25px";
			sys_prim_key.style.left=bttnprimkey.offsetLeft+"px";
			bttnprimkey.className="button gray";
		}else{
			sys_prim_key.style.display="none";
			bttnprimkey.className="button red";
		}
		},
		setDicConfig:function(_obj){
			if(!this.bIsDicConfig){
				this.bIsDicConfig=true;
				bttndic.className="button gray";
				bttndic_text.innerHTML="取消设置";
			}else{
				this.bIsDicConfig=false
				bttndic.className="button pink";
				bttndic_text.innerHTML="设置字典";
			}
		},
		addBindCol:function(_strDBField,_strExcelCol,_strDBFieldName,_strItemType){
			var objChild=this.objBindCol[_strExcelCol];
			if(objChild==null)
				objChild=new Object();
			objChild[_strDBField]=_strDBFieldName+":"+_strItemType;
			this.objBindCol[_strExcelCol]=objChild;
		},
		setHiddenParam:function(){
			
			var objTableFieldMenu=document.getElementById("sys_seltablefields_param");
			if(objTableFieldMenu==null){
				alert("请先选择导入表！");
				return;
			}
			if(objTableFieldMenu.style.display==""){
				objTableFieldMenu.style.display="none";
			}else{
				objTableFieldMenu.style.display="";
				objTableFieldMenu.style.top="25px";
				objTableFieldMenu.style.left=bttnreceve.offsetLeft+"px";
			}
		},
		sys_setExceleTopDbField:function(_event,_obj){
		if(!this.bIsDicConfig){
				var objTableFieldMenu=document.getElementById("sys_seltablefields_menu");
				if(objTableFieldMenu==null){
					alert("请先选择导入表！");
					return;
				}
				objTableFieldMenu.style.left=_event.x+"px";
				objTableFieldMenu.style.top=(_event.y+tab_sys_tools.clientHeight)+"px";
				objTableFieldMenu.style.display="";
				this.objCurSelHead=_obj;
		}else{
			if(confirm("确定要将该列设置为字典翻译吗？")){
				_obj.style.background="#d6edc5";
				this.objDicCol[_obj.attributes["icol"].value]=_obj.attributes["icol"].value;
				//var strBindCol=this.objCurSelHead.attributes["icol"].value;
			}
		}
		},
		selTableField:function(_strId,_strName,_strItemType){
			var strBindCol=this.objCurSelHead.attributes["icol"].value;
			var objCurObjHeadBind=this.objBindCol[strBindCol];
			if(objCurObjHeadBind!=null)
				if(objCurObjHeadBind[_strId]!=null){
					alert("该列已绑定"+_strName+"字段，不能重复绑定！");
					sys_seltablefields_menu.style.display="none";
					return;
				}
				
			this.objCurSelHead.innerHTML+="<font style='font-family:微软雅黑;font-size:14px;color:green;'>("+_strName+")</font>";
			document.getElementById(_strId).style.background="#39ee30";
			
			this.addBindCol(_strId,strBindCol,_strName,_strItemType);
			sys_seltablefields_menu.style.display="none";
		},
		sys_setExceleToDbStartRow:function(_obj){
			var iStartRow=parseInt(_obj.innerText);
			if(confirm("确定要将第 "+iStartRow+" 行设置为起始行吗？")){
				if(this.objStarRow!=null)
					this.objStarRow.style.backgroundColor="#fafafa";
				_obj.style.backgroundColor="red";
				this.objStarRow=_obj;
				this.iStartRow=iStartRow;
			}
		},
		delConfigMsg:function(){
			if(confirm("确定清除所有配置信息吗？")){
				var vResult=getTx("comid=002&S_IMID="+yltExcelToDb.strImId+"&S_BINDCOL=&I_STARTROW=0&S_DICCOL=&S_PARAM=","docommand");
				if(vResult=="true"){
					alert("清除成功！");
					window.location.reload();
				}
			}
		},
		save:function(){
			var strBindField="";
			var strDicCol="";
			for(var i in this.objBindCol){
				if(this.objDicCol[i]!=null)
						strDicCol+=","+i;
				var objChild=this.objBindCol[i];
				for(var j in objChild){
					
					strBindField+="~"+i+":"+j+":"+objChild[j];
				}
			}
			if(strBindField!=""){
				strBindField=strBindField.substring(1);
				if(strDicCol!="")
					strDicCol=strDicCol.substring(1);
				
				var strReciveHiddenMethod="";
				for(var i in this.objParamRecive){
					strReciveHiddenMethod+="~"+i+":"+this.objParamRecive[i];
				}
				if(strReciveHiddenMethod!="")
					strReciveHiddenMethod=strReciveHiddenMethod.substring(1);
				
				
				
				var strPrimKeys="";
				for(var i in this.objprimKey){
					strPrimKeys+=","+i;
				}
				if(strPrimKeys!="")
					strPrimKeys=strPrimKeys.substring(1);
					
				var strFilters="";
				for(var i in this.objFilter){
					strFilters+=","+i;
				}
				if(strFilters!="")
					strFilters=strFilters.substring(1);
				
				var strExtends="";
				for(var i in this.objExtends){
					strExtends+=","+i;
				}
				if(strExtends!="")
					strExtends=strExtends.substring(1);
					
					
					
				var vResult=getTx("comid=002&S_IMID="+yltExcelToDb.strImId+"&S_BINDCOL="+strBindField+"&I_STARTROW="+this.iStartRow+"&S_DICCOL="+strDicCol+
								  "&S_PARAM="+strReciveHiddenMethod+"&S_PRIMKEY="+strPrimKeys+"&S_FILTER="+strFilters+"&S_EXTENDS="+strExtends,"docommand");
				if(vResult=="true")
					alert("保存成功！");
				else
					alert("保存失败！");
			}else{
				alert("没有任何配置信息，不需要保存！");
			}
		}
	}
	
	//yltExcelToDb.addBindCol();