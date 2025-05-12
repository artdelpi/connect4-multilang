import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Connect4 {
    JFrame frame = new JFrame("Tic-Tac-Toe");
    JPanel panel = new JPanel();
    JButton restartButton = new JButton("Restart");
    String currentPlayer = "X";
    JLabel label = new JLabel("Now it's " + currentPlayer + "'s turn!");
    int turn = 1;
    int xScore = 0;
    int yScore = 0;
    JLabel xScoreLabel = new JLabel(String.valueOf(xScore));
    JLabel yScoreLabel = new JLabel(String.valueOf(yScore));
    boolean isOver = false;

    // Matriz 6x7 que representa as casas do jogo (resolução lógica)
    JButton[][] board = new JButton[6][7];

    Connect4() {
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Centraliza a janela
        
        // Painel com layout em grade para organizar os botões (3x3)
        panel.setLayout(new GridLayout(6, 7));

        // This works due to how Java handles object references!
        formatLabel(label);
        formatLabel(yScoreLabel);
        formatLabel(xScoreLabel);
        
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

        // Criação dos botões pra cada slot do tabuleiro
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                JButton slot = new JButton();
                slot.setBackground(Color.BLUE);
                slot.setForeground(Color.WHITE);
                slot.setFont(new Font("Arial", Font.BOLD, 100));
                slot.setFocusable(false);
                board[r][c] = slot; // Atribui o botão ao elemento correspondente na matriz

                panel.add(slot); // Insere botão no grid do painel

                // Adiciona o evento de clique para cada botão (slot)
                slot.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) {
                        JButton selectedTile = (JButton) e.getSource(); // Toma componente GUI acionado
                        if (!isOver && slot.getText().isEmpty() && !hasEmptyBelow(selectedTile)) {
                            selectedTile.setText(currentPlayer);
                            turn++;

                            if (hasWinner()) {
                                isOver = true;
                                if (currentPlayer == "X") {
                                    xScore++;
                                    xScoreLabel.setText(String.valueOf(xScore));
                                } else {
                                    yScore++;
                                    yScoreLabel.setText(String.valueOf(yScore));
                                }
                            } else if (turn > 42) {
                                isOver = true;
                                handleDraw();
                            } else {
                                // Alterna entre os jogadores X e O
                                currentPlayer = currentPlayer == "X" ? "O" : "X";
                                label.setText("Now it's " + currentPlayer + "'s turn!");
                            }
                        }
                    }
                });
            }
        }

        frame.add(label, BorderLayout.NORTH);
        frame.add(yScoreLabel, BorderLayout.EAST);
        frame.add(xScoreLabel, BorderLayout.WEST);
        frame.add(panel, BorderLayout.CENTER);
        frame.add(restartButton, BorderLayout.SOUTH);
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

                        // Colore casas vencedoras e lança mensagem
                        board[i][c].setBackground(Color.yellow);
                        board[i][c].setBackground(Color.GREEN);
                        board[i+1][c].setBackground(Color.yellow);
                        board[i+1][c].setBackground(Color.GREEN);
                        board[i+2][c].setBackground(Color.yellow);
                        board[i+2][c].setBackground(Color.GREEN);
                        board[i+3][c].setBackground(Color.yellow);
                        board[i+3][c].setBackground(Color.GREEN);
                        label.setText(currentPlayer + " is the winner!");
                        return true;
                }
            }
        }

        // Horizontal
        for (int r=0; r<6; r++) {
            for (int i=0; i<4; i++) {
                if (board[r][i].getText() != "" &&
                    board[r][i].getText().equals(board[r][i+1].getText()) &&
                    board[r][i+1].getText().equals(board[r][i+2].getText()) &&
                    board[r][i+2].getText().equals(board[r][i+3].getText())) {
                        
                        // Colore casas vencedoras e lança mensagem
                        board[r][i].setBackground(Color.yellow);
                        board[r][i].setBackground(Color.GREEN);
                        board[r][i+1].setBackground(Color.yellow);
                        board[r][i+1].setBackground(Color.GREEN);
                        board[r][i+2].setBackground(Color.yellow);
                        board[r][i+2].setBackground(Color.GREEN);
                        board[r][i+3].setBackground(Color.yellow);
                        board[r][i+3].setBackground(Color.GREEN);
                        label.setText(currentPlayer + " is the winner!");
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

                    // Colore casas vencedoras
                    board[r][c].setBackground(Color.GREEN);
                    board[r-1][c+1].setBackground(Color.GREEN);
                    board[r-2][c+2].setBackground(Color.GREEN);
                    board[r-3][c+3].setBackground(Color.GREEN);
                    label.setText(currentPlayer + " is the winner!");
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

                    // Colore casas vencedoras
                    board[r][c].setBackground(Color.GREEN);
                    board[r+1][c+1].setBackground(Color.GREEN);
                    board[r+2][c+2].setBackground(Color.GREEN);
                    board[r+3][c+3].setBackground(Color.GREEN);
                    label.setText(currentPlayer + " is the winner!");
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
            return false; // Primeira fileira sempre disponível
        } else if (board[row+1][column].getText() == "") {
            return true; // Caso não haja ficha embaixo
        } else {
            return false; // Caso haja ficha embaixo
        }
    }

    int[] findPosition(JButton selectedTile) {
        // Encontra posição do botão acionado, na matriz
        for (int r=0; r<6; r++) {
            for (int c=0; c<7; c++) {
                if (selectedTile == board[r][c]) {
                    return new int[]{r, c}; // Retorna coordenadas do tile
                }
            }
        }
        return new int[]{-1, -1}; // Retorno padrão caso não encontre
    }

    // Formats JLabel objects directly in the memory by the "label" reference to it given
    void formatLabel(JLabel label) {
        label.setOpaque(true); // Enables background color
        label.setBackground(Color.BLACK); 
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
    }

    void handleDraw() {
        for (int i=0; i<6; i++) {
            for (int j=0; j<7; j++) {
                board[i][j].setBackground(Color.BLACK);
                board[i][j].setForeground(Color.GRAY);
                label.setText("It's a draw!");
            }
        }
    }
}
