import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class AdaptiveQuizExamEngine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter roll number: ");
        String roll = sc.nextLine();
        System.out.print("Enter section: ");
        String section = sc.nextLine();

        String[][] questions = {
            {"2 + 3 = ?", "7 - 4 = ?", "6 + 8 = ?", "9 - 2 = ?", "4 + 6 = ?"},
            {"12 * 2 = ?", "48 / 4 = ?", "9 * 3 = ?", "56 / 7 = ?", "11 * 4 = ?"},
            {"15 * 3 - 10 = ?", "8 * 7 + 6 = ?", "100 / 4 + 17 = ?",
             "12 * 5 - 9 = ?", "9 * 9 - 16 = ?"}
        };
        int[][] answers = {
            {5, 3, 14, 7, 10},
            {24, 12, 27, 8, 44},
            {35, 62, 42, 51, 65}
        };

        int level = 0; // 0 = Easy, 1 = Medium, 2 = Hard
        int score = 0;
        int[] correctByLevel = new int[3];
        ArrayList<String> review = new ArrayList<>();

        System.out.println("\n=== Adaptive Quiz ===");
        for (int i = 0; i < 5; i++) {
            System.out.println("\nQuestion " + (i + 1));
            System.out.println("Level: " + (level + 1));
            System.out.println(questions[level][i]);

            int answer = readNumber(sc);
            if (answer == answers[level][i]) {
                System.out.println("Correct!");
                score++;
                correctByLevel[level]++;
                review.add("Q" + (i + 1) + ": Correct");
                if (level < 2) level++;
            } else {
                System.out.println("Wrong! Answer: " + answers[level][i]);
                review.add("Q" + (i + 1) + ": Wrong");
                if (level > 0) level--;
            }
        }

        System.out.println("\n=== Result ===");
        System.out.println("Name: " + name);
        System.out.println("Roll: " + roll);
        System.out.println("Section: " + section);
        System.out.println("Score: " + score + "/5");
        System.out.println("Percentage: " + (score * 100 / 5) + "%");
        System.out.println("Correct by level: Easy=" + correctByLevel[0]
                + ", Medium=" + correctByLevel[1] + ", Hard=" + correctByLevel[2]);

        for (String line : review) {
            System.out.println(line);
        }

        saveResult(name, roll, score);
        sc.close();
    }

    // Method and exception handling
    static int readNumber(Scanner sc) {
        while (true) {
            System.out.print("Your answer: ");
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    // Basic file handling
    static void saveResult(String name, String roll, int score) {
        try {
            Files.createDirectories(Path.of("data"));
            String line = name.replace(",", " ") + "," + roll.replace(",", " ")
                    + "," + score + "/5\n";
            Files.writeString(Path.of("data", "results.csv"), line,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Result saved in data/results.csv");
        } catch (Exception e) {
            System.out.println("Could not save result.");
        }
    }
}
