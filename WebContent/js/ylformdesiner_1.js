var ylt= ylt ||{};
ylt.Form = ylt.Form || {};
var ylForm=ylt.Form={
		objCurSelect:null,
		excelModClick:function (_obj){
			if(this.objCurSelect!=null)
				this.objCurSelect.style.backgroundColor=this.objCurSelect.strOldColor;
			_obj.style.backgroundColor="green";
			this.objCurSelect=_obj;
		},
		excelModOver:function(_obj){
			if(_obj.strOldColor==null)
				_obj.strOldColor=_obj.style.backgroundColor;
			if(_obj.style.backgroundColor!="green")
				_obj.style.backgroundColor="pink";
			_obj.style.cursor="hand";
		},
		excelModOut:function(_obj){
			if(_obj.style.backgroundColor!="green")
				_obj.style.backgroundColor=_obj.strOldColor;
			_obj.style.cursor="";
		},
		excelModDbClick:function(_obj){
			sys_insert_Graph(_obj.id,"2","test");
		}
	}


	