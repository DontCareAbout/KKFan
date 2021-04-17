package us.dontcareabout.kkfan.shared.vo;

public enum LocationType {
	/** 庫房 */ storage,
	/** 展廳 */ gallery,
	/** 館內某處 */ somewhere,
	/** 外出中 */ trip,
	/** 尚未入賬 */ unborn,
	;

	/** @return 是否為地圖上的類型 */
	public static boolean isMapTypen(LocationType type) {
		return type == storage || type == gallery || type == somewhere;
	}
}
