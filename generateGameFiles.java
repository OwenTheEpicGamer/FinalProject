import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class generateGameFiles {

    public static void main(String[] args) throws IOException {
        //makeDict();
        makePrefixes();
    }

    public static void makeDict() throws IOException {
        HashSet<String> dict = new HashSet<String>();
        File dictFile = new File("dictionary.txt");
        Scanner dictReader = new Scanner(dictFile);

        while (dictReader.hasNext()) {
            dict.add(dictReader.next());
        }

        FileOutputStream saveFile = new FileOutputStream("dictHashSet");
        ObjectOutputStream outFile = new ObjectOutputStream(saveFile);
        outFile.writeObject(dict);
        outFile.close();
    }

    public static void makePrefixes() throws IOException {
        HashSet<String> prefixes = new HashSet<String>();

        File dictFile = new File("dictionary.txt");
        Scanner dictReader = new Scanner(dictFile);

        while (dictReader.hasNext()) {
            String word = dictReader.next();
            switch (word.length()) {
                default:
                case 4:
                    prefixes.add(word.substring(0, 4));
                case 3:
                    prefixes.add(word.substring(0, 3));
                case 2:
                    prefixes.add(word.substring(0, 2));
                case 1:
            }
        }

        FileOutputStream saveFile = new FileOutputStream("prefixesHashSet");
        ObjectOutputStream outFile = new ObjectOutputStream(saveFile);
        outFile.writeObject(prefixes);
        outFile.close();
    }



}
