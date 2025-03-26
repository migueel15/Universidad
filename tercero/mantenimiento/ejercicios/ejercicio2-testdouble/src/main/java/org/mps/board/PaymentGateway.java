package org.mps.board;

public interface PaymentGateway {
	boolean advertiserHasFunds(String advertiserName);

	void chargeAdvertiser(String advertiserName);
}
