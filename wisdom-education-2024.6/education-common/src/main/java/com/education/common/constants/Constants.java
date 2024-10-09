package com.education.common.constants;



public final class Constants {

    public static final String EDUCATION_ADMIN_SECRET_KEY = "education_admin";
    public static final String EDUCATION_FRONT_SECRET_KEY = "education_front";
    public static final String USER_SESSION_ID = "user_id_key";
    public static final String ONE_MINUTE_CACHE = "one.minute";
    public static final String ONE_HOUR_CACHE = "one.hour";

    public static final String USER_INFO_CACHE = "user.cache";
    public static final String SESSION_NAME = "u_id_key";
    public static final String DEFAULT_SESSION_ID = "JSESSIONID";
    public static final int SESSION_TIME_OUT = 24 * 60 * 60 * 5;
    public static final int SESSION_TIME_OUT_MILLISECONDS = SESSION_TIME_OUT * 1000;

    public static final int TWO_HOUR_SECOND = 60 * 60 * 2;
    public static final int TWO_HOUR_SECOND_MILLISECONDS = TWO_HOUR_SECOND * 1000;

  //  public static final long SESSION_TIME_OUT_SECOND = SESSION_TIME_OUT;
    public static final int ONE_DAY = 24 * 60;

    // 默认会话结束时失效
    public static final int DEFAULT_SESSION_TIME_OUT = -1;
    public static final String DEFAULT_STUDENT_NAME = "体验账号";

    public static final String IMAGE_CODE = "image_code";

    public static final String[] IMAGE_ALIAS = new String[] {"head_img", "image"};

    // 90isZJE3LdxOnG9whgEn-nVk9kphyev2ZLGpZprGTvw
    //FVQrzhjO78QCJwhqUxOII2sL-629JfdNHEeRlB8Dq8k
    // 学员登录或者学员学习结束模板消息通知
    public static final String STUDY_STATUS_TEMPLATE_MESSAGE_ID = "rl8tW6VHQkkeRMxq54TAF0byWGtleV6Z8Nl6Is9DaCc";
    // 学员成绩模板消息通知
    public static final String STUDY_SOURCE_TEMPLATE_MESSAGE_ID = "ENvpai6wUX3pjOoIBvltn7RUMzdrlsgZmGZ8u18kByg";

    public static final Integer MIN_PASS_MARK = 60; // 及格分数


    public static final double PASS_MARK_RATE = 0.6; // 及格比例
    public static final double NICE_MARK_RATE = 0.8; // 优秀比例

    public static final String SESSION_KEY = "user.session.cache";

    public static final String PAPER_INFO_TEMPLATE = "/template/paperInfoTemplate.html";
}
