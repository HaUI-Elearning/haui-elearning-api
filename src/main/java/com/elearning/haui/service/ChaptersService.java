package com.elearning.haui.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ChaptersDTO addByTeacher(String username
    , Long CourseId
    ,String title 
    ,String description) {
        Course course =courseRepository.getCoursesIdByTeacher(username, CourseId);
        if(course==null){
            throw new RuntimeException("not found course");
        }
        int currentChapterCount = chaptersRepository.countChaptersByCourseAndAuthor(CourseId, username);
        Chapters chapter=new Chapters();
        chapter.setCourse(course);
        chapter.setTitle(title);
        chapter.setDescription(description);
        chapter.setCreatedAt(LocalDateTime.now());
        chapter.setPosition(currentChapterCount+1);
        chaptersRepository.save(chapter);
        ChaptersDTO dto=mapChaptersToDTO(chapter);
        return dto;

    }

    @Override
    public ChaptersDTO updateByTeacher(String username
    ,Long CouseId
    ,Long ChapterId
    ,String title 
    ,String description
    ,int position) {
        Chapters chapter=chaptersRepository.getChapterById(CouseId, ChapterId,username);
        if(chapter==null){
            throw new RuntimeException("not found chapter");
        }
        chapter.setTitle(title);
        chapter.setDescription(description);
        chapter.setPosition(position);
        chaptersRepository.save(chapter);
        ChaptersDTO dto=mapChaptersToDTO(chapter);
        return dto;
    }

    @Override
    public boolean deleteByTeacher(String username, Long CouseId, Long ChapterId) {
        Chapters chapter=chaptersRepository.getChapterById(CouseId, ChapterId,username);
        if(chapter==null){
            throw new RuntimeException("not found chapter");
        }
        chaptersRepository.delete(chapter);
        return true;
    }
    
    

}
