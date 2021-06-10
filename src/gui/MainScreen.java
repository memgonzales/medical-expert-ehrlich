package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

/**
 * Class implementing the <b>main window</b> of the system, displaying the patient's
 * details, the current inquiry, and the diagnosis log and status.
 */
public class MainScreen extends JFrame {

    /* Text areas and scroll panes for the inquiry, status, and log displays and the user response */
    private JTextArea taEmergency;
    private JTextArea taLog;
    private JScrollPane scrLog;
    private JTextArea taInquiry;
    private JScrollPane scrInquiry;
    private JTextField tfResponse;

    /* Buttons for user response and for restarting the diagnosis process */
    private JButton btnSubmit;
    private JButton btnYes;
    private JButton btnNo;
    private JButton btnRestart;

    /* Labels used to display the patient details and the logo of the diagnosis system */
    private JLabel lblName;
    private JLabel lblAge;
    private JLabel lblSex;
    private JLabel lblLogo;

    /**
     * Creates the main window of the system, displaying the patient's
     * details, the current inquiry, and the diagnosis log and status
     *
     * @param name the patient's name
     * @param age the patient's age
     * @param sex the patient's biological sex (either male or female)
     */
    public MainScreen(String name, double age, String sex) {

        /* "EHRLICH: Autoimmune Diseases Medical Expert" is used as the window title*/
        super("EHRLICH: Autoimmune Diseases Medical Expert");

        /* Use Border layout */
        setLayout(new BorderLayout());

        /* init() is called to initialize the elements of the window */
        init(name, age, sex);

        /* Additional formatting methods for the window are executed */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    /**
     * Initializes the elements comprising this window
     */
    private void init (String name, double age, String sex) {
        JPanel pAll;            // panel holding all window elements
        JPanel pCenter;         // panel holding the patient details, current inquiry and patient response components
        JPanel pInfo;           // panel holding the patient details
        JPanel pInquiry;        // panel holding the current inquiry
        JPanel pResponse;       // panel holding the user's text-based response components
        JPanel pRight;          // panel holding the diagnosis status and diagnosis log
        JPanel pButtons;        // panel holding the YES and NO buttons for the user response
        JPanel pFirstPart;      // panel holding the patient details and diagnosis system logo
        JPanel pEmergency;      // panel holding the diagnosis status
        JPanel pRestart;        // panel holding the RESTART button

        /* The pAll panel is initialized */
        pAll = new JPanel();
        pAll.setLayout(new BorderLayout());
        pAll.setBorder(new EmptyBorder(10, 10, 10, 10));
        pAll.setBackground(Color.pink);

        /* The pCenter panel is initialized */
        pCenter = new JPanel();
        pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.Y_AXIS));
        pCenter.setBorder(new EmptyBorder(0, 0, 0, 0));
        pCenter.setBackground(Color.pink);

        /* The pInfo panel is initialized */
        pInfo = new JPanel();
        pInfo.setLayout(new GridLayout(3, 1));
        pInfo.setBorder(new EmptyBorder(5, 10, 5, 0));
        pInfo.setOpaque(false);

        /* The patient details (name, age, and sex) are initialized using the
         * function input and are added to pInfo
         */
        lblName = new JLabel("NAME:  " + name);
        lblAge = new JLabel("AGE:     " + age);
        lblSex = new JLabel("SEX:     " + sex);
        pInfo.add(lblName);
        pInfo.add(lblAge);
        pInfo.add(lblSex);

        /* setImage() is called to initialize the contents of lblLogo */
        lblLogo = setImage("assets/ehrlich.png");

        /* The pFirstPart panel is initialized */
        pFirstPart = new JPanel();
        pFirstPart.setLayout(new GridLayout(1, 2));
        pFirstPart.setBorder(new EmptyBorder(5, 0, 5, 0));
        pFirstPart.setOpaque(false);

        /* pInfo and lblLogo (holding the logo of the diagnosis system) are added to pFirstPart */
        pFirstPart.add(pInfo);
        pFirstPart.add(lblLogo);

        /* pFirstPart is added to pCenter */
        pCenter.add(pFirstPart);

        /* The pInquiry panel is initialized */
        pInquiry = new JPanel();
        pInquiry.setLayout(new BorderLayout());
        pInquiry.setBorder(new EmptyBorder(5, 0, 5, 0));
        pInquiry.setOpaque(false);

        /* The text area for displaying the current inquiry is initialized and
         * placed in a scroll pane
         */
        taInquiry = new JTextArea();
        taInquiry.setEditable(false);
        taInquiry.setLineWrap(true);
        taInquiry.setWrapStyleWord(true);
        taInquiry.setBorder(new EmptyBorder(5, 5, 5, 5));
        scrInquiry = new JScrollPane(taInquiry);
        scrInquiry.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrInquiry.setPreferredSize(new Dimension(300, 115));

        /* The scroll pane for displaying the current inquiry is added to pInquiry,
         * and pInquiry is in turn added to pCenter
         */
        pInquiry.add(scrInquiry);
        pCenter.add(pInquiry);

        /* The pResponse panel is initialized */
        pResponse = new JPanel();
        pResponse.setLayout(new BoxLayout(pResponse, BoxLayout.X_AXIS));
        pResponse.setBorder(new EmptyBorder(20, 5, 23, 5));
        pResponse.setBackground(Color.pink);

        /* The text field for the user's text-based response and its corresponding SUBMIT button
         * are initialized
         */
        tfResponse = new JTextField();
        tfResponse.setSize(new Dimension(20, 5));
        btnSubmit = new JButton("Submit");
        btnSubmit.setSize(new Dimension(20, 8));

        /* The user response text field and button are added to pResponse, and pResponse is in
         * turn added to pCenter
         */
        pResponse.add(tfResponse);
        pResponse.add(btnSubmit);
        pCenter.add(pResponse);

        /* The pButtons panel is initialized */
        pButtons = new JPanel();
        pButtons.setLayout(new GridLayout(1, 2));

        /* The YES and NO buttons for the user response are initialized */
        btnYes = new JButton("Yes");
        btnNo = new JButton("No");

        /* The two buttons are added to pButtons, and pButtons is in turn added to pCenter */
        pButtons.add(btnYes);
        pButtons.add(btnNo);
        pCenter.add(pButtons);

        /* The pRight panel is initialized */
        pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setBorder(new EmptyBorder(0, 10, 0, 0));
        pRight.setBackground(Color.pink);

        /* The pEmergency panel is initialized */
        pEmergency = new JPanel();
        pEmergency.setLayout(new BorderLayout());
        pEmergency.setBorder(new EmptyBorder(0, 0, 10, 0));
        pEmergency.setBackground(Color.pink);

        /* The text area holding the diagnosis status is initialized */
        taEmergency = new JTextArea();
        taEmergency.setBorder(new EmptyBorder(10, 10, 10, 10));
        taEmergency.setEditable(false);
        taEmergency.setLineWrap(true);
        taEmergency.setWrapStyleWord(true);
        taEmergency.setPreferredSize(new Dimension(300, 20));

        /* The text area for the diagnosis status is added to pEmergency, and pEmergency
         * is in turn added to pRight */
        pEmergency.add(taEmergency);
        pRight.add(pEmergency);

        /* The text area holding the diagnosis log is initialized and stored in a scroll pane */
        taLog = new JTextArea("");
        taLog.setEditable(false);
        taLog.setLineWrap(true);
        taLog.setWrapStyleWord(true);
        taLog.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrLog = new JScrollPane(taLog);
        scrLog.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrLog.setPreferredSize(new Dimension(300, 235));

        /* The scroll pane for the diagnosis log is added to pRight */
        pRight.add(scrLog);

        /* The pRestart panel is initialized */
        pRestart = new JPanel();
        pRestart.setLayout(new GridLayout(1, 1));
        pRestart.setBorder(new EmptyBorder(10, 0, 0, 0));
        pRestart.setBackground(Color.pink);

        /* The RESTART button is initialized */
        btnRestart = new JButton("Restart");

        /* The button is added to pRestart, and pRestart is in turn added to pRight */
        pRestart.add(btnRestart);
        pRight.add(pRestart);

        /* pCenter and pRight are added to pAll */
        pAll.add(pCenter, BorderLayout.CENTER);
        pAll.add(pRight, BorderLayout.EAST);

        /* pAll is added to the overall border layout */
        add(pAll, BorderLayout.CENTER);
    }

    /**
     * Returns a scaled version of an image given its path
     *
     * @param imagePath path to the image to be scaled
     * @return scaled version of the image
     */
    private JLabel setImage (String imagePath) {
        /* Stores the original image */
        ImageIcon imageOrig;
        /* Stores a scaled version of the image */
        Image imageScaled;
        /* Stores a formatted version of the image to be displayed on the window */
        JLabel lblImage;

        /* The original image is retrieved and scaled */
        imageOrig = new ImageIcon(getClass().getResource(imagePath));
        imageScaled = imageOrig.getImage().getScaledInstance(170, 110, Image.SCALE_DEFAULT);

        /* The scaled image is converted to a JLabel, further formatted, and returned */
        lblImage = new JLabel(new ImageIcon(imageScaled));
        lblImage.setBackground(Color.white);
        lblImage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lblImage.setBorder(new EmptyBorder(0, 0, 0, 0));
        return lblImage;
    }

    /**
     * Updates the diagnosis log
     *
     * @param addedInquiry the diagnosis details of the latest inquiry
     */
    public void updateHistory(String addedInquiry) {
        /* addedInquiry is appended to the current diagnosis log */
        taLog.append(addedInquiry + "\n");
    }

    /**
     * Updates the user inquiry
     *
     * @param currentInquiry the latest user inquiry
     */
    public void updateInquiry(String currentInquiry) {
        /* The previous user inquiry is replaced with currentInquiry */
        taInquiry.setText(currentInquiry);

        /* The caret position of the text area is set to 0 to negate the
         * automatic scroll feature for long inquiries
         */
        taInquiry.setCaretPosition(0);
    }

    /**
     * Updates the diagnosis status
     *
     * @param status the current diagnosis status (i.e., whether or not there is an emergency)
     */
    public void updateEmergency(String status) {
        /* The previous diagnosis status is replaced with the passed status*/
        taEmergency.setText(status);
    }

    /**
     * Retrieves the user's text-based response
     *
     * @return text-based response of the user
     */
    public String getResponse() {
        /* The contents of the user response text field are returned */
        return tfResponse.getText();
    }

    /**
     * Resets the text field for the user response
     */
    public void clearResponse() {
        /* The user response text field is set to blank */
        tfResponse.setText("");
    }

    /**
     * Enables the YES and NO buttons for the user response
     *
     * @param enabled whether or not the buttons are to be enabled
     */
    public void setBtnYesNoEnabled(boolean enabled) {
        /* The two buttons are either enabled or disabled depending on the passed parameter */
        btnYes.setEnabled(enabled);
        btnNo.setEnabled(enabled);
    }

    /**
     * Enables the SUBMIT button for the user's text-based response
     *
     * @param enabled whether or not the button is to be enabled
     */
    public void setBtnSubmit(boolean enabled) {
        /* The button is either enabled or disabled depending on the passed parameter */
        btnSubmit.setEnabled(enabled);
    }

    /**
     * Enables the text field for the user's text-based response
     *
     * @param enabled whether or not the button is to be enabled
     */
    public void setTextEnabled(boolean enabled) {
        /* The text field is either enabled or disabled depending on the passed parameter */
        tfResponse.setEditable(enabled);
    }

    /**
     * Sets the action listener for this graphical user interface
     *
     * @param listener action listener receiving action events
     */
    public void setActionListener(ActionListener listener) {
        /* Action listeners are added for the NO, YES, SUBMIT, and RESTART buttons */
        btnNo.addActionListener(listener);
        btnYes.addActionListener(listener);
        btnSubmit.addActionListener(listener);
        btnRestart.addActionListener(listener);
    }

    /**
     * Sets the document listener for this graphical user interface
     *
     * @param listener document listener receiving document events
     */
    public void setDocumentListener(DocumentListener listener) {
        /* A document listener is added to the user response text field */
        tfResponse.getDocument().addDocumentListener(listener);
    }

    /**
     * Sets the window listener for this graphical user interface
     *
     * @param listener window listener receiving window events
     */
    public void setWindowListener(WindowListener listener) {
        /* A window listener is added to the window */
        addWindowListener(listener);
    }
}

