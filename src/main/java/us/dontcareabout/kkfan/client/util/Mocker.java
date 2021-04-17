package us.dontcareabout.kkfan.client.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.kkfan.shared.vo.Crate;
import us.dontcareabout.kkfan.shared.vo.Location;
import us.dontcareabout.kkfan.shared.vo.LocationType;

public class Mocker {
	final static Random random = new Random();
	final static RGB[] colors = { ColorUtil.cyan200, ColorUtil.green200, ColorUtil.yellow200, ColorUtil.purple200};

	static int id = 0;
	static int no = 0;

	static Location[] locArray = {
		location("尚未入庫", LocationType.unborn)
	};
	static Location UNBORN = locArray[locArray.length - 1];

	public static List<Location> locationList() {
		return Arrays.asList(locArray);
	}

	public static List<Crate> crateList() {
		return Arrays.asList(
		);
	}

	static Location location(String name, LocationType type) {
		Location result = new Location();
		result.setId(id++);
		result.setName(name);
		result.setType(type);
		return result;
	}

	static Location location(String name, LocationType type, int floor, String polygon) {
		Location result = location(name, type);
		result.setFloor(floor);
		result.setPolygon(polygon);
		return result;
	}

	static Crate crate(String category) {
		return crate(category, UNBORN);
	}

	static Crate crate(String category, Location loc) {
		int size = size(category);
		Crate result = new Crate();
		result.setId(id++);
		result.setCategory(category);
		result.setNumber(no++);
		result.setWidth(size);
		result.setLength(size);
		result.setHeight(size);
		result.setLocation(loc);
		result.setItem(random.nextDouble() > 0.5 ? null : "白菜 + 滷肉 + 鍋");
		result.setColor(ColorUtil.toString(colors[id % 4]));
		return result;
	}

	static int size(String category) {
		switch (category.length()) {
		case 1: return 50;
		case 2: return 100;
		case 3: return 200;
		case 4: return 300;
		default: return 500;
		}
	}
}