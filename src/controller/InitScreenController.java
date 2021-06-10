package controller;

import expert.PrologJavaRunner;
import gui.InitScreen;
import gui.MainScreen;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class providing the <b>controller</b> for the processes related to <b>getting the personal details
 * of the patient</b> (name, age, and sex).
 */
public class InitScreenController implements ActionListener, DocumentListener {
    /* Graphical user interface for the initialization screen */
    private InitScreen scr;
    /* Expert system featuring a Java interface and a Prolog knowledge base */
    private PrologJavaRunner expert;

    /**
     * Creates a controller object with the initialization screen GUI and the expert system as parameters
     *
     * @param scr graphical user interface for the initialization screen
     * @param expert expert system featuring a Java interface and a Prolog knowledge base
     */
    public InitScreenController(InitScreen scr, PrologJavaRunner expert) {
        this.expert = expert;
        this.scr = scr;

        scr.setActionListener(this);
        scr.setDocumentListener(this);
    }

    /**
     * Invoked when an action occurs
     *
     * @param e semantic event indicative that a component-defined action occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Male")) {              /* The patient is male. */
            expert.setSex("Male");
            scr.enableStart();
        } else if (e.getActionCommand().equals("Female")) {     /* The patient is female. */
            expert.setSex("Female");
            scr.enableStart();
        } else if (e.getActionCommand().equals("Start")) {      /* The user is ready to begin the probing. */
            expert.setName(scr.getName());

            String ageStr;
            ageStr = scr.getAge();

            /* Check if the input is a nonnegative number. */
            if (checkInput(ageStr)) {
                expert.setAge(Double.parseDouble(ageStr));

                scr.setVisible(false);

                /* Launch the main screen of the expert system. */
                MainScreen mainScr;
                mainScr = new MainScreen(expert.getName(), expert.getAge(), expert.getSex());

                MainScreenController mainScrCtrl;
                mainScrCtrl = new MainScreenController(mainScr, expert);
            } else {
                /* Clear the invalid input. */
                scr.clearAge();
            }
        }
    }

    /**
     * Gives notification that an attribute or set of attributes changed
     *
     * @param e document event corresponding to a document change
     */
    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    /**
     * Gives notification that there was an insert into the document
     *
     * @param e document event corresponding to a document change
     */
    @Override
    public void insertUpdate(DocumentEvent e) {

    }

    /**
     * Gives notification that a portion of the document has been removed
     *
     * @param e document event corresponding to a document change
     */
    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    /**
     * Returns <code>true</code> if the patient's age, as supplied by the user, is a nonnegative number;
     * <code>false</code>, otherwise
     *
     * @param ageStr age of the patient as entered through the GUI
     * @return <code>true</code> if the patient's age, as supplied by the user, is a nonnegative number;
     * <code>false</code>, otherwise
     */
    public boolean checkInput(String ageStr) {
        double age;             // age of the patient (parsed as a floating-point)

        /* Check if the age supplied is numeric. */
        try {
            age = Double.parseDouble(ageStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Input a valid number for the patient's age",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        /* If the age is numeric, check if it is nonnegative. */
        if (age < 0) {
            JOptionPane.showMessageDialog(null, "Input a valid number for the patient's age",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        /* Return true if the age supplied is valid. */
        return true;
    }
}
