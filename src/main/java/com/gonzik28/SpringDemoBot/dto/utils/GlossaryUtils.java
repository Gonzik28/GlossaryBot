package com.gonzik28.SpringDemoBot.dto.utils;

import com.gonzik28.SpringDemoBot.dto.RequestGlossaryDto;
import com.gonzik28.SpringDemoBot.dto.ResponseGlossaryDto;
import com.gonzik28.SpringDemoBot.entity.GlossaryEntity;
import com.gonzik28.SpringDemoBot.entity.GlossaryId;

import java.util.Set;
import java.util.stream.Collectors;

public class GlossaryUtils {
    public static ResponseGlossaryDto levelOfStudyEntityToDto(GlossaryEntity glossaryEntity) {
        ResponseGlossaryDto responseGlossaryDto = new ResponseGlossaryDto();
        responseGlossaryDto.setWord(glossaryEntity.getId().getWord());
        responseGlossaryDto.setTranslate(glossaryEntity.getId().getTranslate());
        responseGlossaryDto.setLevel(glossaryEntity.getLevel());
        return responseGlossaryDto;
    }

    public static Set<ResponseGlossaryDto> levelOfStudyEntityToDtos(Set<GlossaryEntity> studyEntities) {
        return studyEntities
                .stream()
                .map(GlossaryUtils::levelOfStudyEntityToDto)
                .collect(Collectors.toSet());
    }

    public static GlossaryEntity levelOfStudyDtoToEntity(RequestGlossaryDto requestGlossaryDto) {
        GlossaryEntity glossaryEntity = new GlossaryEntity();
        GlossaryId glossaryId = new GlossaryId();
        glossaryId.setWord(requestGlossaryDto.getWord());
        glossaryId.setTranslate(requestGlossaryDto.getTranslate());
        glossaryEntity.setId(glossaryId);
        glossaryEntity.setLevel(requestGlossaryDto.getLevel());
        return glossaryEntity;
    }
}
