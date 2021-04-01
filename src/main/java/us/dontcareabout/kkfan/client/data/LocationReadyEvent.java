package us.dontcareabout.kkfan.client.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.kkfan.client.data.LocationReadyEvent.LocationReadyHandler;

public class LocationReadyEvent extends GwtEvent<LocationReadyHandler> {
	public static final Type<LocationReadyHandler> TYPE = new Type<LocationReadyHandler>();

	@Override
	public Type<LocationReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LocationReadyHandler handler) {
		handler.onLocationReady(this);
	}

	public interface LocationReadyHandler extends EventHandler{
		public void onLocationReady(LocationReadyEvent event);
	}
}
