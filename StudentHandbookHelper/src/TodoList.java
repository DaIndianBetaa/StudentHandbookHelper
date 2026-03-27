import java.util.ArrayList;
import java.io.*;

public class TodoList {
	private ArrayList<TodoItem> todos;
	private String filePath;
	private int currId;;
	
	public TodoList (String filePath) {
		setTodos(new ArrayList<TodoItem>());
		this.setFilePath(filePath);
		currId = 0;
	}
	
	public void addTodo (TodoItem item) {
		item.setId(currId);
		currId++;
		todos.add(item);
	}

	public void removeTodo (int id) {
		todos.removeIf(todo -> todo.getId() == id); //was going to use normal arraylist remove but did this so it based of ID number
	}
	
	public void markComplete(int id) {
		for (TodoItem item: todos) {
			if (item.getId() == id) {
				item.setCompleted(true);
				return; //added this so it saves time and doesn't check the rest of arraylist
			}
		}
		System.out.printf("ERROR. Todo with ID # %d is not found. \n", id);
	}
	
	public void sortByDueDate() {
        todos.sort((a, b) -> a.getDueDate().compareTo(b.getDueDate()));
    }

	public void sortRevByDueDate() {
        todos.sort((a, b) -> b.getDueDate().compareTo(a.getDueDate()));
    }
	
    public void sortByPriority() {
        todos.sort((a, b) -> Double.compare(b.getPriority(), a.getPriority()));
    }

    public void sortRevByPriority() {
        todos.sort((a, b) -> Double.compare(a.getPriority(), b.getPriority()));
    }
        
    public void sortAlphabetically() {
        todos.sort((a, b) -> a.getTitle().compareTo(b.getTitle()));
    }

    public void sortRevAlphabetically() {
        todos.sort((a, b) -> b.getTitle().compareTo(a.getTitle()));
    }
    
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(currId)); // First line = ID counter
            writer.newLine();
            for (TodoItem item : todos) {
                writer.write(item.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving todos: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        todos.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            currId = Integer.parseInt(reader.readLine());
            String line;
            while ((line = reader.readLine()) != null) {
                todos.add(TodoItem.fromFileString(line));
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existing todo file found, starting fresh.");
        } catch (IOException e) {
            System.out.println("Error loading todos: " + e.getMessage());
        }
    }

    
	public ArrayList<TodoItem> getTodos() {
		return todos;
	}

	public void setTodos(ArrayList<TodoItem> todos) {
		this.todos = todos;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
