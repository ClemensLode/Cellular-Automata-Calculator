package ALGORITHM;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * An instance of BruijnProductGraph contains the information for one node, i.e. its id, its connections to other nodes and its representative value that encodes its position in the Bruijn product graph.
 * Its static part contains a global grid containing all nodes and various statistical variables that track whether the graph is injective or surjective.
 */
public class PrintBruijnProductGraph extends BruijnProductGraph
{   
    private static List<List> stronglyConnectedComponentsList = new LinkedList<List>();
    protected static Stack<PrintBruijnProductGraph> stack = new Stack<PrintBruijnProductGraph>();
    protected static PrintBruijnProductGraph net[];
    
    /**
     * Constructs a single node.
     * @param my_ax_y First of two values that encode the position in the Bruijn product graph.
     */
    public PrintBruijnProductGraph(int my_ax_y, int my_ax_x)
    {
        super(my_ax_y, my_ax_x);
    }
     
    public boolean visit() throws Function.FunctionException
    {   
	id = BruijnProductGraph.globalId;
	low_id = id;
	BruijnProductGraph.globalId++;
	inStack = true;

/*
	if((Function.isDebugMessage) && (net.size() >= BruijnProductGraph.max_stack))
	{
		BruijnProductGraph.max_stack *= 2;
		printf("net size: %li\n", BruijnProductGraph.max_stack);	
	}
*/

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
// TODO possible here much of the stack can be saved.
	for(int bi = 0; bi < Function.getNumberOfCellStates(); bi++)
		for(int bj = 0; bj < Function.getNumberOfCellStates(); bj++)
			if(Function.getFunction(axb_i[bi]) == 
                                Function.getFunction(axb_j[bj]))
			{
				int index = xb_i[bi] * Function.getLastElementIndex() + xb_j[bj];
				if(net[index] == null)
					net[index] = new PrintBruijnProductGraph(xb_i[bi], xb_j[bj]);
				if(net[index].id == -1)
				{
					if(!net[index].visit())
						return false;
					low_id = low_id < net[index].low_id ? low_id : net[index].low_id;
				} else
				if(net[index].inStack)
					low_id = low_id < net[index].id ? low_id : net[index].id;
			}
	if(low_id == id)
	{
		// trivial case
		if((stack.size() == 0) || (stack.peek().id < id))
		{
			inStack = false;
		}
		else
		{
			// only for debug message
			BruijnProductGraph.max_stack = 100;
			
			PrintBruijnProductGraph w = stack.pop();
			w.inStack = false;			

                        List<PrintBruijnProductGraph> component = new java.util.LinkedList<PrintBruijnProductGraph>();
                        
                        int diagonalElements = Function.getLastElementIndex();
			boolean isDiagonalElement = true;
			while(w.id > id)
			{
				if(isDiagonalElement)
				{
					if(w.ax_x == w.ax_y) // diagonale
						diagonalElements--;
					else
						isDiagonalElement = false;
				}
				w.inStack = false;
                                component.add(w);
                                if(stack.size() > 0)
				{
					w = stack.pop();					
				} else break;
			} 
// last element (this)

			if(isDiagonalElement)
			{
				if(ax_x == ax_y) // diagonale
					diagonalElements--;
				else
					isDiagonalElement = false;
			}		
			inStack = false;
                        component.add(this);

                        if(isDiagonalElement)
			{
				// all diagonal number_of_cell_states match? => surjective
				if(diagonalElements == 0)
					is_surjective = 1;
				else 
				{
					is_surjective = 0;
					is_injective = 0; 
				}
			}
// => new strongly conntected component found.			
                        stronglyConnectedComponentsList.add(component);
                        stronglyConnectedComponents++;
			if((is_surjective == 1) && (stronglyConnectedComponents > 1))
			{
				is_injective = 0;
			}
		}
	} else
		stack.push(this);
	return true;
    }

    
    public static void runConnectionTest() throws Function.FunctionException
    {
//	Searching strongly connected components...	
	int k = 0;
	int l = 1;

// first check diagonale
	for(int i = 0; i < Function.getLastElementIndex(); i++)
	{
		int index = i * Function.getLastElementIndex() + i;
		if(net[index] == null)
			net[index] = new PrintBruijnProductGraph(i, i);

		if(net[index].id == -1)
			if(!net[index].visit())
				break;
	}

	for(int i = 1; i < Function.getLastElementIndex() * Function.getLastElementIndex(); i++)
	{
		if(net[i] == null)
			net[i] = new PrintBruijnProductGraph(k, l);
		if(net[i].id == -1)
			if(!net[i].visit())
				break;
		l++;
		if(l >= Function.getLastElementIndex())
		{
			l = 0;
			k++;
		}			
	}
    }
    
    public static void initStatic()
    {
        initStaticVariables();

	net = new PrintBruijnProductGraph[Function.getLastElementIndex() * Function.getLastElementIndex()];
	for(int i = 0; i < Function.getLastElementIndex(); i++)
		for(int j = 0; j < Function.getLastElementIndex(); j++)
			 net[i * Function.getLastElementIndex() + j] = null;
    }

    public static void resetStatic()
    {
        resetStaticVariables();
//  	if(Function.isDebugMessage)
//  		printf("Clearing memory [");
	stronglyConnectedComponentsList.clear();
//	if(Function.isDebugMessage)	
//		printf("10%% ");
	stack.clear();
//	if(Function.isDebugMessage)	
//		printf("20%% ");
	int last_percent = 10;
	for(int i = 0; i < Function.getLastElementIndex(); i++)
	{
/*		if(Function.isDebugMessage)		
		{
			if((i * 70 / Function.getLastElementIndex()) >= last_percent)
			{
				printf("%i%% ", last_percent+20);
				last_percent+=10;
			}
		}*/
//		for(int j = 0; j < Function.getLastElementIndex(); j++)	
//			 delete net[i*Function.getLastElementIndex()+j];
	}
//	if(Function.isDebugMessage)
//		printf("90%% ");
        net = null;
//	if(Function.isDebugMessage)	
//		printf("100%%]\n");
    }

    /**
     * Writes the current Bruijn product graph into a file using the viz file format
     * @param file_name Base file name of the .viz file
     */
public static void printToFile(String file_name)
{
        FileOutputStream f; 
        PrintStream p; 
        try { 
            f = new FileOutputStream(file_name); 
            p = new PrintStream( f );
        } catch (Exception e) 
        {
            System.err.println ("Error opening file " + file_name); 
            return;
        }
        try {
            p.println( "digraph finite_state_machine {");
            p.println( "rankdir=LR;");
            p.println( "size=\"" + 4*Function.getMaxArraySize() + "," + 4*Function.getMaxArraySize() + "\"");
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

            boolean found = false;

            int color = 0;
            if(stronglyConnectedComponents>0)
                    found = true;

            Iterator it = stronglyConnectedComponentsList.iterator();
            while(it.hasNext())
            {
                List component = (List)it.next();
                p.print( "node [shape = doublecircle, color = " + color_list[color%11] + "]; ");
                color++;
                Iterator j = component.iterator();
                while(j.hasNext())
                {
                    PrintBruijnProductGraph k = (PrintBruijnProductGraph)j.next();

                    if(Function.getLastElementIndex() < 26)
                        p.print( " " + (char)('A' + k.ax_y));
                    else
                        p.print( " " + k.ax_y + "00");

                    if(Function.getLastElementIndex() < 26)
                        p.print( "" + (char)('A' + k.ax_x));
                    else
                        p.print( "" + k.ax_x);
                }
                p.println( ";");                    
            }

            // other nodes that are not strongly connected
            p.println( "node [shape = circle, color = gray];");

            for(int i = 0; i < Function.getLastElementIndex(); i++)		 
                for(int j = 0; j < Function.getLastElementIndex(); j++)				 
//			if(net[i*Function.getLastElementIndex() + j] != null) // TODO?
                {

                    int axb_i[] = new int[Function.getNumberOfCellStates()];
                    int axb_j[] = new int[Function.getNumberOfCellStates()];
                    int xb_i[] = new int[Function.getNumberOfCellStates()];
                    int xb_j[] = new int[Function.getNumberOfCellStates()];

                    for(int b = 0; b < Function.getNumberOfCellStates(); b++)
                    {
                            axb_i[b] = net[i*Function.getLastElementIndex() + j].ax_y * Function.getNumberOfCellStates() + b;
                            axb_j[b] = net[i*Function.getLastElementIndex() + j].ax_x * Function.getNumberOfCellStates() + b;
                            xb_i[b] = axb_i[b] % Function.getLastElementIndex();
                            xb_j[b] = axb_j[b] % Function.getLastElementIndex();
                    }
            // TODO possible here much of the stack [local variables] can be saved.
                    for(int bi = 0; bi < Function.getNumberOfCellStates(); bi++)
                            for(int bj = 0; bj < Function.getNumberOfCellStates(); bj++)
                                    if(Function.getFunction(axb_i[bi]) == 
                                            Function.getFunction(axb_j[bj]))
                                    {
                                            int index = xb_i[bi] * Function.getLastElementIndex() + xb_j[bj];
                                                    if(Function.getLastElementIndex() < 26)
                                                            p.print( "" + (char)('A' + i));
                                                    else
                                                            p.print( "" + i + "00");

                                                    if(Function.getLastElementIndex() < 26)
                                                            p.print( "" + (char)('A' + j));
                                                    else
                                                            p.print( "" + j);
                                                    p.print( " -> ");

                                                    if(Function.getLastElementIndex() < 26)
                                                            p.print( "" + (char)('A' + net[index].ax_y));
                                                    else
                                                            p.print( "" + net[index].ax_y + "00");

                                                    if(Function.getLastElementIndex() < 26)
                                                            p.print( "" + (char)('A' + net[index].ax_x));
                                                    else
                                                            p.print( "" + (net[index].ax_x));	

                                                    p.println( " [ label = \"" + Function.getFunction(axb_i[bi]) + "\" ];");
                                     }
                }
            p.println( "}");
            p.close(); 

		//printf("Calling %s...\n", os.str().c_str());
//                System.
//		system("dot -Tpng -O " + file_name); TODO
                
        } catch (Exception e) 
        { 
            System.err.println ("Error writing to file " + file_name); 
        } 
    }
}