/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ALGORITHM;

import java.math.BigInteger;

/**
 *
 * @author Clawg
 */
public class Misc {
    
    // --- MISC functions
    public static int pow(final int base, final int exp) {
        int l = 1;
        for(int i = 0; i < exp; i++)
            l*=base;
        return l;
    }
    
    public static long powl(final int base, final int exp) {
        long l = 1;
        for(int i = 0; i < exp; i++)
            l*=base;
        return l;
    }

    public static BigInteger faculty(final int n){
        BigInteger fac = BigInteger.ONE;
        int factor = 1;
        while(factor <= n) {
            fac = fac.multiply(BigInteger.valueOf(factor++));
        }
        return fac;
   }  
    
    public static void swap(final int a[], final int i, final int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    public static void swap(final boolean a[], final int i, final int j) {
        boolean temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
     private static void rule_swap(int rule[], final int cell_states, int s1, int s2)
     {
         int t1 = Misc.pow(cell_states, s1);
         int t2 = Misc.pow(cell_states, s2);
         
         for(int i = 0; i < rule.length; i++)
         {
             int a = ((i / t1) % cell_states);//i & t1;
             int b = ((i / t2) % cell_states);//i & t2;
             
             int t = i-(a*t1+b*t2)+(a*t2+b*t1);
             if(i < t)
                 Misc.swap(rule, i, t);
         }
     }
          
     public static boolean next_rule_neighborhood_permutation(int rule[], final int n[], final int cell_states)
     {
        for(int i = n.length-2, j; i >= 0; i--) {
            if (n[i+1] > n[i]) {
                for(j = n.length-1; n[j] <= n[i]; j--);
                Misc.swap(n, i, j);
                rule_swap(rule, cell_states, i, j);
                for(j = 1; j <= (n.length-i)/2; j++)
                {
                    swap(n, i+j, n.length-j);
                    rule_swap(rule, cell_states, i+j, n.length-j);
                    
                }
                return true;
            }
        }
        return false;        
     }
     
     public static int get_max_distance(int array[]) {
         return (1 + get_max_value(array) - get_min_value(array));
     }
     
     public static int get_max_value(int array[]) {
         boolean max_initialized = false;
         int max = 0;
         for(int i: array) {
             if(!max_initialized) {
                 max_initialized = true;
                 max = i;
             }
             if(i > max) {
                 max = i;
             }
         }
         return max;
     }
     public static int get_min_value(int array[]) {
         boolean min_initialized = false;
         int min = 0;
         for(int i: array) {
             if(!min_initialized) {
                 min_initialized = true;
                 min = i;
             }
             if(i < min) {
                 min = i;
             }
         }
         return min;
     }
    

}
