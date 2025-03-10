package com.bankaccount;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankAccountTest {

	private BankAccount bankAccount;

	@BeforeEach
	public void setUp() {
		bankAccount = new BankAccount(100);
	}

	@Test
	public void withdrawMoreThanBalanceExpectFalse() {
		int balance = bankAccount.getBalance();
		boolean result = bankAccount.withdraw(balance + 1);
		assertFalse(result);
	}

	@Test
	public void withdrawLessThanBalanceExpectTrue() {
		int balance = bankAccount.getBalance();
		boolean result = bankAccount.withdraw(balance - 1);
		assertTrue(result);
	}

	@Test
	public void depositNegativeAmountExpectException() {
		assertThrows(IllegalArgumentException.class, () -> bankAccount.deposit(-1));
	}

	@Test
	public void depositPositiveAmountExpectBalance() {
		int amount = 50;
		int balance = bankAccount.getBalance();
		bankAccount.deposit(amount);
		int newBalance = bankAccount.getBalance();
		assertEquals(balance + amount, newBalance);
	}

	@Test
	public void depositZeroAmountExpectSameBalance() {
		int amount = 0;
		int balance = bankAccount.getBalance();
		int newBalance = bankAccount.deposit(amount);
		assertEquals(balance, newBalance);
	}

	@Test

	public void getBalanceExpectBalance() {
		int initalBalance = 100;
		bankAccount = new BankAccount(initalBalance);
		int balance = bankAccount.getBalance();
		assertEquals(initalBalance, balance);
	}

	@Test
	public void paymentWithZeroNumberOfPaymentsExpectZero() {
		double total_amount = 500.0;
		double interest = 0.1;
		int nPayments = 0;

		double expected = 0.0;
		double result = bankAccount.payment(total_amount, interest, nPayments);

		assertEquals(expected, result, 0.1);
	}

	@Test
	public void paymentWithNoInterestMustBeTotalAmount() {
		double total_amount = 500.0;
		double interest = 0.0;
		int nPayments = 14;

		double expected = total_amount / nPayments;
		double result = bankAccount.payment(total_amount, interest, nPayments);

		assertEquals(expected, result, 0.1);
	}

	@Test
	public void paymentNormalCase() {
		double total_amount = 500.0;
		double interest = 0.01;
		int nPayments = 12;

		double expected = 44.42;
		double result = bankAccount.payment(total_amount, interest, nPayments);

		assertEquals(expected, result, 0.1);
	}

	@Test
	public void pendingWithZeroMonthExpectTotalAmount() {
		double total_amount = 500.0;
		double interest = 0.01;
		int nPayments = 14;
		int month = 0;

		double expected = total_amount;
		double result = bankAccount.pending(total_amount, interest, nPayments, month);

		assertEquals(expected, result, 0.1);
	}

	@Test
	public void pendingNormalCase() {
		double total_amount = 500.0;
		double interest = 0.01;
		int nPayments = 12;
		int month = 2;

		double expected = 420.75;
		double result = bankAccount.pending(total_amount, interest, nPayments, month);

		assertEquals(expected, result, 0.1);
	}

	@Test
	public void pendingLastMonthPaymentExpectZero() {
		double total_amount = 500.0;
		double interest = 0.01;
		int nPayments = 12;
		int month = 12;

		double expected = 0.0;
		double result = bankAccount.pending(total_amount, interest, nPayments, month);

		assertEquals(expected, result, 0.1);
	}

	@Test
	public void pendingNegativeMonthExpect() {
		double total_amount = 500.0;
		double interest = 0.01;
		int nPayments = 12;
		int month = -1;

		double expected = total_amount;
		double result = bankAccount.pending(total_amount, interest, nPayments, month);

		assertEquals(expected, result, 0.1);
	}

}
