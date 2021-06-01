package us.dontcareabout.kkfan.client.data.gf;

import java.util.Date;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

public abstract class Supplier<D> {
	private SimpleEventBus eventBus = new SimpleEventBus();
	private boolean fetchLock;
	private D data;
	private Date expire;

	public abstract String name();

	/**
	 * 抓取資料的實做 method。在資料抓取完畢後請呼叫 {@link #ready(Object)}。
	 */
	public abstract void fetch();

	/**
	 * @return 若回傳 null 表示 {@link #tally()} 一定會實際執行 {@link #fetch()}。
	 * @see #latterDate(int)
	 */
	public abstract Date nextExpire();

	public HandlerRegistration addHandler(LogisticsHandler handler) {
		return eventBus.addHandler(LogisticsEvent.TYPE, handler);
	}

	/**
	 * 判斷是否能提供所需資料，
	 * 若可以則呼叫 {@link #fireEvent()}，否則呼叫 {@link #fetch()}。
	 */
	public void tally() {
		if (fetchLock) { return; }

		if (data == null || expire == null) {
			tallyForced();
			return;
		}

		if (new Date().before(expire)) {
			fireEvent();
			return;
		}

		tallyForced();
	}

	/** 強制呼叫 {@link #fetch()} */
	public void tallyForced() {
		fetchLock = true;
		fetch();
	}

	public D getData() {
		return data;
	}

	public Date getExpire() {
		return expire;
	}

	protected void ready(D data) {
		this.data = data;
		fetchLock = false;
		expire = nextExpire();
		fireEvent();
	}

	private void fireEvent() {
		//避免在 event handle 流程中有 add handler + 觸發 event 的狀況
		//（會產生有 handler 沒有觸發的偽靈異現象）
		//所以用了 scheduleFinally() 的手段
		Scheduler.get().scheduleFinally(new ScheduledCommand() {
			@Override
			public void execute() {
				eventBus.fireEvent(new LogisticsEvent(expire));
			}
		});
	}

	/**
	 * @return 目前時間後 n 秒的時間
	 */
	public static Date latterDate(int second) {
		return new Date(new Date().getTime() + second * 1000);
	}
}