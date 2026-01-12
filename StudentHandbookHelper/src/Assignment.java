import java.time.*;
import java.io.File;

public class Assignment {
	private int id;
	private String name;
	private int priority;
	private String dueDate;
	private double pointsWorth;
	private double pointsScored;
	
	public Assignment(int id, String name, int priority, String dueDate, double pointsWorth, double pointsScored) {
		this.setId(id);
		this.setName(name);
		this.setPriority(priority);
		this.setPointsWorth(pointsWorth);
		this.setPointsScored(pointsScored);
		this.setDueDate(dueDate);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
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
	
	
	
}
