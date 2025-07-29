/*
Задача 1. Для введенной с клавиатуры буквы английского алфавита
нужно вывести слева стоящую букву на стандартной клавиатуре. При этом
клавиатура замкнута, т.е. справа от буквы «p» стоит буква «a», а слева от "а"
буква "р", также соседними считаются буквы «l» и буква «z», а буква «m» с
буквой «q».

Входные данные: строка входного потока содержит один символ —
маленькую букву английского алфавита.

Выходные данные: следует вывести букву стоящую слева от заданной
буквы, с учетом замкнутости клавиатуры.
*/

package Keyboard;

import java.util.Scanner; // для указанной задачи мне показалось достаточным java.utl.Scanner с теорией из Урок 11. Типы данных, переменные и массивы

public class KeyboardList {
    public static void main (String[] args) {
        char[] keyboardlist = {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p',
                'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
                'z', 'x', 'c', 'v', 'b', 'n', 'm'}; // сама клавиатура от q до m, но если верно понял, то можно было создать массив вида qpalzm
        Scanner scanner = new Scanner(System.in); // добавляем объект Scanner для чтения Ввода данных с клавиатуры

        /*
        Для удобства отображения с чем можно взаимодействовать
        отдельно выведен массив keyboardlist в виде рядов
         */

        for (int i = 0; i < 10; i++) {
            System.out.print(keyboardlist[i] + " ");
        }
        System.out.println();
        System.out.print(" ");
        for (int i = 10; i < 19; i++) {
            System.out.print(keyboardlist[i] + " ");
        }
        System.out.println();
        System.out.print("  ");
        for (int i = 19; i < keyboardlist.length; i++) {
            System.out.print(keyboardlist[i] + " ");
        }
        System.out.println("\n\nВведите символ от q до m: "); // запрашиваем пользователя на Ввод символов

        char inputchar = scanner.next().charAt(0);
        int index = -1; // в условиях задачи сказано, что нужно вывести слева стоящую букву
        for (int i = 0; i < keyboardlist.length; i++) {
            if (keyboardlist[i] == inputchar) { // смотрим в массив keyboardlist и приравниваем его к введенному с клавиатуры
                index = i; // сохраняем индекс, как i
                break; // как нашли нужную букву, то прерываем работу цикла массива
            }
        }

        char zamkeyb; // переменная zaykeyb служит для хранения результата, то есть слева стоящей буквы
        if (index == 0) {
            zamkeyb = keyboardlist[keyboardlist.length - 1]; // если индекс массива равен 0, то переходим к предыдущему ряду
        } else if (index == -1) {
            System.out.println("Ошибка: введен недопустимый символ!");
            // добавлена проверка на корректность введенного символа,
            // если он не совпадает с массивом, то оповещение об ошибке и прекращение работы программы
            return;
        } else {
            zamkeyb = keyboardlist[index - 1]; // в ином случае в качестве результата выводим предыдущий символ
        }
        System.out.println("Слева от \033[1m" + inputchar + "\033[0m" + " находится буква \033[1m" + zamkeyb + "\033[0m!");
        scanner.close();
    }
}
