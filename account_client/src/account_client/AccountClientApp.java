package account_client;

import javax.swing.*;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AccountClientApp {

    private static JTextField accountNumberField;
    private static JTextField customerNameField;
    private static JTextField balanceField;
    private static JTextField amountField;
    private static JTextField accountIdField;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Mini Banking Client");
        frame.setSize(500, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Account ID:"));
        accountIdField = new JTextField(8);
        JButton searchBtn = new JButton("Search");
        topPanel.add(accountIdField);
        topPanel.add(searchBtn);

        JPanel centerPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        accountNumberField = new JTextField();
        customerNameField = new JTextField();
        balanceField = new JTextField();

        accountNumberField.setEditable(false);
        customerNameField.setEditable(false);
        balanceField.setEditable(false);

        centerPanel.add(new JLabel("Account Number:"));
        centerPanel.add(accountNumberField);
        centerPanel.add(new JLabel("Customer Name:"));
        centerPanel.add(customerNameField);
        centerPanel.add(new JLabel("Balance:"));
        centerPanel.add(balanceField);

        amountField = new JTextField();
        centerPanel.add(new JLabel("Amount:"));
        centerPanel.add(amountField);

        JPanel bottomPanel = new JPanel();
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        bottomPanel.add(depositBtn);
        bottomPanel.add(withdrawBtn);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        searchBtn.addActionListener(e -> fetchAccount());
        depositBtn.addActionListener(e -> performTransaction("deposit"));
        withdrawBtn.addActionListener(e -> performTransaction("withdraw"));

        frame.setVisible(true);
    }

    private static void fetchAccount() {
        try {
            String id = accountIdField.getText();
            String response = callAPI("GET", "/accounts/" + id);

            if (response != null) {
                accountNumberField.setText(extract(response, "accountNumber"));
                customerNameField.setText(extract(response, "customerName"));
                balanceField.setText(extract(response, "balance"));
            }

        } catch (Exception ex) {
            accountNumberField.setText("");
            customerNameField.setText("");
            balanceField.setText("");
            showError(ex.getMessage());
        }
    }

    private static void performTransaction(String type) {
        try {
            String id = accountIdField.getText();
            String amount = amountField.getText();

            callAPI("PUT", "/accounts/" + id + "/" + type + "?amount=" + amount);

            fetchAccount(); // refresh balance

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private static String callAPI(String method, String path) throws Exception {
        URL url = new URL("http://localhost:8080" + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);

        int status = conn.getResponseCode();

        Scanner scanner;
        if (status >= 200 && status < 300) {
            scanner = new Scanner(conn.getInputStream());
        } else {
            scanner = new Scanner(conn.getErrorStream());
        }

        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }

        scanner.close();
        conn.disconnect();

        if (status >= 400) {
            throw new RuntimeException(response.toString());
        }

        return response.toString();
    }

    // Very simple JSON extractor (no external library)
    private static String extract(String json, String key) {
        int start = json.indexOf("\"" + key + "\":");
        if (start == -1) return "";
        start = json.indexOf(":", start) + 1;
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return json.substring(start, end).replace("\"", "").trim();
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}