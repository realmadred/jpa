package com.example.jpa.time;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Java8日期使用
 * <p>
 * Instant：时间戳
 * LocalDate：不带时间的日期
 * LocalTime：不带日期的时间
 * LocalDateTime：含时间与日期，不过没有带时区的偏移量
 * ZonedDateTime：带时区的完整时间
 */
public class DateClient {

    //1-获取当天的日期
    @Test
    public void test1() {
        LocalDate today = LocalDate.now();
        System.out.println("Today's Local date : " + today);
    }

    //2-获取当前的年月日
    @Test
    public void test2() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        System.out.printf("Year : %d Month : %d day : %d \t %n", year, month, day);
    }

    //3-获取某个特定的日期
    @Test
    public void test3() {
        LocalDate dateOfBirth = LocalDate.of(2010, 01, 14);
        System.out.println("Your Date of birth is : " + dateOfBirth);
    }

    //4-检查两个日期是否相等
    @Test
    public void test4() {
        LocalDate today = LocalDate.now();
        LocalDate date1 = LocalDate.of(2014, 01, 14);
        if (date1.equals(today)) {
            System.out.printf("Today %s and date1 %s are same date %n", today, date1);
        }
    }

    //5-检查重复事件，比如说生日
    @Test
    public void test5() {
        LocalDate today = LocalDate.now();
        LocalDate dateOfBirth = LocalDate.of(2010, 01, 14);
        MonthDay birthday = MonthDay.of(dateOfBirth.getMonth(), dateOfBirth.getDayOfMonth());
        MonthDay currentMonthDay = MonthDay.from(today);
        if (currentMonthDay.equals(birthday)) {
            System.out.println("Many Many happy returns of the day !!");
        } else {
            System.out.println("Sorry, today is not your birthday");
        }
    }

    //6-获取当前时间
    @Test
    public void test6() {
        LocalTime time = LocalTime.now();
        System.out.println("local time now : " + time);
    }

    //7-增加时间里面的小时数
    @Test
    public void test7() {
        LocalTime time = LocalTime.now();
        LocalTime newTime = time.plusHours(2); // adding two hours
        System.out.println("Time after 2 hours : " + newTime);
    }

    //8-获取1周后的日期
    @Test
    public void test8() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);
        System.out.println("Today is : " + today);
        System.out.println("Date after 1 week : " + nextWeek);
    }

    //9-一年前后的日期
    @Test
    public void test9() {
        LocalDate today = LocalDate.now();
        LocalDate previousYear = today.minus(1, ChronoUnit.YEARS);
        System.out.println("Date before 1 year : " + previousYear);
        LocalDate nextYear = today.plus(1, ChronoUnit.YEARS);
        System.out.println("Date after 1 year : " + nextYear);
    }

    //10-不同时区的时钟
    @Test
    public void test10() {
        // Returns the current time based on your system clock and set to UTC.
        Clock clock = Clock.systemUTC();
        System.out.println("Clock : " + clock);
        // Returns time based on system clock zone Clock defaultClock =
        Clock.systemDefaultZone();
        System.out.println("Clock : " + clock);
    }

    //11-判断某个日期是在另一个日期的前面还是后面
    @Test
    public void test11() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.of(2014, 1, 15);
        if (tomorrow.isAfter(today)) {
            System.out.println("Tomorrow comes after today");
        }
        LocalDate yesterday = today.minus(1, ChronoUnit.DAYS);
        if (yesterday.isBefore(today)) {
            System.out.println("Yesterday is day before today");
        }
    }

    //12-处理不同的时区
    @Test
    public void test12() {
        ZoneId america = ZoneId.of("America/New_York");
        LocalDateTime localtDateAndTime = LocalDateTime.now();
        ZonedDateTime dateAndTimeInNewYork = ZonedDateTime.of(localtDateAndTime, america);
        System.out.println("Current date and time in a particular timezone : " + dateAndTimeInNewYork);
    }

    //13-表示固定的日期，比如信用卡过期时间
    @Test
    public void tese13() {
        YearMonth currentYearMonth = YearMonth.now();
        System.out.printf("Days in month year %s: %d%n", currentYearMonth, currentYearMonth.lengthOfMonth());
        YearMonth creditCardExpiry = YearMonth.of(2018, Month.FEBRUARY);
        System.out.printf("Your credit card expires on %s %n", creditCardExpiry);
    }

    //14-在Java 8中检查闰年
    @Test
    public void test14() {
        LocalDate today = LocalDate.now();
        if (today.isLeapYear()) {
            System.out.println("This year is Leap year");
        } else {
            System.out.println("2014 is not a Leap year");
        }
    }

    //15-两个日期之间包含多少天，多少个月
    @Test
    public void test15() {
        LocalDate today = LocalDate.now();
        LocalDate java8Release = LocalDate.of(2014, Month.MARCH, 14);
        Period periodToNextJavaRelease =
                Period.between(today, java8Release);
        System.out.println("Months left between today and Java 8 release : " + periodToNextJavaRelease.getMonths());
    }

    //16-带时区偏移量的日期与时间
    @Test
    public void test16() {
        LocalDateTime datetime = LocalDateTime.of(2014, Month.JANUARY, 14, 19, 30);
        ZoneOffset offset = ZoneOffset.of("+05:30");
        OffsetDateTime date = OffsetDateTime.of(datetime, offset);
        System.out.println("Date and Time with timezone offset in Java : " + date);
    }

    //17-获取当前时间戳
    @Test
    public void test17() {
        Instant timestamp = Instant.now();
        System.out.println("What is value of this instant " + timestamp);
    }

    //18-使用预定义的格式器来对日期进行解析/格式化
    @Test
    public void test18() {
        String dayAfterTommorrow = "20140116";
        LocalDate formatted = LocalDate.parse(
                dayAfterTommorrow,
                DateTimeFormatter.BASIC_ISO_DATE);
        System.out.printf("Date generated from String %s is %s %n", dayAfterTommorrow, formatted);
    }

    //19-使用自定义的格式器来解析日期
    @Test
    public void test19() {
        String goodFriday = "Apr 18 2014";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
            LocalDate holiday = LocalDate.parse(goodFriday, formatter);
            System.out.printf("Successfully parsed String %s, date is %s%n", goodFriday, holiday);
        } catch (DateTimeParseException ex) {
            System.out.printf("%s is not parsable!%n", goodFriday);
        }
    }

    //20-对日期进行格式化，转换成字符串
    @Test
    public void test20() {
        LocalDateTime arrivalDate = LocalDateTime.now();
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
            String landing = arrivalDate.format(format);
            System.out.printf("Arriving at : %s %n", landing);
        } catch (DateTimeException ex) {
            System.out.printf("%s can't be formatted!%n", arrivalDate);
            ex.printStackTrace();
        }
    }

    @Test
    public void test21(){
        //Current timestamp
        Instant timestamp = Instant.now();

        System.out.println("Current Timestamp = "+timestamp.toEpochMilli());

        //Instant from timestamp

        Instant specificTime = Instant.ofEpochMilli(timestamp.toEpochMilli());

        System.out.println("Specific Time = "+specificTime);

        //Duration example

        Duration thirtyDay = Duration.ofDays(30);

        System.out.println(thirtyDay.toHours());
    }

    @Test
    public void test22() {

        LocalDate today = LocalDate.now();

        System.out.println("Year "+today.getYear()+" is Leap Year? "+today.isLeapYear());
        System.out.println("10 days after today will be "+today.plusDays(10));
        System.out.println("3 weeks after today will be "+today.plusWeeks(3));
        System.out.println("20 months after today will be "+today.plusMonths(20));
        System.out.println("10 days before today will be "+today.minusDays(10));
        System.out.println("3 weeks before today will be "+today.minusWeeks(3));
        System.out.println("20 months before today will be "+today.minusMonths(20));

        //Temporal adjusters for adjusting the dates

        System.out.println("First date of this month= "+today.with(TemporalAdjusters.firstDayOfMonth()));
        LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());
        System.out.println("Last date of this year= "+lastDayOfYear);
        Period period = today.until(lastDayOfYear);
        System.out.println("Period Format= "+period);

        System.out.println("Months remaining in the year= "+period.getMonths());

        // 取2015年1月第一个周一，这个计算用Calendar要死掉很多脑细胞：
        LocalDate firstMondayOf2015 = LocalDate.parse("2015-01-01").with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)); // 2015-01-05
        System.out.println(firstMondayOf2015);
    }

    @Test
    public void test23() {
        //创建Optional实例，也可以通过方法返回值得到。
        Optional<String> name = Optional.of("Sanaulla");

        //创建没有值的Optional实例，例如值为'null'
        Optional<String> empty = Optional.ofNullable(null);

        //isPresent方法用来检查Optional实例是否有值。
        //调用get()返回Optional值。
        name.ifPresent(System.out::println);

        try {
            //在Optional实例上调用get()抛出NoSuchElementException。
            System.out.println(empty.get());
        } catch (NoSuchElementException ex) {
            System.out.println(ex.getMessage());
        }

        //ifPresent方法接受lambda表达式参数。
        //如果Optional值不为空，lambda表达式会处理并在其上执行操作。
        name.ifPresent((value) -> {
            System.out.println("The length of the value is: " + value.length());
        });

        //如果有值orElse方法会返回Optional实例，否则返回传入的错误信息。
        System.out.println(empty.orElse("There is no value present!"));
        System.out.println(name.orElse("There is some value!"));

        //orElseGet与orElse类似，区别在于传入的默认值。
        //orElseGet接受lambda表达式生成默认值。
        System.out.println(empty.orElseGet(() -> "Default Value"));
        System.out.println(name.orElseGet(() -> "Default Value"));

        try {
            //orElseThrow与orElse方法类似，区别在于返回值。
            //orElseThrow抛出由传入的lambda表达式/方法生成异常。
            empty.orElseThrow(RuntimeException::new);
        } catch (Throwable ex) {
            System.out.println(ex.getMessage());
        }

        //map方法通过传入的lambda表达式修改Optonal实例默认值。
        //lambda表达式返回值会包装为Optional实例。
        Optional<String> upperName = name.map(String::toUpperCase);
        System.out.println(upperName.orElse("No value found"));

        //flatMap与map（Funtion）非常相似，区别在于lambda表达式的返回值。
        //map方法的lambda表达式返回值可以是任何类型，但是返回值会包装成Optional实例。
        //但是flatMap方法的lambda返回值总是Optional类型。
        upperName = name.flatMap((value) -> Optional.of(value.toUpperCase()));
        System.out.println(upperName.orElse("No value found"));

        //filter方法检查Optiona值是否满足给定条件。
        //如果满足返回Optional实例值，否则返回空Optional。
        Optional<String> longName = name.filter((value) -> value.length() > 6);
        System.out.println(longName.orElse("The name is less than 6 characters"));

        //另一个示例，Optional值不满足给定条件。
        Optional<String> anotherName = Optional.of("Sana");
        Optional<String> shortName = anotherName.filter((value) -> value.length() > 6);
        System.out.println(shortName.orElse("The name is less than 6 characters"));
    }

}
