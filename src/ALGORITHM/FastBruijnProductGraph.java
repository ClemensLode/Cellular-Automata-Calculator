package ALGORITHM;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * An instance of BruijnProductGraph contains the information for one node, i.e. its id, its connections to other nodes and its representative value that encodes its position in the Bruijn product graph.
 * Its static part contains a global grid containing all nodes and various statistical variables that track whether the graph is injective or surjective.
 */
public class FastBruijnProductGraph extends BruijnProductGraph
{
    protected static Stack<FastBruijnProductGraph> stack = new Stack<FastBruijnProductGraph>();
    protected static FastBruijnProductGraph net[];

    /**
     * Constructs a single node.
     * @param my_ax_y First of two values that encode the position in the Bruijn product graph.
     */
    public FastBruijnProductGraph(int my_ax_y, int my_ax_x)
    {
        super(my_ax_y, my_ax_x);
    }
        
    public boolean visit() throws Function.FunctionException
    {   
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

	for(int bi = 0; bi < Function.getNumberOfCellStates(); bi++)
		for(int bj = 0; bj < Function.getNumberOfCellStates(); bj++)
			if(Function.getFunction(axb_i[bi]) == 
                                Function.getFunction(axb_j[bj]))
			{
				int index = xb_i[bi] * Function.getLastElementIndex() + xb_j[bj];
				if(net[index] == null)
					net[index] = new FastBruijnProductGraph(xb_i[bi], xb_j[bj]);
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
			max_stack = 100;
			
			FastBruijnProductGraph w = stack.pop();
			w.inStack = false;			

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

                        if(isDiagonalElement)
			{
				// all diagonal number_of_cell_states match? => surjective
				if(diagonalElements == 0)
					is_surjective = 1;
				else 
				{
					is_surjective = 0;
					is_injective = 0; 
// We cannot cancel the calculation early, we want all nodes to be calculated and later printed
					return false;
				}
			}
			
			stronglyConnectedComponents++;
			if((is_surjective == 1) && (stronglyConnectedComponents > 1))
			{
				is_injective = 0;
				return false;
			}

		}
	} else
		stack.push(this);
	return true;
    }
    
    public static void runConnectionTest() throws Function.FunctionException
    {
//      Searching strongly connected components...
	int k = 0;
	int l = 1;

// first check diagonale
	for(int i = 0; i < Function.getLastElementIndex(); i++)
	{
		int index = i * Function.getLastElementIndex() + i;
		if(net[index] == null)
			net[index] = new FastBruijnProductGraph(i, i);

		if(net[index].id == -1)
			if(!net[index].visit())
				break;
	}

	for(int i = 1; i < Function.getLastElementIndex() * Function.getLastElementIndex(); i++)
	{
		if(net[i] == null)
			net[i] = new FastBruijnProductGraph(k, l);
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

	net = new FastBruijnProductGraph[Function.getCurrentProblemSize()];

	for(int i = 0; i < Function.getLastElementIndex(); i++)
		for(int j = 0; j < Function.getLastElementIndex(); j++)
			 net[i * Function.getLastElementIndex() + j] = null;
    }

//       Clearing memory
    public static void resetStatic()
    {
	resetStaticVariables();
	stack.clear();
	int last_percent = 10;
        net = null;
    }

}