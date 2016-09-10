package GUI;

/**
 *
 * @author Clemens Lode, 1151459, University Karlsruhe (TH), clemens@lode.de
 */

import javax.swing.table.AbstractTableModel;
import java.util.Vector;
import java.math.BigInteger;

public class ResultsTable extends AbstractTableModel
{
    /** Column names */
    public final static String[] columnNames = {
            "Significant Rule", 
            "Boolean Representation", 
            "Polynomial Representation", 
            "Neighborhood", 
            "Neighborhood size", 
            "Significant Neighborhood size", 
            "Cell States", 
            "Injective", 
            "Surjective", 
            "Graph", 
            "Image",
            "Simulation"
        }; 
    public final static int GRAPH_INDEX = columnNames.length - 3;
    public final static int IMAGE_INDEX = columnNames.length - 2;
    public final static int SIMULATOR_INDEX = columnNames.length - 1;
    public final static int COLUMN_COUNT = columnNames.length;
            
   /** Vector of Object[], this are the datas of the table */
    Vector datas = new Vector();    
    
    public int getColumnCount () {
        return columnNames.length;
    }
    
    public int getRowCount () {
        return datas.size();
    }
    
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    public Object getValueAt (int row, int col) {
        Object[] array;
        do {
         array = (Object[])(datas.elementAt(row));
        } while(array.length < columnNames.length);
        return array[col];
    }
    
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    public void setHasGraph(int row) {
        Object[] array;
        do {
            array = (Object[])(datas.elementAt(row));
        } while(array.length < columnNames.length);
        array[columnNames.length - 3] = new Boolean(true);
        datas.setElementAt(array,row);
    }
    
    public void setHasImage(int row) {
        Object[] array;
        do {
            array = (Object[])(datas.elementAt(row));
        } while(array.length < columnNames.length);
        array[IMAGE_INDEX] = new Boolean(true);
        datas.setElementAt(array,row);
    }    
    public void setHasSimulation(int row) {
        Object[] array;
        do {
            array = (Object[])(datas.elementAt(row));
        } while(array.length < columnNames.length);
        array[columnNames.length - 1] = new Boolean(true);
        datas.setElementAt(array,row);
    }    
    
    public boolean hasGraph(int row) {
        return (Boolean)getValueAt(row, GRAPH_INDEX);
    }
    
    public boolean hasImage(int row) {
        return (Boolean)getValueAt(row, IMAGE_INDEX);
    }    
    
    public boolean hasSimulation(int row) {
        return (Boolean)getValueAt(row, SIMULATOR_INDEX);
    }
    
    public void addRow(Object[] row) {
        datas.add(row);
    }
    
    public Object[] getRow(int row) {
        return (Object[])datas.get(row);
    }
    
    /**
     * Searchs through the database if it contains the object array
     * @param r object array
     * @return -1 if the entry was not found, otherwise the index of the entry
     */
    public int contains(Object[] r) {
        if(datas.size() == 0) {
            return -1;
        }
        
        for(int row = 0; row < datas.size(); row++) {
            BigInteger t0 = (BigInteger)getValueAt(row, 0);
            if(!t0.equals((BigInteger)r[0])) {
                continue;
               // return false; ?
            }
            
            String t3 = (String)getValueAt(row, 3);
            if(t3.compareTo((String)r[3]) != 0) {
                continue;
            }
            Integer t4 = (Integer)getValueAt(row, 4);
            if(t4.compareTo((Integer)r[4]) != 0) {
                continue;
            }
            Integer t5 = (Integer)getValueAt(row, 5);
            if(t5.compareTo((Integer)r[5]) != 0) {
                continue;
            }
            Integer t6 = (Integer)getValueAt(row, 6);
            if(t6.compareTo((Integer)r[6]) != 0) {
                continue;
            }  
            return row;
        }
        return -1;
    }
}