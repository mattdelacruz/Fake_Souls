package a3.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptManager {
    private ScriptEngineManager factory;
    private ScriptEngine jsEngine;
    private File scriptFile;

    public ScriptManager() {
        factory = new ScriptEngineManager();
        jsEngine = factory.getEngineByName("js");
    }

    public void loadScript(String fileName) {
        scriptFile = new File(fileName);
        executeScript(jsEngine, scriptFile);
    }

    public Object getValue(String val) {
        return jsEngine.get(val);
    }

    private void executeScript(ScriptEngine engine, File file) {
        try {
            FileReader fileReader = new FileReader(file);
            engine.eval(fileReader);
            fileReader.close();
        } catch (FileNotFoundException e1) {
            System.out.println(file + " not found " + e1);
        } catch (IOException e2) {
            System.out.println("IO problem with " + file + e2);
        } catch (ScriptException e3) {
            System.out.println("ScriptException in " + file + e3);
        } catch (NullPointerException e4) {
            System.out.println("Null pointer exception in " + file + e4);
        }
    }
}
