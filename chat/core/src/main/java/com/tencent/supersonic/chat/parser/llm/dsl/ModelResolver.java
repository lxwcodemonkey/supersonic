package com.tencent.supersonic.chat.parser.llm.dsl;


import com.tencent.supersonic.chat.api.pojo.ChatContext;
import com.tencent.supersonic.chat.api.pojo.QueryContext;
import java.util.Set;

public interface ModelResolver {

    Long resolve(QueryContext queryContext, ChatContext chatCtx, Set<Long> restrictiveModels);

}
