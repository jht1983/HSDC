' Webviewer constants (constants.vb)

const INTERACTOR_MODE_NONE       	= 0
const INTERACTOR_MODE_PRE_SCROLL 	= 1
const INTERACTOR_MODE_SCROLL	 	= 2
const INTERACTOR_MODE_PRE_FLY	 	= 3
const INTERACTOR_MODE_FLY		= 4
const INTERACTOR_MODE_PRE_PAN	 	= 5
const INTERACTOR_MODE_PAN		= 6
const INTERACTOR_MODE_PRE_ROTATE 	= 7
const INTERACTOR_MODE_ROTATE	 	= 8
const INTERACTOR_MODE_PRE_MOVE   	= 9
const INTERACTOR_MODE_MOVE	     	= 10
const INTERACTOR_MODE_PRE_CLIP	     	= 11

const PROJECTION_PERSPECTIVE 	= 0
const PROJECTION_ORTHOGONAL 	= 1

const SOLIDCOLOR = 0
const LINECOLOR  = 1

const PRESENTATION_WIREFRAME       	= 0
const PRESENTATION_SHADED_WIREFRAME 	= 1
const PRESENTATION_RENDERED	 	= 2
const PRESENTATION_CUSTOM	 	= 3

dim cmdPan
dim cmdRotate
dim cmdFly 
dim cmdSetClip 
dim cmdCenter  
dim cmdHome
dim cmdCapture
dim cmdProjection
dim cmdLocationFromClipboard
dim cmdLocationToClipboard
dim cmdBackground
dim cmdBackground_black
dim cmdBackground_blue
dim cmdBackground_white
dim cmdPresentation
dim cmdPresentation1
dim cmdPresentation2
dim cmdPresentation3
dim cmdPreEmptive

dim jhlTest


Sub WebViewer_OnMenuActivated(rootMenuItemID, x, y)
	cmdPan = WebViewer.insertMenuItemLabel(rootMenuItemID, str_pan, "n")
	cmdRotate = WebViewer.insertMenuItemLabel(rootMenuItemID, str_rotate, "r")
	cmdFly = WebViewer.insertMenuItemLabel(rootMenuItemID, str_fly, "f")
	cmdSetClip = WebViewer.insertMenuItemLabel(rootMenuItemID, str_setclipplane, "p")
	cmdCenter = WebViewer.insertMenuItemLabel(rootMenuItemID, str_center, "c")
	cmdHome = WebViewer.insertMenuItemLabel(rootMenuItemID, str_home, "home")
	jhlTest = WebViewer.insertMenuItemBreak(rootMenuItemID)
	cmdCapture = WebViewer.insertMenuItemLabel(rootMenuItemID, str_snapshot, "b")
	jhlTest = WebViewer.insertMenuItemBreak(rootMenuItemID)
	cmdLocationToClipboard = WebViewer.insertMenuItemLabel(rootMenuItemID, str_copylocation, "Ctrl+c")
	cmdLocationFromClipboard = WebViewer.insertMenuItemLabel(rootMenuItemID, str_pastelocation, "Ctrl+v")
	if WebViewer.projectionType = PROJECTION_ORTHOGONAL then
           cmdProjection = WebViewer.insertMenuItemLabel(rootMenuItemID, str_changetoperspective, "l")         
	else
           cmdProjection = WebViewer.insertMenuItemLabel(rootMenuItemID, str_changetoorthogonal, "o")         
	end if

	jhlTest = WebViewer.insertMenuItemBreak(rootMenuItemID)
	cmdBackground = WebViewer.insertMenuItemSubMenu(rootMenuItemID, str_backgroundcolor, "")
	cmdBackground_black = WebViewer.insertMenuItemLabel(cmdBackground, str_black, "") 'rather a function sort of.
	cmdBackground_blue = WebViewer.insertMenuItemLabel(cmdBackground, str_blue, "")
	cmdBackground_white = WebViewer.insertMenuItemLabel(cmdBackground, str_white, "")
	cmdPresentation = WebViewer.insertMenuItemSubMenu(rootMenuItemID, str_presentation, "")
	cmdPresentation1 = WebViewer.insertMenuItemLabel(cmdPresentation, str_wireframe, "Ctrl+1")
	cmdPresentation2 = WebViewer.insertMenuItemLabel(cmdPresentation, str_shadedwireframe, "Ctrl+2")
	cmdPresentation3 = WebViewer.insertMenuItemLabel(cmdPresentation, str_rendered, "Ctrl+3")
	if WebViewer.preemptiveRendering = 1 then
		cmdPreEmptive = WebViewer.insertMenuItemLabel(rootMenuItemID, str_enablefullcontentrendering, "")
	else
		cmdPreEmptive = WebViewer.insertMenuItemLabel(rootMenuItemID, str_disablefullcontentrendering, "")
	end if
end sub


Sub WebViewer_OnMenuSelected(menuItemID)
	if menuItemID = cmdPan then
		WebViewer.interactorMode = INTERACTOR_MODE_PRE_PAN
	elseif menuItemID = cmdRotate then
		WebViewer.interactorMode = INTERACTOR_MODE_PRE_ROTATE
	elseif menuItemID = cmdFly then
		WebViewer.interactorMode = INTERACTOR_MODE_PRE_FLY
	elseif menuItemID = cmdSetClip then
		WebViewer.interactorMode = INTERACTOR_MODE_PRE_CLIP
	elseif menuItemID = cmdCenter then
		call WebViewer.CenterCamera()
	elseif menuItemID = cmdHome then
		WebViewer.moveCameraToHome()
	elseif menuItemID = cmdCapture then
		call WebViewer.copyBitmapToClipboard()
        elseif menuItemID = cmdProjection then
                if WebViewer.projectionType = PROJECTION_ORTHOGONAL then
                    WebViewer.projectionType = PROJECTION_PERSPECTIVE
                else
                    WebViewer.projectionType = PROJECTION_ORTHOGONAL
                end if
	elseif menuItemID = cmdLocationToClipboard then
		call WebViewer.copyLocationToClipboard()
	elseif menuItemID = cmdLocationFromClipboard then
		call WebViewer.copyLocationFromClipboard()
	elseif menuItemID = cmdBackground_black then
		setDefaultColors()
	elseif menuItemID = cmdBackground_blue then
		setBlueColors()
	elseif menuItemID = cmdBackground_white then
		setLightColors()
	elseif menuItemID = cmdPresentation1 then
		WebViewer.presentationMode = PRESENTATION_WIREFRAME
	elseif menuItemID = cmdPresentation2 then
		WebViewer.presentationMode = PRESENTATION_SHADED_WIREFRAME
	elseif menuItemID = cmdPresentation3 then
		WebViewer.presentationMode = PRESENTATION_RENDERED
        elseif menuItemID = cmdPreEmptive then
		if WebViewer.preemptiveRendering = 1 then
			WebViewer.preemptiveRendering = 0
		else
			WebViewer.preemptiveRendering = 1
		end if
	end if
end sub


' parses full model path based on 
' the prefix from window.location.href

function parseModelUrl(basepath)
	dim i
	dim str
	dim result
	dim upcaseString
	str = window.location.href
	result = basepath
	dim appendMode
	appendMode = 0
	dim length
	length = Len(str) 
	upcaseString = UCase(str)
	if InStr(upcaseString, "DOMODLE") = 0 then
            if Mid(str, length,1) = "/" then
	         result =  str + basepath
            else
	         result =  str + "/" + basepath
            end if
        else
	  for i = 1 to length-1
	    dim c
	    c = Mid(str,length-i,1)
	    if c="/" then
	      appendMode=1
	    end if
	    if c="\" then
	      appendMode=1
	       c="/"
	    end if
	    if appendMode=1 then
	      result = c & result
	    end if
	  next
	end if
	parseModelUrl = result
end function