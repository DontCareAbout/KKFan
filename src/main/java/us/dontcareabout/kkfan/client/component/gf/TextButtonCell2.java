package us.dontcareabout.kkfan.client.component.gf;

import com.sencha.gxt.cell.core.client.TextButtonCell;

/**
 * {@link TextButtonCell} 的替代品，差異如下：
 *
 * <ul>
 * 	<li>可控制 {@link #handlesSelection()} 的回傳值，預設值改為 true</li>
 * <ul>
 */
public class TextButtonCell2 extends TextButtonCell {
	private boolean handlesSelection = true;

	public boolean isHandlesSelection() {
		return handlesSelection;
	}

	public void setHandlesSelection(boolean handlesSelection) {
		this.handlesSelection = handlesSelection;
	}

	@Override
	public boolean handlesSelection() {
		return isHandlesSelection();
	}
}
