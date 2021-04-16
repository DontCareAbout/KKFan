package us.dontcareabout.kkfan.client.util;

import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.LocationType;

public class StringUtil {
	public static String floor(int floor) {
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
