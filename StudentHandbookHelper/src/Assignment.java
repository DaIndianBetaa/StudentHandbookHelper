import java.time.*;
import java.time.temporal.ChronoUnit;
import java.io.File;

public class Assignment {
	private int id;
	private String name;
	private double priority;
	private LocalDate dueDate;
	private double pointsWorth;
	private double pointsScored;
	private double daysUntil;
	
	
	public Assignment(int id, String name, int priority, LocalDate dueDate, double pointsWorth, double pointsScored) {
		this.setId(id);
		this.setName(name);
		this.setPriority(priority);
		this.setPointsWorth(pointsWorth);
		this.setPointsScored(pointsScored);
		this.setDueDate(dueDate);
		this.setDaysUntil();
		
	}
	public Assignment(int id, String name, LocalDate dueDate, double pointsWorth, double pointsScored) {
		this.setId(id);
		this.setName(name);
		this.setPriority(pointsWorth / daysUntil);
		this.setPointsWorth(pointsWorth);
		this.setPointsScored(pointsScored);
		this.setDueDate(dueDate);
		this.setDaysUntil();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPriority() {
		return priority;
	}

	public void setPriority(double priority) {
		this.priority = priority;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public double getPointsWorth() {
		return pointsWorth;
	}

	public void setPointsWorth(double pointsWorth) {
		this.pointsWorth = pointsWorth;
	}

	public double getPointsScored() {
		return pointsScored;
	}

	public void setPointsScored(double pointsScored) {
		this.pointsScored = pointsScored;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public double getPercentage() {
		return (pointsScored/pointsWorth) * 100;
	}
	
	public void setDaysUntil() {
		daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), dueDate); //Looked into the java.time library and found ChronoUnit which should work
	}
	
	public boolean isCompleted() {
		return pointsScored >= 0;
	}
	
	public String toFileString;
	
}
