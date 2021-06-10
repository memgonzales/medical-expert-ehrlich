package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class implementing a window for <b>inputting the patient's name, age, and sex </b>
 */
public class InitScreen extends JFrame {

    /* Submission button */
    private JButton btnConfirm;

    /* Prompt asking for the patient's name */
    private JLabel lblInputName;
    /* Prompt asking for the patient's age */
    private JLabel lblInputAge;
    /* Prompt asking for the patient's sex */
    private JLabel lblInputSex;

    /* Text field for the patient's name */
    private JTextField tfInputName;
    /* Text field for the patient's age */
    private JTextField tfInputAge;

    /* Radio button for the male option */
    private JRadioButton btnMale;
    /* Radio button for the female option */
    private JRadioButton btnFemale;
    /* Button group for the two sex options */
    private ButtonGroup bgSexes;

    /**
     * Creates a window for inputting the patient's name, age, and sex
     */
    public InitScreen() {
        /* "EHRLICH: Autoimmune Diseases Medical Expert" is set as the window title */
        super("EHRLICH: Autoimmune Diseases Medical Expert");

        /* Use Border layout */
        setLayout(new BorderLayout());

        /* init() is called to initialize the elements of the window */
        init();

        /* Some formatting methods for the window are executed */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 235);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    /**
     * Initializes the elements comprising this window
     */
    private void init () {
        /* Panels used to arrange the window elements */
        JPanel pSouth;
        JPanel pCenter;
        JPanel pPrompts;
        JPanel pFields;
        JPanel pContent;
        JPanel pBuffer;

        /* The pSouth panel is formatted */
        pSouth = new JPanel();
        pSouth.setLayout(new FlowLayout());
        pSouth.setBackground(Color.pink);

        /* The pBuffer panel is formatted */
        pBuffer = new JPanel();
        pBuffer.setLayout(new FlowLayout());
        pBuffer.setBackground(Color.pink);

        /* The confirmatory button is created */
        btnConfirm = new JButton("Start");

        /* The user is not allowed to click the button if complete details have not been entered. */
        btnConfirm.setEnabled(false);

        /* The button is added to pSouth */
        pSouth.add(btnConfirm);

        /* The patient prompts are initialized */
        lblInputName = new JLabel("Enter your name: ");
        lblInputName.setBorder(new EmptyBorder(0, 0, 15, 0));
        lblInputAge = new JLabel("Enter your age: ");
        lblInputAge.setBorder(new EmptyBorder(0, 0, 15, 0));
        lblInputSex = new JLabel("Enter your biological sex: ");
        lblInputSex.setBorder(new EmptyBorder(0, 0, 15, 0));

        /* The pCenter panel is formatted */
        pCenter = new JPanel();
        pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.Y_AXIS));
        pCenter.setBackground(Color.pink);
        pCenter.setBorder(new EmptyBorder(20, 20, 5, 20));

        /* The text fields for the patient's name and age are initialized */
        tfInputName = new JTextField(15);
        tfInputName.setPreferredSize(new Dimension(200, 8));
        tfInputAge = new JTextField(15);
        tfInputAge.setPreferredSize(new Dimension(200, 8));

        /* The grouped radio buttons for the patient's sex options are initialized */
        btnMale = new JRadioButton("Male");
        btnMale.setOpaque(false);
        btnFemale = new JRadioButton("Female");
        btnFemale.setOpaque(false);
        bgSexes = new ButtonGroup();
        bgSexes.add(btnMale);
        bgSexes.add(btnFemale);

        /* The pContent panel is formatted */
        pContent = new JPanel();
        pContent.setLayout(new BoxLayout(pContent, BoxLayout.X_AXIS));
        pContent.setPreferredSize(new Dimension(400, 15));
        pContent.setOpaque(false);
        pContent.setBorder(new EmptyBorder(0, 0, 5, 0));

        /* The pPrompts panel is formatted */
        pPrompts = new JPanel();
        pPrompts.setLayout(new BoxLayout(pPrompts, BoxLayout.Y_AXIS));
        pPrompts.setPreferredSize(new Dimension(200, 15));
        pPrompts.setOpaque(false);

        /* The three patient prompts are added to pPrompts */
        pPrompts.add(lblInputName);
        pPrompts.add(lblInputAge);
        pPrompts.add(lblInputSex);

        /* The pFields panel is formatted */
        pFields = new JPanel();
        pFields.setLayout(new BoxLayout(pFields, BoxLayout.Y_AXIS));
        pFields.setPreferredSize(new Dimension(200, 15));
        pFields.setOpaque(false);

        /* The text fields and radio buttons for the patient input are added to pFields */
        pFields.add(tfInputName);
        pFields.add(tfInputAge);
        pFields.add(btnMale);
        pFields.add(btnFemale);

        /* pContents and pFields are added to pContent */
        pContent.add(pPrompts);
        pContent.add(pFields);

        /* pContent, pBuffer, and pSouth are added to pCenter */
        pCenter.add(pContent);
        pCenter.add(pBuffer);
        pCenter.add(pSouth);

        /* pCenter is positioned in the overall border layout of the window */
        add(pCenter, BorderLayout.CENTER);
    }

    /**
     * Returns the name inputted by the patient
     */
    public String getName() {
        /* Returns the contents of the first text field (patient name) */
        return tfInputName.getText();
    }

    /**
     * Returns the age inputted by the patient
     *
     * @return age inputted by the patient
     */
    public String getAge() {
        /* Returns the contents of the second text field (patient age) */
        return tfInputAge.getText();
    }

    /**
     * Enables the confirmatory button
     */
    public void enableStart() {
        /* Set the confirmatory button to enabled */
        btnConfirm.setEnabled(true);
    }

    /**
     * Clears the contents of the text field for age
     */
    public void clearAge() {
        tfInputAge.setText("");
    }

    /**
     * Sets the action listener for this graphical user interface
     *
     * @param listener action listener receiving action events
     */
    public void setActionListener(ActionListener listener) {
        /* Action listeners for the two radio buttons and the confirmatory button are added */
        btnFemale.addActionListener(listener);
        btnMale.addActionListener(listener);
        btnConfirm.addActionListener(listener);
    }

    /**
     * Sets the document listener for this graphical user interface
     *
     * @param listener document listener receiving the patient's text input
     */
    public void setDocumentListener(DocumentListener listener) {
        /* Document listeners for the two text fields are added */
        tfInputAge.getDocument().addDocumentListener(listener);
        tfInputName.getDocument().addDocumentListener(listener);
    }
}

