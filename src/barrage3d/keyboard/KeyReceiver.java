package barrage3d.keyboard;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class KeyReceiver implements KeyListener {
    protected Map<Short, Integer> pressedFrame = new HashMap<>();

    public KeyReceiver(short... keys) {
        for (short key : keys) {
            this.addReceiveKey(key);
        }
    }

    public void addReceiveKey(short key) {
        if (!pressedFrame.containsKey(key)) {
            pressedFrame.put(key, -1);
        }
    }

    public int pressedFrame(short key) {
        if (!pressedFrame.containsKey(key)) {
            throw new IllegalArgumentException("登録されていないKeyを呼び出しました。");
        }
        return pressedFrame.get(key);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.isAutoRepeat()) {
            return;
        }
        short keycode = keyEvent.getKeyCode();
        if (pressedFrame.containsKey(keycode)) {
            pressedFrame.replace(keycode,
                    pressedFrame.get(keycode) + 1);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.isAutoRepeat()) {
            return;
        }
        if (pressedFrame.containsKey(keyEvent.getKeyCode())) {
            pressedFrame.put(keyEvent.getKeyCode(), -1);
        }
    }

    public void resetKey(short key) {
        pressedFrame.put(key, -1);
    }

    private static final BiFunction<Short, Integer, Integer> replaceFunction = (k, v) -> v >= 0 ? v + 1 : -1;

    public void increaseKeyPressedFrame() {
        this.pressedFrame.replaceAll(replaceFunction);
    }
}
