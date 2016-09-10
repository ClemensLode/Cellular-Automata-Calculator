package ALGORITHM;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * The Function class holds all parameters (especially neighborhood, number of cell 
 * states and rule number), organizes automated tests and calculates expected
 * memory consumption and expected time for the calculation.
 * Both the algorithm classes (*BruijnProductGraph) and the GUI accesses this class.
 */
public class Function {

    public static interface CallBack {

        /**
         * Increases the progress bar by a certain amount
         * @param my_progress amount to add to the progress bar
         */
        void updateBar(int my_progress);

        /**
         * Let the progress bar jump to the next rule/neighborhood to test in order to ignore nodes of the Bruijn product graph that were not calculated because the calculation was already succesful.
         * @param my_progress maximal amount of steps to move 'progress' forward
         */
        void forwardUpdateBar(int my_progress); // moves the progress bar forward to the next multiple, returns true if there was an actual change
// complete current calculation => round it up to the next X
    }
    /**
     * General callback routine in order to update the progress from within the algorithm framework (which doesn't have direct access to the GUI)
     */
    public static CallBack callback = null;
    /**
     * Minimum number of cell states. The cases 0 (empty set) or 1 cell state (always injective) are trivial.
     */
    public final static int MIN_NUMBER_OF_CELL_STATES = 2;
    private final static int MAX_NUMBER_OF_CELL_STATES = 256;
    /**
     * Maximal availible memory. Program prints out a warning if the expected amount of needed memory (worst case, ~5*(k^n)^2)) reaches this value.
     */
    public final static long MAX_MEMORY_SIZE = 2147483647;
    /**
     * Size of a single node in bytes
     */
    public final static int SIZE_OF_NODE = 28;
    /**
     * Rough estimation of how many calculation steps per second the computer and algorithm can execute
     */
    public static BigInteger SPEED_FACTOR = BigInteger.valueOf(500000);

    public static void testIfSingleCalculation() {
        singleCalculation = getTotalCalculationSteps().compareTo(BigInteger.valueOf(getCurrentProblemSize())) > 0;
    }
    private static int numberOfCellStates = MIN_NUMBER_OF_CELL_STATES;
    private static int minNumberOfCellStates = MIN_NUMBER_OF_CELL_STATES;
    private static int maxNumberOfCellStates = MIN_NUMBER_OF_CELL_STATES;
    private static int significantMaxArraySize = 0;
    public static boolean singleCalculation = true;
    private static int significantFunction[] = {};
    // holds the current significant function without the current new-ca rule definition permutations
    private static int currentSignificantFunction[] = {};
    private static int function[] = null;
    private static int lastElementIndex = 0;
    private static int maxArraySize = 0;
    /**
     * Upper boundary of number of remaining calculation steps
     */
    public static BigInteger progress = BigInteger.ZERO;
    /**
     * value of 'progress' where the last update has happened
     */
    public static BigInteger lastUpdate = BigInteger.ZERO;
    public static long lastUpdateTime = 0;
    public static boolean update = false;
    private static BigInteger processedRulePermutations = BigInteger.ONE;
    private static boolean testAllRules = false;
    private static BigInteger totalCalculationSteps;

    /**
     * Returns the upper limit of calculation steps left, uses the parameters of the last time when updateTotalSteps was called
     * @return upper limit of calculation steps left
     * @see Function.updateTotalSteps()
     */
    public static BigInteger getTotalCalculationSteps() {
        return totalCalculationSteps;
    }

    /**
     * Updates totalCalculationSteps according to the current parameters
     * @see Function.getNumberOfAllPermutations()
     */
    public static void updateTotalSteps() {
        totalCalculationSteps = getNumberOfAllPermutations();
    }

    public static int setNumberOfCellStatesRange(int cells[]) throws Exception {
        int result = 0;
        int min_c = getMinNumberOfCellStates();
        int max_c = getMaxNumberOfCellStates();

        if (cells.length == 1) {
            result = setMinMaxNumberOfCellStates(cells[0], cells[0]);
        } else if (cells.length == 2) {
            result = setMinMaxNumberOfCellStates(cells[0], cells[1]);
        } else {
            result = -3;
        }

        if (result == 0) {
            numberOfCellStates = getMinNumberOfCellStates();
            if ((min_c != getMinNumberOfCellStates()) || (max_c != getMaxNumberOfCellStates())) {
                createStandardSignificantFunction();
            }
//          initializeMinimalCellStatesValue(); // TODO?
        } else {
            setMinMaxNumberOfCellStates(min_c, max_c);
        }
        return result;
    }

    /**
     * Sets the lower limit of cell states to test. If minNumberOfCellStates == maxNumberOfCellStates only a single number of cell states will be tested.
     * @param min_number_of_cell_states Lower limit of cell states to test
     * @return -1 if the value is out of range, 0 otherwise
     *TODO
     */
    public static int setMinMaxNumberOfCellStates(int min_number_of_cell_states, int max_number_of_cell_states) throws Exception {
        if (min_number_of_cell_states == getMinNumberOfCellStates() && max_number_of_cell_states == getMaxNumberOfCellStates()) {
            return 0;
        }
        if (min_number_of_cell_states < MIN_NUMBER_OF_CELL_STATES ||
                max_number_of_cell_states < min_number_of_cell_states) {
            return -1;
        }
        if (max_number_of_cell_states > calculateMaxNumberOfCellStates()) {
            return -2;
        }

        minNumberOfCellStates = min_number_of_cell_states;
        maxNumberOfCellStates = max_number_of_cell_states;
        return 0;
    }

    /**
     * Cycles through all numbers of cell states (a simple incrementation), resets the rule number when reaching the 
     * @return false if the last permutation has been reached
     * @throws ALGORITHM.Function.Exception if an internal error occured when the new function was created
     */
    public static boolean nextCellStatePermutation() throws Exception {
        if (getNumberOfCellStates() == getMaxNumberOfCellStates()) {
            setNumberOfCellStates(getMinNumberOfCellStates());
            return false;
        } else {
            setNumberOfCellStates(getNumberOfCellStates() + 1);
            return true;
        }
    }

    public static void initializeMinimalCellStatesValue() throws Exception {
        if (numberOfCellStates == getMinNumberOfCellStates()) {
            return;
        } //new
        numberOfCellStates = getMinNumberOfCellStates();
        createStandardSignificantFunction();
    }

    public static int setNumberOfCellStates(int number_of_cell_states) throws Exception {
        if ((number_of_cell_states < MIN_NUMBER_OF_CELL_STATES) || (number_of_cell_states > calculateMaxNumberOfCellStates())) {
            return -1;
        }
        if (numberOfCellStates == number_of_cell_states) {
            return 0;
        }
        numberOfCellStates = number_of_cell_states;
        createStandardSignificantFunction();
        return 0;
    }

    public static boolean nextNeighborhoodRulePermutation(final int[] significant_neighborhood) {
        return Misc.next_rule_neighborhood_permutation(significantFunction, significant_neighborhood, getNumberOfCellStates());
    }

// neighborhoods
// functions, highest level, needs neighborhoods and cell states
    private static void updateMaxArraySize() {
        lastElementIndex = Misc.pow(getNumberOfCellStates(), Neighborhood.getNeighborhoodSize() - 1);
        maxArraySize = lastElementIndex * getNumberOfCellStates();
        function = new int[getMaxArraySize()];
    }

    private static void updateSignificantMaxArraySize() {
        significantMaxArraySize = Misc.pow(getNumberOfCellStates(), Neighborhood.getSignificantNeighborhoodSize());
        significantFunction = new int[getSignificantMaxArraySize()];
    }

    /**
     * Calculates a readable rule number on basis of the internal significant function and the parameters, i.e. the positions in the neighborhood that were not defined are ignored
     * @return Significant rule number, can become quite big
     */
    public static BigInteger getSignificantRuleNumber() {
        BigInteger rule_nr = BigInteger.ZERO;
        try {
            for (int i = getSignificantMaxArraySize() - 1; i >= 0; i--) {
                rule_nr = rule_nr.multiply(BigInteger.valueOf(getNumberOfCellStates()));
                rule_nr = rule_nr.add(BigInteger.valueOf(getSignificantFunction(i)));
            }
        } catch (Exception e) {
            System.out.println(e);
        // doesn't happen?
        }
        return rule_nr;
    }

    public static String getSignificantFunctionString() {
        String t = "";
        for (int i = 0; i < getSignificantMaxArraySize(); i++) {
            t += significantFunction[i];
        }
        return t;
    }

    /**
     * Calculates a readable rule number on basis of the internal significant function and the parameters, i.e. the positions in the neighborhood that were not defined are ignored
     * If disordered neighborhoods are tested this function will return the (to the disordered neighborhood) appropriate rule number
     * @return Significant rule number, can become quite big
     */
    public static BigInteger getCurrentSignificantRuleNumber() {
        BigInteger rule_nr = BigInteger.ZERO;

        for (int i = getSignificantMaxArraySize() - 1; i >= 0; i--) {
            rule_nr = rule_nr.multiply(BigInteger.valueOf(getNumberOfCellStates()));
            rule_nr = rule_nr.add(BigInteger.valueOf(currentSignificantFunction[i]));
        }
        return rule_nr;

    }

    /**
     * Calculates a readable rule number on basis of the internal function and the parameters, i.e. including the positions in the neighborhood that were not defined
     * @return Rule number as a BigInteger
     */
    public static BigInteger getRuleNumber() {
        BigInteger rule_nr = BigInteger.ZERO;
        try {
            for (int i = getMaxArraySize() - 1; i >= 0; i--) {
                rule_nr = rule_nr.multiply(BigInteger.valueOf(getNumberOfCellStates()));
                rule_nr = rule_nr.add(BigInteger.valueOf(getFunction(i)));
            }
        } catch (Exception e) {
            System.out.println(e);
        // doesn't happen'
        }
        return rule_nr;
    }

    public static BigInteger getMaxRuleNumber() {
        return BigInteger.valueOf(getNumberOfCellStates()).pow(getSignificantMaxArraySize());
    }

    /**
     * Creates a new significant function on basis of the provided rule number by succesively dividing it by the number of cell states.
     * @throws ALGORITHM.Function.Exception when the rule number is out of bounds
     * @param rule_nr the rule number as a BigInteger
     */
    public static void createSignificantFunction(BigInteger rule_nr) throws Exception {
        // TODO allow multiple rules to allow testing of multiple cell states / significant neighborhood values
        updateSignificantMaxArraySize();
        if ((rule_nr.compareTo(BigInteger.ZERO) < 0) || (rule_nr.compareTo(getMaxRuleNumber()) >= 0)) {
            throw new Exception("createSignificantFunction(): parameter rule_nr" + rule_nr + " out of range.");
        }
        for (int i = 0; i < getSignificantMaxArraySize(); i++) {
            significantFunction[i] = rule_nr.mod(BigInteger.valueOf(getNumberOfCellStates())).intValue();
            rule_nr = rule_nr.divide(BigInteger.valueOf(getNumberOfCellStates()));
        }
        initCurrentSignificantFunction();
        Neighborhood.resetSignificantNeighborhoodSize();
    }

    private static class Summand {

        public int factor;
        public int quotient;        
        public int[] power;
        public boolean to_delete = false;

        public Summand(int size, int fac) {
            power = new int[size];
            for (int i = 0; i < size; i++) {
                power[i] = 0;
            }
            factor = fac;
        }

        public Summand(int fac, int[] pow) {
            factor = fac;
            power = pow.clone();
        }
    }

// create a polynom that will return 'f_value' for the x values 'x_values' and '0' otherwise.
    private static ArrayList<Summand> create_function_summands(final int[] x_values, int f_value, int cell_states) {

        int base_factor = 1;

        System.out.print("create_function_summands [");
            for (int k = 0; k < x_values.length; k++) {
                System.out.print(x_values[k]);
            }
        System.out.print(" - " + f_value + "] :");
       
// in order for the function to return always 0 for x != x_values and 'f_value' 
// it has to be in the following format:
// Product over all i: (x_i - 0)(x_i - 1)* ... *(x_i - (c_i - 1))(x_i - (c_i+1))* ... * (x_i - (p-1))
// The final formula has to be multiplicated by f_value/((c_i-0)(c_i-1)*...)
        for (int i = 0; i < x_values.length; i++) {
            for (int j = 0; j < cell_states; j++) {
                if (x_values[i] != j) {
                    base_factor *= (x_values[i] - j);
                }
            }
        }
        
//        TODO Formel nochmal überdenken
        System.out.print("{" + base_factor + "} ");

        ArrayList<Summand> summands = new ArrayList();
        Summand t = new Summand(x_values.length, 1);
        summands.add(t);
        
// Now we have to multiplicate all elements and create the standard form of a polynom:
        for (int i = 0; i < x_values.length; i++) {
            for (int j = 0; j < cell_states; j++) {

                for(int k = 0; k < x_values.length; k++) {
                    
                if (x_values[k] != j) {
                    ArrayList<Summand> temp = new ArrayList();
                    if (j != 0) {
                        // Alle Summanden kopieren und mit -j multiplizieren
                        for (Summand s : summands) {
                            temp.add(new Summand(s.factor * (-j), s.power));
                            System.out.print("<" + s.factor * (-j) + "|");
                            for(int tt : s.power) {
                                System.out.print(tt);
                            }
                            System.out.print(">");
//                            ???
                        }
                    }
                    // bestehende Summanden mit x_i multiplizieren
                    for (Summand s : summands) {
                        s.power[i]++;
                    }
                    summands.addAll(temp);
                }
                }
            }
        }
        
        /*
        System.out.print("Preparing factor: ");
        for(Summand s:summands) {
            System.out.print(s.factor + "*");
            int k = 0;
             for(int i:s.power) {
                 if(i==1) {
                    System.out.print("x" + k);
                 }
                 else if(i != 0) {
                     System.out.print("x_" + k + "^" + i);
                 }
                 System.out.print(" ");
                 k++;
             }
        }
        System.out.println();
        /*
        1*x_0^2 x_1^2 x_2^2 
        -1*x0 x_1^2 x_2^2 
        -2*x0 x_1^2 x_2^2 
        2* x_1^2 x_2^2 
                -1*x_0^2 x1 x_2^2 
                1*x0 x1 x_2^2 
                2*x0 x1 x_2^2 
                -2* x1 x_2^2 
                -2*x_0^2 x1 x_2^2 
                2*x0 x1 x_2^2 
                4*x0 x1 x_2^2 
                -4* x1 x_2^2 
                2*x_0^2  x_2^2 
                -2*x0  x_2^2 
                -4*x0  x_2^2 
                4*  x_2^2 
                -1*x_0^2 x_1^2 x2 
                1*x0 x_1^2 x2 
                2*x0 x_1^2 x2 
                -2* x_1^2 x2 
                1*x_0^2 x1 x2 
                -1*x0 x1 x2 
                -2*x0 x1 x2 
                2* x1 x2 
                2*x_0^2 x1 x2 
                -2*x0 x1 x2 
                -4*x0 x1 x2 
                4* x1 x2 
                -2*x_0^2  x2 
                2*x0  x2 
                4*x0  x2 
                -4*  x2 
                -2*x_0^2 x_1^2 x2 
                2*x0 x_1^2 x2 
                4*x0 x_1^2 x2 
                -4* x_1^2 x2 
                2*x_0^2 x1 x2 
                -2*x0 x1 x2 
                -4*x0 x1 x2 
                4* x1 x2 
                4*x_0^2 x1 x2 -
                4*x0 x1 x2 
                -8*x0 x1 x2 
                8* x1 x2 
                -4*x_0^2  x2 
                4*x0  x2 
                8*x0  x2 
                -8*  x2 
                2*x_0^2 x_1^2  
                -2*x0 x_1^2  
                -4*x0 x_1^2  
                4* x_1^2  
                -2*x_0^2 x1  
                2*x0 x1  
                4*x0 x1  
                -4* x1  
                -4*x_0^2 x1  
                4*x0 x1  
                8*x0 x1  
                -8* x1  
                
                4*x_0^2   
                -4*x0   
                -8*x0   
                8*   
        */
        for(Summand s:summands) {
            s.factor = s.factor * f_value; //(1 + cell_states)?
            s.quotient = base_factor;
        }
        
        for(Summand s:summands) {
            System.out.print("(");
            System.out.print(s.factor + "/" + s.quotient + ":");
            for(int i:s.power) {
                System.out.print(i + " ");
            }
            System.out.print(")");
        }
        System.out.println();

        return summands;
    }

    public static void simplify_summands(ArrayList<Summand> summand_list, int cell_states) {
        int x = 0;
        for (Summand s : summand_list) {
            if (s.to_delete) {
                x++;
                continue;
            }
            int y = 0;
            for (Summand t : summand_list) {
                if (t.to_delete || x == y) {
                    y++;
                    continue;
                }
                boolean equal = true;
                for (int i = 0; i < s.power.length; i++) {
                    if (s.power[i] != t.power[i]) {
                        equal = false;
                        break;
                    }
                }
                if (equal) {
                    t.to_delete = true;
                    int temp = s.quotient;
                    s.factor *= t.quotient;
                    t.factor *= s.quotient;
                    s.factor += t.factor;
                    s.quotient *= t.quotient;
                }
                y++;
            }
            if(s.factor == 0) {
                s.to_delete = true;
            }
            x++;
        }
        ArrayList<Summand> remove_list = new ArrayList();
        for (Summand s : summand_list) {
            if (s.to_delete) {
                remove_list.add(s);
            }
        }
        summand_list.removeAll(remove_list);
        for (Summand s : summand_list) {        
            if(s.factor % s.quotient == 0) {
                s.factor /= s.quotient;
                s.quotient = 1;
            }
            if(s.quotient % s.factor == 0) {
                s.quotient /= s.factor;
                s.factor = 1;
            }
            if(s.quotient < 0) {
                s.factor *= -1;
                s.quotient *= -1;
            }
        }
    }

    public static String getSignificantPolynomialRule() {
        String polynomial_rule = "";

        int p = getNumberOfCellStates();

        int neighborhood_size = Neighborhood.getSignificantNeighborhoodSize();
        int significant_max_array_size = getSignificantMaxArraySize();

        int x_values[] = new int[neighborhood_size];
        for (int i = 0; i < neighborhood_size; i++) {
            x_values[i] = 0;
        }
        ArrayList<Summand> summand_list = new ArrayList();
        
        System.out.print("Significant Function: ");
        for (int i = 0; i < significant_max_array_size; i++) {
            try{
                System.out.print(getSignificantFunction(i));
            }catch(Exception e) {
                
            }
        }
        System.out.println();

        for (int i = 0; i < significant_max_array_size; i++) {
            try {
            if (getSignificantFunction(i) > 0) {
                summand_list.addAll(create_function_summands(x_values, getSignificantFunction(i), p));
            }
            } catch(Exception e) {
                System.out.println("getSignificantPolynomialRule, System error, getSignificantFunction: " + e);
            }

            for (int j = neighborhood_size - 1; j >= 0; j--) {
                x_values[j]++;
                if (x_values[j] == p) {
                    x_values[j] = 0;
                } else {
                    break;
                }
            }
        }
        // simplify summands
        simplify_summands(summand_list, p);
        
        System.out.print("Simplified: ");
        for(Summand s:summand_list) {
            System.out.print(s.factor + "/" + s.quotient + "[");
            for(int i:s.power) {
                System.out.print(i);
            }
            System.out.print("]");
        }


        boolean first_entry = true;
        System.out.println("LAENGE: " + summand_list.size());
        for (Summand s : summand_list) {
            if (s.factor == 0) {
                continue;
            }

            if (first_entry) {
                first_entry = false;
            } else if (s.factor * s.quotient > 0) {
                polynomial_rule += "+ ";
            }
            
            if(s.quotient != 1) {
                polynomial_rule += s.factor + "/" + s.quotient + " ";
            } else if(s.factor != 1) {
                if(s.factor == -1)
                    polynomial_rule += "-";
                else
                    polynomial_rule += s.factor;
            }
            
            boolean at_least_one = false;
            for (int i = 0; i < x_values.length; i++) {
                if (s.power[i] == 0) {
                    continue;
                }
                at_least_one = true;
                polynomial_rule += "x" + i;
                if (s.power[i] != 1) {
                    polynomial_rule += "^" + s.power[i];
                }
                polynomial_rule += " ";
            }
            if (!at_least_one && (Math.abs(s.factor) == Math.abs(s.quotient))) {
                polynomial_rule += "1 ";
            }
        }

        polynomial_rule = "f = " + polynomial_rule;
        if (first_entry) {
            polynomial_rule = polynomial_rule + "0";
        }

        
        System.out.println("new polynomial rule: " + polynomial_rule);
        return polynomial_rule;
    }

    private final static String boolean_not_sign = new String("'");
    private final static String boolean_or_sign = new String("v");
    
    public static String getSignificantBooleanRule() {
        String boolean_rule = "f = ";
//        System.out.println("GET SIGNIFICANT BOOLEAN RULE BEGIN");
  //      System.out.println(ALGORITHM.Function.getSignificantFunctionString());

        int neighborhood_size = Neighborhood.getSignificantNeighborhoodSize();
        int significant_max_array_size = getSignificantMaxArraySize();

        //   Liste machen

        // Falls zwei Elemente zweier Formeln übereinstimmen: Eine rauslöschen, unterschiedliches Element auf Zustand '2' setzen

        int set_bits[][] = new int[significant_max_array_size][neighborhood_size];
        for (int i = 0; i < significant_max_array_size; i++) {
            for (int j = 0; j < neighborhood_size; j++) {
                set_bits[i][j] = 2;
            }
        }
        try {
            for (int i = 0; i < significant_max_array_size; i++) {

                if (getSignificantFunction(i) == 1) {
                    // bit belegung von i finden
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
        } catch (Exception e) {

        }


            /*        System.out.print("Before bits");
                        for (int ii = 0; ii < significant_max_array_size; ii++) {
                            for (int k = 0; k < neighborhood_size; k++) {
                                System.out.print(set_bits[ii][k]);
                            }
                            System.out.print(".");
                        }
                    System.out.println();*/

// to catch all pairs we have to iterate several times (could be optimized but who cares)
        for (int l = 0; l < neighborhood_size; l++) {
 
            for (int i = 0; i < significant_max_array_size; i++) {

                // find a pair of entries with one different bit and combine them by replacing the differenting bit with a DONTCARE ('2')    
                for (int j = i + 1; j < significant_max_array_size; j++) {
                    int different_positions = 0;
                    int different_position = 0;
                    for (int k = 0; k < neighborhood_size; k++) {
                        
                        // cancel if the bit that is different is 2                    
                        if (((set_bits[i][k] == 2) || (set_bits[j][k] == 2)) && (set_bits[i][k] != set_bits[j][k])) {
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
                        // set bit of the differenting part of the pair to DONTCARE
                        set_bits[j][different_position] = 2;
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
                        not_true = false;
                        for(int k = 0; k < neighborhood_size; k++) {
                            if(set_bits[j][k] != 2) {
                                not_true = true;
                            }
                        }
                        if(!not_true) {
    //                        System.out.println("cancel1 " + j + "/" + different_position + " : " + i);
                            return boolean_rule + "true";
                        }
                        
  //                  System.out.println("changed " + j + "/" + different_position + " : " + i);
                    }
/*
                    System.out.print("AfterbitsA: ");
                        for (int ii = 0; ii < significant_max_array_size; ii++) {
                            for (int k = 0; k < neighborhood_size; k++) {
                                System.out.print(set_bits[ii][k]);
                            }
                            System.out.print(".");
                        }
                    System.out.println();*/
                    
                    
                    different_positions = 0;
                    different_position = 0;
                    int two_count_1 = 0;
                    int two_count_2 = 0;
                    for (int k = 0; k < neighborhood_size; k++) {
                        
                        if (set_bits[i][k] != set_bits[j][k]) {
                            if(set_bits[i][k] == 2) {
                                two_count_1++;
                                if(two_count_1 == 2) {
                                    different_positions = 2;
                                    break;
                                }
                            } else if(set_bits[j][k] == 2) {
                                two_count_2++;
                                if(two_count_2 == 2) {
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
                    if(different_positions == 1 && two_count_1 != two_count_2) {
                        if(two_count_1 == 1) {
                            set_bits[j][different_position] = 2;
                        } else if(two_count_2 == 1) {
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
//                            System.out.println("cancel2");
                            return boolean_rule + "true";
                        }       
                        
//                        System.out.println("changed2 " + j + "/" + different_position + " : " + i);
                    }
        /*            System.out.print("AfterbitsB: ");
                        for (int ii = 0; ii < significant_max_array_size; ii++) {
                            for (int k = 0; k < neighborhood_size; k++) {
                                System.out.print(set_bits[ii][k]);
                            }
                            System.out.print(".");
                        }
                    System.out.println();                    */
                }

                    
            }
        }

//TODO, wird irgendwie bei RULE 15 (1111, TRUE), zuwenig entfernt

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
                    if (!first_entry) {
                        boolean_rule = boolean_rule + boolean_or_sign + "  ";
                    }
                    first_entry = false;

                    for (int k = 0; k < neighborhood_size; k++) {
                        if (set_bits[i][k] == 2) {
                            continue;
                        }
                        boolean_rule = boolean_rule + "x" + k;//Neighborhood.getSignificantNeighborhoodPosition(k);
                        if (set_bits[i][k] == 0) {
                            boolean_rule = boolean_rule + boolean_not_sign;
                        }
                        boolean_rule = boolean_rule + " ";
                    }
                }
            }
        } catch (Exception e) {

        }
        if (first_entry) {
            boolean_rule = boolean_rule + "false";
        }

        return boolean_rule;
    }
    
public static BigInteger extractBooleanRuleNumber(String boolean_rule) throws Exception {
        BigInteger rule_nr = BigInteger.ZERO;
        updateSignificantMaxArraySize();
        int resulting_function[] = new int[getSignificantMaxArraySize()];
        for (int i = 0; i < getSignificantMaxArraySize(); i++) {
            resulting_function[i] = 0;
        }

        boolean_rule = boolean_rule.toLowerCase();
        boolean_rule = " " + boolean_rule;
        boolean_rule = boolean_rule.replace(boolean_or_sign, " " + boolean_or_sign + " ");
        boolean_rule = boolean_rule.replace("=", " = ");
        boolean_rule = boolean_rule.replace("*", " ");
        boolean_rule = boolean_rule.replace(" 0", "b0");
        boolean_rule = boolean_rule.replace(" 1", "b1");
        boolean_rule = boolean_rule.replace(" ", "");
//        "f = " teilen
        String[] elements = boolean_rule.split("=");
        if (elements.length > 2) {
            throw new Exception("extractBooleanRuleNumber(): More than one '=' found.");
        }
        if (elements.length == 2) {
            boolean_rule = elements[1];
        }

//        auf false/true pruefen        
        int true_index = boolean_rule.indexOf("true");
        if (true_index > 0) {
            throw new Exception("extractBooleanRuleNumber(): value 'true' found at unexpected place [" + boolean_rule + "].");
        } else if (true_index == 0) {
            return getMaxRuleNumber().subtract(BigInteger.ONE);
        }
        int false_index = boolean_rule.indexOf("false");
        if (false_index > 0) {
            throw new Exception("extractBooleanRuleNumber(): value 'false' found at unexpected place [" + boolean_rule + "].");
        } else if (false_index == 0) {
            return BigInteger.ZERO;
        }



        String[] or_elements = boolean_rule.split(boolean_or_sign);

        ArrayList<ArrayList> xe_list = new ArrayList();
        ArrayList<ArrayList<Boolean> > not_list_list = new ArrayList();

        for (String s : or_elements) {
            if (s.contains("b0")) {
                // ignore, is always false
                continue;
            }
            if (s.compareTo("b1") == 0) {
                return getMaxRuleNumber().subtract(BigInteger.ONE);
            }
            if (s.contains("b1")) {
                s = s.replace("b1", "");
            }

            s = s + " ";

            ArrayList<Integer> xe = new ArrayList();
            ArrayList<Boolean> not_list = new ArrayList();

            String[] not_elements = s.split(boolean_not_sign);

            for (String f : not_elements) {
                if (f.compareTo(" ") == 0) {
                    continue;
                }
                int l1 = f.length();
                f = f.trim();
                boolean last_sign = false;
                if (f.length() != l1) {
                    last_sign = true;
                }

                String[] x_elements = f.split("[Xx]");
                for (int x = 0; x < x_elements.length; x++) {
                    if (x_elements[x].length() == 0) {
                        continue;
                    }
                    xe.add(Integer.valueOf(x_elements[x]));
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
        ArrayList<Integer> element_list = new ArrayList();

        for (ArrayList<Integer> l : xe_list) {
            for (Integer i : l) {
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
            throw new Exception("extractBooleanRuleNumber(): Implied neighborhood size exceeds given neighborhood size");
        }

        int significant_neighborhood_size = element_list.size();

// besser wäre wohl sich auf die vorgegebene neighborhood zu beziehen!
        if (significant_neighborhood_size > Neighborhood.getSignificantNeighborhoodSize()) {
            throw new Exception("extractBooleanRuleNumber(): Implied significant neighborhood size exceeds given significant neighborhood size");
        }

        significant_neighborhood_size = Neighborhood.getSignificantNeighborhoodSize();
        neighborhood_size = Neighborhood.getNeighborhoodSize();

        int m = 0;
        for (ArrayList<Integer> l : xe_list) {
            // x1x3 => setze x1-x2x3 und x1x2x3
            int set_bits[] = new int[significant_neighborhood_size];
            for (int i = 0; i < significant_neighborhood_size; i++) {
                set_bits[i] = -1;
            }
            int n = 0;
// Geht alle Elemente der xe_list durch, prüft gleichzeitig aus der aktuellen entsprechenden not_list ob Eintrag negativ oder positiv ist
            for (Integer i : l) {
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

            // z.B. 3 freie Stellen, 2 feste Stellen => 8 Einträge
            for (int i = 0; i < number_variable_entries; i++) {
                boolean current_entry[] = new boolean[significant_neighborhood_size];
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

        for (int i = getSignificantMaxArraySize() - 1; i >= 0; i--) {
            rule_nr = rule_nr.multiply(BigInteger.valueOf(2));
            rule_nr = rule_nr.add(BigInteger.valueOf(resulting_function[i]));
        }


        /*
        Fehlende Fälle aufspalten
        Größe prüfen
        significantFunction zuweisen
        initCurrentSignificantFunction();
        updateFunction();
        minSignificantNeighborhoodSize = maxSignificantNeighborhoodSize = getSignificantNeighborhoodSize();        
        String neighborhoods[] = boolean_rule_nr.split("[) OR (]");
         */


        return rule_nr;
    }

    /**
     * Creates a standard rule (000....0111...1) on basis of the current parameters
     * @throws ALGORITHM.Function.Exception when creating / accessing the significant function (TODO)
     */
    public static void createStandardSignificantFunction() throws Exception {
        updateSignificantMaxArraySize();
        int k = 0;
        for (int j = 0; j < getNumberOfCellStates(); j++) {
            for (int i = 0; i < getSignificantMaxArraySize() / getNumberOfCellStates(); i++) {
                significantFunction[i + j * getSignificantMaxArraySize() / getNumberOfCellStates()] = j;
            }
        }
        processedRulePermutations = BigInteger.ONE;
        initCurrentSignificantFunction();
    }
    

    public static class PolynomialObject {

        public boolean is_constant = false;
        public int index = 0;
        public int quotient = 1;
        }; 
        
        public static BigInteger extractPolynomialRuleNumber(String polynomial_rule) throws Exception {
        

                System.out.println("EXTRACTING POLYNOMIAL RULE NUMBER");
        System.out.println("ORIGINAL polynomial rule: " + polynomial_rule);
        BigInteger rule_nr = BigInteger.ZERO;
        int p = getNumberOfCellStates();
        updateSignificantMaxArraySize();
        int resulting_function[] = new int[getSignificantMaxArraySize()];
        for (int i = 0; i < getSignificantMaxArraySize(); i++) {
            resulting_function[i] = 0;
        }

        polynomial_rule = polynomial_rule.toLowerCase();

        polynomial_rule = polynomial_rule.replace("+", " + ");
        polynomial_rule = polynomial_rule.replace("-", " + - ");
        polynomial_rule = polynomial_rule.replace("=", " = ");
        polynomial_rule = polynomial_rule.replace("*", " ");
        for (int i = -p; i <= p; i++) {
            polynomial_rule = polynomial_rule.replace(" " + i, "b" + i);
        }
        for (int i = 0; i <= p; i++) {
            polynomial_rule = polynomial_rule.replace("^" + i, "c" + i);
        }

        polynomial_rule = polynomial_rule.replace(" ", "");
        polynomial_rule = polynomial_rule.replace("-x", "+b-1x");
        polynomial_rule = polynomial_rule.replace("-b", "b-");
        polynomial_rule = polynomial_rule.replace("++", "+");        
        polynomial_rule = polynomial_rule.replace("--", "");
        polynomial_rule = polynomial_rule.replace("^b", "c");

//        "f = " teilen
        String[] elements = polynomial_rule.split("=");
        if (elements.length > 2) {
            throw new Exception("extractPolynomialRuleNumber(): More than one '=' found.");
        }
        if (elements.length == 2) {
            polynomial_rule = elements[1];
        }
        if(polynomial_rule.charAt(0) == '+') {
            polynomial_rule = polynomial_rule.substring(1);
        }
        System.out.println("polynomial rule: " + polynomial_rule);

        String[] or_elements = polynomial_rule.split("\\+");

        ArrayList<ArrayList<PolynomialObject>> p_list = new ArrayList();

        for (String s : or_elements) {
            if (s.contains("b0") || s.isEmpty()) {
                // ignore, is always 0
                continue;
            }
            System.out.println("OR_ELEMENT: " + s);
            
            // contains a '1' but is not the only entry => can be ignored
            if (s.compareTo("b1") != 0 && s.contains("b1")) {
                System.out.println("IGNORE '" + s + "'");
                s = s.replace("b1", "");
            }


            ArrayList<PolynomialObject> pe_list = new ArrayList();

            int factor = 1;
            int quotient = 1;

            String[] b_elements = s.split("[b]");
            for (String bs : b_elements) {
                if (bs.length() == 0) {
                    continue;
                }
                System.out.println("B_ELEMENT: " + bs);

                int index_next_x = bs.indexOf("x");
                String value_string = bs;
                if(index_next_x > 0) {
                    value_string = bs.substring(0, index_next_x);
                }
                
                boolean found_quotient = (value_string.indexOf("/") != -1);
                String q_elements[] = value_string.split("/");
               
                if(q_elements.length > 2) {
                    throw new Exception("Too many '/' in polynomial string.");
                } else if(found_quotient) {
                    factor *= Integer.valueOf(q_elements[0]);
                } 
                
                if(q_elements.length == 2) {
                    quotient *= Integer.valueOf(q_elements[1]);
                } 
                
                if (index_next_x == -1) {
                    continue;
                } else if (index_next_x > 0) {
                    bs = bs.substring(index_next_x);
                }
                System.out.println("BS_ELEMENT " + bs);

                String[] x_elements = bs.split("[x]");

                for (String x : x_elements) {
                    if (x.length() == 0) {
                        continue;
                    }
                    System.out.println("X_ELEMENT: " + x);
                    String[] pow_elements = x.split("[c]");
                    int pow_count = 1;
                    if (pow_elements.length == 2) {
                        System.out.println("value of pow_elements[1] " + pow_elements[1]);
                        pow_count = Integer.valueOf(pow_elements[1]);
                    } else if (pow_elements.length > 2) {
                        throw new Exception("Error too many pows");
                    }
                    for (int k = 0; k < pow_count; k++) {

                        PolynomialObject t = new PolynomialObject();
                        System.out.println("value of pow_elements[0] " + pow_elements[0]);
                        t.index = Integer.valueOf(pow_elements[0]);
                        t.is_constant = false;
                        pe_list.add(t);
                        System.out.print("x_" + t.index + " * ");
                    }
                }
                if (factor != 1 || quotient != 1) {
                    PolynomialObject t = new PolynomialObject();
                    t.index = factor;
                    t.quotient = quotient;
                    t.is_constant = true;
                    pe_list.add(t);
                }
                System.out.println(" ... with factor " + factor);
            }
            if(pe_list.isEmpty()) {
                PolynomialObject t = new PolynomialObject();
                t.index = factor;
                t.quotient = quotient;
                t.is_constant = true;
                pe_list.add(t);
                System.out.println(" + constant with factor " + factor);
            }
            p_list.add(pe_list);            
        }

        // do some basic checking

        int largest_x = 0;
        ArrayList<Integer> element_list = new ArrayList();

        for (ArrayList<PolynomialObject> i : p_list) {
            for (PolynomialObject j : i) {
                if (!j.is_constant) {
                    if (j.index > largest_x) {
                        largest_x = j.index;
                    }
                    if (!element_list.contains(j.index)) {
                        element_list.add(j.index);
                    }

                }
            }
        }


        int neighborhood_size = largest_x + 1;

        if (neighborhood_size > Neighborhood.getNeighborhoodSize()) {
            throw new Exception("extractPolynomialRuleNumber(): Implied neighborhood size (" + neighborhood_size + ") exceeds given neighborhood size (" + Neighborhood.getNeighborhoodSize() + ")");
        }

        int significant_neighborhood_size = element_list.size();

        if (significant_neighborhood_size > Neighborhood.getSignificantNeighborhoodSize()) {
            throw new Exception("extractPolynomialRuleNumber(): Implied significant neighborhood size (" + significant_neighborhood_size + ") exceeds given significant neighborhood size (" + Neighborhood.getSignificantNeighborhoodSize() + ")");
        }

        for (ArrayList<PolynomialObject> i : p_list) {
            for (PolynomialObject j : i) {
                if(j.is_constant) {
                    System.out.print(" " + j.index);
                } else {
                    System.out.print("x" + j.index);
                }
            }
            System.out.print(" + ");
        }
        System.out.println();
        
        
        significant_neighborhood_size = Neighborhood.getSignificantNeighborhoodSize();
        neighborhood_size = Neighborhood.getNeighborhoodSize();

        for (int i = 0; i < getSignificantMaxArraySize(); i++) {
            // calculate corresponding values of neighborhood values
            int[] x_values = new int[significant_neighborhood_size];
            for (int j = 0; j < significant_neighborhood_size; j++) {
                x_values[j] = 0;
            }
            int j = i;
            for (int k = 0; k < significant_neighborhood_size; k++) {
                x_values[significant_neighborhood_size - 1 - k] = j % p;
                j /= p;
            }
            resulting_function[i] = calculate_polynomial_function(x_values, p_list, p);
            System.out.print("resulting function [" + i + "/" + j + "] {");
            for (int k = 0; k < significant_neighborhood_size; k++) {
                System.out.print(x_values[k]);
            }
            System.out.println(": " + resulting_function[i]);

        }

        for (int i = getSignificantMaxArraySize() - 1; i >= 0; i--) {
            rule_nr = rule_nr.multiply(BigInteger.valueOf(p));
            rule_nr = rule_nr.add(BigInteger.valueOf(resulting_function[i]));
        }

        return rule_nr;
    }

    private static int calculate_polynomial_function(int[] x_values, ArrayList<ArrayList<PolynomialObject>> p_list, int cell_states) {
        int result = 0;
//        x0x0x1x1 + x1x1 2 + x0x0 2 +  1 + 

        System.out.print("calculate_polynomial_function [");
        for(int x:x_values) {
            System.out.print(x);
        }
        System.out.print("] :");
        for (ArrayList<PolynomialObject> i : p_list) {
            int current_factor = 1;
            int current_quotient = 1;
            for (PolynomialObject j : i) {
                if (j.is_constant) {
                    current_factor *= j.index;
                    current_quotient *= j.quotient;
                } else {
                    current_factor *= x_values[j.index];
                }
            }
            System.out.print(current_factor + ", ");
            if(current_factor % current_quotient != 0) {
                System.out.println("EEEEEEEEEEEEEERRRRRRRRRRRORRRRRRRRRRR");
            }
            current_factor = current_factor / current_quotient;
            
            result += current_factor%cell_states;
        }
        System.out.println();
        while(result < 0) {
            result += cell_states;
        }
        result = result % cell_states;
        return result;
    }

    
    /*  
    TODO:
    min/maxNeighborhoodSize wird nicht gesetzt.
    muesste entweder gleich der neighborhoodSize gesetzt werden (konkrete Nachbarschaft angegeben) oder als Bereich konkret uebergeben werden...    
     */
    /**
     * Updates the function on basis of the significant function by extending the significant function over the holey neighborhood.
     * The final function will map all neighborhood configurations with the same significant neighborhood configuration on the same value that the significant function maps the significant neighborhood configuration.
     * @throws ALGORITHM.Function.Exception if an internal error occured
     */
    public static void updateFunction() throws Exception {
//        updateSignificantMaxArraySize(); ? TODO
        updateMaxArraySize();
        /*
        int[] significant_index_arr = new int[Neighborhood.getNeighborhoodSize()];
        int k = 0;
        for(int i = 0; i < Neighborhood.getSignificantNeighborhoodSize(); i++) {
            significant_index_arr[Neighborhood.getNeighborhood(k)] = k;
            k++;
        }
        */
        
        for (int i = 0; i < getMaxArraySize(); i++) {
            int result = 0;
            int significant_index = 1;
            int z = i;
            
            for (int j = 0; j < Neighborhood.getNeighborhoodSize(); j++) {
                // TODO?
                if (Neighborhood.getNeighborhood(j)) {
                    result += (z % Function.getNumberOfCellStates()) * significant_index;
                    significant_index *= Function.getNumberOfCellStates();
                }
                z /= Function.getNumberOfCellStates();
            }
            function[i] = getSignificantFunction(result);//currentSignificantFunction[result];
        }
    }

    /**
     *
     * @return
    // input: Ascending set
     */
    private static boolean next_rule_permutation(int rule[]) {
        for (int i = rule.length - 2,  j; i >= 0; i--) {
            if (rule[i + 1] > rule[i]) {
                for (j = rule.length - 1; rule[j] <= rule[i]; j--) {
                    ;
                }
                Misc.swap(rule, i, j);
                for (j = 1; j <= (rule.length - i) / 2; j++) {
                    Misc.swap(rule, i + j, rule.length - j);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Creates the next permutation of the rule. Only balanced rules will be generated, i.e. rules which have balanced result values (all other rules are not surjective or injective)
     * @return false if the last permutation has been reached
     * @throws ALGORITHM.Function.Exception if an internal error occured
     */
    public static boolean nextFunctionPermutation() throws Exception {
        if (!isTestAllRules()) {
            return false;
        }
        if (!next_rule_permutation(significantFunction)) {
            createStandardSignificantFunction();
            return false;
        } else {
            processedRulePermutations = processedRulePermutations.add(BigInteger.ONE);
        }
        initCurrentSignificantFunction(); //?
        //updateFunction();
        return true;
    }
    
    public static void initSignificantFunctionFromCurrent() throws Exception {
        significantFunction = currentSignificantFunction.clone();
        updateFunction();
    }

    public static void initCurrentSignificantFunction() throws Exception {
        currentSignificantFunction = significantFunction.clone();
        updateFunction();
    }

    /**
     * Iterates through all rule, cell state number, neighborhood and neighborhood size permutations
     * @return false if last permutation has been reached
     * @throws ALGORITHM.Function.Exception if there was an error creating the function on basis of the newly generated parameters
     */
    public static boolean nextParameterPermutation() throws Exception {

        if (Neighborhood.nextFunctionNeighborhoodPermutation()) {
            return true;
        }

        if (nextFunctionPermutation()) {
            return true;
        }

        if (nextCellStatePermutation()) {
            return true;
        }

// Fall ignoriert, falls manuelle Eingabe Nachbarschaft        
        if (Neighborhood.nextNeighborhoodPermutation()) {
            return true;
        }

        if (Neighborhood.nextSignificantNeighborhoodSizePermutation()) {
            return true;
        }
// new CA rule definition wird ignoriert        
        if (Neighborhood.nextNeighborhoodSizePermutation()) {
            return true;
        } else {
            return false;
        } // done
    }

    /**
     * Problem size is defined in this context by the upper limit of the size of the Bruijn product graph, which is (cell_states) to the power of [2*(neighborhood_size-1)]
     * @param cell_states number of cell states
     * @param neighborhood_size size of the neighborhood including holes
     * @return size of the problem in number of nodes
     */
    public static int getProblemSize(int cell_states, int neighborhood_size) {
        return Misc.pow(cell_states, 2 * neighborhood_size - 2);
    }

    /**
     * Problem size is defined in this context by the upper limit of the size of the Bruijn product graph, which is (cell states) to the power of [2*(neighborhood size-1)]
     * This function will use the current parameter settings
     * @see Function.getProblemSize()
     * @return problem size in number of nodes
     */
    public static int getCurrentProblemSize() {
        return getLastElementIndex() * getLastElementIndex();
    }

// --- statistics
    /**
     * Calculates the number of all permutations that an automated test will go through.
     * @return number of all permutations
     */
    public static BigInteger getNumberOfAllPermutations() {
        BigInteger zz = BigInteger.ZERO;
        for (int n = Neighborhood.getMinNeighborhoodSize(); n <= Neighborhood.getMaxNeighborhoodSize(); n++) {
            for (int s = Neighborhood.getMinSignificantNeighborhoodSize(); (s <= n) && (s <= Neighborhood.getMaxSignificantNeighborhoodSize()); s++) {
                BigInteger z = BigInteger.ZERO;
                for (int cells = getMinNumberOfCellStates(); cells <= getMaxNumberOfCellStates(); cells++) {
                    z = z.add(
                            getNumberOfAllRulePermutations(Misc.pow(cells, s)).multiply(
                            BigInteger.valueOf(getProblemSize(cells, n))).multiply(Neighborhood.getNumberOfAllRuleNeighborhoodPermutations(s)));
                }
                zz = zz.add(z.multiply(Neighborhood.getNumberOfAllNeighborhoodPermutations(s, n)));
            }
        }
        return zz;
    }

    public static BigInteger getNumberOfRemainingPermutations() {
        BigInteger zz = BigInteger.ZERO;
// remaining rule permutations        
        zz = getNumberOfRemainingRulePermutations().multiply(BigInteger.valueOf(getProblemSize(getNumberOfCellStates(), Neighborhood.getNeighborhoodSize())));
        zz = zz.add(Neighborhood.getNumberOfRemainingRuleNeighborhoodPermutations().multiply(BigInteger.valueOf(getProblemSize(getNumberOfCellStates(), Neighborhood.getNeighborhoodSize()))));
// remaining cell state permutations        
        {
            for (int cells = getNumberOfCellStates() + 1; cells <= getMaxNumberOfCellStates(); cells++) {
                zz = zz.add(
                        getNumberOfAllRulePermutations(Misc.pow(cells, Neighborhood.getSignificantNeighborhoodSize())).multiply(
                        BigInteger.valueOf(getProblemSize(cells, Neighborhood.getNeighborhoodSize()))).multiply(Neighborhood.getNumberOfAllRuleNeighborhoodPermutations(Neighborhood.getSignificantNeighborhoodSize())));
            }
        }
// remaining neighborhood permutations        
        {
            BigInteger z = BigInteger.ZERO;
            for (int cells = getNumberOfCellStates(); cells <= getMaxNumberOfCellStates(); cells++) {
                z = z.add(
                        getNumberOfAllRulePermutations(Misc.pow(cells, Neighborhood.getSignificantNeighborhoodSize())).multiply(
                        BigInteger.valueOf(getProblemSize(cells, Neighborhood.getNeighborhoodSize()))).multiply(Neighborhood.getNumberOfAllRuleNeighborhoodPermutations(Neighborhood.getSignificantNeighborhoodSize())));
            }
            zz = zz.add(z.multiply(Neighborhood.getNumberOfRemainingNeighborhoodPermutations()));
        }
// remaining significant neighborhood size permutations            
        for (int s = Neighborhood.getSignificantNeighborhoodSize() + 1; (s <= Neighborhood.getNeighborhoodSize()) && (s <= Neighborhood.getMaxSignificantNeighborhoodSize()); s++) {
            BigInteger z = BigInteger.ZERO;
            for (int cells = getMinNumberOfCellStates(); cells <= getMaxNumberOfCellStates(); cells++) {
                z = z.add(
                        getNumberOfAllRulePermutations(Misc.pow(cells, s)).multiply(
                        BigInteger.valueOf(getProblemSize(cells, Neighborhood.getNeighborhoodSize()))).multiply(Neighborhood.getNumberOfAllRuleNeighborhoodPermutations(s)));
            }
            zz = zz.add(z.multiply(Neighborhood.getNumberOfAllNeighborhoodPermutations(s, Neighborhood.getNeighborhoodSize())));
        }
// remaining neighborhood size permutations            
        for (int n = Neighborhood.getNeighborhoodSize() + 1; n <= Neighborhood.getMaxNeighborhoodSize(); n++) {
            for (int s = Neighborhood.getMinSignificantNeighborhoodSize(); (s <= n) && (s <= Neighborhood.getMaxSignificantNeighborhoodSize()); s++) {
                BigInteger z = BigInteger.ZERO;
                for (int cells = getMinNumberOfCellStates(); cells <= getMaxNumberOfCellStates(); cells++) {
                    z = z.add(
                            getNumberOfAllRulePermutations(Misc.pow(cells, s)).multiply(
                            BigInteger.valueOf(getProblemSize(cells, n))).multiply(Neighborhood.getNumberOfAllRuleNeighborhoodPermutations(s)));
                }
                zz = zz.add(z.multiply(Neighborhood.getNumberOfAllNeighborhoodPermutations(s, n)));
            }
        }
        return zz;
    }

    public static BigInteger getNumberOfRemainingRulePermutations() {
        if (!isTestAllRules()) {
            return BigInteger.ZERO;
        }
        return getNumberOfAllRulePermutations(Misc.pow(getNumberOfCellStates(), Neighborhood.getSignificantNeighborhoodSize())).subtract(processedRulePermutations);
    }

    /**
     * Calculates the number of all rule permutations on basis of maxArraySize (consisting of the neighborhood size and the cell states)
     * Only balanced rules are included in the calculation (non-balanced rules are non-surjective by default).
     * @return number of all rule permutations
     * @param max_significant_array_size The size of the significant array (consisting of the neighborhood size and the cell states)
     */
    public static BigInteger getNumberOfAllRulePermutations(int max_significant_array_size) {
        if (isTestAllRules() == false) {
            return BigInteger.ONE;
        }
        BigInteger permutations = BigInteger.ONE;
        for (int i = max_significant_array_size / 2 + 1; i <= max_significant_array_size; i++) {
            permutations = permutations.multiply(BigInteger.valueOf(i));
        }
        for (int i = 1; i <= max_significant_array_size / 2; i++) {
            permutations = permutations.divide(BigInteger.valueOf(i));
        }
        return permutations;
    }

    private static BigInteger getNeededMemorySize() {
        return getNeededMemorySize(getMaxNumberOfCellStates(), Neighborhood.getMaxNeighborhoodSize());
    }

    private static BigInteger getNeededMemorySize(int number_of_cell_states, int size_of_neighborhood) {
        BigInteger needed_memory = BigInteger.valueOf((long) java.lang.Math.pow((double) number_of_cell_states, (double) size_of_neighborhood));
        needed_memory = needed_memory.multiply(needed_memory.multiply(BigInteger.valueOf(SIZE_OF_NODE)));
        return needed_memory;
    }

    private static BigInteger getNeededTime(BigInteger permutations) {
        return permutations.divideAndRemainder(SPEED_FACTOR)[0]; // TODO run a test
    }

    /**
     * Calculates the maximal value for neighborhood size given the MAX_MEMORY_SIZE constant and the number of cell states
     * @return maximal value for neighborhood size
     */
    public static int calculateMaxNeighborhoodSize() {
        return (int) (Math.log(Math.sqrt((double) MAX_MEMORY_SIZE / (double) SIZE_OF_NODE)) / Math.log(getNumberOfCellStates())); // Faktor 2?
    }

    /**
     * Calculates the maximal value for neighborhood size given the MAX_MEMORY_SIZE constant and the neighborhood size
     * @return maximal value for cell states
     */
    public static int calculateMaxNumberOfCellStates() {
        return (int) (Math.pow(Math.sqrt((double) MAX_MEMORY_SIZE / (double) SIZE_OF_NODE), 1.0 / (double) Neighborhood.getNeighborhoodSize())); // Faktor 2?
    }

// -- parsing functions, interface to the GUI
    /**
     * Parses the string for a single number or a range (e.g. '2-4') and calls 
     * setNumberOfCellStatesRange to set the values and reset the function
     * @return -3 if there was an error parsing the string
     * @throws ALGORITHM.Function.Exception if there was an error creating the new function
     * @param number_of_cell_states_range_string String describing the cell range or a single cell number (e.g. '2-4' or '2')
     */
    public static int setNumberOfCellStatesRange(String number_of_cell_states_range_string) throws Exception, NumberFormatException {
        String cells[] = number_of_cell_states_range_string.split("[-]+");
        int cells_value[] = new int[cells.length];
        for (int i = 0; i < cells.length; i++) {
            cells_value[i] = Integer.parseInt(cells[i]);
        }
        return setNumberOfCellStatesRange(cells_value);
    }

    /**
     * Returns either the number of cell states to test or the range (e.g. '3-5')
     * @return String with the cell states to test
     */
    public static String getNumberOfCellStatesString() {
        if (getMinNumberOfCellStates() == getMaxNumberOfCellStates()) {
            return "" + getMinNumberOfCellStates();
        } else {
            return "" + getMinNumberOfCellStates() + "-" + getMaxNumberOfCellStates();
        }
    }

    /**
     * Generates an image filename on basis of the internal parameters and the properties of the test case (surjective / injective)
     * @param injective true if the current test case was tested positively on injectivity
     * @param surjective true if the current test case was tested positively on surjectivity
     * @return Proposed filename to save the graph information in
     * @throws ALGORITHM.Function.Exception if the significant rule number could not be created
     */
    public static String getImageFileName(boolean injective, boolean surjective) throws Exception {
        return "graphs/" + getSignificantRuleNumber() + "_" + Neighborhood.getNeighborhoodString() + "_" + getNumberOfCellStates() + (injective ? "_i" : "") + (surjective ? "_s" : "");
    }

    /**
     * Generate a file name on basis of the comitted parameter list
     * @param parameter Object list in the usual order (BigInteger, String, Integer, Integer, Integer, Boolean, Boolean, Boolean)
     * @return true if the current test case was tested positively on injectivity
     */
    public static String getImageFileName(Object[] parameter) {
        return "graphs/" + (BigInteger) parameter[0] + "_" + (String) parameter[1] + "_" + (Integer) parameter[4] + (((Boolean) parameter[5]) ? "_i" : "") + (((Boolean) parameter[6]) ? "_s" : "");
    }

    /**
     * Parses the string and returns the list of parameter values
     * @param parameters A line of numbers, seperated by commas. As the neighborhood field itself consists of commas it is encapsulated in "..."
     * @return List of objects with the appropriate types (BigInteger, String, Integer, Integer, Integer, Boolean, Boolean, Boolean)
     * @throws java.lang.NumberFormatException if the object list is corrupted and one item not corresponding to an object type
     */
    public static Object[] parseParametersAsString(String parameters) throws NumberFormatException {
        Object[] l = new Object[9];
        try {
            String t[] = parameters.split("\"");
            l[0] = new BigInteger(t[0].split(", ")[0]);
            l[1] = t[1];
            String t2[] = t[2].split(", ");
            l[2] = Integer.parseInt(t2[1]);
            l[3] = Integer.parseInt(t2[2]);
            l[4] = Integer.parseInt(t2[3]);
            l[5] = Boolean.parseBoolean(t2[4]);
            l[6] = Boolean.parseBoolean(t2[5]);
            // l[7] and l[8] must be filled by the GUI
            return l;
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
    }

    public static String getRemainingNeededTimeString() {
        return getNeededTimeString(getNumberOfRemainingPermutations());
    }

    public static String getTotalNeededTimeString() {
        return getNeededTimeString(getNumberOfAllPermutations());
    }

    /**
     * Calculates the time needed for the calculation and formats it in a readable format (hours, minutes, seconds etc.)
     * @return time needed for the calculation as a formatted string (e.g. "7m 3s")
     */
    public static String getNeededTimeString(BigInteger permutations) {
        String t = "";

        BigInteger time = getNeededTime(permutations);
        if (time.compareTo(BigInteger.ONE) < 0) {
            return "< 1 second";
        }

        BigInteger seconds = time.mod(BigInteger.valueOf(60));
        if (time.compareTo(BigInteger.valueOf(60)) < 0) {
            return "" + seconds + "s";
        }

        time = time.divide(BigInteger.valueOf(60));
        BigInteger minutes = time.mod(BigInteger.valueOf(60));
        if (time.compareTo(BigInteger.valueOf(60)) < 0) {
            return "" + minutes + "m " + seconds + "s";
        }

        time = time.divide(BigInteger.valueOf(60));
        BigInteger hours = time.mod(BigInteger.valueOf(24));
        if (time.compareTo(BigInteger.valueOf(24)) < 0) {
            return "" + hours + "h " + minutes + "m ";
        }

        time = time.divide(BigInteger.valueOf(24));
        BigInteger days = time.mod(BigInteger.valueOf(365));
        if (time.compareTo(BigInteger.valueOf(365)) < 0) {
            return "" + days + "d " + hours + "h " + minutes + "m ";
        } else {
            return "> 1 year";
        }
    }

    /**
     * Translates the value that 'getNeededMemorySize' returns to a string by converting it in the standard B/KB/MB/GB format.
     * @return Memory needed for the calculation as a formatted string (e.g. "7mb")
     */
    public static String getNeededMemorySizeString() {
        BigInteger needed_memory = getNeededMemorySize(getMaxNumberOfCellStates(), Neighborhood.getMaxNeighborhoodSize());
        String size_desc[] = {"Bytes", "KBytes", "MBytes", "GBytes", "TBytes"};
        int i = 1;
        while (needed_memory.compareTo(BigInteger.valueOf(1024)) >= 0) {
            needed_memory = needed_memory.divide(BigInteger.valueOf(1024));
            if ((needed_memory.compareTo(BigInteger.valueOf(1024)) < 0) || (i == 4)) {
                return "~" + needed_memory + " " + size_desc[i];
            }
            i++;
        }
        return "~" + needed_memory + " " + size_desc[0];
    }

// --- simple set/get functions
    private static int getMaxNumberOfCellStates() {
        return maxNumberOfCellStates;
    }

    private static int getMinNumberOfCellStates() {
        return minNumberOfCellStates;
    }

    private static void setSignificantFunction(int[] aSignificantFunction) {
        significantFunction = aSignificantFunction;
    }

    /**
     * Returns the index of the last element of the function TODO
     * @return index of the last element
     */
    public static int getLastElementIndex() {
        return lastElementIndex;
    }

    /**
     * Returns the number of possible neighborhood configurations, i.e. number of different cell states ^ size of neighborhood [last position - first position]
     * @return The number of possible neighborhood configurations
     */
    public static int getMaxArraySize() {
        return maxArraySize;
    }

    /**
     * Main function for the algorithm, applies the rule on the neighborhood configuration
     * @param i Configuration of the neighborhood
     * @return Next state of the cell with that neighborhood configuration
     * @throws ALGORITHM.Function.Exception if the index is out of range
     */
    public static int getFunction(int i) throws Exception {
        if ((i < 0) || (i >= function.length)) {
            throw new Exception("getFunction(): index " + i + " out of range (" + function.length + ")");
        }
        return function[i];
    }

    /**
     * Returns the number of possible neighborhood configurations of the significant function, i.e. number of different cell states ^ number of significant of neighborhood positions
     * @return The number of possible significant neighborhood configurations
     */
    public static int getSignificantMaxArraySize() {
        return significantMaxArraySize;
    }

    /**
     * Returns the value the significant function maps this significant neighborhood configuration to
     * @throws ALGORITHM.Function.Exception if the index is out of range
     * @param i index representing the significant neighborhood configuration
     * @return Next state of the cell with that significant neighborhood configuration
     */
    public static int getSignificantFunction(int i) throws Exception {
        if ((i < 0) || (i >= significantFunction.length)) {
            throw new Exception("getSignificantFunction(): index " + i + " out of range (" + function.length + ")");
        }
        return significantFunction[i];
    }

    /**
     * Returns the number of states each cell can be in. It's usually 2, smaller values are forbidden while larger values are possible.
     * @return Number of cell states
     */
    public static int getNumberOfCellStates() {
        return numberOfCellStates;
    }

    /**
     * Returns whether the user has activated to test all rules within the current significant neighborhood. E.g. Testing all rules on a neighborhood '0,1' would result in the test of 6 different rules.
     * @return whether the user has activated to test all rules
     */
    public static boolean isTestAllRules() {
        return testAllRules;
    }

    /**
     *
     */
    public static void setTestAllRules(boolean aTestAllRules) {
        testAllRules = aTestAllRules;

    }

    public static void setSingleTestCase(boolean test_single_case) {
        if (test_single_case) {
            Neighborhood.setTestAllNeighborhoods(false);
        }
    // TODO
    }

    public Function.CallBack getCallback() {
        return callback;
    }

    public static Object[] getParameterArray(boolean generate_graph) {
        Object[] t = new Object[6];
        t[0] = ALGORITHM.Function.getCurrentSignificantRuleNumber();
        t[1] = ALGORITHM.Neighborhood.getNeighborhoodString();
        t[2] = ALGORITHM.Neighborhood.getNeighborhoodSize();
        t[3] = ALGORITHM.Neighborhood.getSignificantNeighborhoodSize();
        t[4] = ALGORITHM.Function.getNumberOfCellStates();
        t[5] = new Boolean(generate_graph);        
        return t;
    }
    
}

;


