package cn.ecnu.eblog.common.constant;

public class CacheConstant {
    public static final String CACHE_MANAGER = "redisCacheManager";
    public static final Integer COMMON_MIN_TTL = 30000;  // 30s
    public static final Integer COMMON_MAX_TTL = 120000;  // 120s
    public static final String USER_INFO = "userInfo";
    public static final Integer USER_INFO_MIN_TTL = 900000;  // 15分钟
    public static final Integer USER_INFO_MAX_TTL = 1200000;  // 20分钟
    public static final String TAG = "tag";
    public static final Integer TAG_MIN_TTL = 43200000;  // 12小时
    public static final Integer TAG_MAX_TTL = 86400000;  // 一天
    public static final String CATEGORY = "category";
    public static final Integer CATEGORY_MIN_TTL = 43200000;  // 12小时
    public static final Integer CATEGORY_MAX_TTL = 86400000;  // 一天
    public static final String ARTICLE_DETAIL = "articleDetail";
    public static final Integer ARTICLE_DETAIL_MIN_TTL = 1200000;  // 20分钟
    public static final Integer ARTICLE_DETAIL_MAX_TTL = 1800000;  // 30分钟
    public static final String ARTICLE_TAG = "articleTag";
    public static final Integer ARTICLE_TAG_MIN_TTL = 1200000;  // 20分钟
    public static final Integer ARTICLE_TAG_MAX_TTL = 1800000;  // 30分钟
    public static final String ARTICLE_PAGE = "articlePage";
    public static final Integer ARTICLE_PAGE_MIN_TTL = 300000;  // 5分钟
    public static final Integer ARTICLE_PAGE_MAX_TTL = 600000;  // 10分钟
    public static final String COMMENT_PAGE = "commentPage";
    public static final Integer COMMENT_PAGE_MIN_TTL = 180000;  // 3分钟
    public static final Integer COMMENT_PAGE_MAX_TTL = 300000;  // 5分钟
    public static final String SECOND_COMMENT_PAGE = "secondCommentPage";
    public static final Integer SECOND_COMMENT_PAGE_MIN_TTL = 180000;  // 3分钟
    public static final Integer SECOND_COMMENT_PAGE_MAX_TTL = 300000;  // 5分钟
    public static final String LIKED = "liked";
    public static final Integer LIKED_MIN_TTL = 180000;  // 3分钟
    public static final Integer LIKED_MAX_TTL = 300000;  // 5分钟
    public static final String LIKE_COUNT = "likeCount";
    public static final Integer LIKE_COUNT_MIN_TTL = 1200000;  // 20分钟
    public static final Integer LIKE_COUNT_MAX_TTL = 1800000;  // 30分钟

}
