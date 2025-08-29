import java.sql.*;
import java.util.*;

public class Database {
    public Database() {
        // Create table if not exists (runs once)
        String sql = "CREATE TABLE IF NOT EXISTS expenses (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY," +
                     "category VARCHAR(100) NOT NULL," +
                     "amount DECIMAL(10,2) NOT NULL," +
                     "expense_date DATE NOT NULL," +
                     "note VARCHAR(255)" +
                     ")";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addExpense(String category, double amount, String date, String note) {
        String sql = "INSERT INTO expenses (category, amount, expense_date, note) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setBigDecimal(2, java.math.BigDecimal.valueOf(amount));
            ps.setDate(3, java.sql.Date.valueOf(date));// expects "YYYY-MM-DD"
            ps.setString(4, note);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Expense> getAllExpenses() {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT id, category, amount, expense_date, note FROM expenses ORDER BY expense_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Expense(
                    rs.getInt("id"),
                    rs.getString("category"),
                    rs.getDouble("amount"),
                    rs.getDate("expense_date").toString(),
                    rs.getString("note")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public double getMonthlyTotal(String month) { // month format "YYYY-MM"
        String sql = "SELECT SUM(amount) as total FROM expenses WHERE DATE_FORMAT(expense_date, '%Y-%m') = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, month);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public void deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
