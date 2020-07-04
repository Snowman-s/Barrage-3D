package barrage3d.movings;

/**
 * プレイヤーの情報(位置、速度、形状)を保持するクラス。速さの実装は下位クラスに任される。
 */
public abstract class Player extends LocateObject {
    public Player() {
    }

    public Player(float x, float y, float z) {
        super(x, y, z);
    }

    public abstract float speed();

    public void moveBy(float[] normalisedSpeed) {
        float speed = speed();
        this.addX(normalisedSpeed[0] * speed);
        this.addY(normalisedSpeed[1] * speed);
        this.addZ(normalisedSpeed[2] * speed);
    }
}
