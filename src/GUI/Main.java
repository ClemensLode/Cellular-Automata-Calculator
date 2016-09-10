package GUI;
/*
 * Main.java
 *
 * Created on January 30, 2007, 5:33 AM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.math.BigInteger;


public class Main extends java.awt.Frame {
    
    class CalculationThread extends Thread {
        
        private class CallBack implements ALGORITHM.Function.CallBack {
            JProgressBar progressBar = null;
            JLabel remainingTime = null;
            public void updateBar(int my_progress) {
                ALGORITHM.Function.progress = ALGORITHM.Function.progress.subtract(BigInteger.valueOf(my_progress));
                progressBar.setValue(1000 - ALGORITHM.Function.progress.multiply(BigInteger.valueOf(1000)).divide(ALGORITHM.Function.getTotalCalculationSteps()).intValue());
                if(ALGORITHM.Function.lastUpdate.subtract(ALGORITHM.Function.progress).compareTo(BigInteger.ONE)>=0)
                {
                    ALGORITHM.Function.lastUpdate = ALGORITHM.Function.progress;
                    remainingTime.setText(ALGORITHM.Function.getRemainingNeededTimeString().toString());
                    ALGORITHM.Function.update = true;
                }
            }
            public void forwardUpdateBar(int my_progress) {
                if(ALGORITHM.Function.progress.mod(BigInteger.valueOf(my_progress)).equals(BigInteger.ZERO))
                    ALGORITHM.Function.progress = ALGORITHM.Function.progress.subtract(BigInteger.valueOf(my_progress));                   
                else
                    ALGORITHM.Function.progress = ALGORITHM.Function.progress.subtract(ALGORITHM.Function.progress.mod(BigInteger.valueOf(my_progress)));
                progressBar.setValue(1000 - ALGORITHM.Function.progress.multiply(BigInteger.valueOf(1000)).divide(ALGORITHM.Function.getTotalCalculationSteps()).intValue());
                if(ALGORITHM.Function.lastUpdate.subtract(ALGORITHM.Function.progress).compareTo(ALGORITHM.Function.SPEED_FACTOR)>=0)
                {
                    
                    ALGORITHM.Function.SPEED_FACTOR = ALGORITHM.Function.SPEED_FACTOR.multiply(BigInteger.valueOf(9)).add(
                            ALGORITHM.Function.lastUpdate.subtract(ALGORITHM.Function.progress).multiply(BigInteger.valueOf(1000)).divide(BigInteger.valueOf(java.lang.System.currentTimeMillis() - ALGORITHM.Function.lastUpdateTime))
                            ).divide(BigInteger.valueOf(10));
                    ALGORITHM.Function.lastUpdateTime = java.lang.System.currentTimeMillis();
                    
                    ALGORITHM.Function.lastUpdate = ALGORITHM.Function.progress;
                    remainingTime.setText(ALGORITHM.Function.getRemainingNeededTimeString().toString());
                    ALGORITHM.Function.update = true;
                }
            }
        }
        
        public void run() {
            boolean generate_graph = generateGraphCheckBox.isSelected();
            boolean output_all = outputAllRadioButton.isSelected();
            boolean output_surjective = outputSurjectiveRadioButton.isSelected();
            boolean output_injective = outputOnlyInjectiveRadioButton.isSelected();
            boolean add_to_database = addToDatabaseCheckBox.isSelected();
            
            boolean test_all_rules = ALGORITHM.Function.isTestAllRules();
/*            boolean test_all_neighborhoods = ALGORITHM.Function.isTestAllNeighborhoods();
            boolean test_all_significant_neighborhoods = ALGORITHM.Function.isTestAllSignificantNeighborhoods();
            boolean test_all_cell_states = ALGORITHM.Function.isTestAllNumbersOfCellStates();*/ // TODO
            
            ALGORITHM.Function.testIfSingleCalculation();
            ALGORITHM.Function.initializeMinimalNeighborhoodValue();
            
            CallBack callback = new CallBack();
            ALGORITHM.Function.progress = ALGORITHM.Function.getTotalCalculationSteps();
            ALGORITHM.Function.lastUpdate = ALGORITHM.Function.progress;
            ALGORITHM.Function.lastUpdateTime = java.lang.System.currentTimeMillis();
            ALGORITHM.Function.SPEED_FACTOR = BigInteger.valueOf(500000);
            callback.progressBar = progressBar;
            callback.remainingTime = neededTimeField;
            ALGORITHM.Function.callback = callback;           
            
            //TODO create standard (minimal) neighborhood and rule
            
          /*  
            ruleField.setEnabled(false);
            neighborhoodField.setEnabled(false);
            neighborhoodSizeField.setEnabled(false);
            significantNeighborhoodSizeField.setEnabled(false);
            numberOfCellStatesField.setEnabled(false);
*/ // TODO
          try{  
            do
            {
                boolean is_surjective = false;
                boolean is_injective = false;
                if(generate_graph) {
                    ALGORITHM.PrintBruijnProductGraph.initStatic();
                    ALGORITHM.PrintBruijnProductGraph.runConnectionTest();
                    
                    is_surjective = ALGORITHM.PrintBruijnProductGraph.isSurjective();
                    if(is_surjective)
                        is_injective = ALGORITHM.PrintBruijnProductGraph.isInjective();
                } else {
                    ALGORITHM.FastBruijnProductGraph.initStatic();
                    ALGORITHM.FastBruijnProductGraph.runConnectionTest();
                    
                    is_surjective = ALGORITHM.FastBruijnProductGraph.isSurjective();
                    if(is_surjective)
                        is_injective = ALGORITHM.FastBruijnProductGraph.isInjective();
                    ALGORITHM.FastBruijnProductGraph.resetStatic();
                }

                boolean do_output = (output_all || (is_surjective && output_surjective) || (is_injective && output_injective));
                
                
                if(generate_graph) {
                    if(do_output)
                    {
                        try{
                            ALGORITHM.PrintBruijnProductGraph.printToFile(ALGORITHM.Function.getImageFileName(is_injective, is_surjective) + ".viz");
                        } catch(ALGORITHM.Function.FunctionException e) {
                            JOptionPane.showMessageDialog(null, "INTERNAL ERROR when creating the image file name", "INTERNAL ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    ALGORITHM.PrintBruijnProductGraph.resetStatic();
                }
                                
                if(/*(!test_all_rules && ) || */((!add_to_database)&&(do_output))) { // TODO
                        String s = "rule " + ALGORITHM.Function.getSignificantRuleNumber();
                        if(!is_surjective) s += "   -- NOT surjective and ";
                        else s += "   --     SURJECTIVE and ";
                        if(!is_injective) s += "NOT injective";
                        else s += "    INJECTIVE";
//                if(!ALGORITHM.Function.isTestAllRules())
//                    JOptionPane.showMessageDialog(null, "[" + ALGORITHM.Function.getCurrentProblemSize() + " / " + ALGORITHM.Function.getProblemSize(ALGORITHM.Function.getNumberOfCellStates(), ALGORITHM.Function.getNeighborhoodSize()) + "] " + ALGORITHM.Function.progress + " / " + ALGORITHM.Function.getTotalCalculationSteps(), "Result", JOptionPane.INFORMATION_MESSAGE);
  /*              else if(!addToDatabaseCheckBox.isSelected())
                {*/
                    if(JOptionPane.showConfirmDialog(null, s + "\n\nContinue?", "Result", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == 1)
                        break;
                /*}*/
                }
                
                if(do_output && add_to_database) {
                    Object[] row = new Object[] {
                        ALGORITHM.Function.getSignificantRuleNumber(),
                        ALGORITHM.Function.getNeighborhoodString(),
                        ALGORITHM.Function.getNeighborhoodSize(),
                        ALGORITHM.Function.getSignificantNeighborhoodSize(),
                        ALGORITHM.Function.getNumberOfCellStates(),
                        is_injective,
                        is_surjective,
                        generate_graph
                    };
                    results.addRow(row);
                }
                callback.forwardUpdateBar(ALGORITHM.Function.getCurrentProblemSize());
                if(ALGORITHM.Function.update)
                {
                    ALGORITHM.Function.update = false;
                    try{
                        sleep(1);
                    } catch(InterruptedException e) {
                        break;
                    }
                }
                
                try{
                    if(!ALGORITHM.Function.nextParameterPermutation())
                        break;
                } catch(ALGORITHM.Function.FunctionException e) {
                   JOptionPane.showMessageDialog(null, "INTERNAL ERROR when creating next permutation", "INTERNAL ERROR", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                
            } while(true);
          } catch(ALGORITHM.Function.FunctionException e) {
                JOptionPane.showMessageDialog(null, "INTERNAL ERROR when accessing / creating Function", "INTERNAL ERROR", JOptionPane.ERROR_MESSAGE);              
          }
            results.fireTableStructureChanged(); // todo?
            stopCalculationButton.setEnabled(false);
            startCalculationButton.setEnabled(true);
            neededTimeLabel.setText("Needed calculation time");
            neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());
            progressBar.setValue(1000);
        }
    }
    
    CalculationThread calculation_thread;
    ResultsTable results = new ResultsTable();
    TableSorter sorter = new TableSorter(results);
    
    
    /** Creates new form Main */
    public Main() {
        initComponents();

        try{
            ALGORITHM.Function.createStandardSignificantFunction();
        } catch(ALGORITHM.Function.FunctionException e) {
            // TODO
        }

        significantNeighborhoodSizeField.setText("" + ALGORITHM.Function.getSignificantNeighborhoodSize());
        significantNeighborhoodSizeField.setEnabled(false);
        
        neighborhoodSizeField.setText("" + ALGORITHM.Function.getNeighborhoodSize());
        neighborhoodSizeField.setEnabled(false);
        
        neighborhoodField.setText("" + ALGORITHM.Function.getNeighborhoodString());
        
        numberOfCellStatesField.setText("" + ALGORITHM.Function.getNumberOfCellStates());
        ruleField.setText(ALGORITHM.Function.getSignificantRuleNumber().toString());
        
        
        neededMemoryField.setText("" + ALGORITHM.Function.getNeededMemorySizeString());
        neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());
        
        results.fireTableStructureChanged();
        
        resultTable.setModel(sorter);
        sorter.setTableHeader(resultTable.getTableHeader());
        resultTable.getTableHeader().setToolTipText(
                "Click to specify sorting; Control-Click to specify secondary sorting");
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        chooseOutputInjectiveSurjectiveButtonGroup = new javax.swing.ButtonGroup();
        fileChooser = new javax.swing.JFileChooser();
        automationButtonGroup = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        neighborhoodLabel = new java.awt.Label();
        neighborhoodSizeLabel = new java.awt.Label();
        significantNeighborhoodSizeLabel = new java.awt.Label();
        neighborhoodSizeField = new javax.swing.JTextField();
        significantNeighborhoodSizeField = new javax.swing.JTextField();
        neighborhoodField = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        testSingleNeighborhoodRadioButton = new javax.swing.JRadioButton();
        testAllNeighborhoodPermutationsRadioButton = new javax.swing.JRadioButton();
        testAllNeighborhoodSizesRadioButton = new javax.swing.JRadioButton();
        testAllNeighborhoodsRadioButton = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        neededTimeLabel = new javax.swing.JLabel();
        neededTimeField = new javax.swing.JLabel();
        neededMemoryLabel = new javax.swing.JLabel();
        neededMemoryField = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        progressBarLabel = new javax.swing.JLabel();
        stopCalculationButton = new javax.swing.JButton();
        startCalculationButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        testAllRulesCheckBox = new javax.swing.JCheckBox();
        ruleLabel = new java.awt.Label();
        numberOfCellStatesLabel = new java.awt.Label();
        numberOfCellStatesField = new javax.swing.JTextField();
        ruleField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        outputAllRadioButton = new javax.swing.JRadioButton();
        outputSurjectiveRadioButton = new javax.swing.JRadioButton();
        outputOnlyInjectiveRadioButton = new javax.swing.JRadioButton();
        generateGraphCheckBox = new javax.swing.JCheckBox();
        addToDatabaseCheckBox = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();
        eraseMarkedEntriesButton = new javax.swing.JButton();
        saveResultsButton = new javax.swing.JButton();
        loadResultsButton = new javax.swing.JButton();
        showGraphButton = new javax.swing.JButton();

        setBackground(java.awt.Color.lightGray);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        setFocusTraversalPolicyProvider(true);
        setTitle("Test cellular automata on surjectivity and injectivity");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Neighborhood Configuration"));
        neighborhoodLabel.setForeground(java.awt.Color.black);
        neighborhoodLabel.setText("Neighborhood");
        neighborhoodLabel.getAccessibleContext().setAccessibleName("neighborhoodLabel");

        neighborhoodSizeLabel.setForeground(java.awt.Color.black);
        neighborhoodSizeLabel.setText("Neighborhood Size");

        significantNeighborhoodSizeLabel.setForeground(java.awt.Color.black);
        significantNeighborhoodSizeLabel.setText("Significant Neighborhood Size");

        neighborhoodSizeField.setText("2");
        neighborhoodSizeField.setFocusCycleRoot(true);
        neighborhoodSizeField.setFocusTraversalPolicyProvider(true);
        neighborhoodSizeField.setPreferredSize(new java.awt.Dimension(80, 20));
        neighborhoodSizeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                neighborhoodSizeFieldFocusLost(evt);
            }
        });

        significantNeighborhoodSizeField.setText("2");
        significantNeighborhoodSizeField.setFocusCycleRoot(true);
        significantNeighborhoodSizeField.setFocusTraversalPolicyProvider(true);
        significantNeighborhoodSizeField.setPreferredSize(new java.awt.Dimension(80, 20));
        significantNeighborhoodSizeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                significantNeighborhoodSizeFieldFocusLost(evt);
            }
        });

        neighborhoodField.setText("0, 1");
        neighborhoodField.setToolTipText("Neighborhood configuration in the form '0,1,5,7' etc.");
        neighborhoodField.setFocusCycleRoot(true);
        neighborhoodField.setFocusTraversalPolicyProvider(true);
        neighborhoodField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                neighborhoodFieldFocusLost(evt);
            }
        });
        neighborhoodField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                neighborhoodFieldKeyPressed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Automatic tests"));
        automationButtonGroup.add(testSingleNeighborhoodRadioButton);
        testSingleNeighborhoodRadioButton.setSelected(true);
        testSingleNeighborhoodRadioButton.setText("single neighborhood");
        testSingleNeighborhoodRadioButton.setToolTipText("Test a single neighborhood given in the \"Neighborhood\" field");
        testSingleNeighborhoodRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        testSingleNeighborhoodRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        testSingleNeighborhoodRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testSingleNeighborhoodRadioButtonItemStateChanged(evt);
            }
        });

        automationButtonGroup.add(testAllNeighborhoodPermutationsRadioButton);
        testAllNeighborhoodPermutationsRadioButton.setText("all neighborhood permutations");
        testAllNeighborhoodPermutationsRadioButton.setToolTipText("Test all neighborhood permutations within the given range and specified number of positions in \"Neighborhood Size\" and \"Significant Neighborhood Size\"");
        testAllNeighborhoodPermutationsRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        testAllNeighborhoodPermutationsRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        testAllNeighborhoodPermutationsRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testAllNeighborhoodPermutationsRadioButtonItemStateChanged(evt);
            }
        });

        automationButtonGroup.add(testAllNeighborhoodSizesRadioButton);
        testAllNeighborhoodSizesRadioButton.setText("all neighborhood sizes");
        testAllNeighborhoodSizesRadioButton.setToolTipText("Test all neighborhood permutations up to the size specified in \"Neighborhood Size\" with the given number of positions in \"Significant Neighborhood Size\"");
        testAllNeighborhoodSizesRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        testAllNeighborhoodSizesRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        testAllNeighborhoodSizesRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testAllNeighborhoodSizesRadioButtonItemStateChanged(evt);
            }
        });

        automationButtonGroup.add(testAllNeighborhoodsRadioButton);
        testAllNeighborhoodsRadioButton.setText("all neighborhoods");
        testAllNeighborhoodsRadioButton.setToolTipText("Test all neighborhood configurations up to the size specified in \"Neighborhood Size\"");
        testAllNeighborhoodsRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        testAllNeighborhoodsRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        testAllNeighborhoodsRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testAllNeighborhoodsRadioButtonItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(testSingleNeighborhoodRadioButton)
            .add(testAllNeighborhoodPermutationsRadioButton)
            .add(testAllNeighborhoodSizesRadioButton)
            .add(testAllNeighborhoodsRadioButton)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(testSingleNeighborhoodRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testAllNeighborhoodPermutationsRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testAllNeighborhoodSizesRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testAllNeighborhoodsRadioButton))
        );

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(neighborhoodLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(neighborhoodField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(significantNeighborhoodSizeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(neighborhoodSizeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, significantNeighborhoodSizeField, 0, 0, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, neighborhoodSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(15, 15, 15)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(significantNeighborhoodSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(significantNeighborhoodSizeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(neighborhoodSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(neighborhoodSizeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(neighborhoodField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(neighborhoodLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Calculation"));
        neededTimeLabel.setText("Needed calculation time");

        neededTimeField.setText("0s");
        neededTimeField.setToolTipText("Estimated time for the whole calculation, including testing of multiple rules/neighborhoods/cell states");

        neededMemoryLabel.setText("Needed Memory");

        neededMemoryField.setText("0mb");
        neededMemoryField.setToolTipText("Upper limit of memory that will be needed for the calculation");

        progressBar.setStringPainted(true);

        progressBarLabel.setText("Calculation Progress");

        stopCalculationButton.setText("Stop Calculation");
        stopCalculationButton.setEnabled(false);
        stopCalculationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stopCalculationButtonMouseClicked(evt);
            }
        });

        startCalculationButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        startCalculationButton.setText("Start Calculation");
        startCalculationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startCalculationButtonMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(neededMemoryLabel)
                    .add(neededTimeLabel)
                    .add(startCalculationButton)
                    .add(progressBarLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(neededMemoryField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(neededTimeField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 141, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(stopCalculationButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(neededTimeLabel)
                    .add(neededTimeField))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(neededMemoryLabel)
                    .add(neededMemoryField))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(progressBarLabel))
                .add(11, 11, 11)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(stopCalculationButton)
                    .add(startCalculationButton))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Rule Configuration"));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Automation"));
        testAllRulesCheckBox.setText("test all balanced Rules");
        testAllRulesCheckBox.setToolTipText("Test all rules that fit into the given 'Significant Neighborhood Size'");
        testAllRulesCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        testAllRulesCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        testAllRulesCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testAllRulesCheckBoxItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(testAllRulesCheckBox)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(testAllRulesCheckBox)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ruleLabel.setForeground(java.awt.Color.black);
        ruleLabel.setText("Rule Number");

        numberOfCellStatesLabel.setForeground(java.awt.Color.black);
        numberOfCellStatesLabel.setText("Number of Cell States");

        numberOfCellStatesField.setText("2");
        numberOfCellStatesField.setFocusCycleRoot(true);
        numberOfCellStatesField.setFocusTraversalPolicyProvider(true);
        numberOfCellStatesField.setPreferredSize(new java.awt.Dimension(80, 20));
        numberOfCellStatesField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numberOfCellStatesFieldFocusLost(evt);
            }
        });

        ruleField.setText("42");
        ruleField.setFocusCycleRoot(true);
        ruleField.setFocusTraversalPolicyProvider(true);
        ruleField.setPreferredSize(new java.awt.Dimension(80, 20));
        ruleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ruleFieldFocusLost(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel8Layout.createSequentialGroup()
                        .add(ruleLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(ruleField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 193, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel8Layout.createSequentialGroup()
                        .add(numberOfCellStatesLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(numberOfCellStatesField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel8Layout.createSequentialGroup()
                        .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(numberOfCellStatesField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(numberOfCellStatesLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(ruleLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(ruleField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Output options"));
        chooseOutputInjectiveSurjectiveButtonGroup.add(outputAllRadioButton);
        outputAllRadioButton.setText("All");
        outputAllRadioButton.setToolTipText("Report all results");
        outputAllRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        outputAllRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        chooseOutputInjectiveSurjectiveButtonGroup.add(outputSurjectiveRadioButton);
        outputSurjectiveRadioButton.setSelected(true);
        outputSurjectiveRadioButton.setText("At least surjective");
        outputSurjectiveRadioButton.setToolTipText("Only report those results that are either surjective or surjective and injective");
        outputSurjectiveRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        outputSurjectiveRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        chooseOutputInjectiveSurjectiveButtonGroup.add(outputOnlyInjectiveRadioButton);
        outputOnlyInjectiveRadioButton.setText("Only injective");
        outputOnlyInjectiveRadioButton.setToolTipText("Filter all results that are not surjective or not injective");
        outputOnlyInjectiveRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        outputOnlyInjectiveRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        generateGraphCheckBox.setText("Generate Graph");
        generateGraphCheckBox.setToolTipText("Generate File for GraphViz in order to later generate a graph file");
        generateGraphCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        generateGraphCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        generateGraphCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                generateGraphCheckBoxItemStateChanged(evt);
            }
        });

        addToDatabaseCheckBox.setSelected(true);
        addToDatabaseCheckBox.setText("Add result to database");
        addToDatabaseCheckBox.setToolTipText("Instead of a simple output all results will be put into a database (tab 'Results')");
        addToDatabaseCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        addToDatabaseCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(outputAllRadioButton)
                    .add(outputSurjectiveRadioButton)
                    .add(outputOnlyInjectiveRadioButton)
                    .add(generateGraphCheckBox)
                    .add(addToDatabaseCheckBox))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(outputAllRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outputSurjectiveRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outputOnlyInjectiveRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(generateGraphCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(addToDatabaseCheckBox)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(9, 9, 9))
        );
        jTabbedPane1.addTab("Test", jPanel3);

        resultTable.setModel(results);
        int zzz = 1;
        jScrollPane1.setViewportView(resultTable);

        eraseMarkedEntriesButton.setText("Erase marked");
        eraseMarkedEntriesButton.setToolTipText("Press CTRL+A to mark all entries");
        eraseMarkedEntriesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eraseMarkedEntriesButtonMouseClicked(evt);
            }
        });

        saveResultsButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        saveResultsButton.setText("Save results...");
        saveResultsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveResultsButtonMouseClicked(evt);
            }
        });

        loadResultsButton.setText("Load results...");
        loadResultsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadResultsButtonMouseClicked(evt);
            }
        });

        showGraphButton.setText("Show Graph(s)");
        showGraphButton.setToolTipText("Display graphs of marked entries if availible");
        showGraphButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showGraphButtonMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(eraseMarkedEntriesButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(showGraphButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 60, Short.MAX_VALUE)
                        .add(loadResultsButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(saveResultsButton)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 340, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(eraseMarkedEntriesButton)
                    .add(saveResultsButton)
                    .add(loadResultsButton)
                    .add(showGraphButton))
                .add(135, 135, 135))
        );
        jTabbedPane1.addTab("Results", jPanel4);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 423, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void generateGraphCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_generateGraphCheckBoxItemStateChanged
        ALGORITHM.Function.setIsGenerateGraph(generateGraphCheckBox.isSelected());
    }//GEN-LAST:event_generateGraphCheckBoxItemStateChanged

    private void testAllRulesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testAllRulesCheckBoxItemStateChanged
        if(testAllRulesCheckBox.isSelected()) {
            ruleField.setText("all");
            ruleField.setEnabled(false);
        } else {
            ruleField.setEnabled(true);
            ruleField.setText(ALGORITHM.Function.getSignificantRuleNumber().toString());
        }
        ALGORITHM.Function.setTestAllRules(testAllRulesCheckBox.isSelected());
        neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());
    }//GEN-LAST:event_testAllRulesCheckBoxItemStateChanged
    
    private void testAllNeighborhoodsRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testAllNeighborhoodsRadioButtonItemStateChanged
        if(testAllNeighborhoodsRadioButton.isSelected()) {
            ALGORITHM.Function.setTestAllNeighborhoods(true);
            
            neighborhoodField.setText("all");
            neighborhoodField.setEnabled(false);
            
            neighborhoodSizeField.setEnabled(true);
            significantNeighborhoodSizeField.setEnabled(false);
            
            neighborhoodSizeField.setText("" + ALGORITHM.Function.getNeighborhoodSize());
            significantNeighborhoodSizeField.setText("all");
            neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());
        }
    }//GEN-LAST:event_testAllNeighborhoodsRadioButtonItemStateChanged
    
    private void testAllNeighborhoodSizesRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testAllNeighborhoodSizesRadioButtonItemStateChanged
        if(testAllNeighborhoodSizesRadioButton.isSelected()) {
            ALGORITHM.Function.setTestAllNeighborhoodSizesAndPermutations(true);
            
            neighborhoodField.setText("all");
            neighborhoodField.setEnabled(false);
            
            neighborhoodSizeField.setEnabled(true);
            significantNeighborhoodSizeField.setEnabled(true);
            
            neighborhoodSizeField.setText("" + ALGORITHM.Function.getNeighborhoodSize());
            significantNeighborhoodSizeField.setText("" + ALGORITHM.Function.getSignificantNeighborhoodSize());
            neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());            
        }
    }//GEN-LAST:event_testAllNeighborhoodSizesRadioButtonItemStateChanged
    
    private void testAllNeighborhoodPermutationsRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testAllNeighborhoodPermutationsRadioButtonItemStateChanged
        if(testAllNeighborhoodPermutationsRadioButton.isSelected()) {
            ALGORITHM.Function.setTestAllNeighborhoodSizesAndPermutations(false);
            ALGORITHM.Function.setTestAllNeighborhoodPermutations(true);
            
            neighborhoodField.setText("all");
            neighborhoodField.setEnabled(false);
            
            neighborhoodSizeField.setEnabled(true);
            significantNeighborhoodSizeField.setEnabled(true);
            
            neighborhoodSizeField.setText("" + ALGORITHM.Function.getNeighborhoodSize());
            significantNeighborhoodSizeField.setText("" + ALGORITHM.Function.getSignificantNeighborhoodSize());
            neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());            
        }
    }//GEN-LAST:event_testAllNeighborhoodPermutationsRadioButtonItemStateChanged
    
    private void testSingleNeighborhoodRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testSingleNeighborhoodRadioButtonItemStateChanged
        if(testSingleNeighborhoodRadioButton.isSelected()) {
            ALGORITHM.Function.setSingleTestCase(true);
            
            neighborhoodField.setEnabled(true);
            
            neighborhoodSizeField.setEnabled(false);
            significantNeighborhoodSizeField.setEnabled(false);
            
            neighborhoodField.setText(ALGORITHM.Function.getNeighborhoodString());
            significantNeighborhoodSizeField.setText("" + ALGORITHM.Function.getSignificantNeighborhoodSize());
            neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());            
        }
    }//GEN-LAST:event_testSingleNeighborhoodRadioButtonItemStateChanged
    
    private void ruleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ruleFieldFocusLost
        try {
            try {
                ALGORITHM.Function.createSignificantFunction(new BigInteger(ruleField.getText()));
            } catch(NumberFormatException n) {
                JOptionPane.showMessageDialog(this, "Please enter only numerals into the Rule Field", "Wrong number format", JOptionPane.ERROR_MESSAGE);
                ruleField.requestFocusInWindow();
            }
        } catch(ALGORITHM.Function.FunctionException e) {
            JOptionPane.showMessageDialog(this, "Rule Number is out of range, please increase significant neighborhood size", "Rule Number out of range", JOptionPane.ERROR_MESSAGE);
            ruleField.requestFocusInWindow();
        }
            ruleField.setText(ALGORITHM.Function.getSignificantRuleNumber().toString());
    }//GEN-LAST:event_ruleFieldFocusLost
    
    private void neighborhoodSizeFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_neighborhoodSizeFieldFocusLost
        try {
            try{
                int result = ALGORITHM.Function.createStandardNeighborhoodRange(neighborhoodSizeField.getText());
                        //Integer.parseInt(significantNeighborhoodSizeField.getText()), Integer.parseInt(neighborhoodSizeField.getText()));
                switch(result) {
                    case 0:break;
                    case -1:
                        JOptionPane.showMessageDialog(this, "Invalid neighborhood size range, the lower limit of the neighborhood size must be smaller than the upper limit (e.g. '3-5').", "Invalid neighborhood size range", JOptionPane.ERROR_MESSAGE);
                        break;
                    case -2:
                        JOptionPane.showMessageDialog(this, "Neighborhood size is too large, a calculation would need too much memory.", "Neighborhood size too large", JOptionPane.ERROR_MESSAGE);
                        break;
                    case -3:
                        JOptionPane.showMessageDialog(this, "Invalid neighborhood size range, you need to enter either one entry or a lower and upper limit for the neighborhood size.", "Invalid neighborhood size range", JOptionPane.ERROR_MESSAGE);
                        break;
                    case -4:
                        JOptionPane.showMessageDialog(this, "INTERNAL ERROR, significant neighborhood size too small", "INTERNAL ERROR", JOptionPane.ERROR_MESSAGE);
                        break;
                    case -5:
                        JOptionPane.showMessageDialog(this, "Neighborhood Size can only be as small as Significant Neighborhood Size", "Neighborhood Size too small", JOptionPane.ERROR_MESSAGE);
                        break;
                }
                if(result != 0) {
                    if(significantNeighborhoodSizeField.getText() != "all")
                        significantNeighborhoodSizeField.setText(""+ALGORITHM.Function.getSignificantNeighborhoodSize());
                    neighborhoodSizeField.setText(""+ALGORITHM.Function.getNeighborhoodSize());
                    neighborhoodSizeField.requestFocusInWindow();
                }
            } catch(ALGORITHM.Function.FunctionException e) {
                JOptionPane.showMessageDialog(this, "INTERNAL ERROR setting neighborhood", "INTERNAL ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals into the Neighborhood Size field", "Wrong number format", JOptionPane.ERROR_MESSAGE);
            neighborhoodSizeField.setText(""+ALGORITHM.Function.getNeighborhoodSize());
            if(significantNeighborhoodSizeField.getText() != "all")
                significantNeighborhoodSizeField.setText("" + ALGORITHM.Function.getSignificantNeighborhoodSize());
            neighborhoodSizeField.requestFocusInWindow();
            return;
        }
        neededMemoryField.setText("" + ALGORITHM.Function.getNeededMemorySizeString());
        neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());
    }//GEN-LAST:event_neighborhoodSizeFieldFocusLost
    
    private void significantNeighborhoodSizeFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_significantNeighborhoodSizeFieldFocusLost
        try {
            try{
                int result = ALGORITHM.Function.createStandardNeighborhoodRange(Integer.parseInt(significantNeighborhoodSizeField.getText()), Integer.parseInt(neighborhoodSizeField.getText()));
                switch(result) {
                    case 0:break;
                    case -1:
                        JOptionPane.showMessageDialog(this, "Please enter a Significant Neighborhood Size larger than 2.\n The Significant Neighborhood Size is defined by the the number of neighborhood locations that the rule uses", "Significant Neighborhood Size out of range", JOptionPane.ERROR_MESSAGE);
                        break;
                    case -2:
                        JOptionPane.showMessageDialog(this, "Significant Neighborhood Size can only be as large as Neighborhood Size", "Significant Neighborhood Size too large", JOptionPane.ERROR_MESSAGE);
                        break;
                }
                if(result != 0) {
                    if(neighborhoodSizeField.getText() != "all")
                        neighborhoodSizeField.setText(""+ALGORITHM.Function.getNeighborhoodSize());
                    significantNeighborhoodSizeField.setText("" + ALGORITHM.Function.getSignificantNeighborhoodSize());
                    significantNeighborhoodSizeField.requestFocusInWindow();
                }
            } catch(ALGORITHM.Function.FunctionException e) {
                JOptionPane.showMessageDialog(this, "INTERNAL ERROR setting significant neighborhood", "INTERNAL ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals into the Significant Neighborhood Size field", "Wrong number format", JOptionPane.ERROR_MESSAGE);
            if(neighborhoodSizeField.getText() != "all")
                neighborhoodSizeField.setText(""+ALGORITHM.Function.getNeighborhoodSize());
            significantNeighborhoodSizeField.setText("" + ALGORITHM.Function.getSignificantNeighborhoodSize());
            significantNeighborhoodSizeField.requestFocusInWindow();
            return;
        }
        neededMemoryField.setText("" + ALGORITHM.Function.getNeededMemorySizeString());
        neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());
        if(ruleField.isEnabled())
        {
                ruleField.setText(ALGORITHM.Function.getSignificantRuleNumber().toString());        
        }
    }//GEN-LAST:event_significantNeighborhoodSizeFieldFocusLost
    
    private void numberOfCellStatesFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numberOfCellStatesFieldFocusLost
        try {
        try {
            int result = ALGORITHM.Function.setNumberOfCellStatesRange(numberOfCellStatesField.getText());
            switch(result) {
                case -1:
                    JOptionPane.showMessageDialog(this, "Number of Cell States out of range.\n" +
                            "Please use a value larger than " + ALGORITHM.Function.MIN_NUMBER_OF_CELL_STATES + " and when specifing a range put the lower limit first (e.g. '3-5' instead of '5-3').", "Number of Cell States out of range", JOptionPane.ERROR_MESSAGE);
                    break;
                case -2: 
                    JOptionPane.showMessageDialog(this, "Number of Cell States out of range.\n" + 
                            "Please use a value smaller than " + ALGORITHM.Function.calculateMaxNumberOfCellStates() + ".", "Number of Cell States out of range", JOptionPane.ERROR_MESSAGE);
                    break;
                case -3:
                    JOptionPane.showMessageDialog(this, "Wrong number of entries in the Cell States field.\n" +
                            "Please enter either a single value or two values parted by a '-' (e.g. '2-4').",
                            "Invalid number of entries",
                            JOptionPane.ERROR_MESSAGE);
                    break;
            }
            if(result != 0) {
                numberOfCellStatesField.setText(ALGORITHM.Function.getNumberOfCellStatesString());
                numberOfCellStatesField.requestFocusInWindow();
                return;
            }
            neededMemoryField.setText("" + ALGORITHM.Function.getNeededMemorySizeString());
            neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());
        } catch(ALGORITHM.Function.FunctionException e) {
               JOptionPane.showMessageDialog(this, "INTERNAL ERROR", "Error creating function with new cell state range", JOptionPane.ERROR_MESSAGE);            
        }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals into the Number of Cell States field",
                    "Wrong number format",
                    JOptionPane.ERROR_MESSAGE);
            
        }
        
        if(ruleField.isEnabled())
        {
                ruleField.setText(ALGORITHM.Function.getSignificantRuleNumber().toString());        
        }
    }//GEN-LAST:event_numberOfCellStatesFieldFocusLost
    
    private void neighborhoodFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_neighborhoodFieldFocusLost
        try {
            try{
                int result = ALGORITHM.Function.setSignificantNeighborhood(ALGORITHM.Function.parseSignificantNeighborhoodString(neighborhoodField.getText()));
                switch(result) {
                    case 0:break;
                    case -1:
                        JOptionPane.showMessageDialog(this, "Please enter a significant neighborhood size larger than 2.", "Significant Neighborhood Size out of range", JOptionPane.ERROR_MESSAGE);
                        break;
                    case -2:
                        JOptionPane.showMessageDialog(this, "Please check the order of your neighborhood", "Wrong order", JOptionPane.WARNING_MESSAGE);
//TODO evtl zulassen, nachfragen und selber ordnen
                        break;
                    case -3:
                        JOptionPane.showMessageDialog(this, "Neighborhood out of range, you need at least " + ALGORITHM.Function.MIN_NEIGHBORHOOD_SIZE + " different entries and\n at most a difference of " + (ALGORITHM.Function.calculateMaxNeighborhoodSize()-1) + " between first and last entry with the current number of cell states (e.g. '0, 1, 7' for a distance of 7).", "Neighborhood out of range", JOptionPane.WARNING_MESSAGE);
                        break;
                    default:JOptionPane.showMessageDialog(this, "INTERNAL ERROR", "Error Assigning Neighborhood", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                if(result != 0) {
                    neighborhoodField.setText(ALGORITHM.Function.getNeighborhoodString());
                    neighborhoodField.requestFocusInWindow();
                    return;
                }
            } catch(ALGORITHM.Function.FunctionException e) {
                JOptionPane.showMessageDialog(this, "INTERNAL ERROR when assigning the neighborhood", "INTERNAL ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals seperated by commas into the Neighborhood field", "Wrong number format", JOptionPane.ERROR_MESSAGE);
            neighborhoodField.setText(ALGORITHM.Function.getNeighborhoodString());
            neighborhoodField.requestFocusInWindow();
            return;
        }
        if(ruleField.isEnabled()) {
                ruleField.setText(ALGORITHM.Function.getSignificantRuleNumber().toString());        
        }
        if(neighborhoodSizeField.getText() != "all")
            neighborhoodSizeField.setText("" + ALGORITHM.Function.getNeighborhoodSize());
        if(significantNeighborhoodSizeField.getText() != "all")
            significantNeighborhoodSizeField.setText("" + ALGORITHM.Function.getSignificantNeighborhoodSize());

        neededMemoryField.setText("" + ALGORITHM.Function.getNeededMemorySizeString());
        neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());
    }//GEN-LAST:event_neighborhoodFieldFocusLost
    
    private void neighborhoodFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_neighborhoodFieldKeyPressed
//        if(evt.getKeyCode() == evt.VK_ENTER)
//            neighborhoodFieldFocusLost(null);
//        evt.
    }//GEN-LAST:event_neighborhoodFieldKeyPressed
    
    private void showGraphButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showGraphButtonMouseClicked
        int[] list = resultTable.getSelectedRows();
        Vector<Integer> graph_list = new Vector<Integer>();
        for(int i = 0; i < list.length; i++)
            if(((java.lang.Boolean)results.getValueAt(list[i], 7)) == true)
                graph_list.add(new Integer(list[i]));
        if(graph_list.size() == 0) {
            JOptionPane.showMessageDialog(this, "No entries with graphs selected", "No entry selected", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(graph_list.size() >= 16) {
            if(JOptionPane.showConfirmDialog(this, "This will open " + graph_list.size() + " new windows.\nContinue?",
                    "Confirm opening of multiple windows",
                    JOptionPane.YES_NO_OPTION) == 1) {
                return;
            }
        }
        for(int i = 0; i < graph_list.size(); i++) {
            String file_name = ALGORITHM.Function.getImageFileName(results.getRow(graph_list.get(i))) + ".png";
            try {
                BufferedImage image = ImageIO.read(new File(file_name));
                ImageRenderComponent irc = new ImageRenderComponent(image);
                irc.setOpaque(true);  // for use as a content pane
                JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                f.setContentPane(irc);
                f.setSize(400,400);
                f.setLocation(200,200);
                f.setVisible(true);
                
            } catch(IOException e) {
                //JOptionPane.showMessageDialog(this, "Error opening etc.", "No entry selected", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            //JOptionPane.showMessageDialog(this, file_name, file_name, JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_showGraphButtonMouseClicked
    
    private void startCalculationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startCalculationButtonMouseClicked
        ALGORITHM.Function.updateTotalSteps();
        try {
        if(JOptionPane.showConfirmDialog(this, "Start calculation with the following parameters:\n" + ALGORITHM.Function.getParameterString(),
                "Confirm parameters",
                JOptionPane.YES_NO_OPTION) == 1) {
            return;
        }
        } catch(ALGORITHM.Function.FunctionException e) {
            JOptionPane.showMessageDialog(null, "INTERNAL ERROR when creating the parameter string", "INTERNAL ERROR", JOptionPane.ERROR_MESSAGE);
        }
        stopCalculationButton.setEnabled(true);
        startCalculationButton.setEnabled(false);
        neededTimeLabel.setText("Remaining calculation time");
        // ... calculation, progress bar, extra thread?
        
        progressBar.setMaximum(1000);
        progressBar.setValue(0);
        
// start seperate calculation thread
        if((calculation_thread != null) && (calculation_thread.isAlive()))
            calculation_thread.interrupt();
        while((calculation_thread != null) && (calculation_thread.isAlive())) {
        }
        calculation_thread = new CalculationThread();
        calculation_thread.start();
    }//GEN-LAST:event_startCalculationButtonMouseClicked
        
    private void loadResultsButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadResultsButtonMouseClicked
        if(fileChooser.showOpenDialog(this) == fileChooser.APPROVE_OPTION) {
            File my_file = fileChooser.getSelectedFile();
            if(!my_file.exists()) {
                JOptionPane.showMessageDialog(this, "Error opening file " + my_file.getAbsoluteFile(), "File not found", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BufferedReader p;
            try {
                p = new BufferedReader( new FileReader(my_file.getAbsoluteFile()));
                results.datas.clear();
                String t;
                while((t = p.readLine()) != null) {
                    try{
                        Object[] object_row = ALGORITHM.Function.parseParametersAsString(t);
                        results.datas.add(object_row);
                        results.fireTableDataChanged();
                    } catch(NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Error reading from file " + my_file.getAbsoluteFile(), "Error reading file", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                p.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading from file " + my_file.getAbsoluteFile(), "Error reading file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_loadResultsButtonMouseClicked
    
    private void eraseMarkedEntriesButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eraseMarkedEntriesButtonMouseClicked
        int[] list = resultTable.getSelectedRows();
        for(int i = 0; i < list.length; i++)
            results.datas.remove(list[i]-i);
        results.fireTableDataChanged();
    }//GEN-LAST:event_eraseMarkedEntriesButtonMouseClicked
    
    private void saveResultsButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveResultsButtonMouseClicked
        if(fileChooser.showSaveDialog(this) == fileChooser.APPROVE_OPTION) {
            File my_file = fileChooser.getSelectedFile();
            try{
                my_file.createNewFile();
            } catch(IOException e) {
                JOptionPane.showMessageDialog(this, "Error opening file " + my_file.getAbsoluteFile(), "Error opening file", JOptionPane.ERROR_MESSAGE);
                return;
            }
            FileOutputStream f;
            PrintStream p;
            try {
                f = new FileOutputStream(my_file.getAbsoluteFile());
                p = new PrintStream( f );
                for(int i = 0; i < results.datas.size(); i++) {
                    Object[] t = (Object[])results.datas.get(i);
                    p.println(
                            "" + (java.lang.Long)t[0] + ", " +
                            "\"" + (String)t[1] + "\", " +
                            "" + (java.lang.Integer)t[2] + ", " +
                            "" + (java.lang.Integer)t[3] + ", " +
                            "" + (java.lang.Integer)t[4] + ", " +
                            "" + (java.lang.Boolean)t[5] + ", " +
                            "" + (java.lang.Boolean)t[6] + ", " +
                            "" + (java.lang.Boolean)t[7]);
                }
                p.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error writing to file " + my_file.getAbsoluteFile(), "Error writing file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_saveResultsButtonMouseClicked
    
    
    private void stopCalculationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopCalculationButtonMouseClicked
        calculation_thread.interrupt();
    }//GEN-LAST:event_stopCalculationButtonMouseClicked
        
    
    // TODO: bei mehreren Zellzustaenden entweder auf 'test all rules' stellen, oder verlangen, dass der Benutzer mehrere Eintraege bei rule_nr
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox addToDatabaseCheckBox;
    private javax.swing.ButtonGroup automationButtonGroup;
    private javax.swing.ButtonGroup chooseOutputInjectiveSurjectiveButtonGroup;
    private javax.swing.JButton eraseMarkedEntriesButton;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JCheckBox generateGraphCheckBox;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton loadResultsButton;
    private javax.swing.JLabel neededMemoryField;
    private javax.swing.JLabel neededMemoryLabel;
    private javax.swing.JLabel neededTimeField;
    private javax.swing.JLabel neededTimeLabel;
    private javax.swing.JTextField neighborhoodField;
    private java.awt.Label neighborhoodLabel;
    private javax.swing.JTextField neighborhoodSizeField;
    private java.awt.Label neighborhoodSizeLabel;
    private javax.swing.JTextField numberOfCellStatesField;
    private java.awt.Label numberOfCellStatesLabel;
    private javax.swing.JRadioButton outputAllRadioButton;
    private javax.swing.JRadioButton outputOnlyInjectiveRadioButton;
    private javax.swing.JRadioButton outputSurjectiveRadioButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressBarLabel;
    private javax.swing.JTable resultTable;
    private javax.swing.JTextField ruleField;
    private java.awt.Label ruleLabel;
    private javax.swing.JButton saveResultsButton;
    private javax.swing.JButton showGraphButton;
    private javax.swing.JTextField significantNeighborhoodSizeField;
    private java.awt.Label significantNeighborhoodSizeLabel;
    private javax.swing.JButton startCalculationButton;
    private javax.swing.JButton stopCalculationButton;
    private javax.swing.JRadioButton testAllNeighborhoodPermutationsRadioButton;
    private javax.swing.JRadioButton testAllNeighborhoodSizesRadioButton;
    private javax.swing.JRadioButton testAllNeighborhoodsRadioButton;
    private javax.swing.JCheckBox testAllRulesCheckBox;
    private javax.swing.JRadioButton testSingleNeighborhoodRadioButton;
    // End of variables declaration//GEN-END:variables
//public void actionPerformed(ActionEvent event) {
    
}
