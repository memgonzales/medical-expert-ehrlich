import controller.InitScreenController;
import expert.PrologJavaRunner;
import gui.InitScreen;

/**
 * Class for activating <b>EHRLICH (Enhanced Rule- and Logic-based Immunological Consultative Hub)</b>,
 * the medical expert system with interface written in Java and knowledge base written in Prolog
 */
public class ExpertSystem {
    /**
     * Empty constructor
     */
    public ExpertSystem() {

    }

    /**
     * Activates the medical expert system
     *
     * @param args array of command-line arguments
     */
    public static void main(String[] args) {
        PrologJavaRunner connector;
        connector = new PrologJavaRunner();

        InitScreen initScr;
        initScr = new InitScreen();

        InitScreenController ctrl;
        ctrl = new InitScreenController(initScr, connector);
    }
}
