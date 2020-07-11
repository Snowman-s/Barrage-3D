package barrage3d.movings;

/**
 * 標準的な速さのプレイヤー
 */
public class NormalPlayer extends Player {
    public NormalPlayer() {
    }

    public NormalPlayer(float x, float y, float z) {
        super(x, y, z);
    }

    @Override
    public float speed() {
        return 0.02F;
    }

    @Override
    public float getCollisionRadius() {
        return 0.05F;
    }
}
