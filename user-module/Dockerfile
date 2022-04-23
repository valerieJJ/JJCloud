FROM java:8
MAINTAINER valerie
VOLUME /data

ADD user-module-0.0.1-SNAPSHOT.jar user-module.jar

CMD ["--server.port=8003"]

EXPOSE 8003

ENTRYPOINT ["echo","ready!"]
ENTRYPOINT ["java","-jar","user-module.jar"]