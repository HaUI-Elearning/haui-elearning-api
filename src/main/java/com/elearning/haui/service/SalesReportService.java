package com.elearning.haui.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.elearning.haui.domain.dto.CourseSalesDTO;
import com.elearning.haui.repository.OrderDetailRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesReportService {

    private final OrderDetailRepository orderDetailRepository;

    public SalesReportService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    // Phương thức lấy danh sách khóa học bán chạy
    public List<CourseSalesDTO> getTopSellingCourses(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return orderDetailRepository.findTopSellingCourses(pageable);
    }
}
