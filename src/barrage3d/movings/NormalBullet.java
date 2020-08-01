package barrage3d.movings;

import barrage3d.movings.collision.HitCircleObject;
import barrage3d.utility.ColorUtility;

/**
 * ただ真っすぐ進む弾を定義するクラス。通常はこれを使用します。
 */
public class NormalBullet extends Bullet implements HitCircleObject {
    protected NormalBullet() {
        this(0, 0, 0, 0, 0, 0, 0);
    }

    protected NormalBullet(float x, float y, float z, float speedX, float speedY, float speedZ, float collisionRadius) {
        super(x, y, z);

        this.speedX = speedX;
        this.speedY = speedY;
        this.speedZ = speedZ;
        this.collisionRadius = collisionRadius;
    }

    float speedX, speedY, speedZ;
    float collisionRadius;

    @Deprecated
    public float[] color = ColorUtility.getColor(1, 0, 0, 1);

    @Override
    public void move() {
        addX(speedX);
        addY(speedY);
        addZ(speedZ);
    }

    public static NormalBullet create(float x, float y, float z, float speedX, float speedY, float speedZ, float collisionRadius) {
        return new NormalBullet(x, y, z, speedX, speedY, speedZ, collisionRadius);
    }

    @Override
    public float getCollisionRadius() {
        return collisionRadius;
    }
}
