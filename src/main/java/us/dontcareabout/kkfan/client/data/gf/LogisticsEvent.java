package us.dontcareabout.kkfan.client.data.gf;

import java.util.Date;

import com.google.gwt.event.shared.GwtEvent;

public class LogisticsEvent extends GwtEvent<LogisticsHandler>{
	public static final Type<LogisticsHandler> TYPE = new Type<>();

	public final Date expire;

	public LogisticsEvent(Date expire) {
		this.expire = expire;
	}

	@Override
	public Type<LogisticsHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected final void dispatch(LogisticsHandler handler) {
		//也不用費事比較時間先後，只要不一樣就是過期啦 XD
		if (expire.equals(handler.getExpire())) { return; }

		handler.setExpire(expire);
		handler.onReady(this);
	}
}
