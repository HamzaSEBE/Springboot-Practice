// package hamzah;

//Notes
//Always use a package in Java, and don't use the default package, like the above example.

import java.util.*;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
//click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {
	static Scanner input = new Scanner(System.in);
	static String word1;
	static String word2;

	public static void main(String[] args) {
		DrawMenu();
	}

	public static void DrawMenu() {
		try {
			System.out.println(" enter your choice");
			System.out.println("1. test the first soulotion");
			System.out.println("2. test the sceond soulotion using map ");
			System.out.println("3. test the ebra soulotion without java map");
			System.out.println("4. test the ebra soulotion using java map");
			System.out.println("5. quit");
			int choice = input.nextInt();

			switch (choice) {
			case 1:
				startOperation(Main::check1);
				break;
			case 2:
				startOperation(Main::check2);
				break;
			case 3:
				startOperation(Main::ebraCheckWithoutMap);
				break;
			case 4:
				startOperation(Main::ebraCheckWithMap);
				
			default:
				break;
			}

		} catch (NoSuchElementException e) {
			System.out.println("================= re enter A valid value please =================\n");
			input.nextLine();
			DrawMenu();
		}
	}

	private static void startOperation(Runnable runnable) {
		// Let user input the two words
		inputWords();

		// Start count operation time
		long startTime = System.nanoTime();

		// Run the passed check method
		runnable.run();

		// Calculate operation time
		long totalTime = System.nanoTime() - startTime;
		System.out.println("Time: " + (totalTime/1000) + "mS");
	}

	private static void inputWords() {
		System.out.println("Enter the first word");
		word1 = input.next();

		System.out.println("Enter the second word");
		word2 = input.next();

		if (word1.length() < 2 || word2.length() < 2) {
			System.out.println("Error: please the word must be 2 letters or more");
			System.exit(0);
		}

		if (word1.length() != word2.length()) {
			System.out.println("========= Not Anagram =========");
			System.exit(0);
		}
	}

	public static void check1() {
		Stack<Character> total = new Stack<>();

		if (word1.length() == word2.length()) {
			for (int i = 0; i < word1.length(); i++) {
				for (int j = 0; j < word2.length(); j++) {
					if (word1.charAt(i) == word2.charAt(j)) {
						total.push(word1.charAt(i));
						break;
					}
				}
			}

			if (total.isEmpty()) {

				System.out.println("========= Not Anagram =========");
				return;
			} else if (total.size() != word1.length()) {
				System.out.println("========= Not Anagram =========");
				return;
			}

			for (int i = total.size() - 1; i >= 0; i--) {
				if (total.get(i).equals(word1.charAt(i))) {
					total.remove(i);
				}
			}

			if (total.isEmpty()) {
				System.out.println("========= Anagram =========");

			} else
				System.out.println("========= Not Anagram =========");
			// Always use brackets with if, eles, switch, for, and while... even if it have
			// one line.

		} else {
			System.out.println("========= Not Anagram =========");
		}
	}

	public static void check2() {
		Map<Character, Integer> s1 = new HashMap<>();
		for (int i = 0; i < word1.length(); i++) {
			char key = word1.charAt(i);

			Integer value = s1.get(key);
			if (value == null) {
				value = 1;
			} else {
				value++;
			}
			s1.put(key, value);
		}

		Map<Character, Integer> s2 = new HashMap<>();
		for (int i = 0; i < word2.length(); i++) {
			char key = word2.charAt(i);

			Integer value2 = s2.get(key);
			if (value2 == null) {
				value2 = 1;
			} else {
				value2++;
			}
			s2.put(key, value2);
		}

		System.out.println(s1);
		System.out.println(s2);

		if (s1.equals(s2)) {
			System.out.println("====== Anagram ====== ");
		} else {
			System.out.println("====== Not Anagram ====== ");
		}
	}

	public static void ebraCheckWithoutMap() {
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
			// If the code arrive here then it is Anagram
			System.out.println("========= Anagram =========");
		}

	}

	public static void ebraCheckWithMap() {
		Map<Character, Integer> firstWordMap = new HashMap<>();
		for (char ch : word1.toCharArray()) {
			Integer value = firstWordMap.get(ch);
			firstWordMap.put(ch, value == null ? 1 : value++);
		}
		
		Map<Character, Integer> secondWordMap = new HashMap<>();
		for (char ch : word2.toCharArray()) {
			Integer value = secondWordMap.get(ch);
			secondWordMap.put(ch, value == null ? 1 : value++);
		}

		System.out.println(firstWordMap);
		System.out.println(secondWordMap);

		if (firstWordMap.equals(secondWordMap)) {
			System.out.println("====== Anagram ====== ");
		} else {
			System.out.println("====== Not Anagram ====== ");
		}
	}
}
