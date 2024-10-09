package com.tencent.supersonic.headless.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class ModelSchema {

    private String name;

    private String bizName;

    private String description;

    private List<FieldSchema> filedSchemas;

    @JsonIgnore
    public FieldSchema getField(String name) {
        if (filedSchemas == null) {
            return null;
        }
        for (FieldSchema filedSchema : filedSchemas) {
            if (filedSchema.getColumnName().equals(name))
                return filedSchema;
        }
        return null;
    }
}
