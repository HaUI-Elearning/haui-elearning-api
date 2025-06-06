package com.elearning.haui.service.imp;

import java.util.List;

import com.elearning.haui.domain.dto.ChaptersDTO;

public interface ChaptersServiceImp {
    List<ChaptersDTO> getAllChapters(String username,Long CouseId);
    ChaptersDTO getChapterByid(String username,Long CouseId,Long ChapterId);
    ChaptersDTO addByTeacher(String username,Long CouseId,String title 
    ,String description,int Position);
    ChaptersDTO updateByTeacher(String username,Long CouseId,Long ChapterId ,String title 
    ,String description
    ,int position);
    boolean deleteByTeacher(String username,Long CouseId,Long ChapterId);
}
