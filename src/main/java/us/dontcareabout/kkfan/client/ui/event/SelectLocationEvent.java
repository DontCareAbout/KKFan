package us.dontcareabout.kkfan.client.ui.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.kkfan.client.ui.event.SelectLocationEvent.SelectLocationHandler;
import us.dontcareabout.kkfan.shared.vo.Location;

public class SelectLocationEvent extends GwtEvent<SelectLocationHandler> {
	public static final Type<SelectLocationHandler> TYPE = new Type<SelectLocationHandler>();

	public final Location location;

	public SelectLocationEvent(Location location) {
		this.location = location;
	}

	@Override
	public Type<SelectLocationHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SelectLocationHandler handler) {
		handler.onSelectLocation(this);
	}

	public interface SelectLocationHandler extends EventHandler{
		public void onSelectLocation(SelectLocationEvent event);
	}
}
