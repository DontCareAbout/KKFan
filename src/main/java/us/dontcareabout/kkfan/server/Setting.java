package us.dontcareabout.kkfan.server;

import us.dontcareabout.java.common.DoubleProperties;

public class Setting extends DoubleProperties {
	private static Setting instance = new Setting();

	private Setting() {
		super("config.xml", "kkfan.xml");
	}

	public static String workspace() {
		return instance.getProperty("workspace");
	}
}
