package us.dontcareabout.kkfan.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import us.dontcareabout.kkfan.client.component.LocationEditor;
import us.dontcareabout.kkfan.client.component.LocationGrid;
import us.dontcareabout.kkfan.client.data.DataCenter;
import us.dontcareabout.kkfan.client.data.LocationReadyEvent;
import us.dontcareabout.kkfan.client.data.LocationReadyEvent.LocationReadyHandler;
import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.Location;

public class LocationView extends Composite {
	private static LocationPanelUiBinder uiBinder = GWT.create(LocationPanelUiBinder.class);
	interface LocationPanelUiBinder extends UiBinder<Widget, LocationView> {}

	@UiField LocationGrid grid;
	@UiField VerticalLayoutContainer panel;
	@UiField LocationEditor editor;

	public LocationView() {
		initWidget(uiBinder.createAndBindUi(this));

		panel.setPixelSize(300, 320);
		editor.addSubmitHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				DataCenter.save(editor.result());
			}
		});
		editor.addCancelHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				editor.mask(StringUtil.SELECT_OR_NEW);
			}
		});

		Logistics.addHandler("location", new LocationReadyHandler() {
			@Override
			public void onLocationReady(LocationReadyEvent event) {
				grid.refresh(Logistics.getData("location"));
				editor.mask(StringUtil.SELECT_OR_NEW);
			}
		});
		Logistics.want("location");
	}

	@UiHandler("grid")
	void dataSelect(SelectionEvent<Location> event) {
		editor.refresh(event.getSelectedItem());
		editor.unmask();
	}

	@UiHandler("newBtn")
	void newSelect(SelectEvent event) {
		editor.refresh(new Location());
		editor.unmask();
	}
}
