package org.example.ast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.token.Token;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Identifier implements Expression {

    private Token token;
    private String value;

    @Override
    public String tokenLiteral() {
        return token.getLiteral();
    }
}
