package us.dontcareabout.kkfan.client.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.kkfan.client.component.gf.RwdRootPanel;
import us.dontcareabout.kkfan.client.ui.event.FloorPlanFocusEvent;
import us.dontcareabout.kkfan.client.ui.event.FloorPlanFocusEvent.FloorPlanFocusHandler;
import us.dontcareabout.kkfan.client.ui.event.SelectLocationEvent;
import us.dontcareabout.kkfan.client.ui.event.SelectLocationEvent.SelectLocationHandler;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;

public class UiCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	private final static MainView mainView = new MainView();

	public static void start() {
		RwdRootPanel.setComponent(mainView);
	}

	////////////////

	private static final CrateInfoPanel crateInfo = new CrateInfoPanel();

	public static void showCrateInfo(Crate crate) {
		crateInfo.refresh(crate);
		RwdRootPanel.dialog(crateInfo, CrateInfoPanel.W, CrateInfoPanel.H, StringUtil.serial(crate));
	}

	public static void floorPlanFocus() {
		eventBus.fireEvent(new FloorPlanFocusEvent());
	}

	public static HandlerRegistration addFloorPlanFocus(FloorPlanFocusHandler handler) {
		return eventBus.addHandler(FloorPlanFocusEvent.TYPE, handler);
	}

	public static void selectLocation(Location location) {
		eventBus.fireEvent(new SelectLocationEvent(location));
	}

	public static HandlerRegistration addSelectNonMapLocation(SelectLocationHandler handler) {
		return eventBus.addHandler(SelectLocationEvent.TYPE, handler);
	}
}