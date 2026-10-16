# Adaptive Quiz & Exam Engine

A simple Java console project for beginner-level programming.

## Run
```bash
javac AdaptiveQuizExamEngine.java
java AdaptiveQuizExamEngine
```

## What it does
- Asks for student name, roll number and section.
- Asks five maths questions.
- Starts at easy level, increases difficulty after a correct answer and decreases after a wrong answer.
- Shows score, percentage and a simple answer review.
- Saves the result in `data/results.csv`.

## Java concepts used
- CO1: variables, operators, Scanner, output.
- CO2: if/else, for and while loops.
- CO3: methods and arrays.
- CO4: a class, static methods and try/catch.
- CO5: strings and basic file handling.
- CO6: an ArrayList for the answer review.

This version is intentionally small and easy to explain. It does not implement advanced inheritance, interfaces, streams, a leaderboard or a separate exam mode. The optional `data/questions.txt` file from an earlier version is not used by this simplified program.
