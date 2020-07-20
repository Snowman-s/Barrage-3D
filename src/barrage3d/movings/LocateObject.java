package barrage3d.movings;

/**
 * フィールド上に配置される(つまり、座標を持つ)クラス
 */
public abstract class LocateObject {
    public LocateObject() {
    }

    public LocateObject(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private float x, y, z;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void addX(float dx) {
        this.x += dx;
    }

    public void addY(float dy) {
        this.y += dy;
    }

    public void addZ(float dz) {
        this.z += dz;
    }

    public void setXYZ(float x, float y, float z) {
        setX(x);
        setY(y);
        setZ(z);
    }
}
