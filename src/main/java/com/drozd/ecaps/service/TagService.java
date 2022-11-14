package com.drozd.ecaps.service;

import com.drozd.ecaps.model.tag.EcapsTag;
import com.drozd.ecaps.repository.EcapsTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final EcapsTagRepository tagRepository;

    public EcapsTag getOrCreateEcapsTag(String tagName) {
        new EcapsTag().setName(tagName.toLowerCase());
        return tagRepository.findByNameIgnoreCase(tagName)
                .orElseGet(() -> tagRepository.save(new EcapsTag().setName(tagName.toLowerCase())));
    }

    public List<EcapsTag> getOrCreateEcapsTagsList(List<String> tagNames){
        return tagNames.stream()
                .map(this::getOrCreateEcapsTag)
                .toList();
    }
    public Set<EcapsTag> getOrCreateEcapsTagsSet(List<String> tagNames){
        return tagNames.stream()
                .map(this::getOrCreateEcapsTag)
                .collect(Collectors.toSet());
    }
}