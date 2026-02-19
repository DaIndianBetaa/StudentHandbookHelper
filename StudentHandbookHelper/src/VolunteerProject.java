import java.io.*;
import java.time.LocalDate;


class VolunteerProject{
    String Title;
    String Description;
    LocalDate StartDate;
    LocalDate EndDate;

    public VolunteerProject(String _title, String _description, LocalDate _startDate, LocalDate _endDate){
        Title = _title;
        Description = _description;
        StartDate = _startDate;
        EndDate = _endDate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public LocalDate getEndDate() {
        return EndDate;
    }

    public void setEndDate(LocalDate endDate) {
        EndDate = endDate;
    }

    public LocalDate getStartDate() {
        return StartDate;
    }

    public void setStartDate(LocalDate startDate) {
        StartDate = startDate;
    }

}


