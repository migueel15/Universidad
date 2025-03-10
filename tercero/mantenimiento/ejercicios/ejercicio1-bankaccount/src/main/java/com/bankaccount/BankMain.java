package com.bankaccount;

public class BankMain {

	public static void main(String args[]) {
		BankAccount bank = new BankAccount(10);
		double total_amount = 10000;
		double interes = 0.001;
		int months = 2;

		System.out.printf("Load payment of amount %f, with interes %f and in %d months is: %f\n", total_amount,
				interes, months, bank.payment(total_amount, interes, months));

		System.out.printf("Load pending payment of amount %f, with interes %f, %d months, in month %d is: %f\n",
				total_amount,
				interes, months, 2, bank.pending(total_amount, interes, months, 2));
	}

}
