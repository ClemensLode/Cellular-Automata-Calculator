/*
 * ResultsTable.java
 *
 * Created on February 17, 2007, 10:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GUI;

import javax.swing.table.*;
import java.util.*;
import java.math.BigInteger;

class ResultsTable extends AbstractTableModel
{
    /** Column names */
    public static String[] columnNames = {
            "Significant Rule", "Neighborhood", "Neighborhood size", "Significant Neighborhood size", "Cell States", "Injective", "Surjective", "Graph", "Image"
        }; 
            
   /** Vector of Object[], this are the datas of the table */
    Vector datas = new Vector();    
    
    public int getColumnCount () {
        return columnNames.length;
    }
    public int getRowCount () {
        return datas.size();
    }
    public String getColumnName(int col) {
        return columnNames[col];
    }
    public Object getValueAt (int row, int col) {
        Object[] array;
        do {
         array = (Object[])(datas.elementAt(row));
        } while(array.length <= 8);
        return array[col];
    }
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    public void setHasGraph(int row) {
        Object[] array;
        do {
            array = (Object[])(datas.elementAt(row));
        } while(array.length <= 8);
        array[7] = new Boolean(true);
        datas.setElementAt(array,row);
    }
    
    public void setHasImage(int row) {
        Object[] array;
        do {
            array = (Object[])(datas.elementAt(row));
        } while(array.length <= 8);
        array[8] = new Boolean(true);
        datas.setElementAt(array,row);
    }    
    
    public boolean hasGraph(int row) {
        return (Boolean)getValueAt(row, 7);
    }
    public boolean hasImage(int row) {
        return (Boolean)getValueAt(row, 8);
    }    
    
    public void addRow(Object[] row) {
        datas.add(row);
    }
    
    public Object[] getRow(int row) {
        return (Object[])datas.get(row);
    }
    public boolean contains(Object[] r) {
        if(datas.size() == 0)
            return false;
        
        for(int row = 0; row < datas.size(); row++) {
            BigInteger t0 = (BigInteger)getValueAt(row, 0);
            if(!t0.equals((BigInteger)r[0])) {
                return false;
            }
            String t1 = (String)getValueAt(row, 1);
            if(t1.compareTo((String)r[1]) != 0) {
                return false;
            }
            Integer t2 = (Integer)getValueAt(row, 2);
            if(t2.compareTo((Integer)r[2]) != 0) {
                return false;
            }
            Integer t3 = (Integer)getValueAt(row, 3);
            if(t3.compareTo((Integer)r[3]) != 0) {
                return false;
            }
            Integer t4 = (Integer)getValueAt(row, 4);
            if(t4.compareTo((Integer)r[4]) != 0) {
                return false;
            }
            Boolean t5 = (Boolean)getValueAt(row, 5);
            if(t5.compareTo((Boolean)r[5]) != 0) {
                return false;
            }
        }
        return true;
    }
}