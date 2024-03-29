package vjj.movieservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import models.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import requests.FavoriteRequest;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.aggregation.Aggregation;
//import org.springframework.data.mongodb.core.aggregation.AggregationResults;
//import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
//import scala.Int;
//import vjj.movierec.myModel.*;
//import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class FavoriteService {

//    private MongoClient mongoClient = new MongoClient( "localhost", 27017);

    @Autowired
    private MongodbService mongodbService;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private Jedis jedis;
//    @Autowired
//    private MongoTemplate mongoTemplate;
    @Autowired
    RedisTemplate redisTemplate;

    private final String collectionName = "Favorite";
    private final String zsetName = "favoriteRank";

    public FavoriteService() {
    }

    public Favorite DBOjbect2Favorite(DBObject object){
        try{
            return objectMapper.readValue(JSON.serialize(object), Favorite.class);

        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public DBCollection getCollection(String collectionName){
//        DB db = mongoClient.getDB("MovieDB");
//        Set<String> names = db.getCollectionNames();
//        DBCollection collection;
//        if(names.contains(collectionName)){
//            collection = db.getCollection(collectionName);
//        }else{
//            System.out.println("create and get a new collection: "+collectionName);
//            collection = db.getCollection(collectionName);
//        }
//        return collection;
//    }

    public DBCollection getCollection(){
        DBCollection collection = mongodbService.getCollection(this.collectionName);
        return collection;
    }

    public Favorite findFavorite2Mongo(int uid, int mid){
        BasicDBObject query = new BasicDBObject();
        query.append("uid", uid);
        query.append("mid", mid);
        DBCursor cursor = this.getCollection().find(query);
        if (cursor.count()>0){
            System.out.println("It's in your favorites");
            Favorite favorite = DBOjbect2Favorite(cursor.next());
            return favorite;
        }else {
            return null;
        }
    }

    public boolean insertFavorite2Mongo(Favorite favorite) throws IllegalAccessException {
        DBObject dbObject = mongodbService.bean2DBObject(favorite);
        this.getCollection().insert(dbObject);
        return true;
    }

    public boolean dropFavorite2Mongo(Integer uid, Integer mid) throws IllegalAccessException {
        BasicDBObject query = new BasicDBObject();
        query.append("uid", uid);
        query.append("mid", mid);
        Favorite favorite = findFavorite2Mongo(uid, mid);
        this.getCollection().findAndRemove(query);
        return true;
    }

    public List<Favorite> getFavoriteHistory(int uid){
        BasicDBObject query = new BasicDBObject();
        query.append("uid", uid);
        DBCursor cursor = this.getCollection().find(query);
        List<Favorite> favoriteList = new ArrayList<>();
        while(cursor.hasNext()){
            Favorite favorite = DBOjbect2Favorite(cursor.next());
            favoriteList.add(favorite);
        }
        return favoriteList;
    }

//    private void updateFavorite2Redis(Favorite favorite){
//        Set<String> rankList = redisTemplate.opsForZSet().reverseRange(favorite.getMid(), 0L, 49L);
//        List<Map> result = new ArrayList<>();
//        for (String name : rankList){
//            Map<String, Object> map = new HashMap<>();
//            map.put("name", name);
//            Double score = redisTemplate.opsForZSet().score(rankingKey, name);
//            map.put("score", score);
//            result.add(map);
//        }
//        System.out.println("通过key获取value：    " + jedis.get("uid:"+favorite.getUid()));
//    }

    private int getMIDFavoriteCounts(int mid){
        BasicDBObject query = new BasicDBObject();
        query.append("mid", mid);
        DBCursor cursor = this.getCollection().find(query);
        int cnt = 0;
        while(cursor.hasNext()){
            System.out.println(mid+": "+cnt);
            cnt ++;
        }
        return cnt;
    }
//
//    class FavoriteCount{
//        int fmid;
//        int n;
//    }
    private HashMap<String, Double> getAllMoviesFavoritesCounts(){
//
        System.out.println("getAllMoviesFavoritesCounts()... ");

//        TypedAggregation agg = Aggregation.newAggregation(
//                Favorite.class,
//                group("mid").count().as("n"),
//                project("n").and("fmid")
//        );
//        AggregationResults<FavoriteCount> output = mongoTemplate.aggregate(agg,FavoriteCount.class);
//        List<FavoriteCount> favoriteList = output.getMappedResults();
//        System.out.println("getAllMoviesFavoritesCounts()... "+favoriteList.size());
//        HashMap<String, Double> map = new HashMap<>();
//        for(FavoriteCount f:favoriteList){
////            int cnt = getMIDFavoriteCounts(f.fmid);
//            map.put(String.valueOf(f.fmid), (double)f.n);
//        }
//        return  map;
        return  null;
    }

    public boolean favoriteExistMongo(int uid, int mid){
        return findFavorite2Mongo(uid, mid) != null;
    }

    public boolean updateFavorite2Mongo(Favorite favorite){//先查询后重置用户对该电影的评价
        BasicDBObject query = new BasicDBObject();
        query.append("uid", favorite.getUid());
        query.append("mid", favorite.getMid());
        getCollection().update(query,
                new BasicDBObject("$set",  // {$set:{field:value}}把文档中某个字段field的值设为value
                        new BasicDBObject("timestamp",favorite.getTimestamp())
                )
        );
//        redisTemplate.opsForZSet().incrementScore(sortedSetKet, favorite, 1);
        return true;
    }

    /********************   jedis   ******************************/
    private void initZsetFromMongo(){
//        BasicDBObject query = new BasicDBObject();
//        DBCursor cursor = this.getCollection().find(query);
        HashMap<String, Double> map = new HashMap<>();
        DBCursor cursor = this.getCollection().find();
        System.out.println("initZsetFromMongo()...");
        while(cursor.hasNext()){
            Favorite favorite = DBOjbect2Favorite(cursor.next());
            int mid = favorite.getMid();
            if(!map.containsKey(String.valueOf(mid))){
//                int cnt = getMIDFavoriteCounts(mid);
                map.put(String.valueOf(mid), 0.);
                System.out.println("mid="+mid+", cnt=");
            }
        }

        jedis.zadd(zsetName, map);
        System.out.println("initZsetFromMongo()...done");
        return;
    }

    public Set<String> getZsetRank(){
//        Set<String> set = redisTemplate.opsForZSet().reverseRange(zsetName, 0, 9);
//        Set<String> set = jedis.zrevrange(zsetName, 0, -1);
        Set<String> set = redisTemplate.opsForZSet().reverseRangeByScore(zsetName, 1, 100);
//        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRange(zsetName, 0, 100);;
//        for(String mid:set){
//            Double score = redisTemplate.opsForZSet().score(zsetName, mid);
//            System.out.println("!!! mid="+mid+", cnt="+score);
//        }

        return set;
    }

    private void addFavoriteZset(Favorite favorite){
        Set<String> ranks = getZsetRank();

        String mid = String.valueOf(favorite.getMid());
        if(!ranks.contains(mid)) redisTemplate.opsForZSet().add(zsetName, mid,1.0);
        else redisTemplate.opsForZSet().incrementScore(zsetName, mid,1.0);
        System.out.println("addFavoriteZset...");
    }

    private boolean decreaseFavoriteZset(Favorite favorite){
        Set<String> ranks = getZsetRank();
        String mid = String.valueOf(favorite.getMid());

        System.out.println("decreasingZset...");
        if(ranks.size()!=0 && ranks.contains(mid)) {
            System.out.println("decreasingZset...2");
            redisTemplate.opsForZSet().incrementScore(zsetName, mid, -1.0);
//            redisTemplate.opsForZSet().removeRangeByScore(zsetName, -1*Integer.MAX_VALUE, 0.1);
//            redisTemplate.opsForZSet().remove(zsetName, mid);
            return true;
        }else{
            if(ranks.size()==0){
                System.out.println("!jedis.exists(zsetName)...");
                initZsetFromMongo();
            }
            return false;
        }
    }

//    private void dropFavoriteZset(Favorite favorite){
//        // 刷新缓存时，先删缓存再写缓存
//        if(jedis.exists("uid:"+favorite.getUid())
//                && jedis.llen("uid:"+favorite.getUid())>=40){
//            jedis.rpop("uid:"+favorite.getUid());
//        }
//        jedis.lpush("uid:"+favorite.getUid(),String.valueOf(favorite.getMid()));
//    }

    /********************  dealing with requests   ******************************/
    public boolean updateFavorite(FavoriteRequest favoriteRequest) throws IllegalAccessException {
        Favorite favorite = new Favorite(favoriteRequest.getUid(), favoriteRequest.getMid());

//        updateFavorite2Redis(favorite); // 先更新redis缓存
//        addOrUpdateZset(favorite);
        boolean res;
        if(favoriteExistMongo(favorite.getUid(), favorite.getMid())){ //再操作数据库，如果以前收藏过该电影，
            res = updateFavorite2Mongo(favorite); //
        }else{
            res = insertFavorite2Mongo(favorite); // 否则插入新的收藏
        }
        System.out.println("mongo added favorite ... ");

        addFavoriteZset(favorite);
        Set<String> rank = getZsetRank();
        for(String mid:rank){
            Double score = redisTemplate.opsForZSet().score(zsetName, mid);
            System.out.println("!!! mid="+mid+", cnt="+score);
        }
        return res;
    }

    public boolean dropFavorite(FavoriteRequest favoriteRequest) throws IllegalAccessException {
        Favorite favorite = new Favorite(favoriteRequest.getUid(), favoriteRequest.getMid());
//        dropFavorite2Redis(favorite); // 先更新redis缓存
        decreaseFavoriteZset(favorite);
        if(favoriteExistMongo(favoriteRequest.getUid(), favoriteRequest.getMid())){ //再操作数据库，如果以前收藏过该电影，
            return dropFavorite2Mongo(favoriteRequest.getUid(), favoriteRequest.getMid()); //
        }else{
            return false;
        }
    }
    public static void main(String[] args) throws UnknownHostException, IllegalAccessException {
        FavoriteService favoriteService = new FavoriteService();
//        DBCollection collection = favoriteService.getCollection("Favorite");
        Favorite favorite = new Favorite(231141674, 1721);
        FavoriteRequest favoriteRequest = new FavoriteRequest(231141674, 1721);
//        DBObject object = favoriteService.bean2DBObject(favorite);
//        favoriteService.updateFavorite2Mongo(favorite);
//        favoriteService.dropFavorite(favoriteRequest);
//        favoriteService.getCollection().insert(object);

        List<Favorite> favoriteList = favoriteService.getFavoriteHistory(231141674);
        System.out.println("favoriteList.size() = "+favoriteList.size());
        HashMap<Integer,List<Favorite>> map = new HashMap<>();
        for(Favorite favorite1: favoriteList){
            map.put(favorite1.getMid(), favoriteList);
            System.out.println(String.format("%s, %s, %s", favorite1.getUid(), favorite1.getMid()
                    , favorite1.getTimestamp()));

        }
        System.out.println(map.size());
        System.out.println(map.keySet().toString());
    }

}
