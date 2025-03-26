package org.mps.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AdvertisementBoardTest
 */
public class AdvertisementBoardTest {

	@Test
	@DisplayName("Initial post")
	public void AdvertisementBoardInitialBoardExpectsOnePost() {
		int expected = 1;
		AdvertisementBoard board = new AdvertisementBoard();
		int actual = board.numberOfPublishedAdvertisements();
		assertEquals(expected, actual);
	}

	@Test
	@DisplayName("Publish THE Company post")
	public void publishTheCompanyPostExpectsInsertion() {
		AdvertisementBoard board = new AdvertisementBoard();
		Advertisement advertisement = new Advertisement("Title", "Text", AdvertisementBoard.BOARD_OWNER);
		AdvertiserDatabase advertiserDatabase = mock(AdvertiserDatabase.class);
		PaymentGateway paymentGateway = mock(PaymentGateway.class);
		board.publish(advertisement, advertiserDatabase, paymentGateway);
		int expected = 2;
		int actual = board.numberOfPublishedAdvertisements();
		verify(advertiserDatabase, never()).advertiserIsRegistered(anyString());
		verify(paymentGateway, never()).advertiserHasFunds(anyString());
		assertEquals(expected, actual);
	}

	@Test
	@DisplayName("Publish non-THE Company post without funds")
	public void publishNonTheCompanyPostWithoutFundsExpectsNoInsertion() {
		String advertiser = "Pepe Gotera Otilio";
		AdvertisementBoard board = new AdvertisementBoard();
		Advertisement advertisement = new Advertisement("Title", "Text", advertiser);
		int initialNumberOfAdvertisements = board.numberOfPublishedAdvertisements();

		AdvertiserDatabase advertiserDatabase = mock(AdvertiserDatabase.class);
		PaymentGateway paymentGateway = mock(PaymentGateway.class);
		when(advertiserDatabase.advertiserIsRegistered(advertiser)).thenReturn(true);
		when(paymentGateway.advertiserHasFunds(advertiser)).thenReturn(false);

		board.publish(advertisement, advertiserDatabase, paymentGateway);
		int actualNumberOfAdvertisements = board.numberOfPublishedAdvertisements();

		verify(advertiserDatabase).advertiserIsRegistered(advertiser);
		verify(paymentGateway).advertiserHasFunds(advertiser);
		assertEquals(initialNumberOfAdvertisements, actualNumberOfAdvertisements);
	}

	@Test
	@DisplayName("Publish non-THE Company post with funds")
	public void publishNonTheCompanyPostWithFundsExpectsInsertion() {
		String advertiser = "Robin Robot";
		AdvertisementBoard board = new AdvertisementBoard();
		Advertisement advertisement = new Advertisement("Title", "Text", advertiser);
		int initialNumberOfAdvertisements = board.numberOfPublishedAdvertisements();

		AdvertiserDatabase advertiserDatabase = mock(AdvertiserDatabase.class);
		PaymentGateway paymentGateway = mock(PaymentGateway.class);
		when(advertiserDatabase.advertiserIsRegistered(advertiser)).thenReturn(true);
		when(paymentGateway.advertiserHasFunds(advertiser)).thenReturn(true);

		board.publish(advertisement, advertiserDatabase, paymentGateway);
		int actualNumberOfAdvertisements = board.numberOfPublishedAdvertisements();

		verify(advertiserDatabase).advertiserIsRegistered(advertiser);
		verify(paymentGateway).advertiserHasFunds(advertiser);
		assertEquals(initialNumberOfAdvertisements + 1, actualNumberOfAdvertisements);
		verify(paymentGateway).chargeAdvertiser(advertiser);
	}

	@Test
	@DisplayName("Publish two The Company post, delete the first one and search for it")
	public void publishTwoTheCompanyPostsDeleteFirstOneAndSearchForItExpectsEmptyOptional() {
		AdvertisementBoard board = new AdvertisementBoard();
		String title1 = "Title1";
		String title2 = "Title2";
		Advertisement advertisement1 = new Advertisement(title1, "Text1", AdvertisementBoard.BOARD_OWNER);
		Advertisement advertisement2 = new Advertisement(title2, "Text2", AdvertisementBoard.BOARD_OWNER);
		AdvertiserDatabase advertiserDatabase = mock(AdvertiserDatabase.class);
		PaymentGateway paymentGateway = mock(PaymentGateway.class);

		board.publish(advertisement1, advertiserDatabase, paymentGateway);
		board.publish(advertisement2, advertiserDatabase, paymentGateway);
		board.deleteAdvertisement(title1, AdvertisementBoard.BOARD_OWNER);

		assertFalse(board.findByTitle(title1).isPresent());
	}

	@Test
	@DisplayName("Publish repeted post by non-THE Company dont insert it")
	public void publishRepetedPostByNonTheCompanyExpectsNoInsertion() {
		String advertiser = "Robin Robot";
		AdvertisementBoard board = new AdvertisementBoard();
		String title = "Title";
		Advertisement advertisement = new Advertisement(title, "Text", advertiser);
		int initialNumberOfAdvertisements = board.numberOfPublishedAdvertisements();

		AdvertiserDatabase advertiserDatabase = mock(AdvertiserDatabase.class);
		PaymentGateway paymentGateway = mock(PaymentGateway.class);
		when(advertiserDatabase.advertiserIsRegistered(advertiser)).thenReturn(true);
		when(paymentGateway.advertiserHasFunds(advertiser)).thenReturn(true);

		board.publish(advertisement, advertiserDatabase, paymentGateway);
		board.publish(advertisement, advertiserDatabase, paymentGateway);
		int actualNumberOfAdvertisements = board.numberOfPublishedAdvertisements();

		verify(advertiserDatabase).advertiserIsRegistered(advertiser);
		verify(paymentGateway).advertiserHasFunds(advertiser);
		assertEquals(initialNumberOfAdvertisements + 1, actualNumberOfAdvertisements);
		verify(paymentGateway).chargeAdvertiser(advertiser);
	}

	// En este caso no es necesario el uso de un spy ya que solo estamos comprobando
	// que se lanza una excepción al llegar al maximo del board. En este caso
	// estamos simulando completamente las interfaces para hacer el bypass y no es
	// necesario modificar unicamente algunos metodos. Se trata de una accion simple
	// que no requiere simular distintas funciones.
	@Test
	@DisplayName("Publish post on full board")
	public void publishPostOnFullBoardExpectsException() {
		AdvertisementBoard board = new AdvertisementBoard();
		String advertiser = "Tim O'Theo";

		AdvertiserDatabase advertiserDatabase = mock(AdvertiserDatabase.class);
		PaymentGateway paymentGateway = mock(PaymentGateway.class);
		when(advertiserDatabase.advertiserIsRegistered(advertiser)).thenReturn(true);
		when(paymentGateway.advertiserHasFunds(advertiser)).thenReturn(true);

		// fill the board
		for (int i = 1; i < AdvertisementBoard.MAX_BOARD_SIZE; i++) {
			Advertisement advertisement = new Advertisement("Title" + i, "Text" + i, advertiser);
			board.publish(advertisement, advertiserDatabase, paymentGateway);
		}

		// try to publish on a full board
		Advertisement advertisement = new Advertisement("Title", "Text", advertiser);
		assertThrows(AdvertisementBoardException.class,
				() -> board.publish(advertisement, advertiserDatabase, paymentGateway));
	}
}
