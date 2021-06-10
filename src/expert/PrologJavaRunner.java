package expert;

import java.lang.Integer;
import java.util.*;
import org.jpl7.*;
import org.jpl7.Query;
import java.io.File;

/**
 * Class implementing the <b>medical expert system</b> featuring an interface written in the object-oriented
 * language Java and a knowledge base written in the logic language Prolog.
 *
 * <p>Since the knowledge base is written in Prolog, a programming language characterized by its strong support
 * for logical rules and backward chaining, the bidirectional open-source library JPL is used to connect
 * the Java and Prolog components through the SWI Prolog Foreign Language Interface and the Java Native
 * Interface.</p>
 *
 * <p><a href = "https://jpl7.org/">JPL</a> is bundled with SWI Prolog since version 5.4. It is used in this
 * machine project in accordance with the terms in its Simplified BSD License. </p>
 */
public class PrologJavaRunner {
    /**
     * Number of diseases in the knowledge base
     */
    public int NUM_DISEASES;
    /**
     * Lower bound for the age of an adult
     */
    public int ADULT_AGE;
    /**
     * A body temperature (in degree Celsius) equal to or higher than this is considered an emergency.
     */
    public double FEVER_EMERGENCY_TEMP;
    /**
     * A heart rate equal to or slower than this is considered an emergency.
     */
    public double SLOW_HEART_RATE_EMERGENCY_RATE;
    /**
     * A body temperature (in degree Celsius) equal to or higher than this is indicative of fever.
     */
    public double FEVER_DIAGNOSIS_TEMP;
    /**
     * A heart rate equal to or slower tha this is indicative of slow heart rate (brachycardia) for a child.
     */
    public double SLOW_HEART_RATE_CHILD_DIAGNOSIS;
    /**
     * A heart rate equal to or slower tha this is indicative of slow heart rate (brachycardia) for an adult.
     */
    public double SLOW_HEART_RATE_ADULT_DIAGNOSIS;
    /**
     * A disease whose certainty factor falls below this value is ruled out.
     */
    public double CF_VALUE_REMOVE;
    /**
     * A disease that meets this certainty factor is immediately reported as the diagnosis.
     */
    public double CF_VALUE_CONCLUDE;

    /* Response of the user to the probing question */
    private String ans;
    /* Name of the patient */
    private String name;
    /* Age of the patient */
    private double age;
    /* Sex of the patient */
    private String sex;

    /* Index pertinent to the disease (relative to the list in the knowledge base) */
    private int i;
    /* Index pertinent to the symptom (relative to the list in the knowledge base) */
    private int j;

    /* Interim certainty factor */
    private double CFval;
    /* Final certainty factor (highest among those in the knowledge base) */
    private double finalCF;

    /* Current symptoms being considered. */
    private String[] currSymptomsParsed;

    /* Set to true if an emergency symptom has been reported; false, otherwise */
    private boolean emergency;

    /**
     * Constructor for the medical expert system featuring an interface written in the object-oriented language
     * Java and a knowledge base written in the logic language Prolog
     *
     * <p>Since initialization of attributes depends on the personal information of the patient, it is deferred
     * and delegated to the setters, particularly to <code>setName</code>, <code>setAge</code>, and
     * <code>setSex</code>.</p>
     */
    public PrologJavaRunner() {
        emergency = false;
    }

    /**
     * Returns the name of the patient
     *
     * @return name of the patient
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the age of the patient
     *
     * @return age of the patient
     */
    public double getAge() {
        return age;
    }

    /**
     * Returns the sex of the patient
     *
     * @return sex of the patient
     */
    public String getSex() {
        return sex;
    }

    /**
     * Returns the response of the user to the probing question
     *
     * @return response of the user to the probing question
     */
    public String getAns() {
        return ans;
    }

    /**
     * Returns the index pertinent to the current disease being considered, relative to the Prolog
     * list in the knowledge base
     *
     * @return index pertinent to the current disease being considered, relative to the Prolog list
     * in the knowledge base
     */
    public int getI() {
        return i;
    }

    /**
     * Returns the final certainty factor (that is, the highest recorded certainty factor for the diseases
     * in the database)
     *
     * @return highest recorded certainty factor for the diseases in the database
     */
    public double getFinalCF() {
        return finalCF;
    }

    /**
     * Returns the current symptom being considered
     *
     * @return current symptom being considered
     */
    public String getSymptom() {
        return currSymptomsParsed[j];
    }

    /**
     * Returns <code>true</code> if a symptom indicative of an emergency has been reported;
     * <code>false</code>, otherwise
     *
     * @return <code>true</code> if a symptom indicative of an emergency has been reported;
     * <code>false</code>, otherwise
     */
    public boolean getEmergency() {
        return emergency;
    }

    /**
     * Sets the user's response to the probing question to the given value (which is either
     * a <code>"yes"</code> or a <code>"no"</code>)
     *
     * @param ans response of the user to the probing question
     */
    public void setAns(String ans) {
        this.ans = ans;
    }

    /**
     * Sets the name of the patient to the given value
     *
     * @param name name of the patient
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the age of the patient to the given value
     *
     * @param age age of the patient
     */
    public void setAge(double age) {
        this.age = age;
    }

    /**
     * Sets the sex of the patient to the given value (which is either biologically <code>"male"</code>
     * or * <code>"female"</code>)
     *
     * @param sex sex of the patient
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Connects this expert system to the knowledge base written in Prolog given the filename (together
     * with the file extension) of this knowledge base and returns <code>true</code> if the consultation
     * is successful or <code>false</code>, otherwise
     *
     * <p>Prolog files are typically associated with the file extension <code>.pl</code>.</p>
     *
     * @param knowledgeBase Prolog knowledge base where the pertinent facts and rules are stored
     *
     * @return <code>true</code> if the consultation is successful; <code>false</code>, otherwise
     */
    public boolean loadKnowledgeBase(String knowledgeBase) {
        /* Prolog knowledge base */
        File f1;
        f1 = new File(knowledgeBase);

        /* Get the absolute path of the Prolog knowledge base. Otherwise, an existence error will be returned
        by Prolog (the consult predicate does not accept a relative path as its argument).
         */
        String f1s;
        f1s = f1.getAbsolutePath();

        /* Change backslashes to forward slashes in absolute path in compliance with Prolog conventions. */
        f1s = f1s.replace("\\", "/");

        /* Prepare the query to consult the knowledge base. */
        Query qFile;
        qFile = new Query("consult('" + f1s + "')");

        /* Return true if the consultation is successful; false, otherwise. */
        if (qFile.hasSolution()) {
            return true;
        }

        return false;
    }

    /**
     * Resets connection with the knowledge base when the expert system is restarted, effectively
     * deleting all data associated with the previous patient
     *
     * <p>Under the hood, this function operates through a sequence of <code>retractall/1</code> and
     * <code>asserta/1</code> to reset the terms that have been dynamically altered during previous
     * sessions of the expert system. </p>
     */
    public void unloadKnowledgeBase() {
        /* Prolog query to reset the terms that have been dynamically altered during previous sessions */
       Query qUnload;
       qUnload = new Query("restore");

       qUnload.hasSolution();
    }

    /**
     * Initializes the constants used by the expert system by retrieving their values from the Prolog
     * knowledge base
     *
     * <p>In particular, these include the following attributes: </p>
     * <ul>
     *     <li>Number of diseases in the knowledge base</li>
     *     <li>Minimum age for a patient to be considered an adult</li>
     *     <li>Minimum temperature for a body temperature to be considered indicative of an emergency</li>
     *     <li>Upper bound for a slow heart rate to be considered indicative of an emergency</li>
     *     <li>Minimum temperature for a body temperature to be considered indicative of a fever</li>
     *     <li>Upper bound for a slow heart rate (brachycardia) for a child</li>
     *     <li>Upper bound for a slow heart rate (brachycardia) for an adult</li>
     *     <li>Threshold for a disease to be ruled out</li>
     *     <li>Threshold for a diagnosis to be immediately given</li>
     * </ul>
     */
    public void loadConstantsFromKB() {
        /* Number of diseases in the knowledge base */
        Query qNumDiseases = new Query("numberOfDiseases(X)");
        Map<String, Term> solution = qNumDiseases.oneSolution();
        NUM_DISEASES = Integer.parseInt(String.valueOf(solution.get("X")));

        /* Minimum age for a patient to be considered an adult */
        Query qAdultAge = new Query("adultAge(X)");
        solution = qAdultAge.oneSolution();
        ADULT_AGE = Integer.parseInt(String.valueOf(solution.get("X")));

        /* Minimum temperature for a body temperature to be considered indicative of an emergency */
        Query qFeverEmergencyTemp = new Query("feverEmergencyDiagnosis(X)");
        solution = qFeverEmergencyTemp.oneSolution();
        FEVER_EMERGENCY_TEMP = Double.parseDouble(String.valueOf(solution.get("X")));

        /* Upper bound for a slow heart rate to be considered indicative of an emergency */
        Query qSlowHeartRateEmergencyRate = new Query("slowHeartRateEmergencyDiagnosis(X)");
        solution = qSlowHeartRateEmergencyRate.oneSolution();
        SLOW_HEART_RATE_EMERGENCY_RATE = Double.parseDouble(String.valueOf(solution.get("X")));

        /* Minimum temperature for a body temperature to be considered indicative of a fever */
        Query qFeverDiagnosisTemp = new Query("feverDiagnosis(X)");
        solution = qFeverDiagnosisTemp.oneSolution();
        FEVER_DIAGNOSIS_TEMP = Double.parseDouble(String.valueOf(solution.get("X")));

        /* Upper bound for a slow heart rate (brachycardia) for a child */
        Query qSlowHeartRateChildDiagnosis = new Query("slowHeartRateChildDiagnosis(X)");
        solution = qSlowHeartRateChildDiagnosis.oneSolution();
        SLOW_HEART_RATE_CHILD_DIAGNOSIS = Double.parseDouble(String.valueOf(solution.get("X")));

        /* Upper bound for a slow heart rate (brachycardia) for an adult */
        Query qSlowHeartRateAdultDiagnosis = new Query("slowHeartRateAdultDiagnosis(X)");
        solution = qSlowHeartRateAdultDiagnosis.oneSolution();
        SLOW_HEART_RATE_ADULT_DIAGNOSIS = Double.parseDouble(String.valueOf(solution.get("X")));

        /* Threshold for a disease to be ruled out */
        Query qCFValueRemove = new Query("cfValueRemove(X)");
        solution = qCFValueRemove.oneSolution();
        CF_VALUE_REMOVE = Double.parseDouble(String.valueOf(solution.get("X")));

        /* Threshold for a diagnosis to be immediately given */
        Query qCFValueConclude = new Query("cfValueConclude(X)");
        solution = qCFValueConclude.oneSolution();
        CF_VALUE_CONCLUDE = Double.parseDouble(String.valueOf(solution.get("X")));
    }

    /**
     * Returns <code>true</code> if the patient is male or <code>false</code>, otherwise, and removes
     * symptoms in the knowledge base that are exclusive to female patients to make probing more targeted
     *
     * @return <code>true</code> if the patient is male; <code>false</code>, otherwise
     */
    public boolean removeFemaleSymptoms() {
        /* Delete female-specific symptoms if the patient is male. */
        if (sex.equalsIgnoreCase("male")) {
            Query qRemoveOne = new Query("deleteAll(irregularMenstruation)");
            qRemoveOne.hasSolution();

            Query qRemoveTwo = new Query("deleteAll(vaginalDryness)");
            qRemoveTwo.hasSolution();

            return true;
        }

        return false;
    }

    /**
     * Returns <code>true</code> if the patient is not a child  (that is, of age 19 years and above)
     * or <code>false</code>, otherwise, and removes symptoms in the knowledge base that are exclusive
     * to pediatric (child) patients to make probing more targeted
     *
     * @return <code>true</code> if the patient is not a child; <code>false</code>, otherwise
     */
    public boolean removeChildSymptoms() {
        /* Delete pediatric symptoms if the patient is not a child (that is, 19 years old and above). */
        if (age >= ADULT_AGE) {
            Query qRemoveThree = new Query("deleteAll(failureToThrive)");
            qRemoveThree.hasSolution();

            return true;
        }

        return false;
    }

    /**
     * Considers the next disease in the knowledge base
     */
    public void moveToNextDisease() {
        /* Move to the first symptom of the next diseae. */
        j = 0;
        i++;
    }

    /**
     * Returns <code>true</code> if the certainty factor for the disease currently being considered
     * falls below the threshold (ruling it out); <code>false</code>, otherwise
     *
     * @return <code>true</code> if the certainty factor for the disease currently being considered
     * falls below the threshold (ruling it out); <code>false</code>, otherwise
     */
    public boolean isLowConfidence() {
        return CFval < CF_VALUE_REMOVE;
    }

    /**
     * Returns <code>true</code> if the certainty factor for the disease currently being considered
     * falls meets the threshold for immediate diagnosis (eliminating the need to consider the
     * subsequent diseases in the knowledge base); <code>false</code>, otherwise
     *
     * @return <code>true</code> if the certainty factor for the disease currently being considered
     * falls meets the threshold for immediate diagnosis (eliminating the need to consider the
     * subsequent diseases in the knowledge base); <code>false</code>, otherwise
     */
    public boolean isHighConfidence() {
        return CFval >= CF_VALUE_CONCLUDE;
    }

    /**
     * Computes the certainty factor and sets the corresponding attribute, given the current symptom
     * being considered, the patient's response, the patient's age, and the index of the current disease
     * being considered (relative to the Prolog list in the knowledge base)
     *
     * <p>The computation is expressed in the Prolog rules <code>confidenceFactor/3</code> and
     * adjustedWeight/3 (which adjusts the weight depending on whether an affirmative or negative response
     * is received from the patient). This function also asserts the updated certainty factors back into
     * the knowledge base, superseding previous values. </p>
     *
     * <p>It is patterned after the ad-hoc formulae used by the pioneering medical expert system MYCIN;
     * a more detailed description is given in the technical report.</p>
     *
     * @param symptom current symptom being considered
     * @param answer response of the patient
     * @param age age of the patient
     * @param currIndex index of the current disease being considered (relative to the Prolog list)
     *
     * @return updated certainty factor for the pertinent disease
     */
    public double updateCF(String symptom, String answer, double age, int currIndex) {
        /* Retrieve the weight of the current symptom from the knowledge base. */
        Query qWeight = new Query("weight(" + symptom + ",X)");
        Map<String, Term> solution = qWeight.oneSolution();
        double weight = Double.parseDouble(String.valueOf(solution.get("X")));
        symptom = symptom.trim();

        /* Construct a string that is representative of a list of the updated certainty factors for each disease,
        which will be asserted back into the knowledge base.
         */
        String newCF = "[";

        /* Retrieve the certainty factors from the knowledge base, and transfer them into an array for a more
        systematic GUI parsing.
         */
        Query qCF = new Query("cf(X)");
        solution = qCF.oneSolution();
        String init = String.valueOf(solution.get("X"));
        String trimmed = init.substring(1, init.length() - 1);
        String[] parsed = trimmed.split(",");

        double[] cf = new double[NUM_DISEASES];
        for (int i = 0; i < NUM_DISEASES; i++) {
            cf[i] = Double.parseDouble(parsed[i]);
        }

        /* Check if the patient reported a symptom that is indicative of an emergency.

        Currently, there are three such cases recognized in the knowledge base:
        - extremely high fever
        - extremely slow heart beat
        - chest pain
         */
        if (symptom.equalsIgnoreCase("fever")) {
            double num = Double.parseDouble(answer);
            if (num >= FEVER_EMERGENCY_TEMP) {
                emergency = true;
            }

        } else if (symptom.equalsIgnoreCase("slowHeartRate")) {
            double num = Double.parseDouble(answer);
            if (num < SLOW_HEART_RATE_EMERGENCY_RATE) {
                emergency = true;
            }

        } else if (symptom.equalsIgnoreCase("chestPain")) {
            if (answer.equalsIgnoreCase("yes")) {
                emergency = true;
            }
        }

        /* Translate the patient's numerical input for his/her vital signs into a binary affirmation ("yes")
        or negation ("no").
         */
        if (symptom.equalsIgnoreCase("fever")) {
            double num = Double.parseDouble(answer);
            if (num >= FEVER_DIAGNOSIS_TEMP)
                answer = "yes";
            else
                answer = "no";

        } else if (symptom.equalsIgnoreCase("slowHeartRate")) {
            /* Abnormally slow heart rate is different for children and adults. */
            if (age < ADULT_AGE) {
                double num = Double.parseDouble(answer);
                if (num < SLOW_HEART_RATE_CHILD_DIAGNOSIS)
                    answer = "yes";
                else
                    answer = "no";
            } else {
                double num = Double.parseDouble(answer);
                if (num < SLOW_HEART_RATE_ADULT_DIAGNOSIS)
                    answer = "yes";
                else
                    answer = "no";
            }
        }

        /* Update the confidence factor for each disease in the knowledge base. */
        for (int i = 0; i < NUM_DISEASES; i++) {
            /* Retrieve the mapping (stored in the knowledge base) to match each disease with the corresponding
            entry in the Prolog list.
             */
            Query qIndex = new Query("mapping(" + i + ", Y)");
            solution = qIndex.oneSolution();
            String disease = String.valueOf(solution.get("Y"));

            /* Retrieve the list of symptoms from the knowledge base and parse the result of the Prolog
            query into a list for easier manipulation.
             */
            Query qSymptoms = new Query("(" + disease + "Symptoms(X))");
            solution = qSymptoms.oneSolution();
            String initial = String.valueOf(solution.get("X"));
            String trimmedstr = initial.substring(1,initial.length() - 1);
            String[] parsedstr = trimmedstr.split(",");
            for (int j = 0; j < parsedstr.length; j++)
                parsedstr[j] = parsedstr[j].trim();
            List<String> listParsed = Arrays.asList(parsedstr);

            /* Update the confidence factor if the disease is associated with the symptom. */
            if (listParsed.contains(symptom)) {
                double c = cf[i];       // Current (non-updated) certainty factor
                double trueWeight;      // Adjusted weight depending on the user's response
                double newc;            // Updated certainty factor

                /* Adjust the weight of a symptom depending on the response of the patient (either affirmative
                or negative) by calling the relevant Prolog predicate.
                 */
                int answerCode;         // Numerical code corresponding to the patient's response
                answerCode = answer.equalsIgnoreCase("yes") ? 1 : 0;

                Query qAdjustedWeight = new Query("adjustedWeight(" + weight + ", " + answerCode + ", " + "NewWeight)");
                solution = qAdjustedWeight.oneSolution();
                String soln = String.valueOf(solution.get("NewWeight"));

                trueWeight = Double.parseDouble(soln);

                /* Update the confidence factor (following a scheme patterned after the computation introduced
                in the pioneering medical expert system MYCIN) by calling the appropriate Prolog predicate.
                 */
                Query qCalc = new Query("confidenceFactor(" + c + ", " + trueWeight + ", " + "NewCF)");
                solution = qCalc.oneSolution();
                soln = String.valueOf(solution.get("NewCF"));
                newc = Double.parseDouble(soln);

                /* Update the values in the list. */
                newCF += newc;
                cf[i] = newc;

                /* Convert the list back to a Prolog-recognized string representative of a list through the
                addition of comma separators.
                 */
                if (i < NUM_DISEASES - 1)
                    newCF += ", ";

            } else {
                /* Since the disease is not associated with the symptom, the certainty factor is not adjusted. */
                newCF += cf[i];

                if (i < NUM_DISEASES - 1)
                    newCF += ", ";
            }
        }

        /* Complete the conversion of the list back to a Prolog-recognized string representative of a list
        through the appending of a closing square bracket.
         */
        newCF += "]";

        /* Remove the old list of certainty factors, and assert new list to the knowledge base. */
        Query qRetract = new Query ("retract(cf(X))");
        qRetract.hasSolution();

        Query qAssert = new Query ("assertz(cf(" + newCF + "))");
        qAssert.hasSolution();

        /* Update the attribute in this Prolog-Java connector class, and return the certainty factor. */
        CFval = cf[currIndex];
        return cf[currIndex];
    }

    /**
     * Removes a symptom from the list of symptoms associated with a certain disease
     * in order to prevent redundant asking of questions during probing
     */
    public void deleteSymptom() {
        Query qDelete = new Query("deleteAll(" + currSymptomsParsed[j] + ")");
        qDelete.hasSolution();
    }

    /**
     * Returns the question asked by this expert system during probing
     *
     * @return question asked by this expert system during probing
     */
    public String displayInquiry() {
        Map<String, Term> solution;

        /* Retrieve the name of the disease from the knowledge base. */
        Query qIndex = new Query("mapping(" + i + ", Y)");
        solution = qIndex.oneSolution();
        String currDisease = String.valueOf(solution.get("Y"));

        /* Retrieve the list of symptoms associated with the current disease from the knowledge base. */
        Query qSymptoms = new Query("(" + currDisease + "Symptoms(X))");
        solution = qSymptoms.oneSolution();
        String init = String.valueOf(solution.get("X"));
        String trimmed = init.substring(1,init.length() - 1);
        currSymptomsParsed = trimmed.split(",");

        /* Retrieve the current probing question associated with the symptom being considered. */
        Query qInquiry = new Query("display(" + currSymptomsParsed[j] + ",X)");
        solution = qInquiry.oneSolution();
        String temp = String.valueOf(solution.get("X"));
        String forDisplay = temp.substring(1, temp.length() - 1);

        /* Separate the Filipino translation from the English question with a blank newline for readability. */
        forDisplay = forDisplay.replace("? ", "?\n\n");

        /* Return the question for display. */
        return forDisplay;
    }

    /**
     * Return the final diagnosis of this expert system
     *
     * <p>The final diagnosis contains the following details: </p>
     * <ul>
     *     <li>Most probable disease</li>
     *     <li>Qualitative description of the certainty factor</li>
     *     <li>Final certainty factor, expressed in percentage (%)</li>
     * </ul>
     *
     * <p>If no disease meets the 0.2 threshold, then the patient is asked to consult with a larger
     * hospital for a more thorough diagnosis. This rule is overridden if the patient reports a symptom
     * that is indicative of an emergency. </p>
     *
     * @return final diagnosis of this expert system
     */
    public String getFinalDiagnosis() {
        Map<String, Term> solution;

        /* Retrieve the certainty factors per disease, and transfer them in a list for easier manipulation. */
        Query qMax = new Query("cf(X)");
        solution = qMax.oneSolution();
        String init = String.valueOf(solution.get("X"));
        String trimmed = init.substring(1, init.length() - 1);
        String[] parsed = trimmed.split(",");

        double[] cf = new double[NUM_DISEASES];
        for (int i = 0; i < NUM_DISEASES; i++) {
            cf[i] = Double.parseDouble(parsed[i]);
        }

        /* Find the maximum confidence factor and the index associated with the pertinent disease. */
        double maxValue = cf[0];
        int maxIndex = 0;

        for (int i = 0; i < cf.length; i++) {
            if (cf[i] > maxValue) {
                maxValue = cf[i];
                maxIndex = i;
            }
        }

        /* Retrieve the template for the final diagnosis from the knowledge base. */
        Query qFinalDiagnosis = new Query("finalDiagnosis(\" " + name + "\"," + maxValue + "," + maxIndex + "," + emergency + "," + "X)");
        solution = qFinalDiagnosis.oneSolution();
        String diagnosis = String.valueOf(solution.get("X"));
        String trimmedDiagnosis = diagnosis.substring(2, diagnosis.length() - 1);

        /* Express the final certainty factor in percentage. */
        finalCF = maxValue * 100;

        /* Return the final diagnosis for display. */
        return trimmedDiagnosis;
    }

    /**
     * Returns the confidence values per disease for display in the graphical user interface of this expert system,
     * given the most recent symptom considered
     *
     * <p>The format of the returned string is as follows: </p>
     * <ul>
     *     <li>Symptom: most recent symptom considered</li>
     *     <li>Certainty factors: </li>
     *     <li>Name of disease (uppercase): certainty factor (in decimal)</li>
     * </ul>
     *
     * Each entry is separated by a newline.
     *
     * @param symptom most recent symptom considered
     * @return confidence values per disease for display in the graphical user interface of this expert system,
     * given the most recent symptom considered
     */
    public String displayCFvals(String symptom) {
        /* Construct the header. */
        String log = "Symptom: " + symptom + "\nCertainty factors: \n";

        /* Retrieve the certainty factors per disease from the knowledge base. */
        Query qGetCF = new Query("cf(X)");
        Map<String, Term> solution = qGetCF.oneSolution();
        String init = String.valueOf(solution.get("X"));
        String trimmed = init.substring(1, init.length() - 1);
        String[] parsed = trimmed.split(",");

        /* Transfer them into a list for more organized GUI parsing. */
        double[] cf = new double[NUM_DISEASES];
        for (int i = 0; i < NUM_DISEASES; i++) {
            cf[i] = Double.parseDouble(parsed[i]);
        }

        /* Retrieve the names of the diseases from the knowledge base. */
        for (int i = 0; i < NUM_DISEASES; i++) {
            Query qFullName = new Query("fullName(" + i + ",X)");
            solution = qFullName.oneSolution();
            String diseaseName = String.valueOf(solution.get("X"));

            /* Concatenate the disease and the current certainty factor associated with it. */
            String diseaseNameTrimmed = diseaseName.substring(1, diseaseName.length() - 2).toUpperCase();
            log += diseaseNameTrimmed + ": " + String.format("%.2f", cf[i]) + "\n";
        }

        /* Return the list of certainty factors for display. */
        return log;
    }
}
