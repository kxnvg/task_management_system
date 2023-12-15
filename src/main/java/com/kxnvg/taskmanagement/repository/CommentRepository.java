package com.kxnvg.taskmanagement.repository;

import com.kxnvg.taskmanagement.entity.Comment;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Id> {
}
