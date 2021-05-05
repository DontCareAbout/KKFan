package us.dontcareabout.kkfan.client.data.gf;

import java.util.List;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

import us.dontcareabout.kkfan.shared.gf.HasId;

public class Rester<T extends HasId<ID>, ID> {
	private final String name;
	private final ObjectMapper<List<T>> listMapper;
	private final ObjectMapper<T> mapper;

	/**
	 * 搭配 {@link MapperRoom}，不用傳入 mapper 的版本。
	 */
	public Rester(String name) {
		this(name, MapperRoom.getMapper(name), MapperRoom.getListMapper(name));
	}

	public Rester(String name, ObjectMapper<T> mapper, ObjectMapper<List<T>> listMapper) {
		this.name = name;
		this.mapper = mapper;
		this.listMapper = listMapper;
	}

	public void getList(Callback<List<T>> callback) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, name + "/list");
		try {
			builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					callback.onSuccess(listMapper.read(response.getText()));
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onError(exception);
				}
			});
		} catch (RequestException exception) {
			callback.onError(exception);
		}
	}

	public void put(T data, Callback<Integer> callback) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT, name + "/" + data.getId());
		builder.setHeader("Content-Type", "application/json");
		try {
			builder.sendRequest(mapper.write(data), new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					callback.onSuccess(response.getStatusCode());
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onError(exception);
				}
			});
		} catch (RequestException exception) {
			callback.onError(exception);
		}
	}
}