import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.ui.OverlayLayout;

public class PlottingFrame extends JPanel {
	private Graphics2D g2;
	private Axis axisX;
	private Axis axisY;
	private int xTicksNum;
	private int yTicksNum;
	private String expression;
	private Interval interval;
	private Point2D.Double origin;
	private Point2D.Double originLocation;
	private ArrayList<Line2D.Double> lines;
	private ArrayList<Point2D.Double> points;
	private ArrayList<Point2D.Double> pointsLocations;
	private ExpressionParser parser = new ExpressionParser();

	public PlottingFrame(int width, int height) {
		LayoutManager layout = new OverlayLayout();
		setLayout(layout);
		setSize(width, height);

		this.xTicksNum = 11;
		this.yTicksNum = 11;
		this.expression = null;
		this.interval = new Interval();
		this.origin = new Point2D.Double(0, 0);
		this.originLocation = new Point2D.Double(55, height);
		this.lines = new ArrayList<Line2D.Double>();
		this.points = new ArrayList<Point2D.Double>();
		this.pointsLocations = new ArrayList<Point2D.Double>();
		this.axisX = new Axis(new Point2D.Double(55, height), width - 10,
				height - 30, 1);
		this.axisY = new Axis(new Point2D.Double(55, 30), height - 30,
				width - 10, -1);
	}

	public void setNumTicksX(int num) {
		this.xTicksNum = num;
		this.axisX.setNumTicks(num);
		clearPoints();
	}

	public void setNumTicksY(int num) {
		this.yTicksNum = num;
		this.axisY.setNumTicks(num);
	}

	public void setExpression(String exp) {
		this.expression = exp;
		clearPoints();
	}

	public void setInterval(double start, double end) {
		this.interval = new Interval(start, end);
		clearPoints();
	}
	
	public void setIsRadian(boolean b){
		this.parser.setIsRadian(b);
	}
	
	public void repaintXAxis(){
		this.axisX.repaint();
	}
	
	public void repaintYAxis(){
		this.axisY.repaint();
	}
	
	private void clearPoints(){
		this.lines.clear();
		this.points.clear();
		this.pointsLocations.clear();
	}

	private void generatePoints() {
		if (this.expression != null) {
			this.parser.setExpression(this.expression);
			
			double steps = (this.interval.getDistance() + 1) / this.xTicksNum;
			ArrayList<java.lang.Double> result = this.parser.eval(this.interval, steps);
			double maxY = result.get(0);
			double minY = result.get(0);

			for (int i = 0; i < result.size(); i++) {
				this.points.add(new Point2D.Double(this.interval.start + i
						* steps, result.get(i)));
				if (result.get(i) > maxY)
					maxY = result.get(i);
				if (result.get(i) < minY)
					minY = result.get(i);
			}

			this.origin = new Point2D.Double(this.interval.start, minY);
			this.axisX.setInterval(this.interval.start, this.interval.end);
			this.axisX.setNumTicks(xTicksNum);
			this.axisY.setInterval(minY, maxY);
			this.axisY.setNumTicks(yTicksNum);
		}
	}

	private void generatePointsLocations() {
		if (!this.points.isEmpty()) {
			double locX;
			double locY;
			double unitDistX = axisX.getUnitDistance();
			double unitDistY = axisY.getUnitDistance();
			double unitLengthX = axisX.getUnitLength();
			double unitLengthY = axisY.getUnitLength();

			for (int i = 0; i < this.points.size(); i++) {
				locX = originLocation.x + (points.get(i).x - origin.x)
						/ unitDistX * unitLengthX;
				locY = originLocation.y - (points.get(i).y - origin.y)
						/ unitDistY * unitLengthY;
				pointsLocations.add(new Point2D.Double(locX, locY));
			}
		}
	}

	private void generateLines() {
		if (!pointsLocations.isEmpty()) {
			for (int i = 0; i < pointsLocations.size(); i++) {
				if (i + 1 != pointsLocations.size())
					lines.add(new Line2D.Double(pointsLocations.get(i),
							pointsLocations.get(i + 1)));
			}
		}
	}

	public void reset() {
		this.expression = null;
		this.axisX.setInterval(0, 10);
		this.axisY.setInterval(0, 10);
		this.axisX.setNumTicks(11);
		this.axisY.setNumTicks(11);
		clearPoints();
		
		revalidate();
		repaint();
	}

	public void plot() {
		generatePoints();
		generatePointsLocations();
		generateLines();
		add(axisX);
		add(axisY);
		
		revalidate();
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;

		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(55, 30, (int) axisX.getAxisLength(),
				(int) axisY.getAxisLength());

		g2.setColor(Color.RED);
		for (int i = 0; i < lines.size(); i++) {
			g2.draw(lines.get(i));
		}
	}
}
