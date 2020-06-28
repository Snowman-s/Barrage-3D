package barrage3d;

import barrage3d.display.GLDisplay;
import barrage3d.glrenderer.AbstractGLRenderer;
import barrage3d.glrenderer.GLRenderer;
import barrage3d.taskcallable.TaskCallable;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

public class Main {
    private static GLDisplay glDisplay;
    private static List<TaskCallable> taskCallableList;
    private static List<GLRenderer> glRendererList;

    private static final float[] positionLight = new float[]{0, 0, 0, 1},
            ambientLight = new float[]{0.5F, 0.5F, 0.5F, 1F};

    public static void main(String[] args) {
        taskCallableList = new CopyOnWriteArrayList<>();
        glRendererList = new CopyOnWriteArrayList<>();

        glDisplay = GLDisplay.getInstance(Main::task, Main::render);

        glRendererList.add(new AbstractGLRenderer() {
            @Override
            protected void renderBody(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
                GL2 gl2 = glAutoDrawable.getGL().getGL2();
                gl2.glRotated(10, 0, 0, 1);
                gl2.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, new float[]{1, 1, 1, 1}, 0);
                glDisplay.getGLUT().glutSolidCube(0.1F);
            }
        });
    }

    public static void task(TaskCallable.TaskCallArgument arg) {
        taskCallableList.forEach(t -> t.task(arg));
    }

    public static void render(GLDisplay display, GLAutoDrawable autoDrawable) {
        GL2 gl = autoDrawable.getGL().getGL2();
        gl.glLightfv(GL_LIGHT0, GL_POSITION, positionLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);
        glRendererList.forEach(r -> r.render(display, autoDrawable));
    }
}
