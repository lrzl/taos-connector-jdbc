package com.taosdata.jdbc.tmq;

import java.beans.IntrospectionException;
import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface Deserializer<T> extends Closeable {

    default void configure(Map<?, ?> configs) {
        // intentionally left blank
    }

    T deserialize(ResultSet data) throws InstantiationException, IllegalAccessException, SQLException, IntrospectionException, InvocationTargetException;

    @Override
    default void close() {
    }
}
