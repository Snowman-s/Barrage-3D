package barrage3d;

import barrage3d.display.GLDisplay;
import barrage3d.keyboard.VirtualKey;
import barrage3d.keyboard.VirtualKeyReceiver;
import barrage3d.phase.Phase;
import barrage3d.taskcallable.TaskCallable;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.util.EnumMap;

import static com.jogamp.newt.event.KeyEvent.*;

public class Main {
    private static GLDisplay glDisplay;
    private static VirtualKeyReceiver keyReceiver;

    private static Phase phase;

    public static void main(String[] args) {
        glDisplay = GLDisplay.getInstance(Main::init, Main::task, Main::render);

        EnumMap<VirtualKey, Short> keyAllocation = new EnumMap<>(VirtualKey.class);
        keyAllocation.put(VirtualKey.Escape, VK_ESCAPE);
        keyAllocation.put(VirtualKey.Up, VK_UP);
        keyAllocation.put(VirtualKey.Down, VK_DOWN);
        keyAllocation.put(VirtualKey.Left, VK_LEFT);
        keyAllocation.put(VirtualKey.Right, VK_RIGHT);
        keyAllocation.put(VirtualKey.Forward, VK_A);
        keyAllocation.put(VirtualKey.Backward, VK_Z);

        keyReceiver = new VirtualKeyReceiver(keyAllocation);
        keyReceiver.consumeKeyReceiver(glDisplay::bindKeyReceiver);
    }

    public static void init(GL2 gl) {
        phase = Phase.createPhase(Phase.PhaseType.Attack, glDisplay, keyReceiver);
        phase.loadImage(gl);
    }

    public static void task(TaskCallable.TaskCallArgument arg) {
        keyReceiver.increaseKeyPressedFrame();
        if (keyReceiver.isPressed(VirtualKey.Escape, 1)) {
            glDisplay.endWindow();
        }
        phase.task(arg);
    }

    public static void render(GLDisplay display, GLAutoDrawable autoDrawable) {
        phase.render(display, autoDrawable);
    }
}
