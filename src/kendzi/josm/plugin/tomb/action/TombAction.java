/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.action;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import kendzi.josm.plugin.tomb.ui.TombDialogAction;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.tools.Shortcut;

public class TombAction extends JosmAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TombAction() {
        super(tr("Tomb plugin"),
                "tomb_icon.png",
                tr(""),
                Shortcut.registerShortcut("tools:Tomb plugin",
                        tr("Tool: {0}", tr("Tomb")),
                        KeyEvent.VK_C,
                        Shortcut.SHIFT
                        ),
                        true);
    }

    public void actionPerformed(ActionEvent e) {

        Collection<OsmPrimitive> selectedPrimitive = Main.getLayerManager().getEditDataSet().getSelectedNodesAndWays();

        if (selectedPrimitive.size() != 1) {
            JOptionPane.showMessageDialog(null,
                    tr("Choose one node or way"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        OsmPrimitive tombPrimitive = selectedPrimitive.iterator().next();


        try {
            TombDialogAction dialog = new TombDialogAction();
            //            TombDialog dialog = new TombDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            dialog.Load(tombPrimitive);

            dialog.setVisible(true);


        } catch (Exception ee) {
            ee.printStackTrace();
        }




    }

    @Override
    protected void updateEnabledState() {
        setEnabled(getLayerManager().getEditDataSet() != null);
    }
}
