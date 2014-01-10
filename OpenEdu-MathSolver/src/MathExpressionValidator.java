/*
MathExpressionValidator.java:
Simpler Validator, zum Ueberpruefen  von mathematischen
Ausdruecken auf richtigkeit

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

public class MathExpressionValidator {
	
	private String expression = null;
	/**
	 * validate:
	 * Loeschen von Leerzeichen
	 * Ersetzen von Kommata durch Punkt
	 * Aufruf der Ueberpruefungsmethode
	 * Ergebnis zurueckgeben
	 * 
	 * @param expression
	 * @return
	 */
	public boolean validate(String expression)
	{
		char[] expressionAsChars;	// Zeichenkette zeichenweise als char-Array

		expression = expression.replace(" ", "");	// Alle Leerzeichen entfernen
		expression = expression.replace(",", ".");	// Kommata durch Punkt ersetzen
		expression = expression.replace("+-", "-");	// +- durch - ersetzen
		
		expressionAsChars = expression.toCharArray();	// Zeichenkette als char-Array speichern
		
		this.expression = expression;
		
		return validationTest(expressionAsChars);	// Validation ausfuehren und Ergebnis ausgeben
	}
	
	public String getExpression()
	{
		return expression;
	}
	
	private boolean validationTest(char[] expressionAsChars)
	{
		boolean valid = true;	// Ergebnisvariable, als ture vorinitialisiert
		char buf1, buf2;		// Buffervariable fuer aufeinander folgende Zeichen
		
		// char-array durchlaufen
		for(int i=0; i<expressionAsChars.length-1; i++)
		{
			buf1 = expressionAsChars[i];	// Zeichen und ...
			buf2 = expressionAsChars[i+1];	// nachfolgendes Zeichen zwischenspeichern 
			
			if(buf1 == '+' || buf1 == '-' || buf1 == '*' || buf1 == '/' || buf1 == '.') // wenn buf1 eines der gesuchten Zeichen ist ...
				if(buf2 == '+' || buf2 == '-' || buf2 == '*' || buf2 == '/' || buf2 == '.') // und buf2 ebenfalls, dann ...
					valid = false;	// ... ist der Ausdruck ungueltig
		}
		
		return valid;	// Ergebnis zurueckgeben
	}

	public static void main(String[] args) {

		// Testvariablen anlegen
		String expression1 = "-1+20-300+-4000*2/4-10*2.5*2.5-2*3";
		String expression2 = "-1++20-300+  4000*2// 4-10*2.5*2.5+-2/*3";
		
		// Ergebnisvariable
		boolean erg;
		
		// Validator-Objekt erzeugen
		MathExpressionValidator val = new MathExpressionValidator();
		
		// Ueberpruefung durchfuehren
		erg = val.validate(expression1);
		
		// Ueberpruefungsergebnis ausgeben
		System.out.println("Gueltigkeit pruefen von: "+expression1+" -> "+erg);
		System.out.println("Gueltigkeit pruefen von: "+val.getExpression()+" -> "+erg);

	}

}
