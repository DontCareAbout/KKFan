package us.dontcareabout.kkfan.client.data;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.kkfan.client.data.CrateReadyEvent.CrateReadyHandler;
import us.dontcareabout.kkfan.client.data.LocationReadyEvent.LocationReadyHandler;
import us.dontcareabout.kkfan.client.util.Mocker;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	private static List<Location> locationList;

	public static List<Location> getLocationList() {
		return locationList;
	}

	public static void wantLocation() {
		//FIXME fetch real data
		if (locationList == null) { locationList  = Mocker.locationList(); }

		eventBus.fireEvent(new LocationReadyEvent());
	}

	public static HandlerRegistration addLocationReady(LocationReadyHandler handler) {
		return eventBus.addHandler(LocationReadyEvent.TYPE, handler);
	}

	////////////////

	private static List<Crate> crateList = Mocker.crateList();

	public static void wantCrate() {
		//FIXME fetch real data
		if (crateList == null) { crateList = Mocker.crateList(); }

		eventBus.fireEvent(new CrateReadyEvent());
	}

	public static HandlerRegistration addCrateReady(CrateReadyHandler handler) {
		return eventBus.addHandler(CrateReadyEvent.TYPE, handler);
	}

}
