package us.dontcareabout.kkfan.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import us.dontcareabout.kkfan.shared.vo.Location;
import us.dontcareabout.kkfan.shared.vo.LocationType;

public class LocationEditor extends Composite implements Editor<Location> {
	private static LocationPanelUiBinder uiBinder = GWT.create(LocationPanelUiBinder.class);

	@UiField ContentPanel root;
	@UiField TextField name;
	@UiField LocationTypeCB type;
	@UiField IntegerField floor;
	@UiField TextArea polygon;

	@UiField @Ignore TextButton cancel;
	@UiField @Ignore TextButton submit;

	private Driver driver = GWT.create(Driver.class);

	public LocationEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(this);
	}

	public void refresh(Location data) {
		driver.edit(data);
		root.setHeading(data.getId() == 0 ? "新增" : "編輯 (id=" + data.getId() + ")");
	}

	public Location result() {
		return driver.flush();
	}

	public HandlerRegistration addCancelHandler(SelectHandler handler) {
		return cancel.addSelectHandler(handler);
	}

	public HandlerRegistration addSubmitHandler(SelectHandler handler) {
		return submit.addSelectHandler(handler);
	}

	@UiHandler("type")
	void typeChange(SelectionEvent<LocationType> event) {
		boolean enable = LocationType.isMapType(event.getSelectedItem());
		floor.setEnabled(enable);
		polygon.setEnabled(enable);
	}

	interface LocationPanelUiBinder extends UiBinder<Widget, LocationEditor> {}
	interface Driver extends SimpleBeanEditorDriver<Location, LocationEditor> {}
}
