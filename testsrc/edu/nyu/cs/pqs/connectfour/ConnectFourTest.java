package edu.nyu.cs.pqs.connectfour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.nyu.cs.pqs.connectfour.ConnectFourModel.PlayerId;
import edu.nyu.cs.pqs.connectfour.ConnectFourModel.PlayerType;

public class ConnectFourTest {

  ConnectFourModel gameComputerFirst;
  ConnectFourModel gameTwoHumans;
  
  @Before
  public void setUp() {
    // set up a new empty 10 * 18 board.
    gameComputerFirst = new ConnectFourModel.Builder(
        PlayerType.HUMAN, PlayerType.COMPUTER)
        .setRows(10).setColumns(18).setFirstPlayer(PlayerId.PLAYER2)
        .setWinNum(10).build();
    gameTwoHumans = new ConnectFourModel.Builder(
        PlayerType.HUMAN, PlayerType.HUMAN)
        .setRows(10).setColumns(18).setFirstPlayer(PlayerId.PLAYER2)
        .setWinNum(10).build();
  }
  
  /* This test involves multiple threads, and may fail on very rare occasion.
   * The test thread sleeps 1000ms to wait for the AI thread to 
   * make a move, but if the AI thread is preempted, the test may fail
   * (which is very unlikely because 1000ms is more than enough, and depending 
   * on the operation system's scheduling algorithm this may never happen on
   * some machine. But is still not guaranteed to succeed in principle).
   */
  @Test
  public void testComputerAutomaticPlay() {
    gameComputerFirst.start();
    // Wait for the AI thread to make the move.
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    // One of the slots on the first row should be occupied now.
    Boolean boardEmpty = true;
    for (int i = 0; i < gameComputerFirst.getBoardForTest().getColumns() ; ++i) {
      if (gameComputerFirst.getBoardForTest().get(i, 0) != null) {
        boardEmpty = false;
        break;
      }
    }
    assertFalse(boardEmpty);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBuilder_nullPlayers() {
    ConnectFourModel model = new ConnectFourModel.Builder(null, null).build();
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBuilder_negativeRows() {
    ConnectFourModel model = new ConnectFourModel.Builder(
        PlayerType.HUMAN, PlayerType.COMPUTER).setRows(-1).build();
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBuilder_zeroColumns() {
    ConnectFourModel model = new ConnectFourModel.Builder(
        PlayerType.COMPUTER, PlayerType.HUMAN).setColumns(0).build();
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBuilder_negativeWinCondition() {
    ConnectFourModel model = new ConnectFourModel.Builder(
        PlayerType.HUMAN, PlayerType.HUMAN).setWinNum(-99999).build();
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testBuilder_nullfirstPlayer() {
    ConnectFourModel model = new ConnectFourModel.Builder(
        PlayerType.COMPUTER, PlayerType.COMPUTER).setFirstPlayer(null).build();
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testAddListener_null() {
    gameComputerFirst.addListener(null);
  }
  
  @Test
  public void testGetTop_emptyBoard() {
    assertEquals(0, gameComputerFirst.getBoardForTest().getTop(3));
  }
  
  @Test
  public void testGetTop_halfFull() {
    gameComputerFirst.clicked(0);
    gameComputerFirst.clicked(0);
    gameComputerFirst.clicked(0);
    gameComputerFirst.clicked(0);
    gameComputerFirst.clicked(0);
    assertEquals(5, gameComputerFirst.getBoardForTest().getTop(0));
  }
  
  @Test
  public void testGetTop_full() {
    for (int i = 0; i < 10; ++i) {
      gameComputerFirst.clicked(5);
    }
    assertEquals(-1, gameComputerFirst.getBoardForTest().getTop(5));
  }
  
  @Test
  public void testGetRows() {
    assertEquals(10, gameComputerFirst.getBoardForTest().getRows());
  }
  
  @Test
  public void testGetColumns() {
    gameComputerFirst.clicked(2);
    gameComputerFirst.clicked(9);
    assertEquals(18, gameComputerFirst.getBoardForTest().getColumns());
  }
  
  @Test
  public void testGetPosition() {
    gameComputerFirst.clicked(2);
    gameComputerFirst.clicked(9);
    assertEquals(null, gameComputerFirst.getBoardForTest().get(0, 0));
    assertEquals(PlayerId.PLAYER2, gameComputerFirst.getBoardForTest().get(2, 0));
    assertEquals(PlayerId.PLAYER1, gameComputerFirst.getBoardForTest().get(9, 0));
  }
  
  @Test
  public void testWin() {
    for (int i = 0; i < 10; ++i) {
      gameComputerFirst.clicked(0);
      gameComputerFirst.clicked(1);
    }
    /* Player2 plays first, so there should be 10 checkers of player2 on 
     * column 0, and since player2 reached 10 checkers first, which is the 
     * winning condition of this game, the attempt by player1 to place his
     * 10th checker would fail. But if he could put a checker in the last
     * slot of column 1, that would be a winning condition for him, thus the
     * third assertion is true. */
    assertTrue(
        gameComputerFirst.checkWin(gameComputerFirst.getBoardForTest(), 0, 0, PlayerId.PLAYER2, 10));
    assertFalse(
        gameComputerFirst.checkWin(gameComputerFirst.getBoardForTest(), 1, 0, PlayerId.PLAYER1, 10));
    assertTrue(
        gameComputerFirst.checkWin(gameComputerFirst.getBoardForTest(), 1, 9, PlayerId.PLAYER1, 10));
  }
  
  @Test
  public void testAI_winningMove() {
    for (int i = 0; i < 9; ++i) {
      gameTwoHumans.clicked(i);
      gameTwoHumans.clicked(i);
    }
    assertEquals(9, SimpleAi.getInstance().decideMove(
        gameTwoHumans.getBoardForTest(), PlayerId.PLAYER2, 10));
  }
  
  @Test
  public void testAI_preventingOpponentWin() {
    for (int i = 0; i < 9; ++i) {
      gameTwoHumans.clicked(i);
      gameTwoHumans.clicked(i);
    }
    assertEquals(9, SimpleAi.getInstance().decideMove(
        gameTwoHumans.getBoardForTest(), PlayerId.PLAYER1, 10));
  }
}
