package com.study.portaljdbc;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueryGeneratorTest {

    String query1 = "select * from test.test_table;";
    String query2 = "select id, name from test.test_table;";
    String query3 = "select * from test.test_table where id<8;";
    String query4 = "select id, name from test.test_table where id<8;";

    String query6 = "insert into test.test_table(id, name) values( 3, 'new_name');";
    String query7 = "insert into test.test_table values( 3, 'new_name'); ";
    String query8 = "update test.test_table set name='name3_updated', id=3;";
    String query9 = "update test.test_table set name='name3_updated', id=3 where id=3;";

    String badQuery1 = "select id, name from test.test_table1 where id<8;";

    @Test
    public void generateQueryTest(){
        QueryGenerator queryGenerator = new QueryGenerator();
        System.out.println(queryGenerator.generateQuery(query1));
        System.out.println(queryGenerator.generateQuery(query2));
        System.out.println(queryGenerator.generateQuery(query3));
        System.out.println(queryGenerator.generateQuery(query4));
        System.out.println(queryGenerator.generateQuery(query6));
        System.out.println(queryGenerator.generateQuery(query7));
        System.out.println(queryGenerator.generateQuery(query8));
        System.out.println(queryGenerator.generateQuery(query9));

        System.out.println(queryGenerator.generateQuery(badQuery1));
//
    }

//    @Test
//    public void split(){
//        String resource = "test.test_table";
//        String owner;
//        if(resource.contains(".")){
//            String[] tableAndOwner = resource.split("[.]");
//            owner = tableAndOwner[0];
//            resource= tableAndOwner[1];
//
//            System.out.println(owner);
//            System.out.println(resource);
//        }
//    }


}