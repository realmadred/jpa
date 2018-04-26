package com.example.jpa.js;

import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JsTest {

    @Test
    public void test() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval("print('Hello World!');");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {

        String js = "var fun1 = function(name) {\n" +
                "    print('Hi there from Javascript, ' + name);\n" +
                "    return \"greetings from javascript\";\n" +
                "};";
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(js);
            Invocable invocable = (Invocable) engine;
            Object o = invocable.invokeFunction("fun1", "abcd");
            System.out.println(o);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
