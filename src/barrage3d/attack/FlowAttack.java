package barrage3d.attack;

import barrage3d.movings.Enemy;
import barrage3d.movings.NormalBullet;
import barrage3d.movings.Player;
import barrage3d.texture.TextureIndex;

import java.util.List;
import java.util.Set;

import static java.lang.Math.*;

public final class FlowAttack extends PeriodicAttack {
    private float angle = 0;

    public FlowAttack(Player player, Enemy enemy) {
        super(player, enemy);
        enemy.setZ(-1);

        Set<BulletFunctionRecord> bulletFunctions = Set.of(
                BulletFunctionRecord.of(5, arg -> {
                    FlowAttack.this.angle += PI / 20.3;
                    for (float r = 0; r <= 2; r += 1F / 8) {
                        NormalBullet bullet = NormalBullet.create(r * (float) cos(angle), r * (float) sin(angle), -1,
                                0, 0, 0.01F, 0.1F);

                        bullet.setBulletImage(r == 0 ? TextureIndex.YELLOW_BULLET_S : TextureIndex.BLUE_BULLET_S);
                        registerBullet(bullet);
                    }
                })
        );
        register(bulletFunctions);
    }
}
