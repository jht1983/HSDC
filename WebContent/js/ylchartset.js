var ylt= ylt ||{};
ylt.ChartSet = ylt.ChartSet || {};
var yltChartSet=ylt.ChartSet={
	strPageCode:"",strName:"",strWidth:"",strHeight:"",strDataId:"",strDataName:"",strX:"",strY:"",
	objLegend:null,
	strSelOption:"<option value=''>数据轴</option>",
	strLegend:"",
	strgraphOption:"<option>---无---</option><option value='bar'>柱状图</option><option value='line'>折线图</option>",
	$:function(_strId){
		return document.getElementById(_strId);
	},
	_init:function(_strPageCode,_strName,_strWidth,_strHeight,_strDataId,_strDataName,_strX,_strY,_objLegend){
		this.strPageCode=_strPageCode;
		this.$("inname").value=this.strName=_strName;
		this.$("inwidth").value=this.strWidth=_strWidth;
		this.$("inheight").value=this.strHeight=_strHeight;
		this.strDataId=_strDataId;
		this.$("indataname").value=this.strDataName=_strDataName;
		this.objLegend=_objLegend;
		this.$("inx").value=this.strX=_strX;//_strX;//
		this.$("iny").value=this.strY=_strY;
		this._initLegend();
	},
	_initLegend:function(){
		for(var i in this.objLegend){
			var arrLegConfig=this.objLegend[i].split("$");
			this.$("sel_"+i).value=arrLegConfig[1];
		}
	},
	getAjaxActive:function(){
		var xmlHttp;
		if (window.ActiveXObject) { 
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}else if (window.XMLHttpRequest) { 
				xmlHttp = new XMLHttpRequest();
		}
		return xmlHttp;
	},
	doAjax:function(param,aStrUrl){
		var xml=this.getAjaxActive();
		xml.open("POST",aStrUrl,false);
		//xml.setRequestHeader("content-length",param.length);  
		xml.setRequestHeader("content-type","application/x-www-form-urlencoded");  
		xml.setRequestHeader("charset", "UTF-8");
		xml.send(param);  
		var res = xml.responseText;
		return res;
	},
	setAttr:function(_strF,_strValue){
		var bIsTrue=false;
		var strResult=this.doAjax('comid=sys_001&f='+_strF+'&fv='+_strValue+'&id='+this.strPageCode,'docommand');
		if(strResult=="true"){
			bIsTrue=true;
			this.doAjax('','dohome?hometype=init');
		}
		return bIsTrue;
	},
	setConfig:function(_obj,_iType){
		switch(_iType){
			case 1:
				if(this.setAttr("SPAGENAME",_obj.value)){
					this.strName=_obj.value;
				}
				break;
			case 2:				
				if(this.setAttr("SQUERYFIELD",_obj.value)){
					if(_obj.value.indexOf("%")==-1)
						this.$("chart_1").style.width=_obj.value+"px";
					else
						this.$("chart_1").style.width=_obj.value;
					myChart.resize();
				}
				break;
			case 3:
				if(this.setAttr("SGLFIELD",_obj.value)){
					if(_obj.value.indexOf("%")==-1)
						this.$("chart_1").style.height=_obj.value+"px";
					else
						this.$("chart_1").style.height=_obj.value;
					myChart.resize();
				}
				break;
			case 5:
				if(!this.setAxias()){
					_obj.value=this.strX;
					return;
				}
				if(this.setAttr("SFIELDNAME","x:"+_obj.value)){
					myChart.resize();
				}
				break;
			case 6:
				if(!this.setAxias()){
					_obj.value=this.strY;
					return;
				}
				if(this.setAttr("SFIELDNAME","y:"+_obj.value)){
					myChart.resize();
				}
				break;
			case 8:
			alert(_obj.getAttribute("strlab"));
				this.setLegAttr(_obj.id.substring(4),1,_obj.value);
				if(this.setAttr("SFIELDSIZE",this.getLegAttr())){
					//myChart.resize();
				}
				break;
				
		}
	},
	getLegAttr:function(){
		var strLegends="";
		var strSplit="";
		for(var i in this.objLegend){
			strLegends+=strSplit+this.objLegend[i];
			strSplit="~";
		}
		return strLegends;
	},
	setLegAttr:function(_strLegId,_iIndex,_strValue){
		if(_strValue==""){
			delete this.objLegend[_strLegId];
			return;
		}
		var strLeg=this.objLegend[_strLegId];
		if(strLeg==null)
			strLeg="$$$$$";
		var arrStrLeg=strLeg.split("$");
		arrStrLeg[_iIndex]=_strValue;
		this.objLegend[_strLegId]=arrStrLeg.join("$");
	},
	setAxias:function(){
		if(this.$("inx").value!="" && this.$("iny").value!=""){
			alert("x轴与y轴必须有一列为数据轴！");
			return false;
		}
		return true;
	},
	initLegend:function(){
		this.$("divlegend").innerHTML="<table>"+this.strLegend+"</table>";
	}
}