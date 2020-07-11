package barrage3d.movings;

import barrage3d.keyboard.KeyInput;
import barrage3d.keyboard.VirtualKey;
import barrage3d.taskcallable.TaskCallable;
import com.jogamp.opengl.math.VectorUtil;

public class PlayerMover implements TaskCallable {
    private final Player player;
    private final KeyInput input;

    public PlayerMover(Player player, KeyInput input) {
        this.player = player;
        this.input = input;
    }

    @Override
    public void task(TaskCallArgument arg) {
        float[] speed = new float[3];

        speed[1] = (input.isPressed(VirtualKey.Up, 1) ? 1 : 0) +
                (input.isPressed(VirtualKey.Down, 1) ? -1 : 0);
        speed[0] = (input.isPressed(VirtualKey.Left, 1) ? -1 : 0) +
                (input.isPressed(VirtualKey.Right, 1) ? 1 : 0);
        speed[2] = (input.isPressed(VirtualKey.Forward, 1) ? -1 : 0) +
                (input.isPressed(VirtualKey.Backward, 1) ? 1 : 0);

        VectorUtil.normalizeVec3(speed);

        player.moveBy(speed);
    }
}
