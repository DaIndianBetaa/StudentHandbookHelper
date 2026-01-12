import java.util.Scanner;
import java.util.ArrayList;

public class Grades {

	private ArrayList<String> grades;
	private ArrayList<Integer> gradeCredits;
	
	//accepts letter grades with their respective credit values.
	//type "X" or "x" to STOP adding grades.
	public void addGrade(){
		Scanner gradeScanner = new Scanner(System.in);
		Scanner creditScanner = new Scanner(System.in);
		boolean run = true;
		while(run) {
			String grade = gradeScanner.nextLine();
			int credit = creditScanner.nextInt();
			if (grade.equals("X") || grade.equals("x")) {
			run = false;
			}
			else {
				grades.add(grade); 
				gradeCredits.add(credit);
			}
		}
	}
	// Course potential credit (gradePoints)is MULTIPLED by its earned credit for every grade.
	// Then each product is added for a sum. That sum is multiplied by totalCredit for the GPA.
	public double calculateGPA() {
		double gradePoints = 0; 
		double totalCredit = 0; //Sum of all potential credits added together
		for (int i = 0; i < grades.size(); i++) {
			gradePoints += letterGradeToVal(grades.get(i)) * gradeCredits.get(i);
			totalCredit += gradeCredits.get(i);
		}
		
		return gradePoints / totalCredit;
	}
	
	// Converts a letter grade to its numerical value
	public static double letterGradeToVal(String grade) {
        double value;
        if (grade.equals("A+") || grade.equals("A")) {
            value = 4.0;
        } else if (grade.equals("A-")) {
            value = 3.7;
        } else if (grade.equals("B+")) {
            value = 3.3;
        } else if (grade.equals("B")) {
            value = 3.0;
        } else if (grade.equals("B-")) {
            value = 2.7;
        } else if (grade.equals("C+")) {
            value = 2.3;
        } else if (grade.equals("C")) {
            value = 2.0;
        } else if (grade.equals("C-")) {
            value = 1.7;
        } else if (grade.equals("D+")) {
            value = 1.3;
        } else if (grade.equals("D")) {
            value = 1.0;
        } else if (grade.equals("F")) {
            value = 0.0;
        } else {
            value = -1; // Indicate invalid grade
        }
        return value;
    }
		
}
