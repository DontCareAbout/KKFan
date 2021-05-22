package us.dontcareabout.kkfan.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import us.dontcareabout.kkfan.client.component.CrateEditor;
import us.dontcareabout.kkfan.client.component.CrateGrid;
import us.dontcareabout.kkfan.client.data.CrateReadyEvent;
import us.dontcareabout.kkfan.client.data.CrateReadyEvent.CrateReadyHandler;
import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.Crate;

public class CrateView extends Composite {
	private static CrateViewUiBinder uiBinder = GWT.create(CrateViewUiBinder.class);

	@UiField CrateGrid grid;
	@UiField CrateEditor editor;

	public CrateView() {
		initWidget(uiBinder.createAndBindUi(this));

		Logistics.addHandler("crate", new CrateReadyHandler() {
			@Override
			public void onCrateReady(CrateReadyEvent event) {
				grid.refresh(Logistics.getData("crate"));
				editor.mask(StringUtil.SELECT_OR_NEW);
			}
		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		Logistics.want("crate");
	}

	@UiHandler("grid")
	void dataSelect(SelectionEvent<Crate> event) {
		editor.refresh(event.getSelectedItem());
	}

	@UiHandler("newBtn")
	void newSelect(SelectEvent event) {
		editor.refresh(new Crate());
	}

	interface CrateViewUiBinder extends UiBinder<Widget, CrateView> {}
}
