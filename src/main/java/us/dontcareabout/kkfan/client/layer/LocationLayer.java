package us.dontcareabout.kkfan.client.layer;

import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.gxt.client.draw.LRectangleSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;
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
		bg.setLX(0);
		bg.setLY(0);
		bg.setWidth(getWidth());
		bg.setHeight(getHeight());
	}
}