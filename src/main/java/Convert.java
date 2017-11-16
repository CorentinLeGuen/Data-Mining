package main.java;

import com.saxonica.xqj.SaxonXQDataSource;

import javax.xml.xquery.*;
import java.io.*;

public class Convert {
    private final static String pathName = "xml";
    private final static String fileName = "alcoholism";
    private final static String extensionName = "xml";

    private final static String command = "tree-tagger-linux-3.2.1/bin/tree-tagger";

    public static void main(String[] args) throws Exception {
        SaxonXQDataSource ds = new SaxonXQDataSource();
        XQConnection con = ds.getConnection();
        String query = "let $fdd := doc(\"" + pathName + "/" + fileName + "." + extensionName + "\")/PubmedArticles/PubmedArticle\n" +
                "for $article in $fdd\n" +
                "return ('\nT. ',$article//ArticleTitle/text(), '\nA. ',$article//Abstract/AbstractText/text())";
        XQPreparedExpression expr = con.prepareExpression(query);
        XQSequence result = expr.executeQuery();

        File f = new File("files/" + fileName + ".txt");

        if (f.createNewFile()){
            System.out.println("File " + f.getName() + " is created!");
        } else {
            System.out.println("File " + f.getName() + " already exists.");
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

        InputStream stream = null;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.redirectErrorStream(true);
            processBuilder.command(command, "files/english-utf8.par",
                    "files/" + fileName + ".txt",
                    "files/" + fileName + "Result.txt",
                    "-token", "-lemma", "-tokenization", "built-in");

            Process p = processBuilder.start();

            stream = p.getInputStream();

            int i;
            char c;
            while((i = stream.read())!=-1) {
                c = (char)i;
                System.out.print(c);
            }

        } catch (Exception e) {
            System.err.println("Error in the execution of " + command + " : " + e.getMessage());
        } finally {
            if (stream != null)
                stream.close();
        }
    }
}
