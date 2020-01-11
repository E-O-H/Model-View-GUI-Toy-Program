package edu.nyu.cs.pqs.connectfour;

/**
 * Starting class of the Connect-Four game.
 * 
 * <p>Brings up a menu to set game parameters. See related classes for details.
 * </p>
 * 
 * @author Chenyang Tang
 * @see edu.nyu.cs.pqs.connectfour.ConnectFourModel
 * @see edu.nyu.cs.pqs.connectfour.Menu
 */
public class ConnectFourDriver {
  /**
   * Starting point.
   * 
   * @param args Not useful.
   */
  public static void main(String[] args) {
    Menu.getInstance();
  }

}
