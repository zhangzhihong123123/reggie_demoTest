import redis.clients.jedis.Jedis;

import java.util.Random;

public class redisPhone {

    public static void main(String[] args) {
        verifyCode("1231","123");
        getRedisCode("1231","123");
    }
    //1. 生成6位验证码
    public static String getCode()
    {
        Random random=new Random();
        String code="";
        for(int i=0;i<6;i++)
        {
            code=random.nextInt(10)+code;
        }
        return code;
    }
    //2.每个手机只能每天发送三次验证码
    public static void verifyCode(String phone,String code)
    {
        //1.连接redis
        Jedis jedis = new Jedis("192.168.231.131", 6379);
        //2.发生次数key
        String countKey="VerifyCode"+phone+":count";
        String CodeKey="VerifyCode"+phone+":Code";
        String count=jedis.get(countKey);
        if(count==null)
        {
            //没有发生次数
            //设置发生第一次
            jedis.setex(countKey, 24*24*60,"1");

        }
        else if(Integer.parseInt(count)>2)
        {
            jedis.incr(countKey);
        }
        else
        {
            System.out.println("已经发生了三次，无法发送");
        }
        String vcode=getCode();
        jedis.setex(CodeKey,2*60,vcode);
        jedis.close();
    }
    //3.验证
    public static  void getRedisCode(String phone,String code)
    {
        Jedis jedis = new Jedis("192.168.231.131", 6379);
        String CodeKey="VerifyCode"+phone+":Code";
        String s = jedis.get(CodeKey);
        if(s.equals(code))
        {
            System.out.println("成功");

        }
        else {
            System.out.println("失败");
        }
    }



}
