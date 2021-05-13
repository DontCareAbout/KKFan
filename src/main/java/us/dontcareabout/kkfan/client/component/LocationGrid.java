package us.dontcareabout.kkfan.client.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import us.dontcareabout.gxt.client.component.Grid2;
import us.dontcareabout.kkfan.client.data.DataCenter;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.client.util.gf.ColumnConfigBuilder;
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

	public HandlerRegistration addSelectionHandler(SelectionHandler<Location> h) {
		return getSelectionModel().addSelectionHandler(h);
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
		AbstractCell<LocationType> typeCell = new AbstractCell<LocationType>() {
			@Override
			public void render(Context context, LocationType value, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant(StringUtil.toString(value));
			}
		};
		AbstractCell<Integer> floorCell = new AbstractCell<Integer>() {
			@Override
			public void render(Context context, Integer value, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant(StringUtil.floor(value));
			}
		};
		TextButtonCell deleteBtn = new TextButtonCell();
		deleteBtn.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				int row = event.getContext().getIndex();
				DataCenter.delete(getStore().get(row));
			}
		});

		List<ColumnConfig<Location, ?>> list = new ArrayList<>();
		list.add(new ColumnConfig<Location, Long>(properties.id(), 50, "ID"));
		list.add(new ColumnConfig<Location, String>(properties.name(), 150, "名稱"));
		list.add(
			new ColumnConfigBuilder<Location, LocationType>(properties.type())
			.setWidth(100).setHeader("類型").setCell(typeCell).build()
		);
		list.add(
			new ColumnConfigBuilder<Location, Integer>(properties.floor())
			.setWidth(100).setHeader("樓層").setCell(floorCell).build()
		);
		list.add(
			new ColumnConfigBuilder<Location, String>("刪除")
			.setWidth(50).setCell(deleteBtn).centerStyle().build()
		);
		ColumnModel<Location> result = new ColumnModel<>(list);
		return result;
	}

	interface Properties extends PropertyAccess<Location> {
		 ValueProvider<Location, Long> id();
		 ValueProvider<Location, String> name();
		 ValueProvider<Location, LocationType> type();
		 ValueProvider<Location, Integer> floor();
	}
}
