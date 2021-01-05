package shopee.qimian.antlr4task.execution;

import shopee.qimian.antlr4task.node.LogicalPlan;
import shopee.qimian.antlr4task.planer.QueryPlan;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

public interface Execution {
    String exec(LogicalPlan plan);
    Stream<String[]> doexec(LogicalPlan plan, Stream<String[]> dataList);

    // 将链表转化为串行计划
    default List<QueryPlan> getPlanList(LogicalPlan logicalPlan){
        Stack<QueryPlan> stack = new Stack<>();
        List<QueryPlan> planList = new LinkedList<>();
        String tableNm = "";
        QueryPlan plan = (QueryPlan)logicalPlan;
        while(plan.hasChild()){
            stack.push(plan);
            plan = (QueryPlan)plan.getChild();
        }
        stack.push(plan);
        int size = stack.size();
        for(int i=0; i<size ; i++){
            QueryPlan tempPlan = stack.pop();
            if(0 == i){
                tableNm = tempPlan.getTableNm();
            } else {
                tempPlan.setTableNm(tableNm);
            }
            planList.add(tempPlan);
        }
        return planList;
    }
}
