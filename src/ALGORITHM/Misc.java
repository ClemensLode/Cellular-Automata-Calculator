package ALGORITHM;

/**
 *
 * @author Clemens Lode, 1151459, University Karlsruhe (TH), clemens@lode.de
 */

import java.math.BigInteger;

public class Misc {

    // --- MISC functions
    
    /**
     * Calculates the power of a value
     * @param base base
     * @param exp exponent
     * @return base^exponent
     */    
    public static int pow(final int base, final int exp) {
        int l = 1;
        for (int i = 0; i < exp; i++) {
            l *= base;
        }
        return l;
    }

    /**
     * Calculates the power (long integer)
     * @param base base
     * @param exp exponent
     * @return base^exponent
     */
    public static long powl(final int base, final int exp) {
        long l = 1;
        for (int i = 0; i < exp; i++) {
            l *= base;
        }
        return l;
    }

    /**
     * Calculates the faculty of a value
     * @param n value
     * @return the faculty of n
     */
    public static BigInteger faculty(final int n) {
        BigInteger fac = BigInteger.ONE;
        int factor = 1;
        while (factor <= n) {
            fac = fac.multiply(BigInteger.valueOf(factor++));
        }
        return fac;
    }

    /**
     * finds the greatest common divisor of both values
     * @param a first value
     * @param b second value
     * @return greatest common divisor
     */
    public static long ggt(final long a, final long b) {

        if (a == 0 || b == 0) {
            return 1;
        }
        long c = Math.abs(a);
        long d = Math.abs(b);

        if (d > c) {
            long t = d;
            d = c;
            c = t;
        }

        while (true) {
            long rest = c % d;
            if (rest == 0) {
                return d;
            }
            c = d;
            d = rest;
        }
    }
    
    /**
     * swaps two entries within an array
     * @param a array
     * @param i first index
     * @param j second index
     */
    public static void swap(final int a[], final int i, final int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    /**
     * permutates through an ascending array
     * @param n array to be permutated
     */
    public static void next_array_permutation(int n[]) {
         int i = n.length - 1;
      
         while (n[i-1] >= n[i]) {
            i = i-1;
         }
      
         int j = n.length;
      
         while (n[j-1] <= n[i-1]) {
            j = j-1;
         }
      
         swap(n, i-1, j-1);
      
         i++; 
         j = n.length;
      
         while (i < j) {
            swap(n, i-1, j-1);
            i++;
            j--;
         }        
    }
     
    /**
     * permutates through an ascending array keeping it ascended
     * @param n array to be permutated
     */
    public static void next_array_permutation_ignore_equals(int n[]) {
        for(int i = 1; i < n.length-1; i++) {
            if(n[i] < n[i+1]) {
                n[i]++;
                if(n[i] == n[i+1]) {
                    n[i] = n[i-1]+1;
                } else {
                    break;
                }
            }
        }
    }
    
  /**
   * Calculates the next function permutation starting from an ascending array
   * @param function current function as an array, starting with an ascending array
   * @return next function permutation
   */
    public static boolean next_function_permutation(int function[]) {
        for (int i = function.length - 2,  j; i >= 0; i--) {
            if (function[i + 1] > function[i]) {
                j = function.length - 1;
                while(function[j] <= function[i]) {
                    j--;
                }
                Misc.swap(function, i, j);
                for (j = 1; j <= (function.length - i) / 2; j++) {
                    Misc.swap(function, i + j, function.length - j);
                }
                return true;
            }
        }
        return false;
    }    

    /**
     * Determines the maximal distance of entries within an array (max - min value)
     * @param array array of which we want the maximal distance
     * @return maximal distance
     */
    public static int get_max_distance(int array[]) {
        return (1 + get_max_value(array) - get_min_value(array));
    }
    /**
     * Finds the largest value of an array
     * @param array array of which we want the largest value
     * @return largest value of the array
     */
    public static int get_max_value(int array[]) {
        boolean max_initialized = false;
        int max = 0;
        for (int i : array) {
            if (!max_initialized) {
                max_initialized = true;
                max = i;
            }
            if (i > max) {
                max = i;
            }
        }
        return max;
    }
    /**
     * Finds smallest value of an array
     * @param array of which we want the smallest value
     * @return smallest value of the array
     */
    public static int get_min_value(int array[]) {
        boolean min_initialized = false;
        int min = 0;
        for (int i : array) {
            if (!min_initialized) {
                min_initialized = true;
                min = i;
            }
            if (i < min) {
                min = i;
            }
        }
        return min;
    }
    
    /**
     * Calculates the next state of a cell using the current neighborhood of the cell and the cell function
     * @param neighbors neighborhood values for the function
     * @param function cellular automata function that calculates the next value using the neighborhod values
     * @param cell_states number of cell states
     * @return the function value of the appropriate index
     */
    public static int getNeighborhoodFunction(int[] neighbors, int[] function, int cell_states) {
        
        int index = 0;
        for (int i = 0; i < neighbors.length; i++) {
            index *= cell_states;
            index += neighbors[i];
        }
        return function[index];
    }            
}
