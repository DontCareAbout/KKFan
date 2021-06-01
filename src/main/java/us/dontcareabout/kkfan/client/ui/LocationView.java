package us.dontcareabout.kkfan.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import us.dontcareabout.kkfan.client.component.LocationEditor;
import us.dontcareabout.kkfan.client.component.LocationGrid;
import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.data.gf.LogisticsEvent;
import us.dontcareabout.kkfan.client.data.gf.LogisticsHandler;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.Location;

public class LocationView extends Composite {
	private static LocationPanelUiBinder uiBinder = GWT.create(LocationPanelUiBinder.class);
	interface LocationPanelUiBinder extends UiBinder<Widget, LocationView> {}

	@UiField LocationGrid grid;
	@UiField LocationEditor editor;

	public LocationView() {
		initWidget(uiBinder.createAndBindUi(this));

		Logistics.addHandler("location", new LogisticsHandler() {
			@Override
			public void onReady(LogisticsEvent event) {
				grid.refresh(Logistics.getData("location"));
				editor.mask(StringUtil.SELECT_OR_NEW);
			}
		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		Logistics.want("location");
	}

	@UiHandler("grid")
	void dataSelect(SelectionEvent<Location> event) {
		editor.refresh(event.getSelectedItem());
	}

	@UiHandler("newBtn")
	void newSelect(SelectEvent event) {
		editor.refresh(new Location());
	}
}
