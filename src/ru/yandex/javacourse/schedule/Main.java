package ru.yandex.javacourse.schedule;

import static ru.yandex.javacourse.schedule.model.TaskStatus.DONE;
import static ru.yandex.javacourse.schedule.model.TaskStatus.IN_PROGRESS;
import static ru.yandex.javacourse.schedule.model.TaskStatus.NEW;

import ru.yandex.javacourse.schedule.history.FileBasedHistoryManager;
import ru.yandex.javacourse.schedule.history.HistoryManager;
import ru.yandex.javacourse.schedule.manager.TaskManager;
import ru.yandex.javacourse.schedule.manager.UniversalTaskManager;
import ru.yandex.javacourse.schedule.persistence.FileBasedTaskStorage;
import ru.yandex.javacourse.schedule.persistence.TaskStorage;
import ru.yandex.javacourse.schedule.model.Epic;
import ru.yandex.javacourse.schedule.model.Subtask;
import ru.yandex.javacourse.schedule.model.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
	public static void main(String[] args) throws IOException {

		//IoC
		File tmpdir = Files.createTempDirectory("tmp").toFile();
		tmpdir.deleteOnExit();

		TaskStorage storage = new FileBasedTaskStorage(tmpdir);
		HistoryManager historyManager = new FileBasedHistoryManager(tmpdir);

		// init
		UniversalTaskManager manager = new UniversalTaskManager(storage, historyManager);

		// Создание
		Task task1 = new Task("Task #1", "Task1 description", NEW);
		Task task2 = new Task("Task #2", "Task2 description", IN_PROGRESS);
		final int taskId1 = manager.addNewTask(task1);
		final int taskId2 = manager.addNewTask(task2);

		Epic epic1 = new Epic("Epic #1", "Epic1 description", NEW);
		Epic epic2 = new Epic("Epic #2", "Epic2 description", NEW);
		final int epicId1 = manager.addNewEpic(epic1);
		final int epicId2 = manager.addNewEpic(epic2);

		Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", NEW, epicId1);
		Subtask subtask2 = new Subtask("Subtask #2-1", "Subtask1 description", NEW, epicId1);
		Subtask subtask3 = new Subtask("Subtask #3-2", "Subtask1 description", DONE, epicId2);
		final Integer subtaskId1 = manager.addNewSubtask(subtask1);
		final Integer subtaskId2 = manager.addNewSubtask(subtask2);
		final Integer subtaskId3 = manager.addNewSubtask(subtask3);

		printAllTasks(manager);

		// Обновление
		final Task task = manager.getTask(taskId2);
		task.setStatus(DONE);
		manager.updateTask(task);
		System.out.println("CHANGE STATUS: Task2 IN_PROGRESS->DONE");
		System.out.println("Задачи:");
		for (Task t : manager.getTasks()) {
			System.out.println(t);
		}

		Subtask subtask = manager.getSubtask(subtaskId2);
		subtask.setStatus(DONE);
		manager.updateSubtask(subtask);
		System.out.println("CHANGE STATUS: Subtask2 NEW->DONE");
		subtask = manager.getSubtask(subtaskId3);
		subtask.setStatus(NEW);
		manager.updateSubtask(subtask);
		System.out.println("CHANGE STATUS: Subtask3 DONE->NEW");
		System.out.println("Подзадачи:");
		for (Task t : manager.getSubtasks()) {
			System.out.println(t);
		}

		System.out.println("Эпики:");
		for (Task e : manager.getEpics()) {
			System.out.println(e);
			for (Task t : manager.getEpicSubtasks(e.getId())) {
				System.out.println("--> " + t);
			}
		}
		final Epic epic = manager.getEpic(epicId1);
		epic.setStatus(NEW);
		manager.updateEpic(epic);
		System.out.println("CHANGE STATUS: Epic1 IN_PROGRESS->NEW");
		System.out.println("Эпики:");
		for (Task e : manager.getEpics()) {
			System.out.println(e);
			for (Task t : manager.getEpicSubtasks(e.getId())) {
				System.out.println("--> " + t);
			}
		}

		printAllTasks(manager);
		// История без дублирования
		System.out.println("GET Epic2 twice");
		manager.getEpic(epicId2);
		manager.getEpic(epicId2);
		System.out.println("История:");
		for (Task t : manager.getHistory()) {
			System.out.println(t);
		}
		System.out.println("GET Subtasks random");
		manager.getSubtask(subtaskId3);
		manager.getSubtask(subtaskId2);
		manager.getSubtask(subtaskId1);
		manager.getSubtask(subtaskId1);
		manager.getSubtask(subtaskId2);
		manager.getSubtask(subtaskId3);
		for (Task t : manager.getHistory()) {
			System.out.println(t);
		}

		// Удаление
		System.out.println("DELETE: Task1");
		manager.deleteTask(taskId1);
		System.out.println("DELETE: Epic1");
		manager.deleteEpic(epicId1);
		printAllTasks(manager);
	}

	private static void printAllTasks(TaskManager manager) {
		System.out.println("Задачи:");
		for (Task task : manager.getTasks()) {
			System.out.println(task);
		}
		System.out.println("Эпики:");
		for (Task epic : manager.getEpics()) {
			System.out.println(epic);
//			System.out.println("--> Подзадачи эпика:");
			for (Task task : manager.getEpicSubtasks(epic.getId())) {
				System.out.println("--> " + task);
			}
		}
		System.out.println("Подзадачи:");
		for (Task subtask : manager.getSubtasks()) {
			System.out.println(subtask);
		}

		System.out.println("История:");
		for (Task task : manager.getHistory()) {
			System.out.println(task);
		}
	}
}
