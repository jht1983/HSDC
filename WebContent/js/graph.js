
var myGraph = {
		objGraphType:{
			"line":"折线图",
			"pie":"饼状图",
			"bar":"柱状图"
			
		},
		pieType:["","radius","area"],//图模式
		legendObjArr:["graphType","legendWidth","djmc","color","legendGraphWidth"],//图例表单
		formObjArr:["t_sys_pagemsg$SPAGENAME","t_sys_pagemsg$SQUERYFIELD","t_sys_pagemsg$SGLFIELD","spagemsgSelect","axisField"],
		dataControlClass:"graph",//数据调用类
		checkHighColor:"#ccc",
		graphNums:0,//包含的图形个数
		isExistParPage:false,//布局页面是否存在
		allParGraph:"",//布局的图形对应的单元格序列
		parSpagecode:getUrl("spagecode"),//页面图形编号(布局的图形编号)
		currentSpagecode:"",//当前操作的图形编号
		currentGraphDatasetCode:"",//当前操作的图形的数据集编号
		currentFieldCodes:"",//当前操作的图形数据集的字段代码
		currentFieldNames:"",//当前操作的图形数据集的字段名称
		currentGraphLegendObj:"",//当前操作的图例对象
		allgraphLegends:"",//所有图例属性
		currentXias:"",//当前图形的坐标字段
		currentTdId:"",//当前操作的单元格的ID
		isNewGraph:false,//双击判断是新建还是修改
		
		newGraph:function(){
			this.currentSpagecode = "";
			this.currentGraphDatasetCode = "";
			this.currentFieldCodes = "";
			this.currentFieldNames = "";
			this.currentGraphLegendObj = "";
			this.allgraphLegends = "";
			this.currentXias = "";
			
			$("t_sys_pagemsg$SPAGENAME").value = "";
			$("t_sys_pagemsg$SQUERYFIELD").value = "";
			$("t_sys_pagemsg$SGLFIELD").value = "";
			$("legend").innerHTML = "";
			$("graphType").innerHTML = "";
			$("djmc").value = "";
			$("legendWidth").value = "5";
			$("legendGraphWidth").value = "22";
			
			var spagemsgSelect = $("spagemsgSelect");
			spagemsgSelect.value='';
			
			var axisField = $("axisField");//坐标字段
			axisField.options.length=0;
			
			
		},
		
		initPage:function(){//初始化页面数据
			var pageData = getTx("method=getPageData&spagecode="+this.parSpagecode, this.dataControlClass);
			if(pageData!=""){
				pageData = pageData.split("{}");
				this.graphNums = parseInt(pageData[0]);
				$("parPageName").value = pageData[5];
				this.isExistParPage = true;
				this.allParGraph =  pageData[1];
			}
			
			var spagemsgSelect = $("spagemsgSelect");
			spagemsgSelect.options.length=0;
			var pageInfo = getTx("method=initPage", this.dataControlClass).split("&");
			var pageType1 = pageInfo[0].split("|");//数据集
			
			//数据集
			for(var i = 0;i<pageType1.length-1;i++){
				var page = pageType1[i].split(":");
				spagemsgSelect.options.add(new Option(page[1],page[0] ));
			}

			
			
		},
		updateGraph:function(_obj){
			if(myGraph.currentSpagecode != ""){//确定是修改
				var oldPageInfo = getTx("method=getPageData&spagecode="+myGraph.currentSpagecode, myGraph.dataControlClass);
				var oldPageInfoTemp = oldPageInfo.split("{}");
				myGraph.currentGraphDatasetCode = oldPageInfoTemp[0];//数据集编号
				
				
				myGraph.allgraphLegends = oldPageInfoTemp[2];//图例
				
				myGraph.initGraphXais();//生成坐标字段下拉框
				myGraph.createGraphLegend();//生成图例初始数据
				
				
				$("spagemsgSelect").value=myGraph.currentGraphDatasetCode;
				$("t_sys_pagemsg$SQUERYFIELD").value=oldPageInfoTemp[3];//宽度
				$("t_sys_pagemsg$SGLFIELD").value=oldPageInfoTemp[4];//高度
				$("t_sys_pagemsg$SPAGENAME").value=oldPageInfoTemp[5];//图形名称
				
			}
		}
		,
		selectData:function(selectSpagemsg){//选择数据集
			//数据集变更后，图例数据应该重新配置
			if(selectSpagemsg != this.currentGraphDatasetCode){
				this.updateData("t_sys_pagemsg$SFIELDSIZE", "");
				this.updateData("t_sys_pagemsg$SFIELDCODE",selectSpagemsg);
				this.currentGraphLegendObj = "";//当前操作的图例对象
				this.allgraphLegends="";//所有图例属性
			}
			
			this.currentGraphDatasetCode = selectSpagemsg;
			this.initGraphXais();//生成坐标字段下拉框
			this.createGraphLegend();//生成图例初始数据

		},
		
		createGraphLegend:function(){//生成图例初始数据
			var checkboxHtml ="";
			checkboxHtml += "<ul>";
			for(var i = 0 ;i < this.currentFieldCodes.length;i++){
				var strChecked = "";
				if(this.allgraphLegends!=""){
					var graphLendedss = this.allgraphLegends.split("~");
					for(j=0;j<graphLendedss.length;j++){
						if(graphLendedss[j].split("$")[2] == this.currentFieldCodes[i] ){
							strChecked = "checked='checked'";
							break;
						}
					}
				}
				
				checkboxHtml += "<li><input name='graphLegend' id='"+this.currentFieldNames[i]+"' type='checkbox' "+strChecked+"  onclick='myGraph.clickGraphLegend(this,false)' style='width:50px' value='"+this.currentFieldCodes[i]+"'/><span onclick='myGraph.clickGraphLegend(this.previousSibling,false)'>"+this.currentFieldNames[i]+"</span></li>";
			}
			checkboxHtml += "</ul>";
			$("legend").innerHTML=checkboxHtml;
			
			this.initGraphType();//生成图形类型下拉框

		}
		,
		
		initGraphXais:function(){//初始化坐标字段
			var fieldInfo = getTx("method=createGraphCode&spagecode=" + this.currentGraphDatasetCode, this.dataControlClass);//--》得到图例字段
			var fieldInfos = fieldInfo.split("|");
			this.currentFieldCodes = fieldInfos[0].split(",");
			this.currentFieldNames = fieldInfos[1].split(",");
			var axisField = $("axisField");//坐标字段
			axisField.options.length=0;
			
			for(var i = 0 ;i < this.currentFieldCodes.length;i++){
				axisField.options.add(new Option(this.currentFieldNames[i], this.currentFieldCodes[i]));
			}
			
			if(this.currentXias!=""){
				var currentXiass = this.currentXias.split(":");
				axisField.value=currentXiass[1];//坐标字段
				var axiss = document.getElementsByName("axis");//X or Y坐标
				for(var j = 0 ;j < axiss.length;j++){
					if(axiss[j].value == currentXiass[0]){
						axiss[j].checked=true;
					}
				}
			}
		},
		
		initGraphType:function(){//初始化图例其他信息--图形类型
			
			//图形类型
			var graphTypeTemp = $("graphType");
			graphTypeTemp.options.length=0;
			for(var i in this.objGraphType){
				graphTypeTemp.options.add(new Option(this.objGraphType[i],i ));
			}
			
			var pieTypeTemp = $("pieType");
			pieTypeTemp.options.length=0;
			for(var i=0;i<this.pieType.length;i++){
				pieTypeTemp.options.add(new Option(this.pieType[i],this.pieType[i] ));
			}
		},
		updateData:function (fieldcode,fieldvalue){//更新或新新增数据到数据库
			if(this.currentTdId==""){//未选择单元格
				alert("请先选择要生成图形的单元格！");
				return;
			}
			
			var OPTYPE = "NO_OPTYPE=1&";
			
			
			
			if(this.currentSpagecode==""){//新增
				this.currentSpagecode =this.parSpagecode+"_"+(parseInt(this.graphNums)+1);
				OPTYPE = "";
				
				if(this.allParGraph.trim() == ""){
					this.allParGraph += this.currentTdId+":"+this.currentSpagecode;
				}else{
					this.allParGraph += "~" + this.currentTdId+":"+this.currentSpagecode;
				}
				
				var parOPTYPE = "";
				if(!this.isExistParPage){//布局页面未存在
					parOPTYPE = "";
				}else{//布局页面已存在-更新图形数量
					parOPTYPE = "NO_OPTYPE=1&";
				}
				var str = parOPTYPE+"NO_charset=UTF-8&NO_CON=t_sys_pagemsg$SPAGECODE&t_sys_pagemsg$SFIELDNAME="+this.allParGraph+"&t_sys_pagemsg$SFIELDCODE="+(this.graphNums+1)+"&t_sys_pagemsg$SPAGECODE="+this.parSpagecode;
				getTx(str,"YLWebAction");
				
				this.graphNums = this.graphNums+1;//图形数量+1
			}
			
			
			var str = "t_sys_pagemsg$SPAGECODE="+this.currentSpagecode;
			var param = OPTYPE+"t_sys_pagemsg$SPAGETYPE=12&NO_charset=UTF-8&NO_CON=t_sys_pagemsg$SPAGECODE&"+str+"&"+fieldcode+"="+fieldvalue;
			
			param = this.replaceAll(param, "%", "％");
			getTx(param,"YLWebAction");
			
			
			this.reload();
		},
		savePage:function (){//更新或新新增布局数据
			var pageName = $("parPageName").value;
			if(pageName==""){
				alert("请填写页面名称！");
				return;
			}
			if(!this.isExistParPage){//布局页面未存在
				getTx("NO_charset=UTF-8&NO_CON=t_sys_pagemsg$SPAGECODE&t_sys_pagemsg$SFIELDNAME= &t_sys_pagemsg$SPAGETYPE=88&t_sys_pagemsg$SPAGENAME="+pageName+"&t_sys_pagemsg$SFIELDCODE=0&t_sys_pagemsg$SPAGECODE="+this.parSpagecode,"YLWebAction");
			}else{//页面已存在
				getTx("NO_OPTYPE=1&NO_charset=UTF-8&NO_CON=t_sys_pagemsg$SPAGECODE&t_sys_pagemsg$SPAGENAME="+pageName+"&t_sys_pagemsg$SPAGECODE="+this.parSpagecode,"YLWebAction");
			}
			this.isExistParPage = true;
			alert("保存成功！");
		},
		updateDataAuto:function(field){//更新数据到数据库,field：表单对象
			if(field.value==""){
				alert("不能为空！");
				return;
			}
			this.updateData(field.id, field.value);
		},
		
		deleteData:function(pagecode){//清除数据
			if(this.graphNums != 0 && pagecode != ""){
				if(confirm("您确定要清除数据吗？")){
					var str = "method=delData&from=t_sys_pagemsg&where=SPAGECODE LIKE '"+pagecode+"~'";
					var result = getTx(str,this.dataControlClass);
					if(result == "y"){
						if(pagecode.indexOf("-")){
							var fieldName = this.currentTdId+":"+pagecode+"~";
							this.allParGraph = this.allParGraph.replace(fieldName, "");
							getTx("NO_charset=UTF-8&NO_OPTYPE=1&NO_CON=t_sys_pagemsg$SPAGECODE&t_sys_pagemsg$SFIELDNAME="+this.allParGraph+"&t_sys_pagemsg$SFIELDCODE="+(this.graphNums-1)+"&t_sys_pagemsg$SPAGECODE="+this.parSpagecode,"YLWebAction");
						}
						alert("清除成功！");
					}else{
						alert("清除失败！");
					}
					
					
					window.location.reload();
				}
			}else{
				alert("不需要清除!");
			}
		},
	
		clickGraphLegend:function(obj,isNewData){//点击图例
			var lis = document.getElementsByTagName("li");
			for(var i = 0;i<lis.length;i++){
				lis[i].style.backgroundColor="";
			}
			if(obj.checked){
				this.currentGraphLegendObj = obj;//设置为当前图例
				obj.parentNode.style.backgroundColor=this.checkHighColor;
				
				if(this.allgraphLegends!=""){
					var findFlag = false;//已有数据中，是否存在该图例
					var allgraphLegendsTemp = this.allgraphLegends.split("~");
					for(var i = 0 ;i < allgraphLegendsTemp.length;i++){
						
						var _cols = allgraphLegendsTemp[i].split("$");
						var _cols0 = _cols[0];
						if(_cols0 == obj.id){
							findFlag = true;
							//找到了后肯定要把原值显示出来
							var graphType = _cols[1];//图形类型
							var styles = _cols[3].split(",");//样式
							
							$("graphType").value = graphType;
							
							this.generalGraphType();
								
							if(graphType == "pie"){//如果是饼状图
								var pieType = styles[0].split(":")[1];
								var pieFieldTemp = _cols[4].split(",");
								$("pieField").value = pieFieldTemp[0];
								if(pieFieldTemp[1] == "y"){
									$("isGlobal").checked=true;
								}else{
									$("isGlobal").checked=false;
								}
								
								
								$("pieType").value = pieType;
								$("piePosition").value = _cols[5];
								
								
							}else{
								var color = styles[0].split(":")[1].replace("#","").replace("'","").replace("'","");//颜色
								var barBorderRadius = styles[1].split(":")[1];//图例宽度
								var djmc = _cols[4];//堆积名称
								$("legendWidth").value = barBorderRadius;
								$("djmc").value = djmc;
								$("color").value = color;
								$("color").style.backgroundColor="#"+color;
								$("legendGraphWidth").value = _cols[5];
							}
							break;
						}
					}
				}
				
			}else{//去掉
				$("pietb").style.display="none";
				$("nopietb").style.display="none";
				
				var allGraphLegend="";
				this.currentGraphLegendObj = "";
				obj.parentNode.style.backgroundColor="";
				if(this.allgraphLegends!=""){
					var findFlag = false;
					var allgraphLegendsTemp = this.allgraphLegends.split("~");
					for(var i = 0 ;i < allgraphLegendsTemp.length;i++){
						var _cols = allgraphLegendsTemp[i].split("$")[0];
						if(_cols == obj.id){
							findFlag = true;
						}else{
							if(allgraphLegendsTemp[i]!=""){
								allGraphLegend += allgraphLegendsTemp[i]+"~";
							}
						}
					}
				}
				
				this.allgraphLegends = allGraphLegend;
				this.updateData("t_sys_pagemsg$SFIELDSIZE",allGraphLegend);
			}
			
			this.readOnlyLegend();
		},
		
		axisFieldChange:function(objAxisField){
			var axisField = objAxisField;
			var axiss = document.getElementsByName("axis");
			for(var i = 0 ;i < axiss.length;i++){
				if(axiss[i].checked){
					axisField = axiss[i].value+":"+axisField;
				}
			}
			this.updateData("t_sys_pagemsg$SFIELDNAME", axisField);
		},
		axisClick:function(obj){
			this.axisFieldChange($("axisField").value);
		},
		saveLegendPro:function(){
			var graphType = $("graphType");
			var _style = "";
			var thisGraphLegend = "";
			var allGraphLegend = "";
			var pieField = $("pieField").value;
			
			pieField = $("isGlobal").checked?pieField+",y":pieField+",n";
			
			
			if(graphType.value == "pie"){//饼状图
				_style = $("pieType").value == ""?"":"roseType:'"+$("pieType").value+"'";
			    thisGraphLegend = this.currentGraphLegendObj.id+"$"+graphType.value+"$"+this.currentGraphLegendObj.value+"$"+_style+"$"+pieField+"$"+$("piePosition").value;
			}else{
				_style = "color:'#"+$("color").value+"'"+",barBorderRadius:"+$("legendWidth").value+"$"+$("djmc").value+"$"+$("legendGraphWidth").value;
				thisGraphLegend = this.currentGraphLegendObj.id+"$"+graphType.value+"$"+this.currentGraphLegendObj.value+"$"+_style;
			}
			
			if(this.allgraphLegends!=""){
				var findFlag = false;//已有数据中，是否存在该图例
				var allgraphLegendsTemp = this.allgraphLegends.split("~");
				for(var i = 0 ;i < allgraphLegendsTemp.length;i++){
					
					var _cols = allgraphLegendsTemp[i].split("$");
					var _cols0 = _cols[0];
					if(_cols0 == this.currentGraphLegendObj.id){//找到该图例
						findFlag = true;
						allGraphLegend += thisGraphLegend+"~";
					}else{
						if(allgraphLegendsTemp[i]!=""){
							if($("isGlobal").checked){
								allGraphLegend += this.replaceAll(allgraphLegendsTemp[i], ",y", ",n")+"~";
							}else{
								allGraphLegend += allgraphLegendsTemp[i]+"~";
							}
							
						}
					}
				}
				
				if(!findFlag){
					allGraphLegend += thisGraphLegend+"~"
				}
				
			}else{
				allGraphLegend = thisGraphLegend;
			}
			
			this.allgraphLegends = allGraphLegend;
			
			this.updateData("t_sys_pagemsg$SFIELDSIZE",allGraphLegend);
		},
		legendProBlur:function(){
			if(this.currentGraphLegendObj!=""){
				this.saveLegendPro();
			}else{
				alert("请先选择图例！");
			}
		},
		generalGraphType:function(){
			if($("graphType").value == "pie"){//如果是饼状图
				$("pietb").style.display="block";
				$("nopietb").style.display="none";
				
				
				var pieField = $("pieField");
				
				pieField.options.length=0;
				
				for(var i=0;i< this.currentFieldCodes.length;i++){
					if(this.currentGraphLegendObj.value == this.currentFieldCodes[i]){
						continue;
					}
					pieField.options.add(new Option(this.currentFieldNames[i],this.currentFieldCodes[i] ));
				}
			}else{
				$("pietb").style.display="none";
				$("nopietb").style.display="block";
			}
		},
		graphTypeChange:function(){
			this.generalGraphType();
			this.legendProBlur();
		},
		iIndex:0,
		reload:function(){//刷新
			getTx("hometype=init","dohome");
			if(this.isNewGraph){
				this.isNewGraph = false;
				contentFrame.sys_insert_Graph(this.currentTdId,this.currentSpagecode,this.parSpagecode);
				contentFrame.location.reload();
			}else{
				contentFrame.sys_refresh(this.currentTdId,this.currentSpagecode+"&iindex="+this.iIndex);
				this.iIndex++;
			}
		},
		doGraph:function(_obj){
		
			this.currentTdId = _obj.id;
			
			var isexist = false;
			alert(this.allParGraph);
			var parGraphs = this.allParGraph.split("~");
			var spagecode;
			
			for(var i = 0 ;i < parGraphs.length;i++){
				var oneGraphs = parGraphs[i].split(":");
				if(oneGraphs[0] == _obj.id){
					isexist = true;
					spagecode = oneGraphs[1];
					break;
				}
			}
			
			if(isexist){//修改
				this.isNewGraph = false;
				this.currentSpagecode = spagecode;
				this.updateGraph();
			}else{//新建
				this.isNewGraph = true;
				this.newGraph();
			}
			
			this.readOnlyLegend();
		}
		,
		readOnlyLegend:function(){
			var isDisabled = this.currentGraphLegendObj!=""?false: "disabled";
			for(var i=0;i<this.legendObjArr.length;i++){
				$(this.legendObjArr[i]).disabled=isDisabled;
			}
			
			isDisabled = this.currentTdId!=""?"relative": "absolute";
			var dis = this.currentTdId!=""?"none": "block";
			$("isdisabled").style.position=isDisabled;
			$("isdisabled").style.display=dis;
		},
		replaceAll:function(obj,str1,str2){
			return obj.replace(eval("/"+str1+"/gi"),str2);      
		}
}
