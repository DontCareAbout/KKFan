package us.dontcareabout.kkfan.client.component;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import us.dontcareabout.gxt.client.component.Grid2;
import us.dontcareabout.gxt.client.model.GetValueProvider;
import us.dontcareabout.kkfan.client.util.ColorUtil;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;

public class CrateGrid extends Grid2<Crate> {
	private static final Properties properties = GWT.create(Properties.class);
	private static final CellTemplate cellTemplate = GWT.create(CellTemplate.class);

	public CrateGrid() {
		init();
		getView().setAutoFill(true);
	}

	public void refresh(List<Crate> data) {
		getStore().replaceAll(data);
	}

	@Override
	protected ListStore<Crate> genListStore() {
		ListStore<Crate> result = new ListStore<>(new ModelKeyProvider<Crate>() {
			@Override
			public String getKey(Crate item) {
				return item.getId() + "";
			}
		});
		return result;

	}

	@Override
	protected ColumnModel<Crate> genColumnModel() {
		ColumnConfig<Crate, Crate> serial = new ColumnConfig<>(new IdentityValueProvider<>(), 10, "編號");
		serial.setCell(new AbstractCell<Crate>() {
			@Override
			public void render(Context context, Crate value, SafeHtmlBuilder sb) {
				String string = value.getCategory() + "-" + value.getNumber();
				String hexCode = "#" + value.getColor();
				sb.append(
					cellTemplate.nameCell(
						string,
						ColorUtil.blackOrWhite(hexCode).toString(),
						hexCode
					)
				);
			}
		});
		serial.setCellPadding(false);

		ColumnConfig<Crate, Location> location = new ColumnConfig<>(properties.location(), 10, "位置");
		location.setCell(new AbstractCell<Location>() {
			@Override
			public void render(Context context, Location value, SafeHtmlBuilder sb) {
				sb.append(cellTemplate.locationCell(value.getName(), ColorUtil.get(value.getType()).toString()));
			}
		});
		location.setCellPadding(false);

		List<ColumnConfig<Crate, ?>> list = new ArrayList<>();
		list.add(serial);
		list.add(new ColumnConfig<>(new GetValueProvider<Crate, String>() {
			@Override
			public String getValue(Crate object) {
				return object.getLength() + " x " + object.getWidth() + " x " + object.getHeight();
			}
		}, 10, "尺寸"));
		list.add(location);
		return new ColumnModel<>(list);
	}

	interface Properties extends PropertyAccess<Crate> {
		ValueProvider<Crate, Long> id();
		ValueProvider<Crate, String> color();
		ValueProvider<Crate, Location> location();
	}

	interface CellTemplate extends XTemplates {
		@XTemplate("<div style='height:20px; color:{textColor}; background-color:{bgColor}; align-items:center; display:flex; padding:0 0 0 5px;'>{name}</div>")
		SafeHtml nameCell(String name, String textColor, String bgColor);

		@XTemplate("<div style='height:20px; color:white; background-color:{typeColor}; align-items:center; display:flex; padding:0 0 0 5px;'>{name}</div>")
		SafeHtml locationCell(String name, String typeColor);
	}
}
