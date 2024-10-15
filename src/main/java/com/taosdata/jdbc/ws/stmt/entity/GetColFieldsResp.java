package com.taosdata.jdbc.ws.stmt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.taosdata.jdbc.utils.UInt64Deserializer;
import com.taosdata.jdbc.ws.entity.CommonResp;

import java.util.List;

public class GetColFieldsResp extends CommonResp {
    @JsonProperty("stmt_id")
    @JsonDeserialize(using = UInt64Deserializer.class)
    private long stmtId;
    private List<Field> fields;
    public long getStmtId() {
        return stmtId;
    }

    public void setStmtId(long stmtId) {
        this.stmtId = stmtId;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
