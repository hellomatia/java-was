# Java WAS 만들어 보기

| 2024 우아한 테크캠프 프로젝트 WAS

| 프로젝트 기간: 2024.07.01 ~ 2024.07.19

| 우아한 테크캠프 7기 이지표

## 서버 (server 패키지)
### server.core
![server.core](/docs/server.svg)
- Server는 port번호와 threadPoolSize, basePackage(스캔할 Handler가 있는 패키지)를 입력받는다.

- ThreadPool에게 threadPoolSize를 넘겨주고, ThreadPool은 쓰레드 풀을 생성한다.

- Port는 port번호가 유효한지, 검증하고, 포트 번호를 가지고 있다.

- ConnectionAcceptor는 Port를 넘겨받고, 해당 포트 번호로 소켓을 생성한다.

- HandlerScanner는 Server가 넘겨준 basePackage를 기준으로 스캔을하고, 사용자가 설정한 Handler List를 RequestDispatcher에게 넘겨준다.

- 서버는 HttpConnectionProcessor에게 클라이언트 소켓과, requestDispatcher를 넘겨주고, 이를 실행한다.

### server.template
![server.template](/docs/template.svg)
- 사용자가 TemplateEngine에 template 이름과 data를 전달한다.

- TemplateEngine이 templates 폴더에서 해당 template를 읽어온다.

- TemplateEngine이 읽어온 template를 TemplateParser에 전달한다.

- TemplateParser가 template를 Element 단위로 재귀적으로 분석한니다.

- TemplateParser가 분석 결과인 root Element를 TemplateEngine에 반환한다.

- TemplateEngine이 root Element와 data를 TemplateRenderer에 전달한다.

- TemplateRenderer가 data를 적용하여 최종 화면을 생성한다.

- TemplateRenderer가 생성된 화면을 TemplateEngine에 반환한다.

- 마지막으로, TemplateEngine이 최종 결과를 사용자에게 반환한다.

### server.session
![server.session](/docs/session.svg)
- 세션 생성: 사용자가 SessionManager에 새 세션 생성을 요청한다.

- 세션 조회: 사용자가 sessionId로 세션을 요청하고, SessionManager가 유효성을 검사한다.

- 만료된 경우: 세션을 무효화하고 null을 반환한다.

- 유효한 경우: 마지막 접근 시간을 갱신하고 세션을 반환한다.

- 세션 사용: 사용자가 Session 객체의 메서드들을 직접 호출하여 데이터를 관리한다.

- 세션 무효화: 사용자가 SessionManager에 세션 무효화를 요청한다.

- 만료된 세션 정리: SessionManager가 주기적으로 만료된 세션들을 정리한다.

## 데이터베이스 (database 패키지)
![database](/docs/database.svg)
- 사용자가 SQL 문을 SQLParser에 전달한다.

- SQLParser가 SQL을 파싱하고 적절한 Statement 객체를 생성한다.

- Statement 객체가 CsvDataBaseEngine에서 실행된다.

- CsvDataBaseEngine은 Statement 타입에 따라 다르게 동작한다:
  - CreateTableStatement: 새 CSV 파일을 생성하고 컬럼 정보를 추가한다.
  - InsertStatement: CSV 파일에 새 레코드를 추가한다.
  - SelectStatement: CSV 파일의 데이터를 읽고, WHERE 조건을 확인한 후 ResultSet에 결과를 추가한다.

- 실행 결과가 사용자에게 반환된다.

## 테크 스펙
- [WAS1-step1](https://docs.google.com/document/d/1srHMGZ2ZS4elE8HGII7MhIsLwVNmHf67cRJu03A70R0/edit?usp=sharing)
- [WAS1-step2](https://docs.google.com/document/d/1wqb-Mj_2MwjAgH4y4ve7-XOxehNhj_TUSUFEdaXvieU/edit?usp=sharing)
- [WAS1-step3](https://docs.google.com/document/d/1ujdlDzzUJ7WITFGRX4YjR9O0fAybtRrnjRCZJxv6ZEA/edit?usp=sharing)
- [WAS2-step1](https://docs.google.com/document/d/1cgcQsHFtTQgKEim3CimjveSx2E8w0QXvameZUDRzyHc/edit?usp=sharing)
- [WAS2-step2](https://docs.google.com/document/d/1TXw8WIqy4h8Y0s_Ef2OBzXW8wye2TPShUWLMG9QPCQM/edit?usp=sharing)
- [WAS2-step3](https://docs.google.com/document/d/1zSWVet5bleRTjNpFDNfVXRt6qOXVfkeR2-wejhTKBlk/edit?usp=sharing)
- [WAS3-step1](https://docs.google.com/document/d/12F6rgQ8O4h0ajZtjS7Dk4g_ntu55WnHfp58qKu4q_vo/edit?usp=sharing)
- [WAS3-step2](https://docs.google.com/document/d/15E_XfZh5y5ONEh_6KGfKEh_lg8Na_UmG94uPe8ZGKsA/edit?usp=sharing)
- [WAS3-step3](https://docs.google.com/document/d/1h3iqqYm8OOi5IfQM3ND3b2JnKtnYCAFTho-yGcNtW7s/edit?usp=sharing)
