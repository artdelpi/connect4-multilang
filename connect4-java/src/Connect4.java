import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Connect4 {
    JFrame frame = new JFrame("Connect 4");
    JButton restartButton = new JButton("RESTART");
    JPanel panel = new JPanel();
    JPanel gameStatusPanel = new JPanel();
    
    String currentPlayer = "YELLOW";
    JLabel turnLabel = new JLabel("Now it's " + currentPlayer + "'s turn!");
    
    int yellowScore = 0;
    JLabel yellowScoreLabel = new JLabel("YEL: " + String.valueOf(yellowScore));
    
    int redScore = 0;
    JLabel redScoreLabel = new JLabel("RED: " + String.valueOf(redScore));
    
    int turn = 1;
    boolean isOver = false;

    // 6x7 bidimensional array that represents the game board
    JButton[][] board = new JButton[6][7];

    Connect4() {
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Centers the window

        // Panel with a grid layout to organize the buttons (6x7)
        panel.setLayout(new GridLayout(6, 7));

        // This works due to how Java handles object references!
        formatLabel(turnLabel);
        formatLabel(yellowScoreLabel);
        formatLabel(redScoreLabel);

        yellowScoreLabel.setForeground(Color.YELLOW);
        redScoreLabel.setForeground(Color.RED);

        restartButton.setBackground(Color.GRAY);
        restartButton.setFocusable(false);
        restartButton.setFont(new Font("Arial Unicode MS", Font.BOLD, 20));

        gameStatusPanel.setLayout(new BorderLayout());
        gameStatusPanel.add(yellowScoreLabel, BorderLayout.EAST);
        gameStatusPanel.add(redScoreLabel, BorderLayout.WEST);
        gameStatusPanel.add(turnLabel, BorderLayout.CENTER);
        gameStatusPanel.add(restartButton, BorderLayout.SOUTH);
        
        // Add action listener to restart the game when the restart button is called
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Reset the game board to its initial state
                for (int r=0; r<6; r++) {
                    for (int c=0; c<7; c++) {
                        // Clear the tile's text
                        board[r][c].setText("");
                        // Restore the default color of the tiles (needed in case of victory)
                        board[r][c].setBackground(Color.BLUE);
                        board[r][c].setForeground(Color.WHITE);
                    }
                }
                // Reset the turn counter
                turn = 0;
                isOver = false;
            }
        });

        // Creation of buttons for each board tile
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                JButton tileButton = new JButton();
                tileButton.setBackground(Color.BLUE);
                tileButton.setForeground(Color.WHITE);
                tileButton.setFocusable(false);
                board[r][c] = tileButton; // Assigns the button to the corresponding element in the array

                panel.add(tileButton); // Inserts the button into the panel grid

                // Adds a click event to each button (tile)
                tileButton.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) {
                        JButton selectedTile = (JButton) e.getSource(); // Gets the triggered GUI component
                        if (!isOver && tileButton.getText().isEmpty() && !hasEmptyBelow(selectedTile)) {

                            if (currentPlayer.equals("YELLOW")) {
                                selectedTile.setText("\u26AB");
                                selectedTile.setForeground(Color.YELLOW);
                                tileButton.setFont(new Font("Arial Unicode MS", Font.PLAIN, 90));
                            } else if (currentPlayer.equals("RED")) {
                                selectedTile.setText("\u25CF"); // Distinct Unicode symbols to ensure matrix differentiation
                                tileButton.setFont(new Font("Arial Unicode MS", Font.PLAIN, 78));
                                selectedTile.setForeground(Color.RED);
                            }

                            turn++;
                            if (hasWinner()) {
                                isOver = true;
                                if (currentPlayer == "YELLOW") {
                                    yellowScore++;
                                    yellowScoreLabel.setText("YEL: " + String.valueOf(yellowScore));
                                    currentPlayer = "RED";
                                } else {
                                    redScore++;
                                    redScoreLabel.setText("RED: " + String.valueOf(redScore));
                                    currentPlayer = "YELLOW";
                                }
                                turn = 1;
                            } else if (turn == 42) {
                                isOver = true;
                                handleDraw();
                                turn = 1;
                            } else {
                                // Alternate players
                                currentPlayer = currentPlayer == "YELLOW" ? "RED" : "YELLOW";
                                turnLabel.setText("Now it's " + currentPlayer + "'s turn!");
                            }
                        }
                    }
                });
            }
        }

        // Fill the frame
        frame.add(gameStatusPanel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    boolean hasWinner() {
        // Vertical
        for (int c=0; c<7; c++) {
            for (int i=0; i<3; i++) {
                if (board[i][c].getText() != "" &
                    board[i][c].getText().equals(board[i+1][c].getText()) &&
                    board[i+1][c].getText().equals(board[i+2][c].getText()) &&
                    board[i+2][c].getText().equals(board[i+3][c].getText())) {
                        setWinner(board[i][c], board[i+1][c], board[i+2][c], board[i+3][c]);
                        return true;
                }
            }
        }

        // Horizontal
        for (int r=0; r<6; r++) {
            for (int c=0; c<4; c++) {
                if (board[r][c].getText() != "" &&
                    board[r][c].getText().equals(board[r][c+1].getText()) &&
                    board[r][c+1].getText().equals(board[r][c+2].getText()) &&
                    board[r][c+2].getText().equals(board[r][c+3].getText())) {
                        setWinner(board[r][c], board[r][c+1], board[r][c+2], board[r][c+3]);
                        return true;
                }
            }
        }

        // Diagonal Ascendente (↗)
        for (int r = 3; r < 6; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c].getText() != "" &&
                    board[r][c].getText().equals(board[r-1][c+1].getText()) &&
                    board[r-1][c+1].getText().equals(board[r-2][c+2].getText()) &&
                    board[r-2][c+2].getText().equals(board[r-3][c+3].getText())) {
                    setWinner(board[r][c], board[r-1][c+1], board[r-2][c+2], board[r-3][c+3]);
                    return true;
                }
            }
        }

        // Diagonal Descendente (↘)
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c].getText() != "" &&
                    board[r][c].getText().equals(board[r+1][c+1].getText()) &&
                    board[r+1][c+1].getText().equals(board[r+2][c+2].getText()) &&
                    board[r+2][c+2].getText().equals(board[r+3][c+3].getText())) {
                    setWinner(board[r][c], board[r+1][c+1], board[r+2][c+2], board[r+3][c+3]);
                    return true;
                }
            }
        }

        return false;
    }

    boolean hasEmptyBelow(JButton selectedTile) {
        int[] positions = findPosition(selectedTile);
        int row = positions[0];
        int column = positions[1];

        if (row == 5) { 
            return false; // First row always available
        } else if (board[row+1][column].getText() == "") {
            return true; // If there is no piece below
        } else {
            return false; // If there is a piece below
        }
    }

    int[] findPosition(JButton selectedTile) {
        // Finds the position of the triggered button in the array
        for (int r=0; r<6; r++) {
            for (int c=0; c<7; c++) {
                if (selectedTile == board[r][c]) {
                    return new int[]{r, c}; // Returns the coordinates of the tile
                }
            }
        }
        return new int[]{-1, -1}; // Default return if not found
    }

    // Formats JLabel objects directly in the memory by the "label" reference to it given
    void formatLabel(JLabel label) {
        label.setOpaque(true); // Enables background color
        label.setBackground(Color.DARK_GRAY); 
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
    }

    void setWinner(JButton firstTileButton, JButton secondTileButton,
                   JButton thirdTileButton, JButton fourthTileButton) {
        // Paint the entire board gray 
        for (int r=0; r<6; r++) {
            for (int c=0; c<7; c++) {
                board[r][c].setBackground(Color.DARK_GRAY);
            }
        }
        // Display the winner in the turn label
        turnLabel.setText(currentPlayer + " is the winner!");

        // Highlight the winning tiles with a green background
        firstTileButton.setBackground(Color.green);
        secondTileButton.setBackground(Color.green);
        thirdTileButton.setBackground(Color.green);
        fourthTileButton.setBackground(Color.green);
    }

    void handleDraw() {
        for (int i=0; i<6; i++) {
            for (int j=0; j<7; j++) {
                board[i][j].setBackground(Color.BLACK);
                board[i][j].setForeground(Color.GRAY);
                turnLabel.setText("It's a draw!");
            }
        }
    }
}
