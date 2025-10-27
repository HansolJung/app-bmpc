package it.korea.app_bmpc.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.korea.app_bmpc.store.entity.StoreFileEntity;

public interface StoreFileRepository extends JpaRepository<StoreFileEntity, Integer> {

}
