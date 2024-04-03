package jade;

import java.util.List;

public class GameObject {

    private String name;
    private List<Component> components;
    public GameObject(String name){
        this.name=name;

    }

    public <T extends Component> T getComponent(Class<T> componentClass){
        for( Component c : components){
            if(componentClass.isAssignableFrom(c.getClass())){
                try{
                    return componentClass.cast(c);
                } catch(ClassCastException e){
                    e.printStackTrace();
                    assert false :"Erreur de conversion de component.";
                }

            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass){

        for(int a = 0;a < components.size();a ++){
            Component c = components.get(a);
            if(componentClass.isAssignableFrom(c.getClass())){
                components.remove(a);
                return;
            }

        }
    }

    public void addComponent(Component component){
        this.components.add(component);
        component.gameObject= this;
    }

    public void update(float dt){
        for(int a=0; a < components.size() ; a++){
            components.get(a).update(dt);
        }
    }

    public void start(){
        for(int a =0 ; a < components.size();a++){
            components.get(a).start();
        }
    }
}
