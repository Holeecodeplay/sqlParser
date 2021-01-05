package shopee.qimian.antlr4task.execution;

import org.springframework.util.CollectionUtils;
import shopee.qimian.antlr4task.db.metadata.Data;
import shopee.qimian.antlr4task.db.metadata.Table;
import shopee.qimian.antlr4task.node.ExpressionNode;
import shopee.qimian.antlr4task.node.LogicalPlan;
import shopee.qimian.antlr4task.planer.QueryPlan;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qimian.huang 黄奇冕
 * @Description 聚合操作
 */

public class AggExecution implements Execution{

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
        if(CollectionUtils.isEmpty(plan.getGroupBy())){
            return sumWithOutGroupBy(plan, dataStream);
        } else {
            return sumWithGroupBy(plan, dataStream);
        }
    }

    // no group by
    private Stream<String[]> sumWithOutGroupBy(QueryPlan plan,  Stream<String[]> dataStream){
        ExpressionNode expressionNode = plan.getExpressionNodeList().get(0);
        if("SUM".equalsIgnoreCase(expressionNode.getActionTyp())){
            int pos = Table.getFieldPos(plan.getTableNm(), expressionNode.getField());
            String type = Table.getFieldTyp(plan.getTableNm(), expressionNode.getField());

            if("int".equalsIgnoreCase(type) || "long".equalsIgnoreCase(type)){
                BigInteger sum = new BigInteger("0");
                if(-1 != pos){
                    List<String[]> data = dataStream.collect(Collectors.toList());
                    if(data.isEmpty()){
                        data = Data.getData(plan.getTableNm());
                    }
                    for(String[] line : data){
                        sum = sum.add(new BigInteger(line[pos]));
                    }
                }
                String[] temp = new String[]{sum.toString()};
                List<String[]> dataList = new ArrayList<>();
                dataList.add(temp);
                return dataList.stream();
            } else if("float".equalsIgnoreCase(type) || "double".equalsIgnoreCase(type)){
                BigDecimal sum = new BigDecimal(0L);
                if(-1 != pos){
                    List<String[]> data = dataStream.collect(Collectors.toList());
                    if(data.isEmpty()){
                        data = Data.getData(plan.getTableNm());
                    }
                    for(String[] line : data){
                        sum = sum.add(new BigDecimal(line[pos]));
                    }
                }
                String[] temp = new String[]{sum.toString()};
                List<String[]> dataList = new ArrayList<>();
                dataList.add(temp);
                return dataList.stream();
            } else {
                System.out.println("当前字段类型为非数字，不支持聚合操作");
                return null;
            }

        }else{
            System.out.printf("不支持当前聚合操作 [%s]", expressionNode.getActionTyp());
            return null;
        }
    }

    // sum with group by
    private Stream<String[]> sumWithGroupBy(QueryPlan plan,  Stream<String[]> dataStream){
        return null;
    }

    public String formatResult(LogicalPlan logicalPlan, Stream<String[]> dataStream){
        List<String> resultList = dataStream.map(line -> {
            return String.join(",",line);
        }).collect(Collectors.toList());
        return String.join("\n", resultList);
    }


}
