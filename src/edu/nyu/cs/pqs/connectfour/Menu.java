package edu.nyu.cs.pqs.connectfour;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import edu.nyu.cs.pqs.connectfour.ConnectFourModel.PlayerId;
import edu.nyu.cs.pqs.connectfour.ConnectFourModel.PlayerType;

/**
 * GUI launch menu of the Connect-Four game.
 * 
 * <p>Can set the number of rows and columns, rule of winning, 
 * player types (human and computer, for two players respectively), 
 * and which player to play first. Also checks for the validity of 
 * the inputs, including if the specified game parameters is 
 * winnable, and only launches the game if all parameters meet the
 * requirements. </p>
 * 
 * <p>You can lauch a new game without closing an existing one.</p>
 * 
 * <p>The game menu is a singleton. </p>
 * 
 * @author Chenyang Tang
 */
class Menu {
  private static final Menu INSTANCE = new Menu();
  
  /**
   * Get the instance of the launch menu singleton.
   * 
   * @return the instance of Menu.
   */
  public static Menu getInstance() {
    return INSTANCE;
  }
  
  private Menu() {
    final JFrame frame = new JFrame();
    final JPanel panel = new JPanel();
    final JPanel titlePanel = new JPanel();
    final JPanel rulePanel = new JPanel();
    final JPanel selectPanel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(titlePanel);
    panel.add(rulePanel);
    panel.add(selectPanel);
    
    final JLabel title = new JLabel("Connect Four");
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    title.setFont(new Font("cambria", Font.BOLD, 40));
    final JLabel subtitle = new JLabel(
              "                                       (or other number)");
    subtitle.setFont(new Font("serif", Font.ITALIC, 20));
    subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    final JLabel boardDimentionText = new JLabel("Game board dimention: ");
    boardDimentionText.setFont(new Font(null, Font.BOLD, 25));
    final JLabel rowsText = new JLabel("Number of rows: ");
    rowsText.setFont(new Font(null, Font.PLAIN, 25));
    final JTextField rows = new JTextField();
    rows.setText("6");
    rows.setFont(new Font(null, Font.PLAIN, 25));
    final JLabel colsText = new JLabel("Number of columns: ");
    colsText.setFont(new Font(null, Font.PLAIN, 25));
    final JTextField cols = new JTextField();
    cols.setText("7");
    cols.setFont(new Font(null, Font.PLAIN, 25));
    
    final JLabel winNumText = new JLabel("Connect how many to win? ");
    winNumText.setFont(new Font(null, Font.BOLD, 25));
    final JLabel connectText = new JLabel("Connect ");
    connectText.setFont(new Font(null, Font.PLAIN, 25));
    final JTextField winNum = new JTextField();
    winNum.setText("4");
    winNum.setFont(new Font(null, Font.PLAIN, 25));
    
    final JLabel player1Text = new JLabel("Player 1 is: ");
    player1Text.setFont(new Font(null, Font.PLAIN, 20));
    final JRadioButton player1Human = new JRadioButton("Human", true);
    player1Human.setFont(new Font(null, Font.PLAIN, 25));
    final JRadioButton player1Computer = new JRadioButton("Computer", false);
    player1Computer.setFont(new Font(null, Font.PLAIN, 25));
    final ButtonGroup player1Buttons = new ButtonGroup();
    player1Buttons.add(player1Human);
    player1Buttons.add(player1Computer);
    final JLabel player2Text = new JLabel("Player 2 is: ");
    player2Text.setFont(new Font(null, Font.PLAIN, 20));
    final JRadioButton player2Human = new JRadioButton("Human", false);
    player2Human.setFont(new Font(null, Font.PLAIN, 25));
    final JRadioButton player2Computer = new JRadioButton("Computer", true);
    player2Computer.setFont(new Font(null, Font.PLAIN, 25));
    final ButtonGroup player2Buttons = new ButtonGroup();
    player2Buttons.add(player2Human);
    player2Buttons.add(player2Computer);
    
    final JLabel firstPlayerText = new JLabel("Play first: ");
    firstPlayerText.setFont(new Font(null, Font.PLAIN, 20));
    final JRadioButton firstPlayer1 = new JRadioButton("Player 1", true);
    firstPlayer1.setFont(new Font(null, Font.PLAIN, 25));
    final JRadioButton firstPlayer2 = new JRadioButton("Player 2", false);
    firstPlayer2.setFont(new Font(null, Font.PLAIN, 25));
    final ButtonGroup firstPlayerButtons = new ButtonGroup();
    firstPlayerButtons.add(firstPlayer1);
    firstPlayerButtons.add(firstPlayer2);
      
    titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
    titlePanel.add(title);
    titlePanel.add(subtitle);
    
    GroupLayout layoutRulePanel = new GroupLayout(rulePanel);
    rulePanel.setLayout(layoutRulePanel);
    layoutRulePanel.setAutoCreateGaps(true);
    layoutRulePanel.setAutoCreateContainerGaps(true);
    layoutRulePanel.setHorizontalGroup(
        layoutRulePanel.createSequentialGroup()
            .addGroup(layoutRulePanel.createParallelGroup(
                                      GroupLayout.Alignment.LEADING)
                .addComponent(boardDimentionText)
                .addComponent(rowsText)
                .addComponent(colsText)
                .addComponent(winNumText)
                .addComponent(connectText))
            .addGroup(layoutRulePanel.createParallelGroup(
                                      GroupLayout.Alignment.LEADING)
                .addComponent(rows)
                .addComponent(cols)
                .addComponent(winNum))
    );
    layoutRulePanel.setVerticalGroup(
       layoutRulePanel.createSequentialGroup()
          .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
          .addComponent(boardDimentionText)
          .addPreferredGap(boardDimentionText, rowsText, 
                              LayoutStyle.ComponentPlacement.INDENT)
          .addGroup(layoutRulePanel.createParallelGroup(
                                      GroupLayout.Alignment.BASELINE)
               .addComponent(rowsText)
               .addComponent(rows))
          .addGroup(layoutRulePanel.createParallelGroup(
                                      GroupLayout.Alignment.BASELINE)
              .addComponent(colsText)
              .addComponent(cols))
          .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
          .addComponent(winNumText)
          .addPreferredGap(winNumText, connectText, 
                           LayoutStyle.ComponentPlacement.INDENT)
          .addGroup(layoutRulePanel.createParallelGroup(
                                      GroupLayout.Alignment.BASELINE)
              .addComponent(connectText)
              .addComponent(winNum))
    );
     
    GroupLayout layoutSelectPanel = new GroupLayout(selectPanel);
    selectPanel.setLayout(layoutSelectPanel);
    layoutSelectPanel.setAutoCreateGaps(true);
    layoutSelectPanel.setAutoCreateContainerGaps(true);
    layoutSelectPanel.setHorizontalGroup(
        layoutSelectPanel.createSequentialGroup()
            .addGroup(layoutSelectPanel.createParallelGroup(
                                      GroupLayout.Alignment.LEADING)
                .addComponent(player1Text)
                .addComponent(player2Text)
                .addComponent(firstPlayerText))
            .addGroup(layoutSelectPanel.createParallelGroup(
                                      GroupLayout.Alignment.LEADING)
                .addComponent(player1Human)
                .addComponent(player2Human)
                .addComponent(firstPlayer1))
            .addGroup(layoutSelectPanel.createParallelGroup(
                                      GroupLayout.Alignment.LEADING)
                .addComponent(player1Computer)
                .addComponent(player2Computer)
                .addComponent(firstPlayer2))
    );
    layoutSelectPanel.setVerticalGroup(
        layoutSelectPanel.createSequentialGroup()
            .addGroup(layoutSelectPanel.createParallelGroup(
                                      GroupLayout.Alignment.LEADING)
                .addComponent(player1Text)
                .addComponent(player1Human)
                .addComponent(player1Computer))
            .addGroup(layoutSelectPanel.createParallelGroup(
                                      GroupLayout.Alignment.LEADING)
                .addComponent(player2Text)
                .addComponent(player2Human)
                .addComponent(player2Computer))
            .addGroup(layoutSelectPanel.createParallelGroup(
                                      GroupLayout.Alignment.LEADING)
                .addComponent(firstPlayerText)
                .addComponent(firstPlayer1)
                .addComponent(firstPlayer2))
    );
    
    final JButton newGame = new JButton("Launch New Game");
    newGame.setFont(new Font("verdana", Font.BOLD | Font.ITALIC, 30));
    newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(newGame);
    
    newGame.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {    
        if(checkTexts(rows.getText(), cols.getText(), winNum.getText())) {
          int nRows = Integer.parseInt(rows.getText());
          int nCols = Integer.parseInt(cols.getText());
          int nWin = Integer.parseInt(winNum.getText());
          if (checkWinnable(nRows, nCols, nWin)) {
            PlayerType player1 = player1Human.isSelected() ? 
                                      PlayerType.HUMAN : PlayerType.COMPUTER;
            PlayerType player2 = player2Human.isSelected() ? 
                                      PlayerType.HUMAN : PlayerType.COMPUTER;
            PlayerId firstPlayer = firstPlayer1.isSelected() ?
                                      PlayerId.PLAYER1 : PlayerId.PLAYER2;
            // Create new game model.
            ConnectFourModel model = new ConnectFourModel.Builder(
                player1, player2).setRows(nRows)
                                 .setColumns(nCols)
                                 .setWinNum(nWin)
                                 .setFirstPlayer(firstPlayer)
                                 .build();
            // Add a GUI as a listener.
            new BoardDisplay(model);
            // Kick start the game.
            model.start();
            
          } else {
            JOptionPane.showMessageDialog(frame.getContentPane(), 
                "This game is impossible to win! Check your win condition.");
          }
        } else {
          JOptionPane.showMessageDialog(frame.getContentPane(), 
                                        "Inputs must be positive integer.");
        }
        
      }
    });
    
    frame.setTitle("Connect Four");
    frame.getContentPane().add(panel);
    frame.setLocationByPlatform(true);
    frame.setSize(400, 560);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
  
  private boolean checkWinnable(int rows, int cols, int winNum) {
    if (winNum > rows && winNum > cols) {
      return false;
    }
    return true;
  }
  
  private boolean checkTexts(String rows, String cols, String winNum) {
    // Check if input is positive integer.
    try {
      if(Integer.parseInt(rows) <= 0
         || Integer.parseInt(cols) <= 0
         || Integer.parseInt(winNum) <= 0) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
    // Check if input is positive.
    return true;
  }
}
