package com.tencent.supersonic.chat.responder.parse;

import com.tencent.supersonic.chat.api.pojo.QueryContext;
import com.tencent.supersonic.chat.api.pojo.SemanticParseInfo;
import com.tencent.supersonic.chat.api.pojo.request.QueryReq;
import com.tencent.supersonic.chat.api.pojo.response.EntityInfo;
import com.tencent.supersonic.chat.api.pojo.response.ParseResp;
import com.tencent.supersonic.chat.query.QueryManager;
import com.tencent.supersonic.chat.query.llm.dsl.DslQuery;
import com.tencent.supersonic.chat.service.SemanticService;
import com.tencent.supersonic.common.util.ContextUtils;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
@Slf4j
public class EntityInfoParseResponder implements ParseResponder {

    @Override
    public void fillResponse(ParseResp parseResp, QueryContext queryContext) {
        List<SemanticParseInfo> selectedParses = parseResp.getSelectedParses();
        if (CollectionUtils.isEmpty(selectedParses)) {
            return;
        }
        QueryReq queryReq = queryContext.getRequest();
        selectedParses.forEach(parseInfo -> {
            if (QueryManager.isPluginQuery(parseInfo.getQueryMode())
                    && !parseInfo.getQueryMode().equals(DslQuery.QUERY_MODE)) {
                return;
            }
            //1. set entity info
            SemanticService semanticService = ContextUtils.getBean(SemanticService.class);
            EntityInfo entityInfo = semanticService.getEntityInfo(parseInfo, queryReq.getUser());
            if (QueryManager.isEntityQuery(parseInfo.getQueryMode())
                    || QueryManager.isMetricQuery(parseInfo.getQueryMode())) {
                parseInfo.setEntityInfo(entityInfo);
            }
            //2. set native value
            entityInfo = semanticService.getEntityInfo(parseInfo.getModelId());
            log.info("entityInfo:{}", entityInfo);
            String primaryEntityBizName = semanticService.getPrimaryEntityBizName(entityInfo);
            if (StringUtils.isNotEmpty(primaryEntityBizName)) {
                //if exist primaryEntityBizName in parseInfo's dimensions, set nativeQuery to true
                boolean existPrimaryEntityBizName = parseInfo.getDimensions().stream()
                        .anyMatch(schemaElement -> primaryEntityBizName.equalsIgnoreCase(schemaElement.getBizName()));
                parseInfo.setNativeQuery(existPrimaryEntityBizName);
            }
        });
    }
}
