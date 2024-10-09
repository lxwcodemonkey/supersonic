package com.tencent.supersonic.builder;

import com.tencent.supersonic.BaseApplication;
import com.tencent.supersonic.headless.api.pojo.ModelSchema;
import com.tencent.supersonic.headless.api.pojo.enums.FieldType;
import com.tencent.supersonic.headless.api.pojo.request.ModelSchemaReq;
import com.tencent.supersonic.headless.server.service.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ModelIntelligentBuilderTest extends BaseApplication {

    @Autowired
    private ModelService modelService;

    @Test
    public void testBuildS2PVUVModel() throws SQLException {
        String sql =
                "SELECT imp_date, user_name, page, 1 as pv, user_name as uv FROM s2_pv_uv_statis";
        ModelSchemaReq modelSchemaReq = new ModelSchemaReq();
        modelSchemaReq.setSql(sql);
        modelSchemaReq.setDatabaseId(1L);
        modelSchemaReq.setBuildByLLM(true);
        ModelSchema modelSchema = modelService.buildModelSchema(modelSchemaReq);
        Assertions.assertEquals(FieldType.data_time,
                modelSchema.getField("imp_date").getFiledType());
        Assertions.assertEquals(FieldType.dimension,
                modelSchema.getField("user_name").getFiledType());
        Assertions.assertEquals(FieldType.dimension, modelSchema.getField("page").getFiledType());
        Assertions.assertEquals(FieldType.measure, modelSchema.getField("pv").getFiledType());
        Assertions.assertEquals(FieldType.measure, modelSchema.getField("uv").getFiledType());
    }
}
