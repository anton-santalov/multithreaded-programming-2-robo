package com.antonsantalov;

import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static final int threadNumber = 1000;

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        long startTs = System.currentTimeMillis(); // start time

        for (int i = 0; i < threadNumber; i++) {
            Thread thread = new Thread(Main::addRCountToMap);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs - startTs) + "ms");

        sizeToFreq.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .forEach(it -> System.out.printf("Значение %d повторилось %d раз\n", it.getKey(), it.getValue()));
    }

    private static void addRCountToMap() {
        synchronized (sizeToFreq) {
            String route = generateRoute("RLRFR", 100);
            long rNumber = route.chars().filter(c -> c == 'R').count();
            sizeToFreq.put((int) rNumber,
                sizeToFreq.get((int) rNumber) != null ? sizeToFreq.get((int) rNumber) + 1 : 1);
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
