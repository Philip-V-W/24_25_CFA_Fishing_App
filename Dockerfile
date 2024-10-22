FROM ubuntu:latest

# Update and install necessary tools
RUN apt-get update && apt-get install -y curl zip unzip sed

# Install SDKMAN
RUN curl -s "https://get.sdkman.io" | bash

# Add SDKMAN to path
ENV SDKMAN_DIR="/root/.sdkman"
ENV PATH="${PATH}:${SDKMAN_DIR}/bin"

# Install Java
RUN bash -c "source ${SDKMAN_DIR}/bin/sdkman-init.sh && sdk install java 17.0.8-tem"

# Add Java to path
ENV JAVA_HOME="${SDKMAN_DIR}/candidates/java/current"
ENV PATH="${PATH}:${JAVA_HOME}/bin"

# Verify Java installation
RUN java -version

# Install Gradle
RUN bash -c "source ${SDKMAN_DIR}/bin/sdkman-init.sh && sdk install gradle"

# Add Gradle to path
ENV GRADLE_HOME="${SDKMAN_DIR}/candidates/gradle/current"
ENV PATH="${PATH}:${GRADLE_HOME}/bin"

# Install Spring Boot CLI
RUN bash -c "source ${SDKMAN_DIR}/bin/sdkman-init.sh && sdk install springboot"

# Create app directory
WORKDIR /app

# Initialize Spring Boot project
RUN bash -c "source ${SDKMAN_DIR}/bin/sdkman-init.sh && spring init --dependencies=web,data-jpa,thymeleaf,mariadb,devtools,webflux ./"

# Build the project
RUN ./gradlew build -x test

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "build/libs/demo-0.0.1-SNAPSHOT.jar"]