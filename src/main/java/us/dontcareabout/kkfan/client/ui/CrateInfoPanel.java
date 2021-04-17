package us.dontcareabout.kkfan.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;

import us.dontcareabout.kkfan.client.component.LocationCB;
import us.dontcareabout.kkfan.client.component.LocationTypeCB;
import us.dontcareabout.kkfan.client.component.gf.RwdRootPanel;
import us.dontcareabout.kkfan.client.data.DataCenter;
import us.dontcareabout.kkfan.client.data.LocationReadyEvent;
import us.dontcareabout.kkfan.client.data.LocationReadyEvent.LocationReadyHandler;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.LocationType;

public class CrateInfoPanel extends Composite {
	public static final int W = 300;
	public static final int H = 440;

	private static CrateInfoPanelUiBinder uiBinder = GWT.create(CrateInfoPanelUiBinder.class);
	interface CrateInfoPanelUiBinder extends UiBinder<Widget, CrateInfoPanel> {}

	@UiField LabelToolItem size;
	@UiField LocationTypeCB locationType;
	@UiField LocationCB location;
	@UiField TextArea item;
	@UiField TextArea note;

	public CrateInfoPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		DataCenter.addLocationReady(new LocationReadyHandler() {
			@Override
			public void onLocationReady(LocationReadyEvent event) {
				location.fillData(DataCenter.getLocationList());
			}
		});
	}

	public void refresh(Crate crate) {
		size.setLabel(StringUtil.size(crate));
		locationType.setValue(crate.getLocation().getType());
		location.setLocation(crate.getLocation());
		item.setValue(crate.getItem());
		note.setValue(crate.getNote());
	}

	@UiHandler("locationType")
	void typeChange(SelectionEvent<LocationType> event) {
		location.setType(event.getSelectedItem());
	}

	@UiHandler("cancel")
	void cancelSelect(SelectEvent se) {
		RwdRootPanel.closeDialog();
	}

	@UiHandler("submit")
	void submitSelect(SelectEvent se) {
		//TODO
	}
}
