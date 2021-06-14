package us.dontcareabout.kkfan.client.ui.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.kkfan.client.ui.event.FloorPlanFocusEvent.FloorPlanFocusHandler;

public class FloorPlanFocusEvent extends GwtEvent<FloorPlanFocusHandler> {
	public static final Type<FloorPlanFocusHandler> TYPE = new Type<FloorPlanFocusHandler>();

	@Override
	public Type<FloorPlanFocusHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FloorPlanFocusHandler handler) {
		handler.process(this);
	}

	public interface FloorPlanFocusHandler extends EventHandler{
		public void process(FloorPlanFocusEvent event);
	}
}
