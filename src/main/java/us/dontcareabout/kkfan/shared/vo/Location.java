package us.dontcareabout.kkfan.shared.vo;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import us.dontcareabout.kkfan.shared.gf.HasId;

/**
 * 若 {@link #getType()} 為不為 map type（{@link LocationType#isMapType(LocationType)}），
 * 則 {@link #getFloor()} 與 {@link #getPolygon()} 的值應為 null（{@link #clean()}）。
 */
@Entity
public class Location implements HasId<Long> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private LocationType type;

	private Integer floor;
	private String polygon;
	private boolean disable;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
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

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public String getPolygon() {
		return polygon;
	}

	public void setPolygon(String polygon) {
		this.polygon = polygon;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public Location clean() {
		if (LocationType.isMapType(type)) { return this; }

		floor = null;
		polygon = null;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof Location)) {
			return false;
		}
		Location other = (Location)obj;
		return Objects.equals(id, other.id);
	}
}
