OpenEdu-MathSolver
==================

Einfache mathematische Solver

1. SimpleMathSolver.java<br />
Eine einfache Java-Klasse zum berechnen mathematischer Ausdruecke. Punkt-vor-Strich-Rechnung wird nicht beruecksichtigt

2. ExtendedMathSolver.java<br />
Eine weitere Java-Klasse zum berechnen mathematischer Ausdruecke. hier wird die Punkt-vor-Strich-Rechnung beruecksichtigt

3. SecondExtendedMathSolver.java<br />
Wie 2. Hier werden zusätzlich noch folgende mathematische Funktionen berücksichtigt:
sin, cos, tan, asin, acos, atan, exp, log, ln, x^y

4. MathExpressionValidator.java<br />
Eine Validator-Klasse welche eine rudimentaere Pruefung des mathematischen Ausdrucks vornimmt. Leerzeichen werden geloescht,
Kommata werden durch Punkte ersetzt. Zusaetzlich die Dopplung von Rechenoperatoren Überprueft. Als Ueberpruefungsergebnis
wird TRUE oder FALSE zurueckgeleifert.
