// TODO first: check the diagonale (and only the diagonal)

package ALGORITHM;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * An instance of BruijnProductGraph contains the information for one node, i.e. its id, its connections to other nodes and its representative value that encodes its position in the Bruijn product graph.
 * Its static part contains a global grid containing all nodes and various statistical variables that track whether the graph is injective or surjective.
 */
abstract public class BruijnProductGraph
{
    protected int ax_y;
    protected int ax_x;
    protected int id;
    protected int low_id;
    protected boolean inStack;

    protected static int globalId;
    protected static int is_surjective;
    protected static int is_injective;
    protected static int max_stack;
    protected static int stack_size;
    protected static int stronglyConnectedComponents;
    /**
     * Constructs a single node.
     * @param my_ax_y First of two values that encode the position in the Bruijn product graph.
     */
    protected BruijnProductGraph(int my_ax_y, int my_ax_x)
    {
            ax_y = my_ax_y;
            ax_x = my_ax_x;
            id = -1;
            low_id = -1;
            inStack = false;
    }
    private BruijnProductGraph()
    {}
    
    protected static void initStaticVariables()
    {
        globalId = 0;
	is_surjective = -1;
	is_injective = -1;
	max_stack = 100;
        stack_size = 0;
    }
    
    protected static void resetStaticVariables()
    {
        globalId = 0;
        stronglyConnectedComponents = 0;
    }
        
    public static boolean isInjective()
    {
        if(is_surjective <= 0)
            return false;

        if(is_injective != -1)
            return (is_injective == 1);

        if(stronglyConnectedComponents == 1)
            return true;
        else return false;
    }    

    public static boolean isSurjective()
    {
        if(is_surjective != -1)
            return (is_surjective == 1);
        return false;
    }   
}