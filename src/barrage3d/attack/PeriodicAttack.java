package barrage3d.attack;

import barrage3d.movings.Enemy;
import barrage3d.movings.Player;
import barrage3d.taskcallable.TaskCallable;

import java.util.HashSet;
import java.util.Set;

public abstract class PeriodicAttack extends Attack {
    private final Set<BulletFunctionRecord> bulletFunctions = new HashSet<>();
    private int taskCallEndTime = 0;

    public PeriodicAttack(Player player, Enemy enemy) {
        super(player, enemy);
    }

    public void register(Set<BulletFunctionRecord> functionRecords) {
        bulletFunctions.addAll(functionRecords);
    }

    @Override
    protected void taskAttack(TaskCallArgument arg) {
        for (BulletFunctionRecord bulletFunction : bulletFunctions) {
            if (taskCallEndTime % bulletFunction.frame == 0 ||
                    (taskCallEndTime % bulletFunction.frame < bulletFunction.time * bulletFunction.breakFrame &&
                            taskCallEndTime % bulletFunction.frame % bulletFunction.breakFrame == 0)) {
                bulletFunction.function.task(arg);
            }
        }
        taskCallEndTime++;
    }

    public static class BulletFunctionRecord {
        private final int frame;
        private final int time;
        private final int breakFrame;
        private final TaskCallable function;

        private BulletFunctionRecord(int frame, int time, int breakFrame, TaskCallable function) {
            this.frame = frame;
            this.time = time;
            this.breakFrame = breakFrame;
            this.function = function;
        }

        public static BulletFunctionRecord of(int frame, int time, int breakFrame, TaskCallable bulletFunction) {
            return new BulletFunctionRecord(frame, time, breakFrame, bulletFunction);
        }

        /**
         * time=1, breakFrame=0
         */
        public static BulletFunctionRecord of(int frame, TaskCallable bulletFunction) {
            return new BulletFunctionRecord(frame, 1, 0, bulletFunction);
        }
    }
}
