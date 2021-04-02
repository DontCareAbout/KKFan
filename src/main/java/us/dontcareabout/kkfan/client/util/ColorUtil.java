package us.dontcareabout.kkfan.client.util;

import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.HSL;
import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.kkfan.shared.vo.LocationType;

public class ColorUtil {
	public static final RGB red900 = new RGB("#b71c1c");
	public static final RGB purple200 = new RGB("#ce93d8");
	public static final RGB blue900 = new RGB("#0d47a1");
	public static final RGB cyan200 = new RGB("#80deea");
	public static final RGB green200 = new RGB("#a5d6a7");
	public static final RGB green800 = new RGB("#2e7d32");
	public static final RGB yellow200 = new RGB("#fff59d");
	public static final RGB grey700 = new RGB("#616161");
	public static final RGB blueGrey900 = new RGB("#263238");

	public static RGB get(LocationType locationType) {
		switch(locationType) {
		case gallery: return blue900;
		case somewhere: return red900;
		case storage: return green800;
		case trip: return blueGrey900;
		case unborn: return grey700;
		default: return grey700;
		}
	}

	////////////////

	public static String toString(RGB color) {
		return hex(color.getRed()) + hex(color.getGreen()) + hex(color.getBlue());
	}

	/** @return 轉成固定兩位的 16 進位字串 */
	public static String hex(int value) {
		value = Math.abs(value) % 256;
		String result = Integer.toHexString(value);
		return result.length() == 1 ? "0" + result : result;
	}

	/** @return 指定顏色應搭配黑色（明亮度高）或是白色（明亮度低） */
	public static Color blackOrWhite(String hexCode) {
		return blackOrWhite(new RGB(hexCode));
	}

	/** @return 指定顏色應搭配黑色（明亮度高）或是白色（明亮度低） */
	public static Color blackOrWhite(RGB rgb) {
		return new HSL(rgb).getLightness() > 0.6 ? RGB.BLACK : RGB.WHITE;
	}
}
