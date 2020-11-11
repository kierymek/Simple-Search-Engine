package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
//        String[] strings = readData();
        ArrayList<String> strList = new ArrayList<>();
        try {
            File file = new File(args[1]);
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNext()) {
                strList.add(fileScanner.nextLine());
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] strings = new String[strList.size()];
        strings = strList.toArray(strings);
        Map<String, SortedSet<Integer>> map = new HashMap<>();
        for (int i = 0; i < strings.length; i++) {
            String[] line = strings[i].split(" ");
            for (String s : line) {
                s = s.toLowerCase().replace(" ", "");
                TreeSet<Integer> tmp;
                if (map.containsKey(s)) {
                    tmp = new TreeSet<>(map.get(s));
                } else {
                    tmp = new TreeSet<>();
                }
                tmp.add(i);
                map.put(s, tmp);
            }
        }
        searchWord(strings, map);
    }

    static void searchWord(String[] strings, Map<String, SortedSet<Integer>> map) {
        String[] menu = new String[]{
                "=== Menu ===",
                "1. Find a person",
                "2. Print all people",
                "0. Exit"
        };
        boolean exited = false;
        while (!exited) {
            for (String s : menu) {
                System.out.println(s);
            }
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 0:
                    exited = true;
                    System.out.println("Bye!");
                    break;
                case 1:
                    System.out.println("Select a matching strategy: ALL, ANY, NONE");
                    String strategy = scanner.next().toLowerCase();
                    scanner.nextLine();
                    while (!strategy.equals("all") && !strategy.equals("any") && !strategy.equals("none")) {
                        System.out.println("wrong option try again!");
                        strategy = scanner.next().toLowerCase();
                    }
                    System.out.println("Enter a name or email to search all suitable people.");
                    String[] query = scanner.nextLine().toLowerCase().split(" ");
                    Set<String> result = new LinkedHashSet<>();
                    SearchingStrategy search = new SearchingStrategy();
                    search.search(strings, map, result, strategy, query);
                    for (String s : result) {
                        System.out.println(s);
                    }
                    if (result.size() == 0) {
                        System.out.println("No matching people found.");
                    }
                    break;
//                }
                case 2:
                    for (String s : strings) {
                        System.out.println(s);
                    }
                    break;
                default:
                    System.out.println("Incorrect option! Try again.");
                    break;
            }
        }
    }
}

class SearchingStrategy {
    void search(String[] strings, Map<String, SortedSet<Integer>> map, Set<String> result, String strategy, String[] query){
        switch (strategy) {
            case "any":
                for (String word : query) {
                    if (map.containsKey(word)) {
                        SortedSet<Integer> set = map.get(word);
                        for (int i : set) {
                            result.add(strings[i]);
                        }
                    }
                }
                break;
            case "all":
                for (int i = 0; i < strings.length; i++) {
                    boolean check = true;
                    for (String word : query) {
                        if (map.containsKey(word)) {
                            SortedSet<Integer> set = map.get(word);
                            if (!set.contains(i)) {
                                check = false;
                                break;
                            }
                        }
                    }
                    if (check) {
                        result.add(strings[i]);
                    }
                }
                break;
            case "none":
                for (int i = 0; i < strings.length; i++) {
                    boolean check = true;
                    for (String word : query) {
                        SortedSet<Integer> set = map.get(word);
                        if (set.contains(i)) {
                            check = false;
                            break;
                        }
                    }
                    if (check) {
                        result.add(strings[i]);
                    }
                }
                break;
        }
    }
}




