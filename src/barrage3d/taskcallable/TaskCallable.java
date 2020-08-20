package barrage3d.taskcallable;

import java.util.Random;

@FunctionalInterface
public interface TaskCallable {
    void task(TaskCallArgument arg);

    class TaskCallArgument {
        public TaskCallArgument(Random random) {
            this.random = random;
        }

        private final Random random;

        public Random getRandom() {
            return random;
        }
    }
}
