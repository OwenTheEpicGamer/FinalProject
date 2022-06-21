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
        System.out.println(dict.size());

    }

    public static void makePrefixes() throws IOException {
        HashSet<String> prefixes = new HashSet<String>();

        File dictFile = new File("dictionary.txt");
        Scanner dictReader = new Scanner(dictFile);

        while (dictReader.hasNext()) {
            String word = dictReader.next();
            for(int index = 0; index < word.length() && index < 9; index++) {
                prefixes.add(word.substring(0, index));
            }
        }
        FileOutputStream saveFile = new FileOutputStream("prefixesHashSet");
        ObjectOutputStream outFile = new ObjectOutputStream(saveFile);
        outFile.writeObject(prefixes);
        outFile.close();
        System.out.println("size of prefixes: " + prefixes.size());
    }


}
