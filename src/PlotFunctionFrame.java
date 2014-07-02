import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PlotFunctionFrame extends JFrame implements ActionListener{
	private JPanel pnlUpper = new JPanel();
	private JPanel pnlLower = new JPanel();
	private JTextField txtFunction = new JTextField();
	private JTextField txtStart = new JTextField();
	private JTextField txtEnd = new JTextField();
	private JTextField txtXTicks = new JTextField();
	private JTextField txtYTicks = new JTextField();
	private JButton btnPlot = new JButton("PLOT");
	private JComboBox<String> cmbTrig = new JComboBox<String>();
	
	public PlottingFrame pFrame;

	public PlotFunctionFrame(){
		super("Plot Function");
		
		pnlUpper.setLayout(new GridLayout(0, 3));
		
		pnlUpper.add(new JLabel("f(x)"));
		pnlUpper.add(new JLabel("="));
		pnlUpper.add(txtFunction);
		
		pnlUpper.add(new JLabel("Start"));
		pnlUpper.add(new JLabel("="));
		pnlUpper.add(txtStart);
		
		pnlUpper.add(new JLabel("End"));
		pnlUpper.add(new JLabel("="));
		pnlUpper.add(txtEnd);
		
		pnlUpper.add(new JLabel("X Ticks"));
		pnlUpper.add(new JLabel("="));
		pnlUpper.add(txtXTicks);
		
		pnlUpper.add(new JLabel("Y Ticks"));
		pnlUpper.add(new JLabel("="));
		pnlUpper.add(txtYTicks);

		cmbTrig.addItem("Degree");
		cmbTrig.addItem("Radian");
		cmbTrig.addActionListener(this);
		pnlLower.add(cmbTrig);
		
		btnPlot.addActionListener(this);
		pnlLower.add(btnPlot);

		add(pnlUpper, BorderLayout.NORTH);
		add(pnlLower, BorderLayout.SOUTH);
		setSize(280, 200);
	}
	
	public void clearEntry(){
		this.txtStart.setText(null);
		this.txtEnd.setText(null);
		this.txtXTicks.setText(null);
		this.txtYTicks.setText(null);
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getSource() == btnPlot){
			String function = this.txtFunction.getText();
			String start = this.txtStart.getText();
			String end = this.txtEnd.getText();
			String xTicks = this.txtXTicks.getText();
			String yTicks = this.txtYTicks.getText();
			
			if(!function.isEmpty()){
				this.pFrame.setExpression(function);
				if (!start.isEmpty() && !end.isEmpty()) {
					this.pFrame.setInterval(Double.parseDouble(start),
							Double.parseDouble(end));
					if(!xTicks.isEmpty())
						this.pFrame.setNumTicksX(Integer.parseInt(xTicks));
					if(!yTicks.isEmpty())
						this.pFrame.setNumTicksY(Integer.parseInt(yTicks));
				}
			}
			this.pFrame.plot();
			this.setVisible(false);
		} else {
			if (this.cmbTrig.getSelectedIndex() == 0)
				this.pFrame.setIsRadian(false);
			if (this.cmbTrig.getSelectedIndex() == 1)
				this.pFrame.setIsRadian(true);
		}
	}
}
