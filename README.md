# 직원 출퇴근 관리 시스템 (EmployeeAttendanceManagementSystem)

Java 기반 콘솔 애플리케이션으로, 로컬 텍스트 파일(CSV/TXT)을 이용해 직원의 회원 관리와 출퇴근(근태) 기록을 저장 및 조회하는 프로젝트입니다.  
(한국어 UI, 간단한 관리자 모드 포함)

## 한 줄 요약
콘솔에서 직원 가입/로그인 후 출퇴근을 기록하고 개인별 CSV 파일에 근무기록과 근태 집계를 저장하는 Java SE 애플리케이션.

## 주요 기능
- 회원가입 / 로그인 (파일 기반)
- 관리자 로그인: `admin` / `1234` (관리자 전용 기능 진입)
- 출근 / 퇴근 기록 저장 — 개인별 파일: `<아이디>workAttendance.csv`
- 근무시간 계산 및 근태 판정(정상근무, 지각, 조퇴, 조기출근 등)
- 부서별 파일 분리 저장(인사/영업/재무)
- 새로 생성되는 CSV에 UTF-8 BOM 추가(엑셀 호환성 고려)

## 사용된 기술
- 언어: Java (100%)
- 플랫폼: Java SE (module-info.java 포함 — 모듈 사용 가능)
- 표준 라이브러리:
  - java.io (파일 입출력: BufferedReader/Writer, FileInputStream/OutputStream 등)
  - java.time (LocalDate, LocalDateTime, DateTimeFormatter, ChronoUnit)
  - java.util (Scanner, Map 등)
- 데이터 저장: 로컬 텍스트 파일 (CSV/TXT)

## 저장소 구조 (요약)
```
.
├─ .gitignore
├─ HR_department_data.txt        # 인사부 샘플 데이터
├─ employee_data.txt             # 모든 직원 정보 저장 (이름,부서,아이디,비밀번호)
├─ heeworkAttendance.csv         # 루트에 있는 예시/기록 파일
├─ src/
│   ├─ module-info.java
│   └─ project/
│       ├─ MainApp.java          # 진입점 (main)
│       ├─ LoginSystem.java      # 회원가입/로그인, 부서 파일 관리
│       ├─ WorkAttendance.java   # 출퇴근 로직, 기록 저장, 근태 집계
│       └─ AdminMode.java        # 관리자 기능(현황 보기 등)
```

## 데이터 형식 예시
- employee_data.txt: CSV — `이름,부서,아이디,비밀번호`  
  예: `전희진,인사부,hee,123`
- 부서 파일: `HR_department_data.txt`, `sales_department_data.txt`, `exchequer_department_data.txt` — 각 줄 `이름,부서,아이디`
- 개인 출퇴근 파일: `<아이디>workAttendance.csv` — 행 형식: `날짜,출근시간,퇴근시간,근무시간,근태상태`


1. 컴파일 (간단 클래스패스 방식)
   - 터미널에서 프로젝트 루트:
     javac -d out src/project/*.java
2. 실행
   - java -cp out project.MainApp
