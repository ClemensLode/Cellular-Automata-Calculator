package ALGORITHM;

/**
 *
 * @author Clemens Lode, 1151459, University Karlsruhe (TH), clemens@lode.de
 */

public class CellStates {
    /**
     * Minimal number of cell states. The cases 0 (empty set) or 1 cell state (always injective) are trivial.
     */
    private final static int MIN_NUMBER_OF_CELL_STATES = 2;
    
    /**
     * Maximal number of cell states
     */
    private final static int MAX_NUMBER_OF_CELL_STATES = 7;

    /**
     * current number of cell states
     */
    private static int numberOfCellStates = MIN_NUMBER_OF_CELL_STATES;
    
    /**
     * lower boundary of cell states for automatic testing
     */
    private static int minNumberOfCellStates = MIN_NUMBER_OF_CELL_STATES;
    
    /**
     * upper boundary of cell states for automatic testing
     */
    private static int maxNumberOfCellStates = MIN_NUMBER_OF_CELL_STATES;    
    
// --- simple set/get functions
    /**
     * Returns the number of states each cell can be in. It's usually 2, smaller values are forbidden while larger values are possible.
     * @return Number of cell states
     */
    public static final int getNumberOfCellStates() {
        return numberOfCellStates;
    }
    
    /**
     * Upper boundary of number of cell states to test
     * @return maxNumberOfCellStates
     */
    public static int getMaxNumberOfCellStates() {
        return maxNumberOfCellStates;
    }

    /**
     * Lower boundary of number of cell states to test
     * @return minNumberOfCellStates
     */     
    public static int getMinNumberOfCellStates() {
        return minNumberOfCellStates;
    }    

    /**
     * Checks the length of the parameter and calls setMinMaxNumberOfCellStates
     * @param cells array containing either a single element or two elements with the number of cell states range to test
     * @return see setMinMaxNumberOfCellStates
     * @throws java.lang.Exception if the array did not contain one or two elements
     */
    public static boolean setNumberOfCellStatesRange(int cells[]) throws Exception {
        if (cells.length == 1) {
            return setMinMaxNumberOfCellStates(cells[0], cells[0]);
        } else if (cells.length == 2) {
            return setMinMaxNumberOfCellStates(cells[0], cells[1]);
        } else {
            throw new Exception("Invalid format of cell states range (more than two entries)");
        }
    }

    /**
     * Sets the lower limit of cell states to test. If minNumberOfCellStates == maxNumberOfCellStates only a single number of cell states will be tested.
     * @param min_number_of_cell_states Lower limit of cell states to test
     * @param max_number_of_cell_states Upper limit of cell states to test
     * @return -1 if the value is out of range, 0 otherwise
     * @throws java.lang.Exception if cell states are out of range
     */
    public static boolean setMinMaxNumberOfCellStates(int min_number_of_cell_states, int max_number_of_cell_states) throws Exception {
        if (min_number_of_cell_states == getMinNumberOfCellStates() && max_number_of_cell_states == getMaxNumberOfCellStates()) {
            return false;
        }
        
        if(max_number_of_cell_states < min_number_of_cell_states) {
            throw new Exception("Upper limit of number of cell states (" + max_number_of_cell_states + " < " + min_number_of_cell_states + ") below lower limit of cell states.");
        }
        if (min_number_of_cell_states < MIN_NUMBER_OF_CELL_STATES) {
            throw new Exception("Number of cell states (" + min_number_of_cell_states + " < " + MIN_NUMBER_OF_CELL_STATES + ") out of range.");
        }
        if (max_number_of_cell_states > MAX_NUMBER_OF_CELL_STATES) {
//                max_number_of_cell_states > calculateMaxNumberOfCellStates()) { TODO
            throw new Exception("Upper limit of number of cell states (" + max_number_of_cell_states + " > " + MAX_NUMBER_OF_CELL_STATES + ") too large.");
        }

        minNumberOfCellStates = min_number_of_cell_states;
        maxNumberOfCellStates = max_number_of_cell_states;
        numberOfCellStates = getMinNumberOfCellStates();   
        
        Function.createStandardSignificantFunction();
        Function.updateFunction();
        return true;
    }

    /**
     * Cycles through all numbers of cell states (a simple incrementation), resets the rule number when reaching the 
     * @return false if the last permutation has been reached
     * @throws Exception if an internal error occured when the new function was created
     */
    public static boolean nextCellStatePermutation() throws Exception {
        if (getNumberOfCellStates() == getMaxNumberOfCellStates()) {
            initializeMinimalCellStatesValue();
            return false;
        } else {
            setNumberOfCellStates(getNumberOfCellStates() + 1);
            return true;
        }
    }

    /**
     * Resets number of cell states to the minimal value and creates a new function
     * @return false if number of cell states was already equal the minimal number of cell states, true otherwise
     * @throws java.lang.Exception if there was an error creating the significant function
     */
    public static boolean initializeMinimalCellStatesValue() throws Exception {
        if (numberOfCellStates == getMinNumberOfCellStates()) {
            return false;
        } //new
        numberOfCellStates = getMinNumberOfCellStates();
        Function.createStandardSignificantFunction();
        return true;
    }

    /**
     * Sets the number of cell states
     * @param number_of_cell_states desired number of cell states
     * @return false if number of cell states was already set to this value, true otherwise
     * @throws java.lang.Exception if the number of cell states is out of range
     */     
    public static boolean setNumberOfCellStates(int number_of_cell_states) throws Exception {
        if ((number_of_cell_states < MIN_NUMBER_OF_CELL_STATES) || (number_of_cell_states > MAX_NUMBER_OF_CELL_STATES)) { 
            // TODO replace with a number based on problem size / available memory
            throw new Exception("Number of cell states (" + number_of_cell_states + ") out of range.");
        }
        if (numberOfCellStates == number_of_cell_states) {
            return false;
        }
        numberOfCellStates = number_of_cell_states;
        Function.createStandardSignificantFunction();
        return true;
    }    

    /**
     * Parses the string for a single value or a range (e.g. '2-4') and calls setNumberOfCellStatesRange
     * @param number_of_cell_states_range_string String containing the number or the range
     * @return see setNumberOfCellStatesRange
     * @throws java.lang.Exception if there was an error parsing the cell states string
     */
    public static boolean setNumberOfCellStatesRange(String number_of_cell_states_range_string) throws Exception {
        String cells[] = number_of_cell_states_range_string.split("[-]+");
        int cells_value[] = new int[cells.length];
        try {
            for (int i = 0; i < cells.length; i++) {
                cells_value[i] = Integer.parseInt(cells[i]);
            }
        } catch(NumberFormatException e) {
            throw new Exception("Wrong number format, please enter only numerals into the Cell States field");
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
}
