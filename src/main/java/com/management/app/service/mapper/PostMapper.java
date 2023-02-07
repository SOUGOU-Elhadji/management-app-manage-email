package com.management.app.service.mapper;

import com.management.app.domain.Departement;
import com.management.app.domain.Post;
import com.management.app.service.dto.DepartementDTO;
import com.management.app.service.dto.PostDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Post} and its DTO {@link PostDTO}.
 */
@Mapper(componentModel = "spring")
public interface PostMapper extends EntityMapper<PostDTO, Post> {
    @Mapping(target = "departement", source = "departement", qualifiedByName = "departementName_departement")
    PostDTO toDto(Post s);

    @Named("departementName_departement")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name_departement", source = "name_departement")
    DepartementDTO toDtoDepartementName_departement(Departement departement);
}
