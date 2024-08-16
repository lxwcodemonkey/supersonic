package com.tencent.supersonic.headless.core.adaptor.db;

import com.google.common.collect.Lists;
import com.tencent.supersonic.common.jsqlparser.SqlReplaceHelper;
import com.tencent.supersonic.headless.api.pojo.DBColumn;
import com.tencent.supersonic.headless.core.pojo.ConnectInfo;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

@Slf4j
public class PostgresqlAdaptor extends DefaultDbAdaptor {

    @Override
    public String functionNameCorrector(String sql) {
        Map<String, String> functionMap = new HashMap<>();
        functionMap.put("MONTH".toLowerCase(), "TO_CHAR");
        functionMap.put("DAY".toLowerCase(), "TO_CHAR");
        functionMap.put("YEAR".toLowerCase(), "TO_CHAR");
        Map<String, UnaryOperator> functionCall = new HashMap<>();
        functionCall.put("MONTH".toLowerCase(), o -> {
            if (Objects.nonNull(o) && o instanceof ExpressionList) {
                ExpressionList expressionList = (ExpressionList) o;
                expressionList.add(new StringValue("MM"));
                return expressionList;
            }
            return o;
        });
        functionCall.put("DAY".toLowerCase(), o -> {
            if (Objects.nonNull(o) && o instanceof ExpressionList) {
                ExpressionList expressionList = (ExpressionList) o;
                expressionList.add(new StringValue("dd"));
                return expressionList;
            }
            return o;
        });
        functionCall.put("YEAR".toLowerCase(), o -> {
            if (Objects.nonNull(o) && o instanceof ExpressionList) {
                ExpressionList expressionList = (ExpressionList) o;
                expressionList.add(new StringValue("YYYY"));
                return expressionList;
            }
            return o;
        });
        return SqlReplaceHelper.replaceFunction(sql, functionMap, functionCall);
    }

    public List<String> getTables(ConnectInfo connectionInfo, String schemaName) throws SQLException {
        List<String> tablesAndViews = Lists.newArrayList();
        DatabaseMetaData metaData = getDatabaseMetaData(connectionInfo);
        try (ResultSet resultSet = metaData.getTables(null, null, null,
                new String[]{"TABLE", "VIEW"})) {
            while (resultSet.next()) {
                String name = resultSet.getString("TABLE_NAME");
                tablesAndViews.add(name);
            }
        } catch (SQLException e) {
            log.error("Failed to get tables and views", e);
        }
        return tablesAndViews;
    }

    public List<DBColumn> getColumns(ConnectInfo connectInfo, String schemaName, String tableName) throws SQLException {
        List<DBColumn> dbColumns = Lists.newArrayList();
        DatabaseMetaData metaData = getDatabaseMetaData(connectInfo);
        ResultSet columns = metaData.getColumns(null, null, tableName, null);
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String dataType = columns.getString("TYPE_NAME");
            String remarks = columns.getString("REMARKS");
            dbColumns.add(new DBColumn(columnName, dataType, remarks));
        }
        return dbColumns;
    }

}
