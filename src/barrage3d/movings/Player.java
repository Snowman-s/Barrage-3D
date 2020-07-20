package barrage3d.movings;

import barrage3d.movings.collision.HitCircleObject;

/**
 * プレイヤーの情報(位置、速度、形状)を保持するクラス。速さの実装は下位クラスに任される。
 */
public abstract class Player extends LocateObject implements HitCircleObject {
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

    private int invincibleFrame = 0;

    public int getInvincibleFrame() {
        return invincibleFrame;
    }

    public boolean isInvincible() {
        return invincibleFrame > 0;
    }

    public void setInvincibleFrame(int invincibleFrame) {
        this.invincibleFrame = Math.max(invincibleFrame, 0);
    }

    public void decreaseInvincibleFrame() {
        if (invincibleFrame > 0) {
            this.invincibleFrame--;
        }
    }
}
