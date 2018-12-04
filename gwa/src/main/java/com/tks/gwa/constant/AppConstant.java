package com.tks.gwa.constant;

public class AppConstant {

    public static final String[] listModelCrawlUrls = {"https://www.1999.co.jp/eng/list/680/0/1",
            "https://www.1999.co.jp/eng/list/681/0/1"/*,
            "https://www.1999.co.jp/eng/list/678/0/1",
            "https://www.1999.co.jp/eng/list/679/0/1"*/};

    public static final String URL_GUNDAM_HOME_PAGE_CRAWL = "https://www.1999.co.jp";

    public static final String URL_GUNDAM_MODELS_PAGE_BEGIN_SIGN = "list_kensu08";

    public static final String URL_GUNDAM_MODELS_PAGE_END_SIGN = "list_kensu03";

    public static final String URL_GUNDAM_MODELS_BEGIN_SIGN = "masterBody_pnlList";

    public static final String URL_GUNDAM_MODELS_END_SIGN = "list_kensu00";

    public static final String URL_GUNDAM_MODEL_DETAIL_BEGIN_SIGN = "masterContent";

    public static final String URL_GUNDAM_MODEL_DETAIL_END_SIGN = "item_right";

    public static final String URL_GUNDAM_MODEL_IMAGE_BEGIN_SIGN = "imgAll";

    public static final String URL_GUNDAM_MODEL_IMAGE_END_SIGN = "margin-top:30px;";

    public static final String URL_ARTICLE_CONTEXT_PATH = "https://www.gunpla101.com";

    public static final String URL_ARTICLE_CRAWL = "https://www.gunpla101.com/?s=";

    public static final String URL_ARTICLE_CRAWL_BEGIN_SIGN = "w-grid-list";

    public static final String URL_ARTICLE_CRAWL_END_SIGN = "navigation pagination";

    public static final String URL_ARTICLE_PAGE_BEGIN_SIGN = "navigation pagination";

    public static final String URL_ARTICLE_PAGE_END_SIGN = "next page-numbers";

    public static final String URL_ARTICLE_DETAIL_BEGIN_SIGN = "mainContentOfPage";

    public static final String URL_ARTICLE_DETAIL_END_SIGN = "conv-place conv-place_after_post";

    public static final String APPROVED_STATUS = "approved";
    public static final String DECLINED_STATUS = "declined";
    public static final String PENDING_STATUS = "pending";
    public static final String CANCELLED_STATUS = "cancelled";
    public static final String SUCCEED_STATUS = "succeed";

    public static final String USER_PENDING = "userpending";
    public static final String CRAWL_PENDING = "crawlpending";

    //TRADE POST CONSTANT
    public class TRADEPOST {
        public static final String TYPE_SELL = "sell";
        public static final String TYPE_BUY = "buy";
        public static final int TYPE_SELL_INT = 1;
        public static final int TYPE_BUY_INT = 2;
        public static final String CONDITION_NEW = "new";
        public static final String CONDITION_USED = "used";
        public static final int CONDITION_NEW_INT = 1;
        public static final int CONDITION_USED_INT = 2;
        public static final String NEGOTIABLE_ON = "on";
        public static final String NEGOTIABLE_OFF = "off";
        public static final int NEGOTIABLE_ON_INT = 1;
        public static final int NEGOTIABLE_OFF_INT = 0;
        public static final int MAX_POST_PER_PAGE = 6;
        public static final int SORT_DATE_DESC = 1;
        public static final int SORT_DATE_ASC = 2;
        public static final int SORT_PRICE_DESC = 4;
        public static final int SORT_PRICE_ASC = 3;
        public static final int MAX_REQUEST_PER_PAGE =10;
        public static final int FEEDBACK_TYPE_OWNER_TO_TRADER = 1;
        public static final int FEEDBACK_TYPE_TRADER_TO_OWNER = 2;

        public static final long AUTO_DECLINE_ORDER_TIME = 2*24*60*60*1000; //milisecond -> 2 day
    }

    public static final String LOG_FILE_MODEL_CRAWL = "./crawlmodel.dat";

    public static final String LOG_FILE_ARTICLE_CRAWL = "./crawlarticle.dat";

    public static final String LOG_FILE_SCHEDULE = "./logschedule.dat";

    public static final int PAGE_SIZE = 24;

    public  static  final int EVENT_MAX_RECORD_PER_PAGE = 10;
    // This notification is for updating member's profile
    public static final String NOTIFICATION_TYPE_PROFILE = "Profile";

    // This notification is for notice model's error images
    public static final String NOTIFICATION_TYPE_MODEL = "Model";

    // seen
    public static final int NOTIFICATION_SEEN = 1;

    // not seen
    public static final int NOTIFICATION_NOT_SEEN = 0;

    // max display
    public static final int NOTIFICATION_MAX_DISPLAY = 10;

    // send FCM url
    public static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    // FCM Server key - GunplaWorld
    public static final String FIREBASE_SERVER_KEY = "AAAABVIALOE:APA91bHbFMo6WflOhNQj1" +
            "gAvoye2g5XbVPmZY9Xdm9Hs7kG2bCsby0zkxQP567nCzUv79um331M66BZ6HcSjMRgkpcVSCx1FI3tIaBZLbZlOt7Eu4hNttMB3-RJnNWtEfhoZcr_F0MN9";
}
