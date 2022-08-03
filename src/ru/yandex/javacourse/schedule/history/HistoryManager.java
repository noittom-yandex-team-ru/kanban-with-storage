package ru.yandex.javacourse.schedule.history;

import java.util.List;


public interface HistoryManager {
	List<Integer> getHistory();

	void add(int taskId);

	void remove(int id);
}
