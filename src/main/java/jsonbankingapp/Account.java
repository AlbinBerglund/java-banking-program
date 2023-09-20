package jsonbankingapp;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
    private String username;
    private String password;
    private double balance;

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
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
