package edu.nyu.cs.pqs.connectfour;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import edu.nyu.cs.pqs.connectfour.ConnectFourModel.PlayerId;

/**
 * A simple GUI of the Connect-Four game.
 * 
 * @author Chenyang Tang
 * @see edu.nyu.cs.pqs.connectfour.ConnectFourModel
 */
public class BoardDisplay implements ConnectFourListener {
  private final ConnectFourModel model;
  private final List<List<JLabel> > board;
  private final ImageIcon checker1;
  private final ImageIcon checker2;
  private final JFrame frame;
  private final JPanel panel;
  private final JPanel leftPanel;
  private final JTextArea textArea;
  private final List<JButton> buttons;
  private boolean isLocked;
  
  public BoardDisplay(ConnectFourModel model) {
    this.model = model;
    model.addListener(this);
    isLocked = true;
    checker1 = createImageIcon("/checkers/b.png", "black checker");
    checker2 = createImageIcon("/checkers/w.png", "white checker");
    board = new ArrayList<List<JLabel> >(
                         Collections.<List<JLabel> >nCopies(model.COLUMNS, null));
    for (int col = 0; col < model.COLUMNS; ++col) {
      board.set(col, new ArrayList<JLabel>(
                           Collections.<JLabel>nCopies(model.ROWS, null)));
      for (int row = 0; row < model.ROWS; ++row) {
        board.get(col).set(row, new JLabel());
      }
    }
    buttons = new ArrayList<JButton>(Collections.<JButton>nCopies(model.COLUMNS, null));
    frame = new JFrame();
    panel = new JPanel();
    leftPanel = new JPanel();
    textArea = new JTextArea();
    initialize();
  }

  private void initialize() {
    panel.setLayout(new BorderLayout());
    leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    leftPanel.setLayout(new GridLayout(model.ROWS + 1, model.COLUMNS));
    leftPanel.setBackground(Color.GRAY);
    textArea.setPreferredSize(
        new Dimension(220, (checker1.getIconHeight() + 5) * (model.ROWS + 1)));
    textArea.setLineWrap(true);
    textArea.setEditable(false);
    textArea.setBackground(Color.LIGHT_GRAY);
    
    for (int row = model.ROWS - 1; row >= 0; --row) {
      for (int col = 0; col < model.COLUMNS; ++col) {
        getLabel(col, row).setHorizontalAlignment(JLabel.CENTER);
        getLabel(col, row).setVisible(false);
        leftPanel.add(getLabel(col, row));
      }
    }
    for (int i = 0; i < model.COLUMNS; ++i) {
      buttons.set(i, new JButton(Integer.toString(i + 1)));
      //buttons.get(i)
      buttons.get(i).setFont(new Font("cambria", Font.BOLD, 40));
      buttons.get(i).setFocusable(false);
      buttons.get(i).setToolTipText("Put checker in column " + (i + 1));
      leftPanel.add(buttons.get(i));
      final int col = i;
      buttons.get(i).addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          if (isLocked == false) {
            model.clicked(col);
          }
        }
      });
    }
    
    panel.add(leftPanel, BorderLayout.CENTER);
    panel.add(new JScrollPane(textArea), BorderLayout.EAST);
    frame.getContentPane().add(panel);
    frame.setLocationByPlatform(true);
    frame.setSize((checker1.getIconWidth() + 5) * model.COLUMNS + 220, 
                  (checker1.getIconHeight() + 5) * (model.ROWS + 1));
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  @Override
  public void lockBoard() {
    isLocked = true;
  }

  @Override
  public void unlockBoard() {
    isLocked = false;
  }

  @Override
  public void gameDraw() {
    int option = JOptionPane.showConfirmDialog(
            frame.getContentPane(),
            "Draw game! Do you want to play again?\n"
                + "(Press yes to play again, "
                + "Press no to close the game, "
                + "Press cancel to return to the game board.)", 
            "Draw game",
            JOptionPane.YES_NO_CANCEL_OPTION);
    if(option == JOptionPane.YES_OPTION) {
      model.reset();
      gameReset();
    } else if (option == JOptionPane.NO_OPTION) {
      frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
  }

  @Override
  public void gameWon(PlayerId player) {
    int option = JOptionPane.showConfirmDialog(
            frame.getContentPane(),
            player.toString() + " win! Do you want to play again?\n"
            + "(Press yes to play again, "
            + "Press no to close the game, "
            + "Press cancel to return to the game board.)", 
            "We have a winner!",
            JOptionPane.YES_NO_CANCEL_OPTION);
    if(option == JOptionPane.YES_OPTION) {
      model.reset();
      gameReset();
    } else if (option == JOptionPane.NO_OPTION) {
      frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
  }

  @Override
  public void boardUpdate(int col, int row, PlayerId player) {
    putNewChecker(col, row, player);
    textArea.append(
        "  Player " + player.toString() 
        + "     at     row " + row + " , column " + col + "\n");
  }

  @Override
  public void gameReset() {
    for (int row = model.ROWS - 1; row >= 0; --row) {
      for (int col = 0; col < model.COLUMNS; ++col) {
        getLabel(col, row).setIcon(null);
        getLabel(col, row).setVisible(false);
      }
    }
    textArea.setText("");
  }
  
  private JLabel getLabel(int col, int row) {
    return board.get(col).get(row);
  }
  
  private void putNewChecker(int col, int row, PlayerId player) {
    switch (player) {
      case PLAYER1:
        getLabel(col, row).setIcon(checker1);
        break;
      case PLAYER2:
        getLabel(col, row).setIcon(checker2);
        break;
      default:
        throw new IllegalArgumentException("invalid player.");
    }
    appearAnimation(getLabel(col, row), col, row);
  }
  
  private void appearAnimation(JLabel label, int col, int row) {
    // Implement more complex animation here.
    // For now the checker just simply appears.
    label.setVisible(true);
  }
  
  /* Copied from Oracle Java tutorial.
   * https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
   */
  protected ImageIcon createImageIcon(String path, String description) {
      java.net.URL imgURL = getClass().getResource(path);
      if (imgURL != null) {
        return new ImageIcon(imgURL, description);
      } else {
        System.err.println("Couldn't find file: " + path);
        return null;
      }
  }
}
