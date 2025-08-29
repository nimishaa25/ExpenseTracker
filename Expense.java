public class Expense {
    private int id;
    private String category;
    private double amount;
    private String date;
    private String note;

    public Expense(int id, String category, double amount, String date, String note) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    public int getId() { return id; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getNote() { return note; }
}
