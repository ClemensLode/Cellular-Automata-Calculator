package com.clawsoftware.algorithm;

/**
 *
 * @author Clemens Lode, 2008, University Karlsruhe (TH), clemens@lode.de
 */
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class BooleanRule {

	private final static String boolean_not_sign = new String("'");
	private final static String boolean_or_sign = new String("v");

	/**
	 * Generates a boolean rule representation out of the function array
	 * 
	 * @param function
	 *            Function that we want to transform into a boolean rule string
	 * @return The boolean rule string
	 */
	public static String getBooleanRule(final int[] function) {
		String boolean_rule = "f = ";
		final int neighborhood_size = Neighborhood
				.getSignificantNeighborhoodSize();
		final int significant_max_array_size = function.length;

		final int set_bits[][] = new int[significant_max_array_size][neighborhood_size];
		for (int i = 0; i < significant_max_array_size; i++) {
			Arrays.fill(set_bits[i], 2);
		}
		for (int i = 0; i < significant_max_array_size; i++) {
			// determine bits corresponding to each function index
			if (function[i] == 1) {
				int j = i;
				for (int k = 0; k < neighborhood_size; k++) {
					if (j % 2 == 1) {
						set_bits[i][neighborhood_size - 1 - k] = 1;
						j -= 1;
					} else {
						set_bits[i][neighborhood_size - 1 - k] = 0;
					}
					j /= 2;
				}
			}
		}

		// to catch all pairs we have to iterate several times (could be
		// optimized but who cares)
		for (int l = 0; l < neighborhood_size; l++) {

			for (int i = 0; i < significant_max_array_size; i++) {

				// find a pair of entries with one different bit and combine
				// them by replacing the differenting bit with a DONTCARE ('2')
				for (int j = i + 1; j < significant_max_array_size; j++) {
					int different_positions = 0;
					int different_position = 0;
					for (int k = 0; k < neighborhood_size; k++) {

						// cancel if the bit that is different is 2
						if ((set_bits[i][k] == 2 || set_bits[j][k] == 2)
								&& set_bits[i][k] != set_bits[j][k]) {
							different_positions = 2;
							break;
						}

						if (set_bits[i][k] != set_bits[j][k]) {
							different_positions++;
							if (different_positions == 2) {
								break;
							}
							different_position = k;
						}
					}
					if (different_positions == 1) {
						// remove one entry
						for (int k = 0; k < neighborhood_size; k++) {
							set_bits[i][k] = 2;
						}
						// set bit of the differenting part of the pair to
						// DONTCARE
						set_bits[j][different_position] = 2;
						boolean not_true = false;
						for (int ii = 0; ii < significant_max_array_size; ii++) {
							for (int k = 0; k < neighborhood_size; k++) {
								if (set_bits[ii][k] != 2) {
									not_true = true;
								}
							}
						}
						// are all entries set to '2'? => boolean rule is "true"
						if (!not_true) {
							return boolean_rule + "true";
						}
						not_true = false;
						for (int k = 0; k < neighborhood_size; k++) {
							if (set_bits[j][k] != 2) {
								not_true = true;
							}
						}
						if (!not_true) {
							return boolean_rule + "true";
						}
					} else if (different_positions == 0) {
						// remove one entry
						for (int k = 0; k < neighborhood_size; k++) {
							set_bits[j][k] = 2;
						}

					}
					// shorten the boolean rule by combining and replacing with
					// more general rule parts
					different_positions = 0;
					different_position = 0;
					int two_count_1 = 0;
					int two_count_2 = 0;
					for (int k = 0; k < neighborhood_size; k++) {

						if (set_bits[i][k] != set_bits[j][k]) {
							if (set_bits[i][k] == 2) {
								two_count_1++;
								if (two_count_1 == 2) {
									different_positions = 2;
									break;
								}
							} else if (set_bits[j][k] == 2) {
								two_count_2++;
								if (two_count_2 == 2) {
									different_positions = 2;
									break;
								}
							} else {
								different_positions++;
								if (different_positions == 2) {
									break;
								}
								different_position = k;
							}
						}

					}
					if (different_positions == 1 && two_count_1 != two_count_2) {
						if (two_count_1 == 1) {
							set_bits[j][different_position] = 2;
						} else if (two_count_2 == 1) {
							set_bits[i][different_position] = 2;
						}
						boolean not_true = false;
						for (int ii = 0; ii < significant_max_array_size; ii++) {
							for (int k = 0; k < neighborhood_size; k++) {
								if (set_bits[ii][k] != 2) {
									not_true = true;
								}
							}
						}
						if (!not_true) {
							return boolean_rule + "true";
						}
					}
				}

			}
		}

		// now create the actual boolean rule string out of the set_bits array
		boolean first_entry = true;
		try {
			for (int i = 0; i < significant_max_array_size; i++) {
				boolean empty = true;
				for (int k = 0; k < neighborhood_size; k++) {
					if (set_bits[i][k] != 2) {
						empty = false;
						break;
					}
				}
				if (!empty) {
					// first entry does not need a space at the beginning
					if (!first_entry) {
						boolean_rule = boolean_rule + " " + boolean_or_sign
								+ "  ";
					}
					first_entry = false;

					for (int k = 0; k < neighborhood_size; k++) {
						if (set_bits[i][k] == 2) {
							continue;
						}
						boolean_rule = boolean_rule + "x"
								+ (Neighborhood.getNeighborhoodNumber(k) + 1);// (k+1);//Neighborhood.getSignificantNeighborhoodPosition(k);
						if (set_bits[i][k] == 0) {
							boolean_rule = boolean_rule + boolean_not_sign;
						}
						boolean_rule = boolean_rule + " ";
					}
				}
			}
		} catch (final Exception e) {
		}
		// is first entry still set? Then the boolean rule is "false"
		if (first_entry) {
			boolean_rule = boolean_rule + "false";
		}

		boolean_rule = boolean_rule.trim();
		return boolean_rule;
	}

	/**
	 * Extracts the Wolfram Rule Number out of a boolean rule string
	 * 
	 * @param boolean_rule
	 *            The boolean rule string to be parsed
	 * @param significant_max_array_size
	 *            Number of neighborhood combinations of the function
	 * @return The Wolfram Rule Number
	 * @throws java.lang.Exception
	 *             If there was a parse error, i.e. an invalid boolean rule
	 */
	public static BigInteger extractBooleanRuleNumber(String boolean_rule,
			final int significant_max_array_size) throws Exception {
		BigInteger rule_nr = BigInteger.ZERO;
		final int resulting_function[] = new int[significant_max_array_size];
		for (int i = 0; i < significant_max_array_size; i++) {
			resulting_function[i] = 0;
		}

		boolean_rule = boolean_rule.toLowerCase();
		boolean_rule = " " + boolean_rule;
		boolean_rule = boolean_rule.replace(boolean_or_sign, " "
				+ boolean_or_sign + " ");
		boolean_rule = boolean_rule.replace("=", " = ");
		boolean_rule = boolean_rule.replace("*", " ");
		// replace constants in order to prevent confusion with variable indices
		boolean_rule = boolean_rule.replace(" 0", "b0");
		boolean_rule = boolean_rule.replace(" 1", "b1");
		boolean_rule = boolean_rule.replace(" ", "");
		// split "f = "
		final String[] elements = boolean_rule.split("=");
		if (elements.length > 2) {
			throw new Exception(
					"extractBooleanRuleNumber(): More than one '=' found.");
		}
		if (elements.length == 2) {
			boolean_rule = elements[1];
		}

		// check for "false"/"true"
		final int true_index = boolean_rule.indexOf("true");
		if (true_index > 0) {
			throw new Exception(
					"extractBooleanRuleNumber(): value 'true' found at unexpected place ["
							+ boolean_rule + "].");
		} else if (true_index == 0) {
			return WolframRule.getMaxWolframRuleNumber(
					significant_max_array_size, 2).subtract(BigInteger.ONE);
		}
		final int false_index = boolean_rule.indexOf("false");
		if (false_index > 0) {
			throw new Exception(
					"extractBooleanRuleNumber(): value 'false' found at unexpected place ["
							+ boolean_rule + "].");
		} else if (false_index == 0) {
			return BigInteger.ZERO;
		}

		// split OR
		final String[] or_elements = boolean_rule.split(boolean_or_sign);

		final ArrayList<ArrayList<Integer>> xe_list = new ArrayList<ArrayList<Integer>>();
		final ArrayList<ArrayList<Boolean>> not_list_list = new ArrayList<ArrayList<Boolean>>();

		for (String s : or_elements) {
			if (s.contains("b0")) {
				// ignore this OR element, is always false
				continue;
			}
			if (s.compareTo("b1") == 0) {
				// return max wolfram number because an OR element is 1, is
				// always true
				return WolframRule.getMaxWolframRuleNumber(
						significant_max_array_size, 2).subtract(BigInteger.ONE);
			}
			// replace remaining '1' constants
			if (s.contains("b1")) {
				s = s.replace("b1", "");
			}

			s = s + " ";

			final ArrayList<Integer> xe = new ArrayList<Integer>();
			// separate list to keep track which variables are negated
			final ArrayList<Boolean> not_list = new ArrayList<Boolean>();

			final String[] not_elements = s.split(boolean_not_sign);

			for (String f : not_elements) {
				if (f.compareTo(" ") == 0) {
					continue;
				}
				final int l1 = f.length();
				f = f.trim();
				boolean last_sign = false;
				if (f.length() != l1) {
					last_sign = true;
				}

				final String[] x_elements = f.split("[Xx]");
				for (int x = 0; x < x_elements.length; x++) {
					if (x_elements[x].length() == 0) {
						continue;
					}
					// get the variable indices
					int x_value = Integer.valueOf(x_elements[x]);
					if (x_value <= 0) {
						throw new Exception(
								"extractBooleanRuleNumber(): First neighborhood / variable index is 1, not 0");
					}
					x_value--; // internally we begin indices with 0 (x0)
					xe.add(Integer.valueOf(x_value));
					if (x == x_elements.length - 1 && last_sign == false) {
						not_list.add(Boolean.TRUE);
					} else {
						not_list.add(Boolean.FALSE);
					}
				}
			}

			xe_list.add(xe);
			not_list_list.add(not_list);
		}
		int largest_x = 0;
		final ArrayList<Integer> element_list = new ArrayList<Integer>();

		for (final ArrayList<Integer> l : xe_list) {
			for (final Integer i : l) {
				if (largest_x < i) {
					largest_x = i;
				}

				if (!element_list.contains(i)) {
					element_list.add(i);
				}
			}
		}

		int neighborhood_size = largest_x + 1;

		if (neighborhood_size > Neighborhood.getNeighborhoodSize()) {
			throw new Exception(
					"extractBooleanRuleNumber(): Implied neighborhood size ("
							+ neighborhood_size
							+ ") exceeds given neighborhood size ("
							+ Neighborhood.getNeighborhoodSize() + ")");
		}

		int significant_neighborhood_size = element_list.size();

		if (significant_neighborhood_size > Neighborhood
				.getSignificantNeighborhoodSize()) {
			throw new Exception(
					"extractBooleanRuleNumber(): Implied significant neighborhood size exceeds given significant neighborhood size");
		}

		significant_neighborhood_size = Neighborhood
				.getSignificantNeighborhoodSize();
		neighborhood_size = Neighborhood.getNeighborhoodSize();

		int m = 0;
		for (final ArrayList<Integer> l : xe_list) {
			// e.g. x1x3 => set both x1-x2x3 and x1x2x3
			final int set_bits[] = new int[significant_neighborhood_size];
			for (int i = 0; i < significant_neighborhood_size; i++) {
				set_bits[i] = -1;
			}
			int n = 0;
			// move through all elements of the xe_list and check corresponding
			// elements from the not_list if the entry is negated
			for (final Integer i : l) {
				if (not_list_list.get(m).get(n)) {
					set_bits[i] = 0;
				} else {
					set_bits[i] = 1;
				}
				n++;
			}
			m++;

			int number_variable_entries = 1;
			for (int i = 0; i < significant_neighborhood_size; i++) {
				if (set_bits[i] == -1) {
					number_variable_entries *= 2;
				}
			}

			// e.g. 3 free entries, 2 fixed entries => total 8 entries
			for (int i = 0; i < number_variable_entries; i++) {
				final boolean current_entry[] = new boolean[significant_neighborhood_size];
				int k = 0;
				for (int j = 0; j < significant_neighborhood_size; j++) {
					if (set_bits[j] == 0) {
						current_entry[j] = false;
					} else if (set_bits[j] == 1) {
						current_entry[j] = true;
					} else {
						int temp = i;
						for (int z = 0; z < k; z++) {
							if (temp % 2 == 1) {
								temp--;
							}
							temp /= 2;
						}
						if (temp % 2 == 1) {
							current_entry[j] = true;
						} else {
							current_entry[j] = false;
						}
						k++;
					}
				}

				// calculate the index from the current_entry array
				int result = 0;
				for (int j = 0; j < significant_neighborhood_size; j++) {
					result *= 2;
					if (current_entry[j]) {
						result++;
					}
				}
				resulting_function[result] = 1;

			}
		}

		// combine resulting function into a wolfram number
		for (int i = significant_max_array_size - 1; i >= 0; i--) {
			rule_nr = rule_nr.multiply(BigInteger.valueOf(2));
			rule_nr = rule_nr.add(BigInteger.valueOf(resulting_function[i]));
		}
		return rule_nr;
	}
}
