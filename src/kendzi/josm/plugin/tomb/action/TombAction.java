/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.action;

import java.awt.event.ActionEvent;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.gui.MainApplication;

import kendzi.josm.plugin.tomb.ui.TombDialog;
import kendzi.josm.plugin.tomb.ui.TombDialogAction;

public class TombAction extends JosmAction {
    private TombDialogAction dialogAction;

    public TombAction() {
        super("Tomb Plugin", "Tomb plugin icon", "Add/edit tomb related tags", null, false);
        this.dialogAction = new TombDialogAction();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (MainApplication.getLayerManager().getActiveDataSet() == null) {
            return;
        }
        // if (getParticipatingSelection().size() == 1) {
        //     OsmPrimitive primitive = getParticipatingSelection().get(0);
            try {
                TombDialog dialog = new TombDialog(dialogAction);
                // dialog.Load(primitive);
                dialog.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace(); // Wypisz ślad stosu błędów
            }
        // } else if (!getParticipatingSelection().isEmpty()) {
            System.out.println("Zaznacz jeden obiekt, aby edytować dane grobu.");
        // }
    }

    @Override
    protected void updateEnabledState() {
        // setEnabled(getParticipatingSelection().size() == 1);
        setEnabled(true);
    }
}
