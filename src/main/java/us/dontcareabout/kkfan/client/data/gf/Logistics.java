package us.dontcareabout.kkfan.client.data.gf;

import java.util.HashMap;
import java.util.Set;

import com.google.gwt.event.shared.HandlerRegistration;

public class Logistics {
	private static HashMap<String, Supplier<?>> map = new HashMap<>();

	public static void join(Supplier<?> supplier) {
		if (map.containsKey(supplier.name())) {
			throw new IllegalArgumentException("Supplier \"" + supplier.name() + "\" already exist.");
		}

		map.put(supplier.name(), supplier);
	}

	public static void want(String name) {
		find(name).tally();
	}

	public static void wantForced(String name) {
		find(name).tallyForced();
	}

	@SuppressWarnings("unchecked")
	public static <D> D getData(String name) {
		return (D)find(name).getData();
	}

	public static <H extends LogisticsHandler> HandlerRegistration addHandler(String name, H handler) {
		return find(name).addHandler(handler);
	}

	public static Set<String> getSupplierList() {
		return map.keySet();
	}

	private static Supplier<?> find(String name) {
		Supplier<?> hero = map.get(name);

		if (hero == null) { throw new IllegalArgumentException("Supplier \"" + name + "\" does not exist."); }

		return hero;
	}
}
