package us.dontcareabout.kkfan.client.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import us.dontcareabout.gxt.client.component.Grid2;
import us.dontcareabout.kkfan.shared.vo.Location;
import us.dontcareabout.kkfan.shared.vo.LocationType;

public class LocationGrid extends Grid2<Location> {
	private static final Properties properties = GWT.create(Properties.class);

	public LocationGrid() {
		init();
	}

	public void refresh(List<Location> data) {
		getStore().replaceAll(data);
	}

	@Override
	protected ListStore<Location> genListStore() {
		ListStore<Location> result = new ListStore<>(new ModelKeyProvider<Location>() {
			@Override
			public String getKey(Location item) {
				return item.getId() + "";
			}
		});
		return result;
	}

	@Override
	protected ColumnModel<Location> genColumnModel() {
		ColumnConfig<Location, LocationType> type = new ColumnConfig<>(properties.type(), 100, "類型");
		type.setCell(new AbstractCell<LocationType>() {
			@Override
			public void render(Context context, LocationType value, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant(value.toString());	//TODO 轉換成 UF 字串
			}
		});

		List<ColumnConfig<Location, ?>> list = new ArrayList<>();
		list.add(new ColumnConfig<Location, Long>(properties.id(), 50, "ID"));
		list.add(type);
		list.add(new ColumnConfig<Location, String>(properties.name(), 100, "名稱"));
		ColumnModel<Location> result = new ColumnModel<>(list);
		return result;
	}

	interface Properties extends PropertyAccess<Location> {
		 ValueProvider<Location, Long> id();
		 ValueProvider<Location, String> name();
		 ValueProvider<Location, LocationType> type();
	}
}
