package kendzi.josm.plugin.tomb.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.ListSelectionModel;

import kendzi.josm.plugin.tomb.dto.PersonSearchDto;
import kendzi.josm.plugin.tomb.service.SearchPersonRelationService;

import org.openstreetmap.josm.tools.I18n;

public class PersonSearchDialogAction extends PersonSearchDialog {

    SearchPersonRelationService searchPersonRelationService = new SearchPersonRelationService();
    private PersonSearchTableModel personTableModel = new PersonSearchTableModel(new ArrayList<PersonSearchDto>());
    private Long relationId;

    public PersonSearchDialogAction() {
        getTable().setModel(personTableModel);
        getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public Long getSelectedRelationId() {
        return relationId;
    }

    @Override
    protected void doSearch() {
        String name = getTxtName().getText();

        try {
            List<PersonSearchDto> personList = this.searchPersonRelationService.findPerson(name);

            fillPersonsTable(personList);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }

    @Override
    protected void doOk(int[] rowId) {
        //        if (validate(rowId)) {
        //            relationId =
        //        }
        if (rowId.length == 1) {
            Long relationId = personTableModel.relationIdForRow(rowId[0]);

            if (relationId != null) {
                this.relationId = relationId;
                dispose();
                return;
            }
        }

        // XXX message

    }

    private void fillPersonsTable(List<PersonSearchDto> persons) {

        personTableModel = new PersonSearchTableModel(persons) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public String tr(String str) {
                return I18n.tr(str);
            }
        };

        this.getTable().setModel(personTableModel);

        //        personsTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        //        personsTable.getColumnModel().getColumn(2).setPreferredWidth(30);
    }


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            PersonSearchDialogAction dialog = new PersonSearchDialogAction();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setModal(true);
            dialog.setVisible(true);

            System.out.println(dialog.getSelectedRelationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
