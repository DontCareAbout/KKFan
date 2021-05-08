package us.dontcareabout.kkfan.client.data;

import java.util.List;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.kkfan.client.data.gf.Callback;
import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.data.gf.MapperRoom;
import us.dontcareabout.kkfan.client.data.gf.Rester;
import us.dontcareabout.kkfan.shared.vo.Location;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	public static void init() {
		MapperRoom.join("location", GWT.create(LocationMapper.class), GWT.create(LocationListMapper.class));
		locationRester = new Rester<>("location");
		Logistics.join(new LocationSupplier());

		Logistics.join(new CrateSupplier());
	}

	public static Rester<Location, Long> locationRester;
	public static void save(Location data) {
		locationRester.save(data, new Callback<Integer>() {
			@Override
			public void onSuccess(Integer data) {
				Logistics.wantForced("location");
			}

			@Override
			public void onError(Throwable exception) {
				// TODO Auto-generated method stub
			}
		});
	}

	// ==== Mapper Interface ==== //
	public interface LocationMapper extends ObjectMapper<Location> {}
	public interface LocationListMapper extends ObjectMapper<List<Location>> {}
}
