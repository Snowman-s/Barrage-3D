package barrage3d.attack;

import barrage3d.movings.Bullet;
import barrage3d.movings.Enemy;
import barrage3d.movings.NormalBullet;
import barrage3d.movings.Player;

import java.util.ArrayList;
import java.util.List;

public final class TestAttack extends Attack {
    private int taskEndCount = 0;

    public TestAttack(Player player, Enemy enemy) {
        super(player, enemy);
    }

    @Override
    public void taskAttack(TaskCallArgument arg) {
        if (taskEndCount % 200 == 0) {
            List<Bullet> bullet = new ArrayList<>();

            for (float angle = 0; angle < Math.PI * 2; angle += Math.PI / 10) {
                for (float angle2 = 0; angle2 < Math.PI * 2; angle2 += Math.PI / 10) {
                    float[] speed = new float[]{
                            0.005F * (float) (Math.cos(angle) * Math.cos(angle2)),
                            0.005F * (float) (Math.sin(angle2)),
                            0.005F * (float) (-Math.sin(angle) * Math.cos(angle2))};
                    bullet.add(NormalBullet.create(0, 0, 0, speed));
                }
            }

            registerBullets(bullet);
        }

        taskEndCount++;
    }
}
