package com.eveb.saasops.batch.common;

/**
 * Created by William on 2018/1/31.
 */
public class SchemaCode {

    public SchemaCode() {}

    private final String schemaPrex ="/*!mycat:schema = saasops_";
    private final String schemaLast =" */";

    private String schemaCode ;

    public SchemaCode(String schemaCode) {
        this.schemaCode = schemaPrex +schemaCode+schemaLast;
    }

    public String getSchemaCode() {
        return schemaCode;
    }

    public void setSchemaCode(String schemaCode) {
        this.schemaCode = schemaPrex +schemaCode+schemaLast;
    }
}
