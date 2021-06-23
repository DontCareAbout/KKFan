package us.dontcareabout.kkfan.client.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.data.gf.LogisticsEvent;
import us.dontcareabout.kkfan.client.data.gf.LogisticsHandler;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;
import us.dontcareabout.kkfan.shared.vo.LocationType;

public class NonMapLocationPanel extends AccordionLayoutContainer {
	private GroupingHandlerRegistration hrGroup = new GroupingHandlerRegistration();

	//反正 type 不會太多，就懶得用 HashMap 了... XD
	private List<TypePanel> tpList = new ArrayList<>();

	public NonMapLocationPanel() {
		for (LocationType type : LocationType.values()) {
			if (LocationType.isMapType(type)) { continue; }

			TypePanel child = new TypePanel(type);
			add(child);
			tpList.add(child);

			//預設展開的 panel
			if (type == LocationType.somewhere) { setActiveWidget(child); }
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		hrGroup.add(
			Logistics.addHandler("locationCrate", new LogisticsHandler() {
				@Override
				public void onReady(LogisticsEvent event) {
					refresh();
				}
			})
		);
		Logistics.want("locationCrate");
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		hrGroup.removeHandler();
	}

	private void refresh() {
		for (TypePanel tp : tpList) {
			tp.clean();
		}

		HashMap<Location, List<Crate>> locCrate = Logistics.getData("locationCrate");

		for (Location location : locCrate.keySet()) {
			if (LocationType.isMapType(location.getType())) { continue; }

			for (TypePanel tp : tpList) {
				if (!tp.type.equals(location.getType())) { continue; }

				tp.add(location, locCrate.get(location));
			}
		}

		for (TypePanel tp : tpList) {
			tp.refresh();
		}
	}

	//XXX 目前先用 GXT TextButton 頂一下（雖然案主好像也沒覺得不好 Zzz）
	private static final VerticalLayoutData vld = new VerticalLayoutData(1, 32, new Margins(4, 30, 4, 30));
	class TypePanel extends ContentPanel {
		final LocationType type;

		VerticalLayoutContainer main = new VerticalLayoutContainer();

		TypePanel(LocationType type) {
			this.type = type;
			setHeading(StringUtil.toString(type));

			main.setScrollMode(ScrollMode.AUTOY);
			add(main);
		}

		void add(Location location, List<Crate> list) {
			main.add(new LocationTB(location, list), vld);
		}

		void refresh() {
			forceLayout();
		}

		void clean() {
			main.clear();
		}
	}


	class LocationTB extends TextButton {
		final Location location;
		final List<Crate> list;

		LocationTB(Location location, List<Crate> list) {
			this.location = location;
			this.list = list;
			setText(location.getName() + (list.size() == 0 ? "" : " [+" + list.size() + "]"));
		}
	}
}
