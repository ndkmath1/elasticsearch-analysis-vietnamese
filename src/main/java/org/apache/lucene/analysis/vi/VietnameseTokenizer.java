/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.apache.lucene.analysis.vi;

import ai.vitk.type.Token;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;


/**
 * Vietnamese Tokenizer.
 *
 * @author duydo
 */
public class VietnameseTokenizer extends Tokenizer {

    private List<Token> tokens;
    Iterator<Token> tokenIterator;

    private int offset = 0;
    private int skippedPositions;


    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    private ai.vitk.tok.Tokenizer tokenizer;

    public VietnameseTokenizer() {
        super();
        tokenizer = TokenizerUtils.getTokenizer();
    }

    @Override
    public final boolean incrementToken() throws IOException {
        clearAttributes();
        while (tokenIterator.hasNext()) {
            Token t = tokenIterator.next();
            String word = t.getWord();
            if (accept(word)) {
                posIncrAtt.setPositionIncrement(skippedPositions + 1);
                typeAtt.setType(TypeAttribute.DEFAULT_TYPE);
                final int length = word.length();
                termAtt.copyBuffer(word.toCharArray(), 0, length);
                offsetAtt.setOffset(correctOffset(offset), offset = correctOffset(offset + length));
                offset++;
                return true;
            }
            skippedPositions++;
        }
        return false;
    }

    /**
     * Only accept the word characters.
     */
    private final boolean accept(String token) {
        if (token.length() == 1) {
            return Character.isLetterOrDigit(token.charAt(0));
        }
        return true;
    }

    @Override
    public final void end() throws IOException {
        super.end();
        final int finalOffset = correctOffset(offset);
        offsetAtt.setOffset(finalOffset, finalOffset);
        posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement() + skippedPositions);
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        offset = 0;
        skippedPositions = 0;
        tokenize(input);
    }

    private void tokenize(Reader input) {
        int numChars;
        char[] buffer = new char[1024];
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((numChars =
                    input.read(buffer, 0, buffer.length)) != -1) {
                stringBuilder.append(buffer, 0, numChars);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        String stringToTokenize = stringBuilder.toString().toLowerCase();
        this.tokens = tokenizer.tokenize(stringToTokenize);
        this.tokenIterator = tokens.iterator();
    }

    public ai.vitk.tok.Tokenizer get() {
        return tokenizer;
    }

}
