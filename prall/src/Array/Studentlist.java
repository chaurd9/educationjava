package Array;

import java.util.ArrayList;
import java.util.Scanner;

public class Studentlist {
    public static void main(String[] args) {
        ArrayList<String> students = new ArrayList<>();

        students.add("Мария");
        students.add("Боб");
        students.add("Иван");
        students.add("Петр");
        students.add("Вера");

        System.out.println("Список студентов: ");
        for (String student : students) {
            System.out.println("- " + student);
        }

        students.add("Григорий");
        System.out.println("\nПосле добавления списка студентов: ");
        System.out.println(students);

        students.remove(0);
        System.out.println("\nПосле удаления студента с Индексом 1: ");
        System.out.println(students);

        boolean hasBob = students.contains("Боб");
        System.out.println("\nЕсть ли студент Боб в списке студентов? - " + (hasBob ? "Да" : "Нет"));

        System.out.println("\nСколько студентов всего: " + students.size());

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nДобавить нового студента: ");
        String name = scanner.nextLine();
        students.add(name);
        System.out.println("Список студентов: ");
        for (String student : students) {
            System.out.println("- " + student);
        }
        System.out.println("\nСколько студентов всего: " + students.size());
    }
}
