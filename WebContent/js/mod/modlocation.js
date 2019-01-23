    updateLocationBox();

	for (i = 1; i < locations.length; i++)
	{
		if (locations.options[i].text == WebViewer.GetNamedLocationByIndex(itemId))
	    {
			locations.selectedIndex = i;
	    }
	}
	setStatus(str_location + " " + locations.options[locations.selectedIndex].text + " " + str_isnowset);