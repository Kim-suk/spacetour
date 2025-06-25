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
좌측 화면에는 **NASA의 GIBS**(Global Imagery Browse Services)를 연동하여, 날짜별 지구 야간의 도시 불빛을 시각화합니다.
우측 화면에는 **NSYO API**를 통해 실시간 **국제우주정거장(ISS)**의 위치(예: 위도: 39.18, 경도: -54.27)를 확인할 수 있으며,
ISS의 실시간 움직임을 지도로 추적할 수 있어 우주를 실제로 관찰하는 듯한 체험을 제공합니다.

🌦️ 2. 지구 기후 상황 시각화 및 실시간 날씨 정보
사용자가 나라 이름을 입력하면 자동완성 검색 기능을 통해 해당 국가를 추천하며, 클릭 시 3D 지구본 상에 마커가 표시됩니다.
선택한 위치의 **실시간 기후 정보(날씨, 온도, 풍속 등)**를 확인할 수 있어,
여행 또는 우주 관측 시점에서의 기후 조건 시뮬레이션이 가능합니다.
또한 사용자는 **3D 지구본**을 자유롭게 회전 및 확대/축소하여 전 세계를 탐색하며, 관심 **국가의 날씨를 실시간으로 조회**할 수 있습니다.

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
  
- 🧩 **양평근**
  - [GitHub](https://github.com/ypk0680)

## 🔎 주요 기능

<details>
<summary>✅ 로그인 (Login)</summary>
  
+ 아이디 저장
---
![id_save](https://github.com/user-attachments/assets/be34a541-e7ea-40bb-a40d-964cf39a6288)

   아이디를 입력 후 나갔다 들어와도 저장이 되어있어서 다시 입력하지 않아도 된다.
+ 비밀번호 암호화로 db에 저장
  ![image](https://github.com/user-attachments/assets/d1344818-abd4-4175-acfb-ba31e5d93d6f)

+ 아이디 찾기(javamail, google stomp사용)
  ![image](https://github.com/user-attachments/assets/d0a81648-9c14-4eb9-a829-2ad49afe6cc3)

![email](https://github.com/user-attachments/assets/5fd025e3-3cda-4609-809d-fe055e7391c5)
![image](https://github.com/user-attachments/assets/f4f876b8-c92f-4e8a-97e0-1e2e267c2377)
![image](https://github.com/user-attachments/assets/16be4846-c74b-47c4-8b0a-408dd36b6e96)

+ 정보 일치 불일치 판별 -> 보안코드 입력

아이디 또는 비밀번호를 5회이상 틀릴시 보안코드입력창으로 자동 전환

![image](https://github.com/user-attachments/assets/c3615e76-8ce9-416f-bc24-6fb980a6818f)

![image](https://github.com/user-attachments/assets/90ec5174-3a73-4f49-95de-442f215130de)

![스크린샷 2025-06-13 195827](https://github.com/user-attachments/assets/7766917c-6f5e-457b-a404-7ad7d975c460)


보안코드 올바르게 입력시 다시 로그인 화면으로 자동 전환

![image](https://github.com/user-attachments/assets/aa2c42a2-4e7b-47c6-a403-4646c1d43238)

+ 비밀번호 찾기(javamail,Google STOMP)
아이디와 이메일을 입력하면 해당하는 이메일로 임시비밀번호를 발급해 줍니다.

![findpassword-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/19f5f0b7-c332-4dc6-8968-f128d174b41d)

해당하는 메일로 전송된 '임시비밀번호'를 복사해서, '임시비밀번호입력'란에 입력한 후 '임시 비밀번호 확인' 버튼 클릭!

     -> '임시비밀번호'가 맞다면 '비밀번호변경' 페이지로 이동!!

![temppassword-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/ee951712-cf75-4332-8282-363983b9c968)


'새 비밀번호' 와 '새 비밀번호 확인' 입력한 후에 '비밀번호 변경' 버튼을 누르면 비밀번호 변경 완료!

<img width="800" alt="image" src="https://github.com/user-attachments/assets/60baba44-b90f-4447-be27-89951d0cbe83" />
<img width="800" alt="image" src="https://github.com/user-attachments/assets/d1b5f5bd-c149-4a7c-8e34-1548b1cec5c7" />





</details>

<hr style="border: 3px solid #000;">

<details>
<summary>✅ 회원 가입(sign up)</summary>
 아이디 중복 검사
+ 비밀번호 유효성 검사
+  recaptcha  API, firebase 사용 -> 봇인증, 인증번호 전송
+  주소 API 

1️⃣ : 아이디 입력후 아이디 중복여부를 확인하기 위해서 '아이디 중복확인' 클릭!! 

-> 해당하는 아이디가 존재하면, "이미 사용중인 아이디입니다." 알림

-> 해당하는 아이디가 존재하지 않으면, "사용 가능한 아이디입니다." 알림

2️⃣ : 비밀번호는 정규식에 의해서, 비밀번호 보안을 한층더 강화

3️⃣ : 이메일을 입력후 "이메일 중복 확인" 버튼을 눌러 해당하는 이메일이 존재하는지 안하는지 존재 여부를 알려줌.

4️⃣ : 이름, 생년월일, 성별 을 입력 및 선택 

5️⃣ : recaptcha API를 활용하여, 사람인지 봇인지 구별하기 위해 사용

6️⃣ : 휴대전화번를 입력후 "인증번호 전송"을 클릭, 전송받은 인증번호를 입력 후 "인증번호 확인" 버튼 클릭

7️⃣ : "주소찾기"버튼을 클릭하면 주소API를 이용한 우변펀호 와 도로명 주소, 지번을 한번에 입력 받을 수 있음. 

8️⃣ : 마지막으로 '이용약관' 체크박스를 누른 후, '회원가입'버튼을 누르면 정상적으로 "회원가입 완료"!!

<img width="800" alt="image" src="https://github.com/user-attachments/assets/f52281f5-2c8c-4e23-a4d5-c985e7c2133e" />




</details>

<hr style="border: 3px solid #000;">
<details>
<summary>✅ 정보 수정 (My page)</summary>

![image](https://github.com/user-attachments/assets/b35d478e-dd1d-4f3e-8105-25e2e8753e6a)
+ 프로필 이미지 변경/개인정보 수정 
: 로그인 ID, 회원명을 제외한 이메일, 전화번호, 주소만 정보수정 가능
  
![image](https://github.com/user-attachments/assets/c8480845-ccc4-4efe-9839-88712323cf2b)

![pro](https://github.com/user-attachments/assets/d9265bb0-559d-4f51-a85f-8e41cce64874)

+ 비밀번호 변경  
: 기존 비밀번호 입력 후 새 비밀번호 입력
: !! 여기서 다른점은 비밀번호 찾기 페이지와 동일한 페이지지만 **마이페이지에서 접속하였을 경우 현재 비밀번호를 입력하는 창이 나온다.**

![image](https://github.com/user-attachments/assets/a23c78b0-a04b-4d2a-931b-0f1d3f17bb6d)

![image](https://github.com/user-attachments/assets/24696e53-40ee-42ab-932c-58cd50a94230)

+ 회원 탈퇴  
: 마이페이지와 회원정보수정에서 탈퇴하기 가능
![image](https://github.com/user-attachments/assets/cf2d502a-35c7-46ad-b2de-f968881bb65a)

![image](https://github.com/user-attachments/assets/a5b9cf6b-4d23-473d-9fb6-f4a36c4cc9fc)

![image](https://github.com/user-attachments/assets/412a3612-1155-4ca5-af58-74f226570139)

![image](https://github.com/user-attachments/assets/0ca07265-9d8b-4575-b81a-8809dd50e428)

: 탈퇴가 완료된후에는 자동으로 메인페이지로 전환

+ 채팅 관리
: 나의 실시간 채팅방으로 연결
![image](https://github.com/user-attachments/assets/55df59b4-2ae2-4c7b-9106-25bbcf4ff847)

</details>

<hr style="border: 3px solid #000;">

<details>
<summary>✅ 예약하기(ticket) </summary>
+ 안내사항
+ 일정 선택
: 
+ 


</details>

<hr style="border: 3px solid #000;">

<details>
<summary>✅ 실시간 동승자 매칭(채팅) (chat)</summary>
  
+ 실시간 채팅 가능, 메세지 전송(프로필, 메세지 내용, 보낸시간), 채팅방 목록 렌더링(DB 저장)

![스크린샷 2025-06-21 010315](https://github.com/user-attachments/assets/9bd880ce-45ea-42be-804f-6df13be1c5a5)

+ 파일 업로드(사진 누르면 크게 보기 가능)
![스크린샷 2025-06-21 013650](https://github.com/user-attachments/assets/6325f444-0054-47b6-98f7-8a05fe446e50)

![스크린샷 2025-06-21 013723](https://github.com/user-attachments/assets/1c02ae82-03eb-4f87-baf0-a606d92cb0e9)

+ 채팅 내역 상세 기능( 메세지 우 클릭시 전체보기, 복사, 답장, 공지,공유,나에게,삭제)
![스크린샷 2025-06-20 232228](https://github.com/user-attachments/assets/67fa62b4-5121-42c5-aa3e-4181ccca6d9e)

+ 나에게 기능 - 나와의 채팅과 연결(새로운 채팅방이 열림 , 서버연결, DB저장), 나와의 채팅방 안에서도 채팅내용 저장가능(메모장같은 역할)
![스크린샷 2025-06-21 004504](https://github.com/user-attachments/assets/5dc9935b-22d2-4be9-84eb-95afecb30447)

![스크린샷 2025-06-21 005658](https://github.com/user-attachments/assets/f83f097a-207a-43fa-97cb-bb069ce313d0)



+ 채팅방 나가기(상대방에게 나갓다는 알림과, 상대방은 채팅 내역이 남아잇고, 나간사람은 채팅방이 사라짐과 동시에 기록도 모두 사라짐)
+ 프로필 이미지 변경 , 저장 (프로필이미지 우클릭시)
+ 실시간 현재 접속자 렌더링(페이지를 기준으로 채팅페이지를 나가면 접속자 목록에서 사라짐)

</details>

<hr style="border: 3px solid #000;">

<details>
<summary>✅ 지구 기후 상황</summary>

+ 원하는 나라를 3D지구본에서 선택하면 해당하는 나라의 날씨를 실시간으로가져옴( weather API)
자동 완성 검색 기능(날씨가 궁금한 나라의 이름을 자동완성 기능으로 찾으면 3D지구본이 자동으로 해당하는 나라의 국기로 마커를 찍어주고 그 나라도 실시간으로 날씨를 보여줌)
![image](https://github.com/user-attachments/assets/6c32a2a2-2fe5-47af-a316-a66ad6ebe6f8)



</details>
<hr style="border: 3px solid #000;">

<details>
<summary>✅ GIBS </summary>

+ NASA API, N2YO API 사용
+ 왼쪽 화면 : NASA GIBS 로 야간 도시 불빛을 날짜 별로 볼 수 있음
+ 오른쪽 화면 : ISS (우주 정거장) 실시간으로 우주 정거장의 위치를 보여줌
![image](https://github.com/user-attachments/assets/e88bd039-3a1d-4df4-9267-5b865770deb3)

![gibs](https://github.com/user-attachments/assets/ef2f1d1f-0b23-4c77-9834-4848ecbdea09)

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

