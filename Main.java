// package hamzah;

// Notes
// Always use a package in Java, and don't use the default package, like the above example.

import java.util.*;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		DrawMenu();
	}

	public static void DrawMenu() {
		try {
			System.out.println(" enter your choice");
			System.out.println("1. test the first soulotion");
			System.out.println("2. test the sceond soulotion");
			System.out.println("3. test the ebra soulotion without java map");
			System.out.println("4. quit");
			int choice = input.nextInt();
			switch (choice) {
			case 1:
				check1();
			case 3:
				ebraCheckWithoutMap();
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

	public static void check1() {
		System.out.println(" enter the first sent");
		String sent1 = input.next();
		System.out.println("enter the second  sent");
		String sent2 = input.next();
		if (sent1.length() < 2 || sent2.length() < 2) {
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

				System.out.println("========= Not Anagram =========");
				return;
			} else if (total.size() != sent1.length()) {
				System.out.println("========= Not Anagram =========");
				return;
			}

			for (int i = total.size() - 1; i >= 0; i--) {
				if (total.get(i).equals(sent1.charAt(i))) {
					total.remove(i);
				}
			}

			if (total.isEmpty()) {
				System.out.println("========= Anagram =========");

			} else
				System.out.println("========= Not Anagram =========");
                // Always use brackets with if, eles, switch, for, and while... even if it have one line.
                
		} else {
			System.out.println("========= Not Anagram =========");
		}
	}

	public static void check2() {
		System.out.println(" enter the first sent");
		String sent1 = input.next();
		System.out.println("enter the second  sent");
		String sent2 = input.next();
		if (sent1.length() < 2 || sent2.length() < 2) {
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

	public static void ebraCheckWithoutMap() {
		System.out.println("Enter the first word");
		String word1 = input.next();
		System.out.println("Enter the second word");
		String word2 = input.next();
		if (word1.length() < 2 || word2.length() < 2) {
			System.out.println("Error: please enter 2 letters or more");
			return;
		}

		if (word1.length() != word2.length()) {
			System.out.println("========= Not Anagram =========");
			return;
		}

		word1.toLowerCase();
		word2.toLowerCase();

		// Use java 8 stream to break down the String object into sorted characters list
		List<Character> word1Chars = word1.chars().mapToObj(i -> (char) i).sorted().collect(Collectors.toList());
		List<Character> word2Chars = word2.chars().mapToObj(i -> (char) i).sorted().collect(Collectors.toList());

		for (int j = 0; j < word1Chars.size(); j++) {
			char char1 = word1Chars.get(j);
			char char2 = word2Chars.get(j);
			
			if (char1 != char2) {
				System.out.println("========= Not Anagram =========");
				return;
			}
		}
		// If the code arrive here then it is Anagram
		System.out.println("========= Anagram =========");
	}
}
