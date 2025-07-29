/*
Задача 3*. Задана строка, состоящая из букв английского алфавита,
разделенных одним пробелом. Необходимо каждую последовательность
символов упорядочить по возрастанию и вывести слова в нижнем регистре.

Входные данные: в единственной строке последовательность символов
представляющее два слова.

Выходные данные: упорядоченные по возрастанию буквы в нижнем
регистре.
 */

package ABC;

import java.util.Scanner;
import java.util.Arrays; // Импорт Arrays для массива и сортировки

public class Abc {
    public static void main (String[] args) {
        System.out.println("Введите, пожалуйста, несколько слов на английском языке: ");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine(); // Считывание введенного текста пользователем с клавиатуры

        String[] words = str.split(" "); // Разделение слов, которые ввел пользователь в str
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].toLowerCase(); // Изменение всех слов на нижний регистр
            char[] letters = words[i].toCharArray(); // Добавление всех слов в массив
            Arrays.sort(letters); // Сортировка массива по возрастанию (от меньшего к большему)
            words[i] = new String(letters); // Слова заносятся в новую строку
        }
        System.out.println(String.join(" ", words)); // Вывод слов через пробел
        scanner.close();
    }
}
