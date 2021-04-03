package us.dontcareabout.kkfan.client.component;

import java.util.ArrayList;
import java.util.List;

import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;

import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.kkfan.client.layer.LocationLayer;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.vo.Location;

public class FloorPlan extends LayerContainer {
	private double ratio = 0.04;

	private List<LocationLayer> locLayerList = new ArrayList<>();
	private LayerSprite bg = new LayerSprite();
	private InfoLayer infoLayer = new InfoLayer();

	public FloorPlan() {
		bg.setBgColor(RGB.BLACK);
		bg.setLX(0);
		bg.setLY(0);
		addLayer(bg);
		addLayer(infoLayer);
	}

	public void refresh(int floor, List<Location> locations) {
		infoLayer.setFloor(floor);

		for (LocationLayer locLayer : locLayerList) {
			locLayer.undeploy();
		}

		locLayerList.clear();

		for (Location location : locations) {
			if (location.getFloor() == floor) {
				LocationLayer locLayer = new LocationLayer(location);
				locLayerList.add(locLayer);
				addLayer(locLayer);
			}
		}

		adjustMember(getOffsetWidth(), getOffsetHeight());
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
						setRatio(ratio + 0.01 * (isPlus ? 1 : -1));
					}
				});
			}
		}
	}

}
