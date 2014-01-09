/*
SimpleMathSolver.java:
Simpler Parser und Solver, zur Berechnung einfacher mathematischer Ausdruecke,
ohne Beruecksichtung von Punkt-vor-Strichrechnung

Copyright (C) 2013 Karsten Blauel

Dieses Programm ist freie Software. Sie koennen es unter den Bedingungen der GNU General Public License,
wie von der Free Software Foundation veroeffentlicht, weitergeben und/oder modifizieren, entweder gemaess
Version 3 der Lizenz oder (nach Ihrer Option) jeder spaeteren Version.
Die Veroeffentlichung dieses Programms erfolgt in der Hoffnung, dass es Ihnen von Nutzen sein wird, aber
OHNE IRGENDEINE GARANTIE, sogar ohne die implizite Garantie der MARKTREIFE oder der VERWENDBARKEIT FUER
EINEN BESTIMMTEN ZWECK. Details finden Sie in der GNU General Public License.
Sie sollten ein Exemplar der GNU General Public License zusammen mit diesem Programm erhalten haben.
Falls nicht, siehe <http://www.gnu.org/licenses/>.

Erlaeutern Paser:
Der eingegebene Mathematische Ausdruck wird dem Parser uebergeben. Der Parser wandelt die Zeichenkette
in char-Array um und durchsucht diese nach Operatoren (+ - * /) und zaehlt diese.
Anschliessend werden Arrays fuer Zahlen (double[]) und Operatoren (char[]) angelegt. Nun wird das
char-Array Element fuer Element nach Operatoren durchsucht und merkt sich deren Positionen im char-Array,
was der Position im String-Array entspricht. Somit koennen die Operanden von den Operatoren selektiert
werden und in den entsprechenden Arrays abgelegt werden.

 Erlaeuterung Solver:
 Der Solver durchlaeuft das Array fuer die Operanden elementeweise. Dann wird der folgende Operator
 aus dem Operator-Array angefragt, der naechste Operand aus dem Operanden-Array geholt und die entsprechende
 mathematische Operation durchgefuehrt und dem Gesamtergebnis zugefuehrt. Das Endergebnis wird anschliessend
 zurueckgegeben.  
*/

public class SimpleMathSolver {
	
	private double[] numbers;				// double-Array zum Speichern der Zahlen 
	private char[] arithmeticOperations;	// char-Array zum Speichern der Rechenoperationszeichen

	/**
	 * parse:
	 * Analyse des mathematischen Ausdrucks und
	 * trennen in Operationszeichen und Operanden.
	 * Konvertieren der Zahlen und speichern in double-Array.
	 * Speichern der Operatoren in char-Array
	 * 
	 * @param expression
	 */
	private void parse(String expression)
	{
		char[] expressionAsChars = expression.toCharArray();	// Zeichenkette zeichenweise als char-Array
		char buf;						// Zwischenspeicher fuer ein beliebiges Zeichen
		
		int countOperators = 0;	// Zaehlvariable fuer Rechenoperationszeichen
		int start = 0;			// Startposition einer Zahl
				
		// Zaehlen der Rechenoperationen
		for(int i=1; i<expressionAsChars.length; i++)
		{
			buf = expressionAsChars[i];
			if(buf == '+' || buf == '-' || buf == '*' || buf == '/')
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
			if(buf == '+' || buf == '-' || buf == '*' || buf == '/')
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
		double erg, b;	// Speichervariablen
		
		parse(expression);	// Parser aufrufen
		
		erg = numbers[0];	// Ersten Operanden in erg speichern
		
		// Operanden-Array durchlaufen
		for(int i=1; i<numbers.length; i++)
		{
			b = numbers[i];	// Werte holen
			
			// Abfrage der Rechenoperation und durchfuehren
			// der entsprechenden Berechnung
			switch(arithmeticOperations[i-1])
			{
				case '+':
					erg = erg + b;
					break;
					
				case '-':
					erg = erg - b;
					break;
					
				case '*':
					erg = erg * b;
					break;
					
				case '/':
					erg = erg / b;
					break;
			}
		}
		
		return erg;
	}

	public static void main(String[] args) {

		SimpleMathSolver p = new SimpleMathSolver();
		
		String expression = "1+20-300+4000*2/4";
		
		double ergebnis = p.eval(expression);
		
		System.out.println(expression+"="+ergebnis);	// 1860,5
	}
}
