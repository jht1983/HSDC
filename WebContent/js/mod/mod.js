/****************************************************************************
 * User interface strings                                               *
 ****************************************************************************/

/* Strings for snapshot dialog */
var str_snapshot_header = "Snapshot to clipboard";
var str_snapshot_text1 = "To create a snapshot from the<br> model view, use the function<br> key F12 or click the Capture<br>button below.";
var str_snapshot_text2 = "Note that the snapshot is<br>placed on a clipboard. You can<br>view, edit or print the snapshot<br>by pasting it for example in<br>some image processing<br>software.<br><br>You can change the<br>background color here:";
var str_black = "black";
var str_blue = "blue";
var str_white = "white";

/* Strings for links on the left side of the page */
var str_sendlink_link = "Send Web Viewer link";
var str_sendurl_link = "Send URL link";
var str_snapshot_link = "Snapshot to clipboard";
var str_help_link = "Help";

/* Status strings shown in the left on the bottom*/
var str_modelloaded = "Model loaded";
var str_location = "Location";
var str_isnowset = "is now set.";
var str_panon = "Pan mode is now on.";
var str_rotateon = "Rotate mode is now on.";
var str_flyon = "Fly mode is now on.";
var str_cameracentered = "Camera centered.";
var str_homeviewset = "Home view set.";
var str_blackbackgroundset = "Black background set.";
var str_bluebackgroundset = "Blue background set.";
var str_whitebackgroundset = "White background set.";
var str_modelcopiedtoclipboard = "Model copied to the clipboard.";
var str_loaded = "Loaded: ";

/* Menu labels */
var str_pan = "Pan";
var str_rotate = "Rotate";
var str_fly = "Fly";
var str_setclipplane = "Set clip plane";
var str_center = "Center";
var str_home = "Home";
var str_snapshot = "Snapshot to clipboard";
var str_copylocation = "Copy location";
var str_pastelocation = "Paste location";
var str_changetoperspective = "Change to perspective";         
var str_changetoorthogonal = "Change to orthogonal";

var str_backgroundcolor = "Background color";
var str_presentation = "Presentation";
var str_wireframe = "wireframe";
var str_shadedwireframe = "shaded wireframe";
var str_rendered = "rendered";
var str_enablefullcontentrendering = "Enable full content rendering";
var str_disablefullcontentrendering = "Disable full content rendering";

/* Tooltips */
var str_selectpanmode = "Select pan mode";
var str_selectrotatemode = "Select rotate mode";
var str_selectflymode = "Select fly mode";
var str_centerthecamera = "Center the camera";
var str_gotohomeview = "Go to home view";

/* Named locations labels */
var str_locations_first = "--- Named views --------";

/* Error messages */
var str_permissionserror = "Error: WebViewer active-x control could not be loaded. You might not have the needed permissions to install the component.";
var str_browserversionerror = "Tekla Web Viewer requires Internet Explorer 6.0 (Windows version) or greater.\nYou may experience some problems with your current browser.";

var agt=navigator.userAgent.toLowerCase();
var is_major = parseInt(navigator.appVersion);
var is_ie     = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));
var is_ie3    = (is_ie && (is_major < 4));
var is_ie4    = (is_ie && (is_major == 4) && (agt.indexOf("msie 4")!=-1) );
var is_ie4up  = (is_ie && (is_major >= 4));
var is_ie5    = (is_ie && (is_major == 4) && (agt.indexOf("msie 5.0")!=-1) );
var is_ie5_5  = (is_ie && (is_major == 4) && (agt.indexOf("msie 5.5") !=-1));
var is_ie5up  = (is_ie && !is_ie3 && !is_ie4);
var is_ie5_5up =(is_ie && !is_ie3 && !is_ie4 && !is_ie5);
var is_ie6    = (is_ie && (is_major == 4) && (agt.indexOf("msie 6.")!=-1) );
var is_ie6up  = (is_ie && !is_ie3 && !is_ie4 && !is_ie5 && !is_ie5_5);
var is_win   = ( (agt.indexOf("win")!=-1) || (agt.indexOf("16bit")!=-1) );

if (!is_ie6up || !is_win) 
{ 
	alert(str_browserversionerror);
}

/****************************************************************************
 * General purpose DHTML functions                                          *
 ****************************************************************************/

// function to retrive x coordinate of mouse
function getMouseLeft(evt)
{
	if(document.all)
		return window.event.clientX + document.body.scrollLeft;
	else if(document.getElementById || document.layers)
		return evt.pageX; 
}

// function to retrive x coordinate of mouse
function getMouseTop(evt)
{
	if(document.all)
		return window.event.clientY + document.body.scrollTop;
	else if(document.getElementById || document.layers)
		return evt.pageY; 
}

// Function to get object reference to the given object
function getObj(obj)
{
	if (document.layers) 
	{
		if (typeof obj == "string") 
		{
			return document.layers[obj]
		} 
		else 
		{
			return obj
		}
	}
	if (document.all) 
	{
		if (typeof obj == "string") 
		{
			return document.all(obj)
		} 
		else 
		{
			return obj
		}
	}
	if (document.getElementById) 
	{
		if (typeof obj == "string") 
		{
			return document.getElementById(obj)
		} 
		else 
		{
			return obj
		}
	}
	return null	
}

// position an object at a specific pixel coordinate
function shiftTo(obj, x, y) 
{
	obj = getObj(obj)
	
	if (obj.moveTo) 
	{
		obj.moveTo(x,y)
	} 
	else if (typeof obj.style.left != "undefined") 
	{
		obj.style.left = x
		obj.style.top = y
	}
}


// Function to show object
function showObj(obj)
{
	obj = getObj(obj);
	if(document.all || document.getElementById)
		obj.style.visibility = "visible";
	else if (document.layers)
		obj.visibility = "show";	
}

// Function to hide object
function hideObj(obj)
{
	obj = getObj(obj);
	if(document.all || document.getElementById)
		obj.style.visibility = "hidden";
	else if (document.layers)
		obj.visibility = "hide";	
}

// Function to write text in div
function printHTML(obj, textToWrite)
{
	obj = getObj(obj);
	if(document.all || document.getElementById)
	{
		obj.innerHTML = textToWrite;
	}
	else if (document.layers)
	{
		obj.document.open();
		obj.document.write(textToWrite);
		obj.document.close();		
	}
}


/****************************************************************************
 * HTML user interface related functions                                    * 
 ****************************************************************************/
function setStatus(message)
{
	document.all("status_div").innerHTML = message;	
	window.status = "";
}


function updateLocationBox()
{
	/* First index is description text */
	for(i = 0; i < WebViewer.GetNamedLocationCount(); i++)
	{
		locations.options[i + 1] = new Option(WebViewer.GetNamedLocationByIndex(i))
	}
}


function changeLocation()
{
	if (locations.selectedIndex > 0)
	{
		WebViewer.SetNamedLocation(locations.options[locations.selectedIndex].text);
		setStatus(str_location + " " + locations.options[locations.selectedIndex].text + " " + str_isnowset);
	}
}


function showToolTip(message)
{
	showObj("tooltip_div");
	printHTML("tooltip_div", "&nbsp;" + message + "&nbsp;");
	shiftTo("tooltip_div", getMouseLeft() + 20, getMouseTop() + 5);
	window.status = "";
}


function showToolTipAt(message, x, y)
{
	showObj("tooltip_div");
	printHTML("tooltip_div", "&nbsp;" + message + "&nbsp;");
	shiftTo("tooltip_div", x, y);
	window.status = "";
}


function hideToolTip()
{
	hideObj("tooltip_div");
	window.status = "";
}


/****************************************************************************
 * Background color functions                                               *
 ****************************************************************************/
function setBackground()
{
	colorsObj = backcolors	

	if (colorsObj.selectedIndex == 0)
	{
		setDefaultColors();
		setStatus(str_blackbackgroundset);
	}
	else if (colorsObj.selectedIndex == 1)
	{
		setBlueColors();
		setStatus(str_bluebackgroundset);
	}
	else if (colorsObj.selectedIndex == 2)
	{
		setLightColors();
		setStatus(str_whitebackgroundset);
	}
}


function setDefaultColors()
{
      /* parameters are for WebViewer.setClassColor class, state, r, g, b, a
         normally state = 0, and a = 1.0
         default line colors should be solid-color / 2 instead of white */

	WebViewer.setBackgroundColor(0.0, 0.0, 0.0)
	WebViewer.setClassColor(SOLIDCOLOR, 1,0, 0.7, 0.7, 0.7,1.0)
	WebViewer.setClassColor(LINECOLOR, 1,0, 0.49, 0.49, 0.49,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 2,0, 0.9, 0.0, 0.0,1.0)
	WebViewer.setClassColor(LINECOLOR, 2,0, 0.81, 0.0, 0.0,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 3,0, 0.3, 0.9, 0.3,1.0)
	WebViewer.setClassColor(LINECOLOR, 3,0, 0.09, 0.81, 0.09,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 4,0, 0.0, 0.3, 0.9,1.0)
	WebViewer.setClassColor(LINECOLOR, 4,0, 0.0, 0.09, 0.81,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 5,0, 0.3, 0.9, 0.9,1.0)
	WebViewer.setClassColor(LINECOLOR, 5,0, 0.09, 0.81, 0.81,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 6,0, 0.9, 0.9, 0.2,1.0)
	WebViewer.setClassColor(LINECOLOR, 6,0, 0.81, 0.81, 0.04,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 7,0, 0.9, 0.0, 0.9,1.0)
	WebViewer.setClassColor(LINECOLOR, 7,0, 0.81, 0.0, 0.81,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 8,0, 0.4, 0.4, 0.4,1.0)
	WebViewer.setClassColor(LINECOLOR, 8,0, 0.16, 0.16, 0.16,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 9,0, 0.7, 0.2, 0.3,1.0)
	WebViewer.setClassColor(LINECOLOR, 9,0, 0.49, 0.04, 0.09,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 10,0, 0.4, 0.7, 0.2,1.0)
	WebViewer.setClassColor(LINECOLOR, 10,0, 0.16, 0.49, 0.04,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 11,0, 0.2, 0.6, 0.7,1.0)
	WebViewer.setClassColor(LINECOLOR, 11,0, 0.04, 0.36, 0.49,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 12,0, 0.8, 0.4, 0.9,1.0)
	WebViewer.setClassColor(LINECOLOR, 12,0, 0.64, 0.16, 0.81,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 13,0, 0.9, 0.4, 0.0,1.0)
	WebViewer.setClassColor(LINECOLOR, 13,0, 0.81, 0.16, 0.0,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 14,0, 0.6, 0.3, 0.0,1.0)
	WebViewer.setClassColor(LINECOLOR, 14,0, 0.36, 0.09, 0.0,1.0)
}


function setLightColors()
{
      /* parameters are for WebViewer.setClassColor class, state, r, g, b, a
         normally state = 0, and a = 1.0 */

	WebViewer.setBackgroundColor(1.0, 1.0, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 1, 0, 0.7, 0.7, 0.7, 1.0)
	WebViewer.setClassColor(LINECOLOR,  1, 0, 0.85, 0.85, 0.85, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 2, 0, 0.9, 0.0, 0.0, 1.0)
	WebViewer.setClassColor(LINECOLOR,  2, 0, 0.95, 0.5, 0.5, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 3, 0, 0.3, 0.9, 0.3, 1.0)
	WebViewer.setClassColor(LINECOLOR,  3, 0, 0.65, 0.95, 0.65, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 4, 0, 0.0, 0.3, 0.9, 1.0)
	WebViewer.setClassColor(LINECOLOR,  4, 0, 0.5, 0.65, 0.95, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 5, 0, 0.3, 0.9, 0.9, 1.0)
	WebViewer.setClassColor(LINECOLOR,  5, 0, 0.65, 0.95, 0.95, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 6, 0, 0.9, 0.9, 0.2, 1.0)
	WebViewer.setClassColor(LINECOLOR,  6, 0, 0.95, 0.95, 0.6, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 7, 0, 0.9, 0.0, 0.9, 1.0)
	WebViewer.setClassColor(LINECOLOR,  7, 0, 0.95, 0.5, 0.95, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 8, 0, 0.4, 0.4, 0.4, 1.0)
	WebViewer.setClassColor(LINECOLOR,  8, 0, 0.7, 0.7, 0.7, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 9, 0, 0.7, 0.2, 0.3, 1.0)
	WebViewer.setClassColor(LINECOLOR,  9, 0, 0.85, 0.6, 0.65, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 10, 0, 0.4, 0.7, 0.2, 1.0)
	WebViewer.setClassColor(LINECOLOR,  10, 0, 0.7, 0.85, 0.6, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 11, 0, 0.2, 0.6, 0.7, 1.0)
	WebViewer.setClassColor(LINECOLOR,  11, 0, 0.6, 0.8, 0.85, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 12, 0, 0.8, 0.4, 0.9, 1.0)
	WebViewer.setClassColor(LINECOLOR,  12, 0, 0.9, 0.7, 0.95, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 13, 0, 0.9, 0.4, 0.0, 1.0)
	WebViewer.setClassColor(LINECOLOR,  13, 0, 0.95, 0.7, 0.5, 1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 14, 0, 0.6, 0.3, 0.0, 1.0)
	WebViewer.setClassColor(LINECOLOR,  14, 0, 0.8, 0.65, 0.5, 1.0)
}


function setBlueColors() 
{
      /* parameters are for WebViewer.setClassColor class, state, r, g, b, a
         normally state = 0, and a = 1.0
         default line colors should be solid-color / 2 instead of white */

	WebViewer.setBackgroundColor(0.2, 0.2, 0.5)
	WebViewer.setClassColor(SOLIDCOLOR, 1,0, 0.7, 0.7, 0.7,1.0)
	WebViewer.setClassColor(LINECOLOR, 1,0, 0.45, 0.45, 0.75,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 2,0, 0.9, 0.0, 0.0,1.0)
	WebViewer.setClassColor(LINECOLOR, 2,0, 0.55, 0.1, 0.4,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 3,0, 0.3, 0.9, 0.3,1.0)
	WebViewer.setClassColor(LINECOLOR, 3,0, 0.25, 0.55, 0.55,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 4,0, 0.0, 0.3, 0.9,1.0)
	WebViewer.setClassColor(LINECOLOR, 4,0, 0.1, 0.25, 0.85,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 5,0, 0.3, 0.9, 0.9,1.0)
	WebViewer.setClassColor(LINECOLOR, 5,0, 0.25, 0.55, 0.85,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 6,0, 0.9, 0.9, 0.2,1.0)
	WebViewer.setClassColor(LINECOLOR, 6,0, 0.55, 0.55, 0.5,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 7,0, 0.9, 0.0, 0.9,1.0)
	WebViewer.setClassColor(LINECOLOR, 7,0, 0.55, 0.1, 0.85,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 8,0, 0.4, 0.4, 0.4,1.0)
	WebViewer.setClassColor(LINECOLOR, 8,0, 0.3, 0.3, 0.6,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 9,0, 0.7, 0.2, 0.3,1.0)
	WebViewer.setClassColor(LINECOLOR, 9,0, 0.45, 0.2, 0.55,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 10,0, 0.4, 0.7, 0.2,1.0)
	WebViewer.setClassColor(LINECOLOR, 10,0, 0.3, 0.45, 0.5,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 11,0, 0.2, 0.6, 0.7,1.0)
	WebViewer.setClassColor(LINECOLOR, 11,0, 0.2, 0.4, 0.75,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 12,0, 0.8, 0.4, 0.9,1.0)
	WebViewer.setClassColor(LINECOLOR, 12,0, 0.5, 0.3, 0.85,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 13,0, 0.9, 0.4, 0.0,1.0)
	WebViewer.setClassColor(LINECOLOR, 13,0, 0.55, 0.3, 0.4,1.0)
	WebViewer.setClassColor(SOLIDCOLOR, 14,0, 0.6, 0.3, 0.0,1.0)
	WebViewer.setClassColor(LINECOLOR, 14,0, 0.4, 0.25, 0.4,1.0)
}