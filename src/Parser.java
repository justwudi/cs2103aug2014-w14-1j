import org.ocpsoft.prettytime.shade.org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

import static org.mentaregex.Regex.match;
import static org.mentaregex.Regex.matches;

public class Parser {

	DateParser dateParser = new DateParser();

	private String command;
	private Command commandObj;
	private Command.COMMAND_TYPE commandType;
	private final int TYPO_DISTANCE = 1;

	private String[] addCommands = {"add","insert"};
	private String[] editCommands = {"edit","update","change","modify"};
	private String[] deleteCommands = {"delete","remove","destroy"};
	private String[] listCommands = {"list"};
	private String[] searchCommands = {"search","find"};
	private String[] completeCommands = {"complete"};
	private String[] undoCommands = {"undo"};
	private String[] exitCommands = {"quit"};

	public Command parseCommand(String userCommand) {
		command = userCommand;
		String commandTypeString = getFirstWord(command).toLowerCase();
		commandType = parserCommandType(commandTypeString);
		return generateCommandObj();
	}

	private String getFirstWord(String input) {
		return input.split("\\s+")[0];
	}

	private Command.COMMAND_TYPE parserCommandType(String commandTypeString) {
		if (isAddCommand(commandTypeString)) {
			return Command.COMMAND_TYPE.ADD;
		} else if (isEditCommand(commandTypeString)) {
			return Command.COMMAND_TYPE.EDIT;
		} else if (isDeleteCommand(commandTypeString)) {
			return Command.COMMAND_TYPE.DELETE;
		} else if (isListCommand(commandTypeString)) {
			return Command.COMMAND_TYPE.LIST;
		} else if (isSearchCommand(commandTypeString)) {
			return Command.COMMAND_TYPE.SEARCH;
		} else if (isCompleteCommand(commandTypeString)) {
			return Command.COMMAND_TYPE.COMPLETE;
		} else if (isUndoCommand(commandTypeString)) {
			return Command.COMMAND_TYPE.UNDO;
		} else if (isExitCommand(commandTypeString)) {
			return Command.COMMAND_TYPE.EXIT;
		} else {
			return Command.COMMAND_TYPE.INVALID;
		}
	}

	private boolean isAddCommand(String commandTypeString) {
		return containsCommand(commandTypeString, addCommands);
	}

	private boolean isEditCommand(String commandTypeString) {
		return containsCommand(commandTypeString, editCommands);
	}

	private boolean isDeleteCommand(String commandTypeString) {
		return containsCommand(commandTypeString, deleteCommands);
	}

	private boolean isListCommand(String commandTypeString) {
		return containsCommand(commandTypeString, listCommands);
	}

	private boolean isSearchCommand(String commandTypeString) {
		return containsCommand(commandTypeString, searchCommands);
	}

	private boolean isCompleteCommand(String commandTypeString) {
		return containsCommand(commandTypeString, completeCommands);
	}

	private boolean isUndoCommand(String commandTypeString) {
		return containsCommand(commandTypeString, undoCommands);
	}

	private boolean isExitCommand(String commandTypeString) {
		return containsCommand(commandTypeString, exitCommands);
	}

	private boolean containsCommand(String commandTypeString, String[] commands) {
		boolean result = false;
		for (String command: commands) {
			if (StringUtils.getLevenshteinDistance(commandTypeString, command) <= TYPO_DISTANCE) {
				result = true;
			}
		}
		return result;
	}

	private Command generateCommandObj() {
		commandObj = new Command();
		commandObj.setCommandType(commandType);
		String commandDetails = removeCommand();
		if (!commandDetails.equals("")) {
			switch (commandType) {
				case ADD:
					generateAddCommandObj(commandDetails);
					break;
				case EDIT:
					generateEditCommandObj(commandDetails);
					break;
				case DELETE:
					generateDeleteCommandObj(commandDetails);
					break;
				case LIST:
					generateListCommandObj(commandDetails);
					break;
				case SEARCH:
					generateSearchCommandObj(commandDetails);
					break;
				case COMPLETE:
					generateCompleteCommandObj(commandDetails);
					break;
			}
		}
		return commandObj;
	}

	private String removeCommand() {
		if (matches(command, "\\s+")) {
			return command.replaceFirst("^(\\w+)\\s+","");
		} else {
			return "";
		}
	}

	private String[] dateIdentifiers = {"to","until","til","till","by","due","on","from"};

	private void generateAddCommandObj(String commandDetails) {
		assert (!commandDetails.trim().equals("")) : "commandDetails is empty!";
		commandObj.setTaskTags(parseTaskTags(commandDetails));
		commandDetails = removeTaskTags(commandDetails);
		commandDetails = dateParser.parseCommand(commandDetails, commandType, commandObj);
		commandObj.setTaskName(parseTaskName(commandDetails));
	}

	private void generateEditCommandObj(String commandDetails) {
		assert (!commandDetails.trim().equals("")) : "commandDetails is empty!";
		String[] IDs = parseTaskID(commandDetails);
		commandObj.setTaskID(IDs[0]);
		commandDetails = removeTaskID(commandDetails);
		commandObj.setTaskTags(parseTaskTags(commandDetails));
		commandDetails = removeTaskTags(commandDetails);
		commandDetails = dateParser.parseCommand(commandDetails, commandType, commandObj);
		commandObj.setTaskName(parseTaskName(commandDetails));
	}

	private void generateDeleteCommandObj(String commandDetails) {
		assert (!commandDetails.trim().equals("")) : "commandDetails is empty!";
		commandObj.setTaskIDsToDelete(parseTaskID(commandDetails));
	}

	private void generateListCommandObj(String commandDetails) {
		assert (!commandDetails.trim().equals("")) : "commandDetails is empty!";
		dateParser.parseCommand(commandDetails, commandType, commandObj);
	}

	private void generateSearchCommandObj(String commandDetails) {
		assert (!commandDetails.trim().equals("")) : "commandDetails is empty!";
		commandObj.setSearchTags(parseTaskTags(commandDetails));
		commandDetails = removeTaskTags(commandDetails);
		commandDetails = dateParser.parseCommand(commandDetails, commandType, commandObj);
		String[] array = commandDetails.split("\\s+");
		ArrayList<String> keywords = new ArrayList<String>();
		for (String keyword: array) {
			keywords.add(removeLeadingAndClosingPunctuation(keyword));
		}
		commandObj.setSearchKeywords(keywords);
	}

	private void generateCompleteCommandObj(String commandDetails) {
		assert (!commandDetails.trim().equals("")) : "commandDetails is empty!";
		commandObj.setTaskIDsToComplete(parseTaskID(commandDetails));
	}

	private String parseTaskName(String commandDetails) {
		return removeLeadingAndClosingPunctuation(commandDetails);
	}

	private String[] parseTaskID(String commandDetails) {
		return match(commandDetails, "/([TFOtfo]\\d+)/g");
	}

	private String removeTaskID(String commandDetails) {
		return commandDetails.split("\\s+", 2)[1];
	}

	private String[] parseTaskTags(String commandDetails) {
		return match(commandDetails, "/(\\B@[a-zA-Z0-9-]+)/g");
	}

	private String removeTaskTags(String commandDetails) {
		return commandDetails.replaceAll("\\B@[a-zA-Z0-9-]+", "");
	}

	private String removeLeadingAndClosingPunctuation(String input) {
		return input.replaceFirst("^[^0-9a-zA-Z]+", "").replaceAll("[^0-9a-zA-Z]+$", "");
	}
}