package us.dontcareabout.kkfan.client.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.core.client.util.PrecisePoint;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;

import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.kkfan.client.data.gf.Logistics;
import us.dontcareabout.kkfan.client.data.gf.LogisticsEvent;
import us.dontcareabout.kkfan.client.data.gf.LogisticsHandler;
import us.dontcareabout.kkfan.client.layer.LocationLayer;
import us.dontcareabout.kkfan.client.ui.UiCenter;
import us.dontcareabout.kkfan.client.ui.event.CrateInfoCloseEvent;
import us.dontcareabout.kkfan.client.ui.event.CrateInfoCloseEvent.CrateInfoCloseHandler;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.client.util.gf.EventUtil;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;

public class FloorPlan extends LayerContainer {
	//因為 crate 原則上最小是 50x50，這樣可以確保最小的 crate 在螢幕上有 10x10 的大小
	private static final double ratioMin = 0.2;

	private static final double shiftDistance = 50;

	//param 區，預設值參見 resetParam()
	private double preRatio;
	private double ratio;
	private double offsetX;
	private double offsetY;
	/** 操作累積的總位移量 */
	private double totalOffsetX;
	private double totalOffsetY;
	////

	private List<LocationLayer> locLayerList = new ArrayList<>();
	private LayerSprite bg = new LayerSprite();
	private InfoLayer infoLayer = new InfoLayer();

	private Integer floor = 1;
	private GroupingHandlerRegistration hrGroup = new GroupingHandlerRegistration();

	public FloorPlan() {
		sinkEvents(Event.ONMOUSEWHEEL | Event.ONKEYDOWN);
		bg.setBgColor(RGB.BLACK);
		bg.setLX(0);
		bg.setLY(0);
		addLayer(bg);
		addLayer(infoLayer);

		//為了能收到 key event 的黑魔法... O.o （ref: GXT Menu）
		getElement().setTabIndex(0);

		UiCenter.addCrateInfoClose(new CrateInfoCloseHandler() {
			@Override
			public void onCrateInfoClose(CrateInfoCloseEvent event) {
				getElement().focus();
			}
		});
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);

		if (event.getTypeInt() == Event.ONMOUSEWHEEL) {
			doWheel(event);
			return;
		}

		if (event.getTypeInt() == Event.ONKEYDOWN) {
			doKeyin(event);
			return;
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		//由於 refresh() 裡頭會清空重來，所以也得把 ratio / offset 相關參數恢復預設值
		resetParam();

		hrGroup.add(
			Logistics.addHandler("crate", new LogisticsHandler() {
				@Override
				public void onReady(LogisticsEvent event) {
					refresh();
				}
			})
		);
		hrGroup.add(
			Logistics.addHandler("location", new LogisticsHandler() {
				@Override
				public void onReady(LogisticsEvent event) {
					Logistics.want("crate");
				}
			})
		);
		Logistics.want("location");
		getElement().focus();
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

		preRatio = 1;
		offsetX = totalOffsetX;
		offsetY = totalOffsetY;
		//在這邊就要做 adjustMember() 不然 LocationLayer 沒大小可以讓後頭計算
		adjustMember(getOffsetWidth(), getOffsetHeight());
		//為了撰寫方便起見，一開始就會做一次比例尺從 1 到 ratio 的動作
		//所以要校正成正確值，在 scale() 也有一樣的行為
		//但是下面這行不能寫在 adjustMember() 裡頭
		//因為 adjustMember() 的 caller 包含 GXT 的 layout 機制
		preRatio = ratio;
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
			room.setLX(correct(room.getLX(), offsetX));
			room.setLY(correct(room.getLY(), offsetY));
			room.resize(
				Math.abs(room.left.x - room.bottom.x) * ratio,
				Math.abs(room.left.y - room.bottom.y) * ratio
			);
		}

		offsetX = 0;
		offsetY = 0;
	}

	private void resetParam() {
		preRatio = 1;
		ratio = 0.25;
		offsetX = 0;
		offsetY = 0;
		totalOffsetX = 0;
		totalOffsetY = 0;
	}

	private void scale(boolean isPlus) {
		//沒有觸發位置（aka 是 InfoLayer 的 button 觸發）
		//就視為在畫面正中心
		scale(isPlus, bg.getWidth() / 2, bg.getHeight() / 2);
	}

	private void scale(boolean isPlus, double eventX, double eventY) {
		//滾輪縮放才會需要阻擋一直縮小下去，按按鈕不會發生
		if (!isPlus && preRatio <= ratioMin) { return; }

		ratio = preRatio + 0.01 * (isPlus ? 1 : -1);
		offsetX = eventX * (1 - ratio / preRatio);
		offsetY = eventY * (1 - ratio / preRatio);
		totalOffsetX = correct(totalOffsetX, offsetX);
		totalOffsetY = correct(totalOffsetY, offsetY);
		infoLayer.minusTB.setHidden(ratio <= ratioMin);
		adjustMember(getOffsetWidth(), getOffsetHeight());
		preRatio = ratio;
	}

	//嚴格來講不是修正，只是不知道該怎麼取名，反正 private method... [逃]
	private double correct(double value, double offset) {
		return value / preRatio * ratio + offset;
	}

	private void doWheel(Event event) {
		PrecisePoint point = EventUtil.eventPoint(event);

		//這邊是採用 Google Map 的操作，往下滾（velocity > 0）是縮小
		scale(event.getMouseWheelVelocityY() < 0, point.getX(), point.getY());

		//滾輪才需要，按鈕是在 LayerContainer 的 event 機制就呼叫了
		redrawSurface();
	}

	private void doKeyin(Event event) {
		switch(event.getKeyCode()) {
		case KeyCodes.KEY_LEFT:
			shiftX(shiftDistance); return;
		case KeyCodes.KEY_RIGHT:
			shiftX(-shiftDistance); return;
		case KeyCodes.KEY_UP:
			shiftY(shiftDistance); return;
		case KeyCodes.KEY_DOWN:
			shiftY(-shiftDistance); return;
		}
	}

	private void shiftX(double offset) {
		offsetX = offset;
		offsetY = 0;
		totalOffsetX += offsetX;
		adjustMember(getOffsetWidth(), getOffsetHeight());
		redrawSurface();
	}

	private void shiftY(double offset) {
		offsetX = 0;
		offsetY = offset;
		totalOffsetY += offsetY;
		adjustMember(getOffsetWidth(), getOffsetHeight());
		redrawSurface();
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
						scale(isPlus);
					}
				});
			}
		}
	}
}
