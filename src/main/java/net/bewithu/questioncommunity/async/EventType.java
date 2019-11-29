package net.bewithu.questioncommunity.async;

/**
 * 事件的类型
 */
public enum EventType {
        /**
         * 用户点赞
         */
        LIKE(0),
        /**
         * 用户评论
         */
        COMMENT(1),
        /**
         * 用户登陆
         */
        LOGIN(2),
        /**
         * 发送邮件的队列，后期短信验证可以借用
         */
        MAIL(3),

        /**
         * 用户注册
         */
        REGISTER(4),

        /**
         * 用户发布问题
         */
        QUESTION(5),

        /**
         * 用户关注
         */
        FOLLOWER(6);

        private int value;

        EventType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
}
