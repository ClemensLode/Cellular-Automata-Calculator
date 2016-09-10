package com.clawsoftware.algorithm;

/**
 *
 * @author Clemens Lode, 2008, University Karlsruhe (TH), clemens@lode.de
 */
import java.math.BigInteger;

import com.clawsoftware.gui.ResultsTable;

// -- parsing functions, interface to the GUI
public class InOutput {
	/**
	 * Generates an image filename on basis of the internal parameters and the
	 * properties of the test case (surjective / injective)
	 *
	 * @param injective
	 *            true if the current test case was tested positively on
	 *            injectivity
	 * @param surjective
	 *            true if the current test case was tested positively on
	 *            surjectivity
	 * @return Proposed filename to save the graph information in
	 * @throws Exception
	 *             if the significant rule number could not be created
	 */
	public static String getImageFileName(final boolean injective,
			final boolean surjective) throws Exception {
		return "graphs/" + Function.getSignificantWolframRuleNumber() + "_"
				+ Neighborhood.getShortSignificantNeighborhoodString() + "_"
				+ CellStates.getNumberOfCellStates() + (injective ? "_i" : "")
				+ (surjective ? "_s" : "");
	}

	/**
	 * Generate a file name for the graph output on basis of the comitted
	 * parameter list
	 *
	 * @param parameter
	 *            Object list in the usual order (BigInteger, String, Integer,
	 *            Integer, Integer, Boolean, Boolean, Boolean)
	 * @return the file name
	 */
	public static String getImageFileName(final Object[] parameter) {
		return "graphs/" + parameter[0] + "_"
				+ ((String) parameter[3]).replace(" ", "").replace(",", "-")
				+ "_" + parameter[6] + ((Boolean) parameter[7] ? "_i" : "")
				+ ((Boolean) parameter[8] ? "_s" : "");
	}

	/**
	 * Generate a file name for the simulation output on basis of the comitted
	 * parameter list
	 *
	 * @param parameter
	 *            Object list in the usual order (BigInteger, String, Integer,
	 *            Integer, Integer, Boolean, Boolean, Boolean)
	 * @return the file name
	 */
	public static String getSimulationFileName(final Object[] parameter) {
		return "simulations/" + parameter[0] + "_"
				+ ((String) parameter[3]).replace(" ", "").replace(",", "-")
				+ "_" + parameter[6] + ((Boolean) parameter[7] ? "_i" : "")
				+ ((Boolean) parameter[8] ? "_s" : "") + ".sim.png";
	}

	/**
	 * Parses the string and returns the list of parameter values
	 *
	 * @param parameters
	 *            A line of numbers, seperated by commas. As the neighborhood
	 *            field itself consists of commas it is encapsulated in "..."
	 * @return List of objects with the appropriate types (BigInteger, String,
	 *         Integer, Integer, Integer, Boolean, Boolean, Boolean)
	 * @throws java.lang.NumberFormatException
	 *             if the object list is corrupted and one item not
	 *             corresponding to an object type
	 */
	public static Object[] parseParametersAsString(final String parameters)
			throws NumberFormatException {
		final Object[] l = new Object[ResultsTable.COLUMN_COUNT];
		final String t[] = parameters.split("\"");
		l[0] = new BigInteger(t[0].split(", ")[0]); // split '<rule_number>' AND
		// ', '
		l[1] = t[1]; // function
		l[2] = t[3]; // skip t[2], because it is ', '
		l[3] = t[5]; // skip t[4], because it is ', '
		final String t4[] = t[6].split(", ");
		l[4] = Integer.parseInt(t4[1]);
		l[5] = Integer.parseInt(t4[2]);
		l[6] = Integer.parseInt(t4[3]);
		l[7] = Boolean.parseBoolean(t4[4]); // injective
		l[8] = Boolean.parseBoolean(t4[5]); // surjective
		// l[9], l[10] and l[11] must be filled by the GUI
		return l;
	}

	/**
	 * Generates an Object array out of the rule numbers, neighborhood string
	 * and sizes and cell states
	 *
	 * @param generate_graph
	 *            if the entry has an corresponding graph file
	 * @return Object array
	 */
	public static Object[] getParameterArray(final boolean generate_graph) {
		final Object[] t = new Object[8];
		t[0] = Function.getSignificantWolframRuleNumber();
		t[1] = Function.getSignificantBooleanRule();
		t[2] = Function.getSignificantPolynomialRule();

		t[3] = Neighborhood.getSignificantNeighborhoodString();
		t[4] = Neighborhood.getNeighborhoodSize();
		t[5] = Neighborhood.getSignificantNeighborhoodSize();

		t[6] = CellStates.getNumberOfCellStates();

		t[7] = new Boolean(generate_graph);

		return t;
	}

	/**
	 * Calculates the time needed for the calculation and formats it in a
	 * readable format (hours, minutes, seconds etc.)
	 *
	 * @param time
	 *            time we want to display
	 * @return time needed for the calculation as a formatted string (e.g.
	 *         "7m 3s")
	 */
	public static String getNeededTimeString(BigInteger time) {
		if (time.compareTo(BigInteger.ONE) < 0) {
			return "< 1 second";
		}

		final BigInteger seconds = time.mod(BigInteger.valueOf(60));
		if (time.compareTo(BigInteger.valueOf(60)) < 0) {
			return "" + seconds + "s";
		}

		time = time.divide(BigInteger.valueOf(60));
		final BigInteger minutes = time.mod(BigInteger.valueOf(60));
		if (time.compareTo(BigInteger.valueOf(60)) < 0) {
			return "" + minutes + "m " + seconds + "s";
		}

		time = time.divide(BigInteger.valueOf(60));
		final BigInteger hours = time.mod(BigInteger.valueOf(24));
		if (time.compareTo(BigInteger.valueOf(24)) < 0) {
			return "" + hours + "h " + minutes + "m ";
		}

		time = time.divide(BigInteger.valueOf(24));
		final BigInteger days = time.mod(BigInteger.valueOf(365));
		if (time.compareTo(BigInteger.valueOf(365)) < 0) {
			return "" + days + "d " + hours + "h " + minutes + "m ";
		} else {
			return "> 1 year";
		}
	}

	/**
	 * Translates the value 'needed_memory' to a string by converting it in the
	 * standard B/KB/MB/GB format.
	 *
	 * @param needed_memory
	 *            needed memory in Bytes
	 * @return Memory needed for the calculation as a formatted string (e.g.
	 *         "7mb")
	 */
	public static String getNeededMemorySizeString(BigInteger needed_memory) {
		final String size_desc[] = { "Bytes", "KBytes", "MBytes", "GBytes",
		"TBytes" };
		int i = 1;
		while (needed_memory.compareTo(BigInteger.valueOf(1024)) >= 0) {
			needed_memory = needed_memory.divide(BigInteger.valueOf(1024));
			if (needed_memory.compareTo(BigInteger.valueOf(1024)) < 0 || i == 4) {
				return "~" + needed_memory + " " + size_desc[i];
			}
			i++;
		}
		return "~" + needed_memory + " " + size_desc[0];
	}
}
