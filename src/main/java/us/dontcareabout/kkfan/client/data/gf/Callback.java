package us.dontcareabout.kkfan.client.data.gf;

public interface Callback<T> {
	void onSuccess(T data);
	void onError(Throwable exception);
}