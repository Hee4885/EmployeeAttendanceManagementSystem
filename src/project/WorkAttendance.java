package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalDateTime; // 날짜와 시간을 함께 다룸
import java.time.format.DateTimeFormatter; // 날짜/시간 형식 변환
import java.time.temporal.ChronoUnit; // 시간 차이 계산
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class WorkAttendance {

	static boolean isCheckedIn = false; // 출근 여부 (false: 미출근)
	static Scanner sc = new Scanner(System.in);
	private static LocalDateTime checkInTime = null;
	private static LocalDateTime checkOutTime = null;
	static LocalDateTime OFFICE_START; // 회사 기준 출근 시간
	static LocalDateTime OFFICE_END; // 회사 기준 퇴근 시간
	static LocalDate workDate = null; // 근무 날짜
	private static int eLCount = 0; // 조퇴 횟수
	private static int lACount = 0; // 지각 횟수
	private static int eACount = 0; // 조기 출근 횟수
//	static int workExit = 0; // 근무 이탈 횟수
	static int workingDay = 0; // 근무 일수
	static long el = 0L; // 조퇴 시간(분)
	static long lA = 0L; // 지각 시간(분)
	static long eA = 0L; // 조기 출근 시간(분)
	static boolean isLogOut = true; // 로그아웃 상태
	static Map<String, String[]> EmployeeMap = new HashMap<>(); // 직원 정보를 저장할 Map
	static Map<String, String[]> attendanceMap = new HashMap<>(); // 직원 근태 정보를 저장할 Map

	// static 초기화 블록: OFFICE_START, OFFICE_END 설정
	static {
		OFFICE_START = LocalDateTime.now().withHour(9).withMinute(0);
		OFFICE_END = LocalDateTime.now().withHour(18).withMinute(0);
	}

	// 출퇴근 의사 확인
	public static void Attendance() throws IOException {
		int select1 = 0;
		while (true) {
			System.out.println("1.출근하기\r\n2.퇴근하기");
			System.out.print("선택 : ");
			try {
				select1 = sc.nextInt();
				sc.nextLine(); // 버퍼 비우기
				System.out.println();
			} catch (InputMismatchException e) {
				System.out.println("잘못된 입력입니다.\r\n");
				sc.nextLine();
				continue;
			}
			switch (select1) {
			case 1:
				checkIn();
				break;
			case 2:
				checkOut();
				break;
			default:
				System.out.println("올바른 번호를 입력하세요.\r\n");
			}
		}
	}

	// 출근하기
	public static void checkIn() {
		workDate = LocalDate.now();
		if (isCheckedIn) {
			System.out.println("이미 출근을 하셨습니다.\r\n");
			return;
		}
		checkInTime = LocalDateTime.now();
		setCheckInTime(checkInTime);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
		String formattedTime = checkInTime.format(formatter);

		if (checkInTime.isBefore(OFFICE_START)) {
			eACount++; // 조기 출근
			seteACount(eACount);
			eA = ChronoUnit.MINUTES.between(checkInTime, OFFICE_START);
			System.out.println("[ 출근 시간보다 빨리 출근하셨습니다! ]\r\n");
		} else if (checkInTime.isAfter(OFFICE_START)) {
			lACount++;
			setlACount(lACount);
			lA = ChronoUnit.MINUTES.between(checkInTime, OFFICE_START);
			System.out.println("[ 출근 시간보다 늦게 출근하셨습니다. ]\r\n");
		}

		System.out.println("+-------------------------------------+");
		System.out.println("|             출근 확인 완료             |");
		System.out.println("+-------------------------------------+");
		System.out.println("|                                     |");
		System.out.printf("|   [출근 시간] %s               |\r\n", formattedTime);
		System.out.println("|   [근무 시작] 오늘도 힘찬 하루!           |");
		System.out.println("|                                     |");
		System.out.println("| =================================== |");
		System.out.println("|         🚪 출근 완료! 업무 시작!        |");
		System.out.println("+-------------------------------------+\r\n");

		isCheckedIn = true;
	}

	// 퇴근하기
	public static void checkOut() throws IOException {
		if (!isCheckedIn) {
			System.out.println("먼저 출근한 후에 퇴근하실 수 있습니다.\r\n");
			return;
		}
		// 먼저 퇴근 시간 설정
		checkOutTime = LocalDateTime.now();
		setCheckOutTime(checkOutTime);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
		String formattedTime = checkOutTime.format(formatter);

		// 조퇴 여부 판단 (퇴근 시간이 OFFICE_END 보다 빠른 경우)
		if (checkOutTime.isBefore(OFFICE_END)) {
			System.out.println("⚠️ 지금 퇴근하면 조퇴 처리가 됩니다. ⚠️");
			System.out.print("그래도 퇴근하시겠습니까? (y 입력) : ");
			String workOutSelecter = sc.nextLine();
			System.out.println();
			if (!workOutSelecter.equalsIgnoreCase("y")) {
				System.out.println("잘못 된 입력입니다.\r\n");
				return;
			}
			eLCount++;
			seteLCount(eLCount);
			el = ChronoUnit.MINUTES.between(checkOutTime, OFFICE_END);
		}

		System.out.println("+-------------------------------------+");
		System.out.println("|             퇴근 확인 완료             |");
		System.out.println("+-------------------------------------+");
		System.out.println("|                                     |");
		System.out.printf("|   [퇴근 시간] %s               |\r\n", formattedTime);
		System.out.println("|   [근무 종료] 오늘도 수고하셨습니다!        |");
		System.out.println("|                                     |");
		System.out.println("| =================================== |");
		System.out.println("|              🚪 퇴근 완료!            |");
		System.out.println("+-------------------------------------+\r\n");

		workingDay++;
		setWorkingDay(workingDay);
		System.out.println("로그아웃 되었습니다.");

		// 근무 시간과 상태 계산 후 저장
		String fileId = LoginSystem.fileName + "workAttendance.csv"; // 예: "직원아이디workAttendance.csv"
		saveWorkData(fileId);
		saveAttendance(fileId);

		// 로그아웃
		logOut();
	}

	// 로그아웃: 상태 초기화 후 메뉴로 복귀
	public static void logOut() throws IOException {
		if (isCheckedIn) {
			isCheckedIn = false;
			checkInTime = null;
			checkOutTime = null;
			System.out.println("+-------------------------------------+");
			System.out.println("|              로그아웃 완료             |");
			System.out.println("|                                     |");
			System.out.println("|    [출근/퇴근 상태] 초기화 완료           |");
			System.out.println("|                                     |");
			System.out.println("| =================================== |");
			System.out.println("|           🚪 시스템을 종료합니다.        |");
			System.out.println("+-------------------------------------+\r\n");
			LoginSystem.menu();
		}
	}

	// 현재 근태 상태 계산
	public static String currentStatus() {
		LocalDateTime inTime = getCheckInTime();
		LocalDateTime outTime = getCheckOutTime();
		String status = "정상근무";
		if (inTime == null || outTime == null) {
			status = "결근";
		} else if (outTime.equals(OFFICE_END)) {
			if (inTime.isBefore(OFFICE_START)) {
				status = "조기 출근 " + eA + "분";
			} else if (inTime.isAfter(OFFICE_START)) {
				status = "지각 " + lA + "분";
			}
		} else if (outTime.isBefore(OFFICE_END)) {
			status = "조퇴 " + el + "분";
		} // else if (inTime != null && outTime == null) {
//			status = "근무 이탈"
//		}
		return status;
	}

	// 근무 시간 계산
	public static String saveWorkingTime() {
		LocalDateTime inTime = getCheckInTime();
		LocalDateTime outTime = getCheckOutTime();
		if (inTime != null && outTime != null && outTime.isAfter(inTime)) {
			long workHours = ChronoUnit.HOURS.between(inTime, outTime);
			long workMinutes = ChronoUnit.MINUTES.between(inTime, outTime);
			return workHours + "시 " + workMinutes + "분";
		} else {
			return "시간 오류";
		}
	}

	// 직원별 출퇴근 기록 저장 (CSV 형식)
	public static void saveWorkData(String fileName) throws IOException {
		File file = new File(fileName);
		boolean isNewFile = !file.exists(); // 파일이 존재하지 않으면 true (새 파일)

		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"))) {

			// 새 파일이면 UTF-8 BOM 추가 (엑셀에서 깨짐 방지)
			if (isNewFile) {
				writer.write("\uFEFF");
			}

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
			LocalDateTime inTime = getCheckInTime();
			LocalDateTime outTime = getCheckOutTime();

			String workData = workDate + "," + (inTime != null ? inTime.format(formatter) : "미출근") + ","
					+ (outTime != null ? outTime.format(formatter) : "미퇴근") + "," + saveWorkingTime() + ","
					+ currentStatus();

			writer.write(workData);
			writer.newLine();
			writer.flush();
			System.out.println("출퇴근 기록이 성공적으로 저장되었습니다.");
		} catch (IOException e) {
			System.out.println("파일에 데이터를 저장하는 중 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}

	// 근태 횟수 저장 (CSV 형식)
	public static void saveAttendance(String file) {
		File filePath = new File(file);
		boolean isNewFile = !filePath.exists();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(filePath, true), "UTF-8"))) {

			if (isNewFile) {
				writer.write("\uFEFF"); // UTF-8 BOM을 추가
				// 초기 데이터 설정 (조퇴, 지각, 조기 출근, 근무일수는 모두 0으로 시작)
				writer.write("조퇴횟수,지각횟수,조기출근횟수,근무일수\n");
			}

			String line;
			String[] counts = new String[4];
			boolean isUpdated = false;

			// 파일에서 기존 근태 횟수를 읽음
			while ((line = reader.readLine()) != null) {
				// 기존 근태 횟수를 찾음
				if (line.startsWith(LoginSystem.fileName)) {
					counts = line.split(","); // [아이디, 조퇴횟수, 지각횟수, 조기출근횟수, 근무일수]
					break;
				}
			}

			// 기존 값을 바탕으로 횟수를 증가시킨다.
			if (counts.length == 4) {
				// 각 값에 대해 null이나 빈 문자열이 들어있다면 0으로 초기화
				int elCount = parseToInt(counts[0]);
				int laCount = parseToInt(counts[1]);
				int eaCount = parseToInt(counts[2]);
				int workDays = parseToInt(counts[3]);

				elCount += geteLCount(); // 기존 조퇴 횟수에 새 값 추가
				laCount += getlACount(); // 기존 지각 횟수에 새 값 추가
				eaCount += geteACount(); // 기존 조기 출근 횟수에 새 값 추가
				workDays += getWorkingDay(); // 기존 근무일수에 새 값 추가

				// 데이터를 덧붙이기 위해 CSV 포맷으로 작성
				writer.write(
						LoginSystem.fileName + "," + elCount + "," + laCount + "," + eaCount + "," + workDays + "\n");

				// 업데이트된 값을 변수에 반영
				seteLCount(elCount);
				setlACount(laCount);
				seteACount(eaCount);
				setWorkingDay(workDays);
				isUpdated = true;
			}

			if (!isUpdated) {
				// 만약 업데이트된 근태 정보가 없다면, 새롭게 한 줄 추가
				writer.write(LoginSystem.fileName + "," + geteLCount() + "," + getlACount() + "," + geteACount() + ","
						+ getWorkingDay() + "\n");
			}

		} catch (IOException e) {
			System.out.println("파일에 데이터를 저장하는 중 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}

	// 문자열을 정수로 변환하는 메소드, null 또는 빈 문자열일 경우 0 반환
	private static int parseToInt(String value) {
		if (value == null || value.trim().isEmpty()) {
			return 0; // 값이 없으면 0으로 처리
		}
		return Integer.parseInt(value);
	}

	public static LocalDateTime getCheckInTime() {
		return checkInTime;
	}

	public static LocalDateTime getCheckOutTime() {
		return checkOutTime;
	}

	public static void setCheckInTime(LocalDateTime checkInTime) {
		WorkAttendance.checkInTime = checkInTime;
	}

	public static void setCheckOutTime(LocalDateTime checkOutTime) {
		WorkAttendance.checkOutTime = checkOutTime;
	}

	public static int geteLCount() {
		return eLCount; // 조퇴 횟수
	}

	public static void seteLCount(int eLCount) {
		WorkAttendance.eLCount = eLCount; // 조퇴 횟수
	}

	public static int getlACount() {
		return lACount;// 지각횟수
	}

	public static void setlACount(int lACount) {
		WorkAttendance.lACount = lACount; // 지각횟수
	}

	public static int geteACount() {
		return eACount; // 조기 출근 횟수
	}

	public static void seteACount(int eACount) {
		WorkAttendance.eACount = eACount; // 조기 출근 횟수
	}

	public static int getWorkingDay() {
		return workingDay; // 근무 횟수
	}

	public static void setWorkingDay(int workingDay) {
		WorkAttendance.workingDay = workingDay; // 근무 횟수
	}

}
