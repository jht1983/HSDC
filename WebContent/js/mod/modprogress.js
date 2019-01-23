		//¼ÓÔØ½ø¶Èprediv
	
	if (((decompressed * 100) / total) >= 98.0){
		prediv.style.width="100%"
		prediv.innerHTML="100%";
		WebViewer.moveCameraToHome(); 
		//setStatus(str_homeviewset);
		return true;
		//printHTML(document.all("progress_text"), "<div class=status>" + str_modelloaded + "</div>");
	}else
	{	
		var strLoadProgress=parseInt((decompressed * 100) / total)+ "%";
		prediv.innerHTML=strLoadProgress;
		prediv.style.width=strLoadProgress  
		//prediv.innerHTML=parseInt((decompressed * 100) / total) + "%";
		//document.all("progress_text").innerHTML = str_loaded + parseInt((decompressed * 100) / total) + "%";
	}