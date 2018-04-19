package org.apache.lucene.analysis.vi;

import ai.vitk.tok.Tokenizer;

/**
 * Created by khanh on 25/03/2018.
 */
public class TokenizerUtils {

    private static Tokenizer tokenizer;

    public static Tokenizer getTokenizer() {
        if (tokenizer == null) {
            tokenizer = new Tokenizer();
        }
        return tokenizer;
    }

}
