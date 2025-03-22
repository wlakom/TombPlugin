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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.openstreetmap.josm.data.osm.OsmPrimitive;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

/**
 * File generated using WindowsBuilder, DON'T edit it manually!
 *
 * @author Tomasz Kędziora (Kendzi)
 */
public class TombDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtWikidata;
    private JTextField txtSectionName;
    private JTextField txtSectionRow;
    private JTextField txtSectionPlace;
    private JTextField txtRef;
    private JTextField txtDescription;
    private AutoCompletingTextField cbHistoric;
    private AutoCompletingTextField cbTombType;
    private AutoCompletingTextField cbReligion;
    private AutoCompletingTextField cbDenomination;
    private JTextField txtImage;
    private JTextField txtWikimedia_commons;
    private JTextField txtFlickr;
    private JTextField txtWikipedia;
    private JTable personsTable;
    private JButton btnAddPerson;
    private JButton btnRemove;
    private JButton btnSearch;
    private JLabel lblCemetery = new JLabel(tr("Cemetery"));
    private AutoCompletingTextField cbCemetery = new AutoCompletingTextField();
    private JLabel lblDenomination = new JLabel(tr("Denomination"));
    private JLabel lblInscription = new JLabel(tr("Inscription"));
    private JLabel lblWikidata = new JLabel(tr("Wikidata"));
    private JLabel lblSectionName = new JLabel(tr("Section Name"));
    private JLabel lblSectionRow = new JLabel(tr("Section Row"));
    private JLabel lblSectionPlace = new JLabel(tr("Section Place"));
    private JLabel lblRef = new JLabel(tr("Ref"));
    private JLabel lblDescription = new JLabel(tr("Description"));
    private JLabel lblHistoric = new JLabel(tr("Historic"));
    private JLabel lblTombType = new JLabel(tr("Tomb Type"));
    private JLabel lblReligion = new JLabel(tr("Religion"));
    private JLabel lblTombData = new JLabel(tr("Tomb Data"));
    private JLabel lblImage = new JLabel(tr("Image"));
    private JLabel lblWikimedia_commons = new JLabel(tr("Wikimedia Commons"));
    private JLabel lblFlickr = new JLabel(tr("Flickr"));
    private JLabel lblWikipediaArticle = new JLabel(tr("Wikipedia Article"));
    private OsmPrimitive tombPrimitive;
    private TombDialogAction tombDialogAction;

    public TombDialog(TombDialogAction action) {
        super();
        this.tombDialogAction = action;
        setTitle("Tomb Editor"); // Ustaw tytuł okna
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        setTitle("Tomb editor");
        setBounds(100, 100, 1024, 400);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            panel.setBorder(new EmptyBorder(0, 0, 0, 0));
            contentPanel.add(panel, BorderLayout.NORTH);
            panel.setLayout(new BorderLayout(0, 0));
            {
                JPanel panel_1 = new JPanel();
                panel.add(panel_1, BorderLayout.CENTER);
                panel_1.setLayout(new FormLayout(new ColumnSpec[] {
                        ColumnSpec.decode("100px"),
                        FormSpecs.RELATED_GAP_COLSPEC,
                        ColumnSpec.decode("220px:grow"),
                        ColumnSpec.decode("30dlu"),
                        FormSpecs.DEFAULT_COLSPEC,
                        FormSpecs.RELATED_GAP_COLSPEC,
                        ColumnSpec.decode("default:grow"),},
                        new RowSpec[] {
                                FormSpecs.DEFAULT_ROWSPEC,
                                FormSpecs.RELATED_GAP_ROWSPEC,
                                FormSpecs.DEFAULT_ROWSPEC,
                                FormSpecs.RELATED_GAP_ROWSPEC,
                                FormSpecs.DEFAULT_ROWSPEC,
                                FormSpecs.RELATED_GAP_ROWSPEC,
                                RowSpec.decode("12dlu"),}));
                {
                    JLabel lblHistoric = new JLabel("Historic kind");
                    lblHistoric.setFont(lblHistoric.getFont().deriveFont(lblHistoric.getFont().getStyle() | Font.BOLD));
                    panel_1.add(lblHistoric, "1, 1, left, default");
                }
                {
                    cbHistoric = action.getCbHistoric();
                    cbHistoric.setEditable(true);
                    panel_1.add(cbHistoric, "3, 1, fill, default");
                }

                {
                    JLabel lblTombData = new JLabel("Tomb data");
                    panel_1.add(lblTombData, "5, 1");
                }
                {
                    JLabel lblTombType = new JLabel("Tomb type");
                    lblTombType.setFont(lblTombType.getFont().deriveFont(lblTombType.getFont().getStyle() | Font.BOLD));
                    panel_1.add(lblTombType, "1, 3, left, default");
                }
                {
                    cbTombType = action.getCbTombType();
                    cbTombType.setEditable(true);
                    panel_1.add(cbTombType, "3, 3, fill, default");
                }
                {
                    JLabel lblWikipediaArticle = new JLabel("- wikipedia article");
                    panel_1.add(lblWikipediaArticle, "5, 3, left, default");
                }
                {
                    txtWikipedia = new JTextField();
                    panel_1.add(txtWikipedia, "7, 3, fill, default");
                    txtWikipedia.setColumns(10);
                }
                {
                    JLabel lblReligion = new JLabel("Religion");
                    panel_1.add(lblReligion, "1, 5, left, default");
                }
                {
                    cbReligion = action.getCbReligion();
                    cbReligion.setEditable(true);
                    panel_1.add(cbReligion, "3, 5, fill, default");
                }
                {
                    JLabel lblDenomination = new JLabel("Denomination");
                    panel_1.add(lblDenomination, "1, 5, left, default");
                }
                {
                    cbDenomination = action.getCbDenomination();
                    cbDenomination.setEditable(true);
                    panel_1.add(cbDenomination, "3, 5, fill, default");
                }

                {
                    JLabel lblImage = new JLabel("- image");
                    panel_1.add(lblImage, "5, 5, left, default");
                }

                {
                    txtImage = new JTextField();
                    panel_1.add(txtImage, "7, 5, fill, default");
                    txtImage.setColumns(10);
                }

                {
                    JLabel lblWikimedia_commons = new JLabel("- wikimedia_commons");
                    panel_1.add(lblWikimedia_commons, "5, 5, left, default"); // Poprawka: użyłem lblWikimedia_commons
                }
                {
                    txtWikimedia_commons = new JTextField();
                    panel_1.add(txtWikimedia_commons, "7, 5, fill, default");
                    txtWikimedia_commons.setColumns(10);
                }
                {
                    JLabel lblFlickr = new JLabel("- flickr");
                    panel_1.add(lblFlickr, "5, 5, left, default"); // Poprawka: użyłem lblFlickr
                }
                {
                    txtFlickr = new JTextField();
                    panel_1.add(txtFlickr, "7, 5, fill, default");
                    txtFlickr.setColumns(10);
                }

            }
        }
        {
            JPanel panel = new JPanel();
            panel.setBorder(new EmptyBorder(0, 0, 0, 0));
            contentPanel.add(panel, BorderLayout.CENTER);
            panel.setLayout(new BorderLayout(0, 0));
            {
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                panel.add(scrollPane);
                {
                    personsTable = new JTable();
                    int fontHeight = personsTable.getFontMetrics(personsTable.getFont()).getHeight();
                    personsTable.setRowHeight(fontHeight + 2);
                    personsTable.setModel(new DefaultTableModel(
                            new Object[][] {
                                    {"1", "2", "3"},
                                    {"4", "5", "6"},
                            },
                            new String[] {
                                    "New column", "New column", "New column 1"
                            }
                    ));
                    scrollPane.setViewportView(personsTable);
                }
            }
            {
                JPanel panel_tableButtons = new JPanel();
                panel.add(panel_tableButtons, BorderLayout.SOUTH);
                {
                    btnAddPerson = new JButton("New");
                    btnAddPerson.setToolTipText("New person (Ctrl-n)");
                    btnAddPerson.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                            onAddPerson();
                        }
                    });
                    panel_tableButtons.add(btnAddPerson);
                }
                {
                    btnRemove = new JButton("Remove");
                    btnRemove.setToolTipText("Remove person from tomb");
                    btnRemove.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            onRemovePerson(personsTable.getSelectedRows());
                        }
                    });
                    panel_tableButtons.add(btnRemove);
                }
                {
                    btnSearch = new JButton("Search");
                    btnSearch.setToolTipText("Search person in OSM database");
                    btnSearch.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            onSearch();
                        }
                    });
                    panel_tableButtons.add(btnSearch);
                }
            }
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        onSave();
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

    public void Load(OsmPrimitive tombPrimitive) {
        tombDialogAction.Load(tombPrimitive);
        // Dodatkowa logika ładowania specyficzna dla okna dialogowego, jeśli jest potrzebna
    }

    @Override
    public void setVisible(boolean b) {
        pack(); // Ponownie dopasuj rozmiar przed wyświetleniem
        super.setVisible(b);
    }
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            TombDialog dialog = new TombDialog(new TombDialogAction()); // Teraz przekazujemy instancję TombDialogAction
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onSave() {
        //
    }

    protected void onAddPerson() {
        //
    }

    protected void onRemovePerson(int [] rowsId) {
        //
    }

    protected void onSearch() {
        //
    }

    protected JLabel getLblTombType() {
        return lblTombType;
    }

    public JLabel getLblReligion() {
        return lblReligion;
    }
    public JLabel getLblTombData() {
        return lblTombData;
    }
    public JLabel getLblWikipediaArticle() {
        return lblWikipediaArticle;
    }
    public JLabel getLblImage() {
        return lblImage;
    }
    public JLabel getLblWikimedia_commons() {
        return lblWikimedia_commons;
    }
    public JLabel getLblFlickr() {
        return lblFlickr;
    }
    public JLabel getLblHistoric() {
        return lblHistoric;
    }
public AutoCompletingTextField getCbTombType() {
    return cbTombType;
}
public AutoCompletingTextField getCbHistoric() {
    return cbHistoric;
}
}
