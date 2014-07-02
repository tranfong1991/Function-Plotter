import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Stack;

public class ExpressionParser {
	private String infixExpression;
	private boolean isRadian = false;
	private ArrayList<String> token;
	private ArrayList<String> postfixTokens;

	public ExpressionParser() {
		this.postfixTokens = new ArrayList<String>();
		this.token = new ArrayList<String>();
	}

	public ExpressionParser(String exp) {
		this();
		this.infixExpression = exp;

		tokenize();
		infixToPostfix();
	}

	public String getExpression() {
		return this.infixExpression;
	}

	public void setExpression(String exp) {
		this.infixExpression = exp;

		if (!this.postfixTokens.isEmpty())
			this.postfixTokens.clear();
		if (!this.token.isEmpty())
			this.token.clear();

		tokenize();
		infixToPostfix();
	}

	public void setIsRadian(boolean b) {
		this.isRadian = b;
	}

	// Decide precedence of each operator
	private int precedence(String operator) {
		switch (operator) {
		case "+":
		case "-":
			return 1;
		case "*":
		case "/":
			return 2;
		default:
			return 3;
		}
	}

	private int compareOps(String op1, String op2) {
		return precedence(op1) - precedence(op2);
	}

	private boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException n) {
			return false;
		}
		return true;
	}

	private boolean isSpecialOps(String str) {
		if (str.equals("sin") || str.equals("cos") || str.equals("tan")
				|| str.equals("cot") || str.equals("ln") || str.equals("log"))
			return true;
		return false;
	}

	private double degreeToRadian(double deg) {
		return deg * Math.PI / 180;
	}

	// Turn an expression into tokens
	private void tokenize() {
		StreamTokenizer st = new StreamTokenizer(new StringReader(
				this.infixExpression));

		// Make '/' readable. Otherwise, it stops at '/'
		st.ordinaryChar('/');
		try {
			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				switch (st.ttype) {
				case StreamTokenizer.TT_NUMBER:
					token.add(String.valueOf(st.nval));
					break;
				case StreamTokenizer.TT_WORD:
					token.add(st.sval);
					break;
				default:
					token.add(String.valueOf((char) st.ttype));
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void infixToPostfix() {
		Stack<String> opStack = new Stack<String>();

		for (int i = 0; i < token.size(); i++) {
			if (isNumeric(token.get(i)) || token.get(i).equals("x"))
				postfixTokens.add(token.get(i));
			else if (token.get(i).equals("("))
				opStack.push(token.get(i));
			else if (token.get(i).equals(")")) {
				while (!opStack.peek().equals("("))
					postfixTokens.add(opStack.pop());
				opStack.pop();
				if(!opStack.isEmpty() && isSpecialOps(opStack.peek()))
					postfixTokens.add(opStack.pop());
			} else if (opStack.isEmpty() || opStack.peek().equals("(")
					|| compareOps(opStack.peek(), token.get(i)) <= 0)
				opStack.push(token.get(i));
			else {
				while (!opStack.isEmpty() && !opStack.peek().equals("(")
						&& compareOps(opStack.peek(), token.get(i)) >= 0)
					postfixTokens.add(opStack.pop());
				opStack.push(token.get(i));
			}
		}

		while (!opStack.isEmpty())
			postfixTokens.add(opStack.pop());
	}

	private double perform(String operator, double num) {
		double trigNum = num;

		if (!this.isRadian)
			trigNum = degreeToRadian(num);

		switch (operator) {
		case "sin":
			return Math.sin(trigNum);
		case "cos":
			return Math.cos(trigNum);
		case "tan":
			if (Math.cos(trigNum) == 0)
				throw new IllegalArgumentException("Angel is divisible by 90.");
			return Math.sin(trigNum) / Math.cos(trigNum);
		case "cot":
			if (Math.sin(trigNum) == 0)
				throw new IllegalArgumentException("Angel is divible by 180.");
			return Math.cos(trigNum) / Math.sin(trigNum);
		case "ln":
			return Math.log(num);
		case "log":
			return Math.log10(num);
		default:
			throw new IllegalArgumentException("Illegal operator.");
		}
	}

	private double perform(String operator, double num1, double num2) {
		switch (operator) {
		case "+":
			return num1 + num2;
		case "-":
			return num1 - num2;
		case "*":
			return num1 * num2;
		case "/":
			if (num2 == 0)
				throw new IllegalArgumentException("Divisor is 0.");
			return num1 / num2;
		case "^":
			return Math.pow(num1, num2);
		default:
			throw new IllegalArgumentException("Illegal Operator.");
		}
	}

	public double eval(double x) {
		Stack<Double> numStack = new Stack<Double>();

		for (int i = 0; i < postfixTokens.size(); i++) {
			if (isNumeric(postfixTokens.get(i)))
				numStack.push(Double.parseDouble(postfixTokens.get(i)));
			else if (postfixTokens.get(i).equals("x"))
				numStack.push(x);
			else if (isSpecialOps(postfixTokens.get(i))) {
				double num = numStack.pop();
				numStack.push(perform(postfixTokens.get(i), num));
			} else {
				try {
					double num2 = numStack.pop();
					double num1 = numStack.pop();
					numStack.push(perform(postfixTokens.get(i), num1, num2));
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return numStack.pop();
	}

	public ArrayList<Double> eval(Interval interval, double steps) {
		ArrayList<Double> numList = new ArrayList<Double>();

		for (double i = interval.start; i <= interval.end; i += steps) {
			numList.add(eval(i));
		}
		return numList;
	}
}
