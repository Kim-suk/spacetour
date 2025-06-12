document.addEventListener("DOMContentLoaded", () => {
  const canvas = document.getElementById("clock");
  const ctx = canvas.getContext("2d");
  let currentYear = new Date().getFullYear();
  let currentMonth = new Date().getMonth();
  let gmtOffset = 9;

  function drawClock() {
    const now = new Date(new Date().getTime() + gmtOffset * 3600000 - (new Date().getTimezoneOffset() * 60000));
    const hour = now.getHours();
    const minute = now.getMinutes();
    const second = now.getSeconds();

    ctx.clearRect(0, 0, 200, 200);
    ctx.beginPath();
    ctx.arc(100, 100, 90, 0, 2 * Math.PI);
    ctx.strokeStyle = "#0ff";
    ctx.lineWidth = 2;
    ctx.stroke();

    const drawHand = (length, angle, width) => {
      ctx.beginPath();
      ctx.moveTo(100, 100);
      ctx.lineTo(100 + length * Math.cos(angle), 100 + length * Math.sin(angle));
      ctx.strokeStyle = "#0ff";
      ctx.lineWidth = width;
      ctx.stroke();
    };

    drawHand(40, (Math.PI / 6) * ((hour % 12) + minute / 60) - Math.PI / 2, 4);
    drawHand(60, (Math.PI / 30) * minute - Math.PI / 2, 2);
    drawHand(70, (Math.PI / 30) * second - Math.PI / 2, 1);
  }

  function updateClockInputs() {
    const now = new Date(new Date().getTime() + gmtOffset * 3600000 - (new Date().getTimezoneOffset() * 60000));
    document.getElementById("hour").value = now.getHours();
    document.getElementById("minute").value = now.getMinutes();
  }

  function setCurrentTime() {
    updateClockInputs();
  }

  function updateGMT() {
    gmtOffset = parseInt(document.getElementById("gmtSelect").value);
  }

  document.getElementById("gmtSelect").addEventListener("change", () => {
    updateGMT();
    updateClockInputs();
  });

  setInterval(drawClock, 1000);
  updateClockInputs();
  updateGMT();

  const calendarEl = document.getElementById('calendar');
  const yearSelect = document.getElementById('yearSelect');
  const monthSelect = document.getElementById('monthSelect');
  const now = new Date();

  // 연도 셀렉트박스 생성 (1980~2050)  
  for (let y = 1980; y <= 2050; y++) {
    const opt = document.createElement('option');
    opt.value = y;
    opt.text = y;
    if (y === currentYear) opt.selected = true;
    yearSelect.appendChild(opt);
  }

  // 월 셀렉트박스 생성
  for (let m = 0; m < 12; m++) {
    const opt = document.createElement('option');
    opt.value = m;
    opt.text = `${m + 1}월`;
    if (m === currentMonth) opt.selected = true;
    monthSelect.appendChild(opt);
  }

  function renderCalendar(year, month) {
    calendarEl.innerHTML = '';
    const firstDay = new Date(year, month, 1).getDay();
    const lastDate = new Date(year, month + 1, 0).getDate();
    const days = ['일', '월', '화', '수', '목', '금', '토'];

    // 요일 헤더
    for (let d of days) {
      const dayEl = document.createElement('div');
      dayEl.className = 'day';
      dayEl.innerText = d;
      calendarEl.appendChild(dayEl);
    }

    // 빈칸
    for (let i = 0; i < firstDay; i++) {
      calendarEl.appendChild(document.createElement('div'));
    }

    // 날짜
    for (let d = 1; d <= lastDate; d++) {
      const dateEl = document.createElement('div');
      dateEl.className = 'date';
      dateEl.innerText = d;

      // 날짜 클릭 이벤트 (중복 제거해서 하나로 통합)
      dateEl.addEventListener('click', () => {
        const selectedDate = year + '-' + (month + 1).toString().padStart(2, '0') + '-' + d.toString().padStart(2, '0');
        document.getElementById("reserveDate").value = selectedDate;

        const date = new Date(year, month, d);
        const dayOfWeekNames = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
        const dayOfWeek = dayOfWeekNames[date.getDay()];
        document.getElementById("reserveDayOfWeek").value = dayOfWeek;

        // 선택된 날짜 강조
        const allDates = document.querySelectorAll('.calendar .date');
        allDates.forEach(el => el.style.backgroundColor = '');
        dateEl.style.backgroundColor = '#0ff';

        console.log('Selected date:', selectedDate, dayOfWeek);
      });

      calendarEl.appendChild(dateEl);
    }
  }

  // 초기 렌더
  renderCalendar(currentYear, currentMonth);

  // 월 변경 이벤트
  monthSelect.addEventListener('change', () => {
    renderCalendar(+yearSelect.value, +monthSelect.value);
  });
  yearSelect.addEventListener('change', () => {
    renderCalendar(+yearSelect.value, +monthSelect.value);
  });

  // 이전/다음 월 버튼
  document.getElementById('prevMonth').addEventListener('click', () => {
    if (currentMonth === 0) {
      currentYear--;
      currentMonth = 11;
    } else {
      currentMonth--;
    }
    yearSelect.value = currentYear;
    monthSelect.value = currentMonth;
    renderCalendar(currentYear, currentMonth);
  });

  document.getElementById('nextMonth').addEventListener('click', () => {
    if (currentMonth === 11) {
      currentYear++;
      currentMonth = 0;
    } else {
      currentMonth++;
    }
    yearSelect.value = currentYear;
    monthSelect.value = currentMonth;
    renderCalendar(currentYear, currentMonth);
  });

  // 예약 폼 제출 전에 시간도 포함시키기
  document.getElementById("planet").addEventListener("submit", function(event) {
    document.getElementById("reserveHour").value = document.getElementById("hour").value;
    document.getElementById("reserveMinute").value = document.getElementById("minute").value;

    document.querySelector('input[name="reserveDate"]').value = document.getElementById("reserveDate").value;
    document.querySelector('input[name="reserveDayOfWeek"]').value = document.getElementById("reserveDayOfWeek").value;

    document.querySelector('input[name="personCount"]').value = document.getElementById("personCount").value;
  });
});

// 인원수 입력 제한 및 폼에 값 반영
  const personCountInput = document.getElementById('personCountInput');
  const personCountHidden = document.getElementById('personCount');
  const reserveDateInput = document.getElementById('reserveDate');
  const reserveDateForm = document.getElementById('reserveDateForm');
  const reserveDayOfWeekInput = document.getElementById('reserveDayOfWeek');
  const reserveDayOfWeekForm = document.getElementById('reserveDayOfWeekForm');
  const reserveHour = document.getElementById('reserveHour');
  const reserveMinute = document.getElementById('reserveMinute');

  // 최대 3명 제한 (추가 방어 코드)
  personCountInput.addEventListener('input', () => {
    let val = parseInt(personCountInput.value);
    if (isNaN(val) || val < 1) val = 1;
    else if (val > 3) val = 3;
    personCountInput.value = val;
    personCountHidden.value = val;
  });

  // 예약 폼에 날짜 및 시간 값 자동 반영
  function updateFormData() {
    personCountHidden.value = personCountInput.value;
    reserveDateForm.value = reserveDateInput.value;
    reserveDayOfWeekForm.value = reserveDayOfWeekInput.value;
    reserveHour.value = document.getElementById('hour').value;
    reserveMinute.value = document.getElementById('minute').value;
  }

  // 예시: 날짜 선택 시 input값 갱신하는 함수 (calendar.js 내부에서 호출 가능)
  function selectDate(dateStr, dayOfWeekStr) {
    reserveDateInput.value = dateStr;
    reserveDayOfWeekInput.value = dayOfWeekStr;
    updateFormData();
  }

  // 시간 입력 바뀔 때도 폼 값 갱신
  document.getElementById('hour').addEventListener('input', updateFormData);
  document.getElementById('minute').addEventListener('input', updateFormData);
  personCountInput.dispatchEvent(new Event('input')); // 초기값 세팅
