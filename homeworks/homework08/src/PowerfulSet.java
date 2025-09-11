import java.util.HashSet;
import java.util.Set;

public class PowerfulSet {
    public <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        Set<T> end = new HashSet<>(set1);
        end.retainAll(set2);
        return end;
    }

    public <T> Set<T> union(Set<T> set1, Set<T> set2) {
        Set<T> end = new HashSet<>(set1);
        end.addAll(set2);
        return end;
    }

    public <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) {
        Set<T> end = new HashSet<>(set1);
        end.removeAll(set2);
        return end;
    }

    public static void main(String[] args) {
        PowerfulSet powerfulSet = new PowerfulSet();

        Set<Integer> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2);
        set1.add(3);

        Set<Integer> set2 = new HashSet<>();
        set2.add(0);
        set2.add(1);
        set2.add(2);
        set2.add(4);

        System.out.println("\nset1 = " + set1);
        System.out.println("set2 = " + set2);

        System.out.println("intersection: " + powerfulSet.intersection(set1, set2));
        System.out.println("union: " + powerfulSet.union(set1, set2));
        System.out.println("relativeComplement: " + powerfulSet.relativeComplement(set1, set2));
    }
}
