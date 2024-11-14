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

import kendzi.josm.plugin.tomb.dto.PersonSearchDto;
import kendzi.josm.plugin.tomb.util.DateService;

/**
 *
 * @see "http://docs.oracle.com/javase/tutorial/uiswing/components/table.html"
 * 
 * @author Tomasz Kędziora (Kendzi)
 */
public class PersonSearchTableModel extends AbstractTableModel {

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
            tr("Wikidata"),
            tr("Description"),
            tr("Lived in"),
            tr("Family name")
            tr("Birth place"),
            tr("Death place"),
    };

    private List<PersonSearchDto> data = new ArrayList<PersonSearchDto>();

    public PersonSearchTableModel(List<PersonSearchDto> persons) {
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

    public Object getValueAt(int row, int col) {
        if (row >= getRowCount()) {
            return null;
        }

        PersonSearchDto pm = this.data.get(row);

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
        case 7:
            return this.dateService.dateToVisible(pm.getBirthplace());
        case 8:
            return this.dateService.dateToVisible(pm.getDeathplace());
     

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

        return false;
    }

    public Long relationIdForRow(int rowId) {

        PersonSearchDto person = this.data.get(rowId);

        if (person != null) {
            return person.getId();
        }

        return null;
    }

}
