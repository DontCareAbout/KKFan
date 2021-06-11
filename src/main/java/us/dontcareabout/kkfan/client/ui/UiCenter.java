package us.dontcareabout.kkfan.client.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.kkfan.client.component.gf.RwdRootPanel;
import us.dontcareabout.kkfan.client.ui.event.CrateInfoCloseEvent;
import us.dontcareabout.kkfan.client.ui.event.CrateInfoCloseEvent.CrateInfoCloseHandler;

public class UiCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	private final static MainView mainView = new MainView();

	public static void start() {
		RwdRootPanel.setComponent(mainView);
	}

	////////////////
	public static void crateInfoClose() {
		eventBus.fireEvent(new CrateInfoCloseEvent());
	}

	public static HandlerRegistration addCrateInfoClose(CrateInfoCloseHandler handler) {
		return eventBus.addHandler(CrateInfoCloseEvent.TYPE, handler);
	}
}