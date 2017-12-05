package posProject;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;

public class POSGui extends JFrame implements WindowListener {

    //Main panel setup
    private JPanel mainPanel;
    private JTable productDataTable;
    private JButton updateAnExistingProductButton;
    private JButton addItem;
    private JButton deleteButton;
    private JPanel tablePanel;
    private JComboBox viewComboBox;
    private JLabel viewLabel;
    private JScrollPane productScroll;

    //Add panel setup
    private JPanel addPanel;
    private JTextField getPriceTextField;
    private JTextField getNameTextField;
    private JButton addButton;
    private JComboBox typeComboBox;
    private JComboBox subtypeComboBox;
    private JLabel nameLabel;
    private JLabel priceLabel;
    private JLabel typeLabel;
    private JLabel subtypeLabel;
    private JButton cancelButton;

    //Delete panel setup
    private JComboBox deleteIDTypeComboBox;
    private JTextField deleteIDTextField;
    private JButton deleteIDButton;
    private JPanel deleteIDPanel;
    private JLabel deleteIDTypeComboBoxLabel;
    private JLabel deleteIDlabel;
    private JButton deleteByIDCancel;
    private JTabbedPane POSTabbedPane;

    //Model setup
    private final DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel(new String[]{"Select Type", "Food", "Drink", "Merchandise"});
    private final POSModel newDefaultModel;
    private final POSModel newFoodModel;
    private final POSModel newDrinkModel;
    private final POSModel newMerchandiseModel;

    public POSGui(POSModel defaultModel, POSModel foodModel, POSModel drinkModel, POSModel merchandiseModel) {

        //GUI setup
        POSTabbedPane.removeAll();
        POSTabbedPane.addTab("Point of Sales System", mainPanel);
        setContentPane(POSTabbedPane);
        pack();
        addWindowListener(this);
        setVisible(true);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        viewLabel.setSize(150, 50);

        //Sets Models
        newDefaultModel = defaultModel;
        newFoodModel = foodModel;
        newDrinkModel = drinkModel;
        newMerchandiseModel = merchandiseModel;

        //JTable setup
        productDataTable.setModel(defaultModel);
        productScroll.setViewportView(productDataTable);
        productDataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader header = productDataTable.getTableHeader();
        TableColumnModel columnModel = header.getColumnModel();
        TableColumn tableColumn = columnModel.getColumn(0);
        tableColumn.setHeaderValue("ID");
        tableColumn = columnModel.getColumn(1);
        tableColumn.setHeaderValue("Product Name");
        tableColumn = columnModel.getColumn(2);
        tableColumn.setHeaderValue("Price");
        tableColumn = columnModel.getColumn(3);
        tableColumn.setHeaderValue("Subtype");

        //Event handler to add a product and configuration for its associated buttons/event listeners
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        //Event hander to delete a product. Has a switch for the method the user wishes to use to delete
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] deleteOptions = {"ID", "Name", "Selection", "Cancel"};
                int response = JOptionPane.showOptionDialog(null, "Choose a method to delete by",
                        "Delete Product", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, deleteOptions,
                        deleteOptions[0]);
                switch (response) {
                    case 0:
                        deleteByID();
                        break;
                    case 1:
                        deleteByName();
                        break;
                    case 2:
                        deleteBySelection();
                        break;
                    default:
                        break;
                }
            }
        });

        typeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String type = String.valueOf(typeComboBox.getSelectedItem()).trim();
                DefaultComboBoxModel drinkModel = new DefaultComboBoxModel(new String[]{"Select Subtype", "Cocktail", "Bottle Beer", "Draft Beer", "Wine", "N/A"});
                DefaultComboBoxModel foodModel = new DefaultComboBoxModel(new String[]{"Select Subtype", "Entree", "Appetizer", "Dessert", "Soup", "Side", "Misc", "Special", "Salad"});
                DefaultComboBoxModel merchModel = new DefaultComboBoxModel(new String[]{"Select Subtype", "Clothing", "Misc", "Gift Card", "Glassware"});
                switch (type) {
                    case "Food":
                        subtypeComboBox.setModel(foodModel);
                        break;
                    case "Drink":
                        subtypeComboBox.setModel(drinkModel);
                        break;
                    case "Merchandise":
                        subtypeComboBox.setModel(merchModel);
                        break;
                    default:
                        subtypeComboBox.setModel(new DefaultComboBoxModel());
                        break;
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem.setEnabled(true);
                POSTabbedPane.removeTabAt(POSTabbedPane.getTabPlacement());
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = getNameTextField.getText();
                String price = getPriceTextField.getText();
                String type = String.valueOf(typeComboBox.getSelectedItem()).trim();
                String subType = String.valueOf(subtypeComboBox.getSelectedItem()).trim();

                boolean validation = addInputValidation(name, price, type, subType);

                if (validation) {
                    try {
                        double convertPrice = Double.parseDouble(price);

                        switch (type) {
                            case "Food":
                                newFoodModel.addProduct(name, convertPrice, subType);
                                break;
                            case "Drink":
                                newDrinkModel.addProduct(name, convertPrice, subType);
                                break;
                            case "Merchandise":
                                newMerchandiseModel.addProduct(name, convertPrice, subType);
                                break;
                            default:
                                break;
                        }
                        POSTabbedPane.setEnabledAt(1, true);
                        POSTabbedPane.setSelectedIndex(0);
                        POSTabbedPane.removeTabAt(POSTabbedPane.getTabPlacement());
                    } catch (NumberFormatException nfee) {
                        showMessageDialog("Please enter in a valid number for the products price");
                    }
                }
            }
        });

        //Switches database result sets
        viewComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (String.valueOf(viewComboBox.getSelectedItem()).trim()) {
                    case "Food":
                        productDataTable.setModel(newFoodModel);
                        newFoodModel.fireTableDataChanged();
                        viewLabel.setText("Viewing");
                        break;
                    case "Drinks":
                        productDataTable.setModel(newDrinkModel);
                        newDrinkModel.fireTableDataChanged();
                        viewLabel.setText("Viewing");
                        break;
                    case "Merchandise":
                        productDataTable.setModel(newMerchandiseModel);
                        newMerchandiseModel.fireTableDataChanged();
                        viewLabel.setText("Viewing");
                        break;
                    default:
                        productDataTable.setModel(newDefaultModel);
                        newDefaultModel.fireTableDataChanged();
                        viewLabel.setText("View Group");
                        break;
                }
            }
        });
    }
    private void deleteByName() {

    }

    private void deleteBySelection() {
        switch (String.valueOf(viewComboBox.getSelectedItem()).trim()) {
            case "Food":
                newFoodModel.deleteProduct(productDataTable.getSelectedRow());
                newFoodModel.fireTableDataChanged();
                viewLabel.setText("Viewing");
                break;
            case "Drinks":
                newDrinkModel.deleteProduct(productDataTable.getSelectedRow());
                newDrinkModel.fireTableDataChanged();
                viewLabel.setText("Viewing");
                break;
            case "Merchandise":
                newMerchandiseModel.deleteProduct(productDataTable.getSelectedRow());
                newMerchandiseModel.fireTableDataChanged();
                viewLabel.setText("Viewing");
                break;
            default:
                newDefaultModel.deleteProduct(productDataTable.getSelectedRow());
                newDefaultModel.fireTableDataChanged();
                viewLabel.setText("View Group");
                break;
        }

    }

    //Deletes by ID given. Uses primary key to compare user input, and the ID to be deleted
    private void deleteByID() {
        POSTabbedPane.addTab("Delete By ID", deleteIDPanel);
        POSTabbedPane.setSelectedIndex(1);
        POSTabbedPane.setEnabledAt(0, false);
        deleteIDTypeComboBox.setModel(defaultComboBoxModel);

        deleteIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ID = deleteIDTextField.getText();
                try {
                    int convertID = Integer.parseInt(ID);
                    switch (String.valueOf(deleteIDTypeComboBox.getSelectedItem()).trim()) {
                        case "Food":
                            newFoodModel.deleteByID(convertID);
                            break;
                        case "Drinks":
                            newDrinkModel.deleteByID(convertID);
                            break;
                        case "Merchandise":
                            newMerchandiseModel.deleteByID(convertID);
                            break;
                        default:
                            break;
                    }
                    System.out.println("Product was DELETED!");
                    POSTabbedPane.setEnabledAt(1, true);
                    POSTabbedPane.setSelectedIndex(0);
                    POSTabbedPane.removeTabAt(POSTabbedPane.getTabPlacement());
                } catch (NumberFormatException nfee) {
                    showMessageDialog("ID was not found or ID was invalid");
                }
            }
        });
        deleteByIDCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                POSTabbedPane.setEnabledAt(1, true);
                POSTabbedPane.setSelectedIndex(0);
                POSTabbedPane.removeTabAt(POSTabbedPane.getTabPlacement());
            }
        });
    }

    //Method to add a product to a database. Has input validation for each field.
    private void addProduct() {
        POSTabbedPane.addTab("Add Product", addPanel);
        POSTabbedPane.setSelectedIndex(1);
        POSTabbedPane.setEnabledAt(0, false);
        subtypeComboBox.setModel(new DefaultComboBoxModel());
        typeComboBox.setModel(defaultComboBoxModel);
        typeComboBox.setSelectedIndex(0);

    }
    private boolean addInputValidation(String name, String price, String type, String subtype) {
        if (name == null || name.isEmpty()) {
            showMessageDialog("Please enter in a name for the product you wish to add");
            return false;
        } else if (type.equals("") || type.isEmpty()) {
            showMessageDialog("Please select the type for the product you wish to add");
            return false;
        } else if (price == null || price.isEmpty()) {
            showMessageDialog("Please enter in a price for the product you wish to add");
            return false;
        } else if (subtype.equals("") || subtype.isEmpty()) {
            showMessageDialog("Please select a subtype for the product you wish to add");
            return false;
        } else {
            return true;
        }
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }


    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("closing");
        DBConfig.shutdown();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
