#FROM openjdk:23-oracle 
FROM openjdk:21-oracle # jenkins 버전 issue 해결을 위해 21로 변경
# 작업 디렉토리 설정
ENV HOME_DIR=/work
WORKDIR $HOME_DIR
# JAR 파일 복사
COPY build/libs/shelter.jar ./shelter.jar
# 앱 실행 명령
CMD ["java", "-jar", "shelter.jar"]
