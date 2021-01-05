# sql parser
## 描述
1.基于 Java8，利用 Antlr4 实现对 sql 的解析并生成解析树  
2.基于解析树生成执行计划  
3.基于执行计划，对 Java 内存中数据进行操作，对用户透明，并实现部分 sql 执行  
4.Java 内存数据为行式存储，数据均为 String 类型，使用时进行转换
## 运行环境
1.Java 8  
2.Antlr4.9
## 功能
1.实现 where = xxx 的等值单条件筛选查询  
2.支持嵌套子查询  
3.实现 sum 聚合算子
## 方案设计
##### 整体流程： Input(sql) --> Parser --> Execution --> Print  
1.Input：即接收 input 的 sql 入参  
2.Parser：接收 sql 后，经过 parser 解析生成语法树，并通过解析语法树，将关键的 sql 元素保存进入对象，形成执行计划 plan ；若包含嵌套查询则利用链表将父子 plan 关联  
3.Execution：根据执行计划类型，选择不同的执行器 Execution，并对嵌套的父子查询进行拆解，根据 dfs 最底层的子查询需要优先执行，并执行数据处理  
4.Print：对执行结果进行规则化输出，比如字段展示和筛选等等
## 方案细节
1.执行计划分多种类型，只有 CreateTablePlan 和 QueryPlan，但都继承自同一个名为 Node 的抽象类（以方便增加子plan指针）并实现同一个名为 LogicalPlan的接口，以方便类型擦除。
2.根据执行计划类型，选择不同的执行器；这里执行器同理，也是实现了同一个名为 Execution的接口方便类型擦除  
3.全部的执行流程均做了包装，只需调用 StartFactory.start 即可一条龙：解析+生成执行计划+选择执行器+执行+输出
## 运行示例
运行 Main 即可，执行 sql 参考如下：  
1.Create: create table table1 (gender string, name string, age int, income double)  
2.Query-Filter: select name,income from table1 where name = 'one'  
3.Query-Filter-SubQuery: select name,income from (select name,age,income from table1) t where name = 'one'  
4.Sum-WithoutGroupBy-Filter-SubQuery: select sum(income) from (select income from table1 where age = 2) tt
## 运行结果
1.能正常建立表，同时存储数据和元数据等信息  
2.实现了where = xxx的等值筛选查询，同时支持多层嵌套查询，在 50w数据量下，基于内存和并行流，处理时间为 90ms(取决于select字段多少)  
3.实现了 sum 语法操作，在 50w数据量下，基于内存和并行流，处理时间为 20ms  
## 参考资料
1.presto 源码解析：https://blog.csdn.net/LW_Moving_Bricks/article/details/88984561?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.control  
2.presto SQL引擎解析：https://zhuanlan.zhihu.com/p/293775390  
3.presto 官方源码：https://github.com/trinodb/trino
