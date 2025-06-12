let idChecked = false;

function checkId() {
  const id = document.getElementById("id").value.trim();
  if (id === "") {
    alert("아이디를 입력하세요.");
    return;
  }

  const url = '/member/checkId?id=' + encodeURIComponent(id);

  fetch(url)
    .then(response => response.text())
    .then(text => {
      if (text === "DUPLICATE") {
        alert("이미 사용 중인 아이디입니다.");
        idChecked = false;
      } else if (text === "OK") {
        alert("사용 가능한 아이디입니다.");
        idChecked = true;
      } else {
        alert("아이디 확인 중 오류가 발생했습니다.");
        idChecked = false;
      }
    })
    .catch(err => {
      console.error("fetch 오류:", err);
      alert("서버와 통신 중 오류가 발생했습니다.");
      idChecked = false;
    });
}

function fn_submit(){
   let frm = document.frm;
   
   let id = frm.id.value;
   let pwd = frm.pwd.value;
   let name = frm.name.value;
   let email = frm.email.value;
   
   if(id.length == 0 | id == ""){
      alert("아이디가 입력되지 않았습니다. 다시 입력하세요.");
      frm.id.focus();
   }else if(pwd.length == 0 || pwd == ""){
      alert("비밀번호가 입력되지 않았습니다. 다시 입력하세요.");
         frm.pwd.focus();
      }else if(name.length == 0 || name == ""){
         alert("이름이 입력되지 않았습니다. 다시 입력하세요.");
            frm.name.focus();
         }else if(email.length == 0 || email == ""){
            alert("이메일이 입력되지 않았습니다. 다시 입력하세요.");
               frm.email.focus();
            }else {
               frm.action = "/member/joinMember";
               frm.method="post";
               frm.submit();
            }
   }

// 비밀번호 유효성 검사
function validateAndCheckPassword() {
  const pwd = document.getElementById("pwd").value;
  const pwdCheck = document.getElementById("pwdCheck").value;
  const pwdMsg = document.getElementById("pwdMsg");
  const pwdCheckMsg = document.getElementById("pwdCheckMsg");

  const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d!@#$%^&*()_+]{8,20}$/;

  // 비밀번호 유효성 검사
  if (!regex.test(pwd)) {
    pwdMsg.innerText = "영문+숫자 포함 8~20자 입력해주세요.";
    pwdMsg.style.color = "red";
  } else {
    pwdMsg.innerText = "사용 가능한 비밀번호입니다.";
    pwdMsg.style.color = "lightgreen";
  }

  // 비밀번호 일치 확인
  if (pwdCheck !== "") {
    if (pwd === pwdCheck) {
      pwdCheckMsg.innerText = "비밀번호가 일치합니다.";
      pwdCheckMsg.style.color = "lightgreen";
    } else {
      pwdCheckMsg.innerText = "비밀번호가 일치하지 않습니다.";
      pwdCheckMsg.style.color = "red";
    }
  } else {
    pwdCheckMsg.innerText = "";
  }
}

function sendPhoneCode() {
  console.log("sendPhoneCode() 호출됨");
  const phoneInput = document.getElementById("phone");
  const phone = phoneInput ? phoneInput.value.trim() : "";

  if (!phone) {
    alert("전화번호를 입력하세요.");
    phoneInput && phoneInput.focus();
    return;
  }

  // 간단한 전화번호 형식 체크 (숫자만 9~11자리 등, 필요하면 수정)
  const phonePattern = /^[0-9]{9,11}$/;
  if (!phonePattern.test(phone)) {
    alert("유효한 전화번호를 입력하세요 (숫자만, 9~11자리).");
    phoneInput.focus();
    return;
  }

  // contextPath가 있다면 여기에 넣으세요, 없으면 빈 문자열로
  const contextPath = ""; 

  fetch(`${contextPath}/auth/sendPhoneCode?phone=${encodeURIComponent(phone)}`, {
    method: "POST",
    headers: {
      "Accept": "application/json"
    }
  })
    .then(res => {
      if (!res.ok) {
        throw new Error(`서버 응답 오류: 상태 코드 ${res.status}`);
      }
      const contentType = res.headers.get("content-type");
      if (contentType && contentType.includes("application/json")) {
        return res.json();
      } else {
        return res.text().then(text => {
          throw new Error(`서버가 JSON이 아닌 응답을 보냈습니다:\n${text}`);
        });
      }
    })
    .then(data => {
      console.log("서버 응답 데이터:", data);
      if (data.success) {
        alert("인증번호가 전송되었습니다.");
      } else {
        alert("인증번호 전송 실패: " + (data.message || "알 수 없는 오류"));
      }
    })
    .catch(err => {
      console.error("fetch 에러:", err);
      alert("서버 통신 중 오류가 발생했습니다.\n" + err.message);
    });
}
  