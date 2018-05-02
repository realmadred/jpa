package com.example.jpa.js;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import javax.script.*;
import java.util.concurrent.TimeUnit;

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
    String js2 = "var fun1 = function(name) {\n" +
            "    print('Hi there from Javascript js2, ' + name);\n" +
            "    return \"greetings from javascript\";\n" +
            "};";

    static String script1 = "function xx(a, m, n) { " +
            " var x = a + 1; " +
            " var y = x * 2 + m; " +
            " var z = y * 3 - n;" +
            "if (a > 10 && a <= 100) return 10;" +
            "if (a > 100 && a <= 1000) return '大于100';" +
            " return z;" +
            "} ;";


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
            scriptEngine.eval(js2);
            scriptEngine.eval(script1);
            Invocable invocable = (Invocable) scriptEngine;
            StopWatch started = StopWatch.createStarted();
            for (int i = 0; i <10000000 ; i++) {
//                invocable.invokeFunction("calculate", 56800000000000023L,13.9);
//                invocable.invokeFunction("printSalary", 1200,"name");
//                invocable.invokeFunction("fun1", "abcd");
                invocable.invokeFunction("xx", i*10,i*20,i*30);
            }
            started.stop();
            System.out.println(started.getTime());
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        // 用String定义了一段JavaScript代码这段代码定义了一个对象'obj'
        // 给对象增加了一个名为 hello 的方法（hello这个方法是属于对象的）
        String script = "var obj = new Object(); obj.hello = function(name) { print('Hello, ' + name); }";
        //执行这段script脚本
        engine.eval(script);

        // javax.script.Invocable 是一个可选的接口
        // 检查你的script engine 接口是否已实现!
        // 注意：JavaScript engine实现了Invocable接口
        Invocable inv = (Invocable) engine;

        // 获取我们想调用那个方法所属的js对象
        Object obj = engine.get("obj");

        // 执行obj对象的名为hello的方法
        inv.invokeMethod(obj, "hello", "Script Method !!" );
    }

    @Test
    public void test6() throws ScriptException, InterruptedException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        // String里定义一段JavaScript代码脚本
        String script = "function run() { print('run called'); }";

        // 执行这个脚本
        engine.eval(script);

        Invocable inv = (Invocable) engine;

        // 从脚本引擎中获取Runnable接口对象（实例）. 该接口方法由具有相匹配名称的脚本函数实现。
// 在上面的脚本中，我们已经实现了Runnable接口的run()方法
        Runnable r = inv.getInterface(Runnable.class);

        // 启动一个线程运行上面的实现了runnable接口的script脚本
        Thread th = new Thread(r);
        th.start();//打印输出：run called
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void test7() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        // String里定义一段JavaScript代码脚本
        String script = "var obj = new Object(); obj.run = function() { println('run method called'); }";

        // 执行这个脚本
        engine.eval(script);

        // 获取这个我们要用来实现接口的js对象
        Object obj = engine.get("obj");

        Invocable inv = (Invocable) engine;

        // 通过脚本引擎获取Runnable接口对象 该接口的方法已被js对象实现
        Runnable r = inv.getInterface(obj, Runnable.class);

        // 启动一个线程，运行已被脚本实现的Runnable接口
        Thread th = new Thread(r);
        th.start();
    }

    @Test
    public void testScop() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        engine.put("x", "hello");
        // 打印全局变量 "x"
        engine.eval("print(x);");
        // 上面的代码会打印"hello"

        // 现在，传入另一个不同的script context
        ScriptContext newContext = new SimpleScriptContext();
//新的Script context绑定ScriptContext的ScriptContext
        Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);

        // 增加一个新变脸到新的范围 engineScope 中
        engineScope.put("x", "world");

        // 执行同一个脚本 - 但这次传入一个不同的script context
        engine.eval("print(x);", newContext);
        // 上面的代码会打印 "world"
    }

}
