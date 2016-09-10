package ALGORITHM;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * An instance of BruijnProductGraph contains the information for one node, i.e. its id, its connections to other nodes and its representative value that encodes its position in the Bruijn product graph.
 * Its static part contains a global grid containing all nodes and various statistical variables that track whether the graph is injective or surjective.
 *
 * The difference to the class PrintBruijnProductGraph is that the calculation will be canceled early when the result is determined.
 */
public class FastBruijnProductGraph extends BruijnProductGraph {

    /**
     * Constructs a single node.
     * @param my_ax_y First of two values that encode the position in the Bruijn product graph. (y coordinate)
     * @param my_ax_x Second of two values that encode the position in the Bruijn product graph. (x coordinate)
     */
    public FastBruijnProductGraph(int my_ax_y, int my_ax_x) {
        super(my_ax_y, my_ax_x);
    }
    
    protected FastBruijnProductGraph createNewNode(int my_ax_y, int my_ax_x) {
        return new FastBruijnProductGraph(my_ax_y, my_ax_x);    
    }
    
    protected boolean testNodeStronglyConnected() 
    {
        // is this the root node?
        if(low_id == id) {
            /* Ignore the trivial case (a node that is not connected to any other node).
               If this is a diagonal element we can cancel early */
            if(this == stack.peek()) {
                stack.pop();
                inStack = false;
                if(ax_x == ax_y) {
                    is_surjective = 0;
                    is_injective = 0;
                    return false;                
                }
            } else {
                // reset max_stack counter, only important for communication with the GUI
                max_stack = 128;
                
                        /* diagonalElements represent the number of elements of this strongly connected component
                         *that are not diagonal elements*/
                int nonDiagonalElements = Function.getLastElementIndex();
                boolean isDiagonalElement = true;

                /* traverse through the stack */
                BruijnProductGraph w = null;
                do
                {
                    w = stack.pop();
                    if(isDiagonalElement) {
                        if(w.ax_x == w.ax_y) // diagonale
                            nonDiagonalElements--;
                        else
                            isDiagonalElement = false;
                    }
                    w.inStack = false;
                } while(w != this);
                
                // all tested elements were part of the diagonal?
                if(isDiagonalElement) {
                    // were all elements of the diagonal part of the strongly connected component?
                    if(nonDiagonalElements == 0)
                        is_surjective = 1;
                            /* Only a part of the diagonal is a part of a strongly connected component?
                             * Then the whole diagonal is not a single strongly connected component
                             * and we can cancel early*/
                    else {
                        is_surjective = 0;
                        is_injective = 0;
                        return false;
                    }
                } 
                // none or not all elements were part of the diagonal
                else 
                {
                    /* the strongly connected component contains diagonal elements as well as non-diagonal elements
                     * Then the whole diagonal is not a single strongly connected component
                     * and we can cancel early*/
                    
                    if(nonDiagonalElements != Function.getLastElementIndex())
                    {
                        is_surjective = 0;
                        is_injective = 0;
                        return false;
                    }
                    // otherwise it's a component outside the diagonal
                }
                
                stronglyConnectedComponents++;
                        /* we can cancel the calculation early if we know that the diagonal is a strongly
                         * connected component and that there is at least another strongly connected component */
                if((is_surjective == 1) && (stronglyConnectedComponents > 1)) {
                    is_injective = 0;
                    return false;
                }
                
            }
        }
        return true;
}
    
    /**
     * Traverses through the whole Bruijn product graph to test if it's surjective and/or injective
     * @throws Exception when an internal error occured.
     */
    public static void runConnectionTest() throws Exception {
//      Searching strongly connected components...
        int k = 0;
        int l = 1;
        
// first check diagonal, if the diagonal isn't a strongly connected component we can cancel the calculation early
        for(int i = 0; i < Function.getLastElementIndex(); i++) {
            int index = i * Function.getLastElementIndex() + i;
            if(graph[index] == null)
                graph[index] = new FastBruijnProductGraph(i, i);
            
            if(graph[index].id == -1)
                if(!graph[index].visit())
                    break;
        }
        
        for(int i = 1; i < Function.getLastElementIndex() * Function.getLastElementIndex(); i++) {
            if(graph[i] == null) {
                graph[i] = new FastBruijnProductGraph(k, l);
            }
            if(graph[i].id == -1)
                if(!graph[i].visit())
                    break;
            l++;
            if(l >= Function.getLastElementIndex()) {
                l = 0;
                k++;
            }
        }
    }
    

}