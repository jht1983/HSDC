function sys_updateCell(_obj,_iRow,_strGJH,_strPartId){
		var _upValue=_obj.value;
		if(_upValue=='')
			return;
		if(isNaN(_upValue)){
			alert("必须是数字！");
			_obj.value="";
			return;
		}
		var strDate='<<v3>>';
		alert(strDate);
		var vResult=yltPhone.doAjax('comid=006&gjh='+_strGJH+'&sdate='+strDate+'&partid='+_strPartId+'&pid='+'<<v2>>'+'&sl='+_upValue+'&optb=T_MRDZ&opcount=I_DZSL','docommand');
		if(vResult=='true'){
			/**
			var objRow=$('tb').rows[_iRow];
			objRow.cells[6].innerText='<<SYS_STRCURUSERNAME>>';
			objRow.cells[5].innerText=strDate;
			**/
		}else{
			alert('操作失败！');
		}      
}