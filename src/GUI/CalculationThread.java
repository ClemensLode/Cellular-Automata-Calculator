package com.clawsoftware.gui;

import java.io.File;
import java.math.BigInteger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import com.clawsoftware.algorithm.BruijnProductGraph;
import com.clawsoftware.algorithm.CellStates;
import com.clawsoftware.algorithm.FastBruijnProductGraph;
import com.clawsoftware.algorithm.Function;
import com.clawsoftware.algorithm.InOutput;
import com.clawsoftware.algorithm.Iteration;
import com.clawsoftware.algorithm.Neighborhood;
import com.clawsoftware.algorithm.PrintBruijnProductGraph;

/**
 * Main calculation thread class
 *
 * @author Clemens Lode, 2008, University Karlsruhe (TH), clemens@lode.de
 */
public class CalculationThread extends Thread {

	/**
	 * Implementation of the call back procedure
	 */
	private class CallBack implements Iteration.CallBack {

		/**
		 * pointer to the progressBar of the main window
		 */
		JProgressBar progressBar = null;
		/**
		 * pointer to the remainingTime label of the main window
		 */
		JLabel remainingTime = null;

		/**
		 * Updates the remaining time, iterations and progress bar of the main
		 * window
		 */
		private void update() {
			if (Iteration.lastUpdate.subtract(Iteration.progress).compareTo(
					Iteration.SPEED_FACTOR) > 0) {
				if (java.lang.System.currentTimeMillis() != Iteration.lastUpdateTime) {
					Iteration.SPEED_FACTOR = Iteration.SPEED_FACTOR
							.multiply(BigInteger.valueOf(9))
							.add(Iteration.lastUpdate
									.subtract(Iteration.progress)
									.multiply(BigInteger.valueOf(500))
									.divide(BigInteger.valueOf(java.lang.System
											.currentTimeMillis()
											- Iteration.lastUpdateTime)))
											.divide(BigInteger.valueOf(10));
					Iteration.lastUpdateTime = java.lang.System
							.currentTimeMillis();
					Iteration.lastUpdate = Iteration.progress;
					Iteration.update = true;
				}
				progressBar.setValue(1000 - Iteration
						.getRemainingCalculationSteps()
						.multiply(BigInteger.valueOf(1000))
						.divide(Iteration.getTotalCalculationSteps())
						.intValue());
				remainingTime.setText(Iteration.getRemainingNeededTimeString()
						.toString()); // getNumberOfRemainingPermutationsString());
			}
		}

		/**
		 * Update the progress variable with each call (called externally by
		 * BruijnProductGraph)
		 *
		 * @param my_progress
		 *            number of steps calculated in the mean time
		 */
		@Override
		public void updateBar(final int my_progress) {
			Iteration.progress = Iteration.progress.subtract(BigInteger
					.valueOf(my_progress));
			update();
		}

		/**
		 * Resets the update bar by removing the remaining calculation steps
		 * (modulo). This is necessary because the core already called updateBar
		 * but may have been canceled the calculation before all nodes have been
		 * checked
		 *
		 * @param my_progress
		 *            number of steps calculated in the mean time
		 */
		@Override
		public void forwardUpdateBar(final int my_progress) {
			if (Iteration.progress.mod(BigInteger.valueOf(my_progress)).equals(
					BigInteger.ZERO)) {
				Iteration.progress = Iteration.progress.subtract(BigInteger
						.valueOf(my_progress));
			} else {
				Iteration.progress = Iteration.progress
						.subtract(Iteration.progress.mod(BigInteger
								.valueOf(my_progress)));
			}
			update();
		}
	}

	/**
	 * pointer to the main window
	 */
	private Main main = null;

	private CallBack callback = null;

	/**
	 * parameters from the main window
	 */
	private boolean generate_graph;
	private boolean use_fast_cpp;
	private boolean output_all;
	private boolean output_surjective;
	private boolean output_injective;
	private boolean add_to_database;
	private boolean check_duplicates;
	private boolean output_boolean;
	private boolean output_polynomial;

	/**
	 * Init thread with the calculation settings from the main window
	 */
	public void init_thread(final Main main_window,
			final JProgressBar progress_bar, final JLabel remaining_time,
			final boolean is_generate_graph, final boolean is_use_fast_cpp,
			final boolean is_output_all, final boolean is_output_surjective,
			final boolean is_output_injective,
			final boolean is_add_to_database,
			final boolean is_check_duplicates, final boolean is_output_boolean,
			final boolean is_output_polynomial) {
		main = main_window;
		callback = new CallBack();
		callback.progressBar = progress_bar;
		callback.remainingTime = remaining_time;
		Iteration.callback = callback;

		generate_graph = is_generate_graph;
		use_fast_cpp = is_use_fast_cpp;
		output_all = is_output_all;
		output_surjective = is_output_surjective;
		output_injective = is_output_injective;
		output_boolean = is_output_boolean;
		output_polynomial = is_output_polynomial;

		add_to_database = is_add_to_database;
		check_duplicates = is_check_duplicates;
	}

	/**
	 * Main worker procedure
	 */
	@Override
	public void run() {

		final String calling_string[] = new String[4];
		final Runtime rt = Runtime.getRuntime();
		calling_string[0] = "catest_c";
		// TODO LINUX
		try {
			do {
				boolean is_surjective = false;
				boolean is_injective = false;
				int error = 0;
				Function.updateFunction();

				// call external CPP Plugin
				// TODO, does not work with disordered neighborhoods!
				if (use_fast_cpp) {
					// prepare parameters
					calling_string[1] = Function.getSignificantFunctionString();
					calling_string[2] = "" + CellStates.getNumberOfCellStates();
					calling_string[3] = Neighborhood
							.getShortSignificantNeighborhoodString();
					int return_value = 0;
					// call plugin
					try {
						final Process p = rt.exec(calling_string);
						p.getInputStream();
						p.getOutputStream();
						p.getErrorStream();
						return_value = p.waitFor();
						p.destroy();
					} catch (final Exception exc) {
						error = 1;
					}

					// check return value
					switch (return_value) {
					case 1:
						is_surjective = false;
						is_injective = false;
						break;
					case 2:
						is_surjective = true;
						is_injective = false;
						break;
					case 3:
						is_surjective = true;
						is_injective = true;
						break;
					default:
						if (error == 0) {
							error = return_value;
						}
						is_surjective = false;
						is_injective = false;
						break;
					}
				} else
					// main call
				{
					// init static graph structure
					BruijnProductGraph.initStatic();
					// depending on if we want to cancel early or generate a
					// graph call different classes
					if (generate_graph) {
						PrintBruijnProductGraph.runConnectionTest();
					} else {
						FastBruijnProductGraph.runConnectionTest();
					}

					// get the results
					is_surjective = BruijnProductGraph.isSurjective();
					if (is_surjective) {
						is_injective = BruijnProductGraph.isInjective();
					} else {
						is_injective = false;
					}
				}

				// should we output this result?
				final boolean do_output = output_all || is_surjective
						&& output_surjective || is_injective
						&& output_injective;

				// generate .viz file for dot?
				if (generate_graph) {
					if (do_output) {
						try {
							PrintBruijnProductGraph.printToFile(InOutput
									.getImageFileName(is_injective,
											is_surjective)
											+ ".viz");
						} catch (final Exception e) {
							JOptionPane
							.showMessageDialog(
									null,
									e,
									"INTERNAL ERROR when creating the image file name",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}

				if (error != 0
						&& JOptionPane
						.showConfirmDialog(
								null,
								"There was an error either calling the plugin or in the calculation itself [error "
										+ error
										+ "].\n"
										+ calling_string[0]
												+ " "
												+ calling_string[1]
														+ " "
														+ calling_string[2]
																+ " "
																+ calling_string[3]
																		+ "\n(You can ignore this if you have manually canceled the calculation)\n\nContinue?",
																		"Error in Plugin",
																		JOptionPane.YES_NO_OPTION,
																		JOptionPane.ERROR_MESSAGE) == 1) {
					break;
				}

				// output in a window
				// TODO old procedure... needs an update
				if (!add_to_database && do_output) {
					String s = "rule "
							+ Function.getSignificantWolframRuleNumber()
							+ ", neighborhood "
							+ Neighborhood.getSignificantNeighborhoodString()
							+ ", cell states "
							+ CellStates.getNumberOfCellStates() + "\n";
					if (!is_surjective) {
						s += " --> NOT surjective and ";
					} else {
						s += " -->     SURJECTIVE and ";
					}
					if (!is_injective) {
						s += "NOT injective";
					} else {
						s += "    INJECTIVE";
					}
					if (JOptionPane.showConfirmDialog(null,
							s + "\n\nContinue?", "Result",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE) == 1) {
						break;
					}
				}

				// output to database if it is not already in it
				if (do_output
						&& add_to_database
						&& (!check_duplicates || main.results.contains(InOutput
								.getParameterArray(generate_graph)) == -1)) {

					// create entry and add it
					final Object[] row = new Object[] {
							Function.getSignificantWolframRuleNumber(),
							output_boolean ? Function
									.getSignificantBooleanRule() : new String(
											""),
											output_polynomial ? Function
													.getSignificantPolynomialRule()
													: new String(""),
													Neighborhood.getSignificantNeighborhoodString(),
													Neighborhood.getNeighborhoodSize(),
													Neighborhood.getSignificantNeighborhoodSize(),
													CellStates.getNumberOfCellStates(), is_injective,
													is_surjective, new Boolean(false),
													new Boolean(false), new Boolean(false) };
					row[ResultsTable.GRAPH_INDEX] = new Boolean(
							generate_graph
							|| new File(InOutput.getImageFileName(row)
									+ ".viz").exists());
					row[ResultsTable.IMAGE_INDEX] = new Boolean(
							new File(InOutput.getImageFileName(row)
									+ ".viz.png").exists());
					row[ResultsTable.SIMULATOR_INDEX] = new Boolean(new File(
							InOutput.getSimulationFileName(row)).exists());

					main.results.addRow(row);
				}

				// call update bar, check for interrupt
				Iteration.callback.forwardUpdateBar(Function
						.getCurrentProblemSize());
				if (Iteration.update) {
					Iteration.update = false;
					try {
						sleep(1);
					} catch (final InterruptedException e) {
						break;
					}
				}

				try {
					// repeat until we find a parameter configuration we haven't
					// already in our database
					if (check_duplicates) {
						boolean has_next_permutation = true;
						while ((has_next_permutation = Iteration
								.nextParameterConfiguration())
								&& main.results.contains(InOutput
										.getParameterArray(generate_graph)) >= 0) {
						}

						if (!has_next_permutation) {
							break;
						}

					} else {
						// cancel if there are no more parameter configuration
						if (!Iteration.nextParameterConfiguration()) {
							break;
						}
					}
				} catch (final Exception e) {
					JOptionPane.showMessageDialog(null, e,
							"Error creating permutation",
							JOptionPane.ERROR_MESSAGE);
					break;
				}

			} while (true);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(null, e, "Error accessing Function",
					JOptionPane.ERROR_MESSAGE);
		}

		// clear up and return
		BruijnProductGraph.clearMemory();
		if (generate_graph) {
			PrintBruijnProductGraph.clearAdditionalMemory();
		}
		main.clearUpAfterCalculation();

	}
}
