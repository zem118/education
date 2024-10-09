package com.education.common.document;

import java.util.List;


public interface DocumentParser<T> {

    default List<T> getDataList() {
        return null;
    }
}
