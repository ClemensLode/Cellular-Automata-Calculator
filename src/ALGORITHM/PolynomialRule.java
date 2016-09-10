package ALGORITHM;

/**
 *
 * @author Clemens Lode, 1151459, University Karlsruhe (TH), clemens@lode.de
 */
import java.util.ArrayList;
import java.math.BigInteger;

public class PolynomialRule {

    public static class PolynomialObject {
        /**
         * is a constant 
         */
        public boolean is_constant = false;
        /**
         * indice / factor of object
         */
        public int index = 0;
    }

    private static class Summand {

        /**
         * factor in front of the summand
         */
        public int factor;
        /**
         * power of the variables, the indices of the power array correspond to the indices of the variables
         */
        public int[] power;
        /**
         * temporary variable, marked for deletion
         */
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

// addition and multiplication table for 4 cell states    
    private static int[][] add_table_p4 = new int[4][4];
    private static int[][] mul_table_p4 = new int[4][4];

    public static void init_p4() {
        add_table_p4[0][0] = 0;
        add_table_p4[0][1] = 1;
        add_table_p4[0][2] = 2;
        add_table_p4[0][3] = 3;

        add_table_p4[1][0] = 1;
        add_table_p4[1][1] = 0;
        add_table_p4[1][2] = 3;
        add_table_p4[1][3] = 2;

        add_table_p4[2][0] = 2;
        add_table_p4[2][1] = 3;
        add_table_p4[2][2] = 0;
        add_table_p4[2][3] = 1;

        add_table_p4[3][0] = 3;
        add_table_p4[3][1] = 2;
        add_table_p4[3][2] = 1;
        add_table_p4[3][3] = 0;

        mul_table_p4[0][0] = 0;
        mul_table_p4[0][1] = 0;
        mul_table_p4[0][2] = 0;
        mul_table_p4[0][3] = 0;

        mul_table_p4[1][0] = 0;
        mul_table_p4[1][1] = 1;
        mul_table_p4[1][2] = 2;
        mul_table_p4[1][3] = 3;

        mul_table_p4[2][0] = 0;
        mul_table_p4[2][1] = 2;
        mul_table_p4[2][2] = 3;
        mul_table_p4[2][3] = 1;

        mul_table_p4[3][0] = 0;
        mul_table_p4[3][1] = 3;
        mul_table_p4[3][2] = 1;
        mul_table_p4[3][3] = 2;
    }

    private static int multiplicate_p4(int x, int y) {
        return mul_table_p4[x][y];
    }

    private static int add_p4(int x, int y) {
        return add_table_p4[x][y];
    }

    /**
     * create a polynom that will return 'f_value' for the x values 'x_values' and '0' otherwise.
     * @param x_values values of the neighborhood
     * @param f_value value of the function for this neighborhood configuration
     * @param cell_states number of cell states
     * @return an ArrayList containing a number of summands
     */
    private static ArrayList<Summand> create_function_summands(final int[] x_values, int f_value, int cell_states) {

        int q = cell_states - 1;

        ArrayList<Summand> summands = new ArrayList<Summand>();

// start with summand with function value
        Summand t = new Summand(x_values.length, f_value);
        summands.add(t);

//      (1 - (x_i - x_values[i])^q) * ...

// Now we have to multiplicate all elements and create the standard form of a polynom:
        for (int i = 0; i < x_values.length; i++) {
            ArrayList<Summand> temp = new ArrayList<Summand>();
            for (Summand s : summands) {
                // copy all summands in a temporary array, i.e. multiplicate with '1'
                temp.add(new Summand(s.factor, s.power));

                if (cell_states == 4) {
                    // multiplicate the rest with q == -1 (and later with (x_i - x_values[i])^q)
                    s.factor = multiplicate_p4(s.factor, q);
                } else {
                    // multiplicate the rest with -1 (and later with (x_i - x_values[i])^q)                    
                    s.factor *= -1;
                }
            }

            for (int j = 0; j < q; j++) {
                ArrayList<Summand> temp2 = new ArrayList<Summand>();

                if (cell_states == 4) {
                    for (Summand s : summands) {
                        // multiplicate with 'x_values[i]'                        
                        int value = multiplicate_p4(s.factor, x_values[i]);
                        // multiplicate with '-1 == q'
                        value = multiplicate_p4(value, q);

                        temp2.add(new Summand(value, s.power));
                        s.power[i]++;
                    }
                } else {
                    for (Summand s : summands) {
                        // multiplicate with '-x_values[i]'
                        int value = (-1) * s.factor * x_values[i];

                        temp2.add(new Summand(value, s.power));
                        // multiplicate with x_i
                        s.power[i]++;
                    }
                }
                // combine both
                summands.addAll(temp2);
            }
            // combine both
            summands.addAll(temp);
        }

        return summands;
    }

    /**
     * sort summands by their power of the variables
     * @param summand_list list of summands to sort
     * @param n_size size of the neighborhood
     * @param cell_states number of cell states
     */
    private static void sort_summands(ArrayList<Summand> summand_list, int n_size, int cell_states) {
        int[] index = new int[n_size];
        for (int i = 0; i < n_size; i++) {
            index[i] = cell_states;
        }

        ArrayList<Summand> new_summand_list = new ArrayList<Summand>();

        boolean not_null = true;
        do {

            for (Summand s : summand_list) {
                boolean is_equal = true;
                for (int i = 0; i < n_size; i++) {
                    if (s.power[i] != index[i]) {
                        is_equal = false;
                        break;
                    }
                }
                if (is_equal) {
                    new_summand_list.add(new Summand(s.factor, s.power));
                }
            }

            for (int i = n_size - 1; i >= 0; i--) {
                index[i]--;
                if (index[i] == -1) {
                    index[i] = cell_states;
                } else {
                    break;
                }
            }

            not_null = false;
            for (int i = 0; i < n_size; i++) {
                if (index[i] != cell_states) {
                    not_null = true;
                    break;
                }
            }
        } while (not_null);
        summand_list.clear();
        summand_list.addAll(new_summand_list);
    }

    /**
     * simplify summands by adding them
     * @param summand_list list of summands
     * @param cell_states number of cell states
     */
    private static void simplify_summands(ArrayList<Summand> summand_list, int cell_states) {
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
                    if (cell_states == 4) {
                        s.factor = add_p4(Math.abs(s.factor), Math.abs(t.factor));
                    } else {
                        s.factor += t.factor;
                    }
                    t.to_delete = true;
                }
                y++;
            }

            s.factor = s.factor % cell_states;

            if (s.factor == 0) {
                s.to_delete = true;
            } else if (cell_states == 2 || cell_states == 3 || cell_states == 5 || cell_states == 7) {
                // is polynomial => we can shorten the formula
                if (s.factor == 1) {
                    boolean found_entries = true;
                    for (int t : s.power) {
                        if (t != cell_states) {
                            found_entries = false;
                            break;
                        }
                    }
                    if (found_entries) {
                        for (int t = 0; t < s.power.length; t++) {
                            s.power[t] = 1;
                        }
                    }
                }
            }
            x++;
        }
        ArrayList<Summand> remove_list = new ArrayList<Summand>();
        for (Summand s : summand_list) {
            if (s.to_delete) {
                remove_list.add(s);
            }
        }
        summand_list.removeAll(remove_list);

        for (Summand s : summand_list) {
            while (s.factor < 0) {
                s.factor += cell_states;
            }
        }
    }

    /**
     * Generates a polynomial rule representation out of the function array
     * @param function Function that we want to transform into a polynomial rule string
     * @param cell_states number of cell states
     * @return The polynomial rule string
     */
    public static String getPolynomialRule(final int[] function, final int cell_states) {
        String polynomial_rule = "";

        int neighborhood_size = Neighborhood.getSignificantNeighborhoodSize();
        int significant_max_array_size = function.length;

        int x_values[] = new int[neighborhood_size];
        for (int i = 0; i < neighborhood_size; i++) {
            x_values[i] = 0;
        }
        ArrayList<Summand> summand_list = new ArrayList<Summand>();

        for (int i = 0; i < significant_max_array_size; i++) {
            try {
                if (function[i] > 0) {
                    summand_list.addAll(create_function_summands(x_values, function[i], cell_states));
                }
            } catch (Exception e) {
                System.out.println("getSignificantPolynomialRule, System error: " + e);
            }

            for (int j = neighborhood_size - 1; j >= 0; j--) {
                x_values[j]++;
                if (x_values[j] == cell_states) {
                    x_values[j] = 0;
                } else {
                    break;
                }
            }
        }
        // simplify summands
        simplify_summands(summand_list, cell_states);
        sort_summands(summand_list, x_values.length, cell_states);

        boolean first_entry = true;
        for (Summand s : summand_list) {
            if (s.factor == 0) {
                continue;
            }

            if (first_entry) {
                first_entry = false;
            } else if (s.factor > 0) {
                polynomial_rule += "+ ";
            }

            if (s.factor != 1) {
                if (s.factor == -1) {
                    polynomial_rule += "- ";
                } else if (s.factor < 0) {
                    polynomial_rule += "- " + Math.abs(s.factor);
                } else {
                    polynomial_rule += s.factor;
                }

            }
// TODO manchmal wird nur 1 rule berechnet und dann abgebrochen (check all balanced rules)
            boolean at_least_one = false;
            for (int i = 0; i < x_values.length; i++) {
                if (s.power[i] == 0) {
                    continue;
                }
                at_least_one = true;
                polynomial_rule += "x" + (Neighborhood.getNeighborhoodNumber(i) + 1); // i+1
                if (s.power[i] != 1) {
                    polynomial_rule += "^" + s.power[i];
                }
                polynomial_rule += " ";
            }
            if (!at_least_one && (Math.abs(s.factor) == 1)) {
                polynomial_rule += "1 ";
            }
        }

        polynomial_rule = "f = " + polynomial_rule;
        if (first_entry) {
            polynomial_rule = polynomial_rule + "0";
        }

        polynomial_rule = polynomial_rule.trim();
        return polynomial_rule;
    }

    /**
     * Extracts the Wolfram Rule Number out of a polynomial rule string
     * @param polynomial_rule The boolean rule string to be parsed
     * @param significant_max_array_size Number of neighborhood combinations of the function
     * @param cell_states number of cell states
     * @return The Wolfram Rule number
     * @throws java.lang.Exception If there was a parse error, i.e. an invalid polynomial rule
     */
    public static BigInteger extractPolynomialRuleNumber(String polynomial_rule, final int significant_max_array_size, final int cell_states) throws Exception {

        BigInteger rule_nr = BigInteger.ZERO;

        int resulting_function[] = new int[significant_max_array_size];
        for (int i = 0; i < significant_max_array_size; i++) {
            resulting_function[i] = 0;
        }

        polynomial_rule = polynomial_rule.toLowerCase();

        polynomial_rule = polynomial_rule.replace("+", " + ");
        polynomial_rule = polynomial_rule.replace("-", " + - ");
        polynomial_rule = polynomial_rule.replace("=", " = ");
        polynomial_rule = polynomial_rule.replace("*", " ");


        polynomial_rule = polynomial_rule.replaceAll(" ([-]?[0-9]+)", "b$1");
        polynomial_rule = polynomial_rule.replaceAll("\\^([0-9]+)", "c$1");

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
        if (polynomial_rule.charAt(0) == '+') {
            polynomial_rule = polynomial_rule.substring(1);
        }

        String[] or_elements = polynomial_rule.split("\\+");

        ArrayList<ArrayList<PolynomialObject>> p_list = new ArrayList<ArrayList<PolynomialObject>>();

        for (String s : or_elements) {
            if (s.contains("b0") || s.length() == 0) {
                // ignore, is always 0
                continue;
            }

            ArrayList<PolynomialObject> pe_list = new ArrayList<PolynomialObject>();

            int factor = 1;

            String[] b_elements = s.split("[b]");
            for (String bs : b_elements) {

                if (bs.length() == 0) {
                    continue;
                }

                int index_next_x = bs.indexOf("x");
                String value_string = bs;
                if (index_next_x > 0) {
                    value_string = bs.substring(0, index_next_x);
                }

                // TODO konstanter letzter Faktor wird ignoriert! ?

                if (index_next_x == -1) {
                    // last element
                    factor *= Integer.valueOf(value_string);
                    continue;
                }


                // ignore if no x was found
                if (index_next_x == -1) {// && value_string.compareTo(bs) != 0) {
                    continue;
                } else if (index_next_x > 0) {
                    factor *= Integer.valueOf(value_string);
                    bs = bs.substring(index_next_x);
                }

                String[] x_elements = bs.split("[x]");

                for (String x : x_elements) {
                    if (x.length() == 0) {
                        continue;
                    }
                    String[] pow_elements = x.split("[c]");
                    int pow_count = 1;
                    if (pow_elements.length == 2) {
                        pow_count = Integer.valueOf(pow_elements[1]);
                    } else if (pow_elements.length > 2) {
                        throw new Exception("Error too many pows");
                    }
                    for (int k = 0; k < pow_count; k++) {

                        PolynomialObject t = new PolynomialObject();
                        t.index = (Integer.valueOf(pow_elements[0]) - 1);
                        t.is_constant = false;
                        pe_list.add(t);
                    }
                }
                if (factor != 1) {
                    PolynomialObject t = new PolynomialObject();
                    t.index = factor;
                    t.is_constant = true;
                    pe_list.add(t);
                }
            }
            if (pe_list.size() == 0) {
                PolynomialObject t = new PolynomialObject();
                t.index = factor;
                t.is_constant = true;
                pe_list.add(t);
            }
            p_list.add(pe_list);
        }

        // do some basic checking

        int largest_x = 0;
        ArrayList<Integer> element_list = new ArrayList<Integer>();

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

        significant_neighborhood_size = Neighborhood.getSignificantNeighborhoodSize();
        neighborhood_size = Neighborhood.getNeighborhoodSize();

        for (int i = 0; i < significant_max_array_size; i++) {
            // calculate corresponding values of neighborhood values
            int[] x_values = new int[significant_neighborhood_size];
            for (int j = 0; j < significant_neighborhood_size; j++) {
                x_values[j] = 0;
            }
            int j = i;
            for (int k = 0; k < significant_neighborhood_size; k++) {
                x_values[significant_neighborhood_size - 1 - k] = j % cell_states;
                j /= cell_states;
            }
            resulting_function[i] = calculate_polynomial_function(x_values, p_list);
            while (resulting_function[i] < 0) {
                resulting_function[i] += cell_states;
            }
            resulting_function[i] = resulting_function[i] % cell_states;
        }

        for (int i = significant_max_array_size - 1; i >= 0; i--) {
            rule_nr = rule_nr.multiply(BigInteger.valueOf(cell_states));
            rule_nr = rule_nr.add(BigInteger.valueOf(resulting_function[i]));
        }

        return rule_nr;
    }

    /**
     * Calculates the value of the polynomial function for a certain neighborhood
     * @param x_values neighborhood configuration
     * @param p_list list of list of polynomial objects
     * @return function value corresponding to the polynomial rule
     */
    private static int calculate_polynomial_function(int[] x_values, ArrayList<ArrayList<PolynomialObject>> p_list) {
        long result_factor = 0;

        for (ArrayList<PolynomialObject> i : p_list) {
            long current_factor = 1;

            for (PolynomialObject j : i) {
                if (j.is_constant) {
                    current_factor *= j.index;

                } else {
                    current_factor *= x_values[j.index];
                }


            }
            result_factor += current_factor;
        }

        return (int) result_factor;
    }
}
