package us.dontcareabout.kkfan.client.util;

import us.dontcareabout.kkfan.shared.vo.Crate;

public class StringUtil {
	public static String floor(int floor) {
		return floor > 0 ? floor + "F" : "B" + Math.abs(floor);
	}

	public static String serial(Crate crate) {
		return crate.getCategory() + "-" + crate.getNumber();
	}
}
