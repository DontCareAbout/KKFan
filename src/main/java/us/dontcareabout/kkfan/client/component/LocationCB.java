package us.dontcareabout.kkfan.client.component;

import java.util.List;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import us.dontcareabout.kkfan.shared.vo.Location;
import us.dontcareabout.kkfan.shared.vo.LocationType;

/**
 * 因為要顧及 {@link Location} 結構、UI 呈現、還有 caller 無須再寫一堆判斷等因素，
 * 所以無法直接用 {@link #setValue(Location)}，需改用 {@link #setLocation(Location)}。
 */
public class LocationCB extends ComboBox<Location> {
	private Filter filter = new Filter();

	public LocationCB() {
		super(
			new ListStore<>(new ModelKeyProvider<Location>() {
				@Override
				public String getKey(Location item) {
					return "" + item.getId();
				}
			}),
			new LabelProvider<Location>() {
				@Override
				public String getLabel(Location item) {
					return item.getName();
				}
			}
		);
		setTriggerAction(TriggerAction.ALL);
		getStore().addFilter(filter);
	}

	public void fillData(List<Location> list) {
		getStore().replaceAll(list);
	}

	/**
	 * @see #setType(LocationType)
	 */
	public void setLocation(Location location) {
		setType(location.getType());

		//如果 type 是 unborn 就會 disable
		if (isEnabled()) { setValue(location); }
	}

	public void setType(LocationType type) {
		setValue(null);
		filter.setType(type);
		getStore().setEnableFilters(false);
		getStore().setEnableFilters(true);
	}

	private class Filter implements StoreFilter<Location> {
		private LocationType type;

		@Override
		public boolean select(Store<Location> store, Location parent, Location item) {
			return type == null ? true : item.getType() == type;
		}

		void setType(LocationType type) { this.type = type; }
	}
}
