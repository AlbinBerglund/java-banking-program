package jsonbankingapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BankingApp {
    private static final String JSON_FILE_PATH = "C:/Users/albin/OneDrive - Arcada/Mathematical Programming 2023/Projects/Java/jsonbankingapp/src/main/resources/accounts.json";
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private Map<String, Account> accounts;
    private JFrame frame;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JLabel balanceLabel;
    private JTextField transferAmountInput;
    private JTextField recipientInput;
    private Account loggedInAccount;

    public BankingApp() {
        loadingAccounts();
        createUI();
    }

    private void loadingAccounts() {
        // Load account data from JSON file
        File file = new File(JSON_FILE_PATH);
        if (file.exists()) {
            try {
                // Deserialize JSON data into a map of accounts
                accounts = objectMapper.readValue(file, new TypeReference<Map<String, Account>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
                accounts = new HashMap<>();
                System.err.println("stop stop stop stop stop: " + e.getMessage());
            }
        } else {
            accounts = new HashMap<>();
            System.err.println("please load: " + JSON_FILE_PATH);
        }
    }

    private void saveAccountsToFile() {
        // Save account data to JSON file
        try {
            objectMapper.writeValue(new File(JSON_FILE_PATH), accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createUI() {
        // Create the graphical user interface
        frame = new JFrame("best banking website");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameInput = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordInput = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(usernameLabel);
        panel.add(usernameInput);
        panel.add(passwordLabel);
        panel.add(passwordInput);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void showLoggedInUI() {
        // Display the UI for logged-in users
        frame.getContentPane().removeAll();
        frame.repaint();

        JPanel loggedInPanel = new JPanel();
        loggedInPanel.setLayout(new GridLayout(4, 2));

        JLabel balanceTextLabel = new JLabel("Balance:");
        balanceLabel = new JLabel(String.valueOf(loggedInAccount.getBalance()));
        JLabel transferAmountLabel = new JLabel("Transfer Amount:");
        transferAmountInput = new JTextField();
        JLabel recipientLabel = new JLabel("Recipient:");
        recipientInput = new JTextField();
        JButton transferButton = new JButton("Transfer");
        JButton changePasswordButton = new JButton("Change Password");
        JButton logoutButton = new JButton("Logout");

        loggedInPanel.add(balanceTextLabel);
        loggedInPanel.add(balanceLabel);
        loggedInPanel.add(transferAmountLabel);
        loggedInPanel.add(transferAmountInput);
        loggedInPanel.add(recipientLabel);
        loggedInPanel.add(recipientInput);
        loggedInPanel.add(transferButton);
        loggedInPanel.add(changePasswordButton);
        loggedInPanel.add(logoutButton);

        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transfer();
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        frame.add(loggedInPanel);
        frame.setVisible(true);
    }

    private void login() {
        // Perform user login
        String username = usernameInput.getText();
        String password = new String(passwordInput.getPassword());

        if (accounts.containsKey(username) && accounts.get(username).getPassword().equals(password)) {
            loggedInAccount = accounts.get(username);
            showLoggedInUI();
        } else {
            JOptionPane.showMessageDialog(frame, "Wrong username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(username + " " + password + " === " + accounts );
        }
    }

    private void changePassword() {
        // Change user's password
        String newPassword = JOptionPane.showInputDialog(frame, "Enter new password:");
        if (newPassword != null && !newPassword.isEmpty()) {
            loggedInAccount.setPassword(newPassword);
            saveAccountsToFile();
            JOptionPane.showMessageDialog(frame, "Password changed, hurray", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void transfer() {
        // Perform a money transfer between accounts
        String recipient = recipientInput.getText();
        double transferAmount = Double.parseDouble(transferAmountInput.getText());

        if (accounts.containsKey(recipient) && transferAmount > 0 && loggedInAccount.getBalance() >= transferAmount) {
            Account recipientAccount = accounts.get(recipient);
            loggedInAccount.setBalance(loggedInAccount.getBalance() - transferAmount);
            recipientAccount.setBalance(recipientAccount.getBalance() + transferAmount);
            saveAccountsToFile();
            balanceLabel.setText(String.valueOf(loggedInAccount.getBalance()));
            JOptionPane.showMessageDialog(frame, "Transfer worked", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid transfer", "Transfer Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        // Perform user logout
        loggedInAccount = null;
        createUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BankingApp();
            }
        });
    }
}
