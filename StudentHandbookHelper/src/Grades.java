import java.util.Scanner;
import java.util.ArrayList;

public class Grades {

	private ArrayList<String> grades = new ArrayList<>();
    private ArrayList<Integer> gradeCredits = new ArrayList<>();

	
	//accepts letter grades with their respective credit values.
	//type "X" or "x" to STOP adding grades.
	public void addGrade(){
		boolean run = true;
		while(run) {
            Scanner scanner = new Scanner(System.in);
			System.out.print("Enter grade (A, B+, C-, etc.) or X to stop: ");
	     	String grade = scanner.nextLine();
            if(grade.equalsIgnoreCase("X")) {
                run = false;
                break;
            }
			
			System.out.print("Enter credit value earned in this course: ");
			int credit = scanner.nextInt();

			grades.add(grade); 
		    gradeCredits.add(credit);

//			System.out.print("Add another class? (Y/N): ");
//            String choice = scanner.nextLine().trim();
//			while (true) {
//				if (choice.equalsIgnoreCase("N")) {
//					System.out.println("Stopping grade entry.");
//		            run = false;
//				}
//				if (choice.equalsIgnoreCase("Y")) {
//			        break;
//			    } else if (choice.equalsIgnoreCase("N")) {
//			        run = false;
//			    } else {
//			        System.out.println("Uhhh… what? That wasn’t Y or N. Try again.");
//			    }
//			}
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
