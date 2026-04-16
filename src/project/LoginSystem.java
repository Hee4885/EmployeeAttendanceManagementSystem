package project;

/*
	작성자 : 전희진
	학번   : 1416
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LoginSystem {

	static Scanner sc = new Scanner(System.in);
	static final String FILE_PATH = "employee_data.txt"; // 직원 정보 파일 경로
	static final String FILE_HR = "HR_department_data.txt"; // 인사부 정보 파일 경로
	static final String FILE_SALES = "sales_department_data.txt"; // 영업부 정보 파일 경로
	static final String FILE_EXCHEQUER = "exchequer_department_data.txt"; // 재무부 정보 파일 경로
	static String EmployeeID = null;
	static String fileName = null;
	private static String adminId = "admin"; // 관리자 아이디
	private static String adminPw = "1234"; // 관리자 비번
	

	// 메뉴 선택하기
	public static void menu() throws IOException {
		int select1 = 0;
		while (true) {
			System.out.println("1.로그인\r\n2.회원가입");
			System.out.print("선택 : ");

			// 발생할 수 있는 예외 : 문자,문자열,기호를 입력했을 시
			try {
				select1 = sc.nextInt();
				sc.nextLine(); // 버퍼 비우기
				System.out.println();
			} catch (InputMismatchException e) {
				System.out.println("잘못된 입력입니다.\r\n");
				sc.nextLine(); // 버퍼 비우기
				continue; //재귀 호출 대신 continue로 반복
			}

			// 메뉴에 있는 번호 또는 없는 번호를 입력했을 시
			switch (select1) {
			case 1:
				login();
				break;
			case 2:
				signUp();
				break;
			default:
				System.out.println("올바른 번호를 선택하세요\r\n");
			}
		}
	}

	// 회원가입
	public static void signUp() throws IOException {
		System.out.println("*==========*");
		System.out.println("  SIGN UP");
		System.out.println("*==========*");

		// 메뉴로 돌아가기 입력
		System.out.println("[ 계속 진행을 원하시면 아무 키를 눌러주세요. ]");
		System.out.print("메뉴로 돌아가기(b 입력) : ");
		String goBack = sc.nextLine();
		System.out.println();
		if (goBack.equalsIgnoreCase("b")) {
			// signUp()을 종료, main 클래스에 호출한 menu()로 돌아감
			menu();
			return; 
		}

		// 이름 입력 받기
		System.out.print("이름 : ");
		String name = sc.nextLine();
		System.out.println();
		while (!isName(name)) {
			System.out.println("기호나 숫자를 제외한 후 작성하세요.\r\n");
			System.out.print("이름 : ");
			name = sc.nextLine();
			System.out.println();
		}

		// 부서 선택하기
		int select2 = 0;
		String department = "";

		while (true) { // 무한 반복
			System.out.println("부서를 일치한 번호를 선택하세요.");
			System.out.println("1.인사부\r\n2.영업부\r\n3.재무부");
			System.out.print("선택 : ");

			// 발생할 수 있는 예외 : 문자,문자열,기호를 입력했을 시
			try {
				select2 = sc.nextInt();
				sc.nextLine(); // 버퍼 비우기
				System.out.println();
			} catch (InputMismatchException e) {
				System.out.println("잘못 된 입력입니다.");
				sc.nextLine(); // 잘못된 입력 처리 후 버퍼 비우기
				continue; // 잘못된 입력 시 다시 선택
			}

			switch (select2) {
			case 1:
				department = "인사부";
				break; // switch 문 탈출
			case 2:
				department = "영업부";
				break;
			case 3:
				department = "재무부";
				break;
			default:
				System.out.println("잘못된 부서 번호입니다. 다시 선택해주세요.");
				continue; // 잘못된 부서 번호 선택 시 다시 입력
			}
			break; // 유효한 부서 번호가 입력되면 종료
		}

		String id;
		System.out.print("아이디 : ");
		id = sc.nextLine();
		while (!isID(id)) {
			System.out.println("이미 사용 중인 아이디가 존재합니다.\r\n");
			System.out.print("아이디 : ");
			id = sc.nextLine();
		}
		while(id.trim().isEmpty()) {
			System.out.println("아이디를 입력해주세요.\r\n");
			System.out.print("아이디 : ");
			id = sc.nextLine();
		}
		System.out.println("사용 가능한 아이디입니다!\r\n");

		// 비밀번호 입력 받기
		System.out.print("비밀번호 : ");
		String password = sc.nextLine();
		
		while(password.trim().isEmpty()) {
			System.out.println("비밀번호를 입력해주세요.\r\n");
			System.out.print("비밀번호 : ");
			password = sc.nextLine();
		}
		
		System.out.print("비밀번호 재확인 : ");
		String isPassword = sc.nextLine();

		while (!isPassword.equals(password)) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			System.out.print("비밀번호 재확인 : ");
			isPassword = sc.nextLine();
		}
		System.out.println();
		System.out.println("회원가입 성공!\r\n");

		// 부서별 파일 생성
		File fileHR = new File(FILE_HR);
		File fileSales = new File(FILE_SALES);
		File fileEXCHEQUER = new File(FILE_EXCHEQUER);

		// 부서별로 구분 후
		switch (department) {
		case "인사부":
			// 파일 존재 여부 판단
			if (!fileHR.exists()) {
				try {
					// 없을 시 파일 생성 - 예외처리가 필요
					fileHR.createNewFile();
				} catch (IOException e) {
					System.out.println("파일을 생성할 수 없습니다.");
					return;
				}

				// 파일에 데이터 쓰기
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileHR, true))) {
					writer.write(name + "," + department + "," + id);
					writer.newLine();
				} catch (IOException e) {
					System.out.println("파일에 데이터를 저장하던 중 오류가 발생했습니다.");
					e.printStackTrace(); // 오류 발생 시 원인 출력
				}
			}
			break;

		case "영업부":
			if (!fileSales.exists()) {
				try {
					fileSales.createNewFile();
				} catch (IOException e) {
					System.out.println("파일을 생성할 수 없습니다.");
					return;
				}
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_SALES, true))) {
				writer.write(name + "," + department + "," + id);
				writer.newLine();
			} catch (IOException e) {
				System.out.println("파일에 데이터를 저장하던 중 오류가 발생했습니다.");
				e.printStackTrace();
			}
			break;

		case "재무부":
			if (!fileEXCHEQUER.exists()) {
				try {
					fileEXCHEQUER.createNewFile();
				} catch (IOException e) {
					System.out.println("파일을 생성할 수 없습니다.");
					return;
				}
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_EXCHEQUER, true))) {
				writer.write(name + "," + department + "," + id);
				writer.newLine();
			} catch (IOException e) {
				System.out.println("파일에 데이터를 저장하던 중 오류가 발생했습니다.");
				e.printStackTrace();
			}
			break;
		}

		// 직원 정보 저장
		saveEmployeeData(name, department, id, password);
	}

	// 이름이 한글 또는 영문인지 확인하기
	public static boolean isName(String name) {
		// 한글 또는 영어만 허용하는 정규 표현식
		return name.matches("^[가-힣a-zA-Z]+$");
	}

	// 직원 정보 저장
	public static void saveEmployeeData(String name, String department, String id, String password) {
		File file = new File(FILE_PATH);

		// 파일이 존재하지 않으면 새로 생성
		if (!file.exists()) {
			try {
				file.createNewFile(); // 파일 생성
			} catch (IOException e) {
				System.out.println("파일을 생성할 수 없습니다.");
				return; // 파일 생성 실패 시 종료
			}
		}

		// 파일에 데이터를 저장
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
			writer.write(name + "," + department + "," + id + "," + password); // CSV 형식으로 저장
			writer.newLine();
		} catch (IOException e) {
			System.out.println("파일에 데이터를 저장하던 중 오류가 발생했습니다.");
			e.printStackTrace(); // 오류 발생 시 원인 출력
		}

	}

	// 아이디 중복확인
	public static boolean isID(String id) throws IOException {

		File file = new File(FILE_PATH);

		if (!file.exists()) {
        	file.createNewFile();
			return true; // 파일이 없으면 사용 가능
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] idData = line.split(",");
				if (idData.length == 4) {
					String ID = idData[2];
					if (id.equals(ID))
						return false; // 아이디가 중복되면
				}

			}
		} catch (IOException e) {
			System.out.println("파일을 읽는 중 오류가 발생했습니다.");
			e.printStackTrace();
		}
		return true; // 파일 전체를 검사해도 중복이 없고 문제가 없다면
	}

	// 로그인
	public static void login() throws IOException {
	    System.out.println("*==========*");
	    System.out.println("   LOGIN   ");
	    System.out.println("*==========*");

		// 메뉴로 돌아가기 입력
		System.out.println("[ 계속 진행을 원하시면 아무 키를 눌러주세요. ]");
		System.out.print("메뉴로 돌아가기(b 입력) : ");
		String goBack = sc.nextLine();
		System.out.println();
		if (goBack.equalsIgnoreCase("b")) {
			// signUp()을 종료, main 클래스에 호출한 menu()로 돌아감
			menu();
			return;
		}
		else {
			// 이름 입력
			System.out.print("아이디 : ");
			String id = sc.nextLine();
			System.out.println();

			// 비밀번호 입력
			System.out.print("비밀번호 입력 : ");
			String password = sc.nextLine();

			if (id.equals(adminId) && password.equals(adminPw)) {
				// 관리자가 맞다면
				System.out.println("✅ 관리자 모드로 진입합니다.");
				AdminMode.adminMode();
			} else {

				File file = new File(FILE_PATH);

				if (!file.exists()) {
					file.createNewFile();
					return; // 파일이 없으면 읽지 않음
				}

				boolean isLoginSuccess = false;
				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
					String line;

					while ((line = reader.readLine()) != null) {
						String[] data = line.split(",");

						if (data.length == 4) {
							String LoginName = data[0];
							String LoginDepartment = data[1];
							String LoginId = data[2];
							String LoginPassword = data[3];

							if (id.equals(LoginId) && password.equals(LoginPassword)) {
								isLoginSuccess = true;
								EmployeeID = LoginId;
								break;
							} else {
								isLoginSuccess = false;
							}
						}
					}

					fileName = EmployeeID ; // 직원 아이디를 기반으로 파일명 생성

					if (isLoginSuccess) {
						System.out.println("로그인 성공!\r\n");
						WorkAttendance.Attendance(); // 출퇴근 기능으로 이동
					} else {
						System.out.println("로그인 실패! 아이디 또는 비밀번호가 일치하지 않습니다.\r\n");
					}
				} catch (IOException e) {
					System.out.println("파일을 읽는 중 오류가 발생했습니다.");
					e.printStackTrace();
				}
			}
		}
	}
}