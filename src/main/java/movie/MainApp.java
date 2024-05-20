package movie;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainApp {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
			
		ArrayList<String> movieLists[] = new ArrayList[5]; // 영화 목록을 담을 배열
		for (int i = 0; i < movieLists.length; i++) {
			movieLists[i] = new ArrayList<String>(); // 배열 요소마다 ArrayList 초기화
		}
		ArrayList<Reservation1> reservations = new ArrayList<>(); // 예약 정보를 담을 리스트 추가

		// 기본 영화 목록 추가
		movieLists[0].add("스턴트맨");
		movieLists[1].add("쿵푸팬더4");
		movieLists[2].add("범죄도시4");
		movieLists[3].add("파묘");
		movieLists[4].add("챌린저스");

		int[] movieCounts = new int[5]; // 각 영화의 예매 수량을 담을 배열 초기화

		while (true) {
			System.out.println("영화 예매 사이트");
			System.out.print("아이디: ");
			String inputID = scanner.nextLine();
			System.out.print("비밀번호: ");
			String inputPW = scanner.nextLine();

			String adminID = "추승보";
			String adminPW = "qwer";

			if (inputID.equals(adminID) && inputPW.equals(adminPW)) {
				System.out.println();
				System.out.println("관리자 아이디입니다.");
				while (true) {
					System.out.println("1.예매현황 확인하기");
					System.out.println("2.등록된 예매 취소하기");
					System.out.println("3.신규 영화 등록하기");
					System.out.println("4.영화 삭제하기");
					System.out.println("5.관리자 로그아웃");
					System.out.print("선택: ");
					int adminmenu = scanner.nextInt();
					scanner.nextLine();

					switch (adminmenu) {
					case 1:
						displayReservationStatus(movieLists, movieCounts, reservations);
						break;
					case 2:
						System.out.println("-----등록된 예매 취소하기-----");
						System.out.print("예약을 취소할 좌석번호를 입력하세요: ");
						String cancelSeat = scanner.nextLine();
						cancelReservation(reservations, movieCounts, cancelSeat, movieLists);
						break;
					case 3:
						System.out.println("-----신규 영화 등록하기-----");
						System.out.print("영화 제목: ");
						String newMovie = scanner.nextLine();
						for (int i = 0; i < movieLists.length; i++) {
							System.out.println((i + 1) + ". " + movieLists[i]);
						}
						System.out.print("추가할 번호를 선택해주세요");
						int listChoice = scanner.nextInt();
						scanner.nextLine();
						if (listChoice >= 1 && listChoice <= 5) {
							// 이전 영화 목록을 지우고 새로운 영화 추가
							movieLists[listChoice - 1].clear();
							movieLists[listChoice - 1].add(newMovie);
							System.out.println(newMovie + "이(가) 영화 목록에 추가되었습니다.");
							System.out.println();
						} else {
							System.out.println("잘못된 선택입니다.");
							System.out.println();
							continue;
						}
						break;
					case 4:
						System.out.println("------영화삭제하기------");
						for (int i = 0; i < movieLists.length; i++) {
							System.out.println((i + 1) + ". " + movieLists[i]);
						}
						System.out.print("삭제할 번호를 입력해주세요: ");
						int deleteListChoice = scanner.nextInt();
						scanner.nextLine();
						if (deleteListChoice >= 1 && deleteListChoice <= 5) {
							System.out.print("관리자 비밀번호를 입력하세요: ");
							String adminPassword = scanner.nextLine();
							if (adminPassword.equals(adminPW)) { // 비밀번호 확인
								movieLists[deleteListChoice - 1] = new ArrayList<String>(); // 해당 목록을 빈 목록으로 초기화하여 삭제
								System.out.println("영화가 삭제되었습니다.");
								System.out.println();
							} else {
								System.out.println("비밀번호가 일치하지 않습니다. 영화 삭제를 할 수 없습니다.");
								System.out.println();
								break;
							}
						} else {
							System.out.println("잘못된 입력입니다.");
							System.out.println();
							continue;
						}
						break;

					case 5:
						System.out.println("로그아웃되었습니다.");
						System.out.println();
						break;
					default:
						System.out.println("올바른 선택지를 입력하세요.");
						System.out.println();
						break;
					}

					if (adminmenu == 5) {
						break; // 로그아웃 시 관리자 메뉴 반복문 탈출
					}
				}
			} else {
				System.out.println();
				System.out.println(inputID + "님 반갑습니다.");

				while (true) {
					System.out.println("-----------------------------영화예매---------------------------");
					System.out.println("1." + movieLists[0] + " | 2." + movieLists[1] + " | 3." + movieLists[2]
							+ " | 4." + movieLists[3] + " | 5." + movieLists[4] + " | 6.예매취소 | 7.예매종료");
					System.out.println("--------------------------------------------------------------");
					System.out.print("선택: ");
					int moviechoice = scanner.nextInt();
					scanner.nextLine();

					if (moviechoice >= 1 && moviechoice <= movieLists.length) {
						System.out.println("등록된 영화 목록:");
						for (String movie : movieLists[moviechoice - 1]) {
							System.out.println(movie);
						}
						System.out.println(movieLists[moviechoice - 1] + " 예매를 선택하셨습니다.");
						System.out.println("---------------------좌석선택(ex:0a0a)-------------------------");
						System.out.print("좌석번호 : ");
						String inputseat = scanner.next();
						if (Pattern.matches("[0-9a-zA-Z]{4}", inputseat)) {
							movieCounts[moviechoice - 1]++; // 해당 영화의 예매 수량 증가
							reservations.add(new Reservation1(inputID, movieLists[moviechoice - 1].get(0), inputseat)); // 예약
																														// 정보
																														// 저장
							System.out.println("좌석번호 " + inputseat + "로 예매되었습니다.");
							System.out.println();
						} else {
							System.out.println("잘못된 형식의 좌석번호입니다. 예매가 되지 않았습니다.");
							continue;
						}
					} else if (moviechoice == movieLists.length + 1) {
						System.out.println("----예매 취소하기----");
						System.out.print("예약을 취소할 좌석번호를 입력하세요: ");
						String cancelSeat = scanner.nextLine();
						cancelReservation(reservations, movieCounts, cancelSeat, movieLists);
					} else if (moviechoice == movieLists.length + 2) {
						System.out.println("예매를 종료합니다.");
						System.out.println();
						break;
					} else {
						System.out.println("잘못된 입력입니다. 다시 시도하세요.");
						continue; // 반복문의 처음으로 이동
					}

					System.out.println("#현재 영화예매 현황");
					for (int i = 0; i < movieLists.length; i++) {
						System.out.println(movieLists[i] + " 예매수량= " + movieCounts[i] + "매");
					}
					System.out.println();
				}
			}
		}
	}

	private static void displayReservationStatus(ArrayList<String>[] movieLists, int[] movieCounts,
			ArrayList<Reservation1> reservations) {
		System.out.println("---------예매현황--------");
		for (int i = 0; i < reservations.size(); i++) {
			Reservation1 reservation = reservations.get(i);
			System.out.println("아이디: " + reservation.getUserID() + ", 영화: " + reservation.getMovie() + ", 좌석번호: "
					+ reservation.getSeat());
		}
		System.out.println();
	}

	private static void cancelReservation(ArrayList<Reservation1> reservations, int[] movieCounts, String cancelSeat,
			ArrayList<String>[] movieLists) {
		boolean found = false;
		for (Reservation1 reservation : reservations) {
			if (reservation.getSeat().equals(cancelSeat)) {
				// 취소된 예약의 영화를 찾습니다.
				String movie = reservation.getMovie();
				int movieIndex = -1;
				for (int i = 0; i < movieLists.length; i++) {
					if (movieLists[i].contains(movie)) {
						movieIndex = i;
						break;
					}
				}
				// 취소된 예약의 영화를 찾은 경우 해당 영화의 예매 수량을 감소시킵니다.
				if (movieIndex != -1) {
					movieCounts[movieIndex]--;
					reservations.remove(reservation); // 예약 리스트에서 해당 예약을 제거합니다.
					System.out.println("좌석번호 " + cancelSeat + "의 예매가 취소되었습니다.");
					found = true;
					break;
				}
			}
		}
		if (!found) {
			System.out.println("해당하는 좌석번호의 예매를 찾을 수 없습니다.");
		}
		System.out.println();
	}
}

class Reservation1 {
	private String userID;
	private String movie;
	private String seat;

	public Reservation1(String userID, String movie, String seat) {
		this.userID = userID;
		this.movie = movie;
		this.seat = seat;
	}

	public String getUserID() {
		return userID;
	}

	public String getMovie() {
		return movie;
	}

	public String getSeat() {
		return seat;
	}
}