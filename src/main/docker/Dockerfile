FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

RUN adduser -D -s /bin/sh admin
WORKDIR /home/admin

ADD entrypoint.sh entrypoint.sh
RUN chmod 755 entrypoint.sh && chown admin:admin entrypoint.sh
USER jhipster

ENTRYPOINT ["./entrypoint.sh"]

EXPOSE 8080

ADD *.war app.war

