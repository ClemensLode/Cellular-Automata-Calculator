package ALGORITHM;

import java.math.BigInteger;

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
     * General Exception class marking internal errors of the code
     */
    public static class FunctionException extends Exception {
}
    
    /**
     * Minimum number of cell states. The cases 0 (empty set) or 1 cell state (always injective) are trivial.
     */
    public final static int MIN_NUMBER_OF_CELL_STATES = 2;
    private final static int MAX_NUMBER_OF_CELL_STATES = 256;
    
    /**
     * Minimal neighborhood size. Neighborhood sizes smaller than 2 are trivial.
     */
    public final static int MIN_NEIGHBORHOOD_SIZE = 2;
    private final static int MAX_NEIGHBORHOOD_SIZE = 16;
    
    /**
     * Maximal availible memory. Program prints out a warning if the expected amount of needed memory (worst case, ~5*(k^n)^2)) reaches this value.
     */
    public final static long MAX_MEMORY_SIZE = 1342177280;
    /**
     * Rough estimation of how many calculation steps per second the computer and algorithm can execute
     */
    public static BigInteger SPEED_FACTOR = BigInteger.valueOf(500000);
    
    private static int numberOfCellStates = MIN_NUMBER_OF_CELL_STATES;
    private static int minNumberOfCellStates = MIN_NUMBER_OF_CELL_STATES;
    private static int maxNumberOfCellStates = MIN_NUMBER_OF_CELL_STATES;
    
    private static int minNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    private static int maxNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    
    private static int minSignificantNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    private static int maxSignificantNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    
    private static int significantMaxArraySize = 0;
    
    private static boolean neighborhood[] = {true, true};
    private static boolean minNeighborhood[] = {true, true};

    private static int significantFunction[] = {};
    
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

    public static boolean singleCalculation = true;
    
    private static BigInteger processedRulePermutations = BigInteger.ONE;
    private static BigInteger processedNeighborhoodPermutations = BigInteger.ONE;
    
    private static boolean testAllRules = false;
    
    private static boolean testAllNeighborhoodPermutations = false;
    private static boolean testAllNeighborhoodSizes = false;
    private static boolean testAllSignificantNeighborhoodSizes = false;
    
    private static boolean isGenerateGraph = false;
    
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
        totalCalculationSteps = getNumberOfAllPermutations(); //TODO
    }
    
   
    
    public static void testIfSingleCalculation() {
        singleCalculation = (minNumberOfCellStates == maxNumberOfCellStates) && (minNeighborhoodSize == maxNeighborhoodSize) && (minSignificantNeighborhoodSize == maxSignificantNeighborhoodSize) && (!testAllNeighborhoodPermutations) && (!testAllNeighborhoodSizes) && (!testAllRules);
    }
    
    public static int setNumberOfCellStatesRange(int cells[]) throws FunctionException {
        int result = 0;
        int min_c = getMinNumberOfCellStates();
        int max_c = getMaxNumberOfCellStates();
        
        if(cells.length == 1) {
            result = setMinMaxNumberOfCellStates(cells[0], cells[0]);
        } else if(cells.length == 2) {
            result = setMinMaxNumberOfCellStates(cells[0], cells[1]);
        }
        else result = -3;
        
        if(result == 0)
        {
            initializeMinimalCellStatesValue(); // TODO?
        } else 
        {
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
    public static int setMinMaxNumberOfCellStates(int min_number_of_cell_states, int max_number_of_cell_states) {
        if(min_number_of_cell_states == getMinNumberOfCellStates() && max_number_of_cell_states == getMaxNumberOfCellStates())
            return 0;
        if(min_number_of_cell_states < MIN_NUMBER_OF_CELL_STATES ||
                max_number_of_cell_states < min_number_of_cell_states)
            return -1;
        if(getNeededMemorySize(max_number_of_cell_states, getMaxNeighborhoodSize()).compareTo(BigInteger.valueOf(MAX_MEMORY_SIZE)) > 0)
            return -2;

        minNumberOfCellStates = min_number_of_cell_states;
        maxNumberOfCellStates = max_number_of_cell_states;
        return 0;
    }
    
    /**
     * Cycles through all numbers of cell states (a simple incrementation), resets the rule number when reaching the 
     * @return false if the last permutation has been reached
     * @throws ALGORITHM.Function.FunctionException if an internal error occured when the new function was created
     */
    public static boolean nextCellStatePermutation() throws FunctionException {
        if(getNumberOfCellStates() == getMaxNumberOfCellStates())
        {
            setNumberOfCellStates(getMinNumberOfCellStates());
            return false;
        } else {
            setNumberOfCellStates(getNumberOfCellStates()+1);
            return true;
        }
    }

    public static void initializeMinimalCellStatesValue() throws FunctionException {
        numberOfCellStates = getMinNumberOfCellStates();
        createStandardSignificantFunction();
    }
    
    public static int setNumberOfCellStates(int number_of_cell_states) throws FunctionException {
        if((number_of_cell_states< MIN_NUMBER_OF_CELL_STATES) || (getNeededMemorySize(number_of_cell_states, getNeighborhoodSize()).compareTo(BigInteger.valueOf(MAX_MEMORY_SIZE))>0))
            return -1;
        if(numberOfCellStates == number_of_cell_states)
            return 0;
        numberOfCellStates = number_of_cell_states;
        createStandardSignificantFunction();
        return 0;
    }
    
/*    public static boolean nextSignificantNeighborhoodSizePermutation() throws FunctionException {
        if(get)
            
            neues neighborhood auf Basis des alten significantneighborhoodsize+1 kreieren
            
        if(neighborhood.equals(maxNeighborhood))
            return false;
        if(!next_neighborhood_permutation(neighborhood))
            return false;
        updateFunction();
        return true;
    }
    // Todo*/
    
    
    
// neighborhoods
    
// functions, highest level, needs neighborhoods and cell states
    private static void updateMaxArraySize() {
        lastElementIndex = pow(getNumberOfCellStates(), getNeighborhoodSize()-1);
        maxArraySize = lastElementIndex * getNumberOfCellStates();
        function = new int[getMaxArraySize()];
    }
    
    private static void updateSignificantMaxArraySize() {
        significantMaxArraySize = pow(getNumberOfCellStates(), getSignificantNeighborhoodSize());
        significantFunction = new int[getSignificantMaxArraySize()];
    }
    
    
    
    
    /**
     * Calculates a readable rule number on basis of the internal significant function and the parameters, i.e. the positions in the neighborhood that were not defined are ignored
     * @return Significant rule number, can become quite big
     */
    public static BigInteger getSignificantRuleNumber() {
        BigInteger rule_nr = BigInteger.ZERO;
        try {
        for(int i = getSignificantMaxArraySize()-1; i >= 0; i--) {
            rule_nr = rule_nr.multiply(BigInteger.valueOf(getNumberOfCellStates()));
            rule_nr = rule_nr.add(BigInteger.valueOf(getSignificantFunction(i)));
        }
        } catch(FunctionException e) {
            // doesn't happen'
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
        for(int i = getMaxArraySize()-1; i >= 0; i--) {
            rule_nr = rule_nr.multiply(BigInteger.valueOf(getNumberOfCellStates()));
            rule_nr = rule_nr.add(BigInteger.valueOf(getFunction(i)));
        }
        } catch(FunctionException e) {
            // doesn't happen'
        }
        return rule_nr;
    }
    
    /**
     * Creates a new significant function on basis of the provided rule number by succesively dividing it by the number of cell states.
     * @throws ALGORITHM.Function.FunctionException when the rule number is out of bounds
     * @param rule_nr the rule number as a BigInteger
     */
    public static void createSignificantFunction(BigInteger rule_nr) throws FunctionException {
        updateSignificantMaxArraySize();
        if((rule_nr.compareTo(BigInteger.ZERO)<0) || (rule_nr.compareTo(BigInteger.valueOf(getNumberOfCellStates()).pow(getSignificantMaxArraySize())) >= 0)) {
            throw new FunctionException();
        }
        for(int i = 0; i < getSignificantMaxArraySize(); i++) {
            significantFunction[i] = rule_nr.mod(BigInteger.valueOf(getNumberOfCellStates())).intValue();
            rule_nr = rule_nr.divide(BigInteger.valueOf(getNumberOfCellStates()));
        }
        updateFunction();
    }

    
    /**
     * Creates a standard rule (000....0111...1) on basis of the current parameters
     * @throws ALGORITHM.Function.FunctionException when creating / accessing the significant function (TODO)
     */
    public static void createStandardSignificantFunction() throws FunctionException
    {
        updateSignificantMaxArraySize();
        int k = 0;
        for(int j = 0; j < getNumberOfCellStates(); j++)
            for(int i = 0; i < getSignificantMaxArraySize()/getNumberOfCellStates(); i++) {
                significantFunction[i+j * getSignificantMaxArraySize()/getNumberOfCellStates()] = j;
            }
        processedRulePermutations = BigInteger.ONE;
        updateFunction();
    }

    
  /*  
    TODO:
        min/maxNeighborhoodSize wird nicht gesetzt.
        muesste entweder gleich der neighborhoodSize gesetzt werden (konkrete Nachbarschaft angegeben) oder als Bereich konkret uebergeben werden...    
*/    
    
    /**
     * Updates the function on basis of the significant function by extending the significant function over the holey neighborhood.
     * The final function will map all neighborhood configurations with the same significant neighborhood configuration on the same value that the significant function maps the significant neighborhood configuration.
     * @throws ALGORITHM.Function.FunctionException if an internal error occured
     */
     public static void updateFunction() throws FunctionException {
//        updateSignificantMaxArraySize(); ? TODO
        updateMaxArraySize();
        for(int i = 0; i < getMaxArraySize(); i++) {
            int result = 0;
            int significant_index = 1;
            int  z = i;
            for(int j = 0; j < getNeighborhoodSize(); j++) {
                if(getNeighborhood(j)) {
                    result += (z % Function.getNumberOfCellStates()) * significant_index;
                    significant_index *= Function.getNumberOfCellStates();
                }
                z/=Function.getNumberOfCellStates();
            }
            function[i] = getSignificantFunction(result);
        }
    }
        
    
    /**
     *
     * @return
     */
    private static boolean next_rule_permutation(int a[]) {
        for(int i = a.length-2, j; i >= 0; i--) {
            // TODO bedeutet das nicht, dass von oben nach unten gezaehlt wird?
            if (a[i+1] > a[i]) {
                for(j = a.length-1; a[j] <= a[i]; j--);
                swap(a, i, j);
                for(j = 1; j <= (a.length-i)/2; j++)
                    swap(a, i+j, a.length-j);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Creates the next permutation of the rule. Only balanced rules will be generated, i.e. rules which have balanced result values (all other rules are not surjective or injective)
     * @return false if the last permutation has been reached
     * @throws ALGORITHM.Function.FunctionException if an internal error occured
     */
    public static boolean nextFunctionPermutation() throws FunctionException {
        if(!testAllRules)
            return false;
        if(!next_rule_permutation(significantFunction))
        {
            createStandardSignificantFunction();
            return false;
        } else processedRulePermutations = processedRulePermutations.add(BigInteger.ONE);
        updateFunction();
        return true;
    }
    
    
    /**
     * Iterates through all rule, cell state number, neighborhood and neighborhood size permutations
     * @return false if last permutation has been reached
     * @throws ALGORITHM.Function.FunctionException if there was an error creating the function on basis of the newly generated parameters
     */
    public static boolean nextParameterPermutation() throws FunctionException {
        if(nextFunctionPermutation())
            return true;
        
        if(nextNeighborhoodPermutation())
            return true;
        
        if(nextCellStatePermutation())
            return true;
        else return false;
/*        if(nextSignificantNeighborhoodPermutation())
            return true;
        else
            return false; // done*/
        
        // TODO: minimal values initialisieren
    }
    
    /**
     * Creates the first and last neighborhood permutation depending on the current significantNeighborhoodSize and neighborhoodSize.
     * If only a single neighborhood is to be tested, set first and last permutation to the current permutation.
     * @param significant_neighborhood_size Number of significant positions of the neighborhood (e.g. 3 for '0, 2, 5')
     * @param neighborhood_size Size of the neighborhood including the holes
     * @return -1 if the significant_neighborhood_size was too small,
     * -2 if the significant_neighborhood_size was larger than the neighborhood_size
     * @throws ALGORITHM.Function.FunctionException if this function is called although the option to test all neighborhood permutations is deactivated
     */
// call whenever neighborhood size changed
    public static int createStandardNeighborhoodRange(int significant_neighborhood_size, int neighborhood_size) throws FunctionException {
        if((significant_neighborhood_size == getSignificantNeighborhoodSize()) && (neighborhood_size == getNeighborhoodSize()))
            return 0;
        if(significant_neighborhood_size < MIN_NEIGHBORHOOD_SIZE)
            return -1;
        if(significant_neighborhood_size > neighborhood_size)
            return -2;
        
        if(!isTestAllNeighborhoodPermutations())
            throw new FunctionException();
        int old_size = getSignificantNeighborhoodSize();
        minNeighborhood = new boolean[neighborhood_size];
        int i = 0;
        minNeighborhood[0] = true;
        for(i = 1; i < neighborhood_size - significant_neighborhood_size + 1; i++)
            minNeighborhood[i] = false;
        for(; i < neighborhood_size; i++)
            minNeighborhood[i] = true;
        
        if(setNeighborhood(minNeighborhood) != 0)
            return -1;
        if(getSignificantNeighborhoodSize() != old_size)
            createStandardSignificantFunction();
        updateFunction();
        return 0;
    }
    
    public static void initializeMinimalNeighborhoodValue() {
        neighborhood = minNeighborhood.clone();
        processedNeighborhoodPermutations = BigInteger.ZERO;
    }
    
    /**
     * Problem size is defined in this context by the upper limit of the size of the Bruijn product graph, which is (cell_states) to the power of [2*(neighborhood_size-1)]
     * @param cell_states number of cell states
     * @param neighborhood_size size of the neighborhood including holes
     * @return size of the problem in number of nodes
     */
    public static int getProblemSize(int cell_states, int neighborhood_size) {
        return (int)pow(cell_states, 2*neighborhood_size-2);
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
    
    private static boolean next_neighborhood_permutation(boolean a[]) {
        for(int i = a.length-3, j; i >= 1; i--) {
            if (a[i+1] && (!a[i])) {
                for(j = a.length-1; (a[i] || (a[j] == a[i])); j--);
                swap(a, i, j);
                for(j = 1; j <= (a.length-i)/2; j++)
                    swap(a, i+j, a.length-j);
                return true;
            }
        }
        return false;
    }
    
    
    
    /**
     * Generates the next permutation with the current given neighborhood size, i.e. the first and the last entry will always be a significant neighborhood position.
     * For example: (111001 -> 110101 -> 110011 -> 101101 -> 101011 -> 100111)
     * @return false if the last permutation has been reached
     * @throws ALGORITHM.Function.FunctionException if there was an error creating the function on basis of the newly generated parameters
     */
    public static boolean nextNeighborhoodPermutation() throws FunctionException {
        if(!next_neighborhood_permutation(neighborhood))
        {
            initializeMinimalNeighborhoodValue();
            return false;
        } else processedNeighborhoodPermutations = processedNeighborhoodPermutations.add(BigInteger.ONE);
        updateFunction();
        return true;
    }
    
// --- statistics
    /**
     * Calculates the number of all permutations that an automated test will go through.
     * @return number of all permutations
     */
    public static BigInteger getNumberOfAllPermutations() {
        BigInteger zz = BigInteger.ZERO;
//        for(int s = minSignificantNeighborhoodSize; s <= maxSignificantNeighborhoodSize; s++) {
            int s = getSignificantNeighborhoodSize(); // TODO
            for(int n = getMinNeighborhoodSize(); n <= getMaxNeighborhoodSize(); n++) {
                BigInteger z = BigInteger.ZERO;
                for(int cells = getMinNumberOfCellStates(); cells <= getMaxNumberOfCellStates(); cells++) {
                    z = z.add(
                        getNumberOfAllRulePermutations((int)pow(cells, s)).multiply(
                            BigInteger.valueOf(getProblemSize(cells, n)))
                         );
                }
                zz = zz.add(z.multiply(getNumberOfAllNeighborhoodPermutations(s, n)));
            }
  //      }
        return zz;
    }
    
    public static BigInteger getNumberOfRemainingPermutations() {
        BigInteger zz = BigInteger.ZERO;
        zz = getNumberOfRemainingRulePermutations().multiply(BigInteger.valueOf(getProblemSize(getNumberOfCellStates(), getNeighborhoodSize())));
        {
                BigInteger z = BigInteger.ZERO;
                for(int cells = getNumberOfCellStates()+1; cells <= getMaxNumberOfCellStates(); cells++) {
                    zz = zz.add(
                        getNumberOfAllRulePermutations((int)pow(cells, getSignificantNeighborhoodSize())).multiply(
                            BigInteger.valueOf(getProblemSize(cells, getNeighborhoodSize())))
                         );
                }
        }
        {
                BigInteger z = BigInteger.ZERO;
                for(int cells = getMinNumberOfCellStates(); cells <= getMaxNumberOfCellStates(); cells++) {
                    z = z.add(
                        getNumberOfAllRulePermutations((int)pow(cells, getSignificantNeighborhoodSize())).multiply(
                            BigInteger.valueOf(getProblemSize(cells, getNeighborhoodSize())))
                         );
                }
                zz = zz.add(z.multiply(getNumberOfRemainingNeighborhoodPermutations(getSignificantNeighborhoodSize(), getNeighborhoodSize())));        
        }
        
        
            for(int n = getNeighborhoodSize()+1; n <= getMaxNeighborhoodSize(); n++) {
                BigInteger z = BigInteger.ZERO;
                for(int cells = getMinNumberOfCellStates(); cells <= getMaxNumberOfCellStates(); cells++) {
                    z = z.add(
                        getNumberOfAllRulePermutations((int)pow(cells, getSignificantNeighborhoodSize())).multiply(
                            BigInteger.valueOf(getProblemSize(cells, n)))
                         );
                }
                zz = zz.add(z.multiply(getNumberOfAllNeighborhoodPermutations(getSignificantNeighborhoodSize(), n)));
            }        
            return zz;
    }
    
    public static BigInteger getNumberOfRemainingRulePermutations() {
        if(isTestAllRules() == false)
            return BigInteger.ZERO;
        return getNumberOfAllRulePermutations((int)pow(getNumberOfCellStates(), getSignificantNeighborhoodSize())).subtract(processedRulePermutations);
    }
    
    /**
     * Calculates the number of all rule permutations on basis of maxArraySize (consisting of the neighborhood size and the cell states)
     * Only balanced rules are included in the calculation (non-balanced rules are non-surjective by default).
     * @return number of all rule permutations
     * @param max_significant_array_size The size of the significant array (consisting of the neighborhood size and the cell states)
     */
    public static BigInteger getNumberOfAllRulePermutations(int max_significant_array_size) {
        if(isTestAllRules() == false)
            return BigInteger.ONE;
        BigInteger permutations = BigInteger.ONE;
        for(int i = max_significant_array_size/2+1; i <= max_significant_array_size; i++)
            permutations = permutations.multiply(BigInteger.valueOf(i));
        for(int i = 1; i <= max_significant_array_size/2; i++)
            permutations = permutations.divide(BigInteger.valueOf(i));
        return permutations;
    }
    
    
    /**
     * Calculates the number of the remaining neighborhood permutations on basis of the parameters and the processedNeighborhoodPermutations counter
     * As the neighborhood size defines the borders of defined neighborhood positions the actual number of permutations is (significant_neighborhood_size-2) over (neighborhood_size-2)
     * @param significant_neighborhood_size Number of significant positions of the neighborhood (e.g. 3 for '0, 2, 5')
     * @param neighborhood_size Size of the neighborhood including the holes
     * @return Number of neighborhood permutations
     */
    public static BigInteger getNumberOfRemainingNeighborhoodPermutations(int significant_neighborhood_size, int neighborhood_size) {
        if(isTestAllNeighborhoodPermutations() == false)
            return BigInteger.ONE;
        return getNumberOfAllNeighborhoodPermutations(significant_neighborhood_size, neighborhood_size).subtract(processedNeighborhoodPermutations);
    }

    /**
     * Calculates the number of all neighborhood permutations on basis of the parameters. 
     * As the neighborhood size defines the borders of defined neighborhood positions the actual number of permutations is (significant_neighborhood_size-2) over (neighborhood_size-2)
     * @param significant_neighborhood_size Number of significant positions of the neighborhood (e.g. 3 for '0, 2, 5')
     * @param neighborhood_size Size of the neighborhood including the holes
     * @return Number of neighborhood permutations
     */
    public static BigInteger getNumberOfAllNeighborhoodPermutations(int significant_neighborhood_size, int neighborhood_size) {
        if(isTestAllNeighborhoodPermutations() == false)
            return BigInteger.ONE;
        BigInteger permutations = BigInteger.ONE;

        
        for(int i = 1 + neighborhood_size - significant_neighborhood_size; i <= neighborhood_size - 2; i++)
            permutations = permutations.multiply(BigInteger.valueOf(i));
        for(int i = 2; i <= significant_neighborhood_size - 2; i++)
            permutations = permutations.divide(BigInteger.valueOf(i));
        
        
        return permutations;
    }
    
    
    private static BigInteger getNeededMemorySize() {
        return getNeededMemorySize(getMaxNumberOfCellStates(), getMaxNeighborhoodSize());
    }
    
    private static BigInteger getNeededMemorySize(int number_of_cell_states, int size_of_neighborhood) {
        BigInteger needed_memory = BigInteger.valueOf((long)java.lang.Math.pow((double)number_of_cell_states, (double)size_of_neighborhood));
        needed_memory = needed_memory.multiply(needed_memory.multiply(BigInteger.valueOf(5)));
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
        return (int)(Math.log(Math.sqrt((double)MAX_MEMORY_SIZE / 5.0)) / Math.log(getNumberOfCellStates())); // Faktor 2?
    }
    
    /**
     * Calculates the maximal value for neighborhood size given the MAX_MEMORY_SIZE constant and the neighborhood size
     * @return maximal value for cell states
     */
    public static int calculateMaxNumberOfCellStates() {
        return (int)(Math.pow(Math.sqrt((double)MAX_MEMORY_SIZE / 5.0), 1.0 / (double)getNeighborhoodSize())); // Faktor 2?
    }
    
    /**
     * Sets the upper and lower limit of the distance between two positions of the neighborhood.
     * A calculation will cycle through all distances from minNeighborhoodSize to maxNeighborhoodSize.
     * @param min_neighborhood_size lower limit of neighborhood size
     * @param max_neighborhood_size upper limit of neighborhood size
     * @return -1 if min_neighborhood_size is out of range
     * @return -2 if max_neighborhood_size is too large
     */
    public static int setMinMaxNeighborhoodSize(int min_neighborhood_size, int max_neighborhood_size) {
        if(max_neighborhood_size == getMaxNeighborhoodSize() && min_neighborhood_size == getMinNeighborhoodSize())
            return 0;
        if(min_neighborhood_size < MIN_NEIGHBORHOOD_SIZE || 
                max_neighborhood_size < min_neighborhood_size)
            return -1;
        if(getNeededMemorySize(getMaxNumberOfCellStates(), max_neighborhood_size).compareTo(BigInteger.valueOf(MAX_MEMORY_SIZE)) > 0)
            return -2;
        minNeighborhoodSize = min_neighborhood_size;
        maxNeighborhoodSize = max_neighborhood_size;
        return 0;
    }
    

    
    /**
     * Translates the committed significant neighborhood into a binary vector and assigns that to neighborhood.
     * @return -1 if the number of entries is to small ( < 2)
     * -2 if the neighborhood was not comitted in the appropriate ascending order
     * -3 if the neighborhood was too large
     * @param significant_neighborhood An array containing the neighborhood information in compact form (e.g. '0, 2, 5' instead of 'true, false, true, false, false, true')
     * @throws ALGORITHM.Function.FunctionException if there was an internal error creating the new function on basis of the new neighborhood
     */
    public static int setSignificantNeighborhood(int[] significant_neighborhood) throws FunctionException {
        if(testAllNeighborhoodPermutations)
            throw new FunctionException();

        if(significant_neighborhood.length < MIN_NEIGHBORHOOD_SIZE)
            return -1;
        
        // Test ordering TODO maybe remove
        for(int i = 1; i < significant_neighborhood.length; i++)
            if(significant_neighborhood[i-1] >= significant_neighborhood[i])
                return -2;
        int old_size = getSignificantNeighborhoodSize();
        int new_size = 1+significant_neighborhood[significant_neighborhood.length-1] - significant_neighborhood[0];
        if(getNeededMemorySize(getMaxNumberOfCellStates(), new_size).compareTo(BigInteger.valueOf(MAX_MEMORY_SIZE)) > 0)
            return -3;

        boolean new_neighborhood[] = new boolean[new_size];
        for(int i = 0; i < new_size; i++)
            new_neighborhood[i] = false;
        for(int i = 0; i < significant_neighborhood.length; i++)
            new_neighborhood[significant_neighborhood[i]] = true;

        if(setNeighborhood(new_neighborhood) != 0)
            return -4;
        minNeighborhood = neighborhood.clone();
        
        minNeighborhoodSize = maxNeighborhoodSize = minSignificantNeighborhoodSize = maxSignificantNeighborhoodSize = neighborhood.length;
// we have to create a new significant function when the significant neighborhood size has changed
        if(getSignificantNeighborhoodSize() != old_size)
            createStandardSignificantFunction();
        updateFunction();
    
        return 0;
    }
    
    private static int setNeighborhood(boolean[] new_neighborhood) throws FunctionException {
        neighborhood = new_neighborhood;
        processedNeighborhoodPermutations = BigInteger.ONE;
        return 0; // TODO?
    }
      
    
// -- parsing functions, interface to the GUI
    
    /**
     * Parses the string for a single number or a range (e.g. '2-4') and calls 
     * setNumberOfCellStatesRange to set the values and reset the function
     * @return -3 if there was an error parsing the string
     * @throws ALGORITHM.Function.FunctionException if there was an error creating the new function
     * @param number_of_cell_states_range_string String describing the cell range or a single cell number (e.g. '2-4' or '2')
     */
    public static int setNumberOfCellStatesRange(String number_of_cell_states_range_string) throws FunctionException, NumberFormatException {
        String cells[] = number_of_cell_states_range_string.split("[-]+");
        int cells_value[] = new int[cells.length];
        for(int i = 0; i < cells.length; i++)
            cells_value[i] = Integer.parseInt(cells[i]);
        return setNumberOfCellStatesRange(cells_value);
    }
    
    // TODO
    public static int createStandardNeighborhoodRange(String neighborhood_size_range_string) throws FunctionException, NumberFormatException {
        String sizes[] = neighborhood_size_range_string.split("[-]+");
        int sizes_value[] = new int[sizes.length];
            for(int i = 0; i < sizes.length; i++)
                sizes_value[i] = Integer.parseInt(sizes[i]);
            // 0 : ok, -1 : too many entries, -2 : entry out of range
            return setNeighborhoodSizeRange(sizes_value);
    }
    
    /*
     * -1: MaxSize < MinSize
     * -2: MaxSize > availible memory
     * -3: too few / too many entries
     * -4: significant_neighborhood size too small (can't happen)
     * -5: neighborhood_size smaller than significant_neighborhood
     */
    public static int setNeighborhoodSizeRange(int[] sizes) throws FunctionException {
        int result = 0;
        int min_c = getMinNeighborhoodSize();
        int max_c = getMaxNeighborhoodSize();
        
        if(sizes.length == 1) {
            result = setMinMaxNeighborhoodSize(sizes[0], sizes[0]);
        } else if(sizes.length == 2) {
            result = setMinMaxNeighborhoodSize(sizes[0], sizes[1]);
        }
        else result = -3;
        
        if(result == 0)
        {
            result = createStandardNeighborhoodRange(getSignificantNeighborhoodSize(), getMinNeighborhoodSize());
            if(result != 0)
            {
                result -= 2;
                setMinMaxNeighborhoodSize(min_c, max_c);
            }
        } else 
        {
            setMinMaxNeighborhoodSize(min_c, max_c);
        }
        return result;
    }
        
    
    /**
     * Returns either the number of cell states to test or the range (e.g. '3-5')
     * @return String with the cell states to test
     */
    public static String getNumberOfCellStatesString() {
        if(getMinNumberOfCellStates() == getMaxNumberOfCellStates())
            return "" + getMinNumberOfCellStates();
        else
            return "" + getMinNumberOfCellStates() + "-" + getMaxNumberOfCellStates();
    }
    
    /**
     * Creates a list of parameters for debug output on the screen
     * @return String containing several lines of debug code containing the values of all parameters
     * @throws ALGORITHM.Function.FunctionException if the significant function rule number could not be created
     */
    public static String getParameterString() throws FunctionException {
        String cell_states_string = "";
        String function_string = getRuleNumber().toString();
        String sig_function_string = getSignificantRuleNumber().toString();
        if(function_string.length() > 64)
            function_string = "<too large>";
        // TODO: evtl '\n's einfuegen
        
        if(getMinNumberOfCellStates() == getMaxNumberOfCellStates())
            cell_states_string = "" + getMinNumberOfCellStates();
        else cell_states_string = "" + getMinNumberOfCellStates() + "-" + getMaxNumberOfCellStates();
        
        String min_neighborhood = "";
        String max_neighborhood = "";
        for(int i = 0; i < minNeighborhood.length; i++)
            min_neighborhood += minNeighborhood[i]?"1":"0";
        
        return "FUNCTION: " + getRuleNumber() + "\n" +
                "SIGNIFICANT FUNCTION: " + getSignificantRuleNumber() + "\n" +
                "NUMBER OF CELL STATES: " + cell_states_string + "\n" +
                "NEIGHBORHOOD SIZE: " + getNeighborhoodSize() + "\n" +
                "SIGNIFICANT NEIGHBORHOOD SIZE: " + getSignificantNeighborhoodSize() + "\n" +
                "MAX ARRAY SIZE: " + getMaxArraySize() + "\n" +
                "SIGNIFICANT MAX ARRAY SIZE: " + significantMaxArraySize + "\n" + 
                "MIN_NEIGHBORHOOD: " + min_neighborhood + "\n" + 
                "MIN/MAX NEIGHBORHOOD SIZE: " + minNeighborhoodSize + "/" + maxNeighborhoodSize + "\n" + 
                "MIN/MAX CELL STATES: " + minNumberOfCellStates + "/" + maxNumberOfCellStates + "\n" +
                "NEIGHBORHOOD PERMUTATIONS: " + getNumberOfAllNeighborhoodPermutations(getSignificantNeighborhoodSize(), getNeighborhoodSize()) + "\n" + 
                "MAX SINGLE PROBLEM SIZE: " + getProblemSize(getMaxNumberOfCellStates(), getMaxNeighborhoodSize()) + "\n" + 
                "CURRENT TOTAL CALCULATION STEPS: " + getNumberOfAllPermutations().toString() + "\n" +
                "TOTAL CALCULATION STEPS: " + getTotalCalculationSteps();
    }
    
    /**
     * Generates an image filename on basis of the internal parameters and the properties of the test case (surjective / injective)
     * @param injective true if the current test case was tested positively on injectivity
     * @param surjective true if the current test case was tested positively on surjectivity
     * @return Proposed filename to save the graph information in
     * @throws ALGORITHM.Function.FunctionException if the significant rule number could not be created
     */
    public static String getImageFileName(boolean injective, boolean surjective) throws FunctionException {
        return "" + getSignificantRuleNumber() + "_" + getNeighborhoodString() + "_" + getNumberOfCellStates() + (injective?"_i":"") + (surjective?"_s":"");
    }
    
    /**
     * Generate a file name on basis of the comitted parameter list
     * @param parameter Object list in the usual order (BigInteger, String, Integer, Integer, Integer, Boolean, Boolean, Boolean)
     * @return true if the current test case was tested positively on injectivity
     */
    public static String getImageFileName(Object[] parameter) {
        return "" + (BigInteger)parameter[0] + "_" + (String)parameter[1] + "_" + (Integer)parameter[4] + (((Boolean)parameter[5])?"_i":"") + (((Boolean)parameter[6])?"_s":"");
    }
    
    /**
     * Generates a representation of the current neighborhood in compact, readable form
     * @return Neighborhood in compact form (e.g. '0, 2, 3' instead of 'true, false, true, true')
     */
    public static String getNeighborhoodString() {
        String neighborhood_string = "0";
        for(int i = 1; i < getNeighborhoodSize(); i++)
            if(getNeighborhood(i))
                neighborhood_string += ", " + i;
        return neighborhood_string;
    }
    
    /**
     * Parses the string and returns the list of parameter values
     * @param parameters A line of numbers, seperated by commas. As the neighborhood field itself consists of commas it is encapsulated in "..."
     * @return List of objects with the appropriate types (BigInteger, String, Integer, Integer, Integer, Boolean, Boolean, Boolean)
     * @throws java.lang.NumberFormatException if the object list is corrupted and one item not corresponding to an object type
     */
    public static Object[] parseParametersAsString(String parameters) throws NumberFormatException {
        Object[] l = new Object[8];
        try{
            String t[] = parameters.split("\"");
            l[0] = new BigInteger(t[0].split(", ")[0]);
            l[1] = t[1];
            String t2[] = t[2].split(", ");
            l[2] = Integer.parseInt(t2[1]);
            l[3] = Integer.parseInt(t2[2]);
            l[4] = Integer.parseInt(t2[3]);
            l[5] = Boolean.parseBoolean(t2[4]);
            l[6] = Boolean.parseBoolean(t2[5]);
            l[7] = Boolean.parseBoolean(t2[6]);
            return l;
        } catch(NumberFormatException e){
            throw new NumberFormatException();
        }
    }
    
    
    /**
     * Parses the neighborhood string that the user has entered (e.g. "0, 1, 5, 7") and transforms it into an array of ints (e.g. {0, 1, 5, 7})
     * @throws java.lang.NumberFormatException if the numbers could not be parsed (e.g. the user has entered literals instead of numbers)
     * @return array of int containing the significant neighborhood (e.g. {0, 1, 5, 7})
     * @param significant_neighborhood_string string with the significant neighborhood (e.g. "0,1, 5, 7")
     */
    public static int[] parseSignificantNeighborhoodString(String significant_neighborhood_string) throws NumberFormatException {
        String neighborhoods[] = significant_neighborhood_string.split("[, .-]+");
        int significant_neighborhood[] = new int[neighborhoods.length];
        
        for(int i = 0; i < neighborhoods.length; i++) {
            try {
                significant_neighborhood[i] = Integer.parseInt(neighborhoods[i]);
            } catch(NumberFormatException e) {
                throw new NumberFormatException();
            }
        }
        return significant_neighborhood;
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
        if(time.compareTo(BigInteger.ONE) < 0)
            return "< 1 second";
        
        BigInteger seconds = time.mod(BigInteger.valueOf(60));
        if(time.compareTo(BigInteger.valueOf(60)) < 0)
            return "" + seconds + "s";

        time = time.divide(BigInteger.valueOf(60));
        BigInteger minutes = time.mod(BigInteger.valueOf(60));
        if(time.compareTo(BigInteger.valueOf(60)) < 0)
            return "" + minutes + "m " + seconds + "s";
        
        time = time.divide(BigInteger.valueOf(60));
        BigInteger hours = time.mod(BigInteger.valueOf(24));
        if(time.compareTo(BigInteger.valueOf(24)) < 0)
            return "" + hours + "h " + minutes + "m ";
        
        time = time.divide(BigInteger.valueOf(24));
        BigInteger days = time.mod(BigInteger.valueOf(365));
        if(time.compareTo(BigInteger.valueOf(365)) < 0)
            return "" + days + "d " + hours + "h " + minutes + "m ";
        else return "> 1 year";
    }
    
    
    /**
     * Translates the value that 'getNeededMemorySize' returns to a string by converting it in the standard B/KB/MB/GB format.
     * @return Memory needed for the calculation as a formatted string (e.g. "7mb")
     */
    public static String getNeededMemorySizeString() {
        BigInteger needed_memory = getNeededMemorySize(getNumberOfCellStates(), getNeighborhoodSize());
        String size_desc[] = {"Bytes", "KBytes", "MBytes", "GBytes", "TBytes"};
        int i = 1;
        while(needed_memory.compareTo(BigInteger.valueOf(1024))>=0)
        {
            needed_memory = needed_memory.divide(BigInteger.valueOf(1024));
            if((needed_memory.compareTo(BigInteger.valueOf(1024))<0) || (i == 4))
                return "" + needed_memory + " " + size_desc[i];
            i++;
        }
        return "" + needed_memory + " " + size_desc[0];
    }
    
    
    
    
// --- simple set/get functions
    
    private static int getMaxNumberOfCellStates() {
        return maxNumberOfCellStates;
    }
    
    private static int getMinNumberOfCellStates() {
        return minNumberOfCellStates;
    }
    
    private static boolean getNeighborhood(int i) {
        return neighborhood[i];
    }

    private static int getMinNeighborhoodSize() {
        return minNeighborhoodSize;
    }
    
    
    private static int getMaxNeighborhoodSize() {
        return maxNeighborhoodSize;
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
     * @throws ALGORITHM.Function.FunctionException if the index is out of range
     */
    public static int getFunction(int i) throws FunctionException {
        if((i<0) || (i >= function.length))
            throw new FunctionException();            
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
     * @throws ALGORITHM.Function.FunctionException if the index is out of range
     * @param i index representing the significant neighborhood configuration
     * @return Next state of the cell with that significant neighborhood configuration
     */
    public static int getSignificantFunction(int i) throws FunctionException {
        if((i < 0 ) || (i >= significantFunction.length))
            throw new FunctionException();
        return significantFunction[i];
    }
    
    /**
     * Returns the size of the whole neighborhood, including "holes" in the neighborhood that the user has entered (e.g. '0, 5, 10' would result in a neighborhood size of 11)
     * @return Range of the neighborhood [last position - first position]
     */
    public static int getNeighborhoodSize() {
        return neighborhood.length;
    }
    
    /**
     * Returns the number of cells that one cell uses when applying the rule, including its own cell, i.e. a neighborhood of '0, 5, 10' would result in a significant neighborhood size of 3
     * @return The number of cells that one cell uses when applying the rule
     */
    public static int getSignificantNeighborhoodSize() {
        int n = 0;
        for(int i = 0; i < getNeighborhoodSize(); i++)
            if(neighborhood[i])
                n++;
        return n;
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
    
    /**
     * Returns if a .viz file should be created
     * @see Function.setIsGenerateGraph(boolean)
     * @return true if a .viz file should be created
     */
    public static boolean isIsGenerateGraph() {
        return isGenerateGraph;
    }
    
    /**
     * Sets if PrintBruijnProductGraph instead of FastBruijnProductGraph should be used, the Bruijn product graph be saved and then written into a .viz file readable by the external program GraphViz
     * @param aIsGenerateGraph true if a viz file should be created
     */
    public static void setIsGenerateGraph(boolean aIsGenerateGraph) {
        isGenerateGraph = aIsGenerateGraph;
    }
    /**
     * Returns whether the user has activated to test all neighborhoods within the given model parameters.
     * The significant neighborhood size determines the number of neighborhood positions while the neighborhood size determines the maximum distance between the first and last position. E.g. a neighborhood size of 5 and a significant neighborhood size of 3 would result in 10 neighborhoods to be tested.
     * @return whether the user has activated to test all neighborhoods within the given model parameters
     */
    public static boolean isTestAllNeighborhoodPermutations() {
        return testAllNeighborhoodPermutations;
    }
    
    public static void setSingleTestCase(boolean test_single_case) {
        setTestAllNeighborhoodPermutations(!test_single_case);
        setTestAllNeighborhoodSizesAndPermutations(!test_single_case);
    }
    
    public static void setTestAllNeighborhoodPermutations(boolean test_all_neighborhood_permutations) {
        testAllNeighborhoodPermutations = test_all_neighborhood_permutations;
        if(test_all_neighborhood_permutations)
        {
            minNeighborhoodSize = neighborhood.length;
            maxNeighborhoodSize = neighborhood.length;
        }            
    }
    
    public static boolean isTestAllNeighbohoodSizes() {
        return (minNeighborhoodSize != maxNeighborhoodSize);
    }
    
    public static boolean isTestAllSignificantNeighborhoodSizes() {
        return (minSignificantNeighborhoodSize != maxSignificantNeighborhoodSize);
    }
    
    public static void setTestAllNeighborhoodSizesAndPermutations(boolean test_all_neighborhood_sizes_and_permutations) {
        setTestAllNeighborhoodPermutations(test_all_neighborhood_sizes_and_permutations);
        if(test_all_neighborhood_sizes_and_permutations)
        {
            minSignificantNeighborhoodSize = minNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
            maxSignificantNeighborhoodSize = maxNeighborhoodSize = getNeighborhoodSize();
        }
    }
    
    public static void setTestAllNeighborhoods(boolean test_all_neighborhoods) {
        setTestAllNeighborhoodSizesAndPermutations(test_all_neighborhoods);
        testAllSignificantNeighborhoodSizes = test_all_neighborhoods;
        if(test_all_neighborhoods)
        {
            minSignificantNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
            maxSignificantNeighborhoodSize = MAX_NEIGHBORHOOD_SIZE; // TODO
        }
    }
    
    // --- MISC functions
    private static int pow(int base, int exp) {
        int l = 1;
        for(int i = 0; i < exp; i++)
            l*=base;
        return l;
    }
    
    private static long powl(int base, int exp) {
        long l = 1;
        for(int i = 0; i < exp; i++)
            l*=base;
        return l;
    }
    
    
    
    private static void swap(int a[], int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    private static void swap(boolean a[], int i, int j) {
        boolean temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public Function.CallBack getCallback() {
        return callback;
    }
    
    
    
};


