package barrage3d.taskcallable;

@FunctionalInterface
public interface TaskCallable {
    void task(TaskCallArgument arg);

    class TaskCallArgument {
    }
}
