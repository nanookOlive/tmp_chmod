package jade;


import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;
import util.AssetPool;

import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;

public class LevelEditorScene extends Scene {

    private GameObject object1;
    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250,0));

        SpriteSheet sprites= AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        object1 = new GameObject("object1",new Transform(new Vector2f(100,100),new Vector2f(256,256)));
        object1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(object1);

        GameObject object2 = new GameObject("object2",new Transform(new Vector2f(400,100),new Vector2f(256,256)));
        object2.addComponent(new SpriteRenderer(sprites.getSprite(7)));
        this.addGameObjectToScene(object2);

    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"),16,16,26,0));
    }
    @Override
    public void update(float dt) {
//



        //System.out.println("FPS => "+(1.0f/dt));
        for(GameObject go : this.gameObjects){
            go.update(dt);
        }

        this.renderer.render();
    }
}
