package us.dontcareabout.kkfan.shared.vo;

public enum LocationType {
	/** 庫房 */ storage,
	/** 展廳 */ gallery,
	/** 館內某處 */ somewhere,
	/** 外出中 */ trip,
	/** 尚未入賬 */ unborn,
	;

	public static boolean isMapType(LocationType type) {
		//somewhere 目前也沒辦法畫在地圖上，所以改成正面表列... Orz
		return type == storage || type == gallery;
	}
}
