package main.java;

import com.saxonica.xqj.SaxonXQDataSource;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQSequence;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Mapper {

    private final static String pathName = "xml";
    private final static String fileName = "alcoholism";
    private final static String extensionName = "xml";

    public static void main(String[] args) throws Exception {
        SaxonXQDataSource ds = new SaxonXQDataSource();
        XQConnection con = ds.getConnection();
        String query = "let $fdd := doc(\"" + pathName + "/" + fileName + "." + extensionName + "\")/PubmedArticles/PubmedArticle\n" +
                "for $article in $fdd\n" +
                "return ('\nT. ',$article//ArticleTitle/text(), '\nA. ',$article//Abstract/AbstractText/text())";
        XQPreparedExpression expr = con.prepareExpression(query);
        XQSequence result = expr.executeQuery();
        

        result.close();

        expr.close();
        con.close();

    }
}
