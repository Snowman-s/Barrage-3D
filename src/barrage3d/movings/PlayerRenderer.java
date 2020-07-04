package barrage3d.movings;

import barrage3d.display.GLDisplay;
import barrage3d.glrenderer.AbstractGLRenderer;
import barrage3d.utility.ColorUtility;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;

/**
 * 受け取ったプレイヤーを描画します。
 */
public class PlayerRenderer extends AbstractGLRenderer {
    private final Player player;

    public PlayerRenderer(Player player) {
        this.player = player;
    }

    @Override
    protected void renderBody(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glTranslated(player.getX(), player.getY(), player.getZ());
        gl2.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE,
                ColorUtility.getColor(1, 0, 0, 1), 0);
        glDisplay.getGLUT().glutSolidCube(0.1F);
    }
}
