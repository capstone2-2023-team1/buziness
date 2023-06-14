# NeRF-biz

### build
$ ./mvnw clean package -DskipTests //mvnw 있는 곳에서

### run
$ cd target
$ java -jar nerf-biz-0.0.1-SNAPSHOT.jar

### db
$ sudo systemctl start mariadb //db 서버 실행
// db: bootex, user: bootuser, pw: bootuser
