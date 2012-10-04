package kendzi.josm.plugin.tomb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

// http://docs.oracle.com/javase/tutorial/uiswing/components/table.html
class PersonTableModel extends AbstractTableModel {

    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat v1Format = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat v2Format = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat v3Format = new SimpleDateFormat("dd/MM/yyyy");


    private String[] columnNames = {"Name", "Birth", "Death", "Wikipedia", "Description"};
    private List<PersonModel> data = new ArrayList<PersonModel>();

    public PersonTableModel(List<PersonModel> persons) {
        data = persons;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        if (row >= getRowCount()) {
            return null;
        }

        PersonModel pm = data.get(row);

        switch (col) {
        case 0:
            return pm.getName();
        case 1:
            return dateToVisible(pm.getBirth());
        case 2:
            return dateToVisible(pm.getDeath());
        case 3:
            return pm.getWikipedia();
        case 4:
            return pm.getDescription();

        default:
            break;
        }

        return null;
        //        return data.get(row)[col];
    }

    String convertDateFormat(String str, SimpleDateFormat inFormat, SimpleDateFormat outFormat) {

        Date parse = parse(str, inFormat);
        if (parse == null) {
            return null;
        }

        String back = format(parse, inFormat);
        if (back == null) {
            return null;
        }
        if (!str.endsWith(back)) {
            return null;
        }

        return format(parse, outFormat);
    }

    String dateToIso(String str) {
        if (str == null) {
            return null;
        }

        str = str.trim();

        String ret = null;

        ret = convertDateFormat(str, v1Format, isoFormat);
        if (ret != null) {
            return ret;
        }

        ret = convertDateFormat(str, v2Format, isoFormat);
        if (ret != null) {
            return ret;
        }

        ret = convertDateFormat(str, v3Format, isoFormat);
        if (ret != null) {
            return ret;
        }
        return str;
    }

    String dateToVisible(String str) {
        if (str == null) {
            return null;
        }

        str = str.trim();

        SimpleDateFormat outFormat = v2Format;

        String ret = null;

        ret = convertDateFormat(str, isoFormat, outFormat);
        if (ret != null) {
            return ret;
        }

        ret = convertDateFormat(str, v1Format, outFormat);
        if (ret != null) {
            return ret;
        }

        ret = convertDateFormat(str, v3Format, outFormat);
        if (ret != null) {
            return ret;
        }

        return str;
    }

    private String format(Date date, SimpleDateFormat format) {
        try {
            return format.format(date);
        } catch (Exception e) {
            //
        }
        return null;
    }

    Date parse(String in, SimpleDateFormat format) {
        try {
            return format.parse(in);
        } catch (Exception e) {
            //
        }
        return null;
    }


    @Override
    public Class getColumnClass(int c) {

        return String.class;
        //        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        //        if (col < 2) {
        //            return false;
        //        } else {
        return true;
        //        }
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

        PersonModel pm = data.get(row);

        switch (col) {
        case 0:
            pm.setName((String) value);
            break;
        case 1:
            pm.setBirth(dateToIso((String) value));
            break;
        case 2:
            pm.setDeath(dateToIso((String) value));
            break;
        case 3:
            pm.setWikipedia((String) value);
            break;
        case 4:
            pm.setDescription((String) value);
            break;

        default:
            break;
        }

        fireTableCellUpdated(row, col);
    }

    public int addPersonModel(PersonModel personModel) {

        data.add(personModel);

        int rowId = data.size() - 1;


        fireTableRowsInserted(rowId, rowId);

        return rowId;
    }

    public PersonModel removePersonModel(int[] rowsId) {
        if (rowsId.length == 0) {
            return null;
        }

        int row = rowsId[0];
        PersonModel remove = data.remove(row);

        fireTableRowsDeleted(row, row);

        return remove;
    }



}