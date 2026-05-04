import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors; //sort stuff

public class TodoItem {
	private int id;
	private String title;
	private String description;
	private int priority;
	private LocalDate dueDate;
	private boolean completed;
	
	public TodoItem(int id, String title, String description, int priority, LocalDate dueDate) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.priority = priority;
		completed = false;
	}
	
	public double calcPriority(double numDays) {
		if (numDays <= 0) {
			return 5;
		}
		return 5/numDays;
	}
	
	public TodoItem(int id, String title, String description, LocalDate dueDate) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.priority = (int) calcPriority(daysTillDue());
		completed = false;
	}
	
	
	
	public boolean isDueSoon() {
		return ChronoUnit.DAYS.between(LocalDate.now(), dueDate) <= 3;
	}
	
	public double daysTillDue() {
		return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
	}
	
	public String toFileString() {
		List<String> filepath = Arrays.asList(id + "", title, description, priority + "", dueDate.toString(), completed + ""); //all of these used to make displaying information easier (courtesy of Google AI)
		String result = filepath.stream().map(String::valueOf).collect(Collectors.joining(","));
		return result;
	}
	public static TodoItem fromFileString(String line) { //auto changed to static since it wouldn't wor for file read write
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String title = parts[1];
        String description = parts[2];
        double priority = Double.parseDouble(parts[3]);
        LocalDate dueDate = LocalDate.parse(parts[4]);
        boolean completed = Boolean.parseBoolean(parts[5]);

        TodoItem item = new TodoItem(id, title, description, dueDate);
        item.priority = (int) priority;
        if (completed) {
        	item.setCompleted(true);
        }
        return item;
    }

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
