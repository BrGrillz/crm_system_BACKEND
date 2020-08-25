package com.aegis.crmsystem.repositories;

import com.aegis.crmsystem.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
