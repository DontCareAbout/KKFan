package us.dontcareabout.kkfan.shared.vo;

import java.util.Date;

/**
 * 「箱子」的對應 VO。
 * <p>
 * 若 {@link #getDeleteTime()} 不為 null 表示該筆資料已經刪除。
 */
public class Crate {
	private long id;

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
	private Location location;
	private String item;
	////

	private String note;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
}
