/*
SecondExtendedMathSolver.java:
Parser und Solver, zur Berechnung einfacher mathematischer Ausdruecke,
mit Beruecksichtung von Punkt-vor-Strichrechnung

Copyright (C) 2013 Karsten Blauel

Dieses Programm ist freie Software. Sie koennen es unter den Bedingungen der GNU General Public License,
wie von der Free Software Foundation veroeffentlicht, weitergeben und/oder modifizieren, entweder gemaess
Version 3 der Lizenz oder (nach Ihrer Option) jeder spaeteren Version.
Die Veroeffentlichung dieses Programms erfolgt in der Hoffnung, dass es Ihnen von Nutzen sein wird, aber
OHNE IRGENDEINE GARANTIE, sogar ohne die implizite Garantie der MARKTREIFE oder der VERWENDBARKEIT FUER
EINEN BESTIMMTEN ZWECK. Details finden Sie in der GNU General Public License.
Sie sollten ein Exemplar der GNU General Public License zusammen mit diesem Programm erhalten haben.
Falls nicht, siehe <http://www.gnu.org/licenses/>.
*/

public class SecondExtendedMathSolver {
	
	private static boolean SHOW_OUTPUT = true;
	
	private final static int SIN = 0;
	private final static int COS = 1;
	private final static int TAN = 2;
	private final static int ASIN = 3;
	private final static int ACOS = 4;
	private final static int ATAN = 5;
	private final static int SQRT = 6;
	
	private String[] functionNames = new String[] { "sin(", "cos(", "tan(", "asin(", "acos(", "atan(", "sqrt(" };
//	private boolean mathFunctions = false;
	private double[] numbers;				// double-Array zum Speichern der Zahlen 
	private String[] splitExpression;		// Teilausdruecke
//	private int[] splitExpressionTyp;	// Teilausdruecke
	
	/**
	 * parse:
	 * Analyse des mathematischen Ausdrucks und
	 * trennen in Operationszeichen und Operanden.
	 * Speichern der Operatoren in char-Array.
	 * Untersuchen auf Strich-, oder Punktrechnung.
	 * Konvertieren der Zahlen und speichern in double-Array, wenn Strichrechnung.
	 * Zusaetzliches Trennen in Operationszeichen und Operanden bei Punktrechnung mit
	 * anschliessender Berechnung (Funktion innerParser()).
	 * Aufaddieren der Teillösungen.
	 * 
	 * @param expression
	 */
	private void parse(String expression)
	{
		char[] expressionAsChars = expression.toCharArray();	// Zeichenkette zeichenweise als char-Array
		char buf;						// Zwischenspeicher fuer ein beliebiges Zeichen
		
		int countOperators = 0;	// Zaehlvariable fuer Rechenoperationszeichen
		int start = 0;	// Startposition einer Zahl
				
		// Zaehlen der Rechenoperationen zum ermitteln der maximalen Termanzahl
		for(int i=1; i<expressionAsChars.length; i++)
		{
			buf = expressionAsChars[i];	// Zeichen zwischenspeichern
			if(buf == '+' || buf == '-' || buf == '*' || buf == '/') // wenn ein Zeichen Operator ist, dann Zaehler erhoehen
				countOperators++;
		}
		
		// Arrays fuer Zahlen, Operatoren und Operatoren initialisieren
		numbers = new double[countOperators+1];
		splitExpression = new String[countOperators+1];
//		splitExpressionTyp = new int[countOperators+1];
		
		// Trennen von Zahlen und Rechenoperationszeichen
		int n=0;
		for(int i=1; i<expressionAsChars.length; i++)
		{
			// Suche + & - Rechenoperationszeichen
			buf = expressionAsChars[i];
			if(buf == '+' || buf == '-')
			{
				// Therme herausloesen und in String-Array ablegen
				splitExpression[n] = expression.substring(start, i);
						
				start = i;	// Startposition von Rechenoperationszeichen zwischenspeichern
				
				n++;		// Array-Counter erhoehen
			}
		}
		
		// Letzte Zahl herausloesen
		splitExpression[n] = expression.substring(start, expressionAsChars.length);
	
		 containsMathFunction();
	}
	
	/**
	 * Testet, ob mathem. Funktionen in den Ausdruecken
	 * verwendet werden (sin, cos, etc...) und initialisiert
	 * ein Bool-Array welches Auskunft ueber den Index der
	 * Teilausdruecke mit math. Funktionen gibt.
	 */
	private void containsMathFunction()
	{
		for(int i=0; i<splitExpression.length; i++)
			if(splitExpression[i] !=null) // !!! Nullabfrage fixen, warscheinlich splitExpression zu gross !!!
				for(int j=0; j<functionNames.length; j++)
				{	
//					System.out.println(splitExpression[i]+" "+functionNames[j]);
					if(splitExpression[i].contains(functionNames[j]))
					{
						solveMathFunctions(i, j);
					}
				}
	}
	
	private void solveMathFunctions(int i, int j)
	{
		int a, b;
		double value, erg = 0;
		String expr;
		String prefix = "+";
		
		a = splitExpression[i].indexOf("(") + 1;
		b = splitExpression[i].indexOf(")");
		
		expr = splitExpression[i].substring(a, b);
		value = Double.parseDouble(expr);
		
//		System.out.println("splitExpression[i].substring(0)="+splitExpression[i].substring(0,1));
		
		if(splitExpression[i].substring(0, 1).equals("-"))
			prefix = "-";
		
		switch(j)
		{
			case SIN:
				erg = Math.sin(value);
				break;
				
			case COS:
				erg = Math.cos(value);
				break;
				
			case TAN:
				erg = Math.tan(value);
				break;
				
			case ASIN:
				erg = Math.asin(value);
				break;
				
			case ACOS:
				erg = Math.acos(value);
				break;
				
			case ATAN:
				erg = Math.atan(value);
				break;
				
			case SQRT:
				erg = Math.sqrt(value);
				break;
				
		}
		
		if(erg<0)
			splitExpression[i] = "" + erg;
		else
			splitExpression[i] = prefix + erg;
		
//		System.out.println("(="+a);
//		System.out.println(")="+b);
//		System.out.println("(...)="+value);
//		System.out.println("splitExpression[i]="+splitExpression[i]);
		

		
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
	private double eval(String expression)
	{
		double erg = 0;	// Speichervariablen
		
		String buffer;	// Zwischenvariable für Teilausdruck
		
		parse(expression);	// Parser aufrufen
		
		// Operanden-Array durchlaufen
		for(int i=0; i<splitExpression.length; i++)
		{
			buffer = splitExpression[i];
			
			if(buffer==null)
				break;
			
			if(SHOW_OUTPUT)
				System.out.println("buffer="+buffer);
			
			// Wenn Teilausdruck Punktrechnung enthaelt ...
			if(buffer.contains("*") || buffer.contains("/"))
			{
				erg = erg + innerParser(buffer);	// ... dann rufe inenren parser auf ...
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

		SecondExtendedMathSolver p = new SecondExtendedMathSolver();
		double ergebnis;
		
		String expression = "-1+20-300+sin(1.57)+4000*2/4+cos(3.14)-10*2.5*2.5-2*3";
//		String expression = "-1+20-300+4000*2/4-10*2.5*2.5-2*3";
		System.out.println("Berechne: "+expression);
		
		if(SHOW_OUTPUT)
			System.out.println("zerlege in:");
		
		ergebnis = p.eval(expression);
		
		if(SHOW_OUTPUT)
			System.out.println("Auswerten");
		
		System.out.println("Ergebnis: "+expression+"="+ergebnis+
		" (Kontrolle: "+(-1+20-300+Math.sin(1.57)+4000*2/4+Math.cos(3.14)-10*2.5*2.5-2*3)+")");	// 1650.5
	}
}
