package us.dontcareabout.kkfan.shared.vo;

public enum LocationType {
	/** 庫房 */ storage,
	/** 展廳 */ gallery,
	/** 館內某處 */ somewhere,
	/** 外出中 */ trip,
	/** 尚未入賬 */ unborn,
	;

	public static boolean isMapType(LocationType type) {
		return type != trip && type != unborn;
	}
}
