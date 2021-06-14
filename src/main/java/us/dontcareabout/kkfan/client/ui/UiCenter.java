package us.dontcareabout.kkfan.client.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.kkfan.client.component.gf.RwdRootPanel;
import us.dontcareabout.kkfan.client.ui.event.FloorPlanFocusEvent;
import us.dontcareabout.kkfan.client.ui.event.FloorPlanFocusEvent.FloorPlanFocusHandler;

public class UiCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	private final static MainView mainView = new MainView();

	public static void start() {
		RwdRootPanel.setComponent(mainView);
	}

	////////////////

	public static void floorPlanFocus() {
		eventBus.fireEvent(new FloorPlanFocusEvent());
	}

	public static HandlerRegistration addFloorPlanFocus(FloorPlanFocusHandler handler) {
		return eventBus.addHandler(FloorPlanFocusEvent.TYPE, handler);
	}
}