package us.dontcareabout.kkfan.client.component;

import static us.dontcareabout.kkfan.client.util.HtmlTemplate.tplt;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.RowExpander;

import us.dontcareabout.gxt.client.component.Grid2;
import us.dontcareabout.gxt.client.model.GetValueProvider;
import us.dontcareabout.kkfan.client.util.ColorUtil;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.client.util.gf.ColumnConfigBuilder;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;

public class CrateGrid extends Grid2<Crate> {
	private static final Properties properties = GWT.create(Properties.class);

	private final boolean simpleMode;

	private RowExpander<Crate> rowExpander = new RowExpander<>(
		new AbstractCell<Crate>() {
			@Override
			public void render(Context context, Crate value, SafeHtmlBuilder sb) {
				sb.append(tplt.crateGridExpand(value, simpleMode));
			}
		}
	);

	public CrateGrid() {
		this(false);
	}

	public CrateGrid(boolean simpleMode) {
		this.simpleMode = simpleMode;

		init();
		rowExpander.initPlugin(this);
		getView().setAutoFill(true);
	}

	public void refresh(List<Crate> data) {
		if (data.size() == 0) { mask("沒有箱子"); }

		getStore().replaceAll(data);
	}

	public HandlerRegistration addSelectionHandler(SelectionHandler<Crate> h) {
		return getSelectionModel().addSelectionHandler(h);
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
		AbstractCell<Crate> serial = new AbstractCell<Crate>() {
			@Override
			public void render(Context context, Crate value, SafeHtmlBuilder sb) {
				String string = value.getCategory() + "-" + value.getNumber();
				String hexCode = "#" + value.getColor();
				sb.append(
					tplt.crateGridName(
						string,
						ColorUtil.blackOrWhite(hexCode).toString(),
						hexCode
					)
				);
			}
		};

		AbstractCell<Location> location = new AbstractCell<Location>() {
			@Override
			public void render(Context context, Location value, SafeHtmlBuilder sb) {
				sb.append(tplt.crateGridLocation(value.getName(), ColorUtil.get(value.getType()).toString()));
			}
		};

		List<ColumnConfig<Crate, ?>> list = new ArrayList<>();
		list.add(rowExpander);
		list.add(
			new ColumnConfigBuilder<Crate, Crate>(new IdentityValueProvider<>())
				.setWidth(8).setHeader("編號").setCell(serial).setCellPedding(false).build()
		);

		if (!simpleMode) {
			list.add(
				new ColumnConfigBuilder<Crate, Location>(properties.location())
					.setWidth(8).setHeader("位置").setCell(location).setCellPedding(false).build()
			);
		}

		list.add(
			new ColumnConfigBuilder<Crate, String>(new GetValueProvider<Crate, String>() {
				@Override
				public String getValue(Crate object) {
					return StringUtil.size(object);
				}
			}).setWidth(10).setHeader("尺寸").build()
		);

		if (simpleMode) { return new ColumnModel<>(list); }

		list.add(
			new ColumnConfigBuilder<Crate, String>(properties.mfr())
				.setWidth(10).setHeader("製造商").build()
		);
		list.add(
			new ColumnConfigBuilder<Crate, Integer>(properties.mfYear())
				.setWidth(8).setHeader("製造年份").build()
		);
		return new ColumnModel<>(list);
	}

	interface Properties extends PropertyAccess<Crate> {
		ValueProvider<Crate, Long> id();
		ValueProvider<Crate, Location> location();
		ValueProvider<Crate, String> mfr();
		ValueProvider<Crate, Integer> mfYear();
	}
}
