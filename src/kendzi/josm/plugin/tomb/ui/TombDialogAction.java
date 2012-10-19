/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import kendzi.josm.plugin.tomb.dto.PersonModel;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.command.AddCommand;
import org.openstreetmap.josm.command.ChangeCommand;
import org.openstreetmap.josm.command.DeleteCommand;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.RelationMember;

/**
 *
 * @author Tomasz Kędziora (Kendzi)
 */
public class TombDialogAction extends TombDialog {

    private static final String KEY_FROM_FAMILY = "family_name";
    private static final String KEY_LIVED_IN = "lived_in";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DEATH = "died";
    private static final String KEY_BIRTH = "born";
    private static final String KEY_NAME = "name";
    private static final String KEY_PERSON = "person";
    private static final String KEY_TYPE = "type";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_WIKIPEDIA = "wikipedia";
    private static final String KEY_RELIGION = "religion";
    private static final String KEY_TOMB = "tomb";


    private List<PersonModel> persons;
    private Set<Relation> personsRemoved;

    private Node node;
    private PersonTableModel personTableModel;


    public TombDialogAction() {

        bindHotKey();
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

    public void Load(Node node) {
        this.node = node;
        fillAttributes(node);

        this.persons = loadPersons(node);
        this.personsRemoved = new HashSet<Relation>();

        fillPersons(this.persons, this.personsRemoved);
    }

    private void fillPersons(List<PersonModel> persons, Set<Relation> personsRemoved) {

        this.personTableModel = new PersonTableModel(persons);

        this.personsTable.setModel(this.personTableModel);

    }

    private List<PersonModel> loadPersons(Node node) {

        List<PersonModel> ret = new ArrayList<PersonModel>();

        List<OsmPrimitive> referrers = node.getReferrers();

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
        pm.setBirth(osmPrimitive.get(KEY_BIRTH));
        pm.setDeath(osmPrimitive.get(KEY_DEATH));
        pm.setWikipedia(osmPrimitive.get(KEY_WIKIPEDIA));
        pm.setDescription(osmPrimitive.get(KEY_DESCRIPTION));
        pm.setLivedIn(osmPrimitive.get(KEY_LIVED_IN));
        pm.setFromFamily(osmPrimitive.get(KEY_FROM_FAMILY));

        pm.setRelation(osmPrimitive);
        return pm;
    }

    private void fillAttributes(Node node) {

        this.cbTombType.setSelectedItem(node.get(KEY_TOMB));
        this.cbReligion.setSelectedItem(node.get(KEY_RELIGION));

        this.txtWikipedia.setText(node.get(KEY_WIKIPEDIA));
        this.txtImage.setText(node.get(KEY_IMAGE));

    }

    @Override
    protected void onSave() {
        save();


        dispose();
    }

    @Override
    protected void onAddPerson() {

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

        savePersons(this.node, this.persons, this.personsRemoved);

        saveNode(this.node);
    }

    private void stopEdit() {
        if (this.personsTable.isEditing()) {
            this.personsTable.getCellEditor().stopCellEditing();
        }
    }

    private void saveNode(Node node) {

        Node newNode = new Node(node);
        injectNode(newNode);

        newNode.put("historic", KEY_TOMB);

        Main.main.undoRedo.add(new ChangeCommand(node, newNode));
    }

    private void injectNode(Node n) {

        n.put(KEY_TOMB, nullOnBlank((String) this.cbTombType.getSelectedItem()));
        n.put(KEY_RELIGION, nullOnBlank((String) this.cbReligion.getSelectedItem()));

        n.put(KEY_WIKIPEDIA, nullOnBlank(this.txtWikipedia.getText()));
        n.put(KEY_IMAGE, nullOnBlank(this.txtImage.getText()));
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


    private void savePersons(Node node2, List<PersonModel> persons2, Set<Relation> personsRemoved) {

        for (PersonModel pm : persons2) {
            if (pm.getRelation() != null) {
                updateRelation(pm.getRelation(), pm);
            } else {
                saveRelation(node2, pm);
            }
        }

        for (Relation relation : personsRemoved) {
            removeRelation(node2, relation);
        }
    }

    private void removeRelation(Node node, Relation relation) {
        //		if (relation.getMembersCount() < 2)

        //    	for (RelationMember m : relation.getMembers()) {
        //    		m.getNode()
        //
        //    	}

        Main.main.undoRedo.add(new DeleteCommand(relation));
    }

    private void saveRelation(Node node, PersonModel pm) {


        Relation newRelation = new Relation();
        newRelation.addMember(new RelationMember(KEY_TOMB, node));

        injectRelation(pm, newRelation);

        newRelation.put(KEY_TYPE, KEY_PERSON);

        Main.main.undoRedo.add(new AddCommand(newRelation));

    }

    private void updateRelation(Relation relation,
            PersonModel pm) {
        Relation newRelation = new Relation(relation);
        //        Main.main.undoRedo.add(new AddCommand(w));

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
        newRelation.put(KEY_BIRTH, nullOnBlank(pm.getBirth()));
        newRelation.put(KEY_DEATH, nullOnBlank(pm.getDeath()));
        newRelation.put(KEY_WIKIPEDIA, nullOnBlank(pm.getWikipedia()));
        newRelation.put(KEY_DESCRIPTION, nullOnBlank(pm.getDescription()));

        newRelation.put(KEY_LIVED_IN, nullOnBlank(pm.getLivedIn()));
        newRelation.put(KEY_FROM_FAMILY, nullOnBlank(pm.getFromFamily()));
    }

}
