package com.elearning.haui.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elearning.haui.domain.dto.ChaptersDTO;
import com.elearning.haui.domain.entity.Chapters;
import com.elearning.haui.domain.entity.Course;
import com.elearning.haui.repository.ChaptersRepository;
import com.elearning.haui.repository.CourseRepository;
import com.elearning.haui.service.imp.ChaptersServiceImp;

@Service
public class ChaptersService implements ChaptersServiceImp {
    @Autowired
    ChaptersRepository chaptersRepository;
    @Autowired
    CourseRepository courseRepository;
    //Map List chapters to DTO
    public List<ChaptersDTO> mapListChaptersToDTO(List<Chapters> list){
        List<ChaptersDTO>listDTO=new ArrayList<>();
        for(Chapters c : list){
            ChaptersDTO dto=new ChaptersDTO();
            dto.setId(c.getChapterId());
            dto.setTitle(c.getTitle());
            dto.setDescription(c.getDescription());
            dto.setCreatedAt(c.getCreatedAt());
            dto.setPosition(c.getPosition());
            listDTO.add(dto);
        }
        return listDTO;
    }
    //Map chapter to DTO
    public ChaptersDTO mapChaptersToDTO(Chapters c){
        ChaptersDTO dto=new ChaptersDTO();
        dto.setId(c.getChapterId());
        dto.setTitle(c.getTitle());
        dto.setDescription(c.getDescription());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setPosition(c.getPosition());
        return dto;
    }
    //get all
    @Override
    public List<ChaptersDTO> getAllChapters(String username, Long CouseId) {
        List<Chapters> list=chaptersRepository.getAllChaptersByTeacher(CouseId,username);
         if (list == null || list.isEmpty()) {
            throw new RuntimeException("You do not have access to this course chapter.");
        }
        List<ChaptersDTO>listDTO=mapListChaptersToDTO(list);
        return listDTO;
    }
    //get by id
    @Override
    public ChaptersDTO getChapterByid(String username, Long CouseId, Long ChapterId) {
        Chapters chapters=chaptersRepository.getChapterById(CouseId, ChapterId,username);
        if(chapters==null){
            throw new RuntimeException("not found chapter");
        }
        ChaptersDTO dto=mapChaptersToDTO(chapters);
        return dto;
    }
    //Create chapter
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChaptersDTO addByTeacher(String username, Long courseId, String title, String description, int position) {
        Course course = courseRepository.getCoursesIdByTeacher(username, courseId);
        if (course == null) {
            throw new RuntimeException("Not found course");
        }

        if (position <= 0) {
            throw new IllegalArgumentException("Position must be greater than 0");
        }

        List<Chapters> existingChapters = chaptersRepository.findByCourseId(courseId);
        int currentCount = existingChapters.size();
        if (position > currentCount + 1) {
            throw new RuntimeException("Position cannot be greater than " + (currentCount + 1));
        }

        for (Chapters existing : existingChapters) {
            if (existing.getPosition() >= position) {
                existing.setPosition(existing.getPosition() + 1);
                chaptersRepository.save(existing);
            }
        }

        Chapters chapter = new Chapters();
        chapter.setCourse(course);
        chapter.setTitle(title);
        chapter.setDescription(description);
        chapter.setCreatedAt(LocalDateTime.now());
        chapter.setPosition(position);

        chaptersRepository.save(chapter);
        ChaptersDTO dto = mapChaptersToDTO(chapter);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChaptersDTO updateByTeacher(String username, Long courseId, Long chapterId, String title, String description, int position) {
        Chapters chapter = chaptersRepository.getChapterById(courseId, chapterId, username);
        if (chapter == null) {
            throw new RuntimeException("Not found chapter");
        }

        if (position <= 0) {
            throw new IllegalArgumentException("Position must be greater than 0");
        }

        List<Chapters> existingChapters = chaptersRepository.findByCourseId(courseId);
        int currentCount = existingChapters.size();
        if (position > currentCount) {
            throw new RuntimeException("Position cannot be greater than " + currentCount);
        }

        int oldPosition = chapter.getPosition();
        for (Chapters existing : existingChapters) {
            if (existing.getChapterId().equals(chapterId)) {
                continue;
            }
            if (oldPosition < position) {
                if (existing.getPosition() > oldPosition && existing.getPosition() <= position) {
                    existing.setPosition(existing.getPosition() - 1);
                    chaptersRepository.save(existing);
                }
            } else {
                if (existing.getPosition() >= position && existing.getPosition() < oldPosition) {
                    existing.setPosition(existing.getPosition() + 1);
                    chaptersRepository.save(existing);
                }
            }
        }

        chapter.setTitle(title);
        chapter.setDescription(description);
        chapter.setPosition(position);
        chaptersRepository.save(chapter);
        ChaptersDTO dto = mapChaptersToDTO(chapter);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByTeacher(String username, Long courseId, Long chapterId) {
        Chapters chapter = chaptersRepository.getChapterById(courseId, chapterId, username);
        if (chapter == null) {
            throw new RuntimeException("Not found chapter");
        }

        int deletedPosition = chapter.getPosition();
        chaptersRepository.delete(chapter);

        List<Chapters> remainingChapters = chaptersRepository.findByCourseId(courseId);
        for (Chapters remaining : remainingChapters) {
            if (remaining.getPosition() > deletedPosition) {
                remaining.setPosition(remaining.getPosition() - 1);
                chaptersRepository.save(remaining);
            }
        }

        return true;
    }
    
    

}
