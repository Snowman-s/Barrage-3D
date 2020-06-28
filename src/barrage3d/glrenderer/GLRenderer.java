package barrage3d.glrenderer;

import com.jogamp.opengl.GLAutoDrawable;
import barrage3d.display.GLDisplay;

@FunctionalInterface
public interface GLRenderer {
    void render(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable);
}
