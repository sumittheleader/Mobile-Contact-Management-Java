import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * Mobile Contact Management System
 * A comprehensive Java Swing application for managing personal and professional contacts
 * with data persistence, search, and CRUD operations
 */
public class MobileContactManager extends JFrame {
    
    // ==================== CONTACT CLASS ====================
    /**
     * Contact class representing a single contact entry
     * Demonstrates encapsulation and OOP principles
     */
    static class Contact implements Serializable {
        private static final long serialVersionUID = 1L;
        
        // Private attributes for encapsulation
        private String id;
        private String name;
        private String mobile;
        private String email;
        private String address;
        private String category;
        private Date createdDate;
        private Date modifiedDate;
        
        // Constructor
        public Contact(String name, String mobile, String email, String address, String category) {
            this.id = UUID.randomUUID().toString();
            this.name = name;
            this.mobile = mobile;
            this.email = email;
            this.address = address;
            this.category = category;
            this.createdDate = new Date();
            this.modifiedDate = new Date();
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getMobile() { return mobile; }
        public String getEmail() { return email; }
        public String getAddress() { return address; }
        public String getCategory() { return category; }
        public Date getCreatedDate() { return createdDate; }
        public Date getModifiedDate() { return modifiedDate; }
        
        // Setters with validation
        public void setName(String name) {
            if (name != null && !name.trim().isEmpty()) {
                this.name = name;
                this.modifiedDate = new Date();
            }
        }
        
        public void setMobile(String mobile) {
            if (mobile != null && !mobile.trim().isEmpty()) {
                this.mobile = mobile;
                this.modifiedDate = new Date();
            }
        }
        
        public void setEmail(String email) {
            this.email = email;
            this.modifiedDate = new Date();
        }
        
        public void setAddress(String address) {
            this.address = address;
            this.modifiedDate = new Date();
        }
        
        public void setCategory(String category) {
            this.category = category;
            this.modifiedDate = new Date();
        }
        
        @Override
        public String toString() {
            return name + " - " + mobile;
        }
    }
    
    // ==================== CONTACT MANAGER CLASS ====================
    /**
     * ContactManager class for business logic and data operations
     * Handles CRUD operations and file persistence
     */
    static class ContactManager {
        private List<Contact> contacts;
        private static final String DATA_FILE = "contacts.dat";
        
        public ContactManager() {
            contacts = new ArrayList<>();
            loadContacts();
        }
        
        // Add contact
        public boolean addContact(Contact contact) {
            if (contact != null && isValidContact(contact)) {
                // Check for duplicate mobile number
                if (findContactByMobile(contact.getMobile()) == null) {
                    contacts.add(contact);
                    saveContacts();
                    return true;
                }
            }
            return false;
        }
        
        // Update contact
        public boolean updateContact(String id, Contact updatedContact) {
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).getId().equals(id)) {
                    Contact existingContact = contacts.get(i);
                    
                    // Check if mobile number is being changed to an existing one
                    if (!existingContact.getMobile().equals(updatedContact.getMobile())) {
                        Contact duplicate = findContactByMobile(updatedContact.getMobile());
                        if (duplicate != null && !duplicate.getId().equals(id)) {
                            return false; // Duplicate mobile number
                        }
                    }
                    
                    existingContact.setName(updatedContact.getName());
                    existingContact.setMobile(updatedContact.getMobile());
                    existingContact.setEmail(updatedContact.getEmail());
                    existingContact.setAddress(updatedContact.getAddress());
                    existingContact.setCategory(updatedContact.getCategory());
                    saveContacts();
                    return true;
                }
            }
            return false;
        }
        
        // Delete contact
        public boolean deleteContact(String id) {
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).getId().equals(id)) {
                    contacts.remove(i);
                    saveContacts();
                    return true;
                }
            }
            return false;
        }
        
        // Search contacts
        public List<Contact> searchContacts(String keyword) {
            List<Contact> results = new ArrayList<>();
            if (keyword == null || keyword.trim().isEmpty()) {
                return new ArrayList<>(contacts);
            }
            
            String lowerKeyword = keyword.toLowerCase();
            for (Contact contact : contacts) {
                if (contact.getName().toLowerCase().contains(lowerKeyword) ||
                    contact.getMobile().contains(keyword) ||
                    (contact.getEmail() != null && contact.getEmail().toLowerCase().contains(lowerKeyword)) ||
                    (contact.getCategory() != null && contact.getCategory().toLowerCase().contains(lowerKeyword))) {
                    results.add(contact);
                }
            }
            return results;
        }
        
        // Get all contacts
        public List<Contact> getAllContacts() {
            return new ArrayList<>(contacts);
        }
        
        // Find contact by mobile
        private Contact findContactByMobile(String mobile) {
            for (Contact contact : contacts) {
                if (contact.getMobile().equals(mobile)) {
                    return contact;
                }
            }
            return null;
        }
        
        // Find contact by ID
        public Contact findContactById(String id) {
            for (Contact contact : contacts) {
                if (contact.getId().equals(id)) {
                    return contact;
                }
            }
            return null;
        }
        
        // Validate contact
        private boolean isValidContact(Contact contact) {
            return contact.getName() != null && !contact.getName().trim().isEmpty() &&
                   contact.getMobile() != null && !contact.getMobile().trim().isEmpty();
        }
        
        // Save contacts to file
        private void saveContacts() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
                oos.writeObject(contacts);
            } catch (IOException e) {
                System.err.println("Error saving contacts: " + e.getMessage());
            }
        }
        
        // Load contacts from file
        @SuppressWarnings("unchecked")
        private void loadContacts() {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                    contacts = (List<Contact>) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Error loading contacts: " + e.getMessage());
                    contacts = new ArrayList<>();
                }
            }
        }
        
        // Get statistics
        public int getTotalContacts() {
            return contacts.size();
        }
        
        public Map<String, Integer> getCategoryStats() {
            Map<String, Integer> stats = new HashMap<>();
            for (Contact contact : contacts) {
                String category = contact.getCategory();
                if (category == null || category.trim().isEmpty()) {
                    category = "Uncategorized";
                }
                stats.put(category, stats.getOrDefault(category, 0) + 1);
            }
            return stats;
        }
    }
    
    // ==================== GUI COMPONENTS ====================
    private ContactManager contactManager;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel statusLabel;
    private JLabel totalContactsLabel;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(31, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color PANEL_COLOR = Color.WHITE;
    public MobileContactManager() {
        contactManager = new ContactManager();
        initializeGUI();
        loadTableData();
    }
    
    private void initializeGUI() {
        setTitle("Mobile Contact Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Main panel with background color
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center panel with table
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel with status
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel titleLabel = new JLabel("Mobile Contact Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        // Statistics panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        statsPanel.setBackground(PRIMARY_COLOR);
        
        totalContactsLabel = new JLabel("Total Contacts: 0");
        totalContactsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalContactsLabel.setForeground(Color.WHITE);
        statsPanel.add(totalContactsLabel);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(statsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(BACKGROUND_COLOR);
        
        // Search and action panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(PANEL_COLOR);
        topPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(PANEL_COLOR);
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(searchLabel);
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch();
            }
        });
        searchPanel.add(searchField);
        
        JButton clearSearchBtn = createStyledButton("Clear", SECONDARY_COLOR);
        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadTableData();
        });
        searchPanel.add(clearSearchBtn);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(PANEL_COLOR);
        
        JButton addBtn = createStyledButton("Add Contact", SUCCESS_COLOR);
        addBtn.addActionListener(e -> showAddContactDialog());
        buttonPanel.add(addBtn);
        
        JButton editBtn = createStyledButton("Edit", PRIMARY_COLOR);
        editBtn.addActionListener(e -> showEditContactDialog());
        buttonPanel.add(editBtn);
        
        JButton deleteBtn = createStyledButton("Delete", DANGER_COLOR);
        deleteBtn.addActionListener(e -> deleteSelectedContact());
        buttonPanel.add(deleteBtn);
        
        JButton refreshBtn = createStyledButton("Refresh", SECONDARY_COLOR);
        refreshBtn.addActionListener(e -> loadTableData());
        buttonPanel.add(refreshBtn);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Table
        // IMPORTANT: Column names for JTable
         String[] columnNames = {"Name", "Mobile", "Email", "Address", "Category", "Created"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
      

        contactTable = new JTable(tableModel);
        
        contactTable.setIntercellSpacing(new Dimension(1, 5));

        contactTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contactTable.setRowHeight(30);
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactTable.setGridColor(new Color(189, 195, 199));
        
        // Table header styling
       contactTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        if (!isSelected) {
            if (row % 2 == 0) {
                c.setBackground(new Color(245, 247, 250)); // light gray
            } else {
                c.setBackground(Color.WHITE);
            }
            c.setForeground(Color.BLACK);
        }

        setFont(new Font("Segoe UI", Font.BOLD, 13));
        return c;
    }
});

        
        // Column widths
        contactTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        contactTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        contactTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        contactTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        contactTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        contactTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        
        // Double-click to view details
        contactTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showContactDetails();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(PANEL_COLOR);
        bottomPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel copyrightLabel = new JLabel("Mobile Contact Manager v1.0 | © 2025");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copyrightLabel.setForeground(Color.GRAY);
        
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(copyrightLabel, BorderLayout.EAST);
        
        return bottomPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    // Load all contacts into table
    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Contact> contacts = contactManager.getAllContacts();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        
        for (Contact contact : contacts) {
            Object[] row = {
                contact.getName(),
                contact.getMobile(),
                contact.getEmail() != null ? contact.getEmail() : "",
                contact.getAddress() != null ? contact.getAddress() : "",
                contact.getCategory() != null ? contact.getCategory() : "Uncategorized",
                dateFormat.format(contact.getCreatedDate())
            };
            tableModel.addRow(row);
        }
        
        updateStatistics();
        setStatus("Loaded " + contacts.size() + " contact(s)");
    }
    
    // Search functionality
    private void performSearch() {
        String keyword = searchField.getText().trim();
        tableModel.setRowCount(0);
        List<Contact> results = contactManager.searchContacts(keyword);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        
        for (Contact contact : results) {
            Object[] row = {
                contact.getName(),
                contact.getMobile(),
                contact.getEmail() != null ? contact.getEmail() : "",
                contact.getAddress() != null ? contact.getAddress() : "",
                contact.getCategory() != null ? contact.getCategory() : "Uncategorized",
                dateFormat.format(contact.getCreatedDate())
            };
            tableModel.addRow(row);
        }
        
        setStatus("Found " + results.size() + " contact(s)");
    }
    
    // Show add contact dialog
    private void showAddContactDialog() {
        JDialog dialog = new JDialog(this, "Add New Contact", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Form fields
        JTextField nameField = new JTextField(20);
        JTextField mobileField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextArea addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        
        String[] categories = {"Personal", "Work", "Family", "Friends", "Business", "Other"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        
        // Add components
        int row = 0;
        addFormField(formPanel, gbc, row++, "Name: *", nameField);
        addFormField(formPanel, gbc, row++, "Mobile: *", mobileField);
        addFormField(formPanel, gbc, row++, "Email:", emailField);
        addFormField(formPanel, gbc, row++, "Address:", addressScroll);
        addFormField(formPanel, gbc, row++, "Category:", categoryCombo);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveBtn = createStyledButton("Save", SUCCESS_COLOR);
        JButton cancelBtn = createStyledButton("Cancel", DANGER_COLOR);
        
        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String mobile = mobileField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressArea.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            
            if (name.isEmpty() || mobile.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "Name and Mobile are required fields!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isValidMobile(mobile)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please enter a valid mobile number (10-15 digits)!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!email.isEmpty() && !isValidEmail(email)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please enter a valid email address!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Contact contact = new Contact(name, mobile, email, address, category);
            
            if (contactManager.addContact(contact)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Contact added successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to add contact. Mobile number already exists!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    // Show edit contact dialog
    private void showEditContactDialog() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a contact to edit!", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String mobile = (String) tableModel.getValueAt(selectedRow, 1);
        Contact contact = findContactByMobile(mobile);
        
        if (contact == null) {
            JOptionPane.showMessageDialog(this, 
                "Contact not found!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Edit Contact", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Pre-populate fields
        JTextField nameField = new JTextField(contact.getName(), 20);
        JTextField mobileField = new JTextField(contact.getMobile(), 20);
        JTextField emailField = new JTextField(contact.getEmail() != null ? contact.getEmail() : "", 20);
        JTextArea addressArea = new JTextArea(contact.getAddress() != null ? contact.getAddress() : "", 3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        
        String[] categories = {"Personal", "Work", "Family", "Friends", "Business", "Other"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        if (contact.getCategory() != null) {
            categoryCombo.setSelectedItem(contact.getCategory());
        }
        
        // Add components
        int row = 0;
        addFormField(formPanel, gbc, row++, "Name: *", nameField);
        addFormField(formPanel, gbc, row++, "Mobile: *", mobileField);
        addFormField(formPanel, gbc, row++, "Email:", emailField);
        addFormField(formPanel, gbc, row++, "Address:", addressScroll);
        addFormField(formPanel, gbc, row++, "Category:", categoryCombo);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton updateBtn = createStyledButton("Update", SUCCESS_COLOR);
        JButton cancelBtn = createStyledButton("Cancel", DANGER_COLOR);
        
        final String originalId = contact.getId();
        
        updateBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String mobileNum = mobileField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressArea.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            
            if (name.isEmpty() || mobileNum.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "Name and Mobile are required fields!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isValidMobile(mobileNum)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please enter a valid mobile number (10-15 digits)!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!email.isEmpty() && !isValidEmail(email)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please enter a valid email address!", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Contact updatedContact = new Contact(name, mobileNum, email, address, category);
            
            if (contactManager.updateContact(originalId, updatedContact)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Contact updated successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to update contact. Mobile number already exists!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    // Delete selected contact
    private void deleteSelectedContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a contact to delete!", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String mobile = (String) tableModel.getValueAt(selectedRow, 1);
        String name = (String) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete contact: " + name + "?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            Contact contact = findContactByMobile(mobile);
            if (contact != null && contactManager.deleteContact(contact.getId())) {
                JOptionPane.showMessageDialog(this, 
                    "Contact deleted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to delete contact!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Show contact details
    private void showContactDetails() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String mobile = (String) tableModel.getValueAt(selectedRow, 1);
        Contact contact = findContactByMobile(mobile);
        
        if (contact == null) return;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        
        String details = String.format(
            "<html><body style='width: 300px; padding: 10px;'>" +
            "<h2 style='color: #2980b9;'>Contact Details</h2>" +
            "<hr>" +
            "<p><b>Name:</b> %s</p>" +
            "<p><b>Mobile:</b> %s</p>" +
            "<p><b>Email:</b> %s</p>" +
            "<p><b>Address:</b> %s</p>" +
            "<p><b>Category:</b> %s</p>" +
            "<hr>" +
            "<p><b>Created:</b> %s</p>" +
            "<p><b>Modified:</b> %s</p>" +
            "</body></html>",
            contact.getName(),
            contact.getMobile(),
            contact.getEmail() != null ? contact.getEmail() : "N/A",
            contact.getAddress() != null ? contact.getAddress() : "N/A",
            contact.getCategory() != null ? contact.getCategory() : "Uncategorized",
            dateFormat.format(contact.getCreatedDate()),
            dateFormat.format(contact.getModifiedDate())
        );
        
        JOptionPane.showMessageDialog(this, 
            details, 
            "Contact Information", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Helper method to add form fields
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(jLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(field, gbc);
    }
    
    // Find contact by mobile number
    private Contact findContactByMobile(String mobile) {
        List<Contact> contacts = contactManager.getAllContacts();
        for (Contact contact : contacts) {
            if (contact.getMobile().equals(mobile)) {
                return contact;
            }
        }
        return null;
    }
    
    // Validation methods
    private boolean isValidMobile(String mobile) {
        return mobile.matches("\\d{10,15}");
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    // Update statistics
    private void updateStatistics() {
        int total = contactManager.getTotalContacts();
        totalContactsLabel.setText("Total Contacts: " + total);
    }
    
    // Set status message
    private void setStatus(String message) {
        statusLabel.setText(message);
        javax.swing.Timer timer = new javax.swing.Timer(3000, e -> statusLabel.setText("Ready"));

        timer.setRepeats(false);
        timer.start();
    }
    
    // ==================== MAIN METHOD ====================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MobileContactManager app = new MobileContactManager();
            app.setVisible(true);
        });
    }
}