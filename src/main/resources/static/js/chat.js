// 전역 변수 및 상수
// ----------------------------
let socket = null;
let stompClient = null;
let currentRoomId = null;
let selectedImageFile = null;
let selectedTextForPost = '';  // 인용할 메시지 저장용
let rooms = []; // 전역 채팅방 목록 배열
const unreadCounts = {}; // 방별 읽지 않은 메시지 수
let roomNotificationSettings = {}; // 방별 알림 설정
const MAX_FILE_SIZE_MB = 5;
const chatLogs = {};
const currentUser = $('#sender').val() || 'loginId';
// 중복 확인을 위한 roomList 배열 선언
let roomList = [];
let reconnectTimeout = null;
let isLoadingHistory = false;
let currentPage = 0;
const pageSize = 20;
let currentMessage = null;

const $chatLog = $('#chatLog');
const $contextMenu = $('#contextMenu');
const $messageInput = $('#messageInput');
const $sendBtn = $('#sendBtn');
const $fileInput = $('#fileInput');
const $emojiBtn = $('#emojiBtn');
const $emojiPicker = $('#emojiPicker');
const $loading = $('#loadingIndicator');
const token = localStorage.getItem('authToken'); // 또는 쿠키, 세션 저장소 등
let isFirstLoad = true; // ✅ 처음 로딩 여부 체크 변수


let lastMessageDate = null;
let selectedMessage = null; // 우클릭 시 선택된 메시지

// 초기화
$(function() {
  setupEventHandlers();
  loadChatRoomList();


  // 5초마다 목록과 읽지 않은 메시지 수 갱신
  setInterval(() => {
    loadChatRoomList();

    // loadPosts(currentRoomId);
   // loadExitedUsers(currentRoomId);
  }, 5000);

  setupEmojiPicker();

  // 공지사항 복원 추가
  const announcementBox = $("#announcementBox");
  const announcementText = $("#announcementText");
  const savedAnnouncement = localStorage.getItem('announce_' + currentRoomId);

  if (savedAnnouncement) {
    announcementText.text(savedAnnouncement);
    announcementBox.show();
  }
});

function connect() {
    const recipient = $('#recipient').val().trim();
    const roomId = $('#roomId').val().trim();

    if (!recipient) return alert('상대방 이름을 입력하세요.');

    if (stompClient && stompClient.connected) {
        if (currentRoomId === roomId) {
            return alert('이미 해당 채팅방에 연결되어 있습니다.');
        }
        stompClient.disconnect(() => {
            console.log('기존 연결 종료됨');
            startConnection(recipient);
        });
    } else {
        startConnection(recipient);
    }
}

/*// 연결 수행 함수
function proceedConnection(roomId, recipient, roomKey) {
    // roomList에 새 방 추가
    roomList.push({ roomId: roomId, recipient: recipient, roomKey: roomKey });

    // 실제 서버와의 연결 시작
    startConnection(recipient);

    // 채팅방 목록 UI에 표시하려면 여기서 호출
    addRoomToList(roomId, recipient);
}*/

// 웹소켓 연결 시작
function startConnection(roomId) {
    socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        currentRoomId = roomId;

        // 기존에 저장된 채팅 로그가 있으면 보여주기
        if (chatLogs[roomId]) {
            showMessagesList(chatLogs[roomId]);
        } else {
            $('#chatLog').empty();
            currentPage = 0;
            loadChatHistory(roomId);
        }

        // ✅ 해당 방 메시지 구독
        stompClient.subscribe(`/topic/chat/room/${roomId}`, (message) => {
            const parsedMessage = JSON.parse(message.body);
			// 💬 삭제된 메시지 처리
			  if (parsedMessage.deleted) {
			      const msgElement = document.querySelector(`[data-msgid="${parsedMessage.id}"]`);
			      if (msgElement) {
			        showSystemMessage(parsedMessage.deleted);// 또는 msgElement.innerText = "삭제된 메시지입니다";
			      }
			      return;
			  }
			// 퇴장 메시지 처리
		  const loginId = '${sessionScope.loginId}';  
			console.log('받은 메시지:', parsedMessage);  // 확인용 로그
			// 퇴장 메시지 처리 부분 예시
			if (parsedMessage.type === 'LEAVE') {
			    // 상대방 퇴장 메시지를 수신한 경우 출력
			    if (parsedMessage.sender !== loginId) {
			        showSystemMessage(parsedMessage.content);
			    }
			    return;
			}

            // ✅ 기본 메시지 처리
            onReceiveMessage(parsedMessage);
        });

        // 🔵 공지사항 구독
        stompClient.subscribe("/topic/notice", (message) => {
            renderNotice(JSON.parse(message.body));
        });

        // 🔵 공지 및 게시글 초기 불러오기 (주석처리됨)
       
        fetch("/api/notices")
            .then(res => res.json())
            .then(list => list.reverse().forEach(renderNotice));
       
        $('#sendBtn').prop('disabled', false);
        alert(`채팅방 "${roomId}"에 연결됨`);

		loadAnnouncementFromServer();
		
    }, (error) => {
        console.error('WebSocket 연결 실패:', error);
        if (reconnectTimeout) clearTimeout(reconnectTimeout);
        reconnectTimeout = setTimeout(() => {
            console.warn("재연결 시도 중...");
            startConnection(roomId); // 재연결 시도
        }, 3000);
    });
	
}
// 메시지 전송 함수
function sendMessage() {
  const sender = $('#sender').val().trim();
  const profilePic = $('#profileImage').attr('src') || '/image/profile/default-profile.png';
  const recipient = $('#recipient').val().trim();
  let content = $('#messageInput').val().trim();
  const quoted = $('#quotedText').text().trim();

  if (!sender || !recipient || (!messageInput && !selectedImageFile)) {
  	 	        return alert('모든 필드를 입력하거나 이미지를 선택해주세요.');
  	 	    }
  	 	

  if (!stompClient || !stompClient.connected) {
    alert('채팅방에 연결되어 있지 않습니다.');
    return;
  }

  const nl2br = str => str.replace(/\n/g, "<br>");
  if (quoted) {
    content = ` ▶ ${nl2br(quoted)}\n [답장]${nl2br(content)}`;
  } else {
    content = nl2br(content);
  }

  // 이미지가 있으면 이미지 먼저 전송
  if (selectedImageFile) {
    const textContent = content;
    const reader = new FileReader();
    reader.onload = (e) => {
      stompClient.send('/app/chat/send', {}, JSON.stringify({
        sender,
        profilePic,
        recipient,
        roomId: currentRoomId,
        content: e.target.result,
        type: 'image',
      }));

      if (textContent && !quoted) {
        stompClient.send('/app/chat/send', {}, JSON.stringify({
          sender,
          profilePic,
          recipient,
          roomId: currentRoomId,
          content: textContent,
          type: 'text',
        }));
      }
      $('#messageInput').val('');

      // 미리보기 숨기기 및 초기화
      $('#imagePreviewContainer').hide();
      $('#imagePreview').attr('src', '');
    };
    reader.readAsDataURL(selectedImageFile);

    selectedImageFile = null;
    $('#fileInput').val('');
  }

  // 텍스트 메시지 전송
  if (content) {
    stompClient.send('/app/chat/send', {}, JSON.stringify({
      sender,
      profilePic,
      recipient,
      roomId: currentRoomId,
      content,
      type: 'text',
    }));
    $('#messageInput').val('');
  }

  // 인용창 초기화
  $('#replyBox').hide();
  $('#quotedText').text('');
  $('#sendBtn').prop('disabled', true);
}

// 채팅방 선택 시 처리 (UI 및 연결)
function selectRoom(roomId, recipient) {
  if (currentRoomId === roomId) {
    console.log('이미 선택된 방입니다.');
    return;
  }

  $('#roomId').val(roomId);
  $('#recipient').val(recipient);

  loadChatRoomList();

  startConnection(roomId);
}

// 필요시 메시지 HTML 이스케이프 (보안)
function escapeHtml(text) {
    return text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

// 메시지 수신 처리 ---------------------------------------------
 function onReceiveMessage(message) {
  
    // leave 타입 메시지 처리
    if (message.type.toUpperCase() === 'LEAVE') {
        // leave 메시지는 알림으로 띄우기 (ex: 시스템 메시지 스타일)
		
        showLeaveMessage(message);
    } else {
        console.log('Showing message');
        showMessage(message);
        console.log('Message shown');
    }
}

// 채팅방 목록 렌더링 (필터 적용 및 읽지 않은 메시지 표시) -----------------------------
const meId = 'loginId';  // 현재 로그인한 사용자 ID

function renderRoomList(filterText = '') {
    const ul = $('#roomListUl');
    ul.empty();

    const seen = new Set();

    const filteredRooms = rooms.filter(room => {
        if (seen.has(room.id)) return false;
        seen.add(room.id);

        // 나를 제외한 상대방 찾기
        const opponent = room.participants.find(p => p.id !== meId);
        if (!opponent) return false;

        return opponent.name.toLowerCase().includes(filterText.toLowerCase());
    });

    if (filteredRooms.length === 0) {
        ul.append($('<li>').text('참여한 채팅방이 없습니다.').css({
            color: '#999',
            fontStyle: 'italic',
            padding: '10px',
            textAlign: 'center'
        }));
        return;
    }

    filteredRooms.forEach(room => {
        const opponent = room.participants.find(p => p.id !== meId);
        const li = $('<li>').text(opponent.name).css({
            cursor: 'pointer',
            padding: '10px',
            borderBottom: '1px solid #ccc'
        });

        li.on('click', () => {
            currentRoomId = room.id;
            renderRoomList($('#searchRoomInput').val());
            loadChatRoom(currentRoomId);
        });

        ul.append(li);
    });
}


// 여러 메시지 한꺼번에 출력
function showMessagesList(messages) {
    const chatLog = $('#chatLog');
    chatLog.empty();

    if (Array.isArray(messages)) {
        messages.forEach(msg => showMessage(msg));
    } else {
        console.error("messages는 배열이 아닙니다:", messages);
    }

    // 스크롤 아래로 이동 (DOM 요소에서 실행)
    setTimeout(() => {
        const chatLogEl = document.getElementById('chatLog');
        if (chatLogEl) {
            chatLogEl.scrollTop = chatLogEl.scrollHeight;
        }
    }, 0);
}

// 채팅방 목록 AJAX로 불러오기 (필터링, 알림 토글 포함)
function loadUserChatRooms() {
    $.ajax({
        url: '/chat/myChatRooms',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            if (Array.isArray(data)) {
                rooms = data; // 전역 rooms 배열 업데이트

                const $roomList = $('#roomList');
                $roomList.empty();

                const filter = $('#searchRoomInput').val().toLowerCase();

                data.forEach(room => {
                    if (room.name.toLowerCase().includes(filter)) {
                        const unreadCount = unreadCounts[room.id] || 0;
                        const notificationOn = roomNotificationSettings[room.id] !== false;

                        const $roomItem = $(`
                            <div class="chat-room" data-roomid="${room.id}">
                                <span class="room-name">${room.name}</span>
                                ${unreadCount > 0 ? `<span class="unread-count">${unreadCount}</span>` : ''}
                                <button class="toggle-notification">${notificationOn ? '🔔' : '🔕'}</button>
                            </div>
                        `);

                        $roomItem.on('click', () => connectToRoom(room.id));

                        $roomItem.find('.toggle-notification').on('click', e => {
                            e.stopPropagation();
                            roomNotificationSettings[room.id] = !notificationOn;
                            loadUserChatRooms();
                        });

                        $roomList.append($roomItem);
                    }
                });
            }
        },
        error: function(err) {
            console.error('채팅방 목록 로드 실패:', err);
        }
    });
}

// 채팅방 선택 시 처리
function selectRoom(roomId, recipient) {
    if (currentRoomId === roomId) {
        console.log('이미 선택된 방입니다.');
        return;
    }

    // 아직 currentRoomId 변경하지 않고 connect에 방 ID 전달
    $('#roomId').val(roomId);
    $('#recipient').val(recipient);

    loadChatRoomList();

    connect(roomId);
}

// 채팅방 목록 불러오기 (중복 제거 및 나간 방 제외)
function loadChatRoomList() {
    const leftRooms = JSON.parse(sessionStorage.getItem("leftRooms") || "[]");

    $.get('/chat/room/list', {
        userId: $('#sender').val(),
        keyword: $('#searchRoomInput').val().trim()
    }, function(rooms) {
        // 중복 제거: roomId를 키로 하는 Map
        const uniqueRoomsMap = new Map();
		
        rooms.forEach(room => {
            if (!leftRooms.includes(room.roomId) && !uniqueRoomsMap.has(room.roomId)) {
                uniqueRoomsMap.set(room.roomId, room);
            }
        });

        const ul = $('#roomListUl').empty();

        uniqueRoomsMap.forEach(room => {
            const unread = unreadCounts[room.roomId] || 0;

            const badgeHtml = unread > 0 ? `<span class="unread-badge">${unread}</span>` : '';

            ul.append(`
                <li style="cursor:pointer; display:flex; justify-content:space-between; align-items:center;"
                    onclick="selectRoom('${room.roomId}', '${room.recipient}')">
                    <span>${room.roomId} (${room.recipient})</span>
                    ${badgeHtml}
                </li>
            `);
        });
    }).fail(() => console.error('채팅방 목록 로딩 실패'));
}

// 채팅 히스토리 페이징 불러오기
function loadChatHistory(roomId) {
	const $chatLog = $('#chatLog');  // jQuery 객체 선언
// 💡 나간 방이면 기록 로딩 안 함
 const leftRooms = JSON.parse(sessionStorage.getItem("leftRooms") || "[]");
 if (leftRooms.includes(roomId)) {
     console.log(`방 ${roomId}는 나간 상태입니다. 기록을 불러오지 않습니다.`);
     return;
 }
 
  if (isLoadingHistory) return;
  isLoadingHistory = true;

  const chatLog = document.getElementById('chatLog');
  const previousScrollHeight = chatLog.scrollHeight;

  $.get(`/chat/${roomId}/history`, {
    page: currentPage,
    size: pageSize
  }, function(response) {
    if (Array.isArray(response) && response.length > 0) {
      // 가장 오래된 메시지가 먼저 나오도록 순서 그대로 사용
      response.forEach(msg => showMessage(msg));
      currentPage++;

   if (isFirstLoad) {
     // ✅ 부드럽게 스크롤 맨 아래로
	 $chatLog[0].scrollTo({ top: $chatLog[0].scrollHeight, behavior: 'smooth' });
     isFirstLoad = false;
   } else {
     // ✅ 이후 페이지 로딩 시, 기존 위치 유지
     const newScrollHeight = chatLog.scrollHeight;
     chatLog.scrollTop = newScrollHeight - previousScrollHeight;}
    } else {
      console.log("더 이상 불러올 메시지가 없습니다.");
    }
  }).fail(err => {
    console.error("채팅 기록 로딩 실패:", err);
  }).always(() => {
    isLoadingHistory = false;
  });
}
 
 // 이모지 생성
 const emojis = ['😀','😂','😍','😢','😡','👍','🙏','🎉','❤️','😎','🐱','🐶','🌟'];
 emojis.forEach(e => {
     $('<span>').text(e).css({ cursor: 'pointer', fontSize: '24px', margin: '3px' })
     .on('click', () => {
         const $input = $('#messageInput');
         $input.val($input.val() + e).focus();
         $('#emojiPicker').hide();
         $('#sendBtn').prop('disabled', !$input.val().trim());
     }).appendTo('#emojiPicker');
 });

 // 이벤트 핸들링
 $(document).ready(() => {
     loadChatRoomList();

     $('#connectBtn').click(connect);
     $('#sendBtn').click(sendMessage);
     $('#emojiBtn').click(e => {
         e.stopPropagation();
         $('#emojiPicker').toggle();
     });

     $('#messageInput').on('input', function() {
         $('#sendBtn').prop('disabled', !$(this).val().trim());
     });

     $(document).on('click', () => $('#emojiPicker').hide());
     $('#searchRoomInput').on('input', loadChatRoomList);
     setInterval(loadChatRoomList, 5000); // 자동 갱신
 });

// 메세지 송신 처리
function showMessage(message) {
    const currentUser = $('#sender').val().trim();
    const isSent = message.sender === currentUser;
    const $chatWindow = $('#chatLog');
    const msgDate = new Date(message.timestamp || Date.now());
    const dateStr = msgDate.toDateString();
    const timeStr = msgDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    // 중복 렌더링 방지
    if ($chatWindow.find(`[data-msgid="${message.id}"]`).length > 0) {
        return;
    }

    // 날짜 구분자 추가
    if (lastMessageDate !== dateStr) {
        lastMessageDate = dateStr;
        const today = new Date().toDateString();
        const displayDate = (dateStr === today) ? '오늘' : msgDate.toLocaleDateString();
        const $divider = $('<div>').addClass('date-divider').text(displayDate);
        $chatWindow.append($divider);
    }

    // 시스템 메시지: 퇴장 등
    if (message.type?.toUpperCase() === 'LEAVE') {
        const $leaveDiv = $('<div>')
            .addClass('system-message')
            .text(`${message.sender}님이 퇴장하셨습니다. (${timeStr})`);
        $chatWindow.append($leaveDiv);
        $chatWindow.scrollTop($chatWindow[0].scrollHeight);
        return;
    }

    // 메시지 컨테이너 생성
    const $msgDiv = $('<div>')
        .addClass('message')
        .addClass(isSent ? 'sent' : 'received')
        .attr('data-msgid', message.id);

    // 프로필 이미지 (받은 메시지인 경우만)
    if (!isSent) {
        const $profileImg = $('<img>')
            .addClass('profile-pic')
            .attr('src', message.profilePic || '/image/default-profile.png')
            .attr('alt', '프로필 사진');
        $msgDiv.append($profileImg);
    }

    // 메시지 내용 컨테이너
    const $contentDiv = $('<div>').addClass('content');

    // 보낸 사람 이름 표시 (받은 메시지일 때만)
    if (!isSent) {
        $contentDiv.append($('<strong>').text(`${message.sender}: `));
    }

    // 인용문 처리
    let content = message.content || "";
    if (message.quoted) {
        const quotedHtml = "▶ " + escapeHtml(message.quoted).replace(/\n/g, "<br>") + "<br>";
        content = quotedHtml + escapeHtml(content).replace(/\n/g, "<br>");
    } else {
        content = escapeHtml(content).replace(/\n/g, "<br>");
    }

    // 이미지 메시지 처리
    if (message.type?.toLowerCase() === 'image') {
        const $spinner = $('<div>').addClass('spinner');
        $contentDiv.append($spinner);

        const $img = $('<img>')
            .addClass('image-message')
            .attr('alt', '전송된 이미지')
            .css({ maxWidth: '200px', maxHeight: '150px', borderRadius: '8px', display: 'none' });

        $img.on('load', () => {
            $spinner.remove();
            $img.fadeIn(200);
        });
        $img.on('error', () => {
            $spinner.remove();
            $contentDiv.append($('<div>').text('이미지 로딩 실패').css('color', 'red'));
        });
        $img.attr('src', message.content);

        // 이미지 클릭 시 모달 오픈
        $img.on('click', () => {
            $('#modalImage').attr('src', message.content);
            $('#imageModal').fadeIn(200).css('display', 'flex');
        });

        $contentDiv.append($img);
    } else {
		// 텍스트 메시지 처리
		// 삭제된 메시지 처리
		  if (message.deleted) {
		      $contentDiv.text("삭제된 메시지입니다.");
		      $msgDiv.addClass("deleted");

		      // 이벤트 제거: 클릭/우클릭/인용 방지
		      $msgDiv.off('click contextmenu');
		      $contentDiv.off('click');
		  }

		  // 삭제되지 않은 경우 처리
		  else if (content.length > 300) {
		      const shortText = content.substring(0, 300) + '... ';
		      const $textSpan = $('<span>').html(shortText);
		      const $moreBtn = $('<button>').text('전체 보기')
		          .addClass('more-btn')
		          .addClass(isSent ? 'left' : 'right');

		      $moreBtn.on('click', () => {
		          $('#modalTextContent').html(content);
		          $('#textModal').fadeIn(200).css('display', 'flex');
		      });

		      $contentDiv.append($textSpan).append($moreBtn);
		  } else {
		      $contentDiv.html(content);
		  }
    }

    // 시간 표시
    const $timeSpan = $('<div>').addClass('time').text(timeStr);

    // 메시지에 내용과 시간 추가
    $msgDiv.append($contentDiv).append($timeSpan);

    // 메시지 컨텍스트 메뉴 (우클릭)
	if (!message.deleted) {
    $msgDiv.on('contextmenu', function (e) {
        e.preventDefault();
        selectedMessage = $(this);
        const selectedId = selectedMessage.data('msgid');
        console.log("선택된 메시지 ID:", selectedId || '없음');

        $('#contextMenu').css({
            top: e.pageY,
            left: e.pageX
        }).show();
    });
	}

    // 메시지 영역에 추가
    $chatWindow.append($msgDiv);
	// 부드럽게 스크롤 아래로 이동 (새 메시지 도착 시)
	const isScrolledNearBottom = ($chatWindow[0].scrollHeight - $chatWindow.scrollTop() - $chatWindow.outerHeight()) < 150;
	if (isScrolledNearBottom) {
	    $chatWindow[0].scrollTo({ top: $chatWindow[0].scrollHeight, behavior: 'smooth' });
	}
}

// 연결 종료 함수
function disconnect() {
    if (stompClient && stompClient.connected) {
        stompClient.disconnect(() => {
            console.log('WebSocket 연결 종료됨');
            $('#sendBtn').prop('disabled', true);
            $('#chatLog').empty();
        });
    } else {
        alert('연결된 채팅방이 없습니다.');
    }
}

// Enter 키로 메시지 전송 (Shift + Enter 제외)
$('#messageInput').on('keypress', function(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        if (!$('#sendBtn').prop('disabled')) {
            sendMessage();
        }
    }
});

// 🔔 공지 렌더링
function renderNotice(message) {
    const noticeArea = document.getElementById("notice-area");
    const el = document.createElement("div");
    el.classList.add("notice");
    el.innerHTML = `
        <strong>[공지]</strong> ${message.sender}: ${message.content}
        <span class="time">${new Date(message.timestamp).toLocaleString()}</span>
    `;
    noticeArea.appendChild(el);
}

function closeModal(index) {
  const modal = $('#modalContainer .chat-modal').eq(index);
  modal.hide();
  modal.find('.modal-body').html('');

  const visibleModals = $('#modalContainer .chat-modal:visible').length;
  if (visibleModals === 0) {
    $('#modalContainer').hide();
  }
}


// 이벤트 핸들러 등록
// ----------------------------
function setupEventHandlers() {
  $('#connectBtn').on('click', connect);
  $sendBtn.on('click', sendMessage);
  $emojiBtn.on('click', e => {
    e.stopPropagation();
    $emojiPicker.toggle();
  });

  // 우클릭 컨텍스트 메뉴 닫기
  $(document).on('click', () => {
    $contextMenu.hide();
    $emojiPicker.hide();
  });

  // 채팅방 검색 필터링
  $('#searchRoomInput').on('input', loadChatRoomList);
}

// jQuery 초기화 및 이모지 피커 설정
$(document).ready(function() {
  // 변수 정의
  const $emojiBtn = $('#emojiBtn');
  const $emojiPicker = $('#emojiPicker');
  const $messageInput = $('#messageInput');
  const $sendBtn = $('#sendBtn');

  // 이모지 목록 설정 함수
  function setupEmojiPicker() {
    // 버튼 클릭 시 토글
    $emojiBtn.on('click', function(e) {
      e.stopPropagation();
      $emojiPicker.toggle();
    });

    // 이모지 클릭 시 입력창에 추가
    $emojiPicker.on('click', '.emoji', function() {
      const emoji = $(this).text();
      $messageInput.val($messageInput.val() + emoji).focus();
      $emojiPicker.hide();
      $sendBtn.prop('disabled', !$messageInput.val().trim());
    });

    // 바깥 클릭 시 이모지창 닫기
    $(document).on('click', function() {
      $emojiPicker.hide();
    });

    // 이모지 목록 추가
    const emojis = ['😀','😂','😍','😎','😭','👍','🙏','🎉'];
    emojis.forEach(e => {
      $emojiPicker.append($('<span>').addClass('emoji').text(e).css({
        fontSize: '24px',
        cursor: 'pointer',
        margin: '3px'
      }));
    });
  }

  setupEmojiPicker();

  // 메시지 입력 시 전송 버튼 활성화
  $messageInput.on('input', function() {
    $sendBtn.prop('disabled', !$(this).val().trim());
  });
});


// 우클릭 외 영역 클릭 시 메뉴 닫기
 $(document).on("click", () => $("#contextMenu").hide());
 
//  전체 알림 숫자 업데이트 함수
function updateTotalUnreadCount() {
    let total = 0;
    for (const count of Object.values(unreadCounts)) {
        total += count;
    }
    $('.unread-count-header').text(`🔔 ${total}`);
}

$(document).ready(function () {
	$('#chatLog').on('click', function () {
	        unreadCount = 0;
	        $('.unread-count-header').text('🔔 0');
	    });
    $('#fileInput').on('change', function () {
        const file = this.files[0];
        if (!file) {
            selectedImageFile = null;
            $('#sendBtn').prop('disabled', !$('#messageInput').val().trim());
            $('#imagePreviewContainer').hide();
            return;
        }

        const allowedTypes = ["image/jpeg", "image/png", "image/gif"];
        if (!allowedTypes.includes(file.type)) {
            alert("허용되지 않은 이미지 형식입니다.");
            this.value = '';
            selectedImageFile = null;
            $('#imagePreviewContainer').hide();
            $('#sendBtn').prop('disabled', !$('#messageInput').val().trim());
            return;
        }

        if (file.size > 2 * 1024 * 1024) {  // 2MB 제한
            alert("이미지 크기는 2MB를 초과할 수 없습니다.");
            this.value = '';
            selectedImageFile = null;
            $('#imagePreviewContainer').hide();
            $('#sendBtn').prop('disabled', !$('#messageInput').val().trim());
            return;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            $('#imagePreview').attr('src', e.target.result);
            $('#imagePreviewContainer').show();
        };
        reader.readAsDataURL(file);

        selectedImageFile = file;
        $('#sendBtn').prop('disabled', false);
    });
});
	 			
	 // 모달 배경(바깥 영역) 클릭 시 모달 닫기
	 $('#imageModal').on('click', function (e) {
	   if (e.target.id === 'imageModal') {
	     $('#imageModal').fadeOut();
	     $('#clearImageBtn').hide();
	     document.body.style.overflow = 'auto';
	   }
	 });
	 
    // 프로필 사진 바에 프로필 추가 함수
     function addProfileToBar(sender, profilePic) {
         // 이미 있으면 패스
         if ($('#profileBar img[data-sender="' + sender + '"]').length) return;

         const img = $('<img>')
             .attr('src', profilePic || '/image/default-profile.png')
             .attr('title', sender)
             .attr('data-sender', sender)
             .on('click', () => alert(sender + ' 클릭! (필요시 채팅방 이동 등 추가 기능 가능)'));

         $('#profileBar').append(img);
     }  

   document.addEventListener("DOMContentLoaded", function () {
     const modal = document.getElementById("textModal");
     const modalOverlay = document.getElementById("modalOverlay");
     const closeBtn = document.getElementById("closeModal");
     const modalText = document.getElementById("modalTextContent");

     // 닫기 버튼 클릭 시
     closeBtn.onclick = function () {
       modal.style.display = "none";
       modalOverlay.style.display = "none";
     };

     // 바깥 영역 클릭 시
     window.onclick = function (event) {
       if (event.target == modalOverlay) {
         modal.style.display = "none";
         modalOverlay.style.display = "none";
       }
     };

	   function sendMessageToChat(message) {
	       const sender = $('#sender').val().trim();
	       const profilePic = $('#profileImage').attr('src') || '/image/profile/default-profile.png';
	       const currentRoom = currentRoomId; // 현재 채팅방 id 변수

	       if (!sender) {
	           alert('닉네임을 입력하세요.');
	           return;
	       }
	       if (!message) {
	           alert('보낼 메시지가 없습니다.');
	           return;
	       }
	       if (!currentRoom) {
	           alert('채팅방에 연결되어 있지 않습니다.');
	           return;
	       }

	       const messageObj = {
	           sender: sender,
	           content: message,
	           profilePic: profilePic,
	           roomId: currentRoom,
	           type: 'text',
	           timestamp: new Date().toISOString()
	       };

	       if (stompClient && stompClient.connected) {
	           stompClient.send(`/app/chat/${currentRoom}`, {}, JSON.stringify(messageObj));
	       } else {
	           alert('서버와 연결되어 있지 않습니다.');
	       }
	   }
   });

   $(document).ready(function() {
     let currentMessage = null;
	 // 우클릭 시 메뉴 표시
	 $('#chatLog').on('contextmenu', '.message', function(e) {
	   e.preventDefault();

	   // 현재 클릭된 메시지 저장
	   currentMessage = $(this); // 전체 요소 저장

	   // 선택한 메시지의 고유 ID를 콘솔에 출력하거나 변수로 활용
	   const msgId = currentMessage.data('msgid'); // data-msgid 값 가져오기
	   console.log('선택된 메시지 ID:', msgId);

	   // contextMenu에 해당 메시지 ID를 data 속성으로 저장 (필요 시)
	   $('#contextMenu').attr('data-msgid', msgId);

	   // 메뉴 위치 설정 및 표시
	   $('#contextMenu').css({
	     top: e.pageY + 'px',
	     left: e.pageX + 'px',
	     display: 'block'
	   });
	 });
	 
	 
function getMessageById(msgId) {
	   return messageList.find(msg => msg.id === msgId);
	 }

     // 메뉴 외 클릭 시 숨김
     $(document).on('click', function() {
       $('#contextMenu').hide();
     });
	 
	 // 답장 취소 버튼 클릭 이벤트 - 딱 한 번 바인딩
	   $('#cancelReply').on('click', function () {
	     $('#replyBox').hide();
	     $('#quotedText').text('');
	   });
	   
	   function getSelectedMessageId() {
	     return currentMessage ? currentMessage.data('msgid') : null;
	   }
	 // contextMenu 클릭 이벤트
	 $('#contextMenu li').on('click', function (e) {
	   const action = $(this).data('action');
	   const $textElement = currentMessage.find('.text');
	   const $currentMessage = window.currentMessage || null;

	   let textOnly = '';
	   if ($textElement.length) {
	     textOnly = $textElement.text().trim();
	   } else {
	     const cloned = currentMessage.clone();
	     cloned.find('.time').remove(); // 시간 제거
	     textOnly = cloned.text().trim();
	   }
	   console.log("switch문 진입 전");
	   switch (action) {
	     case 'copy':
	       navigator.clipboard.writeText(textOnly)
	         .then(() => alert("복사되었습니다."));
	       break;

	     case 'viewall':
	       $('#modalTextContent').text(textOnly);  // 모달에 텍스트 삽입
	       $('#modalOverlay, #textModal').show();  // 모달 표시
	       break;
		   
		   case 'reply':
		     const $replyBox = $('#replyBox');
		     const $quotedText = $('#quotedText');
		     const $messageInput = $('#messageInput');

		     // 메시지 ID 저장 없이 단순히 텍스트만 인용
		     $quotedText.text(textOnly); // ">"는 CSS에서 붙임
		     $replyBox.show();

		     $messageInput.val('');
		     setTimeout(() => $messageInput.focus(), 10);
			 
		     // 이전에 바인딩된 이벤트 제거 후 다시 바인딩 (중복 전송 방지)
		     $('#sendReply').off('click').on('click', function () {
		       const replyText = $messageInput.val().trim();
		       const quoted = $quotedText.text().trim();

		       if (replyText === '') {
		         alert("답장할 내용을 입력해주세요.");
		         return;
		       }

		       const messageHtml = `
		         <div class="message me">
		           <div class="quoted">▶ ${quoted}\n</div>
		           <div class="text">${replyText}</div>
		         </div>
		       `;
		       $('#chatLog').append(messageHtml);

		       // 초기화
		       $replyBox.hide();
		       $quotedText.text('');
		       $messageInput.val('');
		     });
		     break;

				 case 'toMe': {
				   // cloned 변수 선언과 초기화 필요
				   const cloned = currentMessage.clone(); // 예: currentMessage가 jQuery 객체라면 이렇게 복사

				   cloned.find('.time').remove(); // 시간 제거

				   if (!loginId) {
				     alert("로그인 정보가 없습니다.");
				     break;
				   }

				   const $textElement = currentMessage.find('.text');
				   const textOnly = $textElement.length ? $textElement.text().trim() : currentMessage.text().trim();

				   sendToMyself(loginId, textOnly);
				   break;
				 }

				   case 'delete': {
				     const msgId = getSelectedMessageId();
				     if (!msgId) {
				       alert("삭제할 메시지를 선택하세요.");
				       break;
				     }

				     // 서버에 삭제 요청
					 fetch(`/chat/delete/${msgId}`, { method: 'POST' })
					   .then(response => {
					     if (!response.ok) throw new Error("삭제 실패");
					     return response.text();
					   })
					   .then(() => {				     
					     alert("메시지가 삭제되었습니다.");
						 $(`[data-msgid="${msgId}"]`).remove();
					   })
				       .catch(err => {
				         console.error(err);
				         alert("삭제 중 오류가 발생했습니다.");
				       });

				     break;
				   }

				 case 'announce': {
					const msgId = getSelectedMessageId();
					   console.log("선택된 메시지 ID:", msgId);

					   if (msgId) {
					       // 서버에 공지사항 설정 요청
					       fetch(`/chat/announce/${msgId}`, {
					           method: 'POST'
					       })
					       .then(response => {
					           if (!response.ok) throw new Error("공지 설정 실패");
					           return response.text();
					       })
					       .then(() => {
					           alert("공지로 설정되었습니다.");
					           loadAnnouncementFromServer(); // 서버에서 공지 재로딩
					       })
					       .catch(error => {
					           console.error(error);
					           alert("공지 설정 중 오류 발생");
					       });
					   }
					   break;
				 }
				 case 'forward': {
				     const msgId = getSelectedMessageId(); // 현재 선택된 메시지 ID 가져오기
				     if (!msgId) {
				         alert("공유할 메시지를 선택하세요.");
				         break;
				     }

				     const forwardTo = prompt('어디로 전달할까요? (채팅방명 또는 사용자명 입력)');
				     if (!forwardTo) {
				         alert("공유할 대상을 입력하세요.");
				         break;
				     }

				     // loginId는 현재 로그인한 사용자 ID로 가정
				     $.post(`/chat/forward/${loginId}`, { messageId: msgId, forwardTo })
				         .done(() => alert('메시지를 성공적으로 공유했습니다!'))
				         .fail(() => alert('메시지 공유에 실패했습니다.'));
				     break;
				 }

			     default:
			       alert("해당 기능은 아직 구현되지 않았습니다.");
			       break;
			   }
			   $('#contextMenu').hide();
			 });
		 });
		 // 나에게 보내기
		 function sendToMyself(loginId, message) {
		   if (!loginId || !message) {
		     alert("아이디 또는 메시지가 없습니다.");
		     return;
		   }

		   fetch('/chat/meRoom', {
		     method: 'POST',
		     headers: { 'Content-Type': 'application/json' },
		     body: JSON.stringify({
		       sender: loginId,
		       content: message,
		       receiver: loginId  // 본인에게 보내는 메시지라면 receiver도 본인으로 설정 가능
		     })
		   })
		   .then(res => {
		     if (!res.ok) {
		       return res.text().then(text => {
		         throw new Error(`메시지 전송 실패: ${text}`);
		       });
		     }
		     return res.text();
		   })
		   .then(data => {
		     alert('나에게 메시지 전송 성공!');
		   })
		   .catch(err => {
		     console.error(err);
		     alert(`메시지 전송 중 오류가 발생했습니다.\n${err.message}`);
		   });
		 }

		 function formatDate(dateString) {
		   const date = new Date(dateString);
		   return date.toLocaleString();
		 }


	 function closeTextModal() {
	   const modal = document.getElementById('textModal');

	   // 모달 숨기기
	   modal.style.display = 'none';

	   // 텍스트 초기화
	   document.getElementById('modalTextContent').textContent = '';

	   // 배경 스크롤 다시 가능하게
	   document.body.style.overflow = 'auto';

	   // (선택) modal-open 클래스 제거 (Bootstrap 대비)
	   document.body.classList.remove('modal-open');

	   // (선택) 백드롭 제거 (있을 경우 대비)
	   const backdrop = document.querySelector('.modal-backdrop');
	   if (backdrop) {
	     backdrop.remove();
	   }
	 }
	 
	 function sendMessageFromContextMenu(content) {
	     // 메시지 전송용 공통 함수 sendMessage()와 분리해서 구현하거나
	     // sendMessage() 내부를 수정해서 content 파라미터를 받아 전송 가능하도록 변경할 수도 있음.

	     const sender = $('#sender').val().trim();
	     const profilePic = $('#profileImage').attr('src') || '/image/profile/default-profile.png';
	     const currentRoom = currentRoomId;  // 현재 채팅방 id 변수

	     if (!sender) {
	         alert('닉네임을 입력하세요.');
	         return;
	     }
	     if (!content) {
	         alert('보낼 메시지가 없습니다.');
	         return;
	     }
	     if (!currentRoom) {
	         alert('채팅방에 연결되어 있지 않습니다.');
	         return;
	     }

	     const messageObj = {
	         sender: sender,
	         content: content,
	         profilePic: profilePic,
	         roomId: currentRoom,
	         type: 'text',
	         timestamp: new Date().toISOString()
	     };

	     if (stompClient && stompClient.connected) {
	         stompClient.send(`/app/chat/${currentRoom}`, {}, JSON.stringify(messageObj));
	         // 채팅 입력창 초기화가 필요하다면 여기서 처리
	     } else {
	         alert('서버와 연결되어 있지 않습니다.');
	     }
	 }
	 
	 $(document).ready(function () {
	     $('#closeAnnouncement').on('click', function () {
	         $('#announcementBox').hide();
	         localStorage.removeItem('announce_' + currentRoomId);
	     });
	 });
	 
	 function loadAnnouncementFromServer() {
		console.log("✅ 현재 roomId 확인:", currentRoomId);  // 여기 추가!

		if (!currentRoomId) {
		       console.warn("⚠️ currentRoomId가 정의되지 않았습니다.");
		       return;
		   }
		
	     fetch(`/chat/announcement/${currentRoomId}`)
	         .then(res => {
	             if (!res.ok) throw new Error("공지 조회 실패");
	             return res.json();
	         })
	         .then(data => {
	             if (data && data.content) {
	                 $('#announcementText').text(data.content);
	                 $('#announcementBox').show();

	                 // localStorage 백업 (선택)
	                 localStorage.setItem('announce_' + currentRoomId, data.content);
	             } else {
	                 $('#announcementBox').hide();
	                 localStorage.removeItem('announce_' + currentRoomId);
	             }
	         })
	         .catch(err => {
	             console.warn("공지사항 없음 또는 오류:", err);
	             $('#announcementBox').hide();
	         });
	 }
	 
	 // 접속자 목록 가져오기
	 function fetchUserList() {
	     fetch("/chat/connectedUsers")
	         .then(response => {
	             if (!response.ok) {
	                 return response.text().then(text => {
	                     throw new Error(text || "서버 오류");
	                 });
	             }
	             return response.json();
	         })
	         .then(users => {
	             if (!Array.isArray(users)) {
	                 throw new Error("접속자 목록이 배열이 아닙니다!");
	             }
	             const ul = document.getElementById("userListUl");
	             ul.innerHTML = "";
	             users.forEach(user => {
	                 const li = document.createElement("li");
	                 li.textContent = user;
	                 ul.appendChild(li);
	             });
	         })
	         .catch(err => {
	             console.error("접속자 목록 불러오기 실패:", err);
	         });
	 }

	 // 페이지 로드 시 접속 신호 보내기
	 function sendEnterSignal() {
	   fetch('/chat/enter', {
	     method: 'POST',
	     credentials: 'include'
	   }).then(response => {
	     if (!response.ok) {
	       console.error('접속 신호 전송 실패');
	     }
	   }).catch(console.error);
	 }

	 // 페이지 떠날 때 접속 종료 신호 보내기
	 window.addEventListener("beforeunload", function(event) {
	   navigator.sendBeacon('/chat/leave');
	 });

	 // DOM 준비되면 실행
	 document.addEventListener('DOMContentLoaded', () => {
	   sendEnterSignal();           // 최초 접속 신호
	   setInterval(sendEnterSignal, 30000); // 30초마다 접속 신호 반복해서 서버에 alive 상태 전달

	   fetchUserList();             // 접속자 목록 처음 로드
	   setInterval(fetchUserList, 1000); // 1초마다 접속자 목록 갱신
	 });

	 // 예시 (JS에서 roomId를 생성할 때)
	 function createRoomId(user1, user2) {
	   // 항상 같은 순서로 조합되게 정렬 (중복방지)
	   const sorted = [user1, user2].sort(); // [A, B]
	   return `${sorted[0]}_${sorted[1]}`;   // A_B
	 }


