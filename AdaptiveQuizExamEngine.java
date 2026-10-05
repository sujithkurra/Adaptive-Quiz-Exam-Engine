import java.util.Scanner;
public class AdaptiveQuizExamEngine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
     System.out.println("Enter your name: ");
     String name = sc.next();
     System.out.println("Enter your roll number: ");
     long rollno = sc.nextLong();
     System.out.println("Enter your section: ");
     int sec = sc.nextInt();
        
     int score = 0;
        int difficulty = 1; // 1 = Easy, 2 = Medium, 3 = Hard

        System.out.println("=== Adaptive Quiz Exam Engine ===");

        for (int i = 1; i <= 5; i++) {

            System.out.println("\nQuestion " + i + " (Difficulty: " + difficulty + ")");

            int correctAnswer = 0;
            int userAnswer;
            // EASY LEVEL
            if (difficulty == 1) {
                System.out.println("What is 2 + 3?");
                correctAnswer = 5;
            }

            // MEDIUM LEVEL
            else if (difficulty == 2) {
                System.out.println("What is 12 * 2?");
                correctAnswer = 24;
            }

            // HARD LEVEL
            else {
                System.out.println("What is 15 * 3 - 10?");
                correctAnswer = 35;
                break;
            }

            System.out.print("Enter your answer: ");
            userAnswer = sc.nextInt();

            // CHECK ANSWER
            if (userAnswer == correctAnswer) {
                System.out.println("Correct!");
                score++;

                // Increase difficulty
                if (difficulty < 3) {
                    difficulty++;
                }
            } else {
                System.out.println("Wrong! Correct answer is: " + correctAnswer);

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

        if (score >= 4) {
            System.out.println("Excellent Performance!");
        } else if (score >= 2) {
            System.out.println("Good, but you can improve!");
        } else {
            System.out.println("Need more practice!");
        }

    }
}

