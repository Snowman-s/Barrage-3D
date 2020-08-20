package barrage3d.attack;

import barrage3d.movings.Enemy;
import barrage3d.movings.NormalBullet;
import barrage3d.movings.Player;
import barrage3d.texture.TextureIndex;

import java.util.List;
import java.util.Set;

public final class WallAttack extends PeriodicAttack {
    private static final List<TextureIndex> bulletImages =
            List.of(TextureIndex.RED_BULLET_S, TextureIndex.YELLOW_BULLET_S, TextureIndex.GREEN_BULLET_S,
                    TextureIndex.SKY_BULLET_S, TextureIndex.BLUE_BULLET_S, TextureIndex.PURPLE_BULLET_S);

    public WallAttack(Player player, Enemy enemy) {
        super(player, enemy);
        enemy.setZ(-1);

        Set<BulletFunctionRecord> bulletFunctions = Set.of(
                BulletFunctionRecord.of(150, 3, 20, arg -> {
                    for (float x = -1 + arg.getRandom().nextFloat() * 2F / 8; x <= 1; x += 2F / 8) {
                        for (float y = -1; y <= 1; y += 2F / 30) {
                            NormalBullet bullet = NormalBullet.create(x, y, -1, 0, 0, 0.02F, 0.05F);

                            bullet.setBulletImage(bulletImages.get(arg.getRandom().nextInt(6)));
                            registerBullet(bullet);
                        }
                    }
                })
        );
        register(bulletFunctions);
    }
}
