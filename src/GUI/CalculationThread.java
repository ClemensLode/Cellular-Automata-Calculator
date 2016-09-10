package GUI;

import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

import java.math.BigInteger;

/**
 * Main calculation thread class
 * @author Clemens Lode, 1151459, University Karlsruhe (TH), clemens@lode.de
 */
public class CalculationThread extends Thread {

    /**
     * Implementation of the call back procedure
     */
    private class CallBack implements ALGORITHM.Iteration.CallBack {

        /**
         * pointer to the progressBar of the main window
         */
        JProgressBar progressBar = null;
        /**
         * pointer to the remainingTime label of the main window
         */
        JLabel remainingTime = null;

        /**
         * Updates the remaining time, iterations and progress bar of the main window
         */
        private void update() {
            if (ALGORITHM.Iteration.lastUpdate.subtract(ALGORITHM.Iteration.progress).compareTo(ALGORITHM.Iteration.SPEED_FACTOR) > 0) {
                if (java.lang.System.currentTimeMillis() != ALGORITHM.Iteration.lastUpdateTime) {
                    ALGORITHM.Iteration.SPEED_FACTOR = ALGORITHM.Iteration.SPEED_FACTOR.multiply(BigInteger.valueOf(9)).add(
                            ALGORITHM.Iteration.lastUpdate.subtract(ALGORITHM.Iteration.progress).multiply(BigInteger.valueOf(500)).divide(BigInteger.valueOf(java.lang.System.currentTimeMillis() - ALGORITHM.Iteration.lastUpdateTime))).divide(BigInteger.valueOf(10));
                    ALGORITHM.Iteration.lastUpdateTime = java.lang.System.currentTimeMillis();
                    ALGORITHM.Iteration.lastUpdate = ALGORITHM.Iteration.progress;
                    ALGORITHM.Iteration.update = true;
                }
                progressBar.setValue(1000 - ALGORITHM.Iteration.getRemainingCalculationSteps().multiply(BigInteger.valueOf(1000)).divide(ALGORITHM.Iteration.getTotalCalculationSteps()).intValue());
                remainingTime.setText(ALGORITHM.Iteration.getRemainingNeededTimeString().toString()); //getNumberOfRemainingPermutationsString());
            }
        }

        /**
         * Update the progress variable with each call (called externally by BruijnProductGraph)
         * @param my_progress number of steps calculated in the mean time
         */
        public void updateBar(int my_progress) {
            ALGORITHM.Iteration.progress = ALGORITHM.Iteration.progress.subtract(BigInteger.valueOf(my_progress));
            update();
        }

        /**
         * Resets the update bar by removing the remaining calculation steps (modulo).
         * This is necessary because the core already called updateBar but may have been canceled the calculation before all nodes have been checked
         * @param my_progress number of steps calculated in the mean time
         */
        public void forwardUpdateBar(int my_progress) {
            if (ALGORITHM.Iteration.progress.mod(BigInteger.valueOf(my_progress)).equals(BigInteger.ZERO)) {
                ALGORITHM.Iteration.progress = ALGORITHM.Iteration.progress.subtract(BigInteger.valueOf(my_progress));
            } else {
                ALGORITHM.Iteration.progress = ALGORITHM.Iteration.progress.subtract(ALGORITHM.Iteration.progress.mod(BigInteger.valueOf(my_progress)));
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
    public void init_thread(Main main_window, JProgressBar progress_bar, JLabel remaining_time, boolean is_generate_graph, boolean is_use_fast_cpp, boolean is_output_all, boolean is_output_surjective, boolean is_output_injective, boolean is_add_to_database, boolean is_check_duplicates, boolean is_output_boolean, boolean is_output_polynomial) {
        main = main_window;
        callback = new CallBack();
        callback.progressBar = progress_bar;
        callback.remainingTime = remaining_time;
        ALGORITHM.Iteration.callback = callback;

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

        String calling_string[] = new String[4];
        Runtime rt = Runtime.getRuntime();
        calling_string[0] = "catest_c";
        // TODO LINUX
        try {
            do {
                boolean is_surjective = false;
                boolean is_injective = false;
                int error = 0;
                ALGORITHM.Function.updateFunction();

// call external CPP Plugin 
// TODO, does not work with disordered neighborhoods!
                if (use_fast_cpp) {
                    // prepare parameters
                    calling_string[1] = ALGORITHM.Function.getSignificantFunctionString();
                    calling_string[2] = "" + ALGORITHM.CellStates.getNumberOfCellStates();
                    calling_string[3] = ALGORITHM.Neighborhood.getShortSignificantNeighborhoodString();
                    int return_value = 0;
                    // call plugin
                    try {
                        Process p = rt.exec(calling_string);
                        InputStream in = p.getInputStream();
                        OutputStream out = p.getOutputStream();
                        InputStream err = p.getErrorStream();
                        return_value = p.waitFor();
                        p.destroy();
                    } catch (Exception exc) {
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
                } 
                else 
                // main call
                {
                    // init static graph structure
                    ALGORITHM.BruijnProductGraph.initStatic();
                    // depending on if we want to cancel early or generate a graph call different classes
                    if (generate_graph) {
                        ALGORITHM.PrintBruijnProductGraph.runConnectionTest();
                    } else {
                        ALGORITHM.FastBruijnProductGraph.runConnectionTest();
                    }

                    // get the results
                    is_surjective = ALGORITHM.BruijnProductGraph.isSurjective();
                    if (is_surjective) {
                        is_injective = ALGORITHM.BruijnProductGraph.isInjective();
                    } else {
                        is_injective = false;
                    }
                }

                // should we output this result?
                boolean do_output = (output_all || (is_surjective && output_surjective) || (is_injective && output_injective));

                // generate .viz file for dot?
                if (generate_graph) {
                    if (do_output) {
                        try {
                            ALGORITHM.PrintBruijnProductGraph.printToFile(ALGORITHM.InOutput.getImageFileName(is_injective, is_surjective) + ".viz");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e, "INTERNAL ERROR when creating the image file name", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

                if ((error != 0) && JOptionPane.showConfirmDialog(null, "There was an error either calling the plugin or in the calculation itself [error " + error + "].\n" + calling_string[0] + " " + calling_string[1] + " " + calling_string[2] + " " + calling_string[3] + "\n(You can ignore this if you have manually canceled the calculation)\n\nContinue?", "Error in Plugin", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == 1) {
                    break;
                }

                // output in a window 
                // TODO old procedure... needs an update
                if ((!add_to_database) && (do_output)) {
                    String s = "rule " + ALGORITHM.Function.getSignificantWolframRuleNumber() + ", neighborhood " + ALGORITHM.Neighborhood.getSignificantNeighborhoodString() + ", cell states " + ALGORITHM.CellStates.getNumberOfCellStates() + "\n";
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
                    if (JOptionPane.showConfirmDialog(null, s + "\n\nContinue?", "Result", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == 1) {
                        break;
                    }
                }

                // output to database if it is not already in it
                if (do_output && add_to_database &&
                        (!check_duplicates || (main.results.contains(ALGORITHM.InOutput.getParameterArray(generate_graph)) == -1))) {

                    // create entry and add it
                    Object[] row = new Object[]{
                        ALGORITHM.Function.getSignificantWolframRuleNumber(),
                        output_boolean ? ALGORITHM.Function.getSignificantBooleanRule() : new String(""),
                        output_polynomial ? ALGORITHM.Function.getSignificantPolynomialRule() : new String(""),
                        ALGORITHM.Neighborhood.getSignificantNeighborhoodString(),
                        ALGORITHM.Neighborhood.getNeighborhoodSize(),
                        ALGORITHM.Neighborhood.getSignificantNeighborhoodSize(),
                        ALGORITHM.CellStates.getNumberOfCellStates(),
                        is_injective,
                        is_surjective,
                        new Boolean(false),
                        new Boolean(false),
                        new Boolean(false)
                    };
                    row[GUI.ResultsTable.GRAPH_INDEX] = new Boolean(generate_graph || (new File(ALGORITHM.InOutput.getImageFileName(row) + ".viz")).exists());
                    row[GUI.ResultsTable.IMAGE_INDEX] = new Boolean((new File(ALGORITHM.InOutput.getImageFileName(row) + ".viz.png")).exists());
                    row[GUI.ResultsTable.SIMULATOR_INDEX] = new Boolean((new File(ALGORITHM.InOutput.getSimulationFileName(row))).exists());

                    main.results.addRow(row);
                }
                
                // call update bar, check for interrupt
                ALGORITHM.Iteration.callback.forwardUpdateBar(ALGORITHM.Function.getCurrentProblemSize());
                if (ALGORITHM.Iteration.update) {
                    ALGORITHM.Iteration.update = false;
                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

                try {
                    // repeat until we find a parameter configuration we haven't already in our database
                    if (check_duplicates) {
                        boolean has_next_permutation = true;
                        while ((has_next_permutation = ALGORITHM.Iteration.nextParameterConfiguration()) && 
                                (main.results.contains(ALGORITHM.InOutput.getParameterArray(generate_graph)) >= 0)) 
                        {}
                        
                        if (!has_next_permutation) {
                            break;
                        }
                        
                    } else {
                        // cancel if there are no more parameter configuration
                        if (!ALGORITHM.Iteration.nextParameterConfiguration()) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e, "Error creating permutation", JOptionPane.ERROR_MESSAGE);
                    break;
                }

            } while (true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error accessing Function", JOptionPane.ERROR_MESSAGE);
        }

        // clear up and return
        ALGORITHM.BruijnProductGraph.clearMemory();
        if (generate_graph) {
            ALGORITHM.PrintBruijnProductGraph.clearAdditionalMemory();
        }
        main.clearUpAfterCalculation();

    }
}
