# MARU_SUGI

## 프로젝트 개요
MARU_SUGI는 스프링 기반의 웹 애플리케이션으로, 크레디탑(Creditop) 수기 결제와 관련된 기능을 제공합니다. 기본 진입점은 `/init`으로 연결되며, 세션 상태에 따라 로그인 혹은 가맹점 주문 화면으로 리다이렉트됩니다.

## 디렉터리 구조
- `bin/`: Ant 빌드 스크립트(`build.xml`)와 서버 실행/종료 스크립트가 위치합니다.
- `conf/`: 데이터베이스 및 내장 Tomcat 설정을 담은 JSON과 로그 설정 파일이 위치합니다.
- `src/`: 컨트롤러, 유틸리티, 내보내기(export) 모듈 등 Java 소스가 포함되어 있습니다.
- `web/`: JSP, 정적 자원(css, js, img 등), 업로드 디렉터리 등이 포함된 웹 루트입니다.

## 빌드 방법
1. JDK와 Ant가 설치되어 있는지 확인합니다.
2. 프로젝트 루트에서 아래 명령을 실행하여 클래스를 컴파일하고 JAR을 생성합니다.
   ```bash
   ant -f bin/build.xml compile
   ```
   - 소스는 `src`에서 읽고, 컴파일 결과는 `web/WEB-INF/classes`에 저장됩니다.
   - `lib/` 디렉터리에 기본 JAR(`MARU_v1.jar`)과 공용 라이브러리(`MARU_lib.jar`)가 생성됩니다.

## 실행 및 종료
내장 Tomcat을 활용하여 애플리케이션을 실행합니다.
1. `bin/start.sh`를 실행하면 JVM 옵션을 설정하고 `com.pgmate.lib.tomcat.Tomcat8`을 구동합니다.
   ```bash
   cd bin
   ./start.sh
   ```
   - 설정 경로(`-DCP_CONF`)는 `conf/`를 바라보며, 클래스패스는 `lib/`와 `web/WEB-INF/classes`를 사용합니다.
2. 서버 PID는 `bin/pwd.pid`로 저장됩니다. 종료 시 `bin/stop.sh`를 실행하여 해당 PID를 종료하고 파일을 삭제합니다.

## 환경 설정
애플리케이션의 기본 설정은 `conf/service.json`에서 관리합니다.
- 데이터베이스: MariaDB 드라이버와 JDBC URL, 접속 계정, 커넥션 풀 크기를 정의합니다.
- Tomcat: 서비스 포트, 호스트, 스레드 풀, SSL 사용 여부 및 프로토콜/암호화 스위트를 설정합니다.
- `contextPath`는 웹 루트 상위(`../`)로 지정되어 있습니다.

## 배포/운영 팁
- 설정 변경 후에는 `ant compile`로 소스를 다시 컴파일하고 서버를 재시작해 반영합니다.
- 로그 설정은 `conf/logback.xml`에서 조정할 수 있으며, 업로드 경로는 JVM 옵션의 `java.io.tmpdir`를 통해 `web/upload`로 지정되어 있습니다.