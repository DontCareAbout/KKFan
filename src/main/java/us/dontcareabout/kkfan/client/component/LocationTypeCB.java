package us.dontcareabout.kkfan.client.component;

import java.util.Arrays;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.LocationType;

public class LocationTypeCB extends ComboBox<LocationType> {

	public LocationTypeCB() {
		super(
			new ListStore<>(new ModelKeyProvider<LocationType>() {
				@Override
				public String getKey(LocationType item) {
					return item.name();
				}
			}),
			new LabelProvider<LocationType>() {
				@Override
				public String getLabel(LocationType item) {
					return StringUtil.toString(item);
				}
			}
		);
		setTriggerAction(TriggerAction.ALL);
		getStore().addAll(Arrays.asList(LocationType.values()));
	}
}
