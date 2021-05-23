package us.dontcareabout.kkfan.client.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;

import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.kkfan.client.data.CrateReadyEvent;
import us.dontcareabout.kkfan.client.data.CrateReadyEvent.CrateReadyHandler;
import us.dontcareabout.kkfan.client.data.LocationReadyEvent;
import us.dontcareabout.kkfan.client.data.LocationReadyEvent.LocationReadyHandler;
import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.layer.LocationLayer;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;

public class FloorPlan extends LayerContainer {
	//因為 crate 原則上最小是 50x50，這樣可以確保最小的 crate 在螢幕上有 10x10 的大小
	private static final double ratioMin = 0.2;

	private double ratio = 0.25;

	private List<LocationLayer> locLayerList = new ArrayList<>();
	private LayerSprite bg = new LayerSprite();
	private InfoLayer infoLayer = new InfoLayer();

	private Integer floor = 1;
	private GroupingHandlerRegistration hrGroup = new GroupingHandlerRegistration();

	public FloorPlan() {
		bg.setBgColor(RGB.BLACK);
		bg.setLX(0);
		bg.setLY(0);
		addLayer(bg);
		addLayer(infoLayer);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		hrGroup.add(
			Logistics.addHandler("crate", new CrateReadyHandler() {
				@Override
				public void onCrateReady(CrateReadyEvent event) {
					refresh();
				}
			})
		);
		hrGroup.add(
			Logistics.addHandler("location", new LocationReadyHandler() {
				@Override
				public void onLocationReady(LocationReadyEvent event) {
					Logistics.want("crate");
				}
			})
		);
		Logistics.want("location");
	}

	@Override
	protected void onUnload() {
		hrGroup.removeHandler();
		super.onUnload();
	}

	private void refresh() {
		infoLayer.setFloor(floor);

		//==== 處理 location ====//
		for (LocationLayer locLayer : locLayerList) {
			locLayer.undeploy();
		}

		locLayerList.clear();

		List<Location> locations = Logistics.getData("location");
		for (Location location : locations) {
			if (floor.equals(location.getFloor())) {
				LocationLayer locLayer = new LocationLayer(location);
				locLayerList.add(locLayer);
				addLayer(locLayer);
			}
		}

		//在這邊就要做 adjustMember() 不然 LocationLayer 沒大小可以讓後頭計算
		adjustMember(getOffsetWidth(), getOffsetHeight());
		//========//

		//==== 處理 crate ====//
		List<Crate> crates = Logistics.getData("crate");
		HashMap<LocationLayer, List<Crate>> locateCrate = new HashMap<>();

		for (Crate crate : crates) {
			for (LocationLayer locLayer : locLayerList) {
				if (!crate.getLocation().equals(locLayer.location)) { continue; }

				List<Crate> list = locateCrate.get(locLayer);

				if (list == null) {
					list = new ArrayList<>();
					locateCrate.put(locLayer, list);
				}

				list.add(crate);
				break;
			}
		}

		for (LocationLayer locLayer : locateCrate.keySet()) {
			locLayer.takeOver(locateCrate.get(locLayer));
		}
		//========//

		redrawSurface();
	}

	@Override
	protected void adjustMember(int width, int height) {
		bg.resize(width, height);

		infoLayer.resize(InfoLayer.W, InfoLayer.H);
		infoLayer.setLX(width - InfoLayer.W - 10);
		infoLayer.setLY(10);

		//以下是浮動區
		for (LocationLayer room : locLayerList) {
			room.setLX(room.left.x * ratio);
			room.setLY(room.left.y * ratio);
			room.resize(
				Math.abs(room.left.x - room.bottom.x) * ratio,
				Math.abs(room.left.y - room.bottom.y) * ratio
			);
		}
	}

	private void setRatio(double value) {
		if (value == ratio) { return; }

		ratio = value;
		adjustMember(getOffsetWidth(), getOffsetHeight());
	}

	private class InfoLayer extends LayerSprite {
		static final int gap = 5;
		static final int btnSize = 26;
		static final int floorSize = 80;
		static final int W = floorSize + btnSize + gap;
		static final int H = btnSize * 2 + gap;

		final RGB color = RGB.DARKGRAY;

		TextButton floorBtn = new TextButton();
		Button plusTB = new Button(true);
		Button minusTB = new Button(false);

		InfoLayer() {
			floorBtn.setTextColor(color);
			floorBtn.setMargin(1);
			add(floorBtn);
			add(plusTB);
			add(minusTB);
			setLZIndex(100);
		}

		void setFloor(int floor) {
			floorBtn.setText(StringUtil.floor(floor));
		}

		@Override
		protected void adjustMember() {
			floorBtn.resize(floorSize, H);
			floorBtn.setLX(0);
			floorBtn.setLY(0);

			plusTB.setLX(floorSize + gap);
			plusTB.setLY(0);
			plusTB.resize(btnSize, btnSize);
			minusTB.setLX(floorSize + gap);
			minusTB.setLY(btnSize + 5);
			minusTB.resize(btnSize, btnSize);
		}

		class Button extends TextButton {
			Button(boolean isPlus) {
				super(isPlus ? "+" : "-");
				setBgColor(color);
				setBgRadius(5);
				setMargin(3);
				addSpriteSelectionHandler(new SpriteSelectionHandler() {
					@Override
					public void onSpriteSelect(SpriteSelectionEvent event) {
						double newRatio = ratio + 0.01 * (isPlus ? 1 : -1);
						minusTB.setHidden(newRatio <= ratioMin);
						setRatio(newRatio);
					}
				});
			}
		}
	}

}
