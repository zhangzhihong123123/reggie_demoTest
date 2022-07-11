import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class test {

    @Test
    public void  test01()
    {
        String name="aaa.jpg";
        String substring = name.substring(3);
        System.out.println(substring);
    }


    @Test
    public void  test()
    {
        Integer i=6;
    }

    @Test
    public void test02()
    {
        Jedis jedis = new Jedis("192.168.231.131", 6379);
        String success = jedis.ping("success");
        System.out.println(success);
        Set<String> keys=jedis.keys("*");
        for(String key:keys)
        {
            System.out.println(key);
        }
        System.out.println(jedis.ttl("key2"));
        jedis.set("name","123");
        System.out.println(jedis.get("name"));
        Long lpush = jedis.lpush("key111", "lucy", "123", "12345");
        List<String> key111 = jedis.lrange("key111", 0, -1);
        System.out.println(key111);
        Long hset = jedis.hset("user", "age", "12");
        System.out.println(jedis.hget("user","age"));
        System.out.println(jedis.hgetAll("user"));

    }



}
