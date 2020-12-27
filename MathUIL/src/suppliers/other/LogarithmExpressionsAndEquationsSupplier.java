package suppliers.other;

import static problems.Prettifier.*;
import static problems.Problem.*;
import static suppliers.NamedIntRange.*;

import math.*;
import problems.*;
import suppliers.*;
import utils.IntList;

/**
 * @author Sam Hooper
 *
 */
public class LogarithmExpressionsAndEquationsSupplier extends SettingsProblemSupplier {
	
	private static final RangeStore BASES = RangeStore.of(2, 16), VALUES = RangeStore.of(0, 7, 0, 4);
	
	private final NamedIntRange bases = of(BASES, "Bases of logarithms"), values = of(VALUES, "Values of logarithm expressions");
	
	public LogarithmExpressionsAndEquationsSupplier() {
		settings(bases, values);
	}
	
	@Override
	public Problem get() {
		if(Math.random() <= 0.5)
			return expressionProblem();
		else
			return equationProblem();
	}
	
	/**
	 * The types of expression problems are: simple evaluation of log, adding two log terms with the same base, subtracting two log terms with the same base.
	 */
	private Problem expressionProblem() {
		int ran = RAND.nextInt(3);
		int base = intInclusive(bases);
		if(ran == 0) { //simple evaluation
			int exponent = intInclusive(values);
			long result = Utils.pow(base, exponent);
			return Builder.of(ensureMath(log(num(base), num(result)) + op('='))).addResult(new Complex(exponent)).build();
		}
		else if(ran == 1) { //adding two log terms with the same base
			int exponent = intInclusive(values);
			int result = Math.toIntExact(Utils.pow(base, exponent));
			IntList factors = Utils.factorsUnsorted(result);
			int factor1 = factors.get(intExclusive(factors.size())), factor2 = result / factor1;
			return Builder.of(ensureMath(log(num(base), num(factor1)) + op('+') + log(num(base), num(factor2)) + op('='))).addResult(new Complex(exponent)).build();
		}
		else {
			int exponent = intInclusive(values);
			int result = Math.toIntExact(Utils.pow(base, exponent));
			int b = intInclusive(2, 10);
			int arg1 = result * b, arg2 = b;
			return Builder.of(ensureMath(log(num(base), num(arg1)) + op('-') + log(num(base), num(arg2)) + op('='))).addResult(new Complex(exponent)).build();
		}
	}
	
	private Problem equationProblem() {
		int base = intInclusive(bases), exponent = intInclusive(values), logArg = Math.toIntExact(Utils.pow(base, exponent));
		char variable = (char) intInclusive('a', 'z');
		if("eilo".contains(Character.toString(variable))) variable = 'x'; //those are ambiguous letters or letters that have other meanings.
		if(Math.random() <= 0.5) { //the base is a variable
			if(exponent == 0) { //if the exponent is zero, the variable could have multiple values - consider "log base ? of 1 = 0", ? could be several values.
				exponent = intInclusive(1, Math.max(1, values.high()));
				logArg = Math.toIntExact(Utils.pow(base, exponent));
			}
			String s = String.format("If %s, then %c = ", ensureMath(log(variable(variable), num(logArg)) + op('=') + num(exponent)), variable);
			return Builder.of(s).addResult(new Complex(base)).build();
		}
		else { //the argument to the log function is a variable
			String s = String.format("If %s, then %c = ", ensureMath(log(num(base), variable(variable)) + op('=') + num(exponent)), variable);
			return Builder.of(s).addResult(new Complex(logArg)).build();
		}
	}

}
