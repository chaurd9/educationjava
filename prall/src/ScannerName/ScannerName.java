package ScannerName;

import java.util.Scanner;

public class ScannerName {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите Имя пользователя: ");
        String name = scanner.nextLine();
        System.out.println("Привет, " + name);
        scanner.close();
    }
}
