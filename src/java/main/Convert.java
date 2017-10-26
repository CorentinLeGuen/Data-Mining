package main;


import com.saxonica.xqj.SaxonXQConnection;
import com.saxonica.xqj.SaxonXQDataSource;
import com.saxonica.xqj.SaxonXQPreparedExpression;

import javax.xml.xquery.*;
import java.io.FileInputStream;


public class Convert {
    public static void main(String[] args) throws Exception {
        SaxonXQDataSource ds = new SaxonXQDataSource();
        XQConnection con = ds.getConnection();
        String query = "let $fdd := doc(\"inflammation.xml\")/PubmedArticles/PubmedArticle\n" +
                "for $article in $fdd\n" +
                "return ($article//ArticleTitle/text(), $article//Abstract/AbstractText/text())";
        XQPreparedExpression expr = con.prepareExpression(query);
        XQSequence result = expr.executeQuery();

        // prints "<hello-world>2</hello-world>"
        System.out.println(result.getSequenceAsString(null));

        result.close();
        expr.close();
        con.close();
    }
}
