/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import kendzi.josm.plugin.tomb.dto.PersonModel;
import kendzi.josm.plugin.tomb.util.DateService;

/**
 *
 * @see "http://docs.oracle.com/javase/tutorial/uiswing/components/table.html"
 * 
 * @author Tomasz Kędziora (Kendzi)
 */
public class TombDialogPersonTableModel extends AbstractTableModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    DateService dateService = new DateService();

    private String[] columnNames = {
            tr("Name of person"),
            tr("Date of birth"),
            tr("Date of death"),
            tr("Wikipedia"),
            tr("Description"),
            tr("Lived in"),
            tr("Family name")
    };

    private List<PersonModel> data = new ArrayList<PersonModel>();

    public TombDialogPersonTableModel(List<PersonModel> persons) {
        this.data = persons;
    }

    public int getColumnCount() {
        return this.columnNames.length;
    }

    public int getRowCount() {
        return this.data.size();
    }

    @Override
    public String getColumnName(int col) {
        return this.columnNames[col];
    }

    public String tr(String str) {
        return str;
    }

    //    public void setColumnName(int col, String value) {
    //        this.columnNames[col] = value;
    //    }

    public Object getValueAt(int row, int col) {
        if (row >= getRowCount()) {
            return null;
        }

        PersonModel pm = this.data.get(row);

        switch (col) {
        case 0:
            return pm.getName();
        case 1:
            return this.dateService.dateToVisible(pm.getBorn());
        case 2:
            return this.dateService.dateToVisible(pm.getDied());
        case 3:
            return pm.getWikipedia();
        case 4:
            return pm.getDescription();
        case 5:
            return pm.getLivedIn();
        case 6:
            return pm.getFromFamily();

        default:
            break;
        }

        return null;
        //        return data.get(row)[col];
    }




    @SuppressWarnings("unchecked")
    @Override
    public Class getColumnClass(int c) {

        return String.class;
        //        return getValueAt(0, c).getClass();
    }


    @Override
    public boolean isCellEditable(int row, int col) {

        return true;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        //        data[row][col] = value;
        //        fireTableCellUpdated(row, col);

        //    	if (getRowCount() >= row) {
        //    		return null;
        //    	}

        PersonModel pm = this.data.get(row);

        switch (col) {
        case 0:
            pm.setName((String) value);
            break;
        case 1:
            pm.setBorn(this.dateService.dateToIso((String) value));
            break;
        case 2:
            pm.setDied(this.dateService.dateToIso((String) value));
            break;
        case 3:
            pm.setWikipedia((String) value);
            break;
        case 4:
            pm.setDescription((String) value);
            break;
        case 5:
            pm.setLivedIn((String) value);
            break;
        case 6:
            pm.setFromFamily((String) value);
            break;

        default:
            break;
        }

        fireTableCellUpdated(row, col);
    }

    public synchronized int addPersonModel(PersonModel personModel) {

        this.data.add(personModel);

        int rowId = this.data.size() - 1;


        fireTableRowsInserted(rowId, rowId);

        return rowId;
    }

    public synchronized PersonModel removePersonModel(int[] rowsId) {
        if (rowsId.length == 0) {
            return null;
        }

        int row = rowsId[0];
        PersonModel remove = this.data.remove(row);

        fireTableRowsDeleted(row, row);

        return remove;
    }



}