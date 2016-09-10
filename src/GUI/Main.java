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
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.math.BigInteger;

public class Main extends java.awt.Frame {

    boolean neighborhoodSizeWasActivated = false;
    boolean significantNeighborhoodSizeWasActivated = false;
    boolean neighborhoodWasActivated = false;
    boolean ruleNumberWasActivated = false;


    CalculationThread calculation_thread;
    ResultsTable results = new ResultsTable();
    TableSorter sorter = new TableSorter(results);

    /** Creates new form Main */
    public Main() {
        initComponents();

        try {
            ALGORITHM.Function.createStandardSignificantFunction();
        } catch (Exception e) {
            System.out.println(e);
        // TODO
        }

        significantNeighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getSignificantNeighborhoodSize());
        significantNeighborhoodSizeField.setEnabled(false);

        neighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getNeighborhoodSize());
        neighborhoodSizeField.setEnabled(false);

        neighborhoodField.setText("" + ALGORITHM.Neighborhood.getNeighborhoodString());

        numberOfCellStatesField.setText("" + ALGORITHM.Function.getNumberOfCellStates());

        updateNeededFields();
        results.fireTableStructureChanged();

        resultTable.setModel(sorter);
        sorter.setTableHeader(resultTable.getTableHeader());
        resultTable.getTableHeader().setToolTipText(
                "Click to specify sorting; Control-Click to specify secondary sorting");

        File my_file = new File("temp_results.txt");
        if (my_file.exists()) {

            try {
                BufferedReader p = new BufferedReader(new FileReader(my_file.getAbsoluteFile()));

                boolean test_single_neighborhood = Boolean.valueOf(p.readLine());
                boolean test_all_neighborhood_variations = Boolean.valueOf(p.readLine());
                boolean test_all_neighborhood_sizes = Boolean.valueOf(p.readLine());
                boolean test_all_neighborhoods = Boolean.valueOf(p.readLine());

                if (test_all_neighborhood_variations) {
                    testAllNeighborhoodVariationsRadioButton.doClick();
                } else if (test_all_neighborhood_sizes) {
                    testAllNeighborhoodSizesRadioButton.doClick();
                } else if (test_all_neighborhoods) {
                    testAllNeighborhoodsRadioButton.doClick();
                }
                String neighborhood = p.readLine();
                String significant_neighborhood = p.readLine();
                String neighborhood_size = p.readLine();
                String number_of_cell_states = p.readLine();
                String rule_field = p.readLine();
                String boolean_rule_field = p.readLine();

                if (neighborhoodField.isEnabled()) {
                    neighborhoodField.setText(neighborhood);
                }
                if (significantNeighborhoodSizeField.isEnabled()) {
                    significantNeighborhoodSizeField.setText(significant_neighborhood);
                }
                if (neighborhoodSizeField.isEnabled()) {
                    neighborhoodSizeField.setText(neighborhood_size);
                }
                if (numberOfCellStatesField.isEnabled()) {
                    numberOfCellStatesField.setText(number_of_cell_states);
                }
                if (ruleField.isEnabled()) {
                    ruleField.setText(rule_field);
                }
                if (booleanRuleField.isEnabled()) {
                    booleanRuleField.setText(boolean_rule_field);
                }

                boolean output_all = Boolean.valueOf(p.readLine());
                boolean output_surjective = Boolean.valueOf(p.readLine());
                boolean output_only_injective = Boolean.valueOf(p.readLine());

                if (output_surjective) {
                    outputSurjectiveRadioButton.doClick();
                } else if (output_only_injective) {
                    outputOnlyInjectiveRadioButton.doClick();
                }

                boolean generate_graph = Boolean.valueOf(p.readLine());
                boolean add_to_database = Boolean.valueOf(p.readLine());
                boolean check_duplicates = Boolean.valueOf(p.readLine());
                boolean use_fast_cpp_plugin = Boolean.valueOf(p.readLine());
                boolean show_polynomial_rule = Boolean.valueOf(p.readLine());

                generateGraphCheckBox.setSelected(generate_graph);
                addToDatabaseCheckBox.setSelected(add_to_database);
                checkDatabaseDuplicatesCheckBox.setSelected(check_duplicates);
                useFastCPPPluginCheckBox.setSelected(use_fast_cpp_plugin);
                
                polynomialRuleCheckBox.setSelected(show_polynomial_rule);

                loadFromFields();

                loadResultsIntoDatabase(p);
                p.close();
            } catch (IOException e) {
                System.out.println("IO Exception: Error " + e + " reading from file " + my_file.getAbsoluteFile());
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException: Error " + e + " reading from file " + my_file.getAbsoluteFile());
            }
            my_file.delete();
        }
        updateRuleFields();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chooseOutputInjectiveSurjectiveButtonGroup = new javax.swing.ButtonGroup();
        fileChooser = new javax.swing.JFileChooser();
        automationNeighborhoodButtonGroup = new javax.swing.ButtonGroup();
        automationRuleButtonGroup = new javax.swing.ButtonGroup();
        catestPane = new javax.swing.JTabbedPane();
        testPanel = new javax.swing.JPanel();
        neighborhoodConfigurationPanel = new javax.swing.JPanel();
        neighborhoodSizeField = new javax.swing.JTextField();
        significantNeighborhoodSizeField = new javax.swing.JTextField();
        neighborhoodField = new javax.swing.JTextField();
        neighborhoodSizeLabel = new javax.swing.JLabel();
        significantNeighborhoodSizeLabel = new javax.swing.JLabel();
        neighborhoodLabel = new javax.swing.JLabel();
        numberOfCellStatesLabel = new javax.swing.JLabel();
        numberOfCellStatesField = new javax.swing.JTextField();
        calculationPanel = new javax.swing.JPanel();
        neededTimeLabel = new javax.swing.JLabel();
        neededTimeField = new javax.swing.JLabel();
        neededMemoryLabel = new javax.swing.JLabel();
        neededMemoryField = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        progressBarLabel = new javax.swing.JLabel();
        stopCalculationButton = new javax.swing.JButton();
        startCalculationButton = new javax.swing.JButton();
        rulesToTestLabel = new javax.swing.JLabel();
        rulesToTestField = new javax.swing.JLabel();
        ruleConfigurationPanel = new javax.swing.JPanel();
        ruleField = new javax.swing.JTextField();
        booleanRuleField = new javax.swing.JTextField();
        polynomialRuleField = new javax.swing.JTextField();
        booleanRepresentationLabel = new javax.swing.JLabel();
        ruleNumberLabel = new javax.swing.JLabel();
        polynomialRuleCheckBox = new javax.swing.JCheckBox();
        outputOptionsPanel = new javax.swing.JPanel();
        outputAllRadioButton = new javax.swing.JRadioButton();
        outputSurjectiveRadioButton = new javax.swing.JRadioButton();
        outputOnlyInjectiveRadioButton = new javax.swing.JRadioButton();
        generateGraphCheckBox = new javax.swing.JCheckBox();
        addToDatabaseCheckBox = new javax.swing.JCheckBox();
        useFastCPPPluginCheckBox = new javax.swing.JCheckBox();
        checkDatabaseDuplicatesCheckBox = new javax.swing.JCheckBox();
        automationPanel = new javax.swing.JPanel();
        singleRuleTestRadioButton = new javax.swing.JRadioButton();
        allRuleTestRadioButton = new javax.swing.JRadioButton();
        allNeighborhoodRuleTestRadioButton = new javax.swing.JRadioButton();
        automaticTestsPanel = new javax.swing.JPanel();
        testSingleNeighborhoodRadioButton = new javax.swing.JRadioButton();
        testAllNeighborhoodVariationsRadioButton = new javax.swing.JRadioButton();
        testAllNeighborhoodSizesRadioButton = new javax.swing.JRadioButton();
        testAllNeighborhoodsRadioButton = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        saveEndProgramButton = new javax.swing.JButton();
        versionLabel = new javax.swing.JLabel();
        contactLabel1 = new javax.swing.JLabel();
        contactLabel = new javax.swing.JLabel();
        endProgramButton = new javax.swing.JButton();
        resultsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();
        eraseMarkedEntriesButton = new javax.swing.JButton();
        saveResultsButton = new javax.swing.JButton();
        loadResultsButton = new javax.swing.JButton();
        showImageButton = new javax.swing.JButton();
        eraseAllEntriesButton = new javax.swing.JButton();
        generateImageButton = new javax.swing.JButton();

        setBackground(java.awt.Color.lightGray);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        setFocusTraversalPolicyProvider(true);
        setTitle("Test CA surjectivity and injectivity");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        neighborhoodConfigurationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Neighborhood Configuration"));

        neighborhoodSizeField.setText("2");
        neighborhoodSizeField.setFocusCycleRoot(true);
        neighborhoodSizeField.setFocusTraversalPolicyProvider(true);
        neighborhoodSizeField.setPreferredSize(new java.awt.Dimension(80, 20));
        neighborhoodSizeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                neighborhoodSizeFieldActionPerformed(evt);
            }
        });
        neighborhoodSizeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                neighborhoodSizeFieldFocusLost(evt);
            }
        });

        significantNeighborhoodSizeField.setText("2");
        significantNeighborhoodSizeField.setFocusCycleRoot(true);
        significantNeighborhoodSizeField.setFocusTraversalPolicyProvider(true);
        significantNeighborhoodSizeField.setPreferredSize(new java.awt.Dimension(80, 20));
        significantNeighborhoodSizeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                significantNeighborhoodSizeFieldActionPerformed(evt);
            }
        });
        significantNeighborhoodSizeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                significantNeighborhoodSizeFieldFocusLost(evt);
            }
        });

        neighborhoodField.setText("0, 1");
        neighborhoodField.setToolTipText("Neighborhood configuration in the form '0,1,5,7' etc.");
        neighborhoodField.setFocusCycleRoot(true);
        neighborhoodField.setFocusTraversalPolicyProvider(true);
        neighborhoodField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                neighborhoodFieldActionPerformed(evt);
            }
        });
        neighborhoodField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                neighborhoodFieldFocusLost(evt);
            }
        });

        neighborhoodSizeLabel.setText("Neighborhood Size");

        significantNeighborhoodSizeLabel.setText("Significant Neighborhood Size");

        neighborhoodLabel.setText("Neighborhood");

        numberOfCellStatesLabel.setText("Number of Cell States");

        numberOfCellStatesField.setText("2");
        numberOfCellStatesField.setFocusCycleRoot(true);
        numberOfCellStatesField.setFocusTraversalPolicyProvider(true);
        numberOfCellStatesField.setPreferredSize(new java.awt.Dimension(80, 20));
        numberOfCellStatesField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberOfCellStatesFieldActionPerformed(evt);
            }
        });
        numberOfCellStatesField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numberOfCellStatesFieldFocusLost(evt);
            }
        });

        org.jdesktop.layout.GroupLayout neighborhoodConfigurationPanelLayout = new org.jdesktop.layout.GroupLayout(neighborhoodConfigurationPanel);
        neighborhoodConfigurationPanel.setLayout(neighborhoodConfigurationPanelLayout);
        neighborhoodConfigurationPanelLayout.setHorizontalGroup(
            neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, neighborhoodConfigurationPanelLayout.createSequentialGroup()
                .add(348, 348, 348)
                .add(neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, neighborhoodConfigurationPanelLayout.createSequentialGroup()
                        .add(neighborhoodLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(neighborhoodField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 180, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, neighborhoodConfigurationPanelLayout.createSequentialGroup()
                        .add(28, 28, 28)
                        .add(significantNeighborhoodSizeLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(significantNeighborhoodSizeField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, neighborhoodConfigurationPanelLayout.createSequentialGroup()
                        .add(109, 109, 109)
                        .add(neighborhoodSizeLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(neighborhoodSizeField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, neighborhoodConfigurationPanelLayout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 84, Short.MAX_VALUE)
                        .add(numberOfCellStatesLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(numberOfCellStatesField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
        );

        neighborhoodConfigurationPanelLayout.linkSize(new java.awt.Component[] {neighborhoodSizeField, numberOfCellStatesField, significantNeighborhoodSizeField}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        neighborhoodConfigurationPanelLayout.setVerticalGroup(
            neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(neighborhoodConfigurationPanelLayout.createSequentialGroup()
                .add(neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(numberOfCellStatesField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(numberOfCellStatesLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(neighborhoodSizeLabel)
                    .add(neighborhoodSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(significantNeighborhoodSizeLabel)
                    .add(significantNeighborhoodSizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(neighborhoodField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(neighborhoodLabel)))
        );

        calculationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Calculation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

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

        rulesToTestLabel.setText("Number of rules to test");

        rulesToTestField.setText("1");

        org.jdesktop.layout.GroupLayout calculationPanelLayout = new org.jdesktop.layout.GroupLayout(calculationPanel);
        calculationPanel.setLayout(calculationPanelLayout);
        calculationPanelLayout.setHorizontalGroup(
            calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(calculationPanelLayout.createSequentialGroup()
                .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, calculationPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(startCalculationButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 139, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(38, 38, 38)
                        .add(stopCalculationButton))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, calculationPanelLayout.createSequentialGroup()
                        .add(24, 24, 24)
                        .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(neededMemoryLabel)
                            .add(neededTimeLabel)
                            .add(progressBarLabel)
                            .add(rulesToTestLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(neededTimeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 128, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                            .add(neededMemoryField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 153, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(rulesToTestField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 153, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        calculationPanelLayout.setVerticalGroup(
            calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, calculationPanelLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rulesToTestLabel)
                    .add(rulesToTestField))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(neededTimeLabel)
                    .add(neededTimeField))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(neededMemoryLabel)
                    .add(neededMemoryField))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(progressBarLabel))
                .add(18, 18, 18)
                .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(stopCalculationButton)
                    .add(startCalculationButton))
                .addContainerGap())
        );

        ruleConfigurationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Rule Configuration"));

        ruleField.setText("42");
        ruleField.setFocusCycleRoot(true);
        ruleField.setFocusTraversalPolicyProvider(true);
        ruleField.setPreferredSize(new java.awt.Dimension(80, 20));
        ruleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ruleFieldActionPerformed(evt);
            }
        });
        ruleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ruleFieldFocusLost(evt);
            }
        });

        booleanRuleField.setText("x0' x1 + x0 x1'");
        booleanRuleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                booleanRuleFieldActionPerformed(evt);
            }
        });
        booleanRuleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                booleanRuleFieldFocusLost(evt);
            }
        });

        polynomialRuleField.setText("x0^2 + 2 x0 x1 + x1^2");
        polynomialRuleField.setEnabled(false);
        polynomialRuleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                polynomialRuleFieldActionPerformed(evt);
            }
        });
        polynomialRuleField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                polynomialRuleFieldFocusLost(evt);
            }
        });

        booleanRepresentationLabel.setText("Boolean Representation");

        ruleNumberLabel.setText("Rule Number");

        polynomialRuleCheckBox.setText("Polynomial Representation");
        polynomialRuleCheckBox.setEnabled(false);
        polynomialRuleCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                polynomialRuleCheckBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout ruleConfigurationPanelLayout = new org.jdesktop.layout.GroupLayout(ruleConfigurationPanel);
        ruleConfigurationPanel.setLayout(ruleConfigurationPanelLayout);
        ruleConfigurationPanelLayout.setHorizontalGroup(
            ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, ruleConfigurationPanelLayout.createSequentialGroup()
                .add(31, 31, 31)
                .add(ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(ruleNumberLabel)
                    .add(booleanRepresentationLabel)
                    .add(polynomialRuleCheckBox))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(booleanRuleField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                    .add(polynomialRuleField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                    .add(ruleConfigurationPanelLayout.createSequentialGroup()
                        .add(ruleField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .addContainerGap())
        );
        ruleConfigurationPanelLayout.setVerticalGroup(
            ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(ruleConfigurationPanelLayout.createSequentialGroup()
                .add(ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(ruleNumberLabel)
                    .add(ruleField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(booleanRepresentationLabel)
                    .add(booleanRuleField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(polynomialRuleField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(polynomialRuleCheckBox)))
        );

        outputOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Output options"));

        chooseOutputInjectiveSurjectiveButtonGroup.add(outputAllRadioButton);
        outputAllRadioButton.setSelected(true);
        outputAllRadioButton.setText("All");
        outputAllRadioButton.setToolTipText("Report all results");
        outputAllRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        outputAllRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        chooseOutputInjectiveSurjectiveButtonGroup.add(outputSurjectiveRadioButton);
        outputSurjectiveRadioButton.setText("At least surjective");
        outputSurjectiveRadioButton.setToolTipText("Only report those results that are either surjective or surjective and injective");
        outputSurjectiveRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        outputSurjectiveRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        chooseOutputInjectiveSurjectiveButtonGroup.add(outputOnlyInjectiveRadioButton);
        outputOnlyInjectiveRadioButton.setText("Only both injective and surjective");
        outputOnlyInjectiveRadioButton.setToolTipText("Filter all results that are not surjective or not injective");
        outputOnlyInjectiveRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        outputOnlyInjectiveRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        generateGraphCheckBox.setText("Generate Graph (.viz file)");
        generateGraphCheckBox.setToolTipText("Generate File for GraphViz in order to later generate a graph file");
        generateGraphCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        generateGraphCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        addToDatabaseCheckBox.setSelected(true);
        addToDatabaseCheckBox.setText("Add result to database");
        addToDatabaseCheckBox.setToolTipText("Instead of a simple output all results will be put into a database (tab 'Results')");
        addToDatabaseCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        addToDatabaseCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        useFastCPPPluginCheckBox.setText("Use fast C plugin");
        useFastCPPPluginCheckBox.setToolTipText("Call external 'catest_c', faster + less memory usage, use only for small number of test cases");
        useFastCPPPluginCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        useFastCPPPluginCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        useFastCPPPluginCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                useFastCPPPluginCheckBoxItemStateChanged(evt);
            }
        });

        checkDatabaseDuplicatesCheckBox.setText("Skip already calculated rules");
        checkDatabaseDuplicatesCheckBox.setToolTipText("Check database prior to each calculation");
        checkDatabaseDuplicatesCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        org.jdesktop.layout.GroupLayout outputOptionsPanelLayout = new org.jdesktop.layout.GroupLayout(outputOptionsPanel);
        outputOptionsPanel.setLayout(outputOptionsPanelLayout);
        outputOptionsPanelLayout.setHorizontalGroup(
            outputOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(useFastCPPPluginCheckBox)
            .add(checkDatabaseDuplicatesCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 199, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(addToDatabaseCheckBox)
            .add(generateGraphCheckBox)
            .add(outputAllRadioButton)
            .add(outputSurjectiveRadioButton)
            .add(outputOnlyInjectiveRadioButton)
        );
        outputOptionsPanelLayout.setVerticalGroup(
            outputOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(outputOptionsPanelLayout.createSequentialGroup()
                .add(outputAllRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outputSurjectiveRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outputOnlyInjectiveRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(generateGraphCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(addToDatabaseCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(checkDatabaseDuplicatesCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(useFastCPPPluginCheckBox))
        );

        automationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Automation"));

        automationRuleButtonGroup.add(singleRuleTestRadioButton);
        singleRuleTestRadioButton.setSelected(true);
        singleRuleTestRadioButton.setText("Test a single rule only");
        singleRuleTestRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        singleRuleTestRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        singleRuleTestRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                singleRuleTestRadioButtonItemStateChanged(evt);
            }
        });

        automationRuleButtonGroup.add(allRuleTestRadioButton);
        allRuleTestRadioButton.setText("Test all balanced rules");
        allRuleTestRadioButton.setToolTipText("Test all rules that fit into the given 'Significant Neighborhood Size'");
        allRuleTestRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allRuleTestRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        allRuleTestRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                allRuleTestRadioButtonItemStateChanged(evt);
            }
        });

        automationRuleButtonGroup.add(allNeighborhoodRuleTestRadioButton);
        allNeighborhoodRuleTestRadioButton.setText("Test all neighborhood permutations");
        allNeighborhoodRuleTestRadioButton.setToolTipText("When permutating the neighborhood include disordered neighborhoods (e.g. 1,0,2)");
        allNeighborhoodRuleTestRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        allNeighborhoodRuleTestRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        allNeighborhoodRuleTestRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                allNeighborhoodRuleTestRadioButtonItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout automationPanelLayout = new org.jdesktop.layout.GroupLayout(automationPanel);
        automationPanel.setLayout(automationPanelLayout);
        automationPanelLayout.setHorizontalGroup(
            automationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(automationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(automationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(singleRuleTestRadioButton)
                    .add(allNeighborhoodRuleTestRadioButton)
                    .add(allRuleTestRadioButton))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        automationPanelLayout.setVerticalGroup(
            automationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(automationPanelLayout.createSequentialGroup()
                .add(singleRuleTestRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(allNeighborhoodRuleTestRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(allRuleTestRadioButton)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        automaticTestsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Automatic tests"));

        automationNeighborhoodButtonGroup.add(testSingleNeighborhoodRadioButton);
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

        automationNeighborhoodButtonGroup.add(testAllNeighborhoodVariationsRadioButton);
        testAllNeighborhoodVariationsRadioButton.setText("all neighborhood variations");
        testAllNeighborhoodVariationsRadioButton.setToolTipText("Test all neighborhood variations within the given range and specified number of positions in \"Neighborhood Size\" and \"Significant Neighborhood Size\"");
        testAllNeighborhoodVariationsRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        testAllNeighborhoodVariationsRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        testAllNeighborhoodVariationsRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testAllNeighborhoodVariationsRadioButtonItemStateChanged(evt);
            }
        });

        automationNeighborhoodButtonGroup.add(testAllNeighborhoodSizesRadioButton);
        testAllNeighborhoodSizesRadioButton.setText("all neighborhood sizes and variations");
        testAllNeighborhoodSizesRadioButton.setToolTipText("Test all neighborhood variations up to the size specified in \"Neighborhood Size\" with the given number of positions in \"Significant Neighborhood Size\"");
        testAllNeighborhoodSizesRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        testAllNeighborhoodSizesRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        testAllNeighborhoodSizesRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testAllNeighborhoodSizesRadioButtonItemStateChanged(evt);
            }
        });

        automationNeighborhoodButtonGroup.add(testAllNeighborhoodsRadioButton);
        testAllNeighborhoodsRadioButton.setText("all neighborhoods");
        testAllNeighborhoodsRadioButton.setToolTipText("Test all neighborhood configurations up to the size specified in \"Neighborhood Size\"");
        testAllNeighborhoodsRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        testAllNeighborhoodsRadioButton.setEnabled(false);
        testAllNeighborhoodsRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        testAllNeighborhoodsRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testAllNeighborhoodsRadioButtonItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout automaticTestsPanelLayout = new org.jdesktop.layout.GroupLayout(automaticTestsPanel);
        automaticTestsPanel.setLayout(automaticTestsPanelLayout);
        automaticTestsPanelLayout.setHorizontalGroup(
            automaticTestsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, automaticTestsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(automaticTestsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(testSingleNeighborhoodRadioButton)
                    .add(testAllNeighborhoodVariationsRadioButton)
                    .add(testAllNeighborhoodSizesRadioButton)
                    .add(testAllNeighborhoodsRadioButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        automaticTestsPanelLayout.setVerticalGroup(
            automaticTestsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(automaticTestsPanelLayout.createSequentialGroup()
                .add(testSingleNeighborhoodRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testAllNeighborhoodVariationsRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testAllNeighborhoodSizesRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testAllNeighborhoodsRadioButton)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        saveEndProgramButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        saveEndProgramButton.setText("Save & End Program");
        saveEndProgramButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveEndProgramButtonMouseClicked(evt);
            }
        });

        versionLabel.setText("Version 1.059 (08/12/2008)");

        contactLabel1.setText("clemens@lode.de");

        contactLabel.setText("by Clemens Lode");

        endProgramButton.setText("End Program");
        endProgramButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                endProgramButtonMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(97, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, endProgramButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, saveEndProgramButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, contactLabel)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, contactLabel1)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, versionLabel))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .add(versionLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(contactLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(contactLabel1)
                .add(18, 18, 18)
                .add(endProgramButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(saveEndProgramButton)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout testPanelLayout = new org.jdesktop.layout.GroupLayout(testPanel);
        testPanel.setLayout(testPanelLayout);
        testPanelLayout.setHorizontalGroup(
            testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(testPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(testPanelLayout.createSequentialGroup()
                        .add(calculationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(outputOptionsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, ruleConfigurationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, neighborhoodConfigurationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(automationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(automaticTestsPanel, 0, 285, Short.MAX_VALUE))
                .add(93, 93, 93))
        );
        testPanelLayout.setVerticalGroup(
            testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(testPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(neighborhoodConfigurationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .add(automaticTestsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(automationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(ruleConfigurationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(calculationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(outputOptionsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        catestPane.addTab("Parameters & Test", testPanel);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setDoubleBuffered(true);

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

        showImageButton.setText("Show Image(s)");
        showImageButton.setToolTipText("Display graphs of marked entries if availible");
        showImageButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showImageButtonMouseClicked(evt);
            }
        });

        eraseAllEntriesButton.setText("Erase All");
        eraseAllEntriesButton.setToolTipText("Clear all results");
        eraseAllEntriesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eraseAllEntriesButtonMouseClicked(evt);
            }
        });

        generateImageButton.setText("Generate Image(s)");
        generateImageButton.setToolTipText("Generate graphs of marked entries (note that 'dot' can only handle small graphs)");
        generateImageButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                generateImageButtonMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout resultsPanelLayout = new org.jdesktop.layout.GroupLayout(resultsPanel);
        resultsPanel.setLayout(resultsPanelLayout);
        resultsPanelLayout.setHorizontalGroup(
            resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(resultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 937, Short.MAX_VALUE)
                    .add(resultsPanelLayout.createSequentialGroup()
                        .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(eraseAllEntriesButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(eraseMarkedEntriesButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(showImageButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(generateImageButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 575, Short.MAX_VALUE)
                        .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, loadResultsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, saveResultsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        resultsPanelLayout.setVerticalGroup(
            resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, resultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(loadResultsButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(saveResultsButton)
                .addContainerGap())
            .add(resultsPanelLayout.createSequentialGroup()
                .add(402, 402, 402)
                .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(generateImageButton)
                    .add(eraseMarkedEntriesButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(showImageButton)
                    .add(eraseAllEntriesButton))
                .add(11, 11, 11))
        );

        catestPane.addTab("Results", resultsPanel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(catestPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 966, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(catestPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void checkRuleField() {
        try {
            try {
                ALGORITHM.Function.createSignificantFunction(new BigInteger(ruleField.getText())); 
              //  System.out.println(ALGORITHM.Function.getSignificantFunctionString());
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(this, "Please enter only numerals into the Rule Field", "Wrong number format", JOptionPane.ERROR_MESSAGE);
                ruleField.requestFocusInWindow();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Rule Number is out of range, please increase significant neighborhood size", JOptionPane.ERROR_MESSAGE);
            ruleField.requestFocusInWindow();
        }

    }

    private void checkBooleanRuleField() {
        try {
            try {
                ALGORITHM.Function.createSignificantFunction(ALGORITHM.Function.extractBooleanRuleNumber(booleanRuleField.getText()));
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(this, "Please check the syntax in the boolean rule field: " + n, "Syntax Error", JOptionPane.ERROR_MESSAGE);
                booleanRuleField.requestFocusInWindow();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Boolean Rule Number is out of range, please increase significant neighborhood size", JOptionPane.ERROR_MESSAGE);
            booleanRuleField.requestFocusInWindow();
        }
    }

    private void checkPolynomialRuleField() {
        try {
            try {
                ALGORITHM.Function.createSignificantFunction(ALGORITHM.Function.extractPolynomialRuleNumber(polynomialRuleField.getText()));
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(this, "Please check the syntax in the polynomial rule field: " + n, "Syntax Error", JOptionPane.ERROR_MESSAGE);
                polynomialRuleField.requestFocusInWindow();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Polynomial Rule Number is out of range, please increase significant neighborhood size", JOptionPane.ERROR_MESSAGE);
            polynomialRuleField.requestFocusInWindow();
        }
    }    
    
//   TODO, getSignificantRuleNumber liefert anfangs einen ungültigen Wert (0) ?
    private void updateRuleFields() {
        if (ruleField.isEnabled()) {
            ruleField.setText(ALGORITHM.Function.getSignificantRuleNumber().toString());
        }
        if (ALGORITHM.Function.getNumberOfCellStates() != 2) {
            booleanRuleField.setEnabled(false);
            booleanRuleField.setText("only for binary case");
        } else if (!booleanRuleField.isEnabled() && ruleField.isEnabled()) {
            booleanRuleField.setEnabled(true);
        }
        
        if(polynomialRuleCheckBox.isSelected() && !polynomialRuleField.isEnabled() && ruleField.isEnabled()) {
            polynomialRuleField.setEnabled(true);
        }

        if (booleanRuleField.isEnabled()) {
            booleanRuleField.setText(ALGORITHM.Function.getSignificantBooleanRule());
        }
        
        if(polynomialRuleField.isEnabled()) {
            polynomialRuleField.setText(ALGORITHM.Function.getSignificantPolynomialRule());
        }
    }

    private void generateImageButtonMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_generateImageButtonMouseClicked
    {//GEN-HEADEREND:event_generateImageButtonMouseClicked
        int[] list = resultTable.getSelectedRows();
        Vector<Integer> graph_list = new Vector<Integer>();

        for (int i = 0; i < list.length; i++) {
            if (results.hasGraph(list[i]) && (!results.hasImage(list[i]))) {
                graph_list.add(new Integer(list[i]));
            }
        }

        if (graph_list.size() == 0) {
            JOptionPane.showMessageDialog(this, "No entries with existing graphs selected or selected entries already have a graph", "No entry selected", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (graph_list.size() >= 16) {
            if (JOptionPane.showConfirmDialog(this, "" + graph_list.size() + " entries were selected for graph creation.\nContinue?",
                    "Confirm generating multiple graphs",
                    JOptionPane.YES_NO_OPTION) == 1) {
                return;
            }
        }
        String calling_string = "dot/dot -Tpng -O ";
        Runtime rt = Runtime.getRuntime();
        try {
            for (int i = 0; i < graph_list.size(); i++) {
                String file_name = ALGORITHM.Function.getImageFileName(results.getRow(graph_list.get(i))) + ".viz";
                if (((new File(file_name)).length() / 1024) > 64) {
                    if (JOptionPane.showConfirmDialog(null, "The graph file (" + file_name + ") is larger than 64kb (" + ((new File(file_name)).length() / 1024) + "kb). 'DOT' might need a long time to generate the image.\n\nContinue?", "Large graph warning", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == 1) {
                        break;
                    }
                }
                Process p = rt.exec(calling_string + "\"" + file_name + "\"");
                InputStream in = p.getInputStream();
                OutputStream out = p.getOutputStream();
                InputStream err = p.getErrorStream();
                int x;

                System.out.print("InputStream in: ");
                while ((x = in.read())!= -1) {
                    System.out.print(Character.valueOf((char)x)); 
                }               
                System.out.println();
                System.out.print("InputStream err: ");
                while ((x = err.read())!= -1) {
                    System.out.print(Character.valueOf((char)x)); 
                }                
                System.out.println();                

                int return_value = p.waitFor();
                System.out.println("Return value: " + return_value);
                p.destroy();
            }
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(this, "Error when calling the 'dot' program. Ensure that the tool is in the directory " + System.getProperty("user.dir") + "/dot", "Error generating graphs", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < graph_list.size(); i++) {
            String file_name = ALGORITHM.Function.getImageFileName(results.getRow(graph_list.get(i))) + ".viz.png";
            if ((new File(file_name)).exists()) {
                results.setHasImage(graph_list.get(i));
            }
            results.fireTableRowsUpdated(graph_list.get(i), graph_list.get(i));
        }        
    }//GEN-LAST:event_generateImageButtonMouseClicked

    private void eraseAllEntriesButtonMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_eraseAllEntriesButtonMouseClicked
    {//GEN-HEADEREND:event_eraseAllEntriesButtonMouseClicked
        results.datas.clear();
        results.fireTableDataChanged();
    }//GEN-LAST:event_eraseAllEntriesButtonMouseClicked

    private void useFastCPPPluginCheckBoxItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_useFastCPPPluginCheckBoxItemStateChanged
    {//GEN-HEADEREND:event_useFastCPPPluginCheckBoxItemStateChanged
        if (useFastCPPPluginCheckBox.isSelected()) {
            generateGraphCheckBox.setSelected(false);
            generateGraphCheckBox.setEnabled(false);
        } else {
            generateGraphCheckBox.setEnabled(true);
        }
    }//GEN-LAST:event_useFastCPPPluginCheckBoxItemStateChanged

    private void allNeighborhoodRuleTestRadioButtonItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_allNeighborhoodRuleTestRadioButtonItemStateChanged
    {//GEN-HEADEREND:event_allNeighborhoodRuleTestRadioButtonItemStateChanged
        if (allNeighborhoodRuleTestRadioButton.isSelected()) {
            ALGORITHM.Neighborhood.setAllowDisorderedNeighborhoodPermutations(true);
            if(testAllNeighborhoodsRadioButton.isSelected()) {
                testAllNeighborhoodSizesRadioButton.doClick();
            }
            testAllNeighborhoodsRadioButton.setEnabled(false);
            
        } else {
            ALGORITHM.Neighborhood.setAllowDisorderedNeighborhoodPermutations(false);
            testAllNeighborhoodsRadioButton.setEnabled(true);
        }
        updateNeededFields();
    }//GEN-LAST:event_allNeighborhoodRuleTestRadioButtonItemStateChanged

    private void allRuleTestRadioButtonItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_allRuleTestRadioButtonItemStateChanged
    {//GEN-HEADEREND:event_allRuleTestRadioButtonItemStateChanged
        if (allRuleTestRadioButton.isSelected()) {
            ruleField.setText("all");
            ruleField.setEnabled(false);
            if (ALGORITHM.Function.getNumberOfCellStates() != 2) {
               booleanRuleField.setText("only for binary case");            
            } else {
                booleanRuleField.setText("all");
            }
            booleanRuleField.setEnabled(false);
            polynomialRuleField.setText("all");
            polynomialRuleField.setEnabled(false);
            polynomialRuleCheckBox.setSelected(false);
            testAllNeighborhoodsRadioButton.setEnabled(true);
        } else {
                ruleField.setEnabled(true);
            if (ALGORITHM.Function.getNumberOfCellStates() != 2) {
               booleanRuleField.setEnabled(false);
               booleanRuleField.setText("only for binary case");
           } else if (!booleanRuleField.isEnabled() && ruleField.isEnabled()) {
               booleanRuleField.setEnabled(true);
          }
            if(polynomialRuleCheckBox.isSelected()) {
                polynomialRuleField.setEnabled(true);
            }

            updateRuleFields();
        }
        ALGORITHM.Function.setTestAllRules(allRuleTestRadioButton.isSelected());
        updateNeededFields();
    }//GEN-LAST:event_allRuleTestRadioButtonItemStateChanged

    private void testAllNeighborhoodsRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testAllNeighborhoodsRadioButtonItemStateChanged
        if (testAllNeighborhoodsRadioButton.isSelected()) {
            ALGORITHM.Neighborhood.setTestAllNeighborhoods(true);

            neighborhoodField.setText("all");
            neighborhoodField.setEnabled(false);

            neighborhoodSizeField.setEnabled(true);
            significantNeighborhoodSizeField.setEnabled(false);

            neighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getNeighborhoodSize());
            significantNeighborhoodSizeField.setText("all");
            updateNeededFields();            
            allRuleTestRadioButton.doClick();
            
            allNeighborhoodRuleTestRadioButton.setEnabled(false); //? why?~~~~
            singleRuleTestRadioButton.setEnabled(false);
        } else {
            allNeighborhoodRuleTestRadioButton.setEnabled(true);
            singleRuleTestRadioButton.setEnabled(true);
        }
    }//GEN-LAST:event_testAllNeighborhoodsRadioButtonItemStateChanged

    private void testAllNeighborhoodSizesRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testAllNeighborhoodSizesRadioButtonItemStateChanged
        if (testAllNeighborhoodSizesRadioButton.isSelected()) {
            ALGORITHM.Neighborhood.setTestAllNeighborhoodSizesAndPermutations(true);

            neighborhoodField.setText("all");
            neighborhoodField.setEnabled(false);

            neighborhoodSizeField.setEnabled(true);
            significantNeighborhoodSizeField.setEnabled(true);

            neighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getNeighborhoodSize());
            significantNeighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getSignificantNeighborhoodSize());
            updateNeededFields();        }
    }//GEN-LAST:event_testAllNeighborhoodSizesRadioButtonItemStateChanged

    private void testAllNeighborhoodVariationsRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testAllNeighborhoodVariationsRadioButtonItemStateChanged
        if (testAllNeighborhoodVariationsRadioButton.isSelected()) {
            ALGORITHM.Neighborhood.setTestAllNeighborhoodSizesAndPermutations(false);
            ALGORITHM.Neighborhood.setTestAllNeighborhoodPermutations(true);

            neighborhoodField.setText("all");
            neighborhoodField.setEnabled(false);

            neighborhoodSizeField.setEnabled(true);
            significantNeighborhoodSizeField.setEnabled(true);

            neighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getNeighborhoodSize());
            significantNeighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getSignificantNeighborhoodSize());
            updateNeededFields();
        }
}//GEN-LAST:event_testAllNeighborhoodVariationsRadioButtonItemStateChanged

    private void testSingleNeighborhoodRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testSingleNeighborhoodRadioButtonItemStateChanged
        if (testSingleNeighborhoodRadioButton.isSelected()) {
            ALGORITHM.Function.setSingleTestCase(true);

            neighborhoodField.setEnabled(true);

            neighborhoodSizeField.setEnabled(false);
            significantNeighborhoodSizeField.setEnabled(false);

            neighborhoodField.setText(ALGORITHM.Neighborhood.getNeighborhoodString());
            significantNeighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getSignificantNeighborhoodSize());
            updateNeededFields();
        }
    }//GEN-LAST:event_testSingleNeighborhoodRadioButtonItemStateChanged

    private void ruleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ruleFieldFocusLost
        // TODO: nicht erlauben falls mehrere cell states oder mehrere significante neighborhoods eingestellt sind
        checkRuleField();

        updateRuleFields();
    }//GEN-LAST:event_ruleFieldFocusLost

    private void neighborhoodSizeFieldChanged() {
        try {
            int result = ALGORITHM.Neighborhood.parseStandardNeighborhoodRange(neighborhoodSizeField.getText());
            //Integer.parseInt(significantNeighborhoodSizeField.getText()), Integer.parseInt(neighborhoodSizeField.getText()));
            switch (result) {
                case 0:
                    break;
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
            if (result != 0) {
                if (significantNeighborhoodSizeField.getText().compareTo("all") != 0) {
                    significantNeighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getSignificantNeighborhoodSize());
                }
                neighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getNeighborhoodSize());
                neighborhoodSizeField.requestFocusInWindow();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals into the Neighborhood Size field", "Wrong number format", JOptionPane.ERROR_MESSAGE);
            neighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getNeighborhoodSize());
            if (significantNeighborhoodSizeField.getText().compareTo("all") != 0) {
                significantNeighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getSignificantNeighborhoodSize());
            }
            neighborhoodSizeField.requestFocusInWindow();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "INTERNAL ERROR setting neighborhood", JOptionPane.ERROR_MESSAGE);
        }

        updateNeededFields();        
    }
    
    private void neighborhoodSizeFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_neighborhoodSizeFieldFocusLost
        neighborhoodSizeFieldChanged();
    }//GEN-LAST:event_neighborhoodSizeFieldFocusLost

    private void significantNeighborhoodSizeFieldChanged() {
        try {
            int result = ALGORITHM.Neighborhood.createStandardNeighborhoodRange(Integer.parseInt(significantNeighborhoodSizeField.getText()), Integer.parseInt(neighborhoodSizeField.getText()));
            switch (result) {
                case 0:
                    break;
                case -1:
                    JOptionPane.showMessageDialog(this, "Please enter a Significant Neighborhood Size larger than 2.\n The Significant Neighborhood Size is defined by the the number of neighborhood locations that the rule uses", "Significant Neighborhood Size out of range", JOptionPane.ERROR_MESSAGE);
                    break;
                case -2:
                    JOptionPane.showMessageDialog(this, "Significant Neighborhood Size can only be as large as Neighborhood Size", "Significant Neighborhood Size too large", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            if (result != 0) {
                if (neighborhoodSizeField.getText().compareTo("all") != 0) {
                    neighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getNeighborhoodSize());
                }
                significantNeighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getSignificantNeighborhoodSize());
                significantNeighborhoodSizeField.requestFocusInWindow();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals into the Significant Neighborhood Size field", "Wrong number format", JOptionPane.ERROR_MESSAGE);
            if (neighborhoodSizeField.getText().compareTo("all") != 0) {
                neighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getNeighborhoodSize());
            }
            significantNeighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getSignificantNeighborhoodSize());
            significantNeighborhoodSizeField.requestFocusInWindow();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "INTERNAL ERROR setting significant neighborhood", JOptionPane.ERROR_MESSAGE);
        }

        updateNeededFields();
        updateRuleFields();
        neighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getNeighborhoodSize());
    }
    
    private void significantNeighborhoodSizeFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_significantNeighborhoodSizeFieldFocusLost
        significantNeighborhoodSizeFieldChanged();
    }//GEN-LAST:event_significantNeighborhoodSizeFieldFocusLost

    private void updateNeededFields() {
        neededMemoryField.setText("" + ALGORITHM.Function.getNeededMemorySizeString());
        neededTimeField.setText("" + ALGORITHM.Function.getTotalNeededTimeString());
        ALGORITHM.Function.updateTotalSteps();
        rulesToTestField.setText("" + ALGORITHM.Function.getTotalCalculationSteps().toString());                
    }
    
    private void checkCellStatesField() {
        try {
            int result = ALGORITHM.Function.setNumberOfCellStatesRange(numberOfCellStatesField.getText());
            switch (result) {
                case -1:
                    JOptionPane.showMessageDialog(this, "Number of Cell States out of range.\n" +
                            "Please use a value larger than " + ALGORITHM.Function.MIN_NUMBER_OF_CELL_STATES + " and when specifing a range put the lower limit first (e.g. '3-5' instead of '5-3').", "Number of Cell States out of range", JOptionPane.ERROR_MESSAGE);
                    break;
                case -2:
                    JOptionPane.showMessageDialog(this, "Number of Cell States out of range [out of memory]\n" +
                            "Please use a value smaller than or equal " + ALGORITHM.Function.calculateMaxNumberOfCellStates() + ".", "Number of Cell States out of range", JOptionPane.ERROR_MESSAGE);
                    break;
                case -3:
                    JOptionPane.showMessageDialog(this, "Wrong number of entries in the Cell States field.\n" +
                            "Please enter either a single value or two values parted by a '-' (e.g. '2-4').",
                            "Invalid number of entries",
                            JOptionPane.ERROR_MESSAGE);
                    break;
            }
            if (result != 0) {
                numberOfCellStatesField.setText(ALGORITHM.Function.getNumberOfCellStatesString());
                numberOfCellStatesField.requestFocusInWindow();
                return;
            }
            updateNeededFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals into the Number of Cell States field",
                    "Wrong number format",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "INTERNAL ERROR creating function with new cell state range", JOptionPane.ERROR_MESSAGE);
        }


    }

    private void numberOfCellStatesFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numberOfCellStatesFieldFocusLost

        checkCellStatesField();

        updateRuleFields();
        
    }//GEN-LAST:event_numberOfCellStatesFieldFocusLost

    private void loadFromFields() {
        try {
            if (neighborhoodField.isEnabled()) {
                ALGORITHM.Neighborhood.setSignificantNeighborhood(ALGORITHM.Neighborhood.parseSignificantNeighborhoodString(neighborhoodField.getText()));
            }
            if (numberOfCellStatesField.isEnabled()) {
                ALGORITHM.Function.setNumberOfCellStatesRange(numberOfCellStatesField.getText());
            }
            if (significantNeighborhoodSizeField.isEnabled()) {
                ALGORITHM.Neighborhood.createStandardNeighborhoodRange(Integer.parseInt(significantNeighborhoodSizeField.getText()), Integer.parseInt(neighborhoodSizeField.getText()));
            }
            if (neighborhoodSizeField.isEnabled()) {
                ALGORITHM.Neighborhood.parseStandardNeighborhoodRange(neighborhoodSizeField.getText());
            }
            if (ruleField.isEnabled()) {
                ALGORITHM.Function.createSignificantFunction(new BigInteger(ruleField.getText()));
            }
        } catch (Exception e) {
            System.out.println("Error loading from fields: " + e);
        }
    }

    private void neighborhoodFieldChanged() {
        try {
            int result = ALGORITHM.Neighborhood.setSignificantNeighborhood(ALGORITHM.Neighborhood.parseSignificantNeighborhoodString(neighborhoodField.getText()));
            switch (result) {
                case 0:
                    break;
                case -1:
                    JOptionPane.showMessageDialog(this, "Please enter a significant neighborhood size larger than 2.", "Significant Neighborhood Size out of range", JOptionPane.ERROR_MESSAGE);
                    break;
                case -2:
                    JOptionPane.showMessageDialog(this, "Please check the order of your neighborhood", "Wrong order", JOptionPane.WARNING_MESSAGE);
//TODO evtl zulassen, nachfragen und selber ordnen
                    break;
                case -3:
                    JOptionPane.showMessageDialog(this, "Neighborhood out of range, you need at least " + ALGORITHM.Neighborhood.MIN_NEIGHBORHOOD_SIZE + " different entries and\n at most a difference of " + (ALGORITHM.Function.calculateMaxNeighborhoodSize() - 1) + " between first and last entry with the current number of cell states (e.g. '0, 1, 7' for a distance of 7).", "Neighborhood out of range", JOptionPane.WARNING_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "INTERNAL ERROR", "Error Assigning Neighborhood", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            if (result != 0) {
                neighborhoodField.setText(ALGORITHM.Neighborhood.getNeighborhoodString());
                neighborhoodField.requestFocusInWindow();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals seperated by commas into the Neighborhood field (" + e + ")", "Wrong number format", JOptionPane.ERROR_MESSAGE);
            neighborhoodField.setText(ALGORITHM.Neighborhood.getNeighborhoodString());
            neighborhoodField.requestFocusInWindow();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "INTERNAL ERROR when assigning the neighborhood (" + e + ")", JOptionPane.ERROR_MESSAGE);
        }

        updateRuleFields();

        if (neighborhoodSizeField.getText().compareTo("all") != 0) {
            neighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getNeighborhoodSize());
        }
        if (significantNeighborhoodSizeField.getText().compareTo("all") != 0) {
            significantNeighborhoodSizeField.setText("" + ALGORITHM.Neighborhood.getSignificantNeighborhoodSize());
        }
        updateNeededFields();        
    }
    
    private void neighborhoodFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_neighborhoodFieldFocusLost
        neighborhoodFieldChanged();
    }//GEN-LAST:event_neighborhoodFieldFocusLost

    private void showImageButtonMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_showImageButtonMouseClicked
    {//GEN-HEADEREND:event_showImageButtonMouseClicked
        int[] list = resultTable.getSelectedRows();
        Vector<Integer> graph_list = new Vector<Integer>();
        for (int i = 0; i < list.length; i++) {
            if (results.hasImage(list[i])) {
                graph_list.add(new Integer(list[i]));
            }
        }

        if (graph_list.size() == 0) {
            JOptionPane.showMessageDialog(this, "No entries with graphs selected", "No entry selected", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (graph_list.size() >= 16) {
            if (JOptionPane.showConfirmDialog(this, "This will open " + graph_list.size() + " new windows.\nContinue?",
                    "Confirm opening of multiple windows",
                    JOptionPane.YES_NO_OPTION) == 1) {
                return;
            }
        }
        for (int i = 0; i < graph_list.size(); i++) {
            String file_name = ALGORITHM.Function.getImageFileName(results.getRow(graph_list.get(i))) + ".viz.png";
            try {
                BufferedImage image = ImageIO.read(new File(file_name));
                ImageRenderComponent irc = new ImageRenderComponent(image);
                irc.setOpaque(true);  // for use as a content pane
                JFrame f = new JFrame();
                f.setTitle(file_name);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setContentPane(irc);
                f.setSize(400, 400);
                f.setLocation(200, 200);
                f.setVisible(true);

            } catch (IOException e) {
// TODO                
//JOptionPane.showMessageDialog(this, "Error opening etc.", "No entry selected", JOptionPane.ERROR_MESSAGE);
                continue;
            }
        //JOptionPane.showMessageDialog(this, file_name, file_name, JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_showImageButtonMouseClicked

    private void startCalculationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startCalculationButtonMouseClicked
        ALGORITHM.Function.updateTotalSteps();
        prepareCalculation();
            try {
                ALGORITHM.Neighborhood.initializeMinimalNeighborhoodSizeValue();//initializeMinimalNeighborhoodValue();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e, "INTERNAL ERROR when initializing minimal neighborhood", JOptionPane.ERROR_MESSAGE);
                clearUpAfterCalculation();
                return;
            }
            try {
                if (!(new File("graphs")).exists()) {
                    if (!(new File("graphs")).mkdir()) {
                        throw new Exception("Could not create directory 'graphs'");
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e, "INTERNAL ERROR when creating directory", JOptionPane.ERROR_MESSAGE);
                clearUpAfterCalculation();
                return;
            }

            ALGORITHM.Function.progress = ALGORITHM.Function.getTotalCalculationSteps();
            ALGORITHM.Function.lastUpdate = ALGORITHM.Function.progress;
            ALGORITHM.Function.lastUpdateTime = java.lang.System.currentTimeMillis();
            ALGORITHM.Function.SPEED_FACTOR = BigInteger.valueOf(500000);


            ALGORITHM.Function.testIfSingleCalculation();
            try {
                ALGORITHM.Function.initCurrentSignificantFunction();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e, "INTERNAL ERROR initializing Function", JOptionPane.ERROR_MESSAGE);
                clearUpAfterCalculation();
                return;
            }        

            
// start seperate calculation thread
        if ((calculation_thread != null) && (calculation_thread.isAlive())) {
            calculation_thread.interrupt();
        }
        while ((calculation_thread != null) && (calculation_thread.isAlive())) {
        }
        calculation_thread = new CalculationThread();
        calculation_thread.init_thread(this, progressBar, neededTimeField, generateGraphCheckBox.isSelected(), useFastCPPPluginCheckBox.isSelected(), outputAllRadioButton.isSelected(), outputSurjectiveRadioButton.isSelected(), outputOnlyInjectiveRadioButton.isSelected(), addToDatabaseCheckBox.isSelected(), checkDatabaseDuplicatesCheckBox.isSelected());

        calculation_thread.start();
    }//GEN-LAST:event_startCalculationButtonMouseClicked

    private void prepareCalculation() {
        stopCalculationButton.setEnabled(true);
        startCalculationButton.setEnabled(false);

        neighborhoodSizeWasActivated = neighborhoodSizeField.isEnabled();
        significantNeighborhoodSizeWasActivated = significantNeighborhoodSizeField.isEnabled();
        neighborhoodWasActivated = neighborhoodField.isEnabled();
        ruleNumberWasActivated = ruleField.isEnabled();
        neighborhoodSizeField.setEnabled(false);
        significantNeighborhoodSizeField.setEnabled(false);
        neighborhoodField.setEnabled(false);
        ruleField.setEnabled(false);
        booleanRuleField.setEnabled(false);
        numberOfCellStatesField.setEnabled(false);
        testSingleNeighborhoodRadioButton.setEnabled(false);
        testAllNeighborhoodVariationsRadioButton.setEnabled(false);
        testAllNeighborhoodSizesRadioButton.setEnabled(false);
        testAllNeighborhoodsRadioButton.setEnabled(false);
        singleRuleTestRadioButton.setEnabled(false);
        allRuleTestRadioButton.setEnabled(false);
        allNeighborhoodRuleTestRadioButton.setEnabled(false);
        outputAllRadioButton.setEnabled(false);
        outputSurjectiveRadioButton.setEnabled(false);
        outputOnlyInjectiveRadioButton.setEnabled(false);
        generateGraphCheckBox.setEnabled(false);
        addToDatabaseCheckBox.setEnabled(false);
        checkDatabaseDuplicatesCheckBox.setEnabled(false);
        useFastCPPPluginCheckBox.setEnabled(false);
        endProgramButton.setEnabled(false);
        saveEndProgramButton.setEnabled(false);
        polynomialRuleCheckBox.setEnabled(false);

        neededTimeLabel.setText("Remaining calculation time");
        // ... calculation, progress bar, extra thread?

        // TODO: pruefen ob 'generate graph' aktiviert wurde und auf Problemgroesse testen -> groesser als 5 kaum sinnvoll (VIZ kann es nicht) => User fragen.


        progressBar.setMaximum(1000);
        progressBar.setValue(0);
        
    }
    
    public void clearUpAfterCalculation() {
            results.fireTableStructureChanged(); // todo?

            stopCalculationButton.setEnabled(false);
            startCalculationButton.setEnabled(true);
            neighborhoodSizeField.setEnabled(neighborhoodSizeWasActivated);
            significantNeighborhoodSizeField.setEnabled(significantNeighborhoodSizeWasActivated);
            neighborhoodField.setEnabled(neighborhoodWasActivated);
            ruleField.setEnabled(ruleNumberWasActivated);
            if (ALGORITHM.Function.getNumberOfCellStates() != 2) {
                booleanRuleField.setEnabled(false);
                booleanRuleField.setText("only for binary case");
            } else if (!booleanRuleField.isEnabled()) {
                booleanRuleField.setEnabled(ruleNumberWasActivated);
            }
            numberOfCellStatesField.setEnabled(true);
            testSingleNeighborhoodRadioButton.setEnabled(true);
            testAllNeighborhoodVariationsRadioButton.setEnabled(true);
            testAllNeighborhoodSizesRadioButton.setEnabled(true);
            if (!allNeighborhoodRuleTestRadioButton.isSelected()) {
                testAllNeighborhoodsRadioButton.setEnabled(true);
            }
            singleRuleTestRadioButton.setEnabled(true);
            allRuleTestRadioButton.setEnabled(true);
            if (!testAllNeighborhoodsRadioButton.isSelected()) {
                allNeighborhoodRuleTestRadioButton.setEnabled(true);
            }
            outputAllRadioButton.setEnabled(true);
            outputSurjectiveRadioButton.setEnabled(true);
            outputOnlyInjectiveRadioButton.setEnabled(true);
            if (!useFastCPPPluginCheckBox.isSelected()) {
                generateGraphCheckBox.setEnabled(true);
            }
            addToDatabaseCheckBox.setEnabled(true);
            checkDatabaseDuplicatesCheckBox.setEnabled(true);
            useFastCPPPluginCheckBox.setEnabled(true);
// TODO            polynomialRuleCheckBox.setEnabled(true);

            endProgramButton.setEnabled(true);
            saveEndProgramButton.setEnabled(true);

            neededTimeLabel.setText("Needed calculation time");
            updateNeededFields();
            
            progressBar.setValue(1000);        
    }
    
    private void loadResultsIntoDatabase(BufferedReader p) throws NumberFormatException, IOException {
        results.datas.clear();
        String t;
        while ((t = p.readLine()) != null) {
            Object[] object_row = ALGORITHM.Function.parseParametersAsString(t);
            object_row[7] = new Boolean((new File(ALGORITHM.Function.getImageFileName(object_row) + ".viz")).exists());
            object_row[8] = new Boolean((new File(ALGORITHM.Function.getImageFileName(object_row) + ".viz.png")).exists());
            results.datas.add(object_row);
            results.fireTableDataChanged();
        }
    }

    private void loadResultsButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadResultsButtonMouseClicked
        if (fileChooser.showOpenDialog(this) == fileChooser.APPROVE_OPTION) {
            File my_file = fileChooser.getSelectedFile();
            if (!my_file.exists()) {
                JOptionPane.showMessageDialog(this, "Error opening file " + my_file.getAbsoluteFile(), "File not found", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                BufferedReader p = new BufferedReader(new FileReader(my_file.getAbsoluteFile()));
                loadResultsIntoDatabase(p);
                p.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading from file " + my_file.getAbsoluteFile(), "Error reading file", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error reading from file " + my_file.getAbsoluteFile(), "Error reading file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_loadResultsButtonMouseClicked

    private void eraseMarkedEntriesButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eraseMarkedEntriesButtonMouseClicked
        int[] list = resultTable.getSelectedRows();
        for (int i = 0; i < list.length; i++) {
            results.datas.remove(list[i] - i);
        }
        results.fireTableDataChanged();
    }//GEN-LAST:event_eraseMarkedEntriesButtonMouseClicked

    private void saveResultsButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveResultsButtonMouseClicked
        if (fileChooser.showSaveDialog(this) == fileChooser.APPROVE_OPTION) {
            File my_file = fileChooser.getSelectedFile();
            try {
                my_file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error opening file " + my_file.getAbsoluteFile(), "Error opening file", JOptionPane.ERROR_MESSAGE);
                return;
            }
            FileOutputStream f;
            PrintStream p;
            try {
                f = new FileOutputStream(my_file.getAbsoluteFile());
                p = new PrintStream(f);
                for (int i = 0; i < results.datas.size(); i++) {
                    Object[] t = (Object[]) results.datas.get(i);
                    p.println(
                            "" + (java.math.BigInteger) t[0] + ", " +
                            "\"" + (String) t[1] + "\", " +
                            "" + (java.lang.Integer) t[2] + ", " +
                            "" + (java.lang.Integer) t[3] + ", " +
                            "" + (java.lang.Integer) t[4] + ", " +
                            "" + (java.lang.Boolean) t[5] + ", " +
                            "" + (java.lang.Boolean) t[6]);
                }
                p.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error writing to file " + my_file.getAbsoluteFile() + " : " + e, "Error writing file", JOptionPane.ERROR_MESSAGE);
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

    private void ruleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ruleFieldActionPerformed
        checkRuleField();

        updateRuleFields();
    }//GEN-LAST:event_ruleFieldActionPerformed

    private void booleanRuleFieldChanged() {
        if(ALGORITHM.Function.getNumberOfCellStates() != 2) {
            return;
        }
        
        checkBooleanRuleField();
        
        //System.out.println(ALGORITHM.Function.getSignificantFunctionString());

        updateRuleFields();        
    }
    
    private void booleanRuleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_booleanRuleFieldFocusLost
        booleanRuleFieldChanged();
    }//GEN-LAST:event_booleanRuleFieldFocusLost

    private void numberOfCellStatesFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberOfCellStatesFieldActionPerformed
        checkCellStatesField();

        updateRuleFields();
    }//GEN-LAST:event_numberOfCellStatesFieldActionPerformed

    private void singleRuleTestRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_singleRuleTestRadioButtonItemStateChanged
            if(testAllNeighborhoodsRadioButton.isSelected()) {
                testAllNeighborhoodSizesRadioButton.doClick();
            }
            testAllNeighborhoodsRadioButton.setEnabled(false);
    }//GEN-LAST:event_singleRuleTestRadioButtonItemStateChanged

    private void polynomialRuleFieldChanged() {
        if(!polynomialRuleCheckBox.isSelected()) {
            return;
        }
        checkPolynomialRuleField();

        updateRuleFields();        
    }
    
    private void polynomialRuleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_polynomialRuleFieldFocusLost
        polynomialRuleFieldChanged();
    }//GEN-LAST:event_polynomialRuleFieldFocusLost

private void polynomialRuleCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_polynomialRuleCheckBoxActionPerformed
    if(polynomialRuleCheckBox.isSelected()) {
        polynomialRuleField.setEnabled(true);
        updateRuleFields();
    } else {
        polynomialRuleField.setEnabled(false);
        polynomialRuleField.setText("deactivated");
    }
}//GEN-LAST:event_polynomialRuleCheckBoxActionPerformed

private void endProgramButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_endProgramButtonMouseClicked
        setVisible(false);
        dispose();
        System.exit(0);
}//GEN-LAST:event_endProgramButtonMouseClicked

private void saveEndProgramButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveEndProgramButtonMouseClicked

        // save database, save rule, neigbhorhood etc.
        // reload in main!


        File my_file = new File("temp_results.txt");
        try {
            my_file.createNewFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error opening file " + my_file.getAbsoluteFile(), "Error opening file", JOptionPane.ERROR_MESSAGE);
            return;
        }
        FileOutputStream f;
        PrintStream p;
        try {
            f = new FileOutputStream(my_file.getAbsoluteFile());
            p = new PrintStream(f);

            p.println(testSingleNeighborhoodRadioButton.isSelected());
            p.println(testAllNeighborhoodVariationsRadioButton.isSelected());
            p.println(testAllNeighborhoodSizesRadioButton.isSelected());
            p.println(testAllNeighborhoodsRadioButton.isSelected());

            p.println(neighborhoodField.getText());
            p.println(significantNeighborhoodSizeField.getText());
            p.println(neighborhoodSizeField.getText());

            p.println(numberOfCellStatesField.getText());
            p.println(ruleField.getText());
            p.println(booleanRuleField.getText());

            p.println(outputAllRadioButton.isSelected());
            p.println(outputSurjectiveRadioButton.isSelected());
            p.println(outputOnlyInjectiveRadioButton.isSelected());

            p.println(generateGraphCheckBox.isSelected());
            p.println(addToDatabaseCheckBox.isSelected());
            p.println(checkDatabaseDuplicatesCheckBox.isSelected());
            p.println(useFastCPPPluginCheckBox.isSelected());
            p.println(polynomialRuleCheckBox.isSelected());

            for (int i = 0; i < results.datas.size(); i++) {
                Object[] t = (Object[]) results.datas.get(i);
                p.println(
                        "" + (java.math.BigInteger) t[0] + ", " +
                        "\"" + (String) t[1] + "\", " +
                        "" + (java.lang.Integer) t[2] + ", " +
                        "" + (java.lang.Integer) t[3] + ", " +
                        "" + (java.lang.Integer) t[4] + ", " +
                        "" + (java.lang.Boolean) t[5] + ", " +
                        "" + (java.lang.Boolean) t[6]);
            }
            p.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error writing to file " + my_file.getAbsoluteFile() + " : " + e, "Error writing file", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(false);
        dispose();
        System.exit(0);
}//GEN-LAST:event_saveEndProgramButtonMouseClicked

private void neighborhoodSizeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_neighborhoodSizeFieldActionPerformed
    neighborhoodSizeFieldChanged();
}//GEN-LAST:event_neighborhoodSizeFieldActionPerformed

private void neighborhoodFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_neighborhoodFieldActionPerformed
    neighborhoodFieldChanged();
}//GEN-LAST:event_neighborhoodFieldActionPerformed

private void significantNeighborhoodSizeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_significantNeighborhoodSizeFieldActionPerformed
    significantNeighborhoodSizeFieldChanged();
}//GEN-LAST:event_significantNeighborhoodSizeFieldActionPerformed

private void booleanRuleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_booleanRuleFieldActionPerformed
    booleanRuleFieldChanged();
}//GEN-LAST:event_booleanRuleFieldActionPerformed

private void polynomialRuleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_polynomialRuleFieldActionPerformed
    polynomialRuleFieldChanged();
}//GEN-LAST:event_polynomialRuleFieldActionPerformed

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
    private javax.swing.JRadioButton allNeighborhoodRuleTestRadioButton;
    private javax.swing.JRadioButton allRuleTestRadioButton;
    private javax.swing.JPanel automaticTestsPanel;
    private javax.swing.ButtonGroup automationNeighborhoodButtonGroup;
    private javax.swing.JPanel automationPanel;
    private javax.swing.ButtonGroup automationRuleButtonGroup;
    private javax.swing.JLabel booleanRepresentationLabel;
    private javax.swing.JTextField booleanRuleField;
    private javax.swing.JPanel calculationPanel;
    private javax.swing.JTabbedPane catestPane;
    private javax.swing.JCheckBox checkDatabaseDuplicatesCheckBox;
    private javax.swing.ButtonGroup chooseOutputInjectiveSurjectiveButtonGroup;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JLabel contactLabel1;
    private javax.swing.JButton endProgramButton;
    private javax.swing.JButton eraseAllEntriesButton;
    private javax.swing.JButton eraseMarkedEntriesButton;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JCheckBox generateGraphCheckBox;
    private javax.swing.JButton generateImageButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadResultsButton;
    private javax.swing.JLabel neededMemoryField;
    private javax.swing.JLabel neededMemoryLabel;
    private javax.swing.JLabel neededTimeField;
    private javax.swing.JLabel neededTimeLabel;
    private javax.swing.JPanel neighborhoodConfigurationPanel;
    private javax.swing.JTextField neighborhoodField;
    private javax.swing.JLabel neighborhoodLabel;
    private javax.swing.JTextField neighborhoodSizeField;
    private javax.swing.JLabel neighborhoodSizeLabel;
    private javax.swing.JTextField numberOfCellStatesField;
    private javax.swing.JLabel numberOfCellStatesLabel;
    private javax.swing.JRadioButton outputAllRadioButton;
    private javax.swing.JRadioButton outputOnlyInjectiveRadioButton;
    private javax.swing.JPanel outputOptionsPanel;
    private javax.swing.JRadioButton outputSurjectiveRadioButton;
    private javax.swing.JCheckBox polynomialRuleCheckBox;
    private javax.swing.JTextField polynomialRuleField;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressBarLabel;
    private javax.swing.JTable resultTable;
    private javax.swing.JPanel resultsPanel;
    private javax.swing.JPanel ruleConfigurationPanel;
    private javax.swing.JTextField ruleField;
    private javax.swing.JLabel ruleNumberLabel;
    private javax.swing.JLabel rulesToTestField;
    private javax.swing.JLabel rulesToTestLabel;
    private javax.swing.JButton saveEndProgramButton;
    private javax.swing.JButton saveResultsButton;
    private javax.swing.JButton showImageButton;
    private javax.swing.JTextField significantNeighborhoodSizeField;
    private javax.swing.JLabel significantNeighborhoodSizeLabel;
    private javax.swing.JRadioButton singleRuleTestRadioButton;
    private javax.swing.JButton startCalculationButton;
    private javax.swing.JButton stopCalculationButton;
    private javax.swing.JRadioButton testAllNeighborhoodSizesRadioButton;
    private javax.swing.JRadioButton testAllNeighborhoodVariationsRadioButton;
    private javax.swing.JRadioButton testAllNeighborhoodsRadioButton;
    private javax.swing.JPanel testPanel;
    private javax.swing.JRadioButton testSingleNeighborhoodRadioButton;
    private javax.swing.JCheckBox useFastCPPPluginCheckBox;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
//public void actionPerformed(ActionEvent event) {
}
