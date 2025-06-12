package com.example.demo.userChat.dto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MessageTypeConverter implements AttributeConverter<ChatMessageDTO.MessageType, String> {

    @Override
    public String convertToDatabaseColumn(ChatMessageDTO.MessageType attribute) {
        if (attribute == null) return null;
        return attribute.name();  // DB에는 항상 대문자 저장
    }

    @Override
    public ChatMessageDTO.MessageType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return ChatMessageDTO.MessageType.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 알 수 없는 값은 기본값 또는 null 처리
            return null; 
        }
    }
}
