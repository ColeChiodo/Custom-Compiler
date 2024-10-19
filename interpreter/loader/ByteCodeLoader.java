package interpreter.loader;

import interpreter.bytecode.*;

import java.lang.reflect.*;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ByteCodeLoader {

  private String byteCodeFilePath;

  public ByteCodeLoader(String byteCodeFilePath) throws IOException {
    this.byteCodeFilePath = byteCodeFilePath;
  }

  public Program loadCodes() throws ByteCodeLoaderException {
    Program program = new Program();

    BufferedReader reader;
    try{
      reader = new BufferedReader(new FileReader(this.byteCodeFilePath));
      String line = reader.readLine();

      while(line != null){
        String[] tokens = line.split("\\s+");
        String codeName = tokens[0];
        String className = CodeTable.get(codeName);

        Class<?> codeClass = Class.forName(className);
        Constructor<?> constructor = codeClass.getDeclaredConstructor(List.class);

        ByteCode code;
        List<String> tokenList = List.of(tokens);
        code = (ByteCode) constructor.newInstance(tokenList);
        
        program.addCode(code);
        line = reader.readLine();
      }

    }catch(IOException e){
      throw new ByteCodeLoaderException("Error reading file");
    }catch (Throwable e) {
      System.err.println(e);
   }

    program.resolveSymbolicAddresses();
    return program;
  }
  
}
