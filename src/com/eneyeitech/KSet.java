package com.eneyeitech;

import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class KSet {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] range = scanner.nextLine().split(" ");
        int i = Integer.parseInt(range[0]);
        int j = Integer.parseInt(range[1]);
        int n = Integer.parseInt(scanner.nextLine());
        TreeMap<Integer, String> store = new TreeMap<>();
        for (int m = 0; m < n; m++) {
            String[] input = scanner.nextLine().split(" ");
            int k = Integer.parseInt(input[0]);
            String v = input[1];
            store.put(k, v);
        }
        SortedMap<Integer, String> newStore = store.subMap(i, j+1);

        for(Map.Entry<Integer, String> entry : newStore.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }


    }
}
