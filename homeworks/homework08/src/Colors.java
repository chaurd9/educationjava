import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Colors {
    public static <T> Set<T> getcolor(ArrayList<T> list) {
        return new HashSet<>(list);
    }

    public static void main(String[] args) {
        ArrayList<String> goodlistColors = new ArrayList<>();

        goodlistColors.add("красный");
        goodlistColors.add("синий");
        goodlistColors.add("желтый");
        goodlistColors.add("зеленый");
        goodlistColors.add("розовый");

        ArrayList<String> badlistColors = new ArrayList<>();

        badlistColors.add("красный");
        badlistColors.add("синий");
        badlistColors.add("красный");
        badlistColors.add("зеленый");
        badlistColors.add("синий");

        Set<String> original = getcolor(goodlistColors);
        Set<String> fake = getcolor(badlistColors);

        System.out.println("\nМассив [Оригинал]: " + String.join(", ", goodlistColors));
        System.out.println("Массив [Дубли]: " + String.join(", ", badlistColors));
        System.out.println("\nМассив [уникальные из Оригинала]: " + String.join(", ", original));
        System.out.println("Массив [уникальные из Дублей]: " + String.join(", ", fake));
    }
}