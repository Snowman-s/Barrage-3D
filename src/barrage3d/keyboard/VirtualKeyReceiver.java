package barrage3d.keyboard;

import java.util.EnumMap;
import java.util.function.Consumer;

public class VirtualKeyReceiver {
    private final KeyReceiver keyReceiver;
    private final EnumMap<VirtualKey, Short> keyData;

    public VirtualKeyReceiver(EnumMap<VirtualKey, Short> keyData) {
        keyReceiver = new KeyReceiver();
        this.keyData = keyData;

        keyData.forEach((virtualKey, key) -> keyReceiver.addReceiveKey(key));
    }

    public boolean isPressed(VirtualKey virtualKey, int afford) {
        int frame = keyReceiver.pressedFrame(keyData.get(virtualKey));
        return frame > 0 && frame % afford == 0;
    }

    public void reset(VirtualKey virtualKey) {
        keyReceiver.resetKey(keyData.get(virtualKey));
    }

    public void consumeKeyReceiver(Consumer<? super KeyReceiver> keyReceiverConsumer) {
        keyReceiverConsumer.accept(keyReceiver);
    }

    public void increaseKeyPressedFrame() {
        keyReceiver.increaseKeyPressedFrame();
    }

    public enum VirtualKey {
        Up, Down, Left, Right, Escape;
    }
}
