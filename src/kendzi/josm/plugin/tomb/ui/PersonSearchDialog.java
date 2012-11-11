package kendzi.josm.plugin.tomb.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PersonSearchDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private JTextField txtName;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            PersonSearchDialog dialog = new PersonSearchDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public PersonSearchDialog() {
        setBounds(100, 100, 1024, 400);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            {
                JLabel lblName = new JLabel("Name");
                panel.add(lblName);
            }
            {
                txtName = new JTextField();
                txtName.setText("Kowalski");
                txtName.setToolTipText("Type name (case sensitive)");
                panel.add(txtName);
                txtName.setColumns(10);
            }
            {
                JButton btnSearch = new JButton("Search");
                btnSearch.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        doSearch();
                    }


                });
                panel.add(btnSearch);
            }
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.CENTER);
            panel.setLayout(new BorderLayout(0, 0));
            {
                JScrollPane scrollPane = new JScrollPane();
                panel.add(scrollPane);
                {
                    table = new JTable();
                    table.setModel(new DefaultTableModel(
                            new Object[][] {
                                    {"123", "1234"},
                            },
                            new String[] {
                                    "Test", "New column"
                            }
                            ));
                    scrollPane.setViewportView(table);
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
                    public void actionPerformed(ActionEvent e) {
                        doOk(getTable().getSelectedRows());
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

    protected void doSearch() {
        //
    }

    protected void doOk(int[] rowId) {
        //
    }

    public JTextField getTxtName() {
        return txtName;
    }
    public JTable getTable() {
        return table;
    }
}
