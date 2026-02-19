import java.io.*;
import java.time.LocalDate;


class VolunteerProject{
    private String Title;
    private String Description;
    private LocalDate Date;
    private Float Hours;

    public VolunteerProject(String _title, String _description, LocalDate _date, Float _hours){
        Title = _title;
        Description = _description;
        Date = _date;
        Hours = _hours;
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

    public Float getHours() {
        return Hours;
    }

    public void setHours(Float hours) { Hours = hours; }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

}


