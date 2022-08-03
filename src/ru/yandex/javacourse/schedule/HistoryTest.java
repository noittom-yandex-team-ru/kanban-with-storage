package ru.yandex.javacourse.schedule;

import ru.yandex.javacourse.schedule.history.FileBasedHistoryManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class HistoryTest {
	public static void main(String[] args) throws IOException {
		File tmpdir = Files.createTempDirectory("tmp").toFile();
		tmpdir = new File("./tmp");
		Arrays.stream(tmpdir.listFiles()).forEach(File::delete);
		System.out.println("Storing in " + tmpdir.getAbsolutePath());
		FileBasedHistoryManager historyManager = new FileBasedHistoryManager(tmpdir);
		historyManager.cleanup();

		assert(historyManager.getHistory().size() == 0);
		historyManager.add(0);
		assert(historyManager.getHistory().size() == 1);
		assert(historyManager.getHistory().get(0) == 0);
		historyManager.add(1);
		assert(historyManager.getHistory().size() == 2);
		assert(historyManager.getHistory().get(0) == 0);
		assert(historyManager.getHistory().get(1) == 1);
		historyManager.add(2);
		assert(historyManager.getHistory().size() == 3);
		historyManager.add(1);
		assert(historyManager.getHistory().size() == 3);
		assert(historyManager.getHistory().get(0) == 0);
		assert(historyManager.getHistory().get(1) == 2);
		assert(historyManager.getHistory().get(2) == 1);

		System.out.println("all history pass");
	}
}
