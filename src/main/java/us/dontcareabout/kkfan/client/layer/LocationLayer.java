package us.dontcareabout.kkfan.client.layer;

import static us.dontcareabout.kkfan.client.util.HtmlTemplate.tplt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOutEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import us.dontcareabout.gxt.client.draw.LRectangleSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.kkfan.client.component.CrateGrid;
import us.dontcareabout.kkfan.client.component.FloorPlan;
import us.dontcareabout.kkfan.client.component.gf.RwdRootPanel;
import us.dontcareabout.kkfan.client.layer.gf.LayerSpriteWithTip;
import us.dontcareabout.kkfan.client.ui.CrateInfoPanel;
import us.dontcareabout.kkfan.client.ui.UiCenter;
import us.dontcareabout.kkfan.client.util.ColorUtil;
import us.dontcareabout.kkfan.client.util.StringUtil;
import us.dontcareabout.kkfan.shared.grpah.Polygon;
import us.dontcareabout.kkfan.shared.grpah.XY;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;

/**
 * TODO 目前只能一律視為矩形處理，等待 GF 的 LPathSprite... [遠目]
 */
public class LocationLayer extends LayerSprite {
	/**
	 * 可以看見箱子的最小比例。
	 * <p>
	 * 因為 crate 原則上最小是 50x50，這樣可以確保最小的 crate 在螢幕上有 10x10 的大小
	 * <p>
	 * 注意：這個值必須大於 {@value FloorPlan#ratioMin}，否則沒有意義。
	 */
	public static final double ratioCrateMin = 0.2;
	public static final int amountHeight = 26;

	private static final CrateInfoPanel crateInfo = new CrateInfoPanel();
	private static final GridPanel gridPanel = new GridPanel();

	public final Location location;
	public final XY left;
	public final XY bottom;

	/**
	 * 這個值應該要跟 {@link FloorPlan} 的值相同，
	 * 計算時機是在 {@link #adjustMember()}（因為 {@link FloorPlan} 變更比例勢必會調整 member 大小）
	 * 計算方式是 {@link Location#getPolygon()} 的寬度除以 {@link #getWidth()}。
	 *
	 * 不使用 event-driven 的方式傳值，是考慮到 {@link FloorPlan} 切換樓層時可能會造成不必要的混亂。
	 */
	private double ratio = 1;

	private TextButton bgNameTB = new TextButton();
	private TextButton crateAmountTB = new TextButton();
	private List<CrateBlock> cbList = new ArrayList<>();

	public LocationLayer(Location location) {
		this.location = location;

		Polygon poly = Polygon.valueOf(location.getPolygon());
		this.left = new XY(poly.getLeft().x, poly.getTop().y);
		this.bottom = new XY(poly.getRight().x, poly.getBottom().y);

		setLX(left.x);
		setLY(left.y);

		RGB color = ColorUtil.get(location.getType());
		Color complementary = ColorUtil.blackOrWhite(color);
		bgNameTB.setBgColor(color);
		bgNameTB.setBgStrokeColor(complementary);
		bgNameTB.setBgStrokeWidth(3);
		bgNameTB.setText(location.getName());
		bgNameTB.setTextColor(complementary);
		bgNameTB.setTextOpacity(0.3);
		add(bgNameTB);

		crateAmountTB.setBgColor(complementary);
		crateAmountTB.setBgStrokeColor(complementary);
		crateAmountTB.setBgStrokeWidth(3);
		//text 在 adjustMember() 裡頭設定
		crateAmountTB.setTextColor(color);
		crateAmountTB.setMargin(2);
		crateAmountTB.setHidden(true);
		crateAmountTB.addSpriteSelectionHandler(new SpriteSelectionHandler() {
			@Override
			public void onSpriteSelect(SpriteSelectionEvent event) {
				ArrayList<Crate> list = new ArrayList<>();

				for (CrateBlock cb : cbList) {
					list.add(cb.crate);
				}

				gridPanel.refresh(list);
				RwdRootPanel.dialog(gridPanel, GridPanel.W, GridPanel.H, location.getName());
			}
		});
		add(crateAmountTB);
	}

	public void takeOver(Crate crate) {
		make(crate);
		redeploy();
		lineUp();
	}

	public void takeOver(List<Crate> crates) {
		for (Crate crate :crates) {
			make(crate);
		}
		redeploy();
		lineUp();
	}

	@Override
	protected void adjustMember() {
		double newRatio = getWidth() / Math.abs(left.x - bottom.x);
		boolean hidden = newRatio < ratioCrateMin && !cbList.isEmpty();

		for (CrateBlock cb : cbList) {
			cb.setHidden(hidden);
		}

		bgNameTB.setLX(0);
		bgNameTB.setLY(0);
		bgNameTB.resize(getWidth(), hidden ? getHeight() - amountHeight : getHeight());
		crateAmountTB.setHidden(!hidden);

		if (hidden) {
			crateAmountTB.setLX(0);
			crateAmountTB.setLY(getHeight() - amountHeight);
			crateAmountTB.resize(getWidth(), amountHeight);
			crateAmountTB.setText("+" + cbList.size());
			return;
		}

		double ratioChange = newRatio / ratio;

		for (CrateBlock cb : cbList) {
			cb.setLX(cb.getLX() * ratioChange);
			cb.setLY(cb.getLY() * ratioChange);
			cb.resize(cb.getWidth() * ratioChange, cb.getHeight() * ratioChange);
		}

		ratio = newRatio;
	}

	/** 注意：沒有處理位置的邏輯，那歸 {@link #lineUp()} 處理 */
	private CrateBlock make(Crate crate) {
		CrateBlock result = new CrateBlock(crate);
		cbList.add(result);
		add(result);

		result.resize(crate.getLength() * ratio, crate.getWidth() * ratio);
		return result;
	}

	//目前是用由大到小依序排下去，撞到邊界就換行的方式排列
	private void lineUp() {
		if (cbList.isEmpty()) { return; }

		Collections.sort(cbList);

		final double xStart = 10;
		final double gap = 4;
		double x = xStart;
		double y = 10;
		double maxH = cbList.get(0).getHeight();

		for (CrateBlock cb : cbList) {
			if (x + cb.getWidth() + gap > getWidth()) {
				x = xStart;
				y += maxH + gap;
				maxH = 0;
			}

			cb.setLX(x);
			cb.setLY(y);

			x += cb.getWidth() + gap;
			if (maxH < cb.getHeight()) { maxH = cb.getHeight(); }
		}
	}

	/**
	 * 搞出 bg 跟 borders 的原因：
	 *
	 * 1. 如果用上 {@link #setBgStrokeColor(Color)}，那麼 {@link SpriteOutEvent} 觸發就會不正常。
	 * 2. 如果用上下兩層 {@link LRectangleSprite} 來疊出 stroke 的效果，會無法做出 opcaity 的效果。
	 * 3. 因為 GF 的 LPathSprite 還沒 ready，所以只好用四個 {@link LRectangleSprite} 來拼......
	 */
	class CrateBlock extends LayerSpriteWithTip implements Comparable<CrateBlock> {
		final Crate crate;

		LRectangleSprite bg = new LRectangleSprite();
		LRectangleSprite[] borders = { new LRectangleSprite(), new LRectangleSprite(), new LRectangleSprite(), new LRectangleSprite()};

		CrateBlock(Crate crate) {
			this.crate = crate;

			RGB color = new RGB("#" + crate.getColor());
			Color bOrW = ColorUtil.blackOrWhite(color);
			double opacity = 0.9;

			for (LRectangleSprite border : borders) {
				border.setFill(bOrW);
				border.setOpacity(opacity);
				add(border);
			}

			bg.setFill(color);
			bg.setOpacity(opacity);
			add(bg);

			tipConfig.setAutoHide(false);
			tipConfig.setTitle(tplt.crateTipTitle(crate));
			tipConfig.setBody(tplt.crateTipBody(crate));
			refreshTip();

			addSpriteSelectionHandler(new SpriteSelectionHandler() {
				@Override
				public void onSpriteSelect(SpriteSelectionEvent event) {
					hideTip();
					showCrateInfo(crate);
				}
			});
		}

		@Override
		public int compareTo(CrateBlock o) {
			if (crate.getLength() != o.crate.getLength()) {
				return Double.compare(o.crate.getLength(), crate.getLength());
			}

			return Double.compare(o.crate.getWidth(), crate.getWidth());
		}

		@Override
		protected void adjustMember() {
			bg.setLX(1);
			bg.setLY(1);
			bg.setWidth(getWidth() - 2);
			bg.setHeight(getHeight() - 2);

			borders[0].setLX(0); borders[0].setLY(0);
			borders[0].setWidth(getWidth());
			borders[0].setHeight(1);

			borders[1].setLX(0); borders[1].setLY(getHeight() - 1);
			borders[1].setWidth(getWidth());
			borders[1].setHeight(1);

			borders[2].setLX(0); borders[2].setLY(0);
			borders[2].setWidth(1);
			borders[2].setHeight(getHeight());

			borders[3].setLX(getWidth() - 1); borders[3].setLY(0);
			borders[3].setWidth(1);
			borders[3].setHeight(getHeight());
		}
	}

	static class GridPanel extends SimpleContainer {
		static final int W = 250;
		static final int H = 400;

		CrateGrid grid = new CrateGrid(true);

		GridPanel() {
			grid.addSelectionHandler(new SelectionHandler<Crate>() {
				@Override
				public void onSelection(SelectionEvent<Crate> event) {
					showCrateInfo(event.getSelectedItem());
				}
			});

			add(grid);
		}

		@Override
		protected void onUnload() {
			super.onUnload();
			UiCenter.floorPlanFocus();	//Refactory
		}

		void refresh(List<Crate> list) {
			grid.refresh(list);
		}
	}

	private static void showCrateInfo(Crate crate) {
		crateInfo.refresh(crate);
		RwdRootPanel.dialog(crateInfo, CrateInfoPanel.W, CrateInfoPanel.H, StringUtil.serial(crate));
	}
}