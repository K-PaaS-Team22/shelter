FROM openjdk:23-oracle
# 작업 디렉토리 설정
ENV HOME_DIR=/work
WORKDIR $HOME_DIR
# JAR 파일 복사
COPY build/libs/shelter.jar ./shelter.jar
# 앱 실행 명령
CMD ["java", "-jar", "shelter.jar"]
