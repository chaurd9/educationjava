/*
Задача 2. Задана последовательность, состоящая только из символов ‘>’,


‘<’ и ‘-‘. Требуется найти количество стрел, которые спрятаны в этой
последовательности. Стрелы – это подстроки вида ‘>>-->’ и ‘<--<<’.

Входные данные: в первой строке входного потока записана строка,
состоящая из символов ‘>’, ‘<’ и ‘-‘ (без пробелов). Строка может содержать до
106 символов.

Выходные данные: в единственную строку выходного потока нужно
вывести искомое количество стрелок.
*/

package Sequence;

import java.util.Scanner;

public class Sequence {
    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите строку из символов: ‘>’, ‘<’ и ‘-‘");
        System.out.println("\033[1;3mРекомендация:\033[0m длинна строки должна быть не менее 5 символов");
        String sequence = scanner.nextLine(); // Чтение всей введенной строки

        if(!sequence.matches("^[><-]*$")) { // Проверка на то, какие символы будут введены в консоль от начала строки до конца с использованием регулярных выражений
            System.out.println("Ошибка: Вы ввели недопустимый символ: " + sequence);
            return;
        }

        System.out.println("Введенная строка: " + sequence);
        System.out.println("Длинна строки: " + sequence.length());

        int count = 0; // Инициализация счетчика стрелок

        for (int i = 0; i <= sequence.length() - 5; i++) { // Основной цикл поиска стрелок >>--> и <--<<
            char leftStartArrow = sequence.charAt(i); // Получение текущего символа

            if (leftStartArrow == '>') {  // Проверка на начало стрелки >>-->
                // если начинается с другого символа, например, "-", то проверка проводиться не будет, так как результат равен 0
                char rightEndArrow = sequence.charAt(i + 4);
                if (rightEndArrow == '>') { // Проверка на конец стрелки >>-->
                    String sub = sequence.substring(i, i + 5);
                    if (sub.equals(">>-->")) { // Сравнение введенной строки с шаблоном
                        count++;
                        i += 4; // Пропуск символов
                        continue; // Продолжение программы и переход к проверке шаблона <--<<
                    }
                }
            }

            if (leftStartArrow == '<') {
                char rightEndArrow = sequence.charAt(i + 4);
                if (rightEndArrow == '<') {
                    String sub = sequence.substring(i, i + 5);
                    if (sub.equals("<--<<")) {
                        count++;
                        i += 4;
                    }
                }
            }
        }

        System.out.println("Всего стрелок: " + count);
        scanner.close(); // Закрытие сканера
    }
}
