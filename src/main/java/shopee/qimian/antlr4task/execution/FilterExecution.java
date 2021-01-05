package shopee.qimian.antlr4task.execution;

import shopee.qimian.antlr4task.db.metadata.Data;
import shopee.qimian.antlr4task.db.metadata.Table;
import shopee.qimian.antlr4task.node.ExpressionNode;
import shopee.qimian.antlr4task.node.LogicalPlan;
import shopee.qimian.antlr4task.planer.QueryPlan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qimian.huang 黄奇冕
 * @Description 过滤和普通查询操作
 */

public class FilterExecution implements Execution{

    @Override
    public String exec(LogicalPlan logicalPlan) {
        Stream<String[]> dataStream = null;
        List<QueryPlan> planList = getPlanList(logicalPlan);
        for(QueryPlan plan : planList){
            dataStream = Selector.getExecution(plan).doexec(plan, dataStream);
        }
        return formatResult(logicalPlan, dataStream);
    }

    @Override
    public Stream<String[]> doexec(LogicalPlan logicalPlan, Stream<String[]> dataStream) {
        QueryPlan plan = (QueryPlan)logicalPlan;

        // no where so no filter
        if (null == plan.getConditionNode()) {
            List<String[]> aa = Data.getData(plan.getTableNm().toUpperCase());
            dataStream = Data.getData(plan.getTableNm().toUpperCase()).parallelStream();
            return dataStream;
        }
        // 仅仅对 where = xxx 的单条件等值过滤，其他不做处理
        int pos = -1;
        if ("=".equals(plan.getConditionNode().getCondition())) {
            pos = Table.getFieldPos(plan.getTableNm().toUpperCase(), plan.getConditionNode().getField().toUpperCase());
        }
        // 表结构中存在字段则进行过滤，不存在则返回空
        if (-1 == pos) {
            System.out.println(String.format("字段 [%s] 未在表 [%s] 中", plan.getConditionNode().getField(), plan.getTableNm()));
            dataStream = new LinkedList<String[]>().stream();
        } else {
            String fieldValue = plan.getConditionNode().getValue();
            final int finalPos = pos;
            dataStream = Data.getData(plan.getTableNm().toUpperCase()).parallelStream()
                    .filter(data -> fieldValue.equalsIgnoreCase(String.valueOf(data[finalPos])));
        }
        return dataStream;
    }

    public String formatResult(LogicalPlan logicalPlan, Stream<String[]> dataStream){
        QueryPlan plan = (QueryPlan)logicalPlan;
        List<Integer> posList = new LinkedList<>(); //  存放 select 所需字段的顺序
        List<String> resultList = new LinkedList<>();   // 【逗号拼接的数据】
        List<String> fieldList = plan.getExpressionNodeList().stream().map(ExpressionNode::getField).collect(Collectors.toCollection(LinkedList::new));
        fieldList.forEach(field -> {
            int pos = Table.getFieldPos(plan.getTableNm(),field);
            if(-1 != pos){
                posList.add(pos);
            }else{
                System.out.printf("%s 字段不存在%n", field);
            }
        });
        resultList = dataStream.map(line -> {
            List<String> newLine = new ArrayList<>(posList.size());
            posList.forEach(pos -> {
                newLine.add(line[pos]);
            });
            return String.join(",",newLine);
        }).collect(Collectors.toCollection(LinkedList::new));
        return String.join("\n", resultList);
    }

}
