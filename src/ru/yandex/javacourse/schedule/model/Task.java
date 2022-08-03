package ru.yandex.javacourse.schedule.model;

import java.io.Serializable;
import java.util.Objects;

public class Task implements Serializable {
	private int id;
	private String name;
	private TaskStatus status;
	private String description;
	private final TaskType taskType;

	public Task(int id, String name, String description, TaskStatus status) {
	 this(id, name, description, status, TaskType.TASK);
	}
	public Task(String name, String description, TaskStatus status) {
		this(0, name, description, status, TaskType.TASK);
	}
	Task(int id, String name, String description, TaskStatus status, TaskType taskType) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
		this.taskType = taskType;
	}


	public TaskType getType() {
		return taskType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getEpicId() {
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Task task = (Task) o;
		return id == task.id && Objects.equals(name, task.name) && Objects.equals(status, task.status) && Objects.equals(description, task.description);
	}

	@Override
	public String toString() {
		return "Task{" +
				"id=" + id +
				", name='" + name + '\'' +
				", status='" + status + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
