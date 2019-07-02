package grpc.chat.Server;

import grpc.chat.Message;
import redis.clients.jedis.Jedis;

class RedisService {
    private static RedisService redisService;
    private static Jedis jedis;

    private static final String USERNAME_PASSWORD_MAP = "userNamePasswordMap";

    static RedisService getRedisService() {
        if (redisService == null) {
            redisService = new RedisService();
        }
        return redisService;
    }

    private RedisService() {
        jedis = new Jedis("localhost");
    }

    synchronized boolean isValidCredential(String userName, String password) {
        return userExists(userName) &&
                doesPasswordMatch(userName, password);
    }

    synchronized boolean userExists(String userName) {
        return jedis.hget(USERNAME_PASSWORD_MAP, userName) != null;
    }

    synchronized private boolean doesPasswordMatch(String userName, String password) {
        return jedis.hget(USERNAME_PASSWORD_MAP, userName).equals(password);
    }

    synchronized boolean hasNextMessage(String receiver) {
        return jedis.llen(receiver) > 0;
    }

    synchronized Message getNextMessage(String receiver) {
        String[] messageString = jedis.lindex(receiver, 0).split(":", 2);
        return Message.newBuilder()
                .setMessageText(messageString[1])
                .setFrom(messageString[0])
                .build();
    }

    synchronized void removeLastMessage(String receiver) {
        jedis.lpop(receiver);
    }

    synchronized void addMessage(String sender, String receiver, String message) {
        jedis.rpush(receiver, sender + ":" + message);
    }
}
