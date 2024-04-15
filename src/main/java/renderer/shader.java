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
    private final String filepath;
    private boolean beingUsed = false;

    public shader(String filepath) {

        this.filepath = filepath;

        //permet de récupèrer le code dans shader.glsl soit du vertex soit du fragment
        try {

            String source = new String(Files.readAllBytes(Paths.get(filepath)));//on récupère le contenu de shader.glsl
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");//on divise le contenu en deux selon le type


            int index = source.indexOf("#type") + 6; // on veut l'index du mot après type
            int eol = source.indexOf("\n", index); // fin de la ligne

            String firstPattern = source.substring(index, eol).trim(); /// on récupère le mot après type dans le debut de chaine

//            index = source.indexOf("#type",eol) +6;
//            eol = source.indexOf("\n",index);



            // assigne les contenus trouvés à vertex et fragment
            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
                fragmentSource = splitString[2];
            } else if (firstPattern.equals("fragment")) {

                vertexSource = splitString[2];
                fragmentSource = splitString[1];

            } else {
                throw new IOException("Token inattendu ");
            }


        } catch (IOException e) {

            e.printStackTrace();
            assert false : "Error: could not open file for shader: '" + filepath + "'";
        }


    }


    public void compile() {


        // ============================================================
        // Compile and link shaders
        // ============================================================
        int vertexID, fragmentID;

        //on crée un shader vide et on renvoie une valeur de référence
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // on passe le shader  source au gpu
        glShaderSource(vertexID, vertexSource);
        //on compile
        glCompileShader(vertexID);

        // on check els erreurs
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // on répète l'opération pour le fragment
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // on check les erreurs
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }


        // on crée un objet program vide et renvoie une valeur de référence
        shaderProgramID = glCreateProgram();
        //on lie le vertex et le fragment au program
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        //compile et crée un excecutable qui sera executer par le gpu
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        if (!beingUsed) {

            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }


    //ensemble de fonctions qui vont permettre le chargement de data dans le shader
    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName); // index de varname dans le shader.
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] array) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1iv(varLocation, array);
    }

}