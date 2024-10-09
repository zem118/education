package com.education.common.constants;



public interface CacheKey {

    String KEY_GENERATOR_BEAN_NAME = "entityParamGenerator";
    /**
     * 字典类型缓存 cacheName
     */
    String SYSTEM_DICT = "system:dict";

    String SYSTEM_DICT_VALUE = "system:dict:value";

    /**
     * 考试相关缓存名
     */
    String EXAM_CACHE = "exam:cache";

    String TEST_PAPER_INFO_CACHE = "test:paper:info:cache";

    String QUESTION_INFO_CACHE = "question:info:cache";

    String EXAM_MONITOR_CACHE_KEY = "exam:monitor:cache:";

    String PAPER_EXAM_NUMBER = "paper_exam:number";
}
