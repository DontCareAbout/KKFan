package us.dontcareabout.kkfan.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

import us.dontcareabout.kkfan.shared.vo.Crate;

public interface HtmlTemplate extends XTemplates {
	static HtmlTemplate tplt = GWT.create(HtmlTemplate.class);

	@XTemplate(source = "CrateTipTitle.html")
	SafeHtml crateTipTitle(Crate crate);

	@XTemplate(source = "CrateTipBody.html")
	SafeHtml crateTipBody(Crate crate);
}
