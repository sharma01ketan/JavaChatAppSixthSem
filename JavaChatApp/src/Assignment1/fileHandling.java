package Assignment1;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class fileHandling {
    public static void main(String[] args) throws DirectoryNotFound, FileNotFound {
        String byteFileName = "byteExample.txt";
        String charFileName = "characterExample.txt";


        //1. Create a new directory: "Demo" in cwd
        File directory = new File("Demo");
        if(!directory.exists()){
            directory.mkdir();
        }

        //2. Create a file in Demo: "example.txt"
        File file = new File(directory, "example.txt");
        try{
            boolean created = file.createNewFile();
            if(created){
                System.out.println("Created file: \"example.txt in Demo");
            }
            else{
                System.out.println("File already exists");
            }
        }
        catch(IOException ioe){
            System.out.println("File cannot be created");
        }

        //3. Write some sample text into that file
        try{
            FileWriter writer = new FileWriter(file);
            writer.write("Hey, this is my sample text for the file created inside demo.\n");
            writer.close();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }

        //4. Read the contents of file and print them
        try{
            if(file.exists()){
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    System.out.println(text);
                }

                bufferedReader.close();
                reader.close();
            }
            else{
                throw new FileNotFound("the file you're trying to access was not found");
            }
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }

        //5. Update the contents of the file
        try{
            FileWriter writer = new FileWriter(file, true);
            writer.append("let us append some more text to our previous file");
            writer.close();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }

        //6. Read the updated text and print it on the console
        try{
            if(file.exists()){
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    System.out.println(text);
                }

                bufferedReader.close();
                reader.close();
            }
            else{
                throw new FileNotFound("the file you're trying to access was not found");
            }
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }

        //7. Delete the file "example.txt"
        if(file.exists()){
            file.delete();
        }
        else{
            throw new FileNotFound("file does not exist!");
        }

        //8. Delete the directory "Demo"
        if(directory.exists()){
            directory.delete();
        }
        else{
            throw new DirectoryNotFound("this directory does not exist");
        }

        //9. Create a new file "byteExample.txt" and write some text using byte streams
        try{
            FileOutputStream outputStream = new FileOutputStream(byteFileName);
            String byteContent = "some sample text for the byteStream";
            byte[] bytes = byteContent.getBytes();
            outputStream.write(bytes);
            outputStream.close();
        }
        catch(IOException ioe){
            System.out.println("Error!! could not write/update the desired file");
        }

        //10. Read the contents of "byteExample.txt" using byte streams and print them.
        try {
            FileInputStream inputStream = new FileInputStream(byteFileName);
            int content;
            while ((content = inputStream.read()) != -1) {
                System.out.print((char) content);
            }
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Error!! could not write/update the desired file");
        }

        //11. Create a new file "characterExample.txt" and write some text using character streams.
        try {
            FileWriter writer = new FileWriter(charFileName);
            String charContent = "Sample character stream content.";
            writer.write(charContent);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error!! could not write/update the desired file");
        }

        //12. Read the contents of "characterExample.txt" using character streams and display them.
        try {
            FileReader reader = new FileReader(charFileName);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Error!! could not write/update the desired file");
        }
    }
}

//13. Ensure error handling and exception handling mechanisms are implemented throughout the program.
class DirectoryNotFound extends Exception{
    public DirectoryNotFound(String msg){
        super(msg);
    }
}
class FileNotFound extends Exception{
    public FileNotFound(String msg){
        super(msg);
    }
}
