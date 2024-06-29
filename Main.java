import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        DrawMenu();


    }

       public static void check1() {
        System.out.println(" enter the first sent");
        String sent1 = input.next();
        System.out.println("enter the second  sent");
        String sent2 = input.next();
          if (  sent1.length()<2 || sent2.length()<2){
              System.out.println("error :   pleas enter 2 letters or more");
return;
          }

          sent1.toLowerCase();
          sent2.toLowerCase();
        Stack<Character> total = new Stack<>();

            if (sent1.length() == sent2.length()) {
                for (int i = 0; i < sent1.length(); i++) {
                    for (int j = 0; j < sent2.length(); j++) {
                        if (sent1.charAt(i) == sent2.charAt(j)) {
                            total.push(sent1.charAt(i));
                            break;
                        }
                    }
                }

                if (total.isEmpty()) {


                    System.out.println("=========Not Anagram =========");
                    return;
                }
                else if (total.size()!=sent1.length()){
                    System.out.println("=========Not Anagram =========");
                    return;
                }


                for (int i = total.size() - 1; i >= 0; i--) {
                    if (total.get(i).equals(sent1.charAt(i))) {
                        total.remove(i);
                    }
                }

                if (total.isEmpty()) {
                    System.out.println("========= Anagram =========");

                } else System.out.println("=========Not Anagram =========");
            }
                 else
          System.out.println("=========Not Anagram =========");
        }

       public static void DrawMenu(){
    try {

        System.out.println(" enter your choice");
        System.out.println("1 . test the first soulotion");
        System.out.println("2 . test the sceond soulotion");
        System.out.println(" --- 3. quite ---");
        int choice = input.nextInt();
        switch (choice) {
            case 1:
                check1();
            case 2:

            default:
                break;
        }

    } catch (NoSuchElementException e) {
        System.out.println("================= re enter A valid value please =================\n");
        input.nextLine();
        DrawMenu();
    }
}

public static void check2(){
    System.out.println(" enter the first sent");
    String sent1 = input.next();
    System.out.println("enter the second  sent");
    String sent2 = input.next();
    if (  sent1.length()<2 || sent2.length()<2){
        System.out.println("error :   pleas enter 2 letters or more");
        return;
    }

    sent1.toLowerCase();
    sent2.toLowerCase();
    Stack<Character> total = new Stack<>();
    if (sent1.length() == sent2.length()) {
        for (int i = 0; i < sent1.length(); i++) {

            for (int j = 0; j < sent2.length(); j++) {
                if (sent1.charAt(i) == sent2.charAt(j)) {
                    total.push(sent1.charAt(i));
                    break;
                }

            }
        }

}
    }
}

