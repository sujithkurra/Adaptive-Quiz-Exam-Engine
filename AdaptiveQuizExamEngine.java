import java.util.Scanner;
public class AdaptiveQuizExamEngine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
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

        if (score >= 4) {
            System.out.println("Excellent Performance!");
        } else if (score >= 2) {
            System.out.println("Good, but you can improve!");
        } else {
            System.out.println("Need more practice!");
        }
        sc.close();

    }
}
