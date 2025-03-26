package org.mps.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Board to publish advertisements.
 */
public class AdvertisementBoard {

	public static final String BOARD_OWNER = "THE Company";
	public static final int MAX_BOARD_SIZE = 20;
	private final List<Advertisement> advertisementList;

	/**
	 * Constructs a board containing an initial advertisement published by the
	 * {@code BOARD_OWNER}.
	 */
	public AdvertisementBoard() {
		advertisementList = new ArrayList<>();
		Advertisement initialAdvertisement = new Advertisement(
				"Welcome",
				"This board is intended for your advertisements",
				BOARD_OWNER);
		advertisementList.add(initialAdvertisement);
	}

	/**
	 * Returns the number of advertisements published in this board.
	 * <p>
	 * The number of advertisements is bounded by {@code MAX_BOARD_SIZE}.
	 *
	 * @return the number of advertisements published in this board
	 */
	public int numberOfPublishedAdvertisements() {
		return advertisementList.size();
	}

	/**
	 * Publishes an advertisement in this board.
	 * <p>
	 * For an advertisement to be published, the advertiser must be registered in
	 * the database
	 * and must have sufficient funds in the payment gateway. The advertiser called
	 * <tt>"THE Company"</tt>
	 * is the owner of the advertisement board, so it can publish freely, with no
	 * constraints.
	 */
	public void publish(Advertisement advertisement,
			AdvertiserDatabase advertiserDatabase,
			PaymentGateway paymentGateway) throws AdvertisementBoardException {
		// if board is full, throw exception (ej 7)
		if (this.numberOfPublishedAdvertisements() >= MAX_BOARD_SIZE) {
			throw new AdvertisementBoardException("Board is full");
		}

		// if advertisement is not already in the board, add it (ej 6)
		if (!this.findByTitleAndAdvertiser(advertisement.title, advertisement.advertiser).isPresent()) {
			if (advertisement.advertiser.equals(BOARD_OWNER))
				advertisementList.add(advertisement);
			else {
				if (advertiserDatabase.advertiserIsRegistered(advertisement.advertiser) &&
						paymentGateway.advertiserHasFunds(advertisement.advertiser)) {
					advertisementList.add(advertisement);
					paymentGateway.chargeAdvertiser(advertisement.advertiser);
				}
			}
		}

	}

	// metodo creado para ejercicio 6
	private Optional<Advertisement> findByTitleAndAdvertiser(String title, String advertiser) {
		return advertisementList.stream().filter(ad -> ad.title.equals(title) && ad.advertiser.equals(advertiser))
				.findFirst();
	}

	/**
	 * Returns an {@code Optional} with an advertisement with the given title in
	 * this board,
	 * or an empty {@code Optional} if there is not such an advertisement in this
	 * board.
	 *
	 * @param title the title to search for
	 * @return an {@code Optional} with the appropriate advertisement, or and empty
	 *         {@code Optional}
	 */
	public Optional<Advertisement> findByTitle(String title) {
		return advertisementList.stream().filter(ad -> ad.title.equals(title)).findFirst();
	}

	/**
	 * Deletes all the advertisements with both the given title and advertiser name
	 * from this board.
	 *
	 * @param title          the title of the advertisements to be deleted from this
	 *                       board
	 * @param advertiserName the name of the advertiser of the advertisements to be
	 *                       deleted from this board
	 */
	public void deleteAdvertisement(String title, String advertiserName) {
		advertisementList.removeIf(ad -> ad.title.equals(title) && ad.advertiser.equals(advertiserName));
	}
}
