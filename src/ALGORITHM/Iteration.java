package ALGORITHM;

/**
 *
 * @author Clemens Lode, 1151459, University Karlsruhe (TH), clemens@lode.de
 */
import java.math.BigInteger;

public class Iteration {

    private final static int MAX_NEIGHBORHOOD_SIZE = 16;

    public static interface CallBack {

        /**
         * Increases the progress bar by a certain amount
         * @param my_progress amount to add to the progress bar
         */
        public void updateBar(int my_progress);

        /**
         * Let the progress bar jump to the next rule/neighborhood to test in order to ignore nodes of the Bruijn product graph that were not calculated because the calculation was already succesful.
         * @param my_progress maximal amount of steps to move 'progress' forward
         */
        public void forwardUpdateBar(int my_progress); // moves the progress bar forward to the next multiple, returns true if there was an actual change
// complete current calculation => round it up to the next X
    }
    /**
     * General callback routine in order to update the progress from within the algorithm framework (which doesn't have direct access to the GUI)
     */
    public static CallBack callback = null;
    /**
     * Maximal available memory. Program prints out a warning if the expected amount of needed memory (worst case, ~5*(k^n)^2)) reaches this value.
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
    public static boolean singleCalculation = true;

    public static void testIfSingleCalculation() {
        singleCalculation = getTotalCalculationSteps().compareTo(BigInteger.valueOf(Function.getCurrentProblemSize())) > 0;
    }
    /**
     * Upper boundary of number of remaining calculation steps
     */
    public static BigInteger progress = BigInteger.ZERO;
    /**
     * value of 'progress' where the last update has happened
     */
    public static BigInteger lastUpdate = BigInteger.ZERO;
    /**
     * time of the last update
     */
    public static long lastUpdateTime = 0;
    /**
     * update flag, true whenever the core thread finished a new calculation
     */
    public static boolean update = false;
    private static BigInteger processedCalculationSteps = BigInteger.ONE;
    private static BigInteger totalCalculationSteps;
    private static boolean testAllBalancedFunctions = false;
    private static boolean testAllNeighborhoodVariations = false;
    private static boolean testAllNeighborhoodPermutations = false;

    /**
     * initialize total number of steps, the update time and the number of calculations
     */
    public static void initialize() {
        updateTotalSteps();
        lastUpdateTime = java.lang.System.currentTimeMillis();
        testIfSingleCalculation();
    }

    /**
     * Returns the upper limit of calculation steps left, uses the parameters of the last time when updateTotalSteps was called
     * @return upper limit of calculation steps left
     * @see Function.updateTotalSteps()
     */
    public static BigInteger getTotalCalculationSteps() {
        return totalCalculationSteps;
    }

    /**
     * @return number of remaining calculation steps (total calculation steps - already processed calculation steps)
     */
    public static BigInteger getRemainingCalculationSteps() {
        return totalCalculationSteps.subtract(processedCalculationSteps);
    }

    /**
     * Updates totalCalculationSteps according to the current parameters
     * @see Function.getNumberOfAllPermutations()
     */
    public static void updateTotalSteps() {
        totalCalculationSteps = getNumberOfAllPermutations();
        progress = getTotalCalculationSteps();
        processedCalculationSteps = BigInteger.ZERO;
        lastUpdate = progress;
    }

    /**
     * Iterates through all rule, cell state number, neighborhood and neighborhood size variations and permutations
     * @return false if last permutation has been reached
     * @throws Exception if there was an error creating the function on basis of the newly generated parameters
     */
    public static boolean nextParameterConfiguration() throws Exception {
        processedCalculationSteps = processedCalculationSteps.add(BigInteger.valueOf(getProblemSize(CellStates.getNumberOfCellStates(), Neighborhood.getNeighborhoodSize())));
        // iterate through all balanced rules
        if (Function.nextFunctionPermutation()) {
            return true;
        }

        // all neighborhood permutation based on the current significant function
        if (Neighborhood.nextNeighborhoodPermutation()) {
            return true;
        }

        // iterate through all combinations
        if (Neighborhood.nextNeighborhoodVariation()) {
            ALGORITHM.Neighborhood.initNeighborhoodPermutations();
            return true;
        }

        if (CellStates.nextCellStatePermutation()) {
            return true;
        }

        if (Neighborhood.nextSignificantNeighborhoodSize()) {
            ALGORITHM.Neighborhood.initNeighborhoodPermutations();
            ALGORITHM.Neighborhood.initNeighborhoodVariations();
            return true;
        }

        if (Neighborhood.nextNeighborhoodSize()) {
            ALGORITHM.Neighborhood.initNeighborhoodPermutations();
            ALGORITHM.Neighborhood.initNeighborhoodVariations();

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
     * Calculates the number of all neighborhood permutations on basis of the parameters. 
     * As the neighborhood size defines the borders of defined neighborhood positions the actual number of permutations is (significant_neighborhood_size-2) over (neighborhood_size-2)
     * @param significant_neighborhood_size Number of significant positions of the neighborhood (e.g. 3 for '0, 2, 5')
     * @return Number of neighborhood permutations
     */
    public static int getNumberOfAllNeighborhoodPermutations(int significant_neighborhood_size) {
        if (isTestAllNeighborhoodPermutations() == false) {
            return 1;
        }
        return Misc.faculty(significant_neighborhood_size).intValue();
    }

    /**
     * Calculates the number of all neighborhood variations (e.g. 4 for '0,1,5' => '0,2,5' => '0,3,5' => '0,4,5')
     * @param significant_neighborhood_size size of the significant neighborhood
     * @param neighborhood_size size of the neighborhood
     * @return number of all variations of the neighborhood
     */
    public static int getNumberOfAllNeighborhoodVariations(int significant_neighborhood_size, int neighborhood_size) {
        if (isTestAllNeighborhoodVariations() == false || significant_neighborhood_size <= 2) {
            return 1;
        }
        return Misc.faculty(neighborhood_size - 2).divide(BigInteger.valueOf(significant_neighborhood_size - 2)).intValue();
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
                for (int cells = CellStates.getMinNumberOfCellStates(); cells <= CellStates.getMaxNumberOfCellStates(); cells++) {
                    zz = zz.add(
                            getNumberOfAllRulePermutations(Misc.pow(cells, s)).multiply(
                            BigInteger.valueOf(getProblemSize(cells, n))).multiply(
                            BigInteger.valueOf(getNumberOfAllNeighborhoodPermutations(s))).multiply(
                            BigInteger.valueOf(getNumberOfAllNeighborhoodVariations(s, n))));
                }
            }
        }
        return zz;
    }

    /**
     * Calculates the number of all rule permutations on basis of maxArraySize (consisting of the neighborhood size and the cell states)
     * Only balanced rules are included in the calculation (non-balanced rules are non-surjective by default).
     * @return number of all rule permutations
     * @param max_significant_array_size The size of the significant array (consisting of the neighborhood size and the cell states)
     */
    public static BigInteger getNumberOfAllRulePermutations(int max_significant_array_size) {

        if (isTestAllBalancedFunctions() == false) {
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

    /**
     * calculate the memory needed for each step
     * @return needed memory in bytes
     */
    public static BigInteger getNeededMemorySize() {
        BigInteger needed_memory = BigInteger.valueOf((long) java.lang.Math.pow((double) CellStates.getMaxNumberOfCellStates(), (double) Neighborhood.getMaxNeighborhoodSize()));
        needed_memory = needed_memory.multiply(needed_memory.multiply(BigInteger.valueOf(SIZE_OF_NODE)));
        return needed_memory;
    }

    /**
     * calculate the time needed to run all tests depending on the speed of the computer
     * @param steps number of steps to test
     * @return number of seconds to run through the number of steps
     */
    private static BigInteger getNeededTime(BigInteger steps) {
        return steps.divideAndRemainder(SPEED_FACTOR)[0]; // TODO run a test
    }

    /**
     * Calculates the maximal value for neighborhood size given the MAX_MEMORY_SIZE constant and the number of cell states
     * @return maximal value for neighborhood size
     */
    public static int calculateMaxNeighborhoodSize() {
        int max_size = (int) (Math.log(Math.sqrt((double) MAX_MEMORY_SIZE / (double) SIZE_OF_NODE)) / Math.log(CellStates.getNumberOfCellStates())); // Faktor 2?
        return max_size > MAX_NEIGHBORHOOD_SIZE ? MAX_NEIGHBORHOOD_SIZE : max_size;
    }

    /**
     * Calculates the maximal value for neighborhood size given the MAX_MEMORY_SIZE constant and the neighborhood size
     * @return maximal value for cell states
     */
    public static int calculateMaxNumberOfCellStates() {
        return (int) (Math.pow(Math.sqrt((double) MAX_MEMORY_SIZE / (double) SIZE_OF_NODE), 1.0 / (double) Neighborhood.getNeighborhoodSize())); // Faktor 2?
    }

    public static String getRemainingNeededTimeString() {
        return InOutput.getNeededTimeString(getNeededTime(getRemainingCalculationSteps()));
    }

    public static String getTotalNeededTimeString() {
        return InOutput.getNeededTimeString(getNeededTime(getNumberOfAllPermutations()));
    }

    /**
     * Returns whether the user has activated to test all rules within the current significant neighborhood. E.g. Testing all rules on a neighborhood '0,1' would result in the test of 6 different rules.
     * @return whether the user has activated to test all rules
     */
    public static boolean isTestAllBalancedFunctions() {
        return testAllBalancedFunctions;
    }

    public static void setTestAllBalancedFunctions(boolean test_all_balanced_functions) {
        testAllBalancedFunctions = test_all_balanced_functions;

    }

    // TODO?
    public static void setTestAllNeighborhoodVariations(boolean test_all_neighborhood_variations) {
        testAllNeighborhoodVariations = test_all_neighborhood_variations;
        if (test_all_neighborhood_variations) {
//            minSignificantNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
        //          maxSignificantNeighborhoodSize = getMaxNeighborhoodSize();
        //        minNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
        }
    }

    // TODO?
    public static void setTestAllNeighborhoodPermutations(boolean test_all_neighborhood_permutations) {
        testAllNeighborhoodPermutations = test_all_neighborhood_permutations;
        if (test_all_neighborhood_permutations) {
        //      minNeighborhoodSize = getNeighborhoodSize();
        //    maxNeighborhoodSize = getNeighborhoodSize();
        }             //?
    }

    /**
     * Returns whether the user has activated to test all neighborhoods within the given model parameters.
     * The significant neighborhood size determines the number of neighborhood positions while the neighborhood size determines the maximum distance between the first and last position. E.g. a neighborhood size of 5 and a significant neighborhood size of 3 would result in 10 neighborhoods to be tested.
     * @return whether the user has activated to test all neighborhoods within the given model parameters
     */
    public static boolean isTestAllNeighborhoodPermutations() {
        return testAllNeighborhoodPermutations;
    }

    public static boolean isTestAllNeighborhoodVariations() {
        return testAllNeighborhoodVariations;
    }

    public Iteration.CallBack getCallback() {
        return callback;
    }
}
