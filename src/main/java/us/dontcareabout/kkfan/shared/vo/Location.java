package us.dontcareabout.kkfan.shared.vo;

/**
 * 若 {@link #getType()} 為 {@value LocationType#trip} 或 {@value LocationType#unborn}，
 * 則 {@link #getFloor()} 與 {@link #getPolygon()} 的值無意義。
 */
public class Location {
	private long id;

	private String name;

	private LocationType type;

	private int floor;
	private String polygon;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocationType getType() {
		return type;
	}

	public void setType(LocationType type) {
		this.type = type;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getPolygon() {
		return polygon;
	}

	public void setPolygon(String polygon) {
		this.polygon = polygon;
	}
}
