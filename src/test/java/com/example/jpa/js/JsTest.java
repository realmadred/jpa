package com.example.jpa.js;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JsTest {

    String s1 = "var BigDecimal = Java.type('java.math.BigDecimal'); \n" +
            "function calculate(amount, percentage) { \n" +
            "   var result = new BigDecimal(amount).multiply( \n" +
            "      new BigDecimal(percentage)).divide( \n" +
            "         new BigDecimal(\"100\"), 2, BigDecimal.ROUND_HALF_EVEN); \n" +
            "   return result.toPlainString(); \n" +
            "}";

    String script = "function printSalary(money,field) {" +
            "if (money > 10 && money <= 100) return 10;" +
            "if (money > 100 && money <= 1000) return '大于100';" +
            " return 'My salary is:' + money +'field:'+field" +
            "}";

    String js = "var fun1 = function(name) {\n" +
            "    //print('Hi there from Javascript, ' + name);\n" +
            "    return \"greetings from javascript\";\n" +
            "};";

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

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(js);
            Invocable invocable = (Invocable) engine;
            Object o = null;
            StopWatch started = StopWatch.createStarted();
            for (int i = 0; i <100000 ; i++) {
                o = invocable.invokeFunction("fun1", "abcd");
            }
            started.stop();
            System.out.println(o);
            System.out.println(started.getTime());
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");

        try {
            scriptEngine.eval(script);
            Invocable invocable = (Invocable) scriptEngine;
            System.out.println(invocable.invokeFunction("printSalary", 1200,"name"));
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");

        try {
            scriptEngine.eval(s1);
            scriptEngine.eval(script);
            scriptEngine.eval(js);
            StopWatch started = StopWatch.createStarted();
            for (int i = 0; i <1000000 ; i++) {
                Invocable invocable = (Invocable) scriptEngine;
                invocable.invokeFunction("calculate", 56800000000000023L,13.9);
                invocable.invokeFunction("printSalary", 1200,"name");
                invocable.invokeFunction("fun1", "abcd");
            }
            started.stop();
            System.out.println(started.getTime());
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
