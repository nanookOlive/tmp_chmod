package components;
import org.joml.Vector2f;
import renderer.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SpriteSheet {


    private Texture texture;
    private List<Sprite> sprites;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numberSprites, int spacing ){

        this.sprites = new ArrayList<>();
        this.texture=texture;
        int currentX=0;
        int currentY = texture.getHeight() - spriteHeight;
        for(int a = 0;a < numberSprites;a++){

            float topY=(currentY+spriteHeight) / (float)texture.getHeight();
            float rightX =(currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX  / (float)texture.getWidth();
            float bottomY=currentY / (float)texture.getHeight();

            Vector2f[] textCoords = {
                    new Vector2f(rightX,topY),
                    new Vector2f(rightX,bottomY),
                    new Vector2f(leftX,bottomY),
                    new Vector2f(leftX,topY)
            };

            Sprite sprite =  new Sprite(this.texture, textCoords);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if(currentX >= texture.getWidth()){

                currentX = 0;
                currentY -= spriteHeight + spacing;
            }

        }

    }

    public Sprite getSprite(int index){
        return sprites.get(index);
    }
}
