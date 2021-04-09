package us.dontcareabout.kkfan.client.layer.gf;

import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOutEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOutEvent.SpriteOutHandler;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOverEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOverEvent.SpriteOverHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import us.dontcareabout.gxt.client.draw.LayerSprite;

/**
 * 注意：child class 在設定完 {@link #tipConfig} 之後必須呼叫 {@link #refreshTip()} 才能正常顯示內容。
 */
public 	class LayerSpriteWithTip extends LayerSprite {
	protected ToolTipConfig tipConfig = new ToolTipConfig();

	private ToolTip tip = new ToolTip(tipConfig);

	public LayerSpriteWithTip() {
		addSpriteOverHandler(new SpriteOverHandler() {
			@Override
			public void onSpriteOver(SpriteOverEvent event) {
				showTip(event);
			}
		});
		addSpriteOutHandler(new SpriteOutHandler() {
			@Override
			public void onSpriteLeave(SpriteOutEvent event) {
				tip.hide();
			}
		});
	}

	@Override
	public void setBgStrokeColor(Color color) {
		throw new UnsupportedOperationException("Tooltip can't work correctly when setting background's stroke.");
	}

	@Override
	public void setBgStrokeWidth(double width) {
		throw new UnsupportedOperationException("Tooltip can't work correctly when setting background's stroke.");
	}

	public void refreshTip() {
		tip.update(tipConfig);
	}

	/**
	 * 設計成 protected 是為了能讓 child class 可以 override，
	 * 以便在顯示 {@link ToolTip} 之前有機會呼叫 {@link #refreshTip()} 好變更內容。
	 */
	protected void showTip(SpriteOverEvent event) {
		tip.showAt(
			event.getBrowserEvent().getClientX() + tip.getToolTipConfig().getMouseOffsetX(),
			event.getBrowserEvent().getClientY() + tip.getToolTipConfig().getMouseOffsetY()
		);
	}
}