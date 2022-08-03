package ru.yandex.javacourse.schedule.persistence;

import ru.yandex.javacourse.schedule.model.Task;
import ru.yandex.javacourse.schedule.model.TaskType;

import java.util.*;

public class InMemoryTaskStorage implements TaskStorage {
    private final Map<TaskType, Map<Integer, Task>> taskMap = new HashMap<>();

    public InMemoryTaskStorage() {
        Arrays.stream(TaskType.values()).forEach(type ->
                taskMap.put(type, new HashMap<>()));
    }

    @Override
    public <T extends Task> void add(T task) {
        taskMap.get(task.getType())
                .put(task.getId(), task);
    }

    @Override
    public <T extends Task> T get(Integer id) {
        return Arrays.stream(TaskType.values())
                .map(taskMap::get)
                .map(map -> map.get(id))
                .filter(Objects::nonNull)
                .findFirst()
                .map(t->(T)t)
                .orElse(null);
    }

    @Override
    public <T extends Task> List<T> getAll(TaskType taskType) {
        return new ArrayList<T>((Collection<? extends T>) taskMap.get(taskType).values());
    }

    @Override
    public <T extends Task> T  remove(Integer id) {
        return Arrays.stream(TaskType.values())
                .map(taskMap::get)
                .map(map -> map.remove(id))
                .filter(Objects::nonNull)
                .findFirst()
                .map(t->(T)t)
                .orElse(null);
    }

    @Override
    public void removeAll(TaskType taskType) {
        taskMap.get(taskType).clear();
    }
}
