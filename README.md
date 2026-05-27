# Star Student

A desktop application for students to manage their grades, to-do list, and volunteer projects.

## Features

### Grades
- Add courses with a letter grade (A+ through F) and credit value
- Automatically calculates your unweighted GPA
- Save and undo changes
- Data is saved locally to a JSON file

### To-Do List
- Add tasks with a title, description, due date, and optional priority
- Mark tasks as complete
- Remove tasks
- Tasks are color coded by status: overdue, due soon, and completed
- Sort tasks by due date, priority, or title
- Shows a reminder on startup for any overdue or upcoming tasks

### Volunteer Projects
- Add volunteer projects with a title, description, due date, and hours spent
- View total hours across all projects
- Delete projects via right-click
- Data is saved locally to a JSON file

### Settings
- Set your student name
- Toggle dark mode
- Change font size
- Choose an accent color

## Data
All data is stored locally. Tasks are saved to `todos.csv`, grades to `grades.jsn`, and volunteer projects to `projects.json`.
