package it.korea.app_bmpc.review.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import it.korea.app_bmpc.review.entity.ReviewReplyEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewReplyDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Response {
        private int reviewReplyId;
        private String content;
        private String delYn;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updateDate;
        
        public static Response of(ReviewReplyEntity entity) {

            return Response.builder()
                .reviewReplyId(entity.getReviewReplyId())
                .content(entity.getContent())
                .delYn(entity.getDelYn())
                .createDate(entity.getCreateDate())
                .updateDate(entity.getUpdateDate())
                .build();
        }
    }

    @Data
    public static class Request {

        private int reviewReplyId;
        @NotNull(message = "리뷰 아이디는 필수 항목입니다.")
        private Integer reviewId;
        private String userId;
        @NotBlank(message = "내용은 필수 항목입니다.")
        private String content;
    }
}
