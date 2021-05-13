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
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import us.dontcareabout.kkfan.client.data.DataCenter;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.Location;
import us.dontcareabout.kkfan.shared.vo.LocationType;

public class LocationEditor extends Composite implements Editor<Location> {
	private static LocationPanelUiBinder uiBinder = GWT.create(LocationPanelUiBinder.class);

	@UiField ContentPanel root;
	@UiField TextField name;
	@UiField LocationTypeCB type;
	@UiField IntegerField floor;
	@UiField TextArea polygon;

	private Driver driver = GWT.create(Driver.class);

	public LocationEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(this);
	}

	public void refresh(Location data) {
		//新建資料預設 type 為 unborn
		data.setType(data.getId() == null ? LocationType.unborn : data.getType());
		driver.edit(data);
		root.setHeading(data.getId() == null ? "新增" : "編輯 (id=" + data.getId() + ")");
		mapTypeCheck(data.getType());
	}

	public Location result() {
		return driver.flush().clean();
	}

	@UiHandler("type")
	void typeChange(SelectionEvent<LocationType> event) {
		mapTypeCheck(event.getSelectedItem());
	}

	@UiHandler("cancel")
	void cancelSelect(SelectEvent event) {
		mask(StringUtil.SELECT_OR_NEW);
	}

	@UiHandler("submit")
	void submitSelect(SelectEvent event) {
		DataCenter.save(result());
	}

	private void mapTypeCheck(LocationType type) {
		boolean enable = LocationType.isMapType(type);
		floor.setEnabled(enable);
		polygon.setEnabled(enable);
	}

	interface LocationPanelUiBinder extends UiBinder<Widget, LocationEditor> {}
	interface Driver extends SimpleBeanEditorDriver<Location, LocationEditor> {}
}
