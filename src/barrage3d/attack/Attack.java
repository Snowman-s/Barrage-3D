package barrage3d.attack;

import barrage3d.display.GLDisplay;
import barrage3d.glrenderer.GLRenderer;
import barrage3d.movings.Bullet;
import barrage3d.movings.Enemy;
import barrage3d.movings.Player;
import barrage3d.taskcallable.TaskCallable;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 弾幕シーン時の、弾と敵と演出についてコントロールするクラス。
 */
public abstract class Attack implements TaskCallable, GLRenderer {
    private final Player player;
    private final Enemy enemy;
    /**
     * 新しい弾を保持するセット
     */
    private final Set<Bullet> newBullets = new HashSet<>();

    public Attack(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    /**
     * 前回、これを呼ばれた以降に、新しく作成された弾を返します。
     *
     * @return 新しく作成された弾
     */
    public Set<Bullet> getNewBullets() {
        Set<Bullet> tmp = Set.copyOf(newBullets);
        newBullets.clear();
        return tmp;
    }

    /**
     * 与えられた弾のコレクションを、新しい弾として登録します。
     *
     * @param bullets 登録するコレクション
     */
    protected void registerBullets(Collection<Bullet> bullets) {
        newBullets.addAll(bullets);
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public final void task(TaskCallArgument arg) {
        taskAttack(arg);
    }

    @Override
    public void render(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {

    }

    /**
     * ここで攻撃の処理をしてください。
     */
    protected abstract void taskAttack(TaskCallArgument arg);
}
