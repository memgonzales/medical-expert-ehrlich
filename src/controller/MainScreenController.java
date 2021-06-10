package controller;

import expert.PrologJavaRunner;
import gui.InitScreen;
import gui.MainScreen;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Class providing the <b>controller</b> for the processes related to the main screen of the system
 * (namely, the probing and diagnosis per se, together with the display of the technical logs)
 */
public class MainScreenController implements ActionListener, DocumentListener, WindowListener {
    /* Graphical user interface for the main screen */
    private MainScreen scr;
    /* Expert system featuring a Java interface and a Prolog knowledge base */
    private PrologJavaRunner expert;

    /* Number of times the patient has responses to a probe (used for proper display of logs) */
    private int clickCtr = 0;
    /* Number of times the patient has reported a symptom indicative of an emergency
    (used for proper display of logs)
     */
    private int emergencyCtr = 0;

    /**
     * Creates a controller object with the main screen GUI and the expert system as parameters
     *
     * @param scr graphical user interface for the main screen
     * @param expert expert system featuring a Java interface and a Prolog knowledge base
     */
    public MainScreenController(MainScreen scr, PrologJavaRunner expert) {
        this.expert = expert;
        this.scr = scr;

        scr.setActionListener(this);
        scr.setDocumentListener(this);
        scr.setWindowListener(this);

        /* Load and consult the Prolog knowledge base. */
        if (expert.loadKnowledgeBase("src/expert/KnowledgeBase.pl")) {
            scr.updateEmergency("Prolog knowledge base has been loaded.\n" +
                    "EHRLICH is ready for diagnosis.");

            /* Initialize the constants from the knowledge base. */
            expert.loadConstantsFromKB();

            /* Remove female-specific symptoms if applicable. */
            if (expert.removeFemaleSymptoms()) {
                scr.updateHistory("Female-specific symptoms removed:\n" +
                        "- Irregular menstruation\n" +
                        "- Vaginal dryness\n");
            }

            /* Remove pediatric symptoms if applicable. */
            if (expert.removeChildSymptoms()) {
                scr.updateHistory("Pediatric symptom removed:\n" +
                        "- Failure to thrive\n");
            }

            /* Display the first probing question, the response to which is guaranteed to either be
            a yes or a no.
             */
            scr.updateInquiry(expert.displayInquiry());
            scr.setTextEnabled(false);
            scr.setBtnSubmit(false);

        } else {        /* The specified Prolog knowledge base does not exist. */
            scr.updateEmergency("Failed to load knowledge base.\n" +
                    "Please double check if Prolog file exists.");
            scr.setBtnSubmit(false);
            scr.setBtnYesNoEnabled(false);
        }
    }

    /**
     * Invoked when the user attempts to close the window from the window's system menu
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowClosing(WindowEvent e) {
        int dialogButton;
        int dialogResult;

        dialogButton = JOptionPane.YES_NO_OPTION;
        dialogResult = JOptionPane.showConfirmDialog(scr, "Are you sure you want to exit?",
                "EHRLICH - Quit", dialogButton);

        if (dialogResult == JOptionPane.YES_OPTION) {
            scr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            scr.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }

    /**
     * Invoked when the Window is set to be the active Window
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowActivated(WindowEvent e) {

    }

    /**
     * Invoked when a window has been closed as the result of calling dispose on the window
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowClosed(WindowEvent e) {

    }

    /**
     * Invoked when a window is no longer the active window
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a minimized to a normal state
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a normal to a minimized state
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowIconified(WindowEvent e) {

    }

    /**
     * Invoked when a window is made visible
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * Invoked when an action occurs
     *
     * @param e semantic event indicative that a component-defined action occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Yes")) {       /* The patient reports experiencing the symptom. */
            /* Update the log to reflect that the diagnosis is ongoing. */
            if (clickCtr == 0) {
                scr.updateEmergency("Diagnosis is ongoing.\n" +
                        "Knowledge base has " + expert.NUM_DISEASES + " autoimmune diseases.");
                clickCtr++;
            }

            /* Execute the pertinent back-end processes. */
            expert.setAns("yes");
            expert.updateCF(expert.getSymptom(), expert.getAns(), expert.getAge(), expert.getI());
            expert.deleteSymptom();

            /* Execute the pertinent front-end processes. */
            displayEmergency();
            scr.updateHistory(expert.displayCFvals(expert.getSymptom()));

            /* Move to the next disease if certainty factor drops below the threshold. */
            if (expert.isLowConfidence()) {
                try {
                    expert.moveToNextDisease();
                    scr.updateInquiry(expert.displayInquiry());

                    /* Check if the probing question is open-ended or dichotomous (yes or no). */
                    setQuestionNature(expert.getSymptom());

                } catch (Exception error2) {
                    /* The diseases in the knowledge base have been exhausted. Display the final diagnosis. */
                    displayDiagnosis();
                }

            } else {
                /* Move to the next symptom if certainty factor is still above the threshold. */
                try {
                    scr.updateInquiry(expert.displayInquiry());

                    /* Check if the probing question is open-ended or dichotomous (yes or no). */
                    setQuestionNature(expert.getSymptom());


                } catch (Exception error1) {
                    /* Check if the disease has reached the threshold for near-certain diagnosis. */
                    if (expert.isHighConfidence()) {
                        /* Display the final diagnosis. */
                        displayDiagnosis();
                    } else {
                        /* Move to the next disease since all symptoms have been exhausted. */
                        try {
                            expert.moveToNextDisease();
                            scr.updateInquiry(expert.displayInquiry());

                            /* Check if the probing question is open-ended or dichotomous (yes or no). */
                            setQuestionNature(expert.getSymptom());
                        } catch (Exception error2) {
                            /* The diseases in the knowledge base have been exhausted. Display the final diagnosis. */
                            displayDiagnosis();
                        }
                    }
                }
            }

        } else if (e.getActionCommand().equals("No")) {       /* The patient does not report experiencing the symptom. */
            /* Update the log to reflect that the diagnosis is ongoing. */
            if (clickCtr == 0) {
                scr.updateEmergency("Diagnosis is ongoing.\n" +
                        "Knowledge base has " + expert.NUM_DISEASES + " autoimmune diseases.");
                clickCtr++;
            }

            /* Execute the pertinent back-end processes. */
            expert.setAns("no");
            expert.updateCF(expert.getSymptom(), expert.getAns(), expert.getAge(), expert.getI());
            expert.deleteSymptom();

            /* Execute the pertinent front-end processes. */
            displayEmergency();
            scr.updateHistory(expert.displayCFvals(expert.getSymptom()));

            /* Move to the next disease if certainty factor drops below the threshold. */
            if (expert.isLowConfidence()) {
                try {
                    expert.moveToNextDisease();
                    scr.updateInquiry(expert.displayInquiry());

                    /* Check if the probing question is open-ended or dichotomous (yes or no). */
                    setQuestionNature(expert.getSymptom());

                } catch (Exception error2) {
                    /* The diseases in the knowledge base have been exhausted. Display the final diagnosis. */
                    displayDiagnosis();
                }

            } else {
                /* Move to the next symptom if certainty factor is still above the threshold. */
                try {
                    scr.updateInquiry(expert.displayInquiry());

                    /* Check if the probing question is open-ended or dichotomous (yes or no). */
                    setQuestionNature(expert.getSymptom());
                } catch (Exception error1) {
                    /* Check if the disease has reached the threshold for near-certain diagnosis. */
                    if (expert.isHighConfidence()) {
                        /* Display the final diagnosis. */
                        displayDiagnosis();
                    } else {
                        /* Move to the next disease since all symptoms have been exhausted. */
                        try {
                            expert.moveToNextDisease();
                            scr.updateInquiry(expert.displayInquiry());

                            /* Check if the probing question is open-ended or dichotomous (yes or no). */
                            setQuestionNature(expert.getSymptom());
                        } catch (Exception error2) {
                            /* The diseases in the knowledge base have been exhausted. Display the final diagnosis. */
                            displayDiagnosis();
                        }
                    }
                }
            }

        } else if (e.getActionCommand().equals("Submit")) {       /* The patient inputs a detail regarding a vital sign. */
            /* Update the log to reflect that the diagnosis is ongoing. */
            if (clickCtr == 0) {
                scr.updateEmergency("Diagnosis is ongoing.\n" +
                        "Knowledge base has " + expert.NUM_DISEASES + " autoimmune diseases.");
                clickCtr++;
            }

            String responseStr;
            responseStr = scr.getResponse();

            /* Check if the input is a nonnegative integer. */
            if (checkResponse(responseStr)) {
                /* Back-end processes */
                expert.setAns(responseStr);
                expert.updateCF(expert.getSymptom(), expert.getAns(), expert.getAge(), expert.getI());
                expert.deleteSymptom();

                /* Front-end processes */
                scr.clearResponse();
                displayEmergency();
                scr.updateHistory(expert.displayCFvals(expert.getSymptom()));

                /* Move to the next disease if certainty factor drops below the threshold. */
                if (expert.isLowConfidence()) {
                    try {
                        expert.moveToNextDisease();
                        scr.updateInquiry(expert.displayInquiry());

                        /* Check if the probing question is open-ended or dichotomous (yes or no). */
                        setQuestionNature(expert.getSymptom());
                    } catch (Exception error2) {
                        /* The diseases in the knowledge base have been exhausted. Display the final diagnosis. */
                        displayDiagnosis();
                    }
                } else {
                    /* Move to the next symptom if certainty factor is still above the threshold. */
                    try {
                        scr.updateInquiry(expert.displayInquiry());

                        /* Check if the probing question is open-ended or dichotomous (yes or no). */
                        setQuestionNature(expert.getSymptom());
                    } catch (Exception error1) {
                        /* Check if the disease has reached the threshold for near-certain diagnosis. */
                        if (expert.isHighConfidence()) {
                            /* Display the final diagnosis. */
                            displayDiagnosis();
                        } else {
                            /* Move to the next disease since all symptoms have been exhausted. */
                            try {
                                expert.moveToNextDisease();
                                scr.updateInquiry(expert.displayInquiry());

                                /* Check if the probing question is open-ended or dichotomous (yes or no). */
                                setQuestionNature(expert.getSymptom());
                            } catch (Exception error2) {
                                /* The diseases in the knowledge base have been exhausted. Display the final diagnosis. */
                                displayDiagnosis();
                            }
                        }
                    }
                }
            } else {
                /* Clear the invalid input. */
                scr.clearResponse();
            }

        } else if (e.getActionCommand().equals("Restart")) {       /* The user restarts the system. */
            int dialogButton;
            int dialogResult;

            dialogButton = JOptionPane.YES_NO_OPTION;
            dialogResult = JOptionPane.showConfirmDialog(null, "This will erase the current patient's data. Proceed?",
                    "EHRLICH - Restart", dialogButton);

            /* Confirm if the user is intent on restarting the system since this will delete all the data
            from the current session.
             */
            if (dialogResult == JOptionPane.YES_OPTION) {
                scr.setVisible(false);

                /* Reset all the dynamically altered data in the knowledge base. */
                expert.unloadKnowledgeBase();

                /* Launch the initialization screen anew, along with all the necessary components
                for the next session.
                 */
                PrologJavaRunner connector;
                connector = new PrologJavaRunner();

                InitScreen initScr;
                initScr = new InitScreen();

                InitScreenController ctrl;
                ctrl = new InitScreenController(initScr, connector);
            }
        }
    }

    /**
     * Gives notification that there was an insert into the document
     *
     * @param e document event corresponding to a document change
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        scr.setBtnSubmit(true);
    }

    /**
     * Gives notification that a portion of the document has been removed
     *
     * @param e document event corresponding to a document change
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        scr.setBtnSubmit(true);
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
     * Enables and disables buttons on the screen depending on the nature of the question, given the current
     * symptom being probed
     *
     * <p>In particular, if the question is of a dichotomous nature, only the yes and no buttons are enabled.
     * If the question is open-ended (for example, it is asking for the body temperature or the heart rate),
     * then only the submit button is enabled.</p>
     *
     * @param symptom current symptom being probed
     */
    public void setQuestionNature(String symptom) {
        /* Open-ended question */
        if (symptom.equalsIgnoreCase("fever")
                || symptom.equalsIgnoreCase("slowHeartRate")) {
            scr.setTextEnabled(true);
            scr.setBtnYesNoEnabled(false);
            scr.setBtnSubmit(false);

        } else {        /* Dichotomous question */
            scr.setTextEnabled(false);
            scr.setBtnYesNoEnabled(true);
            scr.setBtnSubmit(false);
        }
    }

    /**
     * Displays the final diagnosis of the medical expert system
     */
    public void displayDiagnosis() {
        scr.updateInquiry(expert.getFinalDiagnosis());

        /* Disables all the buttons, except the restart button. */
        scr.setBtnYesNoEnabled(false);
        scr.setBtnSubmit(false);
        scr.setTextEnabled(false);

        scr.updateEmergency("Diagnosis is finished with confidence factor " +
                expert.getFinalCF() + "%.");
    }

    /**
     * Updates the log and displays a popup message should the patient report experiencing a symptom
     * that is indicative of an emergency
     */
    public void displayEmergency() {
        if (expert.getEmergency()) {
            emergencyCtr++;

            scr.updateEmergency("EMERGENCY! Medical attention required\n" +
                    "Knowledge base has 15 autoimmune diseases.\n");

            /* Display the pop-up only once. */
            if (emergencyCtr == 1) {
                JOptionPane.showMessageDialog(null, "EMERGENCY! Immediate medical attention is required.",
                        "EHRLICH - Emergency", JOptionPane.WARNING_MESSAGE);
            }

            /* This prevents the pop-up from being persistently displayed. */
            emergencyCtr++;
        }
    }

    /**
     * Returns <code>true</code> if the patient's vital sign, as supplied by the user, is a nonnegative number;
     * <code>false</code>, otherwise
     *
     * @param responseStr patient's vital sign, as supplied by the user
     * @return <code>true</code> if the patient's vital sign, as supplied by the user, is a nonnegative number;
     * <code>false</code>, otherwise
     */
    public boolean checkResponse(String responseStr) {
        double response;

        try {
            response = Double.parseDouble(responseStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Input a valid number for the patient's vital sign",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (response < 0) {
            JOptionPane.showMessageDialog(null, "Input a valid number for the patient's vital sign",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
