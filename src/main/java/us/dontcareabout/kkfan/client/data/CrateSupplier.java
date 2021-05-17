package us.dontcareabout.kkfan.client.data;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

import us.dontcareabout.kkfan.client.data.CrateReadyEvent.CrateReadyHandler;
import us.dontcareabout.kkfan.client.data.gf.Callback;
import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.data.gf.Supplier;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;

/**
 * {@link Logistics} 的 {@link Location} 資料必須在 {@link #fetch()} 之前 ready。 
 */
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
		DataCenter.crateRester.getList(new Callback<List<Crate>>() {
			@Override
			public void onSuccess(List<Crate> data) {
				List<Location> locList = Logistics.getData("location");

				for (Crate c : data) {
					for (Location loc : locList) {
						if (c.getLocationId() == loc.getId()) {
							c.setLocation(loc);
							break;
						}
					}
				}

				ready(data);
			}

			@Override
			public void onError(Throwable exception) {
			}
		});
	}

	@Override
	public Date nextExpire() {
		return latterDate(60);
	}
}
