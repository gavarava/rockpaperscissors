# Rock Paper Scissors           [![Build Status](https://travis-ci.org/gavarava/rockpaperscissors.svg?branch=master)](https://travis-ci.org/gavarava/rockpaperscissors)  [![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=gavarava_rockpaperscissors&metric=alert_status)](https://sonarcloud.io/dashboard?id=gavarava_rockpaperscissors)
A simple Rock Paper Scissors REST Application using Spring Boot

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Building the project
Project is bundled with the Gradle Wrapper
```
./gradlew clean build
```


## Running the tests
```
./gradlew clean test
```

## Run the application
### Using java jar command
```
java -jar rock-paper-scissors-<version>.jar
```
### Using Docker
##### Build the Docker image
```
docker build -t rockpaperscissors .
```
##### Run Docker container
```
docker run -p 8080:8080 -dit rockpaperscissors:latest
```

## Built With
* [SpringBoot](http://spring.io/projects/spring-boot) - The web framework used
* [Gradle](https://gradle.org/) - Dependency Management
* [Docker](https://www.docker.com/) - Containerization

## Versioning
* [Git](https://git-scm.com/)

## Java Coding Style for IDE
* [Google Style - Intellij](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)
* [Google Style - Eclipse](https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml)

## Authors
* **Gaurav Edekar** - https://github.com/gavarava

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments
* Google
* Safari Books Online