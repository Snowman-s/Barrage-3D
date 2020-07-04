package barrage3d.movings;

/**
 * ただ真っすぐ進む弾を定義するクラス。
 */
public class NormalBullet extends Bullet {
    protected NormalBullet() {
        this(0, 0, 0, new float[]{0, 0, 0});
    }

    protected NormalBullet(float x, float y, float z, float[] speed) {
        super(x, y, z);

        this.speed = speed;
    }

    float[] speed;

    @Override
    public void move() {
        addX(speed[0]);
        addY(speed[1]);
        addZ(speed[2]);
    }

    public static NormalBullet create(float x, float y, float z, float[] speed) {
        return new NormalBullet(x, y, z, speed);
    }
}
