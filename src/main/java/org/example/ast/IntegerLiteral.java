package org.example.ast;

import lombok.Getter;
import lombok.Setter;
import org.example.token.Token;

@Getter
@Setter
public class IntegerLiteral implements  Expression{
    private Token token;
    private long value;

    @Override
    public String tokenLiteral() {
        return this.token.getLiteral();
    }

    @Override
    public String toString() {
        return this.token.getLiteral();
    }
}
