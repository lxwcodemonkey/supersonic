package com.tencent.supersonic.headless.core.adaptor.db;

import com.tencent.supersonic.headless.api.pojo.enums.EngineType;

import java.util.HashMap;
import java.util.Map;


public class DbAdaptorFactory {

    private static Map<String, DbAdaptor> dbAdaptorMap;

    static {
        dbAdaptorMap = new HashMap<>();
        dbAdaptorMap.put(EngineType.CLICKHOUSE.getName(), new ClickHouseAdaptor());
        dbAdaptorMap.put(EngineType.MYSQL.getName(), new DefaultDbAdaptor());
        dbAdaptorMap.put(EngineType.H2.getName(), new DefaultDbAdaptor());
        dbAdaptorMap.put(EngineType.POSTGRESQL.getName(), new PostgresqlAdaptor());
        dbAdaptorMap.put(EngineType.OTHER.getName(), new DefaultDbAdaptor());
    }

    public static DbAdaptor getEngineAdaptor(String engineType) {
        return dbAdaptorMap.get(engineType);
    }

}
