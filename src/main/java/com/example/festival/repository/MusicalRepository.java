package com.example.festival.repository;

import com.example.festival.domain.Musical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MusicalRepository extends JpaRepository<Musical,Long> {
    boolean existsByMt20id(String mt20id); // 해당 뮤지컬 id를 가진 뮤지컬이 존재하냐
}
