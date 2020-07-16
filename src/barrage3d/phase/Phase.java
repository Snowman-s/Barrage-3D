package barrage3d.phase;

import barrage3d.display.GLDisplay;
import barrage3d.glrenderer.GLRenderer;
import barrage3d.keyboard.VirtualKeyReceiver;
import barrage3d.taskcallable.TaskCallable;

/**
 * 現在のフェイズに応じたタスクと描画を提供するクラス。
 */
public abstract class Phase implements TaskCallable, GLRenderer {
    final GLDisplay glDisplay;
    final VirtualKeyReceiver keyReceiver;

    protected Phase(GLDisplay glDisplay, VirtualKeyReceiver virtualKeyReceiver) {
        this.glDisplay = glDisplay;
        this.keyReceiver = virtualKeyReceiver;
    }

    public static Phase createPhase(PhaseType type, GLDisplay glDisplay, VirtualKeyReceiver virtualKeyReceiver) {
        return type.createPhase(glDisplay, virtualKeyReceiver);
    }

    public enum PhaseType {
        Attack {
            @Override
            Phase createPhase(GLDisplay glDisplay, VirtualKeyReceiver virtualKeyReceiver) {
                return new AttackPhase(glDisplay, virtualKeyReceiver);
            }
        };

        abstract Phase createPhase(GLDisplay glDisplay, VirtualKeyReceiver virtualKeyReceiver);
    }
}
