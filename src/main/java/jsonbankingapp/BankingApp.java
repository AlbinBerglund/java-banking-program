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
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BankingApp {

    private Map<String, Account> accounts;
    private JFrame frame;
    private JTextField usernameInput;

    private JPasswordField passwordInput;
    private JLabel balanceLabel;
    private JTextField transferAmountInput;
    private JTextField recipientInput;
    private Account loggedInAccount;

    //InputStream JsonFilex = getClass().getClassLoader().getResourceAsStream("accounts.json");
    //String result = IOUtils.toString(JsonFilex, StandardCharsets.UTF_8);
    private static final String JsonFile = "C:/Users/berglual/OneDrive - Arcada/Mathematical Programming 2023/Projects/Java/jsonbankingapp/src/main/resources/accounts.json";
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private void loadingAccounts() {
        // Load account data from JSON file
        File fileJson = new File(JsonFile);
        if (fileJson.exists()) {
            try {
                // Moves JSON data into a map of accounts
                accounts = objectMapper.readValue(fileJson, new TypeReference<Map<String, Account>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
                accounts = new HashMap<>();
                System.err.println("är inte en json fil eller fungerar inte: " + e.getMessage());
            }
        } else {
            accounts = new HashMap<>();
            System.err.println("json laddar inte, det finns inte en fil på : " + JsonFile);
        }
    }

    private void saveAccountsToFile() {
        // Save account data to JSON file
        try {
            objectMapper.writeValue(new File(JsonFile), accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createUI() {
        // Create the graphical user interface
        frame = new JFrame("best banking website");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 6));

        JLabel usernameText = new JLabel("Username:");
        usernameText.setHorizontalAlignment(JTextField.CENTER);
        usernameInput = new JTextField();
        usernameInput.setHorizontalAlignment(JTextField.CENTER);
        JLabel passwordText = new JLabel("Password:");
        passwordText.setHorizontalAlignment(JTextField.CENTER);
        passwordInput = new JPasswordField();
        passwordInput.setHorizontalAlignment(JTextField.CENTER);
        JButton loginButton = new JButton("Login");

        panel.add(usernameText);
        panel.add(usernameInput);
        panel.add(passwordText);
        panel.add(passwordInput);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private void showLoggedInUI() {
        // Display the UI efter the loggin UI
        frame.getContentPane().removeAll();
        frame.repaint();

        JPanel loggedInPanel = new JPanel();
        loggedInPanel.setLayout(new GridLayout(5, 2));

        JLabel balanceTextLabel = new JLabel("Balance:");
        balanceLabel = new JLabel(String.valueOf(loggedInAccount.getBalance()));
        JLabel transferAmountLabel = new JLabel("Transfer Amount:");
        transferAmountInput = new JTextField();
        JLabel recipientLabel = new JLabel("Target:");
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

            public void actionPerformed(ActionEvent e) {
                transfer();
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
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
            JOptionPane.showMessageDialog(frame, "Wrong username or password", "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println(username + " " + password + " === " + accounts);
        }
    }

    private void changePassword() {
        // Change user's password
        String newPassword = JOptionPane.showInputDialog(frame, "Enter new password:");
        if (newPassword != null && !newPassword.isEmpty()) {
            loggedInAccount.setPassword(newPassword);
            saveAccountsToFile();
            JOptionPane.showMessageDialog(frame, "Password changed, hurray", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void transfer() {
        // Perform a money transfer between accounts
        String recipient = recipientInput.getText();
        double transferAmount = Double.parseDouble(transferAmountInput.getText());

        // This if statement checks that the recipient is correct, transaction is not 0
        // and the sender has enough money on their account. Then the program writes
        // that into the accounts.json
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
        public BankingApp() {
        loadingAccounts();
        createUI();
    }
    //runs the code
    public static void main(String[] args) {
        new BankingApp();
    }
}
