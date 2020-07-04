package barrage3d;

import barrage3d.display.GLDisplay;
import barrage3d.glrenderer.GLRenderer;
import barrage3d.keyboard.VirtualKey;
import barrage3d.keyboard.VirtualKeyReceiver;
import barrage3d.movings.*;
import barrage3d.taskcallable.TaskCallable;
import barrage3d.utility.ColorUtility;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jogamp.newt.event.KeyEvent.*;
import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

public class Main {
    private static GLDisplay glDisplay;
    private static VirtualKeyReceiver keyReceiver;
    private static List<TaskCallable> taskCallableList;
    private static List<GLRenderer> glRendererList;

    private static final float[] positionLight = new float[]{0, 0, 0, 1},
            ambientLight = new float[]{0.5F, 0.5F, 0.5F, 1F};

    public static void main(String[] args) {
        taskCallableList = new CopyOnWriteArrayList<>();
        glRendererList = new CopyOnWriteArrayList<>();

        glDisplay = GLDisplay.getInstance(Main::task, Main::render);

        Player player = new NormalPlayer(0, 0, 1);

        EnumMap<VirtualKey, Short> keyAllocation = new EnumMap<>(VirtualKey.class);
        keyAllocation.put(VirtualKey.Escape, VK_ESCAPE);
        keyAllocation.put(VirtualKey.Up, VK_UP);
        keyAllocation.put(VirtualKey.Down, VK_DOWN);
        keyAllocation.put(VirtualKey.Left, VK_LEFT);
        keyAllocation.put(VirtualKey.Right, VK_RIGHT);

        keyReceiver = new VirtualKeyReceiver(keyAllocation);
        keyReceiver.consumeKeyReceiver(glDisplay::bindKeyReceiver);

        List<Bullet> bullet = new ArrayList<>();

        for (float angle = 0; angle < Math.PI * 2; angle += Math.PI / 10) {
            for (float angle2 = 0; angle2 < Math.PI * 2; angle2 += Math.PI / 10) {
                float[] speed = new float[]{
                        0.005F * (float) (Math.cos(angle) * Math.cos(angle2)),
                        0.005F * (float) (Math.sin(angle2)),
                        0.005F * (float) (-Math.sin(angle) * Math.cos(angle2))};
                bullet.add(NormalBullet.create(0, 0, 0, speed));
            }
        }

        taskCallableList.add(new PlayerMover(player, keyReceiver));
        taskCallableList.add((arg) -> bullet.forEach(Bullet::move));

        glRendererList.add(new PlayerRenderer(player));
        glRendererList.add((glDisplay1, glAutoDrawable) -> {
            GL2 gl2 = glAutoDrawable.getGL().getGL2();
            bullet.forEach(b -> {
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
    }

    public static void render(GLDisplay display, GLAutoDrawable autoDrawable) {
        GL2 gl = autoDrawable.getGL().getGL2();
        gl.glLightfv(GL_LIGHT0, GL_POSITION, positionLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);
        glRendererList.forEach(r -> r.render(display, autoDrawable));
    }
}
