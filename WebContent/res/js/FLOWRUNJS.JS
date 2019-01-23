function updataPageAndDelSele(_str,_gs_upl_kc){
    var divTabId = "div_"+_str;
    var iframeTabId = "sys_"+_str;
    var openPage=parent.getOpenPage(_gs_upl_kc);
    var divTab= openPage.document.getElementById(divTabId);
    var iframeTab = openPage.document.getElementById(iframeTabId);
    divTab.parentNode.removeChild(divTab);
    iframeTab.src=iframeTab.src;
}
function DelSele(_str){
 
    var divTabId = "div_"+_str;
 
    var divTab=  parent.document.getElementById(divTabId);
  
    divTab.parentNode.removeChild(divTab);
   
}

