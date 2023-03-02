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
public class LetStatement implements Statement {
    private Token token;
    private Identifier name;
    private Expression value;

    @Override
    public String tokenLiteral() {
        return token.getLiteral();
    }

    @Override
    public String toString() {
        var out = new StringBuilder();
        out.append(tokenLiteral())
                .append(" ")
                .append(this.name.toString())
                .append(" = ");
        if (this.value != null) {
            out.append(this.value);
        }
        out.append(";");

        return out.toString();
    }
}
