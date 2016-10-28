package com.gvs.crm.avaliador;

public class Tokenizer {
	private String string;

	private String delimiters = " \t\n\r\f";

	private String quoteChars = "'\"";

	private char startQuoteChar = '[';

	private char endQuoteChar = ']';

	private int position = 0;

	private String currentToken;

	public Tokenizer(String string) {
		this.string = string;
	}

	public void setDelimiter(String delimiters) {
		this.delimiters = delimiters;
	}

	public void setQuoteChars(String quoteChars) {
		this.quoteChars = quoteChars;
	}

	public void setSpecialQuoteChars(char startQuoteChar, char endQuoteChar) {
		this.startQuoteChar = startQuoteChar;
		this.endQuoteChar = endQuoteChar;
	}

	public int skipDelimiters(int startPosition) {
		int currentPosition = startPosition;
		while (currentPosition < this.string.length()
				&& this.delimiters.indexOf(this.string.charAt(currentPosition)) >= 0)
			currentPosition++;
		return currentPosition;
	}

	private int scanToken(int startPosition) {
		if (startPosition >= this.string.length())
			return startPosition;
		int currentPosition = startPosition;
		if (this.quoteChars.indexOf(this.string.charAt(currentPosition)) >= 0) {
			currentPosition++;
			while (currentPosition < this.string.length()
					&& this.quoteChars.indexOf(this.string
							.charAt(currentPosition)) < 0)
				currentPosition++;
			currentPosition++;
		} else if (this.string.charAt(currentPosition) == this.startQuoteChar) {
			currentPosition++;
			while (currentPosition < this.string.length()
					&& this.string.charAt(currentPosition) != this.endQuoteChar)
				currentPosition++;
			currentPosition++;
		} else {
			currentPosition++;
			while (currentPosition < this.string.length()) {
				if (this.quoteChars
						.indexOf(this.string.charAt(currentPosition)) >= 0)
					break;
				if (this.string.charAt(currentPosition) == this.startQuoteChar)
					break;
				if (this.delimiters
						.indexOf(this.string.charAt(currentPosition)) >= 0)
					break;
				currentPosition++;
			}
		}
		return currentPosition;
	}

	public int countTokens() {
		int count = 0;
		int currentPosition = 0;
		while (currentPosition < this.string.length() - 1) {
			currentPosition = skipDelimiters(currentPosition);
			currentPosition = scanToken(currentPosition);
			count++;
		}
		return count;
	}

	public String nextToken() {
		this.currentToken = null;
		int newPosition;
		this.position = this.skipDelimiters(this.position);
		newPosition = this.scanToken(this.position);
		if (newPosition > this.position) {
			this.currentToken = this.string.substring(this.position,
					newPosition);
			this.position = newPosition;
		}
		return this.currentToken;
	}

	public String currentToken() {
		return this.currentToken;
	}
}