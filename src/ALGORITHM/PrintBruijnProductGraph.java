package ALGORITHM;

/**
 *
 * @author Clemens Lode, 1151459, University Karlsruhe (TH), clemens@lode.de
 */

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * An instance of BruijnProductGraph contains the information for one node, i.e. its id, its connections to other nodes and its representative value that encodes its position in the Bruijn product graph.
 * Its static part contains a global grid containing all nodes and various statistical variables that track whether the graph is injective or surjective.
 * 
 * The difference to the class FastBruijnProductGraph is that the calculation will not be canceled early when the result is determined.
 * This is done in order to gather all information for a complete picture of the Bruijn product graph.
 */
public class PrintBruijnProductGraph extends BruijnProductGraph {

    private static List<List> stronglyConnectedComponentsList = new LinkedList<List>();

    /**
     * Constructs a single node.
     * @param my_ax_y First of two values that encode the position in the Bruijn product graph. (y coordinate)
     * @param my_ax_x Second of two values that encode the position in the Bruijn product graph. (x coordinate)
     */
    public PrintBruijnProductGraph(int my_ax_y, int my_ax_x) {
        super(my_ax_y, my_ax_x);
    }
    
    /**
     * Node instance generator (implements BruijnProductGraph.createNewNode)
     * @param my_ax_y First of two values that encode the position in the Bruijn product graph. (y coordinate)
     * @param my_ax_x Second of two values that encode the position in the Bruijn product graph. (x coordinate)
     * @return new node
     */
    protected PrintBruijnProductGraph createNewNode(int my_ax_y, int my_ax_x) {
        return new PrintBruijnProductGraph(my_ax_y, my_ax_x);    
    }

    /**
     * Core function to test for injectivity / surjectivity
     * @return always true, we can't cancel early because for the graph we want to visit all nodes
     */
    protected boolean testNodeStronglyConnected() {
        if (low_id == id) {
            // trivial case, last node?
            List<BruijnProductGraph> component = new java.util.LinkedList<BruijnProductGraph>();
            if (this == stack.peek()) {
                stack.pop();
                inStack = false;
                if (ax_x == ax_y) {
                    is_surjective = 0;
                    is_injective = 0;
                }
                component.add(this);
                stronglyConnectedComponentsList.add(component);
            } else {
                // only for debug message
                BruijnProductGraph.max_stack = 128;

                int nonDiagonalElements = Function.getLastElementIndex();
                boolean isDiagonalElement = true;
                BruijnProductGraph w = null;
                do {
                    w = stack.pop();

                    if (isDiagonalElement) {
                        if (w.ax_x == w.ax_y) // diagonale
                        {
                            nonDiagonalElements--;
                        } else {
                            isDiagonalElement = false;
                        }
                    }
                    w.inStack = false;
                    component.add(w);
                } while (w != this);


                if (isDiagonalElement) {
                    // all diagonal number_of_cell_states match? => surjective
                    if (nonDiagonalElements == 0) {
                        is_surjective = 1;
                    } else {
                        is_surjective = 0;
                        is_injective = 0;
                    }
                } else {
                    /* the strongly connected component contains diagonal elements as well as non-diagonal elements
                     * Then the whole diagonal is not a single strongly connected component
                     * and we can cancel early*/

                    if (nonDiagonalElements != Function.getLastElementIndex()) {
                        is_surjective = 0;
                        is_injective = 0;
                    }
                // otherwise it's a component outside the diagonal
                }

// => new strongly conntected component found.			
                stronglyConnectedComponentsList.add(component);
                stronglyConnectedComponents++;
                if ((is_surjective == 1) && (stronglyConnectedComponents > 1)) {
                    is_injective = 0;
                }
            }
        }
        return true;
    }

    /**
     * Checks if corresponding graph entry is null and creates a new node if that's the case
     * @param index index to test
     * @param x x coordinate
     * @param y y coordinate
     */
    protected void check_for_null(int index, int x, int y) {
        if (graph[index] == null) {
            graph[index] = new PrintBruijnProductGraph(x, y);
        }
    }

    /**
     * Traverses through the whole Bruijn product graph to test if it's surjective and/or injective
     * @throws Exception when an internal error occured.
     */
    public static void runConnectionTest() throws Exception {
//	Searching strongly connected components...	
        int k = 0;
        int l = 1;
        final int last_element_index = Function.getLastElementIndex(); 
        
// first check diagonale
        for (int i = 0; i < last_element_index; i++) {
            int index = i * last_element_index + i;
            if (graph[index] == null) {
                graph[index] = new PrintBruijnProductGraph(i, i);
            }

            if (graph[index].id == -1) {
                if (!graph[index].visit()) {
                    break;
                }
            }
        }

        for (int i = 1; i < last_element_index * last_element_index; i++) {
            if (graph[i] == null) {
                graph[i] = new PrintBruijnProductGraph(k, l);
            }
            if (graph[i].id == -1) {
                if (!graph[i].visit()) {
                    break;
                }
            }
            l++;
            if (l >= last_element_index) {
                l = 0;
                k++;
            }
        }
    }

    /**
     * Clears the stack and the bruijn product graph.
     * Needs only to be called after all calculations are done in order to 
     * allow the OS to reuse the memory while the program is idle.
     */
    public static void clearAdditionalMemory() {
        stronglyConnectedComponentsList.clear();
    }

    /**
     * Writes the current Bruijn product graph into a file using the viz file format
     * @param file_name Base file name of the .viz file
     * @throws Exception I/O Error writing file
     */
    public static void printToFile(String file_name) throws Exception {
        FileOutputStream f;
        PrintStream p;
        try {
            f = new FileOutputStream(file_name);
            p = new PrintStream(f);
        } catch (Exception e) {
            throw new Exception("printToFile(): Error opening file [" + e + "]");
        }
        
        final int cell_states = CellStates.getNumberOfCellStates();
        final int last_element_index = Function.getLastElementIndex();        
        
        try {
            p.println("digraph finite_state_machine {");
            p.println("rankdir=LR;");
            p.println("size=\"" + 4 * Function.getMaxArraySize() + "," + 4 * Function.getMaxArraySize() + "\"");
            String color_list[] =
                    {
                "black",
                "blue",
                "brown",
                "green",
                "darkgreen",
                "darkorange",
                "gold",
                "hotpink",
                "purple",
                "turquoise",
                "magenta"
            };

//            boolean found = false;

            int color = 0;
//            if (stronglyConnectedComponents > 0) {
//                found = true;
//            } TODO?

            Iterator it = stronglyConnectedComponentsList.iterator();
            while (it.hasNext()) {
                List component = (List) it.next();
                if (component.size() == 1) {
                    p.print("node [shape = circle, color = " + color_list[color % 11] + "]; ");
                } else {
                    p.print("node [shape = doublecircle, color = " + color_list[color % 11] + "]; ");
                }
                color++;
                Iterator j = component.iterator();
                while (j.hasNext()) {
                    PrintBruijnProductGraph k = (PrintBruijnProductGraph) j.next();

                    if (last_element_index < 26) {
                        p.print(" " + (char) ('A' + k.ax_y));
                    } else {
                        p.print(" " + k.ax_y + "00");
                    }

                    if (last_element_index < 26) {
                        p.print("" + (char) ('A' + k.ax_x));
                    } else {
                        p.print("" + k.ax_x);
                    }
                }
                p.println(";");
            }

            // other nodes that are not strongly connected
            p.println("node [shape = circle, color = gray];");
            

            for (int i = 0; i < last_element_index; i++) {
                for (int j = 0; j < last_element_index; j++) 
                {

                    int axb_i[] = new int[cell_states];
                    int axb_j[] = new int[cell_states];
                    int xb_i[] = new int[cell_states];
                    int xb_j[] = new int[cell_states];

                    for (int b = 0; b < cell_states; b++) {
                        axb_i[b] = graph[i * last_element_index + j].ax_y * cell_states + b;
                        axb_j[b] = graph[i * last_element_index + j].ax_x * cell_states + b;
                        xb_i[b] = axb_i[b] % last_element_index;
                        xb_j[b] = axb_j[b] % last_element_index;
                    }
                    // TODO possible here much of the stack [local variables] can be saved.
                    for (int bi = 0; bi < CellStates.getNumberOfCellStates(); bi++) {
                        for (int bj = 0; bj < CellStates.getNumberOfCellStates(); bj++) {
                            if (Function.getFunction(axb_i[bi]) ==
                                    Function.getFunction(axb_j[bj])) {
                                int index = xb_i[bi] * last_element_index + xb_j[bj];
                                if (last_element_index < 26) {
                                    p.print("" + (char) ('A' + i));
                                } else {
                                    p.print("" + i + "00");
                                }

                                if (last_element_index < 26) {
                                    p.print("" + (char) ('A' + j));
                                } else {
                                    p.print("" + j);
                                }
                                p.print(" -> ");

                                if (last_element_index < 26) {
                                    p.print("" + (char) ('A' + graph[index].ax_y));
                                } else {
                                    p.print("" + graph[index].ax_y + "00");
                                }

                                if (last_element_index < 26) {
                                    p.print("" + (char) ('A' + graph[index].ax_x));
                                } else {
                                    p.print("" + (graph[index].ax_x));
                                }

                                p.println(" [ label = \"" + Function.getFunction(axb_i[bi]) + "\" ];");
                            }
                        }
                    }
                }
            }
            p.println("}");
            p.close();
        } catch (Exception e) {
            throw new Exception("printToFile(): Error writing to file " + file_name + " [" + e + "]");
        }
    }
}
