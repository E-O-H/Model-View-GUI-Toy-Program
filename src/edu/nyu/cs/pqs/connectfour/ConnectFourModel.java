package edu.nyu.cs.pqs.connectfour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The model of the Connect-Four game.
 * 
 * Uses a builder class to instantiate. Refer to the documentation of
 * the Builder class for details.<br>
 * Example on usage of builder: <br>
 * <code>ConnectFourModel.builder(PlayerType.Human, PlayerType.COMPUTER)
 * .setRows(8).setFirstPlayer(PlayerId.PLAYER2).build()</code>
 * 
 * @author Chenyang Tang
 * @see edu.nyu.cs.pqs.connectfour.ConnectFourModel.Builder
 */
class ConnectFourModel {
  final int COLUMNS;
  final int ROWS;
  private final int WINNUM;
  private final PlayerId firstPlayer;
  private final BoardState board;
  private Player player1;
  private Player player2;
  private PlayerId currentPlayer;
  private final List<ConnectFourListener> listeners;

  /**
   * Builder Class for ConnectFourModel. 
   * 
   * <p>The constructor takes two required arguments, representing the type
   * of the two players respectively (PlayerType.HUMAN for human player, and
   * PlayerType.COMPUTER for computer AI. Number of columns, number of rows,
   * the player to play first, and number of checkers in a row for winning
   * can be set optionally, using setColumns(), setRows(), setFirstPlayer()
   * and setWinNum(). After setting all values, use build() to build and 
   * return the ConnectFourModel object. </p>
   *  
   * <p>The default settings are:<br>
   * Number of columns: 7<br>
   * Number of rows: 6<br>
   * Number of checkers in a row for winning: 4<br>
   * Player1 first.</p>
   * 
   * @author Chenyang Tang
   */
  public static class Builder {
    // Default values
    private int columns = 7;
    private int rows = 6;
    private int winNum = 4;
    private PlayerId firstPlayer = PlayerId.PLAYER1;
    
    private PlayerType player1;
    private PlayerType player2;
    
    /**
     * Construct a Builder object for ConnectFourModel.
     * 
     * @param player1 Type of the first player (HUMAN or COMPUTER).
     * @param player2 Type of the second player (HUMAN or COMPUTER).
     * @throws IllegalArgumentException when one of the player types is null.
     */
    public Builder(PlayerType player1, PlayerType player2) 
                                          throws IllegalArgumentException{
      if (player1 == null || player2 == null) {
        throw new IllegalArgumentException("player type cannot be null.");
      }
      this.player1 = player1;
      this.player2 = player2;
    }
    
    /**
     * Set the number of columns on the builder object.
     * 
     * @param columns Number of columns. Must be positive integer.
     * @return the builder object after modification.
     * @throws IllegalArgumentException if argument is not positive.
     */
    public Builder setColumns(int columns) throws IllegalArgumentException {
      if (columns < 1) {
        throw new IllegalArgumentException(
                                "Number of columns must be positive integer.");
      }
      this.columns = columns;
      return this;
    }
    
    /**
     * Set the number of rows on the builder object.
     * 
     * @param columns Number of rows. Must be positive integer.
     * @return the builder object after modification.
     * @throws IllegalArgumentException if argument is not positive.
     */
    public Builder setRows(int rows) throws IllegalArgumentException {
      if (rows < 1) {
        throw new IllegalArgumentException(
                                "Number of rows must be positive integer.");
      }
      this.rows = rows;
      return this;
    }
    
    /**
     * Set the number of checkers in a row for winning.
     * 
     * @param num Number of checkers in a row to win. Must be positive integer.
     * @return the builder object after modification.
     * @throws IllegalArgumentException if argument is not positive.
     */
    public Builder setWinNum(int num) throws IllegalArgumentException {
      if (num < 1) {
        throw new IllegalArgumentException(
                                "Winning number must be positive integer.");
      }
      this.winNum = num;
      return this;
    }
    
    /**
     * Set which player to play first.
     * 
     * @param player Player id of the player to play first.
     * @return the builder object after modification.
     * @throws IllegalArgumentException if argument is null.
     */
    public Builder setFirstPlayer(PlayerId player) 
                                        throws IllegalArgumentException {
      if (player == null) {
        throw new IllegalArgumentException("Invalid player Id.");
      }
      this.firstPlayer = player;
      return this;
    }
    
    /**
     * Build a new ConnectFourModel from the builder.
     * 
     * @return a new ConnectFourModel object.
     */
    public ConnectFourModel build() {
      return new ConnectFourModel(this);
    } 
  }
  
  private ConnectFourModel(Builder builder) throws IllegalArgumentException {
    COLUMNS = builder.columns;
    ROWS = builder.rows;
    WINNUM = builder.winNum;
    firstPlayer = builder.firstPlayer;
    currentPlayer = firstPlayer;
    board = new BoardState();
    listeners = new ArrayList<ConnectFourListener>();
    switch (builder.player1) {
      case HUMAN:
        player1 = new HumanPlayer();
        break;
      case COMPUTER:
        player1 = new ComputerPlayer();
        break;
      default:
        throw new IllegalArgumentException("Invalid playerId");
    }
    switch (builder.player2) {
      case HUMAN:
        player2 = new HumanPlayer();
        break;
      case COMPUTER:
        player2 = new ComputerPlayer();
        break;
      default:
        throw new IllegalArgumentException("Invalid playerId");
    }
  }
  
  /**
   * Add a listener.
   * 
   * @param listener The listener object to add.
   * @throws IllegalArgumentException If the listener is null.
   */
  public void addListener(ConnectFourListener listener) 
                                            throws IllegalArgumentException {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null.");
    }
    listeners.add(listener);
  }
  
  /**
   * Clicked on the board. Will place a new checker if the column is available.
   * 
   * @param col the column clicked.
   * @return true if the column is available and a new checker is placed;
   *         false if the column is full.
   */
  public synchronized void clicked(int col) {
    fireLockBoard();
    int pos = board.getTop(col);
    // Check if the top position of the column is available.
    if (pos == -1) {
      // Wait for another click.
      fireUnlockBoard();
    } else {
      board.set(col, pos, currentPlayer);
      fireBoardUpdate(col, pos, currentPlayer);
      if (!checkGameEnd(col, pos, currentPlayer)) {
        currentPlayer = currentPlayer.otherPlayer(); // Change player.
        switch (currentPlayer) {
          case PLAYER1:
            player1.act();
            break;
          case PLAYER2:
            player2.act();
            break;
          default:
            throw new NullPointerException("currentPlayer null.");
        }
      }
    }
  }
  
  /**
   * Starts the game.
   */
  public void start() {
    switch (currentPlayer) {
      case PLAYER1:
        player1.act();
        break;
      case PLAYER2:
        player2.act();
        break;
      default:
        // Impossible
    }
  }
  
  /**
   * Resets the game.
   */
  public void reset() {
    fireLockBoard();
    for (int x = 0; x < COLUMNS; ++x) {
      for (int y = 0; y < ROWS; ++y) {
        board.set(x, y, null);
      }
    }
    currentPlayer = firstPlayer;
    fireGameReset();
    start();
  }

  /**
   * Interface for a Connect-Four game AI.
   * 
   * <p>Tell it the state of the gameboard, which player he is to play,
   * and the rule to win, then it should return a column to put checker in.</p>
   * 
   * @author Chenyang Tang
   */
  public interface ConnectFourAi {
    /**
     * Decides which column to put checker in.
     * 
     * @param board The current game board state.
     * @param player The player that is playing.
     * @param winNum The winning rule. (Number of consecutive checkers).
     * @return The column to put new checker in.
     */
    int decideMove(BoardState board, PlayerId player, int winNum);
  }
  
  /**
   * Interface for a player object.
   * 
   * @author Chenyang Tang
   */
  interface Player {
    /**
     * Perform actions in the player's turn.
     */
    void act();
  }
  
  /**
   * A human player waits for a click on the game board GUI.
   * 
   * @author Chenyang Tang
   */
  class HumanPlayer implements Player {
    @Override
    public void act() {
      fireUnlockBoard();
      // Waits for a click on the GUI for further action.
    }
  }
  
  /**
   * A computer player makes a move using algorithm in a ConnectFourAi object.
   * 
   * @author Chenyang Tang
   */
  class ComputerPlayer implements Player {
    private ConnectFourAi ai = SimpleAi.getInstance();
    
    @Override
    public void act() {
      // Spawn a new thread for the computer to "think". 
      // (This is to prevent the potentially slow AI thinking process
      // from freezing the GUI, which could happen if it shares the 
      // same thread with the GUI.)
      new Thread() {
        @Override
        public void run() {
          // Simulate a click on the board.
          clicked(ai.decideMove(board, currentPlayer, WINNUM));
        }
      }.start();
    }
  }
  
  /**
   * Data structure of the game board state.
   * 
   * <p>Constructor takes no argument and returns a new empty board.</p>
   * 
   * @author Chenyang Tang
   */
  class BoardState {
    private final List<List<PlayerId> > board;

    BoardState() {
      // Initialize board.
      // PlayerId denotes which checkers on the board.
      // null denotes empty slots.
      board = new ArrayList<List<PlayerId> >(
                            Collections.<List<PlayerId> >nCopies(COLUMNS, null));
      for (int col = 0; col < COLUMNS; ++col) {
        board.set(col, new ArrayList<PlayerId>(
                              Collections.<PlayerId>nCopies(ROWS, null)));
      }
    }
    
    /**
     * Set or delete a checker.
     * 
     * @param col The number of column to be set on.
     * @param row The number of row to be set on.
     * @param player Which player's checker to set. Null to delete a checker.
     */
    void set(int col, int row, PlayerId player) {
      board.get(col).set(row, player);
    }
    
    /**
     * Get state of a position on the board.
     * 
     * @param col The number of column to be get.
     * @param row The number of row to be get.
     * @return The state of the slot, represented by the PlayerId of
     *         the checker, or null if the slot is empty.
     */
    PlayerId get(int col, int row) {
      return board.get(col).get(row);
    }
    
    /**
     * Get the position of the next available empty slot in a column.
     * Return -1 if the column is full.
     * 
     * @param col the column to check.
     * @return The position of the next empty slot.
     *         -1 if the column is full.
     */
    int getTop(int col) {
      for (int i = 0; i < ROWS; ++i) {
        if (board.get(col).get(i) == null) {
          return i;
        }
      }
      return -1;
    }
    
    /**
     * Get the number of rows in the board.
     * 
     * @return the number of rows.
     */
    int getRows() {
      return ROWS;
    }
    
    /**
     * Get the number of columns in the board.
     * 
     * @return the number of columns.
     */
    int getColumns() {
      return COLUMNS;
    }
    
    @Override
    public String toString() {
      StringBuffer ret = new StringBuffer();
      for (int row = getRows() - 1; row >= 0; --row) {
        for (int col = 0; col < getColumns(); ++col) {
          if (get(col, row) == null) {
            ret.append("0");
          } else {
            ret.append(get(col, row).toString());
          }
        }
        ret.append("\n");
      }
      return ret.toString();
    }
  }
  
  enum PlayerId {
    PLAYER1, PLAYER2;
    
    /**
     * return the ID of the other player
     * 
     * @return PlayerId of the other player
     */
    public PlayerId otherPlayer() {
      switch (this) {
        case PLAYER1:
          return PlayerId.PLAYER2;
        case PLAYER2:
          return PlayerId.PLAYER1;
        default:
          return null;
      }

    }
    
    @Override
    public String toString() {
      switch (this) {
        case PLAYER1:
          return "1";
        case PLAYER2:
          return "2";
        default:
          return null;
      }
    }
  }
  
  enum PlayerType {
    HUMAN, COMPUTER
  }
  
  enum Direction {
    UP, DOWN, LEFT, RIGHT, UPPERLEFT, UPPERRIGHT, LOWERLEFT, LOWERRIGHT
  }
  
  /**
   * Check if a player would win if he puts a checker at position (col, row).
   * 
   * @param board The state of the board.
   * @param col The column to put.
   * @param row The row to put.
   * @param player The player.
   * @param winNum The number of consecutive checkers in a row for winning.
   * @return true if the move would result in a win, false if not.
   */
  static boolean checkWin(BoardState board, int col, int row,
                                 PlayerId player, int winNum) {
    if (  numCheckers(board, col, row, player, Direction.UP)
            + numCheckers(board, col, row, player, Direction.DOWN) 
          >= winNum - 1
       || numCheckers(board, col, row, player, Direction.LEFT)
            + numCheckers(board, col, row, player, Direction.RIGHT) 
          >= winNum - 1
       || numCheckers(board, col, row, player, Direction.UPPERLEFT)
            + numCheckers(board, col, row, player, Direction.LOWERRIGHT) 
          >= winNum - 1
       || numCheckers(board, col, row, player, Direction.UPPERRIGHT)
            + numCheckers(board, col, row, player, Direction.LOWERLEFT) 
          >= winNum - 1  ) {
      return true;
    }
    return false;
  }
  
  /* Number of consecutive checkers of the player on direction dir 
   * to the position (col, row) */
  private static int numCheckers(BoardState board, int col, int row, 
                                    PlayerId player, Direction dir) {
    int ret = 0;
    switch (dir) {
      case UP:
        for (int y = row + 1; y < board.getRows(); ++y) {
          if (board.get(col, y) == player) {
            ++ret;
          } else {
            break;
          }
        }
        break;
      case DOWN:
        for (int y = row - 1; y >= 0; --y) {
          if (board.get(col, y) == player) {
            ++ret;
          } else {
            break;
          }
        }
        break;
      case LEFT:
        for (int x = col - 1; x >= 0; --x) {
          if (board.get(x, row) == player) {
            ++ret;
          } else {
            break;
          }
        }
        break;
      case RIGHT:
        for (int x = col + 1; x < board.getColumns(); ++x) {
          if (board.get(x, row) == player) {
            ++ret;
          } else {
            break;
          }
        }
        break;
      case UPPERLEFT:
        for (int x = col - 1, y = row + 1; 
                 x >= 0 && y < board.getRows(); --x, ++y) {
          if (board.get(x, y) == player) {
            ++ret;
          } else {
            break;
          }
        }
        break;
      case UPPERRIGHT:
        for (int x = col + 1, y = row + 1; 
                 x < board.getColumns() && y < board.getRows(); ++x, ++y) {
          if (board.get(x, y) == player) {
            ++ret;
          } else {
            break;
          }
        }
        break;
      case LOWERLEFT:
        for (int x = col - 1, y = row - 1; x >= 0 && y >= 0; --x, --y) {
          if (board.get(x, y) == player) {
            ++ret;
          } else {
            break;
          }
        }
        break;
      case LOWERRIGHT:
        for (int x = col + 1, y = row - 1; 
                 x < board.getColumns() && y >= 0; ++x, --y) {
          if (board.get(x, y) == player) {
            ++ret;
          } else {
            break;
          }
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid direction.");
    }
    return ret;
  }
  
  private boolean checkGameEnd(int col, int row, PlayerId player) {
    // Check if the game is won after player put checker at (col, row)
    if (checkWin(board, col, row, player, WINNUM)) {
      fireGameWon(player);
      return true;
    } else if (row == ROWS - 1) {
      // Check if the game is a draw
      for (int i = 0; i < COLUMNS; ++i) {
        if (board.get(i, ROWS - 1) == null) {
          return false;
        }
      }
      fireGameDraw();
      return true;
    }
    return false;
  }
  
  private void fireBoardUpdate(int col, int row, PlayerId player) {
    for (ConnectFourListener listener : listeners) {
      listener.boardUpdate(col, row, player);
    }
  }

  private void fireUnlockBoard() {
    for (ConnectFourListener listener : listeners) {
      listener.unlockBoard();
    }
  }

  private void fireLockBoard() {
    for (ConnectFourListener listener : listeners) {
      listener.lockBoard();
    }
  }
  
  private void fireGameDraw() {
    for (ConnectFourListener listener : listeners) {
      listener.gameDraw();
    }
  }

  private void fireGameWon(PlayerId player) {
    for (ConnectFourListener listener : listeners) {
      listener.gameWon(player);
    }
  }
  
  private void fireGameReset() {
    for (ConnectFourListener listener : listeners) {
      listener.gameReset();
    }
  }
  
  // *Only for unit test*
  BoardState getBoardForTest() {
    return board;
  }

  @Override
  public String toString() {
    return "num of Columns = " + COLUMNS 
        + "\nnum of rows = " + ROWS 
        + "\nwinning number = " + WINNUM 
        + "\nfirstPlayer is " + firstPlayer
        + "\nplayer1 is " + player1 + 
        "\nplayer2 is " + player2 
        + "\ncurrentPlayer is " + currentPlayer + 
        "\n*GAME BOARD STATE*\n"
        + board;
  }
}
