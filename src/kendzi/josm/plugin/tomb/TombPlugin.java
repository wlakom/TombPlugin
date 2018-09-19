/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb;

import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

import kendzi.josm.plugin.tomb.action.TombAction;


/**
 *
 * @author Tomasz Kędziora (Kendzi)
 */
public class TombPlugin extends Plugin {
    public TombPlugin(PluginInformation info) {
        super(info);
        MainMenu.add(MainApplication.getMenu().dataMenu, new TombAction());
    }
}
