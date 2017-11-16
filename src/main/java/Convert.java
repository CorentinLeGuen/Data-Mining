package main.java;

import com.saxonica.xqj.SaxonXQDataSource;

import javax.xml.xquery.*;

public class Convert {
    private final static String pathName = "xml/inflammation.xml";

    public static void main(String[] args) throws Exception {
        SaxonXQDataSource ds = new SaxonXQDataSource();
        XQConnection con = ds.getConnection();
        String query = "let $fdd := doc(\"" + pathName + "\")/PubmedArticles/PubmedArticle\n" +
                "for $article in $fdd\n" +
                "return ($article//ArticleTitle/text(), $article//Abstract/AbstractText/text())";
        XQPreparedExpression expr = con.prepareExpression(query);
        XQSequence result = expr.executeQuery();

        System.out.println(result.getSequenceAsString(null));

        result.close();
        expr.close();
        con.close();
    }
}
