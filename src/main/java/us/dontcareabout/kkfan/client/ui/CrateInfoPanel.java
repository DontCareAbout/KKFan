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
import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.data.gf.LogisticsEvent;
import us.dontcareabout.kkfan.client.data.gf.LogisticsHandler;
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

	private Crate crate;

	public CrateInfoPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		Logistics.addHandler("location", new LogisticsHandler() {
			@Override
			public void onReady(LogisticsEvent event) {
				location.fillData(Logistics.getData("location"));
			}
		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		Logistics.want("location");
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		UiCenter.floorPlanFocus();
	}

	public void refresh(Crate crate) {
		this.crate = crate;
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
		crate.setLocation(location.getValue());
		crate.setItem(item.getValue());
		crate.setNote(note.getValue());
		DataCenter.save(crate.clean());
		RwdRootPanel.closeDialog();
	}
}
