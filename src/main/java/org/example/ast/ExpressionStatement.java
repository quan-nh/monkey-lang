package org.example.ast;

import lombok.Getter;
import lombok.Setter;
import org.example.token.Token;

@Getter
@Setter
public class ExpressionStatement implements Statement {
    private Token token; // the first token of the expression
    private Expression expression;

    @Override
    public String tokenLiteral() {
        return this.token.getLiteral();
    }

    @Override
    public String toString() {
        if (expression != null) {
            return expression.toString();
        }
        return "";
    }
}
