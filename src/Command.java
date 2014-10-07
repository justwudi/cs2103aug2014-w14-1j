import java.util.Date;
import java.util.LinkedList;

public class Command {
	public enum COMMAND_TYPE {
		ADD, EDIT, DELETE, LIST, SEARCH, COMPLETE, TAG, INVALID
	}

	private COMMAND_TYPE commandType;
	private String taskID;
	private String taskName;
	private Date taskDueDate;
	private String[] taskIDsToDelete;
	private LinkedList<String> taskTags;

	public void setCommandType(COMMAND_TYPE parsedCommandType) {
		commandType = parsedCommandType;
	}

	public COMMAND_TYPE getCommandType() {
		return commandType;
	}

	public void setTaskID(String ID) {
		taskID = ID;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskName(String name) {
		taskName = name;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskDueDate(Date date) {
		taskDueDate = date;
	}

	public Date getTaskDueDate() {
		return taskDueDate;
	}

	public void setTaskIDsToDelete(String[] IDs) {
		taskIDsToDelete = IDs;
	}

	public String[] getTaskIDsToDelete() {
		return taskIDsToDelete;
	}
}