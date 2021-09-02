package duke.command;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;
import duke.TaskList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Dealing with loading tasks from the file and saving tasks in the file.
 */
public class Storage {
    private File src;
    private TaskList taskList;

    /**
     * Constructor of Storage class asking for a filePath and a taskList
     *
     * @param filePath The path of the input file.
     * @param taskList The TaskList stored tasks' information.
     */
    public Storage(String filePath, TaskList taskList) {
        try {
            this.src = new File(filePath);
            this.taskList = taskList;
            if (this.src.createNewFile()) {
                System.out.println("I have created a new file for you :)");
            }
        } catch (IOException e) {
            System.out.println("I cannot create a new file for you :(");
        }
    }

    /**
     * Deals with updating the tasks file when taskList is modified.
     */
    public void modifyTasks() {
        try {
            FileWriter fileWriter = new FileWriter(this.src);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter fileWriter = new FileWriter(this.src, true);
            for (int i = 0; i < taskList.size(); i++) {
                fileWriter.write(taskList.get(i).toStoredString());
                if (i != taskList.size() - 1) {
                    fileWriter.write("\n");
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads through the src file and update the taskList.
     *
     * @param sc The scanner for the src file.
     */
    public void readEvents(Scanner sc) {
        while (sc.hasNextLine()) {
            String task = sc.nextLine();

            if (task.equals("\n")) {
                continue;
            }

            char taskType = task.charAt(0);
            char taskDone = task.charAt(4);
            String taskValue = task.substring(8);
            boolean finished = (taskDone == '1');
            Task curTask = new Task("");
            int separatorPosition;

            switch (taskType) {
            case 'T':
                curTask = new Todo(taskValue, finished);
                break;
            case 'E':
                separatorPosition = taskValue.indexOf('|');
                String eventName = taskValue.substring(0, separatorPosition - 1);
                String eventTime = taskValue.substring(separatorPosition + 2);
                curTask = new Event(eventName, finished, eventTime);
                break;
            case 'D':
                separatorPosition = taskValue.indexOf('|');
                String deadlineName = taskValue.substring(0, separatorPosition - 1);
                String deadlineTime = taskValue.substring(separatorPosition + 2);
                curTask = new Deadline(deadlineName, finished, deadlineTime);
            }
            taskList.add(curTask);
        }
    }

    /**
     * Adds new task to the file when it is added to the taskList.
     * @param task
     */
    public void saveNewTask(Task task) {
        try {
            FileWriter fileWriter = new FileWriter(this.src, true);
            fileWriter.write(task.toStoredString() + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads tasks saved on the src file.
     */
    public void loadSavedTasks() {
        try {
            Scanner sc = new Scanner(src);
            readEvents(sc);
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
