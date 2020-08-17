package barrage3d.glrenderer;

import barrage3d.display.GLDisplay;
import barrage3d.movings.Bullet;
import barrage3d.movings.LocateObject;
import barrage3d.movings.NormalBullet;
import barrage3d.texture.TextureInfo;
import barrage3d.texture.TextureLoader;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.Set;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static java.lang.Math.*;

public class BulletsRenderer extends AbstractGLRenderer {
    private Set<Bullet> bulletSet;

    public BulletsRenderer(Set<Bullet> bullets) {
        bulletSet = bullets;
    }

    @Override
    protected void renderBody(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glDisable(GL_LIGHTING);
       // gl2.glColor4f(1, 1, 1, 1);
        gl2.glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE);
        bulletSet.stream().sorted(Comparator.comparingDouble(LocateObject::getZ)).forEach(b -> {
                    gl2.glDisable(GL_DEPTH_TEST);
                    gl2.glPushMatrix();

                    gl2.glTranslated(b.getX(), b.getY(), b.getZ());

                    if (b instanceof NormalBullet) {
                        TextureInfo textureInfo = TextureLoader.getTextureInfo(b.getBulletImage());

                        textureInfo.getTexture().bind(gl2);
                        textureInfo.getTexture().enable(gl2);
                        gl2.glColor4f(1, 1, 1, 1);

                        gl2.glBegin(GL2.GL_POLYGON);

                        Rectangle2D.Float tex = textureInfo.getSourceRect();

                        gl2.glTexCoord2d(tex.x, tex.y);
                        gl2.glVertex3d(-b.imageWidth() / 2, -b.imageHeight() / 2, 0);
                        gl2.glTexCoord2d(tex.x + tex.width, tex.y);
                        gl2.glVertex3d(+b.imageWidth() / 2, -b.imageHeight() / 2, 0);
                        gl2.glTexCoord2d(tex.x + tex.width, tex.y + tex.height);
                        gl2.glVertex3d(+b.imageWidth() / 2, +b.imageHeight() / 2, 0);
                        gl2.glTexCoord2d(tex.x, tex.y + tex.height);
                        gl2.glVertex3d(-b.imageWidth() / 2, b.imageHeight() / 2, 0);

                        gl2.glEnd();

                        textureInfo.getTexture().disable(gl2);
                    }
                    gl2.glEnable(GL_DEPTH_TEST);
                    gl2.glColorMask(false, false, false, false);
                    if (b instanceof NormalBullet) {
                        gl2.glBegin(GL2.GL_TRIANGLE_FAN);

                        gl2.glVertex3d(0, 0, 0);
                        for (float rad = 0; rad <= PI * 2.1; rad += PI / 8) {
                            gl2.glVertex3d(b.imageWidth() / 2 * cos(rad), b.imageHeight() / 2 * sin(rad), 0);
                        }
                        gl2.glEnd();
                    }
                    gl2.glColorMask(true, true, true, true);
                    gl2.glPopMatrix();
                }
        );
        gl2.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl2.glEnable(GL_LIGHTING);
    }
}
