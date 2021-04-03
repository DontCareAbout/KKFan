package us.dontcareabout.kkfan.client.util;

public class StringUtil {
	public static String floor(int floor) {
		return floor > 0 ? floor + "F" : "B" + Math.abs(floor);
	}
}
