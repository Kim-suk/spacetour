let tossPayments;

fetch("/api/config/toss-client-key")
  .then(response => response.text())
  .then(key => {
    tossPayments = TossPayments(key);

    const button = document.getElementById('toss-widget-button');
    button.addEventListener('click', () => {
      let amount = parseInt(button.dataset.amount);

      // 쿠폰 적용 (5,000원 할인)
      const couponCheckbox = document.getElementById('coupon-box');
      if (couponCheckbox.checked) {
        amount = Math.max(0, amount - 5000);
      }

      const loginId = button.dataset.loginId;
      const orderId = button.dataset.orderId;
      const planet = button.dataset.planet;

      if (!amount || !loginId || !planet || !orderId) {
        console.error("❌ 결제 정보 부족", { amount, loginId, planet, orderId });
        alert("❌ 결제 정보가 올바르지 않습니다. 로그인 상태를 확인하거나 페이지를 새로고침 해주세요.");
        return;
      }

      tossPayments.requestPayment('CARD', {
        orderId: orderId,
        amount: amount,
        customerName: loginId,
        orderName: planet,
        successUrl: 'http://localhost:8080/payment/success',
        failUrl: 'http://localhost:8080/payment/fail'
      }).catch(error => {
        console.error("결제 오류:", error);
        alert("결제 요청 중 오류가 발생했습니다.");
      });
    });
  })
  .catch(error => {
    console.error("TossPayments 키를 불러오는데 실패했습니다:", error);
    alert("결제 준비 중 오류가 발생했습니다.");
  });
