package us.dontcareabout.kkfan.shared.vo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import us.dontcareabout.kkfan.shared.gf.HasId;

/**
 * 「箱子」的對應 VO。
 * <p>
 * <h1>商業邏輯哏：</h1>
 * <ul>
 * 	<li>{@link #getLength()} 不能小於 {@link #getWidth()} （{@link #clean()}）</li>
 * 	<li>若 {@link #getDeleteTime()} 不為 null 表示該筆資料已經刪除。</li>
 * </ul>
 */
@Entity
public class Crate implements HasId<Long> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Date createTime;
	private Date deleteTime;

	private String category;
	private int number;

	//spec 區
	private double height;
	private double length;
	private double width;
	private String color;
	////

	//製造資訊區
	private int mfYear;
	private String mfr;
	////

	//管理區
	@Transient @JsonIgnore
	private Location location;
	private Long locationId;
	private String item;
	////

	private String note;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	@JsonIgnore
	public boolean isDelete() {
		return deleteTime != null;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	/** @return 箱子的高，單位 cm */
	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	/** @return 箱子的長，單位 cm */
	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	/** @return 箱子的寬，單位 cm */
	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	/** @return 不含「#」的六位 hex 色碼 */
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	/** @return 製造年份 */
	public int getMfYear() {
		return mfYear;
	}

	public void setMfYear(int mfYear) {
		this.mfYear = mfYear;
	}

	/** @return 製造商 */
	public String getMfr() {
		return mfr;
	}

	public void setMfr(String mfr) {
		this.mfr = mfr;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	/** @return 內容物 */
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Crate clean() {
		if (getLength() <= getWidth()) {
			double tmp = getLength();
			length = width;
			width = tmp;
		}

		locationId = location != null ? location.getId() : null;

		return this;
	}
}
