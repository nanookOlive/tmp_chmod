package util;

import renderer.Texture;
import renderer.shader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static Map<String, shader> shaders = new HashMap<>();
    private static Map<String,Texture> textures = new HashMap<>();

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

}
