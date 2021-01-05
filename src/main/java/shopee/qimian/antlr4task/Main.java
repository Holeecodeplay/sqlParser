package shopee.qimian.antlr4task;

import shopee.qimian.antlr4task.db.metadata.Data;
import shopee.qimian.antlr4task.execution.StartFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qimian.huang 黄奇冕
 * @Description
 */

public class Main {
    public static void main(String[] args) {
        final int NUM = 50000;
        final String TABLE_NAME = "table1";
        long start = 0L;
        long useTime = 0L;

        String create = "create table table1 (gender string, name string, age int, income double)";
        String select1 = "select name,income from (select name,age,income from table1) t where name = 'one'";
        String select2 = "select sum(income) from (select income from table1 where age = 2) tt";
        String selectFinal = "SELECT name, age, sum(income) FROM (SELECT gender,name,age,income FROM table1 WHERE age = 2 ) TABLE2 group by name,age";


        // ----------------------------------------- Create -----------------------------------------
        System.out.println("execute sql: " + create);
        System.out.println(StartFactory.start(create));
        System.out.println("-----------------------------------------");

        // ----------------------------------------- insert -----------------------------------------
        insertData(NUM,TABLE_NAME);
        System.out.println("-----------------------------------------");

        // ----------------------------------------- Query-Filter-SubQuery -----------------------------------------
        System.out.println("execute sql: " + select1);
        start = System.currentTimeMillis();
        StartFactory.start(select1);
        //System.out.println(StartFactory.start(select1));
        useTime = System.currentTimeMillis() - start;
        System.out.printf("use %d ms%n", useTime);
        System.out.println("-----------------------------------------");

        // ----------------------------------------- Sum-WithoutGroupBy-Filter-SubQuery -----------------------------------------
        System.out.println("execute sql: " + select2);
        start = System.currentTimeMillis();
        System.out.println(StartFactory.start(select2));
        useTime = System.currentTimeMillis() - start;
        System.out.printf("use %d ms%n", useTime);
        System.out.println("-----------------------------------------");

        // ----------------------------------------- sum with group by -----------------------------------------

        // ----------------------------------------- sum with group by and sub filter -----------------------------------------
    }

    public static void insertData(int num, String tableNm){
        List<String[]> result = new ArrayList<>(num*10);
        List<String[]> dataList = Arrays.asList(
                new String[]{"male", "one", "1", "0.5"},
                new String[]{"male", "two", "2", "0.5"},
                new String[]{"male", "three", "3", "0.5"},
                new String[]{"male", "four", "4", "0.5"},
                new String[]{"male", "five", "5", "0.5"},
                new String[]{"female", "six", "6", "1.5"},
                new String[]{"female", "seven", "7", "1.5"},
                new String[]{"female", "eight", "8", "1.5"},
                new String[]{"female", "nine", "9", "1.5"},
                new String[]{"female", "ten", "10", "1.5"}
        );
        System.out.println("insert data num: " + num*10);
        while(num-- > 0 ){
            result.addAll(dataList);
        }
        System.out.println(Data.addData(tableNm, result));
    }






}
