package main.java;

import com.saxonica.xqj.SaxonXQDataSource;
import javax.xml.xquery.*;

public class Convert {
    public static void main(String[] args) throws Exception {
        SaxonXQDataSource ds = new SaxonXQDataSource();
        XQConnection con = ds.getConnection();
        String query = "let $fdd := doc(\"xml/inflammation.xml\")/PubmedArticles/PubmedArticle\n" +
                "for $article in $fdd\n" +
                "return ('\nT.',$article//ArticleTitle/text(), '\nA.',$article//Abstract/AbstractText/text())";
        XQPreparedExpression expr = con.prepareExpression(query);
        XQSequence result = expr.executeQuery();

        // prints "<hello-world>2</hello-world>"
        System.out.println(result.getSequenceAsString(null));

        result.close();
        expr.close();
        con.close();
    }
}
