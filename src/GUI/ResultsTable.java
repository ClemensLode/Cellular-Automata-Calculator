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

class ResultsTable extends AbstractTableModel
{
    /** Column names */
    private String[] columnNames = {
        "Significant Rule", "Neighborhood", "Neighborhood size", "Significant Neighborhood size", "Cell States", "Injective", "Surjective", "Has Graph"
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
        Object[] array = (Object[])(datas.elementAt(row));
        return array[col];
    }
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    public void addRow(Object[] row) {
        datas.add(row);
    }
    
    public Object[] getRow(int row) {
        return (Object[])datas.get(row);
    }
}