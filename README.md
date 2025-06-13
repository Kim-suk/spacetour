![스크린샷 2025-06-11 212356](https://github.com/user-attachments/assets/04a6c310-ec3f-4f35-bfc7-05bcf2e0a14c)

# 🚀 우주 여행
KG아이티뱅크에서 주관한 프로젝트로, Spring Boot 3.4.5를 사용하여 개발한 우주 여행 웹사이트입니다.
미래에는 지구뿐만 아니라 우주에서도 생활하고 여행을 떠나는 시대가 도래할 것입니다.
그런 시대를 대비해, 미래에 꼭 필요하게 될 웹사이트의 틀을 미리 구현해보고자 시작된 프로젝트입니다.

## 🗂️ 프로젝트 소개 🗂️
Space Travel Web은 누구나 우주 여행을 꿈꿀 수 있도록 기획된 차세대 웹 플랫폼입니다.
단순한 정보 제공을 넘어, 실시간 기능과 몰입형 인터랙션을 통해 사용자가 우주 여행을 실제로 경험하는 듯한 느낌을 받을 수 있도록 구성하였습니다.

🌌 여행 예약 시스템
여행을 가고 싶은 **행성**의 **출발지와 목적지**를 선택하고 **이코노미, 비즈니스, 퍼스트**중에서 선택한 후에 **날짜**와 **인원수**를 선택한다. 티켓을 확인하고 등급에 맞는 좌석을 고른후에 서명을 하고 결제를 하도록 구현하였습니다.
실시간 처리와 동시성 문제를 고려하여, 여러 사용자의 요청이 충돌 없이 처리될 수 있도록 설계하였습니다.

💬 실시간 채팅 기반 동승자 매칭 시스템
현재 접속중인 사용자가 누군지알 수 있도록 **실시간으로 채팅**하며 동승자를 찾는 목적뿐만 아니라 일반 채팅시스템의 기능을 최대한으로 구현했습니다.
이는 기존 여행 예약 플랫폼과의 차별화된 기능으로, 소셜성과 사용자 경험을 동시에 강화합니다.

🌃 1. GIBS 기반 야간 도시 조명 및 ISS 실시간 위치 추적
좌측 화면에는 **NASA의 GIBS(Global Imagery Browse Services)**를 연동하여, 날짜별 지구 야간의 도시 불빛을 시각화합니다.
우측 화면에는 NSYO API를 통해 실시간 **국제우주정거장(ISS)**의 위치(예: 위도: 39.18, 경도: -54.27)를 확인할 수 있으며,
ISS의 실시간 움직임을 지도로 추적할 수 있어 우주를 실제로 관찰하는 듯한 체험을 제공합니다.

🌦️ 2. 지구 기후 상황 시각화 및 실시간 날씨 정보
사용자가 나라 이름을 입력하면 자동완성 검색 기능을 통해 해당 국가를 추천하며, 클릭 시 3D 지구본 상에 마커가 표시됩니다.
선택한 위치의 **실시간 기후 정보(날씨, 온도, 풍속 등)**를 확인할 수 있어,
여행 또는 우주 관측 시점에서의 기후 조건 시뮬레이션이 가능합니다.
또한 사용자는 3D 지구본을 자유롭게 회전 및 확대/축소하여 전 세계를 탐색하며, 관심 국가의 날씨를 실시간으로 조회할 수 있습니다.

🌍 Cesium 기반 3D 지구본 인터랙션
상단에 배치된 회전하는 3D 지구본은 단순한 장식이 아닌, 클릭 시 Cesium ion과 연동되어 CesiumJS와 satellite.js 라이브러리를 활용하여 국제우주정거장(ISS)의 실시간 위치와 궤도를 3D 지구 위에 시각화하는 웹 애플리케이션입니다.
CesiumJS는 고성능 3D 지구 및 지도 렌더링 라이브러리이며, satellite.js는 TLE 데이터를 기반으로 위성 위치를 계산하는 라이브러리입니다.

## ✨ 차별성과 경쟁력

-실시간성과 동시성에 최적화된 기능 설계

-NASA 위성 데이터와 Cesium 기술의 결합을 통한 현실과 가상의 융합

-여행 예약 + 소셜 매칭 기능을 접목한 인터랙티브 플랫폼

-교육, 체험, 커뮤니티 기능을 동시에 만족시키는 복합형 우주 웹사이트

## 👩‍💻 개발자 소개 👨‍💻

### 🛠️ 팀원
- 💎 **김수경**
  - [GitHub](https://github.com/Kim-suk)
  - 
- 🧩 **양평근**
  - [GitHub](https://github.com/ypk0680)

## 🔎 주요 기능

<details>
<summary>✅ 로그인 (Login)</summary>
+ 아이디 저장
+ 비밀번호 암호화로 db에 저장
+ 아이디 찾기(javamail, google stomp사용)
+ 정보 일치 불일치 판졀 -> 보안코드 입력력
</details>

<hr style="border: 3px solid #000;">

<details>
<summary>✅ 회원 가입(sign up)</summary>
+ 아이디 중복 검사
+ 비밀번호 유효성 검사
+  recaptcha  API, firebase 사용 -> 봇인증, 인증번호 전송
+  주소 API 



</details>

<hr style="border: 3px solid #000;">
<details>
<summary>✅ 정보 수정 (My page)</summary>

+ 회원 정보 수정  
: 이름, 나이, 이메일

![image](https://github.com/user-attachments/assets/b1624cdb-8f3e-4478-a085-122e2dd10217)

+ 비밀번호 변경  
: 기존 비밀번호 입력 후 새 비밀번호 입력

![image](https://github.com/user-attachments/assets/df7844ea-8f56-4da5-8ff7-79e4ae28abe5)

+ 회원 탈퇴  
: 현재 비밀번호 입력 후 회원탈퇴 가능

![image](https://github.com/user-attachments/assets/9e2ce22f-4f0b-4eca-802b-130a2b1511b3)

</details>

<hr style="border: 3px solid #000;">

<details>
<summary>✅ 예약하기(ticket) </summary>

+ 현재 위치 기반으로 반경 1km에 있는 동물병원 위치 확인 가능

![image](https://github.com/user-attachments/assets/81039edf-e25c-475c-9c22-65d1443570b1)

+ 마킹 되어있는 동물병원 클릭 시 상세 정보 확인 가능

![image](https://github.com/user-attachments/assets/042a80ef-4af7-45ee-bd05-93d09adf6595)

</details>

<hr style="border: 3px solid #000;">

<details>
<summary>✅ 실시간 동승자 매칭(채팅) (chat)</summary>

+ websocket, stomp사용
+ 실시간 채팅 가능
+ 메세지 전송(프로필, 메세지 내용, 보낸시간)
+ 채팅방 목록 렌더링(DB 저장)
+ 파일 업로드(사진 누르면 크게 보기 가능)
+ 채팅 내역 상세 기능( 메세지 우 클릭시 전체보기, 복사, 답장, 공지,공유,나에게,삭제)  
+ 나에게 기능 - 나와의 채팅과 연결(새로운 채팅방이 열림 , 서버연결, DB저장)
+ 채팅방 나가기(상대방에게 나갓다는 알림과, 상대방은 채팅 내역이 남아잇고, 나간사람은 채팅방이 사라짐과 동시에 기록도 모두 사라짐)
+ 프로필 이미지 변경 , 저장 (프로필이미지 우클릭시)
+ 실시간 현재 접속자 렌더링(페이지를 기준으로 채팅페이지를 나가면 접속자 목록에서 사라짐)

</details>

<hr style="border: 3px solid #000;">

<details>
<summary>✅ 지구 기후 상황</summary>

+ 원하는 나라를 3D지구본에서 선택하면 해당하는 나라의 날씨를 실시간으로가져옴( weather API)

![결제 성공](https://github.com/user-attachments/assets/7a0f5066-5d21-47c6-8a16-f39774a4fbfc)

+ 자동 완성 검색 기능(날씨가 궁금한 나라의 이름을 자동완성 기능으로 찾으면 3D지구본이 자동으로 해당하는 나라에 마커를 찍어주고 그 나라도 실시간으로 날씨를 보여줌)

![결제 성공](https://github.com/user-attachments/assets/7a0f5066-5d21-47c6-8a16-f39774a4fbfc)

</details>
<hr style="border: 3px solid #000;">

<details>
<summary>✅ GIBS </summary>

+ NASA API, N2YO API 사용
+ 왼쪽 화면 : NASA GIBS 로 야간 도시 불빛을 날짜 별로 볼 수 있음
+ 오른쪽 화면 : ISS (우주 정거장) 실시간으로 우주 정거장의 위치를 보여줌

</details>
<hr style="border: 3px solid #000;">

<details>
<summary>✅ Cesium Ion </summary>

+ Ceseium API 사용 -> 위성 실시간 위치 갱신

</details>
<hr style="border: 3px solid #000;">

## 🔍 참고 사이트 
- theplanetstoday : https://www.theplanetstoday.com/geocentric_orrery.html
- NASA : https://www.nasa.gov/
- 우주항공청 : https://xn--ob0b642ba552bc2ae1eb1jfke9sodrd16a.com/
- 우주환경센터: https://spaceweather.kasa.go.kr/main.do
- 한국 우주 과학회 : https://ksss.or.kr/
- KAI(한국항공우주산업) : https://www.koreaaero.com/KO/
- 한국 천문 연구원 : https://astro.kasi.re.kr/index
- 한국 항공 우주 연구원 : https://www.kari.re.kr/kor

<hr style="border: 3px solid #000;">

## ⚙ 사용 기술

| 구분 | 내용 | 설명 |
|:---|:---|:---|
| **Backend** | ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)  | Java 11, SpringBoot 3.4.5 |
| **Frontend** | ![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white) ![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E) | JSP, HTML5, CSS3, JavaScript |
| **Database** | ![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white) | Oracle (ojdbc8) |
| **Version Control** | ![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) | Git, GitHub |


## 🛠️ 개발 도구

| 구분 | 도구 | 설명 |
|:----:|:----:|:----|
| IDE | ![STS3](https://img.shields.io/badge/STS3-F7DF1E?style=for-the-badge&logo=Spring&logoColor=black) | Spring Tool Suite 3 |
| Database Tool | ![SQL](https://img.shields.io/badge/SQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=black) | Oracle, MySQL 등 사용 가능 |
| Server | ![Apache Tomcat](https://img.shields.io/badge/Apache_Tomcat-F8DC75?style=for-the-badge&logo=ApacheTomcat&logoColor=black) | Tomcat 서버 |
| Build Tool | ![Maven](https://img.shields.io/badge/Apache_Maven-C71A36?style=for-the-badge&logo=ApacheMaven&logoColor=white) | Maven 프로젝트 빌드 관리 |

<hr style="border: 3px solid #000;">

## 📅 프로젝트 기간
2025.05.12 ~ 2025.06.27

<hr style="border: 3px solid #000;">

## 🧭스토리 보드
*FIGMA*
https://www.figma.com/design/TwF9fVmaqF8oSR83Z5MBeW/%EC%88%98%ED%8F%89%EC%84%A0?node-id=0-1&p=f&t=VnWJGuVSjZvHg4VJ-0

<hr style="border: 3px solid #000;">

## WBS 
C:\Users\82103\Documents\카카오톡 받은 파일\우주여행예약 WBS.xlsx

## 🗃 PRESENTATION
### 📎[계란고냥이](https://docs.google.com/presentation/d/1CY25JnAKzPIY2Xtao2h9KX9L9YvBFZdpKj--OTn5MUw/edit?slide=id.p1#slide=id.p1)

## 🧶ERD ( DB 설계)
![ERD](https://github.com/user-attachments/assets/b6b1a397-8bfd-49bb-a878-e45952142601)

