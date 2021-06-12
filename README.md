# EHRLICH (Medical Expert System)
**E**n**h**anced **R**ule- and **L**ogic-Based **I**mmunological **C**onsultative **H**ub or **EHRLICH** &mdash; a reverse acronym inspired by the German pioneer of immunology Paul Ehrlich &mdash; is a medical expert system specializing in autoimmune diseases, conditions triggered by malfunctions of the immune system that cause it to attack healthy body tissues. In particular, it seeks to automate the diagnosis for 15 such diseases, selected on the basis of their prevalence and the accessibility of symptomatic detection with visual inspection or the patient’s own assessment prior to conclusive laboratory tests.

***DISCLAIMER: The goal of this project is to explore the possibility of employing backward chaining in the implementation of medical expert systems that are able to handle uncertainty. Although care has been taken to consult technical sources, this system is ultimately subject to a closed-world assumption and to the limited size of its knowledge base.***

***Therefore, it is not intended to be used &mdash; and should not be used &mdash; for real-world or professional diagnosis.***

## Task
**EHRLICH** is the major course output for an introduction to intelligent systems class. The task is to create an expert system that is able to diagnose at least 10 diseases affecting a particular body system. The knowledge base has to be written in the **logic programming language Prolog**, and the system has to employ **backward chaining** as its primary inference method. The rules should consist solely of symptoms that can be observed or identified by the patient to a certain degree prior to laboratory analysis.

The **15 autoimmune diseases** included in the knowledge base of EHRLICH are as follows: Addison disease, dermatomyositis, Hashimoto thyroiditis, multiple sclerosis, Crohn’s disease, Grave’s disease, myasthenia gravis, pernicious anemia, reactive arthritis, Sjögren syndrome, lupus, rheumatoid arthritis, type I diabetes, celiac disease, and alopecia areata.

The program initially asks for the patient's information to rule out certain age- and sex-specific conditions and to determine the normal thresholds for medical statistics (such as heart rate).

For the diagnosis, it adapts <a href = "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC2464549/pdf/procascamc00015-0074.pdf">MYCIN</a>-style calculations to admit uncertainty to an extent. To reduce the length of probing, a disease is ruled out once the certainty level falls below 0.2. Meanwhile, if a disease registers a certainty level of at least 0.9, a diagnosis is immediately given, thus terminating the inquiry process. The screenshots below show parts of the diagnosis:

There are two special cases handled by the system. First, if none of the diseases in the knowledge base satisfy the 0.2 threshold, it refers the patient to a larger medical facility for a more thorough diagnosis. Second, if the patient reports a symptom that is indicative of an emergency, EHRLICH explicitly displays an alert message, and, per the task specifications, a diagnosis is compulsorily given along with the certainty level.

The project consists of three folders:
- <a href = "https://github.com/memgonzales/medical-expert-ehrlich/tree/master/api"><code>api</code></a> - <code>Javadoc</code> documentation of this project
- <a href = "https://github.com/memgonzales/medical-expert-ehrlich/tree/master/out"><code>out</code></a> - <code>.class</code> files
- <a href = "https://github.com/memgonzales/medical-expert-ehrlich/tree/master/src"><code>src</code></a> - <code>.java</code> files (source codes)

Besides the <a href = "https://github.com/memgonzales/medical-expert-ehrlich/blob/master/Medical%20Expert%20System.jar"><code>Medical Expert System.jar</code></a> file, it also includes the following documents:
- <a href = "https://github.com/memgonzales/medical-expert-ehrlich/blob/master/Technical%20Report.pdf"><code>Technical Report.pdf</code></a> - Formal discussion of the knowledge base, underlying algorithm, and behavior of the system
- <a href = "https://github.com/memgonzales/medical-expert-ehrlich/blob/master/Demo%20File.pdf"><code>Demo File.pdf</code></a> - Instructions for using EHRLICH and screenshots of the running system

## Built Using
The project was built using **Java** following the Model-View-Controller (MVC) architectural pattern, with the <code>.class</code> files generated via **Java SE Development Kit 14**. The graphical user interface was created using **Swing**, a platform-independent toolkit that is part of the Java Foundation Classes.

The knowledge base was written in **Prolog** (with **SWI-Prolog** as the particular implementation). The open-source library <a href = "https://jpl7.org/">**JPL 7**</a>, released under the Simplified BSD License and bundled with a SWI-Prolog installation, provided the classes and functions for the bidirectional interface between Java and Prolog.

## Authors
- <b>Mark Edward M. Gonzales</b> <br/>
  mark_gonzales@dlsu.edu.ph <br/>
  gonzales.markedward@gmail.com <br/>
  
- <b>Hylene Jules G. Lee</b> <br/>
  hylene_jules_lee@dlsu.edu.ph <br/>
  lee.hylene@gmail.com <br/>
  
**In no event shall the authors and their affiliates be liable to any party for direct, indirect, special, incidental, or consequential damages arising out of the use of this software and its documentation, even if advised of the possibility of such damage.**
