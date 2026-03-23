FROM openjdk:17
WORKDIR /app
COPY Calculator.java .
RUN javac Calculator.java
EXPOSE 8080
CMD ["java", "Calculator"]
