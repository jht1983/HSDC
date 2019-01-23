var objOpenMenu=null;
function $(_strId){
	return document.getElementById(_strId);
}
function doMainMenu(_obj,_strParCode){
	if(objOpenMenu!=null)
		objOpenMenu.style.display="none";
	objOpenMenu=$("menu"+_strParCode);
	objOpenMenu.style.display="";
}