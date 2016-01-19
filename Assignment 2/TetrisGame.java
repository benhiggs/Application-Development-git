package Tetris;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;

/**
 *  Created by ben higgs 1403099 for the CE203 Assignment 2. Tetris TetrisGame
 */
public class TetrisGame extends JApplet{

    protected static final int WIDTH = 400, HEIGHT = 500;  //set some basic sizes for both the applet and the gameboard to use
    protected static int[] gamesize ={21,12};  //basic normal game size.
    protected static boolean gamestate=false;  //false until game started
    protected static String difficultystring ="Normal";    //some game settings
    protected static String boardsizestring="Normal";
    protected static GameBoard board = new GameBoard();    //new GameBoard
    public static JButton startbut = new JButton("Start game"); //button has to be public, gameboard changes the text on one occasion.
    AudioClip music;

    //initialisation of the applet
    public void init(){
        JPanel title = new JPanel();        //title panel to hold text and buttons
        title.setLayout(new BorderLayout(0,0));
        setSize(WIDTH, HEIGHT+150);

        //button setup
        JButton difficultychange = new JButton("Change Difficulty");
        JButton boardsizechange = new JButton("Change Board");
        JLabel text = new JLabel("TETRIS");
        text.setFont(new Font("Impact", Font.BOLD, 30));
        text.setForeground(Color.cyan);
        JPanel textcenter = new JPanel();
        textcenter.add(text);
        JPanel buttons = new JPanel(new GridLayout(0,3));
        buttons.add(startbut);
        buttons.add(difficultychange);
        buttons.add(boardsizechange);
        textcenter.setBackground(Color.DARK_GRAY);
        title.add(textcenter,BorderLayout.NORTH);
        title.add(buttons,BorderLayout.CENTER);

        //add everything to board
        add(title,BorderLayout.NORTH);
        add(board,BorderLayout.CENTER);


        //action event for the difficulty change button. Lists options and upon choice, gameboard is refreshed.
        difficultychange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(gamestate==false) {
                    String[] difficulties = {"Easy", "Normal", "Hard", "ARE YOU MAD"};
                    difficultystring = (String) JOptionPane.showInputDialog(
                            title, "Select your mode", "Change Difficulty and Restart", JOptionPane.QUESTION_MESSAGE, null, difficulties, difficultystring);
                    board.setVisible(false);
                    board=new GameBoard();
                    add(board);
                    board.setVisible(true);
                }
            }
        });

        //action event for the board size change button. Lists options and upon choice, gameboard is refreshed.
        boardsizechange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(gamestate==false) {
                    String[] boardsizes = {"Small", "Normal", "Large"};
                    setGamesize( (String) JOptionPane.showInputDialog(
                            title, "Select the board size", "Change Board and Restart", JOptionPane.QUESTION_MESSAGE, null, boardsizes, boardsizestring));
                    board.setVisible(false);
                    board=new GameBoard();
                    add(board);
                    board.setVisible(true);
                }

            }
        });

        //start pause clear button. start game on startup, pause is shown when game in progress. Then resume is shown when paused. once game complete clear board is shown.
        startbut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(startbut.getText()=="Clear Grid"){
                    startbut.setText("Start Game");
                    gamestate=false;
                }
                if(gamestate==false) {
                    if(!GameBoard.game){
                        board.setVisible(false);
                        board=new GameBoard();
                        add(board);
                        board.setVisible(true);
                        revalidate();
                        repaint();
                    }
                    else {
                        startbut.setText("Pause Game");
                        gamestate = true;
                        board.run();
                    }
                }
                else{
                    startbut.setText("Resume Game");
                    gamestate=false;
                    board.stop();
                }
            }
        });
    }

    //sets the gamesize.
    public void setGamesize(String s){
        if(s!=boardsizestring){
            if (s=="Small"){
                gamesize= new int[]{16, 8};
                boardsizestring = s;
            }
            else if (s=="Normal"){
                gamesize= new int[]{21, 12};
                boardsizestring = s;
            }
            else if (s=="Large"){
                gamesize= new int[]{31, 22};
                boardsizestring = s;
            }
        }
    }

}