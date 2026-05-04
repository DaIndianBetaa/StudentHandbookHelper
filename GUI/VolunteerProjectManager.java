
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VolunteerProjectManager {

    private ArrayList<VolunteerProject> volunteerProjects;
    private String jsonFile = "projects.json";

    public VolunteerProjectManager()
    {
        volunteerProjects = new ArrayList<>();
    }
    public VolunteerProjectManager(ArrayList<VolunteerProject> _volunteerProjects)
    {
        volunteerProjects = _volunteerProjects;
    }

    public void AddProject(VolunteerProject _volunteerProject)
    {
        volunteerProjects.add(_volunteerProject);
    }


    public void AddProject(String _title, String _description, LocalDate _date, Float _hours)
    {
        VolunteerProject temp = new VolunteerProject(_title, _description, _date, _hours);
        volunteerProjects.add(temp);
    }

    public void RemoveProject(String _title)
    {
        for(int i = 0; i < volunteerProjects.size(); i++){
            if (volunteerProjects.get(i).getTitle().equals(_title)){
                volunteerProjects.remove(i);
                break;
            }
        }
    }


    public Float getTotalHours()
    {
        Float totalHours = 0f;
        for(int i = 0; i < volunteerProjects.size(); i++){
            totalHours += volunteerProjects.get(i).getHours();
        }
        return totalHours;
    }

    public void loadJsonData() {
        try {
            System.out.println("Working directory: " + new File(".").getAbsolutePath());
            List<String> lines = Files.readAllLines(Paths.get(jsonFile));
            StringBuilder sb = new StringBuilder();
            volunteerProjects = new ArrayList<>();
            for (String line : lines) sb.append(line.trim());


            String json = sb.toString();


            // Remove outer brackets
            json = json.substring(1, json.length() - 1).trim();


            // Split into rows
            String[] rows = json.split("],");

            if(json.isEmpty())
                return;
            for (String row : rows) {
                row = row.replace("[", "").replace("]", "").trim();
                String[] parts = row.split(",");


                String title = parts[0].replace("\"", "").trim();
                String description = parts[1].replace("\"", "").trim();
                LocalDate  date = LocalDate.parse(parts[2].replace("\"", "").trim());
                float hours = Float.parseFloat(parts[3].replace("\"", "").trim());
                volunteerProjects.add(new VolunteerProject(title, description, date, hours));
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading JSON file.");
        }
    }


    private void reloadJsonData() {
        loadJsonData();
    }


    // --- Grades SAVING to a json file
    // custom JSON writer so there is no external dependencies
    // to JSON library
    public void saveJsonData() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");

            for (int i = 0; i < getVolunteerProjects().size(); i++) {
                String title = getVolunteerProjects().get(i).getTitle();
                String description = getVolunteerProjects().get(i).getDescription();
                LocalDate date = getVolunteerProjects().get(i).getDate();
                float hours = getVolunteerProjects().get(i).getHours();


                sb.append("  [\"")
                        .append(title).append("\", \"")
                        .append(description).append("\", \"")
                        .append(date.toString()).append("\", ")
                        .append(hours).append("]");


                if (i < getVolunteerProjects().size() - 1) sb.append(",");
                sb.append("\n");
            }


            sb.append("]");


            Files.writeString(Paths.get(jsonFile), sb.toString());



        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving JSON file.");
        }
    }

    public ArrayList<VolunteerProject> getVolunteerProjects() {
        return volunteerProjects;
    }

}
