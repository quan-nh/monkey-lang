package org.example.ast;

import lombok.Getter;
import lombok.Setter;
import org.example.token.Token;

@Getter
@Setter
public class LetStatement implements Statement{
    private Token token;
    private Identifier name;
    private Expression value;

    @Override
    public String tokenLiteral() {
        return token.getLiteral();
    }
}
