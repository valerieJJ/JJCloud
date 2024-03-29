package vjj.movieservice.services;

import DTO.MovieDTO;
import VO.MovieVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import models.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import requests.HotMovieRequest;
import requests.LatestMovieRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//import vjj.movierec.myModel.DTO.MovieDTO;
//import models.Movie;
//import vjj.movierec.myModel.VO.MovieVO;
//import requests.HotMovieRequest;
//import requests.LatestMovieRequest;

@Service
public class RecService {

    private final Logger log = LoggerFactory.getLogger(RecService.class);

    @Autowired
    private MovieService movieService;
    @Autowired
    private MongodbService mongodbService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RatingService ratingService;

    private MovieDTO DBObject2MovieDTO(DBObject object){
        try{
            return objectMapper.readValue(JSON.serialize(object), MovieDTO.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Async
    public CompletableFuture<List<MovieVO>> getLatestRecommendations(LatestMovieRequest request) {
        int sum = request.getSum();
        System.out.println("async late"+Thread.currentThread());
        System.out.println("latest recommendation getSum = " + sum);
        DBCollection movieCollection = mongodbService.getCollection("Movie");
//        BasicDBObject shoot =
//        DBCursor cursor = movieCollection.find().sort(Sorts.orderBy(Sorts.descending("times"))).limit(sum);
        DBCursor cursor = movieCollection.find()
                                .sort(new BasicDBObject("shoot", -1));
                                //.sort(Sorts.descending("yearmonth")).limit(sum)
        List<MovieVO> movieVOS = new ArrayList<>();
        while (cursor.hasNext() && sum != 0) {
            MovieDTO movieDTO = DBObject2MovieDTO(cursor.next());
            int mid = movieDTO.getMid();
            MovieVO movieVO = new MovieVO();
            movieVO.setMid(mid);
            movieVO.setCountRates(movieDTO.getCount());
            Movie movie = movieService.findByMID(mid);
            String avgScore = ratingService.getMovieAverageScores(mid);
            movieVO.setName(movie.getName());
            movieVO.setScore(avgScore);
            movieVO.setDescri(movie.getDescri());
            movieVO.setDirectors(movie.getDirectors());
            movieVO.setActors(movie.getActors());
            movieVO.setGenres(movie.getGenres());
            movieVO.setLanguage(movie.getLanguage());
            movieVO.setIssue(movie.getIssue());
            movieVOS.add(movieVO);

            sum--;
        }
        return CompletableFuture.completedFuture(movieVOS);
    }
    //运行超过5秒就降级，调用fallback_timeout方法
    @Async
    public CompletableFuture<List<MovieVO>> getHotRecommendations(HotMovieRequest request){

//        try { // for test hystrixcommand
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        int sum = request.getSum();
        System.out.println("async hot "+Thread.currentThread());
        System.out.println("hot recommendation getSum = "+sum);
        DBCollection movieCollection = mongodbService.getCollection("RateMoreMovies");
        DBCursor cursor = movieCollection.find().sort(new BasicDBObject("count",-1));//.sort(Sorts.descending("yearmonth")).limit(sum)
        List<MovieVO> movieVOS = new ArrayList<>();
        while (cursor.hasNext() && sum!=0){
            MovieDTO movieDTO = DBObject2MovieDTO(cursor.next());
            int mid = movieDTO.getMid();
            MovieVO movieVO = new MovieVO();
            movieVO.setMid(mid);
            movieVO.setCountRates(movieDTO.getCount());
            Movie movie = movieService.findByMID(mid);
            String avgScore = ratingService.getMovieAverageScores(mid);
            movieVO.setName(movie.getName());
            movieVO.setScore(avgScore);
            movieVO.setDescri(movie.getDescri());
            movieVO.setDirectors(movie.getDirectors());
            movieVO.setActors(movie.getActors());
            movieVO.setGenres(movie.getGenres());
            movieVO.setLanguage(movie.getLanguage());
            movieVO.setIssue(movie.getIssue());
            movieVOS.add(movieVO);

            sum--;
        }
        return CompletableFuture.completedFuture(movieVOS);
    }


}
