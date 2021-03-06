package k35_ch04;

/** 소프트웨어 코딩 심화 4강 - p15 실습
 * 
 *  단순비교
 *  줄이 바뀜에 따라 출력되는 *의 개수 증가
 * 
 * @author KOPO35
 *
 */
public class K35_ex02 {
	public static void main(String[] args) {
		
		int k35_iA = 0;					// 현재 몇 번째 줄인지를 나타내는 변수
		int k35_iB;						// 현재 줄에서 몇번째 글자 칸인지를 나타내는 변수

		while(true) {					// 줄에 대한 반복 -> 루프 내 마지막에 위치한 조건문에 의해 0~29, 즉 30줄을 출력하면 종료
			k35_iB=0;					// 현재 줄에서 0번째 글자로 명시적 위치? 0 초기화
			while (true) {				// 글자 칸에 대한 반복
				System.out.printf("*");	// 현재 글자칸에 *출력
				if (k35_iA == k35_iB) 	// 현재 줄 값과 일치하면
					break;				// 		반복문 종료 -> 다음 줄로 이동하기 위해서.
				k35_iB++;				// 다음 글자 칸으로 이동하기 위해 k35_iB++, 이 코드가 실행된다는 것은 위의 조건문을 만족하지 않았다는 것.
			}
			System.out.printf("\n");	// 현재 줄에 대해 *출력이 모두 종료했으므로 줄바꿈 출력
			k35_iA++;					// 현재 줄의 위치 변수 증가
			if(k35_iA == 30)			// 현재 줄의 위치 변수가 30이면	
				break;					//		반복문 종료
		}
	}
}