/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ALGORITHM;

/**
 *
 * @author Clemens Lode
 */

import java.math.BigInteger;

public class Neighborhood {
    
    /**
     * Minimal neighborhood size. Neighborhood sizes smaller than 2 are trivial.
     */
    public final static int MIN_NEIGHBORHOOD_SIZE = 2;
    private final static int MAX_NEIGHBORHOOD_SIZE = 16;
    
    private static int minNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    private static int maxNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    
    private static int minSignificantNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    private static int maxSignificantNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    
//  TODO  
    private static boolean neighborhood[] = {true, true};
    private static boolean minNeighborhood[] = {true, true};

    private static int significantNeighborhood[] = {0,1};
    
    private static BigInteger processedNeighborhoodPermutations = BigInteger.ONE;
    private static BigInteger processedRuleNeighborhoodPermutations = BigInteger.ONE;
    
    private static boolean allowDisorderedNeighborhoodPermutations = false;

        
    private static boolean testAllNeighborhoodPermutations = false;
    private static boolean testAllNeighborhoodSizes = false;
    private static boolean testAllSignificantNeighborhoodSizes = false;
    
    
    
    public static boolean nextNeighborhoodSizePermutation() throws Exception {
        if(!isTestAllNeighborhoodSizes())
            return false;
        if(getNeighborhoodSize() == getMaxNeighborhoodSize())
        {
            createStandardNeighborhoodRange(getSignificantNeighborhoodSize(), getMinNeighborhoodSize());
            return false;
        } else {
            createStandardNeighborhoodRange(getSignificantNeighborhoodSize(), getNeighborhoodSize()+1);
            return true;
        }
    }

  
    // permutates the neighborhood, e.g. 0,1,2 -> 0,2,1 -> 1,0,2 -> ...
    public static boolean nextFunctionNeighborhoodPermutation() throws Exception {
        if(!isAllowDisorderedNeighborhoodPermutations())
            return false;
        if(!Function.nextNeighborhoodRulePermutation(significantNeighborhood))
        {
            updateSignificantNeighborhood();
            Function.initSignificantFunctionFromCurrent();
            processedRuleNeighborhoodPermutations = BigInteger.ONE;
            return false;
        } else {
            processedRuleNeighborhoodPermutations = processedRuleNeighborhoodPermutations.add(BigInteger.ONE);
        }
        Function.updateFunction();
        return true;
    }    
     
    
    public static boolean nextSignificantNeighborhoodSizePermutation() throws Exception  {
        if(!isTestAllSignificantNeighborhoodSizes())
            return false;
        if((getSignificantNeighborhoodSize() >= getNeighborhoodSize()) || (getSignificantNeighborhoodSize() >= getMaxSignificantNeighborhoodSize()))
        {
//            System.out.println(getSignificantNeighborhoodSize() + " : " + getNeighborhoodSize() + " : " + getMaxSignificantNeighborhoodSize());
            createStandardNeighborhoodRange(minSignificantNeighborhoodSize, getNeighborhoodSize());
            return false;
        } else {
            createStandardNeighborhoodRange(getSignificantNeighborhoodSize()+1, getNeighborhoodSize());
            return true;
        }
    }    
    
    /**
     * Creates the first and last neighborhood permutation depending on the current significantNeighborhoodSize and neighborhoodSize.
     * If only a single neighborhood is to be tested, set first and last permutation to the current permutation.
     * @param significant_neighborhood_size Number of significant positions of the neighborhood (e.g. 3 for '0, 2, 5')
     * @param neighborhood_size Size of the neighborhood including the holes
     * @return -1 if the significant_neighborhood_size was too small,
     * -2 if the significant_neighborhood_size was larger than the neighborhood_size
     * @throws ALGORITHM.Function.Exception if this function is called although the option to test all neighborhood permutations is deactivated
     */
// call whenever neighborhood size changed
    public static int createStandardNeighborhoodRange(int significant_neighborhood_size, int neighborhood_size) throws Exception {
        if((significant_neighborhood_size == getSignificantNeighborhoodSize()) && (neighborhood_size == getNeighborhoodSize()))
            return 0;
        if(significant_neighborhood_size < MIN_NEIGHBORHOOD_SIZE)
            return -1;
        if(significant_neighborhood_size > neighborhood_size) {
            //return -2;
            neighborhood_size = significant_neighborhood_size;
        }
        
        if(!isTestAllNeighborhoodPermutations()) {
            throw new Exception("createStandardNeighborhoodRange() was called although the option to test all neighborhood permutations is deactivated");
        }            
        int old_size = getSignificantNeighborhoodSize();
        minNeighborhood = new boolean[neighborhood_size];
        int i = 0;
        minNeighborhood[0] = true;
        for(i = 1; i < neighborhood_size - significant_neighborhood_size + 1; i++)
            minNeighborhood[i] = false;
        for(; i < neighborhood_size; i++)
            minNeighborhood[i] = true;
/*        for(; i < significant_neighborhood_size-1; i++)
            minNeighborhood[i] = true;
        for(; i < neighborhood_size - 1; i++)
            minNeighborhood[i] = false;
        minNeighborhood[neighborhood_size-1] = true;*/
        
        if(setNeighborhood(minNeighborhood) != 0)
            return -1;
        if(getSignificantNeighborhoodSize() != old_size)
        {
            ALGORITHM.Function.createStandardSignificantFunction();
        }
        ALGORITHM.Function.updateFunction(); // TODO
//        evtl von aufrufender Funktion durchführen lassen
        if(isTestAllNeighborhoodSizes())
            minNeighborhoodSize = getSignificantNeighborhoodSize();
        if(!isTestAllSignificantNeighborhoodSizes())
            minSignificantNeighborhoodSize = maxSignificantNeighborhoodSize = getSignificantNeighborhoodSize();
        return 0;
    }
    
    public static void initializeMinimalNeighborhoodSizeValue() throws Exception {
        createStandardNeighborhoodRange(getSignificantNeighborhoodSize(), getMinNeighborhoodSize());
        initializeMinimalNeighborhoodValue();
    }
    
    
    
//     Einführen von "actual_neighborhood", das zu Beginn gespeichert wird (bei einzelner Nachbarschaft, also nach dem Parsen) und bei updateSignificantNeighborhood zugewiesen wird (anstatt k++)
  //   NEW_CA Rule nochmal durchlesen, da wird ja permutiert...
             
    private static void updateSignificantNeighborhood() {
        int j = 0;
        int k = 0;
        significantNeighborhood = new int[getSignificantNeighborhoodSize()];
        for(int i = 0; i < getNeighborhoodSize(); i++) {
            if(neighborhood[i]) {
                significantNeighborhood[j] = k;
                j++;
            }
            k++;
        }
    }
    
    public static void resetSignificantNeighborhoodSize() {
        minSignificantNeighborhoodSize = maxSignificantNeighborhoodSize = getSignificantNeighborhoodSize();
    }
    
    public static void initializeMinimalNeighborhoodValue() {
        neighborhood = minNeighborhood.clone();
        updateSignificantNeighborhood();
        processedNeighborhoodPermutations = BigInteger.ONE;
    }

    private static boolean next_neighborhood_permutation(boolean a[]) {
        for(int i = a.length-3, j; i >= 1; i--) {
            if (a[i+1] && (!a[i])) {
                for(j = a.length-1; (a[i] || (a[j] == a[i])); j--);
                Misc.swap(a, i, j);
                for(j = 1; j <= (a.length-i)/2; j++)
                    Misc.swap(a, i+j, a.length-j);
                return true;
            }
        }
        return false;
    }
    

//    TODO: "all neighborhoods" cyclet nicht durch significant neighborhood sizes ??
    
    
    
    /**
     * Generates the next permutation with the current given neighborhood size, i.e. the first and the last entry will always be a significant neighborhood position.
     * For example: (111001 -> 110101 -> 110011 -> 101101 -> 101011 -> 100111)
     * @return false if the last permutation has been reached
     * @throws ALGORITHM.Function.Exception if there was an error creating the function on basis of the newly generated parameters
     */
    public static boolean nextNeighborhoodPermutation() throws Exception {
        if(!isTestAllNeighborhoodPermutations()) {
            return false;
        }
        if(!next_neighborhood_permutation(neighborhood))
        {
            initializeMinimalNeighborhoodValue();
            updateSignificantNeighborhood();
            return false;
        } else processedNeighborhoodPermutations = processedNeighborhoodPermutations.add(BigInteger.ONE);
        ALGORITHM.Function.updateFunction();
        updateSignificantNeighborhood();        
        return true;
    }
    

    public static BigInteger getNumberOfRemainingRuleNeighborhoodPermutations() {
        if(!isAllowDisorderedNeighborhoodPermutations())
            return BigInteger.ZERO;
        return getNumberOfAllRuleNeighborhoodPermutations(getSignificantNeighborhoodSize()).subtract(processedRuleNeighborhoodPermutations);
    }
    
    public static BigInteger getNumberOfAllRuleNeighborhoodPermutations(int significant_neighborhood_size) {
        if(!isAllowDisorderedNeighborhoodPermutations())
            return BigInteger.ONE;
        return Misc.faculty(significant_neighborhood_size);
    }
    
  /**
     * Calculates the number of the remaining neighborhood permutations on basis of the parameters and the processedNeighborhoodPermutations counter
     * As the neighborhood size defines the borders of defined neighborhood positions the actual number of permutations is (significant_neighborhood_size-2) over (neighborhood_size-2)
     * @param significant_neighborhood_size Number of significant positions of the neighborhood (e.g. 3 for '0, 2, 5')
     * @param neighborhood_size Size of the neighborhood including the holes
     * @return Number of neighborhood permutations
     */
    public static BigInteger getNumberOfRemainingNeighborhoodPermutations() {
        if(isTestAllNeighborhoodPermutations() == false)
            return BigInteger.ZERO;
        return getNumberOfAllNeighborhoodPermutations(getSignificantNeighborhoodSize(), getNeighborhoodSize()).subtract(processedNeighborhoodPermutations);
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
    
   
    /**
     * Sets the upper and lower limit of the distance between two positions of the neighborhood.
     * A calculation will cycle through all distances from minNeighborhoodSize to maxNeighborhoodSize.
     * @param min_neighborhood_size lower limit of neighborhood size
     * @param max_neighborhood_size upper limit of neighborhood size
     * @return -1 if min_neighborhood_size is out of range
     * @return -2 if max_neighborhood_size is too large
     */
    public static int setMinMaxNeighborhoodSize(int min_neighborhood_size, int max_neighborhood_size) {
        if(max_neighborhood_size == getMaxNeighborhoodSize() && min_neighborhood_size == getMinNeighborhoodSize()) {
            return 0;
        }
        if(min_neighborhood_size < MIN_NEIGHBORHOOD_SIZE || 
                max_neighborhood_size < min_neighborhood_size) {
            return -1;
        }
        if(max_neighborhood_size > ALGORITHM.Function.calculateMaxNeighborhoodSize()) {
            return -2;
        }
        minNeighborhoodSize = min_neighborhood_size;
        maxNeighborhoodSize = max_neighborhood_size;
        if(isTestAllSignificantNeighborhoodSizes())
            maxSignificantNeighborhoodSize = maxNeighborhoodSize;
        return 0;
    }
    

    
    /**
     * Translates the committed significant neighborhood into a binary vector and assigns that to neighborhood.
     * @return -1 if the number of entries is to small ( < 2)
     * -2 if the neighborhood was not comitted in the appropriate ascending order
     * -3 if the neighborhood was too large
     * @param significant_neighborhood An array containing the neighborhood information in compact form (e.g. '0, 2, 5' instead of 'true, false, true, false, false, true')
     * @throws ALGORITHM.Function.Exception if there was an internal error creating the new function on basis of the new neighborhood
     */
    public static int setSignificantNeighborhood(int[] significant_neighborhood) throws Exception {
        if(testAllNeighborhoodPermutations || testAllNeighborhoodSizes || testAllSignificantNeighborhoodSizes) {
            throw new Exception("setSignificantNeighborhood(): Internal error creating the new function on basis of the new neighborhood");
        }

        if(significant_neighborhood.length < MIN_NEIGHBORHOOD_SIZE) {
            return -1;
        }
        
        //neighborhood_not_in_order = false;
        for(int i = 1; i < significant_neighborhood.length; i++) {
            // not in order => special case
            if(significant_neighborhood[i-1] >= significant_neighborhood[i]) {
          //      neighborhood_not_in_order = true;
                    return -2;
            }
    }
        
        int old_size = getSignificantNeighborhoodSize();
        int new_size = Misc.get_max_distance(significant_neighborhood);
        int min_value = Misc.get_min_value(significant_neighborhood);
        
// standardize to begin with zero        
        for(int i = 0; i < significant_neighborhood.length; i++) {
            significant_neighborhood[i] -= min_value;
        }

        if(new_size > ALGORITHM.Function.calculateMaxNeighborhoodSize())
            return -3;

        boolean new_neighborhood[] = new boolean[new_size];
        for(int i = 0; i < new_size; i++)
            new_neighborhood[i] = false;
        
        for(int i = 0; i < significant_neighborhood.length; i++) {
            new_neighborhood[significant_neighborhood[i]] = true;
        }

        if(setNeighborhood(new_neighborhood) != 0)
            return -4;
        minNeighborhood = neighborhood.clone();
        
        maxNeighborhoodSize = getNeighborhoodSize();

        if(isTestAllNeighborhoodSizes())
        {
            minNeighborhoodSize = getSignificantNeighborhoodSize();
        } else minNeighborhoodSize = maxNeighborhoodSize;
        
        if(isTestAllSignificantNeighborhoodSizes()) {
            maxSignificantNeighborhoodSize = maxNeighborhoodSize;
        }
        
// we have to create a new significant function when the significant neighborhood size has changed
        if(getSignificantNeighborhoodSize() != old_size) {
            ALGORITHM.Function.createStandardSignificantFunction();
        }
        ALGORITHM.Function.updateFunction();
        minSignificantNeighborhoodSize = maxSignificantNeighborhoodSize = getSignificantNeighborhoodSize();        
        return 0;
    }
    
    private static int setNeighborhood(boolean[] new_neighborhood) throws Exception {
        neighborhood = new boolean[new_neighborhood.length];
        for(int i = 0; i < new_neighborhood.length; i++) {
            neighborhood[i] = new_neighborhood[i];
        }
        updateSignificantNeighborhood();
        processedNeighborhoodPermutations = BigInteger.ONE;
        return 0; // TODO?
    }
      
    
    // TODO
    public static int parseStandardNeighborhoodRange(String neighborhood_size_range_string) throws Exception, NumberFormatException {
        String sizes[] = neighborhood_size_range_string.split("[-]+");
        int sizes_value[] = new int[sizes.length];
        for(int i = 0; i < sizes.length; i++) {
//            System.out.println(sizes[i]);
            sizes_value[i] = Integer.parseInt(sizes[i]);
        }
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
    private static int setNeighborhoodSizeRange(int[] sizes) throws Exception {
        int result = 0;
        int min_c = getMinNeighborhoodSize();
        int max_c = getMaxNeighborhoodSize();
        
        if(sizes.length == 1) {
            if(isTestAllNeighborhoodSizes())
                result = setMinMaxNeighborhoodSize(minNeighborhoodSize, sizes[0]);
            else 
                result = setMinMaxNeighborhoodSize(sizes[0], sizes[0]);
        } else if(sizes.length == 2) {
            result = setMinMaxNeighborhoodSize(sizes[0], sizes[1]);
        }
        else result = -3;
        
        if(result == 0)
        {
            if(isTestAllSignificantNeighborhoodSizes())
                result = createStandardNeighborhoodRange(getMinNeighborhoodSize(), getMinNeighborhoodSize());
            else
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
     * Generates a representation of the current neighborhood in compact, readable form
     * @return Neighborhood in compact form (e.g. '0, 2, 3' instead of 'true, false, true, true')
     */
    public static String getNeighborhoodString() {
        String neighborhood_string = "" + getSignificantNeighborhood(0);
        /*for(int i = 1; i < getNeighborhoodSize(); i++)
            if(getNeighborhood(i))
                neighborhood_string += ", " + i;*/
        for(int i = 1; i < getSignificantNeighborhoodSize(); i++) {
            neighborhood_string += ", " + getSignificantNeighborhood(i);
        }
        return neighborhood_string;
        
    }
    
    public static String getBinaryNeighborhoodString() {
        String t = "";
        for(int i = 0; i < getNeighborhoodSize(); i++)
            if(getNeighborhood(i))
                t+= "1";
            else t+= "0";
        return t;
    }
    
        
    /**
     * Parses the neighborhood string that the user has entered (e.g. "0, 1, 5, 7") and transforms it into an array of ints (e.g. {0, 1, 5, 7})
     * @throws java.lang.NumberFormatException if the numbers could not be parsed (e.g. the user has entered literals instead of numbers)
     * @return array of int containing the significant neighborhood (e.g. {0, 1, 5, 7})
     * @param significant_neighborhood_string string with the significant neighborhood (e.g. "0,1, 5, 7")
     */
    public static int[] parseSignificantNeighborhoodString(String significant_neighborhood_string) throws NumberFormatException {
        String neighborhoods[] = significant_neighborhood_string.split("[, .]+");
        int significant_neighborhood[] = new int[neighborhoods.length];
        
        for(int i = 0; i < neighborhoods.length; i++) {
            try {
//              System.out.println("'" + neighborhoods[i] + "'");
                significant_neighborhood[i] = Integer.parseInt(neighborhoods[i]);
            } catch(NumberFormatException e) {
                throw new NumberFormatException("Cannot parse '" + neighborhoods[i] + "' as a numeric value.");
            }
        }
        for(int i= 0; i < neighborhoods.length; i++) {
            for(int j = 0; j < neighborhoods.length; j++) {
                if(i == j)
                    continue;
                if(significant_neighborhood[i] == significant_neighborhood[j]) {
                    throw new NumberFormatException("You have used the same entry ('" + significant_neighborhood[i] + "') more than once.");
                }
            }
        }
        return significant_neighborhood;
    }
    
//    TODO currentSignificantRuleNumber pruefen
    public static boolean getNeighborhood(final int i) {
        return neighborhood[i];
    }
    
    private static int getSignificantNeighborhood(final int i) {
        return significantNeighborhood[i];
    }

    public static int getMinNeighborhoodSize() {
        return minNeighborhoodSize;
    }
    public static int getMaxNeighborhoodSize() {
        return maxNeighborhoodSize;
    }

    public static int getMinSignificantNeighborhoodSize() {
        return minSignificantNeighborhoodSize;
    }
    public static int getMaxSignificantNeighborhoodSize() {
        return maxSignificantNeighborhoodSize;
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
        for(int i = 0; i < getNeighborhoodSize(); i++) {
            if(neighborhood[i]) {
                n++;
            }
        }
        return n;
    }
    
    public static int getSignificantNeighborhoodPosition(int nr) throws Exception {
        int n = 0;
        for(int i = 0; i < getNeighborhoodSize(); i++) {
            if(neighborhood[i]) {
                if(n == nr)
                    return i;
                n++;                    
            }
        }
        throw new Exception("getSignificantNeighborhoodPosition(): Significant position " + nr + " not found.");
    }
    
    public static void setAllowDisorderedNeighborhoodPermutations(boolean allow_disordered_neighborhood_permutations) {
        allowDisorderedNeighborhoodPermutations = allow_disordered_neighborhood_permutations;
    }
    
    /**
     * Returns whether the user has activated to test all neighborhoods within the given model parameters.
     * The significant neighborhood size determines the number of neighborhood positions while the neighborhood size determines the maximum distance between the first and last position. E.g. a neighborhood size of 5 and a significant neighborhood size of 3 would result in 10 neighborhoods to be tested.
     * @return whether the user has activated to test all neighborhoods within the given model parameters
     */
    public static boolean isTestAllNeighborhoodPermutations() {
        return testAllNeighborhoodPermutations;
    }
    
        
    public static void setTestAllNeighborhoodSizesAndPermutations(boolean test_all_neighborhood_sizes_and_permutations) {
        setTestAllNeighborhoodPermutations(test_all_neighborhood_sizes_and_permutations);
        testAllNeighborhoodSizes = test_all_neighborhood_sizes_and_permutations;
        if(test_all_neighborhood_sizes_and_permutations)
        {
            minSignificantNeighborhoodSize = maxSignificantNeighborhoodSize = minNeighborhoodSize = getSignificantNeighborhoodSize();
            maxNeighborhoodSize = getNeighborhoodSize();
            testAllSignificantNeighborhoodSizes = false;
        }
    }
    
    public static void setTestAllNeighborhoods(boolean test_all_neighborhoods) {
        setTestAllNeighborhoodSizesAndPermutations(test_all_neighborhoods);
        testAllSignificantNeighborhoodSizes = test_all_neighborhoods;
        if(test_all_neighborhoods)
        {
            minSignificantNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
            maxSignificantNeighborhoodSize = getMaxNeighborhoodSize();
            minNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
        }
    }
    

    public static void setTestAllNeighborhoodPermutations(boolean test_all_neighborhood_permutations) {
        testAllNeighborhoodPermutations = test_all_neighborhood_permutations;
        if(test_all_neighborhood_permutations)
        {
            minNeighborhoodSize = neighborhood.length;
            maxNeighborhoodSize = neighborhood.length;
            testAllNeighborhoodSizes = false;
            testAllSignificantNeighborhoodSizes = false;
        }            
    }  
    
    public static boolean isAllowDisorderedNeighborhoodPermutations() {
        return allowDisorderedNeighborhoodPermutations;
    }
    
    public static boolean isTestAllNeighborhoodSizes() {
        return testAllNeighborhoodSizes;
    }
    
    public static boolean isTestAllSignificantNeighborhoodSizes() {
        return testAllSignificantNeighborhoodSizes;
    }
    
    

}
