package barrage3d;

import barrage3d.attack.Attack;
import barrage3d.attack.TestAttack;
import barrage3d.display.GLDisplay;
import barrage3d.glrenderer.GLRenderer;
import barrage3d.keyboard.VirtualKey;
import barrage3d.keyboard.VirtualKeyReceiver;
import barrage3d.movings.*;
import barrage3d.taskcallable.TaskCallable;
import barrage3d.utility.ColorUtility;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jogamp.newt.event.KeyEvent.*;
import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

public class Main {
    private static GLDisplay glDisplay;
    private static VirtualKeyReceiver keyReceiver;
    private static List<TaskCallable> taskCallableList;
    private static List<GLRenderer> glRendererList;

    //攻撃
    private static Attack attack;
    private static Set<Bullet> bulletSet;
    private static Player player;

    private static final float[] positionLight = new float[]{0, 0, 0, 1},
            ambientLight = new float[]{0.5F, 0.5F, 0.5F, 1F};

    public static void main(String[] args) {
        taskCallableList = new CopyOnWriteArrayList<>();
        glRendererList = new CopyOnWriteArrayList<>();
        bulletSet = new HashSet<>();

        glDisplay = GLDisplay.getInstance(Main::task, Main::render);

        player = new NormalPlayer(0, 0, 1);

        EnumMap<VirtualKey, Short> keyAllocation = new EnumMap<>(VirtualKey.class);
        keyAllocation.put(VirtualKey.Escape, VK_ESCAPE);
        keyAllocation.put(VirtualKey.Up, VK_UP);
        keyAllocation.put(VirtualKey.Down, VK_DOWN);
        keyAllocation.put(VirtualKey.Left, VK_LEFT);
        keyAllocation.put(VirtualKey.Right, VK_RIGHT);

        keyReceiver = new VirtualKeyReceiver(keyAllocation);
        keyReceiver.consumeKeyReceiver(glDisplay::bindKeyReceiver);

        attack = new TestAttack(player, new Enemy());

        taskCallableList.add(new PlayerMover(player, keyReceiver));

        glRendererList.add(new PlayerRenderer(player));
        glRendererList.add((glDisplay1, glAutoDrawable) -> {
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
        });
    }

    public static void task(TaskCallable.TaskCallArgument arg) {
        keyReceiver.increaseKeyPressedFrame();
        taskCallableList.forEach(t -> t.task(arg));

        //攻撃
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
    }

    public static void render(GLDisplay display, GLAutoDrawable autoDrawable) {
        GL2 gl = autoDrawable.getGL().getGL2();

        gl.glLoadIdentity();
        display.getGLU().gluPerspective(30.0, display.getWidthByHeight(), 3.5, 100.0);
        display.getGLU().gluLookAt(player.getX(), player.getY(), 5, 0, 0, 0, 0, 1, 0);

        gl.glLightfv(GL_LIGHT0, GL_POSITION, positionLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);
        glRendererList.forEach(r -> r.render(display, autoDrawable));

        display.getGLUT().glutWireCube(2);
    }
}
