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

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

public class AttackPhase extends Phase {
    //攻撃
    private Attack attack;
    private Set<Bullet> bulletSet = new HashSet<>();
    private Player player = new NormalPlayer(0, 0, 1);

    private final TaskCallable playerMover;
    private final GLRenderer playerRenderer, bulletsRenderer;

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

        float bounds = 1.2F;
        bulletSet.removeIf(bullet -> bullet.getX() < -bounds || bounds < bullet.getX() ||
                bullet.getY() < -bounds || bounds < bullet.getY() ||
                bullet.getZ() < -bounds || bounds < bullet.getZ());

        if (player.getX() < -1) {
            player.setX(-1);
        } else if (player.getX() > 1) {
            player.setX(1);
        }
        if (player.getY() < -1) {
            player.setY(-1);
        } else if (player.getY() > 1) {
            player.setY(1);
        }
        if (player.getZ() < -1) {
            player.setZ(-1);
        } else if (player.getZ() > 1) {
            player.setZ(1);
        }

        for (Bullet bullet : bulletSet) {
            boolean hit = HitChecker.hit(bullet, player);

            if (hit) {
                System.out.println("hit!");
                break;
            }
        }
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

        glDisplay.getGLUT().glutWireCube(2);
    }
}
