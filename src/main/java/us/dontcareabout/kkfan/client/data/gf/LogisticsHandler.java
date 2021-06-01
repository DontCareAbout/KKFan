package us.dontcareabout.kkfan.client.data.gf;

import java.util.Date;

import com.google.gwt.event.shared.EventHandler;

public abstract class LogisticsHandler implements EventHandler {
	private Date expire;

	public abstract void onReady(LogisticsEvent event);

	public Date getExpire() {
		return expire;
	}

	//鎖住只讓 LogisticsEvent 才能修改
	void setExpire(Date expire) {
		this.expire = expire;
	}
}
