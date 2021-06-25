package us.dontcareabout.kkfan.client.component;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.widget.core.client.ContentPanel;

import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.data.gf.LogisticsEvent;
import us.dontcareabout.kkfan.client.data.gf.LogisticsHandler;
import us.dontcareabout.kkfan.client.ui.UiCenter;
import us.dontcareabout.kkfan.client.ui.event.SelectLocationEvent;
import us.dontcareabout.kkfan.client.ui.event.SelectLocationEvent.SelectLocationHandler;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;

public class LocationCratePanel extends ContentPanel {
	private Location location;
	private GroupingHandlerRegistration hrGroup = new GroupingHandlerRegistration();

	private CrateGrid grid = new CrateGrid(true);

	public LocationCratePanel() {
		grid.mask("請選擇地點");
		grid.addSelectionHandler(new SelectionHandler<Crate>() {
			@Override
			public void onSelection(SelectionEvent<Crate> event) {
				UiCenter.showCrateInfo(event.getSelectedItem());
			}
		});
		add(grid);

		UiCenter.addSelectNonMapLocation(new SelectLocationHandler() {
			@Override
			public void onSelectLocation(SelectLocationEvent event) {
				location = event.location;
				setHeading(
					"[" + StringUtil.toString(location.getType()) + "] " +
					location.getName()
				);
				refresh();
			}
		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		hrGroup.add(
			Logistics.addHandler("locationCrate", new LogisticsHandler() {
				@Override
				public void onReady(LogisticsEvent event) {
					if (location == null) { return; }

					refresh();
				}
			})
		);
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		hrGroup.removeHandler();
	}

	private void refresh() {
		grid.unmask();
		HashMap<Location, List<Crate>> map = Logistics.getData("locationCrate");
		grid.refresh(map.get(location));
	}
}
