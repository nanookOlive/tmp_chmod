package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class shader {

    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String filepath;
    private boolean beingUsed = false;

    public shader(String filepath){

        this.filepath=filepath;

        try{

            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\n",index);

            String firstPattern = source.substring(index,eol).trim();

//            index = source.indexOf("#type",eol) +6;
//            eol = source.indexOf("\n",index);


            if(firstPattern.equals("vertex")){
                vertexSource = splitString[1];
                fragmentSource = splitString[2];
            } else if(firstPattern.equals("fragment")){

                vertexSource = splitString[2];
                fragmentSource= splitString[1];

            } else {
                throw new IOException("Token inattendu ");
            }




        }catch (IOException e){

            e.printStackTrace();
            assert false : "Error: could not open file for shader: '"+filepath+"'";
        }



    }

    public void compile(){

        int vertexID, fragmentID;
        //////////////////////////////vertex
        vertexID=glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        //gestion erreur

        int succes = glGetShaderi(vertexID,GL_COMPILE_STATUS);
        if(succes == GL_FALSE){

            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'Vertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false:"";

        }

        ////////////////////////fragment
        fragmentID=glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        //gestion erreur

        succes = glGetShaderi(fragmentID,GL_COMPILE_STATUS);
        if(succes == GL_FALSE){

            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'Fragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID,len));
            assert false :"";
        }

        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID,vertexID);
        glAttachShader(shaderProgramID,fragmentID);
        glLinkProgram(shaderProgramID);

        succes = glGetProgrami(shaderProgramID,GL_LINK_STATUS);
        if(succes == GL_FALSE){

            int len = glGetProgrami(shaderProgramID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID,len));


        }


    }

    public void use(){

        if(!beingUsed){
            glUseProgram(shaderProgramID);
            beingUsed=true;
        }



    }

    public void detach(){
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4){

        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation,false,matBuffer);
    }

    public void uploadMat3f(String varname, Matrix3f mat3){

        int varLocation = glGetUniformLocation(shaderProgramID,varname);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation,false, matBuffer);
    }

    public void uploadVec4f(String varname, Vector4f vec){

        int varlocation = glGetUniformLocation(shaderProgramID,varname);
        use();
        glUniform4f(varlocation,vec.x,vec.y, vec.z, vec.w);

    }

    public void uploadVec3f(String varname, Vector3f vec){
        int varLocation = glGetUniformLocation(shaderProgramID,varname);
        use();
        glUniform3f(varLocation,vec.x, vec.y, vec.z);
    }

    public void uploadVec2(String varname, Vector2f vec){

        int varLocation = glGetUniformLocation(shaderProgramID,varname);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varname, float val){

        int varLocation = glGetUniformLocation(shaderProgramID,varname);
        use();
        glUniform1f(varLocation,val);
    }

    public void uploadInt(String varname, int val){

        int varLocation = glGetUniformLocation(shaderProgramID,varname);
        use();
        glUniform1i(varLocation,val);
        
    }

    public void uploadTexture(String varname, int slot){
        int varLocation = glGetUniformLocation(shaderProgramID,varname);
        use();
        glUniform1i(varLocation,slot);
    }

    public void uploadIntArray(String varname, int[]array){

        int varLocation = glGetUniformLocation(shaderProgramID,varname);
        use();
        glUniform1iv(varLocation,array);

    }
}
