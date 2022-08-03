package ru.yandex.javacourse.schedule;

import ru.yandex.javacourse.schedule.persistence.FileBasedTaskStorage;
import ru.yandex.javacourse.schedule.persistence.InMemoryTaskStorage;
import ru.yandex.javacourse.schedule.persistence.TaskStorage;
import ru.yandex.javacourse.schedule.model.Epic;
import ru.yandex.javacourse.schedule.model.Subtask;
import ru.yandex.javacourse.schedule.model.Task;
import ru.yandex.javacourse.schedule.model.TaskType;

import java.io.File;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.javacourse.schedule.model.TaskStatus.NEW;
import static ru.yandex.javacourse.schedule.model.TaskType.*;

public class StorageTest {
	public static void main(String[] args) throws Exception {
		File tmpdir = Files.createTempDirectory("tmp").toFile();
		tmpdir = new File("./tmp");

		System.out.println("Storing in " + tmpdir.getAbsolutePath());
		TaskStorage fileBasedTaskStorage = new FileBasedTaskStorage(tmpdir);
		testStorage(fileBasedTaskStorage);

		TaskStorage inMemoryTaskStorage = new InMemoryTaskStorage();
		testStorage(inMemoryTaskStorage);

	}
	public static void testStorage(TaskStorage storage) throws Exception {

		Task task1 = new Task(1, "Task #1", "Task1 description", NEW);
		Epic epic1 = new Epic(2, "Epic #2", "Epic1 description", NEW);
		Subtask subtask1 = new Subtask(3,"Subtask #1-2", "Subtask1 description", NEW, 4);
		epic1.addSubtaskId(5);
		epic1.addSubtaskId(6);
		epic1.addSubtaskId(3);
		storage.add(task1);
		storage.add(epic1);
		storage.add(subtask1);

		assert(storage.get(0) == null);
		assert(storage.get(1).getType() == TASK);
		assert(storage.get(1).getName().equals("Task #1"));
		assert(storage.get(1).getId() == 1);

		assert(storage.get(2).getType() == TaskType.EPIC);
		assert(storage.get(2) instanceof Epic);
		assert(storage.get(2).getName().equals("Epic #2"));
		assert(new HashSet<>(((Epic) storage.get(2)).getSubtaskIds()).equals(Set.of(3,5,6)));

		assert(storage.get(3).getType() == TaskType.SUBTASK);
		assert(storage.get(3) instanceof Subtask);
		assert(storage.get(3).getName().equals("Subtask #1-2"));

		assert(storage.get(4) == null);

		assert(storage.getAll(TASK).size() == 1);
		assert(storage.getAll(TASK).get(0).getId() == 1);

		assert(storage.getAll(TaskType.EPIC).size() == 1);
		assert(storage.getAll(TaskType.EPIC).get(0).getId() == 2);

		assert(storage.getAll(TaskType.SUBTASK).size() == 1);
		assert(storage.getAll(TaskType.SUBTASK).get(0).getId() == 3);

		assert(storage.remove(0) == null);
		assert(storage.remove(1).getType() == TASK);
		assert(storage.remove(2).getType() == TaskType.EPIC);
		assert(storage.remove(3).getType() == TaskType.SUBTASK);
		assert(storage.remove(4) == null);

		storage.add(task1);
		storage.add(epic1);
		storage.add(subtask1);
		assert(storage.getAll(TASK).size() == 1);
		assert(storage.getAll(TaskType.EPIC).size() == 1);
		assert(storage.getAll(TaskType.SUBTASK).size() == 1);
		storage.removeAll(TASK);
		assert(storage.getAll(TASK).size() == 0);
		assert(storage.getAll(TaskType.EPIC).size() == 1);
		assert(storage.getAll(TaskType.SUBTASK).size() == 1);
		storage.removeAll(EPIC);
		assert(storage.getAll(EPIC).size() == 0);
		storage.removeAll(SUBTASK);
		assert(storage.getAll(EPIC).size() == 0);
		System.out.println("all pass");
	}
}
