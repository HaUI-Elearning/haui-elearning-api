package com.elearning.haui.service;

import com.elearning.haui.domain.dto.CourseSalesDTO;
import com.elearning.haui.repository.OrderDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesReportService {

    private final OrderDetailRepository orderDetailRepository;

    public SalesReportService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    // Phương thức lấy danh sách khóa học bán chạy
    public List<CourseSalesDTO> getTopSellingCourses() {
        return orderDetailRepository.findTopSellingCourses();
    }
}
