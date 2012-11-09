package kendzi.josm.plugin.tomb.ui;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import kendzi.josm.plugin.tomb.dto.PersonSearchDto;
import kendzi.josm.plugin.tomb.service.SearchPersonRelationService;

import org.openstreetmap.josm.tools.I18n;
import org.openstreetmap.josm.tools.ImageProvider;

public class PersonSearchDialogAction extends PersonSearchDialog {

    SearchPersonRelationService searchPersonRelationService = new SearchPersonRelationService();
    private PersonSearchTableModel personTableModel;
    private Long relationId;

    public PersonSearchDialogAction() {

        setupPersonTable(new PersonSearchTableModel(new ArrayList<PersonSearchDto>()));

        loadIcon();
    }


    /**
     * @param personSearchTableModel
     */
    public void setupPersonTable(PersonSearchTableModel personSearchTableModel) {

        this.personTableModel = personSearchTableModel;

        getTable().setModel(this.personTableModel);
        getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        getTable().getColumnModel().getColumn(1).setPreferredWidth(30);
        getTable().getColumnModel().getColumn(2).setPreferredWidth(30);
    }

    /**
     * 
     */
    private void loadIcon() {

        ImageIcon imageIcon = ImageProvider.get("cc2/person_search_icon.png");

        if (imageIcon != null) {
            setIconImage(imageIcon.getImage());
        }
    }

    public Long getSelectedRelationId() {
        return this.relationId;
    }

    @Override
    protected void doSearch() {
        String name = getTxtName().getText();

        try {
            List<PersonSearchDto> personList = this.searchPersonRelationService.findPerson(name);

            fillPersonsTable(personList);


        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    tr("Search error") + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void doOk(int[] rowId) {

        if (rowId.length == 1) {
            Long relationId = this.personTableModel.relationIdForRow(rowId[0]);

            if (relationId != null) {
                this.relationId = relationId;
                dispose();
                return;
            }
        }

        JOptionPane.showMessageDialog(null,
                tr("Select one row"),
                "Error",
                JOptionPane.ERROR_MESSAGE);

    }

    private void fillPersonsTable(List<PersonSearchDto> persons) {

        PersonSearchTableModel personSearchTableModel = new PersonSearchTableModel(persons) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public String tr(String str) {
                return I18n.tr(str);
            }
        };

        this.getTable().setModel(personSearchTableModel);

        setupPersonTable(personSearchTableModel);
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
