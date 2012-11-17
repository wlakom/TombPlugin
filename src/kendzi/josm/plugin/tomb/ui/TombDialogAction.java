/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.ui;


import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellEditor;

import kendzi.josm.plugin.tomb.dto.PersonModel;
import kendzi.josm.plugin.tomb.util.StringUtil;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.DownloadPrimitiveAction;
import org.openstreetmap.josm.command.AddCommand;
import org.openstreetmap.josm.command.ChangeCommand;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.OsmPrimitiveType;
import org.openstreetmap.josm.data.osm.PrimitiveId;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.RelationMember;
import org.openstreetmap.josm.data.osm.SimplePrimitiveId;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.gui.tagging.ac.AutoCompletingTextField;
import org.openstreetmap.josm.tools.I18n;
import org.openstreetmap.josm.tools.ImageProvider;

/**
 *
 * @author Tomasz Kędziora (Kendzi)
 */
public class TombDialogAction extends TombDialog {

    private static final String ROLE_MEMORIAL = "memorial";
    private static final String ROLE_TOMB = "tomb";
    private static final String KEY_FROM_FAMILY = "family_name";
    private static final String KEY_LIVED_IN = "lived_in";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DIED = "died";
    private static final String KEY_BORN = "born";
    private static final String KEY_NAME = "name";
    private static final String KEY_PERSON = "person";
    private static final String KEY_TYPE = "type";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_WIKIPEDIA = "wikipedia";
    private static final String KEY_RELIGION = "religion";
    private static final String KEY_HISTORIC = "historic";
    private static final String KEY_TOMB = ROLE_TOMB;
    private static final String VALUE_TOMB = ROLE_TOMB;


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
        //        getCbTombType().setModel(new DefaultComboBoxModel(new String[] {"details", "computer", "folder", "computer"}));


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

        //        personsTable.setDefaultEditor(String.class,
        //                new BigFontCellEditor());
    }

    public class BigFontCellEditor extends AbstractCellEditor implements TableCellEditor {
        JTextField component = new JTextField();
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
        KeyStroke key1 = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);

        //        getRootPane().registerKeyboardAction(anAction, aKeyStroke, aCondition)

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

        this.persons = loadPersons(tombPrimitive);
        this.personsRemoved = new HashSet<Relation>();

        fillPersons(this.persons, this.personsRemoved);
    }

    private void fillPersons(List<PersonModel> persons, Set<Relation> personsRemoved) {

        this.personTableModel = new TombDialogPersonTableModel(persons) {
            @Override
            public String tr(String str) {
                return I18n.tr(str);
            }
        };

        this.personsTable.setModel(this.personTableModel);

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
        pm.setBorn(osmPrimitive.get(KEY_BORN));
        pm.setDied(osmPrimitive.get(KEY_DIED));
        pm.setWikipedia(osmPrimitive.get(KEY_WIKIPEDIA));
        pm.setDescription(osmPrimitive.get(KEY_DESCRIPTION));
        pm.setLivedIn(osmPrimitive.get(KEY_LIVED_IN));
        pm.setFromFamily(osmPrimitive.get(KEY_FROM_FAMILY));

        pm.setRelation(osmPrimitive);
        return pm;
    }

    private void fillAttributes(OsmPrimitive tombPrimitive) {

        String historic = defaultValue(tombPrimitive.get(KEY_HISTORIC), VALUE_TOMB);

        this.getCbHistoric().setSelectedItem(historic);
        this.cbTombType.setSelectedItem(tombPrimitive.get(KEY_TOMB));
        this.cbReligion.setSelectedItem(tombPrimitive.get(KEY_RELIGION));

        this.txtWikipedia.setText(tombPrimitive.get(KEY_WIKIPEDIA));
        this.txtImage.setText(tombPrimitive.get(KEY_IMAGE));

    }

    @Override
    protected void onSave() {
        save();


        dispose();
    }

    @Override
    protected void onAddPerson() {

        stopEdit();

        int rowId = this.personTableModel.addPersonModel(new PersonModel());

        //    	personsTable.setRowSelectionInterval(rowId, rowId);
        this.personsTable.changeSelection(rowId, 0, false, false);
        this.personsTable.requestFocus();

        //    	personsTable.setFocusable(true);
        //    	persons.add(new PersonModel());
        //    	fireTableRowsInserted()
    }

    @Override
    protected void onRemovePerson(int [] rowsId) {
        PersonModel pm = this.personTableModel.removePersonModel(rowsId);

        if (pm != null && pm.getRelation() != null) {
            this.personsRemoved.add(pm.getRelation());
        }
    }

    private void save() {

        stopEdit();

        savePersons(this.tombPrimitive, this.persons, this.personsRemoved);

        saveTombPrimitive(this.tombPrimitive);
    }

    private void stopEdit() {
        if (this.personsTable.isEditing()) {
            this.personsTable.getCellEditor().stopCellEditing();
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

        //        newPrimitive.put("historic", KEY_TOMB);

        Main.main.undoRedo.add(new ChangeCommand(tombPrimitive, newPrimitive));
    }



    private void injectTombPrimitive(OsmPrimitive n) {

        n.put(KEY_HISTORIC, defaultValue((String) this.getCbHistoric().getSelectedItem(), VALUE_TOMB));

        n.put(KEY_TOMB, nullOnBlank((String) this.cbTombType.getSelectedItem()));
        n.put(KEY_RELIGION, nullOnBlank((String) this.cbReligion.getSelectedItem()));

        n.put(KEY_WIKIPEDIA, nullOnBlank(this.txtWikipedia.getText()));
        n.put(KEY_IMAGE, nullOnBlank(this.txtImage.getText()));
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
        //		if (relation.getMembersCount() < 2)

        Relation newRelation = new Relation(relation);

        boolean changed = false;

        for (int i = newRelation.getMembersCount() - 1; i >= 0 ; i--) {

            RelationMember m = newRelation.getMember(i);

            if (m.getType().equals(node.getType())
                    && m.getUniqueId() == node.getUniqueId()
                    && (ROLE_TOMB.equals(m.getRole()) || ROLE_MEMORIAL.equals(m.getRole()))) {

                newRelation.removeMember(i);
                changed = true;

                //                Main.main.undoRedo.add(new DeleteCommand(m));


            }

        }
        if (changed) {
            Main.main.undoRedo.add(new ChangeCommand(relation, newRelation));
        }

        //        Main.main.undoRedo.add(new DeleteCommand(relation));
    }

    private void saveRelation(OsmPrimitive tombPrimitive, PersonModel pm) {


        Relation newRelation = new Relation();
        newRelation.addMember(new RelationMember(KEY_TOMB, tombPrimitive));

        injectRelation(pm, newRelation);

        newRelation.put(KEY_TYPE, KEY_PERSON);

        Main.main.undoRedo.add(new AddCommand(newRelation));

    }

    private void updateRelation(Relation relation, OsmPrimitive tombPrimitive, PersonModel pm) {
        Relation newRelation = new Relation(relation);
        //        Main.main.undoRedo.add(new AddCommand(w));

        boolean relationHaveThisTomb = false;
        for (int i = 0; i < relation.getMembersCount(); i++ ) {
            RelationMember member = relation.getMember(i);
            if (ROLE_TOMB.equals(member.getRole()) &&
                    member.getMember().equals(tombPrimitive)) {
                // skip
                relationHaveThisTomb = true;
            } else {
                //
            }
        }
        if (!relationHaveThisTomb) {

            newRelation.addMember(new RelationMember(KEY_TOMB, tombPrimitive));
        }

        injectRelation(pm, newRelation);

        newRelation.put(KEY_TYPE, KEY_PERSON);

        Main.main.undoRedo.add(new ChangeCommand(relation, newRelation));


    }

    /**
     * @param pm
     * @param newRelation
     */
    public void injectRelation(PersonModel pm, Relation newRelation) {
        newRelation.put(KEY_NAME, nullOnBlank(pm.getName()));
        newRelation.put(KEY_BORN, nullOnBlank(pm.getBorn()));
        newRelation.put(KEY_DIED, nullOnBlank(pm.getDied()));
        newRelation.put(KEY_WIKIPEDIA, nullOnBlank(pm.getWikipedia()));
        newRelation.put(KEY_DESCRIPTION, nullOnBlank(pm.getDescription()));

        newRelation.put(KEY_LIVED_IN, nullOnBlank(pm.getLivedIn()));
        newRelation.put(KEY_FROM_FAMILY, nullOnBlank(pm.getFromFamily()));
    }

    public void localize() {
        try {

            getLblHistoric().setText(tr(getLblHistoric().getText()));
            getLblTombType().setText(tr(getLblTombType().getText()));
            //getLblOptionalAttributes().setText(tr("Optional Attributes") + ":");
            getLblReligion().setText(tr(getLblReligion().getText()));
            getLblTombData().setText(tr(getLblTombData().getText()));

            // XXX i don't known if it is corect translation for strange names: (with "-")
            getLblWikipediaArticle().setText("- " + tr("wikipedia article"));
            getLblImage().setText("- " + tr("image"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSearch() {
        try {
            PersonSearchDialogAction dialog = new PersonSearchDialogAction();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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

                OsmDataLayer layer = Main.main.getEditLayer();
                Relation relation = (Relation) layer.data.getPrimitiveById(primitiveId);

                PersonModel pm = convert(relation);
                personTableModel.addPersonModel(pm);




                //                Set<PrimitiveId> errs = task.getMissingPrimitives();
                //                if (errs != null && !errs.isEmpty()) {
                //                    final ExtendedDialog dlg = reportProblemDialog(errs,
                //                            trn("Object could not be downloaded", "Some objects could not be downloaded", errs.size()),
                //                            trn("One object could not be downloaded.<br>",
                //                                "{0} objects could not be downloaded.<br>",
                //                                errs.size(),
                //                                errs.size())
                //                            + tr("The server replied with response code 404.<br>"
                //                                + "This usually means, the server does not know an object with the requested id."),
                //                            tr("missing objects:"),
                //                            JOptionPane.ERROR_MESSAGE
                //                    );
                //                    try {
                //                        SwingUtilities.invokeAndWait(new Runnable() {
                //                            @Override
                //                            public void run() {
                //                                dlg.showDialog();
                //                            }
                //                        });
                //                    } catch (InterruptedException ex) {
                //                    } catch (InvocationTargetException ex) {
                //                    }
                //                }
                //
                //                Set<PrimitiveId> del = new TreeSet<PrimitiveId>();
                //                DataSet ds = getCurrentDataSet();
                //                for (PrimitiveId id : ids) {
                //                    OsmPrimitive osm = ds.getPrimitiveById(id);
                //                    if (osm != null && osm.isDeleted()) {
                //                        del.add(id);
                //                    }
                //                }
                //                if (!del.isEmpty()) {
                //                    final ExtendedDialog dlg = reportProblemDialog(del,
                //                            trn("Object deleted", "Objects deleted", del.size()),
                //                            trn(
                //                                "One downloaded object is deleted.",
                //                                "{0} downloaded objects are deleted.",
                //                                del.size(),
                //                                del.size()),
                //                            null,
                //                            JOptionPane.WARNING_MESSAGE
                //                    );
                //                    SwingUtilities.invokeLater(new Runnable() {
                //                        @Override
                //                        public void run() {
                //                            dlg.showDialog();
                //                        }
                //                    });
                //                }
            }
        };
        Main.worker.submit(showErrorsAndWarnings);

    }

}
