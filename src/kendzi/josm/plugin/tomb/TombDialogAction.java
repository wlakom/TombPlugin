package kendzi.josm.plugin.tomb;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.command.AddCommand;
import org.openstreetmap.josm.command.ChangeCommand;
import org.openstreetmap.josm.command.DeleteCommand;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.RelationMember;

public class TombDialogAction extends TombDialog {

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DEATH = "death";
    private static final String KEY_BIRTH = "birth";
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

    void Load(Node node) {
        this.node = node;
        fillAttributes(node);

        persons = loadPersons(node);
        personsRemoved = new HashSet<Relation>();

        fillPersons(persons, personsRemoved);
    }

    private void fillPersons(List<PersonModel> persons, Set<Relation> personsRemoved) {

        personTableModel = new PersonTableModel(persons);

        personsTable.setModel(personTableModel);

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

        pm.setRelation(osmPrimitive);
        return pm;
    }

    private void fillAttributes(Node node) {

        cbTombType.setSelectedItem(node.get(KEY_TOMB));
        cbReligion.setSelectedItem(node.get(KEY_RELIGION));

        txtWikipedia.setText(node.get(KEY_WIKIPEDIA));
        txtImage.setText(node.get(KEY_IMAGE));

    }

    @Override
    protected void onSave() {
        save();


        dispose();
    }

    @Override
    protected void onAddPerson() {

        int rowId = personTableModel.addPersonModel(new PersonModel());

        //    	personsTable.setRowSelectionInterval(rowId, rowId);
        personsTable.changeSelection(rowId, 0, false, false);
        personsTable.requestFocus();

        //    	personsTable.setFocusable(true);
        //    	persons.add(new PersonModel());
        //    	fireTableRowsInserted()
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

        savePersons(this.node, this.persons, this.personsRemoved);

        saveNode(this.node);
    }

    private void stopEdit() {
        if (personsTable.isEditing()) {
            personsTable.getCellEditor().stopCellEditing();
        }
    }

    private void saveNode(Node node) {

        Node newNode = new Node(node);
        injectNode(newNode);

        newNode.put("historic", KEY_TOMB);

        Main.main.undoRedo.add(new ChangeCommand(node, newNode));
    }

    private void injectNode(Node n) {

        n.put(KEY_TOMB, blankOnNull((String) cbTombType.getSelectedItem()));
        n.put(KEY_RELIGION, blankOnNull((String) cbReligion.getSelectedItem()));

        n.put(KEY_WIKIPEDIA, blankOnNull(txtWikipedia.getText()));
        n.put(KEY_IMAGE, blankOnNull(txtImage.getText()));
    }

    private String blankOnNull(String str) {
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
        newRelation.put(KEY_NAME, pm.getName());
        newRelation.put(KEY_BIRTH, pm.getBirth());
        newRelation.put(KEY_DEATH, pm.getDeath());
        newRelation.put(KEY_WIKIPEDIA, pm.getWikipedia());
        newRelation.put(KEY_DESCRIPTION, pm.getDescription());
    }

}
