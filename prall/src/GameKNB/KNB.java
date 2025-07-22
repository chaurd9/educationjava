package GameKNB;

import java.util.Random;

public class KNB {
    public static void main(String[] args) {
        Random random = new Random();

        enum Choices {камень, ножницы, бумага}
        Choices vasya = Choices.values()[random.nextInt(3)];
        Choices petya = Choices.values()[random.nextInt(3)];

        if ((vasya.equals(Choices.камень) && petya.equals(Choices.ножницы)) ||
           (vasya.equals(Choices.ножницы) && petya.equals(Choices.бумага)) ||
           (vasya.equals(Choices.бумага) && petya.equals(Choices.камень)))
        {
            System.out.println("Игрок Вася победил!");
        }
        else if (vasya.equals(petya))
        {
            System.out.println("Ничья");
        }
        else
        {
            System.out.println("Игрок Петя победил!");
        }
    }
}
