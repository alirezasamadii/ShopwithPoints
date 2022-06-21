# 1.creating docker file to create a docker image for our backend
#1. docker image for java backend as a base image for our this image
# we need open jdk image for our java applicatioon dependenceis 
FROM openjdk:11
#Running gradle build command, we generated the jar file of our backend(whichis in buld/libs) 
# from our directory we put the dinewithme-0.1-all.jar in the docker image to run it
COPY ./build/libs/dinewithme-0.1-all.jar /home/dinewithme-0.1-all.jar
# port 8080 is going to be exposed from container created to local machine
EXPOSE 8080
#running command
CMD ["java","-jar","/home/dinewithme-0.1-all.jar"]

#then run in terminal to create image: docker build  -t dinewithme-be .
# then to run image :  docker run -p 8080:8080 -d  dinewithme-be   /// but we will use
# docker compose because there are mutiple images to be handled and run .