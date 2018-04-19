package test;

import ai.vitk.tok.Tokenizer;
import ai.vitk.type.Token;
import org.apache.lucene.analysis.vi.TokenizerUtils;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by khanh on 02/04/2018.
 */
public class Test {

    public static void main(String[] args) throws FileNotFoundException {
        long t1 = System.currentTimeMillis();
        Tokenizer t = TokenizerUtils.getTokenizer();
        long t2 = System.currentTimeMillis();
        System.out.println("init time: " + (t2-t1));
        t1 = System.currentTimeMillis();
        String s = "Hà Nội là thủ đô của Việt Nam.";
        List<Token> list = t.tokenize(s);
        for (Token tk: list) {
            System.out.println(tk.getWord());
        }
        t2 = System.currentTimeMillis();
        System.out.println("tokenize time: " + (t2-t1));

    }

}
