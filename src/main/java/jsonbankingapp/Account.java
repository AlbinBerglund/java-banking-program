package jsonbankingapp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {

    private String password; // The password associated with the account
    private double balance; // The balance in the account

    // No-argument constructor, required for Jackson
    public Account() {
    }

    public Account(
            @JsonProperty("password") String password,
            @JsonProperty("balance") double balance) {
        this.password = password;
        this.balance = balance;
    }

    public String getPassword() {
        return password; // Returns the password
    }

    public void setPassword(String password) {
        this.password = password; // Sets the password
    }

    public double getBalance() {
        return balance; // Returns the balance
    }

    public void setBalance(double balance) {
        this.balance = balance; // Sets the balance
    }
}
