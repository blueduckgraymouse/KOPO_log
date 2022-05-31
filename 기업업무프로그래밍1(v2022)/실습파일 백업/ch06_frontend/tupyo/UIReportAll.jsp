<meta http-equiv="Content-Type" content="text/html; charset=utf8" />
	<!-- http-equiv 속성은 content 속성에 명시된 값에 대한 HTTP 헤더를 제공 -->
<%@ page contentType="text/html; charset=UTF-8" %>	<!-- 디렉티브 태그를 이용하여 생성할 문서의 컨텐츠 유형을 text/html로, 문자열세트를 utf-8로 지정-->
<%@ page import="java.sql.*, javax.sql.*, java.io.*, java.net.*, org.w3c.dom.*, javax.xml.parsers.*, java.text.DecimalFormat"%>
<html>
<head>
	<style>
		a {							/* a태그의 css속성 지정 */
			color : grey;			/* 글자 색 회색으로 지정 */
			text-decoration : none;	/* a태그 글자의 밑줄 삭제 */
		}
		a:hover {					/* a태그에 마우스가 올라왔을 때의 css속성 지정*/
			color : blue;			/* 글자 색 파란색으로 지정 */
		}		
		.banner {
			border : 1px blue solid;	/* 배너 클래스에 꽉 찬 바란색 경계선 두께 1px로 지정 & 폰트 사이즈 20px */
			font-size : 20px;
		}
		.btn {
			border-radius : 5px;	/* 버튼 클래스의 경계선 라운딩 처리, 너비 80, 높이25px, 배경색 녹색 지정 */
			width : 80px;
			height : 25px;
			background-color : green;
		}
		input[type=text], input[type=number] {	
			 border: 2px solid green;			/* 숫자와 텍스트 타입의 input태그의 경계선을 꽉 찬 녹색 2px로 지정 */
		}
		hr {
			float : left;						/* hr태그의 한줄 공간 차지를 변경하여 이어서 다른 태그나 html이 위치할 수 있도록 설정, 두께 15px로 지정 */
			height : 15px;
		}
		#bar tr {
			height : 50px;
		}
	</style>
	<%
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();		// DocumentBuilderFactory를 이용하여 DocumentBuilder객체 생성
		Document doc = docBuilder.parse("http://192.168.23.94:8002/ch06/tupyo/ReportAll.jsp");		// 생성한 DocumentBuilder객체에 xml파일의 위치 혹은 url를 지정하여 파싱한 결과를 Document객체에 저장
		
		NodeList tag_id = doc.getElementsByTagName("id");		// 태그이름이 id인 모든 태그를 nodelist형태로 묶어 변수에 저장
		NodeList tag_name = doc.getElementsByTagName("name");	// 태그이름이 name인 모든 태그를 nodelist형태로 묶어 변수에 저장
		NodeList tag_count = doc.getElementsByTagName("count");	// 태그이름이 count인 모든 태그를 nodelist형태로 묶어 변수에 저장
		NodeList tag_rate = doc.getElementsByTagName("rate");	// 태그이름이 rate인 모든 태그를 nodelist형태로 묶어 변수에 저장
	%>
	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>
	<div align="center">
		<table class="banner" border=1 cellspacing=1>
			<tr>
				<td><a href="#">후보등록</a></td>
				<td><a href="#">투표</a></td>
				<th bgcolor=yellow><a href="http://192.168.23.94:8003/ch06/tupyo/UIReportAll.jsp">개표결과</a></th>
			</tr>
		</table>
		
		<br>
		
		<table border=1 id="bar">
			<%	
				String labels = "";
				String datas = "";
				for (int i = 0 ; i < tag_id.getLength() ; i++) {
					int id = Integer.parseInt(tag_id.item(i).getFirstChild().getNodeValue());			// 모든 id인태그를 저장한 nodelist 객체에 i번째 node에 접근하여 요소를 변수에 저장
					String name = tag_name.item(i).getFirstChild().getNodeValue();						// 모든 name인태그를 저장한 nodelist 객체에 i번째 node에 접근하여 요소를 변수에 저장
					int count = Integer.parseInt(tag_count.item(i).getFirstChild().getNodeValue());		// 모든 count인태그를 저장한 nodelist 객체에 i번째 node에 접근하여 요소를 변수에 저장
					Double rate = Double.parseDouble(tag_rate.item(i).getFirstChild().getNodeValue());	// 모든 rate인태그를 저장한 nodelist 객체에 i번째 node에 접근하여 요소를 변수에 저장
					
					DecimalFormat form = new DecimalFormat("#.##");
					
					out.println("<tr><td><a href='http://192.168.23.94:8003/ch06/tupyo/UIReportOne.jsp?id_hubo=" + id + "'>" + id + ". " + name +"</a></td>");
					if(count == 0) {
						out.println("<td width=450 height=15>" + count + "표 (" + form.format(rate*100) + "%)</td>");	// 득표수가 0일 경우 득표수만 출력
					} else {
						out.println("<td width=450 height=15><hr color='red' width=" + (int)(rate*300) +
								" height=15>&nbsp;&nbsp;" + count + "표 (" + form.format(rate*100) + "%)</td>");		// 득표수가 0일 경우 득표수와 바 출력
					}
					
					
					labels = labels + "'" + id + "번. " + name + "'";
					if (i != tag_id.getLength()-1) {
						labels = labels + ",";
					}
					
					datas = "[" +  + "]";
				}
			%>
		</table>
		
		<canvas id="myChart" width="400" height="400"></canvas>	
	</div>
	
	<script>
		const ctx = document.getElementById('tupyo').getContext('2d');
		const myChart = new Chart(ctx, {
			type: 'bar',
			data: {
				labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],   <!-- 여기에 후보 기호와 이름 -->
				datasets: [{
					label: '# of Votes',
					data: [12, 19, 3, 5, 2, 3],
					backgroundColor: [
						'rgba(255, 99, 132, 0.2)',
						'rgba(54, 162, 235, 0.2)',
						'rgba(255, 206, 86, 0.2)',
						'rgba(75, 192, 192, 0.2)',
						'rgba(153, 102, 255, 0.2)',
						'rgba(255, 159, 64, 0.2)'
					],
					borderColor: [
						'rgba(255, 99, 132, 1)',
						'rgba(54, 162, 235, 1)',
						'rgba(255, 206, 86, 1)',
						'rgba(75, 192, 192, 1)',
						'rgba(153, 102, 255, 1)',
						'rgba(255, 159, 64, 1)' 
					],
					borderWidth: 1
				}]
			},
			options: {
				scales: {
					y: {
						beginAtZero: true
					}
				}
			}
		});
	</script>
</body>
</html>

 