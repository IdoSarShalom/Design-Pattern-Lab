package test;

import java.util.LinkedList;
import java.util.Stack;

public class Q3 {

	public static double calc(String equation) {

		LinkedList<String> queue = new LinkedList<String>(); // dijkstra queue
		Stack<String> stack = new Stack<String>(); // stack
		Boolean multiDigitFlag = false;

		for (int i = 0; i < equation.length(); i++) {

			char input = equation.charAt(i);

			if (Character.isDigit(input)) { // number 0-9

				if (multiDigitFlag == false) {
					queue.add((String) (input + "")); // put in queue
					multiDigitFlag = true;
				} else { // multidigit number

					// concatenate the next digit
					String str = queue.removeLast(); // remove the recent element added to queue queue

					str = str + (String) (input + "");

					queue.add(str);
				}
			} else
				multiDigitFlag = false;

			if (input == '+' || input == '-' || input == '*' || input == '/') { // operator

				while (true) {
					
					if (stack.isEmpty() == false) {
						
						String stackPeek = (String) stack.peek();
						
						if (stackPeek.equals("+") || stackPeek.equals("-") || stackPeek.equals("*") ||stackPeek.equals("/")) {
							// the top of the stack is an operator 
							
							if(input == '+' || input == '-') { // the input has the lowest precedence
								
								// pop the stackPeek from stack onto queue
								String element = (String) stack.pop();
								queue.add(element);
							}
							
							if(input == '*' || input == '/') { 
								// only if stackPeek is * or / than pop the stackPeek from stack onto queue
								
								if (stackPeek.equals("*") || stackPeek.equals("/")) {
									
									// pop the stackPeek from stack onto queue
									String element = (String) stack.pop();
									queue.add(element);				
								} else { // the top of the stack is + or - and doesnt have precedence 
									
									// push input onto stack
									stack.push((String) (input + ""));
									break;									
								}								
							}
							
							
						}else { // the top of the stack is not an operator 
							// push input onto stack
							stack.push((String) (input + ""));
							break;	
						}
									
					} else { // stack is empty 
						// push input onto stack
						stack.push((String) (input + ""));
						break;			
					}
					
				}

			} // closure of input operator case

			if (input == '(')
				stack.push((String) (input + ""));

			if (input == ')') { // assume before this char the right bracket "(" got into the stack

				if (stack.contains("(") == true) {

					String stackPeek = (String) stack.peek();

					while (stackPeek.equals("(") == false) {

						// pop the stackPeek from stack onto queue
						String element = (String) stack.pop();
						queue.add(element);

						// update stackPeek variable

						stackPeek = (String) stack.peek();

					}
					stack.pop(); // pop "(" from the stack
				}
			}

			if (input == '.') {

				// concatenate the dot to the number
				String str = queue.removeLast(); // remove the recent element added to queue queue

				str = str + (String) (input + "");

				queue.add(str);

				multiDigitFlag = true; // multidigit number !
			}

		} // for loop bracket closer

		while (stack.isEmpty() == false) {

			String stackPeek = (String) stack.peek();

			// if there's an operator at the top of the stack
			if (stackPeek.equals("+") || stackPeek.equals("-") || stackPeek.equals("*") || stackPeek.equals("/")) {
				// pop operator from stack onto queue
				String element = (String) stack.pop();
				queue.add(element);
			}

		}

		Stack<Expression> postfixStack = new Stack<Expression>(); // postfix algorithm auxiliary stack

		for (String str : queue) {

			if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/")) { // operator

				// pop two elements from stack
				Number right = (Number) postfixStack.pop();
				Number left = (Number) postfixStack.pop();
				// BinaryExpression exp = new BinaryExpression(left, right);
				double result;

				switch (str) {
				case "+":
					Plus p = new Plus((Number) left, (Number) right);
					result = p.calculate();
					break;
				case "-":
					Minus m = new Minus((Number) left, (Number) right);
					result = m.calculate();
					break;
				case "*":
					Mul mu = new Mul(left, right);
					result = mu.calculate();
					break;
				default: // case "/"
					Div d = new Div(left, right);
					result = d.calculate();
				}

				// round the result by three decimal places
				result = result * 1000;
				result = Math.round(result);
				result = result / 1000.0;

				postfixStack.push(new Number(result)); // push to stack

			} else // operand, push it to stack
				postfixStack.push(new Number(Double.parseDouble(str)));

		}

		double finalResult = ((Number) postfixStack.pop()).getNumber();
		return finalResult;

	} // calc method bracket closer

} // Q3 class bracket closer

