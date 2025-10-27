package it.korea.app_bmpc.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.korea.app_bmpc.store.entity.StoreCategoryEntity;

public interface StoreCategoryRepository extends JpaRepository<StoreCategoryEntity, Integer> {

}
