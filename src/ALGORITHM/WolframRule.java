package com.clawsoftware.algorithm;

/**
 *
 * @author Clemens Lode, 2008, University Karlsruhe (TH), clemens@lode.de
 */

import java.math.BigInteger;

public class WolframRule {

	/**
	 * Calculates a readable rule number on basis of the internal significant
	 * function and the parameters, i.e. the positions in the neighborhood that
	 * were not defined are ignored If disordered neighborhoods are tested this
	 * function will return the (to the disordered neighborhood) appropriate
	 * rule number
	 * 
	 * @param function
	 *            function to convert
	 * @param cell_states
	 *            number of cell states
	 * @return Significant rule number, can become quite big
	 */
	public static BigInteger getWolframRuleNumber(final int[] function,
			final int cell_states) {
		BigInteger rule_nr = BigInteger.ZERO;
		for (int i = function.length - 1; i >= 0; i--) {
			rule_nr = rule_nr.multiply(BigInteger.valueOf(cell_states));
			rule_nr = rule_nr.add(BigInteger.valueOf(function[i]));
		}
		return rule_nr;
	}

	/**
	 * Calculates the upper boundary of all wolfram rule numbers with the
	 * current settings
	 * 
	 * @param function_size
	 *            size of function array (number of different values for the
	 *            neighborhood)
	 * @param cell_states
	 *            number of cell states
	 * @return upper boundary of all wolfram rule numbers
	 */
	public static BigInteger getMaxWolframRuleNumber(final int function_size,
			final int cell_states) {
		return BigInteger.valueOf(cell_states).pow(function_size);
	}

}
