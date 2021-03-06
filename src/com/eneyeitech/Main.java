package com.eneyeitech;

import java.io.*;
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
    private static List<Numeric> numerics2 = new ArrayList<>();
    private static List<Word> words2 = new ArrayList<>();
    private static List<Line> lines2 = new ArrayList<>();
    private static TreeMap<Numeric, Integer> numericTreeMap;
    private static TreeMap<Word, Integer> wordTreeMap;
    private static TreeMap<Line, Integer> lineTreeMap;
    private static NumericCountCompare numericCountCompare = new NumericCountCompare();
    private static WordCountCompare wordCountCompare = new WordCountCompare();
    private static LineCountCompare lineCountCompare = new LineCountCompare();
    private static boolean fileReading = false;
    private static boolean fileWriting = false;
    private static int wIndex;
    private static Scanner scanner;
    private static Writer Candidateoutput = null;

    private static boolean natural = true;

    public static void main(final String[] args) {

        Inputs inputs;
        boolean found = Arrays.asList(args).contains("-dataType");
        boolean sorting = Arrays.asList(args).contains("-sortIntegers");
        int index = Arrays.asList(args).indexOf("-dataType");
        List<String> valid = List.of("line","word","long","bycount","natural", "-datatype", "-sortingtype", "-inputfile", "-outputfile");
        boolean sortingType = Arrays.asList(args).contains("-sortingType");
        int index2 = Arrays.asList(args).indexOf("-sortingType");

        fileReading = Arrays.asList(args).contains("-inputFile");
        int rIndex = Arrays.asList(args).indexOf("-inputFile");

        if (fileReading) {
            try {
                String fileName = args[rIndex + 1];
                File file = new File(fileName);
                scanner = new Scanner(file);
            } catch (ArrayIndexOutOfBoundsException e){
                scanner = new Scanner(System.in);
            } catch (FileNotFoundException e) {
                scanner = new Scanner(System.in);
                e.printStackTrace();
            }
        } else {
            scanner = new Scanner(System.in);
        }

        fileWriting = Arrays.asList(args).contains("-outputFile");
        wIndex = Arrays.asList(args).indexOf("-outputFile");

        if (fileWriting) {
            try {
                String fileName = args[wIndex + 1];
                File file = new File(fileName);
                Candidateoutput = new BufferedWriter(new FileWriter(file));

            } catch (ArrayIndexOutOfBoundsException e){
                fileWriting = false;
            } catch (IOException e) {
                fileWriting = false;
                e.printStackTrace();
            }
        }


        if (sortingType) {
            boolean natural = Arrays.asList(args).contains("natural");
            boolean bycount = Arrays.asList(args).contains("byCount");
            if (!natural && !bycount) {
                System.out.println("No sorting type defined!");
                return;
            }
            if (args.length >= 2) {
                if ("natural".equals(args[index2 + 1].toLowerCase())) {
                    Main.natural = true;
                } else if ("bycount".equals(args[index2 + 1].toLowerCase())) {
                    Main.natural = false;
                } else {
                    if (!valid.contains(args[index2 + 1].toLowerCase())) {
                        System.out.println("-arg\" is not a valid parameter. It will be skipped.");
                    }
                }
            }

        }

        if (sorting) {
            processSorting(scanner);
            return;
        }
        if (found) {
            boolean lon = Arrays.asList(args).contains("long");
            boolean lin = Arrays.asList(args).contains("line");
            boolean wor = Arrays.asList(args).contains("word");
            if (!lon && !lin && !wor) {
                System.out.println("No data type defined!");
                return;
            }

            for (String arg : args) {
                if (!valid.contains(arg.toLowerCase())) {
                    System.out.printf("\"%s\" is not a valid parameter. It will be skipped.\n", arg);
                }
            }

            if (args.length >= 2) {

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

    static class NumericCountCompare implements Comparator<Numeric>{

        @Override
        public int compare(Numeric o1, Numeric o2) {
            if (o1.getCount() > o2.getCount()) {
                return 1;
            } else if (o1.getCount() < o2.getCount()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    static class WordCountCompare implements Comparator<Word>{

        @Override
        public int compare(Word o1, Word o2) {
            if (o1.getCount() > o2.getCount()) {
                return 1;
            } else if (o1.getCount() < o2.getCount()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    static class LineCountCompare implements Comparator<Line>{

        @Override
        public int compare(Line o1, Line o2) {
            if (o1.getCount() > o2.getCount()) {
                return 1;
            } else if (o1.getCount() < o2.getCount()) {
                return -1;
            } else {
                return o1.getValue().compareTo(o2.getValue());
            }
        }
    }

    public static void processNumerals(Scanner scanner){
        String entry = "";
        while (scanner.hasNext()) {
            try {
                entry = scanner.next();
                long number = Long.parseLong(entry);
                numerics.add(new Numeric(number));
            } catch (NumberFormatException e) {
                System.out.printf("\"%s\" is not a long. It will be skipped.", entry);
            }
        }
        scanner.close();
        Processor<Numeric> numericProcessor = new Processor<>(numerics);
        float total = numericProcessor.getCount();
        long maxNumber = numericProcessor.getMax().getValue();
        float maxCount = countMax(maxNumber);

        countNumerals();
        for (Map.Entry<Numeric, Integer> e : numericTreeMap.entrySet()) {
            Numeric n = new Numeric(e.getKey().getValue());
            n.setCount(e.getValue());
            numerics2.add(n);
        }
        if (!natural) {
            printLongCount(total);
        } else {
            Collections.sort(numerics);
            printNumbers(total);
        }
    }

    public static void processSorting(Scanner scanner){
        while (scanner.hasNextLong()) {
            try {
                long number = scanner.nextLong();
                numerics.add(new Numeric(number));
            } catch (NumberFormatException e) {

            }
        }
        scanner.close();
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
        scanner.close();
        Processor<Word> numericProcessor = new Processor<>(words);
        float total = numericProcessor.getCount();
        String maxWord = numericProcessor.getMax().getValue();
        float maxCount = countMax(maxWord);

        countWords();
        for (Map.Entry<Word, Integer> e : wordTreeMap.entrySet()) {
            Word n = new Word(e.getKey().getValue());
            n.setCount(e.getValue());
            words2.add(n);
        }
        if (!natural) {
            printWordCount(total);
        } else {
            Collections.sort(words);
            printWords(total);
        }

    }

    public static void processLines(Scanner scanner){
        while (scanner.hasNextLine()) {
            try {
                String line = scanner.nextLine();
                lines.add(new Line(line));
            } catch (NumberFormatException e) {

            }
        }
        scanner.close();

        Processor<Line> numericProcessor = new Processor<>(lines);
        float total = numericProcessor.getCount();
        String maxLine = numericProcessor.getMax().getValue();
        float maxCount = countMaxLine(maxLine);

        countLines();
        for (Map.Entry<Line, Integer> e : lineTreeMap.entrySet()) {
            Line n = new Line(e.getKey().getValue());
            n.setCount(e.getValue());
            lines2.add(n);
        }
        if (!natural) {
            printLineCount(total);
        } else {
            Collections.sort(lines);
            printLines(total);
        }

    }

    public static void countNumerals(){
        int count = 1;
        TreeSet<Long> c = new TreeSet<>();
        numericTreeMap = new TreeMap<>();
        for (int i = 0; i < numerics.size(); i++) {
            for (int j = 0; j < numerics.size(); j++) {
                if (i != j) {
                    if (numerics.get(i).getValue() == numerics.get(j).getValue()) {
                        count++;
                    }
                }
            }
            numerics.get(i).setCount(count);
            numericTreeMap.put(numerics.get(i), count);
            count = 1;
        }
    }

    public static void countWords(){
        int count = 1;
        wordTreeMap = new TreeMap<>();
        for (int i = 0; i < words.size(); i++) {
            for (int j = 0; j < words.size(); j++) {
                if (i != j) {
                    if (words.get(i).getValue().equals(words.get(j).getValue())) {
                        count++;
                    }
                }
            }
            words.get(i).setCount(count);
            wordTreeMap.put(words.get(i), count);
            count = 1;
        }
    }

    public static void countLines(){
        int count = 1;
        lineTreeMap = new TreeMap<>();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.size(); j++) {
                if (i != j) {
                    if (lines.get(i).getValue().equals(lines.get(j).getValue())) {
                        count++;
                    }
                }
            }
            lines.get(i).setCount(count);
            lineTreeMap.put(lines.get(i), count);
            count = 1;
        }
    }

    public static void print(float count, List<Numeric> list) {
        System.out.printf("Total numbers: %s.\n" +
                "Sorted data: ", (int)count);
        for (Numeric n : list) {
            System.out.print(n.getValue() + " ");
        }
    }

    public static void printNumbers(float count) {
        StringBuilder builder = new StringBuilder();
        String separator = System.getProperty("line.separator");
        String out = String.format("Total numbers: %s.%s" +
                "Sorted data: ", (int)count, separator);

        builder.append(out);
        for (Numeric n : numerics) {
            builder.append(n.getValue()+" ");
        }
        if (fileWriting) {
            try {
                Candidateoutput.write(builder.toString());
                Candidateoutput.close();
            } catch (IOException e) {
                System.out.println(builder.toString());
            }
            return;
        } else {
            System.out.println(builder.toString());
        }

    }

    public static void printLongCount(float numCount){
        Collections.sort(numerics2, numericCountCompare);
        StringBuilder builder = new StringBuilder();
        String separator = System.getProperty("line.separator");
        String out = String.format("Total numbers: %s.%s", (int)numCount, separator);
        builder.append(out);
        for (Numeric n : numerics2) {
            float per = (n.getCount() / numCount) * 100;
            String o = String.format("%s: %s time(s), %s%%%s", n.getValue(), n.getCount(), (int)per,separator);
            builder.append(o);
        }
        if (fileWriting) {
            try {
                Candidateoutput.write(builder.toString());
                Candidateoutput.close();
            } catch (IOException e) {
                System.out.println(builder.toString());
            }
            return;
        } else {
            System.out.println(builder.toString());
        }

    }

    public static void printWords(float count) {
        StringBuilder builder = new StringBuilder();
        String separator = System.getProperty("line.separator");
        String out = String.format("Total words: %s.%s" +
                "Sorted data: ", (int)count, separator);

        builder.append(out);
        for (Word n : words) {
            builder.append(n.getValue()+" ");
        }

        if (fileWriting) {
            try {
                Candidateoutput.write(builder.toString());
                Candidateoutput.close();
            } catch (IOException e) {
                System.out.println(builder.toString());
            }
            return;
        } else {
            System.out.println(builder.toString());
        }
    }

    public static void printWordCount(float numCount){
        Collections.sort(words2, wordCountCompare);
        StringBuilder builder = new StringBuilder();
        String separator = System.getProperty("line.separator");
        String out = String.format("Total words: %s.%s", (int)numCount, separator);
        builder.append(out);
        for (Word n : words2) {
            float per = (n.getCount() / numCount) * 100;
            String o = String.format("%s: %s time(s), %s%%%s", n.getValue(), n.getCount(), (int)per,separator);
            builder.append(o);
        }
        if (fileWriting) {
            try {
                Candidateoutput.write(builder.toString());
                Candidateoutput.close();
            } catch (IOException e) {
                System.out.println(builder.toString());
            }
            return;
        } else {
            System.out.println(builder.toString());
        }
    }

    public static void printLines(float count) {
        //System.out.printf("Total lines: %s\n" +
        //      "Sorted data:\n", (int)count);

        StringBuilder builder = new StringBuilder();
        String separator = System.getProperty("line.separator");
        String out = String.format("Total lines: %s.%s" +
                "Sorted data:%s", (int)count, separator, separator);
        builder.append(out);
        for (Line n : lines) {
            //System.out.print(n.getValue() + "\n");
            builder.append(n.getValue() + separator);
        }

        if (fileWriting) {
            try {
                Candidateoutput.write(builder.toString());
                Candidateoutput.close();
            } catch (IOException e) {
                System.out.println(builder.toString());
            }
            return;
        } else {
            System.out.println(builder.toString());
        }
    }

    public static void printLineCount(float numCount){
        Collections.sort(lines2, lineCountCompare);
        //System.out.printf("Total lines: %s\n", (int)numCount);
        StringBuilder builder = new StringBuilder();
        String separator = System.getProperty("line.separator");
        String out = String.format("Total lines: %s.%s", (int)numCount, separator);
        builder.append(out);
        for (Line n : lines2) {
            float per = (n.getCount() / numCount) * 100;
            //System.out.printf("%s: %s time(s), %s%%\n", n.getValue(), n.getCount(), (int)per);
            String o = String.format("%s: %s time(s), %s%%%s", n.getValue(), n.getCount(), (int)per,separator);
            builder.append(o);
        }

        if (fileWriting) {
            try {
                Candidateoutput.write(builder.toString());
                Candidateoutput.close();
            } catch (IOException e) {
                System.out.println(builder.toString());
            }
            return;
        } else {
            System.out.println(builder.toString());
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
    private int count;

    public Numeric(long value) {
        this.value = value;
    }

    public long getValue(){
        return  value;
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(Object o) {
        Numeric n = (Numeric) o;
        return Long.compare(value, n.getValue());
    }

}

class Word implements Comparable{
    private String value;
    private int count;

    public Word(String value) {
        this.value = value;
    }

    public String getValue(){
        return  value;
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(Object o) {
        Word n = (Word) o;
        return value.compareTo(n.getValue());
    }
}

class Line implements Comparable{
    private String value;
    private int count;

    public Line(String value) {
        this.value = value;
    }

    public String getValue(){
        return  value;
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(Object o) {
        Line n = (Line) o;
        if (value.length() == n.getValue().length()) {
            return value.compareTo(n.getValue());
        } else if (value.length() < n.getValue().length()) {
            return 1;
        } else {
            return -1;
        }
    }
}

class Processor<T extends Comparable> {
    private List<T> list;
    private TreeSet<T> c;
    private TreeMap<T, Integer> entry;

    public Processor(List<T> list) {
        this.list = new ArrayList<>(list);
    }

    public float getCount() {
        return list.size();
    }

    public T getMax() {
        return (T) Collections.max(list);
    }

    public List<T> sortedList() {
        List<T> a = new ArrayList<>(list);
        Collections.sort(a);
        return a;
    }


}