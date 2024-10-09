package com.tencent.supersonic.headless.server.builder;

import com.tencent.supersonic.common.util.JsonUtil;
import com.tencent.supersonic.headless.api.pojo.DbSchema;
import com.tencent.supersonic.headless.api.pojo.ModelSchema;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ModelIntelligentBuilder extends IntelligentBuilder<DbSchema, ModelSchema> {

    public static final String INSTRUCTION = ""
            + "Role: It is assumed that you are an experienced data analyst, have extensive modeling experience, "
            + "and are very familiar with related concepts of data analysis and data modeling."
            + "\nJob: You will be provided with a db table structure, including db table name, field name, "
            + "field type, and field comments. You need to combine the given information for data modeling."
            + "\nTask: " + "\nYou need to use the existing database table field information :"
            + "\n1. Generate name and description for the model, ps: bizName is English name, name is Chinese name"
            + "\n2. Generate a Chinese name for the field and Divide the filed into the following five types"
            + "\n   primary_key: the concept of primary key in a database, it is the unique identifier of a row of records"
            + "\n   foreign_key: the concept of foreign key in database, its value comes from the primary key in another table"
            + "\n   data_time: the time when data is generated in the data warehouse"
            + "\n   dimension: usually a string type, used to group and filter data"
            + "\n   measure: usually a numeric type, used to express the degree of data from a certain evaluation perspective"
            + "\nDBSchema:{{DBSchema}}" + "\nexemplar:{{exemplar}}";

    interface ModelSchemaExtractor {
        ModelSchema generateModelSchema(String text);
    }

    @Override
    public ModelSchema build(DbSchema dbSchema) {
        ChatLanguageModel chatModel = getChatModel();
        ModelSchemaExtractor extractor = AiServices.create(ModelSchemaExtractor.class, chatModel);
        Prompt prompt = generatePrompt(dbSchema);
        return extractor.generateModelSchema(prompt.toUserMessage().singleText());
    }

    private Prompt generatePrompt(DbSchema dbSchema) {
        Map<String, Object> variable = new HashMap<>();
        variable.put("exemplar", loadExemplars());
        variable.put("DBSchema", JsonUtil.toString(dbSchema));
        return PromptTemplate.from(INSTRUCTION).apply(variable);
    }

    private String loadExemplars() {

        return "";
    }
}
