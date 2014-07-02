import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

enum Orientation {
	HORIZONTAL, VERTICAL;

	public static Orientation getOrien(int i) {
		return i == 1 ? HORIZONTAL : VERTICAL;
	}
}

public class Tick {
	private String label;
	private Line2D.Double tickLine;
	private Point2D.Double startPoint;
	private Orientation orientation;

	public Tick(Point2D.Double startPoint, String label, int orientation) {
		this.label = label;
		this.startPoint = startPoint;
		this.orientation = Orientation.getOrien(orientation);

		generateTickLine();
	}

	public String getLabel() {
		return this.label;
	}

	public Line2D.Double getTickLine() {
		return this.tickLine;
	}

	public Point2D.Double getStartingPoint() {
		return this.startPoint;
	}

	private void generateTickLine() {
		if (this.orientation == Orientation.HORIZONTAL)
			this.tickLine = new Line2D.Double(new Point2D.Double(
					startPoint.x - 3, startPoint.y), startPoint);
		else
			this.tickLine = new Line2D.Double(startPoint,
					new Point2D.Double(startPoint.x, startPoint.y + 3));
	}
}
