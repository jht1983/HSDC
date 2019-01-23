  var LODOP; //声明为全局变量 
  var strPrintPage="";
	function prn1_preview() {
		doGenerZxdMx_Mt_Print();
		if(strPrintPage=="")
			return;
		CreatePageData();
		LODOP.PREVIEW();	
	};
	function prn1_printA() {		
		CreateOneFormPage();
		LODOP.PRINTA(); 	
	};	
	function CreatePageData(){
		LODOP=getLodop(document.getElementById('LODOP'),document.getElementById('LODOP_EM'));  
		LODOP.PRINT_INIT("客户回单");
		LODOP.SET_PRINT_STYLE("FontSize",18);
		LODOP.SET_PRINT_STYLE("Bold",1);
		LODOP.ADD_PRINT_HTM(88,200,350,600,getData());
	};	                     
	function printSetUp() {
		doGenerZxdMx_Mt_Print();
		if(strPrintPage=="")
			return;
		CreatePageData();
		LODOP.PRINT_SETUP();	
	};	
	function getData(){
		var xml = new ActiveXObject("Microsoft.XMLHTTP");
		var param="";
		xml.open("POST",strPrintPage,false);//以同步方式通信  
		xml.setrequestheader("content-length",param.length);  
		xml.setrequestheader("content-type","application/x-www-form-urlencoded");  
		xml.setRequestHeader("charset", "UTF-8");
		xml.send(param);  
		var res = xml.responseText;		
		return res;
	}
	function doGenerZxdMx_Mt_Print(){
	var objCheckChilds=document.getElementsByName("syscheckbox");
	var objTable=$('tb');
	var strZxdh="";
		var iCheckCount=objCheckChilds.length;
		for(var i=0;i<iCheckCount;i++){
			if(objCheckChilds[i].checked)
				strZxdh+=","+objTable.rows[parseInt(objCheckChilds[i].value)+1].cells[1].innerText;
		}
		if(strZxdh==""){
			alert("请至少选择一条装箱单！");
			strPrintPage="";
		}else
			strPrintPage='generzxqdmx_mt.jsp?xh='+strZxdh.substring(1);
}