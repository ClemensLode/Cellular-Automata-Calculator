/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.math.BigInteger;

/**
 *
 * @author Clemens Lode
 */
public class CalculationThread extends Thread {

        private class CallBack implements ALGORITHM.Function.CallBack {

            JProgressBar progressBar = null;
            JLabel remainingTime = null;

            private void update() {
                if (ALGORITHM.Function.lastUpdate.subtract(ALGORITHM.Function.progress).compareTo(ALGORITHM.Function.SPEED_FACTOR) > 0) {
                    if (java.lang.System.currentTimeMillis() != ALGORITHM.Function.lastUpdateTime) {
                        ALGORITHM.Function.SPEED_FACTOR = ALGORITHM.Function.SPEED_FACTOR.multiply(BigInteger.valueOf(9)).add(
                                ALGORITHM.Function.lastUpdate.subtract(ALGORITHM.Function.progress).multiply(BigInteger.valueOf(500)).divide(BigInteger.valueOf(java.lang.System.currentTimeMillis() - ALGORITHM.Function.lastUpdateTime))).divide(BigInteger.valueOf(10));
                        ALGORITHM.Function.lastUpdateTime = java.lang.System.currentTimeMillis();
                        ALGORITHM.Function.lastUpdate = ALGORITHM.Function.progress;
                        ALGORITHM.Function.update = true;
                    }
                    progressBar.setValue(1000 - ALGORITHM.Function.getNumberOfRemainingPermutations().multiply(BigInteger.valueOf(1000)).divide(ALGORITHM.Function.getTotalCalculationSteps()).intValue());
                    remainingTime.setText(ALGORITHM.Function.getRemainingNeededTimeString().toString()); //getNumberOfRemainingPermutationsString());
                }
            }

            public void updateBar(int my_progress) {
                ALGORITHM.Function.progress = ALGORITHM.Function.progress.subtract(BigInteger.valueOf(my_progress));
                update();
            }

            public void forwardUpdateBar(int my_progress) {
                if (ALGORITHM.Function.progress.mod(BigInteger.valueOf(my_progress)).equals(BigInteger.ZERO)) {
                    ALGORITHM.Function.progress = ALGORITHM.Function.progress.subtract(BigInteger.valueOf(my_progress));
                } else {
                    ALGORITHM.Function.progress = ALGORITHM.Function.progress.subtract(ALGORITHM.Function.progress.mod(BigInteger.valueOf(my_progress)));
                }
                update();
            }
        }
        
        Main main = null;
        CallBack callback = null;
        boolean generate_graph;
        boolean use_fast_cpp;
        boolean output_all;
        boolean output_surjective;
        boolean output_injective;
        boolean add_to_database;
        boolean check_duplicates;
            
        public void init_thread(Main main_window, JProgressBar progress_bar, JLabel remaining_time, boolean is_generate_graph, boolean is_use_fast_cpp, boolean is_output_all, boolean is_output_surjective, boolean is_output_injective, boolean is_add_to_database, boolean is_check_duplicates) {
            main = main_window;
            callback = new CallBack();
            callback.progressBar = progress_bar;
            callback.remainingTime = remaining_time;
            ALGORITHM.Function.callback = callback;

            generate_graph = is_generate_graph;
            use_fast_cpp = is_use_fast_cpp;
            output_all = is_output_all;
            output_surjective = is_output_surjective;
            output_injective = is_output_injective;
            add_to_database = is_add_to_database;
            check_duplicates = is_check_duplicates;
        }

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

                    if (use_fast_cpp) {
                        calling_string[1] = ALGORITHM.Function.getSignificantFunctionString();
                        calling_string[2] = "" + ALGORITHM.Function.getNumberOfCellStates();
                        calling_string[3] = ALGORITHM.Neighborhood.getBinaryNeighborhoodString();
                        int return_value = 0;
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

                    } else {
                        ALGORITHM.BruijnProductGraph.initStatic();
                        if (generate_graph) {
                            ALGORITHM.PrintBruijnProductGraph.runConnectionTest();
                        } else {
                            ALGORITHM.FastBruijnProductGraph.runConnectionTest();
                        }

                        is_surjective = ALGORITHM.BruijnProductGraph.isSurjective();
                        if (is_surjective) {
                            is_injective = ALGORITHM.BruijnProductGraph.isInjective();
                        } else {
                            is_injective = false;
                        }
                    }

                    boolean do_output = (output_all || (is_surjective && output_surjective) || (is_injective && output_injective));

                    if (generate_graph) {
                        if (do_output) {
                            try {
                                ALGORITHM.PrintBruijnProductGraph.printToFile(ALGORITHM.Function.getImageFileName(is_injective, is_surjective) + ".viz");
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, e, "INTERNAL ERROR when creating the image file name", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                    if ((error != 0) && JOptionPane.showConfirmDialog(null, "There was an error either calling the plugin or in the calculation itself [error " + error + "].\n" + calling_string[0] + " " + calling_string[1] + " " + calling_string[2] + " " + calling_string[3] + "\n(You can ignore this if you have manually canceled the calculation)\n\nContinue?", "Error in Plugin", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == 1) {
                        break;
                    }

                    if ((!add_to_database) && (do_output)) {
                        String s = "rule " + ALGORITHM.Function.getCurrentSignificantRuleNumber() + ", neighborhood " + ALGORITHM.Neighborhood.getNeighborhoodString() + ", cell states " + ALGORITHM.Function.getNumberOfCellStates() + "\n";
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

                    if (do_output && add_to_database && 
                            (!check_duplicates ||  main.results.contains(ALGORITHM.Function.getParameterArray(generate_graph)))) {
                        
                         
                        Object[] row = new Object[]{
                            ALGORITHM.Function.getCurrentSignificantRuleNumber(),
                            ALGORITHM.Neighborhood.getNeighborhoodString(),
                            ALGORITHM.Neighborhood.getNeighborhoodSize(),
                            ALGORITHM.Neighborhood.getSignificantNeighborhoodSize(),
                            ALGORITHM.Function.getNumberOfCellStates(),
                            is_injective,
                            is_surjective,
                            new Boolean(false),
                            new Boolean(false)
                        };
                        row[7] = new Boolean(  generate_graph || (new File(ALGORITHM.Function.getImageFileName(row) + ".viz") ).exists() );
                        row[8] = new Boolean((new File(ALGORITHM.Function.getImageFileName(row) + ".viz.png")).exists());
                        
                        main.results.addRow(row);
                    }
                    callback.forwardUpdateBar(ALGORITHM.Function.getCurrentProblemSize());
                    if (ALGORITHM.Function.update) {
                        ALGORITHM.Function.update = false;
                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }

                    try {
                        if(check_duplicates) {
                            boolean has_next_permutation = true;
                            while((has_next_permutation = ALGORITHM.Function.nextParameterPermutation()) && main.results.contains(ALGORITHM.Function.getParameterArray(generate_graph)));
                            if(!has_next_permutation) {
                                break;
                            }
                        } else 
                            if (!ALGORITHM.Function.nextParameterPermutation()) {
                                  break;
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e, "INTERNAL ERROR when creating next permutation", JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                } while (true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e, "INTERNAL ERROR when accessing / creating Function", JOptionPane.ERROR_MESSAGE);
            }
            ALGORITHM.BruijnProductGraph.clearMemory();

            if (generate_graph) {
                ALGORITHM.PrintBruijnProductGraph.clearAdditionalMemory();
            }
            main.clearUpAfterCalculation();

        }
}
