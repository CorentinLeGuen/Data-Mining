package main.java;

import com.saxonica.xqj.SaxonXQDataSource;

import javax.xml.xquery.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Convert {
    private final static String pathName = "xml/";
    private final static String fileName = "inflammation";
    private final static String extensionName = "xml";

    public static void main(String[] args) throws Exception {
        SaxonXQDataSource ds = new SaxonXQDataSource();
        XQConnection con = ds.getConnection();
        String query = "let $fdd := doc(\"" + pathName + fileName + "." + extensionName + "\")/PubmedArticles/PubmedArticle\n" +
                "for $article in $fdd\n" +
                "return ('\nT.',$article//ArticleTitle/text(), '\nA.',$article//Abstract/AbstractText/text())";
        XQPreparedExpression expr = con.prepareExpression(query);
        XQSequence result = expr.executeQuery();

        File f = new File("files/" + fileName + ".txt");

        if (f.createNewFile()){
            System.out.println("File " + f.getName() + " is created!");
        } else {
            System.out.println("File " + f.getName() + "already exists.");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));

            writer.write(result.getSequenceAsString(null));

            writer.close();
        } catch (Exception e) {
            System.err.println("Error while writing in the file " + fileName + " : " + e.getMessage());
        }

        result.close();
        expr.close();
        con.close();
    }
}
