package triviaMaze;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ResourceManager {
	public void save(Serializable data, String filename) {
		// Serialization  
        try
        {    
            //Saving of object in a file 
            FileOutputStream file = new FileOutputStream(filename); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
              
            // Method for serialization of object 
            out.writeObject(data); 
              
            out.close(); 
            file.close(); 
              
            System.out.println("Object has been serialized"); 
  
        } 
          
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught"); 
        } 
	}
	
	public Object load(String filename) {
		// Deserialization 
		Object temp = new Object();
        try
        {   
        	
            // Reading the object from a file 
            FileInputStream file = new FileInputStream(filename); 
            ObjectInputStream in = new ObjectInputStream(file); 
              
            // Method for deserialization of object 
            //object1 = (Demo)in.readObject(); 
            temp = in.readObject();
            
            in.close(); 
            file.close(); 
              
            System.out.println("Object has been deserialized "); 
            
            //System.out.println("a = " + object1.a); 
            //System.out.println("b = " + object1.b); 
        } 
          
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught"); 
        } 
          
        catch(ClassNotFoundException ex) 
        { 
            System.out.println("ClassNotFoundException is caught"); 
        }
        return temp;
	}
}
