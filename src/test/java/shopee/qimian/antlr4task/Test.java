package shopee.qimian.antlr4task;

import shopee.qimian.antlr4task.db.metadata.Data;
import shopee.qimian.antlr4task.execution.Execution;
import shopee.qimian.antlr4task.execution.Selector;
import shopee.qimian.antlr4task.node.LogicalPlan;
import shopee.qimian.antlr4task.parser.MyVisitor;
import shopee.qimian.antlr4task.parser.SqlBaseParser;
import shopee.qimian.antlr4task.planer.CreateTablePlan;
import shopee.qimian.antlr4task.util.ParseUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author qimian.huang 黄奇冕
 * @Description 测试
 */

public class Test {
    public static void main(String[] args) {
        String create = "create table table1 (gender string, name string, age int, income double)";
        String select1 = "select name from (select name from table1) t where name = 'allen'";
        String select2 = "select age from table1";
        String select3 = "select name, age from table1 where age = 2";
        String selectFinal = "SELECT name, age, sum(income) FROM (SELECT gender,name,age,income FROM table1 WHERE age = 2 ) TABLE2 group by name,age";
        String selectSumEasy = "select sum(income) from (select income from table1 where age = 2) tt";


        MyVisitor visitor = new MyVisitor();

        SqlBaseParser parser = ParseUtils.getSqlParser(create);
        LogicalPlan plan = visitor.startVisit(parser);
        Execution execution = Selector.getExecution(plan);
        String result = execution.exec(plan);
        System.out.println(result);

        // insert data
        String tableNm = ((CreateTablePlan)plan).getTableNm();
        insertData(tableNm);

        // filter
        SqlBaseParser parser3 = ParseUtils.getSqlParser(selectSumEasy);
        LogicalPlan plan3 = visitor.startVisit(parser3);
        Execution execution3 = Selector.getExecution(plan3);
        String result3 = execution3.exec(plan3);
        System.out.println(result3);
        // sub filter

        // sum

        // sub sum


    }

    public static void insertData(String tableNm){
        List<String[]> dataList = Arrays.asList(
                new String[]{"male", "allen", "2", "0.1"},
                new String[]{"male", "bob", "2", "0.1"},
                new String[]{"female", "alice", "4", "0.2"},
                new String[]{"male", "jack", "6", "0.3"},
                new String[]{"male", "micheal", "6", "0.3"},
                new String[]{"female", "lily", "8", "0.4"},
                new String[]{"male", "lee", "2", "0.1"},
                new String[]{"male", "roby", "2", "0.1"},
                new String[]{"female", "alexandra", "4", "0.2"},
                new String[]{"male", "putin", "6", "0.3"},
                new String[]{"male", "trump", "6", "0.3"},
                new String[]{"female", "lucy", "8", "0.4"}
        );
        System.out.println(Data.addData(tableNm, dataList));
    }

}
