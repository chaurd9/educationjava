import java.util.Scanner;
import java.util.Arrays;

public class Anagram {
    public static boolean thisisAnagram(String s, String t) {
        if (s.toLowerCase().length() != t.toLowerCase().length()) {
            return false;
        }

        char[] schar = s.toLowerCase().toCharArray();
        char[] tchar = t.toLowerCase().toCharArray();

        Arrays.sort(schar);
        Arrays.sort(tchar);

        return Arrays.equals(schar, tchar);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nВведите первое слово из строки: ");
        String s = scanner.nextLine();
        s = s.trim().replace(" ", "");

        System.out.println("Введите второе слово из строки: ");
        String t = scanner.nextLine();
        t = t.trim().replace(" ", "");

        boolean endprogram = thisisAnagram(s, t);
        System.out.println("\nВы ввели слова: " + s + " - " + t);
        System.out.print("Является ли это Анаграммой? - ");
        if (endprogram) {
            System.out.println("Да (true)");
        } else {
            System.out.println("Нет (false)");
        }
    }
}