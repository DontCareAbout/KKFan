package us.dontcareabout.kkfan.client.data.gf;

import java.util.HashMap;
import java.util.List;

import com.github.nmorel.gwtjackson.client.ObjectMapper;

public class MapperRoom {
	private static HashMap<String, ObjectMapper<?>> mapper = new HashMap<>();
	private static HashMap<String, ObjectMapper<List<?>>> mapperL = new HashMap<>();

	public static void join(String name, ObjectMapper<?> entityMapper, ObjectMapper<List<?>> listMapper) {
		mapper.put(name, entityMapper);
		mapperL.put(name, listMapper);
	}

	@SuppressWarnings("unchecked")
	public static <T> ObjectMapper<T> getMapper(String name) {
		return (ObjectMapper<T>)mapper.get(name);
	}

	@SuppressWarnings("unchecked")
	public static <T extends List<?>> ObjectMapper<T> getListMapper(String name) {
		return (ObjectMapper<T>)mapperL.get(name);
	}
}
