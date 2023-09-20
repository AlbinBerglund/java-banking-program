package jsonbankingapp;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
    private String username;   // The username of the account holder
    private String password;   // The password associated with the account
    private double balance;    // The balance in the account

    // No-argument constructor (required for Jackson)
    public Account() {
    }

    @JsonCreator
    public Account(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("balance") double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public String getUsername() {
        return username;  // Returns the username
    }

    public void setUsername(String username) {
        this.username = username;  // Sets the username
    }

    public String getPassword() {
        return password;  // Returns the password
    }

    public void setPassword(String password) {
        this.password = password;  // Sets the password
    }

    public double getBalance() {
        return balance;  // Returns the balance
    }

    public void setBalance(double balance) {
        this.balance = balance;  // Sets the balance
    }
}
