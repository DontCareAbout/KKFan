package us.dontcareabout.kkfan.client.util;

import us.dontcareabout.kkfan.shared.gf.HasId;
import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.LocationType;

public class StringUtil {
	public static final String SELECT_OR_NEW = "請點選資料或按下「新增」";

	public static String newOrEdit(HasId<?> data) {
		return data.getId() == null ? "新增" : "編輯 (id=" + data.getId() + ")";
	}

	////////

	public static String floor(Integer floor) {
		if (floor == null) { return ""; }

		return floor > 0 ? floor + "F" : "B" + Math.abs(floor);
	}

	public static String serial(Crate crate) {
		return crate.getCategory() + "-" + crate.getNumber();
	}

	public static String size(Crate crate) {
		return crate.getLength() + " x " + crate.getWidth() + " x " + crate.getHeight();
	}

	public static String toString(LocationType type) {
		switch(type) {
		case gallery: return "展廳";
		case somewhere: return "館內";
		case storage: return "庫房";
		case trip: return "外出中";
		case unborn:
		default: return "尚未入帳";
		}
	}
}
