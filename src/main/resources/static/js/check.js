document.addEventListener("DOMContentLoaded", () => {
  const canvas = document.getElementById('signatureCanvas');
  const confirmButton = document.getElementById('confirm-button');
  const ctx = canvas.getContext('2d');
  const allTermsCheckbox = document.getElementById('terms-checkbox');
  const termCheckboxes = document.querySelectorAll('.term-checkbox');
  const accordionHeaders = document.querySelectorAll('.accordion-header');

  let drawing = false;

  // 아코디언 토글
  accordionHeaders.forEach(header => {
    header.addEventListener('click', () => {
      header.classList.toggle('active');
      const content = header.nextElementSibling;
      content.classList.toggle('open');
    });
  });

  // 서명 이벤트 등록
  canvas.addEventListener('mousedown', startDraw);
  canvas.addEventListener('mouseup', endDraw);
  canvas.addEventListener('mouseout', endDraw);
  canvas.addEventListener('mousemove', draw);
  canvas.addEventListener('touchstart', startDraw, { passive: false });
  canvas.addEventListener('touchend', endDraw);
  canvas.addEventListener('touchmove', draw, { passive: false });

  function getMousePos(e) {
    if (e.touches) {
      return {
        x: e.touches[0].clientX - canvas.getBoundingClientRect().left,
        y: e.touches[0].clientY - canvas.getBoundingClientRect().top
      };
    }
    return {
      x: e.clientX - canvas.getBoundingClientRect().left,
      y: e.clientY - canvas.getBoundingClientRect().top
    };
  }

  // 서명 시작 전에 전체 동의 체크 확인
  function startDraw(e) {
    if (!allTermsCheckbox.checked) {
      alert("서명을 위해서는 전체 약관 동의가 필요합니다.");
      return;
    }
    drawing = true;
    ctx.beginPath();
    const pos = getMousePos(e);
    ctx.moveTo(pos.x, pos.y);
    e.preventDefault();
  }

  function draw(e) {
    if (!drawing) return;
    const pos = getMousePos(e);
    ctx.lineTo(pos.x, pos.y);
    ctx.strokeStyle = '#000';
    ctx.lineWidth = 2;
    ctx.stroke();
    checkFormValidity();
    e.preventDefault();
  }

  function endDraw() {
    drawing = false;
  }

  function clearSignature() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    checkFormValidity();
  }

  function isSignaturePresent() {
    const pixels = ctx.getImageData(0, 0, canvas.width, canvas.height).data;
    return [...pixels].some(channel => channel !== 0);
  }

  // 초기 상태: 전체 동의 비활성화, 버튼 비활성화, 서명 막기
  allTermsCheckbox.disabled = true;
  confirmButton.disabled = true;
  canvas.style.pointerEvents = 'none';

  // 전체 동의 체크박스 변화
  allTermsCheckbox.addEventListener('change', () => {
    if (allTermsCheckbox.checked) {
      confirmButton.disabled = false;
      canvas.style.pointerEvents = 'auto';
    } else {
      confirmButton.disabled = true;
      canvas.style.pointerEvents = 'none';
    }
  });

  // 개별 약관 체크박스 변화
  termCheckboxes.forEach(cb => {
    cb.addEventListener('change', () => {
      const allChecked = Array.from(termCheckboxes).every(c => c.checked);
      if (allChecked) {
        allTermsCheckbox.disabled = false;
        allTermsCheckbox.checked = true;
        confirmButton.disabled = false;
        canvas.style.pointerEvents = 'auto';
      } else {
        allTermsCheckbox.checked = false;
        allTermsCheckbox.disabled = true;
        confirmButton.disabled = true;
        canvas.style.pointerEvents = 'none';
      }
    });
  });

  // 최종 유효성 검사 (모든 약관 + 서명 존재)
  function checkFormValidity() {
    const allChecked = Array.from(termCheckboxes).every(cb => cb.checked);
    confirmButton.disabled = !(allChecked && isSignaturePresent());
  }

  // 예약 완료 버튼 클릭
  confirmButton.addEventListener('click', () => {
    if (!confirmButton.disabled) {
      alert("예약이 확정되었습니다! 즐거운 우주 여행 되세요 🚀");
      window.location.href = '/payment/payment';
    }
  });
});
