# Adaptive Quiz & Exam Engine (Java)

Project ID: KLH/PSPJ/S11/3

## Run (Java 17+)
From the repository root:
```bash
javac AdaptiveQuizExamEngine.java
java AdaptiveQuizExamEngine
```

The program asks for name, roll number, and section. The menu provides adaptive quiz, exam mode, leaderboard, question-bank summary, and exit.

## CO1–CO6 implementation
- **CO1 – Fundamentals:** Java variables, Scanner, arithmetic and formatted console output.
- **CO2 – Control flow:** switch menu, if/else, for/while loops; adaptive mode increases/decreases difficulty after **two consecutive** correct/incorrect answers.
- **CO3 – Methods, recursion, arrays:** modular methods, recursiveTotal(), and a 2D difficulty performance matrix.
- **CO4 – OOP, interfaces, exceptions:** abstract Question class, QuizQuestion inheritance, Assessable interface, private fields, QuizResult record, exception handling.
- **CO5 – Strings and file I/O:** trim(), split(), Files.readAllLines() for `data/questions.txt`, and Files.writeString() for `data/results.csv`.
- **CO6 – Collections and streams:** List question bank, Set to avoid repeated questions, Map for topic scoring, Stream grouping/filtering/sorting, Comparator leaderboard.

## Question bank format
Edit `data/questions.txt` to add questions. Each row has eight pipe-separated fields:
```
topic|difficulty(0-2)|question|option1|option2|option3|option4|correctOption(1-4)
```
Difficulty 0=easy, 1=medium, 2=hard. The included file contains 15 questions. Invalid rows are skipped.

## Notes
- Results are saved locally to `data/results.csv`, which is ignored by Git to avoid publishing student data.
- Adaptive and exam modes each ask up to five multiple-choice questions.
- This is a Java console application, not a GUI or web app.
- The project should be compiled and demonstrated locally before submission.
