package barrage3d.movings;

import barrage3d.movings.collision.HitCircleObject;

public class HitChecker {
    private HitChecker() {
    }

    /**
     * 二つのオブジェクトが接触しているか調査します。
     *
     * <b>現在では、a, b両方ともHitCircleObjectを継承していなければ、検査は行われずfalseが返ります。</b>
     * この仕様は将来的に修正される可能性があります。
     *
     * @param a 一つ目のオブジェクト
     * @param b 二つ目のオブジェクト
     * @return 二つのオブジェクトが接触しているか
     */
    public static boolean hit(LocateObject a, LocateObject b) {
        if (a instanceof HitCircleObject && b instanceof HitCircleObject) {
            float dx = a.getX() - b.getX();
            float dy = a.getY() - b.getY();
            float dz = a.getZ() - b.getZ();
            float distance = ((HitCircleObject) a).getCollisionRadius() +
                    ((HitCircleObject) b).getCollisionRadius();

            return dx * dx + dy * dy + dz * dz <= distance * distance;
        }
        return false;
    }
}
