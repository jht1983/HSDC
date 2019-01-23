//locations.selectedIndex = 0;

	if (WebViewer.INTERACTORMODE == INTERACTOR_MODE_PRE_PAN)
		setStatus(str_panon);
	else if (WebViewer.INTERACTORMODE == INTERACTOR_MODE_PRE_ROTATE)
		setStatus(str_rotateon);
	else if (WebViewer.INTERACTORMODE == INTERACTOR_MODE_PRE_FLY)
		setStatus(str_flyon);

	return true;