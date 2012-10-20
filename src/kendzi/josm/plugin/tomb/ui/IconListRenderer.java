package kendzi.josm.plugin.tomb.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

public class IconListRenderer
extends DefaultListCellRenderer {

    private Map<Object, ImageIcon> icons = new HashMap<Object, ImageIcon>();

    public IconListRenderer() {
        //
    }

    public IconListRenderer(Map<Object, ImageIcon> icons) {
        this.icons = icons;
    }

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        // Get the renderer component from parent class

        JLabel label =
                (JLabel) super.getListCellRendererComponent(list,
                        value, index, isSelected, cellHasFocus);

        label.setPreferredSize(null);

        // Get icon to use for the list item value

        ImageIcon icon = getImage((String) value);

        // Set icon to display for value

        label.setIcon(icon);
        label.setText(tr((String)value));
        // We do not want the editor to have the maximum height of all
        // entries. Return a dummy with bogus height.
        if (index == -1) {
            label.setPreferredSize(new Dimension(label.getPreferredSize().width, 10));
        }


        return label;
    }

    private ImageIcon getImage(String key) {
        if (icons.containsKey(key)) {
            return icons.get(key);
        }

        ImageIcon img = loadImage(key);
        icons.put(key, img);
        return img;

    }

    public ImageIcon loadImage(String key) {

        return null;
    }

    public String tr(String key) {
        return key;
    }
}
