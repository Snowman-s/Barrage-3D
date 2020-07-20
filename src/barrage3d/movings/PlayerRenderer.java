package barrage3d.movings;

import barrage3d.display.GLDisplay;
import barrage3d.glrenderer.AbstractGLRenderer;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;

/**
 * 受け取ったプレイヤーを描画します。
 */
public class PlayerRenderer extends AbstractGLRenderer {
    private final Player player;
    private float[] playerColor = new float[4];

    public PlayerRenderer(Player player) {
        this.player = player;
    }

    @Override
    protected void renderBody(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glTranslated(player.getX(), player.getY(), player.getZ());
        playerColor[0] = 1;
        playerColor[3] = 1 - (player.getInvincibleFrame() % 10) / 10F;
        gl2.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, playerColor, 0);
        glDisplay.getGLUT().glutSolidCube(0.1F);
    }
}
