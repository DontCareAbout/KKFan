package us.dontcareabout.kkfan.client.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.kkfan.client.data.CrateReadyEvent.CrateReadyHandler;

public class CrateReadyEvent extends GwtEvent<CrateReadyHandler> {
	public static final Type<CrateReadyHandler> TYPE = new Type<CrateReadyHandler>();

	@Override
	public Type<CrateReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CrateReadyHandler handler) {
		handler.onCrateReady(this);
	}

	public interface CrateReadyHandler extends EventHandler{
		public void onCrateReady(CrateReadyEvent event);
	}
}
