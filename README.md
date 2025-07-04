![스크린샷 2025-06-11 212356](https://github.com/user-attachments/assets/04a6c310-ec3f-4f35-bfc7-05bcf2e0a14c)

## 🗂️ 프로젝트 소개 🗂️
Space Travel Web 은 Spring Boot 3.4.5 를 사용하여 개발한 우주 여행 웹사이트입니다. 단순한 정보 제공을 넘어, 실시간 채팅 기능과 예약기능, 몰입형 인터랙션을 통해 사용자가 우주 여행을 실제로  경험하는 듯한 느낌을 받을 수 있도록 구성하였습니다. 또한 HTML, CSS, JavaScript 를 활용한 애니메이션과 3D  모델·영상  구현으로  시각적  몰입도를  높였습니다.  또한  외부  API  를  연동해  실시간 우주 정보와 우주 데이터를 제공함으로써 콘텐츠의 실제성과 활용도를 강화했습니다.
미래에는 지구뿐만 아니라 우주에서도 생활하고 여행을 떠나는 시대가 도래할 것입니다. 그런 시대를 대비해, 미래에 꼭 필요하게 될 웹사이트의 틀을 미리 구현해보고자 시작된 프로젝트입니다.

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

![스크린샷 2025-06-19 165649](https://github.com/user-attachments/assets/663c6d6f-5ff3-4f90-a84f-07fe9d1f5fb8)

-> 해당하는 아이디가 존재하면, "이미 사용중인 아이디입니다." 알림

![스크린샷 2025-06-19 165558](https://github.com/user-attachments/assets/bdabb310-16e9-409d-a38b-9bee57bd8aa8)

-> 해당하는 아이디가 존재하지 않으면, "사용 가능한 아이디입니다." 알림

![스크린샷 2025-06-19 165510](https://github.com/user-attachments/assets/863922b4-abe7-45a2-a80c-5425edc16595)

2️⃣ : 비밀번호는 정규식에 의해서, 비밀번호 보안을 한층더 강화

![스크린샷 2025-06-19 165837](https://github.com/user-attachments/assets/428b77b4-f172-4bc9-bc50-44d548befb2f)

3️⃣ : 이메일을 입력후 "이메일 중복 확인" 버튼을 눌러 해당하는 이메일이 존재하는지 안하는지 존재 여부를 알려줌.

![스크린샷 2025-06-19 170241](https://github.com/user-attachments/assets/e7368507-22fc-47fb-9baa-b4397b925ad8)

![스크린샷 2025-06-19 170350](https://github.com/user-attachments/assets/7a018bc0-0e65-4992-b65a-c615d6c6a94b)

4️⃣ : 이름, 생년월일, 성별 을 입력 및 선택 

5️⃣ : recaptcha API를 활용하여, 사람인지 봇인지 구별하기 위해 사용

6️⃣ : 휴대전화번를 입력후 "인증번호 전송"을 클릭, 전송받은 인증번호를 입력 후 "인증번호 확인" 버튼 클릭

7️⃣ : "주소찾기"버튼을 클릭하면 주소API를 이용한 우변펀호 와 도로명 주소, 지번을 한번에 입력 받을 수 있음. 

![스크린샷 2025-06-19 170834](https://github.com/user-attachments/assets/c0c3f6b1-a4a3-4c47-9bba-cc3fa9c4d583)

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
: 예약하기 페이지를 들어가면 들어가자마자 팝업창으로 안내문을 가장 먼저 띄워서 사용자들에게 강조해서 알려준다.

![image](https://github.com/user-attachments/assets/d2bf1697-20b7-4542-8c82-e3b84dc4aea2)

+ 우주 일정 선택
: 편도, 왕복, 출발지, 도착지, 출발일, 탑승인원(최대3인 제한) 선택, 왕복을 클릭하면 복귀일을 고를수 있는 칸이 생성된다.

![image](https://github.com/user-attachments/assets/05851266-4d2c-4740-aefc-441d3902a421)


+ '일정 선택 완료' 버튼을 누르면 내가 선택한 정보들을 알림을 통해서 알 수 있습니다.
![image](https://github.com/user-attachments/assets/9a4d72c3-ae52-4704-8fee-b70ca6d34aa7)


+**항공편 선택**

1️⃣ : '일정선택' 부분에서 선택했던, 출발지, 도착지, 춥발일, 복귀일을 보여주는 영역  
2️⃣ : 출발편 선택 부분 → 출발/도착시간, 우주비행선, 좌석등급 (ECONOMY, BUSINESS, FIRST) 3개중 1택  
3️⃣ : 복귀편 선택 부분 → 출발/도착시간, 우주비해언, 좌석등급 (ECONOMY, BUSINESS, FIRST) 3개중 1택  
4️⃣ : 왕복일 경우 출발편, 복귀편 좌석을 선택을 하면 [좌석선택] 버튼 활성화  

![image](https://github.com/user-attachments/assets/54fd0ac1-ddc7-47c7-9a78-0f64a472a613)

+ **출발편 선택**  
출발편 좌석을 선택하는 페이지 입니다.  
1️⃣ : 내가 선택한 좌석 등급이 ECONOMY이면, BUSINESS, FIRST 좌석은 선택이 불가능하고, ECONOMY 좌석만 선택이 가능합니다.
ECONOMY의 경우 예약 된 좌석은 좌석 선택이 안된고 '하늘색'좌석으로, 선택 가능한 좌석은 '검은색'좌석으로 그리고 선택한 좌석은 '보라색'으로 표현  
2️⃣ : 2번 영역에는 출발지, 도착지, 출발/도착(날짜, 시간), 인원수, 선택한 좌석의 번호랑 등급을 보여주는 영역

![image](https://github.com/user-attachments/assets/ddcd5c51-c2fc-49ce-8867-0730723a55b3)

+ **복귀편 선택**<br>
복귀편(왕복) 좌석을 선택하는 페이지 입니다.<br> 
1️⃣ : 내가 선택한 좌석 등급이 BUSINESS이면, ECONOMY, FIRST 좌석은 선택이 불가능하고, BUSINESS 좌석만 선택이 가능합니다.<br>
2️⃣ : 2번 영역에는 출발지, 도착지, 출발/도착(날짜, 시간), 인원수, 선택한 좌석의 번호랑 등급을 보여주는 영역<br>

![image](https://github.com/user-attachments/assets/9cff7b15-fbe4-42f1-9281-8a6499d2804d)

+ **예약 확인**<br>
내가 선택한 출발지, 도착지, 출발날짜, 도착날짜, 좌석등 내가 입력한 정보들을 보여주는 '예약확인'페이지 입니다.<br>
1️⃣ : 내가 선택한 출발편/복귀편, 그리고 '총 인원수'와 '총 가격'에 대한 정보를 보여주는 영역
![image](https://github.com/user-attachments/assets/aa9022d8-fd99-40d6-ae47-02898a4a490d)

1️⃣ : '우주여행 약관동의'영역이며, 해당하는 조항들을 다 열어서 동의한다는 [체크박스]를 체크해야지만, "위 약관에 모두 동의합니다."라는 체크박스에 체크가 됨<br>
![image](https://github.com/user-attachments/assets/283b392c-1c15-4910-ba24-90cb1ad5ed8b)

1️⃣ : '전자서명'을 해야지만 [예약확정] 버튼이 활성화 되고, [지우기] 버튼을 통해서 전자서명을 다시 할 수 있음.<br>
예약확정이 되면, "예약이 확정되었습니다.", "예약이 확정되었습니다! 즐거운 우주 여행 되세요 🚀" 라는 알림을 보냄
![image](https://github.com/user-attachments/assets/ca2774c6-f7b5-4635-ac38-d7d2ba25f34d)

+ **결제하기**<br>
결제하기는 두가지의 방식으로 구현(STRIPE 결제, TOSS 결제)<bR>

## [STRIPE 결제]
1️⃣ : STRIPE 결제 부분이며, 현재는 테스트 버전이므로 테스트가능한 '카드번호'를 입력
![image](https://github.com/user-attachments/assets/f3db283f-0b60-4176-badf-24f5af9a5739)

1️⃣ : 결제가 성공하면 보여지는 페이지, '주문번호'는 항상 랜덤으로 발행이되며, '결제금액','상품명','구매자명'을 보여줌
![image](https://github.com/user-attachments/assets/dc2ce649-14d2-4af3-a9ca-fd61e718f586)

## [TOSS 결제]
TOSS에서 테스트 가능한 결제 API를 사용함<br>
[다양한 결제방법] 버튼을 클릭한 후, '토스페이' 클릭, 개인정보 동의 체크박스 체크 후, [다음]버튼을 누르면 QR코드 발급<br>
QR코드를 이용해서 가상 결제 진행함<br>
![toss-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/3978c37c-6700-499a-b8d7-dcbf6f2f0385)

결제가 완료되었으면, 결제 완료 페이지로 넘어가며 해당하는 '주문번호','결제금액','상품명','구매자명'을 보여줌.
![image](https://github.com/user-attachments/assets/4f6457bd-e81e-47ec-a432-e72fa354c620)




</details>

<hr style="border: 3px solid #000;">

<details>
<summary>✅ 실시간 동승자 매칭(채팅) (chat)</summary>
  
+ 실시간 채팅 가능, 메세지 전송(프로필, 메세지 내용, 보낸시간), 채팅방 목록 렌더링(DB 저장)

![스크린샷 2025-06-21 010315](https://github.com/user-attachments/assets/9bd880ce-45ea-42be-804f-6df13be1c5a5)

+ 파일 업로드(미리보기, 사진 누르면 크게 보기 가능)

![스크린샷 2025-06-21 013650](https://github.com/user-attachments/assets/6325f444-0054-47b6-98f7-8a05fe446e50)

![스크린샷 2025-06-21 013723](https://github.com/user-attachments/assets/1c02ae82-03eb-4f87-baf0-a606d92cb0e9)


+ 채팅방 나가기(상대방에게 나갓다는 알림과, 상대방은 채팅 내역이 남아잇고, 나간사람은 채팅방이 사라짐과 동시에 기록도 모두 사라짐)

![스크린샷 2025-06-21 015717](https://github.com/user-attachments/assets/2b486feb-8c5a-49fc-a426-499c043b386a)

![스크린샷 2025-06-21 020040](https://github.com/user-attachments/assets/18863bf0-6824-4816-84d1-f11e17854230)

+ 이모지 전송

![스크린샷 2025-06-21 014139](https://github.com/user-attachments/assets/c1611828-71f9-4ec1-95a4-f2b42b70131d)

![스크린샷 2025-06-21 014455](https://github.com/user-attachments/assets/ab426afc-1e69-4e86-96fb-bc3867cdbdfc)

![스크린샷 2025-06-21 014204](https://github.com/user-attachments/assets/f246fab7-2e90-4637-a814-352c21408c6e)

+ GIF 파일 전송

![스크린샷 2025-06-20 230158](https://github.com/user-attachments/assets/6e60428b-5559-4f21-aa76-9a0ac242ff62)

+ 프로필 이미지 변경 , 저장 (프로필이미지 우클릭시)

![스크린샷 2025-06-21 012020](https://github.com/user-attachments/assets/780e22ea-a592-4b6c-baba-0381c5641146)

+ 실시간 현재 접속자 렌더링(페이지를 기준으로 채팅페이지를 나가면 접속자 목록에서 사라짐)

![스크린샷 2025-06-19 174134](https://github.com/user-attachments/assets/13a54afa-a720-4aa1-987a-978a13cff8e5)

</details>

<hr style="border: 3px solid #000;">

<details>
<summary>✅ 채팅 상세 기능(채팅)</summary>

+  전체보기, 복사, 답장, 공지,공유,나에게,삭제)
  
![스크린샷 2025-06-20 232228](https://github.com/user-attachments/assets/67fa62b4-5121-42c5-aa3e-4181ccca6d9e)

+ 나에게 기능 - 나와의 채팅과 연결(새로운 채팅방이 열림 , 서버연결, DB저장), 나와의 채팅방 안에서도 채팅내용 저장가능(메모장같은 역할)
  
![스크린샷 2025-06-21 004504](https://github.com/user-attachments/assets/5dc9935b-22d2-4be9-84eb-95afecb30447)

![스크린샷 2025-06-21 005658](https://github.com/user-attachments/assets/f83f097a-207a-43fa-97cb-bb069ce313d0)

![mechat](https://github.com/user-attachments/assets/bfe6aca6-106b-4918-80e1-37282245ad4f)

+ 전체 보기
: 메세지가 100자 이상일시에 더보기 버튼으로 모달 띄워줌

![스크린샷 2025-06-21 001337](https://github.com/user-attachments/assets/b7033664-b1e7-444a-9006-8244e5700698)

+ 공지 사항
: 중요 내용 또는 긴급한 내용을 사라지지않게 상단 또는 잘보이는 곳에 위치하도록 공지하는 기능 ( 채팅방 목록쪽에 배치하였다.)

![스크린샷 2025-06-21 011053](https://github.com/user-attachments/assets/08fb8ddb-94d2-4dec-86e2-970656644090)

![스크린샷 2025-06-21 011212](https://github.com/user-attachments/assets/6b44630c-6b91-4cf1-9322-20dff63b620e)

+ 공유 하기
: 원하는 상대방에게 공유하기 기능

![스크린샷 2025-06-21 003543](https://github.com/user-attachments/assets/2a83e98a-3b2e-4b21-8323-6b7569a8ca3e)

![스크린샷 2025-06-21 003643](https://github.com/user-attachments/assets/2ef5d948-210b-4548-8406-0cea34f8eaa3)

![스크린샷 2025-06-21 003732](https://github.com/user-attachments/assets/d1f4d98e-97a7-4934-abe8-93cd1dca9028)

+ 답장
; 메세지가 길어지거나 양이 많아질때 상대방의 이해력을 돕기위한 답장기능, 또는 그룹채팅에서도 쓰기 유용한 기능

![스크린샷 2025-06-21 002427](https://github.com/user-attachments/assets/9f4cb8d7-b660-480b-88cc-40d8ef1a5589)

![스크린샷 2025-06-21 002654](https://github.com/user-attachments/assets/a3e25a0c-6112-4260-a073-56101146f75e)

+ 메세지 삭제
  
![스크린샷 2025-06-21 010107](https://github.com/user-attachments/assets/bcee7d54-eb3f-4cac-82bb-c4ba1de42b3b)

+ 복사
: 복사를 누른 후 ctrl + v를 하면 붙여넣기가 된다.

</details>

<hr style="border: 3px solid #000;">

<details>
<summary>✅ 지구 기후 상황</summary>

![스크린샷 2025-06-21 021643](https://github.com/user-attachments/assets/e0e8df39-7bd4-4a69-a281-eb9f253865b9)

+ 원하는 나라를 3D지구본에서 선택하면 해당하는 나라의 날씨를 실시간으로가져옴( weather API)

![스크린샷 2025-06-21 020755](https://github.com/user-attachments/assets/c30154fa-cd68-4418-ad9a-cdfe41964b34)

+ 자동 완성 검색 기능

![스크린샷 2025-06-21 021212](https://github.com/user-attachments/assets/b5f719eb-04cb-4a9d-9f92-0a74d4a6ec12)

+ 해당하는 나라의 국기로 마커를 찍고 실시간으로 날씨를 불러온다.

![스크린샷 2025-06-21 021515](https://github.com/user-attachments/assets/aae9b5d2-9179-47f4-bfad-58808ebcbc38)

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
  
![스크린샷 2025-06-21 032949](https://github.com/user-attachments/assets/01f4ab53-270f-4a5c-8865-c888094956ef)


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
| **Backend** | ![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/SpringBoot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white) ![JPA](https://img.shields.io/badge/JPA-336791?style=for-the-badge&logo=hibernate&logoColor=white) ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white) ![WebSocket](https://img.shields.io/badge/WebSocket-333333?style=for-the-badge&logo=socketdotio&logoColor=white) ![STOMP](https://img.shields.io/badge/STOMP-6A1B9A?style=for-the-badge&logo=socketdotio&logoColor=white) ![JavaMail](https://img.shields.io/badge/JavaMail-007396?style=for-the-badge&logo=gmail&logoColor=white) | Java 21, Spring Boot 3.4.5, JPA, Hibernate, WebSocket, STOMP, JavaMail |
| **Frontend** | ![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white) ![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E) | HTML5, CSS3, JavaScript |
| **Database** | ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white) | MySQL, JDBC Driver: MySQL Connector/J |
| **Version Control** | ![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white) ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) | Git, GitHub |


## 🛠️ 개발 도구

| 구분 | 도구 | 설명 |
|:----:|:----:|:----|
| IDE | ![STS3](https://img.shields.io/badge/STS3-F7DF1E?style=for-the-badge&logo=Spring&logoColor=black) | Spring Tool Suite 3 (STS) |
| Database Tool | ![SQL](https://img.shields.io/badge/SQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=black) | MySQL |
| Server | ![Apache Tomcat](https://img.shields.io/badge/Apache_Tomcat-F8DC75?style=for-the-badge&logo=ApacheTomcat&logoColor=black) | Spring Boot 내장 서버 사용(Tomcat) |
| Build Tool | ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white) | Gradle 프로젝트 빌드 관리 |

<hr style="border: 3px solid #000;">

## 📅 프로젝트 기간
2025.05.12 ~ 2025.06.27

<hr style="border: 3px solid #000;">

## 📊 프로젝트 자료

### 🎞️ 스토리보드 (PPT)
- 서비스 흐름과 화면 기획 요약본
- 👉 ![image](https://github.com/user-attachments/assets/24d487cc-284c-41f0-953e-e620065a0f9b)

(https://docs.google.com/presentation/d/12QLhK0n4ehCRoFopw73NukH6HiY3r00X/edit?usp=drive_web&ouid=115265115342844139543&rtpof=true)



### 🎨 UI/UX 디자인 (Figma)
- 실제 화면 디자인 및 인터랙션
- 👉*FIGMA*
(https://www.figma.com/design/TwF9fVmaqF8oSR83Z5MBeW/%EC%88%98%ED%8F%89%EC%84%A0?node-id=0-1&p=f&t=VnWJGuVSjZvHg4VJ-0)](https://www.figma.com/design/TwF9fVmaqF8oSR83Z5MBeW/%EC%88%98%ED%8F%89%EC%84%A0?node-id=169-28&p=f&t=aRDsfEfDwGF4TQlH-0)
<hr style="border: 3px solid #000;">

## WBS 
- 개발 일정 관리
  
![image](https://github.com/user-attachments/assets/ab4986e6-1652-41a9-8495-add2f5b2e31f)

---
## 🧶ERD ( DB 설계)
![ERD](https://github.com/user-attachments/assets/b6b1a397-8bfd-49bb-a878-e45952142601)

