package shopee.qimian.antlr4task.execution;

import shopee.qimian.antlr4task.db.metadata.Table;
import shopee.qimian.antlr4task.node.LogicalPlan;
import shopee.qimian.antlr4task.planer.CreateTablePlan;

import java.util.stream.Stream;

/**
 * @author qimian.huang 黄奇冕
 * @Description 建表操作
 */
public class CreateExecution implements Execution{

    @Override
    public String exec(LogicalPlan logicalPlan) {
        CreateTablePlan plan = (CreateTablePlan)logicalPlan;
        if(!Table.containsTable(plan.getTableNm())){
            Table.addTable(plan.getTableNm(), plan.getFieldList());
            return Table.showTable(plan.getTableNm());
        } else {
            return String.format("table [%s] is already existed", plan.getTableNm());
        }

    }

    @Override
    public Stream<String[]> doexec(LogicalPlan plan, Stream<String[]> dataList) {
        return null;
    }

}
