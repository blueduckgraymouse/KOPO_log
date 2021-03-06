package crawling_distance;

//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CrawlingDistanceElementarySchool_branch1 {
	
	final static int startGuIndex = 13;
	
	//private static final String filePath = "c:\\KOPO\\git_tarcking\\기본프로그래밍_java\\Pro\\Data.csv";
	private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	//private static final String WEB_DRIVER_PATH = "D:\\KOPO\\utility\\chromedriver_win32\\chromedriver.exe";
	private static final String WEB_DRIVER_PATH = "C:\\chromedriver\\chromedriver.exe";

	public static void main(String[] args) {
		
		try {
			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		ChromeOptions options = new ChromeOptions();
		WebDriver driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
		
		driver.get("https://new.land.naver.com/complexes?ms=37.566427,126.977872,13&a=APT:ABYG:JGC&e=RETAIL");
		
		// 광역시 배너 클릭
		clickSelectionCity(wait);
		
		// 광역시 중 서울시 선택 -> 자동으로 구 선택을 넘어감
		selectSeoul(wait);

		// 총 구의 개수 확인
		int guSize = checkRegionSize(driver);
		
		// 구 개수 만큼 반복
		for(int i = 13 ; i < guSize ; i++) {
			// 서울인지 확인 다르면 처리
			// 추가 예정
			
//			// 구 목록이 닫혀잇으면
//			if (driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div")).getAttribute("aria-hidden").equals("true")) {
//				// 구 목록 열기
//				openGuSelection(wait);
//			}
			
			// 구 선택 -> 자동으로 동 선택으로 넘아감
			selectGu(wait, i);
			String guName = getGuName(driver);
			
			// 총 동의 개수 확인, 26
			int dongSize = checkRegionSize(driver);
			
			// 동 개수 만큼 반복
			for(int j = 1 ; j <= dongSize ; j++) {
				// 두번째 동부터
				if(j != 1) {
					// 지역 확인 후 바꼈으면 재설정
					compareAndRearrangeGu(driver, wait, guName, i);
				}
				
//				// 동 목록이 닫혀잇으면
//				if (driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div")).getAttribute("aria-hidden").equals("true")) {
//					// 동 목록 열기
//					openDongSelection(wait);
//				}
				
				// 동 선택 -> 자동으로 단지 선택으로 넘어감
				selectDong(wait, j);
				String dongName = getDongName(driver);
				
				// 총 단지의 개수 확인
				int complexSize = checkComplexSize(driver);
				
				// 단지 개수만큼 반복
				for (int k = 1 ; k <= complexSize ; k++) {
					// 두번째 단지부터
					if (k != 1) {
						// 지역 확인 후 바꼈으면 재설정
						compareAndRearrangeGu(driver, wait, guName, i);
						compareAndRearrangeDong(driver, wait, dongName, j);
					}
					
					// 단지 목록이 닫혀잇으면
					if (driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div")).getAttribute("aria-hidden").equals("true")) {
						// 단지 목록 열기
						openComplexSelection(wait);
					}
					
					// 단지 선택 - 자동으로 단지 정보로 펼쳐짐
					selectComplex(wait, k);
					
					// 아파트 단지명 수집
					String complexName = collectComplexName(wait);
					
					// 아파트 매매가 범위 수집
					String priceRange = collectComplexPriceRange(wait);
					
					// 아파트 단지 규모 수집
					String complexScale = collectComplexScale(wait);
					
				// try -> 학군 배너가 없는 경우 존재를 예외 처리
					// 학군 정보 배너 클릭 -> 배너가 없는 단지가 존재, 에러 처리 해야한다
					clickSchoolDataBanner(driver, wait);
					
					// 학교 이름 수집
					String schoolName = collectSchoolName(wait);
					
					// 초등학교까지 거리 수집
					String schoolData = collectSchoolData(wait);
					String distance = " ";
					String[] datas = schoolData.split("\n");
					for (String data : datas) {
						if (data.contains("도보로")) {
							distance = data;
						}
					}
				//	
				
					System.out.println(guName + " - " + dongName + " - " + complexName + " - " + priceRange + " - " + complexScale + " // " + schoolName + " - " + distance);
					
					// 현재 단지 닫기
					closeComplexInformation(wait);
				}
			}
		}
	}

	private static String collectSchoolName(WebDriverWait wait) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"detailContents5\"]/div[1]/div[1]/div[1]/h5"))).getText();
	}

	private static String collectComplexScale(WebDriverWait wait) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"summaryInfo\"]/dl"))).getText();
	}
	
	private static String collectComplexPriceRange(WebDriverWait wait) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"summaryInfo\"]/div[2]/div[1]/div/dl[1]/dd"))).getText();
	}
	
	private static String collectComplexName(WebDriverWait wait) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"complexTitle\"]"))).getText();
	}

	private static void openComplexSelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div[1]/div/div/a/span[4]"))).click();
		wait500ms();
	}

	private static void compareAndRearrangeDong(WebDriver driver, WebDriverWait wait, String dongName, int j) {
		// 현재 지도 상 설정된 동의 이름 확인
		String CurrentDongName = driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/a/span[3]")).getText();
		// 수집중이던 동과 현재 설정된 동이 다른지 확인, 다르면 동선택 열고 수집중이던 동 선택
		if (!dongName.equals(CurrentDongName)) {
			openDongSelection(wait);
			wait500ms();
			selectDong(wait, j);
		}
	}

	private static void openDongSelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[3]"))).click();
		wait500ms();
	}

	private static void compareAndRearrangeGu(WebDriver driver, WebDriverWait wait, String guName, int i) {
		String CurrentGuName = driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/a/span[2]")).getText();
		if (!guName.equals(CurrentGuName)) {
			openGuSelection(wait);
			wait500ms();
			selectGu(wait, i);
		}
	}

	private static void openGuSelection(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/a/span[2]"))).click();
		wait500ms();
	}

	private static String getDongName(WebDriver driver) {
		return driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div/div[1]/div/a[3]")).getText();
	}

	private static String getGuName(WebDriver driver) {
		return driver.findElement(By.xpath("//*[@id=\"region_filter\"]/div/div/div[1]/div/a[2]")).getText();
	}

	private static void closeComplexInformation(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/button"))).click();
		wait500ms();
	}

	private static String collectSchoolData(WebDriverWait wait) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"detailContents5\"]/div/div[1]"))).getText();
	}

	private static void clickSchoolDataBanner(WebDriver driver, WebDriverWait wait) {
		// 총 배너의 개수 확인
		int size = driver.findElements(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div/div/a")).size();
		// 모든 배너 중 학군정보 배너가 있는지 확인, 있으면 클릭
		for(int i = 1 ; i <= size ; i++) {
			String content = driver.findElement(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div/div/a[" + i + "]/span")).getText();
			if(content.equals("학군정보")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div[2]/div/div[2]/div[2]/div/div/a[" + i + "]"))).click();
				wait500ms();
				break;
			}
		}
	}

	private static void selectComplex(WebDriverWait wait, int complexIndex) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/div/div[3]/ul/li[" + complexIndex + "]/a"))).click();
		wait500ms();
	}
	
	private static int checkComplexSize(WebDriver driver) {
		return driver.findElements(By.xpath("//*[@class=\"complex_item\"]")).size();
	}
	
	// @@@@@@@@@@@@@@@@
	private static void selectDong(WebDriverWait wait, int dongIndex) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/div/div[2]/ul/li[" + dongIndex + "]/label"))).click();
	}

	private static int checkRegionSize(WebDriver driver) {
		return driver.findElements(By.xpath("//*[@class=\"area_item\"]")).size();
	}

	private static void clickSelectionCity(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/a/span[1]"))).click();
		wait500ms();
	}

	private static void selectSeoul(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/section/div[2]/div/div[1]/div/div/div/div[2]/ul/li[1]/label"))).click();
		wait500ms();
	}

	public static void selectGu(WebDriverWait wait, int guIndex) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"region_filter\"]/div/div/div[2]/ul/li[" + guIndex + "]/label"))).click();
		wait500ms();
	}
	
	public static void wait500ms() {
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}