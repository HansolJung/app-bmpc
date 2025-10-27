package it.korea.app_bmpc.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.korea.app_bmpc.review.entity.ReviewReplyEntity;

public interface ReviewReplyRepository extends JpaRepository<ReviewReplyEntity, Integer> {

}
