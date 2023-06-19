/*
 * ICS4U Culminating - Brooke Y - 2048 Game
 * Input: Prompt user to: enter username or play as guest. During gameplay, provide the 
 *        option to start a new game, logout, or quit the application.
 * Processing: Calculate board position after each move (arrow keys). Calculate score.
 * Output: Display sliding tiles and resulting board position after each move. 
 *         Display current and best score. 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Thread;

public class App extends JPanel implements ActionListener, KeyListener {
    // menu variables
    private JFrame win;
    private JLabel image, image2;
    private JPanel login;
    private JTextField userBox;
    private JLabel validUsername, nameUser;
    private JButton enter, guest, quit;
    private JButton logout, newGame, quit2;
    private JButton resume, quit3;
    // gameplay variables
    private int bestScoreValue, scoreValue;
    private JLabel endMessage;
    private boolean hasWon = false;
    private JPanel gui, grid;
    private ArrayList<Integer> empty = new ArrayList<Integer>();
    private int[][] values = new int[4][4];
    private int[][] moves = new int[4][4];
    private JLabel[][] tiles = new JLabel[4][4];
    private Color[] colors = new Color[12];
        
    private JLabel score, bestScore;
    private boolean legalMove = true, tilesInMotion = false;
    private int duration = 83;
    public static void main(String[] args) {
        new App();
    }
    public App() {
        win = new JFrame("2048 Game");
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(1200, 800);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        initColors();
        login = new JPanel();
        login.setBackground(new Color(250, 242, 190));
        login.setLayout(null);
        JLabel title = new JLabel("2048");
        title.setFont(new Font("Courier", Font.BOLD, 80));
        title.setBounds(500,80, 320, 100);
        title.setForeground(Color.DARK_GRAY);
        login.add(title);
        JPanel loginBackground = new JPanel();
        loginBackground.setBackground(Color.lightGray);
        loginBackground.setBorder(BorderFactory.createLineBorder(Color.gray));
        loginBackground.setLayout(null);
        loginBackground.setBounds(130, 320, 420, 200);
        JLabel user = new JLabel("Username:");
        user.setBounds(20, 20, 100, 22);
        user.setFont(new Font("Dialog", Font.PLAIN, 15));
        loginBackground.add(user);
        userBox = new JTextField();
        userBox.setBounds(100, 20, 200, 22);
        userBox.setFont(new Font("Dialog", Font.PLAIN, 15));
        loginBackground.add(userBox);
        enter = new JButton("Enter");
        enter.setBounds(300, 20, 70, 22);
        enter.setFont(new Font("Dialog", Font.PLAIN, 15));
        enter.addActionListener(this);
        loginBackground.add(enter);
        validUsername = new JLabel();
        validUsername.setBounds(110, 50, 200, 15);
        validUsername.setFont(new Font("Dialog", Font.ITALIC, 15));
        validUsername.setForeground(Color.RED);
        loginBackground.add(validUsername);
        JLabel or = new JLabel("or");
        or.setBounds(50, 70, 20, 22);
        or.setFont(new Font("Dialog", Font.PLAIN, 15));
        loginBackground.add(or);
        guest = new JButton("Play as Guest");
        guest.setBounds(30, 120, 150, 22);
        guest.setFont(new Font("Dialog", Font.PLAIN, 15));
        guest.addActionListener(this);
        loginBackground.add(guest);
        login.add(loginBackground);
        JPanel rulesBackground = instructions();
        rulesBackground.setBounds(650, 250, 440, 410);
        login.add(rulesBackground);
        quit = new JButton("Quit");
        quit.setBounds(60, 60, 80, 40);
        quit.setFont(new Font("SansSerif", Font.BOLD, 18));
        quit.addActionListener(this);
        login.add(quit);
        image = new JLabel(new ImageIcon("image.png"));
        image.setBounds(0, 0, 1200, 800);
        login.add(image);
        win.add(login);
        win.setVisible(true);
        board();
        generateTile();
        generateTile();
        updateTiles();
    }
    public void initColors() {
        colors[0] = new Color(240, 239, 233); // 2
        colors[1] = new Color(242, 233, 206); // 4
        colors[2] = new Color(237, 190, 119); // 8
        colors[3] = new Color(240, 169, 110); // 16
        colors[4] = new Color(237, 129, 90); // 32
        colors[5] = new Color(232, 104, 58); // 64
        colors[6] = new Color(232, 215, 121); // 128
        colors[7] = new Color(230, 212, 110); // 256
        colors[8] = new Color(227, 208, 98); // 512
        colors[9] = new Color(235, 212, 84); // 1024
        colors[10] = new Color(235, 204, 52); // 2048
        colors[11] = new Color(32, 40, 64); // > 2048
    }
    public JPanel instructions() {
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(Color.lightGray);
        p.setBorder(BorderFactory.createLineBorder(Color.gray));
        JLabel rules = new JLabel("How to Play");
        rules.setFont(new Font("Courier", Font.BOLD, 20));
        rules.setBounds(25, 10, 300, 40);
        JLabel arrows = new JLabel("1. Use your arrow keys to move the tiles.");
        JLabel merge = new JLabel("2. Equal value tiles merge when they touch.");
        JLabel winCond = new JLabel("3. Add them up to reach 2048!");
        JLabel loseCond = new JLabel("4. The game ends once the grid is filled.");
        JLabel detail = new JLabel("Details");
        detail.setFont(new Font("Courier", Font.BOLD, 20));
        detail.setBounds(25, 220, 300, 40);
        JLabel[] descrip = new JLabel[4];
        descrip[0] = new JLabel("2048 is a single-player sliding puzzle video game.");
        descrip[1] = new JLabel("Numbered tiles (of powers of 2) are shifted on a 4x4 grid.");
        descrip[2] = new JLabel("After every move, a new tile of value 2 or 4 will appear on");
        descrip[3] = new JLabel("a vacant square.");
        int y = 0;
        for (int i = 0; i < 4; i++) {
            descrip[i].setBounds(25, 260 + y, 400, 20);
            descrip[i].setFont(new Font("Dialog", Font.PLAIN, 14));
            y += 35;
            p.add(descrip[i]);
        }
        arrows.setBounds(25, 60, 400, 20);
        arrows.setFont(new Font("Dialog", Font.PLAIN, 14));
        merge.setBounds(25, 100, 400, 20);
        merge.setFont(new Font("Dialog", Font.PLAIN, 14));
        winCond.setBounds(25, 140, 400, 20);
        winCond.setFont(new Font("Dialog", Font.PLAIN, 14));
        loseCond.setBounds(25, 180, 400, 20);
        loseCond.setFont(new Font("Dialog", Font.PLAIN, 14));
        p.add(arrows);
        p.add(merge);
        p.add(winCond);
        p.add(loseCond);
        p.add(rules);
        p.add(detail);
        return p;
    }
    public void board() {
        gui = new JPanel();
        gui.setLayout(null);
        gui.setBackground(new Color(250, 242, 190));
        gui.setBounds(0, 0, 1200, 800);
        endGame();
        grid = new JPanel();
        grid.setLayout(null);
        grid.setBackground(new Color(163, 157, 147));
        grid.setBounds(480, 60, 680, 680);
        JPanel gridCover = new JPanel();
        gridCover.setLayout(null);
        gridCover.setBackground(new Color(163, 157, 147));
        gridCover.setBounds(0, 0, 680, 680);
        JPanel[][] squares = new JPanel[4][4];
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                squares[i][j] = new JPanel();
                squares[i][j].setBackground(Color.lightGray);
                squares[i][j].setBounds(16 + j * 166, 16 + i * 166, 150, 150);
                tiles[i][j] = new JLabel(Integer.toString(values[i][j]), JLabel.CENTER);
                if (values[i][j] == 0) {
                    tiles[i][j].setVisible(false);
                }
                tiles[i][j].setOpaque(true);
                tiles[i][j].setForeground(new Color(110, 104, 102));
                tiles[i][j].setBackground(colors[0]);
                tiles[i][j].setFont(new Font("Courier", Font.BOLD, 30));
                tiles[i][j].setBounds(16 + j * 166, 16 + i * 166, 150, 150);
                gridCover.add(squares[i][j]);
                grid.add(tiles[i][j]);
            }
        }
        grid.add(gridCover);
        gui.add(grid);
        JLabel title = new JLabel("2048");
        title.setFont(new Font("Courier", Font.BOLD, 30));
        title.setBounds(20, 30, 80, 30);
        title.setForeground(Color.DARK_GRAY);
        gui.add(title);
        nameUser = new JLabel("Guest");
        nameUser.setFont(new Font("Courier", Font.BOLD, 16));
        nameUser.setBounds(150, 35, 250, 20);
        gui.add(nameUser);
        bestScore = new JLabel("Best Score: " + bestScoreValue);
        bestScore.setFont(new Font("Courier", Font.PLAIN, 15));
        bestScore.setBounds(40, 100, 200, 20);
        gui.add(bestScore);
        score = new JLabel("Score: " + scoreValue);
        score.setFont(new Font("Courier", Font.PLAIN, 15));
        score.setBounds(40, 140, 200, 20);
        gui.add(score);
        newGame = new JButton("New Game");
        newGame.setBounds(50, 200, 120, 20);
        newGame.setFont(new Font("Dialog", Font.PLAIN, 14));
        newGame.setFocusable(false);
        newGame.addActionListener(this);
        gui.add(newGame);
        logout = new JButton("Logout");
        logout.setBounds(280, 200, 100, 20);
        logout.setFont(new Font("Dialog", Font.PLAIN, 14));
        logout.setFocusable(false);
        logout.addActionListener(this);
        gui.add(logout);
        quit2 = new JButton("Quit");
        quit2.setBounds(290, 130, 80, 20);
        quit2.setFont(new Font("Dialog", Font.PLAIN, 14));
        quit2.setFocusable(false);
        quit2.addActionListener(this);
        gui.add(quit2);
        JPanel rulesBackground = instructions();
        rulesBackground.setBounds(20, 280, 440, 410);
        gui.add(rulesBackground);
        image2 = new JLabel(new ImageIcon("image.png"));
        image2.setBounds(0, 0, 1200, 800);
        gui.add(image2);
        gui.setFocusable(true);
    }
    public void endGame() {
        endMessage = new JLabel("", JLabel.CENTER);
        endMessage.setVisible(false);
        endMessage.setBounds(100, 50, 1000, 200);
        endMessage.setForeground(Color.darkGray);
        endMessage.setFont(new Font("Courier", Font.BOLD, 60));
        resume = new JButton();
        resume.setVisible(false);
        resume.setBounds(520, 300, 160, 50);
        resume.setFont(new Font("SansSerif", Font.BOLD, 22));
        resume.setFocusable(false);
        resume.addActionListener(this);
        quit3 = new JButton("Quit");
        quit3.setVisible(false);
        quit3.setBounds(560, 400, 80, 40);
        quit3.setFont(new Font("SansSerif", Font.BOLD, 18));
        quit3.setFocusable(false);
        quit3.addActionListener(this);
        gui.add(endMessage);
        gui.add(resume);
        gui.add(quit3);
    }
    public void gameplay(KeyEvent e) {
        if (hasWon) {
            endMessage.setVisible(false);
            resume.setVisible(false);
            quit3.setVisible(false);
        }
        legalMove = false;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            calcHorizMove(false);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            calcHorizMove(true);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            calcVertMove(true);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            calcVertMove(false);
        }
    }
    public void updateTiles() {
        boolean winCheck = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (values[i][j] == 0) {
                    tiles[i][j].setVisible(false);
                } else {
                    tiles[i][j].setLocation(16 + j * 166, 16 + i * 166);
                    tiles[i][j].setText(Integer.toString(values[i][j]));
                    int p = (int) (Math.log(values[i][j]) / Math.log(2));
                    if (p > 12) {
                        tiles[i][j].setBackground(colors[11]);
                    } else {
                        tiles[i][j].setBackground(colors[p - 1]);
                    }
                    if (values[i][j] >= 8) {
                        tiles[i][j].setForeground(new Color(250, 250, 247));
                    } else {
                        tiles[i][j].setForeground(new Color(110, 104, 102));
                    }
                    tiles[i][j].setVisible(true);
                }
                moves[i][j] = 0;
                if (values[i][j] == 2048 && !hasWon) {
                    hasWon = true;
                    winCheck = true;
                }
            }
        }
        score.setText("Score: " + scoreValue);
        if (scoreValue > bestScoreValue) {
            bestScoreValue = scoreValue;
            bestScore.setText("Best Score: " + bestScoreValue);
        }
        if (winCheck) {
            won();
        }
        isLost();
    }
    public void generateTile() {
        int[] newTileValues = {2,2,2,2,2,2,2,2,2,4};
        //int[] newTileValues = {1024};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (values[i][j] == 0) {
                    empty.add(i * 4 + j);
                }
            }
        }
        if (empty.size() > 0 && legalMove) {
            int newTilePos = empty.get(new Random().nextInt(empty.size()));
            int newTileValue = new Random().nextInt(newTileValues.length);
            values[newTilePos / 4][newTilePos % 4] = newTileValues[newTileValue];
        } 
        empty.clear();
    }
    public void moveTilesHoriz(boolean dir) {
        tilesInMotion = true;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int[][] x = new int[4][4];
            public void run() {
                if (dir) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (x[i][j] < 166 * moves[i][j]) {
                                tiles[i][j].setLocation(16 + j * 166 + x[i][j], 16 + i * 166);
                                x[i][j] += 2 * moves[i][j];
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (x[i][j] > 166 * moves[i][j]) {
                                tiles[i][j].setLocation(16 + j * 166 + x[i][j], 16 + i * 166);
                                x[i][j] += 2 * moves[i][j];
                            }
                        }
                    }
                }
                if (duration-- == 0) {
                    duration = 83;
                    timer.cancel();
                    generateTile();
                    updateTiles();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1, 1);
    }
    public void moveTilesVert(boolean dir) { // find number of squares moved for each tile
        tilesInMotion = true;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            int[][] x = new int[4][4];
            public void run() {
                if (dir) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (x[i][j] < 166 * moves[i][j]) {
                                tiles[i][j].setLocation(16 + j * 166, 16 + i * 166 + x[i][j]);
                                x[i][j] += 2 * moves[i][j];
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (x[i][j] > 166 * moves[i][j]) {
                                tiles[i][j].setLocation(16 + j * 166, 16 + i * 166 + x[i][j]);
                                x[i][j] += 2 * moves[i][j];
                            }
                        }
                    }
                }
                if (duration-- == 0) {
                    duration = 83;
                    timer.cancel();
                    generateTile();
                    updateTiles();
                } 
            }
        };
        timer.scheduleAtFixedRate(task, 1, 1);
    }
    // true = right/down, false = left/up
            // inspect the transformations by row/col
            // align tiles
            // merge tiles
            // align tiles after merges
            // determine resulting position
            // keep track of move distance of each tile
    public void calcHorizMove(boolean dir) {
        if (dir) { // right
            for (int i = 0; i < 4; i++) {
                String[] initPos = {"a", "b", "c", "d"};
                String[] endPos = {"a", "b", "c", "d"};
                String[] merged = new String[4];
                boolean notAligned = true;
                while (notAligned) {
                    notAligned = false;
                    for (int j = 3; j > 0; j--) { // aligning alg
                        if (values[i][j] == 0) {
                            if (values[i][j - 1] != 0) {
                                legalMove = true;
                                values[i][j] = values[i][j - 1];
                                values[i][j - 1] = 0;
                                String s = endPos[j];
                                endPos[j] = endPos[j - 1];
                                endPos[j - 1] = s;
                                notAligned = true;
                            }
                        }
                    }
                }
                for (int j = 3; j > 0; j--) { // merging alg
                    if (values[i][j] == values[i][j - 1]) {
                        if (values[i][j] != 0) {
                            merged[j] = endPos[j - 1];
                            endPos[j - 1] = null;
                            legalMove = true;
                        }
                        values[i][j] *= 2;
                        scoreValue += values[i][j];
                        values[i][j - 1] = 0;
                    }
                }
                notAligned = true;
                while (notAligned) {
                    notAligned = false;
                    for (int j = 3; j > 0; j--) { // aligning alg
                        if (values[i][j] == 0) {
                            if (values[i][j - 1] != 0) {
                                values[i][j] = values[i][j - 1];
                                values[i][j - 1] = 0;
                                String s = endPos[j];
                                endPos[j] = endPos[j - 1];
                                endPos[j - 1] = s;
                                String l = merged[j];
                                merged[j] = merged[j - 1];
                                merged[j - 1] = l;
                                notAligned = true;
                            }
                        }
                    }
                }
                for (int m = 3; m >= 0; m--) {
                    for (int n = 3; n >= 0; n--) {
                        if (endPos[m] != null && endPos[m].equals(initPos[n])) {
                            if (m > n) moves[i][n] = m - n;
                        } else if (merged[m] != null && merged[m].equals(initPos[n])) {
                            if (m > n) moves[i][n] = m - n;
                        }
                    }
                }
            }
        } else { // left
            for (int i = 0; i < 4; i++) {
                String[] initPos = {"a", "b", "c", "d"};
                String[] endPos = {"a", "b", "c", "d"};
                String[] merged = new String[4];
                boolean notAligned = true;
                while (notAligned) {
                    notAligned = false;
                    for (int j = 0; j < 3; j++) { // aligning alg
                        if (values[i][j] == 0) {
                            if (values[i][j + 1] != 0) {
                                legalMove = true;
                                values[i][j] = values[i][j + 1];
                                values[i][j + 1] = 0;
                                String s = endPos[j];
                                endPos[j] = endPos[j + 1];
                                endPos[j + 1] = s;
                                notAligned = true;
                            }
                        }
                    }
                }
                for (int j = 0; j < 3; j++) { // merging alg
                    if (values[i][j] == values[i][j + 1]) {
                        if (values[i][j] != 0) {
                            merged[j] = endPos[j + 1];
                            endPos[j + 1] = null;
                            legalMove = true;
                        }
                        values[i][j] *= 2;
                        scoreValue += values[i][j];
                        values[i][j + 1] = 0;
                    }
                }
                notAligned = true;
                while (notAligned) {
                    notAligned = false;
                    for (int j = 0; j < 3; j++) { // aligning alg
                        if (values[i][j] == 0) {
                            if (values[i][j + 1] != 0) {
                                values[i][j] = values[i][j + 1];
                                values[i][j + 1] = 0;
                                String s = endPos[j];
                                endPos[j] = endPos[j + 1];
                                endPos[j + 1] = s;
                                String l = merged[j];
                                merged[j] = merged[j + 1];
                                merged[j + 1] = l;                                
                                notAligned = true;
                            }
                        }
                    }
                }
                for (int m = 0; m < 4; m++) {
                    for (int n = 0; n < 4; n++) {
                        if (endPos[m] != null && endPos[m].equals(initPos[n])) {
                            if (m < n) moves[i][n] = m - n;
                        } else if (merged[m] != null && merged[m].equals(initPos[n])) {
                            if (m < n) moves[i][n] = m - n;
                        }
                    }
                }
            }
        }
        moveTilesHoriz(dir);
    }
    public void calcVertMove(boolean dir) {
        if (dir) { // down
            for (int i = 0; i < 4; i++) {
                String[] initPos = {"a", "b", "c", "d"};
                String[] endPos = {"a", "b", "c", "d"};
                String[] merged = new String[4];
                boolean notAligned = true;
                while (notAligned) {
                    notAligned = false;
                    for (int j = 3; j > 0; j--) { // aligning alg
                        if (values[j][i] == 0) {
                            if (values[j - 1][i] != 0) {
                                legalMove = true;
                                values[j][i] = values[j - 1][i];
                                values[j - 1][i] = 0;
                                String s = endPos[j];
                                endPos[j] = endPos[j - 1];
                                endPos[j - 1] = s;
                                notAligned = true;
                            }
                        }
                    }
                }
                for (int j = 3; j > 0; j--) { // merging alg
                    if (values[j][i] == values[j - 1][i]) {
                        if (values[j][i] != 0) {
                            merged[j] = endPos[j - 1];
                            endPos[j - 1] = null;
                            legalMove = true;
                        }
                        values[j][i] *= 2;
                        scoreValue += values[j][i];
                        values[j - 1][i] = 0;
                    }
                }
                notAligned = true;
                while (notAligned) {
                    notAligned = false;
                    for (int j = 3; j > 0; j--) { // aligning alg
                        if (values[j][i] == 0) {
                            if (values[j - 1][i] != 0) {
                                values[j][i] = values[j - 1][i];
                                values[j - 1][i] = 0;
                                String s = endPos[j];
                                endPos[j] = endPos[j - 1];
                                endPos[j - 1] = s;
                                String l = merged[j];
                                merged[j] = merged[j - 1];
                                merged[j - 1] = l;
                                notAligned = true;
                            }
                        }
                    }
                }
                for (int m = 3; m >= 0; m--) {
                    for (int n = 3; n >= 0; n--) {
                        if (endPos[m] != null && endPos[m].equals(initPos[n])) {
                            if (m > n) moves[n][i] = m - n;
                        } else if (merged[m] != null && merged[m].equals(initPos[n])) {
                            if (m > n) moves[n][i] = m - n;
                        }
                    }
                }
            }
        } else { // up
            for (int i = 0; i < 4; i++) {
                String[] initPos = {"a", "b", "c", "d"};
                String[] endPos = {"a", "b", "c", "d"};
                String[] merged = new String[4];
                boolean notAligned = true;
                while (notAligned) {
                    notAligned = false;
                    for (int j = 0; j < 3; j++) { // aligning alg
                        if (values[j][i] == 0) {
                            if (values[j + 1][i] != 0) {
                                legalMove = true;
                                values[j][i] = values[j + 1][i];
                                values[j + 1][i] = 0;
                                String s = endPos[j];
                                endPos[j] = endPos[j + 1];
                                endPos[j + 1] = s;
                                notAligned = true;
                            }
                        }
                    }
                }
                for (int j = 0; j < 3; j++) { // merging alg
                    if (values[j][i] == values[j + 1][i]) {
                        if (values[j][i] != 0) {
                            merged[j] = endPos[j + 1];
                            endPos[j + 1] = null;
                            legalMove = true;
                        }
                        values[j][i] *= 2;
                        scoreValue += values[j][i];
                        values[j + 1][i] = 0;
                    }
                }
                notAligned = true;
                while (notAligned) {
                    notAligned = false;
                    for (int j = 0; j < 3; j++) { // aligning alg
                        if (values[j][i] == 0) {
                            if (values[j + 1][i] != 0) {
                                values[j][i] = values[j + 1][i];
                                values[j + 1][i] = 0;
                                String s = endPos[j];
                                endPos[j] = endPos[j + 1];
                                endPos[j + 1] = s;
                                String l = merged[j];
                                merged[j] = merged[j + 1];
                                merged[j + 1] = l;
                                notAligned = true;
                            }
                        }
                    }
                }
                for (int m = 0; m < 4; m++) {
                    for (int n = 0; n < 4; n++) {
                        if (endPos[m] != null && endPos[m].equals(initPos[n])) {
                            if (m < n) moves[n][i] = m - n;
                        } else if (merged[m] != null && merged[m].equals(initPos[n])) {
                            if (m < n) moves[n][i] = m - n;
                        }
                    }
                }
            }
        }
        moveTilesVert(dir);
    }
    public void isLost() {
        for (int i = 0; i < 4; i++) { // check filled grid
            for (int j = 0; j < 4; j++) {
                if (values[i][j] == 0) return;
            }
        }
        for (int i = 0; i < 4; i++) { // check horizontal merges
            for (int j = 0; j < 3; j++) {
                if (values[i][j + 1] == values[i][j]) return;
            }
        }
        for (int i = 0; i < 4; i++) { // check vertical merges
            for (int j = 0; j < 3; j++) {
                if (values[j + 1][i] == values[j][i]) return;
            }
        }
        // Code below runs if game over
        endMessage.setText("You Lose, " + nameUser.getText());
        endMessage.setVisible(true);
        resume.setText("New Game");
        resume.setVisible(true);
        quit3.setVisible(true);
    }
    public void won() {
        resume.setText("Continue");
        endMessage.setText("You Win, " + nameUser.getText() + "!");
        endMessage.setVisible(true);
        resume.setVisible(true);
        quit3.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if (tilesInMotion) {
            try {
                Thread.sleep(duration);
            } catch (Exception err) {
                System.exit(0);
            }
        }
        if (e.getSource() == enter) {
            String username = userBox.getText();
            if (username.equals("")) {
                validUsername.setText("Please enter a username");
            } else {
                nameUser.setText("<" + username + ">");
                gui.addKeyListener(this);
                win.add(gui);
                login.setVisible(false);
            }
        } else if (e.getSource() == guest) {
            gui.addKeyListener(this);
            win.add(gui);
            login.setVisible(false);
        } else if (e.getSource() == logout) {
            new App();
        } else if (e.getSource() == newGame) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    values[i][j] = 0;
                    moves[i][j] = 0;
                }
            }
            endMessage.setVisible(false);
            resume.setVisible(false);
            quit3.setVisible(false);
            hasWon = false;
            scoreValue = 0;
            legalMove = true;
            generateTile();
            generateTile();
            updateTiles();
        } else if (e.getSource() == quit || e.getSource() == quit2 || e.getSource() == quit3) {
            System.exit(0);
        } else if (e.getSource() == resume) { // Continue/New Game
            endMessage.setVisible(false);
            resume.setVisible(false);
            quit3.setVisible(false);
            if (resume.getText().equals("New Game")) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        values[i][j] = 0;
                        moves[i][j] = 0;
                    }
                }
                hasWon = false;
                scoreValue = 0;
                legalMove = true;
                generateTile();
                generateTile();
                updateTiles();
            }
        }
    }
    public void keyPressed(KeyEvent e) {
        gameplay(e);
    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) {

    }
}