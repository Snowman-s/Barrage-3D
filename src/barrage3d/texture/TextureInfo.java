package barrage3d.texture;

import com.jogamp.opengl.util.texture.Texture;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TextureInfo {
    private Texture texture;
    private Rectangle.Float sourceRect;

    TextureInfo(Texture texture, Rectangle.Float sourceRect) {
        this.texture = texture;
        this.sourceRect = sourceRect;
    }

    public Rectangle.Float getSourceRect() {
        return sourceRect;
    }

    public Texture getTexture() {
        return texture;
    }
}
