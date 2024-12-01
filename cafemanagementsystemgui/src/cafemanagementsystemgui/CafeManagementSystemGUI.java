/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cafemanagementsystemgui;

/**
 *
 * @author Saad
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CafeManagementSystemGUI extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private List<Product> inventory = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private ViewInventoryPanel viewInventoryPanel;
    private ViewMenuPanel viewMenuPanel;
    private ViewAnalyticsPanel viewAnalyticsPanel;

    public CafeManagementSystemGUI() {
        setTitle("Cafe Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        // To create Sidebar
        JPanel sidebar = Sidebar();
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        mainPanel.setBackground(Color.WHITE);

        // To initialize Panels
        viewInventoryPanel = new ViewInventoryPanel();
        viewMenuPanel = new ViewMenuPanel();
        viewAnalyticsPanel = new ViewAnalyticsPanel();

        mainPanel.add(viewInventoryPanel, "Inventory Management");
        mainPanel.add(viewMenuPanel, "Menu and Order");
        mainPanel.add(viewAnalyticsPanel, "Analytics");

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel Sidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1));
        sidebar.setBackground(new Color(85, 53, 30));  
        sidebar.setPreferredSize(new Dimension(250, 0));

        String[] options = {"Inventory Management", "Menu and Order", "Analytics"};
        for (String option : options) {
            JButton button = Button(option);
            button.addActionListener(e -> cardLayout.show(mainPanel, option));
            sidebar.add(button);
        }
        return sidebar;
    }

    private JButton Button(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(160, 90, 44)); 
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(250, 50));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(191, 111, 61));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(160, 90, 44));
            }
        });
        return button;
    }

    private JTextField TextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return textField;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(85, 53, 30));
        return label;
    }

    // Inventory Management Panel
    private class ViewInventoryPanel extends JPanel {
        private JTable inventoryTable;
        private JTextField idField, nameField, stockField, priceField, statusField, dateField;

        public ViewInventoryPanel() {
            setLayout(new BorderLayout());
            String[] columns = {"Product ID", "Product Name", "Stock", "Price", "Status", "Date"};
            inventoryTable = new JTable(new DefaultTableModel(columns, 0));
            refreshInventoryTable();

            JPanel productDetailsPanel = new JPanel();
            productDetailsPanel.setLayout(new GridLayout(0, 2, 10, 10));
            productDetailsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            idField = TextField();
            nameField = TextField();
            stockField = TextField();
            priceField = TextField();
            statusField = TextField();
            dateField = TextField();

            productDetailsPanel.add(createStyledLabel("Product ID:"));
            productDetailsPanel.add(idField);
            productDetailsPanel.add(createStyledLabel("Product Name:"));
            productDetailsPanel.add(nameField);
            productDetailsPanel.add(createStyledLabel("Stock:"));
            productDetailsPanel.add(stockField);
            productDetailsPanel.add(createStyledLabel("Price:"));
            productDetailsPanel.add(priceField);
            productDetailsPanel.add(createStyledLabel("Status:"));
            productDetailsPanel.add(statusField);
            productDetailsPanel.add(createStyledLabel("Date (YYYY-MM-DD):"));
            productDetailsPanel.add(dateField);

            JButton addButton = Button("Add Product");
            addButton.addActionListener(e -> addProduct());

            JButton updateButton = Button("Update Product");
            updateButton.addActionListener(e -> updateProduct());

            JButton deleteButton = Button("Delete Product");
            deleteButton.addActionListener(e -> deleteProduct());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(addButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);

            add(productDetailsPanel, BorderLayout.NORTH);
            add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            inventoryTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int selectedRow = inventoryTable.getSelectedRow();
                    if (selectedRow != -1) {
                        populateFields(selectedRow);
                    }
                }
            });
        }

        private void refreshInventoryTable() {
            DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
            model.setRowCount(0);
            for (Product product : inventory) {
                model.addRow(new Object[] {
                        product.getId(), product.getName(), product.getStock(), product.getPrice(), product.getStatus(), product.getDate()
                });
            }
        }

        private void addProduct() {
            Product product = new Product(
                    idField.getText(),
                    nameField.getText(),
                    Integer.parseInt(stockField.getText()),
                    Double.parseDouble(priceField.getText()),
                    statusField.getText(),
                    dateField.getText()
            );
            inventory.add(product);
            refreshInventoryTable();
            clearFields();
            viewMenuPanel.refreshMenuTable();
        }

        private void updateProduct() {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow >= 0) {
                Product product = inventory.get(selectedRow);
                product.setId(idField.getText());
                product.setName(nameField.getText());
                product.setStock(Integer.parseInt(stockField.getText()));
                product.setPrice(Double.parseDouble(priceField.getText()));
                product.setStatus(statusField.getText());
                product.setDate(dateField.getText());
                refreshInventoryTable();
                clearFields();
                viewMenuPanel.refreshMenuTable();
            } else {
                JOptionPane.showMessageDialog(this, "Select a product to update.");
            }
        }

        private void deleteProduct() {
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow >= 0) {
                inventory.remove(selectedRow);
                refreshInventoryTable();
                clearFields();
                viewMenuPanel.refreshMenuTable();
            } else {
                JOptionPane.showMessageDialog(this, "Select a product to delete.");
            }
        }

        private void populateFields(int selectedRow) {
            Product product = inventory.get(selectedRow);
            idField.setText(product.getId());
            nameField.setText(product.getName());
            stockField.setText(String.valueOf(product.getStock()));
            priceField.setText(String.valueOf(product.getPrice()));
            statusField.setText(product.getStatus());
            dateField.setText(product.getDate());
        }

        private void clearFields() {
            idField.setText("");
            nameField.setText("");
            stockField.setText("");
            priceField.setText("");
            statusField.setText("");
            dateField.setText("");
        }
    }

    // Menu and Order Panel
    private class ViewMenuPanel extends JPanel {
        private JTable menuTable;
        private JTextField customerNameField;

        public ViewMenuPanel() {
            setLayout(new BorderLayout());
            String[] columns = {"Product ID", "Product Name", "Price", "Quantity"};
            DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
            menuTable = new JTable(tableModel);
            menuTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            menuTable.getColumnModel().getColumn(3).setCellEditor(new SpinnerEditor());

            refreshMenuTable();

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(10, 10));

            customerNameField = TextField();
            topPanel.add(createStyledLabel("Customer Name: "), BorderLayout.WEST);
            topPanel.add(customerNameField, BorderLayout.CENTER);

            JButton orderButton = Button("Place Order");
            orderButton.addActionListener(e -> placeOrder());
            topPanel.add(orderButton, BorderLayout.EAST);

            add(topPanel, BorderLayout.NORTH);
            add(new JScrollPane(menuTable), BorderLayout.CENTER);
        }

        private void refreshMenuTable() {
            DefaultTableModel model = (DefaultTableModel) menuTable.getModel();
            model.setRowCount(0);
            for (Product product : inventory) {
                model.addRow(new Object[] {
                        product.getId(), product.getName(), product.getPrice(), 0
                });
            }
        }

        private void placeOrder() {
            String customerName = customerNameField.getText();
            if (customerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a customer name.");
                return;
            }

            boolean orderPlaced = false;
            for (int i = 0; i < menuTable.getRowCount(); i++) {
                int quantity = (int) menuTable.getValueAt(i, 3);
                if (quantity > 0) {
                    String productId = (String) menuTable.getValueAt(i, 0);
                    String productName = (String) menuTable.getValueAt(i, 1);
                    double price = (Double) menuTable.getValueAt(i, 2);
                    Order order = new Order(customerName, productId, productName, quantity, price);
                    orders.add(order);
                    orderPlaced = true;
                }
            }

            if (orderPlaced) {
                JOptionPane.showMessageDialog(this, "Order placed successfully for " + customerName + "!");
                customerNameField.setText("");
                viewAnalyticsPanel.refreshAnalytics();
                viewAnalyticsPanel.refreshOrderHistory();
            } else {
                JOptionPane.showMessageDialog(this, "No items selected for the order.");
            }
        }
    }

    // Analytics Panel
    private class ViewAnalyticsPanel extends JPanel {
        private JTextArea analyticsArea;
        private JTable orderHistoryTable;

        public ViewAnalyticsPanel() {
            setLayout(new BorderLayout());
            analyticsArea = new JTextArea(5, 20);
            analyticsArea.setEditable(false);
            analyticsArea.setBackground(new Color(240, 240, 240));
            analyticsArea.setFont(new Font("Arial", Font.PLAIN, 14));
            analyticsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            orderHistoryTable = new JTable(new DefaultTableModel(new String[] {
                    "Customer Name", "Product ID", "Product Name", "Quantity", "Price", "Total"
            }, 0));
            orderHistoryTable.setFont(new Font("Arial", Font.PLAIN, 14));

            add(new JScrollPane(analyticsArea), BorderLayout.NORTH);
            add(new JScrollPane(orderHistoryTable), BorderLayout.CENTER);
        }

        public void refreshAnalytics() {
            StringBuilder sb = new StringBuilder();
            sb.append("Orders Placed: ").append(orders.size()).append("\n");

            double totalRevenue = 0;
            for (Order order : orders) {
                totalRevenue += order.getPrice() * order.getQuantity();
            }
            sb.append("Total Revenue: $").append(String.format("%.2f", totalRevenue)).append("\n");

            analyticsArea.setText(sb.toString());
        }

        public void refreshOrderHistory() {
            DefaultTableModel model = (DefaultTableModel) orderHistoryTable.getModel();
            model.setRowCount(0);
            for (Order order : orders) {
                double total = order.getPrice() * order.getQuantity();
                model.addRow(new Object[] {
                        order.getCustomerName(),order.getProductId(),
                        order.getProductName(),
                        order.getQuantity(),
                        String.format("%.2f", order.getPrice()),
                        String.format("%.2f", total)
                });
            }
        }
    }

    // Product Class
    private class Product {
        private String id;
        private String name;
        private int stock;
        private double price;
        private String status;
        private String date;

        public Product(String id, String name, int stock, double price, String status, String date) {
            this.id = id;
            this.name = name;
            this.stock = stock;
            this.price = price;
            this.status = status;
            this.date = date;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public int getStock() { return stock; }
        public double getPrice() { return price; }
        public String getStatus() { return status; }
        public String getDate() { return date; }

        public void setId(String id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setStock(int stock) { this.stock = stock; }
        public void setPrice(double price) { this.price = price; }
        public void setStatus(String status) { this.status = status; }
        public void setDate(String date) { this.date = date; }
    }

    // Order Class
    private class Order {
        private String customerName;
        private String productId;
        private String productName;
        private int quantity;
        private double price;

        public Order(String customerName, String productId, String productName, int quantity, double price) {
            this.customerName = customerName;
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }

        public String getCustomerName() { return customerName; }
        public String getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
    }

    // Spinner Editor for Quantity
    private class SpinnerEditor extends DefaultCellEditor {
        private JSpinner spinner;

        public SpinnerEditor() {
            super(new JTextField());
            spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
            spinner.setBorder(BorderFactory.createLineBorder(new Color(160, 90, 44)));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return spinner;
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }
    }

    // Main Method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminLoginForm();  
        });
    }
}