package movie;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MemberExam {	
	private Scanner scanner = new Scanner(System.in);
	private Connection conn;
	
	public MemberExam() {
		try {			
			Class.forName("com.mysql.cj.jdbc.Driver");			
			
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/movie?useUnicode=true&characterEncoding=utf8",
					"root",
					"1234");
		}catch(Exception e) {
			e.printStackTrace();
			exit();
		}
	}

public void list() {
		try {
			String sql="SELECT no, title FROM t_movie order by no";
			PreparedStatement pstmt = conn.prepareStatement(sql); 
			ResultSet rs = pstmt.executeQuery(); 
			System.out.println("** 영화 예매 사이트 입니다 **");
			System.out.println("--------------현재 상영작--------------");
			while(rs.next()) {
				List List = new List();
				String no = rs.getString("no");
				String title = rs.getString("title");
				List.setNo(no);
				List.setTitle(title);				
				System.out.printf("%-2s%-7s",
						List.getNo(),
						List.getTitle());
			}
			rs.close();
			pstmt.close();
		}catch(SQLException e) { 
			e.printStackTrace();
			exit();
		}
		System.out.println();
		System.out.println("----------------------------------");
		System.out.println("메인메뉴:1.로그인|2.회원가입|3.회원확인|4.프로그램 종료");
		System.out.println("메뉴선택:");
		String menuNo = scanner.nextLine();
		System.out.println();
		
		switch(menuNo) {
					case "1" -> login();
					case "2" -> create();
					case "3" -> read();
					case "4" -> exit();
}
}

public void login() {
	try {
        System.out.println("로그인");
        System.out.print("아이디: ");
        String inputID = scanner.nextLine();
        System.out.print("비밀번호: ");
        String inputPW = scanner.nextLine();
        
        String sql = "SELECT * FROM t_member WHERE id=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, inputID);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String pwd = rs.getString("pwd");
            if (inputPW.equals(pwd)) {
                System.out.println();
                System.out.println("로그인 되었습니다.");
                System.out.println();
                pstmt.close();
                reservation();
            } else {
                System.out.println("비밀번호가 틀렸습니다.");
            }
        } else {
            System.out.println("해당 아이디를 찾을 수 없습니다.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        exit();
    }
}

public void create() {
	Member member = new Member();
	System.out.println("[회원가입]"); 
	System.out.print("아이디: ");
	member.setId(scanner.nextLine()); 
	System.out.print("비밀번호: ");
	member.setPwd(scanner.nextLine());
	System.out.print("이름: ");
	member.setName(scanner.nextLine());
	System.out.print("email: ");
	member.setEmail(scanner.nextLine());

	System.out.println("-------------------");
	System.out.println("가입하시겠습니가?:1.확인|2.취소");
	System.out.println("선택:");
	String menuNo = scanner.nextLine();
	if(menuNo.equals("1")) {					
		try {
			String sql=""+ "INSERT INTO t_member (id, pwd, name, email, joinDate)" +
						"VALUES(?, ?, ?, ?, now())";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPwd());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getEmail());
			pstmt.executeUpdate();
			System.out.println("가입이 완료되었습니다.");
			System.out.println();
			pstmt.close();
		}catch(Exception e) {
			e.printStackTrace();
			exit();
		}
}

	list();
}

public void read() {
	System.out.print("아이디:");
	String _id = scanner.nextLine();	
	
	try {
		String sql = "SELECT id, name, email, joinDate FROM t_member WHERE id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, _id);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()) {
			Member member = new Member();
			String id= rs.getString("id");
			String name = rs.getString("name"); 
			String email = rs.getString("email");
			Date joinDate = rs.getDate("joinDate");
			member.setId(id);
			member.setName(name);
			member.setEmail(email);
			member.setJoinDate(joinDate);
			System.out.println("");
			System.out.println("아이디: " + member.getId());
			System.out.println("이름: " + member.getName());
			System.out.println("email: " + member.getEmail());
			System.out.println("가입날짜: " + member.getJoinDate());
		
			System.out.println("-------------------");
			System.out.println("1.수정하기 | 2.탈퇴하기");
			System.out.print("선택: ");
			String menuNo = scanner.nextLine(); 
			System.out.println();
			
			if(menuNo.equals("1")) { update(member);
			} else if(menuNo.equals("2")) {
				delete(member); 
				}
		}
		rs.close();
		pstmt.close();
		} catch (Exception e) {
			e.printStackTrace(); 
			exit(); 
			}
		list();
	}

public void update(Member member) {
	System.out.println("[수정 내용 입력]");
	System.out.print("이름: ");
	member.setName(scanner.nextLine());
	System.out.print("비밀번호: ");
	member.setPwd(scanner.nextLine());
	System.out.print("email: ");
	member.setEmail(scanner.nextLine());
	
	System.out.println("--------------");
	System.out.println("수정하시겠습니까?:1.수정하기 | 2.취소하기");
	System.out.print("선택:");
	String menuNo = scanner.nextLine();
	if(menuNo.equals("1")) {
			try { String sql = "" +
								"UPDATE t_member SET pwd=?, name=?, email=? WHERE id=?"; 
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getPwd());
			pstmt.setString(2, member.getName()); 
			pstmt.setString(3, member.getEmail()); 
			pstmt.setString(4, member.getId());
			System.out.println("수정이 완료되엇습니다.");
			System.out.println();
			pstmt.executeUpdate();
			pstmt.close(); 
			} catch (Exception e) {
				e.printStackTrace(); 
				exit();
				}
			}
	list();			
}

public void delete(Member member) {
    try {
        String sql = "DELETE FROM t_member WHERE id=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, member.getId());
        System.out.println("정말로 탈퇴하시겠습니까?");
        System.out.println("1.탈퇴하기 | 2.취소하기");
        System.out.println("선택: ");
        String menuNo = scanner.nextLine();
		if(menuNo.equals("1")) {delete(member);		
		}
		System.out.println("탈퇴가 완료되었습니다");
		System.out.println();
        pstmt.close();
    } catch (SQLException e) {  
        e.printStackTrace();
        exit();
    }
    
    list();
}

public void reservation() {
	try {
		System.out.print("영화제목: ");
		String inputTitle = scanner.nextLine();    

		String sql = "UPDATE t_member set title=? where id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, inputTitle);    
		System.out.println("예매가 완료되엇습니다.");
		System.out.println();
		pstmt.executeUpdate();
		pstmt.close(); 
		} catch (Exception e) {
			e.printStackTrace(); 
			exit();
		}
	}


	/*while (true) {
		System.out.println("-----------------------------영화예매---------------------------");
		System.out.println("1." + movieLists[0]+ " | 2." + movieLists[1] + " | 3." + movieLists[2]
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
	
list();
}*/

public void clear() {
	 System.out.println("[회원전체삭제]"); 
	 System.out.println("-------------------------"); 
	 System.out.println("회원전체를 삭제하시겠습니까?: 1.Ok | 2.Cancel");
	 System.out.print("메뉴선택: "); String menuNo = scanner.nextLine();
	 if(menuNo.equals("1")) { 
		 				//boards 테이블에 게시물 정보 전체 삭제	 }
		 				try {
		 					String sql = "TRUNCATE TABLE t_login";
		 					PreparedStatement pstmt = conn.prepareStatement(sql);
		 					pstmt.executeUpdate();
		 					pstmt.close(); 
		 					} catch (Exception e) {
		 						e.printStackTrace(); 
		 						exit();		 						
		 					}
		 				}
	list();
}

public void exit() {
			if(conn !=null) {
						try {
								conn.close();
						}catch (SQLException e) {
						}
			}
			System.out.println("** 예매 사이트 종료 **");
			System.exit(0);
}



public static void main(String[] args) {
	MemberExam memberExam = new MemberExam();
	memberExam.list();
	}		
}