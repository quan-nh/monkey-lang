package org.example.ast;

import lombok.Getter;
import lombok.Setter;
import org.example.token.Token;

@Getter
@Setter
public class ReturnStatement implements Statement {
    private Token token; // 'return' token
    private Expression returnValue;

    @Override
    public String tokenLiteral() {
        return this.token.getLiteral();
    }

    @Override
    public String toString() {
        var out = new StringBuilder();

        out.append(tokenLiteral()).append(" ");

        if (returnValue != null) {
            out.append(returnValue);
        }

        out.append(";");

        return out.toString();
    }
}
