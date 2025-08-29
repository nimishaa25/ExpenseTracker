import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ExpenseTrackerGUI extends JFrame {
    private Database db;
    private JTable table;
    private DefaultTableModel model;

    public ExpenseTrackerGUI() {
        db = new Database();

        setTitle("Expense Tracker");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new String[]{"ID", "Category", "Amount", "Date", "Note"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton addButton = new JButton("Add Expense");
        JButton refreshButton = new JButton("Refresh");
        JButton totalButton = new JButton("Monthly Total");
        JButton deleteButton = new JButton("Delete Selected");

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(addButton);
        topPanel.add(refreshButton);
        topPanel.add(totalButton);
        topPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        addButton.addActionListener(e -> addExpenseDialog());
        refreshButton.addActionListener(e -> loadExpenses());
        totalButton.addActionListener(e -> showMonthlyTotal());
        deleteButton.addActionListener(e -> deleteSelected());

        loadExpenses();
    }

    private void loadExpenses() {
        model.setRowCount(0);
        List<Expense> expenses = db.getAllExpenses();
        for (Expense ex : expenses) {
            model.addRow(new Object[]{ex.getId(), ex.getCategory(), ex.getAmount(), ex.getDate(), ex.getNote()});
        }
    }

    private void addExpenseDialog() {
        JTextField categoryField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField("YYYY-MM-DD");
        JTextField noteField = new JTextField();

        Object[] form = {
            "Category:", categoryField,
            "Amount:", amountField,
            "Date (YYYY-MM-DD):", dateField,
            "Note:", noteField
        };

        int option = JOptionPane.showConfirmDialog(this, form, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String category = categoryField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());
                String date = dateField.getText().trim();
                String note = noteField.getText().trim();
                db.addExpense(category, amount, date, note);
                loadExpenses();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Amount must be a number.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void showMonthlyTotal() {
        String month = JOptionPane.showInputDialog(this, "Enter month (YYYY-MM):");
        if (month != null && !month.trim().isEmpty()) {
            double total = db.getMonthlyTotal(month.trim());
            JOptionPane.showMessageDialog(this, "Total for " + month + " = " + total);
        }
    }

    private void deleteSelected() {
        int sel = table.getSelectedRow();
        if (sel == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }
        int id = (int) model.getValueAt(sel, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete record ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            db.deleteExpense(id);
            loadExpenses();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExpenseTrackerGUI().setVisible(true);
        });
    }
}
