package com.javalaunchpad.service;

import com.javalaunchpad.entity.Tag;
import com.javalaunchpad.exception.RessourceNotFoundException;

import java.util.List;

public interface TagService {
    Tag createTag(Tag tag);
    Tag getTagById(Long tagId) throws RessourceNotFoundException;
    List<Tag> getAllTags();
    void deleteTag(Long tagId);
    // Other methods as per your requirements
}
