FROM java:8
MAINTAINER valerie
VOLUME /data

RUN apt-get update
RUN apt-get install net-tools
RUN ifconfig

ADD user-module-0.0.1-SNAPSHOT.jar user-module.jar

CMD ["--server.port=8003"]

EXPOSE 8003


ENTRYPOINT ["java","-jar","user-module.jar"]