package com.kxnvg.taskmanagement.mapper;

import com.kxnvg.taskmanagement.dto.UserDto;
import com.kxnvg.taskmanagement.entity.Comment;
import com.kxnvg.taskmanagement.entity.Task;
import com.kxnvg.taskmanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "createdTasksId", source = "createdTasks", qualifiedByName = "mapTaskToId")
    @Mapping(target = "executableTasksId", source = "executableTasks", qualifiedByName = "mapTaskToId")
    @Mapping(target = "commentsId", source = "comments", qualifiedByName = "mapCommentToId")
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    @Named("mapTaskToId")
    default List<Long> mapTaskToId(List<Task> tasks) {
        return tasks.stream()
                .map(Task::getId)
                .toList();
    }

    @Named("mapCommentToId")
    default List<Long> mapCommentToId(List<Comment> comments) {
        return comments.stream()
                .map(Comment::getId)
                .toList();
    }
}
