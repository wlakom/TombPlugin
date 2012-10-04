package kendzi.josm.plugin.tomb;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;


/**
 *
 * @author Tomasz Kędziora (Kendzi)
 */
public class TombPlugin extends Plugin {
    public TombPlugin(PluginInformation info) {
        super(info);
        MainMenu.add(Main.main.menu.toolsMenu, new TombAction());
    }
}
