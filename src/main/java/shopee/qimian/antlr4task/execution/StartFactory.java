package shopee.qimian.antlr4task.execution;

import shopee.qimian.antlr4task.node.LogicalPlan;
import shopee.qimian.antlr4task.parser.MyVisitor;
import shopee.qimian.antlr4task.parser.SqlBaseParser;
import shopee.qimian.antlr4task.util.ParseUtils;

/**
 * @author qimian.huang 黄奇冕
 * @Description sql 执行汇总工厂
 */

public class StartFactory {
    private static final MyVisitor visitor = new MyVisitor();

    public static String start(String sql){
        SqlBaseParser parser = ParseUtils.getSqlParser(sql);
        LogicalPlan plan = visitor.startVisit(parser);
        Execution execution = Selector.getExecution(plan);
        return execution.exec(plan);
    }
}
