# 삼쩜삼 백엔드 엔지니어 채용 과제 

## 🎓 개요
회원가입, 스크래핑 데이터 조회, 결정세액 계산을 위한 백엔드 시스템입니다. JWT 인증, AES256 기반 주민등록번호 암복호화, 외부 API 연동(스크래핑), 연도별 소득 공제 정보 저장, 결정세액 계산까지 포함되어 있습니다.

---

## 🔨 사용 라이브러리 및 기술 스택
- java 17
- Spring Boot 3.2.4
- Spring Security
- Spring Data JPA
- H2 Embedded DB (In-Memory 모드)
- Feign Client (Spring Cloud OpenFeign)
- JWT (jjwt)
- Lombok
- Jackson (JSON 파싱 및 한글 필드 매핑)
- SLF4J + Logback (로깅)

---

## 📌 주요 기능

### ✅ 회원가입 & 로그인
- 지정된 사용자 이름과 주민등록번호만 허용
- 비밀번호와 주민등록번호 뒤 6자리만 AES256으로 암호화하여 저장
- 로그인 성공 시 JWT 발급

### ✅ 외부 API 연동 (스크래핑)
- `FeignClient`를 통해 외부 스크래핑 서버 호출
- 응답 구조를 `Generic`으로 파싱 가능하도록 구성
- `ScrapClientWrapper` 를 통해 예외/에러 공통 처리
- 실패 시 사용자 친화적 메시지 제공

### ✅ 소득 공제 저장
- 스크래핑 결과로부터 연도, 공제 항목(국민연금, 카드, 세액) 추출
- `TaxInfo` + `PensionDeduction` + `CreditCardDeduction` 으로 저장
- 동일 연도 데이터 존재 시 삭제 후 재저장 처리

### ✅ 결정세액 계산
- 과세표준 = 종합소득금액 - 총소득공제
- 세율 구간은 Enum(`TaxBracket`)으로 관리
- 산출세액 = 구간별 baseTax + 초과금액 * rate
- 결정세액 = 산출세액 - 세액공제
- 각 계산 단계에서 소수점 발생 시 반올림 적용

---

## 🧪 유틸
- `AmountUtil` : 콤마 제거/추가 등 문자열 ↔ 숫자 변환
- `DateUtil` : YearMonth 파싱 및 포맷 처리
- `@CreatedDate`, `@LastModifiedDate` 통한 Auditing
- H2 콘솔 접근 지원 (`/h2-console`)

---

## 🚀 실행 방법

- 기본 포트: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console

---

## 📚 과제 요구사항 요약
| Method | URL            | 설명                     |
|--------|----------------|--------------------------|
| POST   | /szs/signup    | 회원가입                |
| POST   | /szs/login     | 로그인 및 JWT 발급       |
| POST   | /szs/scrap     | 소득 정보 스크래핑       |
| GET    | /szs/refund    | 결정세액 조회            |
