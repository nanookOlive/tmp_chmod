package components;

import jade.Component;
import jade.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

    private Vector4f color;


    private Sprite sprite;
    private Transform lastTransform;

    public SpriteRenderer(Vector4f color){

        this.color = color;
        this.sprite = new Sprite(null);

    }

    public SpriteRenderer(Sprite sprite){
        this.sprite=sprite;
        this.color= new Vector4f(1,1,1,1);

    }
    @Override
    public  void start(){

    }

    @Override
    public void update(float dt) {


    }

    public Texture getTexture(){

        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords(){

        return sprite.getTexCoords();
    }

    public Vector4f getColor(){

        return this.color;
    }

    public void setSprite(Sprite sprite){

        this.sprite=sprite;
    }

    public void setColor(Vector4f color){
        this.color.set(color);
    }
}
