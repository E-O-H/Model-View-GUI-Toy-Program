package edu.nyu.cs.pqs.connectfour;

import edu.nyu.cs.pqs.connectfour.ConnectFourModel.PlayerId;

/**
 * A Listener for the Connect-Four game.
 * 
 * @author Chenyang Tang
 */
interface ConnectFourListener {
  
  /**
   * Update the board at position (col, row).
   * 
   * @param col The column position to update.
   * @param row The row postion to update.
   * @param player Which player issued the update.
   */
  void boardUpdate(int col, int row, PlayerId player);
  
  /**
   * Lock the board so that it no longer responds to clicks.
   * 
   * <p>This is to prevent a user click during computer move.
   * Allowing for the potential implementation of a very slow
   * thinking AI.</p>
   * 
   * <p>Another reason to lock the board is that the game has ended.</p>
   */
  void lockBoard();
  
  /**
   * Unlock the board so that it starts to responds to clicks.
   * 
   * <p>The board should only be unlocked during a human player turn.</p>
   */
  void unlockBoard();
  
  /**
   * Game has reached a draw.
   */
  void gameDraw();
  
  /**
   * One player has won the game.
   * 
   * @param player The winning player.
   */
  void gameWon(PlayerId player);
  
  /**
   * Reset the game.
   */
  void gameReset();
}
