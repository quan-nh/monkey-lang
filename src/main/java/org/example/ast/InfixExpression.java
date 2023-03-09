package org.example.ast;

import lombok.Getter;
import lombok.Setter;
import org.example.token.Token;

@Getter
@Setter
public class InfixExpression implements Expression {
    private Token token;
    private Expression left;
    private String operator;
    private Expression right;

    @Override
    public String tokenLiteral() {
        return this.token.getLiteral();
    }

    @Override
    public String toString() {
        return "(" + this.left.toString() + " " + this.operator + " " + this.right.toString() + ")";
    }
}
