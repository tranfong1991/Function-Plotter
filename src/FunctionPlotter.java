import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FunctionPlotter extends JFrame implements ActionListener, ChangeListener {
	private final String ABOUT = "Function Plotter v1.0"
					+ "\nCreated by: Andy Tran " 
					+ "\nTexas A&M University"
					+ "\nGitHub: github.com/tranfong1991"
					+ "\nLinkedIn: linkedin.com/in/andyntran"
					+ "\nEmail: andytran@AggieNetwork.com";
	
	private JMenuBar mnb = new JMenuBar();
	private JMenu mnuActions = new JMenu("Actions");
	private JMenu mnuEdit = new JMenu("Edit");
	private JMenu mnuAbout = new JMenu("About");
	private JMenuItem mniPlotFunc = new JMenuItem("Plot Function");
	private JMenuItem mniCalculator = new JMenuItem("Calculator");
	private JMenuItem mniExit = new JMenuItem("Exit");
	private JMenuItem mniClear = new JMenuItem("Clear");
	private JMenuItem mniSetInterval = new JMenuItem("Set Interval");
	private JMenuItem mniAbout = new JMenuItem("About");
	private JSlider sldX = new JSlider(JSlider.HORIZONTAL, 2, 50, 26);
	private JSlider sldY = new JSlider(JSlider.VERTICAL, 2, 50, 26);
	private ImageIcon imiBig = new ImageIcon("Images/function_plotter_big.png");
	private ImageIcon imiSmall = new ImageIcon("Images/function_plotter_small.png");

	private PlotFunctionFrame pfFrame = new PlotFunctionFrame();
	private CalculatorFrame cFrame = new CalculatorFrame();
	private SetIntervalFrame siFrame = new SetIntervalFrame();
	private PlottingFrame pFrame;

	public FunctionPlotter(int width, int height) {
		super("Function Plotter");
		setSize(width, height);
		setIconImage(imiSmall.getImage());
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pfFrame.setIconImage(imiSmall.getImage());
		pfFrame.setVisible(false);

		cFrame.setIconImage(imiSmall.getImage());
		cFrame.setVisible(false);

		siFrame.setIconImage(imiSmall.getImage());
		siFrame.setVisible(false);

		sldX.addChangeListener(this);
		sldX.setMajorTickSpacing(10);
		sldX.setMinorTickSpacing(1);
		sldX.setPaintTicks(true);
		sldX.setPaintLabels(true);

		sldY.addChangeListener(this);
		sldY.setMajorTickSpacing(10);
		sldY.setMinorTickSpacing(1);
		sldY.setPaintTicks(true);
		sldY.setPaintLabels(true);

		mniPlotFunc.addActionListener(this);
		mniCalculator.addActionListener(this);
		mniExit.addActionListener(this);
		mnuActions.add(mniPlotFunc);
		mnuActions.add(mniCalculator);
		mnuActions.add(mniExit);
		mnb.add(mnuActions);

		mniClear.addActionListener(this);
		mniSetInterval.addActionListener(this);
		mnuEdit.add(mniClear);
		mnuEdit.add(mniSetInterval);
		mnb.add(mnuEdit);

		mniAbout.addActionListener(this);
		mnuAbout.add(mniAbout);
		mnb.add(mnuAbout);

		pFrame = new PlottingFrame(width - 150, height - 150);
		pFrame.plot();

		add(mnb, BorderLayout.NORTH);
		add(sldX, BorderLayout.SOUTH);
		add(sldY, BorderLayout.EAST);
		add(pFrame);
		validate();
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		Object source = a.getSource();
		if (source == mniPlotFunc) {
			pfFrame.setLocation(getWidth() / 2 + getLocation().x - 140,
					getHeight() / 2 + getLocation().y - 100);
			pfFrame.clearEntry();
			pfFrame.pFrame = this.pFrame;
			pfFrame.setVisible(true);
		} else if (source == mniCalculator) {
			cFrame.setLocation(
					getWidth() / 2 + getLocation().x - 110, getHeight() / 2
							+ getLocation().y - 80);
			cFrame.setVisible(true);
		}
		if (source == mniExit) {
			System.exit(0);
		} else if (source == mniClear) {
			pFrame.reset();
		} else if (source == mniSetInterval) {
			siFrame.setLocation(getWidth() / 2 + getLocation().x - 110,
					getHeight() / 2 + getLocation().y - 90);
			siFrame.clearEntry();
			siFrame.pFrame = this.pFrame;
			siFrame.setVisible(true);
		} else if (source == mniAbout) {
			JOptionPane.showMessageDialog(this, ABOUT, "About",
					JOptionPane.INFORMATION_MESSAGE, imiBig);
		}
	}

	@Override
	public void stateChanged(ChangeEvent a) {
		Object source = a.getSource();
		if (source == sldX) {
			pFrame.setNumTicksX(sldX.getValue());
			pFrame.plot();
		} else {
			pFrame.setNumTicksY(sldY.getValue());
			pFrame.repaintYAxis();
		}
	}
}
