package ru.yandex.javacourse.schedule.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
	protected ArrayList<Integer> subtaskIds = new ArrayList<>();

	public Epic(int id, String name, String description, TaskStatus status) {
		super(id, name, description, status, TaskType.EPIC);
	}

	public Epic(String name, String description, TaskStatus status) {
		super(name, description, status);
	}

	public void addSubtaskId(int id) {
		subtaskIds.add(id);
	}

	public List<Integer> getSubtaskIds() {
		return subtaskIds;
	}

	public void removeSubtask(int id) {
		subtaskIds.remove(Integer.valueOf(id));
	}

	@Override
	public TaskType getType() {
		return TaskType.EPIC;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status=" + getStatus() +
                ", description='" + getDescription() + '\'' +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
