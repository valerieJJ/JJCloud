package vjj.webconsumer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private Jedis jedis;

    @Autowired
    RedisTemplate redisTemplate;

    private static final String key = "history";

    public boolean addHistory(Integer uid, Integer mid){
        String field = mid.toString();
        List<Integer> mids = getHistory(uid);
        if(mids!=null && mids.contains(mid)){
            redisTemplate.opsForList().remove(key+":"+uid, 1, field);
        }
        jedis.lpush(key+":"+uid, field);
        while (jedis.llen(key+":"+uid)>=20){
            jedis.rpop(key+":"+uid);
        }
        return true;
    }

    public List<Integer> getHistory(Integer uid){
        if(jedis.exists(key+":"+uid)){
            List<String> result = jedis.lrange(key+":"+uid,0, -1);
            List<Integer> res = new ArrayList<>();
            for(String mid:result){
                res.add(Integer.parseInt(mid));
            }
            return res;
        }else{
            return null;
        }
    }

    public boolean clear(Integer uid){
        redisTemplate.opsForList().trim(key+":"+uid,0,0);
        redisTemplate.opsForList().leftPop(key+":"+uid);
        return true;
    }
}
