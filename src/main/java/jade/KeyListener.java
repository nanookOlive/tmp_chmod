package jade;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {

    private static KeyListener instance ;
    private boolean keyPressed[]=new boolean[350];

    //////////////////singleton
    private KeyListener(){}

    public static KeyListener get(){

        if(instance == null){
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }


    //////////////////////////////la fonction de callback
    public static void keyCallback(long window, int key, int scancode, int action, int mods){

        if(action == GLFW_PRESS){ // si une touche est appuyée

            get().keyPressed[key]=true; // dans le tableau on associe le code la touche pressée avec TRUE



        }else if(action == GLFW_RELEASE){ // si la touche est relachée à la clé on associe false

            get().keyPressed[key]=false;

        }
    }

    public static boolean isKeyPressed(int keyCode){

        if(keyCode < get().keyPressed.length){
            return get().keyPressed[keyCode];
        }else{
            return false;
        }
    }
}
