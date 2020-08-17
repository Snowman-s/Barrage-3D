package barrage3d.movings;

import barrage3d.texture.TextureIndex;

/**
 * 弾の情報(位置)を保持するクラス。速度、形状(つまり当たり判定)の実装は下位クラスに任される。
 */
public abstract class Bullet extends LocateObject {
    private TextureIndex bulletImage = TextureIndex.RED_BULLET_S;

    public Bullet() {
    }

    public Bullet(float x, float y, float z) {
        super(x, y, z);
    }

    public void setBulletImage(TextureIndex bulletImage) {
        this.bulletImage = bulletImage;
    }

    public TextureIndex getBulletImage() {
        return bulletImage;
    }

    public abstract void move();

    public abstract float imageWidth();

    public abstract float imageHeight();
}
