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
    /**
     * y position in the Bruijn product automat
     */
    protected int ax_y;
    /**
     * x position in the Bruijn product automat
     */
    protected int ax_x;
    /**
     * unique node identifier
     */
    protected int id;
    /**
     * identifier of the root node
     */
    protected int low_id;
    /**
     * true if this node is in the stack
     */
    protected boolean inStack;

    /**
     * static counter to create unique ids for the nodes
     */
    protected static int globalId;
    /**
     * -1 (not initialized) or 0: not surjective (and not injective)
     * 1: surjective
     */
    protected static int is_surjective;
    /**
     * -1 (not initialized) or 0: not injective
     * 1: injective (and surjective)
     */
    protected static int is_injective;
    /**
     * Internal counter that calls the update function (of the GUI) whenever globalId becomes greater than max_stack.
     * When this happens max_stack is multiplied by 2.
     */
    protected static int max_stack;
    /**
     * Number of strongly connected components that were found during the calculation.
     */
    protected static int stronglyConnectedComponents;
    /**
     * Constructs a single node.
     * @param my_ax_y First of two values that encode the position in the Bruijn product graph. (y position)
     * @param my_ax_x Second of two values that encode the position in the Bruijn product graph. (x positon)
     */
    
    /**
     * The stack contains all elements that are part of the current possibly strongly connected component.
     */
    protected static Stack<BruijnProductGraph> stack = new Stack<BruijnProductGraph>();
    /**
     * Array that holds references to the nodes of the Bruijn product graph.
     * Necessary to identify nodes by their ax_x and ax_y values.
     */
    protected static BruijnProductGraph graph[];
    
    
    protected BruijnProductGraph(int my_ax_y, int my_ax_x)
    {
            ax_y = my_ax_y;
            ax_x = my_ax_x;
            id = -1;
            low_id = -1;
            inStack = false;
    }
    
    /**
     * Safeguard to deny the creation of inherited classes without initialized ax_x and ax_y values
     */
    private BruijnProductGraph()
    {}
    
    protected abstract boolean testNodeStronglyConnected();
    protected abstract BruijnProductGraph createNewNode(int my_ax_y, int my_ax_x);
    
    /**
     * 'Visits' a single node.
     * This is the main function to search for strongly connected components in the Bruijn product graph.
     * It is based on Tarjan's algorithm. As we only need to test if the diagonal of the product graph (ax_x == ax_y) is
     * a atrongly connected component and if there is at least one other strongly connected component we can optimize quite a bit.
     * The algorithm basically traverses through the graph until it reaches a node that was already looked at.
     * @return false if it is already determined if the configuration is injective, only surjective or not surjective/injective.
     * true if we have to test further nodes
     * @throws Exception when a neighborhood configuration was created that is outside the range of the current configuration (only internal error)
     */
    public boolean visit() throws Exception 
    {
        initNode();
        stack.push(this);

/* 'axb' will contain the neighborhood configuration that 'connects' two nodes that 
 correspond to the first n-1 positions (ax) and respectivly the last n-1 positions (xb) */
        int axb_i[] = new int[Function.getNumberOfCellStates()];
        int axb_j[] = new int[Function.getNumberOfCellStates()];
        int xb_i[] = new int[Function.getNumberOfCellStates()];
        int xb_j[] = new int[Function.getNumberOfCellStates()];
        
        for(int b = 0; b < Function.getNumberOfCellStates(); b++) 
        {
            axb_i[b] = ax_y * Function.getNumberOfCellStates() + b;
            axb_j[b] = ax_x * Function.getNumberOfCellStates() + b;
            xb_i[b] = axb_i[b] % Function.getLastElementIndex();
            xb_j[b] = axb_j[b] % Function.getLastElementIndex();
        }
 
// Test the (numberOfCellStates)^2 possible connections if the nodes correspond in 'axb'
        for(int bi = 0; bi < Function.getNumberOfCellStates(); bi++)
            for(int bj = 0; bj < Function.getNumberOfCellStates(); bj++)
                if(Function.getFunction(axb_i[bi]) ==
                Function.getFunction(axb_j[bj])) {
            // calculate the index of the connected node
            int index = xb_i[bi] * Function.getLastElementIndex() + xb_j[bj];
            
            if(graph[index] == null) {
                graph[index] = createNewNode(xb_i[bi], xb_j[bj]);
            }
            
            // visit the connected node
            if(graph[index].id == -1) {
                if(!graph[index].visit())
                    return false;
                // update the root node id
                low_id = low_id < graph[index].low_id ? low_id : graph[index].low_id;
            } else
                // node was already visited and still marked? (i.e. not a part of a different strongly connected component)
                if(graph[index].inStack)
                    low_id = low_id < graph[index].id ? low_id : graph[index].id;
                }
        
        return testNodeStronglyConnected();
    }
    

    /**
     * Assigns the node id, tests if an update is necessary and marks the node
     */
    protected void initNode() {
    	id = BruijnProductGraph.globalId;
	low_id = id;
	BruijnProductGraph.globalId++;
	inStack = true;
        if(Function.singleCalculation)
        {
            if(BruijnProductGraph.globalId >= BruijnProductGraph.max_stack)
            {
                Function.callback.updateBar(BruijnProductGraph.max_stack);
                BruijnProductGraph.max_stack *= 2;
            }
        }
    }
    

    /**
     * Reallocates the memory if necessary and resets the static variables.
     * Needs to be called before each single calculation
     */
    public static void initStatic() {
        globalId = 0;
	is_surjective = -1;
	is_injective = -1;
	max_stack = 128;
        stronglyConnectedComponents = 0;
        int size = Function.getCurrentProblemSize();
// only reallocate the memory if the problem size has changed
        if((graph == null) || (graph.length != size)) {
            graph = new BruijnProductGraph[size];
        }
        Arrays.fill(graph, null);        
    }    
    
   /**
     * Clears the stack and the bruijn product graph.
     * Needs only to be called after all calculations are done in order to 
     * allow the OS to reuse the memory while the program is idle.
     */
    public static void clearMemory() {
        stack.clear();
        graph = null;
    }    
        
    /**
     * Determines on base of stronglyConnectedComponents, is_surjective and is_injective if the current configuration is injective
     * @return true if current configuration is injective
     */
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

    /**
     * Determines on base of is_surjective if the current configuration is injective
     * @return true if current configuration is surjective
     */
    public static boolean isSurjective()
    {
        if(is_surjective != -1)
            return (is_surjective == 1);
        return false;
    }   
}