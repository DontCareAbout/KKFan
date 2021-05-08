package us.dontcareabout.kkfan.client.data.gf;

import java.util.HashMap;
import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

public class Logistics {
	private static HashMap<String, Supplier<?, ? extends EventHandler>> map = new HashMap<>();
	private static SimpleEventBus eventBus = new SimpleEventBus();

	public static void join(Supplier<?, ? extends EventHandler> supplier) {
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

	public static <H extends EventHandler> HandlerRegistration addHandler(String name, H handler) {
		@SuppressWarnings("unchecked")
		Supplier<?, H> hero = (Supplier<?, H>)find(name);
		return eventBus.addHandler(hero.eventType(), handler);
	}

	public static Set<String> getSupplierList() {
		return map.keySet();
	}

	private static Supplier<?, ? extends EventHandler> find(String name) {
		Supplier<?, ? extends EventHandler> hero = map.get(name);

		if (hero == null) { throw new IllegalArgumentException("Supplier \"" + name + "\" does not exist."); }

		return hero;
	}

	static void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}
}
