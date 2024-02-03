package com.accolite.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CompilerService {
    private final ConverterService converterService;

    public List<String> compileAndrunJava( String BASE_PATH,String code) {
        List<String> result = new ArrayList<>();
        if(compile(code))
        {
            String folderPath = BASE_PATH+"Java";
            File folder = new File(folderPath);
            List<String> input = new ArrayList<>();
            List<String> output = new ArrayList<>();
            if (folder.exists() && folder.isDirectory()) {
                // Get the list of files in the folder
                File[] files = folder.listFiles();

                if (files != null) {

                    for (File file : files) {
                        if(!file.getName().equals("Solution.java")) {
                            if(file.getName().startsWith("In"))
                            {
                                input.add(converterService.readFile(file.getAbsolutePath()));
                            }
                            else
                            {
                                output.add(converterService.readFile(file.getAbsolutePath()));
                            }
                        }
                    }
                }
            }

            int size = input.size();
            for (int i=0;i<size;i++)
            {
                StringBuilder res= new StringBuilder("Input : " + input.get(i) + " Expected Output : " + output.get(i));

                String generatedClassName = extractClassName(code);
                String[] in = input.get(i).split(" ");
                res.append(" Actual Output : ").append(runCompiledClass(generatedClassName, in)).replace(res.length()-2,res.length(),"");
                result.add(res.toString());
            }
        }
        return result;
    }

    public List<String> runPython(String BASE_PATH,String code) {
        List<String> result = new ArrayList<>();
        try {
            String folderPath = BASE_PATH+"Python";
            File folder = new File(folderPath);
            List<String> input = new ArrayList<>();
            List<String> output = new ArrayList<>();
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();

                if (files != null) {

                    for (File file : files) {
                        if(!file.getName().equals("Solution.py")) {
                            if(file.getName().startsWith("In"))
                            {
                                input.add(converterService.readFile(file.getAbsolutePath()));
                            }
                            else
                            {
                                output.add(converterService.readFile(file.getAbsolutePath()));
                            }
                        }
                    }
                }
            }
            int size = input.size();
            String pythonScriptPath = "C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\backend\\Solution.py";
            writeCodeInFile(code, pythonScriptPath);
            for (int i=0;i<size;i++) {
                StringBuilder res= new StringBuilder("Input : " + input.get(i) + " Expected Output : " + output.get(i));
                ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
                Process process = processBuilder.start();

                try (OutputStream outputStream = process.getOutputStream();
                     OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
                    writer.write(input.get(i));
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    res.append(" Actual Output : ").append(line);
                }
                result.add(res.toString());
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    private void writeCodeInFile(String code,String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> compileAndrunCpp(String BASE_PATH,String code) throws IOException, InterruptedException {
        List<String> result = new ArrayList<>();
        String folderPath = BASE_PATH+"Cpp";
        File folder = new File(folderPath);
        List<String> input = new ArrayList<>();
        List<String> output = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {
            // Get the list of files in the folder
            File[] files = folder.listFiles();

            if (files != null) {

                for (File file : files) {
                    if(!file.getName().equals("Solution.cpp")) {
                        if(file.getName().startsWith("In"))
                        {
                            input.add(converterService.readFile(file.getAbsolutePath()));
                        }
                        else
                        {
                            output.add(converterService.readFile(file.getAbsolutePath()));
                        }
                    }
                }
            }
        }
        int size = input.size();
        String cpp = "C:\\Users\\bharath.m\\Desktop\\Accolite-Project\\backend\\Solution";
        writeCodeInFile(code,cpp+".cpp");
        compileCpp(cpp+".cpp","Solution");
        for (int i=0;i<size;i++) {
            result.add("Input : " + input.get(i) + " Expected Output : " + output.get(i) + " Acutal Output : " + executeCpp(input.get(i), cpp));
        }
        return result;
    }

    public boolean compile(String javaCode) {
        javax.tools.JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        if (compiler == null) {
            return false;
        }

        String generatedClassName = extractClassName(javaCode);
        if(!generatedClassName.equals("Not Found")) {
            // Create a compilation task
            javax.tools.JavaCompiler.CompilationTask task = compiler.getTask(null, null, null, null, null, Arrays.asList(new JavaSourceFromString(generatedClassName, javaCode)));

            // Perform the compilation
            boolean compilationSuccess = task.call();

            if (compilationSuccess) {
                return true;
                // Run the compiled class
            } else {
                return false;
            }
        }
        return false;
    }

    public String runCompiledClass(String className, String[] input) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);

            // Redirect System.out to capture the output
            PrintStream originalOut = System.out;
            System.setOut(printStream);

            // Load the compiled class
            URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("./").toURI().toURL()});
            Class<?> loadedClass = classLoader.loadClass(className);

            // Check if the class has a main method
            Method mainMethod = loadedClass.getMethod("main", String[].class);

            // Prepare user input if needed
            String[] userInput = input;

            // Invoke the main method
            mainMethod.invoke(null, (Object) userInput);

            // Restore the original System.out
            System.setOut(originalOut);

            // Convert the captured output to a String
            return outputStream.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper class to represent a simple Java source code object
    static class JavaSourceFromString extends SimpleJavaFileObject {
        private final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    public String extractClassName(String javaCode) {
        Pattern pattern = Pattern.compile("public\\s+class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(javaCode);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "Not Found";
        }
    }
    public Boolean compileCpp(String filepath,String fileName) throws IOException, InterruptedException {
        Process compileProcess = new ProcessBuilder("g++", filepath, "-o",fileName+".exe" ).start();
        compileProcess.waitFor();

        if (compileProcess.exitValue() == 0) {
            return true;
        }
        return false;
    }

    public String executeCpp(String input,String fileName) throws IOException, InterruptedException {
        // Execute the compiled C++ program
        Process executeProcess = new ProcessBuilder(fileName+".exe" ).start();
        InputStream inputDataStream = executeProcess.getInputStream();

        // Provide user-defined input to the C++ program
        executeProcess.getOutputStream().write(input.getBytes());
        executeProcess.getOutputStream().close();

        // Read the output of the C++ program
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputDataStream));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        output.replace(output.length() - 1, output.length(), "");
        return output.toString();
    }

}
