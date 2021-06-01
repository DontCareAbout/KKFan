package us.dontcareabout.kkfan.client.data;

import java.util.Date;
import java.util.List;

import us.dontcareabout.kkfan.client.data.gf.Callback;
import us.dontcareabout.kkfan.client.data.gf.Supplier;
import us.dontcareabout.kkfan.shared.vo.Location;

public class LocationSupplier extends Supplier<List<Location>> {
	@Override
	public String name() {
		return "location";
	}

	@Override
	public void fetch() {
		DataCenter.locationRester.getList(new Callback<List<Location>>() {
			@Override
			public void onSuccess(List<Location> data) {
				ready(data);
			}

			@Override
			public void onError(Throwable exception) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public Date nextExpire() {
		return latterDate(60);
	}
}