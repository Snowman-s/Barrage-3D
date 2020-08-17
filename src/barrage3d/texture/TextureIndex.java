package barrage3d.texture;

import java.awt.*;

public enum TextureIndex {
    RED_BULLET_S("bullet",0,1-50F/2048F,50F/1024F,50F/2048F),
    SWORD("sword");

    public final String filePath;
    public final Rectangle.Float rect;

    TextureIndex(String path) {
        filePath = String.format("imgsrc/%s.png", path);
        this.rect = new Rectangle.Float(0, 0, 1, 1);
    }

    TextureIndex(String path, float left, float up, float width, float height) {
        filePath = String.format("imgsrc/%s.png", path);
        this.rect = new Rectangle.Float(left, up, width, height);
    }
}
