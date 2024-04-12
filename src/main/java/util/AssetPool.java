package util;

import components.SpriteSheet;
import renderer.Texture;
import renderer.shader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static Map<String, shader> shaders = new HashMap<>();
    private static Map<String,Texture> textures = new HashMap<>();

    private static Map<String,SpriteSheet> spriteSheets = new HashMap<>();

    public static shader getShader(String resourceName){

        File file = new File(resourceName);
        if(AssetPool.shaders.containsKey(file.getAbsolutePath())){
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {

            shader shader = new shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(),shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName){

        File file = new File(resourceName);
        if(AssetPool.textures.containsKey(file.getAbsolutePath())){
            return AssetPool.textures.get(file.getAbsolutePath());
        } else {

            Texture texture =new Texture(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(),texture);
            return texture;
        }

    }

    public static void addSpriteSheet(String resourcename, SpriteSheet spriteSheet){

        File file = new File(resourcename);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){

            AssetPool.spriteSheets.put(file.getAbsolutePath(),spriteSheet);

        }

    }

    public static SpriteSheet getSpriteSheet(String resourceName){

        File file = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            assert false : "Erreur : tentative d'accéder à la spritesheet '"+resourceName+"' non ajoutée.";
        }

        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(),null);
    }

}
