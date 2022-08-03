package ru.yandex.javacourse.schedule.persistence;


import ru.yandex.javacourse.schedule.model.Task;
import ru.yandex.javacourse.schedule.model.TaskType;

import java.util.List;

public interface TaskStorage {
    <T extends Task> void add(T task);

    <T extends Task> T get(Integer id);
    <T extends Task> List<T> getAll(TaskType taskType);

    <T extends Task> T remove(Integer id);
    void removeAll(TaskType taskType);
}
