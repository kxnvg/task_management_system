package com.kxnvg.taskmanagement.mapper;

import com.kxnvg.taskmanagement.dto.TaskDto;
import com.kxnvg.taskmanagement.entity.Comment;
import com.kxnvg.taskmanagement.entity.Task;
import com.kxnvg.taskmanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "executorId", source = "executor.id")
    @Mapping(target = "commentsId", source = "comments", qualifiedByName = "mapCommentToId")
    TaskDto toDto(Task task);

    Task toEntity(TaskDto taskDto);

    @Named("mapCommentToId")
    default List<Long> mapCommentToId(List<Comment> comments) {
        return comments.stream()
                .map(Comment::getId)
                .toList();
    }
}
