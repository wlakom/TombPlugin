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

import java.awt.Component;
import java.awt.Font;
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
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.table.TableCellEditor;

import org.openstreetmap.josm.actions.DownloadPrimitiveAction;
import org.openstreetmap.josm.command.AddCommand;
import org.openstreetmap.josm.command.ChangeCommand;
import org.openstreetmap.josm.data.UndoRedoHandler;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.OsmPrimitiveType;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.RelationMember;
import org.openstreetmap.josm.data.osm.SimplePrimitiveId;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.gui.tagging.ac.AutoCompletingTextField;
import org.openstreetmap.josm.tools.I18n;
import org.openstreetmap.josm.tools.ImageProvider;

import kendzi.josm.plugin.tomb.dto.PersonModel;
import kendzi.josm.plugin.tomb.util.StringUtil;

/**
 *
 * @author Tomasz Kędziora (Kendzi)
 */
public class TombDialogAction extends TombDialog {
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
    private static final String KEY_LIVED_IN = "lived_in";
    private static final String KEY_DESCRIPTION = "description";   
    private static final String KEY_INSCRIPTION = "inscription";   
    private static final String KEY_IMAGE = "image";
    private static final String KEY_WIKIMEDIA_COMMONS = "wikimedia_commons";
    private static final String KEY_FRICKR = "frickr";    
    private static final String KEY_WIKIPEDIA = "wikipedia";
    private static final String KEY_WIKIDATA = "wikidata";
    private static final String KEY_RELIGION = "religion";
    private static final String KEY_DENOMINATION = "denomination";
    private static final String KEY_TOMB = ROLE_TOMB;
    private static final String VALUE_TOMB = ROLE_TOMB;
    private static final String VALUE_MEMORIAL = ROLE_MEMORIAL;

    private List<PersonModel> persons;
    private Set<Relation> personsRemoved;

    private OsmPrimitive tombPrimitive;
    private TombDialogPersonTableModel personTableModel;


    public TombDialogAction() {

        super();

        bindHotKey();

        localize();

        loadIcon();

        cellRenderer();

        setupTombTypeCombo();

        setupHistoricCombo();

    }

    private void setupTombTypeCombo() {

        IconListRenderer ilr = new IconListRenderer() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public ImageIcon loadImage(String key) {
                try {
                    return ImageProvider.get("cc2/tomb_"+key+".jpg");
                } catch (Exception e) {
                    //
                }
                return null;

            };

            @Override
            public String tr(String str) {
                return I18n.tr(str);
            }
        };

        getCbTombType().setEditable(true);

        getCbTombType().setRenderer(ilr);
        AutoCompletingTextField tf = new AutoCompletingTextField();

        getCbTombType().setEditor(tf);

    }
    private void setupHistoricCombo() {

        IconListRenderer ilr = new IconListRenderer() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public ImageIcon loadImage(String key) {
                try {
                    return ImageProvider.get("historic_"+key+".png");
                } catch (Exception e) {
                    //
                }
                return null;

            };

            @Override
            public String tr(String str) {
                return I18n.tr(str);
            }
        };

        getCbHistoric().setEditable(true);

        getCbHistoric().setRenderer(ilr);
        AutoCompletingTextField tf = new AutoCompletingTextField();

        getCbTombType().setEditor(tf);

    }

    private void cellRenderer() {
        //
    }

    public class BigFontCellEditor extends AbstractCellEditor implements TableCellEditor {
        JTextField component = new JTextField();
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
            component.setText((String)value);
            component.setFont(new Font("Tahoma", Font.PLAIN, 14));
            return component;
        }
        @Override
        public Object getCellEditorValue() {
            return component.getText();
        }
    }



    private void loadIcon() {

        ImageIcon imageIcon = ImageProvider.get("tomb_icon.png");

        if (imageIcon != null) {
            setIconImage(imageIcon.getImage());
        }
    }

    /**
     * 
     */
    public void bindHotKey() {
        KeyStroke key1 = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(key1, "new_person");
        getRootPane().getActionMap().put("new_person", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onAddPerson();
            }
        });
    }

    public void Load(OsmPrimitive tombPrimitive) {
        this.tombPrimitive = tombPrimitive;
        fillAttributes(tombPrimitive);

        persons = loadPersons(tombPrimitive);
        personsRemoved = new HashSet<Relation>();

        fillPersons(persons, personsRemoved);
    }

    private void fillPersons(List<PersonModel> persons, Set<Relation> personsRemoved) {

        personTableModel = new TombDialogPersonTableModel(persons) {
            @Override
            public String tr(String str) {
                return I18n.tr(str);
            }
        };

        personsTable.setModel(personTableModel);

        personsTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        personsTable.getColumnModel().getColumn(2).setPreferredWidth(30);
    }

    private List<PersonModel> loadPersons(OsmPrimitive tombPrimitive) {

        List<PersonModel> ret = new ArrayList<PersonModel>();

        List<OsmPrimitive> referrers = tombPrimitive.getReferrers();

        for (OsmPrimitive osmPrimitive : referrers) {
            if (osmPrimitive instanceof Relation
                    && KEY_PERSON.equals(osmPrimitive.get(KEY_TYPE))) {

                PersonModel pm = convert((Relation) osmPrimitive);
                ret.add(pm);
            }
        }

        return ret;
    }

    private PersonModel convert(Relation osmPrimitive) {
        PersonModel pm = new PersonModel();
        pm.setName(osmPrimitive.get(KEY_NAME));
        pm.setFamily_name(osmPrimitive.get(KEY_FAMILY_NAME));        
        pm.setBorn(osmPrimitive.get(KEY_BORN));
        pm.setDied(osmPrimitive.get(KEY_DIED));        
        pm.setBirthplace(osmPrimitive.get(KEY_BIRTHPLACE));        
        pm.setDeathplace(osmPrimitive.get(KEY_DEATHPLACE));  
        pm.setLivedIn(osmPrimitive.get(KEY_LIVED_IN));        
        pm.setDescription(osmPrimitive.get(KEY_DESCRIPTION));
        pm.setInscription(osmPrimitive.get(KEY_INSCRIPTION));
        pm.setImage(osmPrimitive.get(IMAGE));
        pm.setWikimedia_commons(osmPrimitive.get(WIKIMEDIA_COMMONS));
        pm.setFlickr(osmPrimitive.get(FLICKR));
        pm.setWikipedia(osmPrimitive.get(KEY_WIKIPEDIA));
        pm.setWikipedia(osmPrimitive.get(KEY_WIKIDATA));
        pm.setRelation(osmPrimitive);
        return pm;
    }

    private void fillAttributes(OsmPrimitive tombPrimitive) {

        String cemetery = defaultValue(tombPrimitive.get(KEY_CEMETERY), VALUE_GRAVE);
        String historic = defaultValue(tombPrimitive.get(KEY_HISTORIC), VALUE_TOMB);

        getCbHistoric().setSelectedItem(historic);
        cbTombType.setSelectedItem(tombPrimitive.get(KEY_TOMB));
        cbReligion.setSelectedItem(tombPrimitive.get(KEY_RELIGION));
        cbDenomination.setSelectedItem(tombPrimitive.get(KEY_DENOMINATION));

        txtRef.setText(tombPrimitive.get(REF));        
        txtSection_name.setText(tombPrimitive.get(SECTION_NAME));
        txtSection_row.setText(tombPrimitive.get(SECTION_ROW));
        txtSection_place.setText(tombPrimitive.get(SECTION_PLACE));
        
        txtImage.setText(tombPrimitive.get(KEY_IMAGE));
        txtWikimedia_commons.setText(tombPrimitive.get(KEY_WIKIMEDIA_COMMONS));
        txtFlickr.setText(tombPrimitive.get(KEY_FLICKR));

        txtWikipedia.setText(tombPrimitive.get(KEY_WIKIPEDIA));
        txtWikidata.setText(tombPrimitive.get(KEY_WIKIDATA));
    }

    @Override
    protected void onSave() {
        save();


        dispose();
    }

    @Override
    protected void onAddPerson() {

        stopEdit();

        int rowId = personTableModel.addPersonModel(new PersonModel());

        personsTable.changeSelection(rowId, 0, false, false);
        personsTable.requestFocus();

    }

    @Override
    protected void onRemovePerson(int [] rowsId) {
        PersonModel pm = personTableModel.removePersonModel(rowsId);

        if (pm != null && pm.getRelation() != null) {
            personsRemoved.add(pm.getRelation());
        }
    }

    private void save() {

        stopEdit();

        savePersons(tombPrimitive, persons, personsRemoved);

        saveTombPrimitive(tombPrimitive);
    }

    private void stopEdit() {
        if (personsTable.isEditing()) {
            personsTable.getCellEditor().stopCellEditing();
        }
    }

    private void saveTombPrimitive(OsmPrimitive tombPrimitive) {

        OsmPrimitive newPrimitive = null;
        if (tombPrimitive instanceof Node) {
            newPrimitive = new Node((Node) tombPrimitive);
        } else if (tombPrimitive instanceof Way) {
            newPrimitive = new Way((Way) tombPrimitive);
        }

        injectTombPrimitive(newPrimitive);

        UndoRedoHandler.getInstance().add(new ChangeCommand(tombPrimitive, newPrimitive));
    }



    private void injectTombPrimitive(OsmPrimitive n) {

        n.put(KEY_CEMETERY, defaultValue((String) getCbCemetery().getSelectedItem(), VALUE_GRAVE));
        n.put(KEY_HISTORIC, defaultValue((String) getCbHistoric().getSelectedItem(), VALUE_TOMB));

        n.put(KEY_TOMB, nullOnBlank((String) cbTombType.getSelectedItem()));
        n.put(KEY_RELIGION, nullOnBlank((String) cbReligion.getSelectedItem()));
        n.put(KEY_DENOMINATION, nullOnBlank((String) cbDenomination.getSelectedItem()));
        n.put(KEY_INSCRIPTION, nullOnBlank(txtInscription.getText()));

        n.put(KEY_REF, nullOnBlank(txtRef.getText()));     
        n.put(KEY_SECTION_NAME, nullOnBlank(txtFlickr.getSection_name()));
        n.put(KEY_SECTION_ROW, nullOnBlank(txtFlickr.getSection_row()));
        n.put(KEY_SECTION_PLACE, nullOnBlank(txtFlickr.getSection_place()));        

        n.put(KEY_IMAGE, nullOnBlank(txtImage.getText()));
        n.put(KEY_WIKIMEDIA_COMMONS, nullOnBlank(txtWikimedia_commons.getText()));
        n.put(KEY_FLICKR, nullOnBlank(txtFlickr.getText()));

        n.put(KEY_DESCRIPTION, nullOnBlank(txtDescription.getText()));
        n.put(KEY_WIKIPEDIA, nullOnBlank(txtWikipedia.getText()));
        n.put(KEY_WIKIDATA, nullOnBlank(txtWikidata.getText()));
    }

    private String defaultValue(String str, String defaultValue) {
        if (StringUtil.isBlankOrNull(str)) {
            return defaultValue;
        }
        return str;
    }

    private String nullOnBlank(String str) {
        if (str == null) {
            return null;
        }

        if ("".equals(str.trim())) {
            return null;
        }

        return str;
    }


    private void savePersons(OsmPrimitive tombPrimitive, List<PersonModel> persons2, Set<Relation> personsRemoved) {

        for (PersonModel pm : persons2) {
            if (pm.getRelation() != null) {
                updateRelation(pm.getRelation(), tombPrimitive, pm);
            } else {
                saveRelation(tombPrimitive, pm);
            }
        }

        for (Relation relation : personsRemoved) {
            removeRelation(tombPrimitive, relation);
        }
    }

    private void removeRelation(OsmPrimitive node, Relation relation) {

        Relation newRelation = new Relation(relation);

        boolean changed = false;

        for (int i = newRelation.getMembersCount() - 1; i >= 0 ; i--) {

            RelationMember m = newRelation.getMember(i);

            if (m.getType().equals(node.getType())
                    && m.getUniqueId() == node.getUniqueId()
                    && (ROLE_TOMB.equals(m.getRole()) || ROLE_MEMORIAL.equals(m.getRole()))) {

                newRelation.removeMember(i);
                changed = true;
            }

        }
        if (changed) {
            UndoRedoHandler.getInstance().add(new ChangeCommand(relation, newRelation));
        }

    }

    /**
     * Returns role for 'person' relation for given OSM primitive of tomb.
     * <p/>
     * I.e. usually tombs (historic=tomb) should be added to 'person' relation as role 'tomb', memorials
     * (<code>historic=memorial</code>) as role 'memorial'. It guesses role for OSM primitive representing memorial/tomb. Currently
     * returns 'memorial' for <code>historic=memorial</code> and 'tomb' for all other cases.
     *
     * @param tombPrimitive OSM primitive (usually node) representing this tomb/memorial
     * @return String to be used as role in <code>type=person</code> relation
     */
    private String getRoleForTombPrimitive(OsmPrimitive tombPrimitive) {
        final String historicValue = tombPrimitive.get(KEY_HISTORIC);
        if (VALUE_MEMORIAL.equals(historicValue)) {
            return ROLE_MEMORIAL;
        } else {
            return ROLE_TOMB;
        }
    }

    /**
     * Returns true if relation member represents given OSM primitive of tomb.
     *
     * @param member Relation member to check
     * @param tombPrimitive Tomb primitive (usually node) to check
     */
    private boolean isMemberRepresentsThisPrimitive(RelationMember member, OsmPrimitive tombPrimitive) {
        return (ROLE_TOMB.equals(member.getRole()) || ROLE_MEMORIAL.equals(member.getRole())) &&
                member.getMember().equals(tombPrimitive);
    }

    private void saveRelation(OsmPrimitive tombPrimitive, PersonModel pm) {


        Relation newRelation = new Relation();
        newRelation.addMember(new RelationMember(getRoleForTombPrimitive(tombPrimitive), tombPrimitive));

        injectRelation(pm, newRelation);

        newRelation.put(KEY_TYPE, KEY_PERSON);

        DataSet ds = MainApplication.getLayerManager().getEditDataSet();
        UndoRedoHandler.getInstance().add(new AddCommand(ds, newRelation));

    }

    private void updateRelation(Relation relation, OsmPrimitive tombPrimitive, PersonModel pm) {
        Relation newRelation = new Relation(relation);

        boolean relationHaveThisTomb = false;
        for (int i = 0; i < relation.getMembersCount(); i++ ) {
            RelationMember member = relation.getMember(i);
            if (isMemberRepresentsThisPrimitive(member, tombPrimitive)) {
                // skip
                relationHaveThisTomb = true;
            } else {
                //
            }
        }
        if (!relationHaveThisTomb) {

            newRelation.addMember(new RelationMember(getRoleForTombPrimitive(tombPrimitive), tombPrimitive));
        }

        injectRelation(pm, newRelation);

        newRelation.put(KEY_TYPE, KEY_PERSON);

        UndoRedoHandler.getInstance().add(new ChangeCommand(relation, newRelation));


    }

    public void injectRelation(PersonModel pm, Relation newRelation) {
        newRelation.put(KEY_NAME, nullOnBlank(pm.getName()));
        newRelation.put(KEY_FAMILY_NAME, nullOnBlank(pm.getFamily_name()));        
        newRelation.put(KEY_BORN, nullOnBlank(pm.getBorn()));
        newRelation.put(KEY_DIED, nullOnBlank(pm.getDied()));        
        newRelation.put(KEY_BIRTHPLACE, nullOnBlank(pm.getBirthplace()));        
        newRelation.put(KEY_DEATHPLACE, nullOnBlank(pm.getDeathplace()));
        newRelation.put(KEY_LIVED_IN, nullOnBlank(pm.getLivedIn()));        
        newRelation.put(KEY_DESCRIPTION, nullOnBlank(pm.getDescription()));  
        newRelation.put(KEY_INSCRIPTION, nullOnBlank(pm.getInscription())); 
        
        newRelation.put(KEY_IMAGE, nullOnBlank(pm.getImage()));  
        newRelation.put(KEY_WIKIMEDIA_COMMONS, nullOnBlank(pm.getWikimedia_commons()));
       newRelation.put(KEY_FLICKR, nullOnBlank(pm.getFlickr())); 
        
        newRelation.put(KEY_WIKIPEDIA, nullOnBlank(pm.getWikipedia()));
        newRelation.put(KEY_WIKIDATA, nullOnBlank(pm.getWikidata()));  
        
        newRelation.put(KEY_REF, nullOnBlank(pm.getRef()));
        newRelation.put(KEY_SECTION_NAME, nullOnBlank(pm.getSection_name()));
        newRelation.put(KEY_SECTION_NAME, nullOnBlank(pm.getSection_name()));
        newRelation.put(KEY_SECTION_ROW, nullOnBlank(pm.getSection_row()));
        newRelation.put(KEY_SECTION_PLACE, nullOnBlank(pm.getSection_place()));        

    }

    public void localize() {
        try {

            getLblCemetery().setText(tr(getLblCemetery().getText()));
            getLblHistoric().setText(tr(getLblHistoric().getText()));
            getLblTombType().setText(tr(getLblTombType().getText()));
            getLblReligion().setText(tr(getLblReligion().getText()));
            getLblDenomination().setText(tr(getLblDenomination().getText()));
            getLblTombData().setText(tr(getLblTombData().getText()));
            getLblInscription().setText("- " + tr("inscription"));
            
            // XXX i don't known if it is correct translation for strange names: (with "-")
            getLblImage().setText("- " + tr("image"));
            getLblWikimedia_commons().setText("- " + tr("wikimedia_commons"));
            getLblFlickr().setText("- " + tr("flickr"));

            getLblDescription().setText("- " + tr("description"));
            getLblWikipediaArticle().setText("- " + tr("wikipedia article"));
            getLblWikidata().setText("- " + tr("wikidata"));
            
            getLblRef().setText("- " + tr("ref"));
            getLblSection_name().setText("- " + tr("section:name"));
            getLblSection_row().setText("- " + tr("section:row"));
            getLblSection_place().setText("- " + tr("section:place"));            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSearch() {
        try {
            PersonSearchDialogAction dialog = new PersonSearchDialogAction();
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setModal(true);
            dialog.setVisible(true);

            Long relationId = dialog.getSelectedRelationId();
            if (relationId == null) {
                return;
            }

            SimplePrimitiveId primitiveId = new SimplePrimitiveId(relationId, OsmPrimitiveType.RELATION);

            List<PrimitiveId> ids = new ArrayList<PrimitiveId>();
            ids.add(primitiveId);

            DownloadPrimitiveAction.processItems(false, ids, false, true);

            submitAddTableRowAfterDownload(primitiveId);



        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(null,
                    tr("Error"+ e),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        }

    }

    private void submitAddTableRowAfterDownload(final SimplePrimitiveId primitiveId) {




        Runnable showErrorsAndWarnings = new Runnable() {
            @Override
            public void run() {

                OsmDataLayer layer = MainApplication.getLayerManager().getEditLayer();
                Relation relation = (Relation) layer.data.getPrimitiveById(primitiveId);

                PersonModel pm = convert(relation);
                personTableModel.addPersonModel(pm);
            }
        };
        MainApplication.worker.submit(showErrorsAndWarnings);

    }

}
