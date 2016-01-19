package Tetris;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import javax.swing.SwingUtilities;

import static java.util.Objects.deepEquals;

/**
 * Created by ben higgs 1403099 for the CE203 Assignment 2. Tetris Gameboard
 */
public class GameBoard extends JComponent implements ActionListener {

    protected int[][] board;  //new 2d array board
    protected int[][] nextshape;  //new 2d array that will hold the next shape
    protected static Timer timer;
    protected boolean pieceactive = false;    //tells if a piece is currently moving down the board
    protected int shapeactive = 0;    //id of shape active
    protected int shaperotation = 0;  //rotation id of shape active
    protected int nextshapeid = 0;    //id of next shape
    protected int score = 0;
    protected char lastmove = 's';    //the last move committed
    public static boolean game = true;  //game status
    protected double difficultyMultiplier = 0.0;  //difficulty multipliers

    //list of colours. contains black to fill the space
    static Color[] colours = {Color.white, Color.blue, Color.green, Color.red, Color.cyan, Color.orange, Color.pink, Color.yellow, Color.black, Color.black, Color.black, Color.blue, Color.green, Color.red, Color.cyan, Color.orange, Color.pink, Color.yellow};

    //list of game pieces. This holds a 4d array. It is an array of integer arrays that hold each shape, in each 4 possible rotations. This allows the rotation and spawn function to work.
    final static int[][][][] GAMEPIECES = {
            {{
                    {0, 1, 0, 0},//     1
                    {0, 1, 0, 0},//     1
                    {0, 1, 0, 0},//     1
                    {0, 1, 0, 0},//     1
            }, {
                    {0, 0, 0, 0},//     1
                    {1, 1, 1, 1},//     1
                    {0, 0, 0, 0},//     1
                    {0, 0, 0, 0},//     1
            }, {
                    {0, 0, 1, 0},//     1
                    {0, 0, 1, 0},//     1
                    {0, 0, 1, 0},//     1
                    {0, 0, 1, 0},//     1
            }, {
                    {0, 0, 0, 0},//     1
                    {0, 0, 0, 0},//     1
                    {1, 1, 1, 1},//     1
                    {0, 0, 0, 0},//     1
            }},
            {{
                    {2, 2, 0, 0},//     1 1
                    {0, 2, 0, 0},//     1
                    {0, 2, 0, 0},//     1
                    {0, 0, 0, 0},
            }, {
                    {0, 0, 2, 0},//     1 1
                    {2, 2, 2, 0},//     1
                    {0, 0, 0, 0},//     1
                    {0, 0, 0, 0},
            }, {
                    {0, 2, 0, 0},//     1 1
                    {0, 2, 0, 0},//     1
                    {0, 2, 2, 0},//     1
                    {0, 0, 0, 0},
            }, {
                    {0, 0, 0, 0},//     1 1
                    {2, 2, 2, 0},//     1
                    {2, 0, 0, 0},//     1
                    {0, 0, 0, 0},
            }},
            {{
                    {0, 3, 3, 0},//     1
                    {0, 3, 0, 0},//     1
                    {0, 3, 0, 0},//   1 1
                    {0, 0, 0, 0},
            }, {
                    {0, 0, 0, 0},//     1
                    {3, 3, 3, 0},//     1
                    {0, 0, 3, 0},//   1 1
                    {0, 0, 0, 0},
            }, {
                    {0, 3, 0, 0},//     1
                    {0, 3, 0, 0},//     1
                    {3, 3, 0, 0},//   1 1
                    {0, 0, 0, 0},
            }, {
                    {3, 0, 0, 0},//     1
                    {3, 3, 3, 0},//     1
                    {0, 0, 0, 0},//   1 1
                    {0, 0, 0, 0},
            }},
            {{
                    {4, 0, 0, 0},//     1
                    {4, 4, 0, 0},//     1 1
                    {0, 4, 0, 0},//       1
                    {0, 0, 0, 0},
            }, {
                    {0, 4, 4, 0},//     1
                    {4, 4, 0, 0},//     1 1
                    {0, 0, 0, 0},//       1
                    {0, 0, 0, 0},
            }, {
                    {0, 4, 0, 0},//     1
                    {0, 4, 4, 0},//     1 1
                    {0, 0, 4, 0},//       1
                    {0, 0, 0, 0},
            }, {
                    {0, 0, 0, 0},//     1
                    {0, 4, 4, 0},//     1 1
                    {4, 4, 0, 0},//       1
                    {0, 0, 0, 0},
            }},
            {{
                    {0, 5, 0, 0},
                    {5, 5, 0, 0},//       1
                    {5, 0, 0, 0},//     1 1
                    {0, 0, 0, 0},//     1
            }, {
                    {5, 5, 0, 0},
                    {0, 5, 5, 0},//       1
                    {0, 0, 0, 0},//     1 1
                    {0, 0, 0, 0},//     1
            }, {
                    {0, 0, 5, 0},
                    {0, 5, 5, 0},//       1
                    {0, 5, 0, 0},//     1 1
                    {0, 0, 0, 0},//     1
            }, {
                    {0, 0, 0, 0},
                    {5, 5, 0, 0},//       1
                    {0, 5, 5, 0},//     1 1
                    {0, 0, 0, 0},//     1
            }},
            {{
                    {0, 6, 0, 0},
                    {6, 6, 6, 0},//     1
                    {0, 0, 0, 0},//     1 1
                    {0, 0, 0, 0},//     1
            }, {
                    {0, 6, 0, 0},
                    {0, 6, 6, 0},//     1
                    {0, 6, 0, 0},//     1 1
                    {0, 0, 0, 0},//     1
            }, {
                    {0, 0, 0, 0},
                    {6, 6, 6, 0},//     1
                    {0, 6, 0, 0},//     1 1
                    {0, 0, 0, 0},//     1
            }, {
                    {0, 6, 0, 0},
                    {6, 6, 0, 0},//     1
                    {0, 6, 0, 0},//     1 1
                    {0, 0, 0, 0},//     1
            }},
            {{
                    {0, 0, 0, 0},
                    {0, 7, 7, 0},
                    {0, 7, 7, 0},//     1 1
                    {0, 0, 0, 0},//     1 1
            }, {
                    {0, 0, 0, 0},
                    {0, 7, 7, 0},
                    {0, 7, 7, 0},//     1 1
                    {0, 0, 0, 0},//     1 1
            }, {
                    {0, 0, 0, 0},
                    {0, 7, 7, 0},
                    {0, 7, 7, 0},//     1 1
                    {0, 0, 0, 0},//     1 1
            }, {
                    {0, 0, 0, 0},
                    {0, 7, 7, 0},
                    {0, 7, 7, 0},//     1 1
                    {0, 0, 0, 0},//     1 1
            }},
    };


    //constructor that sets up basic elements of the board.
    public GameBoard() {
        board = createBoard();  //new blank board is generated
        nextshape = new int[4][4];  //blank for next shape
        setGameComponents();   //sets up game board properties
        TetrisKeyListener listeners = new TetrisKeyListener();
        addMouseListener(listeners);
        addMouseWheelListener(listeners);
        game=true;  //set game to true as we have begun
    }



    //set difficulty multipliers based on the difficulty string in the TetrisGame class.
    public void setGameComponents() {
        if (TetrisGame.difficultystring == "Normal") {
            timer = new Timer(600, this);
            difficultyMultiplier = 1.0;
        } else if (TetrisGame.difficultystring == "Easy") {
            timer = new Timer(1000, this);
            difficultyMultiplier = 0.5;
        } else if (TetrisGame.difficultystring == "Hard") {
            timer = new Timer(300, this);
            difficultyMultiplier = 1.5;
        } else if (TetrisGame.difficultystring == "ARE YOU MAD") {
            timer = new Timer(100, this);
            difficultyMultiplier = 2.0;
        }
    }

    //create a blank board based on the board size in the TetrisGame class.
    //Each board has a boundary of 9's to indicate the edge of the board
    public int[][] createBoard() {
        int[][] b = new int[TetrisGame.gamesize[0]][TetrisGame.gamesize[1]];
        for (int x = TetrisGame.gamesize[1] - 1; x >= 0; x--) {
            b[TetrisGame.gamesize[0] - 1][x] = 9;
        }
        for (int[] r : b) {
            r[0] = 9;
            r[r.length - 1] = 9;
        }
        return b;
    }


    //Control what is painted and where.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, TetrisGame.WIDTH, TetrisGame.HEIGHT + 200);
        //determine size of the blocks to make sure that it fills the same area each time, no matter how wide or tall the grid is
        int h = TetrisGame.HEIGHT / TetrisGame.gamesize[0];
        int w = TetrisGame.WIDTH / TetrisGame.gamesize[1];

        //print a colour block for each item. This will be based on the integer in each board position that refers to a colour in the colours list
        for (int x = 0; x < board.length - 1; x++) { //the -1 represents the borders
            for (int y = 1; y < board[x].length - 1; y++) {
                if (board[x][y] > 0) {
                    g.setColor(colours[board[x][y]]);

                    g.fill3DRect(y * w,10+ x * h,
                            w, h, true);
                } else {
                    g.setColor(Color.white);
                    g.fill3DRect(y * w,10+x * h,
                            w, h, true);
                }
            }
        }
        g.setFont(new Font("Impact", Font.PLAIN, 25));

        //if game is active. print the next shapes.
        if (game) {
        g.setColor(Color.WHITE);
        g.drawString("Next shape:", 35, 515);
        g.drawString("Score:", 250, 515);
        g.setColor(Color.cyan);
        g.drawString(Integer.toString(score), 300, 545);

            for (int x = 0; x < nextshape.length; x++) {
                for (int y = 0; y < nextshape[x].length; y++) {
                    if (nextshape[x][y] > 0) {
                        g.setColor(colours[nextshape[x][y]]);
                        g.fill3DRect(55 + y * 15, 525 + x * 15,
                                15, 15, true);
                    }
                }
            }
        }
        else{//if game is not active. print game over and the score
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", 35, 515);
            g.drawString("Score:", 250, 515);

            g.setColor(Color.cyan);
            g.drawString(Integer.toString(score), 300, 545);
        }
    }

    //run the timer
    public void run() {
        timer.start();
    }

    //stop the timer
    public void stop() {
        timer.stop();
    }

    //action event that occurs on each timer event.
    public void actionPerformed(ActionEvent e) {
        if (game) {//if game is on.
            if (!pieceactive) {//if no piece is moving on the board, create a new one, a new next shape also and add them to the board
                nextShape();
                addToBoard();
            } else {    //otherwise move the shape down
                try {
                    moveDown();
                } catch (IllegalMoveException e1) { //if move down is illegal(it has a shape blocking the way.), convert active shape to inactive shape and add a new shape. perform a check for completed rows
                    lockBoard();
                    checkRows();
                    addToBoard();
                }
            }
        } else {    //if game is not active. stop the game and timer.
            gameover();
        }
        repaint();
    }

    //stops the game and timer. changes text field on a button in TetrisGame also.
    private void gameover() {
        game = false;
        stop();
        TetrisGame.gamestate=false;
        TetrisGame.startbut.setText("Clear Grid");
    }

    //adds a new block to the board.
    public void addToBoard() {
        //if the last move was to add,and you are adding again, it is game over.
        if(lastmove=='a'){
            gameover();
        }
        //swap next shape => current shape, generate new next shape and add the current shape to the board (only where the shape grid !=0.)
        shapeactive = nextshapeid;
        int[][] shape = nextshape;
        nextShape();
        if(shape[0][0]!=0){board[0][((TetrisGame.gamesize[1] - 2) / 2) - 1] = shape[0][0];}
        if(shape[0][1]!=0){board[0][((TetrisGame.gamesize[1] - 2) / 2)] = shape[0][1];}
        if(shape[0][2]!=0){board[0][((TetrisGame.gamesize[1] - 2) / 2) + 1] = shape[0][2];}
        if(shape[0][3]!=0){board[0][((TetrisGame.gamesize[1] - 2) / 2) + 2] = shape[0][3];}

        if(shape[1][0]!=0){board[1][((TetrisGame.gamesize[1] - 2) / 2) - 1] = shape[1][0];}
        if(shape[1][1]!=0){board[1][((TetrisGame.gamesize[1] - 2) / 2)] = shape[1][1];}
        if(shape[1][2]!=0){board[1][((TetrisGame.gamesize[1] - 2) / 2) + 1] = shape[1][2];}
        if(shape[1][3]!=0){board[1][((TetrisGame.gamesize[1] - 2) / 2) + 2] = shape[1][3];}

        if(shape[2][0]!=0){board[2][((TetrisGame.gamesize[1] - 2) / 2) - 1] = shape[2][0];}
        if(shape[2][1]!=0){board[2][((TetrisGame.gamesize[1] - 2) / 2)] = shape[2][1];}
        if(shape[2][2]!=0){board[2][((TetrisGame.gamesize[1] - 2) / 2) + 1] = shape[2][2];}
        if(shape[2][3]!=0){board[2][((TetrisGame.gamesize[1] - 2) / 2) + 2] = shape[2][3];}

        if(shape[3][0]!=0){board[3][((TetrisGame.gamesize[1] - 2) / 2) - 1] = shape[3][0];}
        if(shape[3][1]!=0){board[3][((TetrisGame.gamesize[1] - 2) / 2)] = shape[3][1];}
        if(shape[3][2]!=0){board[3][((TetrisGame.gamesize[1] - 2) / 2) + 1] = shape[3][2];}
        if(shape[3][3]!=0){board[3][((TetrisGame.gamesize[1] - 2) / 2) + 2] = shape[3][3];}

        pieceactive = true;
        lastmove = 'a';
    }

    //create a new shape to add next. This is shown at the bottom of the screen
    public void nextShape() {
        nextshapeid = new Random().nextInt(7);
        nextshape = GAMEPIECES[nextshapeid][new Random().nextInt(4)];
        nextshapeid++;
    }

    //moves the shape down one row.
    public void moveDown() throws IllegalMoveException {
        if(moveDownCheck()) {//if move check is true, then permit a move on the board
            for (int r = TetrisGame.gamesize[0] - 2; r >= 0; r--) {     //goes through each row and column. Finds all that are the current shape and moves down.
                for (int c = TetrisGame.gamesize[1] - 1; c > 0; c--) {
                    if (board[r][c] == shapeactive) {
                        if (board[r + 1][c] == 0) {
                            board[r + 1][c] = shapeactive;
                            board[r][c] = 0;
                        }
                    }
                }
            }
            repaint();
        }
        else{throw new IllegalMoveException("Cannot Move Down");}   //otherwise move is not legal.
        lastmove = 'd';

    }

    //checks move to make sure downwards move is legal
    public boolean moveDownCheck(){
        int movecount =0;//total of moves
        for (int r = TetrisGame.gamesize[0] - 2; r >= 0; r--) {     //goes through each row and column. Finds all that are the current shape and checks that they can be moved down.
            for (int c = TetrisGame.gamesize[1] - 1; c > 0; c--) {
                if (board[r][c] == shapeactive) {
                    if (board[r + 1][c] == 0||board[r + 1][c] == shapeactive) {
                        if (lastmove == 'a') {                                                  //if lastmove was a, and there is not 2 blocks clear under the shape, its game over. The block will not be able to move.
                            if (board[r + 2][c] != 0 && board[r + 2][c] != shapeactive) {
                                gameover();
                            }
                        }
                        movecount++;
                    }
                }
            }
        }
        if (movecount==4 ) {return(true);}      //if each block can move. return true.
        else{return false;}     //else return false
    }



    //moves the shape left one column.
    public void moveleft() throws IllegalMoveException {
        if(moveLeftCheck()) {//if move check is true, then permit a move on the board
            for (int r = TetrisGame.gamesize[0] - 2; r >= 0; r--) { ///goes through each row and column. Finds all that are the current shape and moves left.
                for (int c = 1; c <= TetrisGame.gamesize[1] - 1; c++) {
                    if (board[r][c] == shapeactive) {
                        if (board[r][c - 1] == 0) {
                            board[r][c - 1] = shapeactive;
                            board[r][c] = 0;
                        }
                    }
                }
            }
            repaint();
        }
        else{throw new IllegalMoveException("Cannot Move Left");}   //otherwise move is not legal.
        lastmove = 'l';
    }

    //checks if a move left is legal, and that all blocks will be able to move.
    public boolean moveLeftCheck(){
        int movecount =0;//total of moves
        for (int r = TetrisGame.gamesize[0] - 2; r >= 0; r--) {     //goes through each row and column. Finds all that are the current shape and checks that they can be moved left.
            for (int c = 1; c <= TetrisGame.gamesize[1] - 1; c++) {
                if (board[r][c] == shapeactive) {
                    if (board[r][c-1] == 0||board[r][c-1] == shapeactive) {
                        movecount++;
                    }
                }
            }
        }
        if (movecount==4 ) {return(true);}      //if each block can move. return true.
        else{return false;}     //else return false
    }



    //moves the shape right by one column
    public void moveright() throws IllegalMoveException {
        if(moveRightCheck()) {  //if move check is true, then permit a move on the board
            for (int r = TetrisGame.gamesize[0] - 2; r >= 0; r--) {     //goes through each row and column. Finds all that are the current shape and moves right.
                for (int c = TetrisGame.gamesize[1] - 1; c >=1; c--) {
                    if (board[r][c] == shapeactive) {
                        if (board[r][c + 1] == 0) {
                            board[r][c + 1] = shapeactive;
                            board[r][c] = 0;
                        }
                    }
                }
            }
            repaint();
        }
        else{throw new IllegalMoveException("Cannot Move Right");}   //otherwise move is not legal.
        lastmove = 'r';
    }

    public boolean moveRightCheck(){
        int movecount =0;//total of moves
        for (int r = TetrisGame.gamesize[0] - 2; r >= 0; r--) {     //goes through each row and column. Finds all that are the current shape and checks that they can be moved right.
            for (int c = TetrisGame.gamesize[1] -1; c >=1; c--) {
                if (board[r][c] == shapeactive) {
                    if (board[r][c+1] == 0||board[r][c+1] == shapeactive) {
                        movecount++;
                    }
                }
            }
        }
        if (movecount==4 ) {return(true);}      //if each block can move. return true.
        else{return false;}     //else return false
    }


/*
    public void rotate() throws IllegalMoveException {
        int[][] temp = board.clone();
        int[][] curshape = GAMEPIECES[shapeactive-1][shaperotation];
        int[] rotatepoint;
        int[] firstoccurence={0,0};
        boolean rotationfound=false;
        boolean coords =false;
        if (shapeactive != 7) {
            while(!coords) {
                for (int r = 0; r < TetrisGame.gamesize[0] - 2; r++) {
                    for (int c = 0; c < TetrisGame.gamesize[1] - 1; c++) {
                        if (temp[r][c] == shapeactive) {
                            firstoccurence = new int[]{r, c};
                            coords=true;
                        }
                    }
                }
            }
            System.out.println(firstoccurence[0]+" "+firstoccurence[1]);
            int[][] shape = shapearray(firstoccurence);

            while (!rotationfound){
            for (int x = 0; x <= 4; x++) {
                int[] tempoccurence = firstoccurence.clone();
                //checks rows above and to the left of the matching number, to find the box match with the shape from GAMEPIECES
                for (int rr = 0; rr < 3; rr++) {//rows above check
                    for (int cc = 0; cc < 3; cc++) {//column to the left check
                        int[][] block = shapearray(tempoccurence);  //set shapearray as a block from the board
                        printarray(block);
                        tempoccurence = new int[]{tempoccurence[0]-1,tempoccurence[1]-1};
                        int curcount = 0;//counts the current shape number matches
                        for (int r=0;r<=3;r++){
                            tempoccurence[0]=firstoccurence[0]+r;
                            for(int c=0;c<=3;c++){
                                tempoccurence[1]=firstoccurence[1]+c;
                                if(block[r][c]==curshape[r][c]){
                                    curcount++;
                                }
                            }
                        }
                        if (curcount == 4) {
                            rotationfound=true;
                            System.out.print("Found");
                        }
                    }
                }
                if(rotationfound==false){
                    System.out.print("Not Found");
                    throw new IllegalMoveException("Cannot find shape for some reason");
                }
             }
            }
        }
        else {
            if (shaperotation == 4) {
                shaperotation = 0;
            } else {
                shaperotation++;
            }
        }
    }
*/

    //creates a 2d array of just the shape on the gameboard.this will include all blocks as it will be used to assess if a rotation can be made.
    public int[][] shapearray(int[] toppointer){
        int[][] temp = new int[4][4];
        toppointer = new int[] {toppointer[0]-1,toppointer[1]-1};

        temp[0][0] = board[toppointer[0]][toppointer[1]];
        temp[0][1] = board[toppointer[0]][toppointer[1]+1];
        temp[0][2] = board[toppointer[0]][toppointer[1]+2];
        temp[0][3] = board[toppointer[0]][toppointer[1]+3];

        temp[1][0] = board[toppointer[0]+1][toppointer[1]];
        temp[1][1] = board[toppointer[0]+1][toppointer[1]+1];
        temp[1][2] = board[toppointer[0]+1][toppointer[1]+2];
        temp[1][3] = board[toppointer[0]+1][toppointer[1]+3];

        temp[2][0] = board[toppointer[0]+2][toppointer[1]];
        temp[2][1] = board[toppointer[0]+2][toppointer[1]+1];
        temp[2][2] = board[toppointer[0]+2][toppointer[1]+2];
        temp[2][3] = board[toppointer[0]+2][toppointer[1]+3];

        temp[3][0] = board[toppointer[0]+3][toppointer[1]];
        temp[3][1] = board[toppointer[0]+3][toppointer[1]+1];
        temp[3][2] = board[toppointer[0]+3][toppointer[1]+2];
        temp[3][3] = board[toppointer[0]+3][toppointer[1]+3];

        return temp;
    }






    //This adds a value of 10 to any shape that was the current piece. This allows the ints to stay on the board, and not be confused with moving objects that may be the same shape.
    public void lockBoard() {
        for (int r = TetrisGame.gamesize[0] - 2; r >= 0; r--) {     //checks all excluding borders
            for (int c = TetrisGame.gamesize[1] - 1; c >= 0; c--) {
                if (board[r][c] == shapeactive) {
                    board[r][c] = shapeactive + 10;      //plus 10 to shapeactive blocks.
                }
            }
        }
    }

    //checks if rows are full, and adds the score if they are after removing the full row from the board
    public void checkRows() {
        int rowtally = 0;   //tracks how many full rows.
        for (int r = 0; r < TetrisGame.gamesize[0] - 1; r++) {
            int c = 0;
            for (int cc = TetrisGame.gamesize[1] - 2; cc > 0; cc--) {
                if (board[r][cc] != 0) {
                    c++;
                }
                if (c == TetrisGame.gamesize[1] - 2) {
                    board = removeRow(board, r);
                    rowtally++;
                }
            }
        }
        score = score + (int) (10 * rowtally * difficultyMultiplier);   //works out the score and factors in the difficulty level.
    }

    //used by the checkrow function. This takes the board, and the row number to remove. It creates a new board, transfers everything over but then shifts everything down one and adds a fresh top row.
    public int[][] removeRow(int[][] b, int r) {
        int[][] newb = createBoard();

        for (int row = TetrisGame.gamesize[0] - 1; row > r; row--) {
            newb[row] = b[row];
        }
        for (int row = r; row > 0; row--) {
            newb[row] = b[row - 1];
        }
        return newb;
    }

    //debugging function to print a 2d array.... was very helpful!
    public void printarray(int[][] x) {
        for (int[] a : x) {
            System.out.println(Arrays.toString(a));
        }

    }


    //inner class that is the mouse listener.
    class TetrisKeyListener implements MouseListener, MouseWheelListener {
        //only work when timer is running, so pieces cannot be moved when game is paused.
        public void mouseClicked(MouseEvent e) {
            if (timer.isRunning() && javax.swing.SwingUtilities.isLeftMouseButton(e)) {   //if mouse clicked is left.. move left
                try {
                    moveleft();
                } catch (IllegalMoveException e1) {
                    System.out.println(e1.getMessage());
                }
            } else if (timer.isRunning() && javax.swing.SwingUtilities.isRightMouseButton(e)) {  //if mouse clicked is right.. move right
                try {
                    moveright();
                } catch (IllegalMoveException e1) {
                    System.out.println(e1.getMessage());
                }
            } else if (timer.isRunning() && javax.swing.SwingUtilities.isMiddleMouseButton(e)) {   //if mouse clicked is center... ROTATE IS NOT WORKING, MOVES DOWN INSTEAD.
                //rotate
            }
        }
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (timer.isRunning()&&javax.swing.SwingUtilities.isMiddleMouseButton(e)) {   //if mouse clicked and dragged.. move down.... ROTATE IS NOT WORKING, MOVES DOWN INSTEAD.
                try {
                    moveDown();
                } catch (IllegalMoveException e1) {
                    System.out.println(e1.getMessage());
                }
            }
        }

        //necessary to implement, but not used.
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }
}


//Basic exception that is thrown and caught by the program when an illegal move is attempted.
class IllegalMoveException extends Exception {
    public IllegalMoveException(String message) {
        super(message);
    }
}





