FROM openjdk:14

ARG JAR_FILE

RUN mkdir -p /apps
COPY ./${JAR_FILE} /apps/app.jar
COPY ./entrypoint.sh /apps/entrypoint.sh

RUN chmod +x /apps/entrypoint.sh
CMD ["/apps/entrypoint.sh"]