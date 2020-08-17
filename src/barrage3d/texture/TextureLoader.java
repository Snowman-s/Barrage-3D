package barrage3d.texture;

import barrage3d.log.MyLogger;
import barrage3d.resource.ResourceLoader;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class TextureLoader {
    private static final Map<TextureIndex, TextureInfo> textureInformationMap = new EnumMap<>(TextureIndex.class);

    public static TextureInfo getTextureInfo(TextureIndex textureIndex) {
        return textureInformationMap.get(textureIndex);
    }

    public static void loadAllTexture() {
        Map<String, Texture> textures = new HashMap<>();

        for (TextureIndex textureIndex : TextureIndex.values()) {
            Texture texture;
            if (textures.containsKey(textureIndex.filePath)) {
                texture = textures.get(textureIndex.filePath);
            } else {
                try (InputStream inputStream = ResourceLoader.getInputStream(textureIndex.filePath)) {
                    texture = TextureIO.newTexture(inputStream, true, TextureIO.PNG);
                    textures.put(textureIndex.filePath, texture);
                } catch (IOException e) {
                    MyLogger.loggerIfAbsent(logger -> logger.warning("cannot find:" + textureIndex.filePath));
                    continue;
                }
            }
            TextureInfo textureInfo = new TextureInfo(texture, textureIndex.rect);
            textureInformationMap.put(textureIndex, textureInfo);
        }
    }

    public static void dispose(GL2 gl) {
        for (TextureInfo r : textureInformationMap.values()) {
            r.getTexture().destroy(gl);
        }
    }
}