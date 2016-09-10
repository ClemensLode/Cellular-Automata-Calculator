package ALGORITHM;

/**
 *
 * @author Clemens Lode, 1151459, University Karlsruhe (TH), clemens@lode.de
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Neighborhood {
    
    /**
     * Minimal neighborhood size. Neighborhood sizes smaller than 2 are trivial.
     */
    public final static int MIN_NEIGHBORHOOD_SIZE = 2;
    
    private static int minNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    private static int maxNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    private static int minSignificantNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    private static int maxSignificantNeighborhoodSize = MIN_NEIGHBORHOOD_SIZE;
    
    private static int significantNeighborhood[] = {0, 1};
    
// (0,1,2), (0,2,1), (1,0,2), (1,2,0), (2,0,1), (2,1,0)    
    private static int neighborhoodPermutationNumber = 0;
    private static ArrayList<int[]> neighborhoodPermutation;

// (0,1,2,5), (0,1,3,5), (0,1,4,5), (0,2,3,5), (0,2,4,5), (0,3,4,5)
    private static int neighborhoodVariationNumber = 0;
    private static ArrayList<int[]> neighborhoodVariation;
  
    
    private static int neighborhoodSize = 2; // update whenever significant neighborhood size changes!
    
  /**
     * Calculates the number of the remaining neighborhood permutations on basis of the parameters and the processedNeighborhoodPermutations counter
     * As the neighborhood size defines the borders of defined neighborhood positions the actual number of permutations is (significant_neighborhood_size-2) over (neighborhood_size-2)
     * @return Number of neighborhood permutations
     */
    public static int getNumberOfRemainingNeighborhoodPermutations() {
        if(!Iteration.isTestAllNeighborhoodPermutations()) {
            return 0;
        }
        return neighborhoodPermutation.size() - neighborhoodPermutationNumber;
    }    
    
    /**
     * Calculates the number of remaining neighborhood variations 
     * (e.g. 0,1,2,5 -> 0,1,3,5 -> 0,1,4,5 -> 0,2,3,5 -> 0,2,4,5 -> 0,3,4,5 
     * => 6 variations minus the number already calculated)
     * @return 0 if we do not test neighborhood variations, otherwise the number of remaining neighborhood variations
     */
    public static int getNumberOfRemainingNeighborhoodVariations() {
        if(!Iteration.isTestAllNeighborhoodVariations()) {
            return 0;
        }
        return neighborhoodVariation.size() - neighborhoodVariationNumber;
    }
    
    /**
     * Increases the significant neighborhood size or resets it if we reached the neighborhood size
     * @return false if we are done iterating through all significant neighborhood sizes, true otherwise
     * @throws java.lang.Exception if there was an error initializing the next neighborhood size
     */
    public static boolean nextSignificantNeighborhoodSize() throws Exception  {
        if((getSignificantNeighborhoodSize() >= getNeighborhoodSize()) || (getSignificantNeighborhoodSize() >= getMaxSignificantNeighborhoodSize()))
        {
            // reset Neighborhood
            createStandardNeighborhoodRange(getMinSignificantNeighborhoodSize(), getNeighborhoodSize());
            return false;
        } else {
            createStandardNeighborhoodRange(getSignificantNeighborhoodSize()+1, getNeighborhoodSize());
            return true;
        }    
    }    
    
    /**
     * Iterates through all neighborhood sizes from significant neighborhood size to neighborhood size, resets neighborhood size to significant neighborhood size if we iterated through all neighborhood sizes
     * @return false if we are done iterating through all neighborhood sizes, true otherwise
     * @throws java.lang.Exception if there was an error initializing the next neighborhood size
     */
    public static boolean nextNeighborhoodSize() throws Exception {
        if(getNeighborhoodSize() >= getMaxNeighborhoodSize()) {
            createStandardNeighborhoodRange(getSignificantNeighborhoodSize(), getSignificantNeighborhoodSize());
            return false;
        } else {
            createStandardNeighborhoodRange(getSignificantNeighborhoodSize(), getNeighborhoodSize()+1);
            return true;
        }
    }
    
    /**
     * Initialize all neighborhood permutations for the current set of parameters
     */
    public static void initNeighborhoodPermutations() {
        if(!Iteration.isTestAllNeighborhoodPermutations()) {
            return;
        }
        
        neighborhoodPermutation = new ArrayList<int[]>();
        int[] t = significantNeighborhood.clone();

        // sort neighborhood in an ascending order
        Arrays.sort(t);
        int fac = Misc.faculty(getSignificantNeighborhoodSize()).intValue();
        neighborhoodPermutation.add(t.clone());
        for(int i = 0; i < fac-1; i++) {
            Misc.next_array_permutation(t);
            neighborhoodPermutation.add(t.clone());            
        }
        neighborhoodPermutationNumber = 0;
    }
    
    /**
     * Initialize all neighborhood variations for the current set of parameters
     */
    public static void initNeighborhoodVariations() {
        if(!Iteration.isTestAllNeighborhoodVariations() || getSignificantNeighborhoodSize() == 2 ) {
            return;
        }
        
        neighborhoodVariation = new ArrayList<int[]>();
        int[] t = significantNeighborhood.clone();
        
        
// t mit (0, 1, 2, 3, ..., 5, 10) füllen, für z.B. Neighborhood size 11, significant Neighborhood size 6
        for(int i = 0; i < t.length-1; i++) {
            t[i] = significantNeighborhood[0] + i;
        }
        t[t.length-1] = significantNeighborhood[significantNeighborhood.length-1];

        // n! / x!
        int fac = Misc.faculty(getNeighborhoodSize()-2).divide(BigInteger.valueOf(getSignificantNeighborhoodSize() - 2)).intValue();
        neighborhoodVariation.add(t.clone());
        for(int i = 0; i < fac-1; i++) {
            Misc.next_array_permutation_ignore_equals(t);
            neighborhoodVariation.add(t.clone());
        }     
        neighborhoodVariationNumber = 0;
    }

/**
 * permutates the neighborhood, e.g. 0,1,2 -> 0,2,1 -> 1,0,2 -> ..., and updates the function
 * @return false if no permutations are left or if we do not need to permutate the neighborhood, true otherwise
 * @throws java.lang.Exception if there was an error updating the function
 */
    public static boolean nextNeighborhoodPermutation() throws Exception {
        if(!Iteration.isTestAllNeighborhoodPermutations()) {
            return false;
        }        
        neighborhoodPermutationNumber++;
        if(neighborhoodPermutationNumber >= neighborhoodPermutation.size()) {
            neighborhoodPermutationNumber = 0;
        }
        significantNeighborhood = neighborhoodPermutation.get(neighborhoodPermutationNumber).clone();

        if(neighborhoodPermutationNumber == 0) {
            return false;
        } 
        
        ALGORITHM.Function.updateFunction();

        return true;
    }
  
    /**
     * variates the neighborhood, e.g. 0,1,2,5 -> 0,1,3,5 -> 0,1,4,5 -> 0,2,3,5 -> 0,2,4,5 -> 0,3,4,5
     * @return false if no variation are left or if we do not need to variate the neighborhood, true otherwise
     * @throws java.lang.Exception if there was an error updating the function
     */
    public static boolean nextNeighborhoodVariation() throws Exception {
        if(!Iteration.isTestAllNeighborhoodVariations() || getSignificantNeighborhoodSize() == 2) {
            return false;
        }        

        neighborhoodVariationNumber++;
        if(neighborhoodVariationNumber >= neighborhoodVariation.size()) {
            neighborhoodVariationNumber = 0;
        }

        significantNeighborhood = neighborhoodVariation.get(neighborhoodVariationNumber).clone();

        if(neighborhoodVariationNumber == 0) {
            return false;
        }
        
        ALGORITHM.Function.updateFunction();

        return true;
    }
  
    /**
     * Creates the first and last neighborhood permutation depending on the current significantNeighborhoodSize and neighborhoodSize.
     * If only a single neighborhood is to be tested, set first and last permutation to the current permutation.
     * Call whenever neighborhood size changed
     * @param significant_neighborhood_size Number of significant positions of the neighborhood (e.g. 3 for '0, 2, 5')
     * @param neighborhood_size Size of the neighborhood including the holes
     * @throws Exception if there was an error updating the function
     */

    private static void createStandardNeighborhoodRange(int significant_neighborhood_size, int neighborhood_size) throws Exception {
        
        if(significant_neighborhood_size == getSignificantNeighborhoodSize() && neighborhood_size == getNeighborhoodSize()) {
            return;
        }
        
        if(significant_neighborhood_size > neighborhood_size) {
            neighborhood_size = significant_neighborhood_size;
        }
        
        int old_size = getSignificantNeighborhoodSize();
        significantNeighborhood = new int[significant_neighborhood_size];
        for(int i = 0; i < significant_neighborhood_size - 1; i++) {
            significantNeighborhood[i] = i;
        }
        significantNeighborhood[significant_neighborhood_size - 1] = neighborhood_size-1; // TODO
        updateNeighborhoodSize();
        
        if(getSignificantNeighborhoodSize() != old_size)
        {
            ALGORITHM.Function.createStandardSignificantFunction();
        }
        ALGORITHM.Function.updateFunction(); // TODO
    }

    /**
     * Calculates which array index of the neighborhood array corresponds to a certain entry of the sorted array of the corresponding neighborhood array
     * @param position index of the sorted array
     * @return corresponding index of unsorted array, -1 if there was an error
     */
    public static int getNeighborhoodNumber(int position) {// throws Exception {
        if(position < 0 || position > significantNeighborhood.length) {
  //          throw new Exception("getNeighborhoodNumber(): position out of range (" + position + ")");
        }
        int[] temp_neighborhood = significantNeighborhood.clone();
        Arrays.sort(temp_neighborhood);
        for(int j = 0; j < getSignificantNeighborhoodSize(); j++) {
            if(temp_neighborhood[position] == significantNeighborhood[j]) {
                return j;
            }
        }
        return 0;
//        throw new Exception("getNeighborhoodNumber(): Unspecified error");
    }
        
    /**
     * resets the minimum and maximum significant neighborhood size to the current significant neighborhood size
     */
    public static void resetSignificantNeighborhoodSize() {
        minSignificantNeighborhoodSize = maxSignificantNeighborhoodSize = getSignificantNeighborhoodSize();
    }

    /**
     * Standardizes a copy of the significant neighborhood array so that it begins with 0
     * @return standardized array
     */
    public static int[] getStandardizedSignificantNeighborhoodCopy() {
        int[] t = new int[getSignificantNeighborhoodSize()];
        int min_value = Misc.get_min_value(significantNeighborhood);
        for(int i = 0; i < t.length; i++) {
            t[i] = significantNeighborhood[i] + min_value;
        }
        return t;
    }
       
    /**
     * Assigns a new significant neighborhood and updates the function
     * @param significant_neighborhood An array containing the neighborhood (e.g. -4, 2, 3)
     * @throws Exception if there was an internal error creating the new function on basis of the new neighborhood 
     * or if the function was called while the parameters were set to multiple automatic tests of the neighborhood
     * or if the neighborhood was out of range
     */
    public static void setSignificantNeighborhood(int[] significant_neighborhood) throws Exception {
        if(!isSingleNeighborhood()) {
            throw new Exception("setSignificantNeighborhood(): Internal error creating the new function on basis of the new neighborhood");
        }

        int old_size = getSignificantNeighborhoodSize();
        int new_size = Misc.get_max_distance(significant_neighborhood);
        int min_value = Misc.get_min_value(significant_neighborhood);
// standardize to begin with zero        
        for(int i = 0; i < significant_neighborhood.length; i++) {
            significant_neighborhood[i] -= min_value;
        }
        
        int max_neighborhood_size = ALGORITHM.Iteration.calculateMaxNeighborhoodSize();
        if(new_size > max_neighborhood_size) {
            throw new Exception("Implied neighborhood out of range (" + new_size + " > " + max_neighborhood_size + ")");
        }
        
        significantNeighborhood = significant_neighborhood.clone();
        updateNeighborhoodSize();
            
        minSignificantNeighborhoodSize = maxSignificantNeighborhoodSize = getSignificantNeighborhoodSize(); 
        minNeighborhoodSize = maxNeighborhoodSize = getNeighborhoodSize();
        
// we have to create a new significant function when the significant neighborhood size has changed
        if(getSignificantNeighborhoodSize() != old_size) {
            ALGORITHM.Function.createStandardSignificantFunction();
        }
// function has to be recreated anyways        
        ALGORITHM.Function.updateFunction();        
        
    }
    
    /**
     * Sets the lower boundary of the neighborhood
     * @param min_neighborhood_size lower boundary of the neighborhood
     * @throws Exception if the parameter was out of range or if there was an error setting the neighborhood
     */
    public static void setMinNeighborhoodSize(int min_neighborhood_size) throws Exception {
        if(min_neighborhood_size > getMaxNeighborhoodSize()) {
            throw new Exception("Invalid neighborhood size range, the lower boundary of the neighborhood must be smaller than the upper boundary (" + getMaxNeighborhoodSize() + " < " + min_neighborhood_size + ").");
        }
        if(min_neighborhood_size < getMinSignificantNeighborhoodSize()) {
            throw new Exception("Invalid neighborhood size range, lower boundary of the neighborhood size must be larger than the lower boundary of the significant neighborhood size (" + min_neighborhood_size + " < " + getMinSignificantNeighborhoodSize() + ").");
        }
        minNeighborhoodSize = min_neighborhood_size;
        createStandardNeighborhoodRange(getMinSignificantNeighborhoodSize(), getMinNeighborhoodSize());
    }

    /**
     * Sets the upper boundary of the neighborhood
     * @param max_neighborhood_size upper boundary of the neighborhood
     * @throws Exception if the parameter was out of range or if there was an error setting the neighborhood
     */    
    public static void setMaxNeighborhoodSize(int max_neighborhood_size) throws Exception {
        if(getMinNeighborhoodSize() > max_neighborhood_size) {
            throw new Exception("Invalid neighborhood size range, the upper boundary of the neighborhood must be larger than the lower boundary (" + max_neighborhood_size + " < " + getMinNeighborhoodSize() + ").");
        }
        if(max_neighborhood_size < getMaxSignificantNeighborhoodSize()) {
            throw new Exception("Invalid neighborhood size range, upper boundary of the neighborhood size must be larger than the upper boundary of the significant neighborhood size (" + max_neighborhood_size + " < " + getMaxSignificantNeighborhoodSize() + ").");        
        }
        maxNeighborhoodSize = max_neighborhood_size;
    }  
    
    /**
     * Sets the lower boundary of the significant neighborhood
     * @param min_significant_neighborhood_size lower boundary of the significant neighborhood
     * @throws Exception if the parameter was out of range or if there was an error setting the neighborhood
     */    
    public static void setMinSignificantNeighborhoodSize(int min_significant_neighborhood_size) throws Exception {
        if(min_significant_neighborhood_size > getMaxSignificantNeighborhoodSize()) {
            throw new Exception("Invalid neighborhood size range, the lower boundary of the significant neighborhood must be smaller than the upper boundary (" + getMaxSignificantNeighborhoodSize() + " < " + min_significant_neighborhood_size + ").");
        }
        if(getMinNeighborhoodSize() < min_significant_neighborhood_size) {
            throw new Exception("Invalid neighborhood size range, lower boundary of the neighborhood size must be larger than the lower boundary of the significant neighborhood size (" + getMinNeighborhoodSize() + " < " + min_significant_neighborhood_size + ").");
        }
        minSignificantNeighborhoodSize = min_significant_neighborhood_size;
        createStandardNeighborhoodRange(getMinSignificantNeighborhoodSize(), getMinNeighborhoodSize());  
    }

    /**
     * Sets the upper boundary of the significant neighborhood
     * @param max_significant_neighborhood_size upper boundary of the neighborhood
     * @throws Exception if the parameter was out of range or if there was an error setting the neighborhood
     */        
    public static void setMaxSignificantNeighborhoodSize(int max_significant_neighborhood_size) throws Exception {
        if(getMinSignificantNeighborhoodSize() > max_significant_neighborhood_size) {
            throw new Exception("Invalid neighborhood size range, the lower boundary of the significant neighborhood must be smaller than the upper boundary (" + max_significant_neighborhood_size + " < " + getMinSignificantNeighborhoodSize() + ").");
        }
        if(getMaxNeighborhoodSize() < max_significant_neighborhood_size) {
            throw new Exception("Invalid neighborhood size range, upper boundary of the neighborhood size must be larger than the upper boundary of the significant neighborhood size (" + getMaxNeighborhoodSize() + " < " + max_significant_neighborhood_size + ").");        
        }
        maxSignificantNeighborhoodSize = max_significant_neighborhood_size;
    }        

    /**
     * Assigns the neighborhood and significant neighborhood ranges
     * @param min_significant_neighborhood_size lower boundary of the significant neighborhood size
     * @param max_significant_neighborhood_size upper boundary of the significant neighborhood size
     * @param min_neighborhood_size lower boundary of the neighborhood size
     * @param max_neighborhood_size upper boundary of the neighborhood size
     * @throws java.lang.Exception if one of the parameters was out of range or if there was an error setting the neighborhood
     */
    public static void setMinMaxNeighborhoodSize(int min_significant_neighborhood_size, int max_significant_neighborhood_size, int min_neighborhood_size, int max_neighborhood_size) throws Exception {
        if(min_neighborhood_size > max_neighborhood_size) {
            throw new Exception("Invalid neighborhood size range, the lower boundary of the neighborhood must be smaller than the upper boundary (" + max_neighborhood_size + " < " + min_neighborhood_size + ").");            
        }
        if(min_significant_neighborhood_size > max_significant_neighborhood_size) {
            throw new Exception("Invalid neighborhood size range, the lower boundary of the significant neighborhood must be smaller than the upper boundary (" + max_significant_neighborhood_size + " < " + min_significant_neighborhood_size + ").");            
        }
        if(min_neighborhood_size < min_significant_neighborhood_size) {
            throw new Exception("Invalid neighborhood size range, lower boundary of the neighborhood size must be larger than the lower boundary of the significant neighborhood size (" + min_neighborhood_size + " < " + min_significant_neighborhood_size + ").");
        }
        if(max_neighborhood_size < max_significant_neighborhood_size) {
            throw new Exception("Invalid neighborhood size range, upper boundary of the neighborhood size must be larger than the upper boundary of the significant neighborhood size (" + max_neighborhood_size + " < " + max_significant_neighborhood_size + ").");        
        }

        int max_max_neighborhood_size = Iteration.calculateMaxNeighborhoodSize();
        if(max_neighborhood_size > max_max_neighborhood_size) {
            throw new Exception("Invalid neighborhood size range, the neighborhood size would take too much memory (max size " + max_max_neighborhood_size + " < " + max_neighborhood_size + ").");
        }
       
        minNeighborhoodSize = min_neighborhood_size;
        maxNeighborhoodSize = max_neighborhood_size;
        minSignificantNeighborhoodSize = min_significant_neighborhood_size;
        maxSignificantNeighborhoodSize = max_significant_neighborhood_size;
        
        createStandardNeighborhoodRange(getMinSignificantNeighborhoodSize(), getMinNeighborhoodSize());
    }

    /**
     * Sets the neighborhood to the lower boundary of the significant neighborhood size and the lower boundary of the neighborhood size
     * @throws java.lang.Exception if there was an error setting the new neighborhood
     */
    public static void initializeMinimalNeighborhoodSizeValue() throws Exception {
        createStandardNeighborhoodRange(minSignificantNeighborhoodSize, minNeighborhoodSize);
    }
    
    
    

    /* ---------------------
     *    INPUT/OUTPUT
     * ---------------------*/
    
    /**
     * Parses the significant neighborhood string and splits it into its parts
     * @param significant_neighborhood_string String containing the neighborhood entries (split by '[, .]+')
     * @return integer array of the parsed neighborhood
     * @throws java.lang.Exception if there was an error parsing the string
     */
    public static int[] parseSignificantNeighborhoodString(String significant_neighborhood_string) throws Exception {
        String neighborhoods[] = significant_neighborhood_string.split("[, .]+");
        int significant_neighborhood[] = new int[neighborhoods.length];
        
        for(int i = 0; i < neighborhoods.length; i++) {
            try {
                significant_neighborhood[i] = Integer.parseInt(neighborhoods[i]);
            } catch(NumberFormatException e) {
                throw new Exception("Cannot parse '" + neighborhoods[i] + "' as a numeric value.");
            }
        }

        for(int i= 0; i < neighborhoods.length; i++) {
            for(int j = 0; j < neighborhoods.length; j++) {
                if(i == j) {
                    continue;
                }
                if(significant_neighborhood[i] == significant_neighborhood[j]) {
                    throw new Exception("You have used the same entry ('" + significant_neighborhood[i] + "') more than once.");
                }
            }
        }
        if(significant_neighborhood.length < MIN_NEIGHBORHOOD_SIZE) {
            throw new Exception("Significant neighborhood size too small.");
        }         
        int max_size = Iteration.calculateMaxNeighborhoodSize();
        if(Misc.get_max_distance(significant_neighborhood) >= max_size) {
            throw new Exception("Significant neighborhood (i.e. neighborhood size) out of range");
        }
        
        return significant_neighborhood;
    }
 

    /**
     * Generates a representation of the current neighborhood
     * @return Neighborhood as a String (e.g. '-1, 2, 3')
     */
    public static String getSignificantNeighborhoodString() {
        String neighborhood_string = "" + getSignificantNeighborhood(0);
        for(int i = 1; i < getSignificantNeighborhoodSize(); i++) {
            neighborhood_string += ", " + getSignificantNeighborhood(i);
        }
        return neighborhood_string;
        
    }
    
    /**
     * Generates a short representation of the current neighborhood
     * @return Neighborhood as a String (e.g. '-0-2-3')
     */
    public static String getShortSignificantNeighborhoodString() {
        String neighborhood_string = getSignificantNeighborhoodString();
        neighborhood_string = neighborhood_string.replace(" ", "");
        neighborhood_string = neighborhood_string.replace(",", "-");
        return neighborhood_string;
    }    
    
    /**
     * Checks if we iterate through different neighborhood or significant neighborhood sizes
     * @return true if we are, false otherwise
     */
    public static boolean isSingleNeighborhood() {
        return (getMinSignificantNeighborhoodSize() == getMaxSignificantNeighborhoodSize() && getMinNeighborhoodSize() == getMaxNeighborhoodSize());
    }
   

    
    /* ---------------------
    *    GET/SET FUNCTIONS
    * ---------------------*/    
    /**
     * Updates the neighborhood size value on basis of the significant neighborhood
     */
    private static void updateNeighborhoodSize() {
        int min = significantNeighborhood[0];
        int max = significantNeighborhood[1];
        for(int i:significantNeighborhood) {
            if(i < min) {
                min = i;
            } else
            if(i > max) {
                max = i;
            }
        }
        neighborhoodSize = max - min + 1;
    }
    
    /**
     * @return current neighborhood size 
     */
    public static int getNeighborhoodSize() {
        // TODO optimize?
        updateNeighborhoodSize();
        return neighborhoodSize;
    }    
    
    
    public static int getSignificantNeighborhood(final int i) {
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
     * Returns the number of cells that one cell uses when applying the rule, including its own cell, i.e. a neighborhood of '0, 5, 10' would result in a significant neighborhood size of 3
     * @return The number of cells that one cell uses when applying the rule
     */
    public static int getSignificantNeighborhoodSize() {
        return significantNeighborhood.length;
    }    
    public static final int[] getSignificantNeighborhoodArray() {
        return significantNeighborhood;
    }
    

}
