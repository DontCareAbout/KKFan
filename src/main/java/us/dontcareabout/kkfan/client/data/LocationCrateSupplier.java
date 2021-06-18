package us.dontcareabout.kkfan.client.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.data.gf.LogisticsEvent;
import us.dontcareabout.kkfan.client.data.gf.LogisticsHandler;
import us.dontcareabout.kkfan.client.data.gf.Supplier;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;

public class LocationCrateSupplier extends Supplier<HashMap<Location, List<Crate>>> {
	public LocationCrateSupplier() {
		Logistics.addHandler("location", new LogisticsHandler() {
			@Override
			public void onReady(LogisticsEvent event) {
				Logistics.want("crate");
			}
		});
		Logistics.addHandler("crate", new LogisticsHandler() {
			@Override
			public void onReady(LogisticsEvent event) {
				finish();
			}
		});
	}

	@Override
	public String name() {
		return "locationCrate";
	}

	@Override
	public void fetch() {
		Logistics.want("location");
		//實際呼叫 ready() 是在 finish()
	}

	@Override
	public Date nextExpire() {
		return latterDate(60);
	}

	private void finish() {
		HashMap<Location, List<Crate>> data = new HashMap<>();

		List<Location> locations = Logistics.getData("location");
		for (Location location : locations) {
			data.put(location, new ArrayList<>());
		}

		List<Crate> crates = Logistics.getData("crate");
		for (Crate crate : crates) {
			data.get(crate.getLocation()).add(crate);
		}

		ready(data);
	}
}
