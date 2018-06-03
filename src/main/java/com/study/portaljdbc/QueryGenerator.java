package com.study.portaljdbc;

import java.util.StringJoiner;

public class QueryGenerator {
    private String queryType="";
    private String columns="";
    private String resource="";
    private String owner="";
    private String values = "";
    private String filter = "";

    public String generateQuery(String query) {
        int indexEnd = query.indexOf(";");
        query = (indexEnd>0)? query.substring(0, indexEnd).trim() : query.trim();
        String tableAndOwner;

        if(query.toUpperCase().startsWith("SELECT")){
            queryType="SELECT";
            int indexFrom = query.toUpperCase().indexOf("FROM");
            if(indexFrom>0) {
                columns = columnsToList(query.substring(6, indexFrom).trim());
                String temp = query.substring(indexFrom+4).trim();
                int indexWhere = temp.toUpperCase().indexOf("WHERE");
                if (indexWhere>0){
                    tableAndOwner =temp.substring(0, indexWhere).trim();
                    filter = temp.substring(indexWhere+ 5).trim();
                } else {
                    tableAndOwner = query.substring(indexFrom + 4).trim();
                }
                setTableAndOwner(tableAndOwner);
            } else  throw new RuntimeException("Select statement should contains FROM clause");
        }else if (query.toUpperCase().startsWith("INSERT INTO")){
            queryType="INSERT";
            int indexValue = query.toUpperCase().indexOf("VALUES");
            String tableAndColumns = query.substring(12,indexValue-1 ).trim();
            int indexColStart = tableAndColumns.indexOf("(");
            int indexColEnd = tableAndColumns.indexOf(")");
            if((indexColStart>0)&(indexColEnd>0)){
                tableAndOwner = tableAndColumns.substring(0, indexColStart);
                columns = columnsToList(tableAndColumns.substring(indexColStart+1,indexColEnd));
            }else{
                tableAndOwner = tableAndColumns;
            }
            setTableAndOwner(tableAndOwner);
            values = query.substring(indexValue+6 );
            int indexValStart = values.indexOf("(");
            int indexValEnd = values.indexOf(")");
            values = values.substring(indexValStart+1, indexValEnd).trim();
        }else if (query.toUpperCase().startsWith("UPDATE")){
            queryType="UPDATE";

            int indexSet = query.toUpperCase().indexOf("SET");
            if (indexSet<0) throw new RuntimeException("Update statement should contains SET clause");
            tableAndOwner =query.substring(6, indexSet).trim();
            setTableAndOwner(tableAndOwner);

            String temp = query.substring(indexSet+3).trim();
            int indexWhere = temp.toUpperCase().indexOf("WHERE");
            String sets="";
            if (indexWhere>0){
                sets = temp.substring(0, indexWhere).trim();
                filter = temp.substring(indexWhere+ 5).trim();
            } else {
                sets = temp;
            }
            //add split rows and columns
            String[] setsArray = sets.split(",");
            StringJoiner valuesArray = new StringJoiner("\", \"");
            StringJoiner columnsArray = new StringJoiner("\", \"");
            for (String set : setsArray){
                int indexEqual = set.indexOf("=");
                columnsArray.add(set.substring(0, indexEqual).trim());
                valuesArray.add(set.substring(indexEqual+1).trim());
            }
            values = valuesArray.toString();
            columns = columnsArray.toString();

        }else if (query.toUpperCase().startsWith("DELETE FROM")){
            queryType="DELETE";
            String temp = query.substring(11).trim();
            int indexWhere = temp.toUpperCase().indexOf("WHERE");
            if (indexWhere>0){
                tableAndOwner =temp.substring(0, indexWhere).trim();
                filter = temp.substring(indexWhere+ 5).trim();
            } else {
                tableAndOwner = temp;
            }
            setTableAndOwner(tableAndOwner);

        }else if (query.toUpperCase().startsWith("CREATE TABLE")){
            queryType="CREATE";

        }else {
            throw new RuntimeException("Input statement is not valid");
        }

        StringBuilder request = new StringBuilder();
        request.append("{");
        request.append("\"queryType\": \"");
        request.append(queryType);
        request.append("\", ");
        request.append("\"columns\": [\"");
        request.append(columns);
        request.append("\"], ");
        request.append("\"owner\": \"");
        request.append(owner);
        request.append("\", ");
        request.append("\"resource\": \"");
        request.append(resource);
        request.append("\", ");
        request.append("\"rows\": [\"");
        request.append(values);
        request.append("\"], ");
        request.append("\"filter\": [\"");
        request.append(filter);
        request.append("\"]");
        request.append("}");
        return request.toString();
    }

    private void setTableAndOwner(String tableAndOwner) {
        if(tableAndOwner.contains(".")){
            String [] array = tableAndOwner.split("[.]");
            owner = array[0];
            resource= array[1];
        }
    }

    String columnsToList(String list) {
        if (list.equalsIgnoreCase("*")){
            return list;
        }
        String[] setsArray = list.split(",");
        StringJoiner columnsArray = new StringJoiner("\", \"");
        for (String set : setsArray){
            columnsArray.add(set.trim());
        }
        return columnsArray.toString();
    }
}
