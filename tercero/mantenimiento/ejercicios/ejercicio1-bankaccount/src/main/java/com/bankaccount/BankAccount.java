package com.bankaccount;

public class BankAccount {
	private int balance = 0;

	public BankAccount(int startingBalance) {
		this.balance = startingBalance;
	}

	public boolean withdraw(int amount) {
		if (balance >= amount) {
			balance -= amount;
			return true;
		}
		return false;
	}

	public int deposit(int amount) {
		if (amount < 0)
			throw new IllegalArgumentException("Amount cannot be negative");
		balance += amount;
		return balance;
	}

	public int getBalance() {
		return balance;
	}

	// ERROR: En caso de tener 0 pagos no controlado: Expected: 0.0
	// ERROR: En caso de tener 0 intereses no controlado: Expected: total_amount /
	// npayments

	// Calculate the payment per month for a loan
	public double payment(double total_amount, double interest, int npayments) {
		if (npayments <= 0) {
			return 0;
		}

		if (interest == 0) {
			return total_amount / npayments;
		}

		return total_amount * (interest * Math.pow((1 + interest), npayments) /
				(Math.pow((1 + interest), npayments) - 1));
	}

	// Calculate the pending amount for a loan in a month
	// ERROR: En caso de ingresar numero de mes negativo no controlado: Expected:
	// amount
	public double pending(double amount, double inte, int npayments, int month) {
		double res;
		if (month <= 0) {
			res = amount;
		} else {
			double ant = pending(amount, inte, npayments, month - 1);
			res = ant - (payment(amount, inte, npayments) - inte * ant);
		}
		return res;
	}
}
