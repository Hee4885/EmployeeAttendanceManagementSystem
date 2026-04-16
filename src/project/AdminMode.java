package project;

import java.io.*;
import java.util.Scanner;
import java.util.InputMismatchException;

public class AdminMode {

    // 관리자 모드 메소드
    public static void adminMode() throws IOException {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("+======================================+");
            System.out.println("|            🔧 관리자 모드 🔧            |");
            System.out.println("+======================================+");
            System.out.println("| 1. 출퇴근 기록 조회                      |");
            System.out.println("| 2. 근태 상태 확인 (지각, 조퇴, 조기 출근)    |");
            System.out.println("|  0. 로그아웃                           |");
            System.out.println("+======================================+");

            System.out.print("▶ 선택: ");
            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine(); // 버퍼 비우기
            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                sc.nextLine();
                continue;
            }

            if (choice == 0) {
                System.out.println("관리자 모드가 종료되었습니다.\r\n");
                LoginSystem.menu();
                return;
            }

            // 직원 아이디 입력 받기
            System.out.print("직원 아이디를 입력하세요: ");
            String employeeId = sc.nextLine();

            // 파일명 생성
            String fileName = employeeId + "workAttendance.csv";

            // 파일 존재 여부 확인
            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("해당 직원이 존재하지 않거나 파일이 없습니다.");
                continue; // 관리자 메뉴로 돌아감
            }

            // 선택한 기능 실행
            switch (choice) {
                case 1:
                    showAttendanceRecords(fileName);
                    break;
                case 2:
                    showAttendanceStatus(fileName);
                    break;
                default:
                    System.out.println("올바른 번호를 입력하세요.");
                    break;
            }
        }
    }

    // 출퇴근 기록을 출력하는 메소드
    public static void showAttendanceRecords(String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {
            String[] records = reader.lines().toArray(String[]::new); // 파일 전체 읽기
            
            if (records.length <= 1) { 
                System.out.println("출퇴근 기록이 존재하지 않습니다.");
                return;
            }

            System.out.println("+--------------------------------------------+");
            System.out.println("|                출퇴근 기록 테이블               |");
            System.out.println("+--------------------------------------------+");
            System.out.println("|   날짜   |   출근 시간  |  퇴근 시간  |  근무 상태  |");
            System.out.println("+--------------------------------------------+");

            // 마지막 줄(근태 현황)을 제외한 출퇴근 기록 출력
            for (int i = 0; i < records.length - 1; i++) {  
                String[] data = records[i].split(",");
                if (data.length < 4) continue; // 데이터가 부족한 경우 무시
                String date = data[0];
                String checkInTime = data[1];
                String checkOutTime = data[2];
                String workingStatus = data[3];
                System.out.printf("| %s | %s | %s | %s |\n", date, checkInTime, checkOutTime, workingStatus);
            }

            System.out.println("+--------------------------------------------+\r\n");
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 근태 현황을 출력하는 메소드
    public static void showAttendanceStatus(String fileName) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {
            String[] records = reader.lines().toArray(String[]::new); // 파일 전체 읽기
            
            if (records.length == 0) { 
                System.out.println("근태 현황 데이터가 존재하지 않습니다.");
                return;
            }

            String attendanceStatus = records[records.length - 1]; // 마지막 줄이 근태 현황

            // 근태 현황을 출력
            String[] status = attendanceStatus.split(",");
            if (status.length < 5) {
                System.out.println("근태 현황 데이터가 올바르지 않습니다.");
                return;
            }

            String elCount = status[1]; // 조퇴 횟수
            String laCount = status[2]; // 지각 횟수
            String eaCount = status[3]; // 조기 출근 횟수
            String workDays = status[4]; // 근무일수

            System.out.println("+------------------------------+");
            System.out.println("|            근태 현황           |");
            System.out.println("+------------------------------+");
            System.out.printf("| 조퇴 횟수     | %s               |\n", elCount);
            System.out.printf("| 지각 횟수     | %s               |\n", laCount);
            System.out.printf("| 조기 출근 횟수 | %s               |\n", eaCount);
            System.out.printf("| 근무 일수     | %s               |\n", workDays);
            System.out.println("+------------------------------+\r\n");
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}
