import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Created by Ben Higgs (1403099) for the Application programming assignment part 2.
 */
public class CE203_2015_Ex2 extends JApplet{
    //must all be declared outside init so they can be changed or cleared from another class.
    JTextField input = new JTextField(25); //user input
    LinkedList<String> wordlist = new LinkedList(); //init linkedlist wordlist
    JTextArea panel2 = new JTextArea("Enter a word and add it to start a list \n\nThis was created by Ben Higgs (1403099)");  //this is the text panel.



    public void init() {
        JPanel panel1 = new JPanel();
        JPanel panel3 = new JPanel();

        JButton add = new JButton("Add");
        JButton searchl = new JButton("Search via letter");
        JButton searchw = new JButton("Search for word");
        JButton remove = new JButton("Delete one word");
        JButton removeall = new JButton("Delete all word");
        JButton clear = new JButton("Empty List");

        GridLayout p3layout = new GridLayout(2,3);
        panel3.setLayout(p3layout);

        panel1.add(input);
        panel3.add(add);
        panel3.add(searchl);
        panel3.add(searchw);
        panel3.add(remove);
        panel3.add(removeall);
        panel3.add(clear);

        add.addActionListener(new ButtonListener(this, 'a'));
        searchl.addActionListener(new ButtonListener(this, 'f'));
        searchw.addActionListener(new ButtonListener(this, 's'));
        remove.addActionListener(new ButtonListener(this, 'r'));
        removeall.addActionListener(new ButtonListener(this, 'd'));
        clear.addActionListener(new ButtonListener(this, 'c'));


        //panel2 set line wrap and scroll to stop text exiting the screen and program becoming unusable
        panel2.setFont(new Font("Verdana", Font.BOLD, 14));
        panel2.setLineWrap(true);
        panel2.setWrapStyleWord(true);
        panel2.setEditable(false);
        JScrollPane scrollingpanel2 = new JScrollPane(panel2);

        add(panel1, BorderLayout.NORTH);
        add(scrollingpanel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);

    }
}




//actionlistener class, this handles all the methods related to editing the word list and textarea
class ButtonListener implements ActionListener {
    CE203_2015_Ex2 theApplet; //the applet is refered to throughout to access and charge vars
    char act; //action of the button saved as char

    ButtonListener (CE203_2015_Ex2 app, char c){
        theApplet=app;
        act=c;
    }

    //switch deals with choosing the right actions based off what button is pressed.
    public void actionPerformed(ActionEvent e) {
        String input=theApplet.input.getText();
        switch (act){
            case 'a'://add
                if(wordcheck(input)==true){theApplet.wordlist.add(input);theApplet.panel2.setText("Word ' "+input+" '"+" has been added to the list");}
                else{theApplet.panel2.setText("The string ' " + input + " '" + " was not added as it is not a valid word");}
                break;

            case 'f'://find via single character
                if(wordcheck(input)==true){
                    if(input.length()>1){theApplet.panel2.setText("The search term is invalid, enter a single character");}
                    else{ String s = find1(input);theApplet.panel2.setText("Words beginning with character " + input.charAt(0)+" : " + s );}}
                else{theApplet.panel2.setText("The search term is invalid, enter a valid single character only");}
                break;

            case 's'://search for word. If length of input is zero, output length of list
                if(wordcheck(input)==true){int i = find2(input);if(input.length()==0){theApplet.panel2.setText("Size of the current word list : " + i);}else {theApplet.panel2.setText("Occurrences of the word " + input +" : " + i );}}
                else{theApplet.panel2.setText("The search term is invalid, enter a valid word");}
                break;

            case 'r'://remove first occurrence
                if(wordcheck(input)==true){
                    if(theApplet.wordlist.removeFirstOccurrence(input)){theApplet.panel2.setText("The first occurrence of ' " + input + " ' was removed");}
                    else{theApplet.panel2.setText("The word ' "+input+" ' is not in the list");}
                }
                else{theApplet.panel2.setText("The entered word is invalid, enter a valid word");}
                break;

            case 'd'://remove all occurrences
                if(wordcheck(input)==true){
                    if(removeall(input)){theApplet.panel2.setText("All occurrences of the word " + input +" were removed"  );}
                    else{theApplet.panel2.setText("Occurrences of the word " + input +" were not removed, try again" );}
                }
                else{theApplet.panel2.setText("The search term is invalid, enter a valid word");}
                break;

            case 'c'://clear list
                if(theApplet.wordlist.size()>0){theApplet.wordlist.clear();theApplet.panel2.setText("The list of words was cleared");}
                else{theApplet.panel2.setText("The word list is already empty");}
                break;

        }
        theApplet.input.setText(null);//set the text input as blank after every button press
    }


    //checks the entered word using regex. Checks the pattern of the word starts with lowercase or uppercase and then contains only lowercase or uppercase or - or _ chars
    public boolean wordcheck(String i){
        if(!(Pattern.matches("[a-zA-Z]+[a-zA-Z-_]+", i))&&!(Pattern.matches("[a-zA-Z]", i))){
            if(act=='s'){return true;}//if searching for word, system has to return true and show list length
            else{return false;}
        }
        else{return true;}
        }


    //find via character, takes the first character and searches the wordlist for it. Appends each word to a string and a \n char.
    // These are handed back to the switch where its printed. If no words found, an error string is sent back instead.
    public String find1 (String i){
        char c = i.charAt(0);
        String o =" ";
        for (String s:theApplet.wordlist){
            if (Character.toLowerCase(s.charAt(0))==c||Character.toUpperCase(s.charAt(0))==c){ //this makes the search case insensitive
                o=o+"\n"+s;
            }
        }
        if (o==" "){
            o="0";
        }
        return o;
    }


    //find all occurrences of words. searches wordlist and returns an int, number of all occurrences. If length of input is zero, returns size of list
    public int find2 (String i){
        if(i.length()==0){return theApplet.wordlist.size();}
        int o = 0 ;
        for (String s:theApplet.wordlist){
            if (s.equalsIgnoreCase(i)){ //this makes the search case insensitive. I believe that it should be case insensitive because words are the same regardless of their capitalisation.
                o++;
            }
        }
        return o;
    }


    //removes all occurences of a word. Takes string arg, while c=false, loops through removing the first occurrence until there are no more. then c= true and returns true. if error, will return false.
    public boolean removeall(String i){
        boolean c=false;
        if (theApplet.wordlist.contains(i)){
            while (c==false){
                if(!theApplet.wordlist.removeFirstOccurrence(i)){
                    c=true;
                }
            }
        }
        return c;
    }


}
