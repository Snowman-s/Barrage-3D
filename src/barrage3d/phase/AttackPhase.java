package barrage3d.phase;

import barrage3d.attack.Attack;
import barrage3d.attack.FlowAttack;
import barrage3d.attack.TestAttack;
import barrage3d.attack.WallAttack;
import barrage3d.display.GLDisplay;
import barrage3d.glrenderer.BulletsRenderer;
import barrage3d.glrenderer.GLRenderer;
import barrage3d.keyboard.VirtualKeyReceiver;
import barrage3d.movings.*;
import barrage3d.taskcallable.TaskCallable;
import barrage3d.texture.TextureInfo;
import barrage3d.texture.TextureLoader;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;
import static java.lang.Math.*;

public class AttackPhase extends Phase {
    //攻撃
    private List<Attack> attacks;
    private int attackIndex;
    private ToNextAttackTask toNextAttackTask = new ToNextAttackTask();

    private final Set<Bullet> bulletSet = new HashSet<>();
    private final Player player = new NormalPlayer(0, 0, 0);

    private final TaskCallable playerMover;
    private final GLRenderer playerRenderer, bulletsRenderer;

    private final TextRenderer attackRemainSecondRenderer;

    private static final float PLAYER_MOVE_BOUND = 1, BULLET_DELETE_BOUND = 1.2F;

    private static final float[] positionLight = new float[]{0, 0, 1, 1},
            ambientLight = new float[]{0.5F, 0.5F, 0.5F, 1F};

    protected AttackPhase(GLDisplay glDisplay, VirtualKeyReceiver virtualKeyReceiver) {
        super(glDisplay, virtualKeyReceiver);
        Enemy enemy = new Enemy();
        attacks = List.of(new TestAttack(player, enemy), new WallAttack(player, enemy), new FlowAttack(player, enemy));
        playerMover = new PlayerMover(player, keyReceiver);
        playerRenderer = new PlayerRenderer(player);
        bulletsRenderer = new BulletsRenderer(bulletSet);
        attackRemainSecondRenderer = new TextRenderer(Font.decode("Arial-50"), true);
        attackIndex = 0;
    }

    @Override
    public void task(TaskCallArgument arg) {
        Attack attack = attacks.get(attackIndex);
        if (attack.getRemainFrame() == 0) {
            if (!toNextAttackTask.isEnabled()) {
                toNextAttackTask.enable(Set.copyOf(bulletSet));
                bulletSet.clear();
            }
            toNextAttackTask.task(arg);
        } else {
            attack.task(arg);
        }

        //攻撃
        playerMover.task(arg);

        bulletSet.addAll(attack.getNewBullets());
        bulletSet.forEach(Bullet::move);

        bulletSet.removeIf(bullet ->
                bullet.getX() < -BULLET_DELETE_BOUND || BULLET_DELETE_BOUND < bullet.getX() ||
                        bullet.getY() < -BULLET_DELETE_BOUND || BULLET_DELETE_BOUND < bullet.getY() ||
                        bullet.getZ() < -BULLET_DELETE_BOUND || BULLET_DELETE_BOUND < bullet.getZ());

        if (player.getX() < -PLAYER_MOVE_BOUND) {
            player.setX(-PLAYER_MOVE_BOUND);
        } else if (player.getX() > PLAYER_MOVE_BOUND) {
            player.setX(PLAYER_MOVE_BOUND);
        }
        if (player.getY() < -PLAYER_MOVE_BOUND) {
            player.setY(-PLAYER_MOVE_BOUND);
        } else if (player.getY() > PLAYER_MOVE_BOUND) {
            player.setY(PLAYER_MOVE_BOUND);
        }
        if (player.getZ() < -PLAYER_MOVE_BOUND) {
            player.setZ(-PLAYER_MOVE_BOUND);
        } else if (player.getZ() > PLAYER_MOVE_BOUND) {
            player.setZ(PLAYER_MOVE_BOUND);
        }

        if (!player.isInvincible()) {
            for (Bullet bullet : bulletSet) {
                boolean hit = HitChecker.hit(bullet, player);

                if (hit) {
                    player.setInvincibleFrame(60);
                    player.setXYZ(0, 0, 1);
                    break;
                }
            }
        }

        player.decreaseInvincibleFrame();
    }

    @Override
    public void render(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
        Attack attack = attacks.get(attackIndex);
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glLoadIdentity();

        glDisplay.getGLU().gluPerspective(30.0, glDisplay.getWidthByHeight(), 3.5, 100.0);
        glDisplay.getGLU().gluLookAt(player.getX(), player.getY(), player.getZ() + 4, 0, 0, 0, 0, 1, 0);

        gl.glLightfv(GL_LIGHT0, GL_POSITION, positionLight, 0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight, 0);

        toNextAttackTask.render(glDisplay, glAutoDrawable);
        bulletsRenderer.render(glDisplay, glAutoDrawable);
        playerRenderer.render(glDisplay, glAutoDrawable);

        glDisplay.getGLUT().glutWireCube(PLAYER_MOVE_BOUND * 2);

        //弾に当たった時のフラッシュ
        if (player.isInvincible()) {
            gl.glPushMatrix();

            gl.glLoadIdentity();

            gl.glDisable(GL_DEPTH_TEST);
            gl.glDisable(GL_LIGHTING);

            gl.glColor4f(1, 1, 1, player.getInvincibleFrame() / 60F);
            gl.glBegin(GL_QUADS);

            gl.glVertex3d(-1, -1, 0);
            gl.glVertex3d(1, -1, 0);
            gl.glVertex3d(1, 1, 0);
            gl.glVertex3d(-1, 1, 0);

            gl.glEnd();

            gl.glEnable(GL_LIGHTING);
            gl.glEnable(GL_DEPTH_TEST);

            gl.glPopMatrix();
        }

        //残り時間
        attackRemainSecondRenderer.beginRendering(glDisplay.getWidth(), glDisplay.getHeight());
        attackRemainSecondRenderer.draw(Integer.toString(attack.getRemainFrame() / 60), 0, 0);
        attackRemainSecondRenderer.endRendering();
    }

    @Override
    public void loadImage(GL2 gl) {
        TextureLoader.loadAllTexture();
    }

    private final class ToNextAttackTask implements TaskCallable, GLRenderer {
        private boolean enable = false;
        private int taskCallEndCount;
        private final int TO_NEXT_FRAME = 200;
        private Set<Bullet> copyOfBeforeBullets;

        public ToNextAttackTask() {
        }

        @Override
        public void task(TaskCallArgument arg) {
            if (!enable) return;
            if (taskCallEndCount >= TO_NEXT_FRAME) {
                if (attacks.size() - 1 > attackIndex) {
                    attackIndex++;
                }
                this.disable();
            }
            taskCallEndCount++;
        }

        public boolean isEnabled() {
            return this.enable;
        }

        public void enable(Set<Bullet> copyOfBeforeBullets) {
            taskCallEndCount = 0;
            this.enable = true;
            this.copyOfBeforeBullets = copyOfBeforeBullets;
        }

        public void disable() {
            this.enable = false;
        }

        @Override
        public void render(GLDisplay glDisplay, GLAutoDrawable glAutoDrawable) {
            if (!enable) return;
            float color = 1 - (float) taskCallEndCount / (float) (TO_NEXT_FRAME - 50);
            if (color < 0) return;

            GL2 gl2 = glAutoDrawable.getGL().getGL2();
            gl2.glDisable(GL_LIGHTING);
            gl2.glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE);
            gl2.glColor3f(color, color, color);
            copyOfBeforeBullets.stream().sorted(Comparator.comparingDouble(LocateObject::getZ)).forEach(b -> {
                        gl2.glDisable(GL_DEPTH_TEST);
                        gl2.glPushMatrix();

                        gl2.glTranslated(b.getX(), b.getY(), b.getZ());

                        if (b instanceof NormalBullet) {
                            TextureInfo textureInfo = TextureLoader.getTextureInfo(b.getBulletImage());

                            textureInfo.getTexture().bind(gl2);
                            textureInfo.getTexture().enable(gl2);

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
            gl2.glColor4d(1, 1, 1, 1);
            gl2.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            gl2.glEnable(GL_LIGHTING);
        }
    }
}
