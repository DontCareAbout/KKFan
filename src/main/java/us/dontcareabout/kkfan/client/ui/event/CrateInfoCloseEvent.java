package us.dontcareabout.kkfan.client.ui.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.kkfan.client.ui.event.CrateInfoCloseEvent.CrateInfoCloseHandler;

public class CrateInfoCloseEvent extends GwtEvent<CrateInfoCloseHandler> {
	public static final Type<CrateInfoCloseHandler> TYPE = new Type<CrateInfoCloseHandler>();

	@Override
	public Type<CrateInfoCloseHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CrateInfoCloseHandler handler) {
		handler.onCrateInfoClose(this);
	}

	public interface CrateInfoCloseHandler extends EventHandler{
		public void onCrateInfoClose(CrateInfoCloseEvent event);
	}
}
