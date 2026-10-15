# Adaptive Quiz & Exam Engine

A basic console-based Java quiz project for Problem Solving using Programming (Java).

## Features
- Student name, roll number, and section entry
- Five arithmetic quiz questions selected from an easy, medium, and hard question bank
- Adaptive difficulty: correct answers increase difficulty; wrong answers decrease it
- Final score, percentage, performance feedback, difficulty-wise breakdown, and longest correct streak
- Question-by-question answer review
- Numeric-answer input validation and required student details

## Requirements
Java JDK 17 or later.

## Run
```bash
javac AdaptiveQuizExamEngine.java
java AdaptiveQuizExamEngine
```

## How it works
The quiz starts at EASY. Correct answers increase difficulty (up to HARD); incorrect answers decrease it (down to EASY). Questions are chosen by the current difficulty. This introductory version repeats a question when the same level is encountered and accepts numeric answers, not multiple-choice selections.

## Team
- Sujith Kurra — 2620080098
- Rahul Datta — 2620030499
- Swarajith — 2620030583

**Guide:** Dr. N. Shiva Krishna

**Course:** Problem Solving using Programming (Java)

## Java concepts
Scanner, variables, conditionals, loops, strings, and input validation. More advanced course outcomes such as object-oriented design, file handling, and collections are not yet implemented in this basic version.

## Example quiz flow
1. Enter your name, roll number, and section.
2. Answer each arithmetic question using a number.
3. Correct answers increase the next question's difficulty; wrong answers decrease it.
4. Review the final score and performance at each difficulty level.

## Expo demonstration tips
- Show a correct answer to demonstrate the difficulty increasing.
- Show a wrong answer to demonstrate the difficulty decreasing.
- Show the results with the student's roll number.
- Open the GitHub **Commits** tab and explain each genuine change.

## Recent improvements
The question bank is declared inside the Java `main` method. The results now include the longest consecutive correct-answer streak and a review of each submitted answer.
