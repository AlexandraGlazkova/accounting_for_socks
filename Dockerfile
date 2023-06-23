FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar accounting-for-socks.jar
ENTRYPOINT ["java","-jar","/accounting-for-socks.jar","-Dfile.encoding=UTF-8"]