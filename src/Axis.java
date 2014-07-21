import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import javax.swing.JComponent;

public class Axis extends JComponent {
	private final Color TICK_LINE_COLOR = Color.BLACK;
	private final Color DASH_LINE_COLOR = Color.GRAY;
	// Dash line settings
	private final float[] DASH = { 2f, 0f, 2f };
	private final BasicStroke DASH_STROKE = new BasicStroke(1,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, DASH, 2f);

	private int ticksNum;
	private double axisLength;
	private double dashLength;
	private double unitLength;
	private double unitDistance;
	private Graphics2D g2;
	private Tick[] ticks;
	private Interval interval;
	private Point2D.Double startPoint;
	private Line2D.Double axisLine;
	private Orientation orientation;

	public Axis(Point2D.Double startPoint, double axisLength,
			double dashLength, int orientation) {
		this.ticksNum = 11;
		this.interval = new Interval();
		this.ticks = new Tick[11];
		this.startPoint = startPoint;
		this.axisLength = axisLength;
		this.dashLength = dashLength;
		this.unitDistance = 1;
		this.unitLength = axisLength / 10;
		this.orientation = Orientation.getOrien(orientation);
		generateAxis();
	}

	public Axis(Point2D.Double startPoint, double axisLength,
			double dashLength, Interval interval, int ticksNum,
			int orientation) {
		this.ticksNum = ticksNum;
		this.interval = interval;
		this.startPoint = startPoint;
		this.axisLength = axisLength;
		this.dashLength = dashLength;
		this.ticks = new Tick[ticksNum];
		this.unitLength = axisLength / (ticksNum - 1);
		this.unitDistance = interval.getDistance() / (ticksNum - 1);
		this.orientation = Orientation.getOrien(orientation);
		generateAxis();
	}

	public int getNumTicks() {
		return this.ticksNum;
	}

	public double getAxisLength() {
		return this.axisLength;
	}

	public double getDashLength() {
		return this.dashLength;
	}

	public double getUnitLength() {
		return this.unitLength;
	}

	public double getUnitDistance() {
		return this.unitDistance;
	}

	public Interval getInterval() {
		return this.interval;
	}

	public void setNumTicks(int num) {
		this.ticksNum = num;
		this.ticks = new Tick[num];
		this.unitLength = axisLength / (num - 1);
		this.unitDistance = this.interval.getDistance() / (num - 1);
	}

	public void setAxisLength(double length) {
		this.axisLength = length;
	}

	public void setDashLength(double length) {
		this.dashLength = length;
	}

	public void setUnitLength(double length) {
		this.unitLength = length;
	}

	public void setUnitDistance(double dist) {
		this.unitDistance = dist;
	}

	public void setInterval(double start, double end) {
		this.interval = new Interval(start, end);
	}

	private void generateAxis() {
		if (this.orientation == Orientation.HORIZONTAL)
			axisLine = new Line2D.Double(startPoint, new Point2D.Double(
					startPoint.x + this.axisLength, startPoint.y));
		else
			axisLine = new Line2D.Double(startPoint, new Point2D.Double(
					startPoint.x, startPoint.y + this.axisLength));
	}

	private void generateTicks() {
		int index = 0;
		String numStr;

		for (double i = interval.start; index < ticks.length; i += unitDistance, index++) {
			if (i >= 1000 || i <= -1000)
				numStr = new DecimalFormat("0.##E0").format(i);
			else
				numStr = new DecimalFormat("0.00").format(i);

			if (this.orientation == Orientation.HORIZONTAL)
				ticks[index] = new Tick(new Point2D.Double(startPoint.x
						+ index * unitLength, startPoint.y), numStr, -1);
			else
				ticks[index] = new Tick(new Point2D.Double(startPoint.x,
						axisLength + startPoint.y - index * unitLength),
						numStr, 1);
		}

	}

	private void drawTickLine() {
		g2.setColor(TICK_LINE_COLOR);

		for (int i = 0; i < ticks.length; i++) {
			g2.draw(ticks[i].getTickLine());
			if (this.orientation == Orientation.HORIZONTAL)
				g2.drawString(ticks[i].getLabel(),
						(int) ticks[i].getStartingPoint().x,
						(int) ticks[i].getStartingPoint().y + 35);
			else
				g2.drawString(ticks[i].getLabel(),
						(int) ticks[i].getStartingPoint().x - 45,
						(int) ticks[i].getStartingPoint().y);
		}
	}

	private void drawDashLine() {
		g2.setStroke(DASH_STROKE);
		g2.setColor(DASH_LINE_COLOR);

		Line2D.Double dashLine;
		for (int i = 1; i < ticks.length; i++) {
			if (this.orientation == Orientation.HORIZONTAL)
				dashLine = new Line2D.Double(ticks[i].getStartingPoint().x,
						ticks[i].getStartingPoint().y - this.dashLength,
						ticks[i].getStartingPoint().x,
						ticks[i].getStartingPoint().y);
			else
				dashLine = new Line2D.Double(ticks[i].getStartingPoint().x,
						ticks[i].getStartingPoint().y,
						ticks[i].getStartingPoint().x + this.dashLength,
						ticks[i].getStartingPoint().y);
			g2.draw(dashLine);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;

		g2.draw(axisLine);
		generateTicks();
		drawTickLine();
		drawDashLine();
	}
}
