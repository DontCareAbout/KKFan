package us.dontcareabout.kkfan.shared.grpah;

public class XY {
	public static final XY ORIGIN = new XY(0, 0);

	private static final String splitter = ",";

	public final double x;
	public final double y;

	public XY(double x, double y) { this.x = x; this.y = y;}
	@Override public String toString() { return x + splitter + y; }

	public static XY valueOf(String string) {
		String value[] = string.split(splitter);

		if (value.length != 2) { return ORIGIN ; }

		double x = 0;
		double y = 0;

		try { x = Double.valueOf(value[0]); } catch(Exception e) {}
		try { y = Double.valueOf(value[1]); } catch(Exception e) {}

		return new XY(x, y);
	}
}
