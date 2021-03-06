package net.ranzer.caexbot.functions.dice.expressions;
import java.util.ArrayList;

import net.ranzer.caexbot.functions.dice.Lexer;

public class Multiplication extends Expression {

	public Multiplication(ArrayList<Lexer.Token> tokens) {
		super(tokens);

		boolean twoMultiplications = false;

		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).type == Lexer.TokenType.MULTIPLICATION) {
				twoMultiplications = true;
				Multiplication a = new Multiplication(new ArrayList<>(tokens.subList(0, i)));
				Multiplication b = new Multiplication(new ArrayList<>(tokens.subList(i + 1, tokens.size())));
				value = a.value * b.value;
				description = a.description + " * " + b.description;
			}
		}

		if (!twoMultiplications) {
			Negation negation = new Negation(tokens);
			value = negation.value;
			description = negation.description;
		}

	}

}