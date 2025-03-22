/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.ui;

import static org.openstreetmap.josm.tools.I18n.*;

import java.util.Arrays;
import java.util.Collections;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.openstreetmap.josm.actions.DownloadPrimitiveAction;
import org.openstreetmap.josm.command.AddCommand;
import org.openstreetmap.josm.command.ChangeCommand;
import org.openstreetmap.josm.data.UndoRedoHandler;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.RelationMember;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.tools.I18n;
import org.openstreetmap.josm.tools.ImageProvider;
import kendzi.josm.plugin.tomb.dto.PersonModel;
import kendzi.josm.plugin.tomb.util.StringUtil;

public class TombDialogAction {
    private static final long serialVersionUID = 1L;

    private static final String KEY_HISTORIC = "historic";
    private static final String ROLE_MEMORIAL = "memorial";
    private static final String ROLE_TOMB = "tomb";
    private static final String KEY_PERSON = "person";
    private static final String KEY_TYPE = "type";
    private static final String KEY_NAME = "name";
    private static final String KEY_FAMILY_NAME = "family_name";
    private static final String KEY_BORN = "born";
    private static final String KEY_DIED = "died";
    private static final String KEY_DEATHPLACE = "deathplace";
    private static final String KEY_BIRTHPLACE = "birthplace";
    private static final String KEY_LIVED_IN = "lived_In";
    private static final String VALUE_GRAVE = "grave";
    private static final String KEY_CEMETERY = "cemetery";
    private static final String KEY_INSCRIPTION = "inscription";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_REF = "ref";
    private static final String KEY_SECTION_NAME = "section_name";
    private static final String KEY_SECTION_ROW = "section_row";
    private static final String KEY_SECTION_PLACE = "section_place";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_WIKIMEDIA_COMMONS = "wikimedia_commons";
    private static final String KEY_FLICKR = "flickr";
    private static final String KEY_WIKIPEDIA = "wikipedia";
    private static final String KEY_WIKIDATA = "wikidata";
    private static final String KEY_RELIGION = "religion";
    private static final String KEY_DENOMINATION = "denomination";
    private static final String KEY_TOMB = ROLE_TOMB;
    private static final String VALUE_TOMB = ROLE_TOMB;
    private static final String VALUE_MEMORIAL = ROLE_MEMORIAL;

    private IconListRenderer ilr;
    private TombDialogPersonTableModel personTableModel;
    private JTable personsTable;
    private AutoCompletingTextField cbTombType;
    private AutoCompletingTextField cbReligion;
    private AutoCompletingTextField cbDenomination;
    private AutoCompletingTextField cbHistoric;

    private JTextField txtInscription;
    private JTextField txtDescription;
    private JTextField txtRef;
    private JTextField txtSectionName;
    private JTextField txtSectionRow;
    private JTextField txtSectionPlace;
    private JTextField txtWikidata;
    private JTextField txtImage;
    private JTextField txtWikimedia_commons;
    private JTextField txtFlickr;
    private JTextField txtWikipedia;

    private List<PersonModel> persons;
    private Set<Relation> personsRemoved;

    private OsmPrimitive tombPrimitive;

    private JComboBox<String> cbCemetery;
    private JLabel lblCemetery;
    private JLabel lblDenomination;
    private JLabel lblInscription;
    private JLabel lblWikidata;
    private JLabel lblSectionName = new JLabel();
    private JLabel lblSectionRow = new JLabel();
    private JLabel lblSectionPlace = new JLabel();
    private JLabel lblRef;
    private JLabel lblDescription;
    private JLabel lblHistoric;
    private JLabel lblTombType;
    private JLabel lblReligion;
    private JLabel lblTombData;
    private JLabel lblImage;
    private JLabel lblWikimedia_commons;
    private JLabel lblFlickr;
    private JLabel lblWikipediaArticle;

    public JComboBox<String> getCbCemetery() {
        return cbCemetery;
    }

    public JLabel getLblCemetery() {
        return lblCemetery;
    }

    public JLabel getLblDenomination() {
        return lblDenomination;
    }

    public JLabel getLblInscription() {
        return lblInscription;
    }

    public JLabel getLblSectionName() {
        return lblSectionName;
    }

    public JLabel getLblSectionRow() {
        return lblSectionRow;
    }

    public JLabel getLblSectionPlace() {
        return lblSectionPlace;
    }

    public TombDialogAction() {
        bindHotKey();
        localize();
        loadIcon();
        cellRenderer();
        setupTombTypeCombo();
        setupHistoricCombo();
        setupReligionCombo();
        setupDenominationCombo();
    }

    public AutoCompletingTextField getCbReligion() {
        if (cbReligion == null) {
            cbReligion = new AutoCompletingTextField();
        }
        return cbReligion;
    }

    public AutoCompletingTextField getCbDenomination() {
        if (cbDenomination == null) {
            cbDenomination = new AutoCompletingTextField();
        }
        return cbDenomination;
    }

private void setupTombTypeCombo() {
List<String> tombTypes = Collections.unmodifiableList(Arrays.asList("", "tombstone", "tumulus", "rock-cut", "war_grave", "mausoleum", "columbarium", "pyramid", "sarcophagus", "vault"));
    getCbTombType().setItems(tombTypes);
}

private void setupHistoricCombo() {
    List<String> historicTypes = Collections.unmodifiableList(Arrays.asList("", "tomb", "memorial"));
    getCbHistoric().setItems(historicTypes);
}
private void setupReligionCombo() {
    List<String> religionTypes = Collections.unmodifiableList(Arrays.asList("", "christian", "jewish", "muslim"));
    getCbReligion().setItems(religionTypes);
}
private void setupDenominationCombo() {
    List<String> cTypes = Collections.unmodifiableList(Arrays.asList("", "roman_catholic", "polish_catholic", "orthodox", "protestant", "evangelical", "jehovahs_witness"));
 getCbDenomination().setItems(cTypes);

    private void cellRenderer() {
    }

    private void bindHotKey() {

        // Implementacja wiązania skrótów klawiszowych
        // Możesz na przykład użyć InputMap i ActionMap
    }

    private void localize() {
        // Implementacja lokalizacji (ustawienie etykiet itp.)
        lblCemetery.setText(tr("Cemetery"));
        lblDenomination.setText(tr("Denomination"));
        lblInscription.setText(tr("Inscription"));
        lblWikidata.setText(tr("Wikidata"));
        lblSectionName.setText(tr("Section Name"));
        lblSectionRow.setText(tr("Section Row"));
        lblSectionPlace.setText(tr("Section Place"));
        lblRef.setText(tr("Ref"));
        lblDescription.setText(tr("Description"));
        lblHistoric.setText(tr("Historic"));
        lblTombType.setText(tr("Tomb Type"));
        lblReligion.setText(tr("Religion"));
        lblTombData.setText(tr("Tomb Data"));
        lblImage.setText(tr("Image"));
        lblWikimedia_commons.setText(tr("Wikimedia Commons"));
        lblFlickr.setText(tr("Flickr"));
        lblWikipediaArticle.setText(tr("Wikipedia Article"));
    }

    private void loadIcon() {
        // Implementacja ładowania ikon
    }

  public void Load(OsmPrimitive tombPrimitive) {
        this.tombPrimitive = tombPrimitive;
        // Implementacja logiki ładowania danych z OsmPrimitive do pól dialogu
        // Na przykład:
        // txtInscription.setText(tombPrimitive.get("inscription"));
        // getCbTombType().setText(tombPrimitive.get("type"));
    }

    public void setVisible(boolean visible) {
        // Ta metoda prawdopodobnie powinna znajdować się w klasie TombDialog
        // Jeśli jednak chcesz ją tutaj, musisz utworzyć i zarządzać JFrame lub JDialog
    }

    public void setDefaultCloseOperation(int operation) {
        // Ta metoda również powinna znajdować się w klasie TombDialog
    }

    public AutoCompletingTextField getCbTombType() {
        if (cbTombType == null) {
            cbTombType = new AutoCompletingTextField();
        }
        return cbTombType;
    }

    public AutoCompletingTextField getCbHistoric() {
        if (cbHistoric == null) {
            cbHistoric = new AutoCompletingTextField();
        }
        return cbHistoric;
    }

 public class BigFontCellEditor extends AbstractCellEditor implements TableCellEditor {
        JTextField component = new JTextField();

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
            component.setText((String) value);
            component.setFont(new Font("Tahoma", Font.PLAIN, 14));
            return component;
        }

        @Override
        public Object getCellEditorValue() {
            return component.getText();
        }
	}
}
