import java.io.*


class VolunteerProject{
    String Title;
    String Description;
    Date StartDate;
    Date EndDate;

    public volunteerProject(String _title, String _description, Date _startDate, Date _endDate){
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

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

}

