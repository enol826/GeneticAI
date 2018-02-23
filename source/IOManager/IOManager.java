package source.IOManager;
import java.io.*;

public class IOManager {
    private File file;

    public IOManager(String file) {
        File  f = new File(file);
        if(!f.exists()) throw new RuntimeException("File not found");
        this.file = f;
    }

    private String read() throws IOException{
        StringBuilder string = new StringBuilder();
        int by;
        FileReader inputStream = new FileReader(this.file);
        while( (by = inputStream.read()) != -1)
            string.append(String.valueOf((char) by));
        inputStream.close();
        return string.toString();
    }

    private void write(String string) throws IOException {
        FileWriter outputStream = new FileWriter(this.file);
        outputStream.write(string);
        outputStream.close();
    }

    public void append(String string,int lines) {
        try {
            String previous = this.read();
            for(int i=0;i<lines;i++) string += '\n';
            this.write(previous+string);
        }catch (IOException ex) {
            System.out.println("Something has gone wrong while reading and/or writing to the file");
        }
    }

    public File getFile() {
        return this.file;
    }

}
