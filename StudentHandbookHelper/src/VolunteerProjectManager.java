
import java.time.LocalDate;
import java.util.ArrayList;

public class VolunteerProjectManager {
    private ArrayList<VolunteerProject> volunteerProjects;
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
}
