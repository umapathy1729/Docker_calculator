FROM openjdk:17

# Set working directory inside container
WORKDIR /app

# Copy Java file into container
COPY Calculator.java .

# Compile the Java program
RUN javac Calculator.java

# Expose port (optional if your Java app listens on a port)
EXPOSE 8080

# Run the Java program
CMD ["java", "Calculator"]
