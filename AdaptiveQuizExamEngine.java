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
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<QuizQuestion> questionBank = QuizQuestion.loadBank();
        System.out.println("Loaded " + questionBank.size() + " MCQ questions.");
     System.out.println("Enter your name: ");
     String name = sc.nextLine().trim();
     while (name.isEmpty()) {
         System.out.print("Name cannot be empty. Enter your name: ");
         name = sc.nextLine().trim();
     }
     System.out.println("Enter your roll number: ");
     String rollno = sc.nextLine().trim();
     while (rollno.isEmpty()) {
         System.out.print("Roll number cannot be empty. Enter roll number: ");
         rollno = sc.nextLine().trim();
     }
     System.out.println("Enter your section: ");
     String sec = sc.nextLine().trim();
     while (sec.isEmpty()) {
         System.out.print("Section cannot be empty. Enter section: ");
         sec = sc.nextLine().trim();
     }
        
        String[][] questions = {
            {"What is 2 + 3?", "What is 7 - 4?", "What is 6 + 8?", "What is 9 - 2?", "What is 4 + 6?"},
            {"What is 12 * 2?", "What is 48 / 4?", "What is 9 * 3?", "What is 56 / 7?", "What is 11 * 4?"},
            {"What is 15 * 3 - 10?", "What is 8 * 7 + 6?", "What is 100 / 4 + 17?", "What is 12 * 5 - 9?", "What is 9 * 9 - 16?"}
        };
        int[][] answers = {
            {5, 3, 14, 7, 10},
            {24, 12, 27, 8, 44},
            {35, 62, 42, 51, 65}
        };

     int score = 0;
        int difficulty = 1; // 1 = Easy, 2 = Medium, 3 = Hard
        int[] correctByLevel = new int[3];
        int[] askedByLevel = new int[3];
        int streak = 0;
        int bestStreak = 0;
        int[] questionLevels = new int[5];
        String[] askedQuestions = new String[5];
        int[] studentAnswers = new int[5];
        int[] correctAnswers = new int[5];
        boolean[] answerResults = new boolean[5];

        System.out.println("=== Adaptive Quiz Exam Engine ===");

        for (int i = 1; i <= 5; i++) {

            System.out.println("\nQuestion " + i + " (Difficulty: " + difficulty + ")");

            int level = difficulty - 1;
            askedByLevel[level]++;
            int questionIndex = i - 1;
            System.out.println(questions[level][questionIndex]);
            questionLevels[i - 1] = level;
            askedQuestions[i - 1] = questions[level][questionIndex];
            int correctAnswer = answers[level][questionIndex];
            int userAnswer;

            System.out.print("Enter your answer: ");
            while (true) {
                String input = sc.nextLine().trim();
                try {
                    userAnswer = Integer.parseInt(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.print("Invalid number. Please try again: ");
                }
            }
            studentAnswers[i - 1] = userAnswer;
            correctAnswers[i - 1] = correctAnswer;
            answerResults[i - 1] = userAnswer == correctAnswer;

            // CHECK ANSWER
            if (userAnswer == correctAnswer) {
                System.out.println("Correct!");
                score++;
                correctByLevel[level]++;
                streak++;
                if (streak > bestStreak) bestStreak = streak;

                // Increase difficulty
                if (difficulty < 3) {
                    difficulty++;
                }
            } else {
                System.out.println("Wrong! Correct answer is: " + correctAnswer);
                streak = 0;

                // Decrease difficulty
                if (difficulty > 1) {
                    difficulty--;
                }
            }
        }

        // FINAL RESULT
        System.out.println("\n=== Exam Finished ===");
        System.out.println("Name: " + name);
        System.out.println("Roll No: " + rollno);
        System.out.println("Section: " + sec);
        System.out.println("Your Score: " + score + "/5");

        System.out.println("Percentage: " + (score * 100 / 5) + "%");
        System.out.println("Longest correct streak: " + bestStreak);
        System.out.println("Recursive total correct: " + recursiveTotal(correctByLevel, 0));
        System.out.println("Final difficulty: " + (difficulty == 1 ? "Easy" : difficulty == 2 ? "Medium" : "Hard"));

        String[] levelNames = {"Easy", "Medium", "Hard"};
        System.out.println("Performance by difficulty:");
        for (int level = 0; level < 3; level++) {
            System.out.println(levelNames[level] + ": " + correctByLevel[level]
                    + "/" + askedByLevel[level] + " correct");
        }

        System.out.println("Answer review:");
        for (int i = 0; i < 5; i++) {
            System.out.println("Q" + (i + 1) + " [" + levelNames[questionLevels[i]] + "] "
                    + askedQuestions[i] + ": Your answer = " + studentAnswers[i]
                    + ", Correct = " + correctAnswers[i]
                    + (answerResults[i] ? " (Correct)" : " (Wrong)"));
        }

        if (score == 5) {
            System.out.println("Perfect score! Great work!");
        } else if (score >= 4) {
            System.out.println("Excellent Performance!");
        } else if (score >= 2) {
            System.out.println("Good, but you can improve!");
        } else {
            System.out.println("Need more practice!");
        }
        sc.close();

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
