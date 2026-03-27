import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class Main {
   public static VolunteerProjectManager volunteerProjects = new VolunteerProjectManager();
    public static void main(String[] args) {
        boolean run = true;
        while(run){
            Scanner input = new Scanner(System.in);
            System.out.println("What do you want to do? select one: \n1.) Grades\n2.) Assignments\n3.) Volunteer Hours\n(Only volunteer hours implemented so far)");
            String navigation = input.nextLine();
            switch (navigation){
                case "3":
                    while(true) {
                        System.out.println("What do you want to do? select one: \n1.) add a volunteer project\n2.) remove a volunteer project\n3.) Check total hours\n4.) quit");
                        navigation = input.nextLine();
                        if (navigation.equals("1")) {
                            LocalDate date = LocalDate.now();
                            System.out.println("Project Name: ");
                            String projectName = input.nextLine();
                            System.out.println("Project Description: ");
                            String projectDescription = input.nextLine();
                            System.out.println("Hours: ");
                            float hours = input.nextFloat();
                            volunteerProjects.AddProject(projectName, projectDescription, date, hours);
                        } else if (navigation.equals("2")) {
                            System.out.println("Project Name: ");
                            String projectName = input.nextLine();
                            volunteerProjects.RemoveProject(projectName);
                        } else if (navigation.equals("3")) {
                            System.out.println("Total hours: " + volunteerProjects.getTotalHours());
                        } else if (navigation.equals("4")) {
                            break;
                        } else
                            System.out.println("Invalid input, Try again");
                    }
                    break;
                default:
                    System.out.println("Invalid input, try again");
                    break;
            }
        }
    }
}
