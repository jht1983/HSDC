function doNext(_iCurStep){
	_iCurStep++;
	window.location="comp?sys_type=zzbgd&im="+_iCurStep+"&pid="+str_sys_pid;
}
function doPre(_iCurStep){
	_iCurStep--;
	window.location="comp?sys_type=zzbgd&im="+_iCurStep+"&pid="+str_sys_pid;
}