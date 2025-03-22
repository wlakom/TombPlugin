
package kendzi.josm.plugin.tomb.ui;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AutoCompletingTextField extends JTextField implements DocumentListener, ActionListener {

    private List<String> items;
    private boolean updating;

    public AutoCompletingTextField() {
        this.items = new ArrayList<>();
        getDocument().addDocumentListener(this);
        addActionListener(this);
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        update();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        update();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        update();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
    }

    private void update() {
        if (updating) {
            return;
        }

        updating = true;
        String text = getText();
        List<String> matches = new ArrayList<>();

        for (String item : items) {
            if (item.toLowerCase().startsWith(text.toLowerCase())) {
                matches.add(item);
            }
        }

        if (matches.size() == 1) {
            setText(matches.get(0));
            setCaretPosition(matches.get(0).length());
        }

        updating = false;
    }
}
