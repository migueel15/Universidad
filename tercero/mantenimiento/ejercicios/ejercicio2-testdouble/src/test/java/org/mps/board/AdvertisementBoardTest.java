package org.mps.board;

import java.awt.DisplayMode;
import java.awt.List;

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
	

}
