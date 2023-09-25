import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class Suffix {
    public static <T extends Comparable<? super T>> List<T> longestHigherSuffixSeq(List<T> a, List<T> b, Comparator<T> cmp) {
        List<T> c = new ArrayList<>();
        int aIndex = a.size() - 1;
        int bIndex = b.size() - 1;

        while (aIndex > -1 && bIndex > -1 && a.get(aIndex).compareTo(b.get(bIndex)) >= 0) {
            c.add(a.get(aIndex));
            aIndex -= 1;
            bIndex -= 1;
        }

        Collections.reverse(c);

        return c;
    }


    public static <T extends Comparable<? super T>> List<T> longestHigherSuffixIt(List<T> a, List<T> b, Comparator<T> cmp) {
        List<T> c = new ArrayList<>();

        ListIterator<T> aLi = a.listIterator(a.size());
        ListIterator<T> bLi = b.listIterator(b.size());
        while (aLi.hasPrevious() && bLi.hasPrevious()) {
            T curr = aLi.previous();
            if (curr.compareTo(bLi.previous()) >= 0)
                c.add(curr);
        }

        Collections.reverse(c);

        return c;
    }


    public static <T extends Comparable<? super T>> List<T> longestHigherSuffixStr(List<T> a, List<T> b, Comparator<T> cmp) {
        // Reverse lists so that length of smallest list can be factored in
        // (Can't start counting 0 -> x from end of list)
         List<T> a1 = IntStream.range(0, a.size())
                .map(i -> (a.size() - 1 - i))
                .mapToObj(a::get)
                .collect(Collectors.toList());
         List<T> b1 = IntStream.range(0, b.size())
                .map(i -> (b.size() - 1 - i))
                .mapToObj(b::get)
                .collect(Collectors.toList());

        // Find last index of b that marks end of suffix, or travel distance of smaller list's length
        int min = Math.min(a1.size(), b1.size());
        int last = IntStream.range(0, min)
                .filter(x -> a1.get(x).compareTo(b1.get(x)) < 0)
                .findFirst()
                .orElse(min);

        // Collect valid indices of a that constitute higher suffix
        List<T> highestSuffix = IntStream.range(0, last)
                .mapToObj(a1::get)
                .collect(Collectors.toList());

        // Return reversed highestSuffix for correct format
        return IntStream.range(0, highestSuffix.size())
                .map(i -> highestSuffix.size() - 1 - i)
                .mapToObj(highestSuffix::get)
                .collect(Collectors.toList());
    }


    public static void main(String[] args) {
        if (args.length != 2)
            throw new IllegalArgumentException();

        // Convert Strings to Lists
        List<Character> l1 = new ArrayList<>();
        for(int i = 0; i < args[0].length(); i++)
            l1.add(args[0].charAt(i));

        List<Character> l2 = new ArrayList<>();
        for (int i = 0; i < args[1].length(); i++)
            l2.add(args[1].charAt(i));

        // Find and print longest higher suffix
        List<Character> l3 = Suffix.longestHigherSuffixStr(l1, l2, new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return o1.compareTo(o2);
            }

            @Override
            public boolean equals(Object obj) {
                return this.equals(obj);
            }
        });
        Collections.reverse(l1);
        Collections.reverse(l2);
        System.out.println(l1);
        System.out.println(l2);
        System.out.println(l3);
    }

}
