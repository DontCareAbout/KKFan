package us.dontcareabout.kkfan.client.layer;

import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.gxt.client.draw.LRectangleSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.kkfan.client.component.FloorPlan;
import us.dontcareabout.kkfan.client.util.ColorUtil;
import us.dontcareabout.kkfan.shared.grpah.Polygon;
import us.dontcareabout.kkfan.shared.grpah.XY;
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

	private LRectangleSprite bg = new LRectangleSprite();

	public LocationLayer(Location location) {
		this.location = location;

		Polygon poly = Polygon.valueOf(location.getPolygon());
		this.left = new XY(poly.getLeft().x, poly.getTop().y);
		this.bottom = new XY(poly.getRight().x, poly.getBottom().y);

		RGB color = ColorUtil.get(location.getType());
		bg.setFill(color);
		bg.setStroke(ColorUtil.blackOrWhite(color));
		bg.setStrokeWidth(3);
		add(bg);
	}

	@Override
	protected void adjustMember() {
		ratio = getWidth() / Math.abs(left.x - bottom.x);

		bg.setLX(0);
		bg.setLY(0);
		bg.setWidth(getWidth());
		bg.setHeight(getHeight());
	}
}