package us.dontcareabout.kkfan.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.DoubleField;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import us.dontcareabout.kkfan.client.data.DataCenter;
import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.data.gf.LogisticsEvent;
import us.dontcareabout.kkfan.client.data.gf.LogisticsHandler;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.LocationType;

public class CrateEditor extends Composite implements Editor<Crate> {
	private static CrateEditorUiBinder uiBinder = GWT.create(CrateEditorUiBinder.class);

	@UiField ContentPanel root;
	@UiField TextField category;
	@UiField IntegerField number;
	@UiField LocationCB location;
	@UiField DoubleField length;
	@UiField DoubleField width;
	@UiField DoubleField height;
	@UiField IntegerField mfYear;
	@UiField TextField mfr;
	@UiField TextField color;
	@UiField TextArea item;
	@UiField TextArea note;

	@UiField @Ignore LocationTypeCB locationType;

	private Driver driver = GWT.create(Driver.class);

	public CrateEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(this);

		Logistics.addHandler("location", new LogisticsHandler() {
			@Override
			public void onReady(LogisticsEvent event) {
				location.fillData(Logistics.getData("location"));
			}
		});
	}

	public void refresh(Crate data) {
		if (data.getLocation() != null) {
			locationType.setValue(data.getLocation().getType());
			location.setType(data.getLocation().getType());
			//location.setType() 必須在 driver.edit() 之前做，因為裡頭有 setValue(null);
		} else {
			locationType.setValue(null);
		}

		driver.edit(data);
		root.setHeading(StringUtil.newOrEdit(data));
		unmask();
	}

	public Crate result() {
		return driver.flush();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		Logistics.want("location");
	}

	@UiHandler("locationType")
	void typeChange(SelectionEvent<LocationType> event) {
		location.setType(event.getSelectedItem());
	}

	@UiHandler("cancel")
	void cancelSelect(SelectEvent event) {
		mask(StringUtil.SELECT_OR_NEW);
	}

	@UiHandler("submit")
	void submitSelect(SelectEvent event) {
		DataCenter.save(result().clean());
	}

	interface CrateEditorUiBinder extends UiBinder<Widget, CrateEditor> {}
	interface Driver extends SimpleBeanEditorDriver<Crate, CrateEditor> {}
}
