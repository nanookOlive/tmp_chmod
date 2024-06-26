package jade;


import imgui.ImGui;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height;
    private String title;
    private static Window window =null; //instance de notre objet Window
    private long glfwindow;
    public float r,g,b,a;
    private static Scene currentScene ;

    private ImGuiLayer imGuiLayer;

    //initialisation de l'objet Window
    //private car singleton
    private Window(){

        //width et heigth en constante  ???
        this.width=1200;
        this.height=800;
        this.title = "Chmod";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }



    //singleton
    public static Window get(){

        if(Window.window == null){
            Window.window=new Window();
        }
        return Window.window;
    }

    public static Scene getScene(){

        return get().currentScene;
    }
    public void run(){

        init();
        loop();

        //gestion de la mémoire

        glfwFreeCallbacks(glfwindow);
        glfwDestroyWindow(glfwindow );

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }



    public void init(){

        //gestion des erreurs
        GLFWErrorCallback.createPrint(System.err).set();
        //initialise la librairie GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Impossible de d'initialiser GLFW.");
        }

        //config window

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
       // glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);//deja à true dans les valeurs par défault
        //glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //create the window

        glfwindow = glfwCreateWindow(this.width, this.height,this.title,NULL,NULL);

        if(glfwindow == NULL){

            throw new IllegalStateException("Impossible de créer l'objet window");
        }

        glfwSetCursorPosCallback(glfwindow,MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwindow,KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwindow,(w,newWidth,newHeight)->{
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);

        });


        glfwMakeContextCurrent(glfwindow);
        //intervalle au bout duquel les frammes buffers sont échangés
        glfwSwapInterval(1);
        glfwShowWindow(glfwindow);

        GL.createCapabilities();

        //fonction pour permettre de blend
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE,GL_ONE_MINUS_SRC_ALPHA);

        this.imGuiLayer = new ImGuiLayer(this.glfwindow);
        this.imGuiLayer.initImGui();
        //choix de la scène
        Window.changeScene(0);


    }
    public void loop(){

        float beginTime = (float)glfwGetTime(); // renvoie un timer depuis initialisation de GLFW
        float endTime;
        float dt =-1.0f;
        //tant que le flag window is closes est false
        while(!glfwWindowShouldClose(glfwindow)){

            glfwPollEvents(); // processe les événements reçu

            glClearColor(r,g,b,a); // modifie l'état
            glClear(GL_COLOR_BUFFER_BIT); // met en service l'état

            if(dt >= 0){

                currentScene.update(dt);

            }
            this.imGuiLayer.update(dt);
            glfwSwapBuffers(glfwindow); // on permutte les buffers

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime; // delta entre le début et la fin de la loop
            beginTime = endTime; //
        }


        System.exit(0);
    }
////fonction qui permet la créati)on d'un niveau de jeu qui sera affiché
    public static void changeScene(int newScene){
        switch(newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Scene inconnue "+newScene+".";
                break;
        }

    }

    public static int getWidth(){
        return get().width;
    }

    public static int getHeight(){
        return get().height;
    }

    public static void setWidth(int newWidth){
        get().width=newWidth;
    }
    public static void setHeight(int newHeight){
        get().height=newHeight;
    }
}
