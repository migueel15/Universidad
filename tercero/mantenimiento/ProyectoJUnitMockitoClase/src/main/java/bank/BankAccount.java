package bank;

public class BankAccount {
    private int balance = 0;
    public BankAccount(int startingBalance) {
        this.balance = startingBalance;
    }
    public boolean withdraw(int amount) {
        if(balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
    public int deposit(int amount) {
        balance += amount;
        return balance;
    }
    public int getBalance() {
        return balance;
    }
}
