
import java.util.ArrayList;
import java.util.Calendar;

public class Task {
	private String taskName;
	private String taskId;
	private ArrayList<Calendar> taskDatesTimes;
	private ArrayList<Calendar> taskReminderDatesTimes;
	private boolean taskFloating;
	private String taskRecur;
	private boolean taskCompleted;
	private ArrayList<String> taskTag;
	
	/*
	 * No input constructor
	 */
	public Task() {
		taskId = "";
		taskName = "";
		taskDatesTimes = new ArrayList<Calendar>();
		taskReminderDatesTimes = new ArrayList<Calendar>();
		taskFloating = false;
		taskRecur = "";
		taskCompleted = false;
		taskTag = new ArrayList<String>();
	}
	
	public Task(String taskId, String taskName, ArrayList<Calendar> taskDatesTimes,
			ArrayList<Calendar> taskReminderDatesTimes, boolean taskFloating,
			String taskRecur, boolean taskCompleted, ArrayList<String> taskTag) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.taskDatesTimes = taskDatesTimes;
		this.taskReminderDatesTimes = taskReminderDatesTimes;
		this.taskFloating = taskFloating;
		this.taskRecur = taskRecur;
		this.taskCompleted = taskCompleted;
		this.taskTag = taskTag;
	}

	// Task ID************************************
	public void setTaskId(int id) {
		this.taskId = "" + id;
	}

	public String getTaskId() {
		return this.taskId;
	}

	// Task ID************************************

	// Task Name ************************************
	public void setTaskName(String taskname) {
		this.taskName = taskname;
	}

	public String getTaskName() {
		return this.taskName;
	}

	// Task Name ************************************

	// Task Dates and Times ********************************
	public void setTaskDatesTimes(ArrayList<Calendar> al){
		this.taskDatesTimes = al;
	}
	public void removeTaskDatesTimes(Calendar date){
		this.taskDatesTimes.remove(date);
	}
	public void addTaskDatesTimes(Calendar date) {
		this.taskDatesTimes.add(date);
	}

	public ArrayList<Calendar> getTaskDatesTimes() {
		return this.taskDatesTimes;
	}
	// Task Dates and Times ********************************

	// Task Reminder Dates Times***********************************
	public void setTaskReminderDatesTimes(ArrayList<Calendar> al){
		this.taskReminderDatesTimes = al;
	}
	public void removeTaskReminderDatesTimes(Calendar date){
		this.taskReminderDatesTimes.remove(date);
	}
	public void addTaskReminderDatesTimes(Calendar date) {
		this.taskReminderDatesTimes.add(date);
	}

	public ArrayList<Calendar> getTaskReminderDatesTimes() {
		return this.taskReminderDatesTimes;
	}

	// Task Reminder Dates Times***********************************

	// Task Floating*******************************
	public void setTaskFloating(boolean b) {
		this.taskFloating = b;
	}

	public boolean isTaskFloating() {
		return this.taskFloating;
	}

	// Task Floating*******************************

	// Task Recur***************************************
	public void setTaskRecur(String str) {
		this.taskRecur = str;
	}

	public String getTaskRecur() {
		return this.taskRecur;
	}

	// Task Recur***************************************

	// Task Completed*********************************************
	public void setTaskCompleted(boolean b) {
		this.taskCompleted = b;
	}

	public boolean getTaskCompleted() {
		return this.taskCompleted;
	}

	// Task Completed*********************************************

	// Task Tags**************************************
	public void addTaskTags(String str) {
		this.taskTag.add(str);
	}

	public ArrayList<String> getTaskTags() {
		return this.taskTag;
	}
	
	// Additional methods*****************************************
	
	// Search function********************
	
	private boolean containsKeyword(String keyword) {
		return taskName.contains(keyword);
	}
	
	private boolean containsTag(String tag) {
		return taskTag.contains(tag);
	}
	
	public boolean containsKeywords(ArrayList<String> keywords) {
		for (String keyword : keywords) {
			if (!containsKeyword(keyword)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean containsTags(ArrayList<String> tags) {
		for (String tag : tags) {
			if (!containsTag(tag)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean withinDateRange(Calendar start_date, Calendar end_date) {
		if (taskFloating) return true;		//autopass
		for (Calendar date : taskDatesTimes) {
			if (start_date == null || date.after(start_date)) {
				if (end_date == null || date.before(end_date)) {
					return true;
				}
			}
		}
		return false;
	}
}
