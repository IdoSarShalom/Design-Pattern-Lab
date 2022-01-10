package test;

public class BinaryExpression implements Expression{
	protected Expression left; 
	protected Expression right;
	
	
	public BinaryExpression(Expression left, Expression right) {
		this.left = left; 
		this.right = right;
	}
	
	
	@Override
	public double calculate() {	// the inherited classes override this method 
		return 0;
	}
	
}
