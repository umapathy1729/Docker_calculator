FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy your Java file into container
COPY Calculator.java .

# Compile Java program
RUN javac Calculator.java

# Expose the port your server listens on
EXPOSE 8080

# Run the program
CMD ["java", "Calculator"]
