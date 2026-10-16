import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
public class AdaptiveQuizExamEngine {
    private static final Path RESULTS = Path.of("data", "results.csv");
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            List<QuizQuestion> bank = QuizQuestion.loadBank();
            if (bank.isEmpty()) bank = fallbackQuestions();
            System.out.println("=== Adaptive Quiz & Exam Engine ===");
            String name = required(sc, "Name: ");
            String roll = required(sc, "Roll number: ");
            String section = required(sc, "Section: ");
            System.out.println("Welcome " + name + " (" + section + ")");
            while (true) {
                System.out.println("\n1. Adaptive quiz  2. Exam mode  3. Leaderboard  4. Question bank  5. Exit");
                int choice = readInt(sc, "Choose: ", 1, 5);
                switch (choice) {
                    case 1 -> runQuiz(sc, bank, name, roll, true);
                    case 2 -> runQuiz(sc, bank, name, roll, false);
                    case 3 -> leaderboard();
                    case 4 -> bankSummary(bank);
                    case 5 -> { System.out.println("Goodbye!"); return; }
                }
            }
        }
    }
    static String required(Scanner sc, String label) {
        while (true) {
            System.out.print(label);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Cannot be blank.");
        }
    }
    static int readInt(Scanner sc, String label, int min, int max) {
        while (true) {
            System.out.print(label);
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n >= min && n <= max) return n;
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a number between " + min + " and " + max + ".");
        }
    }
    static void runQuiz(Scanner sc, List<QuizQuestion> bank, String name, String roll, boolean adaptive) {
        int level = adaptive ? 0 : 1;
        int correctStreak = 0, wrongStreak = 0, score = 0;
        int[][] matrix = new int[3][2];
        Set<QuizQuestion> used = new HashSet<>();
        Map<String, Integer> topicScores = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            QuizQuestion q = chooseQuestion(bank, used, level);
            if (q == null) break;
            used.add(q);
            System.out.println("\nQuestion " + (i + 1) + " (Level " + (q.difficulty() + 1) + ")");
            q.show();
            int answer = readInt(sc, "Answer (1-4): ", 1, 4);
            boolean correct = q.isCorrect(answer);
            matrix[q.difficulty()][correct ? 0 : 1]++;
            if (correct) {
                score++;
                correctStreak++;
                wrongStreak = 0;
                topicScores.merge(q.topic(), 1, Integer::sum);
                System.out.println("Correct!");
            } else {
                wrongStreak++;
                correctStreak = 0;
                System.out.println("Wrong. Correct option: " + q.correctOption);
            }
            if (adaptive && correctStreak == 2) {
                level = Math.min(2, level + 1);
                correctStreak = 0;
            } else if (adaptive && wrongStreak == 2) {
                level = Math.max(0, level - 1);
                wrongStreak = 0;
            }
        }
        int total = used.size();
        System.out.printf("Score: %d/%d (%.1f%%)%n", score, total,
                total == 0 ? 0 : score * 100.0 / total);
        System.out.println("Correct answers by topic: " + topicScores);
        System.out.println("Recursive score check: " + recursiveTotal(
                new int[]{matrix[0][0], matrix[1][0], matrix[2][0]}, 0));
        for (int i = 0; i < 3; i++)
            System.out.printf("Level %d: %d correct, %d wrong%n", i + 1, matrix[i][0], matrix[i][1]);
        save(new QuizResult(name, roll, score, total, adaptive ? "ADAPTIVE" : "EXAM"));
    }
    static QuizQuestion chooseQuestion(List<QuizQuestion> bank, Set<QuizQuestion> used, int level) {
        for (int distance = 0; distance < 3; distance++)
            for (QuizQuestion q : bank)
                if (!used.contains(q) && Math.abs(q.difficulty() - level) == distance) return q;
        return null;
    }
    static List<QuizQuestion> fallbackQuestions() {
        List<QuizQuestion> bank = new ArrayList<>();
        bank.add(new QuizQuestion("Java", 0, "Which keyword defines a class?",
                new String[]{"class", "new", "void", "import"}, 1));
        bank.add(new QuizQuestion("Math", 0, "What is 2 + 3?",
                new String[]{"4", "5", "6", "7"}, 2));
        bank.add(new QuizQuestion("Java", 1, "Which is a loop?",
                new String[]{"switch", "while", "break", "return"}, 2));
        bank.add(new QuizQuestion("Math", 1, "What is 12 * 2?",
                new String[]{"22", "26", "24", "20"}, 3));
        bank.add(new QuizQuestion("Java", 2, "Which collection prevents duplicates?",
                new String[]{"List", "Set", "Map", "ArrayList"}, 2));
        return bank;
    }
    static void bankSummary(List<QuizQuestion> bank) {
        System.out.println("Total questions: " + bank.size());
        for (int i = 0; i < 3; i++) {
            final int level = i;
            System.out.println("Level " + (i + 1) + ": " +
                    bank.stream().filter(q -> q.difficulty() == level).count());
        }
        System.out.println("Topics: " + bank.stream().collect(
                Collectors.groupingBy(QuizQuestion::topic, Collectors.counting())));
    }
    static void save(QuizResult result) {
        try {
            Files.createDirectories(RESULTS.getParent());
            if (!Files.exists(RESULTS))
                Files.writeString(RESULTS, "name,roll,score,total,mode\n", StandardOpenOption.CREATE);
            String name = result.name().replace(',', ' ').replace('\n', ' ');
            String roll = result.roll().replace(',', ' ').replace('\n', ' ');
            Files.writeString(RESULTS, name + "," + roll + "," + result.score() + "," +
                    result.total() + "," + result.mode() + "\n", StandardOpenOption.APPEND);
            System.out.println("Result saved.");
        } catch (IOException e) {
            System.out.println("Could not save result: " + e.getMessage());
        }
    }
    static void leaderboard() {
        if (!Files.exists(RESULTS)) {
            System.out.println("No results yet.");
            return;
        }
        try {
            List<QuizResult> results = new ArrayList<>();
            for (String line : Files.readAllLines(RESULTS).stream().skip(1).toList()) {
                String[] p = line.split(",", -1);
                if (p.length != 5) continue;
                try {
                    results.add(new QuizResult(p[0], p[1], Integer.parseInt(p[2]),
                            Integer.parseInt(p[3]), p[4]));
                } catch (NumberFormatException ignored) { }
            }
            results.stream().sorted(Comparator.comparingDouble((QuizResult r) ->
                    r.total() == 0 ? 0 : (double) r.score() / r.total()).reversed())
                    .limit(10).forEach(r -> System.out.printf("%s: %d/%d (%s)%n",
                            r.name(), r.score(), r.total(), r.mode()));
            System.out.printf("Average: %.1f%%%n", results.stream().mapToDouble(r ->
                    r.total() == 0 ? 0 : r.score() * 100.0 / r.total()).average().orElse(0));
        } catch (IOException e) {
            System.out.println("Cannot read leaderboard: " + e.getMessage());
        }
    }

    // CO4: interface, abstract class, inheritance, encapsulation, and records
    interface Assessable { boolean isCorrect(int answer); }
    static abstract class Question implements Assessable {
        private final String topic;
        private final int difficulty;
        private final String prompt;
        Question(String topic, int difficulty, String prompt) {
            this.topic = topic; this.difficulty = difficulty; this.prompt = prompt;
        }
        String topic() { return topic; }
        int difficulty() { return difficulty; }
        String prompt() { return prompt; }
    }
    static class QuizQuestion extends Question {
        private final String[] options;
        private final int correctOption;
        QuizQuestion(String topic, int difficulty, String prompt, String[] options, int correctOption) {
            super(topic, difficulty, prompt);
            this.options = options.clone();
            this.correctOption = correctOption;
        }
        @Override public boolean isCorrect(int answer) { return answer == correctOption; }
        void show() {
            System.out.println(prompt());
            for (int i = 0; i < options.length; i++)
                System.out.println((i + 1) + ". " + options[i]);
        }
        static List<QuizQuestion> loadBank() {
            List<QuizQuestion> bank = new ArrayList<>();
            Path path = Path.of("data", "questions.txt");
            if (!Files.exists(path)) return bank;
            try {
                for (String line : Files.readAllLines(path)) {
                    if (line.isBlank() || line.startsWith("#")) continue;
                    String[] p = line.split("\\|", -1);
                    if (p.length != 8) continue;
                    try {
                        int level = Integer.parseInt(p[1].trim());
                        int answer = Integer.parseInt(p[7].trim());
                        if (level < 0 || level > 2 || answer < 1 || answer > 4) continue;
                        bank.add(new QuizQuestion(p[0].trim(), level, p[2].trim(),
                                new String[]{p[3].trim(), p[4].trim(), p[5].trim(), p[6].trim()}, answer));
                    } catch (NumberFormatException ex) {
                        System.out.println("Skipping invalid question record.");
                    }
                }
            } catch (IOException ex) {
                System.out.println("Question file error: " + ex.getMessage());
            }
            return bank;
        }
    }
    record QuizResult(String name, String roll, int score, int total, String mode) {}

    // CO3: recursion and methods
    static int recursiveTotal(int[] values, int index) {
        if (index == values.length) return 0;
        return values[index] + recursiveTotal(values, index + 1);
    }
}
