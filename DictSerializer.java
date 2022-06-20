import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class DictSerializer {
    public static void main(String args[]) throws FileNotFoundException {
        HashSet<String> dict = new HashSet<String>();
        File dictFile = new File("dictionary.txt");
        Scanner dictReader = new Scanner(dictFile);
        dict.add(dictReader.nextLine());


    }
}
