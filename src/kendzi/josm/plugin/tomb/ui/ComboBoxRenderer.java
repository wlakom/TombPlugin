package kendzi.josm.plugin.tomb.ui;

import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

class ComboBoxRenderer extends DefaultListCellRenderer {
    //    implements ListCellRenderer {
    //}

    JLabel lbl = new JLabel();

    Map<String, ImageIcon> images = new HashMap<String, ImageIcon>();

    private Font uhOhFont;

    public ComboBoxRenderer() {

        lbl.setHorizontalAlignment(JLabel.LEFT);
        lbl.setVerticalAlignment(JLabel.CENTER);

        lbl.setOpaque(true);
    }

    /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        JLabel label =

                (JLabel) super.getListCellRendererComponent(list,
                        value, index, isSelected, cellHasFocus);
        lbl = label;

        String key = ((String)value);

        //        lbl.setPreferredSize(null);


        //        if (isSelected) {
        //            lbl.setBackground(list.getSelectionBackground());
        //            lbl.setForeground(list.getSelectionForeground());
        //        } else {
        //            lbl.setBackground(list.getBackground());
        //            lbl.setForeground(list.getForeground());
        //        }

        //Set the icon and text.  If icon was null, say so.
        //        ImageIcon icon = images.get(key);
        ImageIcon icon = getImage(key);

        String name = tr(key);

        lbl.setOpaque(true);
        lbl.setIcon(icon);

        String text = key + (icon == null ? " NO ICON" : "");
        ;
        lbl.setText(text);

        //        if (icon != null) {
        //            lbl.setText(name);
        //
        //            lbl.setFont(list.getFont());
        //        } else {
        //            setUhOhText(lbl, name + " (no image available)",
        //                    list.getFont());
        //        }

        return lbl;
    }

    private ImageIcon getImage(String key) {
        if (images.containsKey(key)) {
            return images.get(key);
        }

        ImageIcon img = loadImage(key);
        images.put(key, img);
        return img;

    }

    public ImageIcon loadImage(String key) {

        return null;
    }

    private String tr(String key) {
        return key;
    }

    //Set the font and text when no image was found.
    protected void setUhOhText(JLabel lbl, String uhOhText, Font normalFont) {
        if (uhOhFont == null) { //lazily create this font
            uhOhFont = normalFont.deriveFont(Font.ITALIC);
        }
        lbl.setFont(uhOhFont);
        lbl.setText(uhOhText);
    }
}