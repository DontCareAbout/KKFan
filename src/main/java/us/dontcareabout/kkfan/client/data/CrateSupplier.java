package us.dontcareabout.kkfan.client.data;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

import us.dontcareabout.kkfan.client.data.CrateReadyEvent.CrateReadyHandler;
import us.dontcareabout.kkfan.client.data.gf.Supplier;
import us.dontcareabout.kkfan.client.util.Mocker;
import us.dontcareabout.kkfan.shared.vo.Crate;

public class CrateSupplier extends Supplier<List<Crate>, CrateReadyHandler> {

	@Override
	public String name() {
		return "crate";
	}

	@Override
	public Type<CrateReadyHandler> eventType() {
		return CrateReadyEvent.TYPE;
	}

	@Override
	public GwtEvent<CrateReadyHandler> genEvent() {
		return new CrateReadyEvent();
	}

	@Override
	public void fetch() {
		ready(Mocker.crateList());
	}

	@Override
	public Date nextExpire() {
		return latterDate(60);
	}

}
