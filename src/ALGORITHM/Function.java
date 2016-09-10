package com.clawsoftware.algorithm;

/**
 *
 * @author Clemens Lode, 2008, University Karlsruhe (TH), clemens@lode.de
 */

import java.math.BigInteger;

/**
 * The Function class holds all parameters (especially neighborhood, number of
 * cell states and rule number), organizes automated tests and calculates
 * expected memory consumption and expected time for the calculation. Both the
 * algorithm classes (*BruijnProductGraph) and the GUI accesses this class.
 */
public class Function {

	/**
	 * the number of neighborhood configurations of a cell of the significant
	 * function
	 */
	private static int significantMaxArraySize = 0;
	/**
	 * the function that maps each neighborhood configuration of the significant
	 * neighborhood to a value
	 */
	private static int significantFunction[] = {};
	/**
	 * the function that maps each neighborhood configuration to a value
	 */
	private static int function[] = null;
	/**
	 * significantMaxArraySize / cellStates, used only by the core
	 */
	private static int lastElementIndex = 0;
	/**
	 * the number of neighborhood configurations of a cell
	 */
	private static int maxArraySize = 0;

	/**
	 * updates the last element index and the max Array size, reinitialized the
	 * function
	 */
	private static void updateMaxArraySize() {
		lastElementIndex = Misc.pow(CellStates.getNumberOfCellStates(),
				Neighborhood.getNeighborhoodSize() - 1);
		maxArraySize = lastElementIndex * CellStates.getNumberOfCellStates();
		function = new int[getMaxArraySize()];
	}

	/**
	 * Calculates the number of possible neighborhood configurations a cell can
	 * have
	 * 
	 * @param cell_states
	 *            number of cell states
	 * @param neighborhood_size
	 *            size of the neighborhood
	 * @return number of possible neighborhood configurations
	 */
	public static int calculateMaxArraySize(final int cell_states,
			final int neighborhood_size) {
		return Misc.pow(cell_states, neighborhood_size);
	}

	/**
	 * updates the significant max array size, i.e. the number of possible
	 * neighborhood configurations of the significant neighborhood
	 */
	private static void updateSignificantMaxArraySize() {
		significantMaxArraySize = calculateMaxArraySize(
				CellStates.getNumberOfCellStates(),
				Neighborhood.getSignificantNeighborhoodSize());
		significantFunction = new int[getSignificantMaxArraySize()];
	}

	/**
	 * Creates a new significant function on basis of the provided rule number
	 * by succesively dividing it by the number of cell states.
	 * 
	 * @throws Exception
	 *             when the rule number is out of bounds
	 * @param rule_nr
	 *            the rule number as a BigInteger
	 */
	public static void createSignificantFunction(final BigInteger rule_nr)
			throws Exception {
		updateMaxArraySize();
		updateSignificantMaxArraySize();

		significantFunction = calculateFunction(rule_nr,
				getSignificantMaxArraySize(), getMaxArraySize(),
				CellStates.getNumberOfCellStates());

		Neighborhood.resetSignificantNeighborhoodSize();
	}

	/**
	 * Determines the function array out of a wolfram rule number and the
	 * corresponding parameters
	 * 
	 * @param rule_nr
	 *            Wolfram rule number
	 * @param significant_max_array_size
	 *            number of possible neighborhood configurations of the
	 *            significant neighborhood
	 * @param max_array_size
	 *            number of possible neighborhood configurations of the
	 *            neighborhood
	 * @param cell_states
	 *            number of cell states
	 * @return function array
	 * @throws java.lang.Exception
	 *             if the rule number parameter is out of range
	 */
	public static int[] calculateFunction(BigInteger rule_nr,
			final int significant_max_array_size, final int max_array_size,
			final int cell_states) throws Exception {
		final int[] new_function = new int[significant_max_array_size];
		final BigInteger max_rule_number = WolframRule.getMaxWolframRuleNumber(
				max_array_size, cell_states);
		if (rule_nr.compareTo(BigInteger.ZERO) < 0
				|| rule_nr.compareTo(max_rule_number) >= 0) {
			throw new Exception(
					"createSignificantFunction(): wolfram rule number "
							+ rule_nr + " out of range (not within [0, "
							+ max_rule_number.toString() + "]).");
		}
		for (int i = 0; i < significant_max_array_size; i++) {
			new_function[i] = rule_nr.mod(BigInteger.valueOf(cell_states))
					.intValue();
			rule_nr = rule_nr.divide(BigInteger.valueOf(cell_states));
		}
		return new_function;
	}

	/**
	 * Creates a standard rule (000....0111...1) on basis of the current
	 * parameters
	 * 
	 * @throws Exception
	 *             when creating / accessing the significant function (TODO)
	 */
	public static void createStandardSignificantFunction() throws Exception {
		updateSignificantMaxArraySize();
		for (int j = 0; j < CellStates.getNumberOfCellStates(); j++) {
			for (int i = 0; i < getSignificantMaxArraySize()
					/ CellStates.getNumberOfCellStates(); i++) {
				significantFunction[i + j * getSignificantMaxArraySize()
						/ CellStates.getNumberOfCellStates()] = j;
			}
		}
	}

	/**
	 * Updates the function on basis of the significant function by extending
	 * the significant function over the holey neighborhood. The final function
	 * will map all neighborhood configurations with the same significant
	 * neighborhood configuration on the same value that the significant
	 * function maps the significant neighborhood configuration.
	 * 
	 * @throws Exception
	 *             if an internal error occured
	 */
	public static void updateFunction() throws Exception {
		updateMaxArraySize();

		for (int i = 0; i < getMaxArraySize(); i++) {

			// create bit array out of i
			// transform bit array with getSignificantNeighborhoodPosition to
			// new bit array
			// use new bit array as new z

			int z = i;
			final int[] bit_array = new int[Neighborhood.getNeighborhoodSize()];
			for (int k = 0; k < bit_array.length; k++) {
				bit_array[k] = z % CellStates.getNumberOfCellStates();
				z /= CellStates.getNumberOfCellStates();
			}

			final int[] transformed_bit_array = new int[Neighborhood
					.getSignificantNeighborhoodSize()];
			final int[] sig_bit_array = Neighborhood
					.getStandardizedSignificantNeighborhoodCopy();

			for (int k = 0; k < transformed_bit_array.length; k++) {
				transformed_bit_array[k] = bit_array[sig_bit_array[k]];
			}

			int result = 0;
			for (int k = transformed_bit_array.length - 1; k >= 0; k--) {
				result *= CellStates.getNumberOfCellStates();
				result += transformed_bit_array[k];
			}

			function[i] = getSignificantFunction(result);
		}
	}

	/**
	 * Creates the next permutation of the rule. Only balanced rules will be
	 * generated, i.e. rules which have balanced result values (all other rules
	 * are not surjective or injective)
	 * 
	 * @return false if the last permutation has been reached
	 * @throws Exception
	 *             if an internal error occured
	 */
	public static boolean nextFunctionPermutation() throws Exception {
		if (!Iteration.isTestAllBalancedFunctions()) {
			return false;
		}
		if (!Misc.next_function_permutation(significantFunction)) {
			createStandardSignificantFunction();
			return false;
		}
		return true;
	}

	/*
	 * ------------------ GET/SET FUNCTIONS -------------------
	 */

	/**
	 * @return the significant function as a string
	 */
	public static String getSignificantFunctionString() {
		String t = "";
		for (int i = 0; i < getSignificantMaxArraySize(); i++) {
			t += significantFunction[i];
		}
		return t;
	}

	/**
	 * Problem size is defined in this context by the upper limit of the size of
	 * the Bruijn product graph, which is (cell states) to the power of
	 * [2*(neighborhood size-1)] This function will use the current parameter
	 * settings
	 * 
	 * @see Function.getProblemSize()
	 * @return problem size in number of nodes
	 */
	public static int getCurrentProblemSize() {
		return getLastElementIndex() * getLastElementIndex();
	}

	/**
	 * @return the Wolfram rule number based on the current significant function
	 *         and the number of cell states
	 */
	public static BigInteger getSignificantWolframRuleNumber() {
		return WolframRule.getWolframRuleNumber(significantFunction,
				CellStates.getNumberOfCellStates());
	}

	/**
	 * @return the polynomial rule based on the current significant function and
	 *         the number of cell states
	 */
	public static String getSignificantPolynomialRule() {
		return PolynomialRule.getPolynomialRule(significantFunction,
				CellStates.getNumberOfCellStates());
	}

	/**
	 * @return the boolean rule based on the current significant function
	 */
	public static String getSignificantBooleanRule() {
		return BooleanRule.getBooleanRule(significantFunction);
	}

	/**
	 * Returns the index of the last element of the function TODO
	 * 
	 * @return index of the last element
	 */
	public static int getLastElementIndex() {
		return lastElementIndex;
	}

	/**
	 * Returns the number of possible neighborhood configurations, i.e. number
	 * of different cell states ^ size of neighborhood [last position - first
	 * position]
	 * 
	 * @return The number of possible neighborhood configurations
	 */
	public static int getMaxArraySize() {
		return maxArraySize;
	}

	/**
	 * Main function for the algorithm, applies the rule on the neighborhood
	 * configuration
	 * 
	 * @param i
	 *            Configuration of the neighborhood
	 * @return Next state of the cell with that neighborhood configuration
	 * @throws Exception
	 *             if the index is out of range
	 */
	public static int getFunction(final int i) throws Exception {
		if (i < 0 || i >= function.length) {
			throw new Exception("getFunction(): index " + i + " out of range ("
					+ function.length + ")");
		}
		return function[i];
	}

	/**
	 * Returns the number of possible neighborhood configurations of the
	 * significant function, i.e. number of different cell states ^ number of
	 * significant of neighborhood positions
	 * 
	 * @return The number of possible significant neighborhood configurations
	 */
	public static int getSignificantMaxArraySize() {
		return significantMaxArraySize;
	}

	/**
	 * Returns the value the significant function maps this significant
	 * neighborhood configuration to
	 * 
	 * @throws Exception
	 *             if the index is out of range
	 * @param i
	 *            index representing the significant neighborhood configuration
	 * @return Next state of the cell with that significant neighborhood
	 *         configuration
	 */
	public static int getSignificantFunction(final int i) throws Exception {
		if (i < 0 || i >= significantFunction.length) {
			throw new Exception("getSignificantFunction(): index " + i
					+ " out of range (" + significantFunction.length + ")");
		}
		return significantFunction[i];
	}

	/**
	 * Calculates the result of the function applied on the neighborhood of a
	 * cell
	 * 
	 * @param neighbors
	 *            the neighborhood of the cell
	 * @return the corresponding value of the function
	 * @throws java.lang.Exception
	 *             see Misc.getNeighborhoodFunction
	 */
	public static int getSignificantFunction(final int[] neighbors)
			throws Exception {
		return Misc.getNeighborhoodFunction(neighbors, significantFunction,
				CellStates.getNumberOfCellStates());
	}

	/**
	 * @return a pointer to the significantFunction
	 */
	public static final int[] getSignificantFunctionArray() {
		return significantFunction;
	}
};
