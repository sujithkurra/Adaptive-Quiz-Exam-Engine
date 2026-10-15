# Adaptive Quiz & Exam Engine

A basic console-based Java quiz project for Problem Solving using Programming (Java).

## Features
- Student name, roll number, and section entry
- Five arithmetic quiz questions
- Adaptive difficulty: correct answers increase difficulty; wrong answers decrease it
- Final score, percentage, and performance feedback
- Numeric-answer input validation

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
