import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Ben Higgs (1403099) for the Application programming assignment part 1.
 */
public class CE203_2015_Ex1 extends JApplet{
    JTextField rgb1; //field for rgb val 1 - so we can edit vals outside of init
    JTextField rgb2; //field for rgb val 2 - so we can edit vals outside of init
    JTextField rgb3; //field for rgb val 3 - so we can edit vals outside of init
    Color col = Color.GREEN; //colour object  - so we can edit vals outside of init
    char error ='s'; //char used for error checking, changes between 3 states, 's','f','t'. S is start state, f is fine(colour vals were fine), 't' is for error so red is shown.

    //init function for applet. contains all necessary settings and layout constructors.
    public void init() {
        JPanel panel1 = new JPanel();
        JPanel panel3= new JPanel();

        Panel panel2 = new Panel(this);
        add(panel2, BorderLayout.CENTER);

        rgb1 = new JTextField(3);
        rgb2 = new JTextField(3);
        rgb3 = new JTextField(3);
        JButton genbut = new JButton("Generate");
        JButton resbut = new JButton("Reset");

        panel1.add(rgb1);
        panel1.add(rgb2);
        panel1.add(rgb3);
        panel1.add(genbut);
        panel3.add(resbut);

        genbut.addActionListener(new ButtonHandler(this,'g'));
        resbut.addActionListener(new ButtonHandler(this,'r'));

        add(panel1, BorderLayout.NORTH);
        add(panel3, BorderLayout.SOUTH);
    }
}

//handles button clicks and changes object colour accordingly.
class ButtonHandler implements ActionListener {
    CE203_2015_Ex1 theApplet; //theapplet object initiated
    char act; //action character, denotes which button was clicked and which option to process.

    //constructor for ButtonHandler object
    ButtonHandler(CE203_2015_Ex1 app, char c) {
        theApplet = app;
        act = c;
    }

    //chooses which button click event was chosen and starts processing accordingly
    public void actionPerformed(ActionEvent e) {
        if (act == 'g'){
            if (checkvals()==0){changec();theApplet.error='f';}
        }
        else{
            theApplet.error='s';
            theApplet.rgb1.setText("");
            theApplet.rgb2.setText("");
            theApplet.rgb3.setText("");
            theApplet.repaint();
        }
    }


    //checks the values of the RGB fields. each field is processed and exceptions are thrown and caught. If an error occurs it is dealt with
    public int checkvals(){
        int check=0;
        try{ checknum(theApplet.rgb1);}
        catch (NumberFormatException nex){theApplet.rgb1.setText("");check++;}

        try{checknum(theApplet.rgb2);}
        catch (NumberFormatException nex){theApplet.rgb2.setText("");check++;}

        try{checknum(theApplet.rgb3);}
        catch (NumberFormatException nex){theApplet.rgb3.setText("");check++;}

        theApplet.error='t';//sets state to no errors
        theApplet.repaint(); //repaints text panel
        return check; //returns boolean
    }

    //checks the numbers are in range. If they aren't, the values are changed. Also throws exceptions if it cannot parse the int, these are caught in the checkvals method.
    public void checknum(JTextField i){
        int k = Integer.parseInt(i.getText());
        if (k > 255){i.setText("255");}
        else if (k<0){i.setText("150");}
    }


    //change colour method. This is where the values are taken and the colour object in theApplet is changed accordingly.
    public void changec(){
        int[] cs = new int[3];
        cs[0]=Integer.parseInt((theApplet.rgb1.getText()));
        cs[1]=Integer.parseInt((theApplet.rgb2.getText()));
        cs[2]=Integer.parseInt((theApplet.rgb3.getText()));

        theApplet.col=(new Color(cs[0],cs[1],cs[2]));
        theApplet.repaint();
    }

}


//this is the text panel. It uses drawstring methods to display the text on screen.
class Panel extends JPanel{
    CE203_2015_Ex1 theApplet; //the applet object is created.

    //Constructor for panel. theApplet now references the actual applet
    Panel(CE203_2015_Ex1 app){
        theApplet=app;
    }

    //paint component function deals with the graphics using the Graphics object
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setFont(new Font("Veranda", Font.BOLD, 24)); //set a font scheme for g
        if(theApplet.error=='t'){ //if error is found
            g.setColor(Color.RED); //red font
            g.drawString("You entered an incorrect", 15, 80);
            g.drawString("RGB value. Please try again!", 15, 130);
        }
        else if(theApplet.error=='s'){ //is reset function or the app has just started
            g.setColor(Color.GREEN); //green font
            g.drawString("Welcome to CE203 Assignment 1", 15, 80);
            g.drawString("Submitted by:Ben Higgs 1403099", 15, 130);
        }
        else{ //the only other state is fine.
            g.setColor(theApplet.col); //user assigned font colour
            g.drawString("Ben Higgs", 140, 80);
            g.drawString("1403099", 150, 130);
        }
    }
}