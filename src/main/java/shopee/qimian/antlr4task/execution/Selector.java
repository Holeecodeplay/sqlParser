package shopee.qimian.antlr4task.execution;

import shopee.qimian.antlr4task.node.LogicalPlan;
import shopee.qimian.antlr4task.planer.CreateTablePlan;
import shopee.qimian.antlr4task.planer.QueryPlan;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qimian.huang 黄奇冕
 * @Description 根据执行计划选择执行器，静态单例
 */

public class Selector {
    private static final Map<String, Execution> selectorMap = new HashMap<String, Execution>(){{
        put("CREATE", new CreateExecution());
        put("AGG", new AggExecution());
        put("FILTER", new FilterExecution());
        put("DEFAULT", new DefaultExecution());
    }};

    public static Execution getExecution(LogicalPlan plan){
        if(plan instanceof CreateTablePlan){
            return selectorMap.get("CREATE");
        } else if(plan instanceof QueryPlan){
            if(((QueryPlan) plan).getAggFlag()){
                return selectorMap.get("AGG");
            } else {
                return selectorMap.get("FILTER");
            }
        } else {
            System.out.println("本次 sql 类型不在处理范围内");
            return selectorMap.get("DEFAULT");
        }
    }


}
