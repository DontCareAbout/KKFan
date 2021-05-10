package us.dontcareabout.kkfan.client.util.gf;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

import us.dontcareabout.gxt.client.model.GetValueProvider;

public class ColumnConfigBuilder<T, V> {
	private static SafeStyles centerStyle = SafeStylesUtils.fromTrustedString("display:flex;justify-content:center;align-items:center;padding:1px;");

	private final ColumnConfig<T, V> result;

	private Integer width;
	private String header;
	private Cell<V> cell;
	private SafeStyles textStyles;

	public ColumnConfigBuilder(ValueProvider<T, V> vp) {
		 result = new ColumnConfig<>(vp);
	}

	/**
	 * @param constant {@link ValueProvider#getValue(Object)} 的固定回傳值
	 */
	public ColumnConfigBuilder(V constant) {
		this(new GetValueProvider<T, V>() {
			@Override
			public V getValue(T object) {
				return constant;
			}
		});
	}

	public ColumnConfig<T, V> build() {
		if (width != null) { result.setWidth(width); }
		if (header != null) { result.setHeader(header); }
		if (cell != null) { result.setCell(cell); }
		if (textStyles != null) { result.setColumnTextStyle(textStyles); }
		return result;
	}

	public ColumnConfigBuilder<T, V> centerStyle() {
		return setTextStyles(centerStyle);
	}

	// ==== 單純 setter 區 ==== //
	public ColumnConfigBuilder<T, V> setWidth(int width) {
		this.width = width;
		return this;
	}

	public ColumnConfigBuilder<T, V> setHeader(String header) {
		this.header = header;
		return this;
	}

	public ColumnConfigBuilder<T, V> setCell(Cell<V> cell) {
		this.cell = cell;
		return this;
	}

	public ColumnConfigBuilder<T, V> setTextStyles(SafeStyles textStyles) {
		this.textStyles = textStyles;
		return this;
	}
}
