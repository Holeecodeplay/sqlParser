package shopee.qimian.antlr4task.execution;

import shopee.qimian.antlr4task.node.LogicalPlan;

import java.util.stream.Stream;

/**
 * @author qimian.huang 黄奇冕
 * @Description 默认返回异常
 */

public class DefaultExecution implements Execution{
    @Override
    public String exec(LogicalPlan plan) {
        return "Execution is not existed !!!";
    }

    @Override
    public Stream<String[]> doexec(LogicalPlan plan, Stream<String[]> dataList) {
        return null;
    }
}
