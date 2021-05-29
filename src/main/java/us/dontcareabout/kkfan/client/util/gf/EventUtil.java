package us.dontcareabout.kkfan.client.util.gf;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.chart.client.draw.sprite.SpriteOverEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent;
import com.sencha.gxt.chart.client.draw.sprite.SpriteUpEvent;
import com.sencha.gxt.core.client.util.PrecisePoint;

//Refactory GF
public class EventUtil {
	public static PrecisePoint eventPoint(SpriteSelectionEvent event) {
		return new PrecisePoint(
			eventLayerX(event.getBrowserEvent()),
			eventLayerY(event.getBrowserEvent())
		);
	}

	public static PrecisePoint eventPoint(SpriteUpEvent event) {
		return new PrecisePoint(
			eventLayerX(event.getBrowserEvent()),
			eventLayerY(event.getBrowserEvent())
		);
	}

	public static PrecisePoint eventPoint(SpriteOverEvent event) {
		return new PrecisePoint(
			eventLayerX(event.getBrowserEvent()),
			eventLayerY(event.getBrowserEvent())
		);
	}

	public static PrecisePoint eventPoint(Event event) {
		return new PrecisePoint(
			eventLayerX(event),
			eventLayerY(event)
		);
	}

	public static native double eventLayerX(NativeEvent evt) /*-{
		return evt.layerX || 0;
	}-*/;

	public static native double eventLayerY(NativeEvent evt) /*-{
		return evt.layerY || 0;
	}-*/;
}
