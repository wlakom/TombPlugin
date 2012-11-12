/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.plugin.tomb.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 *
 * @author Tomasz Kędziora (Kendzi)
 */
public class TombDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    protected JTable personsTable;
    protected JTextField txtWikipedia;
    protected JTextField txtImage;
    protected JComboBox cbTombType;
    protected JComboBox cbReligion;
    private JLabel lblTombType;
    private JLabel lblReligion;
    private JLabel lblTombData;
    private JLabel lblWikipediaArticle;
    private JLabel lblImage;
    private JLabel lblHistoric;
    private JComboBox cbHistoric;
    private JButton btnSearch;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            TombDialog dialog = new TombDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public TombDialog() {
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
                        FormFactory.RELATED_GAP_COLSPEC,
                        ColumnSpec.decode("220px:grow"),
                        ColumnSpec.decode("30dlu"),
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.RELATED_GAP_COLSPEC,
                        ColumnSpec.decode("default:grow"),},
                        new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("12dlu"),}));
                {
                    lblHistoric = new JLabel("Historic kind");
                    lblHistoric.setFont(lblHistoric.getFont().deriveFont(lblHistoric.getFont().getStyle() | Font.BOLD));
                    panel_1.add(lblHistoric, "1, 1, left, default");
                }
                {
                    cbHistoric = new JComboBox();
                    cbHistoric.setModel(new DefaultComboBoxModel(new String[] {"", "tomb", "memorial"}));
                    cbHistoric.setEditable(true);
                    panel_1.add(cbHistoric, "3, 1, fill, default");
                }
                {
                    lblTombData = new JLabel("Tomb data");
                    panel_1.add(lblTombData, "5, 1");
                }
                {
                    lblTombType = new JLabel("Tomb type");
                    lblTombType.setFont(lblTombType.getFont().deriveFont(lblTombType.getFont().getStyle() | Font.BOLD));
                    panel_1.add(lblTombType, "1, 3, left, default");
                }
                {
                    cbTombType = new JComboBox();
                    cbTombType.setEditable(true);
                    cbTombType.setModel(new DefaultComboBoxModel(new String[] {"", "tombstone", "tumulus", "rock-cut", "war_grave", "mausoleum", "columbarium", "pyramid", "sarcophagus", "vault"}));
                    panel_1.add(cbTombType, "3, 3, fill, default");
                }
                {
                    lblWikipediaArticle = new JLabel("- wikipedia article");
                    panel_1.add(lblWikipediaArticle, "5, 3, left, default");
                }
                {
                    txtWikipedia = new JTextField();
                    panel_1.add(txtWikipedia, "7, 3, fill, default");
                    txtWikipedia.setColumns(10);
                }
                {
                    lblReligion = new JLabel("Religion");
                    panel_1.add(lblReligion, "1, 5, left, default");
                }
                {
                    cbReligion = new JComboBox();
                    cbReligion.setEditable(true);
                    cbReligion.setModel(new DefaultComboBoxModel(new String[] {"", "christian", "jewish", "muslim"}));
                    panel_1.add(cbReligion, "3, 5, fill, default");
                }
                {
                    lblImage = new JLabel("- image");
                    panel_1.add(lblImage, "5, 5, left, default");
                }
                {
                    txtImage = new JTextField();
                    panel_1.add(txtImage, "7, 5, fill, default");
                    txtImage.setColumns(10);
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
                    //                    personsTable.setRowHeight(17);
                    //                    personsTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
                    //                    personsTable.setRowHeight(20);

                    //                    personsTable.getFont().get
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
                    JButton btnAddPerson = new JButton("New");
                    btnAddPerson.setToolTipText("New person (Ctrl-n)");
                    btnAddPerson.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                            onAddPerson();
                        }
                    });
                    panel_tableButtons.add(btnAddPerson);
                }
                {
                    JButton btnRemove = new JButton("Remove");
                    btnRemove.setToolTipText("Remove person from tomb");
                    btnRemove.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {

                            ;
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
    public JLabel getLblHistoric() {
        return lblHistoric;
    }
    public JComboBox getCbTombType() {
        return cbTombType;
    }
    public JComboBox getCbHistoric() {
        return cbHistoric;
    }
}
