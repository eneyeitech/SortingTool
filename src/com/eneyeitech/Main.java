package com.eneyeitech;

import java.util.*;

enum Inputs {
    LINE,
    LONG,
    WORD;
}

public class Main {
    private static List<Numeric> numerics = new ArrayList<>();
    private static List<Word> words = new ArrayList<>();
    private static List<Line> lines = new ArrayList<>();

    public static void main(final String[] args) {
        Set<String> nameSet = new TreeSet<>(Arrays.asList("Mr.Green", "Mr.Yellow", "Mr.Red"));
        for (String s : nameSet) {
            System.out.println(s);
        }

        Scanner scanner = new Scanner(System.in);
        Inputs inputs;
        boolean found = Arrays.asList(args).contains("-dataType");
        boolean sorting = Arrays.asList(args).contains("-sortIntegers");
        boolean sortingType = Arrays.asList(args).contains("-sortingType");
        int index = Arrays.asList(args).indexOf("-dataType");

        if (sortingType) {
            
            return;
        }
        if (sorting) {
            processSorting(scanner);
            return;
        }
        if (found) {
            if (args.length == 2) {
                if ("long".equals(args[index + 1].toLowerCase())) {
                    inputs = Inputs.values()[1];
                    choice(inputs, scanner);
                } else if ("word".equals(args[index + 1].toLowerCase())) {
                    inputs = Inputs.values()[2];
                    choice(inputs, scanner);
                } else if ("line".equals(args[index + 1].toLowerCase())) {
                    inputs = Inputs.values()[0];
                    choice(inputs, scanner);
                } else {
                    inputs = Inputs.values()[2];
                    choice(inputs, scanner);
                }
            } else {
                inputs = Inputs.values()[2];
                choice(inputs, scanner);
            }
        } else {
            inputs = Inputs.values()[2];
            choice(inputs, scanner);
        }
    }

    public static void choice(Inputs input, Scanner scanner) {
        switch (input) {
            case LINE:
                processLines(scanner);
                break;
            case LONG:
                processNumerals(scanner);
                break;
            case WORD:
                processWords(scanner);
                break;
            default:
        }
    }

    public static void processNumerals(Scanner scanner){
        while (scanner.hasNextLong()) {
            try {
                long number = scanner.nextLong();
                numerics.add(new Numeric(number));
            } catch (NumberFormatException e) {

            }
        }
        Processor<Numeric> numericProcessor = new Processor<>(numerics);
        float total = numericProcessor.getCount();
        long maxNumber = numericProcessor.getMax().getValue();
        float maxCount = countMax(maxNumber);

        printResult(total, maxNumber, maxCount);
    }

    public static void processSorting(Scanner scanner){
        while (scanner.hasNextLong()) {
            try {
                long number = scanner.nextLong();
                numerics.add(new Numeric(number));
            } catch (NumberFormatException e) {

            }
        }
        Processor<Numeric> numericProcessor = new Processor<>(numerics);
        float total = numericProcessor.getCount();
        List<Numeric> sortedN = numericProcessor.sortedList();
        print(total,sortedN);
    }

    public static void processWords(Scanner scanner){
        while (scanner.hasNext()) {
            try {
                String word = scanner.next();
                words.add(new Word(word));
            } catch (NumberFormatException e) {

            }
        }

        Processor<Word> numericProcessor = new Processor<>(words);
        float total = numericProcessor.getCount();
        String maxWord = numericProcessor.getMax().getValue();
        float maxCount = countMax(maxWord);

        printResult(total, maxWord, maxCount);

    }

    public static void processLines(Scanner scanner){
        while (scanner.hasNextLine()) {
            try {
                String line = scanner.nextLine();
                lines.add(new Line(line));
            } catch (NumberFormatException e) {

            }
        }

        Processor<Line> numericProcessor = new Processor<>(lines);
        float total = numericProcessor.getCount();
        String maxLine = numericProcessor.getMax().getValue();
        float maxCount = countMaxLine(maxLine);

        printResultLine(total, maxLine, maxCount);

    }


    public static void print(float count, List<Numeric> list) {
        System.out.printf("Total numbers: %s.\n" +
                "Sorted data: ", (int)count);
        for (Numeric n : list) {
            System.out.print(n.getValue() + " ");
        }
    }

    public static void printResult(float numCount, long max, float maxCount) {
        float per = (maxCount / numCount) * 100;
        System.out.printf("Total numbers: %s.\n" +
                "The greatest number: %s (%s time(s), %s%%).", (int)numCount, max, (int)maxCount, (int)per);
    }

    public static void printResult(float numCount, String maxWord, float maxCount) {
        float per = (maxCount / numCount) * 100;
        System.out.printf("Total words: %s.\n" +
                "The longest word: %s (%s time(s), %s%%).", (int)numCount, maxWord, (int)maxCount, (int)per);
    }

    public static void printResultLine(float numCount, String maxLine, float maxCount) {
        float per = (maxCount / numCount) * 100;
        System.out.printf("Total lines: %s.\n" +
                "The longest line: \n%s\n(%s time(s), %s%%).", (int)numCount, maxLine, (int)maxCount, (int)per);
    }


    public static float countMax(long max) {
        int count = 0;
        for (Numeric n : numerics) {
            if (n.getValue() == max) {
                count++;
            }
        }
        return count;
    }

    public static float countMax(String maxString) {
        int count = 0;
        for (Word n : words) {
            if (n.getValue().equals(maxString)) {
                count++;
            }
        }
        return count;
    }

    public static float countMaxLine(String maxLine) {
        int count = 0;
        for (Line n : lines) {
            if (n.getValue().equals(maxLine)) {
                count++;
            }
        }
        return count;
    }
}

interface Value {

}

class Numeric implements Comparable{
    private long value;

    public Numeric(long value) {
        this.value = value;
    }

    public long getValue(){
        return  value;
    }

    @Override
    public int compareTo(Object o) {
        Numeric n = (Numeric) o;
        return Long.compare(value, n.getValue());
    }
}

class Word implements Comparable{
    private String value;

    public Word(String value) {
        this.value = value;
    }

    public String getValue(){
        return  value;
    }

    @Override
    public int compareTo(Object o) {
        Word n = (Word) o;
        if (value.length() == n.getValue().length()) {
            return value.compareTo(n.getValue());
        } else if (value.length() > n.getValue().length()) {
            return 1;
        } else {
            return -1;
        }
    }
}

class Line implements Comparable{
    private String value;

    public Line(String value) {
        this.value = value;
    }

    public String getValue(){
        return  value;
    }

    @Override
    public int compareTo(Object o) {
        Line n = (Line) o;
        if (value.length() == n.getValue().length()) {
            return value.compareTo(n.getValue());
        } else if (value.length() > n.getValue().length()) {
            return 1;
        } else {
            return -1;
        }
    }
}



class Processor<T extends Comparable>{
    private List<T> list;

    public Processor(List<T> list){
        this.list = new ArrayList<>(list);
    }

    public float getCount(){
        return list.size();
    }

   public T getMax(){
        return (T) Collections.max(list);
   }

    public List<T> sortedList() {
        List<T> a = new ArrayList<>(list);
        Collections.sort(a);
        return a;
    }


}
