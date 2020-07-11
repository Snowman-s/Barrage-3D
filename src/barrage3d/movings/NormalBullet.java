package barrage3d.movings;

import barrage3d.movings.collision.HitCircleObject;

/**
 * ただ真っすぐ進む弾を定義するクラス。通常はこれを使用します。
 */
public class NormalBullet extends Bullet implements HitCircleObject {
    protected NormalBullet() {
        this(0, 0, 0, new float[]{0, 0, 0}, 0);
    }

    protected NormalBullet(float x, float y, float z, float[] speed, float collisionRadius) {
        super(x, y, z);

        this.speed = speed;
        this.collisionRadius = collisionRadius;
    }

    float[] speed;
    float collisionRadius;

    @Override
    public void move() {
        addX(speed[0]);
        addY(speed[1]);
        addZ(speed[2]);
    }

    public static NormalBullet create(float x, float y, float z, float[] speed, float collisionRadius) {
        return new NormalBullet(x, y, z, speed, collisionRadius);
    }

    @Override
    public float getCollisionRadius() {
        return collisionRadius;
    }
}
