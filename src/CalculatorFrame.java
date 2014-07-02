import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CalculatorFrame extends JFrame implements ActionListener {
	private JPanel pnlUpper = new JPanel();
	private JPanel pnlLower = new JPanel();
	private JTextField txtExpression = new JTextField();
	private JTextField txtX = new JTextField();
	private JTextField txtY = new JTextField();
	private JButton btnCalculate = new JButton("CALCULATE");
	private JComboBox<String> cmbTrig = new JComboBox<String>();

	private String prevExpression;
	private ExpressionParser parser = new ExpressionParser();

	public CalculatorFrame() {
		super("Calculator");

		pnlUpper.setLayout(new GridLayout(0, 3));

		pnlUpper.add(new JLabel("f(x)"));
		pnlUpper.add(new JLabel("="));
		pnlUpper.add(txtExpression);

		pnlUpper.add(new JLabel("X"));
		pnlUpper.add(new JLabel("="));
		pnlUpper.add(txtX);

		txtY.setEditable(false);
		pnlUpper.add(new JLabel("Y"));
		pnlUpper.add(new JLabel("="));
		pnlUpper.add(txtY);

		cmbTrig.addItem("Degree");
		cmbTrig.addItem("Radian");
		cmbTrig.addActionListener(this);
		pnlLower.add(cmbTrig);
		
		btnCalculate.addActionListener(this);
		pnlLower.add(btnCalculate);

		add(pnlUpper, BorderLayout.NORTH);
		add(pnlLower, BorderLayout.SOUTH);
		setSize(220, 160);
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getSource() == btnCalculate) {
			String exp = this.txtExpression.getText();
			String x = this.txtX.getText();

			if (!exp.isEmpty() && !x.isEmpty()) {
				String result;

				if(!exp.equals(prevExpression)){
					parser.setExpression(exp);
					prevExpression = exp;
				}
				try{
					result = new DecimalFormat("#0.00").format(parser.eval(Double.parseDouble(x)));
					this.txtY.setText(result);
				} catch(IllegalArgumentException e){
					this.txtY.setText("Undefined");
				}
			}
		} else {
			if (this.cmbTrig.getSelectedIndex() == 0)
				this.parser.setIsRadian(false);
			if (this.cmbTrig.getSelectedIndex() == 1)
				this.parser.setIsRadian(true);
		}
	}

}
