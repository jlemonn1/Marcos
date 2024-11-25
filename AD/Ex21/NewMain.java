package AD.Ex21;

import java.io.File;
import java.io.FileOutputStream;
import java.rmi.MarshalException;
import java.util.ArrayList;

public class NewMain {
    
        public static void main(String[] args) {
            ArrayList<Alumno> alumnos = new ArrayList<>();
            alumnos.add(new Alumno("Paco"));
            alumnos.add(new Alumno("Maria"));
            alumnos.add(new Alumno("Pepe"));
    
            Grupo g1  = new Grupo(alumnos);
    
        }
    
    
        //Para serializar --> convertir obj en archivo .xml
        public static void SerializeGroup(Grupo grupo) {
            Xstream serializador = new Xtream();
            serializador.toXML(grupo, new FileOutputStream("Grupo.xml"));  
        }

        public static Grupo DeserializeGroup(){
            Xstream deserializador = new Xtream();
            Grupo grupoDeserializado = (Grupo) deserializador.fromXML(new File("Grupo.xml")); 
            return grupoDeserializado;
        }
}

