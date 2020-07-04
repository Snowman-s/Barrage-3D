package barrage3d.movings;

/**
 * 弾の情報(位置、形状)を保持するクラス。速度の実装は下位クラスに任される。
 */
public abstract class Bullet extends LocateObject {
    public Bullet() {
    }

    public Bullet(float x, float y, float z) {
        super(x, y, z);
    }

    public abstract void move();
}
