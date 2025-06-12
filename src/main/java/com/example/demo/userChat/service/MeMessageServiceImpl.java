package com.example.demo.userChat.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.userChat.dto.MeMessageDTO;
import com.example.demo.userChat.repository.MeMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MeMessageServiceImpl implements MeMessageService {

    private final MeMessageRepository meMessageRepository;

    @Override
    public void saveMessage(MeMessageDTO dto) {
        meMessageRepository.save(dto);
    }

    @Override
    public void saveToMyRoom(MeMessageDTO messageDto) {
    	MeMessageDTO entity = new MeMessageDTO();
        entity.setSender(messageDto.getSender());
        entity.setContent(messageDto.getContent());
        // 저장 시점의 시간 넣기
        entity.setCreatedAt(LocalDateTime.now());
        meMessageRepository.save(entity);
    }
    
    @Override
    public List<MeMessageDTO> findMessagesBySender(String sender) {
        List<MeMessageDTO> messages = meMessageRepository.findBySenderOrderByCreatedAtAsc(sender);

        // 엔티티 → DTO 변환
        return messages.stream()
                .map(m -> new MeMessageDTO(m.getId(), m.getSender(), m.getContent(), m.getCreatedAt()))
                .toList();
    }

    @Override
    public List<MeMessageDTO> getMessages(String sender) {
        // findMessagesBySender와 같은 기능이라면 이렇게 구현 가능
        return meMessageRepository.findBySenderOrderByCreatedAtAsc(sender);
    }

}
