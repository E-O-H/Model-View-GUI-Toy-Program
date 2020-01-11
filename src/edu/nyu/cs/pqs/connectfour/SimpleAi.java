package edu.nyu.cs.pqs.connectfour;

import java.util.Random;

import edu.nyu.cs.pqs.connectfour.ConnectFourModel.BoardState;
import edu.nyu.cs.pqs.connectfour.ConnectFourModel.ConnectFourAi;
import edu.nyu.cs.pqs.connectfour.ConnectFourModel.PlayerId;

/**
 * A very simple AI for the Connect-Four game.
 * 
 * <p>This is a singleton. Use getInstance() method to get the instance.</p>
 * 
 * <p>The AI can only do two things:<br>
 * 1) If there is a move that can result in a win, it will play it.<br>
 * 2) If there is a position that will cause the opponent to win, 
 *    it will try to block it.<br>
 * Otherwise it will just throw checkers mindlessly (randomly). </p>
 * 
 * <p>The AI is deliberately slowed by Thread.sleep() to give a better feel, 
 * so it should not be run on the same thread as the GUI to prevent lag. </p>
 * 
 * @author Chenyang Tang
 */
class SimpleAi implements ConnectFourAi {
  private static final SimpleAi INSTANCE = new SimpleAi();
  
  /**
   * Get the instance of the AI singleton.
   * 
   * @return the instance of the AI singleton.
   */
  public static ConnectFourAi getInstance() {
    return INSTANCE;
  }

  @Override
  public int decideMove(BoardState board, PlayerId player, int winNum) {
    try {
      Thread.sleep(150); // Simulate a slow thinking process.
                         // (Visually more fun.)
    } catch (InterruptedException ignore) {
      // Ignored
    }
    // Check winning move
    for (int col = 0; col < board.getColumns(); ++col) {
      int row = board.getTop(col);
      if (row != -1) {
        if(ConnectFourModel.checkWin(board, col, row, player, winNum)) {
          return col;
        }
      }
    }
    // Check opponent's winning move
    player = player.otherPlayer();
    for (int col = 0; col < board.getColumns(); ++col) {
      int row = board.getTop(col);
      if (row != -1) {
        if(ConnectFourModel.checkWin(board, col, row, player, winNum)) {
          return col;
        }
      }
    }
    // Randomly pick a move
    int ret;
    Random rand = new Random();
    while(true) {
      ret = rand.nextInt(board.getColumns());
      if (board.getTop(ret) != -1) {
        return ret;
      }
    }
  }

  private SimpleAi() {}
}
