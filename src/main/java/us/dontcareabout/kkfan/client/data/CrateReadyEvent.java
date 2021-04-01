package us.dontcareabout.kkfan.client.data;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.kkfan.client.data.CrateReadyEvent.CrateReadyHandler;
import us.dontcareabout.kkfan.shared.vo.Crate;

public class CrateReadyEvent extends GwtEvent<CrateReadyHandler> {
	public static final Type<CrateReadyHandler> TYPE = new Type<CrateReadyHandler>();

	public final List<Crate> data;

	public CrateReadyEvent(List<Crate> crateList) {
		data = crateList;
	}

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
