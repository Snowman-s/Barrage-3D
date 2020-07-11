package barrage3d.attack;

import barrage3d.movings.Enemy;
import barrage3d.movings.NormalBullet;
import barrage3d.movings.Player;

import static java.lang.Math.*;

public final class TestAttack extends Attack {
    private int taskEndCount = 0;

    public TestAttack(Player player, Enemy enemy) {
        super(player, enemy);
        enemy.setZ(-1);
    }

    @Override
    public void taskAttack(TaskCallArgument arg) {
        if (taskEndCount % 200 == 0) {
            Enemy enemy = getEnemy();

            for (float angle = 0; angle < PI * 2; angle += PI / 10) {
                for (float angle2 = 0; angle2 < PI * 2; angle2 += PI / 10) {
                    float[] speed = new float[]{
                            0.005F * (float) (cos(angle) * cos(angle2)),
                            0.005F * (float) (sin(angle2)),
                            0.005F * (float) (-sin(angle) * cos(angle2))};
                    registerBullet(NormalBullet.create(enemy.getX(), enemy.getY(), enemy.getZ(), speed, 0.05F));
                }
            }
        }

        taskEndCount++;
    }
}
