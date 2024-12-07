package com.elearning.haui.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.elearning.haui.domain.dto.Meta;
import com.elearning.haui.domain.dto.ResultPaginationDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PaginationUtils {

    public static <T, D> ResultPaginationDTO paginate(List<T> allItems, Pageable pageable,
            java.util.function.Function<T, D> dtoConverter) {
        // Tổng số phần tử sau khi lọc
        int totalItems = allItems.size();

        // Tính toán vị trí bắt đầu và kết thúc cho trang hiện tại
        int pageSize = pageable.getPageSize(); // Số phần tử trên mỗi trang
        int currentPage = pageable.getPageNumber(); // Trang hiện tại
        int startItem = currentPage * pageSize; // Vị trí phần tử bắt đầu của trang
        int toIndex = Math.min(startItem + pageSize, totalItems); // Vị trí phần tử kết thúc của trang

        // Lấy danh sách các phần tử trong phạm vi trang hiện tại
        List<T> paginatedItems = startItem > totalItems ? new ArrayList<>() : allItems.subList(startItem, toIndex);

        // Chuyển đổi danh sách đối tượng thành DTO
        List<D> itemDTOs = paginatedItems.stream()
                .map(dtoConverter)
                .collect(Collectors.toList());

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Tạo meta thông tin phân trang
        Meta meta = new Meta();
        meta.setPage(currentPage + 1); // Trang hiện tại (hiển thị từ 1 thay vì 0)
        meta.setPageSize(pageSize); // Số phần tử trên mỗi trang
        meta.setPages(totalPages); // Tổng số trang
        meta.setTotal(totalItems); // Tổng số phần tử sau khi lọc

        // Tạo đối tượng kết quả phân trang
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        resultPaginationDTO.setMeta(meta); // Gán meta thông tin
        resultPaginationDTO.setResult(itemDTOs); // Gán danh sách kết quả

        return resultPaginationDTO;
    }
}
