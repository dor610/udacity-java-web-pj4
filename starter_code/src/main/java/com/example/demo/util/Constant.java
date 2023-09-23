package com.example.demo.util;

public class Constant {

    public static class Security {
        private Security() {}

        public static final String SECRET = "pj4_secret_key";
        public static final long EXPIRATION_TIME = 864_000_000;
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String HEADER_STRING = "Authorization";
        public static final String SIGN_UP_URL = "/api/user/create";
        public static final String LOGIN_URL = "/login";
    }

    public static class APIUri {
        private APIUri() {}

        public static final String CART_API = "/api/cart";
        public static final String CART_API_ADD_TO_CART = "/addToCart";
        public static final String CART_API_REMOVE_FROM_CART = "/removeFromCart";
        public static final String ITEM_API = "/api/item";
        public static final String ITEM_API_ITEM_ID = "/{id}";
        public static final String ITEM_API_ITEM_NAME = "/name/{name}";
        public static final String ORDER_API = "/api/order";
        public static final String ORDER_API_SUBMIT = "/submit/{username}";
        public static final String ORDER_API_GET_ORDER_FOR_USER = "/history/{username}";


        public static final String USER_API = "/api/user";
        public static final String USER_API_FIND_BY_ID = "/id/{id}";
        public static final String USER_API_FIND_BY_USERNAME = "/{username}";
        public static final String USER_API_CREATE = "/create";


    }

    public static class Message {
        private Message() {}

        public static final String PASSWORD_TOO_SHORT = "Password too short";
        public static final String PASSWORDS_DO_NOT_MATCH = "The passwords you entered do not match";
    }
}
