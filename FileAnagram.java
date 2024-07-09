import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileAnagram {
    public void read() throws FileNotFoundException {
        File f = new File("src/data.txt");

        Scanner scan = new Scanner(f);
        while (scan.hasNext()) {
            String anagram1 = scan.next();
            String anagram2 = scan.next();


            anagram1.toLowerCase();
            anagram2.toLowerCase();
            Map<Character, Integer> s1 = new HashMap();
            for (int i = 0; i < anagram1.length(); i++) {
                char key = anagram1.charAt(i);

                Integer value = s1.get(key);
                if (value == null) {
                    value = 1;
                } else {
                    value++;
                }
                s1.put(key, value);
            }
            Map<Character, Integer> s2 = new HashMap();
            for (int i = 0; i < anagram2.length(); i++) {
                char key = anagram2.charAt(i);

                Integer value2 = s2.get(key);
                if (value2 == null) {
                    value2 = 1;
                } else {
                    value2++;
                }
                s2.put(key, value2);
            }


            if (s1.equals(s2)) {
                System.out.println(anagram1+" "+anagram2+"            "+"====== this is  Anagram ====== \n ");
            } else System.out.println(anagram1+" "+anagram2+"         "+"====== this is  Not Anagram ====== \n ");
        }
    }

}



