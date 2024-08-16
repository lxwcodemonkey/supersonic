package com.tencent.supersonic.headless.core.adaptor.db;

import com.tencent.supersonic.common.jsqlparser.SqlReplaceHelper;

import java.util.HashMap;
import java.util.Map;

public class ClickHouseAdaptor extends DefaultDbAdaptor {

    @Override
    public String functionNameCorrector(String sql) {
        Map<String, String> functionMap = new HashMap<>();
        functionMap.put("MONTH".toLowerCase(), "toMonth");
        functionMap.put("DAY".toLowerCase(), "toDayOfMonth");
        functionMap.put("YEAR".toLowerCase(), "toYear");
        return SqlReplaceHelper.replaceFunction(sql, functionMap);
    }

}
