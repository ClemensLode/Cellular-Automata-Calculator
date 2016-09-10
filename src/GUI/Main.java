package GUI;

/**
 * Version 1.06b
 * @author Clemens Lode, 1151459, University Karlsruhe (TH), clemens@lode.de
 */
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import java.math.BigInteger;

public class Main extends java.awt.Frame {

    /**
     * worker thread for calculation
     */
    CalculationThread calculation_thread;
    
    /**
     * result database
     */
    ResultsTable results = new ResultsTable();
    TableSorter sorter = new TableSorter(results);
    
    /**
     * Objects for the simulator output
     */
    static BufferedImage simulationImage;
    static JFrame simulatorFrame = null;
    static ImageRenderComponent simulatorIRC = null;

    /**
     * temporary variables to save whether certain fields were activated
     */
    boolean minNeighborhoodSizeWasActivated = false;
    boolean minSignificantNeighborhoodSizeWasActivated = false;
    boolean maxNeighborhoodSizeWasActivated = false;
    boolean maxSignificantNeighborhoodSizeWasActivated = false;
    boolean neighborhoodWasActivated = false;
    boolean ruleNumberWasActivated = false;
    
    
    /** Creates new form Main */
    public Main() {
        initComponents();
        ALGORITHM.PolynomialRule.init_p4();

        try {
            ALGORITHM.Function.createStandardSignificantFunction();
        } catch (Exception e) {
            System.out.println(e);
        // TODO
        }

        minSignificantNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMinSignificantNeighborhoodSize());
        maxSignificantNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMaxSignificantNeighborhoodSize());
        minNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMinNeighborhoodSize());
        maxNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMaxNeighborhoodSize());

        neighborhoodField.setText(ALGORITHM.Neighborhood.getSignificantNeighborhoodString());

        numberOfCellStatesField.setText("" + ALGORITHM.CellStates.getNumberOfCellStates());

        updateNeededRessourcesFields();
        results.fireTableStructureChanged();

        resultTable.setModel(sorter);
        sorter.setTableHeader(resultTable.getTableHeader());
        resultTable.getTableHeader().setToolTipText(
                "Click to specify sorting; Control-Click to specify secondary sorting");

        loadSettings("temp_results.txt");

        updateRuleFields();

    }

    /**
     * Load GUI settings from a file
     * @param file_name name of file
     */
    private void loadSettings(String file_name) {
        // load old settings if file exists
        File my_file = new File(file_name);
        if (my_file.exists()) {

            try {
                BufferedReader p = new BufferedReader(new FileReader(my_file.getAbsoluteFile()));

                testAllNeighborhoodVariationsCheckBox.setSelected(Boolean.valueOf(p.readLine()));
                testAllNeighborhoodPermutationsCheckBox.setSelected(Boolean.valueOf(p.readLine()));
                testAllBalancedRulesCheckBox.setSelected(Boolean.valueOf(p.readLine()));



                String neighborhood = p.readLine();
                if (neighborhoodField.isEnabled()) {
                    neighborhoodField.setText(neighborhood);
                }

                minSignificantNeighborhoodSizeTextField.setText(p.readLine());
                maxSignificantNeighborhoodSizeTextField.setText(p.readLine());
                minNeighborhoodSizeTextField.setText(p.readLine());
                maxNeighborhoodSizeTextField.setText(p.readLine());

                numberOfCellStatesField.setText(p.readLine());

                String rule_field = p.readLine();
                if (ruleField.isEnabled()) {
                    ruleField.setText(rule_field);
                }

                boolean output_all = Boolean.valueOf(p.readLine());
                boolean output_surjective = Boolean.valueOf(p.readLine());
                boolean output_only_injective = Boolean.valueOf(p.readLine());

                if (output_all) {
                    outputAllRadioButton.doClick();
                } else if (output_surjective) {
                    outputSurjectiveRadioButton.doClick();
                } else if (output_only_injective) {
                    outputOnlyInjectiveRadioButton.doClick();
                }

                boolean add_to_database = Boolean.valueOf(p.readLine());
                boolean generate_graph = Boolean.valueOf(p.readLine());
                boolean simulator_output = Boolean.valueOf(p.readLine());
                boolean check_duplicates = Boolean.valueOf(p.readLine());
                boolean use_fast_cpp_plugin = Boolean.valueOf(p.readLine());

                boolean show_polynomial_rule = Boolean.valueOf(p.readLine());

                boolean boolean_output = Boolean.valueOf(p.readLine());
                boolean polynomial_output = Boolean.valueOf(p.readLine());

                generateGraphCheckBox.setSelected(generate_graph);
                simulatorOutputCheckBox.setSelected(simulator_output);
                addToDatabaseCheckBox.setSelected(add_to_database);
                checkDatabaseDuplicatesCheckBox.setSelected(check_duplicates);
                useFastCPPPluginCheckBox.setSelected(use_fast_cpp_plugin);

                polynomialRuleCheckBox.setSelected(show_polynomial_rule);

                outputBooleanRepresentationCheckBox.setSelected(boolean_output);
                outputPolynomialRepresentationCheckBox.setSelected(polynomial_output);

                calculationStepsTextField.setText(p.readLine());
                simulatorConfigurationSizeTextField.setText(p.readLine());
                startConfigurationTextField.setText(p.readLine());


                loadFromFields();

                loadResultsIntoDatabase(p);
                p.close();
            } catch (IOException e) {
                System.out.println("IO Exception: Error " + e + " reading from file " + my_file.getAbsoluteFile());
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException: Error " + e + " reading from file " + my_file.getAbsoluteFile());
            } catch (Exception e) {
                System.out.println("Exception: Error " + e);
            }

            if (!my_file.delete()) {
                System.out.println("Could not delete cfg file " + file_name + ".");
            }
        }

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
        minNeighborhoodSizeTextField = new javax.swing.JTextField();
        minSignificantNeighborhoodSizeTextField = new javax.swing.JTextField();
        neighborhoodField = new javax.swing.JTextField();
        significantNeighborhoodSizeLabel = new javax.swing.JLabel();
        neighborhoodLabel = new javax.swing.JLabel();
        numberOfCellStatesLabel = new javax.swing.JLabel();
        numberOfCellStatesField = new javax.swing.JTextField();
        slashLabel = new javax.swing.JLabel();
        dots1Label = new javax.swing.JLabel();
        maxSignificantNeighborhoodSizeTextField = new javax.swing.JTextField();
        maxNeighborhoodSizeTextField = new javax.swing.JTextField();
        dots2Label = new javax.swing.JLabel();
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
        testAllBalancedRulesCheckBox = new javax.swing.JCheckBox();
        outputOptionsPanel = new javax.swing.JPanel();
        outputAllRadioButton = new javax.swing.JRadioButton();
        outputSurjectiveRadioButton = new javax.swing.JRadioButton();
        outputOnlyInjectiveRadioButton = new javax.swing.JRadioButton();
        outputBooleanRepresentationCheckBox = new javax.swing.JCheckBox();
        outputPolynomialRepresentationCheckBox = new javax.swing.JCheckBox();
        contactPanel = new javax.swing.JPanel();
        saveEndProgramButton = new javax.swing.JButton();
        versionLabel = new javax.swing.JLabel();
        contactLabel1 = new javax.swing.JLabel();
        contactLabel = new javax.swing.JLabel();
        endProgramButton = new javax.swing.JButton();
        simulatorStartConfigurationPanel = new javax.swing.JPanel();
        simulatorConfigurationSizeLabel = new javax.swing.JLabel();
        simulatorConfigurationSizeTextField = new javax.swing.JTextField();
        startConfigurationLabel = new javax.swing.JLabel();
        startConfigurationTextField = new javax.swing.JTextField();
        saveSimulatorConfigurationButton = new javax.swing.JButton();
        loadSimulatorConfigurationButton = new javax.swing.JButton();
        generateNewSimulatorConfigurationButton = new javax.swing.JButton();
        zoomLabel = new javax.swing.JLabel();
        zoomSlider = new javax.swing.JSlider();
        calculationStepsTextField = new javax.swing.JTextField();
        calculationStepsLabel = new javax.swing.JLabel();
        startSimulationButton = new javax.swing.JButton();
        automaticTestsPanel = new javax.swing.JPanel();
        testAllNeighborhoodVariationsCheckBox = new javax.swing.JCheckBox();
        testAllNeighborhoodPermutationsCheckBox = new javax.swing.JCheckBox();
        miscOptionsPanel = new javax.swing.JPanel();
        useFastCPPPluginCheckBox = new javax.swing.JCheckBox();
        checkDatabaseDuplicatesCheckBox = new javax.swing.JCheckBox();
        addToDatabaseCheckBox = new javax.swing.JCheckBox();
        generateGraphCheckBox = new javax.swing.JCheckBox();
        simulatorOutputCheckBox = new javax.swing.JCheckBox();
        resultsPanel = new javax.swing.JPanel();
        resultsScrollPane = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();
        eraseMarkedEntriesButton = new javax.swing.JButton();
        saveResultsButton = new javax.swing.JButton();
        loadResultsButton = new javax.swing.JButton();
        showImageButton = new javax.swing.JButton();
        eraseAllEntriesButton = new javax.swing.JButton();
        generateImageButton = new javax.swing.JButton();
        generateSimulationButton = new javax.swing.JButton();
        showSimulationButton = new javax.swing.JButton();

        setBackground(java.awt.Color.lightGray);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        setFocusTraversalPolicyProvider(true);
        setResizable(false);
        setTitle("Simulate 1D-CA and test CA-surjectivity and -injectivity");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        neighborhoodConfigurationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Neighborhood Configuration", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11)));

        minNeighborhoodSizeTextField.setText("2");
        minNeighborhoodSizeTextField.setFocusCycleRoot(true);
        minNeighborhoodSizeTextField.setFocusTraversalPolicyProvider(true);
        minNeighborhoodSizeTextField.setPreferredSize(new java.awt.Dimension(80, 20));
        minNeighborhoodSizeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minNeighborhoodSizeTextFieldActionPerformed(evt);
            }
        });
        minNeighborhoodSizeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                minNeighborhoodSizeTextFieldFocusLost(evt);
            }
        });

        minSignificantNeighborhoodSizeTextField.setText("2");
        minSignificantNeighborhoodSizeTextField.setEnabled(false);
        minSignificantNeighborhoodSizeTextField.setFocusCycleRoot(true);
        minSignificantNeighborhoodSizeTextField.setFocusTraversalPolicyProvider(true);
        minSignificantNeighborhoodSizeTextField.setPreferredSize(new java.awt.Dimension(80, 20));
        minSignificantNeighborhoodSizeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minSignificantNeighborhoodSizeTextFieldActionPerformed(evt);
            }
        });
        minSignificantNeighborhoodSizeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                minSignificantNeighborhoodSizeTextFieldFocusLost(evt);
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

        significantNeighborhoodSizeLabel.setFont(new java.awt.Font("Arial", 0, 12));
        significantNeighborhoodSizeLabel.setText("Significant Neighborhood Size / Neighborhood Size");

        neighborhoodLabel.setFont(new java.awt.Font("Arial", 1, 12));
        neighborhoodLabel.setText("Neighborhood");

        numberOfCellStatesLabel.setFont(new java.awt.Font("Arial", 1, 12));
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

        slashLabel.setFont(new java.awt.Font("Arial", 0, 12));
        slashLabel.setText("/");

        dots1Label.setFont(new java.awt.Font("Arial", 0, 12));
        dots1Label.setText("...");

        maxSignificantNeighborhoodSizeTextField.setText("2");
        maxSignificantNeighborhoodSizeTextField.setEnabled(false);
        maxSignificantNeighborhoodSizeTextField.setFocusCycleRoot(true);
        maxSignificantNeighborhoodSizeTextField.setFocusTraversalPolicyProvider(true);
        maxSignificantNeighborhoodSizeTextField.setPreferredSize(new java.awt.Dimension(80, 20));
        maxSignificantNeighborhoodSizeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxSignificantNeighborhoodSizeTextFieldActionPerformed(evt);
            }
        });
        maxSignificantNeighborhoodSizeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                maxSignificantNeighborhoodSizeTextFieldFocusLost(evt);
            }
        });

        maxNeighborhoodSizeTextField.setText("2");
        maxNeighborhoodSizeTextField.setFocusCycleRoot(true);
        maxNeighborhoodSizeTextField.setFocusTraversalPolicyProvider(true);
        maxNeighborhoodSizeTextField.setPreferredSize(new java.awt.Dimension(80, 20));
        maxNeighborhoodSizeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxNeighborhoodSizeTextFieldActionPerformed(evt);
            }
        });
        maxNeighborhoodSizeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                maxNeighborhoodSizeTextFieldFocusLost(evt);
            }
        });

        dots2Label.setFont(new java.awt.Font("Arial", 0, 12));
        dots2Label.setText("...");

        org.jdesktop.layout.GroupLayout neighborhoodConfigurationPanelLayout = new org.jdesktop.layout.GroupLayout(neighborhoodConfigurationPanel);
        neighborhoodConfigurationPanel.setLayout(neighborhoodConfigurationPanelLayout);
        neighborhoodConfigurationPanelLayout.setHorizontalGroup(
            neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(neighborhoodConfigurationPanelLayout.createSequentialGroup()
                .add(neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(neighborhoodConfigurationPanelLayout.createSequentialGroup()
                        .add(51, 51, 51)
                        .add(significantNeighborhoodSizeLabel))
                    .add(neighborhoodConfigurationPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(numberOfCellStatesLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(numberOfCellStatesField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(neighborhoodLabel)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(neighborhoodConfigurationPanelLayout.createSequentialGroup()
                        .add(minSignificantNeighborhoodSizeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(dots1Label)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(maxSignificantNeighborhoodSizeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(slashLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(12, 12, 12)
                        .add(minNeighborhoodSizeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(dots2Label)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(maxNeighborhoodSizeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(neighborhoodField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 242, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        neighborhoodConfigurationPanelLayout.linkSize(new java.awt.Component[] {maxNeighborhoodSizeTextField, maxSignificantNeighborhoodSizeTextField, minNeighborhoodSizeTextField, minSignificantNeighborhoodSizeTextField}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        neighborhoodConfigurationPanelLayout.setVerticalGroup(
            neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(neighborhoodConfigurationPanelLayout.createSequentialGroup()
                .add(neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(numberOfCellStatesField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(numberOfCellStatesLabel)
                    .add(neighborhoodLabel)
                    .add(neighborhoodField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(neighborhoodConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dots2Label)
                    .add(slashLabel)
                    .add(minNeighborhoodSizeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(maxNeighborhoodSizeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dots1Label)
                    .add(minSignificantNeighborhoodSizeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(maxSignificantNeighborhoodSizeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(significantNeighborhoodSizeLabel)))
        );

        calculationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Calculation", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));

        neededTimeLabel.setFont(new java.awt.Font("Arial", 0, 12));
        neededTimeLabel.setText("Needed calculation time");

        neededTimeField.setFont(new java.awt.Font("Arial", 0, 12));
        neededTimeField.setText("0s");
        neededTimeField.setToolTipText("Estimated time for the whole calculation, including testing of multiple rules/neighborhoods/cell states");

        neededMemoryLabel.setFont(new java.awt.Font("Arial", 0, 12));
        neededMemoryLabel.setText("Needed Memory");

        neededMemoryField.setFont(new java.awt.Font("Arial", 0, 12));
        neededMemoryField.setText("0mb");
        neededMemoryField.setToolTipText("Upper limit of memory that will be needed for the calculation");

        progressBar.setStringPainted(true);

        progressBarLabel.setFont(new java.awt.Font("Arial", 0, 12));
        progressBarLabel.setText("Calculation Progress");

        stopCalculationButton.setFont(new java.awt.Font("Arial", 0, 12));
        stopCalculationButton.setText("Stop Calculation");
        stopCalculationButton.setEnabled(false);
        stopCalculationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stopCalculationButtonMouseClicked(evt);
            }
        });

        startCalculationButton.setFont(new java.awt.Font("Arial", 1, 12));
        startCalculationButton.setText("Start Calculation");
        startCalculationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startCalculationButtonMouseClicked(evt);
            }
        });

        rulesToTestLabel.setFont(new java.awt.Font("Arial", 0, 12));
        rulesToTestLabel.setText("Number of nodes to calculate");

        rulesToTestField.setFont(new java.awt.Font("Arial", 0, 12));
        rulesToTestField.setText("1");

        org.jdesktop.layout.GroupLayout calculationPanelLayout = new org.jdesktop.layout.GroupLayout(calculationPanel);
        calculationPanel.setLayout(calculationPanelLayout);
        calculationPanelLayout.setHorizontalGroup(
            calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, calculationPanelLayout.createSequentialGroup()
                .add(24, 24, 24)
                .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(neededMemoryLabel)
                    .add(rulesToTestLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(neededTimeLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(calculationPanelLayout.createSequentialGroup()
                        .add(rulesToTestField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(neededTimeField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .add(neededMemoryField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
                .addContainerGap())
            .add(calculationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                    .add(calculationPanelLayout.createSequentialGroup()
                        .add(startCalculationButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 123, Short.MAX_VALUE)
                        .add(stopCalculationButton)))
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, calculationPanelLayout.createSequentialGroup()
                .addContainerGap(154, Short.MAX_VALUE)
                .add(progressBarLabel)
                .add(139, 139, 139))
        );

        calculationPanelLayout.linkSize(new java.awt.Component[] {startCalculationButton, stopCalculationButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        calculationPanelLayout.setVerticalGroup(
            calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, calculationPanelLayout.createSequentialGroup()
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
                .add(progressBarLabel)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(calculationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(startCalculationButton)
                    .add(stopCalculationButton))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        ruleConfigurationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Rule Configuration", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11)));

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

        polynomialRuleField.setText("deactivated");
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

        booleanRepresentationLabel.setFont(new java.awt.Font("Arial", 0, 12));
        booleanRepresentationLabel.setText("Boolean Representation");

        ruleNumberLabel.setFont(new java.awt.Font("Arial", 1, 12));
        ruleNumberLabel.setText("Rule Number");

        polynomialRuleCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
        polynomialRuleCheckBox.setText("Polynomial Representation");
        polynomialRuleCheckBox.setBorder(null);
        polynomialRuleCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                polynomialRuleCheckBoxActionPerformed(evt);
            }
        });

        testAllBalancedRulesCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
        testAllBalancedRulesCheckBox.setText("all balanced rules");
        testAllBalancedRulesCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testAllBalancedRulesCheckBoxItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout ruleConfigurationPanelLayout = new org.jdesktop.layout.GroupLayout(ruleConfigurationPanel);
        ruleConfigurationPanel.setLayout(ruleConfigurationPanelLayout);
        ruleConfigurationPanelLayout.setHorizontalGroup(
            ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(ruleConfigurationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(ruleConfigurationPanelLayout.createSequentialGroup()
                        .add(testAllBalancedRulesCheckBox)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 94, Short.MAX_VALUE)
                        .add(ruleNumberLabel))
                    .add(polynomialRuleCheckBox)
                    .add(booleanRepresentationLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(ruleField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                    .add(booleanRuleField)
                    .add(polynomialRuleField)))
        );
        ruleConfigurationPanelLayout.setVerticalGroup(
            ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(ruleConfigurationPanelLayout.createSequentialGroup()
                .add(ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(ruleNumberLabel)
                    .add(testAllBalancedRulesCheckBox)
                    .add(ruleField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(booleanRepresentationLabel)
                    .add(booleanRuleField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3)
                .add(ruleConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(polynomialRuleCheckBox)
                    .add(polynomialRuleField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        outputOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Database Output options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11)));

        chooseOutputInjectiveSurjectiveButtonGroup.add(outputAllRadioButton);
        outputAllRadioButton.setFont(new java.awt.Font("Arial", 0, 12));
        outputAllRadioButton.setSelected(true);
        outputAllRadioButton.setText("All");
        outputAllRadioButton.setToolTipText("Report all results");
        outputAllRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        outputAllRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        chooseOutputInjectiveSurjectiveButtonGroup.add(outputSurjectiveRadioButton);
        outputSurjectiveRadioButton.setFont(new java.awt.Font("Arial", 0, 12));
        outputSurjectiveRadioButton.setText("At least surjective");
        outputSurjectiveRadioButton.setToolTipText("Only report those results that are either surjective or surjective and injective");
        outputSurjectiveRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        outputSurjectiveRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        chooseOutputInjectiveSurjectiveButtonGroup.add(outputOnlyInjectiveRadioButton);
        outputOnlyInjectiveRadioButton.setFont(new java.awt.Font("Arial", 0, 12));
        outputOnlyInjectiveRadioButton.setText("At least injective");
        outputOnlyInjectiveRadioButton.setToolTipText("Filter all results that are not surjective or not injective");
        outputOnlyInjectiveRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        outputOnlyInjectiveRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        outputBooleanRepresentationCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
        outputBooleanRepresentationCheckBox.setText("Boolean Representation");
        outputBooleanRepresentationCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        outputBooleanRepresentationCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        outputPolynomialRepresentationCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
        outputPolynomialRepresentationCheckBox.setText("Polynomial Representation");
        outputPolynomialRepresentationCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        outputPolynomialRepresentationCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout outputOptionsPanelLayout = new org.jdesktop.layout.GroupLayout(outputOptionsPanel);
        outputOptionsPanel.setLayout(outputOptionsPanelLayout);
        outputOptionsPanelLayout.setHorizontalGroup(
            outputOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(outputOptionsPanelLayout.createSequentialGroup()
                .add(outputOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(outputAllRadioButton)
                    .add(outputSurjectiveRadioButton)
                    .add(outputOnlyInjectiveRadioButton)
                    .add(outputBooleanRepresentationCheckBox)
                    .add(outputPolynomialRepresentationCheckBox))
                .addContainerGap(22, Short.MAX_VALUE))
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
                .add(outputBooleanRepresentationCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outputPolynomialRepresentationCheckBox)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        saveEndProgramButton.setFont(new java.awt.Font("Arial", 1, 12));
        saveEndProgramButton.setText("Save & End Program");
        saveEndProgramButton.setToolTipText("Save the current database and settings and quit the program");
        saveEndProgramButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveEndProgramButtonMouseClicked(evt);
            }
        });

        versionLabel.setFont(new java.awt.Font("Arial", 0, 12));
        versionLabel.setText("Version 1.060d (12/1/2008)");

        contactLabel1.setFont(new java.awt.Font("Arial", 0, 12));
        contactLabel1.setText("clemens@lode.de");

        contactLabel.setFont(new java.awt.Font("Arial", 0, 12));
        contactLabel.setText("by Clemens Lode");

        endProgramButton.setFont(new java.awt.Font("Arial", 0, 12));
        endProgramButton.setText("End Program");
        endProgramButton.setToolTipText("Ends the program, all settings and the database are not saved");
        endProgramButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                endProgramButtonMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout contactPanelLayout = new org.jdesktop.layout.GroupLayout(contactPanel);
        contactPanel.setLayout(contactPanelLayout);
        contactPanelLayout.setHorizontalGroup(
            contactPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(contactPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(contactPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, contactPanelLayout.createSequentialGroup()
                        .add(contactPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, endProgramButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, saveEndProgramButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .add(contactPanelLayout.createSequentialGroup()
                        .add(contactPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(versionLabel)
                            .add(contactLabel)
                            .add(contactLabel1))
                        .addContainerGap(22, Short.MAX_VALUE))))
        );
        contactPanelLayout.setVerticalGroup(
            contactPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, contactPanelLayout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .add(versionLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(contactLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(contactLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(endProgramButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(saveEndProgramButton)
                .addContainerGap())
        );

        versionLabel.getAccessibleContext().setAccessibleName("Version 1.060b (09/22/2008)");

        simulatorStartConfigurationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Simulator Start Configuration", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11)));

        simulatorConfigurationSizeLabel.setFont(new java.awt.Font("Arial", 0, 12));
        simulatorConfigurationSizeLabel.setText("Configuration size");

        simulatorConfigurationSizeTextField.setText("256");
        simulatorConfigurationSizeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulatorConfigurationSizeTextFieldActionPerformed(evt);
            }
        });
        simulatorConfigurationSizeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                simulatorConfigurationSizeTextFieldFocusLost(evt);
            }
        });

        startConfigurationLabel.setFont(new java.awt.Font("Arial", 0, 12));
        startConfigurationLabel.setText("Start configuration");

        startConfigurationTextField.setText("0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1");

        saveSimulatorConfigurationButton.setFont(new java.awt.Font("Arial", 0, 12));
        saveSimulatorConfigurationButton.setText("Save...");
        saveSimulatorConfigurationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveSimulatorConfigurationButtonMouseClicked(evt);
            }
        });

        loadSimulatorConfigurationButton.setFont(new java.awt.Font("Arial", 0, 12));
        loadSimulatorConfigurationButton.setText("Load...");
        loadSimulatorConfigurationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadSimulatorConfigurationButtonMouseClicked(evt);
            }
        });

        generateNewSimulatorConfigurationButton.setFont(new java.awt.Font("Arial", 0, 12));
        generateNewSimulatorConfigurationButton.setText("Generate new");
        generateNewSimulatorConfigurationButton.setToolTipText("Generates new random start configuration");
        generateNewSimulatorConfigurationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                generateNewSimulatorConfigurationButtonMouseClicked(evt);
            }
        });

        zoomLabel.setFont(new java.awt.Font("Arial", 0, 12));
        zoomLabel.setText("Zoom");

        zoomSlider.setMaximum(16);
        zoomSlider.setMinimum(1);
        zoomSlider.setMinorTickSpacing(1);
        zoomSlider.setPaintLabels(true);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setSnapToTicks(true);
        zoomSlider.setValue(2);
        zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                zoomSliderStateChanged(evt);
            }
        });
        zoomSlider.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                zoomSliderCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });

        calculationStepsTextField.setText("256");
        calculationStepsTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculationStepsTextFieldActionPerformed(evt);
            }
        });
        calculationStepsTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                calculationStepsTextFieldFocusLost(evt);
            }
        });

        calculationStepsLabel.setFont(new java.awt.Font("Arial", 0, 12));
        calculationStepsLabel.setText("Calculation steps");

        startSimulationButton.setFont(new java.awt.Font("Arial", 1, 12));
        startSimulationButton.setText("Start Simulation");
        startSimulationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startSimulationButtonMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout simulatorStartConfigurationPanelLayout = new org.jdesktop.layout.GroupLayout(simulatorStartConfigurationPanel);
        simulatorStartConfigurationPanel.setLayout(simulatorStartConfigurationPanelLayout);
        simulatorStartConfigurationPanelLayout.setHorizontalGroup(
            simulatorStartConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, simulatorStartConfigurationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(simulatorStartConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(simulatorStartConfigurationPanelLayout.createSequentialGroup()
                        .add(simulatorConfigurationSizeLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(simulatorConfigurationSizeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(saveSimulatorConfigurationButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(loadSimulatorConfigurationButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(generateNewSimulatorConfigurationButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 48, Short.MAX_VALUE)
                        .add(startSimulationButton))
                    .add(simulatorStartConfigurationPanelLayout.createSequentialGroup()
                        .add(startConfigurationLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(startConfigurationTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 477, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(simulatorStartConfigurationPanelLayout.createSequentialGroup()
                        .add(zoomLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(zoomSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 396, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(calculationStepsLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(calculationStepsTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        simulatorStartConfigurationPanelLayout.setVerticalGroup(
            simulatorStartConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, simulatorStartConfigurationPanelLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .add(simulatorStartConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(simulatorConfigurationSizeLabel)
                    .add(simulatorConfigurationSizeTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(saveSimulatorConfigurationButton)
                    .add(loadSimulatorConfigurationButton)
                    .add(generateNewSimulatorConfigurationButton)
                    .add(startSimulationButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(simulatorStartConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(startConfigurationLabel)
                    .add(startConfigurationTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(simulatorStartConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(zoomLabel)
                    .add(simulatorStartConfigurationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(calculationStepsLabel)
                        .add(calculationStepsTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(zoomSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        automaticTestsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Automatic Tests", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11)));

        testAllNeighborhoodVariationsCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
        testAllNeighborhoodVariationsCheckBox.setText("all neighborhood variations");
        testAllNeighborhoodVariationsCheckBox.setAlignmentY(0.0F);
        testAllNeighborhoodVariationsCheckBox.setEnabled(false);
        testAllNeighborhoodVariationsCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        testAllNeighborhoodVariationsCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testAllNeighborhoodVariationsCheckBoxItemStateChanged(evt);
            }
        });

        testAllNeighborhoodPermutationsCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
        testAllNeighborhoodPermutationsCheckBox.setText("all neighborhood permutations");
        testAllNeighborhoodPermutationsCheckBox.setAlignmentY(0.0F);
        testAllNeighborhoodPermutationsCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        testAllNeighborhoodPermutationsCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testAllNeighborhoodPermutationsCheckBoxItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout automaticTestsPanelLayout = new org.jdesktop.layout.GroupLayout(automaticTestsPanel);
        automaticTestsPanel.setLayout(automaticTestsPanelLayout);
        automaticTestsPanelLayout.setHorizontalGroup(
            automaticTestsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(automaticTestsPanelLayout.createSequentialGroup()
                .add(automaticTestsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(testAllNeighborhoodVariationsCheckBox)
                    .add(testAllNeighborhoodPermutationsCheckBox))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        automaticTestsPanelLayout.setVerticalGroup(
            automaticTestsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(automaticTestsPanelLayout.createSequentialGroup()
                .add(testAllNeighborhoodVariationsCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testAllNeighborhoodPermutationsCheckBox))
        );

        miscOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Misc options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11)));

        useFastCPPPluginCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
        useFastCPPPluginCheckBox.setText("Use fast C plugin");
        useFastCPPPluginCheckBox.setToolTipText("Call external 'catest_c', faster + less memory usage, use only for small number of test cases");
        useFastCPPPluginCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        useFastCPPPluginCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        useFastCPPPluginCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                useFastCPPPluginCheckBoxItemStateChanged(evt);
            }
        });

        checkDatabaseDuplicatesCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
        checkDatabaseDuplicatesCheckBox.setToolTipText("Check database prior to each calculation");
        checkDatabaseDuplicatesCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        checkDatabaseDuplicatesCheckBox.setLabel("Skip already calculated");
        checkDatabaseDuplicatesCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        checkDatabaseDuplicatesCheckBox.setMaximumSize(new java.awt.Dimension(145, 15));
        checkDatabaseDuplicatesCheckBox.setMinimumSize(new java.awt.Dimension(145, 15));

        addToDatabaseCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
        addToDatabaseCheckBox.setSelected(true);
        addToDatabaseCheckBox.setText("Add result to database");
        addToDatabaseCheckBox.setToolTipText("Instead of a simple output all results will be put into a database (tab 'Results')");
        addToDatabaseCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        addToDatabaseCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));

        generateGraphCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
        generateGraphCheckBox.setText("Generate Graph (.viz file)");
        generateGraphCheckBox.setToolTipText("Generate File for GraphViz in order to later generate a graph file");
        generateGraphCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        generateGraphCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        generateGraphCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                generateGraphCheckBoxItemStateChanged(evt);
            }
        });

        simulatorOutputCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
        simulatorOutputCheckBox.setText("Simulator Output (.png file)");
        simulatorOutputCheckBox.setToolTipText("Call external 'catest_c', faster + less memory usage, use only for small number of test cases");
        simulatorOutputCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        simulatorOutputCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        simulatorOutputCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                simulatorOutputCheckBoxItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout miscOptionsPanelLayout = new org.jdesktop.layout.GroupLayout(miscOptionsPanel);
        miscOptionsPanel.setLayout(miscOptionsPanelLayout);
        miscOptionsPanelLayout.setHorizontalGroup(
            miscOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(miscOptionsPanelLayout.createSequentialGroup()
                .add(miscOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(useFastCPPPluginCheckBox)
                    .add(generateGraphCheckBox)
                    .add(addToDatabaseCheckBox)
                    .add(simulatorOutputCheckBox)
                    .add(checkDatabaseDuplicatesCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        miscOptionsPanelLayout.setVerticalGroup(
            miscOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(miscOptionsPanelLayout.createSequentialGroup()
                .add(addToDatabaseCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(generateGraphCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(simulatorOutputCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(checkDatabaseDuplicatesCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(useFastCPPPluginCheckBox)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout testPanelLayout = new org.jdesktop.layout.GroupLayout(testPanel);
        testPanel.setLayout(testPanelLayout);
        testPanelLayout.setHorizontalGroup(
            testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(testPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(testPanelLayout.createSequentialGroup()
                        .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, testPanelLayout.createSequentialGroup()
                                .add(calculationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(contactPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, simulatorStartConfigurationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(miscOptionsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(outputOptionsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(testPanelLayout.createSequentialGroup()
                        .add(neighborhoodConfigurationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(automaticTestsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(ruleConfigurationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        testPanelLayout.setVerticalGroup(
            testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(testPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(neighborhoodConfigurationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(automaticTestsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ruleConfigurationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(outputOptionsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .add(simulatorStartConfigurationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(miscOptionsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(testPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(calculationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(contactPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .add(34, 34, 34))
        );

        catestPane.addTab("Parameters & Test", testPanel);

        resultsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        resultsScrollPane.setDoubleBuffered(true);

        resultTable.setModel(results);
        int zzz = 1;
        resultsScrollPane.setViewportView(resultTable);

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
        showImageButton.setToolTipText("Display graphs of marked entries if available");
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

        generateSimulationButton.setText("Generate Simulation(s)");
        generateSimulationButton.setToolTipText("Generate simulations of marked entries with the current settings (overwrites old simulation image)");
        generateSimulationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                generateSimulationButtonMouseClicked(evt);
            }
        });

        showSimulationButton.setText("Show Simulation(s)");
        showSimulationButton.setToolTipText("Display simulations of marked entries if available");
        showSimulationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showSimulationButtonMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout resultsPanelLayout = new org.jdesktop.layout.GroupLayout(resultsPanel);
        resultsPanel.setLayout(resultsPanelLayout);
        resultsPanelLayout.setHorizontalGroup(
            resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, resultsPanelLayout.createSequentialGroup()
                .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, resultsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(resultsScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE))
                    .add(resultsPanelLayout.createSequentialGroup()
                        .add(37, 37, 37)
                        .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(eraseAllEntriesButton)
                            .add(eraseMarkedEntriesButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(showImageButton)
                            .add(generateImageButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(generateSimulationButton)
                            .add(showSimulationButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 237, Short.MAX_VALUE)
                        .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, loadResultsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, saveResultsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        resultsPanelLayout.linkSize(new java.awt.Component[] {eraseAllEntriesButton, eraseMarkedEntriesButton, generateImageButton, generateSimulationButton, loadResultsButton, saveResultsButton, showImageButton, showSimulationButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        resultsPanelLayout.setVerticalGroup(
            resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(resultsPanelLayout.createSequentialGroup()
                .add(6, 6, 6)
                .add(resultsScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 428, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(resultsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(resultsPanelLayout.createSequentialGroup()
                            .add(generateSimulationButton)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(showSimulationButton))
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, resultsPanelLayout.createSequentialGroup()
                            .add(generateImageButton)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(showImageButton))
                        .add(resultsPanelLayout.createSequentialGroup()
                            .add(eraseMarkedEntriesButton)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(eraseAllEntriesButton)))
                    .add(resultsPanelLayout.createSequentialGroup()
                        .add(loadResultsButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(saveResultsButton)))
                .addContainerGap())
        );

        catestPane.addTab("Results", resultsPanel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(catestPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(catestPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 547, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Updates function from the text of the wolfram rule field
     */
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

    /**
     * Updates function from the text of the boolean rule field
     */
    private void checkBooleanRuleField() {
        try {
            try {
                ALGORITHM.Function.createSignificantFunction(ALGORITHM.BooleanRule.extractBooleanRuleNumber(booleanRuleField.getText(), ALGORITHM.Function.getSignificantMaxArraySize()));
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(this, "Please check the syntax in the boolean rule field: " + n, "Syntax Error", JOptionPane.ERROR_MESSAGE);
                booleanRuleField.requestFocusInWindow();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Boolean Rule Number is out of range, please increase significant neighborhood size", JOptionPane.ERROR_MESSAGE);
            booleanRuleField.requestFocusInWindow();
        }
    }

    /**
     * Updates function from the text of the polynomial rule field
     */
    private void checkPolynomialRuleField() {
        try {
            try {
                ALGORITHM.Function.createSignificantFunction(ALGORITHM.PolynomialRule.extractPolynomialRuleNumber(polynomialRuleField.getText(), ALGORITHM.Function.getSignificantMaxArraySize(), ALGORITHM.CellStates.getMaxNumberOfCellStates()));
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(this, "Please check the syntax in the polynomial rule field: " + n, "Syntax Error", JOptionPane.ERROR_MESSAGE);
                polynomialRuleField.requestFocusInWindow();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Polynomial Rule Number is out of range, please increase significant neighborhood size", JOptionPane.ERROR_MESSAGE);
            polynomialRuleField.requestFocusInWindow();
        }
    }

    /**
     * Updates wolfram rule field, boolean rule field and the polynomial rule field
     */
    private void updateRuleFields() {
        if (ruleField.isEnabled()) {
            ruleField.setText(ALGORITHM.Function.getSignificantWolframRuleNumber().toString());
        }
        if (ALGORITHM.CellStates.getNumberOfCellStates() != 2) {
            disableBooleanRepresentation();
        } else {
            if (!booleanRuleField.isEnabled() && ruleField.isEnabled()) {
                booleanRuleField.setEnabled(true);
            }
            outputBooleanRepresentationCheckBox.setEnabled(true);
        }

        if (polynomialRuleCheckBox.isSelected() && !polynomialRuleField.isEnabled() && ruleField.isEnabled()) {
            polynomialRuleField.setEnabled(true);
        }

        if (booleanRuleField.isEnabled()) {
            booleanRuleField.setText(ALGORITHM.Function.getSignificantBooleanRule());
        }

        if (polynomialRuleField.isEnabled()) {
            polynomialRuleField.setText(ALGORITHM.Function.getSignificantPolynomialRule());
        }
    }

    /**
     * Disables the boolean rule field and corresponding checkbox
     */
    private void disableBooleanRepresentation() {
        booleanRuleField.setEnabled(false);
        booleanRuleField.setText("only for binary case");
        outputBooleanRepresentationCheckBox.setEnabled(false);
        outputBooleanRepresentationCheckBox.setSelected(false);
    }

    /**
     * Called if the minNeighborhoodSizeField was changed, parses and checks the text and updates corresponding fields
     */
    private void minNeighborhoodSizeFieldChanged() {
        try {
            ALGORITHM.Neighborhood.setMinNeighborhoodSize(Integer.parseInt(minNeighborhoodSizeTextField.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals into the lower limit Neighborhood Size field", "Wrong number format", JOptionPane.ERROR_MESSAGE);
            minNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMinNeighborhoodSize());
            minNeighborhoodSizeTextField.requestFocusInWindow();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error setting lower limit neighborhood size", JOptionPane.ERROR_MESSAGE);
            minNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMinNeighborhoodSize());
            minNeighborhoodSizeTextField.requestFocusInWindow();
            return;
        }
        checkNeighborhoodRuleFields();
        updateNeededRessourcesFields();
        updateRuleFields();
        updateNeighborhoodFields();
    }

    /**
     * Called if the maxNeighborhoodSizeField was changed, parses and checks the text and updates corresponding fields
     */
    private void maxNeighborhoodSizeFieldChanged() {
        try {
            ALGORITHM.Neighborhood.setMaxNeighborhoodSize(Integer.parseInt(maxNeighborhoodSizeTextField.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals into the upper limit Neighborhood Size field", "Wrong number format", JOptionPane.ERROR_MESSAGE);
            maxNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMaxNeighborhoodSize());
            maxNeighborhoodSizeTextField.requestFocusInWindow();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error setting upper limit neighborhood size", JOptionPane.ERROR_MESSAGE);
            maxNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMaxNeighborhoodSize());
            maxNeighborhoodSizeTextField.requestFocusInWindow();
            return;
        }
        checkNeighborhoodRuleFields();
        updateNeededRessourcesFields();
        updateRuleFields();
        updateNeighborhoodFields();
    }

    /**
     * Called if the minSignificantNeighborhoodSizeField was changed, parses and checks the text and updates corresponding fields
     */
    private void minSignificantNeighborhoodSizeFieldChanged() {
        try {
            ALGORITHM.Neighborhood.setMinSignificantNeighborhoodSize(Integer.parseInt(minSignificantNeighborhoodSizeTextField.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals into the lower limit Significant Neighborhood Size field", "Wrong number format", JOptionPane.ERROR_MESSAGE);
            minSignificantNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMinSignificantNeighborhoodSize());
            minSignificantNeighborhoodSizeTextField.requestFocusInWindow();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error setting lower limit significant neighborhood size", JOptionPane.ERROR_MESSAGE);
            minSignificantNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMinSignificantNeighborhoodSize());
            minSignificantNeighborhoodSizeTextField.requestFocusInWindow();
        }

        if (!ALGORITHM.Iteration.isTestAllBalancedFunctions()) {
            maxSignificantNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMinSignificantNeighborhoodSize());
        }

        checkNeighborhoodRuleFields();
        updateNeededRessourcesFields();
        updateRuleFields();
        updateNeighborhoodFields();
    }

    /**
     * Called if the maxSignificantNeighborhoodSizeField was changed, parses and checks the text and updates corresponding fields
     */
    private void maxSignificantNeighborhoodSizeFieldChanged() {
        try {
            ALGORITHM.Neighborhood.setMaxSignificantNeighborhoodSize(Integer.parseInt(maxSignificantNeighborhoodSizeTextField.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter only numerals into the upper limit Significant Neighborhood Size field", "Wrong number format", JOptionPane.ERROR_MESSAGE);
            minSignificantNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMaxSignificantNeighborhoodSize());
            minSignificantNeighborhoodSizeTextField.requestFocusInWindow();
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error setting upper limit significant neighborhood size", JOptionPane.ERROR_MESSAGE);
            minSignificantNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMaxSignificantNeighborhoodSize());
            minSignificantNeighborhoodSizeTextField.requestFocusInWindow();
        }


        checkNeighborhoodRuleFields();
        updateNeededRessourcesFields();
        updateRuleFields();
        updateNeighborhoodFields();
    }

    /**
     * Checks neighborhood rule fields if they should be disabled when multiple neighborhoods should be tested automatically
     */
    private void checkNeighborhoodRuleFields() {
        if (ALGORITHM.Neighborhood.getMinNeighborhoodSize() != ALGORITHM.Neighborhood.getMaxNeighborhoodSize() ||
                ALGORITHM.Neighborhood.getMinSignificantNeighborhoodSize() != ALGORITHM.Neighborhood.getMaxSignificantNeighborhoodSize()) {
            neighborhoodField.setEnabled(false);
            neighborhoodField.setText("multiple neighborhoods");
        } else {
            neighborhoodField.setEnabled(true);
            neighborhoodField.setText(ALGORITHM.Neighborhood.getSignificantNeighborhoodString());
        }
    }

    /**
     * Updates the estimated memory and time requirements for the calculation
     */
    private void updateNeededRessourcesFields() {
        neededMemoryField.setText("" + ALGORITHM.InOutput.getNeededMemorySizeString(ALGORITHM.Iteration.getNeededMemorySize()));
        neededTimeField.setText("" + ALGORITHM.Iteration.getTotalNeededTimeString());
        ALGORITHM.Iteration.updateTotalSteps();
        rulesToTestField.setText("" + ALGORITHM.Iteration.getTotalCalculationSteps().toString());
    }

    /**
     * Parses and checks the cell state field and updates the corresponding fields and the function
     */
    private void checkCellStatesField() {
        try {
            if (ALGORITHM.CellStates.setNumberOfCellStatesRange(numberOfCellStatesField.getText().trim())) {
                // TODO?
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error setting new cell state range", JOptionPane.ERROR_MESSAGE);
            numberOfCellStatesField.setText(ALGORITHM.CellStates.getNumberOfCellStatesString());
            numberOfCellStatesField.requestFocusInWindow();
            return;
        }
        updateNeededRessourcesFields();
    }

    /**
     * Load all data from fields into the core
     * @throws java.lang.NumberFormatException 
     * @throws java.lang.Exception
     */
    private void loadFromFields() throws NumberFormatException, Exception {
        if (neighborhoodField.isEnabled()) {
            ALGORITHM.Neighborhood.setSignificantNeighborhood(ALGORITHM.Neighborhood.parseSignificantNeighborhoodString(neighborhoodField.getText()));
        }
        ALGORITHM.CellStates.setNumberOfCellStatesRange(numberOfCellStatesField.getText());

        ALGORITHM.Neighborhood.setMinMaxNeighborhoodSize(
                Integer.parseInt(minSignificantNeighborhoodSizeTextField.getText()),
                Integer.parseInt(maxSignificantNeighborhoodSizeTextField.getText()),
                Integer.parseInt(minNeighborhoodSizeTextField.getText()),
                Integer.parseInt(maxNeighborhoodSizeTextField.getText()));

        if (ruleField.isEnabled()) {
            ALGORITHM.Function.createSignificantFunction(new BigInteger(ruleField.getText()));
        }
    }

    /**
     * update the min/max neighborhood size fields
     */
    private void updateNeighborhoodFields() {
        minNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMinNeighborhoodSize());
        maxNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMaxNeighborhoodSize());
        minSignificantNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMinSignificantNeighborhoodSize());
        maxSignificantNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMaxSignificantNeighborhoodSize());

        if (ALGORITHM.Neighborhood.getMaxSignificantNeighborhoodSize() == 2 || ALGORITHM.Neighborhood.getMinSignificantNeighborhoodSize() == ALGORITHM.Neighborhood.getMaxNeighborhoodSize()) {
            testAllNeighborhoodVariationsCheckBox.setEnabled(false);
            testAllNeighborhoodVariationsCheckBox.setSelected(false);
        } else {
            testAllNeighborhoodVariationsCheckBox.setEnabled(true);
        }
    }

    /**
     * check and parse the neighborhood field, update the neighborhood after a change of the neighborhood field
     */
    private void neighborhoodFieldChanged() {
        if (!neighborhoodField.isEnabled()) {
            return;
        }

        try {
            ALGORITHM.Neighborhood.setSignificantNeighborhood(ALGORITHM.Neighborhood.parseSignificantNeighborhoodString(neighborhoodField.getText()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error setting neighborhood", JOptionPane.ERROR_MESSAGE);
            neighborhoodField.setText(ALGORITHM.Neighborhood.getSignificantNeighborhoodString());
            neighborhoodField.requestFocusInWindow();
        }

        updateRuleFields();
        updateNeighborhoodFields();
        updateNeededRessourcesFields();
    }

    /**
     * Draw a new simulator image of the 1D Cellular automata
     * @param cell_states number of cell states
     * @param function function to draw
     * @param neighborhood starting neighborhood
     * @return Image as BufferedImage
     * @throws java.lang.Exception if the neighborhood is out of range
     */
    private BufferedImage generateSimulatorBufferedImage(int cell_states, int[] function, int[] neighborhood) throws Exception {
        int calculation_steps = Integer.parseInt(calculationStepsTextField.getText());
        int calculation_width = Integer.parseInt(simulatorConfigurationSizeTextField.getText());
        int zoom_slider_steps = zoomSlider.getValue();
        String start_configuration_text = startConfigurationTextField.getText().trim();

        int[] cellColor_R = new int[cell_states];
        int[] cellColor_G = new int[cell_states];
        int[] cellColor_B = new int[cell_states];
        // black, white, red, green, blue
        switch (cell_states) {
            case 5:
                cellColor_R[4] = 64;
                cellColor_G[4] = 64;
                cellColor_B[4] = 255;
            case 4:
                cellColor_R[3] = 64;
                cellColor_G[3] = 255;
                cellColor_B[3] = 64;
            case 3:
                cellColor_R[2] = 255;
                cellColor_G[2] = 64;
                cellColor_B[2] = 64;
            case 2:
                cellColor_R[1] = 255;
                cellColor_G[1] = 255;
                cellColor_B[1] = 255;
            default:
                cellColor_R[0] = 0;
                cellColor_G[0] = 0;
                cellColor_B[0] = 0;
                break;
        }

        String conf[] = start_configuration_text.split("[, ]+");

        int total_size = calculation_width * calculation_steps;
        int[] data = new int[total_size];
        int[] output = new int[total_size * 3 * zoom_slider_steps * zoom_slider_steps];

        for (int i = 0; i < calculation_width; i++) {
            int value = Integer.parseInt(conf[i]);
            if (value < 0 || value >= cell_states) {
                throw new Exception("Start configuration cell state out of range [" + conf[i] + "]");
            }
            data[i] = value;
        }

        for (int y = 1; y < calculation_steps; y++) {
            int last_index = (y - 1) * calculation_width;
            int index = y * calculation_width;
            for (int x = 0; x < calculation_width; x++) {
                int[] n = new int[neighborhood.length];

                for (int i = 0; i < n.length; i++) {
                    int t = x + neighborhood[i];
                    if (t < 0) {
                        t += calculation_width;
                    } else if (t >= calculation_width) {
                        t -= calculation_width;
                    }
                    n[i] = data[last_index + t];
                }
                data[index + x] = ALGORITHM.Misc.getNeighborhoodFunction(n, function, cell_states);
            }
        }

        for (int y = 0; y < calculation_steps; y++) {
            int index = 3 * y * calculation_width * zoom_slider_steps * zoom_slider_steps;
            int data_index = y * calculation_width;

            for (int x = 0; x < calculation_width; x++) {
                int new_value_R = cellColor_R[data[data_index + x]];
                int new_value_G = cellColor_G[data[data_index + x]];
                int new_value_B = cellColor_B[data[data_index + x]];
                for (int i = 0; i < zoom_slider_steps; i++) {
                    for (int j = 0; j < zoom_slider_steps; j++) {
                        output[3 * zoom_slider_steps * x + 3 * j + i * 3 * zoom_slider_steps * calculation_width + index] = new_value_R;
                        output[3 * zoom_slider_steps * x + 3 * j + 1 + i * 3 * zoom_slider_steps * calculation_width + index] = new_value_G;
                        output[3 * zoom_slider_steps * x + 3 * j + 2 + i * 3 * zoom_slider_steps * calculation_width + index] = new_value_B;
                    }
                }
            }
        }

        BufferedImage image = new BufferedImage(calculation_width * zoom_slider_steps, calculation_steps * zoom_slider_steps, BufferedImage.TYPE_INT_RGB);
        WritableRaster wr = image.getRaster();
        wr.setPixels(0, 0, calculation_width * zoom_slider_steps, calculation_steps * zoom_slider_steps, output);

        return image;
    }

    /**
     * Generate new image
     * @throws java.lang.Exception
     */
    private void updateCurrentSimulatorImage() throws Exception {
        simulationImage = generateSimulatorBufferedImage(ALGORITHM.CellStates.getMaxNumberOfCellStates(), ALGORITHM.Function.getSignificantFunctionArray(), ALGORITHM.Neighborhood.getSignificantNeighborhoodArray());
    }

    /**
     * Draw a new frame of the 1D cellular automata simulator
     */
    private void updateSimulatorFrame() {
        if (simulatorFrame == null) {
            simulatorIRC = new ImageRenderComponent(simulationImage);
            simulatorIRC.setOpaque(true);

            simulatorFrame = new JFrame("Simulator Output");
            simulatorFrame.setAlwaysOnTop(true);
            simulatorFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            simulatorFrame.setLocation(200, 100);
            simulatorFrame.add("Center", new JScrollPane(simulatorIRC));
        } else {
            simulatorIRC.setImage(simulationImage);
            simulatorIRC.revalidate();
            simulatorIRC.repaint();
//            simulatorFrame.repaint();
  //          simulatorFrame.validate();
        }
        simulatorFrame.setSize(531, 551);//calculation_width * zoom_slider_steps, calculation_steps * zoom_slider_steps);
        simulatorFrame.setVisible(true);
    }

    /**
     * deactivate GUI controls until the run is complete or stopped
     */
    private void prepareCalculation() {
        stopCalculationButton.setEnabled(true);
        startCalculationButton.setEnabled(false);

        minNeighborhoodSizeWasActivated = minNeighborhoodSizeTextField.isEnabled();
        minSignificantNeighborhoodSizeWasActivated = minSignificantNeighborhoodSizeTextField.isEnabled();
        maxNeighborhoodSizeWasActivated = minNeighborhoodSizeTextField.isEnabled();
        maxSignificantNeighborhoodSizeWasActivated = minSignificantNeighborhoodSizeTextField.isEnabled();
        neighborhoodWasActivated = neighborhoodField.isEnabled();
        ruleNumberWasActivated = ruleField.isEnabled();
        minNeighborhoodSizeTextField.setEnabled(false);
        minSignificantNeighborhoodSizeTextField.setEnabled(false);
        maxNeighborhoodSizeTextField.setEnabled(false);
        maxSignificantNeighborhoodSizeTextField.setEnabled(false);
        neighborhoodField.setEnabled(false);
        ruleField.setEnabled(false);
        booleanRuleField.setEnabled(false);
        numberOfCellStatesField.setEnabled(false);

        testAllNeighborhoodVariationsCheckBox.setEnabled(false);
        testAllNeighborhoodPermutationsCheckBox.setEnabled(false);
        testAllBalancedRulesCheckBox.setEnabled(false);

        startSimulationButton.setEnabled(false);

        outputAllRadioButton.setEnabled(false);
        outputSurjectiveRadioButton.setEnabled(false);
        outputOnlyInjectiveRadioButton.setEnabled(false);
        addToDatabaseCheckBox.setEnabled(false);
        generateGraphCheckBox.setEnabled(false);
        simulatorOutputCheckBox.setEnabled(false);
        checkDatabaseDuplicatesCheckBox.setEnabled(false);
        useFastCPPPluginCheckBox.setEnabled(false);
        endProgramButton.setEnabled(false);
        saveEndProgramButton.setEnabled(false);
        polynomialRuleCheckBox.setEnabled(false);

        outputBooleanRepresentationCheckBox.setEnabled(false);
        outputPolynomialRepresentationCheckBox.setEnabled(false);

        neededTimeLabel.setText("Remaining calculation time");
        // ... calculation, progress bar, extra thread?

        // TODO: pruefen ob 'generate graph' aktiviert wurde und auf Problemgroesse testen -> groesser als 5 kaum sinnvoll (VIZ kann es nicht) => User fragen.


        progressBar.setMaximum(1000);
        progressBar.setValue(0);

        try {
            ALGORITHM.Neighborhood.initializeMinimalNeighborhoodSizeValue();
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
            if (!(new File("simulations")).exists()) {
                if (!(new File("simulations")).mkdir()) {
                    throw new Exception("Could not create directory 'simulations'");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "INTERNAL ERROR when creating directory", JOptionPane.ERROR_MESSAGE);
            clearUpAfterCalculation();
            return;
        }
        ALGORITHM.Neighborhood.initNeighborhoodPermutations();
        ALGORITHM.Neighborhood.initNeighborhoodVariations();
    }

    /**
     * reactivate GUI controls after the calculation is done or stopped
     */
    public void clearUpAfterCalculation() {
        results.fireTableStructureChanged(); // todo?


        stopCalculationButton.setEnabled(false);
        startCalculationButton.setEnabled(true);
        minNeighborhoodSizeTextField.setEnabled(minNeighborhoodSizeWasActivated);
        minSignificantNeighborhoodSizeTextField.setEnabled(minSignificantNeighborhoodSizeWasActivated);
        maxNeighborhoodSizeTextField.setEnabled(maxNeighborhoodSizeWasActivated);
        maxSignificantNeighborhoodSizeTextField.setEnabled(maxSignificantNeighborhoodSizeWasActivated);
        neighborhoodField.setEnabled(neighborhoodWasActivated);
        ruleField.setEnabled(ruleNumberWasActivated);
        polynomialRuleCheckBox.setEnabled(ruleNumberWasActivated);

        if (ALGORITHM.CellStates.getNumberOfCellStates() != 2) {
            disableBooleanRepresentation();
        } else {
            if (!booleanRuleField.isEnabled()) {
                booleanRuleField.setEnabled(ruleNumberWasActivated);
            }
            outputBooleanRepresentationCheckBox.setEnabled(true);
        }
        numberOfCellStatesField.setEnabled(true);

        testAllNeighborhoodPermutationsCheckBox.setEnabled(true);
        testAllBalancedRulesCheckBox.setEnabled(true);

        if (ALGORITHM.Neighborhood.getMinSignificantNeighborhoodSize() == ALGORITHM.Neighborhood.getMaxNeighborhoodSize()) {
            testAllNeighborhoodVariationsCheckBox.setEnabled(false);
        } else {
            testAllNeighborhoodVariationsCheckBox.setEnabled(true);
        }


        outputAllRadioButton.setEnabled(true);
        outputSurjectiveRadioButton.setEnabled(true);
        outputOnlyInjectiveRadioButton.setEnabled(true);
        if (!useFastCPPPluginCheckBox.isSelected()) {
            generateGraphCheckBox.setEnabled(true);
            simulatorOutputCheckBox.setEnabled(true);
            if ((!generateGraphCheckBox.isSelected()) && (!simulatorOutputCheckBox.isSelected())) {
                useFastCPPPluginCheckBox.setEnabled(true);
            }
        } else {
            useFastCPPPluginCheckBox.setEnabled(true);
        }

        if (testAllNeighborhoodVariationsCheckBox.isSelected() || testAllNeighborhoodPermutationsCheckBox.isSelected() || testAllBalancedRulesCheckBox.isSelected()) {
            startSimulationButton.setEnabled(false);
        } else {
            startSimulationButton.setEnabled(true);
        }

        addToDatabaseCheckBox.setEnabled(true);
        checkDatabaseDuplicatesCheckBox.setEnabled(true);


        outputPolynomialRepresentationCheckBox.setEnabled(true);

        endProgramButton.setEnabled(true);
        saveEndProgramButton.setEnabled(true);

        neededTimeLabel.setText("Needed calculation time");
        updateNeededRessourcesFields();

        progressBar.setValue(1000);
    }

    /**
     * load data from a BufferedReader into the results database
     * @param p A BufferedReader device, e.g. a file
     * @throws java.lang.NumberFormatException Error parsing an integer field
     * @throws java.io.IOException Error reading from BufferedReader
     */
    private void loadResultsIntoDatabase(BufferedReader p) throws NumberFormatException, IOException {
        results.datas.clear();
        String t;
        while ((t = p.readLine()) != null) {
            Object[] object_row = ALGORITHM.InOutput.parseParametersAsString(t);
            object_row[ResultsTable.GRAPH_INDEX] = new Boolean((new File(ALGORITHM.InOutput.getImageFileName(object_row) + ".viz")).exists());
            object_row[ResultsTable.IMAGE_INDEX] = new Boolean((new File(ALGORITHM.InOutput.getImageFileName(object_row) + ".viz.png")).exists());
            object_row[ResultsTable.SIMULATOR_INDEX] = new Boolean((new File(ALGORITHM.InOutput.getSimulationFileName(object_row))).exists());

            results.datas.add(object_row);
            results.fireTableDataChanged();
        }
    }

    /**
     * Exit the Application
     * @param evt unused
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        exitProgram();
    }//GEN-LAST:event_exitForm

    /**
     * Exit procedure
     */
    private void exitProgram() {
        setVisible(false);
        dispose();
        System.exit(0);
    }

    /**
     * Generate simulator images from the results that were selected in the result database
     * @param evt unused
     */
    private void generateImageButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generateImageButtonMouseClicked
        int[] list = resultTable.getSelectedRows();
        for (int i = 0; i < list.length; i++) {
            list[i] = sorter.modelIndex(list[i]);
        }

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
        //String calling_string = System.getProperty("user.dir") + "\\dot\\dot -Tpng -o";
        String calling_string = "dot -Tpng -o";
        Runtime rt = Runtime.getRuntime();
        try {
            for (int i = 0; i < graph_list.size(); i++) {
                String file_name = ALGORITHM.InOutput.getImageFileName(results.getRow(graph_list.get(i))) + ".viz";
                if (((new File(file_name)).length() / 1024) > 64) {
                    if (JOptionPane.showConfirmDialog(null, "The graph file (" + file_name + ") is larger than 64kb (" + ((new File(file_name)).length() / 1024) + "kb). 'DOT' might need a long time to generate the image.\n\nContinue?", "Large graph warning", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == 1) {
                        break;
                    }
                }
                System.out.println("CALL " + calling_string + file_name + ".png " + file_name);
                Process p = rt.exec(calling_string + file_name + ".png " + file_name);
                InputStream in = p.getInputStream();
                OutputStream out = p.getOutputStream();
                InputStream err = p.getErrorStream();
                int x;

                System.out.print("InputStream in: ");
                while ((x = in.read()) != -1) {
                    System.out.print(Character.valueOf((char) x));
                }
                System.out.println();
                System.out.print("InputStream err: ");
                while ((x = err.read()) != -1) {
                    System.out.print(Character.valueOf((char) x));
                }
                System.out.println();

                int return_value = p.waitFor();
                System.out.println("Return value: " + return_value);
                p.destroy();
            }
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(this, "Error when calling the 'dot' program. Ensure that the tool is in the directory " + System.getProperty("user.dir") + "\\dot", "Error generating graphs", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < graph_list.size(); i++) {
            String file_name = ALGORITHM.InOutput.getImageFileName(results.getRow(graph_list.get(i))) + ".viz.png";
            if ((new File(file_name)).exists()) {
                results.setHasImage(graph_list.get(i));
            }
            results.fireTableRowsUpdated(graph_list.get(i), graph_list.get(i));
        }
        if (graph_list.size() == 1) {
// TODO select single row
        }
    }//GEN-LAST:event_generateImageButtonMouseClicked

    /**
     * Clears all results from the database
     * @param evt unused
     */
    private void eraseAllEntriesButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eraseAllEntriesButtonMouseClicked
        results.datas.clear();
        results.fireTableDataChanged();
    }//GEN-LAST:event_eraseAllEntriesButtonMouseClicked

    /**
     * Displays the image corresponding to the selected rows of the results database
     * @param evt unused
     */
    private void showImageButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showImageButtonMouseClicked
        int[] list = resultTable.getSelectedRows();
        for (int i = 0; i < list.length; i++) {
            list[i] = sorter.modelIndex(list[i]);
        }

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
            String file_name = ALGORITHM.InOutput.getImageFileName(results.getRow(graph_list.get(i))) + ".viz.png";
            try {
                BufferedImage image = ImageIO.read(new File(file_name));
                ImageRenderComponent irc = new ImageRenderComponent(image);
                irc.setOpaque(true);  // for use as a content pane

                JFrame f = new JFrame("DeBruijn graph output '" + file_name + "'");

                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                //f.setContentPane(irc);
                f.add("Center", new JScrollPane(irc));
                f.setSize(531, 551);
                f.setLocation(200, 100);
                f.setVisible(true);

            } catch (IOException e) {
                // TODO
                JOptionPane.showMessageDialog(this, "Error opening file " + file_name, "I/O error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
        }
    }//GEN-LAST:event_showImageButtonMouseClicked

    /**
     * load results into the database from a file
     * @param evt unused
     */
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

    /**
     * save results from the database into a file
     * @param evt unused
     */
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
                            "\"" + (String) t[2] + "\", " +
                            "\"" + (String) t[3] + "\", " +
                            "" + (java.lang.Integer) t[4] + ", " +
                            "" + (java.lang.Integer) t[5] + ", " +
                            "" + (java.lang.Integer) t[6] + ", " +
                            "" + (java.lang.Boolean) t[7] + ", " +
                            "" + (java.lang.Boolean) t[8]);
                }
                p.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error writing to file " + my_file.getAbsoluteFile() + " : " + e, "Error writing file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_saveResultsButtonMouseClicked

    /**
     * erase all selected entries of the result database
     * @param evt unused
     */
    private void eraseMarkedEntriesButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eraseMarkedEntriesButtonMouseClicked
        int[] list = resultTable.getSelectedRows();
        for (int i = 0; i < list.length; i++) {
            list[i] = sorter.modelIndex(list[i]);
        }

        for (int i = 0; i < list.length; i++) {
            results.datas.remove(list[i] - i);
        }
        results.fireTableDataChanged();
    }//GEN-LAST:event_eraseMarkedEntriesButtonMouseClicked

    /**
     * Number of calculation steps was changed, update the output
     * @param evt unused
     */
    private void calculationStepsTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculationStepsTextFieldActionPerformed
        update_simulator_output();
    }//GEN-LAST:event_calculationStepsTextFieldActionPerformed

    /**
     * Zoom factor of the output was changed, update the output
     * @param evt unused
     */
    private void zoomSliderCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_zoomSliderCaretPositionChanged
        update_simulator_output();
    }//GEN-LAST:event_zoomSliderCaretPositionChanged

    /**
     * Generate a new random start configuration for the simulator
     * @param evt unused
     */
    private void generateNewSimulatorConfigurationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generateNewSimulatorConfigurationButtonMouseClicked
        generateNewSimulatorConfiguration();
    }//GEN-LAST:event_generateNewSimulatorConfigurationButtonMouseClicked

    /**
     * Size of start configuration was changed, generate a new random start configuration for the simulator
     * @param evt unused
     */
    private void simulatorConfigurationSizeTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_simulatorConfigurationSizeTextFieldFocusLost
        // TODO Prüfung
        generateNewSimulatorConfiguration();
    }//GEN-LAST:event_simulatorConfigurationSizeTextFieldFocusLost

    /**
     * Size of start configuration was changed, generate a new random start configuration for the simulator
     * @param evt unused
     */
    private void simulatorConfigurationSizeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulatorConfigurationSizeTextFieldActionPerformed
        generateNewSimulatorConfiguration();
    }//GEN-LAST:event_simulatorConfigurationSizeTextFieldActionPerformed

    /**
     * Deactivate CPP Plugin if simulator generator is activated (TODO)
     * @param evt unused
     */
    private void simulatorOutputCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_simulatorOutputCheckBoxItemStateChanged
        if (simulatorOutputCheckBox.isSelected()) {
            useFastCPPPluginCheckBox.setSelected(false);
            useFastCPPPluginCheckBox.setEnabled(false);
        } else {
            if (!generateGraphCheckBox.isSelected()) {
                useFastCPPPluginCheckBox.setEnabled(true);
            }
        }
}//GEN-LAST:event_simulatorOutputCheckBoxItemStateChanged

    /**
     * Deactivate graph and simulator output when CPP plugin is selected
     * @param evt unused
     */
    /**
     * Exit program
     * @param evt unused
     */
    private void endProgramButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_endProgramButtonMouseClicked
        exitProgram();
    }//GEN-LAST:event_endProgramButtonMouseClicked

    /** 
     * Save GUI control settings and exit the program
     * @param evt unused
     */
    private void saveEndProgramButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveEndProgramButtonMouseClicked
        // save database, save rule, neigbhorhood etc.
        // reload in main!
        saveSettings("temp_results");
        exitProgram();
    }//GEN-LAST:event_saveEndProgramButtonMouseClicked

    /**
     * Save GUI control settings into a file
     * @param file_name file name
     */
    private void saveSettings(String file_name) {
        File my_file = new File(file_name);
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


            p.println(testAllNeighborhoodVariationsCheckBox.isSelected());
            p.println(testAllNeighborhoodPermutationsCheckBox.isSelected());
            p.println(testAllBalancedRulesCheckBox.isSelected());

            p.println(neighborhoodField.getText());

            p.println(minSignificantNeighborhoodSizeTextField.getText());
            p.println(maxSignificantNeighborhoodSizeTextField.getText());
            p.println(minNeighborhoodSizeTextField.getText());
            p.println(maxNeighborhoodSizeTextField.getText());

            p.println(numberOfCellStatesField.getText());
            p.println(ruleField.getText());

            p.println(outputAllRadioButton.isSelected());
            p.println(outputSurjectiveRadioButton.isSelected());
            p.println(outputOnlyInjectiveRadioButton.isSelected());

            p.println(addToDatabaseCheckBox.isSelected());
            p.println(simulatorOutputCheckBox.isSelected());
            p.println(generateGraphCheckBox.isSelected());

            p.println(checkDatabaseDuplicatesCheckBox.isSelected());
            p.println(useFastCPPPluginCheckBox.isSelected());
            p.println(polynomialRuleCheckBox.isSelected());

            p.println(calculationStepsTextField.getText());
            p.println(simulatorConfigurationSizeTextField.getText());
            p.println(startConfigurationTextField.getText());


// database
            for (int i = 0; i < results.datas.size(); i++) {
                Object[] t = (Object[]) results.datas.get(i);
                p.println(
                        "" + (java.math.BigInteger) t[0] + ", " +
                        "\"" + (String) t[1] + "\", " +
                        "\"" + (String) t[2] + "\", " +
                        "\"" + (String) t[3] + "\", " +
                        "" + (java.lang.Integer) t[4] + ", " +
                        "" + (java.lang.Integer) t[5] + ", " +
                        "" + (java.lang.Integer) t[6] + ", " +
                        "" + (java.lang.Boolean) t[7] + ", " +
                        "" + (java.lang.Boolean) t[8]);
            }
            p.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error writing to file " + my_file.getAbsoluteFile() + " : " + e, "Error writing file", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Activate and update the polynomial rule field if the corresponding check box was activated
     * @param evt unused
     */
    private void polynomialRuleCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_polynomialRuleCheckBoxActionPerformed
        if (polynomialRuleCheckBox.isSelected()) {
            polynomialRuleField.setEnabled(true);
            updateRuleFields();
        } else {
            polynomialRuleField.setEnabled(false);
            polynomialRuleField.setText("deactivated");
        }
    }//GEN-LAST:event_polynomialRuleCheckBoxActionPerformed

    /**
     * Polynomial rule field was changed, call update procedure
     * @param evt unused
     */
    private void polynomialRuleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_polynomialRuleFieldFocusLost
        polynomialRuleFieldChanged();
    }//GEN-LAST:event_polynomialRuleFieldFocusLost

    /**
     * Polynomial rule field was changed, call update procedure
     * @param evt unused
     */
    private void polynomialRuleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_polynomialRuleFieldActionPerformed
        polynomialRuleFieldChanged();
    }//GEN-LAST:event_polynomialRuleFieldActionPerformed

    /**
     * Boolean rule field was changed, call update procedure
     * @param evt unused
     */
    private void booleanRuleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_booleanRuleFieldFocusLost
        booleanRuleFieldChanged();
    }//GEN-LAST:event_booleanRuleFieldFocusLost

    /**
     * Boolean rule field was changed, call update procedure
     * @param evt unused
     */
    private void booleanRuleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_booleanRuleFieldActionPerformed
        booleanRuleFieldChanged();
    }//GEN-LAST:event_booleanRuleFieldActionPerformed

    /**
     * Wolfram rule field was changed, call update procedure
     * @param evt unused
     */
    private void ruleFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ruleFieldFocusLost
        // TODO: nicht erlauben falls mehrere cell states oder mehrere significante neighborhoods eingestellt sind
        checkRuleField();

        updateRuleFields();
    }//GEN-LAST:event_ruleFieldFocusLost

    /**
     * Wolfram rule field was changed, call update procedure
     * @param evt unused
     */
    private void ruleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ruleFieldActionPerformed
        checkRuleField();

        updateRuleFields();
    }//GEN-LAST:event_ruleFieldActionPerformed

    /**
     * Prepare new calculation, initialize iterations and automatic tests, start a new calculation thread
     * @param evt unused
     */
    private void startCalculationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startCalculationButtonMouseClicked
        prepareCalculation();
        ALGORITHM.Iteration.initialize();

        // start seperate calculation thread
        if ((calculation_thread != null) && (calculation_thread.isAlive())) {
            calculation_thread.interrupt();
        }
        while ((calculation_thread != null) && (calculation_thread.isAlive())) {
        }
        calculation_thread = new CalculationThread();
        calculation_thread.init_thread(this, progressBar, neededTimeField, generateGraphCheckBox.isSelected(), useFastCPPPluginCheckBox.isSelected(), outputAllRadioButton.isSelected(), outputSurjectiveRadioButton.isSelected(), outputOnlyInjectiveRadioButton.isSelected(), addToDatabaseCheckBox.isSelected(), checkDatabaseDuplicatesCheckBox.isSelected(), outputBooleanRepresentationCheckBox.isSelected(), outputPolynomialRepresentationCheckBox.isSelected());

        calculation_thread.start();
    }//GEN-LAST:event_startCalculationButtonMouseClicked

    /**
     * Interrupt the ongoing calculation
     * @param evt unused
     */
    private void stopCalculationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopCalculationButtonMouseClicked
        calculation_thread.interrupt();
    }//GEN-LAST:event_stopCalculationButtonMouseClicked

    /**
     * Number of cell states have been changed, update and check corresponding fields
     * @param evt unused
     */
    private void numberOfCellStatesFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numberOfCellStatesFieldFocusLost
        checkCellStatesField();

        updateRuleFields();
    }//GEN-LAST:event_numberOfCellStatesFieldFocusLost

    /**
     * Number of cell states have been changed, update and check corresponding fields
     * @param evt unused
     */
    private void numberOfCellStatesFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberOfCellStatesFieldActionPerformed
        checkCellStatesField();

        updateRuleFields();
    }//GEN-LAST:event_numberOfCellStatesFieldActionPerformed

    /**
     * Neighborhood field was changed, update and check corresponding fields
     * @param evt unused
     */
    private void neighborhoodFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_neighborhoodFieldFocusLost
        neighborhoodFieldChanged();
    }//GEN-LAST:event_neighborhoodFieldFocusLost

    /**
     * Neighborhood field was changed, update and check corresponding fields
     * @param evt unused
     */
    private void neighborhoodFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_neighborhoodFieldActionPerformed
        neighborhoodFieldChanged();
    }//GEN-LAST:event_neighborhoodFieldActionPerformed

    /**
     * Minimal significant neighborhood size field was changed, update and check corresponding fields
     * @param evt unused
     */
    private void minSignificantNeighborhoodSizeTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_minSignificantNeighborhoodSizeTextFieldFocusLost
        minSignificantNeighborhoodSizeFieldChanged();
}//GEN-LAST:event_minSignificantNeighborhoodSizeTextFieldFocusLost

    /**
     * Minimal significant neighborhood size field was changed, update and check corresponding fields
     * @param evt unused
     */
    private void minSignificantNeighborhoodSizeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minSignificantNeighborhoodSizeTextFieldActionPerformed
        minSignificantNeighborhoodSizeFieldChanged();
}//GEN-LAST:event_minSignificantNeighborhoodSizeTextFieldActionPerformed

    /**
     * Minimal significant neighborhood size field was changed, update and check corresponding fields
     * @param evt unused
     */
    private void minNeighborhoodSizeTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_minNeighborhoodSizeTextFieldFocusLost
        minNeighborhoodSizeFieldChanged();
}//GEN-LAST:event_minNeighborhoodSizeTextFieldFocusLost

    /**
     * Minimal significant neighborhood size field was changed, update and check corresponding fields
     * @param evt unused
     */
    private void minNeighborhoodSizeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minNeighborhoodSizeTextFieldActionPerformed
        minNeighborhoodSizeFieldChanged();
}//GEN-LAST:event_minNeighborhoodSizeTextFieldActionPerformed

    /**
     * Maximal significant neighborhood size field was changed, update and check corresponding fields
     * @param evt unused
     */
    private void maxSignificantNeighborhoodSizeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxSignificantNeighborhoodSizeTextFieldActionPerformed
        maxSignificantNeighborhoodSizeFieldChanged();
}//GEN-LAST:event_maxSignificantNeighborhoodSizeTextFieldActionPerformed

    /**
     * Minimal significant neighborhood size field was changed, update and check corresponding fields
     * @param evt unused
     */
    private void maxSignificantNeighborhoodSizeTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_maxSignificantNeighborhoodSizeTextFieldFocusLost
        maxSignificantNeighborhoodSizeFieldChanged();
}//GEN-LAST:event_maxSignificantNeighborhoodSizeTextFieldFocusLost

    /**
     * Maximal neighborhood size field was changed, update and check corresponding fields
     * @param evt unused
     */
    private void maxNeighborhoodSizeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxNeighborhoodSizeTextFieldActionPerformed
        maxNeighborhoodSizeFieldChanged();
}//GEN-LAST:event_maxNeighborhoodSizeTextFieldActionPerformed

    /**
     * Maximal neighborhood size field was changed, update and check corresponding fields
     * @param evt unused
     */
    private void maxNeighborhoodSizeTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_maxNeighborhoodSizeTextFieldFocusLost
        maxNeighborhoodSizeFieldChanged();
}//GEN-LAST:event_maxNeighborhoodSizeTextFieldFocusLost

    /**
     * Activating the automatic test of all balanced rules allows the test of multiple neighborhoods
     * and deactivates the rule fields and updates the required time
     * @param evt unused
     */
    private void testAllBalancedRulesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testAllBalancedRulesCheckBoxItemStateChanged
        if (testAllNeighborhoodVariationsCheckBox.isSelected() || testAllNeighborhoodPermutationsCheckBox.isSelected() || testAllBalancedRulesCheckBox.isSelected()) {
            startSimulationButton.setEnabled(false);
        } else {
            startSimulationButton.setEnabled(true);
        }

        if (testAllBalancedRulesCheckBox.isSelected()) {
            minSignificantNeighborhoodSizeTextField.setEnabled(true);
            maxSignificantNeighborhoodSizeTextField.setEnabled(true);
            ruleField.setEnabled(false);
            ruleField.setText("all balanced rules");
            if (ALGORITHM.CellStates.getNumberOfCellStates() != 2) {
                booleanRuleField.setText("only for binary case");
            } else {
                booleanRuleField.setText("all");
            }
            booleanRuleField.setEnabled(false);
            polynomialRuleField.setText("all");
            polynomialRuleField.setEnabled(false);
            polynomialRuleCheckBox.setSelected(false);
            polynomialRuleCheckBox.setEnabled(false);
        } else {
            minSignificantNeighborhoodSizeTextField.setEnabled(false);
            maxSignificantNeighborhoodSizeTextField.setEnabled(false);
            try {
                ALGORITHM.Neighborhood.setMaxSignificantNeighborhoodSize(ALGORITHM.Neighborhood.getMinSignificantNeighborhoodSize());
            } catch (Exception e) {
            }
        
            maxSignificantNeighborhoodSizeTextField.setText("" + ALGORITHM.Neighborhood.getMaxSignificantNeighborhoodSize());
            ruleField.setText(ALGORITHM.Function.getSignificantWolframRuleNumber().toString());
            ruleField.setEnabled(true);
            if (polynomialRuleCheckBox.isSelected()) {
                polynomialRuleField.setEnabled(true);
            }
            polynomialRuleCheckBox.setEnabled(true);

            if (ALGORITHM.CellStates.getNumberOfCellStates() != 2) {
                booleanRuleField.setEnabled(false);
                booleanRuleField.setText("only for binary case");
            } else if (!booleanRuleField.isEnabled() && ruleField.isEnabled()) {
                booleanRuleField.setEnabled(true);
            }
            updateRuleFields();
        }
        ALGORITHM.Iteration.setTestAllBalancedFunctions(testAllBalancedRulesCheckBox.isSelected());
        updateNeededRessourcesFields();
    }//GEN-LAST:event_testAllBalancedRulesCheckBoxItemStateChanged

    /**
     * Generate simulation images from selected entries of the results database
     * @param evt unused
     */
    private void generateSimulationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generateSimulationButtonMouseClicked
        int[] list = resultTable.getSelectedRows();
        for (int i = 0; i < list.length; i++) {
            list[i] = sorter.modelIndex(list[i]);
        }

        Vector<Integer> graph_list = new Vector<Integer>();

        for (int i = 0; i < list.length; i++) {
            //   if (!results.hasSimulation(list[i])) {
            graph_list.add(new Integer(list[i]));
        //    } allow overwriting old
        }

        if (graph_list.size() == 0) {
            JOptionPane.showMessageDialog(this, "Selected entries already have a simulation image.", "Entries already have Simulation", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (graph_list.size() >= 16) {
            if (JOptionPane.showConfirmDialog(this, "" + graph_list.size() + " entries were selected for simulation image creation.\nContinue?",
                    "Confirm generating multiple simulations",
                    JOptionPane.YES_NO_OPTION) == 1) {
                return;
            }
        }

        String file_name = "";
        try {
            for (int i = 0; i < graph_list.size(); i++) {
                Object[] row = results.getRow(graph_list.get(i));
                file_name = ALGORITHM.InOutput.getSimulationFileName(row);

                int cell_states = (Integer) row[6];
                int neighborhood_size = (Integer) row[4];
                int significant_neighborhood_size = (Integer) row[5];
                int[] function = ALGORITHM.Function.calculateFunction((BigInteger) row[0], ALGORITHM.Function.calculateMaxArraySize(cell_states, significant_neighborhood_size), ALGORITHM.Function.calculateMaxArraySize(cell_states, neighborhood_size), cell_states);
                int[] neighborhood = ALGORITHM.Neighborhood.parseSignificantNeighborhoodString((String) row[3]);
                BufferedImage image = generateSimulatorBufferedImage(cell_states, function, neighborhood);
                ImageIO.write(image, "png", new File(file_name));
            }
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(this, "Error (" + exc + ") when creating image (" + file_name + ") in current directory (" + System.getProperty("user.dir") + ").", "Error generating simulation image", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < graph_list.size(); i++) {
            file_name = ALGORITHM.InOutput.getSimulationFileName(results.getRow(graph_list.get(i)));
            if ((new File(file_name)).exists()) {
                results.setHasSimulation(graph_list.get(i));
            }
            results.fireTableRowsUpdated(graph_list.get(i), graph_list.get(i));
        }
        if (graph_list.size() == 1) {
// TODO select single row
        }
}//GEN-LAST:event_generateSimulationButtonMouseClicked

    /**
     * Displays the corresponding images in new frames of the selected entries of the result database
     * @param evt unused
     */
    private void showSimulationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showSimulationButtonMouseClicked
        int[] list = resultTable.getSelectedRows();
        for (int i = 0; i < list.length; i++) {
            list[i] = sorter.modelIndex(list[i]);
        }

        Vector<Integer> graph_list = new Vector<Integer>();
        for (int i = 0; i < list.length; i++) {
            if (results.hasSimulation(list[i])) {
                graph_list.add(new Integer(list[i]));
            }
        }

        if (graph_list.size() == 0) {
            JOptionPane.showMessageDialog(this, "No entries with simulations selected", "No entry selected", JOptionPane.ERROR_MESSAGE);
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
            String file_name = ALGORITHM.InOutput.getSimulationFileName(results.getRow(graph_list.get(i)));
            try {
                BufferedImage image = ImageIO.read(new File(file_name));
                ImageRenderComponent irc = new ImageRenderComponent(image);
                irc.setOpaque(true);  // for use as a content pane

                JFrame f = new JFrame("Simulator output '" + file_name + "'");

                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.add("Center", new JScrollPane(irc));
                f.setSize(531, 551);
                f.setLocation(200, 100);
                f.setVisible(true);

            } catch (IOException e) {

                JOptionPane.showMessageDialog(this, "Error opening file " + file_name, "I/O error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
        }    
}//GEN-LAST:event_showSimulationButtonMouseClicked

    /**
     * deactivates the CPP plugin if graphs should be generated
     * @param evt unused
     */
    private void generateGraphCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_generateGraphCheckBoxItemStateChanged
        if (generateGraphCheckBox.isSelected()) {
            useFastCPPPluginCheckBox.setSelected(false);
            useFastCPPPluginCheckBox.setEnabled(false);
        } else {
            if (!simulatorOutputCheckBox.isSelected()) {
                useFastCPPPluginCheckBox.setEnabled(true);
            }
        }
    }//GEN-LAST:event_generateGraphCheckBoxItemStateChanged

    /**
     * Disable the simulation button if multiple neighborhood variations should be tested
     * @param evt unused
     */
    private void testAllNeighborhoodVariationsCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testAllNeighborhoodVariationsCheckBoxItemStateChanged
        ALGORITHM.Iteration.setTestAllNeighborhoodVariations(testAllNeighborhoodVariationsCheckBox.isSelected());
        if (testAllNeighborhoodVariationsCheckBox.isSelected() || testAllNeighborhoodPermutationsCheckBox.isSelected() || testAllBalancedRulesCheckBox.isSelected()) {
            startSimulationButton.setEnabled(false);
        } else {
            startSimulationButton.setEnabled(true);
        }
        updateNeededRessourcesFields();
    }//GEN-LAST:event_testAllNeighborhoodVariationsCheckBoxItemStateChanged

    /**
     * Disable the simulation button if multiple neighborhood permutation should be tested
     * @param evt unused
     */
    private void testAllNeighborhoodPermutationsCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testAllNeighborhoodPermutationsCheckBoxItemStateChanged
        ALGORITHM.Iteration.setTestAllNeighborhoodPermutations(testAllNeighborhoodPermutationsCheckBox.isSelected());
        if (testAllNeighborhoodVariationsCheckBox.isSelected() || testAllNeighborhoodPermutationsCheckBox.isSelected() || testAllBalancedRulesCheckBox.isSelected()) {
            startSimulationButton.setEnabled(false);
        } else {
            startSimulationButton.setEnabled(true);
        }
        updateNeededRessourcesFields();
    }//GEN-LAST:event_testAllNeighborhoodPermutationsCheckBoxItemStateChanged

    /**
     * Update the simulator output
     * @param evt unused
     */
    private void startSimulationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startSimulationButtonMouseClicked
        update_simulator_output();
    }//GEN-LAST:event_startSimulationButtonMouseClicked

    /**
     * The calculation steps have changed, update the simulator output
     * @param evt unused
     */
    private void calculationStepsTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_calculationStepsTextFieldFocusLost
        update_simulator_output();
    }//GEN-LAST:event_calculationStepsTextFieldFocusLost

    /**
     * Save current simulator start configuration to a file
     * @param evt unused
     */
    private void saveSimulatorConfigurationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveSimulatorConfigurationButtonMouseClicked
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
                p.println(simulatorConfigurationSizeTextField.getText());
                p.println(startConfigurationTextField.getText());
                p.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error writing to file " + my_file.getAbsoluteFile() + " : " + e, "Error writing file", JOptionPane.ERROR_MESSAGE);
            }
        }        
    }//GEN-LAST:event_saveSimulatorConfigurationButtonMouseClicked

    /**
     * Load simulator start configuration from a file into the start configuration field
     * @param evt unused
     */
    private void loadSimulatorConfigurationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loadSimulatorConfigurationButtonMouseClicked
        if (fileChooser.showOpenDialog(this) == fileChooser.APPROVE_OPTION) {
            File my_file = fileChooser.getSelectedFile();
            if (!my_file.exists()) {
                JOptionPane.showMessageDialog(this, "Error opening file " + my_file.getAbsoluteFile(), "File not found", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                BufferedReader p = new BufferedReader(new FileReader(my_file.getAbsoluteFile()));
                simulatorConfigurationSizeTextField.setText(p.readLine());
                startConfigurationTextField.setText(p.readLine());
                p.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading from file " + my_file.getAbsoluteFile(), "Error reading file", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error reading from file " + my_file.getAbsoluteFile(), "Error reading file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_loadSimulatorConfigurationButtonMouseClicked

    private void zoomSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_zoomSliderStateChanged
        update_simulator_output();
    }//GEN-LAST:event_zoomSliderStateChanged

    private void useFastCPPPluginCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_useFastCPPPluginCheckBoxItemStateChanged
        if (useFastCPPPluginCheckBox.isSelected()) {
            generateGraphCheckBox.setSelected(false);
            generateGraphCheckBox.setEnabled(false);
            simulatorOutputCheckBox.setSelected(false);
            simulatorOutputCheckBox.setEnabled(false);
        } else {
            generateGraphCheckBox.setEnabled(true);
            simulatorOutputCheckBox.setEnabled(true);
        }
    }//GEN-LAST:event_useFastCPPPluginCheckBoxItemStateChanged

    /**
     * The boolean rule field has changed, parse and check its contents and update the corresponding rule fields 
     */
    private void booleanRuleFieldChanged() {
        if (ALGORITHM.CellStates.getNumberOfCellStates() != 2) {
            return;
        }
        checkBooleanRuleField();
        updateRuleFields();
    }

    /**
     * The polynomial rule field has changed, parse and check its contents and update the corresponding rule fields
     */
    private void polynomialRuleFieldChanged() {
        if (!polynomialRuleCheckBox.isSelected()) {
            return;
        }
        checkPolynomialRuleField();
        updateRuleFields();
    }

    /**
     * Generate new simulator image and update the simulator frame with the new image
     */
    private void update_simulator_output() {
        if (testAllNeighborhoodVariationsCheckBox.isSelected() || testAllNeighborhoodPermutationsCheckBox.isSelected() || testAllBalancedRulesCheckBox.isSelected()) {
            return;
        }

        try {
            updateCurrentSimulatorImage();
            updateSimulatorFrame();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating simulator output : " + e, "Error Simulator output", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Generate a new random start configuration for the 1D Simulator and updates the output
     */
    private void generateNewSimulatorConfiguration() {
        int size = Integer.parseInt(simulatorConfigurationSizeTextField.getText());
        Random generator = new Random();
        int cell_states = ALGORITHM.CellStates.getNumberOfCellStates();

        int[] output = new int[size];
        for (int i = 0; i < size; i++) {
            int r = generator.nextInt(cell_states);
            output[i] = r;
        }
        String start_configuration = new String("");
        for (int i = 0; i < size; i++) {
            start_configuration += output[i];
            if (i < size - 1) {
                start_configuration += ",";
            }
        }
        startConfigurationTextField.setText(start_configuration);
        update_simulator_output();
    }

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
    private javax.swing.JPanel automaticTestsPanel;
    private javax.swing.ButtonGroup automationNeighborhoodButtonGroup;
    private javax.swing.ButtonGroup automationRuleButtonGroup;
    private javax.swing.JLabel booleanRepresentationLabel;
    private javax.swing.JTextField booleanRuleField;
    private javax.swing.JPanel calculationPanel;
    private javax.swing.JLabel calculationStepsLabel;
    private javax.swing.JTextField calculationStepsTextField;
    private javax.swing.JTabbedPane catestPane;
    private javax.swing.JCheckBox checkDatabaseDuplicatesCheckBox;
    private javax.swing.ButtonGroup chooseOutputInjectiveSurjectiveButtonGroup;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JLabel contactLabel1;
    private javax.swing.JPanel contactPanel;
    private javax.swing.JLabel dots1Label;
    private javax.swing.JLabel dots2Label;
    private javax.swing.JButton endProgramButton;
    private javax.swing.JButton eraseAllEntriesButton;
    private javax.swing.JButton eraseMarkedEntriesButton;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JCheckBox generateGraphCheckBox;
    private javax.swing.JButton generateImageButton;
    private javax.swing.JButton generateNewSimulatorConfigurationButton;
    private javax.swing.JButton generateSimulationButton;
    private javax.swing.JButton loadResultsButton;
    private javax.swing.JButton loadSimulatorConfigurationButton;
    private javax.swing.JTextField maxNeighborhoodSizeTextField;
    private javax.swing.JTextField maxSignificantNeighborhoodSizeTextField;
    private javax.swing.JTextField minNeighborhoodSizeTextField;
    private javax.swing.JTextField minSignificantNeighborhoodSizeTextField;
    private javax.swing.JPanel miscOptionsPanel;
    private javax.swing.JLabel neededMemoryField;
    private javax.swing.JLabel neededMemoryLabel;
    private javax.swing.JLabel neededTimeField;
    private javax.swing.JLabel neededTimeLabel;
    private javax.swing.JPanel neighborhoodConfigurationPanel;
    private javax.swing.JTextField neighborhoodField;
    private javax.swing.JLabel neighborhoodLabel;
    private javax.swing.JTextField numberOfCellStatesField;
    private javax.swing.JLabel numberOfCellStatesLabel;
    private javax.swing.JRadioButton outputAllRadioButton;
    private javax.swing.JCheckBox outputBooleanRepresentationCheckBox;
    private javax.swing.JRadioButton outputOnlyInjectiveRadioButton;
    private javax.swing.JPanel outputOptionsPanel;
    private javax.swing.JCheckBox outputPolynomialRepresentationCheckBox;
    private javax.swing.JRadioButton outputSurjectiveRadioButton;
    private javax.swing.JCheckBox polynomialRuleCheckBox;
    private javax.swing.JTextField polynomialRuleField;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressBarLabel;
    private javax.swing.JTable resultTable;
    private javax.swing.JPanel resultsPanel;
    private javax.swing.JScrollPane resultsScrollPane;
    private javax.swing.JPanel ruleConfigurationPanel;
    private javax.swing.JTextField ruleField;
    private javax.swing.JLabel ruleNumberLabel;
    private javax.swing.JLabel rulesToTestField;
    private javax.swing.JLabel rulesToTestLabel;
    private javax.swing.JButton saveEndProgramButton;
    private javax.swing.JButton saveResultsButton;
    private javax.swing.JButton saveSimulatorConfigurationButton;
    private javax.swing.JButton showImageButton;
    private javax.swing.JButton showSimulationButton;
    private javax.swing.JLabel significantNeighborhoodSizeLabel;
    private javax.swing.JLabel simulatorConfigurationSizeLabel;
    private javax.swing.JTextField simulatorConfigurationSizeTextField;
    private javax.swing.JCheckBox simulatorOutputCheckBox;
    private javax.swing.JPanel simulatorStartConfigurationPanel;
    private javax.swing.JLabel slashLabel;
    private javax.swing.JButton startCalculationButton;
    private javax.swing.JLabel startConfigurationLabel;
    private javax.swing.JTextField startConfigurationTextField;
    private javax.swing.JButton startSimulationButton;
    private javax.swing.JButton stopCalculationButton;
    private javax.swing.JCheckBox testAllBalancedRulesCheckBox;
    private javax.swing.JCheckBox testAllNeighborhoodPermutationsCheckBox;
    private javax.swing.JCheckBox testAllNeighborhoodVariationsCheckBox;
    private javax.swing.JPanel testPanel;
    private javax.swing.JCheckBox useFastCPPPluginCheckBox;
    private javax.swing.JLabel versionLabel;
    private javax.swing.JLabel zoomLabel;
    private javax.swing.JSlider zoomSlider;
    // End of variables declaration//GEN-END:variables
}
