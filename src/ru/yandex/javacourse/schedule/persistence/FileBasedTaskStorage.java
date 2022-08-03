package ru.yandex.javacourse.schedule.persistence;

import ru.yandex.javacourse.schedule.model.Task;
import ru.yandex.javacourse.schedule.model.TaskType;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileBasedTaskStorage implements TaskStorage {
    private final File baseDir;

    public FileBasedTaskStorage(File baseDir) {
        this.baseDir = baseDir;
        baseDir.mkdirs();
        Arrays.stream(TaskType.values()).forEach(type -> {
            File f = new File(baseDir.getAbsoluteFile() + "/" + type);
            boolean mkdir = f.mkdir();
            System.out.println("baseDir created: " + f.getAbsolutePath() + ", result: " + mkdir + ", baseDir exists: " + f.exists());

        });

    }

    @Override
    public <T extends Task> void add(T task) {
        String path = getPath(task.getType(), task.getId());
        write(path, task);
    }

    @Override
    public <T extends Task> T get(Integer id) {
        return Arrays.stream(TaskType.values())
                .map(type -> new File(getPath(type, id)))
                .filter(File::exists)
                .findFirst()
                .map(this::read)
                .filter(Objects::nonNull)
                .map(f->(T)f)
                .orElse(null);

    }

    @Override
    public <T extends Task> List<T> getAll(TaskType taskType) {
        String path = getPath(taskType);
        File typeDir = new File(path);
        return Arrays.stream(typeDir.listFiles())
                .map(this::read).map(t->(T)t)
                .collect(Collectors.toList());
    }

    @Override
    public <T extends Task> T remove(Integer id) {
        File file = Arrays.stream(TaskType.values())
                .map(type -> new File(getPath(type, id)))
                .filter(File::exists).findFirst().orElse(null);
        if (file == null)
            return null;
        return read(file);
    }

    @Override
    public void removeAll(TaskType taskType) {
        Arrays.stream(new File(getPath(taskType))
                        .listFiles())
                .forEach(File::delete);

    }

    //INTERNALS

    private String getPath(TaskType type) {
        return baseDir.getAbsoluteFile() + "/" + type.name();
    }

    private String getPath(TaskType type, Integer id) {
        return getPath(type) + "/" + id + ".bin";
    }

    private void write(String path, Task task) {
        try {
            File f = new File(path);
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(task);
            oos.close();
        } catch (IOException e) {
            System.err.println("can not create file for task, path: " + path);
            e.printStackTrace();
        }
    }

    private <T extends Task> T read(File path) {
        try {
            FileInputStream fileIn = new FileInputStream(path);

            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();
            objectIn.close();
            return (T) obj;
        } catch (Exception e) {
            System.err.println("Can not read from: " + path.getAbsolutePath());
            e.printStackTrace();
        }
        return null;
    }
}
