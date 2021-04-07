package us.dontcareabout.kkfan.client.layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.gxt.client.draw.LRectangleSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.kkfan.client.component.FloorPlan;
import us.dontcareabout.kkfan.client.util.ColorUtil;
import us.dontcareabout.kkfan.shared.grpah.Polygon;
import us.dontcareabout.kkfan.shared.grpah.XY;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;

/**
 * TODO 目前只能一律視為矩形處理，等待 GF 的 LPathSprite... [遠目]
 */
public class LocationLayer extends LayerSprite {
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
	private double ratio;

	private TextButton bgNameTB = new TextButton();
	private List<CrateBlock> cbList = new ArrayList<>();

	public LocationLayer(Location location) {
		this.location = location;

		Polygon poly = Polygon.valueOf(location.getPolygon());
		this.left = new XY(poly.getLeft().x, poly.getTop().y);
		this.bottom = new XY(poly.getRight().x, poly.getBottom().y);

		RGB color = ColorUtil.get(location.getType());
		Color complementary = ColorUtil.blackOrWhite(color);
		bgNameTB.setBgColor(color);
		bgNameTB.setBgStrokeColor(complementary);
		bgNameTB.setBgStrokeWidth(3);
		bgNameTB.setText(location.getName());
		bgNameTB.setTextColor(complementary);
		bgNameTB.setTextOpacity(0.3);
		add(bgNameTB);
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
		double ratioChange = newRatio / ratio;

		for (CrateBlock cb : cbList) {
			cb.setLX(cb.getLX() * ratioChange);
			cb.setLY(cb.getLY() * ratioChange);
			cb.setWidth(cb.getWidth() * ratioChange);
			cb.setHeight(cb.getHeight() * ratioChange);
		}

		ratio = newRatio;

		bgNameTB.setLX(0);
		bgNameTB.setLY(0);
		bgNameTB.resize(getWidth(), getHeight());
	}

	/** 注意：沒有處理位置的邏輯，那歸 {@link #lineUp()} 處理 */
	private CrateBlock make(Crate crate) {
		CrateBlock result = new CrateBlock(crate);
		cbList.add(result);
		add(result);

		result.setWidth(crate.getLength() * ratio);
		result.setHeight(crate.getWidth() * ratio);
		return result;
	}

	//目前是用由大到小依序排下去，撞到邊界就換行的方式排列
	private void lineUp() {
		if (cbList.isEmpty()) { return; }

		Collections.sort(cbList);

		final double xStart = 10;
		double x = xStart;
		double y = 10;
		double maxH = cbList.get(0).getHeight();

		for (CrateBlock cb : cbList) {
			if (x + cb.getWidth() > getWidth()) {
				x = xStart;
				y += maxH;
				maxH = 0;
			}

			cb.setLX(x);
			cb.setLY(y);

			x += cb.getWidth();
			if (maxH < cb.getHeight()) { maxH = cb.getHeight(); }
		}
	}

	class CrateBlock extends LRectangleSprite implements Comparable<CrateBlock> {
		final Crate crate;

		CrateBlock(Crate crate) {
			this.crate = crate;
			RGB color = new RGB("#" + crate.getColor());
			setFill(color);
			setOpacity(0.9);
			setStroke(ColorUtil.blackOrWhite(color));
		}

		@Override
		public int compareTo(CrateBlock o) {
			if (crate.getLength() != o.crate.getLength()) {
				return Double.compare(o.crate.getLength(), crate.getLength());
			}

			return Double.compare(o.crate.getWidth(), crate.getWidth());
		}
	}
}