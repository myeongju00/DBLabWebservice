package com.dblab.webservice.service;

import com.dblab.webservice.model.comment.CommentRepository;
import com.dblab.webservice.repository.dto.CommentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    CommentRepository commentRepository;

    @Transactional
    public Long save(CommentDto commentDto) {
        return commentRepository.save(commentDto.toEntity()).getCommentId();
    }

}
