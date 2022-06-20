import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class DictSerializer {
    public static void main(String args[]) throws IOException {
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


}
