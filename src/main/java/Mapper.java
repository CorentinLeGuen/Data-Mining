package main.java;

import com.saxonica.xqj.SaxonXQDataSource;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQSequence;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mapper {

    private final static String pathName = "xml";
    private final static String fileName = "inflammation";
    private final static String matriceFileName = "inflammationMatrice";
    private final static String extensionName = "xml";

    private final static String taggedFile = "files/inflammationTagged";
    private final static String termList = taggedFile + "/default/raw/termList.txt";

    public static void main(String[] args) throws Exception {


        Map<String, List<Boolean>> occurences = new HashMap<String, List<Boolean>>();

        List<String> terms = new ArrayList<String>();


        //Sauvegarde des termes
        BufferedReader br = new BufferedReader(new FileReader(termList));
        try {
            String line = br.readLine();
            Pattern p = Pattern.compile("^(.*)(?:\\s+\\d+){2}$");

            while (line != null) {

                line = br.readLine();
                //System.out.println(line);
                try {
                    Matcher m = p.matcher(line);
                    m.matches();
                    //System.out.println(m.group(1));
                    terms.add(m.group(1));
                } catch (Exception e) {}

            }
        } finally {
            br.close();
        }

        //Lecture du corpus
        SaxonXQDataSource ds = new SaxonXQDataSource();
        XQConnection con = ds.getConnection();
        String query = "let $fdd := doc(\"" + pathName + "/" + fileName + "." + extensionName + "\")/PubmedArticles/PubmedArticle\n" +
                "for $article in $fdd\n" +
                "return ('\n',$article//PMID/text(), ' ',$article//Abstract/AbstractText/text())";
        XQPreparedExpression expr = con.prepareExpression(query);
        XQSequence result = expr.executeQuery();
        String originFile = result.getSequenceAsString(null);
        result.close();
        expr.close();
        con.close();

        //Création de la matrice
        Pattern p = Pattern.compile("^(\\d*)\\s(.*)$", Pattern.MULTILINE);
        Matcher m = p.matcher(originFile);

        while (m.find()) {
            List<Boolean> occurence = new ArrayList<Boolean>();
            String text = m.group(2);
            System.out.println(m.group(1));
            for(int i = 0; i < terms.size(); ++i) {
                occurence.add(i, text.contains(terms.get(i)));
            }
            occurences.put(m.group(1), occurence);
        }

        //Exportation de la matrice
        File f = new File("files/" + matriceFileName + ".csv");

        if (f.createNewFile()){
            System.out.println("File " + f.getName() + " is created!");
        } else {
            System.out.println("File " + f.getName() + " already exists.");
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        writer.write('?');
        for(String term : terms) {
            writer.write( term.replace(' ', '_') + ",");
        }
        writer.write("\n");
        Set<String> pmids = occurences.keySet();
        for(String pmid : pmids) {
            writer.write(pmid + ",");
            List<Boolean> occurence = occurences.get(pmid);
            for (Boolean o : occurence) {
                writer.write(o.toString() + ",");
            }
            writer.write("\n");
        }
        writer.close();

    }
}
