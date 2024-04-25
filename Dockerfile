FROM openjdk:11.0.16
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/*.jar /app/ecommerce-product-service.jar
ENTRYPOINT ["java","-jar","/app/ecommerce-product-service.jar"]