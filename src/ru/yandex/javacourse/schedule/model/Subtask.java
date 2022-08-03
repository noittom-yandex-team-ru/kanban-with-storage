package ru.yandex.javacourse.schedule.model;

public class Subtask extends Task {
	protected int epicId;

	public Subtask(int id, String name, String description, TaskStatus status, int epicId) {
		super(id, name, description, status, TaskType.SUBTASK);
		this.epicId = epicId;
	}

	public Subtask(String name, String description, TaskStatus status, int epicId) {
		super(name, description, status);
		this.epicId = epicId;
	}

	@Override
	public TaskType getType() {
		return TaskType.SUBTASK;
	}

	@Override
	public Integer getEpicId() {
		return epicId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Subtask)) return false;
		if (!super.equals(o)) return false;
		Subtask subtask = (Subtask) o;
		return epicId == subtask.epicId;
	}

	@Override
	public String toString() {
		return "Subtask{" +
				"id=" + getId() +
				", epicId=" + epicId +
				", name='" + getName() + '\'' +
				", status=" + getStatus() +
				", description='" + getDescription() + '\'' +
				'}';
	}
}
