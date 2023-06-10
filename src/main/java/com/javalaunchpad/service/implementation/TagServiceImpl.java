package com.javalaunchpad.service.implementation;

import com.javalaunchpad.entity.Tag;
import com.javalaunchpad.exception.RessourceNotFoundException;
import com.javalaunchpad.repository.TagRepository;
import com.javalaunchpad.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag createTag(Tag tag) {
        // Implement the logic to create a tag
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTagById(Long tagId) throws RessourceNotFoundException {
        // Implement the logic to retrieve a tag by ID
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new RessourceNotFoundException("Tag not found"));
    }

    @Override
    public List<Tag> getAllTags() {
        // Implement the logic to retrieve all tags
        return tagRepository.findAll();

    }
    @Override
    public void deleteTag(Long tagId) {
        // Implement the logic to delete a tag
        tagRepository.deleteById(tagId);
    }

    // Other methods as per your requirements
}
