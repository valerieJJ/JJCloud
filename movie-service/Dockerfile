FROM java:8
MAINTAINER valerie
VOLUME /data

ADD movie-service-0.0.1-SNAPSHOT.jar movie-service.jar

CMD ["--server.port=8004"]

EXPOSE 8004

ENTRYPOINT ["java","-jar","movie-service.jar"]