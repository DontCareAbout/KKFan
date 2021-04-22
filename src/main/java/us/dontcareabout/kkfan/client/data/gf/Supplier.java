package us.dontcareabout.kkfan.client.data.gf;

import java.util.Date;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

public abstract class Supplier<D, H extends EventHandler> {
	private D data;
	private Date expire;

	public abstract String name();
	public abstract Type<H> eventType();

	//TODO 考慮改成自訂 event type，可以夾帶 expire 好讓 handler 可以決定要不要處理
	public abstract GwtEvent<H> genEvent();

	/**
	 * 抓取資料的實做 method。在資料抓取完畢後請呼叫 {@link #ready(Object)}。
	 */
	public abstract void fetch();

	/**
	 * @return 若回傳 null 表示 {@link #tally()} 一定會實際執行 {@link #fetch()}。
	 * @see #latterDate(int)
	 */
	public abstract Date nextExpire();

	/**
	 * 判斷是否能提供所需資料，
	 * 若可以則呼叫 {@link #fireEvent()}，否則呼叫 {@link #fetch()}。
	 */
	public void tally() {
		if (data == null || expire == null) {
			fetch();
			return;
		}

		if (new Date().before(expire)) {
			fireEvent();
			return;
		}

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
				Logistics.fireEvent(genEvent());
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