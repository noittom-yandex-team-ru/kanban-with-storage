package ru.yandex.javacourse.schedule.history;

import ru.yandex.javacourse.schedule.persistence.ReadWriteUtil;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileBasedHistoryManager implements HistoryManager {

    private final File baseDir;
    private final File historyDir;

    public FileBasedHistoryManager(File baseDir) {
        this.baseDir = baseDir;
        baseDir.mkdirs();
        historyDir = new File(baseDir.getAbsoluteFile() + "/" + "HISTORY");
        boolean mkdir = historyDir.mkdir();
        System.out.println("baseDir created: " + historyDir.getAbsolutePath() + ", result: " + mkdir + ", baseDir exists: " + historyDir.exists());

    }

    @Override
    public List<Integer> getHistory() {
        return Arrays.stream(historyDir.listFiles())
                .map(ReadWriteUtil::read)
                .filter(Objects::nonNull)
                .map(h -> (HistoryNode) h)
                .sorted(Comparator.comparing(h -> h.time))
                .map(h -> h.id)
                .collect(Collectors.toList());

    }

    @Override
    public void add(int taskId) {
        HistoryNode h = new HistoryNode();
        h.id = taskId;
        h.time = System.currentTimeMillis();
        ReadWriteUtil.write(historyDir.getAbsolutePath() + "/" + taskId, h);
    }

    @Override
    public void remove(int id) {
        File file = new File(historyDir + "/" + id);
        if (file.exists())
            file.delete();
    }

    private static class HistoryNode implements Serializable {
        int id;
        long time;
    }

    public void cleanup(){
        Arrays.stream(historyDir.listFiles()).forEach(File::delete);
    }

}
