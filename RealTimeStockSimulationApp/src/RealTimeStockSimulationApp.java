import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RealTimeStockSimulationApp {
    private JFrame frame;
    private JTable stockTable;
    private DefaultTableModel tableModel;
    private JLabel balanceLabel;
    private double userBalance = 10000.0;
    private String[] popularStocks = { "ADANIGREEN", "RELIANCE", "LT", "SBIN", "INFY" };
    private Random random = new Random();

    public RealTimeStockSimulationApp() {
        frame = new JFrame("Real-Time Stock Buy-Sell Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel(new Object[] { "Stock Symbol", "Price", "Quantity" }, 0);
        stockTable = new JTable(tableModel);

        JButton buyButton = new JButton("Buy");
        JButton sellButton = new JButton("Sell");
        JButton addFundsButton = new JButton("Add Funds");
        JButton withdrawFundsButton = new JButton("Withdraw Funds");
        balanceLabel = new JLabel("Balance: $" + userBalance);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Popular Stocks: " + String.join(", ", popularStocks)));
        inputPanel.add(buyButton);
        inputPanel.add(sellButton);
        inputPanel.add(addFundsButton);
        inputPanel.add(withdrawFundsButton);
        inputPanel.add(balanceLabel);

        buyButton.addActionListener(e -> buyStock());
        sellButton.addActionListener(e -> sellStock());
        addFundsButton.addActionListener(e -> addFunds());
        withdrawFundsButton.addActionListener(e -> withdrawFunds());

        frame.add(new JScrollPane(stockTable), BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::updateStockPrices, 0, 1, TimeUnit.SECONDS);
    }

    private Connection createDatabaseConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/stock";
        String username = "root";
        String password = "yourpassword"; // Replace with your MySQL password
        return DriverManager.getConnection(jdbcURL, username, password);
    }

    private void buyStock() {
        try (Connection connection = createDatabaseConnection()) {
            String symbol = popularStocks[random.nextInt(popularStocks.length)];
            int quantity = random.nextInt(10) + 1;
            double stockPrice = random.nextDouble() * 1000;

            if (userBalance >= stockPrice * quantity) {
                String insertSQL = "INSERT INTO stocks (symbol, price, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                    preparedStatement.setString(1, symbol);
                    preparedStatement.setDouble(2, stockPrice);
                    preparedStatement.setInt(3, quantity);
                    preparedStatement.executeUpdate();
                }

                tableModel.addRow(new Object[] { symbol, stockPrice, quantity });
                userBalance -= stockPrice * quantity;
                balanceLabel.setText("Balance: $" + userBalance);
            } else {
                JOptionPane.showMessageDialog(frame, "Insufficient balance to buy!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error during buy operation.");
        }
    }

    private void sellStock() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow >= 0) {
            String symbol = (String) tableModel.getValueAt(selectedRow, 0);
            double stockPrice = (double) tableModel.getValueAt(selectedRow, 1);
            int quantity = (int) tableModel.getValueAt(selectedRow, 2);

            try (Connection connection = createDatabaseConnection()) {
                String deleteSQL = "DELETE FROM stocks WHERE symbol = ? AND price = ? AND quantity = ? LIMIT 1";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                    preparedStatement.setString(1, symbol);
                    preparedStatement.setDouble(2, stockPrice);
                    preparedStatement.setInt(3, quantity);
                    preparedStatement.executeUpdate();
                }
                tableModel.removeRow(selectedRow);
                userBalance += stockPrice * quantity;
                balanceLabel.setText("Balance: $" + userBalance);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Database error during sell operation.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a stock to sell.");
        }
    }

    private void addFunds() {
        String input = JOptionPane.showInputDialog("Enter amount to add:");
        try {
            double amount = Double.parseDouble(input);
            if (amount > 0) {
                userBalance += amount;
                balanceLabel.setText("Balance: $" + userBalance);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid amount.");
        }
    }

    private void withdrawFunds() {
        String input = JOptionPane.showInputDialog("Enter amount to withdraw:");
        try {
            double amount = Double.parseDouble(input);
            if (amount > 0 && amount <= userBalance) {
                userBalance -= amount;
                balanceLabel.setText("Balance: $" + userBalance);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid or insufficient balance.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid amount.");
        }
    }

    private void updateStockPrices() {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            double oldPrice = (double) tableModel.getValueAt(row, 1);
            double newPrice = oldPrice + (random.nextDouble() * 20 - 10);
            tableModel.setValueAt(newPrice, row, 1);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RealTimeStockSimulationApp::new);
    }
}
