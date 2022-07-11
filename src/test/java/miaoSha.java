import redis.clients.jedis.Jedis;

public class miaoSha {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.231.131", 6379);

        String set = jedis.set("sk" + "001" + ":qt", "3");
        start("001","001");
    }

    public static  boolean start(String uid,String proid)
    {
        //1.uid和proid的非空判断
        if(uid==null||proid==null)
        {
            return false;
        }
        //2.连接redis
        Jedis jedis = new Jedis("192.168.231.131", 6379);
        //3.拼接key
        String keKey="sk"+proid+":qt";
        String userKey="sk"+proid+"user";
        //4.获取库存，如果库存维null，秒杀还没有开始
        String s = jedis.get(keKey);
        if(s==null)
        {
            System.out.println("秒杀还没有开始");
            return false;
        }
        //5.判断用户是不是重复秒杀
        Boolean sismember = jedis.sismember(userKey, uid);
        if(sismember==true)
        {
            System.out.println("不可重复秒杀");
            return  false;
        }

        //6.如果商品小于1结束秒杀
        if(s==null||Integer.parseInt(s)==0)
        {
            System.out.println("秒杀结束");
            return false;
        }
        //7.秒杀过程
        //7.1库存-1
        jedis.decr(keKey);
        //7.2把秒杀的用户id加到清单中
        Long sadd = jedis.sadd(userKey, uid);
        return true;
    }












}
