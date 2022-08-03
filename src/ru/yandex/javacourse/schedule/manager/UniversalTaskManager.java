package ru.yandex.javacourse.schedule.manager;

import static ru.yandex.javacourse.schedule.model.TaskStatus.NEW;

import java.util.*;
import java.util.stream.Collectors;

import ru.yandex.javacourse.schedule.history.HistoryManager;
import ru.yandex.javacourse.schedule.model.*;
import ru.yandex.javacourse.schedule.persistence.TaskStorage;
import ru.yandex.javacourse.schedule.tasks.*;

public class UniversalTaskManager implements TaskManager {
	private int generatorId = 0;
	private final TaskStorage storage;
	private final HistoryManager historyManager;

	public UniversalTaskManager(TaskStorage storage, HistoryManager historyManager) {
		this.storage = storage;
		this.historyManager = historyManager;
	}


	@Override
	public List<Task> getTasks() {
		return storage.getAll(TaskType.TASK);
	}

	@Override
	public List<Subtask> getSubtasks() {
		return storage.getAll(TaskType.SUBTASK);
	}

	@Override
	public List<Epic> getEpics() {
		return storage.getAll(TaskType.EPIC);
	}

	@Override
	public List<Subtask> getEpicSubtasks(int epicId) {
		Epic epic = storage.get(epicId);
		if (epic == null) {
			return null;
		}
		List<Subtask> tasks = new ArrayList<>();
		for (int id : epic.getSubtaskIds()) {
			tasks.add(storage.get(id));
		}
		return tasks;
	}

	@Override
	public Task getTask(int id) {
		final Task task = storage.get(id);
		historyManager.add(task.getId());
		return task;
	}

	@Override
	public Subtask getSubtask(int id) {
		final Subtask subtask = storage.get(id);
		historyManager.add(subtask.getId());
		return subtask;
	}

	@Override
	public Epic getEpic(int id) {
		final Epic epic = storage.get(id);
		historyManager.add(epic.getId());
		return epic;
	}

	@Override
	public int addNewTask(Task task) {
		final int id = ++generatorId;
		task.setId(id);
		storage.add(task);
		return id;
	}

	@Override
	public int addNewEpic(Epic epic) {
		final int id = ++generatorId;
		epic.setId(id);
		storage.add(epic);
		return id;

	}

	@Override
	public Integer addNewSubtask(Subtask subtask) {
		final int epicId = subtask.getEpicId();
		Epic epic = storage.get(epicId);
		if (epic == null) {
			return null;
		}
		final int id = ++generatorId;
		subtask.setId(id);
		storage.add(subtask);
		epic.addSubtaskId(subtask.getId());
		updateEpicStatus(epicId);
		return id;
	}

	@Override
	public void updateTask(Task task) {
		final int id = task.getId();
		final Task savedTask = storage.get(id);
		if (savedTask == null) {
			return;
		}
		storage.add(task);
	}

	@Override
	public void updateEpic(Epic epic) {
		final Epic savedEpic = storage.get(epic.getId());
		savedEpic.setName(epic.getName());
		savedEpic.setDescription(epic.getDescription());
	}

	@Override
	public void updateSubtask(Subtask subtask) {
		final int id = subtask.getId();
		final int epicId = subtask.getEpicId();
		final Subtask savedSubtask = storage.get(id);
		if (savedSubtask == null) {
			return;
		}
		final Epic epic = storage.get(epicId);
		if (epic == null) {
			return;
		}
		storage.add(subtask);
		updateEpicStatus(epicId);
	}

	@Override
	public void deleteTask(int id) {
		storage.remove(id);
		historyManager.remove(id);
	}

	@Override
	public void deleteEpic(int id) {
		final Epic epic = storage.remove(id);
		if (epic == null) {
			return;
		}
		historyManager.remove(id);
		for (Integer subtaskId : epic.getSubtaskIds()) {
			storage.remove(subtaskId);
			historyManager.remove(subtaskId);
		}
	}

	@Override
	public void deleteSubtask(int id) {
		Subtask subtask = storage.remove(id);
		if (subtask == null) {
			return;
		}
		historyManager.remove(id);
		Epic epic = storage.get(subtask.getEpicId());
		epic.removeSubtask(id);
		updateEpicStatus(epic.getId());
	}

	@Override
	public void deleteTasks() {
		storage.removeAll(TaskType.TASK);
	}

	@Override
	public void deleteSubtasks() {
		storage.removeAll(TaskType.SUBTASK);
	}

	@Override
	public void deleteEpics() {
		storage.removeAll(TaskType.EPIC);
		storage.removeAll(TaskType.SUBTASK);
	}

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistory().stream()
				.map(this::getTask)
				.collect(Collectors.toList());
	}

	private void updateEpicStatus(int epicId) {
		Epic epic = storage.get(epicId);
		List<Integer> subs = epic.getSubtaskIds();
		if (subs.isEmpty()) {
			epic.setStatus(NEW);
			return;
		}
		Set<TaskStatus> statusSet = new HashSet<>();
		for (int id : subs) {
			final Subtask subtask = storage.get(id);
			statusSet.add(subtask.getStatus());
		}

		if (statusSet.size() != 1) {
			epic.setStatus(NEW);
		} else {
			epic.setStatus(statusSet.iterator().next());
		}


	}
}
