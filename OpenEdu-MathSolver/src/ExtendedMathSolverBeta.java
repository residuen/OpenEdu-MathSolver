/*
SecondExtendedMathSolver.java:
Parser und Solver, zur Berechnung einfacher mathematischer Ausdruecke,
mit Beruecksichtung von Punkt-vor-Strichrechnung, sowie von mathematischen Funktionen
wie z.B.: sin, cos, tan, asin, acos, atan, exp, log, ln, x^y

Copyright (C) 2014 Karsten Blauel

Dieses Programm ist freie Software. Sie koennen es unter den Bedingungen der GNU General Public License,
wie von der Free Software Foundation veroeffentlicht, weitergeben und/oder modifizieren, entweder gemaess
Version 3 der Lizenz oder (nach Ihrer Option) jeder spaeteren Version.
Die Veroeffentlichung dieses Programms erfolgt in der Hoffnung, dass es Ihnen von Nutzen sein wird, aber
OHNE IRGENDEINE GARANTIE, sogar ohne die implizite Garantie der MARKTREIFE oder der VERWENDBARKEIT FUER
EINEN BESTIMMTEN ZWECK. Details finden Sie in der GNU General Public License.
Sie sollten ein Exemplar der GNU General Public License zusammen mit diesem Programm erhalten haben.
Falls nicht, siehe <http://www.gnu.org/licenses/>.
*/

/*
 * Performance-ToDos:
 * - Berechnete mathem. Funktionen als Double ablegen und nicht wieder in String umwandeln
 * - String-Array mit Teilausdruecken auf Laenge pruefen
 */

public class ExtendedMathSolverBeta {
	
	private final static boolean SHOW_OUTPUT = true;	// Sysout zeigen oder nicht
	
	// Konstantensammlung, in Reihenfolge der math. Funktionen zugeordnet (siehe Array functionNames in Zeile 43)
	private final static int SIN  = 0;
	private final static int COS  = 1;
	private final static int TAN  = 2;
	private final static int ASIN = 3;
	private final static int ACOS = 4;
	private final static int ATAN = 5;
	private final static int SQRT = 6;
	private final static int EXP  = 7;
	private final static int LOG  = 8;
	private final static int LN   = 9;
	private final static int POW  = 10;
	
	// String-Array mit Namen mathematischer Funktionen 
	private String[] functionNames = new String[] { "sin(", "cos(", "tan(", "asin(", "acos(", "atan(",
													"sqrt(", "exp(", "log(", "ln(", "^" };
	private double[] numbers;				// double-Array zum Speichern der Zahlen 
	private String[] splitExpression;		// Teilausdruecke
	
	/**
	 * parse:
	 * Analyse des mathematischen Ausdrucks und
	 * trennen in Operationszeichen und Operanden.
	 * Speichern der Operatoren in char-Array.
	 * Untersuchen auf Strich-, oder Punktrechnung.
	 * Konvertieren der Zahlen und speichern in double-Array, wenn Strichrechnung.
	 * Zusaetzliches Trennen in Operationszeichen und Operanden bei Punktrechnung mit
	 * anschliessender Berechnung (Funktion innerParser()).
	 * Zusaetzlich untersuchen nach math. Funktionen (sin, cos, sqrt, etc ...) und berechnen
	 * dieser Teilausdruecke, sowie zurueckschreiben in Zeichenkette (!!BAD!!)
	 * Aufaddieren der Teillösungen.
	 * 
	 * @param expression
	 */
	private void parse(String expression)
	{
		expression = validate(expression);	// Validierung der Zeichenkette
		
		char[] expressionAsChars = expression.toCharArray();	// Zeichenkette zeichenweise als char-Array
		char buf;						// Zwischenspeicher fuer ein beliebiges Zeichen
		
		int countOperators = 0;	// Zaehlvariable fuer Rechenoperationszeichen
		int start = 0;	// Startposition einer Zahl
				
		// Zaehlen der Rechenoperationen zum ermitteln der maximalen Termanzahl
		for(int i=1; i<expressionAsChars.length; i++)
		{
			buf = expressionAsChars[i];	// Zeichen zwischenspeichern
			
			// wenn ein Zeichen + - * / Operator ist, dann Zaehler erhoehen
			// bei - wird zusaetzlich auf Exponentenschreibweise mit E ueberprueft
			if(buf == '+' || buf == '*' || buf == '/' || (buf == '-' && !(expressionAsChars[i-1]=='E')))
				countOperators++;
			
			// Ueberspringen des naechsten Zeichens bei Teilstring *- 
			if(buf == '*' && expressionAsChars[i+1] == '-')
				i++;
		}
		
		// Arrays fuer Zahlen, Operatoren und Operatoren initialisieren
		numbers = new double[countOperators+1];
		splitExpression = new String[countOperators+1];
		
		// Trennen von Zahlen und Rechenoperationszeichen
		int n=0;
		for(int i=1; i<expressionAsChars.length; i++)
		{
			// Suche + und - Rechenoperationszeichen
			buf = expressionAsChars[i];
			
			// Ueberspringen des naechsten Zeichens bei Teilstring *- 
			if(buf == '*' && expressionAsChars[i+1] == '-')
			{
				i++;
				
				continue;
			}
			
			if(buf == '+' || (buf == '-' && !(expressionAsChars[i-1]=='E')))
			{
				// Therme herausloesen und in String-Array ablegen
				splitExpression[n] = expression.substring(start, i);
						
				start = i;	// Startposition von Rechenoperationszeichen zwischenspeichern
				
				n++;		// Array-Counter erhoehen
			}
		}
		
		// Letzte Zahl herausloesen
		splitExpression[n] = expression.substring(start, expressionAsChars.length);
	
		// Auf math. Funktionen ueberpruefen
		containsMathFunction();
	}
	
	/**
	 * Validieren des Ausdrucks:
	 * Ueberpruefen auf Leerzeichen, falsche Kommata
	 * und falsche Rechenoperatoren
	 * @param expression
	 * @return
	 */
	private String validate(String expression)
	{
		// Validierung der Zeichenkette
		expression = expression.replace(" ", "");	// Alle Leerzeichen entfernen
		expression = expression.replace(",", ".");	// Kommata durch Punkt ersetzen
		expression = expression.replace("+-", "-");	// +- durch - ersetzen

		return expression;
	}
	
	/**
	 * Testet, ob mathem. Funktionen in den Ausdruecken
	 * verwendet werden (sin, cos, etc...).
	 * Ist dies der Fall, wird dieser berechnet und
	 * das Ergebnis als Zeichenkette in den Ausdruck geschrieben (!! TODO)
	 */
	private void containsMathFunction()
	{
		for(int i=0; i<splitExpression.length; i++)			// Teilausdruecke durchlaufen
			if(splitExpression[i] !=null) 					// !!! Nullabfrage fixen, warscheinlich splitExpression zu gross !!!
				for(int j=0; j<functionNames.length; j++)	// Suchbegriffe durchlaufen
				{	
					// Teilausdruck nach math. Funktion durchsuchen, wenn gefunden ...
					if(splitExpression[i].contains(functionNames[j]))
					{
//						System.out.println("splitExpression[i]="+splitExpression[i]);
						solveMathFunctions(i, j);	// ... dann Funktionswert berechnen

						break;
					}
				}
	}
	
	/**
	 * Funktionsparameter ermitteln und als Zeichenkette extrahieren.
	 * Den Funktiosnwert berechnen und mit Prefix in Teilausdruck
	 * als String (!!BAD!!) zurueckkopieren
	 * 
	 * @param i
	 * @param j
	 */
	private void solveMathFunctions(int i, int j)
	{
		int a, b;	// Variablen fuer Position der Funktionsparameter in Zeichenkette
		double value, base, exponent, erg = 0;
		String expr, splitExpr[];
		
		if(j==POW)	// ueberpruefen auf die Funktion x^y
		{
			splitExpr = splitExpression[i].split("\\^");	// Zerlegen in Basis und Exponent
			
			// Basis und Exponent in Double konvertieren
			base = Double.parseDouble(splitExpr[0]);
			exponent = Double.parseDouble(splitExpr[1]);
			
			// Berechnen des Ergebnisses aus x^y
			erg = Math.pow(base, exponent);
		}
		else		// Ansonsten auf andere Funktionen ueberpruefen
		{
			// Ermitteln der Position der Funktionsparameter in Zeichenkette
			a = splitExpression[i].indexOf("(") + 1;
			b = splitExpression[i].indexOf(")");
			
			// Funktionsparameter aus String extrahieren
			expr = splitExpression[i].substring(a, b);
			
			// Funktionsparameter in double umwandeln
			value = Double.parseDouble(expr);
			
			// Berechnen des Funktiosnwertes
			switch(j)
			{
				case SIN:
					erg = Math.sin(value);	// Sinus
					break;
					
				case COS:
					erg = Math.cos(value);	// Kosinus
					break;
					
				case TAN:
					erg = Math.tan(value);	// Tangens
					break;
					
				case ASIN:
					erg = Math.asin(value);	// A-Sinus
					break;
					
				case ACOS:
					erg = Math.acos(value);	// A-Kosinus
					break;
					
				case ATAN:
					erg = Math.atan(value);	// A-Tangens
					break;
					
				case SQRT:
					erg = Math.sqrt(value);	// Quadratwurzel
					break;
					
				case EXP:					// E-Funktion
					erg = Math.exp(value);
					break;
					
				case LOG:
					erg = Math.log10(value);	// Logarithmus zur Basis 10
					break;
					
				case LN:
					erg = Math.log(value);		// Natuerlicher Logarithmus
					break;
			}
		}
		
		// Ergebnis-String mit Prefix aus Ergebnis erzeugen
		if(erg<0)
			splitExpression[i] = "" + erg;
		else
			splitExpression[i] = "+" + erg;

	}
	
	/**
	 * eval:
	 * Aufruf des Parsers.
	 * Durchlaufen des Operanden-Arrays (double[]) und Verknuepfen mit
	 * Operator-Array (char[]).
	 * Abfrage der erforderlichen Rechenoperationen und durchfuehren
	 * der Berechnungen.
	 * Rueckgabe des Ergebniswertes.
	 * 
	 * @param expression
	 * @return
	 */
	public double eval(String expression)
	{
		double erg = 0;	// Speichervariablen
		
		String buffer;	// Zwischenvariable für Teilausdruck
		
		parse(expression);	// Parser aufrufen
		
		// Operanden-Array durchlaufen
		for(int i=0; i<splitExpression.length; i++)
		{
			buffer = splitExpression[i];
			
			if(buffer==null)	// (!! Arraylaenge ueberpruefen !!)
				break;
			
			if(SHOW_OUTPUT)
				System.out.println("buffer="+buffer);
			
			// Wenn Teilausdruck Punktrechnung enthaelt ...
			if(buffer.contains("*") || buffer.contains("/"))
			{
				erg = erg + innerParser(buffer);	// ... dann rufe inneren Parser auf ...
			}
			else
				erg = erg + Double.parseDouble(buffer);	// ... ansonsten Teilausdruck aufaddieren 
		}
		
		return erg;	// Ergebnis zurueckgeben
	}
	
	/**
	 * innerParser:
	 * Analyse des mathematischen Ausdrucks und
	 * trennen in Operationszeichen und Operanden.
	 * Konvertieren der Zahlen und speichern in double-Array.
	 * Speichern der Operatoren in char-Array.
	 * Ausrechnen des gesamten Ausdrucks.
	 * Rueckgabe des Ergebniswertes.
	 * 
	 * @param expression
	 * @return
	 */
	private double innerParser(String expression)
	{
		double erg, b;	// Speichervariablen
		
		char[] expressionAsChars = expression.toCharArray();	// Zeichenkette zeichenweise als char-Array
		char[] arithmeticOperations;	// char-Array zum Speichern der Rechenoperationszeichen
		char buf;						// Zwischenspeicher fuer ein beliebiges Zeichen
		
		int countOperators = 0;	// Zaehlvariable fuer Rechenoperationszeichen
		int start = 0;			// Startposition einer Zahl
				
		// Zaehlen der Rechenoperationen * und /
		for(int i=1; i<expressionAsChars.length; i++)
		{
			buf = expressionAsChars[i];
			if(buf == '*' || buf == '/')
				countOperators++;
		}
		
		// Arrays fuer Zahlen initialisieren
		numbers = new double[countOperators+1];
		arithmeticOperations = new char[countOperators];
		
		// Trennen von Zahlen und Rechenoperationszeichen
		for(int i=1, n=0; i<expressionAsChars.length; i++)
		{
			// Suche Rechenoperationszeichen
			buf = expressionAsChars[i];
			if(buf == '*' || buf == '/')
			{
				// Zahlen herausloesen
				if(n==0)
					numbers[n] = Double.parseDouble(expression.substring(start, i));
				else
					numbers[n] =  Double.parseDouble(expression.substring(start+1, i));

				// Rechenoperationszeichen herausloesen
				arithmeticOperations[n] = expression.substring(i, i+1).toCharArray()[0];

				start = i;	// Startposition von Rechenoperationszeichen zwischenspeichern

				n++;		// Array-Counter erhoehen
			}
		}
		
		// Letzte Zahl herausloesen
		numbers[numbers.length-1] =  Double.parseDouble(expression.substring(start+1, expressionAsChars.length));
		
		erg = numbers[0];	// Ersten Operanden in erg speichern
		
		// Berechnen des mathematischen Ausdrucks
		
		// Operanden-Array durchlaufen
		for(int i=1; i<numbers.length; i++)
		{
			b = numbers[i];	// Werte holen
			
			// Abfrage der Rechenoperation und durchfuehren der entsprechenden Berechnung
			switch(arithmeticOperations[i-1])
			{
				case '*':
					erg = erg * b;
					break;
					
				case '/':
					erg = erg / b;
					break;
			}
		}
		// Berechnen des mathematischen Ausdrucks - ENDE
		
		return erg;
	}

	public static void main(String[] args) {

		ExtendedMathSolverBeta p = new ExtendedMathSolverBeta();
		double ergebnis;
		
		String expression = "-1+20-300+sin(1.57)+-4000*-2/4+cos(3.14)-10*2.5*2.5-2*3+2^3-1.23E-2";
		System.out.println("Berechne: "+expression);
		
		if(SHOW_OUTPUT)
			System.out.println("zerlege in:");
		
		ergebnis = p.eval(expression);
		
		if(SHOW_OUTPUT)
			System.out.println("Auswerten");
		
		System.out.println("Ergebnis: "+expression+"="+ergebnis+
		" (Kontrolle: "+(-1+20-300+Math.sin(1.57)+-4000*(-2)/4+Math.cos(3.14)-10*2.5*2.5-2*3+Math.pow(2,3)-1.23E-2)+")");	// 1650.5
	}
}
