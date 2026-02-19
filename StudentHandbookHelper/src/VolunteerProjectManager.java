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
    public Float GetTotalHours()
    {
        Float totalHours = 0f;
        for(int i = 0; i < volunteerProjects.size(); i++){
            totalHours += volunteerProjects.get(i).getHours();
        }
        return totalHours;
    }
}
