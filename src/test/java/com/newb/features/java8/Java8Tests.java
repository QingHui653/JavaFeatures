package com.newb.features.java8;

import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * https://zhuanlan.zhihu.com/p/439176814
 * 1. Lambda 表达式
 * 2. 函数数接口 @FunctionalInterface
 * 3. 方法引用 ::  方法引用使用一对冒号::，通过方法的名字来指向一个方法
 * 4. 默认方法和静态方法,接口中 使用 default,static 标记的方法
 * 5. Steam 流
 * 6. Optional 类
 * 7. Date-Time API
 * 8. Base64
 * 9. Nashorn JavaScript 引擎
 */
public class Java8Tests {
    final static String salutation = "Hello! ";

    /**
     * 将函数当成参数传递给某个方法，或者把代码本身当作数据处理，函数式开发者非常熟悉这些概念
     *
     * (parameters) -> expression
     * (parameters) ->{ statements; }
     * 以下是lambda表达式的重要特征:
     * <p>
     * 可选类型声明：不需要声明参数类型，编译器可以统一识别参数值。
     * 可选的参数圆括号：一个参数无需定义圆括号，但多个参数需要定义圆括号。
     * 可选的大括号：如果主体包含了一个语句，就不需要使用大括号。
     * 可选的返回关键字：如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定表达式返回了一个数值。
     * https://www.runoob.com/java/java8-lambda-expressions.html
     */
    public static void main(String[] args) {
        /**
         * Lambda 表达式主要用来定义行内执行的方法类型接口（例如，一个简单方法接口）。在上面例子中，我们使用各种类型的 Lambda 表达式来定义 MathOperation 接口的方法，然后我们定义了 operation 的执行。
         * Lambda 表达式免去了使用匿名方法的麻烦，并且给予 Java 简单但是强大的函数化的编程能力
         */
        Java8Tests tester = new Java8Tests();
        // 类型声明
        MathOperation addition = (int a, int b) -> a + b;
        // 不用类型声明
        MathOperation subtraction = (a, b) -> a - b;
        // 大括号中的返回语句
        MathOperation multiplication = (int a, int b) -> {
            return a * b;
        };
        // 没有大括号及返回语句
        MathOperation division = (int a, int b) -> a / b;

        System.out.println("10 + 5 = " + tester.operate(10, 5, addition));
        System.out.println("10 - 5 = " + tester.operate(10, 5, subtraction));
        System.out.println("10 x 5 = " + tester.operate(10, 5, multiplication));
        System.out.println("10 / 5 = " + tester.operate(10, 5, division));

        // 不用括号
        GreetingService greetService1 = message ->
                System.out.println("Hello " + message);

        // 用括号
        GreetingService greetService2 = (message) ->
                System.out.println("Hello " + message);

        greetService1.sayMessage("Runoob");
        greetService2.sayMessage("Google");

        System.out.println("---------------");
        // lambda 表达式只能引用标记了 final 的外层局部变量，这就是说不能在 lambda 内部修改定义在域外的局部变量，否则会编译错误
        GreetingService greetService3 = message ->
                System.out.println(salutation + message);
        greetService3.sayMessage("Runoob");

        /**
         * 我们也可以直接在 lambda 表达式中访问外层的局部变量：
         */
        System.out.println("---------------");
        final int num = 1;
        Converter<Integer, String> s = (param) -> System.out.println(String.valueOf(param + num));
        s.convert(2);  // 输出结果为 3

        /*
        lambda 表达式的局部变量可以不用声明为 final，但是必须不可被后面的代码修改（即隐性的具有 final 的语义）
        int num2 = 1;
        Converter<Integer, String> s = (param) -> System.out.println(String.valueOf(param + num2));
        s.convert(2);
        num2 = 5;*/

        /**
         * 在 Lambda 表达式当中不允许声明一个与局部变量同名的参数或者局部变量。
         * String first = "";
         * Comparator<String> comparator = (first, second) -> Integer.compare(first.length(), second.length());  //编译会出错
         */

    }

    /**
     * 函数接口指的是一个有且仅有一个抽象方法，但是可以有多个非抽象方法的接口，这样的接口可以隐式转换为 Lambda 表达式。
     *
     * 但是在实践中，函数式接口非常脆弱，只要某个开发者在该接口中添加一个函数，则该接口就不再是函数式接口进而导致编译失败。
     * 为了克服这种代码层面的脆弱性，并显式说明某个接口是函数式接口，Java 8 提供了一个特殊的注解@FunctionalInterface，
     * 举个简单的函数式接口的定义：
     */
    @FunctionalInterface
    interface MathOperation {
        int operation(int a, int b);
    }
    @FunctionalInterface
    interface GreetingService {
        void sayMessage(String message);
    }
    private int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }
    @FunctionalInterface
    public interface Converter<T1, T2> {
        void convert(int i);
    }

    @Test
    public void LambdaTest() {
        String[] players = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka", "David Ferrer",
                "Roger Federer", "Andy Murray",
                "Tomas Berdych", "Juan Martin Del Potro",
                "Richard Gasquet", "John Isner"};
        // 1.1 使用匿名内部类根据 name 排序 players
        Arrays.sort(players, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return (s1.compareTo(s2));
            }
        });

        // 1.2 使用 lambda expression 排序 players
        Comparator<String> sortByName = (String s1, String s2) -> (s1.compareTo(s2));
        Arrays.sort(players, sortByName);

        // 1.3 也可以采用如下形式:
        Arrays.sort(players, (String s1, String s2) -> (s1.compareTo(s2)));

        // 1.1 使用匿名内部类根据 surname 排序 players
        Arrays.sort(players, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return (s1.substring(s1.indexOf(" ")).compareTo(s2.substring(s2.indexOf(" "))));
            }
        });

        // 1.2 使用 lambda expression 排序,根据 surname
        Comparator<String> sortBySurname = (String s1, String s2) ->
                (s1.substring(s1.indexOf(" ")).compareTo(s2.substring(s2.indexOf(" "))));
        Arrays.sort(players, sortBySurname);

        // 1.3 或者这样,怀疑原作者是不是想错了,括号好多...
        Arrays.sort(players, (String s1, String s2) ->
                (s1.substring(s1.indexOf(" ")).compareTo(s2.substring(s2.indexOf(" "))))
        );

        // 2.1 使用匿名内部类根据 name lenght 排序 players
        Arrays.sort(players, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return (s1.length() - s2.length());
            }
        });

        // 2.2 使用 lambda expression 排序,根据 name lenght
        Comparator<String> sortByNameLenght = (String s1, String s2) -> (s1.length() - s2.length());
        Arrays.sort(players, sortByNameLenght);

        // 2.3 or this
        Arrays.sort(players, (String s1, String s2) -> (s1.length() - s2.length()));

        // 3.1 使用匿名内部类排序 players, 根据最后一个字母
        Arrays.sort(players, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return (s1.charAt(s1.length() - 1) - s2.charAt(s2.length() - 1));
            }
        });

        // 3.2 使用 lambda expression 排序,根据最后一个字母
        Comparator<String> sortByLastLetter =
                (String s1, String s2) ->
                        (s1.charAt(s1.length() - 1) - s2.charAt(s2.length() - 1));
        Arrays.sort(players, sortByLastLetter);

        // 3.3 or this
        Arrays.sort(players, (String s1, String s2) -> (s1.charAt(s1.length() - 1) - s2.charAt(s2.length() - 1)));
    }

    /**
     * 将数据按流一样进行操作
     * stream()：为集合创建串行流
     * parallelStream()：为集合创建并行流
     * filter limit limit sorted map reduce forEach Collectors
     */
    @Test
    public void steamTest(){
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        // 获取集合中大于2、并且经过排序、平方去重的有序集合
        List<Integer> squaresList = numbers
                .stream()
                .filter(x -> x > 2)
                .sorted((x,y) -> x.compareTo(y))
                .map( i -> i*i).distinct().collect(Collectors.toList());
        //Collectors
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());

        System.out.println("筛选列表: " + filtered);
        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);

        //统计
        IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics();

        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());

        // 获取空字符串的数量 parallelStream
        long count = strings.parallelStream().filter(string -> string.isEmpty()).count();
    }

    @Test
    public void optionalTest(){
        Integer value1 = null;
        Integer value2 = 10;
        // Optional.ofNullable - 允许传递为 null 参数
        Optional<Integer> a = Optional.ofNullable(value1);
        // Optional.of - 如果传递的参数是 null，抛出异常 NullPointerException
        Optional<Integer> b = Optional.of(value2);
        System.out.println(this.sum(a,b));
    }

    public Integer sum(Optional<Integer> a, Optional<Integer> b){

        // Optional.isPresent - 判断值是否存在

        System.out.println("第一个参数值存在: " + a.isPresent());
        System.out.println("第二个参数值存在: " + b.isPresent());

        // Optional.orElse - 如果值存在，返回它，否则返回默认值
        Integer value1 = a.orElse(0);

        //Optional.get - 获取值，值需要存在
        Integer value2 = b.get();
        return value1 + value2;
    }

    @Test
    public void dateTimeTest(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = "2021-01-01 12:00:00";

        //1 .Clock
        //Clock类使用时区来返回当前的纳秒时间和日期。Clock可以替代System.currentTimeMillis()和TimeZone.getDefault()
        final Clock clock = Clock.systemUTC();
        System.out.println( clock.instant() );
        System.out.println( clock.millis() );
        System.out.println("---------");
        //2. LocalDate
        //获取当前日期
        final LocalDate date = LocalDate.now();
        //获取指定时钟的日期
        final LocalDate dateFromClock = LocalDate.now( clock );
        System.out.println( date );
        System.out.println( dateFromClock );
        System.out.println("---------");
        //3. LocalTime
        //获取当前时间
        final LocalTime time = LocalTime.now();
        //获取指定时钟的时间
        final LocalTime timeFromClock = LocalTime.now( clock );
        System.out.println( time );
        System.out.println( timeFromClock );
        System.out.println("---------");
        //4. LocalDateTime
        //获取当前日期时间
        final LocalDateTime datetime = LocalDateTime.now();
        //获取指定时钟的日期时间
        final LocalDateTime datetimeFromClock = LocalDateTime.now( clock );
        System.out.println( datetime );
        System.out.println( datetimeFromClock );
        System.out.println("转换string: " + datetime.format(formatter));
        System.out.println("string 转换: " + LocalDateTime.parse(formatDateTime, formatter));
        System.out.println("---------");
        //5 ZonedDateTime
        // 获取当前时间日期
        final ZonedDateTime zonedDatetime = ZonedDateTime.now();
        //获取指定时钟的日期时间
        final ZonedDateTime zonedDatetimeFromClock = ZonedDateTime.now( clock );
        //获取纽约时区的当前时间日期
        final ZonedDateTime zonedDatetimeFromZone = ZonedDateTime.now( ZoneId.of("America/New_York") );

        System.out.println( zonedDatetime );
        System.out.println( zonedDatetimeFromClock );
        System.out.println( zonedDatetimeFromZone );
        System.out.println("---------");
        //6. Duration
        final LocalDateTime from = LocalDateTime.of( 2020, Month.APRIL, 16, 0, 0, 0 );
        final LocalDateTime to = LocalDateTime.of( 2021, Month.APRIL, 16, 23, 59, 59 );
        //获取时间差
        final Duration duration = Duration.between( from, to );
        System.out.println( "Duration in days: " + duration.toDays() );
        System.out.println( "Duration in hours: " + duration.toHours() );
        System.out.println("---------");
    }

    @Test
    public void base64Test(){
        final String text = "Base64 finally in Java 8!";
        final String encoded = Base64.getEncoder().encodeToString( text.getBytes( StandardCharsets.UTF_8 ) );
        System.out.println( encoded );
        final String decoded = new String(Base64.getDecoder().decode( encoded ), StandardCharsets.UTF_8 );
        System.out.println( decoded );
    }

    @Test
    public void javaScriptTest(){
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");

        String name = "Hello World";
        try {
            nashorn.eval("print('" + name + "')");
        }catch(ScriptException e){
            System.out.println("执行脚本错误: "+ e.getMessage());
        }
    }
}
