package barrage3d.phase;

import barrage3d.attack.Attack;
import barrage3d.attack.TestAttack;
import barrage3d.display.GLDisplay;
import barrage3d.glrenderer.GLRenderer;
import barrage3d.keyboard.VirtualKeyReceiver;
import barrage3d.movings.*;
import barrage3d.taskcallable.TaskCallable;
import barrage3d.utility.ColorUtility;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.HashSet;
import java.util.Set;

import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

public class AttackPhase extends Phase {
    //攻撃
    private Attack attack;
    private final Set<Bullet> bulletSet = new HashSet<>();
    private final Player player = new NormalPlayer(0, 0, 1);

    private final TaskCallable playerMover;
    private final GLRenderer playerRenderer, bulletsRenderer;

    private static final float PLAYER_MOVE_BOUND = 1, BULLET_DELETE_BOUND = 1.2F;

    private static final float[] positionLight = new float[]{0, 0, 1, 1},
            ambientLight = new float[]{0.5F, 0.5F, 0.5F, 1F};

    protected AttackPhase(GLDisplay glDisplay, VirtualKeyReceiver virtualKeyReceiver) {
        super(glDisplay, virtualKeyReceiver);
        attack = new TestAttack(player, new Enemy());
        playerMover = new PlayerMover(player, keyReceiver);
        playerRenderer = new PlayerRenderer(player);
        bulletsRenderer = (glDisplay1, glAutoDrawable) -> {
            GL2 gl2 = glAutoDrawable.getGL().getGL2();
            bulletSet.forEach(b -> {
                        gl2.glPushMatrix();
                        gl2.glTranslated(b.getX(), b.getY(), b.getZ());
                        gl2.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE,
                                ColorUtility.getColor(1, 1, 0, 1), 0);
                        glDisplay.getGLUT().glutSolidCube(0.1F);
                        gl2.glPopMatrix();
                    }
            );
        };
    }

    @Override
    public void task(TaskCallArgument arg) {
        //攻撃
        playerMover.task(arg);
        attack.task(arg);

        bulletSet.addAll(attack.getNewBullets());
        bulletSet.forEach(Bullet::move);

        bulletSet.removeIf(bullet ->
                bullet.getX() < -BULLET_DELETE_BOUND || BULLET_DELETE_BOUND < bullet.getX() ||
                        bullet.getY() < -BULLET_DELETE_BOUND || BULLET_DELETE_BOUND < bullet.getY() ||
                        bullet.getZ() < -BULLET_DELETE_BOUND || BULLET_DELETE_BOUND < bullet.getZ());

        if (player.getX() < -PLAYER_MOVE_BOUND) {
            player.setX(-PLAYER_MOVE_BOUND);
        } else if (player.getX() > PLAYER_MOVE_BOUND) {
            player.setX(PLAYER_MOVE_BOUND);
        }
        if (player.getY() < -PLAYER_MOVE_BOUND) {
            player.setY(-PLAYER_MOVE_BOUND);
        } else if (player.getY() > PLAYER_MOVE_BOUND) {
            player.setY(PLAYER_MOVE_BOUND);
        }
        if (player.getZ() < -PLAYER_MOVE_BOUND) {
            player.setZ(-PLAYER_MOVE_BOUND);
        } else if (player.getZ() > PLAYER_MOVE_BOUND) {
            player.setZ(PLAYER_MOVE_BOUND);
        }

        if (!player.isInvincible()) {
            for (Bullet bullet : bulletSet) {
                boolean hit = HitChecker.hit(bullet, player);

                if (hit) {
                    player.setInvincibleFrame(60);
                    player.setXYZ(0, 0, 1);
                    break;
                }
            }
        }
        player.decreaseInvincibleFrame();
    }

    @Override
    public void render(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glLoadIdentity();

        glDisplay.getGLU().gluPerspective(30.0, glDisplay.getWidthByHeight(), 3.5, 100.0);

        glDisplay.getGLU().gluLookAt(player.getX(), player.getY(), player.getZ() + 4, 0, 0, 0, 0, 1, 0);

        gl.glLightfv(GL_LIGHT0, GL_POSITION, positionLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);

        playerRenderer.render(glDisplay, glAutoDrawable);
        bulletsRenderer.render(glDisplay, glAutoDrawable);

        glDisplay.getGLUT().glutWireCube(PLAYER_MOVE_BOUND * 2);

        //弾に当たった時のフラッシュ
        gl.glPushMatrix();

        gl.glLoadIdentity();

        gl.glDisable(GL_DEPTH_TEST);
        gl.glDisable(GL_LIGHTING);

        gl.glColor4f(1, 1, 1, player.getInvincibleFrame() / 60F);
        gl.glBegin(GL_QUADS);

        gl.glVertex3d(-1, -1, 0);
        gl.glVertex3d(1, -1, 0);
        gl.glVertex3d(1, 1, 0);
        gl.glVertex3d(-1, 1, 0);

        gl.glEnd();

        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_DEPTH_TEST);

        gl.glPopMatrix();
    }
}
