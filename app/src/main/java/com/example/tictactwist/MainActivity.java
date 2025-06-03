package com.example.tictactwist;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button[][] buttons = new Button[3][3];
    int currentPlayer = 0;
    int playerCount = 2;
    String[] playerSymbols = {"X", "O", "△", "□"};
    String[] playerNames = new String[4];
    boolean gameActive = false;

    TextView statusText;
    EditText playerCountEditText;
    EditText[] nameInputs = new EditText[4];
    Button startButton, resetButton;
    GridLayout gridLayout;
    LinearLayout namesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerCountEditText = findViewById(R.id.playerCountEditText);
        statusText = findViewById(R.id.statusText);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        gridLayout = findViewById(R.id.gridLayout);
        namesLayout = findViewById(R.id.namesLayout);

        nameInputs[0] = findViewById(R.id.name1);
        nameInputs[1] = findViewById(R.id.name2);
        nameInputs[2] = findViewById(R.id.name3);
        nameInputs[3] = findViewById(R.id.name4);

        int[][] ids = {
                {R.id.button00, R.id.button01, R.id.button02},
                {R.id.button10, R.id.button11, R.id.button12},
                {R.id.button20, R.id.button21, R.id.button22}
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = findViewById(ids[i][j]);
                final int finalI = i;
                final int finalJ = j;
                buttons[i][j].setOnClickListener(v -> onCellClicked(finalI, finalJ));
            }
        }

        startButton.setOnClickListener(v -> {
            String input = playerCountEditText.getText().toString().trim();
            if (input.isEmpty() || Integer.parseInt(input) < 2 || Integer.parseInt(input) > 4) {
                Toast.makeText(this, "Enter player count between 2 and 4", Toast.LENGTH_SHORT).show();
                return;
            }

            playerCount = Integer.parseInt(input);

            // Show only needed name inputs
            for (int i = 0; i < 4; i++) {
                nameInputs[i].setVisibility(i < playerCount ? View.VISIBLE : View.GONE);
            }

            for (int i = 0; i < playerCount; i++) {
                String name = nameInputs[i].getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(this, "Enter name for Player " + (i + 1), Toast.LENGTH_SHORT).show();
                    return;
                }
                playerNames[i] = name;
            }

            startGame();
        });

        resetButton.setOnClickListener(v -> resetGame());
    }

    void startGame() {
        gameActive = true;
        currentPlayer = 0;
        statusText.setText(playerNames[currentPlayer] + "'s turn (" + playerSymbols[currentPlayer] + ")");
        resetButton.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.VISIBLE);
        namesLayout.setVisibility(View.GONE);
        clearBoard();
    }

    void onCellClicked(int i, int j) {
        if (!gameActive || !buttons[i][j].getText().toString().equals("")) return;

        buttons[i][j].setText(playerSymbols[currentPlayer]);

        String[] winnerComments = {
                "What a legend! 🏆",
                "Unstoppable! 💪",
                "The Tic Tac Master strikes again!",
                "Winner winner, chicken dinner! 🍗",
                "Too easy! 😎",
                "Victory smells sweet today! 🌸",
                "Can anyone stop them?! 🔥"
        };

        String[] loserComments = {
                "Better luck next time! 😅",
                "Ouch, that must've hurt! 💔",
                "So close... yet so far!",
                "Try again, future champ!",
                "Don't cry... it's just a game! 😭",
                "Defeat tastes like broccoli 🥦",
                "Next time, maybe! 🍀"
        };

        if (checkWinner()) {
            String winMsg = winnerComments[new Random().nextInt(winnerComments.length)];
            String loseMsg = loserComments[new Random().nextInt(loserComments.length)];
            statusText.setText(playerNames[currentPlayer] + " (" + playerSymbols[currentPlayer] + ") wins! 🎉\n" + winMsg + "\n" + loseMsg);
            gameActive = false;
        } else if (isBoardFull()) {
            String drawMsg = loserComments[new Random().nextInt(loserComments.length)];
            statusText.setText("It's a draw! 😐\n" + drawMsg);
            gameActive = false;
        } else {
            currentPlayer = (currentPlayer + 1) % playerCount;
            statusText.setText(playerNames[currentPlayer] + "'s turn (" + playerSymbols[currentPlayer] + ")");
        }
    }

    boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (checkThree(buttons[i][0], buttons[i][1], buttons[i][2])) return true;
            if (checkThree(buttons[0][i], buttons[1][i], buttons[2][i])) return true;
        }
        return checkThree(buttons[0][0], buttons[1][1], buttons[2][2]) ||
                checkThree(buttons[0][2], buttons[1][1], buttons[2][0]);
    }

    boolean checkThree(Button b1, Button b2, Button b3) {
        String s1 = b1.getText().toString();
        return !s1.equals("") && s1.equals(b2.getText().toString()) && s1.equals(b3.getText().toString());
    }

    boolean isBoardFull() {
        for (Button[] row : buttons)
            for (Button b : row)
                if (b.getText().toString().equals(""))
                    return false;
        return true;
    }

    void clearBoard() {
        for (Button[] row : buttons)
            for (Button b : row)
                b.setText("");
    }

    void resetGame() {
        playerCountEditText.setText("");
        for (EditText nameInput : nameInputs) {
            nameInput.setText("");
            nameInput.setVisibility(View.VISIBLE); // Show all name fields again
        }
        namesLayout.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);
        statusText.setText("");
        gameActive = false;
        clearBoard();
    }
}
