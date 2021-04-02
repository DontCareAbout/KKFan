package us.dontcareabout.kkfan.shared.grpah;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Polygon {
	private static final String splitter = "~";

	private List<XY> list = new ArrayList<>();
	private boolean isModify;
	private XY left;
	private XY right;
	private XY top;
	private XY bottom;

	public void addPoint(XY point) {
		list.add(point);
		isModify = true;
	}

	public void reset() {
		list.clear();
		rebuild();
	}

	public List<XY> getList() {
		return Collections.unmodifiableList(list);
	}

	public XY getLeft() {
		if (isModify) { rebuild(); }
		return left;
	}

	public XY getRight() {
		if (isModify) { rebuild(); }
		return right;
	}

	public XY getTop() {
		if (isModify) { rebuild(); }
		return top;
	}

	public XY getBottom() {
		if (isModify) { rebuild(); }
		return bottom;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();

		for (XY xy : list) {
			result.append(xy.toString() + splitter);
		}

		return result.substring(0, result.length() - 1);
	}

	private void rebuild() {
		isModify = false;

		if (list.isEmpty()) {
			left = null; right = null; top = null; bottom = null;
			return;
		}

		left = list.get(0); right = left; top = left; bottom = left;

		for (XY point : list) {
			if (point.x < left.x) { left = point; }
			if (point.x > right.x) { right = point; }
			if (point.y < top.y) { top = point; }
			if (point.y > bottom.y) { bottom = point; }
		}
	}

	public static Polygon valueOf(String string) {
		String[] value = string.split(splitter);
		Polygon result = new Polygon();

		for (String v : value) {
			result.addPoint(XY.valueOf(v));
		}

		return result;
	}
}
