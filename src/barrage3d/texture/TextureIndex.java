package barrage3d.texture;

import java.awt.*;

public enum TextureIndex {
    RED_BULLET_S("bullet", 0, 1 - 50F / 2048F, 50F / 1024F, 50F / 2048F),
    YELLOW_BULLET_S("bullet", 50F / 2048F * 2, 1 - 50F / 2048F, 50F / 1024F, 50F / 2048F),
    GREEN_BULLET_S("bullet", 50F / 2048F * 4, 1 - 50F / 2048F, 50F / 1024F, 50F / 2048F),
    SKY_BULLET_S("bullet", 50F / 2048F * 6, 1 - 50F / 2048F, 50F / 1024F, 50F / 2048F),
    BLUE_BULLET_S("bullet", 50F / 2048F * 8, 1 - 50F / 2048F, 50F / 1024F, 50F / 2048F),
    PURPLE_BULLET_S("bullet", 50F / 2048F * 10, 1 - 50F / 2048F, 50F / 1024F, 50F / 2048F);
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
