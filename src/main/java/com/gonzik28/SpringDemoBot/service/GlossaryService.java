package com.gonzik28.SpringDemoBot.service;

import com.gonzik28.SpringDemoBot.dto.ResponseGlossaryDto;
import com.gonzik28.SpringDemoBot.dto.utils.GlossaryUtils;
import com.gonzik28.SpringDemoBot.entity.GlossaryEntity;
import com.gonzik28.SpringDemoBot.repository.GlossaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class GlossaryService {
    @Autowired
    private GlossaryRepository glossaryRepository;

    public ResponseGlossaryDto findByWord (String word) {
        GlossaryEntity glossaryEntities = glossaryRepository.findByWord(word).get();
        return GlossaryUtils.levelOfStudyEntityToDto(glossaryEntities);
    }

    public Set<ResponseGlossaryDto> findByLevelAll(String level) {
        Set<GlossaryEntity> glossaryEntities = glossaryRepository.findAllByLevel(level);
        return GlossaryUtils.levelOfStudyEntityToDtos(glossaryEntities);
    }
}
