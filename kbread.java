import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class kbread {
    private BufferedReader file;
    private LinkedList<String> list;

    public static void main(String[] stringArray) throws FileNotFoundException, IOException {
        kbread kbread2 = new kbread("test1");
        LinkedList<String> linkedList = kbread2.read();
        for (int i = 0; i < linkedList.size(); ++i) {
            System.out.println(linkedList.get(i));
        }
    }

    kbread(String string) throws FileNotFoundException {
        FileReader fileReader = new FileReader(string);
        this.file = new BufferedReader(fileReader);
        this.list = new LinkedList();
    }

    LinkedList<String> read() throws IOException {
        String string = this.file.readLine();
        while (string != null) {
            if (string.compareTo("\n") != 0 && string.compareTo("\r") != 0 && string.length() > 2) {
                this.list.add(string);
                string = this.file.readLine();
                continue;
            }
            this.list.add(null);
            string = this.file.readLine();
        }
        return this.list;
    }
}
